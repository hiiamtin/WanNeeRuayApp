package com.wanneeruay.wanneeruay;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wanneeruay.wanneeruay.Firebase.Spacecraft;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class
CheckNumber extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText number;
    ArrayList<String> date=Menu.date;
    ArrayList<Spacecraft> lottary_data=Menu.lottary_data;
    static String readQr;
    ArrayList<TextView> settext;
    Spinner dateSp;
    int rewardPrice = 0;
    int currentDate = date.size();
    final Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        number = findViewById(R.id.text_input_numberC);
        final Button btConferm = findViewById(R.id.bt_confermC);
        final ConstraintLayout ct = findViewById(R.id.constraintLayoutC);
        final Button butQr2 = findViewById(R.id.QrbutC);
        dateSp = findViewById(R.id.spinner_date);
        settext = new ArrayList<TextView>();
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
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
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
        currentDate = (date.size())- dateSp.getSelectedItemPosition();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        if(x.toString().equals("เสียใจด้วยคุณไม่ถูกรางวัลใดๆ")){
            builder.setMessage(x.toString()+"\nคุณต้องการบันทึกลงในประวัติ?");
            builder.setNegativeButton("ไม่", (dialog, id) -> number.setText(""));
            builder.setPositiveButton("บันทึก", (dialog, id) -> {
                ArrayList<String> text = loadhis(dateSp.getSelectedItem().toString());
                text.add(s);
                savehis(dateSp.getSelectedItem().toString(),text);

                number.setText("");
            });
        }else{
            builder.setMessage(x.toString()+"\nคุณต้องการบันทึกลงในประวัติ?");
            builder.setNegativeButton("ไม่", (dialog, id) -> number.setText(""));
            builder.setPositiveButton("บันทึก", (dialog, id) -> {
                ArrayList<String> text = loadhis(dateSp.getSelectedItem().toString());
                text.add(s);
                savehis(dateSp.getSelectedItem().toString(),text);
                number.setText("");
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private ArrayAdapter<String> updateSpiner(){
        ArrayList<String> value = new ArrayList<>();
        for (String s:date.subList(1,date.size())) {
            value.add(s);
        }
        ArrayAdapter<String> data = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,value);
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
        IntentResult result2 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String result = result2.getContents().trim();
        if(result.length() != 15){
            Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_LONG).show();
            Intent resultIntent = new Intent();
            setResult(RESULT_CANCELED,resultIntent);
            return;
        }
        String lot_year = result.substring(0,2);
        String lot_time = "";
        lot_time += result.substring(3,5);
        if(result.charAt(2) != '-'){
            Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_SHORT).show();
            return;
        }
        if(result.charAt(5) != '-'){
            Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_SHORT).show();
            return;
        }
        if(result.charAt(8) != '-'){
            Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_SHORT).show();
            return;
        }
        int[] Checktextqr = {0,1,3,4,6,7,9,10,11,12,13,14};
        for(int i =0; i < Checktextqr.length;i++){
            if (result.charAt(Checktextqr[i]) < '0' ||result.charAt(Checktextqr[i]) >'9'){
                Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!lot_year.equals("62") && !lot_year.equals("61")){
            Toast.makeText(getApplicationContext(),"ขออภัยไม่สามรถเก็บ วันที่ และ ข้อมูล ดังกล่าวได้", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(lot_time) > 48 ||Integer.parseInt(lot_time) == 0){
            Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ลอตเตอรี่", Toast.LENGTH_SHORT).show();
            return;
        }
        String text = result.substring(9);
        android.app.AlertDialog.Builder altdial = new android.app.AlertDialog.Builder(CheckNumber.this);
        altdial.setMessage(text+"\nใช่เลขที่คุณต้องการหรือไม่?");
        altdial.setCancelable(false);
        altdial.setPositiveButton("No", (dialog, which) -> {
        });
        altdial.setNegativeButton("Yes", (dialog, which) -> {
            if (result.length() == 15){
                String lot_num  = "";
                String lot_time1 = "";
                lot_num += result.substring(9);
                lot_time1 += result.substring(3,5);
                String lot_year1 = result.substring(0,2);
                int lot_number = (Integer.parseInt(lot_time1)+1)/2;
                if (Integer.parseInt(lot_year1) <62){
                    lot_number -= 24*(62-Integer.parseInt(lot_year1));
                }
                while (currentDate != lot_number) {
                    if (currentDate > lot_number) {
                        int index = dateSp.getSelectedItemPosition() + 1;
                        if (index >= dateSp.getAdapter().getCount()){
                            Toast.makeText(getApplicationContext(),"ขออภัย ไม่สามารภตรวจข้อมูลดังกล่าวได้", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dateSp.setSelection(index);
                        currentDate -=1;
                    }
                    else{
                        int index = dateSp.getSelectedItemPosition() - 1;
                        if (index< 0){
                            Toast.makeText(getApplicationContext(),"ขออภัย ไม่สามารภตรวจข้อมูลดังกล่าวได้", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_LONG).show();
            }
        });
        android.app.AlertDialog alert = altdial.create();
        alert.setTitle("Record");
        alert.show();
    }
}


