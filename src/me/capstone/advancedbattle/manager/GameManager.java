package me.capstone.advancedbattle.manager;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.util.color.Color;

import me.capstone.advancedbattle.manager.managers.ActionMenuManager;
import me.capstone.advancedbattle.manager.managers.AttackManager;
import me.capstone.advancedbattle.manager.managers.EndTurnManager;
import me.capstone.advancedbattle.manager.managers.HUDManager;
import me.capstone.advancedbattle.manager.managers.LiberateManager;
import me.capstone.advancedbattle.manager.managers.MoveManager;
import me.capstone.advancedbattle.map.Map;
import me.capstone.advancedbattle.resources.ResourcesManager;
import me.capstone.advancedbattle.resources.data.TeamColor;
import me.capstone.advancedbattle.resources.tile.CursorTile;
import me.capstone.advancedbattle.resources.tile.TerrainTile;
import me.capstone.advancedbattle.tile.Tile;
import me.capstone.advancedbattle.tile.piece.Piece;

public class GameManager {
	private static ResourcesManager resourcesManager = ResourcesManager.getInstance();
	
	private Map map;
	
	private HUDManager hud;
	
	// Managers
	private ActionMenuManager actionMenuManager;
	private AttackManager attackManager;
	private LiberateManager liberateManager;
	private MoveManager moveManager;
	private EndTurnManager endTurnManager;
	
	// Team
	private int blueFunds;
	private int redFunds;
	private TeamColor turn;
	
	// Disaled
	private ArrayList<Tile> disabledTiles = new ArrayList<Tile>();
	
	// Victory
	private Sprite victoryImage;
	
	public GameManager() {
		createMap();
		initGame();
	}
	
	private void createMap() {
	    this.map = new Map(resourcesManager.getGameMap().getTileColumns(), resourcesManager.getGameMap().getTileRows());
	    Tile[][] arrayMap = map.getMap();
	    
	    TMXLayer terrainLayer = resourcesManager.getGameMap().getTMXLayers().get(0);
	    TMXLayer structureLayer = resourcesManager.getGameMap().getTMXLayers().get(1);
	    TMXLayer pieceLayer = resourcesManager.getGameMap().getTMXLayers().get(2);
	        	
	    for (int i = 0; i < map.getColumns(); i++) {
	    	for (int j = 0; j < map.getRows(); j++) {
	    		arrayMap[i][j] = new Tile(i, j, terrainLayer.getTMXTile(i, j).getGlobalTileID(), structureLayer.getTMXTile(i, j).getGlobalTileID(), pieceLayer.getTMXTile(i, j).getGlobalTileID());
	    		
	    		if (structureLayer.getTMXTile(i, j).getGlobalTileID() == TerrainTile.CITY_BLUE.getId()) {
	    			map.setBlueCities(map.getBlueCities() + 1);
	    		} else if (structureLayer.getTMXTile(i, j).getGlobalTileID() == TerrainTile.CITY_RED.getId()) {
	    			map.setRedCities(map.getRedCities() + 1);
	    		} else {
	    			// Other team colors
	    		}
	    	}
	    }
	    
	    System.out.println(map.getBlueCities());
	    System.out.println(map.getRedCities());
	    
	    map.setMap(arrayMap);
	}
	
	private void initGame() {
		this.turn = TeamColor.RED;
		
		this.blueFunds = map.getBlueCities() * 1000;
		this.redFunds = map.getRedCities() * 1000;
		
		this.hud = new HUDManager(this);
		
		this.actionMenuManager = new ActionMenuManager(this);
		this.attackManager = new AttackManager(this);
		this.liberateManager = new LiberateManager(this);
		this.moveManager = new MoveManager(this);
		this.endTurnManager = new EndTurnManager(this);
	}
	
	public TeamColor getPieceColor(Piece piece) {
		if (piece.getPieceTile().getId() >= 98 && piece.getPieceTile().getId() < 116) {
			return TeamColor.RED;
		} else if (piece.getPieceTile().getId() >= 116 && piece.getPieceTile().getId() < 134) {
			return TeamColor.BLUE;
		} else {
			return TeamColor.NULL;
		}
	}
	
	public void handleAction() {
		Tile tile = map.getTile(resourcesManager.getCursorColumn(), resourcesManager.getCursorRow());
		
		int count = 2;
		if (tile.getPiece() != null) {			
			if (disabledTiles.contains(tile)) {
				actionMenuManager.createActionMenu(count, false, false, false, false);
				return;
			}
			
			if (getPieceColor(tile.getPiece()) != turn) {
				actionMenuManager.createActionMenu(count, false, false, false, false);
				return;
			}
				
			count++;
				
			boolean canAttack = attackManager.canAttack(tile);
			if (canAttack) {
				count++;
			}
				
			boolean canLiberate = liberateManager.canLiberate(tile);			
			if (canLiberate) {
				count++;
			}
				
			actionMenuManager.createActionMenu(count, false, canAttack, canLiberate, true);
		} else {
			if ((tile.getStructureTileID() == TerrainTile.FACTORY_BLUE.getId() && turn == TeamColor.BLUE) || (tile.getStructureTileID() == TerrainTile.FACTORY_RED.getId() && turn == TeamColor.RED)) {
				count++;
				actionMenuManager.createActionMenu(count, true, false, false, true);
			} else {
				actionMenuManager.createActionMenu(count, false, false, false, true);
			}
		}
	}
	
