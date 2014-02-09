package com.swimyfish;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame implements ApplicationListener, InputProcessor{
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
	
	// Screen Settings
	float aspectRatio;
	float scale;
	float ww, hh;
	float v_height;
	float v_width;
	
	// Game Vars
	float fly_time;      // > 0 = moving up
	float max_fly_time;  // Amount of time player raises and gravity disabled 
	float drop_rate;     // Gravity
	float fly_up;        // Increase Y by amount per tick
	float glide;         // 
	boolean hit;         // If true end game
	
	// Difficulty / Speed
	float scroll_speed;
	private int gap;
	
	class TouchInfo {
		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
	}
	
	class Player {
		public float x = w/2;
		public float y = h/2;
		public Texture texture;
		public float height;
		public float width;
		public Rectangle hitbox;
			
		public Player(){
			width = 60;
			height = 60;
			x = (w/2) - 200;
			y = (h/2) - (width/2);	
			hitbox = new Rectangle(x,y,width,height);
			texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		}
	}
	
	class Obstacle {
		public float x = 0;
		public float y = 0;
		public Texture texture;
		public float height;
		public float width;
		public Rectangle hitbox;
		
		public Obstacle(){
			width = 80;
			height = 200;
			x = w + 20;
			y = 0;	
			hitbox = new Rectangle(x,y,width,height);
			texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		}
	}
	 
	@Override
	public void create() {		
		Gdx.input.setInputProcessor(this);
		camera = new OrthographicCamera();
		scale = 2;
			
		// Most Popular 16:9
		v_height = 768;
		v_width = 1280;
		
		// Screen width and height
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		// Set screen size
		re_size(w,h);
			
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
		player = new Player();
		
		// Game settings
		hit          = false;
		fly_time     = 0;
		max_fly_time = 25;
		drop_rate    = 9;
		fly_up       = 5;
		glide        = 3;
		scroll_speed = 4;
		
		// Obstacles
		gap = 450;
		object_array = new ArrayList<Obstacle>();
		Obstacle obs_t, obs_b;
		
		for (int i = 0; i < 3; i++){
			obs_b = new Obstacle();
			obs_b.x = (i * gap) + w;
			obs_b.y = 0;
			object_array.add(obs_b);	
			
			obs_t = new Obstacle();
			obs_t.x = obs_b.x;
			obs_t.y = (h + 1) - obs_t.height;
			object_array.add(obs_t);
		}
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
		} else {
			
		}
		draw();
	}
	
	private void logic() {	
		// Obstacles
		for (Obstacle obs : object_array){			
			if (obs.x < -obs.width){
				obs.x += 3 * gap;
			} else {
				obs.x -= scroll_speed;	
			}
			obs.hitbox.setPosition(obs.x, obs.y);
		}	
		
		// Player
		if (fly_time > glide){
			fly_time -= 1;
			if ( not_too_high() ){
				player.y += fly_up;	
			}
		} else if (fly_time > 0 && fly_time <= glide ){
			fly_time -= 1;
		} else {
			if ( not_too_low() ){
				player.y -= drop_rate;
			}
		}
		
		player.hitbox.setPosition(player.x, player.y);
		
		for (Obstacle obs : object_array){
			if (!hit){
				check_collision(obs);
			}
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		batch.begin();
		batch.draw(player.texture, player.x, player.y, player.width, player.height);
		for (Obstacle obs : object_array){
			batch.draw(obs.texture, obs.x, obs.y, obs.width, obs.height);	
		}
		batch.end();
		
		//screen.begin();
		//screen.draw(texture, 0, 0, 32,32);
		//screen.draw(fish.texture, fish.x, fish.y, fish.width, fish.height);
		//screen.end();	
	}

	private void reset_game() {
		// Player
		player.y = (w/2) - 200;
		
		// Game settings
		hit          = false;
		fly_time     = 0;
		
		// Obstacles
		object_array.clear();
		Obstacle obs_t, obs_b;
		
		for (int i = 0; i < 3; i++){
			obs_b = new Obstacle();
			obs_b.x = (i * gap) + w;
			obs_b.y = 0;
			object_array.add(obs_b);	
			
			obs_t = new Obstacle();
			obs_t.x = obs_b.x;
			obs_t.y = (h + 1) - obs_t.height;
			object_array.add(obs_t);
		}
		
	}
	
	private void check_collision(Obstacle obs) {		
		if (obs.hitbox.overlaps(player.hitbox)){
			hit = true;
		}
	}
	
	private boolean not_too_high() {
		return player.y + fly_up + player.height < h + player.height;
	}
	
	private boolean not_too_low() {
		return player.y - drop_rate > -player.height;
	}  
	
	@Override
	public void resize(int width, int height) {	
		re_size(width, height);
	}
	
	private void re_size(float w, float h){
		if (w > v_width){
			ww = (v_width/w)*scale;
		} else {
			ww = (w/v_width)*scale;
		}
			
		if (h > v_height){
			hh = (v_height/h)*scale;
		} else {
			hh = (h/v_height)*scale;
		}
		
		camera.setToOrtho(false, ww, hh);
		camera.update(); 
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