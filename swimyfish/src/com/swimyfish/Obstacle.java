package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
	public float x = 0;
	public float y = 0;
	public Texture texture;
	public float height;
	public float width;
	public Rectangle hitbox;
	
	public Obstacle(float w, float h){
		width = 80;
		height = 200;
		x = w + 20;
		y = 0;	
		hitbox = new Rectangle(x,y,width,height);
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
	}
}