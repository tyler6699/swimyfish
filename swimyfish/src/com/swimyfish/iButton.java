package com.swimyfish;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class iButton extends Entity {
	String target;
	String menu;
	float fx, fy;
	
	public iButton(String menu, float x, float y, String target, Texture texture, float w_scale, float h_scale){
		super();
		
		this.menu = menu;
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
