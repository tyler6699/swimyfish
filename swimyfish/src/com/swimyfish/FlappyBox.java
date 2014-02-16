package com.swimyfish;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.swimyfish.Player;

public class FlappyBox implements ApplicationListener, InputProcessor{
	private Preferences prefs;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	// SOUND PLAYER
	private HiFi hifi;
	private int jump_id;
	
	public float v_width, v_height, w_scale, h_scale;
	int level_id, tick = 0,trail_length;
	float w, h, x = 0, y = 0;
	private TouchInfo touch;
	float delta;

	// TRAIL
	boolean trail = true;
	public ArrayList<Entity> plotter = new ArrayList<Entity>();
	
	// MENU
	Menu menu;
	
	// PLAYER ENTITY
	private Player player;
	
	// LEVEL
	private Level level;
	
	// BLOCKERS
	private ArrayList<Blocker> object_array;
	// Gap player has to fit through
	float hole;
	
	// GAME VARS & FLIGHT SETTINGS
	float fly_time;      // > 0 = moving up
	float max_fly_time;  // Amount of time player raises and gravity disabled 
	float gravity;       // Gravity
	float max_gravity;   // Gravity
	float min_gravity;   // Gravity
	float drop_gravity;  // Gravity
	float fly_up;        // Increase Y by amount per tick
	float re_jump_time;  //
	float grace_period;  //
	float max_grace;     //
	float glide;         // 
	boolean hit;         // If true end game
	boolean start; 
	boolean up;
	
	// BANK AND SCORES
	int score, top_score, bank; 
	ArrayList<LevelScores> level_scores;
	LevelScores current_level;	
	int number_of_levels;
	
	// Difficulty / Speed
	float scroll_speed;
	private float gap;
	float max;
	int min;
			
	class TouchInfo {
		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
		public boolean checked_click = true;
		public Rectangle clicked_at = new Rectangle();
	}

	@Override
	public void create(){
		Gdx.input.setInputProcessor(this);
		
		// NUMBER OF LEVELS!!!
		number_of_levels = 2;
		
		//SOUND
		hifi = new HiFi();
		jump_id = 1;
		
		// SCORE ARRAY
		level_scores = new ArrayList<LevelScores>();
		for (int i = 1; i <= number_of_levels; i++){
			current_level = new LevelScores();
			current_level.level_id = i;
			current_level.top_score = 0;
			current_level.progress = 0;
			current_level.points_needed = i*100;
			if (i == 1){
				current_level.locked = false;
			} else {
				current_level.locked = true;
			}
			
			level_scores.add(current_level);
		}
		
		// LEVEL TOGGLE
		level_id = 1;
	
		// TRAIL
		trail_length = 50;
				
		// Load Top Score
		load_prefs();
		
		// Screen width and height
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
				
		// SCALE / RATIO
		v_width = 1196;
		v_height = 786;
		w_scale = w/v_width;
		h_scale = h/v_height;
		
		// MENU
		top_score = level_scores.get(level_id-1).top_score;
		menu = new Menu(w, h, w_scale, h_scale, number_of_levels);
		menu.update_score(score, top_score);
		
		// CAMERA SETUP
	    camera = new OrthographicCamera(2,2);
	    camera.update();
						
		touch = new TouchInfo();
		
		// SB FOR CAMERA
		batch = new SpriteBatch();
		batch.setTransformMatrix(camera.combined);
		
		// NEW PLAYER
		player = new Player(w, h, w_scale, h_scale);
		
		// NEW LEVEL
		level = new Level("level_" + level_id, level_id, w, h, w_scale, h_scale);
		
		// GAME CONFIG
		hit           = true;
		max_grace	  = 1;
		grace_period  = 0;
		re_jump_time  = 3;
		fly_time      = 0;
		max_fly_time  = 25;
		min_gravity   = 0;
		max_gravity   = h_scale*20;
		drop_gravity  = h_scale*.5f;
		gravity       = min_gravity;
		fly_up        = h_scale*5;
		glide         = h_scale*3;
		scroll_speed  = w_scale * level.scroll_speed;
				
		// BLOCKERS
		gap = w/3; // Horizontal distance between blockers
		object_array = new ArrayList<Blocker>();		
	}
	
