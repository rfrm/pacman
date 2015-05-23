package com.rfrodriguez.pacman.screen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rfrodriguez.pacman.PacmanGame;
import com.rfrodriguez.pacman.Score;
import com.rfrodriguez.pacman.ScoreHandler;
import com.rfrodriguez.pacman.ScoreService;

public class AddBestScoreScreen extends ScreenAdapter {
	
	private int newScore;
	private PacmanGame game;
	private TextField nameText;
	private ScoreHandler scoreHandler;
	private Stage stage = new Stage();
	private Table table = new Table();

	public AddBestScoreScreen(PacmanGame g, int score) {
		game = g;
		newScore = score;
		scoreHandler = ScoreService.loadScoreHandler();		
	}
	
	public void show(){
		Skin skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));
		Label title = new Label("Has logrado un puntaje de "+newScore+"!", skin);
	    Label nameLabel = new Label("Nombre:", skin);
	    nameText = new TextField("", skin);
	    nameText.setMaxLength(10);
	    TextButton okButton = new TextButton("Guardar", skin);

	    table.add(title).padBottom(25).colspan(2);
	    table.row();
	    table.add(nameLabel);
	    table.add(nameText).width(100);
	    table.row();	    
	    table.add(okButton).colspan(2).padTop(20);
	    table.row();
		table.setFillParent(true);
		
		okButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
            	DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");            	
            	Date today = Calendar.getInstance().getTime();  
            	String scoreDate = df.format(today);
            	
            	String userName = nameText.getText();
            	scoreHandler.addScore(new Score(userName, newScore, scoreDate));
            	scoreHandler.persist();
            	game.goToBestScoreScreen();
            }
		});		
		
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
