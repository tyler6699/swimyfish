package com.swimyfish;
import com.badlogic.gdx.math.Rectangle;

public class Blocker {
	public int id;
	public float width;	
	public Rectangle scorebox;
	public boolean scored;
	public Entity high;
	public Entity low;
	private float scale_w;
	
	public Blocker(float w, float h, int no, Scene scene, float scale_w, float scale_h){
		// 1196 x 768	
		id = no;
		this.scale_w = scale_w;
		float third = h/3;
		
		// TOP BOX
		high = new Entity();
		high.w = scale_w * scene.blocker.getWidth();
		high.h = scale_h * scene.blocker.getHeight();
		high.x = w + (w/100)*scale_w;
		high.y = h - third;
		high.hitbox = new Rectangle(high.x,high.y,high.w,high.h); 
		high.texture = scene.blocker;
		
		// BOTTOM BOX
		low = new Entity();
		low.w = scale_w * scene.blocker.getWidth();
		low.h = scale_h * scene.blocker.getHeight();
		low.x = w + (w/100)*scale_w;
		low.y = -low.h + third;
		low.hitbox = new Rectangle(low.x,low.y,low.w,low.h); 
		low.texture = scene.blocker;
		
		scorebox  = new Rectangle(0,0,high.w,high.h);
		scored    = false;
	}
	
	public void set_hitboxes(){
		high.hitbox.set(high.x,high.y,high.w,high.h);
		low.hitbox.set(low.x,low.y,low.w,low.h);
	}
	
	public void update_hitboxes(float x){
		high.hitbox.setPosition(high.x - x,high.y);	
		low.hitbox.setPosition(low.x - x,low.y);	
		scorebox.setPosition((high.x + 30*scale_w) - x, 0);
	}
	
}