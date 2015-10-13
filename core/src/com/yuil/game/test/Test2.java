package com.yuil.game.test;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Random;

import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpServer;

public class Test2 {
	static int delay=1000;
	static long lastSendTime=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UdpServer server=null;
		try {
			server = new UdpServer(9092);
		} catch (BindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Session session=new Session(new Random().nextLong());
		session.setContactorAddress(new InetSocketAddress("127.0.0.1", 9091));
		server.sessionArray.add(session);
		server.start();
		
		
		
		while(true){
			if(System.currentTimeMillis()-lastSendTime>delay){
				lastSendTime=System.currentTimeMillis();
				String str=String.valueOf(System.currentTimeMillis());
				server.send(str.getBytes(), session,false);
			}
		}
	}

}
