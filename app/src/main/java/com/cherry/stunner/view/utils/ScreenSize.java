package com.cherry.stunner.view.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ScreenSize {

    public static Point get(Context context) {

        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        return point;
    }

}
