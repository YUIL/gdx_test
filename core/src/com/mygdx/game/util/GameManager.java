package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.stage.StageManager;

public class GameManager {

	
	public static void setInputProcessor(Stage stage) {
		StageManager.superStage.setViewport(stage.getViewport());
		setInputProcessor((InputProcessor)stage);
	}
	public static void setInputProcessor(InputProcessor inputProcessor) {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(StageManager.superStage);
		multiplexer.addProcessor(inputProcessor);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public static void setInputProcessor(InputProcessor... inputProcessor) {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(StageManager.superStage);
		for (int i = 0; i < inputProcessor.length; i++) {
			multiplexer.addProcessor(inputProcessor[i]);
		}
		Gdx.input.setInputProcessor(multiplexer);
	}
	
}
