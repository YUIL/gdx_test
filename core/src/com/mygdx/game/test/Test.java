package com.mygdx.game.test;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonValue jsonValue;
		JsonReader jsonReader=new JsonReader();
		String str="{name:asd}";
		long time=0;
		time=System.currentTimeMillis();
		
		jsonValue=jsonReader.parse(str);
		str=jsonValue.getString("name");
		time=System.currentTimeMillis()-time;
		System.out.println(time);
	}

}
