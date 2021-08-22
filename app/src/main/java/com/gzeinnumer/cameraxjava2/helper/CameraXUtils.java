package com.gzeinnumer.cameraxjava2.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Size;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.gzeinnumer.cameraxjava2.dialog.CameraXUtils2;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraXUtils {

    private FileCallBack fileCallBack;
    private ErrorCallBack errorCallBack;


    private Activity activity;
    private final Context context;
    private int rotate;

    public CameraXUtils(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    private CameraXUtils build() {
        return this;
    }

    public CameraXUtils startCamera(PreviewView mPreviewView, CardView btnShoot, int rotate) {
        this.rotate = rotate;
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(activity);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider, mPreviewView, btnShoot);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    errorCallBack.message(e.getMessage());
                }
            }
        }, ContextCompat.getMainExecutor(activity));
        return this;
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider, PreviewView mPreviewView, CardView btnShoot) {
        Executor executor = Executors.newSingleThreadExecutor();
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .requireLensFacing(rotate == 1 ? CameraSelector.LENS_FACING_BACK : CameraSelector.LENS_FACING_FRONT)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

        ImageCapture.Builder builder = new ImageCapture.Builder();
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            builder.setTargetResolution(new Size(1080, 1920));
        } else {
            builder.setTargetResolution(new Size(1920, 1080));
        }

        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        ImageCapture imageCapture = builder
                .setTargetRotation(activity.getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) activity, cameraSelector, preview, imageAnalysis, imageCapture);

        btnShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + ".jpg");

                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (fileCallBack != null)
                                    fileCallBack.path(file);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        error.printStackTrace();
                        if (errorCallBack != null)
                            errorCallBack.message(error.getMessage());
                    }
                });
            }
        });
    }

    public String getBatchDirectoryName() {
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        return app_folder_path;
    }

    public CameraXUtils result(FileCallBack fileCallBack) {
        this.fileCallBack = fileCallBack;
        return this;
    }

    public interface FileCallBack {
        void path(File file);
    }

    public interface ErrorCallBack {
        void message(String msg);
    }

    public void onError(ErrorCallBack errorCallBack) {
        this.errorCallBack = errorCallBack;
    }
}
