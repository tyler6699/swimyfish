package com.swimyfish;
import com.badlogic.gdx.math.Rectangle;

public class Blocker {
	public int id;
	public float width;	
	public Rectangle scorebox;
	public boolean scored;
	public Entity high;
	public Entity low;
	
	public Blocker(float w, float h, int no, Level level){
		// 1196 x 768	
		id = no;
		
		// TOP BOX
		high = new Entity();
		high.x = w + 20;
		high.y = 0;
		high.w = 70;//level.floor.getWidth();
		high.h = h/3;
		high.hitbox = new Rectangle(high.x,high.y,high.w,high.h); 
		high.texture = level.log_up;
		
		// BOTTOM BOX
		low = new Entity();
		low.x = w + 20;
		low.y = 0;
		low.w = 70;//level.floor.getWidth();
		low.h = h/3;
		low.hitbox = new Rectangle(low.x,low.y,low.w,low.h); 
		low.texture = level.log_up;
		
		scorebox  = new Rectangle(0,0,10,h);
		scored    = false;
	}
	
	public void set_hitboxes(float h){
		high.hitbox.set(high.x,high.y,high.w,high.h);
		low.hitbox.set(low.x,low.y,low.w,low.h);;
	}
	
	public void update_hitboxes(float x){
		high.hitbox.setPosition(high.x - x,high.y);	
		low.hitbox.setPosition(low.x - x,low.y);	
		scorebox.setPosition(high.x - x, 0);
	}
	
}