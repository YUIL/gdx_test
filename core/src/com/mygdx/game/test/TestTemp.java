package com.mygdx.game.test;


import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.game.entity.GameObject;
import com.mygdx.game.net.udp.UdpMessage;

public class TestTemp {
	static int length=1;
	static long startTime=0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		UdpMessage m=new UdpMessage();
		m.setSequenceId(1);
		m.setSessionId(1);
		m.setType((byte)1);
		UdpMessage message=new UdpMessage();
		message.initUdpMessageByDatagramPacket(message, m.toBytes());
		System.out.println(message.getData());
		/*ArrayMap<Long,GameObject> map=new ArrayMap<Long, GameObject>();
		Array<GameObject> array=new Array<GameObject>();
		generateMap(map);
		generateArray(array);
		testMap(map);
		testArray(array);*/
	}
	
	public static void testArray(Array<GameObject> array){
		Object tempObj=null;
		int times=0;
		startTime=System.nanoTime();
		for (int i = 0; i < array.size; i++) {
			if(!array.get(i).getName().equals("a")){
				tempObj=array.get(i);
				times++;
			}
		}
		
		startTime=System.nanoTime()-startTime;
		System.out.println("testarray:"+startTime);
		System.out.println("times:"+times);
	}
	
	public static void testMap(ArrayMap<Long, GameObject> map){
		Object tempObj=null;
		int times=0;
		startTime=System.nanoTime();
		for (com.badlogic.gdx.utils.ObjectMap.Entry<Long, GameObject> entry : map.entries()) {
			tempObj=entry.value;
			times++;
		}
		startTime=System.nanoTime()-startTime;
		System.out.println("testmap:"+startTime);
		System.out.println("times:"+times);
	}
	
	public static void generateMap(ArrayMap map){
		for (int i = 0; i < length; i++) {
			map.put(new Random().nextLong(), new GameObject("asd"));
		}
		
		
	}
	public static void generateArray(Array<GameObject> array){
		for (int i = 0; i < length; i++) {
			array.add(new GameObject("asd"));
		}
	}
	

}
