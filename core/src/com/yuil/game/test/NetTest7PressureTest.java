package com.yuil.game.test;

import com.yuil.game.entity.message.C2S_TEST;
import com.yuil.game.net.ClientSocket;
import com.yuil.game.net.message.GAME_MESSAGE;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessageListener;

public class NetTest7PressureTest {
	static byte[] testData;
	static final int testInterval=100;
	static final int clientNum=100;
	static final int sendTimes=100;
	
	static volatile long time;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final NetTest7PressureTest test=new NetTest7PressureTest();
		time=System.currentTimeMillis();
		for (int i = 0; i < clientNum; i++) {
			Thread t=new Thread(){
				public void run(){
					test.test();
				}
			};
			t.start();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public NetTest7PressureTest() {
		super();
		testData =new GAME_MESSAGE(new C2S_TEST().toBytes()).toBytes();
	}


	public void test(){

		ClientSocket clientSocket=new ClientSocket(9091, "123.57.14.122", 9093,new Listener());
		for (int i = 0; i <sendTimes; i++) {
			
		
			clientSocket.send(testData, false);
			try {
				Thread.sleep(testInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public class Listener implements UdpMessageListener{
		int recvCount=0;
		@Override
		public void disposeUdpMessage(Session session, byte[] data) {
			// TODO Auto-generated method stub
			recvCount++;
			if (recvCount%10f==0f){
				System.out.println(recvCount);
				if(recvCount==sendTimes){
					System.out.println("time:"+(System.currentTimeMillis()-time));
				}
			}
		}
	}
}
