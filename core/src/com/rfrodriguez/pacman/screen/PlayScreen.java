package com.rfrodriguez.pacman.screen;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rfrodriguez.pacman.Ghost;
import com.rfrodriguez.pacman.Maze;
import com.rfrodriguez.pacman.MyContactListener;
import com.rfrodriguez.pacman.MyMapLoader;
import com.rfrodriguez.pacman.PacMan;
import com.rfrodriguez.pacman.PacmanGame;

import data.GameVars;

public class PlayScreen extends ScreenAdapter{
			
	private Maze maze;
	private World world;
	private PacMan player;
	private PacmanGame game;
	private Viewport viewport;
	private SpriteBatch batch;	
	private Box2DDebugRenderer b2rd;
	private OrthographicCamera camera;
	private ShapeRenderer sr;
	private Array<Body> pacDeletionList;
	private boolean debug;
	private Map<String, Ghost> ghosts;

	public PlayScreen(PacmanGame g){
		game = g;
		debug = false;
		batch = new SpriteBatch();		
		
		camera = new OrthographicCamera();
		camera.position.set(11.5f*GameVars.PPM, 11.5f*GameVars.PPM, 0);
		viewport = new FitViewport(23*GameVars.PPM, 26*GameVars.PPM, camera);
		viewport.apply();
		
		world = new World(new Vector2(0, 0), true);
		MyContactListener cl = new MyContactListener(this);
		world.setContactListener(cl);		
		b2rd = new Box2DDebugRenderer();
		
		
		MyMapLoader ml = new MyMapLoader("assets/map.tmx", world);
		
		player = ml.loadPacman();
		maze = ml.getMaze();
		ml.loadPacs();
		ml.loadEnergizers();
		ghosts = ml.getGhosts(maze, player);
		
		pacDeletionList = new Array<Body>();
		
		sr = new ShapeRenderer();
		sr.setProjectionMatrix(camera.combined);
	}
	
	public void show(){
		
	}
	
	public void render(float dt){	
		update(dt);		

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		render_pacs();
		render_player();
		render_enemies();
		render_maze();
		render_stats();
		
		if(debug){
			b2rd.render(world, camera.combined);	
		}
	}
	
	private void update(float dt){
		world.step(0.02f, 6, 2);
		handle_keypress();
		update_player(dt);
		update_enemies(dt);
		destroyEatenPacs();
	}
	
	private void handle_keypress(){
		if(Gdx.input.isKeyPressed(Keys.LEFT) && maze.canMoveLeft(player)){
			player.moveLeft();
		}
		else if(Gdx.input.isKeyPressed(Keys.UP) && maze.canMoveUp(player)) {
			player.moveUp();
		}
		else if(Gdx.input.isKeyPressed(Keys.RIGHT) && maze.canMoveRight(player)){
			player.moveRigth();
		}
		else if(Gdx.input.isKeyPressed(Keys.DOWN) && maze.canMoveDown(player)){
			player.moveDown();
		}
		
		if(Gdx.input.isKeyPressed(Keys.S)){
			debug = !debug;
		}
	}
	
	private void update_player(float dt){
		player.update(dt);
	}
	
	private void update_enemies(float dt){
		for(Ghost ghost: ghosts.values())
			ghost.update(dt);		
	}
	
	private void render_maze(){
		batch.begin();
		maze.render(camera);
        batch.end();
	}
	
	private void render_player(){
		Vector2 playerPos = player.getPosition();
		
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
        batch.draw(player.currentFrame, playerPos.x-11, playerPos.y-11);
        batch.end();
	}
	
	private void render_enemies(){
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		for(Ghost ghost: ghosts.values()){
			Vector2 ghostPos = ghost.getPosition();
			batch.draw(ghost.getSprite(), ghostPos.x-11, ghostPos.y-11);
		}
		batch.end();
	}
	
	private void render_stats(){
		
	}
	
	private void render_pacs(){
		Fixture bodyFixture;
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);

		sr.begin(ShapeType.Filled);
		sr.setColor(Color.WHITE);
		for(Body body: bodies) {
			bodyFixture = body.getFixtureList().first(); 
			if(bodyFixture.getUserData().equals("pac")) {
				Vector2 position = body.getPosition();				
				sr.circle(position.x, position.y, 0.15f*GameVars.PPM);
			}
			if(bodyFixture.getUserData().equals("energizer")) {
				Vector2 position = body.getPosition();				
				sr.circle(position.x, position.y, 0.3f*GameVars.PPM);
			}
		}
		sr.end();
	}
	
	public void destroyEatenPacs(){
		for(int i=0;i<pacDeletionList.size;i++){
			Body pac = pacDeletionList.get(i);
			world.destroyBody(pac);
			//pointCount += 50;
		}
		pacDeletionList.clear();		
	}
	
	public void addToDeletionList(Body b){
		pacDeletionList.add(b);
	}
	
	public void beginFrightenedMode(){
		for(Ghost ghost: ghosts.values()){
			ghost.frighten();
		}
	}
	
	public void checkGhostPacmanContact(){
		for(Ghost ghost: ghosts.values()){
			if(ghost.inContact(player)){
				if(ghost.isFrightened()){
					ghost.kill();
				}
			}
		}
	}
}
