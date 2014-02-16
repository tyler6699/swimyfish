package com.swimyfish;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swimyfish.FlappyBox.TouchInfo;

public class Menu {
	float w, h, tick;
	Entity background;
	Entity e_score, best, bank, level, level_number;
	Entity current_level;
	Entity lockdown;
	Entity progress_back, progress_bar;
	float full_progress;
	iButton play, left_arrow, right_arrow;
	
	public boolean ready;
	public String action = ""; // Processed by main game
	Alphabet alphabet;
	ArrayList<Entity> score_array;
	ArrayList<Entity> top_score_array;
	ArrayList<Entity> level_array;
	ArrayList<iButton> buttons;
	float w_scale, h_scale;
	int number_of_levels;
	
	// Background coords
	float bx, by, bxx, byy;
	float score_y, score_x, t_score_y, w_pad, h_pad;
	
	public Menu(float w, float h, float w_scale, float h_scale, int number_of_levels){
		this.w_scale = w_scale;
		this.h_scale = h_scale;
		this.w = w;
		this.h = h;
		this.number_of_levels = number_of_levels;
		tick = 0;
		ready = false;
		alphabet = new Alphabet();
		full_progress = h_scale*438;
		
		// SCORES
		score_array = new ArrayList<Entity>();	
		score_array = alphabet.get_number("0");
		
		top_score_array = new ArrayList<Entity>();	
		top_score_array = alphabet.get_number("0");
		
		w_pad = w_scale * 30;
		h_pad = h_scale * 30;
		
		background = new Entity();
		background.texture = new Texture(Gdx.files.internal("data/ui/menu_background.png"));
		background.w = w_scale * background.texture.getWidth();
		background.h = h_scale * background.texture.getHeight();
		background.x = w/2 - background.w/2;
		background.y = h/2 - background.h/2;
		
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
		score_x	= e_score.x + e_score.w; 
		
		level = new Entity();
		level.texture = new Texture(Gdx.files.internal("data/text/level.png"));
		level.w = w_scale * level.texture.getWidth();
		level.h = h_scale * level.texture.getHeight();
		level.x = bx + w_pad;
		level.y = byy - (level.h + h_pad);
		
		level_number = new Entity();
		level_number.texture =  alphabet.get_number_texture(1);
		level_number.w = w_scale * level_number.texture.getWidth();
		level_number.h = h_scale * level_number.texture.getHeight();
		level_number.x = level.x + level.w + w_pad;
		level_number.y = level.y;
		
		best = new Entity();
		best.texture = new Texture(Gdx.files.internal("data/text/best.png"));
		best.w = w_scale * best.texture.getWidth();
		best.h = h_scale * best.texture.getHeight();
		best.x = e_score.x;
		best.y = score_y - best.h - h_pad;
		
		t_score_y = best.y - best.h; 
				
		// BUTTONS
		buttons = new ArrayList<iButton>();
				
		left_arrow = new iButton(0,0, "LEFT_ARROW", "LEFT_ARROW", new Texture(Gdx.files.internal("data/ui/left_arrow.png")), w_scale, h_scale);
		left_arrow.x = background.x + w_pad;
		left_arrow.y = (background.y + (background.h/2)) - (left_arrow.h/2);
		left_arrow.set_hitbox();
		buttons.add(left_arrow);
		
		right_arrow = new iButton(0,0, "RIGHT_ARROW", "RIGHT_ARROW", new Texture(Gdx.files.internal("data/ui/right_arrow.png")), w_scale, h_scale);
		right_arrow.x = left_arrow.x + (w_scale * 600);
		right_arrow.y = left_arrow.y;
		right_arrow.set_hitbox();
		buttons.add(right_arrow);
		
		play = new iButton(0,0, "PLAY", "PLAY", new Texture(Gdx.files.internal("data/ui/play.png")), w_scale, h_scale);
		play.x = ((left_arrow.x + left_arrow.w + right_arrow.x)/2) - (play.w/2) ;
		play.y = left_arrow.y - ((play.h - left_arrow.h)/2);
		play.set_hitbox();
		buttons.add(play);
				
		// IMAGES FOR LEVELS
		level_array = new ArrayList<Entity>();
		for (int i = 1; i <= number_of_levels; i++){
			Entity level = new Entity();
			level.texture =  new Texture(Gdx.files.internal("data/ui/level_" + i + ".png"));
			level.w = w_scale * level.texture.getWidth();
			level.h = h_scale * level.texture.getHeight();
			level.x = play.x - (level.w/4);
			level.y = play.y - (level.h/3);	
			level_array.add(level);
		}
		current_level = level_array.get(0);
		
		// For locked levels
		lockdown = new Entity();
		lockdown.texture = new Texture(Gdx.files.internal("data/ui/lockdown.png"));
		lockdown.w = w_scale * lockdown.texture.getWidth();
		lockdown.h = h_scale * lockdown.texture.getHeight();
		lockdown.x = current_level.x;
		lockdown.y = current_level.y;
		
		// Progress Bar Background
		progress_back = new Entity();
		progress_back.texture = new Texture(Gdx.files.internal("data/ui/pro_back.png"));
		progress_back.w = w_scale * progress_back.texture.getWidth();
		progress_back.h = h_scale * progress_back.texture.getHeight();
		progress_back.x = current_level.x;
		progress_back.y = current_level.y - progress_back.h - (h_pad/8);
		
		progress_bar = new Entity();
		progress_bar.texture = new Texture(Gdx.files.internal("data/ui/pro_bar.png"));
		progress_bar.w = w_scale * progress_bar.texture.getWidth();
		progress_bar.h = h_scale * progress_bar.texture.getHeight();
		progress_bar.x = progress_back.x + (w_scale*6);
		progress_bar.y = progress_back.y + (h_scale*16);
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
			for (iButton btn:buttons){
				if (touch.clicked_at.overlaps(btn.hitbox)){
					action = btn.target;					
					touch.checked_click = true;
				}
			}
		}
	}
	
	public void update_score(int score, int top_score){
		score_array = alphabet.get_number(Integer.toString(score));
		top_score_array = alphabet.get_number(Integer.toString(top_score));
	}
	
	public void tick(SpriteBatch sb, Player player, float delta, int score, LevelScores ls){
		level_number.texture = alphabet.get_number_texture(ls.level_id);
		sb.draw(background.texture, background.x, background.y, background.w, background.h);
		sb.draw(level.texture, level.x, level.y, level.w, level.h);	
		sb.draw(level_number.texture, level_number.x, level_number.y, level_number.w, level_number.h);	
		
		//SCORE
		sb.draw(e_score.texture, e_score.x, e_score.y, e_score.w, e_score.h);		
		int i = 1;
		for(Entity e : score_array){
			sb.draw(e.texture, score_x - (i*e.w), score_y , w_scale * e.w, h_scale * e.h);
			i ++;
		}
		
		// TOP SCORE
		sb.draw(best.texture, best.x, best.y, best.w, best.h);
		i = 1;
		for(Entity e : top_score_array){
			sb.draw(e.texture, score_x - (i*e.w), t_score_y, w_scale * e.w, h_scale * e.h);
			i ++;
		}

		// LEVEL IMAGE
		current_level = level_array.get(ls.level_id-1);
		
		// Progress (Cannot be more than 1)
		float percent = (float) ls.progress/(float) ls.points_needed;
		percent = percent >= 1 ? 1 : percent;

		sb.draw(current_level.texture, current_level.x, current_level.y, current_level.w, current_level.h);
		sb.draw(progress_back.texture, progress_back.x, progress_back.y, progress_back.w, progress_back.h);		
		sb.draw(progress_bar.texture, progress_bar.x, progress_bar.y, percent*full_progress, progress_bar.h);	
				
		// BUTTONS
		if (!ls.locked){
			sb.draw(play.texture, play.x, play.y, play.w, play.h);
		} else{
			sb.draw(lockdown.texture, lockdown.x, lockdown.y, lockdown.w, lockdown.h);
		}
		
		sb.draw(left_arrow.texture, left_arrow.x, left_arrow.y, left_arrow.w, left_arrow.h);
		sb.draw(right_arrow.texture, right_arrow.x, right_arrow.y, right_arrow.w, right_arrow.h);
	}

}