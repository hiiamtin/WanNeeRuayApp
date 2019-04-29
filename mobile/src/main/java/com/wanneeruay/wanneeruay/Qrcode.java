package com.wanneeruay.wanneeruay;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Parameter;

public class Qrcode extends AppCompatActivity implements View.OnClickListener {


    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    static TextView textView;
    static String Qrtext;
    Camera.Parameters params;
    Camera camera = Camera.open();
    //android.graphics.Camera cam;
    boolean stop = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        surfaceView = (SurfaceView) findViewById(R.id.Camsurface);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        textView = (TextView) findViewById(R.id.Qrstr);
        textView.setOnClickListener(this); //test
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(640,480).build();
        surfaceView.setOnClickListener(this);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                params = camera.getParameters();
                if (params.getSupportedFocusModes().contains(
                        android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    params.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    return;
                }

                try{
                    cameraSource.start(holder);
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0)
                {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {

                            if (stop == true) {

                                if(qrCodes.valueAt(0).displayValue.length() != 15){
                                    Toast.makeText(getApplicationContext(),"Qrcode ของคุณไม่ใช่ของลอตเอตรี่", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                textView.setText(qrCodes.valueAt(0).displayValue);
                                String text = textView.getText().toString().substring(9);
                                History.readQr = (String) textView.getText();
                                CheckNumber.readQr = (String) textView.getText();
                                AlertDialog.Builder altdial = new AlertDialog.Builder(Qrcode.this);
                                altdial.setMessage(text+"\nใช่เลขที่คุณต้องการหรือไม่?");
                                altdial.setCancelable(false);
                                altdial.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stop = true;
                                        return;
                                    }
                                });
                                altdial.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("result",textView.getText());
                                        setResult(RESULT_OK,resultIntent);
                                        //History.number.setText(History.readQr);
                                        //History.number_his.add(History.number_his.size(), History.readQr);
                                        //History saveH = null;
                                        //History.savehis(History.dateSp.getSelectedItem().toString(), History.number_his);
                                        finish();
                                    }
                                });
                                AlertDialog alert = altdial.create();
                                alert.setTitle("Record");
                                alert.show();
                                stop = false;
                            }

                           // Toast toast = Toast.makeText(getApplicationContext(),History.readQr ,Toast.LENGTH_LONG);
                           // toast.setGravity(0,0,0);
                           // toast.show();

                        }
                    });
                }
            }
        });
    }

    public void takeQrCode() {
        startActivity(new Intent(this, Qrcode.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Qrstr :
                textView.setText("62-18-01-570331");
                String text = textView.getText().toString().substring(9);
                History.readQr = (String) textView.getText();
                CheckNumber.readQr = (String) textView.getText();
                AlertDialog.Builder altdial = new AlertDialog.Builder(Qrcode.this);
                altdial.setMessage(text+"\nใช่เลขที่คุณต้องการหรือไม่?");
                altdial.setCancelable(false);
                altdial.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                altdial.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result",textView.getText());
                        setResult(RESULT_OK,resultIntent);
                        //History.number.setText(History.readQr);
                        //History.number_his.add(History.number_his.size(), History.readQr);
                        //History saveH = null;
                        //History.savehis(History.dateSp.getSelectedItem().toString(), History.number_his);
                        finish();
                    }
                });
                AlertDialog alert = altdial.create();
                alert.setTitle("Record");
                alert.show();
                //camera.setParameters(params);
        }
    }
}
