package com.swimyfish;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "swimyfish";
		cfg.useGL20 = false;
		
		cfg.width = 1196;
		cfg.height = 786;

		//cfg.width = 1920;
		//cfg.height = 1080;
		
		//cfg.width = 1280;
		//cfg.height = 720;
			    
	    //cfg.width = 800;
	    //cfg.height = 480;

		new LwjglApplication(new FlappyBox(), cfg);
	}
}