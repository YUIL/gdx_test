package com.mygdx.game.entity;

public class User {
	int user_id;
	String open_id;
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", open_id=" + open_id + "]";
	}
	
	
	
}