	private void load_prefs(){
		prefs = Gdx.app.getPreferences("pixel_jump");
		
		for (LevelScores ls : level_scores){
			// TOP SCORE
			if (!prefs.contains("top_score_" + ls.level_id)){
				prefs.putInteger("top_score_" + ls.level_id, ls.top_score);
				top_score = 0;
			} else {
				ls.top_score = prefs.getInteger("top_score_" + ls.level_id);
			}
			
			if (!prefs.contains("locked_" + ls.level_id)){
				prefs.putBoolean("locked_" + ls.level_id, ls.locked);
				if(ls.level_id == 1){
					ls.locked = false;
				} else {
					ls.locked = true;
				}
			} else {
				ls.locked = prefs.getBoolean("locked_" + ls.level_id);
			}
			
			if (!prefs.contains("progress_" + ls.level_id)){
				prefs.putInteger("progress_" + ls.level_id, ls.progress);
				ls.progress = 0;
			} else {
				ls.progress = prefs.getInteger("progress_" + ls.level_id);
			}
			
			if (!prefs.contains("required_" + ls.level_id)){
				prefs.putInteger("required_" + ls.level_id, ls.points_needed);
			} else {
				ls.points_needed = prefs.getInteger("required_" + ls.level_id);
			}
		}
				
		// BANK
		if (!prefs.contains("bank")){
			prefs.putInteger("bank", 0);
			bank = 0;
		} else {
			bank = prefs.getInteger("bank");
		}

		prefs.flush();
	}
	
