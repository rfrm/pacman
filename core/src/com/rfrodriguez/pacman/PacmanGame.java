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
		

//		fpsDisplay = new BitmapFont();
//
//		
//		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/bg.mp3"));
//		bgMusic.play();
	}

//		
		
//		
//		fpsDisplay.draw(batch, ""+pointCount, 1f*GameVars.PPM, 22f*GameVars.PPM);
	
	public void render(){		
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
}
