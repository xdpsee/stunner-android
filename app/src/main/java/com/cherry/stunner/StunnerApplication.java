package com.cherry.stunner;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class StunnerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
