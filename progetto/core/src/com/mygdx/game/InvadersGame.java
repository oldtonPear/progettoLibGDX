package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.coreGame.Parameters;

public class InvadersGame extends ApplicationAdapter implements InputProcessor, Observer{
	private SpriteBatch batch;
	private float camwidth = 4;
	private float camHeight;

	private Camera cam;
	private Screen currentScreen;

	private Sound music_level;
	private Sound music_menu;

	private int currentKeyPressed;

	private int timepassed;

	private int nWins;
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		Parameters.setAspectRatio(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camHeight = camwidth * Parameters.getInverseAspectRatio();
		cam = new OrthographicCamera(camwidth, camHeight);
		cam.position.set(camwidth * 0.5f, camHeight * 0.5f, 0);

		batch = new SpriteBatch();
		
		music_level = ResourceLoader.getSound(ResourceEnum.MUSIC_LEVEL);
		music_menu = ResourceLoader.getSound(ResourceEnum.MENU_MUSIC);


		enterMenu();


		nWins = 0;
	}

	@Override
	public void render () {

		cam.update();

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		currentScreen.update();
		currentScreen.draw(batch);

		if(currentScreen instanceof Losing_screen || currentScreen instanceof Winning_screen){
			timepassed++;
			if(timepassed >= 300){
				enterMenu();
				timepassed = 0;
			} 
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(currentScreen instanceof Level){
			if(keycode == 29 || keycode == 32){
				((Level) currentScreen).moveDragon(keycode);
				currentKeyPressed = keycode;
			}
			
			if(keycode == 62){
					((Level) currentScreen).spawnFireball();
				}
			else if(keycode == 66){
				((Level) currentScreen).executeSuperAttack();
				} 
		} 
		else{
			if(keycode == 62){
				enterLevel();
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(currentScreen instanceof Level){
			if(keycode == currentKeyPressed){
				((Level) currentScreen).moveDragon(0);
			}
		} 
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;

	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;

	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	/**
	 * enters in the Level screen
	 * @see Screen
	 */
	public void enterLevel(){
		if(currentScreen instanceof Menu){
			music_menu.stop();
			currentScreen = new Level("Level", camwidth * Parameters.getInverseAspectRatio(), nWins);
			((Level) currentScreen).register(this);
			music_level.loop();
		} 
	}
	/**
	 * enters in the Menu screen
	 * @see Screen
	 */
	public void enterMenu(){
		music_level.stop();
		music_menu.loop();
		currentScreen = new Menu("Menu", camwidth * Parameters.getInverseAspectRatio());
	}

	/**
	 * enters in the losing/winning screen
	 * @see Screen
	 */
	public void winningLosingScreen(){
		music_level.stop();
		if(currentScreen instanceof Level){
			int score = ((Level) currentScreen).getScore();
			if(score >= ((Level) currentScreen).nPlanes()){
				currentScreen = new Winning_screen("YOU WIN!", camwidth * Parameters.getInverseAspectRatio());
				if(nWins < 8) nWins++;
				Sound wS = ResourceLoader.getSound(ResourceEnum.YOU_WIN_SOUND);
				wS.play();
			}
			else{
				nWins = 0;
				currentScreen = new Losing_screen("YOU LOSE!", camwidth * Parameters.getInverseAspectRatio());
				Sound lS = ResourceLoader.getSound(ResourceEnum.YOU_LOSE_SOUND);
				lS.play();
			}
		}
	}

	/**
	 * updates observers
	 * @see Observer
	 * @param invoking's class name
	 */
	@Override
	public void updateObserver() {
		winningLosingScreen();
	}
}
