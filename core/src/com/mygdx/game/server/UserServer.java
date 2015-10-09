package com.mygdx.game.server;

import java.util.HashMap;
import java.util.Map;

import com.mygdx.game.database.dao.DaoFactory;
import com.mygdx.game.database.dao.UserDao;
import com.mygdx.game.entity.User;
import com.mygdx.game.entity.UserManager;
import com.mygdx.game.entity.message.C2S_LOGIN;
import com.mygdx.game.entity.message.GameMessageType;
import com.mygdx.game.net.message.Message;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessageListener;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.util.ByteUtil;

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
		if (data.length<Message.TYPE_BYTE_LENGTH) {
			return;
		}
		System.out.println("disposing");
		// disposing=true;
		int typeOrdinal = ByteUtil.bytesToInt(ByteUtil.subByte(data, Message.TYPE_BYTE_LENGTH, 0));
		System.out.println("type:" + GameMessageType.values()[typeOrdinal]);
		byte[] src = ByteUtil.subByte(data, data.length - Message.TYPE_BYTE_LENGTH, Message.TYPE_BYTE_LENGTH);
		switch (GameMessageType.values()[typeOrdinal]) {
		case C2S_LOGIN:
			System.out.println("login");
			C2S_LOGIN message = new C2S_LOGIN(src);
			login(message.openId);
			break;
		case S2C_B2D_REMOVE_GAMEOBJECT:

			break;
		default:
			break;
		}

	}


}
