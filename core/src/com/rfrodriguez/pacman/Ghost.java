package com.rfrodriguez.pacman;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rfrodriguez.pacman.ai.DeadAI;
import com.rfrodriguez.pacman.ai.GhostAI;
import com.rfrodriguez.pacman.ai.ScatterAI;
import com.rfrodriguez.pacman.ai.StateManager;

import data.GameVars;
import data.GameVars.Direction;

public class Ghost extends Player {
	public String name;
	public enum GhostState {stoped, scatter, chase, frightened, frightened_ending, dead, penalized};

	float delta;
	private GhostAI chase_ai, scatter_ai, dead_ai;	
	public Direction currentDirection;	
	private Vector2 startPostion, homeTarget;
	private StateManager sm;
	private float stateTime;
	private World world;
	
	public Ghost(String name, Vector2 startPosition, Vector2 homeTarget, Vector2 scatterTarget, World w, Maze m, AnimationLoader animationLoader, GhostAI chaseAI){		
		super(animationLoader);
		world = w;
		this.name = name;
		this.homeTarget = homeTarget;
		this.startPostion = startPosition;
		createBody(w);
		
		sm = new StateManager(this);
		
		currentDirection = Direction.up;
		stateTime = 0;
		
		chase_ai = chaseAI;
		chase_ai.setGhost(this);
		
		scatter_ai = new ScatterAI(m, scatterTarget);
		scatter_ai.setGhost(this);
		
		dead_ai = new DeadAI(m, homeTarget);
		dead_ai.setGhost(this);
		
		update(0);
	}
	
	public void init(){
		sm.init();
	}
	
	public boolean isNormal(){
		return currentState() == GhostState.chase || currentState() == GhostState.scatter;
	}
	
	public boolean isFrightened(){
		return currentState() == GhostState.frightened || currentState() == GhostState.frightened_ending;
	}
	
	public boolean inContact(PacMan p){
		// System.out.println("Dist is: "+getPosition().sub(p.getPosition()).len()/GameVars.PPM);
		return getPosition().sub(p.getPosition()).len()/GameVars.PPM <= 1.1;
	}
	
	public TextureRegion getSprite(){ 
		return getCurrentAnimation().getKeyFrame(stateTime, true);
	}
	
	public void penalize(){
		Fixture f = playerBody.getFixtureList().first();
		Filter filter = f.getFilterData();
		filter.maskBits = GameVars.BIT_MAZE | GameVars.BIT_PACMAN | GameVars.BIT_HOME;
		f.setFilterData(filter);		
		sm.penalize();
	}
	
	public void notifyHome(){
		sm.notifyHome();
	}
	
	public void backToHome(){
		sm = new StateManager(this);
	}
	
	public boolean inHome(){
		return getPosition().sub(homeTarget).len() <= 1*GameVars.PPM;
	}
	
	public void kill(){
		Fixture f = playerBody.getFixtureList().first();
		Filter filter = f.getFilterData();
		filter.maskBits = GameVars.BIT_MAZE | GameVars.BIT_HOME;
		f.setFilterData(filter);		
		sm.kill();
	}
	
	public void frighten(){
		sm.setFrightened();
	}
	
	public void setDirection(Direction d){
		currentDirection = d;
	}
	
	public void update(float dt){
		stateTime += dt;		
		getAI().update(dt);
	    if(currentDirection == Direction.left)
	    	playerBody.setLinearVelocity(-1*getVelocity()*GameVars.PPM, 0);
	    else if(currentDirection == Direction.up)	    
	    	playerBody.setLinearVelocity(0, getVelocity()*GameVars.PPM);
	    else if(currentDirection == Direction.right)
	    	playerBody.setLinearVelocity(getVelocity()*GameVars.PPM, 0);
	    else if(currentDirection == Direction.down)
	    	playerBody.setLinearVelocity(0, -1*getVelocity()*GameVars.PPM);
	    else if(currentDirection == Direction.stoped)
	    	playerBody.setLinearVelocity(0, 0);
	}

	private GhostAI getAI(){
		GhostAI ai = null;
		switch (currentState()) {
		case scatter:
			ai = scatter_ai;
			break;
		case chase:
			ai = chase_ai;
			break;
		case dead:
		case penalized:
			ai = dead_ai;
			break;
		default:
			ai = scatter_ai;
		}		
		return ai;
	}

	private float getVelocity(){
		GhostState currentState = currentState();
		if(currentState == GhostState.stoped)
			return 0;
		else if(currentState == GhostState.frightened || currentState == GhostState.frightened_ending)
			return 2f;
		else
			return 5f;
	}
	
	private void createBody(World w){
		FixtureDef fdef = new FixtureDef();
		BodyDef bdef = new BodyDef();
		
		bdef = new BodyDef();
		bdef.position.set(startPostion.x, startPostion.y);
		bdef.type = BodyType.DynamicBody;
		playerBody = w.createBody(bdef);
		
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.49f*GameVars.PPM);
		fdef.shape = circleShape;
		fdef.friction = 0;				
		fdef.filter.categoryBits = GameVars.BIT_GHOST;
		fdef.filter.maskBits = GameVars.BIT_PACMAN | GameVars.BIT_MAZE | GameVars.BIT_HOME;
		playerBody.createFixture(fdef).setUserData("ghost");
	}
	
	private GhostState currentState(){
		return sm.currentState();
	}

	@Override
	protected Animation getCurrentAnimation() {
		Animation animation = null;
		if(currentState() == GhostState.frightened){
			animation = animations.get("frightened");
		}
		else if(currentState() == GhostState.frightened_ending){
			animation = animations.get("frightened_ending");
		}
		else if(currentState() == GhostState.dead){
			if(currentDirection==Direction.left)
				animation = animations.get("dead_left");
			else if(currentDirection==Direction.up)
				animation = animations.get("dead_up");
			else if(currentDirection==Direction.right)
				animation = animations.get("dead_right");
			else if(currentDirection==Direction.down)
				animation = animations.get("dead_down");			
		}
		else{
			if(currentDirection==Direction.left)
				animation = animations.get("normal_left");
			else if(currentDirection==Direction.up)
				animation = animations.get("normal_up");
			else if(currentDirection==Direction.right)
				animation = animations.get("normal_right");
			else if(currentDirection==Direction.down)
				animation = animations.get("normal_down");
		}
		return animation; 
	}
	
	public void dispose(){
		world.destroyBody(playerBody);
	}
}
