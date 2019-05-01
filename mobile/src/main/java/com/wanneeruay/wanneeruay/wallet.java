package com.wanneeruay.wanneeruay;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class wallet extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,View.OnClickListener {
    EditText wallettext;
    Spinner spin;
    TextView sum,budall,proall,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        final Button ok_bt = findViewById(R.id.OK);
        final Button  clearbt = findViewById(R.id.clear);
        clearbt.setOnClickListener(this);
        wallettext = findViewById(R.id.budget);
        ok_bt.setOnClickListener(this);
        spin = findViewById(R.id.choose_wal);
        sum  = findViewById(R.id.Sum);
        budall  = findViewById(R.id.expendprice);
        proall  = findViewById(R.id.incomeprice);
        status = findViewById(R.id.status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.wallet, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        ok_bt.setText("ยืนยัน");
        loadwal();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.OK:
                if(wallettext.getText().toString().length() <=10   && wallettext.getText().toString().length() != 0)  {
                    char[] checkinput ={'2','1','4','7','4','8','3','6','4','7'};
                    String checktext =  wallettext.getText().toString();
                    if(wallettext.getText().toString().length() ==10){
                        for (int i =0; i < checktext.length();i++){
                            if(checktext.charAt(i) > checkinput[i]){
                                EditText priceLot = findViewById(R.id.budget);
                                priceLot.setError("ไม่สามาถบันทึกลขที่ท่านต้องการได้");
                                wallettext.setText("");
                                return;
                            }
                        }
                    }
                    EditText priceLot = findViewById(R.id.budget);
                    if (priceLot.getText().toString().equals("")) {
                        priceLot.setError("โปรดใส่ราคาที่ท่านต้องการ");
                    }
                    else {
                        if (spin.getSelectedItem().toString().equals("ซื้อ")) {
                            int budget = Integer.parseInt(wallettext.getText().toString());// + Integer.parseInt(budall.getText().toString()) ;
                            savewal(spin.getSelectedItem().toString(), budget);
                        } else if (spin.getSelectedItem().toString().equals("ถูก")) {
                            int profit = Integer.parseInt(wallettext.getText().toString());// +  Integer.parseInt(proall.getText().toString()) ;
                            savewal(spin.getSelectedItem().toString(), profit);
                        }
                        priceLot.setText("");
                    }
                }
                else{
                    EditText priceLot = findViewById(R.id.budget);
                    priceLot.setError("ไม่สามาถบันทึกลขที่ท่านต้องการได้");
                    wallettext.setText("");
                }
                break;
            case R.id.clear:
                clearwallet();
                break;
        }
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
        loadwal();
    }
   public void loadwal(){
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int buy = sharedPreferences.getInt("ซื้อ",0);
        int bet = sharedPreferences.getInt("ถูก",0);
        int all = bet-buy;
        if (all >= 0){
            status.setText("กำไร");
        }
        else {
            status.setText("ขาดทุน");
        }
        int absall = java.lang.Math.abs(all);
        sum.setText(Integer.toString(absall));
        budall.setText(Integer.toString(buy));
        proall.setText(Integer.toString(bet));
}
    public void clearwallet(){
        SharedPreferences clear = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = clear.edit();
        editor.putInt("ซื้อ",0);
        editor.apply();
        editor.putInt("ถูก",0);
        editor.apply();
        loadwal();
    }
}
