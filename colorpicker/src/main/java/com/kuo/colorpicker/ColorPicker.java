package com.kuo.colorpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/*
 * Created by User on 2015/11/24.
 */
public class ColorPicker extends RelativeLayout implements SeekBar.OnTouchListener, SeekBar.OnSeekBarChangeListener{

    private EditText color_edit;
    private ImageView colorView;
    private TextView r_edit, g_edit, b_edit;
    private SeekBar r_seek, g_seek, b_seek;
    private CircleTextView r_text, g_text, b_text;

    private String r_Hex = "00";
    private String g_Hex = "00";
    private String b_Hex = "00";
    public  String colorString = "#000000";

    private ViewPropertyAnimatorCompat r_text_anima;
    private ViewPropertyAnimatorCompat g_text_anima;
    private ViewPropertyAnimatorCompat b_text_anima;
    public int currentColor;//目前颜色

    public void setCurrentColor(int color)
    {
        currentColor=color;
//        int red = (currentColor & 0xff0000) >> 16;
//        int green = (currentColor & 0x00ff00) >> 8;
//        int blue = (currentColor & 0x0000ff);

    }

    public ColorPicker(Context context) {
        super(context);

        onCreateView();
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        onCreateView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onCreateView() {

        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.color_picker_view, this);

        initSeekBar();
        initTextView();
        initEditText();
        initColorView();
        initSearchButton();
    }

    private void initSeekBar() {

        r_seek = (SeekBar) findViewById(R.id.r_seek);
        g_seek = (SeekBar) findViewById(R.id.g_seek);
        b_seek = (SeekBar) findViewById(R.id.b_seek);

        r_seek.setMax(255);
        g_seek.setMax(255);
        b_seek.setMax(255);

        r_seek.setOnTouchListener(this);
        g_seek.setOnTouchListener(this);
        b_seek.setOnTouchListener(this);

        r_seek.setOnSeekBarChangeListener(this);
        g_seek.setOnSeekBarChangeListener(this);
        b_seek.setOnSeekBarChangeListener(this);
    }

    private void initTextView() {

        r_text = (CircleTextView) findViewById(R.id.r_text);
        g_text = (CircleTextView) findViewById(R.id.g_text);
        b_text = (CircleTextView) findViewById(R.id.b_text);

        r_text.setCircleColor(Color.parseColor("#F44336"));
        g_text.setCircleColor(Color.parseColor("#4CAF50"));
        b_text.setCircleColor(Color.parseColor("#2196F3"));

        r_text.setScaleX(0);
        r_text.setScaleY(0);

        g_text.setScaleX(0);
        g_text.setScaleY(0);

        b_text.setScaleX(0);
        b_text.setScaleY(0);

        r_text_anima = ViewCompat.animate(r_text);
        g_text_anima = ViewCompat.animate(g_text);
        b_text_anima = ViewCompat.animate(b_text);

        r_edit = (TextView) findViewById(R.id.r_edit);
        g_edit = (TextView) findViewById(R.id.g_edit);
        b_edit = (TextView) findViewById(R.id.b_edit);

        r_edit.setText("0");
        g_edit.setText("0");
        b_edit.setText("0");
    }

    private void initEditText() {

        color_edit = (EditText) findViewById(R.id.color_edit);;
        color_edit.setText(colorString);

    }

    private void initColorView() {
        colorView = (ImageView) findViewById(R.id.colorView);
    }

