package com.yuil.game.net;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Random;

import com.yuil.game.net.message.Message;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessageListener;
import com.yuil.game.net.udp.UdpServer;

public class ClientSocket implements UdpMessageListener {
	volatile String remoteIp = null;
	volatile int remotePort;
	volatile UdpServer udpServer;
	volatile Session session;
	UdpMessageListener listenner = null;

	public ClientSocket() {
		super();
	}

	public ClientSocket(int port, String remoteIp, int remotePort, UdpMessageListener listener) {
		super();
		this.remoteIp = remoteIp;
		this.remotePort = remotePort;
		this.listenner = listener;
		if (initUdpServer(port)) {
			udpServer.start();
		}
	}

	private boolean initUdpServer(int port) {
		if (port < 10000) {
			try {
				System.out.println("try start at port:" + port);
				udpServer = new UdpServer(port);
				udpServer.setUdpMessageListener(this);
				return true;
			} catch (BindException e) {
				System.out.println(port + " exception!");
				port++;
				return initUdpServer(port);
			}
		} else {
			System.err.println("port must <10000!");
			return false;
		}

	}

	public boolean sendMessage(String str,boolean isImmediately) {
		if (str == null) {
			System.err.println("message==null");
		} else {
			sendMessage(str.getBytes(),isImmediately);
		}
		return false;

	}


	public synchronized boolean sendMessage(byte[] bytes,boolean isImmediately) {
		if (udpServer == null) {
			System.err.println("updServer==null");
			return false;
		} else {
			if (session == null) {
				System.err.println("session==null");
				session = udpServer.createSession(new Random().nextLong(), new InetSocketAddress(remoteIp, remotePort));
				System.out.println("session id:" + session.getId());
			}
			return udpServer.send(bytes, session,isImmediately);
		}
	}
	public UdpServer getUdpServer() {
		return this.udpServer;
	}

	public void close() {
		if (udpServer != null) {
			udpServer.stop();
		}
	}

	@Override
	public void disposeUdpMessage(Session session, byte[] data) {
		// TODO Auto-generated method stub
		if (data.length > Message.TYPE_BYTE_LENGTH) {
			listenner.disposeUdpMessage(session, data);
		}
	}
}
