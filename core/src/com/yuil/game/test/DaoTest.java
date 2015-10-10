package com.yuil.game.test;

import com.yuil.game.database.dao.DaoFactory;
import com.yuil.game.database.dao.UserDao;
import com.yuil.game.entity.User;

public class DaoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserDao userDao=DaoFactory.getInstance().getUserDao();
		User user=new User();
		user.setOpenId("123");
		userDao.addUser(user);
	}

}
