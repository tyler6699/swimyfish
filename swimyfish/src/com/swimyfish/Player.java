package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player {
	public int id;
	public float x;
	public float y;
	public Texture player_alive;
	public Texture player_hit;
	public Texture trail, trail_2, locked_price;
	public int price;
	public float height;
	public float width;
	public Rectangle hitbox;
	boolean locked;
	

	public Player(Device device, int player_id){
		id = player_id;
		width = device.w_scale * 60;
		height = device.h_scale * 60;
		
		if (player_id == 2){
			width = device.w_scale * 50;
			height = device.h_scale * 50;
		} else if (player_id == 3) {
			width = device.w_scale * 40;
			height = device.h_scale * 40;
		} else if (player_id == 4) {	
			width = device.w_scale * 30;
			height = device.h_scale * 30;
		}

		x = (device.w/2) - (device.w_scale*200);
		y = (device.h/2) - (width/2);	
		hitbox = new Rectangle(x,y,width,height);
		player_alive = new Texture(Gdx.files.internal("data/hero/" + player_id + "/player.png"));
		player_hit = new Texture(Gdx.files.internal("data/hero/" + player_id + "/hit.png"));
		trail = new Texture(Gdx.files.internal("data/hero/" + player_id + "/player-trail.png"));
		trail_2 = new Texture(Gdx.files.internal("data/hero/" + player_id + "/player-trail-2.png"));
		if (player_id != 1){
			locked_price = new Texture(Gdx.files.internal("data/ui/" + player_id +"00.png"));
			price = player_id * 100;
		}
		// texture = new Texture(Gdx.files.internal("data/disco.png"));
	}
	
}