package com.mygdx.game.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		/*int x=1;
		int x2=2;
		int x3=3;
		
		x2=x;
		x=x3;
		System.out.println(x);
		System.out.println(x2);
		System.out.println(x3);*/
		
	/*	long lastWhileTime=0;
		
		Runnable r1=new Class1();
		Thread t1=new Thread(r1);
		
		while(true){
			if(System.currentTimeMillis()-lastWhileTime>1000){
				lastWhileTime=System.currentTimeMillis();
				t1.start();
			}
			
		}*/
		 //创建一个可重用固定线程数的线程池 
        ExecutorService pool = Executors.newFixedThreadPool(2); 
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口 
        Thread t1 = new MyThread(); 
        Thread t2 = new MyThread(); 
        Thread t3 = new MyThread(); 
        Thread t4 = new MyThread(); 
        Thread t5 = new MyThread(); 
        //将线程放入池中进行执行 
        pool.execute(t1); 
        pool.execute(t2); 
        pool.execute(t3); 
        pool.execute(t4); 
        pool.execute(t5); 
        //关闭线程池 
        pool.shutdown(); 
	}
	static class MyThread extends Thread{ 
        @Override 
        public void run() { 
                System.out.println(Thread.currentThread().getName()+"正在执行。。。"); 
        } 
}
	
	
}
