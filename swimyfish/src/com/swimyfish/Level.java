package com.swimyfish;

public class Level {

	public int level_id;
	public int top_score;
	public boolean locked;
	public int progress;
	public int points_needed;
	public Scene scene;
	
	public Level(int level_id, Device device, Player player){
		this.scene = new Scene(level_id, device, player);
	}

}