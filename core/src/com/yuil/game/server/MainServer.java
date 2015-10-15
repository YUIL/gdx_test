package com.yuil.game.server;

import java.net.BindException;

import com.yuil.game.entity.message.GameMessageType;
import com.yuil.game.net.message.Message;
import com.yuil.game.net.message.MessageType;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessageListener;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.util.DataUtil;

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
		udpServer.setUdpMessageListener(this);
		
		netTest7LogicServer=new NetTest7LogicServer();
		netTest7LogicServer.udpServer=this.udpServer;
		
		userServer=new UserServer();
		userServer.udpServer=this.udpServer;
		
	}
	public void start(){
		System.out.println("MainServer started");
		udpServer.start();
		netTest7LogicServer.start();
	}

	@Override
	public void disposeUdpMessage(Session session, byte[] data) {
		if (data.length<Message.TYPE_BYTE_LENGTH) {
			return;
		}
		//System.out.println("data.length:"+data.length);
		int typeOrdinal = DataUtil.bytesToInt(DataUtil.subByte(data, Message.TYPE_BYTE_LENGTH, 0));
		//System.out.println("type:" + GameMessageType.values()[typeOrdinal]);
		byte[] src = DataUtil.subByte(data, data.length - Message.TYPE_BYTE_LENGTH, Message.TYPE_BYTE_LENGTH);
		switch (MessageType.values()[typeOrdinal]) {
		case USER_MESSAGE:
			userServer.disposeUdpMessage(session, src);
			break;
		case GAME_MESSAGE:
			netTest7LogicServer.disposeUdpMessage(session, src);
			break;
		default:
			break;
		}
	}
	


}
