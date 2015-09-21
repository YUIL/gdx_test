package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.game.gui.GuiFactory;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;

public class MainMenuScreen extends TestScreen2D {
	
	
	BitmapFont font;
	GuiFactory guiFactory;
	String guiXmlPath="gui/MainMenuGui.xml";
	public MainMenuScreen(Game game) {

		super(game);
		font = new BitmapFont();
		guiFactory = new GuiFactory();
		guiFactory.setStageFromXml(stage, guiXmlPath, skin);
		inputProcess();
		//<tempCode>
		Window window=new Window("main", skin);
		Actor actor=guiFactory.getActorByNameFromXML(guiXmlPath, "TestButton", skin);
		actor.addListener(StageManager.testInputListenner);
		window.add(actor);
		stage.addActor(window);
		stage.addActor(new TextButton("0906", skin));
		//</tempCode>
		GameManager.setInputProcessor(stage);
		
		font=new BitmapFont();
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("show");
		super.show();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		super.render(delta);
		stage.act(delta);
		stage.draw();

		batch.begin();
		font.draw(batch, "111", 100, 100);
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		//System.out.println("resize");
		super.resize(width, height);
		
		
	}

	@Override
	public void pause() {
		System.out.println("pause");
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		System.out.println("resume");
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		System.out.println("hide");
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		System.out.println("dispose");
		// TODO Auto-generated method stub

		font.dispose();
		batch.dispose();
		stage.dispose();
		skin.dispose();

	}

	public void inputProcess() {

		stage.getRoot().findActor("BulletTest").addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new BulletTestScreen(game));
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		});

		stage.getRoot().findActor("ShapeTest").addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new ShapeTestScreen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		});

		stage.getRoot().findActor("ModelLoad").addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new ModelLoadScreen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("RayPickingTest")
				.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y,
										int pointer, int button) {
						game.setScreen(new RayPickingTestScreen(game));
						return;
					}

					public boolean touchDown(InputEvent event, float x,
											 float y, int pointer, int button) {
						return true;
					}
				});
		stage.getRoot().findActor("ShaderTest2")
				.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						game.setScreen(new ShaderTest2Screen(game));
						return;
					}

					public boolean touchDown(InputEvent event, float x,
							float y, int pointer, int button) {
						return true;
					}
				});
		stage.getRoot().findActor("FrustumCullingTest")
				.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						game.setScreen(new FrustumCullingTestScreen(game));
						return;
					}

					public boolean touchDown(InputEvent event, float x,
							float y, int pointer, int button) {
						return true;
					}
				});
		
		stage.getRoot().findActor("UiTest")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				game.setScreen(new UiTestScreen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
									 float y, int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("UiTest2")
				.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y,
										int pointer, int button) {
						game.setScreen(new UiTest2Screen(game));
						return;
					}

					public boolean touchDown(InputEvent event, float x,
											 float y, int pointer, int button) {
						return true;
					}
				});
		
		stage.getRoot().findActor("NetTest")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				game.setScreen(new NetTestScreen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
									 float y, int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("NetTest2")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new NetTest2Screen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
					float y, int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("NetTest3")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new NetTest3Screen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
					float y, int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("NetTest4")
				.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y,
										int pointer, int button) {
						game.setScreen(new NetTest4Screen(game));
						return;
					}

					public boolean touchDown(InputEvent event, float x,
											 float y, int pointer, int button) {
						return true;
					}
		});
		stage.getRoot().findActor("ViewportTest1")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				game.setScreen(new ViewportTest1Screen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
									 float y, int pointer, int button) {
				return true;
			}
		});
		
		stage.getRoot().findActor("NetTest7")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				game.setScreen(new NetTest7Screen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
									 float y, int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("B2dTest1")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				game.setScreen(new B2dTest1Screen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
									 float y, int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("NetTest6")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				game.setScreen(new NetTest6Screen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
									 float y, int pointer, int button) {
				return true;
			}
		});
		stage.getRoot().findActor("BtTest1")
		.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
				System.out.println("1");
				game.setScreen(new BtTest1Screen(game));
				return;
			}

			public boolean touchDown(InputEvent event, float x,
									 float y, int pointer, int button) {
				return true;
			}
		});
	}

	
	
}
