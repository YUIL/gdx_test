package com.mygdx.game.server;

import java.util.HashMap;
import java.util.Map;

import com.mygdx.game.database.dao.DaoFactory;
import com.mygdx.game.database.dao.UserDao;
import com.mygdx.game.entity.User;
import com.mygdx.game.entity.UserManager;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessageListener;

public class UserServer implements UdpMessageListener{
	UserManager userManager=new UserManager();
	UserDao userDao=DaoFactory.getInstance().getUserDao();
	
	Map<Integer,User> userMap =new HashMap<Integer,User>();
	
	public User login(String openId){
		
		User user=userDao.getUser(openId);
		if(user==null){
			user=new User();
			userDao.addUser(user);
		}
		userMap.put(user.getUser_id(), user);
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
		
	}


}
