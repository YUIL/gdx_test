package com.mygdx.game.entity;

public class User {
	int userId;
	String openId;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int user_id) {
		this.userId = user_id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String open_id) {
		this.openId = open_id;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", openId=" + openId + "]";
	}

	
	
}
