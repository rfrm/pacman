package com.rfrodriguez.pacman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.rfrodriguez.pacman.screen.PlayScreen;

public class PacmanGame extends Game {	
	BitmapFont fpsDisplay;	
	private Music bgMusic;	
	private PlayScreen playScreen;
	
	public void create () {
		playScreen= new PlayScreen(this);
		setScreen(playScreen);
//		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/bg.mp3"));
//		bgMusic.play();
	}

	public void render(){		
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
}
