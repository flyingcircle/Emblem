package com.charred.emblem.touch;

import android.view.GestureDetector;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class MotionForwarder implements IOnSceneTouchListener {
    private GestureDetector detector;

    public MotionForwarder(GestureDetector detector) {
        this.detector = detector;
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        detector.onTouchEvent(pSceneTouchEvent.getMotionEvent());
        return false;
    }
}