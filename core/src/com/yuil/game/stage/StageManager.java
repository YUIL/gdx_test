package com.yuil.game.stage;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yuil.game.gui.GuiFactory;
import com.yuil.game.input.ActorInputListenner;
import com.yuil.game.screen.MainMenuScreen;

public class StageManager {

	public static Game game;
	public static GuiFactory guiFactor = new GuiFactory();
	public static Stage superStage = new Stage(new ScreenViewport());
	public static Skin defaultSkin = new Skin(
			Gdx.files.internal("data/uiskin.json"));
	public static TextButton textButton = (TextButton) guiFactor
			.getActorByNameFromXML("gui/SuperStageGui.xml", "MainMenu",
					defaultSkin);

	public static ActorInputListenner testInputListenner;
	public static void init() {
		testInputListenner=new ActorInputListenner(textButton) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				System.out.println(getActor().getName()+"'s testInput!");
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		};
		textButton.addListener(new ActorInputListenner(textButton) {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				if (x < 0 
						|| x > getActor().getWidth() 
						|| y < 0
						|| y > getActor().getHeight()) {
					return;
				}
				game.setScreen(new MainMenuScreen(game));
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		});
		superStage.addActor(textButton);
	}
	public static void showSuperStage(float delta){
		superStage.act(delta);
		superStage.draw();
	}
}
