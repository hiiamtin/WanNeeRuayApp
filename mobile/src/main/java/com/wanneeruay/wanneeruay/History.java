package com.wanneeruay.wanneeruay;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class History extends AppCompatActivity implements View.OnClickListener{

    EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        number = findViewById(R.id.text_input_number);
        final Button btConferm = findViewById(R.id.bt_conferm);
        final ConstraintLayout ct = findViewById(R.id.constraintLayout);
        btConferm.setOnClickListener(this);
        ct.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_conferm:
                if(number.getText().toString().length()<6){
                    number.setError("โปรดใส่ตัวเลขให้ครบ 6 ตัว");
                }else{
                    number.setError(null);
                    for (char n : number.getText().toString().toCharArray()) {
                        if(n=='.'){
                            number.setError("โปรดใส่เฉพาะตัวเลขเท่านั้น");
                            break;
                        }
                    }
                }
                hideSoftKeyboard(v);
                break;
            case R.id.constraintLayout:
                hideSoftKeyboard(v);
                break;
        }
    }

    public void showKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
