package com.rfrodriguez.pacman;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rfrodriguez.pacman.ai.AmbushAI;
import com.rfrodriguez.pacman.ai.ChaseAI;
import com.rfrodriguez.pacman.ai.ColaboratorAI;
import com.rfrodriguez.pacman.ai.ScatterAI;

import data.GameVars;

public class MyMapLoader {
	private World world;
	private TiledMap map;

	public MyMapLoader(String mapFile, World w){
		world = w;		
		map = new TmxMapLoader().load(Gdx.files.local(mapFile).path());
	}
	
	public Maze getMaze() {
		Body box;
		BodyDef bdef;
		FixtureDef fdef;
		
		TiledMapTileLayer maze = (TiledMapTileLayer) map.getLayers().get("maze");
		
		for(int row=0;row<maze.getHeight();row++){
			for(int col=0;col<maze.getWidth();col++){
				Cell cell = maze.getCell(col, row);
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				bdef = new BodyDef();
				bdef.position.set((col+0.5f)*GameVars.PPM, (row+0.5f)*GameVars.PPM);
				bdef.type = BodyType.StaticBody;
				box = world.createBody(bdef);
				
				ChainShape shape = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-0.5f*GameVars.PPM, -0.5f*GameVars.PPM);
				v[1] = new Vector2(-0.5f*GameVars.PPM, 0.5f*GameVars.PPM);
				v[2] = new Vector2(0.5f*GameVars.PPM, 0.5f*GameVars.PPM);
				v[3] = new Vector2(0.5f*GameVars.PPM, -0.5f*GameVars.PPM);
				v[4] = new Vector2(-0.5f*GameVars.PPM, -0.5f*GameVars.PPM);
				shape.createChain(v);
				
				fdef = new FixtureDef();
				fdef.shape = shape;
				fdef.friction = 0;
				fdef.filter.categoryBits = GameVars.BIT_MAZE;
				fdef.filter.maskBits = GameVars.BIT_PACMAN | GameVars.BIT_GHOST;
				box.createFixture(fdef).setUserData("maze");
			}
		}
		
