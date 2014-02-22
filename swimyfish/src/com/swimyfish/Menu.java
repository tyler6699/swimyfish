package com.swimyfish;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {
	float w, h, tick;
	public String current_menu;
	Entity background;
	Entity e_score, best, bank, level, level_number, e_shop, new_level;
	Entity current_level;
	Entity lockdown;
	Entity progress_back, progress_bar, percent_sign;
	float full_progress;
	iButton play, play_2, shop, left_arrow, right_arrow, sound, back;
	iButton shop_left, shop_right, buy;
	public int new_level_show;
	
	public boolean ready;
	public String action = ""; // Processed by main game
	Alphabet alphabet;
	ArrayList<Entity> score_array;
	ArrayList<Entity> top_score_array;
	ArrayList<Entity> percent_array;
	ArrayList<Entity> level_array;
	ArrayList<Entity> bank_array;
	ArrayList<iButton> buttons;
	float w_scale, h_scale;
	int number_of_levels;
	
	// Background coords
	float bx, by, bxx, byy;
	float score_y, score_x, t_score_y, w_pad, h_pad;
	
	public Menu(Device device, int number_of_levels){
		// menu or shop
		current_menu = "MAIN";
		this.w_scale = device.w_scale;
		this.h_scale = device.h_scale;
		this.w = device.w;
		this.h = device.h;
		this.number_of_levels = number_of_levels;
		tick = 0;
		ready = false;
		alphabet = new Alphabet();
		
		// SCORES
		score_array = new ArrayList<Entity>();	
		score_array = alphabet.get_number("0");
		
		top_score_array = new ArrayList<Entity>();
		top_score_array = alphabet.get_number("0");
		
		percent_array = new ArrayList<Entity>();
		percent_array = alphabet.get_number("0");
		
		// BANK
		bank_array = new ArrayList<Entity>();	
		bank_array = alphabet.get_number("0");
				
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
		
		new_level = new Entity();
		new_level.texture = new Texture(Gdx.files.internal("data/ui/new_level.png"));
		new_level.w = w_scale * new_level.texture.getWidth();
		new_level.h = h_scale * new_level.texture.getHeight();
		new_level.x = w/2 - new_level.w/2;
		new_level.y = h/2 - new_level.h/2;
				
		e_score = new Entity();
		e_score.texture = new Texture(Gdx.files.internal("data/text/score.png"));
		e_score.w = w_scale * (e_score.texture.getWidth()*.85f);
		e_score.h = h_scale * (e_score.texture.getHeight()*.85f);
		e_score.x = bxx - (e_score.w + w_pad);
		e_score.y = byy - (e_score.h + h_pad);
		
		bank = new Entity();
		bank.texture = new Texture(Gdx.files.internal("data/text/bank.png"));
		bank.w = e_score.w;
		bank.h = e_score.h;
		bank.x = e_score.x;
		bank.y = e_score.y;

		score_y = e_score.y - e_score.h; 
		score_x	= e_score.x + e_score.w; 
		
		level = new Entity();
		level.texture = new Texture(Gdx.files.internal("data/text/level.png"));
		level.w = w_scale * level.texture.getWidth();
		level.h = h_scale * level.texture.getHeight();
		level.x = bx + w_pad;
		level.y = byy - (level.h + h_pad);
		
		e_shop = new Entity();
		e_shop.texture = new Texture(Gdx.files.internal("data/text/shop.png"));
		e_shop.w = w_scale * e_shop.texture.getWidth();
		e_shop.h = h_scale * e_shop.texture.getHeight();
		e_shop.x = level.x;
		e_shop.y = level.y;
		
		level_number = new Entity();
		level_number.texture =  alphabet.get_number_texture(1);
		level_number.w = w_scale * level_number.texture.getWidth();
		level_number.h = h_scale * level_number.texture.getHeight();
		level_number.x = level.x + level.w + w_pad;
		level_number.y = level.y;
		
		best = new Entity();
		best.texture = new Texture(Gdx.files.internal("data/text/best.png"));
		best.w = w_scale * (best.texture.getWidth()*.85f);
		best.h = h_scale * (best.texture.getHeight()*.85f);
		best.x = e_score.x;
		best.y = score_y - best.h - h_pad;
		
		t_score_y = best.y - best.h; 
				
		// BUTTONS
		buttons = new ArrayList<iButton>();
				
		left_arrow = new iButton("MAIN", 0, 0, "LEFT_ARROW", new Texture(Gdx.files.internal("data/ui/left_arrow.png")), w_scale, h_scale,true);
		left_arrow.x = background.x + w_pad;
		left_arrow.y = (background.y + (background.h/2)) - (left_arrow.h/2);
		left_arrow.set_hitbox();
		buttons.add(left_arrow);
		
		right_arrow = new iButton("MAIN", 0, 0, "RIGHT_ARROW", new Texture(Gdx.files.internal("data/ui/right_arrow.png")), w_scale, h_scale,true);
		right_arrow.x = left_arrow.x + (w_scale * 600);
		right_arrow.y = left_arrow.y;
		right_arrow.set_hitbox();
		buttons.add(right_arrow);
		
		shop_left = new iButton("SHOP", 0, 0, "LEFT_ARROW", left_arrow.texture, w_scale, h_scale,true);
		shop_left.x = left_arrow.x;
		shop_left.y = left_arrow.y;
		shop_left.set_hitbox();
		buttons.add(shop_left);
		
		shop_right = new iButton("SHOP", 0, 0, "RIGHT_ARROW", right_arrow.texture, w_scale, h_scale,true);
		shop_right.x = shop_left.x + (w_scale * 400);
		shop_right.y = shop_left.y;
		shop_right.set_hitbox();
		buttons.add(shop_right);
		
		buy = new iButton("SHOP", 0, 0, "BUY", new Texture(Gdx.files.internal("data/ui/200.png")), w_scale, h_scale,true);
		buy.x = w_scale*252;
		buy.y = h_scale*320;
		buy.set_hitbox();
		buttons.add(buy);
			
		// NOT NEEDED BUT USING COORDS
		play = new iButton("MAIN", 0, 0, "PLAY", new Texture(Gdx.files.internal("data/ui/play.png")), w_scale, h_scale,true);
		play.x = ((left_arrow.x + left_arrow.w + right_arrow.x)/2) - (play.w/2);
		play.y = left_arrow.y - ((play.h - left_arrow.h)/2);
		// ^^ REMOVE
		
		play_2 = new iButton("MAIN", 0, 0, "PLAY", new Texture(Gdx.files.internal("data/ui/play.png")), w_scale, h_scale,true);
		play_2.x = bxx - play_2.w - w_pad;
		play_2.y = by + h_pad;
		play_2.set_hitbox();
		buttons.add(play_2);
		
		shop = new iButton("MAIN", 0, 0, "SHOP", new Texture(Gdx.files.internal("data/ui/shop.png")), w_scale, h_scale,true);
		shop.x = bxx - play_2.w - w_pad;
		shop.y = play_2.y + play_2.h + h_pad;
		shop.set_hitbox();
		buttons.add(shop);
				
		// SOUND ON
		sound = new iButton("BOTH", 0, 0, "SOUND", new Texture(Gdx.files.internal("data/ui/sound_on.png")), w_scale, h_scale,true);
		sound.alt_texture = new Texture(Gdx.files.internal("data/ui/sound_off.png"));
		sound.x = w - sound.w;
		sound.y = h - sound.h;
		sound.set_hitbox();
		buttons.add(sound);
		
		back = new iButton("SHOP", 0, 0, "BACK", new Texture(Gdx.files.internal("data/ui/back.png")), w_scale, h_scale,true);
		back.x = bxx - back.w - w_pad;
		back.y = by + h_pad;
		back.set_hitbox();
		buttons.add(back);
						
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
		progress_bar.x = progress_back.x + (w_scale*7);
		progress_bar.y = progress_back.y + (h_scale*18);
		
		percent_sign = new Entity();
		percent_sign.texture = new Texture(Gdx.files.internal("data/numbers/percent.png"));
		percent_sign.y = progress_bar.y - h_scale * 3;
		percent_sign.w = w_scale * percent_sign.texture.getWidth();
		percent_sign.x = progress_back.x + (progress_back.w/2) + (1.5f * percent_sign.w);
		percent_sign.h = h_scale * percent_sign.texture.getHeight();
				
		full_progress = progress_back.w - (h_scale*21);
	}
	
	public void tick(Device device){
		if (!ready){
			ready = tick > 40 ? true:false;
			tick ++;
		} else {
			tick = 0;
		}
		
		if (!device.checked_click){
			for (iButton btn:buttons){
				if(btn.active){
					if (device.clicked_at.overlaps(btn.hitbox) && (btn.menu.equals(current_menu) || btn.menu.equals("BOTH") )){
						action = btn.target;					
						device.checked_click = true;
						break;
					}
				}
			}
			device.checked_click = true;
		}
	}
	
	public void playing_tick(Device device, boolean end_click_checks){
		if (device.checked_click == false){
			if (device.clicked_at.overlaps(sound.hitbox)){
				action = sound.target;		
			}
		}
		
		if (end_click_checks){
			device.checked_click = true;
		}
	}
	
	public void update_score(int score, int top_score, int bank){
		score_array = alphabet.get_number(Integer.toString(score));
		top_score_array = alphabet.get_number(Integer.toString(top_score));
		bank_array = alphabet.get_number(Integer.toString(bank));
	}
	
	public void update_bank(int bank){
		bank_array = alphabet.get_number(Integer.toString(bank));
	}
	
	public void tick(SpriteBatch sb, Player player, float delta, int score, Level ls){
		if (current_menu.equals("MAIN")){
			render_main_menu(sb, player, delta, score, ls);
		} else {
			render_shop_menu(sb, player, delta, score, ls);
		}
	}
	
	void render_main_menu(SpriteBatch sb, Player player, float delta, int score, Level ls){
		level_number.texture = alphabet.get_number_texture(ls.level_id);
		sb.draw(background.texture, background.x, background.y, background.w, background.h);
		sb.draw(level.texture, level.x, level.y, level.w, level.h);	
		sb.draw(level_number.texture, level_number.x, level_number.y, level_number.w, level_number.h);	
		
		//SCORE
		sb.draw(e_score.texture, e_score.x, e_score.y, e_score.w, e_score.h);		
		int i = 1;
		for(Entity e : score_array){
			sb.draw(e.texture, score_x - (i*(e.w*.85f)), score_y , w_scale * (e.w*.85f), h_scale * (e.h*.85f));
			i ++;
		}
		
		// TOP SCORE
		sb.draw(best.texture, best.x, best.y, best.w, best.h);
		i = 1;
		for(Entity e : top_score_array){
			sb.draw(e.texture, score_x - (i*(e.w*.85f)), t_score_y, w_scale * (e.w*.85f), h_scale * (e.h*.85f));
			i ++;
		}
		
		// Progress (Cannot be more than 1)
		float percent = (float) ls.progress/(float) ls.points_needed;
		percent = percent >= 1 ? 1 : percent;

		sb.draw(current_level.texture, current_level.x, current_level.y, current_level.w, current_level.h);
		sb.draw(progress_back.texture, progress_back.x, progress_back.y, progress_back.w, progress_back.h);		
		sb.draw(progress_bar.texture, progress_bar.x, progress_bar.y, percent*full_progress, progress_bar.h);
		
		// PROGRESS PERCENTAGE
		percent_array.clear();
		percent_array = alphabet.get_number(Float.toString(percent*100));
		float start_x = progress_back.x + (progress_back.w/2);

		if (percent_array.size() == 1){
			start_x -= percent_sign.w;
		} else if (percent_array.size() == 2){
			start_x -= percent_sign.w/2;
		}
		
		i = 0;
		for(Entity e : percent_array){
			sb.draw(e.alt_texture, start_x - (i * (w_scale * e.alt_w)), progress_bar.y - h_scale*3, w_scale * e.alt_w, h_scale * e.alt_h);
			i ++;
		}
		
		// %
		sb.draw(percent_sign.texture, start_x + percent_sign.w , percent_sign.y, percent_sign.w, percent_sign.h);
				
		// BUTTONS
		if (!ls.locked){
			//sb.draw(play.texture, play.x, play.y, play.w, play.h);
			sb.draw(play_2.texture, play_2.x, play_2.y, play_2.w, play_2.h);
		} else{
			sb.draw(lockdown.texture, lockdown.x, lockdown.y, lockdown.w, lockdown.h);
		}
		
		sb.draw(shop.texture, shop.x, shop.y, shop.w, shop.h);
		sb.draw(left_arrow.texture, left_arrow.x, left_arrow.y, left_arrow.w, left_arrow.h);
		sb.draw(right_arrow.texture, right_arrow.x, right_arrow.y, right_arrow.w, right_arrow.h);
		
		// NEW LEVEL?
		if(new_level_show > 0){
			sb.draw(new_level.texture, new_level.x, new_level.y, new_level.w, new_level.h);
			new_level_show --;
		}
	}
	
	void render_shop_menu(SpriteBatch sb, Player player, float delta, int score, Level ls){
		sb.draw(background.texture, background.x, background.y, background.w, background.h);
		sb.draw(e_shop.texture, e_shop.x, e_shop.y, e_shop.w, e_shop.h);	
		sb.draw(back.texture, back.x, back.y, back.w, back.h);
		
		//SCORE
		sb.draw(bank.texture, bank.x, bank.y, bank.w, bank.h);		
		int i = 1;
		for(Entity e : bank_array){
			sb.draw(e.texture, score_x - (i*(e.w*.85f)), score_y , w_scale * (e.w*.85f), h_scale * (e.h*.85f));
			i ++;
		}
		
		// ARROWS FOR CHAR
		sb.draw(shop_left.texture, shop_left.x, shop_left.y, shop_left.w, shop_left.h);
		sb.draw(shop_right.texture, shop_right.x, shop_right.y, shop_right.w, shop_right.h);	
	}
	
	public void tick(SpriteBatch sb, boolean sound_on, boolean menu_screen){
		float s = menu_screen == false ? 1 : 0.5f; 
		float y = menu_screen == false ? 0 : .4f * sound.h;
		
		if (sound_on){
			sb.draw(sound.texture, sound.x + y, sound.y + y, s*sound.w, s*sound.h);
		} else {
			sb.draw(sound.alt_texture, sound.x + y, sound.y + y, s*sound.w, s*sound.h);
		}
	}

	public void add_heart_buttons(Game game) {
		int hp = game.max_hp;
		int i = 1;
		for (Entity heart: game.hearts){	
			iButton button = new iButton("SHOP", 0, 0, "HEART", new Texture(Gdx.files.internal("data/ui/200.png")), w_scale, h_scale, true);	
			button.x = w_scale*100+(i*64);
			button.y = h_scale*200;
			button.w = 64;
			button.h = 64;
			button.id = i;
			button.set_hitbox();
			if(i <= hp){
				button.active = false;
			}
			buttons.add(button);
			i++;
		}   
    }
	
	public void update_heart_buttons(Game game){
		for (iButton btn : buttons){
			if (btn.target.equals("HEART")){
				if(btn.id <= game.max_hp){
					btn.active = false;
				} else {
					btn.active = true;
				}
			}
		}
	}
}