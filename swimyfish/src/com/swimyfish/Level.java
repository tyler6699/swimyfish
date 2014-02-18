package com.swimyfish;

public class Level {

	public int level_id;
	public int top_score;
	public boolean locked;
	public int progress;
	public int points_needed;
	public Scene scene;
	
	public Level(int level_id, float w, float h, float w_scale, float h_scale){
		this.scene = new Scene(level_id, w, h, w_scale, h_scale);
	}

}