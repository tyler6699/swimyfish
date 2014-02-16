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
	}
	
	public ArrayList<Entity> get_number(String number){
		ArrayList<Entity> numbers;
		numbers = new ArrayList<Entity>();
		
		Entity e = new Entity();
		String[] nos = number.split("");
		
		for (String no: nos){
			if (no.equals("0")){
				e.texture = no_0;
				e.w = no_0.getWidth();
				e.h =  no_0.getHeight();
				numbers.add(e);	
			} else if (no.equals("1")){
				e.texture = no_1;
				e.w = no_1.getWidth();
				e.h =  no_1.getHeight();
				numbers.add(e);	
			} else if (no.equals("2")){
				e.texture = no_2;
				e.w = no_2.getWidth();
				e.h =  no_2.getHeight();
				numbers.add(e);	
			} else if (no.equals("3")){
				e.texture = no_3;
				e.w = no_3.getWidth();
				e.h =  no_3.getHeight();
				numbers.add(e);	
			} else if (no.equals("4")){
				e.texture = no_4;
				e.w = no_4.getWidth();
				e.h =  no_4.getHeight();
				numbers.add(e);	
			} else if (no.equals("5")){
				e.texture = no_5;
				e.w = no_5.getWidth();
				e.h =  no_5.getHeight();
				numbers.add(e);	
			} else if (no.equals("6")){
				e.texture = no_6;
				e.w = no_6.getWidth();
				e.h =  no_6.getHeight();
				numbers.add(e);	
			} else if (no.equals("7")){
				e.texture = no_7;
				e.w = no_7.getWidth();
				e.h =  no_7.getHeight();
				numbers.add(e);	
			} else if (no.equals("8")){
				e.texture = no_8;
				e.w = no_8.getWidth();
				e.h =  no_8.getHeight();
				numbers.add(e);	
			} else if (no.equals("9")){
				e.texture = no_9;
				e.w = no_9.getWidth();
				e.h =  no_9.getHeight();
				numbers.add(e);	
			}
			e = new Entity();
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