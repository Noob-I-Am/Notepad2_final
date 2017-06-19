package edu.fjnu.birdie.notepad2.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import edu.fjnu.birdie.notepad2.Utils.NotesDB;
import edu.fjnu.birdie.notepad2.function.User_function;

/**
 * Created by TOSHIBA on 2017/6/19.
 */
public class Backup_Service extends IntentService{

    private SQLiteDatabase dbread;
    private NotesDB DB;
    Handler backup_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x11) {
                Bundle info=msg.getData();
                String result=info.getString("result");
                if(result.equals("success"))
                    Toast.makeText(getApplicationContext(), "备份成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "备份失败", Toast.LENGTH_SHORT).show();
            }
            if(msg.what==0x11) {
                    Toast.makeText(getApplicationContext(), "请登录", Toast.LENGTH_SHORT).show();

            }
        }
    };

    public Backup_Service() {
        super("Backup_Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DB = new NotesDB(this);
        dbread=DB.getReadableDatabase();
        User_function user_back = new User_function();
        SharedPreferences read=getSharedPreferences("login",MODE_PRIVATE);
        if(read.contains("user_id")) {
            String uid = read.getString("user_id", "");
            String result = user_back.backup(dbread, uid);
            Bundle info = new Bundle();
            info.putString("result", result);
            Message msg = new Message();
            msg.what = 0x11;
            msg.setData(info);
            backup_handler.sendMessage(msg);
        }
        else
        {
            Message msg = new Message();
            msg.what = 0x12;
            backup_handler.sendMessage(msg);
        }

    }
}
