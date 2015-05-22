package com.rfrodriguez.pacman.ai;

import com.badlogic.gdx.math.Vector2;
import com.rfrodriguez.pacman.Maze;

public class DeadAI extends GhostAI {

	private Vector2 home;

	public DeadAI(Maze m, Vector2 h){
		super(m);
		home = h;
	}
	
	@Override
	public Vector2 getTarget() {
		return home;
	}

}
