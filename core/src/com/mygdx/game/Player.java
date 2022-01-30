package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.lang.*;


public class Player{

    // the player states
    public enum playerState{
        Running,
        Falling,
        Jumping,
        Idle,
        HoldingGun
    }

    float x;
    float y;
    int width, height;
    double Xspeed, Yspeed;
    Rectangle hitBox;

    public float fallTime;
    float idle_animation_time;
    Boolean isFacingLeft;
    Texture outputTexture;
    Texture playerTexture;
    ObjectAnimation player_running_animation;
    ObjectAnimation player_jumping_animation;
    ObjectAnimation player_idle_animation;
    playerState state;

    public Player(float x, float y) {

        this.x = x;
        this.y = y;

        width = 200;
        height = 200;
        hitBox = new Rectangle(x, y, width, height);


        fallTime = 1f;
        state = playerState.Idle;
        idle_animation_time = 0;

        //set up the player animations
        player_running_animation = new ObjectAnimation();
        player_running_animation.loadAnimation("player_running_", 4);
        player_jumping_animation = new ObjectAnimation();
        player_jumping_animation.loadAnimation("player_jumping_", 2);
        player_idle_animation = new ObjectAnimation();
        player_idle_animation.loadAnimation("player_idle_", 1);
        playerTexture = new Texture(Gdx.files.internal("player_idle_1.png"));
        outputTexture = playerTexture;

    }

    public void PlayerInputHandling(Array<MapObject> Walls){


        //Horizontal Player input
        if ( (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && (Gdx.input.isKeyPressed(Input.Keys.LEFT)) || !(Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ){
            Xspeed *= 0.8;
        }
        else if( (Gdx.input.isKeyPressed(Input.Keys.LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ) Xspeed--;
        else if((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.LEFT))) Xspeed++;

        //Smooths Player Input
        if(Xspeed > 0 && Xspeed < 0.75) Xspeed = 0;
        if(Xspeed < 0 && Xspeed > -0.75) Xspeed = 0;
        if(Xspeed > 9) Xspeed = 9;
        if(Xspeed < -9 ) Xspeed = -9;

        //vertical input
        if((Gdx.input.isKeyPressed(Input.Keys.UP))){
            hitBox.y++;
            if(IsPlayerOnGround() == true){
                Yspeed += 14;
            }
            hitBox.y--;
        }
        Yspeed -= 0.6;

        //Horizontal Collision
        hitBox.x += Xspeed;
        for(MapObject i_wall : Walls) {
            if (hitBox.overlaps(i_wall.hitBox)) {
                Xspeed -= Xspeed;
            }

        }


        //Vertical Collision
        hitBox.y += Yspeed;
        for(MapObject i_wall : Walls){
            if(hitBox.overlaps(i_wall.hitBox)){
                Yspeed -= Yspeed;
            }
        }

        //Updates Position of the player
        x += Xspeed;
        y += Yspeed;

        hitBox.x = x;
        hitBox.y = y;
    }




    public Texture render(float delta, Array<MapObject> Walls) {

        IsPlayerOnGround();
        PlayerInputHandling(Walls);


        // checks if the player is moving up or down
        if (Yspeed > 0) {
            fallTime += delta;
            state = playerState.Jumping;
        }

        else if (Yspeed < 0){
            fallTime += delta;
            state = playerState.Falling;
        }


        // checks if the player is moving left or right
        if (Xspeed != 0) {
            if (Xspeed != 0 && Yspeed == 0) {
                state = playerState.Running;
            }

            if (Xspeed == -Xspeed) {
                isFacingLeft = true;
            }

            else {
                isFacingLeft = false;
            }
        }

        else if (Xspeed == 0 && Yspeed == 0) {
            state = playerState.Idle;
        }



        // checks which animation should play according to the state enum
        switch (state) {
            case Running:
                outputTexture = player_running_animation.getFrame(delta);
                player_jumping_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;


            case Jumping:
                if (player_jumping_animation.currentFrame >= player_jumping_animation.frames.size - 2){
                    outputTexture = player_jumping_animation.frames.get(player_jumping_animation.currentFrame);
                }

                else {
                    outputTexture = player_jumping_animation.getFrame(delta);
                }
                player_running_animation.resetAnimation();
                player_idle_animation.resetAnimation();
                idle_animation_time = 0;
                break;

            case Idle:
                if (idle_animation_time > 2f) {
                    outputTexture = player_idle_animation.getFrame(delta);

                    if (idle_animation_time > 3f) {
                        idle_animation_time = 0;
                    }
                }

                else outputTexture = playerTexture;

                player_running_animation.resetAnimation();
                player_jumping_animation.resetAnimation();
                idle_animation_time += delta;
                break;
        }

        // Need to be fixed
        /*

        if (isFacingLeft) {
            flip();
        }

         */


        // Returns the animation
        return outputTexture;
    }



    // flips the Player
    public void flip(){
        width = (width * -1);
    }

    //Check if Player on ground for jumping
    public boolean IsPlayerOnGround(){
        if(Yspeed == -Yspeed){
            return true;
        }
        else {
            return false;
        }
    }



    public void dispose(){
        player_running_animation.dispose();
        player_jumping_animation.dispose();
    }

}
