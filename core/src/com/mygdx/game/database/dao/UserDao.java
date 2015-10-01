package com.mygdx.game.database.dao;

import com.mygdx.game.entity.User;

public interface UserDao {
	public void addUser(User user);

	public User getUser(int userId);
	
	public User getUser(String openId);

	public User findUser(String loginName, String password);

	public void update(User user);

	public void delete(User user);
}
