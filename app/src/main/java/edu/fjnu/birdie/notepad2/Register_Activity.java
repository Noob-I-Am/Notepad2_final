package edu.fjnu.birdie.notepad2;

import android.app.ProgressDialog;
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

public class Register_Activity extends AppCompatActivity {


    Button register;
    EditText id,pwd1,pwd2,name,email;
    ProgressDialog pd;
    Handler register_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x11)
            {
                Bundle info=msg.getData();
                String result=info.getString("result");
                if(result.equals("success"))
                    Toast.makeText(Register_Activity.this, "注册成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register_Activity.this, "注册失败", Toast.LENGTH_SHORT).show();

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());


        register=(Button)findViewById(R.id.rigister_confirm);
        id=(EditText)findViewById(R.id.register_id);
        pwd1=(EditText)findViewById(R.id.register_pwd1);
        pwd2=(EditText)findViewById(R.id.register_pwd2);
        name=(EditText)findViewById(R.id.register_name);
        email=(EditText)findViewById(R.id.register_email);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id=id.getText().toString();
                final String user_pwd1=pwd1.getText().toString();
                final String user_pwd2=pwd2.getText().toString();
                final String user_name=name.getText().toString();
                final String user_email=email.getText().toString();
                if(user_id.equals(""))
                    Toast.makeText(Register_Activity.this, "请输入ID", Toast.LENGTH_SHORT).show();
                else if(user_pwd1.equals(""))
                    Toast.makeText(Register_Activity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                else if(user_pwd2.equals(""))
                    Toast.makeText(Register_Activity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                else if(user_name.equals(""))
                    Toast.makeText(Register_Activity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                else if(user_email.equals(""))
                    Toast.makeText(Register_Activity.this, "请输入Email", Toast.LENGTH_SHORT).show();
                else if(!user_pwd1.equals(user_pwd2))
                    Toast.makeText(Register_Activity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                else {
                    pd = new ProgressDialog(Register_Activity.this);
                    // 设置对话框的标题
                    pd.setTitle("注册");
                    // 设置对话框显示的内容
                    pd.setMessage("正在注册中，请等待...");
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
                            User_function user_register = new User_function();
                            String result = user_register.register(user_id, user_pwd1, user_name,user_email);
                            Bundle info = new Bundle();
                            info.putString("result", result);
                            Message msg = new Message();
                            msg.what = 0x11;
                            msg.setData(info);
                            pd.dismiss();
                            register_handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });
    }
}
