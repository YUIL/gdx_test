package com.yuil.game.test;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Random;

import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessage;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.util.DataUtil;

public class UdpTest {

	public static void main(String[] args) throws BindException {
	
		
		
		
		

	}
	
	public static void sendMessages() throws BindException{
		UdpServer server=new UdpServer(9092);
		server.start();
		UdpServer client=new UdpServer(9091);
		client.start();
		Session session=client.createSession(new Random().nextLong(), new InetSocketAddress("127.0.0.1",9092));
		 
		byte[] data=new byte[1];
		
		client.send(data, session, false);
		client.send(data, session, false);
		client.send(data, session, false);
	}

}
