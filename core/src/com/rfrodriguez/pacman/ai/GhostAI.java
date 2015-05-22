package com.rfrodriguez.pacman.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.rfrodriguez.pacman.Ghost;
import com.rfrodriguez.pacman.Maze;

import data.GameVars.Direction;

public abstract class GhostAI {
	protected Ghost ghost;
	protected Maze maze;
	private Vector2 previousCell;
	private boolean needsChoice;
	
	public GhostAI(Maze m){
		maze = m;
		needsChoice = true;		
	}
	
	public void setGhost(Ghost g){
		ghost = g;
		previousCell = maze.currentCell(ghost);
	}
	
	public void update(float dt){
		Vector2 currentCell = maze.currentCell(ghost);
		if(!previousCell.epsilonEquals(currentCell, 10e-6f)){
			needsChoice = true;
			previousCell = currentCell;
		}
		
		if(maze.inCell(ghost) && needsChoice){
			needsChoice = false;
			ArrayList<Direction> availableMovements = new ArrayList<Direction>();
			
			if(maze.canMoveDown(ghost)) availableMovements.add(Direction.down);
			if(maze.canMoveLeft(ghost)) availableMovements.add(Direction.left);
			if(maze.canMoveRight(ghost)) availableMovements.add(Direction.right);
			if(maze.canMoveUp(ghost)) availableMovements.add(Direction.up);
			
			if(availableMovements.size() != 1 || availableMovements.get(0) != ghost.currentDirection.getOppositeDirection())
				availableMovements.remove(ghost.currentDirection.getOppositeDirection());
			
			Direction betterDirection = null;
			double betterDist = Float.MAX_VALUE;			
			for(Direction candidate: availableMovements){
				double candidateDist = dist(false, candidate);
				if(candidateDist < betterDist){
					betterDirection = candidate;
					betterDist = candidateDist;
				}
			}
			ghost.setDirection(betterDirection);
		}
	}
	
	public abstract Vector2 getTarget();
	
	public double dist(boolean cilindric, Direction d){
		Vector2 ghostPosition = ghost.getPosition();
				
		int offsetY;
		if(d == Direction.up)
			offsetY = 1;
		else if(d == Direction.down)
			offsetY = -1;
		else
			offsetY = 0;
		
		int offsetX;
		if(d == Direction.right)
			offsetX = 1;
		else if(d == Direction.left)
			offsetX = -1;
		else
			offsetX = 0;
		
		Vector2 target = getTarget();
		float dx = ghostPosition.x - target.x + offsetX;
		float dy = ghostPosition.y - target.y + offsetY;
		
		return Math.sqrt(dx*dx + dy*dy);
	}
}
