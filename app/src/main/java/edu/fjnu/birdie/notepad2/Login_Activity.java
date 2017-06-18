package edu.fjnu.birdie.notepad2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import edu.fjnu.birdie.notepad2.function.User_function;

public class Login_Activity extends AppCompatActivity {

    Button login, register,forgetpwd,updatepwd;
    EditText id,pwd;
    CheckBox remember_user;
    ProgressDialog pd;
    Handler login_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x11)
            {
                Bundle info=msg.getData();
                String result=info.getString("result");
                if(result.equals("success")) {
                    Toast.makeText(Login_Activity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    if(remember_user.isChecked())
                    {
                        SharedPreferences read=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor edit=read.edit();
                        edit.putString("user_id",info.getString("user_id"));
                        edit.putString("user_pwd",info.getString("user_pwd"));
                        edit.commit();
                    }
                    else
                    {
                        SharedPreferences read=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor edit=read.edit();
                        edit.clear();
                    }
                }
                else
                    Toast.makeText(Login_Activity.this, "登录失败", Toast.LENGTH_SHORT).show();

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        forgetpwd=(Button)findViewById(R.id.forgetpwd);
        updatepwd=(Button)findViewById(R.id.updatepwd);
        id=(EditText)findViewById(R.id.login_id);
        pwd=(EditText)findViewById(R.id.login_pwd);
        remember_user=(CheckBox)findViewById(R.id.checkBox);
        remember_user.setChecked(true);
        SharedPreferences read=getSharedPreferences("user",MODE_PRIVATE);
        if(read.contains("user_id")) {
            String uid=read.getString("user_id","");
            String upwd=read.getString("user_pwd","");
            id.setText(uid);
            pwd.setText(upwd);
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Login_Activity.this,Register_Activity.class);
//                startActivity(intent);
            }
        });
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Login_Activity.this,Forgetpwd_Activity.class);
//                startActivity(intent);
            }
        });
        updatepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Login_Activity.this,Updatepwd_Activity.class);
//                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id=id.getText().toString();
                final String user_pwd=pwd.getText().toString();
                if(user_id.equals(""))
                    Toast.makeText(Login_Activity.this, "请输入ID", Toast.LENGTH_SHORT).show();
                else if(user_pwd.equals(""))
                    Toast.makeText(Login_Activity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                else
                {
                    pd = new ProgressDialog(Login_Activity.this);
                    // 设置对话框的标题
                    pd.setTitle("登录");
                    // 设置对话框显示的内容
                    pd.setMessage("正在登录中，请等待...");
                    // 设置对话框能用“取消”按钮关闭
                    pd.setCancelable(false);
                    // 设置对话框的进度条风格
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // 设置对话框的进度条是否显示进度
                    pd.setIndeterminate(false);
                    pd.show(); // ②
                    new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            User_function user=new User_function();
                            String result=user.login(user_id,user_pwd);
                            Bundle info=new Bundle();
                            info.putString("result",result);
                            info.putString("user_id",user_id);
                            info.putString("user_pwd",user_pwd);
                            Message msg=new Message();
                            msg.what=0x11;
                            msg.setData(info);
                            pd.dismiss();
                            login_handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });

    }
}
