package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button startButton;

    private final static int CAMERA_PERMISSIONS_GRANTED = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCameraPermission();
    }

    private boolean getCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // 권한이 왜 필요한지 설명이 필요한가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Toast.makeText(this, "카메라 사용을 위해 확인버튼을 눌러주세요!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        CAMERA_PERMISSIONS_GRANTED);
                return true;
            }
        }
    }
    public void clicked_res(View view) {
        Intent intent1 =new Intent(MainActivity.this, RestaurantActivity.class);
        startActivity(intent1);
    }

    public void clicked_cafe(View view) {
        Intent intent2 =new Intent(MainActivity.this, CafeActivity.class);
        startActivity(intent2);
    }

    public void clicked_karaoke(View view) {
        Intent intent3 =new Intent(MainActivity.this, KaraokeActivity.class);
        startActivity(intent3);
    }

    public void clicked_internet(View view) {
        Intent intent4 =new Intent(MainActivity.this, InternetActivity.class);
        startActivity(intent4);
    }

    public void QRButtonClicked(View view) {
        Intent goNextActivity = new Intent(getApplicationContext(), QRCodeScan.class);
        startActivity(goNextActivity);
    }
}

