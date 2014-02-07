package com.swimyfish;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class MyGdxGame implements ApplicationListener, InputProcessor{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SpriteBatch screen;
	private Texture texture;

	
	//private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();
	private TouchInfo touched;
	private BitmapFont font;
	float w, h, x = 0, y = 0;
	
	float aspectRatio;
    float scale;
    float ww, hh;
    float v_height;
    float v_width;
	
	class TouchInfo {
		public float touchX = 0;
	    public float touchY = 0;
	    public boolean touched = false;
	}
	 
	@Override
	public void create() {		
		Gdx.input.setInputProcessor(this);
		camera = new OrthographicCamera();
		scale = 2;
		v_height = 800;
		v_width = 1200;
		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		re_size(w,h);
			
		touched = new TouchInfo();

	    font = new BitmapFont();
	    font.setColor(Color.RED);
	    
		batch = new SpriteBatch();
		batch.setTransformMatrix(camera.combined);
		
		screen = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
	}

	@Override
	public void dispose() {
		screen.dispose();
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera.update();
        
        batch.begin();
        batch.draw(texture, x, y, 32, 32);
        batch.end();
                
        screen.begin();
         screen.draw(texture, 0, 0, 32,32);
         font.draw(screen, "H: " + h + " W: 0", 0, h);
         font.draw(screen, "H: " + h + " W: " + w, w-130, h);
         
         font.draw(screen, "H: 0" + " W: " + w, w-130, 20);
         font.draw(screen, "H: 0" + " W: 0", 0, 20);
         font.draw(screen, "Y: " + y + " X: " + x + " VW: " + camera.viewportWidth + " VH: " + camera.viewportHeight, w/2, h/4);
         font.draw(screen,"+", w/2, h/2);
        screen.end();	
	}

	@Override
	public void resize(int width, int height) {	
		re_size(width, height);
	}
	
	private void re_size(float w, float h){
	 			    
		if (w > v_width){
			ww = (v_width/w)*scale;
		} else {
			ww = (w/v_width)*scale;
		}
		
		if (h > v_height){
			hh = (v_height/h)*scale;
		} else {
			hh = (h/v_height)*scale;
		}
		
	    camera.setToOrtho(false, ww, hh);
	    camera.update(); 
	}
	

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touched.touchX = screenX;
		touched.touchY = screenY;
		touched.touched = true;
	       y += 5;
	       x += 20;	     
		//	if(pointer < 5){
		//		touches.get(pointer).touchX = screenX;
	    //  	touches.get(pointer).touchY = screenX;
	    //    	touches.get(pointer).touched = true;
	    //	}
		return true;
	}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	if(pointer <= 2){
    		touched.touchX = 0;
			touched.touchY = 0;
			touched.touched = false;
    	}
    	//	if(pointer < 5){
    	//  	touches.get(pointer).touchX = 0;
    	//      touches.get(pointer).touchY = 0;
    	//      touches.get(pointer).touched = false;
    	//  }
        return true;
    }

    final Vector3 curr = new Vector3();
    final Vector3 last = new Vector3(-1, -1, -1);
    final Vector3 delta = new Vector3();

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//camera.unproject(curr.set(x, y, 0));
		//if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
		//camera.unproject(delta.set(last.x, last.y, 0));
		//delta.sub(curr);
		//camera.position.add(delta.x, delta.y, 0);
		//}
		//last.set(x, y, 0);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
