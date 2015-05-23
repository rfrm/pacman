package com.rfrodriguez.pacman.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rfrodriguez.pacman.PacmanGame;

public class MainMenuScreen extends ScreenAdapter {
	private PacmanGame game;
	private Stage stage;
	private Skin skin;
	private Texture Bg;
	private SpriteBatch spriteBatch;

	public MainMenuScreen(PacmanGame g) {
		game = g;
		stage = new Stage();
		Bg = new Texture(Gdx.files.internal("assets/splash.png"));
		skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));
		
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		TextButton newGameButton = new TextButton("Jugar", skin, "default"); 
		newGameButton.setWidth(200);		
		newGameButton.setPosition(
				(Gdx.graphics.getWidth() - newGameButton.getWidth())/2,
				0.25f*Gdx.graphics.getHeight());		
		stage.addActor(newGameButton);
		newGameButton.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
                game.normalGame();
            }
        });
		
//		TextButton newOnlineGameButton = new TextButton("Jugar VS", skin, "default"); 
//		newOnlineGameButton.setWidth(200);		
//		newOnlineGameButton.setPosition(
//				(Gdx.graphics.getWidth() - newOnlineGameButton.getWidth())/2,
//				0.20f*Gdx.graphics.getHeight());
//		stage.addActor(newOnlineGameButton);
		
		TextButton bestScoresButton = new TextButton("Mejores puntajes", skin, "default"); 
		bestScoresButton.setWidth(200);		
		bestScoresButton.setPosition(
				(Gdx.graphics.getWidth() - bestScoresButton.getWidth())/2,
				 0.20f*Gdx.graphics.getHeight());
		bestScoresButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.goToBestScoreScreen();
			}
		});
		
		stage.addActor(bestScoresButton);
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
        spriteBatch.draw(Bg, 0, 120);
        spriteBatch.end();

		stage.act();
		stage.draw();
	}
	
	public void show(){
		spriteBatch = new SpriteBatch();
	}
}
