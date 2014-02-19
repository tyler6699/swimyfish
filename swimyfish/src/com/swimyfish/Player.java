package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player {
	public float x;
	public float y;
	public Texture player_alive;
	public Texture player_hit;
	public Texture trail;
	public float height;
	public float width;
	public Rectangle hitbox;

	public Player(Device device, int player_id){
		
		if (player_id == 1){
			width = device.w_scale * 60;
			height = device.h_scale * 60;
		} else if (player_id == 2) {
			width = device.w_scale * 40;
			height = device.h_scale * 40;
		}

		x = (device.w/2) - (device.w_scale*200);
		y = (device.h/2) - (width/2);	
		hitbox = new Rectangle(x,y,width,height);
		player_alive = new Texture(Gdx.files.internal("data/hero/" + player_id + "/player.png"));
		player_hit = new Texture(Gdx.files.internal("data/hero/" + player_id + "/hit.png"));
		trail = new Texture(Gdx.files.internal("data/hero/" + player_id + "/player-trail.png"));
		// texture = new Texture(Gdx.files.internal("data/disco.png"));
	}
	
}