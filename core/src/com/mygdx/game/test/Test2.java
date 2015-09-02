package com.mygdx.game.test;

import java.net.InetSocketAddress;
import java.util.Random;

import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UdpServer server=new UdpServer(9092);
		Session session=new Session(new Random().nextLong());
		session.setContactorAddress(new InetSocketAddress("127.0.0.1", 9091));
		server.sessionMap.put(session.getId(), session);
		server.start();
		
		
		int delay=1000;
		long lastSendTime=0;
		while(true){
			if(System.currentTimeMillis()-lastSendTime>delay){
				lastSendTime=System.currentTimeMillis();
				String str=String.valueOf(System.currentTimeMillis());
				server.send(str.getBytes(), session);
			}
		}
	}

}
