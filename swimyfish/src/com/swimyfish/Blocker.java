package com.swimyfish;
import com.badlogic.gdx.math.Rectangle;

public class Blocker {
	public int id;
	public float width;	
	public Rectangle scorebox;
	public boolean scored;
	public Entity high;
	public Entity low;
	
	public Blocker(float w, float h, int no, Level level, float scale_w, float scale_h){
		// 1196 x 768	
		id = no;
		float third = h/3;
		
		// TOP BOX
		high = new Entity();
		high.w = scale_w * level.blocker.getWidth();
		high.h = scale_h * level.blocker.getHeight();
		high.x = w + (w/100)*scale_w;
		high.y = h - third;
		System.out.println(h + " " + high.h);
		high.hitbox = new Rectangle(high.x,high.y,high.w,high.h); 
		high.texture = level.blocker;
		
		// BOTTOM BOX
		low = new Entity();
		low.w = scale_w * level.blocker.getWidth();
		low.h = scale_h * level.blocker.getHeight();
		low.x = w + (w/100)*scale_w;
		low.y = -low.h + third;
		low.hitbox = new Rectangle(low.x,low.y,low.w,low.h); 
		low.texture = level.blocker;
		
		scorebox  = new Rectangle(0,0,10,h);
		scored    = false;
	}
	
	public void set_hitboxes(){
		high.hitbox.set(high.x,high.y,high.w,high.h);
		low.hitbox.set(low.x,low.y,low.w,low.h);
	}
	
	public void update_hitboxes(float x){
		high.hitbox.setPosition(high.x - x,high.y);	
		low.hitbox.setPosition(low.x - x,low.y);	
		scorebox.setPosition(high.x - x, 0);
	}
	
}