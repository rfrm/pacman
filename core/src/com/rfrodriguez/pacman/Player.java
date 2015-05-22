package com.rfrodriguez.pacman;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import data.GameVars.Direction;

public abstract class Player {
	Body playerBody;
	Map<String, Animation> animations;
	
	public Player(AnimationLoader animationLoader){
		animations = animationLoader.load();
	}
	
	public Vector2 getPosition(){
		return playerBody.getPosition();
	}	
	
	
	protected abstract Animation getCurrentAnimation();
}
