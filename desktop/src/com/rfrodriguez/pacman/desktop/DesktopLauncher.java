package com.rfrodriguez.pacman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rfrodriguez.pacman.PacmanGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pacman";
		config.width = 480;
		config.height = 600;
		new LwjglApplication(new PacmanGame(), config);
	}
}
