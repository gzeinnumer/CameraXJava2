package com.gzeinnumer.cameraxjava2.dialog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class CameraXBuilder {
    private final FragmentActivity activity;
    public CameraXBuilder(FragmentActivity activity) {
        this.activity = activity;
    }

    public void build() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment previous = activity.getSupportFragmentManager().findFragmentByTag(CameraDialog.TAG);
        if(previous != null){
            transaction.remove(previous);
        }
        CameraDialog dialog = CameraDialog.newInstance();
        dialog.show(transaction, CameraDialog.TAG);
    }
}
