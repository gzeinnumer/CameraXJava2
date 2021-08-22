package com.gzeinnumer.cameraxjava2.dialog;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.gzeinnumer.cameraxjava2.helper.CameraXUtils;
import com.gzeinnumer.cameraxjava2.R;

import java.io.File;

public class CameraDialog extends DialogFragment {

    public static final String TAG = "CameraDialog";
    private int animationStyle = R.style.DialogStyle;
    private PreviewView mPreviewView;
    private CardView btnShhot;
    private ConstraintLayout _dialogCanvas;

    public static CameraDialog newInstance() {
        return new CameraDialog();
    }

    public CameraDialog() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, animationStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = animationStyle;

        _dialogCanvas = view.findViewById(R.id.dialog_canvas);
        mPreviewView = view.findViewById(R.id.camera);
        btnShhot = view.findViewById(R.id.capture);

        _dialogCanvas.setBackground(requireActivity().getResources().getDrawable(R.drawable.rounded_corner));

        initCamera(view);
    }

    private void initCamera(View view) {
//        new CameraXUtils(requireActivity()).startCamera(mPreviewView, btnShhot).result(new CameraXUtils.FileCallBack() {
//            @Override
//            public void path(File file) {
//                ImageView imageView = view.findViewById(R.id.iv);
//                imageView.setRotation(90);
//                imageView.setImageBitmap(BitmapFactory.decodeFile(file.toString()));
//            }
//        }).onError(new CameraXUtils.ErrorCallBack() {
//            @Override
//            public void message(String msg) {
//                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}