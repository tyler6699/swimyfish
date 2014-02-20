package com.swimyfish;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swimyfish.Player;

public class FlappyBox implements ApplicationListener, InputProcessor{
	
	Game game;                  // MAIN GAME
	Device device;              // SCREEN & TOUCH 
	Level current_level;        // LEVEL
	Preferences prefs;          // SAVE GAME
	OrthographicCamera camera;  // CAMERA
	SpriteBatch batch;          // BATCH
	HiFi hifi;                  // SOUNDS
	Menu menu;                  // MENU
	Player player;              // HERO
			
	@Override
	public void create(){
		Gdx.input.setInputProcessor(this);
		
		//SOUNDS
		hifi = new HiFi();
		
		// SCREEN & TOUCH
		device = new Device();
		
		// MAIN GAME / SETTINGS
		game = new Game(device, player);
		current_level = game.current_level;
				
		// LOAD GAME
		load_prefs();
		game.pref_level();
				
		// MENU
		menu = new Menu(device, game.number_of_levels);
		menu.update_score(game.score, game.top_score, game.bank);
		
		// CAMERA SETUP
		camera = new OrthographicCamera(2,2);
		camera.update();
		
		// SB FOR CAMERA
		batch = new SpriteBatch();
		batch.setTransformMatrix(camera.combined);
		
		// NEW PLAYER
		player = new Player(device, 1);
	}
	
