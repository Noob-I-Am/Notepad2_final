package edu.fjnu.birdie.notepad2.stuff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.kuo.colorpicker.ColorPicker;

import edu.fjnu.birdie.notepad2.R;

/**
 * Created by User on 2015/11/28.
 */
public class ColorPickerDialog extends DialogFragment {
    int currentColor;

    public void setCurrentColor(int color)
    {
        currentColor=color;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_color_picker, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        findView(rootView);

        return rootView;
    }

    private void findView(View view) {

        final ColorPicker colorPicker = (ColorPicker) view.findViewById(R.id.colorPicker);
        colorPicker.setCurrentColor(currentColor);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button enter = (Button) view.findViewById(R.id.enter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEnterListener != null)
                    onEnterListener.onEnter(colorPicker.getColorString());

                getDialog().dismiss();
            }
        });

    }

    /**
     * Interface
     * */

    private OnEnterListener onEnterListener;

    public interface OnEnterListener {
        void onEnter(String colorString);
    }

    public void setOnEnterListener(OnEnterListener onEnterListener) {
        this.onEnterListener = onEnterListener;
    }
}
