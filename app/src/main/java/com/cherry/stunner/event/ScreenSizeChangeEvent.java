package com.cherry.stunner.event;

import lombok.Data;

@Data
public class ScreenSizeChangeEvent {

    // ORIENTATION_LANDSCAPE,ORIENTATION_PORTRAIT
    private int orientation;
    // in pixels
    private int width;
    // in pixels
    private int height;

    public ScreenSizeChangeEvent(int orientation, int width, int height) {
        this.orientation = orientation;
        this.width = width;
        this.height = height;
    }

}