		return new Maze(map);		
	}
	
	public int loadPacs(){
		Body box;
		FixtureDef fdef = new FixtureDef();
		BodyDef bdef = new BodyDef();
		CircleShape cshape = new CircleShape();
		TiledMapTileLayer pacsLayer = (TiledMapTileLayer) map.getLayers().get("pacs");

		int pacCount = 0;
		for(int row=0;row<pacsLayer.getHeight();row++){
			for(int col=0;col<pacsLayer.getWidth();col++){
				Cell cell = pacsLayer.getCell(col, row);

				if(cell == null) continue;
				if(cell.getTile() == null) continue;

				bdef.position.set((col+0.5f)*GameVars.PPM, (row+0.5f)*GameVars.PPM);
				bdef.type = BodyType.StaticBody;
				box = world.createBody(bdef);				
				cshape.setRadius(0.1f*GameVars.PPM);				
				fdef.shape = cshape;
				fdef.friction = 0;
				fdef.isSensor = true;
				fdef.filter.categoryBits = GameVars.BIT_PAC;
				fdef.filter.maskBits = GameVars.BIT_PACMAN;
				box.createFixture(fdef).setUserData("pac");
				pacCount++;
			}
		}
		return pacCount;
	}

	public void loadEnergizers(){
		Body box;
		FixtureDef fdef = new FixtureDef();
		BodyDef bdef = new BodyDef();
		MapObjects energizers = (MapObjects) map.getLayers().get("energizers").getObjects();
		
		for(MapObject o: energizers){
			EllipseMapObject co = (EllipseMapObject) o;
			Ellipse c = co.getEllipse();
			
			bdef = new BodyDef();
			bdef.position.set(c.x, c.y);
			bdef.type = BodyType.StaticBody;
			box = world.createBody(bdef);
			CircleShape cshape = new CircleShape();
			cshape.setRadius(0.3f*GameVars.PPM);
			
			fdef = new FixtureDef();
			fdef.shape = cshape;
			fdef.friction = 0;
			fdef.isSensor = true;
			fdef.filter.categoryBits = GameVars.BIT_ENERGIZER;
			fdef.filter.maskBits = GameVars.BIT_PACMAN;
			box.createFixture(fdef).setUserData("energizer");
		}
	}
	
	public void loadHome(){
		Body box;
		FixtureDef fdef = new FixtureDef();
		BodyDef bdef = new BodyDef();
		
		Ellipse home = ((EllipseMapObject) map.getLayers().get("home").getObjects().get("home")).getEllipse();
		bdef = new BodyDef();
		bdef.position.set(home.x, home.y);
		bdef.type = BodyType.StaticBody;
		box = world.createBody(bdef);
		CircleShape cshape = new CircleShape();
		cshape.setRadius(0.3f*GameVars.PPM);
		
		fdef = new FixtureDef();
		fdef.shape = cshape;
		fdef.friction = 0;
		fdef.isSensor = true;
		fdef.filter.categoryBits = GameVars.BIT_HOME;
		fdef.filter.maskBits = GameVars.BIT_GHOST ;
		box.createFixture(fdef).setUserData("home");
		
	}
	
	public Map<String, Ghost> getGhosts(Maze maze, PacMan p){
		String ghostName;
		EllipseMapObject startPositionEllipseObject;		
		Ellipse startPosition, scatterTarget;
		Ghost blinky, pinky, inky, clyde;
		
		Map<String, Ghost> ghosts = new HashMap<String, Ghost>();
		MapObjects startPositions = (MapObjects) map.getLayers().get("start_positions").getObjects();
		MapObjects scatterTargets = (MapObjects) map.getLayers().get("scatter_targets").getObjects();
		Ellipse home = ((EllipseMapObject) map.getLayers().get("home").getObjects().get("home")).getEllipse();
		
		// Load blinky
		ghostName = "blinky";
		startPositionEllipseObject = (EllipseMapObject) startPositions.get(ghostName);
		startPosition = startPositionEllipseObject.getEllipse();
		scatterTarget = ((EllipseMapObject) scatterTargets.get(ghostName)).getEllipse();
		blinky = new Ghost(ghostName, getTarget(startPosition), getTarget(home), getTarget(scatterTarget), world, maze, new AnimationLoader() {
			
			@Override
			public Map<String, Animation> load() {
				Map<String, Animation> animations = new HashMap<String, Animation>();
				animations.put("normal_left", new Animation(0.15f, new TextureRegion[] {sprites[2][4], sprites[2][5]}));
				animations.put("normal_up", new Animation(0.15f, new TextureRegion[] {sprites[2][6], sprites[2][7]}));
				animations.put("normal_right", new Animation(0.15f, new TextureRegion[] {sprites[2][0], sprites[2][1]}));
				animations.put("normal_down", new Animation(0.15f, new TextureRegion[] {sprites[2][2], sprites[2][3]}));

				animations.put("dead_right", new Animation(0.15f, new TextureRegion[] {sprites[5][8], sprites[5][9]}));
				animations.put("dead_down", new Animation(0.15f, new TextureRegion[] {sprites[5][10], sprites[5][11]}));
				animations.put("dead_left", new Animation(0.15f, new TextureRegion[] {sprites[5][12], sprites[5][13]}));
				animations.put("dead_up", new Animation(0.15f, new TextureRegion[] {sprites[5][14], sprites[5][15]}));		

				animations.put("frightened", new Animation(0.15f, new TextureRegion[] {sprites[1][12], sprites[1][13]}));
				animations.put("frightened_ending", new Animation(0.15f, new TextureRegion[] {sprites[1][12], sprites[6][4], sprites[1][13], sprites[6][5]}));
				
				return animations;
			}
		}, new ChaseAI(maze, p));
		ghosts.put(ghostName, blinky);		
		
		// Load pinky
		ghostName = "pinky";
		startPositionEllipseObject = (EllipseMapObject) startPositions.get(ghostName);
		startPosition = startPositionEllipseObject.getEllipse();
		scatterTarget = ((EllipseMapObject) scatterTargets.get(ghostName)).getEllipse();
		pinky = new Ghost(ghostName, getTarget(startPosition), getTarget(home), getTarget(scatterTarget), world, maze, new AnimationLoader() {
			
			@Override
			public Map<String, Animation> load() {
				Map<String, Animation> animations = new HashMap<String, Animation>();
				animations.put("normal_left", new Animation(0.15f, new TextureRegion[] {sprites[4][4], sprites[4][5]}));
				animations.put("normal_up", new Animation(0.15f, new TextureRegion[] {sprites[4][6], sprites[4][7]}));
				animations.put("normal_right", new Animation(0.15f, new TextureRegion[] {sprites[4][0], sprites[4][1]}));
				animations.put("normal_down", new Animation(0.15f, new TextureRegion[] {sprites[4][2], sprites[4][3]}));

				animations.put("dead_right", new Animation(0.15f, new TextureRegion[] {sprites[5][8], sprites[5][9]}));
				animations.put("dead_down", new Animation(0.15f, new TextureRegion[] {sprites[5][10], sprites[5][11]}));
				animations.put("dead_left", new Animation(0.15f, new TextureRegion[] {sprites[5][12], sprites[5][13]}));
				animations.put("dead_up", new Animation(0.15f, new TextureRegion[] {sprites[5][14], sprites[5][15]}));			

				animations.put("frightened", new Animation(0.15f, new TextureRegion[] {sprites[1][12], sprites[1][13]}));
				animations.put("frightened_ending", new Animation(0.075f, new TextureRegion[] {sprites[1][12], sprites[6][4], sprites[1][13], sprites[6][5]}));
				return animations;
			}
		}, new AmbushAI(maze, p));
		ghosts.put(ghostName, pinky);
		
		// Load inky
		ghostName = "inky";
		startPositionEllipseObject = (EllipseMapObject) startPositions.get(ghostName);
		startPosition = startPositionEllipseObject.getEllipse();
		scatterTarget = ((EllipseMapObject) scatterTargets.get(ghostName)).getEllipse();
		inky = new Ghost(ghostName, getTarget(startPosition), getTarget(home), getTarget(scatterTarget), world, maze, new AnimationLoader() {
			
			@Override
			public Map<String, Animation> load() {
				Map<String, Animation> animations = new HashMap<String, Animation>();
				animations.put("normal_left", new Animation(0.15f, new TextureRegion[] {sprites[4][12], sprites[4][13]}));
				animations.put("normal_up", new Animation(0.15f, new TextureRegion[] {sprites[4][14], sprites[4][15]}));
				animations.put("normal_right", new Animation(0.15f, new TextureRegion[] {sprites[4][8], sprites[4][9]}));
				animations.put("normal_down", new Animation(0.15f, new TextureRegion[] {sprites[4][10], sprites[4][11]}));

				animations.put("dead_right", new Animation(0.15f, new TextureRegion[] {sprites[5][8], sprites[5][9]}));
				animations.put("dead_down", new Animation(0.15f, new TextureRegion[] {sprites[5][10], sprites[5][11]}));
				animations.put("dead_left", new Animation(0.15f, new TextureRegion[] {sprites[5][12], sprites[5][13]}));
				animations.put("dead_up", new Animation(0.15f, new TextureRegion[] {sprites[5][14], sprites[5][15]}));			

				animations.put("frightened", new Animation(0.15f, new TextureRegion[] {sprites[1][12], sprites[1][13]}));
				animations.put("frightened_ending", new Animation(0.075f, new TextureRegion[] {sprites[1][12], sprites[6][4], sprites[1][13], sprites[6][5]}));
				return animations;				
			}
		}, new ColaboratorAI(maze, blinky, p));
		ghosts.put(ghostName, inky);
		
		// Load clyde
		ghostName = "clyde";
		startPositionEllipseObject = (EllipseMapObject) startPositions.get(ghostName);
		startPosition = startPositionEllipseObject.getEllipse();
		scatterTarget = ((EllipseMapObject) scatterTargets.get(ghostName)).getEllipse();
		clyde = new Ghost(ghostName, getTarget(startPosition), getTarget(home), getTarget(scatterTarget), world, maze, new AnimationLoader() {
			
			@Override
			public Map<String, Animation> load() {
				Map<String, Animation> animations = new HashMap<String, Animation>();
				animations.put("normal_left", new Animation(0.15f, new TextureRegion[] {sprites[5][4], sprites[5][5]}));
				animations.put("normal_up", new Animation(0.15f, new TextureRegion[] {sprites[5][6], sprites[5][7]}));
				animations.put("normal_right", new Animation(0.15f, new TextureRegion[] {sprites[5][0], sprites[5][1]}));
				animations.put("normal_down", new Animation(0.15f, new TextureRegion[] {sprites[5][2], sprites[5][3]}));

				animations.put("dead_right", new Animation(0.15f, new TextureRegion[] {sprites[5][8], sprites[5][9]}));
				animations.put("dead_down", new Animation(0.15f, new TextureRegion[] {sprites[5][10], sprites[5][11]}));
				animations.put("dead_left", new Animation(0.15f, new TextureRegion[] {sprites[5][12], sprites[5][13]}));
				animations.put("dead_up", new Animation(0.15f, new TextureRegion[] {sprites[5][14], sprites[5][15]}));			

				animations.put("frightened", new Animation(0.15f, new TextureRegion[] {sprites[1][12], sprites[1][13]}));
				animations.put("frightened_ending", new Animation(0.075f, new TextureRegion[] {sprites[1][12], sprites[6][4], sprites[1][13], sprites[6][5]}));
				return animations;
			}
		}, new ScatterAI(maze, getTarget(scatterTarget)));
		ghosts.put(ghostName, clyde);
				
		return ghosts;
	}
	
	public PacMan loadPacman(){
		MapObjects startPositions = (MapObjects) map.getLayers().get("start_positions").getObjects();
		Vector2 startPosition = getTarget(((EllipseMapObject)startPositions.get("pacman")).getEllipse());
		PacMan pacman = new PacMan(world, startPosition, new AnimationLoader(){
			@Override
			public Map<String, Animation> load() {				
				Map<String, Animation> animations = new HashMap<String, Animation>();
				animations.put("normal_left", new Animation(0.15f, new TextureRegion[] {sprites[6][0], sprites[6][2]}));
				animations.put("normal_up", new Animation(0.15f, new TextureRegion[] {sprites[6][1], sprites[6][3]}));
				animations.put("normal_right", new Animation(0.15f, new TextureRegion[] {sprites[2][12], sprites[2][14]}));
				animations.put("normal_down", new Animation(0.15f, new TextureRegion[] {sprites[2][13], sprites[2][15]}));
				
				Animation dead =  new Animation(0.5f, 
					new TextureRegion[] {sprites[3][4], sprites[3][5], sprites[3][6], sprites[3][7], sprites[3][8],
						sprites[3][9], sprites[3][10], sprites[3][11], sprites[3][12], sprites[3][13], sprites[3][14]});
				dead.setPlayMode(Animation.PlayMode.NORMAL);
				animations.put("dead", dead);
				
				return animations;
			}			
		});
		
		return pacman; 
	}
	
	private Vector2 getTarget(Ellipse e){
		return new Vector2(e.x, e.y);		
	}
}
