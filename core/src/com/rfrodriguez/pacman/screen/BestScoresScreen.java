package com.rfrodriguez.pacman.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rfrodriguez.pacman.PacmanGame;
import com.rfrodriguez.pacman.Score;
import com.rfrodriguez.pacman.ScoreHandler;
import com.rfrodriguez.pacman.ScoreService;

public class BestScoresScreen extends ScreenAdapter {
	
	private PacmanGame game;
	private ScoreHandler scoreHandler;
	
	private Stage stage = new Stage();
	private Table table = new Table();
	
	public BestScoresScreen(PacmanGame g) {
		game = g;
		scoreHandler = ScoreService.loadScoreHandler();
	}
	
	public void show(){
		Skin skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));
		TextButton returnButton = new TextButton("Regresar", skin);
		returnButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				game.menu();
			}
		});
		Label title = new Label("Mejores puntajes", skin);
		
		table.setSkin(skin);
		table.add(title).padBottom(25).colspan(3).row();
		
		for(Score score: scoreHandler.bestScores){
			System.out.println(score.playerName+"|"+score.playerScore+"|"+score.scoreDate);
		    table.add(score.playerName).padLeft(20).padRight(10);
		    table.add(""+score.playerScore).padLeft(10).padRight(10);
		    table.add(score.scoreDate).padLeft(10).padRight(20);
		    table.row();
		}
		
		table.add(returnButton).colspan(3).padTop(20);
	    table.row();
		table.setFillParent(true);
		
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}
}
