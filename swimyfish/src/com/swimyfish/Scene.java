package com.swimyfish;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;

public class Scene {
	int level_id;
	// Device
	float w,h,w_scale,h_scale;
	float floor_h;
	float cloud_h;
	float top_gap;
	public boolean show_blocker_lower = true;
	
	// SETTINGS
	public float scroll_speed;
	public float gap;
	
	// Scene
	public Texture floor;
	public Texture floor_top;
	public Texture blocker;
	public Texture blocker_floor;
	public Texture rays_1;
	public Texture rays_2;
	public Texture sky;
	public Texture cloud;
	public Entity ecloud;
	public ArrayList<Entity> eclouds;
	
	public Entity efloor;
	public Entity efloor_top;
	public ArrayList<Entity> floor_toppers;
	public Entity elog_up;
	public Entity elog_floor;
	public Entity erays_1;
	public Entity erays_2;
	public Entity esky;
	
	public Scene(int level_id, Device device){	
		GLTexture.setEnforcePotImages(false);
		this.w = device.w;
		this.h = device.h;
		this.w_scale = device.w_scale;
		this.h_scale = device.h_scale;
		
		this.level_id = level_id;
		
		// Array for toppers & clouds
		floor_toppers = new ArrayList<Entity>(); 
		eclouds       = new ArrayList<Entity>(); 
				
		blocker_floor = new Texture(Gdx.files.internal("data/level_" + level_id + "/blocker_floor.png"));
		blocker       = new Texture(Gdx.files.internal("data/level_" + level_id + "/blocker.png"));
		floor         = new Texture(Gdx.files.internal("data/level_" + level_id + "/floor.png"));
		floor_top     = new Texture(Gdx.files.internal("data/level_" + level_id + "/floor_top.png"));
		sky           = new Texture(Gdx.files.internal("data/level_" + level_id + "/sky.png"));
		rays_1        = new Texture(Gdx.files.internal("data/level_" + level_id + "/rays_1.png"));
		rays_2        = new Texture(Gdx.files.internal("data/level_" + level_id + "/rays_2.png"));
		cloud         = new Texture(Gdx.files.internal("data/level_" + level_id + "/cloud.png"));

		if (level_id == 1){
			floor_h = 0.2f*h;
			cloud_h = h-(device.h_scale * cloud.getHeight());
			top_gap = 8;
			scroll_speed = 6;
			gap = w/3;
		} else if (level_id == 2){
			floor_h = 0.1f*h;
			cloud_h = floor_h;
			top_gap = 8;
			scroll_speed = 7;
			gap = (w/3) - device.w_scale*20;
		} else if (level_id == 3) {
			floor_h = 0.2f*h;
			cloud_h = floor_h - floor_top.getHeight();
			top_gap = 30;
			scroll_speed = 7;
			gap = (w/3) - device.w_scale*30;
		} else if (level_id == 4) {
			floor_h = 0.03f*h;
			cloud_h = floor_h - floor_top.getHeight();
			top_gap = 30;
			scroll_speed = 7;
			gap = (w/3) - device.w_scale*30;
		}
		
		// FLOOR
		efloor     = new Entity();
		efloor_top = new Entity();
		elog_up    = new Entity();
		elog_floor = new Entity();
		erays_1    = new Entity();
		erays_2    = new Entity();
		esky       = new Entity();
		
		// COLUMN
		elog_up.x = 0;
		elog_up.y = 0;
		elog_up.texture = blocker;
		elog_up.w = w_scale * blocker.getWidth();
		elog_up.h = h_scale * blocker.getHeight();
		elog_up.alt_texture = blocker_floor;
		elog_up.w = w_scale * blocker.getWidth();
		elog_up.h = floor_h;
				
		// SKY
		esky.x = 0;
		esky.y = 0;
		esky.w = w;
		esky.h = h;
		esky.texture = sky;
		
		// FLOOR
		efloor.x = 0;
		efloor.y = 0;
		efloor.w = w;
		efloor.h = floor_h;
		efloor.texture = floor;
		
		// FLOOR TOP
		int min = (int) Math.ceil(w/floor_top.getWidth());
		min += 1;
		for (int i = 0; i < min; i++){
			efloor_top = new Entity();
			efloor_top.x = (w_scale * floor_top.getWidth())*(i);
			efloor_top.y = efloor.h - top_gap*h_scale;
			efloor_top.w = w_scale * floor_top.getWidth();
			efloor_top.h = h_scale * floor_top.getHeight();
			efloor_top.texture = floor_top;
			floor_toppers.add(efloor_top);
		}
		
		// CLOUDS
		min = (int) Math.ceil(w/cloud.getWidth());
		min += 2;
		for (int i = 0; i < min; i++){
			ecloud = new Entity();
			ecloud.x = (w_scale * cloud.getWidth())*(i);
			ecloud.y = cloud_h;
			ecloud.w = w_scale * cloud.getWidth();
			ecloud.h = h_scale * cloud.getHeight();
			ecloud.texture = cloud;
			eclouds.add(ecloud);
		}
		
		// RAYS 1
		erays_1.x = (w/5) *1;
		erays_1.y = efloor.h - (h_scale * rays_1.getHeight());
		erays_1.w = w_scale * rays_1.getWidth();
		erays_1.h = h_scale * rays_1.getHeight();
		erays_1.texture = rays_1;
		
		// RAYS 2
		erays_2.x = (w/5) *4;
		erays_2.y = efloor.h - (h_scale * rays_1.getHeight());
		erays_2.w = w_scale * rays_1.getWidth();
		erays_2.h = h_scale * rays_1.getHeight();
		erays_2.texture = rays_2;
	}

}
