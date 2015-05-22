package com.rfrodriguez.pacman;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import data.GameVars;
import data.GameVars.Direction;

public class PacMan extends Player {	
	public Direction currentDirection;	
	public TextureRegion currentFrame;	
	
	float stateTime;
	
	public PacMan(World world, Vector2 startPosition, AnimationLoader animationLoader){
		super(animationLoader);
		stateTime = 0;	
		
		currentDirection = Direction.up;
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		bdef.position.set(startPosition);
		//bdef.position.set(0, 0);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);		
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.49f*GameVars.PPM);
		fdef.shape = circleShape;
		fdef.filter.categoryBits = GameVars.BIT_PACMAN;
		fdef.filter.maskBits = GameVars.BIT_MAZE | GameVars.BIT_PAC | GameVars.BIT_ENERGIZER | GameVars.BIT_GHOST;		
		playerBody.createFixture(fdef).setUserData("pacman");
	}

	public void moveLeft() {
		currentDirection = Direction.left;
	}
	
	public void moveUp() {
		currentDirection = Direction.up;
	}
	
	public void moveRigth() {
		currentDirection = Direction.right;
	}
	
	public void moveDown() {
		currentDirection = Direction.down;
	}
	
	public void stop() {
		currentDirection = Direction.stoped;
	}
	
	public void update(float dt) {
		
		stateTime += dt;
		currentFrame = getCurrentAnimation().getKeyFrame(stateTime, true);
		
	    if(currentDirection == Direction.left)
	    	playerBody.setLinearVelocity(-5*GameVars.PPM, 0);
	    else if(currentDirection == Direction.up)	    
	    	playerBody.setLinearVelocity(0, 5*GameVars.PPM);
	    else if(currentDirection == Direction.right)
	    	playerBody.setLinearVelocity(5*GameVars.PPM, 0);
	    else if(currentDirection == Direction.down)
	    	playerBody.setLinearVelocity(0, -5*GameVars.PPM);
	    else if(currentDirection == Direction.stoped)
	    	playerBody.setLinearVelocity(0, 0);
	}

	@Override
	protected Animation getCurrentAnimation() {
		Animation animation = null;
		if(currentDirection==Direction.left)
			animation = animations.get("normal_left");
		else if(currentDirection==Direction.up)
			animation = animations.get("normal_up");
		else if(currentDirection==Direction.right)
			animation = animations.get("normal_right");
		else if(currentDirection==Direction.down)
			animation = animations.get("normal_down");
		return animation;
	}
}
