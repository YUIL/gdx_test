package com.mygdx.game.test;

import com.mygdx.game.database.dao.DaoFactory;
import com.mygdx.game.database.dao.UserDao;
import com.mygdx.game.entity.User;

public class DaoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserDao userDao=DaoFactory.getInstance().getUserDao();
		User user=new User();
		user.setOpenId("123");
		userDao.addUser(user);
	}

}
