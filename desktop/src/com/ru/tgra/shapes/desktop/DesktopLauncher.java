package com.ru.tgra.shapes.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ru.tgra.shapes.First3DGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Lab1"; // or whatever you like
		config.width = 1080;  //experiment with
		config.height = 1080;  //the window size
		config.x = 150;
		config.y = 50;

		new LwjglApplication(new First3DGame(), config);
	}
}
