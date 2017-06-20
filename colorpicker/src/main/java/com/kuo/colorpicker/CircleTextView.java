package com.kuo.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Kuo on 2015/11/23.
 */
public class CircleTextView extends RelativeLayout {

    private TextView textView;
    private int color;

    private static final String defaultText = "No String.";

    public CircleTextView(Context context) {
        super(context);

        onCreateView();
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        onCreateView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);
    }

    private void onCreateView() {

        setBackgroundColor(Color.TRANSPARENT);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText(defaultText);
        textView.setTextColor(Color.parseColor("#FFFFFF"));

        addView(textView);
    }

    public void setCircleColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextSize(int size) {
        textView.setTextSize(size);
    }
}
