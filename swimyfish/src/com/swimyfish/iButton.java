package com.swimyfish;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class iButton extends Entity {
	String target;
	float fx, fy;
	
	public iButton(float x, float y, String target, String name, Texture texture, float w_scale, float h_scale){
		super();
		
		this.name = name;
		this.target = target;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.w = w_scale * texture.getWidth();
		this.h = h_scale * texture.getHeight();
	}
	
	public void set_hitbox(){
		this.hitbox = new Rectangle(x,y,w,h);
	}
}
