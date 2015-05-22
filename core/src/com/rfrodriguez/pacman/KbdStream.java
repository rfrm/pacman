package com.rfrodriguez.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KbdStream {
	
	public enum Direction {left, up, right, down, none};
	
	public Direction value(){
		
		Direction direction = Direction.none;
		if(Gdx.input.isKeyPressed(Keys.LEFT)) direction = Direction.left;
		if(Gdx.input.isKeyPressed(Keys.UP)) direction = Direction.up;
	    if(Gdx.input.isKeyPressed(Keys.RIGHT)) direction = Direction.right;
	    if(Gdx.input.isKeyPressed(Keys.DOWN)) direction = Direction.down;
	    
	    return direction;
	}
}
