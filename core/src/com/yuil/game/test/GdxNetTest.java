package com.yuil.game.test;

import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.NetJavaServerSocketImpl;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.SocketHints;

public class GdxNetTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket serverSocket=new NetJavaServerSocketImpl(Protocol.TCP, 9091, new ServerSocketHints());
		serverSocket.accept(new SocketHints());
	}

}
