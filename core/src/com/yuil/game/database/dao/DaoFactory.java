package com.yuil.game.database.dao;

import com.yuil.game.database.dao.imp.UserDaoImp;

public class DaoFactory {
	private static UserDao userDao = null;
	private static DaoFactory instance = new DaoFactory();

	private DaoFactory() {
		userDao=new UserDaoImp();
	}

	public static DaoFactory getInstance() {
		return instance;
	}

	public UserDao getUserDao() {
		return userDao;
	}
}
