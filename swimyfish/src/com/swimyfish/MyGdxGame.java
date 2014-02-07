package com.swimyfish;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Map;


public class MyGdxGame implements ApplicationListener, InputProcessor{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	//private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();
	private TouchInfo touched;
	private BitmapFont font;
	private String message = "Touch something already!";
	float w, h, x, y;
	class TouchInfo {
		public float touchX = 0;
	    public float touchY = 0;
	    public boolean touched = false;
	}
	 
	@Override
	public void create() {		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		x=0;
		y=0;
		Gdx.input.setInputProcessor(this);
	    //for(int i = 0; i < 5; i++){
	    //	touches.put(i, new TouchInfo());
	    //}
		touched = new TouchInfo();
		
	    font = new BitmapFont();
	    font.setColor(Color.RED);
	    
		camera = new OrthographicCamera(1, w/h);
		camera.update();
		
		batch = new SpriteBatch();
		batch.setTransformMatrix(camera.combined);
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
               
        batch.setTransformMatrix(camera.combined);
        camera.update();
        batch.begin();
        batch.draw(sprite, 0, 0);
     
        message = "touch at:" + Float.toString(touched.touchX) + "," + Float.toString(touched.touchY);        
        //	for(int i = 0; i < 5; i++){
        //  	if(touches.get(i).touched)
        //      	message += "Finger:" + Integer.toString(i) + "touch at:" + Float.toString(touches.get(i).touchX) + "," + Float.toString(touches.get(i).touchY) + "\n";
        //		}
        //	}
        //System.out.println(camera.position.x + "  :  " + camera.position.y);
        TextBounds tb = font.getBounds(message);
        //float x = w/2 - tb.width/2;
        //float y = h/2 + tb.height/2;
        font.draw(batch, ""+ -camera.position.x , 10, 10);
        batch.end();
        System.out.println(w + "  " + camera.position.x);
        if (-camera.position.x < (w/2)){
        	camera.position.x -= 1;
        	camera.update();
	    }
	}

	@Override
	public void resize(int width, int height) {
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
	       y += 1;
	       x += 1;	     
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
	    
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
