package com.nevena.threerules;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {


    @Override
    public void create() {
        // Switches to a new game screen by creating and setting an instance of GameScreen
        setScreen(new GameScreen());
    }

}