    private void initSearchButton() {

        ImageButton search_button = (ImageButton) findViewById(R.id.search_button);
        search_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String color = color_edit.getText().toString();

                int r_ten = Integer.parseInt(color.substring(1, 3), 16);
                int g_ten = Integer.parseInt(color.substring(3, 5), 16);
                int b_ten = Integer.parseInt(color.substring(5, 7), 16);

                r_edit.setText(String.valueOf(r_ten));
                g_edit.setText(String.valueOf(g_ten));
                b_edit.setText(String.valueOf(b_ten));

                r_seek.setProgress(r_ten);
                g_seek.setProgress(g_ten);
                b_seek.setProgress(b_ten);

            }
        });
    }

    private void moveText(int id, float x) {
        if (id == R.id.r_seek) {
            r_text_anima.x(x).setDuration(0).start();

        } else if (id == R.id.g_seek) {
            g_text_anima.x(x).setDuration(0).start();

        } else if (id == R.id.b_seek) {
            b_text_anima.x(x).setDuration(0).start();

        }
    }

    private void updateEditText(int id) {
        if (id == R.id.r_seek) {
            int r_progress = r_seek.getProgress();
            String r_progress_str = String.valueOf(r_progress);
            r_Hex = Integer.toHexString(r_seek.getProgress());
            r_text.setText(r_progress_str);
            r_edit.setText(r_progress_str);

        } else if (id == R.id.g_seek) {
            int g_progress = g_seek.getProgress();
            String g_progress_str = String.valueOf(g_progress);
            g_Hex = Integer.toHexString(g_seek.getProgress());
            g_text.setText(g_progress_str);
            g_edit.setText(g_progress_str);

        } else if (id == R.id.b_seek) {
            int b_progress = b_seek.getProgress();
            String b_progress_str = String.valueOf(b_progress);
            b_Hex = Integer.toHexString(b_seek.getProgress());
            b_text.setText(b_progress_str);
            b_edit.setText(b_progress_str);

        }
        colorString = "#" + r_Hex + g_Hex + b_Hex;
        color_edit.setText(colorString);
        colorView.setBackgroundColor(Color.rgb(r_seek.getProgress(), g_seek.getProgress(), b_seek.getProgress()));

        if(onColorChangeListener != null)
            onColorChangeListener.onColorChangeed(r_seek.getProgress(), g_seek.getProgress(), b_seek.getProgress(), colorString);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float oldX = 0f;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                oldX = getX() - (b_text.getWidth()/2);

                int i = v.getId();

                if (i == R.id.r_seek)
                    r_text_anima.x(oldX).scaleX(1).scaleY(1).setDuration(200).start();
                else if (i == R.id.g_seek)
                    g_text_anima.x(oldX).scaleX(1).scaleY(1).setDuration(200).start();
                else if (i == R.id.b_seek)
                    b_text_anima.x(oldX).scaleX(1).scaleY(1).setDuration(200).start();

                break;

            case MotionEvent.ACTION_MOVE:

                float curX = event.getX();
                float rawX = curX - oldX - (b_text.getWidth()/2);

                if(curX - (b_text.getWidth()/2) >= v.getMinimumWidth() && curX + (b_text.getWidth()/2) <= v.getWidth())
                    moveText(v.getId(), rawX);

                break;

            case MotionEvent.ACTION_CANCEL:

                curX = event.getX();

                if(curX - (b_text.getWidth()/2) >= v.getMinimumWidth() && curX + (b_text.getWidth()/2) <= v.getWidth())
                    moveText(v.getId(), curX);

                break;

            case MotionEvent.ACTION_UP:

                curX = oldX - (b_text.getWidth()/2);

                if(curX - b_text.getWidth() > v.getMinimumWidth() && curX + b_text.getWidth() < v.getWidth())
                    moveText(v.getId(), curX);

                int i1 = v.getId();

                if (i1 == R.id.r_seek)
                    r_text_anima.scaleX(0).scaleY(0).setDuration(200).start();
                else if (i1 == R.id.g_seek)
                    g_text_anima.scaleX(0).scaleY(0).setDuration(200).start();
                else if (i1 == R.id.b_seek)
                    b_text_anima.scaleX(0).scaleY(0).setDuration(200).start();

                break;
        }

        return false;
    }
//拖动进度条进度改变的时候调用
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateEditText(seekBar.getId());
    }
//拖动进度条开始拖动的时候调用
    @Override
    public void onStartTrackingTouch(SeekBar seekBar){
        updateEditText(seekBar.getId());
    }
//拖动进度条停止拖动的时候调用
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        updateEditText(seekBar.getId());
    }

    /**
     * Public Method
     * */

    public String getColorString() {
        return colorString;
    }

    public int getR() {
        return  r_seek.getProgress();
    }

    public int getG() {
        return  g_seek.getProgress();
    }

    public int getB() {
        return  b_seek.getProgress();
    }

    /**
     * Interface Method
     * */

    private OnColorChangeListener onColorChangeListener;

    public interface OnColorChangeListener {
        void onColorChangeed(int r, int g, int b, String colorString);
    }

    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
        this.onColorChangeListener = onColorChangeListener;
    }
}
