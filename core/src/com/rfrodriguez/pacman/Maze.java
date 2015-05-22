package com.rfrodriguez.pacman;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import data.GameVars;

public class Maze {
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMapTileLayer maze;

	public Maze(TiledMap m){		
		map = m;
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		maze = (TiledMapTileLayer) map.getLayers().get("maze");
	}

	public boolean canMoveDown(Player player){
		int current_row = (int) (player.getPosition().y/GameVars.PPM);
		int current_column = (int) (player.getPosition().x/GameVars.PPM);
		return maze.getCell(current_column, current_row-1) == null && inCell(player);		
	}
	
	public boolean canMoveUp(Player player){
		int current_row = (int) (player.getPosition().y/GameVars.PPM);
		int current_column = (int) (player.getPosition().x/GameVars.PPM);		
		return maze.getCell(current_column, current_row+1) == null && inCell(player);
	}
	
	public boolean canMoveRight(Player player){
		int current_row = (int) (player.getPosition().y/GameVars.PPM);
		int current_column = (int) (player.getPosition().x/GameVars.PPM);
		return maze.getCell(current_column+1, current_row) == null && inCell(player);
	}
	
	public boolean canMoveLeft(Player player){		
		int current_row = (int) (player.getPosition().y/GameVars.PPM);
		int current_column = (int) (player.getPosition().x/GameVars.PPM);
		return maze.getCell(current_column-1, current_row) == null && inCell(player);
	}
	
	public boolean inCell(Player player){
		float current_row = player.getPosition().y/GameVars.PPM;
		float current_column = player.getPosition().x/GameVars.PPM;
		
		float cellCenterX = ((int) (player.getPosition().x/GameVars.PPM)) + 0.5f;
		float cellCenterY = ((int) (player.getPosition().y/GameVars.PPM)) + 0.5f;
		
		float dx = current_column - cellCenterX;
		float dy = current_row - cellCenterY;

		return Math.sqrt(dx*dx + dy*dy)<=0.25;
	}
	
	public Vector2 currentCell(Player player){
		int current_row = (int) (player.getPosition().y/GameVars.PPM);
		int current_column = (int) (player.getPosition().x/GameVars.PPM);
		return new Vector2(current_column, current_row);
	}
	
	public void render(OrthographicCamera camera){
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
}
