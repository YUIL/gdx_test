package com.yuil.game.test;

public class ThreadLockTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadLockTest test=new ThreadLockTest();
		test.test1();
	}

	
	public void test1(){
		Thread t1=new Thread(new Task());
		Thread t2=new Thread(new Task2());
		t1.start();
		t2.start();
		
	}
	

	
	public class Task implements Runnable{

		int count=0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//showName();
			fun();
		}
		public synchronized void showName(){
			while(true){
				System.out.println(Thread.currentThread().getName()+"  "+count++);
			}
		}
		
	}
	
	public class Task2 implements Runnable{

		int count=0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//showName();
			fun2();
		}
		public synchronized void showName(){
			while(true){
				System.out.println(Thread.currentThread().getName()+"  "+count++);
			}
		}
		
	}
	
	public synchronized static void fun(){
		while(true){
			System.out.println(Thread.currentThread().getName());
		}
	}
	public synchronized static void fun2(){
		while(true){
			System.out.println(Thread.currentThread().getName());
		}
	}
}
