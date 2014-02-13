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

		
	public Player(float w, float h){
		width = 60;
		height = 60;
		x = (w/2) - 200;
		y = (h/2) - (width/2);	
		hitbox = new Rectangle(x,y,width,height);
		player_alive = new Texture(Gdx.files.internal("data/player.png"));
		player_hit = new Texture(Gdx.files.internal("data/hit.png"));
		trail = new Texture(Gdx.files.internal("data/player-trail.png"));
		// texture = new Texture(Gdx.files.internal("data/disco.png"));
	}
}
