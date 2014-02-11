package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Blocker {
	public int id;
	public float width;
	
	// TOP
	public float top_x = 0;
	public float top_y = 0;
	public Texture top_texture;
	public float top_h;
	public Rectangle top_hitbox;
	
	// BOTTOM
	public float bottom_x = 0;
	public float bottom_y = 0;
	public Texture bottom_texture;
	public float bottom_h;
	public Rectangle bottom_hitbox;
	
	public Rectangle scorebox;
	public boolean scored;
		
	public Blocker(float w, float h, int no){
		// 1196 x 768	
		id         = no;
		width      = 95;
		top_x      = w + 20;
		top_y      = 0;	
		top_h      = h/3;
		top_hitbox = new Rectangle(top_x,top_y,width,top_h);
		
		bottom_x      = w + 20;
		bottom_y      = 0;	
		bottom_h      = h/3;
		bottom_hitbox = new Rectangle(bottom_x,bottom_y,width,bottom_h);

		scorebox  = new Rectangle(0,0,10,h);
		scored    = false;
		
		top_texture   = new Texture(Gdx.files.internal("data/libgdx.png"));
	}
	
	public void set_hitboxes(float h){
		top_hitbox.set(top_x,top_y,width,h);
		bottom_hitbox.set(bottom_x,bottom_y,width,h);
	}
	
	public void update_hitboxes(float x){
		top_hitbox.setPosition(top_x - x, top_y);
		bottom_hitbox.setPosition(bottom_x - x, bottom_y);
		scorebox.setPosition(top_x - x, 0);
	}
	
}