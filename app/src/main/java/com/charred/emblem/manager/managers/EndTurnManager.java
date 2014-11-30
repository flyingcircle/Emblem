package com.charred.emblem.manager.managers;

import com.charred.emblem.manager.GameManager;
import com.charred.emblem.resources.ResourcesManager;
import com.charred.emblem.resources.data.TeamColor;

public class EndTurnManager {
    private static ResourcesManager resourcesManager = ResourcesManager.getInstance();

    private GameManager game;

    public EndTurnManager(GameManager game) {
        this.game = game;
    }

    public void endTurn() {
        if (game.getTurn() == TeamColor.RED) {
            resourcesManager.unloadGameAudio();
            resourcesManager.loadGameAudio();
            resourcesManager.getBlueMusic().play();
            game.setTurn(TeamColor.BLUE);
            game.setBlueFunds(game.getBlueFunds() + game.getMap().getBlueCities() * 1000);
        } else if (game.getTurn() == TeamColor.BLUE) {
            resourcesManager.unloadGameAudio();
            resourcesManager.loadGameAudio();
            resourcesManager.getRedMusic().play();
            game.setTurn(TeamColor.RED);
            game.setRedFunds(game.getRedFunds() + game.getMap().getRedCities() * 1000);
        } else {
            game.setTurn(TeamColor.NULL);
        }

        game.enableAllTiles();
        game.getHud().updateHUD();
    }
}
