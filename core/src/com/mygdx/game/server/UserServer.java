package com.mygdx.game.server;

import com.mygdx.game.database.dao.DaoFactory;
import com.mygdx.game.database.dao.UserDao;
import com.mygdx.game.entity.User;
import com.mygdx.game.entity.UserManager;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessage;
import com.mygdx.game.net.udp.UdpMessageListener;

public class UserServer implements UdpMessageListener{
	UserManager userManager=new UserManager();
	UserDao userDao=DaoFactory.getInstance().getUserDao();
	
	public User login(String openId){
		//检查是否已经拥有该用户
		/*if(已拥有）{
			登录
		}else{
			注册
			登录
		}
		
		
		*/
		User user=userDao.getUser(openId);
		if(user==null){
			user=new User();
			userDao.addUser(user);
		}
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
