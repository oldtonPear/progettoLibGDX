package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.coreGame.Parameters;

public class Level extends Screen{

    private float skySpeed;
    
    private float backgroundY;

    private float width;
    private float height;
    
    private Texture skyPicture;
    
    
    private Dragon dragon;
	private Plane[] planes;

    Level(String name, float height){
        super();

        this.skySpeed = -0.005f;

        this.backgroundY = 0;

        this.height = height;
        this.width = height * Parameters.getAspectRatio();

        this.skyPicture = ResourceLoader.getTexture(ResourceEnum.SKY);
        
        planes = new Plane[20];
		for (int i = 0; i < planes.length; i++) {
			if(i%2==0) planes[i] = new Stealth_plane();
            else planes[i] = new War_plane();
			planes[i].setY((float)(3.5+Math.random()*30));
			planes[i].setX((float)(Math.random()*3));
		}
		dragon = new Dragon();
        
    }

    public float getSpeed() {
        return skySpeed;
    }
    public void setSpeed(float speed) {
        this.skySpeed = speed;
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(skyPicture, 0, backgroundY, width, height);
        sb.draw(skyPicture, 0, backgroundY + height, width, height);

        dragon.update();
		dragon.draw(sb);

		for (int i = 0; i < planes.length; i++) {
			planes[i].update();
			planes[i].draw(sb);
		}

    }

    public void update(){
        backgroundY += skySpeed;
        if(backgroundY <= -height) backgroundY += height;

        for (int j = 0; j < planes.length; j++) {
            if(dragon.collidesWidth(planes[j])){
                dragon.manageCollisionWith(planes[j]);
                planes[j].manageCollisionWith(dragon);
            } 
        }
    } 

    public void moveDragon(char character){
        if(character == 'a'){
			if(dragon.getX() > 0f)
			dragon.setX(dragon.getX() - 0.05f);
		}
		if(character == 'd'){
			if(dragon.getX() < 4-dragon.getWidth())
			dragon.setX(dragon.getX() + 0.05f);
		}
    }
}