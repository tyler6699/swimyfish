package com.swimyfish;

import com.badlogic.gdx.graphics.Texture;

public class iButton extends Entity {
	String target;
	float fx, fy;
	
	public iButton(float x, float y, String target, String name, Texture texture){
		super();
		
		this.name = name;
		this.target = target;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.w = texture.getWidth();
		this.h = texture.getHeight();
		
		//float font_height = font.getBounds(name).height/2;
		//fx = (x + (width/2)) - (font.getBounds(name).width/2);
		//fy = y + (height/2) + font_height;
	}
}
