package com.mygdx.game.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.SerializablePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.message.GameMessage;
import com.mygdx.game.entity.message.GameMessageType;
import com.mygdx.game.net.udp.UdpMessage;
import com.mygdx.game.test.Test.PhoneNumber;
import com.mygdx.game.util.ByteUtil;
import com.sun.org.apache.xml.internal.utils.SerializableLocatorImpl;

public class Test {
	static String str = "{name:asd}";
	static long time = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * JsonValue jsonValue; JsonReader jsonReader=new JsonReader();
		 * time=System.currentTimeMillis();
		 * 
		 * 
		 * jsonValue=jsonReader.parse(str);
		 * 
		 * time=System.currentTimeMillis()-time; System.out.println(time);
		 */
		// System.out.println("{cgo:{name:yuil,p:{x:950.0,y:100.0},i:{x:0,y:0}}}".length());
		// System.out.println("{asdasdasdasd}".getBytes().length);
		/*
		 * int x=1; int x2=2; int x3=3;
		 * 
		 * x2=x; x=x3; System.out.println(x); System.out.println(x2);
		 * System.out.println(x3);
		 */

		/*
		 * long lastWhileTime=0;
		 * 
		 * Runnable r1=new Class1(); Thread t1=new Thread(r1);
		 * 
		 * while(true){ if(System.currentTimeMillis()-lastWhileTime>1000){
		 * lastWhileTime=System.currentTimeMillis(); t1.start(); }
		 * 
		 * }
		 */
		/*
		 * //创建一个可重用固定线程数的线程池 ExecutorService pool =
		 * Executors.newFixedThreadPool(2);
		 * //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口 Thread t1 = new
		 * MyThread(); Thread t2 = new MyThread(); Thread t3 = new MyThread();
		 * Thread t4 = new MyThread(); Thread t5 = new MyThread(); //将线程放入池中进行执行
		 * pool.execute(t1); pool.execute(t2); pool.execute(t3);
		 * pool.execute(t4); pool.execute(t5); //关闭线程池 pool.shutdown();
		 */
		// UdpMessage message=new UdpMessage();

		/*
		 * Person person = new Person(); System.out.println(person.toString());
		 * person.setName("Nate"); person.setAge(31); person.setId(1);
		 * person.setBytes("Asd".getBytes());
		 * 
		 * 
		 * ArrayList numbers = new ArrayList(); numbers.add(new
		 * PhoneNumber("home","206-555-1234")); numbers.add(new
		 * PhoneNumber("work","425-555-4321")); person.setNumbers(numbers);
		 * 
		 * Json json = new Json();//Json对象的创建及初始化
		 *//**
			 * json.toJson(person):将person对象序列化成json字符串
			 */
		/*
		 * // System.out.println( "-------->" + json.toJson(person));
		 * 
		 *//**
			 * 为Person中的名为numbers时的成员变量指定类型名为PhoneNumber
			 */
		/*
		 * // json.setElementType(Person.class, "numbers", PhoneNumber.class);
		 * 
		 *//**
			 * 如果在JSON中对类的写操作无法避免的时候,我们可以给类起一个别名
			 * 
			 * json.addClassTag("phoneNumber", PhoneNumber.class)在输出的时候给
			 * PhoneNumber类起了一个别名phoneNumber
			 */
		/*
		 * //json.addClassTag("phoneNumber", PhoneNumber.class);
		 * 
		 *//**
			 * prettyPrint(person):格式化输出
			 *//*
			 * System.out.println(json.toJson(person));
			 */
/*
		GameMessage m = new GameMessage();
		System.out.println();

		ByteArrayOutputStream bao=new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bao);// 输出流保存的文件名为
																							// my.out
																							// ；ObjectOutputStream能把Object输出成Byte流
			oos.writeObject(m);
			oos.flush(); // 缓冲流
			oos.close(); // 关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(bao.toByteArray().length);
		fan(new ByteArrayInputStream(bao.toByteArray()));// 调用下面的 反序列化 代码
*/	
		/*Class1 c1=new Class1();
		testfun(c1);
		System.out.println(c1.s);*/
		System.out.println(State.getStateByIndex(1));
	}
	
	public static void testfun(Object c1){
		((Class1)c1).s="ASd";
	}

	public static class Class1{
		public String s;
	}
	public static void fan(InputStream in)// 反序列的过程
	{
		ObjectInputStream oin = null;// 局部变量必须要初始化
		try {
			oin = new ObjectInputStream(in);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		GameMessage m = null;
		try {
			m = (GameMessage) oin.readObject();// 由Object对象向下转型为MyTest对象
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("type=" + m.toString());

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
