package com.swimyfish;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swimyfish.FlappyBox.TouchInfo;

public class Menu {
	float w, h, tick;
	Entity background;
	Entity e_score, best, bank;
	public boolean ready;
	Alphabet alphabet;
	ArrayList<Entity> score_array;
	ArrayList<Entity> top_score_array;
	ArrayList<iButton> buttons;
	float w_scale, h_scale;
	
	// Background coords
	float bx, by, bxx, byy;
	float score_y, score_x, t_score_y, t_score_x, w_pad, h_pad;
	
	public Menu(float w, float h, float w_scale, float h_scale){
		this.w_scale = w_scale;
		this.h_scale = h_scale;
		this.w = w;
		this.h = h;
		tick = 0;
		ready = false;
		alphabet = new Alphabet();
		
		// SCORES
		score_array = new ArrayList<Entity>();	
		score_array = alphabet.get_number("0");
		
		top_score_array = new ArrayList<Entity>();	
		top_score_array = alphabet.get_number("0");
		
		w_pad = w_scale * 45;
		h_pad = h_scale * 20;
				
		background = new Entity();
		background.texture = new Texture(Gdx.files.internal("data/ui/menu_background.png"));
		background.w = w_scale * background.texture.getWidth();
		background.h = h_scale * background.texture.getHeight();
		background.x = w/2 - background.w/2;
		background.y = h/2 - background.h/2;

		System.out.println(background.x );
		
		bx = background.x;
		by = background.y;
		bxx = background.x + background.w;
		byy = background.y + background.h;
				
		e_score = new Entity();
		e_score.texture = new Texture(Gdx.files.internal("data/text/score.png"));
		e_score.w = w_scale * e_score.texture.getWidth();
		e_score.h = h_scale * e_score.texture.getHeight();
		e_score.x = bxx - (e_score.w + w_pad);
		e_score.y = byy - (e_score.h + h_pad);
		
		score_y = e_score.y - e_score.h; 
		score_x	= e_score.x + e_score.w - w_pad; 
		
		best = new Entity();
		best.texture = new Texture(Gdx.files.internal("data/text/best.png"));
		best.w = w_scale * best.texture.getWidth();
		best.h = h_scale * best.texture.getHeight();
		best.x = bxx - (best.w + w_pad);
		best.y = score_y - best.h - h_pad;
		
		t_score_y = best.y - best.h; 
		t_score_x	= e_score.x + e_score.w - w_pad; 
		
		// BUTTONS
		buttons = new ArrayList<iButton>();
		iButton button;
		button = new iButton(background.x/2,background.y/2, "RETRY", "RETRY", new Texture(Gdx.files.internal("data/ui/tubes.png")));
		
		buttons.add(button);
	}
	
	public void tick(TouchInfo touch){
		if (!ready){
			if (tick > 40){
				ready = true;
			}
			
			tick ++;
		} else {
			tick = 0;
		}
		
		if (!touch.checked_click){
			//System.out.println(touch.clicked_at.x + " " + touch.clicked_at.y + " "  + touch.clicked_at.width + " " + touch.clicked_at.height);
		}
	}
	
	public void update_score(int score, int top_score){
		score_array = alphabet.get_number(Integer.toString(score));
		top_score_array = alphabet.get_number(Integer.toString(top_score));
	}
	
	public void tick(SpriteBatch sb, Player player, float delta, int score){
		sb.draw(background.texture, background.x, background.y, background.w, background.h);
		
		//SCORE
		sb.draw(e_score.texture, e_score.x, e_score.y, e_score.w, e_score.h);		
		int i = 0;
		for(Entity e : score_array){
			sb.draw(e.texture, score_x - (i*e.w), score_y , w_scale * e.w, h_scale * e.h);
			i ++;
		}
		
		// TOP SCORE
		sb.draw(best.texture, best.x, best.y, best.w, best.h);
		i = 0;
		for(Entity e : top_score_array){
			sb.draw(e.texture, score_x - (i*e.w), t_score_y, w_scale * e.w, h_scale * e.h);
			i ++;
		}
	}

}