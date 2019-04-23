package com.wanneeruay.wanneeruay;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RandomNumber extends AppCompatActivity implements View.OnClickListener{

    ImageView picture;
    ConstraintLayout numberLayout;
    TextView number;
    boolean status = false;
    private SensorManager sm;
    private float   ace1Val;    //current acc value and gravity
    private float   ace1Last;
    private float   shake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        picture = findViewById(R.id.p1);
        numberLayout = findViewById(R.id.bt_lektaided);
        final Button btRandom = findViewById(R.id.bt_random);
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        ace1Val = SensorManager.GRAVITY_EARTH;
        ace1Last= SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
        clear();
        btRandom.setOnClickListener(this);
    }
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            ace1Last = ace1Val;
            ace1Val = (float) Math.sqrt((double) (x*x+y*y+z*z));
            float delta = ace1Val - ace1Last;
            //shake = shake * 0.9f + delta ;
            //number = findViewById(R.id.two0);number.setText(String.valueOf(shake));
            if ((ace1Val > 16)  && (status == true) ){
                shack();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    private void clear(){
        picture.setVisibility(View.VISIBLE);
        numberLayout.setVisibility(View.INVISIBLE);
        status = false;
    }
    private void shack(){
        random();
        picture.setVisibility(View.INVISIBLE);
        numberLayout.setVisibility(View.VISIBLE);
        status = false;
    }
    private void random(){
        number = findViewById(R.id.two0);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = findViewById(R.id.two1);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = findViewById(R.id.three0);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = findViewById(R.id.three1);
        number.setText(String.valueOf(new Random().nextInt(10)));
        number = findViewById(R.id.three2);
        number.setText(String.valueOf(new Random().nextInt(10)));
    }

    @Override
    public void onClick(View v) {

        if (status == false &&  picture.getVisibility() == View.INVISIBLE){
            clear();
            status = true;
            Toast toast = Toast.makeText(getApplicationContext(),"Please Shake ME!!! <3" ,Toast.LENGTH_LONG);
            toast.setGravity(0,0,0);
            toast.show();

        }
        else{
            status = true;
            //Toast toast = Toast.makeText(getApplicationContext(),"Please Shake ME!!! <3" ,Toast.LENGTH_LONG);
            Toast toast = Toast.makeText(getApplicationContext(),"Please Shake ME!!! <3" ,Toast.LENGTH_LONG);
            toast.setGravity(0,0,0);
            toast.show();
        }
    }
}
