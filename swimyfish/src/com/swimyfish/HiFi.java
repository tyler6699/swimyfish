package com.swimyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class HiFi {
	private Sound collect;
	private Sound death;
	private Sound jump_1;
	private Sound jump_2;
	private Sound unlock;
	
	public HiFi(){
		collect = Gdx.audio.newSound(Gdx.files.internal("data/sounds/point.wav")); 
		death = Gdx.audio.newSound(Gdx.files.internal("data/sounds/death.wav"));
		jump_1 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/jump_1.wav"));
		jump_2 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/jump_2.wav"));
		unlock = Gdx.audio.newSound(Gdx.files.internal("data/sounds/unlock.wav"));
	}
	
	public void play_collect(){
		collect.stop();
		collect.play(.5f);
	}
	
	public void play_death(){
		death.stop();
		death.play(1);
	}
	
	public void play_jump(int i){
		if (i == 1){
			jump_1.play();
		} else {
			jump_2.play();
		}
	}
	
	public void play_unlock(){
		unlock.play();
	}

}