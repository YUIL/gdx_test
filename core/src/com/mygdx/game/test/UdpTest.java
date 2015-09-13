package com.mygdx.game.test;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Random;

import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessage;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.util.JavaDataConverter;

public class UdpTest {

	public static void main(String[] args) throws BindException {
		// TODO Auto-generated method stub
		UdpServer server=new UdpServer(9091);
		server.start();
		Session session=new Session(new Random().nextLong());
		session.setContactorAddress( new InetSocketAddress("127.0.0.1",9092));
		
		UdpMessage message=new UdpMessage();
		message.setSessionId(session.getId());
		message.setSequenceId(0);
		message.setType((byte)1);
		message.setLength(4);
		message.setData(JavaDataConverter.intToBytes(1));
		session.currentSendUdpMessage(message);
		server.sessionArray.add(session);
		

	}

}
