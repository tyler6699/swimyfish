package com.swimyfish;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swimyfish.Player;

public class FlappyBox implements ApplicationListener, InputProcessor{
	private Preferences prefs;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SpriteBatch screen;
	public float v_width, v_height, w_scale, h_scale;
	
	// DEBUG
	int level_id;
	boolean trail = true;
	int tick = 0;
	public ArrayList<Entity> plotter = new ArrayList<Entity>();
	int trail_length;
	// DEBUG
	
	private TouchInfo touched;
	private BitmapFont font;
	float w, h, x = 0, y = 0;
	
	// Menu
	private Texture menu, tubes;
	
	// Player Entity
	private Player player;
	
	//Level
	private Level level;
	
	// Obstacles
	private ArrayList<Blocker> object_array;
	float hole;
	
	// Game Vars
	float fly_time;      // > 0 = moving up
	float max_fly_time;  // Amount of time player raises and gravity disabled 
	float gravity;      // Gravity
	float max_gravity;  // Gravity
	float min_gravity;  // Gravity
	float fly_up;       // Increase Y by amount per tick
	float re_jump_time;
	float grace_period;
	float max_grace;
	float glide;        // 
	boolean hit;        // If true end game
	boolean start;
	int score;
	int top_score;
	
	// Difficulty / Speed
	float scroll_speed;
	private float gap;
	int max, min;
			
	class TouchInfo {
		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
	}

	@Override
	public void create() {		
		Gdx.input.setInputProcessor(this);
		
		// test level changer
		level_id = 1;
	
		trail_length = 50;
				
		// Load Top Score
		load_prefs();
		
		// Screen width and height
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		// MENU
		GLTexture.setEnforcePotImages(false);
		menu = new Texture(Gdx.files.internal("data/ui/menu_background.png"));
		tubes = new Texture(Gdx.files.internal("data/ui/tubes.png"));
				
		// SCALE / RATIO
		v_width = 1196;
		v_height = 786;
		w_scale = w/v_width;
		h_scale = h/v_height;
				
	    camera = new OrthographicCamera(2,2);
	    camera.update();
						
		// holds touch info 
		touched = new TouchInfo();
		
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		// SpriteBatch for Screen
		batch = new SpriteBatch();
		batch.setTransformMatrix(camera.combined);
		
		// SpriteBatch for camera
		screen = new SpriteBatch();
		
		// New Player
		player = new Player(w, h, w_scale, h_scale);
		
		// Level
		level = new Level("level_" + level_id, w, h, w_scale, h_scale);
		
		// Game settings
		hit           = true;
		max_grace	  = 1;
		grace_period  = 0;
		re_jump_time  = 3;
		fly_time      = 0;
		max_fly_time  = 25;
		min_gravity   = 0;
		max_gravity   = h_scale*20;
		gravity       = min_gravity;
		fly_up        = h_scale*5;
		glide         = 3;
		scroll_speed  = w_scale*6;
		
		// Obstacles
		gap = w/3;
		object_array = new ArrayList<Blocker>();		
	}
	
	private void load_prefs(){
		// Settings
		prefs = Gdx.app.getPreferences("flappy_box");

		if (!prefs.contains("top_score")){
			prefs.putInteger("top_score", 0);
			top_score = 0;
		} else {
			top_score = prefs.getInteger("top_score");
		}

		prefs.flush();
	}
	
	private void save_prefs(){
		// Settings
		prefs.putInteger("top_score",score);
		prefs.flush();
		top_score = score;
	}
	
	@Override
	public void dispose() {
		screen.dispose();
		batch.dispose();
	}
	
	@Override
	public void render() {		
		if (!hit){ 
			logic(); 			
		}
		draw();
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
						
				max = (int) (box.high.y + box.high.h/3);
				if (max > h){
					max = (int) h;
				}
				min = (int) (box.high.y - box.high.h/3);
				
				//System.out.println("MOVE: " + box.id + " to Y:" + box.low.y + " Last (" + last.id +") Y: " + last.low.y);
				System.out.println(" Currnet Y: " + box.high.y + " min: " +min + " max: " + max);
				// Set Random height for bottom box
				rand_height = random_height(min,max);		
				
				// LOW
				box.low.x += 4 * gap;	
				box.low.y = rand_height - box.low.h;
				
				// HIGH
				box.high.x += 4 * gap;
				box.high.y = box.low.y + box.low.h + hole;					
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
				gravity += .5f;	
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
					e.x -= 4;
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
		// REFACTOR ALL OF THIS ITS A MESS
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
			// MOVE TO MENU CLASS
			float m_width  = w_scale * menu.getWidth();
			float m_height = h_scale * menu.getHeight();
			batch.draw(menu, w/2 - m_width/2 , h/2 - m_height/2, m_width, m_height);
			
			float t_width  = w_scale * tubes.getWidth();
			float t_height = h_scale * menu.getHeight();
			batch.draw(tubes, w/2 - m_width/2, h/2 - m_height/2, t_width, t_height);
		}
		batch.end();
		
		screen.begin();
		
		font.draw(screen, "Score           "+score, 20, 50);
		font.draw(screen, "High Score  "+ top_score, 20, 20);		
		screen.end();	
	}

	private void reset_game() {
		if (level_id == 1){
			level_id = 2;
		} else {
			level_id = 1;
		}
		level = new Level("level_" + level_id, w, h, w_scale, h_scale);
		
		plotter.clear();
		score = 0;
		scroll_speed = 4;
		fly_time = max_fly_time;
		grace_period  = max_grace;
		
		// Player
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
			// Set Random height for bottom box
			rand_height = random_height(min,max);
			
			// BOTTOM BOX
			box = new Blocker(w, h, i+1, level, w_scale, h_scale);
			box.low.x = (i * gap) + w;
			//box.low.y = 0;
			
			// TOP BOX
			box.high.x = box.low.x;
			//box.high.y = 400;
						
			box.set_hitboxes();
			object_array.add(box);	
		}		
	}
	
	private void check_collision(Blocker box) {		
		if (box.scorebox.overlaps(player.hitbox) && !box.scored){
			box.scored = true;
			score ++;
		} else {
			if (box.high.hitbox.overlaps(player.hitbox) || box.low.hitbox.overlaps(player.hitbox)){
				hit = true;
				if (score > top_score ){
					save_prefs();
				}
			}
		}
	}
	
	private boolean not_too_high() {
		return player.y + fly_up + player.height < h + player.height;
	}
	
	private boolean not_too_low() {
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
		Random r = new Random(); 
		int number = r.nextInt(max-min) + min;
		    
		return number;
	}
	
	@Override
	public void resize(int width, int height) {		      
	}

	@Override
	public void pause() {
	}
	
	@Override
	public void resume() {
	}
	
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
		touched.touchX = screenX;
		touched.touchY = screenY;
		touched.touched = true;
		
		if (!hit){ 
			if (fly_time <= re_jump_time){
				fly_time = max_fly_time;
				grace_period  = max_grace;
			}
		} else {
			reset_game();
		}
	 
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(pointer <= 2){
			touched.touchX = 0;
			touched.touchY = 0;
			touched.touched = false;
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