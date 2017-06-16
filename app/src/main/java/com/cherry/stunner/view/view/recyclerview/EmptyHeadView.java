package com.cherry.stunner.view.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cherry.stunner.R;

public class EmptyHeadView extends RelativeLayout {

    public EmptyHeadView(Context context) {
        super(context);
        init(context);
    }

    public EmptyHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.recycler_view_empty_header, this);
    }
}
