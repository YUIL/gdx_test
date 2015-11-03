package com.yuil.game.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ErlUdpTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DatagramSocket socket=new DatagramSocket(9091);
		byte[] buf="asd".getBytes();
		DatagramPacket datagramPacket=new DatagramPacket(buf, buf.length, new InetSocketAddress("127.0.0.1",1234));
		socket.send(datagramPacket);
		socket.close();
	}

}
