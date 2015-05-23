package com.rfrodriguez.pacman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.rfrodriguez.pacman.screen.AddBestScoreScreen;
import com.rfrodriguez.pacman.screen.BestScoresScreen;
import com.rfrodriguez.pacman.screen.MainMenuScreen;
import com.rfrodriguez.pacman.screen.PlayScreen;

public class PacmanGame extends Game {	
	BitmapFont fpsDisplay;	
	
	@Override
	public void create () {
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render(){
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	public void menu(){
		setScreen(new MainMenuScreen(this));
	}
	
	public void normalGame(){
		setScreen(new PlayScreen(this));
	}
	
	public void goToAddNewScore(int pointCount){
		setScreen(new AddBestScoreScreen(this, pointCount));
	}	
	
	public void goToBestScoreScreen(){
		setScreen(new BestScoresScreen(this));
	}
}
