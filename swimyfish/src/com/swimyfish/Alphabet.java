package com.swimyfish;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;

public class Alphabet {
	private Texture no_0;
	private Texture no_1;
	private Texture no_2;
	private Texture no_3;
	private Texture no_4;
	private Texture no_5;
	private Texture no_6;
	private Texture no_7;
	private Texture no_8;
	private Texture no_9;
	
	private Texture s_no_0;
	private Texture s_no_1;
	private Texture s_no_2;
	private Texture s_no_3;
	private Texture s_no_4;
	private Texture s_no_5;
	private Texture s_no_6;
	private Texture s_no_7;
	private Texture s_no_8;
	private Texture s_no_9;
	
	public Alphabet(){
		GLTexture.setEnforcePotImages(false);
		
		no_0 = new Texture(Gdx.files.internal("data/numbers/0.png"));
		no_1 = new Texture(Gdx.files.internal("data/numbers/1.png"));
		no_2 = new Texture(Gdx.files.internal("data/numbers/2.png"));
		no_3 = new Texture(Gdx.files.internal("data/numbers/3.png"));
		no_4 = new Texture(Gdx.files.internal("data/numbers/4.png"));
		no_5 = new Texture(Gdx.files.internal("data/numbers/5.png"));
		no_6 = new Texture(Gdx.files.internal("data/numbers/6.png"));
		no_7 = new Texture(Gdx.files.internal("data/numbers/7.png"));
		no_8 = new Texture(Gdx.files.internal("data/numbers/8.png"));
		no_9 = new Texture(Gdx.files.internal("data/numbers/9.png"));
		
		s_no_0 = new Texture(Gdx.files.internal("data/numbers/small/0.png"));
		s_no_1 = new Texture(Gdx.files.internal("data/numbers/small/1.png"));
		s_no_2 = new Texture(Gdx.files.internal("data/numbers/small/2.png"));
		s_no_3 = new Texture(Gdx.files.internal("data/numbers/small/3.png"));
		s_no_4 = new Texture(Gdx.files.internal("data/numbers/small/4.png"));
		s_no_5 = new Texture(Gdx.files.internal("data/numbers/small/5.png"));
		s_no_6 = new Texture(Gdx.files.internal("data/numbers/small/6.png"));
		s_no_7 = new Texture(Gdx.files.internal("data/numbers/small/7.png"));
		s_no_8 = new Texture(Gdx.files.internal("data/numbers/small/8.png"));
		s_no_9 = new Texture(Gdx.files.internal("data/numbers/small/9.png"));
	}
	
	private void set_textures(Entity e, Texture main, Texture small){
		// MAIN
		e.texture = main;
		e.w = main.getWidth();
		e.h =  main.getHeight();
		// ALT
		e.alt_texture = small;
		e.alt_w = small.getWidth();
		e.alt_h = small.getHeight();
	}
	
	public ArrayList<Entity> get_number(String number){
		ArrayList<Entity> numbers;
		numbers = new ArrayList<Entity>();		
		String[] nos = number.split("");
	
		for (String no: nos){
			Entity e = new Entity();
			if (no.equals("0")){
				set_textures(e, no_0, s_no_0);
				numbers.add(e);	
			} else if (no.equals("1")){
				set_textures(e, no_1, s_no_1);
				numbers.add(e);	
			} else if (no.equals("2")){
				set_textures(e, no_2, s_no_2);
				numbers.add(e);	
			} else if (no.equals("3")){
				set_textures(e, no_3, s_no_3);
				numbers.add(e);	
			} else if (no.equals("4")){
				set_textures(e, no_4, s_no_4);
				numbers.add(e);	
			} else if (no.equals("5")){
				set_textures(e, no_5, s_no_5);
				numbers.add(e);	
			} else if (no.equals("6")){
				set_textures(e, no_6, s_no_6);
				numbers.add(e);	
			} else if (no.equals("7")){
				set_textures(e, no_7, s_no_7);
				numbers.add(e);	
			} else if (no.equals("8")){
				set_textures(e, no_8, s_no_8);
				numbers.add(e);	
			} else if (no.equals("9")){
				set_textures(e, no_9, s_no_9);
				numbers.add(e);	
			} else if (no.equals(".")){
				break;
			}
		}
		Collections.reverse(numbers);
		return numbers;
	}
	
	public Texture get_number_texture(String no){
		if (no.equals("0")){
			return no_0;
		} else if (no.equals("1")){
			return no_1;
		} else if (no.equals("2")){
			return no_2;
		} else if (no.equals("3")){
			return no_3;
		} else if (no.equals("4")){
			return no_4;
		} else if (no.equals("5")){
			return no_5;
		} else if (no.equals("6")){
			return no_6;
		} else if (no.equals("7")){
			return no_7;
		} else if (no.equals("8")){
			return no_8;
		} else if (no.equals("9")){
			return no_9;
		} else {
			return no_0;
		}
	}
	
	public Texture get_number_texture(int no){
		if (no == 0){
			return no_0;
		} else if (no == 1){
			return no_1;
		} else if (no == 2){
			return no_2;
		} else if (no == 3){
			return no_3;
		} else if (no == 4){
			return no_4;
		} else if (no == 5){
			return no_5;
		} else if (no == 6){
			return no_6;
		} else if (no == 7){
			return no_7;
		} else if (no == 8){
			return no_8;
		} else if (no == 9){
			return no_9;
		} else {
			return no_0;
		}
	}
}