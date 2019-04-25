package com.wanneeruay.wanneeruay;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanneeruay.wanneeruay.Firebase.FirebaseHelper;
import com.wanneeruay.wanneeruay.Firebase.Spacecraft;

public class History extends AppCompatActivity implements View.OnClickListener{

    EditText number;
    static String readQr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        number = findViewById(R.id.text_input_number);
        final Button btConferm = findViewById(R.id.bt_conferm);
        final ConstraintLayout ct = findViewById(R.id.constraintLayout);
        final Button btMoney = findViewById(R.id.manage_money_bt);
        number.setBackgroundTintMode(PorterDuff.Mode.ADD);
        number.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNOTOK,getTheme())));
        btConferm.setOnClickListener(this);
        btMoney.setOnClickListener(this);
        ct.setOnClickListener(this);
        number.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                checkErrorTextInput(v);
                hideSoftKeyboard(v);
            }else{
                showKeyboard(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_conferm:
                checkErrorTextInput(v);
                if(number.getError()==null) {
                    hideSoftKeyboard(v);
                    Toast.makeText(this,"OK",Toast.LENGTH_LONG ).show();
                }else{
                    number.requestFocus();
                }
                break;
            case R.id.constraintLayout:
                checkErrorTextInput(v);
                hideSoftKeyboard(v);
                break;
            case R.id.manage_money_bt:
                startActivity(new Intent(this, Qrcode.class));
                String m = Qrcode.Qrtext;
                Toast toast = Toast.makeText(getApplicationContext(),"Please Shake ME!!! <3" ,Toast.LENGTH_LONG);
                toast.setGravity(0,0,0);
                toast.show();
                break;
        }
    }
    public void checkErrorTextInput(View v){
        if(number.getText().toString().length()<6){
            number.setError("โปรดใส่ตัวเลขให้ครบ 6 ตัว");
            number.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNOTOK,getTheme())));
        }else{
            number.setError(null);
            number.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOK,getTheme())));
            for (char n : number.getText().toString().toCharArray()) {
                if(n=='.'){
                    number.setError("โปรดใส่เฉพาะตัวเลขเท่านั้น");
                    number.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNOTOK,getTheme())));
                    break;
                }
            }
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
    public  void toastprint(){
        Toast toast = Toast.makeText(getApplicationContext(),readQr ,Toast.LENGTH_LONG);
        toast.setGravity(0,0,0);
        toast.show();
    }
}
