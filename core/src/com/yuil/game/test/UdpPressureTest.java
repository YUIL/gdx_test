package com.yuil.game.test;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Random;

import com.yuil.game.entity.message.C2S_TEST;
import com.yuil.game.net.message.GAME_MESSAGE;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessageListener;
import com.yuil.game.net.udp.UdpServer;

public class UdpPressureTest {
	static int sendNum=4000;
	volatile static long time;
	public class MessageProccessor implements UdpMessageListener{
		volatile int count=0;
		@Override
		public void disposeUdpMessage(Session session, byte[] data) {
			// TODO Auto-generated method stub
			//time=System.currentTimeMillis()-time;
			//System.out.println("time:"+time);
			count++;
			
			System.out.println(count);
			if (count==sendNum){
				time=System.currentTimeMillis()-time;
				System.out.println("time:"+time);
			}
		}
		
	}
	public static void main(String[] args) throws BindException {
		// TODO Auto-generated method stub
		
		//test1();
		//test2();
		test3();
	}
	static UdpServer udpServer;
	public static void test1() throws BindException{
		UdpPressureTest udpPressureTest=new UdpPressureTest();
		MessageProccessor messageProccessor=udpPressureTest.new  MessageProccessor();
		udpServer=new UdpServer(9091);
		udpServer.setUdpMessageListener(messageProccessor);
		udpServer.start();
		Random random=new Random();
		time=System.currentTimeMillis();
		for (int i = 0; i < sendNum; i++) {
			Session session=udpServer.createSession(random.nextLong(), new InetSocketAddress("127.0.0.1", 9093));
			C2S_TEST c2s_TEST=new C2S_TEST();
			GAME_MESSAGE game_MESSAGE=new GAME_MESSAGE(c2s_TEST.toBytes());
			
			udpServer.send(game_MESSAGE.toBytes(), session, false);
		}
	}
	
	public static void test2() throws BindException{
		UdpPressureTest udpPressureTest=new UdpPressureTest();
		MessageProccessor messageProccessor=udpPressureTest.new  MessageProccessor();
		UdpServer udpServer=new UdpServer(9091);
		udpServer.setUdpMessageListener(messageProccessor);
		udpServer.start();
		Random random=new Random();
		Session session=udpServer.createSession(random.nextLong(), new InetSocketAddress("127.0.0.1", 9093));
		time=System.currentTimeMillis();
		for (int i = 0; i < sendNum; i++) {
			C2S_TEST c2s_TEST=new C2S_TEST();
			GAME_MESSAGE game_MESSAGE=new GAME_MESSAGE(c2s_TEST.toBytes());
			
			udpServer.send(game_MESSAGE.toBytes(), session, false);
		}
	}
	
	public static void test3() throws BindException{
		UdpServer server1=new UdpServer(9091);
		UdpServer server2=new UdpServer(9092);
		server1.start();
		server2.start();

	}
	
	
}
