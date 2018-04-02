package com.asd.sokoban;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SokobanGame extends Game {
	SpriteBatch batch;
	Texture img;

	public SplashScreen spashScreen;
	public FirstScreen firstScreen;
	public ShopScreen shopScreen;





	@Override
	public void create () {
		// menuScreen = new MenuScreen(this);
		spashScreen = new SplashScreen(this);
		//firstScreen = new FirstScreen(this);
		setScreen(spashScreen);
	}


	public void switchToFirstScreen() {
		firstScreen = new FirstScreen(this);
		setScreen(firstScreen);
	}

	public void switchToShopScreen() {
		shopScreen = new ShopScreen(this);
		setScreen(shopScreen);
	}

	@Override
	public void render() {
		super.render();
	}


}



