package com.wanneeruay.wanneeruay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wanneeruay.wanneeruay.Firebase.Spacecraft;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class CheckNumber extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText number;
    ArrayList<String> date=Menu.date;
    ArrayList<Spacecraft> lottary_data=Menu.lottary_data;
    static String readQr;
    ArrayList<TextView> settext = new ArrayList<>();
    Spinner dateSp;
    int rewardPrice = 0;
    int currentDate = date.size() +1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


        number = findViewById(R.id.text_input_numberC);
        final Button btConferm = findViewById(R.id.bt_confermC);
        final ConstraintLayout ct = findViewById(R.id.constraintLayoutC);
        final Button butQr2 = findViewById(R.id.QrbutC);
        dateSp = findViewById(R.id.spinner_date);
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
                    checkNumberReward(number.getText().toString());

                }else{
                    number.requestFocus();
                }
                break;
            case R.id.constraintLayoutC:
                checkErrorTextInput(v);
                hideSoftKeyboard(v);
                break;
            case R.id.QrbutC:
                Intent intent = new Intent(CheckNumber.this,Qrcode.class);
                startActivityForResult(intent,1);
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
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
        Spacecraft group = lottary_data.get(parent.getSelectedItemPosition());
        ArrayList<ArrayList<String>> type = group.getValue();
        for (int i = 0; i < 9; i++) {
            Collections.sort(type.get(i));
            settext.get(i).setText(type.get(i).toString()
                    .replace("[", "").replace("]", "").replace(",",""));
        }
        currentDate = (date.size() +1)- dateSp.getSelectedItemPosition();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void checkNumberReward(String s){
        //String s = number.getText().toString();
        boolean[] reward = new boolean[9];
        if(s.equals(settext.get(0).getText().toString())){
            reward[0]=true;
        }
        for (int i = 1; i < 6; i++) {
            String[] num = settext.get(i).getText().toString().split(" ");
            for (String s1 : num) {
                if (s.equals(s1)) {
                    reward[i] = true;
                    break;
                }
            }
        }
        String[] num = settext.get(6).getText().toString().split(" ");
        for (String s1 : num) {
            if (s.substring(0, 3).equals(s1)) {
                reward[6] = true;
                break;
            }
        }
        num = settext.get(7).getText().toString().split(" ");
        for (String s1 : num) {
            if (s.substring(3).equals(s1)) {
                reward[7] = true;
                break;
            }
        }
        num = settext.get(8).getText().toString().split(" ");
        for (String s1 : num) {
            if (s.substring(4).equals(s1)) {
                reward[8] = true;
                break;
            }
        }
        StringBuilder x = new StringBuilder("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ");
        for (int i = 0; i < reward.length; i++) {
            if (reward[i]){
                switch (i){
                    case 0:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัลที่1");
                            rewardPrice = 6000000;
                        }else{
                            x.append(",ถูกรางวัลที่1");
                            rewardPrice += 6000000;
                        }
                        break;
                    case 1:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัลข้างเคียงที่1");
                            rewardPrice = 100000;
                        }else{
                            x.append(",ถูกรางวัลข้างเคียงที่1");
                            rewardPrice += 100000;
                        }
                        break;
                    case 2:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัลที่2");
                            rewardPrice = 200000;
                        }else{
                            x.append(",ถูกรางวัลที่2");
                            rewardPrice += 200000;
                        }
                        break;
                    case 3:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัลที่3");
                            rewardPrice = 80000;
                        }else{
                            x.append(",ถูกรางวัลที่3");
                            rewardPrice += 80000;
                        }
                        break;
                    case 4:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัลที่4");
                            rewardPrice = 40000;
                        }else{
                            x.append(",ถูกรางวัลที่4");
                            rewardPrice += 40000;
                        }
                        break;
                    case 5:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัลที่5");
                            rewardPrice = 20000;
                        }else{
                            x.append(",ถูกรางวัลที่5");
                            rewardPrice += 20000;
                        }
                        break;
                    case 6:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัล3ตัวหน้า");
                            rewardPrice = 4000;
                        }else{
                            x.append(",ถูกรางวัล3ตัวหน้า");
                            rewardPrice += 4000;
                        }
                        break;
                    case 7:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัล3ตัวท้าย");
                            rewardPrice = 4000;
                        }else{
                            x.append(",ถูกรางวัล3ตัวท้าย");
                            rewardPrice += 4000;
                        }
                        break;
                    case 8:
                        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
                            x = new StringBuilder("ถูกรางวัล2ตัวท้าย");
                            rewardPrice = 2000;
                        }else{
                            x.append(",ถูกรางวัล2ตัวท้าย");
                            rewardPrice += 2000;
                        }
                        break;
                }
            }
        }
     //   return x;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
            builder.setMessage(x.toString());
        }else{
            builder.setMessage(x.toString()+"\nบันทึกลงในประวัติ?");
            builder.setNegativeButton("ไม่", (dialog, id) -> number.setText(""));
            builder.setPositiveButton("บันทึก", (dialog, id) -> {
                //Toast.makeText(this,loadhis(dateSp.getSelectedItem().toString()).toString(),Toast.LENGTH_SHORT).show();
                ArrayList<String> text = loadhis(dateSp.getSelectedItem().toString());
                text.add(s);
                savehis(dateSp.getSelectedItem().toString(),text);
                //Toast.makeText(this,loadhis(dateSp.getSelectedItem().toString()).toString(),Toast.LENGTH_SHORT).show();
                number.setText("");
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayAdapter<String> updateSpiner(){
        ArrayAdapter<String> data = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,date);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return data;
    }

    public void savehis(String key,ArrayList<String> data ){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(key,json);
        editor.apply();
        savewal("ถูก",rewardPrice);
    }

    public ArrayList<String> loadhis(String key){
        ArrayList<String> value;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key,null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        value = gson.fromJson(json,type);
        if((value == null) || (value.toString().equals("[]") )){
            value = new ArrayList<>();
        }
        return value;
    }
    public void savewal(String key,int data){
        int before =0;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (key.equals("ซื้อ")){
            before = sp.getInt("ซื้อ",0);}
        if (key.equals("ถูก")){
            before = sp.getInt("ถูก",0);}
        data = data + before ;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,data);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> number_his=loadhis("16 เมษายน 2562");
        Toast.makeText(this,number_his.toString(), Toast.LENGTH_LONG).show();
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("result");
                if (result.length() == 15){
                    String lot_num  = "";
                    String lot_time = "";
                    lot_num += result.substring(9);
                    lot_time += result.substring(3,5);
                    String lot_year = result.substring(0,2);
                    int lot_number = (Integer.parseInt(lot_time)+1)/2;
                    if (Integer.parseInt(lot_year) <62){
                        lot_number -= 24*(62-Integer.parseInt(lot_year));
                    }
                    while (currentDate != lot_number) {
                        if (currentDate > lot_number) {
                            int index = dateSp.getSelectedItemPosition() + 1;
                            if (index >= dateSp.getAdapter().getCount()){
                                Toast.makeText(this,"ขออภัย ไม่สามารภตรวจข้อมูลดังกล่าวได้", Toast.LENGTH_LONG).show();
                                return;
                            }
                            dateSp.setSelection(index);
                            currentDate -=1;
                        }
                        else{
                            int index = dateSp.getSelectedItemPosition() - 1;
                            if (index< 0){
                                Toast.makeText(this,"ขออภัย ไม่สามารภตรวจข้อมูลดังกล่าวได้", Toast.LENGTH_LONG).show();
                                return;
                            }
                            dateSp.setSelection(index);
                            currentDate +=1;
                        }
                    }
                    if(lot_num.length() != 6){
                        return;
                    }
                    Spacecraft group = lottary_data.get(dateSp.getSelectedItemPosition());
                    ArrayList<ArrayList<String>> type = group.getValue();
                    for (int i = 0; i < 9; i++) {
                        Collections.sort(type.get(i));
                        settext.get(i).setText(type.get(i).toString()
                                .replace("[", "").replace("]", "").replace(",",""));
                    }
                    checkNumberReward(lot_num);
                }
                else {
                    Toast.makeText(this,"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}


