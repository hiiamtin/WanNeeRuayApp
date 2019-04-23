package com.wanneeruay.wanneeruay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class wallet extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        final Button ok_bt = findViewById(R.id.OK);
        ok_bt.setOnClickListener(this);
        ok_bt.setText("ยืนยัน");
        Spinner spin = findViewById(R.id.choose_wal);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.wallet, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        EditText priceLot = findViewById(R.id.Priceloterry);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    @Override
    public void onClick(View v) {
        EditText priceLot = findViewById(R.id.Priceloterry);
        if (priceLot.getText().toString().equals("")) {
            priceLot.setError("โปรดใส่ราคาที่ท่านต้องการ");
        }
        else {
            priceLot.setText("");
        }
    }
}
