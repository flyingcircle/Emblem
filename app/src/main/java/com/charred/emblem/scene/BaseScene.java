package com.charred.emblem.scene;

import com.charred.emblem.scene.SceneManager.SceneType;

import org.andengine.entity.scene.Scene;

public abstract class BaseScene extends Scene {

    public BaseScene() {
        createScene();
    }

    public abstract void createScene();

    public abstract void onBackKeyPressed();

    public abstract void onMenuKeyPressed();

    public abstract SceneType getSceneType();

    public abstract void disposeScene();

}