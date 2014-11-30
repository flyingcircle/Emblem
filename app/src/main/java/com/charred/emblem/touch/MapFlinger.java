package com.charred.emblem.touch;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class MapFlinger extends SimpleOnGestureListener {
    private MapScroller scroller;

    public MapFlinger(MapScroller scroller) {
        this.scroller = scroller;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        scroller.setSpeedX(velocityX * .8f);
        scroller.setSpeedY(velocityY * .8f);
        return true;
    }

}
