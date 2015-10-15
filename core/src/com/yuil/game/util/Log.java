package com.yuil.game.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	static Date date=new Date();
	static SimpleDateFormat timeFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	
	private static void p(String str){
		date.setTime(System.currentTimeMillis());
		System.out.print(timeFormat.format(date)+"  ");
		System.out.println(str);
	}
	public static void println(String str){
		p(str);
	}
	
	
	
	public static void println(int i){
		p(String.valueOf(i));
	}
	
	public static void println(Object o){
		p(o.toString());
	}
}
