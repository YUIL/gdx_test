package com.yuil.game.test;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Random;

import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessage;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.util.ByteUtil;

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
		message.setData(ByteUtil.intToBytes(1));
	//	session.currentSendUdpMessage(message);
		server.sessionArray.add(session);
		

	}

}
