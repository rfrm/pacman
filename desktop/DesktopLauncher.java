package com.rfrodriguez.pacman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rfrodriguez.pacman.Pacman;

import data.GameVars;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pacman";
	    config.width = 1000;
	    config.height = 1000;
		new LwjglApplication(new Pacman(), config);			
	}
}
