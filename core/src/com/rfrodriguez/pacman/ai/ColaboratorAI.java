package com.rfrodriguez.pacman.ai;

import com.badlogic.gdx.math.Vector2;
import com.rfrodriguez.pacman.Ghost;
import com.rfrodriguez.pacman.Maze;
import com.rfrodriguez.pacman.PacMan;

import data.GameVars;
import data.GameVars.Direction;

public class ColaboratorAI extends GhostAI {

	private Ghost helped;
	private PacMan pacman;

	public ColaboratorAI(Maze m, Ghost h, PacMan p) {
		super(m);
		helped = h;		
		pacman = p;
	}

	@Override
	public Vector2 getTarget() {
		Vector2 pacmanPosition = pacman.getPosition();
		
		Vector2 offset = null;
		if(pacman.currentDirection == Direction.left)
			offset = new Vector2(-2*GameVars.PPM, 0);
		else if(pacman.currentDirection == Direction.up)
			offset = new Vector2(0, 2*GameVars.PPM);
		else if(pacman.currentDirection == Direction.right)
			offset = new Vector2(2*GameVars.PPM, 0);
		else if(pacman.currentDirection == Direction.down)
			offset = new Vector2(0, -2*GameVars.PPM);
		
		Vector2 ahead = pacmanPosition.add(offset);		
		return ahead.scl(2).sub(helped.getPosition());		
	}
}
