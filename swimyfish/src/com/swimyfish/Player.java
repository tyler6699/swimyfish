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

	public Player(float w, float h, float scale_w, float scale_h, int player_id){
		
		if (player_id == 1){
			width = scale_w * 60;
			height = scale_h * 60;
		} else if (player_id == 2) {
			width = scale_w * 40;
			height = scale_h * 40;
		}

		x = (w/2) - (scale_w*200);
		y = (h/2) - (width/2);	
		hitbox = new Rectangle(x,y,width,height);
		player_alive = new Texture(Gdx.files.internal("data/hero/" + player_id + "/player.png"));
		player_hit = new Texture(Gdx.files.internal("data/hero/" + player_id + "/hit.png"));
		trail = new Texture(Gdx.files.internal("data/hero/" + player_id + "/player-trail.png"));
		// texture = new Texture(Gdx.files.internal("data/disco.png"));
	}
	
}