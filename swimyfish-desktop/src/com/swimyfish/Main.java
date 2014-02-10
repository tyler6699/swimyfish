package com.swimyfish;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "swimyfish";
		cfg.useGL20 = false;
		cfg.width = 1196;
		cfg.height = 768;
		// 1196.0 768.0
 
		new LwjglApplication(new FlappyBox(), cfg);
	}
}