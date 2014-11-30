package com.charred.emblem.scene.scenes;

import com.charred.emblem.resources.ResourcesManager;
import com.charred.emblem.scene.BaseScene;
import com.charred.emblem.scene.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class SplashScene extends BaseScene {
    private ResourcesManager resourcesManager;
    private Sprite splash;

    @Override
    public void createScene() {
        this.resourcesManager = ResourcesManager.getInstance();
        this.splash = new Sprite(0, 0, resourcesManager.getSplashRegion(), resourcesManager.getVbom()) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };

        splash.setScale(2f);
        splash.setPosition(235, 200);
        attachChild(splash);
    }

    @Override
    public void onBackKeyPressed() {
        return;
    }

    @Override
    public void onMenuKeyPressed() {
        return;
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
        resourcesManager.unloadSplashResources();

        detachSelf();
        dispose();
    }

    public Sprite getSplash() {
        return splash;
    }

    public void setSplash(Sprite splash) {
        this.splash = splash;
    }
}
