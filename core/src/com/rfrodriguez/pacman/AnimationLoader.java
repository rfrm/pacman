package com.rfrodriguez.pacman;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AnimationLoader {
	
	protected Texture spriteSheet;
	protected TextureRegion[][] sprites;
	
	public AnimationLoader(){
		spriteSheet = new Texture(Gdx.files.internal("assets/tileset1.png"));
		sprites = TextureRegion.split(spriteSheet, 22, 22);		
	}
	
	public abstract Map<String, Animation> load();
}
