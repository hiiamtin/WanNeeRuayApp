package com.wanneeruay.wanneeruay;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        surfaceView = (SurfaceView) findViewById(R.id.Camsurface);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        textView = (TextView) findViewById(R.id.Qrstr);
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(640,480).build();
        surfaceView.setOnClickListener(this);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
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
                            //Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            //vibrator.vibrate(1000);
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            History.readQr = (String) textView.getText();
                            Toast toast = Toast.makeText(getApplicationContext(),History.readQr ,Toast.LENGTH_LONG);
                            toast.setGravity(0,0,0);
                            toast.show();
                            finish();
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

    }
}
