package edu.fjnu.birdie.notepad2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.fjnu.birdie.notepad2.function.User_function;

public class Forgetpwd_Activity extends AppCompatActivity {

    Button confirm,getcode;
    EditText email,code;

    Handler forgetpwd_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x11)
            {
                Bundle info=msg.getData();
                String result=info.getString("result");
                if(result.equals("success")) {
                    //Toast.makeText(Forgetpwd_Activity.this, "重置成功，密码为888888", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent( Forgetpwd_Activity.this ,Updatepwd_Activity.class);
                    intent.putExtra("state","byMail");
                    startActivity(intent);
                }
                else
                    Toast.makeText(Forgetpwd_Activity.this, "重置失败", Toast.LENGTH_SHORT).show();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        confirm=(Button)findViewById(R.id.forget_comfir);
        getcode=(Button)findViewById(R.id.getcode);
        email=(EditText)findViewById(R.id.forget_email);
        code=(EditText)findViewById(R.id.forget_code);
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_email=email.getText().toString();
                if(user_email.equals(""))
                    Toast.makeText(Forgetpwd_Activity.this, "请输入email", Toast.LENGTH_SHORT).show();
                else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            User_function user = new User_function();
                            String result = user.getcode(user_email);
                        }
                    }.start();
                    getcode.setText("已发送");
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_email=email.getText().toString();
                final String user_code=code.getText().toString();
                if(user_email.equals(""))
                    Toast.makeText(Forgetpwd_Activity.this, "请输入email", Toast.LENGTH_SHORT).show();
                else if(user_code.equals(""))
                    Toast.makeText(Forgetpwd_Activity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            User_function user = new User_function();
                            String result = user.forgetpwd(user_email, user_code);
                            Bundle info = new Bundle();
                            info.putString("result", result);
                            Message msg = new Message();
                            msg.what = 0x11;
                            msg.setData(info);
                            forgetpwd_handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });
    }
}
