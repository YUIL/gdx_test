package com.yuil.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.yuil.game.input.InputProcessor;
import com.yuil.game.stage.StageManager;
import com.yuil.game.util.GameManager;

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
        StageManager.guiFactor.setStage(stage, guiXmlPath);

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
        
        
        
        String s="{list:[{a:1},{a:2}]}";
        String s2="{a:1}";

        JsonValue jsonValue;
        JsonReader jsonReader=new JsonReader();
        jsonValue =jsonReader.parse(s);
        JsonValue jsonValue2=jsonReader.parse(s2);
     
       System.out.println(jsonValue.get(0)); 
       System.out.println(jsonValue2.get(0));



    }

    @Override
    public void render(float delta) {
    	super.render(delta);
    	 stage.draw();
      //  System.out.println(System.currentTimeMillis() - lastRender);
      //  lastRender=System.currentTimeMillis();
    }
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
	}

}
