package com.swimyfish;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;

public class Alphabet {
	
	private ArrayList<Texture> numbers;
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
		numbers = new ArrayList<Texture>();
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
	
		public ArrayList<Texture> get_number(String number){
		numbers.clear();
		
		String[] nos = number.split("");
		
		for (String no: nos){
			if (no.equals("0")){
				numbers.add(no_0);
			} else if (no.equals("1")){
				numbers.add(no_1);
			} else if (no.equals("2")){
				numbers.add(no_2);
			} else if (no.equals("3")){
				numbers.add(no_3);
			} else if (no.equals("4")){
				numbers.add(no_4);
			} else if (no.equals("5")){
				numbers.add(no_5);
			} else if (no.equals("6")){
				numbers.add(no_6);
			} else if (no.equals("7")){
				numbers.add(no_7);
			} else if (no.equals("8")){
				numbers.add(no_8);
			} else if (no.equals("9")){
				numbers.add(no_9);
			}
		}
		return numbers;
	}
}