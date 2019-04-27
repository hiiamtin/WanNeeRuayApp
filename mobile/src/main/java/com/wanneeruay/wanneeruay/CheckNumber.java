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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.wanneeruay.wanneeruay.Firebase.Spacecraft;
import java.util.ArrayList;
import java.util.Collections;

public class CheckNumber extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText number;
    ArrayList<String> date=Menu.date;
    ArrayList<Spacecraft> lottary_data=Menu.lottary_data;
    static String readQr;
    ArrayList<TextView> settext = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


        number = findViewById(R.id.text_input_numberC);
        final Button btConferm = findViewById(R.id.bt_confermC);
        final ConstraintLayout ct = findViewById(R.id.constraintLayoutC);
        final Spinner dateSp = findViewById(R.id.spinner_date);
        final Button butQr2 = findViewById(R.id.QrbutC);
        settext.add(findViewById(R.id.awardnum1));
        settext.add(findViewById(R.id.nearnum1));
        settext.add(findViewById(R.id.awardnum2));
        settext.add(findViewById(R.id.awardnum3));
        settext.add(findViewById(R.id.awardnum4));
        settext.add(findViewById(R.id.awardnum5));
        settext.add(findViewById(R.id.frontnum3));
        settext.add(findViewById(R.id.lastnum3));
        settext.add(findViewById(R.id.lastnum2));

        dateSp.setAdapter(updateSpiner());
        dateSp.setOnItemSelectedListener(this);

        number.setBackgroundTintMode(PorterDuff.Mode.ADD);
        number.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNOTOK,getTheme())));

        btConferm.setOnClickListener(this);
        ct.setOnClickListener(this);
        butQr2.setOnClickListener(this);
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
            case R.id.bt_confermC:
                checkErrorTextInput(v);
                if(number.getError()==null) {
                    hideSoftKeyboard(v);
                    checkNumberReward();
                }else{
                    number.requestFocus();
                }
                break;
            case R.id.constraintLayoutC:
                checkErrorTextInput(v);
                hideSoftKeyboard(v);
                break;
            case R.id.QrbutC:
                startActivity(new Intent(this, Qrcode.class));
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();
        Spacecraft group = lottary_data.get(parent.getSelectedItemPosition());
        ArrayList<ArrayList<String>> type = group.getValue();
        for (int i = 0; i < 9; i++) {
            Collections.sort(type.get(i));
            settext.get(i).setText(type.get(i).toString()
                    .replace("[", "").replace("]", "").replace(",",""));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void checkNumberReward(){
        String s = number.getText().toString();
        boolean[] reward = new boolean[9];
        if(s.equals(settext.get(0).getText().toString())){
            reward[0]=true;
            //Toast.makeText(getApplicationContext(),"ถูกรางวัลที่ 1"+reward[0].toString(),Toast.LENGTH_LONG).show();
        }
        for (int i = 1; i < 6; i++) {
            String[] num = settext.get(i).getText().toString().split(" ");
            for (int j = 0; j < num.length; j++) {
                if(s.equals(num[j])){
                    reward[i]=true;
                    //Toast.makeText(getApplicationContext(),"ถูกรางวัล"+" i="+i,Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
        String[] num = settext.get(6).getText().toString().split(" ");
        for (int j = 0; j < num.length; j++) {
            if(s.substring(0,3).equals(num[j])){
                reward[6]=true;
                //Toast.makeText(getApplicationContext(),"ถูกรางวัล"+" i="+6,Toast.LENGTH_LONG).show();
                break;
            }
        }
        num = settext.get(7).getText().toString().split(" ");
        for (int j = 0; j < num.length; j++) {
            if(s.substring(3).equals(num[j])){
                reward[7]=true;
                //Toast.makeText(getApplicationContext(),"ถูกรางวัล"+" i="+7,Toast.LENGTH_LONG).show();
                break;
            }
        }
        num = settext.get(8).getText().toString().split(" ");
        for (int j = 0; j < num.length; j++) {
            if(s.substring(4).equals(num[j])){
                reward[8]=true;
                //Toast.makeText(getApplicationContext(),"ถูกรางวัล"+" i="+8,Toast.LENGTH_LONG).show();
                break;
            }
        }
        for (int i = 0; i < reward.length; i++) {
            if (reward[i]){
                switch (i){
                    case 0:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลที่1",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลข้างเคียงที่1",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลที่2",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลที่3",Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลที่4",Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลที่5",Toast.LENGTH_LONG).show();
                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัล",Toast.LENGTH_LONG).show();
                        break;
                    case 7:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลที่2",Toast.LENGTH_LONG).show();
                        break;
                    case 8:
                        Toast.makeText(getApplicationContext(),"ถูกรางวัลที่2",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }

    private ArrayAdapter<String> updateSpiner(){
        ArrayAdapter<String> data = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,date);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return data;
    }
}
