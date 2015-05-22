package com.rfrodriguez.pacman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.rfrodriguez.pacman.screen.MainMenuScreen;
import com.rfrodriguez.pacman.screen.PlayScreen;

public class PacmanGame extends Game {	
	BitmapFont fpsDisplay;	
	private Music bgMusic;	
	private Screen normalGameScreen, currentScreen;
	
	public void create () {
		normalGameScreen = new PlayScreen(this);
		currentScreen  = new MainMenuScreen(this);
		setScreen(currentScreen);
//		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/bg.mp3"));
//		bgMusic.play();
	}

	public void render(){		
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	public void normalGame(){
		setScreen(normalGameScreen);
	}
}
