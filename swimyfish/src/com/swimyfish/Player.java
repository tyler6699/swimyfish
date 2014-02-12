package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player {
	public float x;
	public float y;
	public Texture texture;
	public Texture texture_2;
	public float height;
	public float width;
	public Rectangle hitbox;

		
	public Player(float w, float h){
		width = 60;
		height = 60;
		x = (w/2) - 200;
		y = (h/2) - (width/2);	
		hitbox = new Rectangle(x,y,width,height);
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		// texture = new Texture(Gdx.files.internal("data/disco.png"));
		texture_2 = new Texture(Gdx.files.internal("data/dark.png"));
	}
}
