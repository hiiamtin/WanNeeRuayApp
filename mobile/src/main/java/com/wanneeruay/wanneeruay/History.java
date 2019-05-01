package com.wanneeruay.wanneeruay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class History extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    EditText number;
    static ArrayList<String> date=Menu.date;
    static String readQr;
    static ArrayList<String> number_his = new ArrayList<>();
    Spinner dateSp;
    ListView hisList ;
    int currentDate = date.size()+1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        number = findViewById(R.id.text_input_number);
        final Button btConferm = findViewById(R.id.bt_conferm);
        final ConstraintLayout ct = findViewById(R.id.constraintLayout);
        final Button btMoney = findViewById(R.id.manage_money_bt);
        final Button btQr = findViewById(R.id.Qrbut);
        final Button checkB = findViewById(R.id.check_bt);
        checkB.setOnClickListener(this);
        hisList =findViewById(R.id.list_history);
        dateSp = findViewById(R.id.spinner_date_H);
        dateSp.setAdapter(updateSpiner());
        dateSp.setOnItemSelectedListener(this);
        number.setBackgroundTintMode(PorterDuff.Mode.ADD);
        number.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNOTOK,getTheme())));
        btConferm.setOnClickListener(this);
        btMoney.setOnClickListener(this);
        ct.setOnClickListener(this);
        btQr.setOnClickListener(this);
        number.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                checkErrorTextInput(v);
                hideSoftKeyboard(v);
            }else{
                showKeyboard(v);
            }
        });
        loadhis(dateSp.getSelectedItem().toString());
        hisList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder altdial = new AlertDialog.Builder(History.this);
            altdial.setMessage("Do you want to delete this number?")
                    .setCancelable(false).setPositiveButton("No", (dialog, which) -> dialog.cancel())
                    .setNegativeButton("Yes", (dialog, which) -> {
                        String temp = number_his.get(position);
                        for (int i = number_his.size()-1; i >= 0 ;i--){
                            if (number_his.get(i).equals(temp)){
                                number_his.remove(i);
                                break;
                            }
                        }

                        savehis(dateSp.getSelectedItem().toString(),number_his);
                        dialog.cancel();
                    });
            AlertDialog alert = altdial.create();
            alert.setTitle("DELETE");
            alert.show();
            return false;
        });
    }
    @Override
    public  void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_conferm:
                checkErrorTextInput(v);
                if(number.getError()==null) {
                    hideSoftKeyboard(v);
                    ArrayList<String> text = number_his;
                    text.add(number.getText().toString());
                    savehis(dateSp.getSelectedItem().toString(),text);
                    number.setText("");
                }else{
                    number.requestFocus();
                }
                break;
            case R.id.constraintLayout:
                checkErrorTextInput(v);
                hideSoftKeyboard(v);
                break;
            case R.id.Qrbut:
                Intent intent = new Intent(History.this,Qrcode.class);
                startActivityForResult(intent,1);
                break;
            case R.id.manage_money_bt:
                startActivity(new Intent(this, wallet.class));
                break;
            case R.id.check_bt:
                Intent intent2 = new Intent(History.this,CheckNumber.class);
                startActivityForResult(intent2,1);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loadhis(dateSp.getSelectedItem().toString());
        if(number_his.toString().equals("[]") ){
            Toast.makeText(this, "คุณไม่ได้ซื้อลอตเตอรี่ในงวดนี้", Toast.LENGTH_LONG).show();
        }
        currentDate = (date.size()+1)- dateSp.getSelectedItemPosition();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        loadhis(dateSp.getSelectedItem().toString());
    }
    public void loadhis(String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key,null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        number_his = gson.fromJson(json,type);
        ArrayList<String> text = new ArrayList<>();
        if((number_his == null) || (number_his.toString().equals("[]") )){
            hisList.setVisibility(View.INVISIBLE);
            number_his = new ArrayList<>();
        }
        else{
            ArrayList<String> number_his2 = new ArrayList<>();
            number_his2.addAll(number_his);
            for(int i = 0;i < number_his2.size();i++)
            {
                int  count =1;
                String temp = number_his2.get(i);
                text.add(text.size(),temp);
                for(int h =i+1; h < number_his2.size();h++){
                    if(temp.equals(number_his2.get(h))){
                        number_his2.remove(h);
                        count +=1;
                        h-=1;
                    }
                }
                text.set(i,text.get(i)+"                                                        "+count+"ใบ");
            }
            hisList.setVisibility(View.VISIBLE);
            ArrayAdapter hisAdabt = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,text);
            hisList.setAdapter(hisAdabt);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                            Toast.makeText(this,"ขออภัย ไม่สามารถเก็บ เลข และว ันที่ ดังกล่าวได้", Toast.LENGTH_LONG).show();
                            return;

                        }
                        dateSp.setSelection(index);
                        currentDate -=1;
                    }
                    else{
                        int index = dateSp.getSelectedItemPosition() - 1;
                        if (index< 0){
                            Toast.makeText(this,"ขออภัย ไม่สามารถเก็บ เลข และว ันที่ ดังกล่าวได้", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dateSp.setSelection(index);
                        currentDate +=1;
                    }
                }
                    loadhis(dateSp.getSelectedItem().toString());
                    number_his.add(number_his.size(),lot_num);
               }
            else {
                    Toast.makeText(this,"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_LONG).show();
                 }
                savehis(dateSp.getSelectedItem().toString(),number_his);
            }
        }
    }
}
