package com.mygdx.game.test;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Test {
	static String str="{name:asd}";
	static long time=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*JsonValue jsonValue;
		JsonReader jsonReader=new JsonReader();
		time=System.currentTimeMillis();
		
		
		jsonValue=jsonReader.parse(str);

		time=System.currentTimeMillis()-time;
		System.out.println(time);*/
		//System.out.println("{cgo:{name:yuil,p:{x:950.0,y:100.0},i:{x:0,y:0}}}".length());
		//System.out.println("{asdasdasdasd}".getBytes().length);
		int x=1;
		int x2=2;
		int x3=3;
		
		x2=x;
		x=x3;
		System.out.println(x);
		System.out.println(x2);
		System.out.println(x3);
	}

}
