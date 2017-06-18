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

public class Updatepwd_Activity extends AppCompatActivity {


    Button confirm;
    EditText id,pwd1,pwd2,pwd3;
    ProgressDialog pd;
    Handler updatepwd_handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x11)
            {
                Bundle info=msg.getData();
                String result=info.getString("result");
                if(result.equals("success"))
                    Toast.makeText(Updatepwd_Activity.this, "修改成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Updatepwd_Activity.this, "修改失败", Toast.LENGTH_SHORT).show();

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd_);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        confirm=(Button)findViewById(R.id.updatepwd_confirm);
        id=(EditText)findViewById(R.id.updatepwd_id);
        pwd1=(EditText)findViewById(R.id.updatepwd_pwd1);
        pwd2=(EditText)findViewById(R.id.updatepwd_pwd2);
        pwd3=(EditText)findViewById(R.id.updatepwd_pwd3);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id=id.getText().toString();
                final String user_pwd1=pwd1.getText().toString();
                final String user_pwd2=pwd2.getText().toString();
                final String user_pwd3=pwd3.getText().toString();
                if(user_id.equals(""))
                    Toast.makeText(Updatepwd_Activity.this, "请输入ID", Toast.LENGTH_SHORT).show();
                else if(user_pwd1.equals(""))
                    Toast.makeText(Updatepwd_Activity.this, "请输入原密码", Toast.LENGTH_SHORT).show();
                else if(user_pwd2.equals(""))
                    Toast.makeText(Updatepwd_Activity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                else if(user_pwd3.equals(""))
                    Toast.makeText(Updatepwd_Activity.this, "请确认新密码", Toast.LENGTH_SHORT).show();
                else if(!user_pwd2.equals(user_pwd3))
                    Toast.makeText(Updatepwd_Activity.this, "新密码不一致", Toast.LENGTH_SHORT).show();
                else {
                    pd = new ProgressDialog(Updatepwd_Activity.this);
                    // 设置对话框的标题
                    pd.setTitle("修改");
                    // 设置对话框显示的内容
                    pd.setMessage("正在修改中，请等待...");
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
                            User_function user_updatepwd = new User_function();
                            String result = user_updatepwd.updatepwd(user_id, user_pwd1,user_pwd2);
                            Bundle info = new Bundle();
                            info.putString("result", result);
                            Message msg = new Message();
                            msg.what = 0x11;
                            msg.setData(info);
                            pd.dismiss();
                            updatepwd_handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });
    }
}
