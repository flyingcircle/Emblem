package me.capstone.advancedbattle.tile.terrain.structure;

import me.capstone.advancedbattle.data.tile.TileType;
import me.capstone.advancedbattle.tile.terrain.DynamicTerrain;
import me.capstone.advancedbattle.player.Player;

//Class for the Building terrain. It extends DynamicTerrain so we will eventually add more to this so it can change states.
public class Building extends DynamicTerrain {

	public Building(int row, int column, Player owner) {
		super(row, column, TileType.BUILDING, owner);
	}

}
