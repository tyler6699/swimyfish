package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
	public int id;
	public float x = 0;
	public float y = 0;
	public Texture texture;
	public float height;
	public float width;
	public Rectangle hitbox;
	public Rectangle scorebox;
	public boolean scored;
	public String t_or_b;
	
	public Obstacle(float w, float h, int no, String place){
		// 1196 x 768	
		id        = no;
		t_or_b    = place;
		width     = w/12;
		height    = h/3.5f;
		x         = w + 20;
		y         = 0;	
		hitbox    = new Rectangle(x,y,width,height);
		scorebox  = new Rectangle(0,0,10,h);
		scored    = false;
		texture   = new Texture(Gdx.files.internal("data/libgdx.png"));
	}
	
	public void set_hitboxes(float h){
		hitbox.set(x,y,width,h);
	}
}