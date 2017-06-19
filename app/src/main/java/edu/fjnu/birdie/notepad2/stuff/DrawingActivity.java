package edu.fjnu.birdie.notepad2.stuff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.fjnu.birdie.notepad2.R;

public class DrawingActivity extends AppCompatActivity {

    private PaintView paintView;
    public String choosenColor="";
    public  LinearLayout llBottomSheet;
    public BottomSheetBehavior bottomSheetBehavior;
    public  RadioGroup mRadioGroup;
    public RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_main);
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
        mRadioGroup =(RadioGroup) findViewById(R.id.group);
        mRadioGroup.check(R.id.Rnormal);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(R.id.Rnormal == checkedId){
                    paintView.normal();
                }
                else if(R.id.Remboss == checkedId){
                    paintView.emboss();
                }
                else if(R.id.Rblur == checkedId){
                    paintView.blur();
                }
            }
        });


        findButton();
    }

    private void findButton() {
        Button openColorPicker = (Button) findViewById(R.id.chooseColor);
        openColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
                colorPickerDialog.setCurrentColor( paintView.getColor());
                colorPickerDialog.setOnEnterListener(new ColorPickerDialog.OnEnterListener() {
                    @Override
                    public void onEnter(String colorString) {
                        choosenColor=colorString;
                        paintView.receiveColor(choosenColor);
                    }
                });
                colorPickerDialog.show(getSupportFragmentManager(), "dialog");
            }
        });
        Button undoS=(Button)findViewById(R.id.undo);
        undoS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.undo();
            }
        });
    }
    public void over()
    {
        Intent intent = getIntent();
        Uri myuri=paintView.getUri();
        intent.putExtra("uri", myuri);
        intent.putExtra("path",paintView.getPath());
        setResult(RESULT_OK,intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.drawing_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.clear:
                paintView.clear();
                return true;
            case R.id.save:
                paintView.storage();
                over();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}