package com.gzeinnumer.cameraxjava2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzeinnumer.cameraxjava2.helper.CameraXUtils;
import com.gzeinnumer.cameraxjava2.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private PreviewView mPreviewView;
    private CardView btnShoot;
    private CardView btnRotate;
    private int rotate = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPreviewView = findViewById(R.id.camera);
        btnShoot = findViewById(R.id.capture);
        btnRotate = findViewById(R.id.rotate);

        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
            initOnClick();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void initOnClick() {
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rotate==1){
                    rotate=2;
                } else {
                    rotate =1;
                }
                startCamera();
            }
        });
    }

    private void startCamera() {
        new CameraXUtils(this).startCamera(mPreviewView, btnShoot, rotate).result(new CameraXUtils.FileCallBack() {
            @Override
            public void path(File file) {
//                ImageView imageView = findViewById(R.id.iv);
//                imageView.setRotation(90);
//                imageView.setImageBitmap(BitmapFactory.decodeFile(file.toString()));
                Intent returnIntent = new Intent();
                returnIntent.putExtra("status","1");
                returnIntent.putExtra("result",file.toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }).onError(new CameraXUtils.ErrorCallBack() {
            @Override
            public void message(String msg) {
//                Toast.makeText(CameraActivity.this, msg, Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("status","0");
                returnIntent.putExtra("result", msg);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}