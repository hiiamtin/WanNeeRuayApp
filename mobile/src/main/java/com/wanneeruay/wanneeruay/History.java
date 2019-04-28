package com.wanneeruay.wanneeruay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.MotionEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class History extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    static EditText number;
    static ArrayList<String> date=Menu.date;
    static String readQr;
    static ArrayList<String> number_his = new ArrayList<>();
    static Spinner dateSp;
    static ListView hisList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        number = findViewById(R.id.text_input_number);
        final Button btConferm = findViewById(R.id.bt_conferm);
        final ConstraintLayout ct = findViewById(R.id.constraintLayout);
        final Button btMoney = findViewById(R.id.manage_money_bt);
        final Button btQr = findViewById(R.id.Qrbut);


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
        hisList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder altdial = new AlertDialog.Builder(History.this);
                altdial.setMessage("Do you want to delete this number?")
                        .setCancelable(false).setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                number_his.remove(position);
                                savehis(dateSp.getSelectedItem().toString(),number_his);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = altdial.create();
                alert.setTitle("DELETE");
                alert.show();
                return false;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent object holds X-Y values
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            String text = "You click at x = " + event.getX() + " and y = " + event.getY();
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }

        return super.onTouchEvent(event);
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
        String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();
        loadhis(dateSp.getSelectedItem().toString());
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
        //ArrayAdapter<CharSequence> dateSelecAp = ArrayAdapter.createFromResource(this,R.array.lottary_date,android.R.layout.simple_spinner_item);
        //dateSelecAp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return data;
    }
    private ArrayAdapter<String> updatelistView(){
        ArrayAdapter<String> data = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,Menu.date);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //ArrayAdapter<CharSequence> dateSelecAp = ArrayAdapter.createFromResource(this,R.array.lottary_date,android.R.layout.simple_spinner_item);
        //dateSelecAp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return data;
    }

    public  void toastprint() {
        Toast toast = Toast.makeText(getApplicationContext(), readQr, Toast.LENGTH_LONG);
        toast.setGravity(0, 0, 0);
        toast.show();
    }
    public void savehis(String key,ArrayList<String> data ){
        SharedPreferences sp = getSharedPreferences("History_number", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(key,json);
        editor.apply();
        loadhis(dateSp.getSelectedItem().toString());
    }
    public void loadhis(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("History_number", this.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key,null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        number_his = gson.fromJson(json,type);
        if(number_his == null){
            Toast.makeText(this, "คุณไม่ได้ซื้อลอตเตอรี่ในงวดนี้", Toast.LENGTH_LONG).show();
            hisList.setVisibility(View.INVISIBLE);
        }
        else{
            // Toast.makeText(this,dateSp.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            ArrayAdapter hisAdabt = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,number_his);
            hisList.setAdapter(hisAdabt);
            //Toast.makeText(this,number_his.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //number.setText("555555");
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("result");
                number_his.add(number_his.size(),result);
                savehis(dateSp.getSelectedItem().toString(),number_his);
            }
        }
    }
}
