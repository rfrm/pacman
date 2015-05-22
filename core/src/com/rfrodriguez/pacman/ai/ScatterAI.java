package com.rfrodriguez.pacman.ai;

import com.badlogic.gdx.math.Vector2;
import com.rfrodriguez.pacman.Maze;

public class ScatterAI extends GhostAI {
	
	private Vector2 target;

	public ScatterAI(Maze m, Vector2 t){
		super(m);
		target = t;		
	}
	
	public Vector2 getTarget(){
		return target;
	}
}
