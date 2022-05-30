package com.amithalpert.contamination.Entities.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BulletOld {

    public static final int BULLET_MOVEMENT_SPEED = 2000;

    // bullet parameters
    double DirectionSpeed;
    double Xspeed;
    public int width = 20;
    public int height = 15;
    public float bulletX;
    public float bulletY;
    public Rectangle hitBox;
    Texture bulletTex;
    Texture bulletNotExisting;
    Texture outTexture;


    public BulletOld(float x, float y, boolean IsPlayerFacingLeft) {

        this.bulletX = x;
        this.bulletY = y;
        hitBox = new Rectangle(bulletX, bulletY, width, height);

        // changes the direction and sprite width
        if((IsPlayerFacingLeft && this.width > 0) || (!IsPlayerFacingLeft && this.width < 0)){

            // changes the sprite width
            FlipBulletSprite(IsPlayerFacingLeft);

            // move left
            DirectionSpeed = -BULLET_MOVEMENT_SPEED;

        }
        else{
            // move right
            DirectionSpeed = BULLET_MOVEMENT_SPEED;
        }



        outTexture = new Texture("bullet.png");
        bulletNotExisting = new Texture("player_dead_5.png");
        bulletTex = new Texture("bullet.png");


    }

    public Texture update(float delta) {


        Xspeed = DirectionSpeed;

        // updates bullets position;
        bulletX += Xspeed * Gdx.graphics.getDeltaTime();
        hitBox.x = bulletX;
        hitBox.y = bulletY;

        Xspeed = 0;


        return outTexture;
    }

    public void FlipBulletSprite(boolean IsPlayerFacingLeft){
        width = width * -1;
        bulletX = bulletX + width * -1;

            if (IsPlayerFacingLeft) {
            hitBox.width = (hitBox.width * -1) / 1.3f;
            hitBox.x = hitBox.x + hitBox.width * -1;
            }

    }

}