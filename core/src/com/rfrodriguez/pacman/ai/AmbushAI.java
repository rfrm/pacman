package com.rfrodriguez.pacman.ai;

import com.badlogic.gdx.math.Vector2;
import com.rfrodriguez.pacman.Maze;
import com.rfrodriguez.pacman.PacMan;

import data.GameVars;
import data.GameVars.Direction;

public class AmbushAI extends GhostAI {
	private PacMan pacman;

	public AmbushAI(Maze m, PacMan p){
		super(m);
		pacman = p;		
	}

	@Override
	public Vector2 getTarget() {
		Vector2 offset = null;
		if(pacman.currentDirection == Direction.left)
			offset = new Vector2(-4*GameVars.PPM, 0);
		else if(pacman.currentDirection == Direction.up)
			offset = new Vector2(0, 4*GameVars.PPM);
		else if(pacman.currentDirection == Direction.right)
			offset = new Vector2(4*GameVars.PPM, 0);
		else if(pacman.currentDirection == Direction.down)
			offset = new Vector2(0, -4*GameVars.PPM);
		
		return pacman.getPosition().add(offset);
	}
}
