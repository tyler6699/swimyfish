package com.swimyfish;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {
	float w, h, tick;
	Entity test_tubes;
	Entity background;
	public boolean ready;
	Alphabet alphabet;
	ArrayList<Texture> letters;
	
	public Menu(float w, float h, float w_scale, float h_scale){
		this.w = w;
		this.h = h;
		tick = 0;
		ready = false;
		alphabet = new Alphabet();
		letters = new ArrayList<Texture>();
		
		background = new Entity();
		background.texture = new Texture(Gdx.files.internal("data/ui/menu_background.png"));
		background.x = w/2 - background.texture.getWidth()/2;
		background.y = h/2 - background.texture.getHeight()/2;
		background.w = w_scale * background.texture.getWidth();
		background.h = h_scale * background.texture.getHeight();
		
		test_tubes = new Entity();
		test_tubes.texture = new Texture(Gdx.files.internal("data/ui/tubes.png"));
		test_tubes.x = w/2 - test_tubes.texture.getWidth()/2;
		test_tubes.y = h/2 - test_tubes.texture.getHeight()/2;
		test_tubes.w = w_scale * test_tubes.texture.getWidth();
		test_tubes.h = h_scale * test_tubes.texture.getHeight();
	}
	
	public void tick(){
		if (!ready){
			if (tick > 40){
				ready = true;
			}
			
			tick ++;
		} else {
			tick = 0;
		}
	}
	
	public void update_score(int score){
		letters = alphabet.get_number(Integer.toString(score));
	}
	
	public void tick(SpriteBatch sb, Player player, float delta, int score){
		letters = alphabet.get_number(Integer.toString(score));
		sb.draw(background.texture, background.x, background.y, background.w, background.h);
		sb.draw(test_tubes.texture, test_tubes.x, test_tubes.y, test_tubes.w, test_tubes.h);
		
		int i = 0;
		for(Texture t:letters){
			sb.draw(t,background.x + i * t.getWidth(), background.y, t.getWidth(), t.getHeight());
			i ++;
		}
	}

}