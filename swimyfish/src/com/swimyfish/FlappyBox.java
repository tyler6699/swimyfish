package com.swimyfish;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.swimyfish.Player;

public class FlappyBox implements ApplicationListener, InputProcessor{
	private Preferences prefs;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SpriteBatch screen;
	private Texture texture;
	
	private TouchInfo touched;
	private BitmapFont font;
	float w, h, x = 0, y = 0;
		
	// Player Entity
	private Player player;
	
	// Obstacles
	private ArrayList<Obstacle> object_array;
		
	// Game Vars
	float fly_time;      // > 0 = moving up
	float max_fly_time;  // Amount of time player raises and gravity disabled 
	float gravity;     // Gravity
	float max_gravity; // Gravity
	float min_gravity; // Gravity
	float fly_up;        // Increase Y by amount per tick
	float grace_period;
	float max_grace;
	float glide;         // 
	boolean hit;         // If true end game
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
		// Load Top Score
		load_prefs();
		
		// Screen width and height
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		// Columns
		max = (int) (h*.8);
		min = (int) (h*.2);
		
		camera = new OrthographicCamera(2,2);
						
		// holds touch info 
		touched = new TouchInfo();
		
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		// SpriteBatch for Screen
		batch = new SpriteBatch();
		batch.setTransformMatrix(camera.combined);
		
		// SpriteBatch for camera
		screen = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		
		// New Player
		player = new Player(w,h);
		
		// Game settings
		hit           = true;
		max_grace	  = 30;
		grace_period  = 0;
		fly_time      = 0;
		max_fly_time  = 25;
		min_gravity   = 5;
		max_gravity   = 10;
		gravity       = min_gravity;
		fly_up        = 5;
		glide         = 3;
		scroll_speed  = 4;
		
		// Obstacles
		gap = w/3;
		object_array = new ArrayList<Obstacle>();		
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
		texture.dispose();
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
		// Obstacles
		for (Obstacle obs : object_array){			
			if (obs.x < -obs.width){
				obs.x += 4 * gap;
				
				// SET NEW RANDOM HEIGHT FOR TOP AND BOTTOM WITH ID
				//rand_height = random_height(min,max);
				//obs.height = rand_height;
				//obs.set_hitboxes(rand_height);
			
				obs.scored = false;
			} else {
				obs.x -= scroll_speed;	
			}
			obs.hitbox.setPosition(obs.x, obs.y);
			obs.scorebox.setPosition(obs.x + (obs.width/2), obs.y);
		}	
		
		// Player
		if (grace_period <= max_grace){
			grace_period ++;
		} else {
			if (fly_time > glide){
				fly_time -= 1;
				if ( not_too_high() ){
					update_gravity(false);
					player.y += fly_up;	
				}
			} else if (fly_time > 0 && fly_time <= glide ){
				update_gravity(false);
				fly_time -= 1;
			} else {
				if ( not_too_low() ){
					update_gravity(true);
					player.y -= gravity;
				}
			}
			
			player.hitbox.setPosition(player.x, player.y);
			
			for (Obstacle obs : object_array){
				if (!hit){
					check_collision(obs);
				}
			}
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
		// PLAYER
		batch.draw(player.texture, player.x, player.y, player.width, player.height);
		
		// OBSTACLES
		for (Obstacle obs : object_array){
			batch.draw(obs.texture, obs.x, obs.y, obs.width, obs.height);	
		}
		batch.end();
		
		screen.begin();
		font.draw(screen, "Score           "+score, 20, 50);
		font.draw(screen, "High Score  "+ top_score, 20, 20);
		screen.end();	
	}

	private void reset_game() {
		score = 0;
		scroll_speed = 4;
		fly_time = max_fly_time;
		grace_period  = 0;
		
		// Player
		player.y = (w/2) - 200;
		
		// Game settings
		hit          = false;
		fly_time     = 0;
		
		// Obstacles
		object_array.clear();
		Obstacle obs_t, obs_b;
		
		float rand_height;
		float hole =  h/3f;
				
		for (int i = 0; i < 4; i++){
			
			// Set Random height for bottom box
			rand_height = random_height(min,max);
			
			// BOTTOM BOX
			obs_b = new Obstacle(w, h, i+1);
			obs_b.height = rand_height;
			obs_b.x = (i * gap) + w;
			obs_b.y = 0;
			obs_b.set_hitboxes(rand_height);
			object_array.add(obs_b);	
			
			// TOP BOX
			obs_t = new Obstacle(w, h, i+1);
			obs_t.x = obs_b.x;
			obs_t.y = obs_b.height + hole;
			obs_t.height = h - (obs_b.height + hole);
			obs_t.set_hitboxes(obs_t.height);
			object_array.add(obs_t);
		}
	}
	
	private void check_collision(Obstacle obs) {		
		if (obs.scorebox.overlaps(player.hitbox) && !obs.scored){
			obs.scored = true;
			score ++;
		} else {
			if (obs.hitbox.overlaps(player.hitbox)){
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
	
	// Random Functions
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
			if (fly_time <= glide){
				fly_time = max_fly_time;
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
	
	//final Vector3 curr = new Vector3();
	//final Vector3 last = new Vector3(-1, -1, -1);
	//final Vector3 delta = new Vector3();
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//camera.unproject(curr.set(x, y, 0));
		//if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
		//camera.unproject(delta.set(last.x, last.y, 0));
		//delta.sub(curr);
		//camera.position.add(delta.x, delta.y, 0);
		//}
		//last.set(x, y, 0);
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