	private void load_prefs(){
		prefs = Gdx.app.getPreferences("pixel_jump");
		
		for (Level ls : game.levels){
			// TOP SCORE
			if (!prefs.contains("top_score_" + ls.level_id)){
				prefs.putInteger("top_score_" + ls.level_id, ls.top_score);
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
				
		// CURRENT LEVEL
		if (!prefs.contains("current_level")){
			prefs.putInteger("current_level", 1);
			game.level_id = 1;
		} else {
			game.level_id = prefs.getInteger("current_level");
		}
				
		// BANK
		if (!prefs.contains("bank")){
			prefs.putInteger("bank", 0);
			game.bank = 0;
		} else {
			game.bank = prefs.getInteger("bank");
		}
		
		// SOUND ON/OFF
		if (!prefs.contains("sound")){
			prefs.putBoolean("sound", true);
			game.sound = true;
		} else {
			game.sound = prefs.getBoolean("sound");
		}
		
		// GAME COMPLETE
		if (!prefs.contains("complete")){
			prefs.putBoolean("complete", false);
			game.complete = false;
		} else {
			game.complete = prefs.getBoolean("complete");
		}
		prefs.flush();
	}
	
	private void save_prefs(){
		for (Level ls : game.levels){
			prefs.putInteger("top_score_" + ls.level_id, ls.top_score);
			prefs.putInteger("progress_" + ls.level_id, ls.progress);
			prefs.putBoolean("locked_" + ls.level_id, ls.locked);
		}	
		prefs.putInteger("bank", game.bank);
		prefs.putInteger("current_level", game.level_id);
		prefs.putBoolean("sound", game.sound);
		prefs.putBoolean("complete", game.complete);
		prefs.flush();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	@Override
	public void render(){
		// SET CURRENT LEVEL
		current_level = game.current_level;
		game.delta = Gdx.graphics.getDeltaTime();
		
		// PLAYING GAME
		if ( game.playing_game() ){
			menu.playing_tick(device, true);
			menu_logic(true);
			game.logic(device, player, hifi, menu);
			
		// MAIN MENU
		} else if( game.main_menu() ){ 
			menu.tick(device);
			menu_logic(false);
			
		// MAIN MENU
		} else if( game.death_scene() ){ 
			if (game.hit_time == 1){
				save_prefs();
			}
			game.hit_time --;
		
				
		// WAITING TO START
		} else if ( game.tap_to_start() ) { 
			menu.playing_tick(device, false);
			menu_logic(true);
			
			// START THE GAME 
			if (!device.checked_click){
				game.started = true;
			}
		}
		
		// DRAW
		draw();
	}
	
	private void menu_logic(boolean playing) {
		if (!menu.action.equals("")){
			if ( playing ){
				if (menu.action.equals("SOUND")){
					game.sound = !game.sound;
				}
			} else {
				if (menu.action.equals("LEFT_ARROW")){
					if (game.level_id > 1){
						game.level_down();
						menu.update_score(game.score, game.top_score, game.bank);
						hifi.play_jump(1, game.sound);
					} else {
						game.level_last();
						menu.update_score(game.score, game.top_score, game.bank);
						hifi.play_jump(1, game.sound);
					}
				} else if (menu.action.equals("RIGHT_ARROW")){
					if (game.level_id < game.number_of_levels){
						game.level_up();
						hifi.play_jump(1, game.sound);
						menu.update_score(game.score, game.top_score, game.bank);
					} else {
						game.level_first();
						menu.update_score(game.score, game.top_score, game.bank);
						hifi.play_jump(1, game.sound);
					}
				} else if (menu.action.equals("PLAY")) {
					if (menu.ready && !game.current_level.locked){
						game.reset(device, menu, player);	
						hifi.play_collect(game.sound);
					 }
				} else if (menu.action.equals("SOUND")){
					game.sound = !game.sound;
				} else if (menu.action.equals("SHOP")){
					menu.current_menu = "SHOP";
					hifi.play_collect(game.sound);
				} else if (menu.action.equals("BACK")){
					menu.current_menu = "MAIN";
					hifi.play_jump(2, game.sound);
				}
			}
			menu.action = "";
			device.checked_click = true;
		}
	}
			
	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		batch.begin();
		// BACKGROUND
		batch.draw(current_level.scene.esky.texture, current_level.scene.esky.x, current_level.scene.esky.y, current_level.scene.esky.w, current_level.scene.esky.h);
		
		// CLOUDS
		for (Entity e: current_level.scene.eclouds){
			batch.draw(e.texture, e.x, e.y, e.w, e.h);
		}
				
		// FLOOR
		batch.draw(current_level.scene.efloor.texture, current_level.scene.efloor.x, current_level.scene.efloor.y, current_level.scene.efloor.w, current_level.scene.efloor.h);
		
		if (game.trail && game.plotter.size() > 0){
			for (int i = 0; i < game.plotter.size(); i++){
				Entity e = game.plotter.get(i);
				if (!game.hit){
					e.x -= device.w_scale*3;
				}
				if(e.w - (game.plotter.size()-i) > 1){
					batch.draw(e.texture, e.x, e.y, e.w - (game.plotter.size()-i), e.h - (game.plotter.size()-i));
				}
			}
		}		
		
		// PLAYER
		if (!game.hit){
			batch.draw(player.player_alive, player.x, player.y, player.width, player.height);
		} else {	
			batch.draw(player.player_hit, player.x, player.y, player.width, player.height);
		}
				
		// OBSTACLES
		for (Blocker box : game.object_array){
			batch.draw(box.high.texture, box.high.x, box.high.y, box.high.w, box.high.h);	
			batch.draw(box.low.texture, box.low.x, box.low.y, box.low.w, box.low.h);
			if (current_level.scene.show_blocker_lower){
				batch.draw(current_level.scene.elog_up.alt_texture, box.low.x, 0, box.low.w, current_level.scene.floor_h);	
			}
			
		}
		
		// RAYS
		batch.draw(current_level.scene.erays_1.texture, current_level.scene.erays_1.x, current_level.scene.erays_1.y, current_level.scene.erays_1.w, current_level.scene.erays_1.h);
		batch.draw(current_level.scene.erays_2.texture, current_level.scene.erays_2.x, current_level.scene.erays_2.y, current_level.scene.erays_2.w, current_level.scene.erays_2.h);
		
		// FLOOR TOPPER
		for (Entity e: current_level.scene.floor_toppers){
			batch.draw(e.texture, e.x, e.y, e.w, e.h);
		}
		
		// DRAW MENU ON TOP
		if (game.hit && game.hit_time == 0){
			menu.tick(batch,player, game.delta, game.score, current_level);
			menu.tick(batch, game.sound, true);
		} else {
			menu.tick(batch, game.sound, false);
		}
		
		//game.score
		if (!game.hit){
			int i = 0;
			float fw = menu.score_array.size(); 
			for(Entity e: menu.score_array){
				batch.draw(e.texture, (device.w/2) - i * (device.w_scale*e.texture.getWidth()) - (fw * (device.w_scale*e.texture.getWidth()/2)), device.h - (device.h_scale*e.texture.getHeight()) - device.h_scale*10, device.w_scale*e.texture.getWidth(), device.h_scale*e.texture.getHeight());
				i ++;
			}
		}
				
		batch.end();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		device.checked_click = false;
		device.touchX = screenX;
		device.touchY = device.h-screenY;
		device.touched = true;
		device.clicked_at.set(screenX, device.h-screenY, device.w_scale+5, device.h_scale*5);
				
		if (!game.hit && game.started){ 
			//if (game.fly_time <= game.re_jump_time){
				hifi.play_jump(game.jump_id, game.sound);
				game.jump_id = game.jump_id == 1 ? 2 : 1;
				
				game.fly_time = game.max_fly_time;
				game.grace_period  = game.max_grace;
			//}	
		}
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(pointer <= 2){
			device.touchX = 0;
			device.touchY = 0;
			device.touched = false;
		}
		return true;
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