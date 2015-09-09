package com.mygdx.game.test;

import java.net.BindException;

import com.mygdx.game.net.udp.UdpServer;

public class ThreadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UdpServer server=new UdpServer(9999);
			server.start();
		} catch (BindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
