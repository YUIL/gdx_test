package com.mygdx.game.server;

import java.net.BindException;

import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessageListener;
import com.mygdx.game.net.udp.UdpServer;

public class MainServer  implements UdpMessageListener{
	volatile UdpServer udpServer;
	volatile UserServer userServer;
	volatile NetTest7LogicServer netTest7LogicServer;
	public static void main(String[] args) throws BindException {
		MainServer mainServer=new MainServer();
		mainServer.start();
	}

	public MainServer() throws BindException {
		udpServer=new UdpServer(9093);
		netTest7LogicServer=new NetTest7LogicServer();
		netTest7LogicServer.udpServer=this.udpServer;
		udpServer.setUdpMessageListener(this);
		
	}
	public void start(){
		System.out.println("MainServer started");
		udpServer.start();
		netTest7LogicServer.start();
	}

	@Override
	public void disposeUdpMessage(Session session, byte[] data) {
		// TODO Auto-generated method stub
		
	}


}
