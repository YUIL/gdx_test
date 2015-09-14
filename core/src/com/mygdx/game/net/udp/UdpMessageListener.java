package com.mygdx.game.net.udp;

public interface UdpMessageListener {
	public void disposeUdpMessage(Session session,UdpMessage message);
}
