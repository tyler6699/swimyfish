package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {
	float m_width;
	float m_height; 
	float t_width;
	float t_height;
	float w, h, tick;
	Texture tubes, menu;
	public boolean ready;
	
	public Menu(float w, float h, float w_scale, float h_scale){
		this.w = w;
		this.h = h;
		tick = 0;
		ready = false;
		
		GLTexture.setEnforcePotImages(false);
		menu = new Texture(Gdx.files.internal("data/ui/menu_background.png"));
		tubes = new Texture(Gdx.files.internal("data/ui/tubes.png"));
		
		m_width  = w_scale * menu.getWidth();
		m_height = h_scale * menu.getHeight();
		
		
		t_width  = w_scale * tubes.getWidth();
		t_height = h_scale * menu.getHeight();
	}
	
	public void tick(SpriteBatch sb, Player player, float delta){
		sb.draw(menu, w/2 - m_width/2 , h/2 - m_height/2, m_width, m_height);
		sb.draw(tubes, w/2 - m_width/2, h/2 - m_height/2, t_width, t_height);
		
		if (!ready){
			if (tick > 40){
				System.out.println("Ready!");
				ready = true;
			}
			
			tick ++;
		} else {
			tick = 0;
		}

	}

}
