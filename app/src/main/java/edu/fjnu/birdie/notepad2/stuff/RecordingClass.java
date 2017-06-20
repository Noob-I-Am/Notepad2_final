package edu.fjnu.birdie.notepad2.stuff;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import java.io.File;

import edu.fjnu.birdie.notepad2.R;

/**
 * Created by max on 2017/6/19.
 */

public class RecordingClass extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private String path="";
    private Button pla,sto;
    private Chronometer mChronometer = null;
    private FloatingActionButton mRecordButton = null;
    private boolean mStartRecording = true;
    private int mRecordPromptCount = 0;
    private String node="";
    private boolean iscon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        Intent i =getIntent();

        Bundle data=i.getExtras();
        node=data.getString("authentication");
        if(node.equals("")==false)
        path= Environment.getExternalStorageDirectory()+"/audio"+node+".3gpp";

        mChronometer = (Chronometer)findViewById(R.id.chronometer);
        mRecordButton = (FloatingActionButton)findViewById(R.id.btnRecord);
        mRecordButton.setColorNormal(getResources().getColor(R.color.primary));
        mRecordButton.setColorPressed(getResources().getColor(R.color.primary_dark));
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
                mRecordPromptCount = 0;
            }
        });

        pla=(Button)findViewById(R.id.playBtn);
        pla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    playRecording();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        sto=(Button)findViewById(R.id.stopBtn);
        sto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    stopPlayback();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("到了----------------");
    }
    private void onRecord(boolean start){
        if (start) {
            try{
                System.out.println("开始录音----");
                iscon=false;
                File outFile =new File(path);
                if(outFile.exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("警告");
                    builder.setMessage("本次录音会覆盖已有录音文件，是否继续？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            iscon=false;
                            mStartRecording = !mStartRecording;
                        }
                    });
                    builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new File(path).delete();
                            {
                                try {
                                    beginRecording();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mRecordButton.setImageResource(R.drawable.ic_media_stop);
                                mChronometer.setBase(SystemClock.elapsedRealtime());
                                mChronometer.start();
                                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                                    @Override
                                    public void onChronometerTick(Chronometer chronometer) {
                                        if (mRecordPromptCount == 0) {

                                        } else if (mRecordPromptCount == 1) {

                                        } else if (mRecordPromptCount == 2) {

                                            mRecordPromptCount = -1;
                                        }

                                        mRecordPromptCount++;
                                    }
                                });
                            }

                        }
                    });
                    builder.create();
                    builder.show();
                    // outFile.delete();
                }

               {

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }else{
            mRecordButton.setImageResource(R.drawable.ic_mic_white_36dp);
            try{
                System.out.println("结束录音----");
                stopRecording();
                mChronometer.stop();
                mChronometer.setBase(SystemClock.elapsedRealtime());
                Toast.makeText(getApplicationContext(), "已保存到:"+path,
                        Toast.LENGTH_SHORT).show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            stopPlayback();
            RecordingClass.this.finish();
        }
        return true;
    }

    private void stopPlayback() {
        if(mediaPlayer!=null)
            mediaPlayer.stop();
    }

    private void playRecording() throws Exception {
        File outFile =new File(path);
        if(outFile.exists()==false)
        {
            Toast.makeText(getApplicationContext(), "本记录还未创建录音:",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            ditchMediaPlayer();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
    }

    private void ditchMediaPlayer() {
        if(mediaPlayer!=null)
        {
            try{
                mediaPlayer.release();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording() {
        if( mediaRecorder!=null)
            mediaRecorder.stop();
    }

    private void beginRecording() throws Exception {
        ditchMediaRecorder();
//        File outFile =new File(path);
//        if(outFile.exists()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("警告");
//            builder.setMessage("本次录音会覆盖已有录音文件，是否继续？");
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    new File(path).delete();
//                }
//            });
//            builder.create();
//            builder.show();
//           // outFile.delete();
//        }
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(path);
        mediaRecorder.prepare();
        mediaRecorder.start();

    }

    private void ditchMediaRecorder() {
        if(mediaRecorder!=null)
        {
            mediaRecorder.release();
        }
    }
}
