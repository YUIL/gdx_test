package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.inputprocessor.InputProcessor;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;

import java.util.Arrays;


/**
 * Created by i008 on 2015/8/25.
 */
public class UiTest2Screen extends TestScreen
{
    long lastRender=0;
    Stage stage;
    String guiXmlPath;
    public UiTest2Screen(Game game) {
        super(game);
        stage=new Stage();
        guiXmlPath="gui/UiTest2Gui.xml";
        StageManager.guiFactor.setStageFromXml(stage, guiXmlPath);

        TextButton textButton1=new TextButton("button1",StageManager.defaultSkin);
        TextButton textButton2=new TextButton("button2",StageManager.defaultSkin);
        Table table =new Table(StageManager.defaultSkin);
        Table table1=new Table(StageManager.defaultSkin);
        table1.pad(10f);
        Table table2=new Table(StageManager.defaultSkin);
        table2.pad(10f);
        table1.add(textButton1);
        table2.add(textButton2);
        table.setX(100);
        table.setY(100);
        table.add(table1);
        table.row();
        table.add(table2);
        table.row();
        stage.addActor(table);
        GameManager.setInputProcessor(stage);
        System.out.println(textButton1.hasChildren());
        String s="{type:1,x:1,y:1,login:{name:'a'}}";


        JsonValue jsonValue;
        JsonReader jsonReader=new JsonReader();
        jsonValue =jsonReader.parse(s);
      
       System.out.println(jsonValue.get("login").get("name").asString()); 



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
       // stage.draw();
      //  System.out.println(System.currentTimeMillis() - lastRender);
      //  lastRender=System.currentTimeMillis();
        InputProcessor.handleInput(game, delta);
    }
}