	private void save_prefs(){
		for (LevelScores ls : level_scores){
			prefs.putInteger("top_score_" + ls.level_id, ls.top_score);
			prefs.putInteger("progress_" + ls.level_id, ls.progress);
			prefs.putBoolean("locked_" + ls.level_id, ls.locked);
		}	
		prefs.putInteger("bank",bank);
		prefs.flush();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	@Override
	public void render(){
		delta = Gdx.graphics.getDeltaTime();
			
		if (!hit){ 
			logic(); 
		} else {
			menu_logic();
		}
		draw();
	}
	
	private void menu_logic() {
		if (!menu.action.equals("")){
			if (menu.action.equals("LEFT_ARROW")){
				if (level_id > 1){
					level_id -= 1;
					top_score = level_scores.get(level_id-1).top_score;
					menu.update_score(score,top_score);
				} else {
					level_id = number_of_levels;
					top_score = level_scores.get(level_id-1).top_score;
					menu.update_score(score,top_score);
				}
				menu.action = "";
			} else if (menu.action.equals("RIGHT_ARROW")){
				if (level_id < number_of_levels){
					level_id += 1;
					top_score = level_scores.get(level_id-1).top_score;
					menu.update_score(score,top_score);
				} else {
					level_id = 1;
					top_score = level_scores.get(level_id-1).top_score;
					menu.update_score(score,top_score);
				}
				menu.action = "";
			} else if (menu.action.equals("PLAY") && menu.ready && !level_scores.get(level_id-1).locked) {
				reset_game();	
				menu.action = "";
			}
		}
	}

	private void logic() {	
		float rand_height;
		Blocker last = new Blocker(w, h, 1, level, w_scale, h_scale);
								
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
				max = h_scale*(h/4f);
				
				if (up){
					if (last.high.y + max >= h ){max = h - last.high.y - (h_scale*30);}
					rand_height = random_height(min,(int) max);
					box.high.y = last.high.y + rand_height;
				} else { // DOWN	
					if ((last.high.y + last.high.h - (2*max)) > h ){
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
		for (Entity e: level.eclouds){
			if (e.x < -(e.w)){
				e.x += level.eclouds.size() * e.w - (scroll_speed/2);
			} else {
				e.x -= scroll_speed/2;
			}
		}
		
		for (Entity e: level.floor_toppers){
			if (e.x < -(e.w)){
				e.x += level.floor_toppers.size() * e.w - (scroll_speed*1.5);
			} else {
				e.x -= scroll_speed*1.5;
			}
		}
				
		// Player
		if (fly_time > 0){
			fly_time -= 1;
			if ( not_too_high() ){
				update_gravity(false);
				player.y += fly_up;	
			}
		} else if (grace_period > 0){
			grace_period -= 1;
			update_gravity(false);
		} else {
			if ( not_too_low() ){
				update_gravity(true);
				player.y -= gravity;
			}
		}	
			
		player.hitbox.setPosition(player.x, player.y);
			
		for (Blocker box : object_array){
			if (!hit){
				check_collision(box);
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

	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		batch.begin();
		// BACKGROUND
		batch.draw(level.esky.texture, level.esky.x, level.esky.y, level.esky.w, level.esky.h);
		
		// CLOUDS
		for (Entity e: level.eclouds){
			batch.draw(e.texture, e.x, e.y, e.w, e.h);
		}
				
		// FLOOR
		batch.draw(level.efloor.texture, level.efloor.x, level.efloor.y, level.efloor.w, level.efloor.h);

		if (trail && plotter.size() > 0){
			for (int i = 0; i < plotter.size(); i++){
				Entity e = plotter.get(i);
				if (!hit){
					e.x -= w_scale*3;
				}
				batch.draw(e.texture, e.x, e.y, e.w - (plotter.size()-i), e.h - (plotter.size()-i));
			}
		}		
		
		// PLAYER
		if (!hit){
			batch.draw(player.player_alive, player.x, player.y, player.width, player.height);
		} else {	
			batch.draw(player.player_hit, player.x, player.y, player.width, player.height);
		}
				
		// OBSTACLES
		for (Blocker box : object_array){
			batch.draw(box.high.texture, box.high.x, box.high.y, box.high.w, box.high.h);	
			batch.draw(box.low.texture, box.low.x, box.low.y, box.low.w, box.low.h);
			if (level.show_blocker_lower){
				batch.draw(level.blocker_floor, box.low.x, box.low.y, box.low.w, level.floor_h);	
			}
			
		}
		
		// RAYS
		batch.draw(level.erays_1.texture, level.erays_1.x, level.erays_1.y, level.erays_1.w, level.erays_1.h);
		batch.draw(level.erays_2.texture, level.erays_2.x, level.erays_2.y, level.erays_2.w, level.erays_2.h);
		
		// FLOOR TOPPER
		for (Entity e: level.floor_toppers){
			batch.draw(e.texture, e.x, e.y, e.w, e.h);
		}
		
		if (hit){
			menu.tick(touch);
			menu.tick(batch,player, delta, score, level_scores.get(level_id-1));
		}
		
		//SCORE
		if (!hit){
			int i = 0;
			float fw = menu.score_array.size(); 
			for(Entity e: menu.score_array){
				batch.draw(e.texture, (w/2) - i * (w_scale*e.texture.getWidth()) - (fw * (w_scale*e.texture.getWidth()/2)), h - (h_scale*e.texture.getHeight()) - h_scale*10, w_scale*e.texture.getWidth(), h_scale*e.texture.getHeight());
				i ++;
			}
		}
		batch.end();
	}

	private void reset_game() {
		menu.ready = false;
		menu.update_score(0, top_score);
		
		level = new Level("level_" + level_id,level_id, w, h, w_scale, h_scale);
		plotter.clear();
		score = 0;
		scroll_speed  = w_scale * level.scroll_speed;
		gap = level.gap;
		fly_time = max_fly_time;
		grace_period  = max_grace;
		
		// Player
		// FIX
		player.y = (h/2) - (h/100) * 16.52f;
		
		// Game settings
		hit          = false;
		fly_time     = 0;
		
		// Columns
		Blocker box;
		box = new Blocker(w, h, 1, level, w_scale, h_scale);
		max = (int) box.low.h;
		min = (int) 0;
				
		// Obstacles
		object_array.clear();
		
		float rand_height;
		hole =  h/3f;
		
		for (int i = 0; i < 4; i++){
			// TODO CLEAN UP
			box = new Blocker(w, h, i+1, level, w_scale, h_scale);
			box.low.x = (i * gap) + w;
			box.high.x = box.low.x;
						
			if (i > 1){
				min = (int) (box.high.y-(h_scale*50));
				rand_height = random_height( (int) (box.high.y-(h_scale*50)),  (int) (box.high.y+(h_scale*50)));
				box.high.y = rand_height;
				box.low.y = box.high.y - box.low.h - hole;;
			}
						
			box.set_hitboxes();
			object_array.add(box);	
		}		
	}
		
	private void check_collision(Blocker box) {
		current_level = level_scores.get(level_id-1);
		
		if (box.scorebox.overlaps(player.hitbox) && !box.scored){
			box.scored = true;
			score ++;
			hifi.play_collect();
		} else {
			if (box.high.hitbox.overlaps(player.hitbox) || box.low.hitbox.overlaps(player.hitbox)){
				hit = true;
				hifi.play_death();
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
				if (current_level.progress >= current_level.points_needed && level_scores.get(level_id).locked == true){
					if(current_level.level_id < number_of_levels){
						level_scores.get(level_id).locked = false;
						hifi.play_unlock();
					}
				}
				
				save_prefs();
			}
		}
		
		//UPDATE SCORES
		menu.update_score(score, top_score);
	}
	
	private boolean not_too_high(){
		return player.y + fly_up + player.height < h + player.height;
	}
	
	private boolean not_too_low(){
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
	
	public int random_height(int min, int max){
		min = min > 0 ? min : 0;
		max = max > 0 ? max : 1;
		
		Random r = new Random(); 
		int number = r.nextInt(max-min) + min;
		    
		return number;
	}
	
	@Override
	public void resize(int width, int height){}

	@Override
	public void pause(){}
	
	@Override
	public void resume(){}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touch.touchX = screenX;
		touch.touchY = h-screenY;
		touch.touched = true;
				
		if (!hit){ 
			if (fly_time <= re_jump_time){
				hifi.play_jump(jump_id);
				jump_id = jump_id == 1 ? 2 : 1;
				
				fly_time = max_fly_time;
				grace_period  = max_grace;
			}	
		} else {
			touch.checked_click = false;
			touch.clicked_at.set(screenX, h-screenY, w_scale+5, h_scale*5);
		}
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(pointer <= 2){
			touch.touchX = 0;
			touch.touchY = 0;
			touch.touched = false;
		}
		return true;
	}
		
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}