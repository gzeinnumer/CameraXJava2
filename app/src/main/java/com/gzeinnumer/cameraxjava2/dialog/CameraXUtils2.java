package com.gzeinnumer.cameraxjava2.dialog;

import android.annotation.SuppressLint;
import android.content.Context;

public class CameraXUtils2 {

    @SuppressLint("StaticFieldLeak") static volatile CameraXUtils2 singleton = null;

    public static CameraXUtils2 get() {
        if (singleton == null) {
            synchronized (CameraXUtils2.class) {
                if (singleton == null) {
                    if (CameraXUtils2.context == null) {
                        throw new IllegalStateException("context == null");
                    }
                    singleton = new CameraXUtils2(CameraXUtils2.context).build();
                }
            }
        }
        return singleton;
    }

    static Context context;

    public CameraXUtils2(Context context) {
        this.context = context;
    }

    public CameraXUtils2 build() {
        return this;
    }
}
