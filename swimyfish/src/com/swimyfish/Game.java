package com.swimyfish;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	public boolean sound;
	public boolean trail;
	public boolean complete;
	public boolean hit;         // If true end game
	public boolean started;
	public boolean up;
	
	public int number_of_levels;
	public int level_id;
	public int tick;
	public int trail_length;
	public int jump_id;			// PLAY 1 or 2 SOUND
	public int score; 
	public int top_score;
	public int bank;
	public int min;				// MIN MOVE DST BLOCKER
	
	public float max;			// MAX MOVE DST BLOCKER
	public float hole;
	public float gap;
	public float delta;
	public float fly_time;      // > 0 = moving up
	public float max_fly_time;  // Amount of time player raises and gravity disabled 
	public float gravity;       // Gravity
	public float max_gravity;   // Gravity
	public float min_gravity;   // Gravity
	public float drop_gravity;  // Gravity
	public float fly_up;        // Increase Y by amount per tick
	public float re_jump_time;  //
	public float grace_period;  //
	public float max_grace;     //
	public float glide;         //
	public float scroll_speed;

	public Level current_level;
	
	public ArrayList<Level> levels = new ArrayList<Level>();;
	public ArrayList<Entity> plotter = new ArrayList<Entity>();
	public ArrayList<Blocker> object_array = new ArrayList<Blocker>();
	
	public Game(Device device, Player player){		
		number_of_levels = 4;
		started 		 = false;
		trail 			 = true;
		jump_id 		 = 1;
		level_id 		 = 1;
		trail_length	 = 50; 
		
		Level lvl;		
		for (int i = 1; i <= number_of_levels; i++){
			lvl = new Level(i, device, player);
			lvl.level_id = i;
			lvl.top_score = 0;
			lvl.progress = 0;
			lvl.points_needed = i*100;
			if (i == 1){
				lvl.locked = false;
			} else {
				lvl.locked = true;
			}	
			levels.add(lvl);
		}
		
		//SET CURRENT LEVEL
		current_level = levels.get(0);
		
		// MENU
		top_score = current_level.top_score;
		
		// GAME CONFIG
		hit           = true;
		max_grace	  = 1;
		grace_period  = 0;
		re_jump_time  = 3;
		fly_time      = 0;
		max_fly_time  = 25;
		min_gravity   = 0;
		max_gravity   = device.h_scale*20;
		drop_gravity  = device.h_scale*.5f;
		gravity       = min_gravity;
		fly_up        = device.h_scale*5;
		glide         = device.h_scale*3;
		scroll_speed  = device.w_scale * current_level.scene.scroll_speed;
				
		// BLOCKERS
		gap = device.w/3; // Horizontal distance between blockers
	}

	public void reset(Device device, Menu menu, Player player){
		started = false;
		menu.ready = false;
		menu.update_score(0, top_score, bank);

		plotter.clear();
		score = 0;
		scroll_speed = device.w_scale * current_level.scene.scroll_speed;
		gap = current_level.scene.gap;
		fly_time = max_fly_time;
		grace_period  = max_grace;
		
		// Player
		// FIX
		player.y = (device.h/2);
		
		// Game settings
		hit          = false;
		fly_time     = 0;
		
		// Columns
		Blocker box;
		box = new Blocker(device, 1, current_level.scene);
		max = (int) box.low.h;
		min = (int) 0;
				
		// Obstacles
		object_array.clear();
		
		float rand_height;
		hole =  device.h/3f;
		
		for (int i = 0; i < 4; i++){
			// TODO CLEAN UP
			box = new Blocker(device, i+1, current_level.scene);
			box.low.x = (i * gap) + device.w;
			box.high.x = box.low.x;
						
			if (i > 1){
				min = (int) (box.high.y-(device.h_scale*50));
				rand_height = random_height( (int) (box.high.y-(device.h_scale*50)),  (int) (box.high.y+(device.h_scale*50)));
				box.high.y = rand_height;
				box.low.y = box.high.y - box.low.h - hole;;
			}
						
			box.set_hitboxes();
			object_array.add(box);	
		}		
	}
	
	public void level_up(){
		level_id += 1;
		current_level = levels.get(level_id-1);
		top_score = current_level.top_score;
	}
	
	public void level_down(){
		level_id -= 1;
		current_level = levels.get(level_id-1);
		top_score = current_level.top_score;
	}
	
	public void level_first(){
		level_id = 1;
		current_level = levels.get(level_id-1);
		top_score = current_level.top_score;
	}
	
	public void level_last(){
		level_id = number_of_levels;
		current_level = levels.get(level_id-1);
		top_score = current_level.top_score;
	}
	
	public int random_height(int min, int max){
		min = min > 0 ? min : 0;
		max = max > 0 ? max : 1;
		
		Random r = new Random(); 
		int number = r.nextInt(max-min) + min;
		    
		return number;
	}

	public void logic(Device device, Player player, HiFi hifi, Menu menu) {	
		float rand_height;
		Blocker last = new Blocker(device, 1, current_level.scene);
				
		// Obstacles
		for (Blocker box : object_array){		
			if (box.low.x < -(box.low.w*2)){
				// Check box in front to use as a guide for the new Y				
				for (Blocker b : object_array){
					if (box.id == 1 && b.id == 4){
						last = b;
					} else if (box.id == 2 && b.id == 1) {
						last = b;
					} else if (box.id == 3 && b.id == 2) {
						last = b;
					} else if (box.id == 4 && b.id == 3) {
						last = b;
					}
				}
								
				// UP OR DOWN?
				up = go_up(50);
				min = 0;
				max = device.h_scale*(device.h/4f);
				
				if (up){
					if (last.high.y + max >= device.h ){max = device.h - last.high.y - (device.h_scale*30);}
					rand_height = random_height(min,(int) max);
					box.high.y = last.high.y + rand_height;
				} else { // DOWN	
					if ((last.high.y + last.high.h - (2*max)) > device.h ){
						rand_height = random_height(min,(int) max);
						box.high.y = last.high.y - rand_height;
					}
				}
				
				box.high.x += 4 * gap;
				
				// LOW
				box.low.x += 4 * gap;	
				box.low.y = box.high.y - box.low.h - (hole*.9f); // CLOSE HOLE HERE		 
				box.set_hitboxes();
				box.scored = false;
			} else {
				box.low.x -= scroll_speed;	
				box.high.x -= scroll_speed;
				box.update_hitboxes(scroll_speed);
			}
		}	
		
		// CLOUDS 
		for (Entity e: current_level.scene.eclouds){
			if (e.x < -(e.w)){
				e.x += current_level.scene.eclouds.size() * e.w - (scroll_speed/2);
			} else {
				e.x -= scroll_speed/2;
			}
		}
		
		for (Entity e: current_level.scene.floor_toppers){
			if (e.x < -(e.w)){
				e.x += current_level.scene.floor_toppers.size() * e.w - (scroll_speed*1.5);
			} else {
				e.x -= scroll_speed*1.5;
			}
		}
				
		// Player
		if (fly_time > 0){
			fly_time -= 1;
			if ( not_too_high(device.h, player) ){
				update_gravity(false);
				player.y += fly_up;	
			}
		} else if (grace_period > 0){
			grace_period -= 1;
			update_gravity(false);
		} else {
			if ( not_too_low(player) ){
				update_gravity(true);
				player.y -= gravity;
			}
		}	
			
		player.hitbox.setPosition(player.x, player.y);
			
		for (Blocker box : object_array){
			if (!hit){
				check_collision(box, hifi, player, menu);
			}
		}	
		
		// TRAIL
		if (trail){
			tick ++;
			Entity tmp = new Entity();
			tmp.x = player.x;
			tmp.y = player.y;
			tmp.w = player.width;
			tmp.h = player.height;
			tmp.texture = player.trail;
			plotter.add(tmp);
		}
		
		if (plotter.size() > trail_length){
			plotter.remove(0);
		}
	}
	
	private void update_gravity(boolean falling){
		if (falling){
			if (gravity < max_gravity){
				gravity += drop_gravity;	
			}
			
		} else {
			gravity = min_gravity;
		}
	}

	private void check_collision(Blocker box, HiFi hifi, Player player, Menu menu) {		
		if (box.scorebox.overlaps(player.hitbox) && !box.scored){
			box.scored = true;
			score ++;
			hifi.play_collect(sound);
		} else {
			if (box.high.hitbox.overlaps(player.hitbox) || box.low.hitbox.overlaps(player.hitbox)){
				hit = true;
				started = false;
				
				hifi.play_death(sound);
				// BANK
				bank += score;
				
				// UPDATE PROGRESS
				if(current_level.progress <= current_level.points_needed){
					if (current_level.progress + score > current_level.points_needed){
						current_level.progress = current_level.points_needed;
					} else {
						current_level.progress += score;
					}
				}
				
				// CHECK HIGH SCORES
				if (score > current_level.top_score){
					current_level.top_score = score;
					top_score = score;
				}
				
				// UNLOCK LEVELS?
				if (current_level.progress >= current_level.points_needed && current_level.level_id < number_of_levels){
					Level next_level = levels.get(current_level.level_id);
					if (next_level.locked == true){
						next_level.locked = false;
						hifi.play_unlock(sound);
					}
				} else if(!complete) {
					// todo Show Credits
					complete = true;
					hifi.play_unlock(sound);
				}
			}
		}
		
		//UPDATE SCORES
		menu.update_score(score, top_score, bank);
	}
	
	private boolean not_too_high(float h, Player player){
		return player.y + fly_up + player.height < h + player.height;
	}
		
	private boolean not_too_low(Player player){
		return player.y - gravity > -player.height;
	}  
	
	public boolean go_up(int percentage){
		Random r = new Random(); 
		int chance = r.nextInt(100);
		    
		if(percentage > chance){
			return true;
		}else{
			return false;
		}
	}

	public boolean playing_game(){
		return started && !hit;
	}

	public boolean main_menu(){
		return !started && hit;
	}
	
	public boolean tap_to_start(){
		return !started && !hit;
	}

}