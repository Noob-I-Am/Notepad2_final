package edu.fjnu.birdie.notepad2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.fjnu.birdie.notepad2.Utils.NoteCursorAdapter;
import edu.fjnu.birdie.notepad2.Utils.NotePad;
import edu.fjnu.birdie.notepad2.Utils.NotesDB;
import edu.fjnu.birdie.notepad2.function.User_function;

public class MainActivity extends AppCompatActivity implements OnScrollListener,
        OnItemClickListener , OnItemLongClickListener {

    private String[] category = new String[] { "笔记", "重要", "备忘", "私密" };

    private Context mContext;
    private ListView listview;
    private ListView importantList;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> datalist;
    private FloatingActionButton  addNote;
    private TextView tv_content;
    private NotesDB DB;
    private SQLiteDatabase dbread;
    public SearchView searchView;
    //DatabaseManager dbManager;
    public String setCategory;
    ProgressDialog pd;

    Handler restore_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x11)
            {
                Bundle info=msg.getData();
                String result=info.getString("result");
                if(result.equals("success")) {


                    Toast.makeText(MainActivity.this, "还原成功", Toast.LENGTH_SHORT).show();
                    RefreshNotesList();
                }
                else
                    Toast.makeText(MainActivity.this, "还原失败", Toast.LENGTH_SHORT).show();

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_content=(TextView)findViewById(R.id.tv_content);
        listview = (ListView)findViewById(R.id.notelist);
        importantList = (ListView)findViewById(R.id.important_list);
        datalist = new ArrayList<Map<String,Object>>();
        addNote = (FloatingActionButton) findViewById(R.id.btn_add_note);
        mContext = this;

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                noteEdit.ENTER_STATE = 0;
                Intent intent = new Intent(mContext ,noteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info","");
                bundle.putString("info_title","");
                intent.putExtras(bundle);
                startActivityForResult(intent ,1);
            }
        });

        DB = new NotesDB(this);
        //dbManager = new DatabaseManager(this);
        dbread = DB.getReadableDatabase();

        RefreshNotesList();

        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);

        importantList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                noteEdit.ENTER_STATE = 1;
                Cursor content = (Cursor) importantList.getItemAtPosition(position);
                String content1 = content.getString(content.getColumnIndex("content"));
                Cursor title = (Cursor) importantList.getItemAtPosition(position);
                String title1 = title.getString(title.getColumnIndex("title"));
                Cursor id = (Cursor) importantList.getItemAtPosition(position);
                String No = id.getString(title.getColumnIndex("_id"));

                Log.d("TEXT", No);
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("info", content1);
                bundle.putString("info_title", title1);
                noteEdit.id = Integer.parseInt(No);
                myIntent.putExtras(bundle);
                myIntent.setClass(MainActivity.this, noteEdit.class);
                startActivityForResult(myIntent, 1);

            }
        });
        importantList.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int n = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(R.array.change_delete_recovery,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                String action[]= getResources()
                                        .getStringArray(R.array.change_value_delete_recovery);
                                switch(action[which]){
                                    case "change":{
                                        Cursor content = (Cursor) importantList.getItemAtPosition(n);
                                        changeCategory(content);
                                        //Toast.makeText(DeletedActivity.this, "恢复 "+content.getString(content.getColumnIndex("title")), Toast.LENGTH_SHORT).show();
                                        RefreshNotesList();
                                        break;
                                    }
                                    case "delete":{
                                        Cursor content = (Cursor) importantList.getItemAtPosition(n);
                                        noteDelete(content);
                                        RefreshNotesList();
                                        //Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }
                        });
                builder.create();
                builder.show();

                return true;
            }
        });
        importantList.setOnScrollListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        RefreshNotesList();
    }

    //刷新页面  重新适配Listview
    public void RefreshNotesList(){
        isNoteNull();
        String sql = "select * from note where category !='"+ NotePad.CATEGORY_DELETED + "'" +
                "and category !='" + NotePad.CATEGORY_IMPORTANT + "'" ;
        String important_sql = "select * from note where category = '" + NotePad.CATEGORY_IMPORTANT + "'";
        //Cursor cursor = dbManager.executeSql(sql, null);

        Cursor cursor = dbread.rawQuery(sql, null);
        NoteCursorAdapter adapter = new NoteCursorAdapter(this,
                R.layout.listview, cursor, new String[] {"title","content","date"},
                new int[] { R.id.tv_title,R.id.tv_content,R.id.tv_date });
        listview.setAdapter(adapter);

        //重要列表
        Cursor c = dbread.rawQuery(important_sql, null);
        int number = c.getCount();
        c.close();

        if (number == 0){
            importantList.setVisibility(View.GONE);
        }else{
            importantList.setVisibility(View.VISIBLE);
            cursor = dbread.rawQuery(important_sql, null);
            adapter = new NoteCursorAdapter(this,
                    R.layout.listview, cursor, new String[] {"title","content","date"},
                    new int[] { R.id.tv_title,R.id.tv_content,R.id.tv_date });
            importantList.setAdapter(adapter);

        }
    }

    //屏幕滑动
    @Override
    public void onScroll(AbsListView arg0,int arg1,int arg2,int arg3){
//            Log.d("onScroll","屏幕正在滑动");
    }

    //屏幕滑动状态
    @Override
    public void onScrollStateChanged(AbsListView arg0,int arg1){
//        switch (arg1){
//            case SCROLL_STATE_FLING:
//                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
//            case SCROLL_STATE_IDLE:
//                Log.i("main", "视图已经停止滑动");
//            case SCROLL_STATE_TOUCH_SCROLL:
//                Log.i("main", "手指没有离开屏幕，试图正在滑动");
//        }
    }

    //点击ListView
    @Override
    public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        noteEdit.ENTER_STATE = 1;
        Log.d("Onclick_STATE",noteEdit.ENTER_STATE + "");
        Log.d("position",arg2+"");
        Cursor content = (Cursor) listview.getItemAtPosition(arg2);
        String content1 = content.getString(content.getColumnIndex("content"));
        Cursor title = (Cursor) listview.getItemAtPosition(arg2);
        String title1 = title.getString(title.getColumnIndex("title"));
        Cursor id = (Cursor) listview.getItemAtPosition(arg2);
        String No = id.getString(title.getColumnIndex("_id"));
        Log.d("Content", content1);
        Log.d("Title", title1);
        Log.d("_Id", No);
        Log.d("arg2",arg2+"");
        Log.d("arg3",arg3+"");

        Log.d("TEXT", No);
        Intent myIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("info", content1);
        bundle.putString("info_title", title1);
        bundle.putString("id",No);
        noteEdit.id = Integer.parseInt(No);
        myIntent.putExtras(bundle);
        myIntent.setClass(MainActivity.this, noteEdit.class);
        startActivityForResult(myIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==2){
            RefreshNotesList();
        }
    }

    //长按删除 或改变标签
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        final int n = arg2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.change_delete_recovery,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        String action[]= getResources()
                                .getStringArray(R.array.change_value_delete_recovery);
                        switch(action[which]){
                            case "change":{
                                Cursor content = (Cursor) listview.getItemAtPosition(n);
                                changeCategory(content);
                                //Toast.makeText(DeletedActivity.this, "恢复 "+content.getString(content.getColumnIndex("title")), Toast.LENGTH_SHORT).show();
                                RefreshNotesList();
                                break;
                            }
                            case "delete":{
                                Cursor content = (Cursor) listview.getItemAtPosition(n);
                                noteDelete(content);
                                RefreshNotesList();
                                //Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                });
        builder.create();
        builder.show();

        return true;
    }

    //菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //搜索栏
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        String SearchContent = getIntent().getStringExtra(SearchManager.QUERY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Searching",query);
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("word",query);
                Log.d("Searching","intent");
                startActivity(intent);
                Log.d("Searching","intentstart");
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //菜单栏响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_search: {
                Log.d("Search","start");
                break;
            }
            case R.id.action_settings:{
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_deleted:{
                Intent intent = new Intent(MainActivity.this,DeletedActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_aboutus:{
                Intent intent = new Intent(MainActivity.this,AboutUsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_backup :{
                final Intent sIntent = new Intent(this, Backup_Service.class);
                sIntent.setAction("edu.fjnu.birdie.notepad2.Backup_Service");
                //sIntent.setPackage(this.getPackageName());
                //startService(sIntent);
                Intent eIntent = new Intent(getExplicitIntent(mContext,sIntent));
                startService(eIntent);
                break;

               // bindService(intent,coon,Service.BIND_AUTO_CREATE)
            }

            case R.id.action_recovery    :{
                SharedPreferences read=getSharedPreferences("login",MODE_PRIVATE);
                if(read.contains("user_id")) {
                    final String uid = read.getString("user_id", "");
                    pd = new ProgressDialog(MainActivity.this);
                    // 设置对话框的标题
                    pd.setTitle("还原");
                    // 设置对话框显示的内容
                    pd.setMessage("正在还原中，请等待...");
                    // 设置对话框能用“取消”按钮关闭
                    pd.setCancelable(false);
                    // 设置对话框的进度条风格
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // 设置对话框的进度条是否显示进度
                    pd.setIndeterminate(false);
                    pd.show(); // ②
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            User_function user_restore = new User_function();
                            String result = user_restore.restore(dbread, uid);
                            user_restore.restore_ima(uid);
                            Bundle info = new Bundle();
                            info.putString("result", result);
                            Message msg = new Message();
                            msg.what = 0x11;
                            msg.setData(info);
                            pd.dismiss();
                            restore_handler.sendMessage(msg);
                        }
                    }.start();
                }
                else
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    //将隐式启动转换为显示启动
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    //如果列表项为空,则显示背景和文字
    public boolean isNoteNull(){
        String sql = "select * from note where category !='"+ NotePad.CATEGORY_DELETED+"'";
        Log.d("sql",sql);
        // Cursor c = dbManager.executeSql(sql, null);
        Cursor c = dbread.rawQuery(sql, null);
        int number = c.getCount();
        Log.d("Note number",number+"");
        if(number == 0){
            ListView listView = (ListView)findViewById(R.id.notelist);
            TextView textView = (TextView)findViewById(R.id.main_text);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            return true;
        }else{
            ListView listView = (ListView)findViewById(R.id.notelist);
            TextView textView = (TextView)findViewById(R.id.main_text);
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            return false;
        }
    }



    //设置分组
    public void changeCategory(Cursor content){
        //Toast.makeText(this,"add_catagory",Toast.LENGTH_SHORT).show();
        //{ "默认", "重要", "备忘", "笔记", "日程" }
        final String id = content.getString(content.getColumnIndex("_id"));

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("设置分组");
        builder.setSingleChoiceItems(category, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int choose = which;
                switch (which) {
                    case 0: {
                        setCategory = "update note set category ='" + NotePad.CATEGORY_NORMAL + "' where _id=" + id;
                        Log.d("EXE", setCategory);
                        break;
                    }
                    case 1: {
                        setCategory = "update note set category ='" + NotePad.CATEGORY_IMPORTANT + "' where _id=" + id;
                        Log.d("EXE", setCategory);
                        break;
                    }
                    case 2: {
                        setCategory = "update note set category ='" + NotePad.CATEGORY_MEMO + "' where _id=" + id;
                        Log.d("EXE", setCategory);
                        break;
                    }
                    case 3: {
                        setCategory = "update note set category ='" + NotePad.CATEGORY_PWD + "' where _id=" + id;
                        Log.d("EXE", setCategory);
                        break;
                    }
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbread.execSQL(setCategory);
                RefreshNotesList();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    public void noteDelete(Cursor content){
        final Cursor c1 = content;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //长按直接删除
        builder.setTitle("删除");
        builder.setMessage("确认删除?");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cursor content =  (Cursor) listview.getItemAtPosition(n);
                Cursor content = c1;
                String id = c1.getString(content.getColumnIndex("_id"));
                String setCategory = "update note set category ='" + NotePad.CATEGORY_DELETED + "' where _id=" + id;
                Log.d("DELETE",setCategory);
                dbread.execSQL(setCategory);
                RefreshNotesList();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }



}
