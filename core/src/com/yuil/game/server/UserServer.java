package com.yuil.game.server;

import java.util.HashMap;
import java.util.Map;

import com.yuil.game.database.dao.DaoFactory;
import com.yuil.game.database.dao.UserDao;
import com.yuil.game.entity.User;
import com.yuil.game.entity.UserManager;
import com.yuil.game.entity.message.C2S_LOGIN;
import com.yuil.game.entity.message.GameMessageType;
import com.yuil.game.entity.message.S2C_LOGIN_SUCCESS;
import com.yuil.game.net.message.Message;
import com.yuil.game.net.message.USER_MESSAGE;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessageListener;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.util.DataUtil;

public class UserServer implements UdpMessageListener{
	volatile UdpServer udpServer;
	UserManager userManager=new UserManager();
	UserDao userDao=DaoFactory.getInstance().getUserDao();
	
	Map<Integer,User> userMap =new HashMap<Integer,User>();
	
	public User login(String openId){
		
		User user=userDao.getUser(openId);
		if(user==null){
			user=new User();
			user.setOpenId(openId);
			userDao.addUser(user);
		}
		userMap.put(user.getUserId(), user);
		System.out.println("login success!!");
		return user;
	}
	public boolean isLogon(String openId){
		User user=userDao.getUser(openId);
		if(user!=null){
			return true;
		}
		
		return false;
	}
	@Override
	public void disposeUdpMessage(Session session, byte[] data) {
		// TODO Auto-generated method stub
		if (data.length<Message.TYPE_LENGTH) {
			return;
		}
		System.out.println("disposing");
		// disposing=true;
		int typeOrdinal = DataUtil.bytesToInt(DataUtil.subByte(data, Message.TYPE_LENGTH, 0));
		System.out.println("type:" + GameMessageType.values()[typeOrdinal]);
		byte[] src = DataUtil.subByte(data, data.length - Message.TYPE_LENGTH, Message.TYPE_LENGTH);
		switch (GameMessageType.values()[typeOrdinal]) {
		case C2S_LOGIN:
			System.out.println("login");
			C2S_LOGIN message = new C2S_LOGIN(src);
			User user=login(message.openId);
			USER_MESSAGE responseMessage=new USER_MESSAGE(new S2C_LOGIN_SUCCESS(DataUtil.intToBytes(user.getUserId())).toBytes());
			udpServer.send(responseMessage.toBytes(), session,false);
			break;
		case S2C_B2D_REMOVE_GAMEOBJECT:

			break;
		default:
			break;
		}

	}


}
