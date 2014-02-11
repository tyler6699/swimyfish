package com.swimyfish;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;

public class Level {
	
	// Device
	float w,h;
	
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
	
	public Level(String level, float w, float h){
		// Scene	
		GLTexture.setEnforcePotImages(false);
		this.w = w;
		this.h = h;
		
		// Array for toppers & clouds
		floor_toppers = new ArrayList<Entity>(); 
		eclouds       = new ArrayList<Entity>(); 
		if (level.equals("level_1")){
			blocker_floor = new Texture(Gdx.files.internal("data/box_water.png"));
			blocker       = new Texture(Gdx.files.internal("data/log.png"));
			floor         = new Texture(Gdx.files.internal("data/sea.png"));
			floor_top     = new Texture(Gdx.files.internal("data/wave.png"));
			sky           = new Texture(Gdx.files.internal("data/sky.png"));
			rays_1        = new Texture(Gdx.files.internal("data/rays_2.png"));
			rays_2        = new Texture(Gdx.files.internal("data/rays_4.png"));
			cloud         = new Texture(Gdx.files.internal("data/cloud.png"));
		}
		
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
		elog_up.w = blocker.getHeight();
		elog_up.h = blocker.getHeight();
		elog_up.texture = blocker;
		
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
		efloor.h = 0.2f*h;
		efloor.texture = floor;
		
		// FLOOR TOP
		int min = (int) Math.ceil(w/floor_top.getWidth());
		min += 1;
		for (int i = 0; i < min; i++){
			efloor_top = new Entity();
			efloor_top.x = floor_top.getWidth()*(i);
			efloor_top.y = (0.2f*h)-8;
			efloor_top.w = floor_top.getWidth();
			efloor_top.h = floor_top.getHeight();
			efloor_top.texture = floor_top;
			floor_toppers.add(efloor_top);
		}
		
		// CLOUDS
		min = (int) Math.ceil(w/cloud.getWidth());
		min += 2;
		for (int i = 0; i < min; i++){
			ecloud = new Entity();
			ecloud.x = cloud.getWidth()*(i);
			ecloud.y = h-cloud.getHeight();
			ecloud.w = cloud.getWidth();
			ecloud.h = cloud.getHeight();
			ecloud.texture = cloud;
			eclouds.add(ecloud);
		}
		
		
		// RAYS 1
		erays_1.x = (w/5) *1;
		erays_1.y = efloor.h - rays_1.getHeight();
		erays_1.w = rays_1.getWidth();
		erays_1.h = rays_1.getHeight();
		erays_1.texture = rays_1;
		// RAYS 2
		erays_2.x = (w/5) *4;
		erays_2.y = efloor.h - rays_1.getHeight();
		erays_2.w = rays_1.getWidth();
		erays_2.h = rays_1.getHeight();
		erays_2.texture = rays_2;
	}

}
