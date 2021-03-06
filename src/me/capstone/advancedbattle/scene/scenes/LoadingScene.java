package me.capstone.advancedbattle.scene.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import me.capstone.advancedbattle.resources.ResourcesManager;
import me.capstone.advancedbattle.scene.BaseScene;
import me.capstone.advancedbattle.scene.SceneManager.SceneType;

public class LoadingScene extends BaseScene {
	private ResourcesManager resourcesManager;
	
    @Override
    public void createScene() {
    	this.resourcesManager = ResourcesManager.getInstance();
        setBackground(new Background(Color.WHITE));
        Text text = new Text(0, 0, resourcesManager.getFont(), "Loading...", new TextOptions(HorizontalAlign.CENTER), resourcesManager.getVbom());
        text.setScale(1.5F);
        text.setPosition(400 - text.getWidth() / 2, 240 - text.getHeight() / 2);
        attachChild(text);
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
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene() {
    	detachSelf();
    	dispose();
    }
}