	public void disable(Tile tile) {
		disabledTiles.add(tile);
		
		TMXLayer statusLayer = resourcesManager.getGameMap().getTMXLayers().get(3);
		TMXTile disabled = statusLayer.getTMXTile(tile.getColumn(), tile.getRow());
		disabled.setGlobalTileID(resourcesManager.getGameMap(), CursorTile.DISABLED.getId());
		statusLayer.setIndex(disabled.getTileRow() * resourcesManager.getGameMap().getTileColumns() + disabled.getTileColumn());
		statusLayer.drawWithoutChecks(disabled.getTextureRegion(), disabled.getTileX(), disabled.getTileY(), resourcesManager.getGameMap().getTileWidth(), resourcesManager.getGameMap().getTileHeight(), Color.WHITE_ABGR_PACKED_FLOAT);
		statusLayer.submit();
	}
	
	public void enable(Tile tile) {
		disabledTiles.remove(tile);
		
		TMXLayer statusLayer = resourcesManager.getGameMap().getTMXLayers().get(3);
		TMXTile enabled = statusLayer.getTMXTile(tile.getColumn(), tile.getRow());
		enabled.setGlobalTileID(resourcesManager.getGameMap(), CursorTile.CURSOR_NULL.getId());
		statusLayer.setIndex(enabled.getTileRow() * resourcesManager.getGameMap().getTileColumns() + enabled.getTileColumn());
		statusLayer.drawWithoutChecks(enabled.getTextureRegion(), enabled.getTileX(), enabled.getTileY(), resourcesManager.getGameMap().getTileWidth(), resourcesManager.getGameMap().getTileHeight(), Color.WHITE_ABGR_PACKED_FLOAT);
		statusLayer.submit();
	}
	
	public void enableAllTiles() {
		Tile[] array = disabledTiles.toArray(new Tile[disabledTiles.size()]);
		for (int i = 0; i < array.length; i++) {
			enable(array[i]);
		}
	}
	
	public void createVictoryImage() {
		if (turn == TeamColor.RED) {
			resourcesManager.getRedVictoryTextureAtlas().load();
			this.victoryImage = new Sprite(0, 0, resourcesManager.getRedVictoryTextureRegion(), resourcesManager.getVbom());
		} else if (turn == TeamColor.BLUE) {
			resourcesManager.getBlueVictoryTextureAtlas().load();
			this.victoryImage = new Sprite(0, 0, resourcesManager.getBlueVictoryTextureRegion(), resourcesManager.getVbom());
		} else {
			this.victoryImage = null;
		}
		
		victoryImage.setScale(2F);
		victoryImage.setPosition(400 - victoryImage.getWidth() / 2, 240 - victoryImage.getHeight() / 2);
		hud.getHud().attachChild(victoryImage);
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public HUDManager getHud() {
		return hud;
	}

	public void setHud(HUDManager hud) {
		this.hud = hud;
	}

	public ActionMenuManager getActionMenuManager() {
		return actionMenuManager;
	}

	public void setActionMenuManager(ActionMenuManager actionMenuManager) {
		this.actionMenuManager = actionMenuManager;
	}

	public AttackManager getAttackManager() {
		return attackManager;
	}

	public void setAttackManager(AttackManager attackManager) {
		this.attackManager = attackManager;
	}

	public LiberateManager getLiberateManager() {
		return liberateManager;
	}

	public void setLiberateManager(LiberateManager liberateManager) {
		this.liberateManager = liberateManager;
	}

	public MoveManager getMoveManager() {
		return moveManager;
	}

	public void setMoveManager(MoveManager moveManager) {
		this.moveManager = moveManager;
	}

	public EndTurnManager getEndTurnManager() {
		return endTurnManager;
	}

	public void setEndTurnManager(EndTurnManager endTurnManager) {
		this.endTurnManager = endTurnManager;
	}

	public int getBlueFunds() {
		return blueFunds;
	}

	public void setBlueFunds(int blueFunds) {
		this.blueFunds = blueFunds;
	}

	public int getRedFunds() {
		return redFunds;
	}

	public void setRedFunds(int redFunds) {
		this.redFunds = redFunds;
	}

	public TeamColor getTurn() {
		return turn;
	}

	public void setTurn(TeamColor turn) {
		this.turn = turn;
	}

	public ArrayList<Tile> getDisabledTiles() {
		return disabledTiles;
	}

	public void setDisabledTiles(ArrayList<Tile> disabledTiles) {
		this.disabledTiles = disabledTiles;
	}

	public Sprite getVictoryImage() {
		return victoryImage;
	}

	public void setVictoryImage(Sprite victoryImage) {
		this.victoryImage = victoryImage;
	}

}
