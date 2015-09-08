package com.mygdx.game.server;

import com.mygdx.game.net.udp.Session;

public class User {
	String name=null;
	Session session=null;
	public User(String name ,Session session){
		this.name=name;
		this.session=session;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
}
