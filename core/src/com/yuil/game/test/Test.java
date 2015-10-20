package com.yuil.game.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.yuil.game.net.message.Message;

public class Test {
	static long time = 0;

	public static void main(String[] args) {
		testfun();
		testfun();
		testfun();
	}
	
	
	public static void testfun(){
		time=System.nanoTime();
		int i=new Random().nextInt();
		time=System.nanoTime()-time;
		System.out.println("time:"+time);
	}
	public static void testfun(Object c1){
		((Class1)c1).s="ASd";
	}

	public static class Class1{
		public String s;
	}
	public static Message fan(InputStream in)// 反序列的过程
	{
		ObjectInputStream oin = null;// 局部变量必须要初始化
		try {
			oin = new ObjectInputStream(in);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Message m = null;
		try {
			m = (Message) oin.readObject();// 由Object对象向下转型为MyTest对象
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println( m.toString());
		return m;
	}

	public static class Person {
		private String name;
		private int age;
		private ArrayList numbers;
		private byte[] bytes = "asdsad".getBytes();
		private int id;

		public Person() {

		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public ArrayList getNumbers() {
			return numbers;
		}

		public void setNumbers(ArrayList numbers) {
			this.numbers = numbers;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + ", numbers=" + numbers + ", bytes="
					+ Arrays.toString(bytes) + "]";
		}

	}

	public static class PhoneNumber {
		private String name;
		private String number;

		public PhoneNumber() {
			// TODO Auto-generated constructor stub
		}

		public PhoneNumber(String name, String number) {
			super();
			this.name = name;
			this.number = number;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

	}

	/*
	 * static class MyThread extends Thread{
	 * 
	 * @Override public void run() {
	 * System.out.println(Thread.currentThread().getName()+"正在执行。。。"); } }
	 */

}
