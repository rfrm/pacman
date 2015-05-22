package com.rfrodriguez.pacman.ai;

import com.badlogic.gdx.math.Vector2;
import com.rfrodriguez.pacman.Maze;
import com.rfrodriguez.pacman.PacMan;

public class ChaseAI extends GhostAI{
	private PacMan pacman;

	public ChaseAI(Maze m, PacMan p){
		super(m);
		pacman = p;		
	}

	@Override
	public Vector2 getTarget() {		
		return pacman.getPosition();
	}
}
