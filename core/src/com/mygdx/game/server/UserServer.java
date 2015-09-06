package com.mygdx.game.server;


import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;

public class UserServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserServer userSerser = new UserServer(9092);
		userSerser.Start();
	}

	Map<String, Session> userMap = new HashMap<String, Session>();
	volatile UdpServer udpServer;
	volatile Session session;
	String recvString;
	String responseString;
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	boolean stoped = false;

	public UserServer(int port) {
		udpServer = new UdpServer(port);

	}

	public void Start() {
		System.out.println("userServer start!");
		udpServer.start();
		while (!stoped) {
			try {
				if (!userMap.isEmpty()) {
					for (Entry<String, Session> entry : userMap.entrySet()) {
						session = entry.getValue();
						if (udpServer.sessionMap.get(session.getId()) == null) {
							userMap.remove(entry.getKey());
						}
					}
				}

				if (!udpServer.sessionMap.isEmpty()) {
				
					for (Map.Entry<Long, Session> entry : udpServer.sessionMap
							.entrySet()) {
						session = entry.getValue();

						while (!session.getRecvMessageQueue().isEmpty()) {
							recvString = new String(session
									.getRecvMessageQueue().poll().getData());
							jsonValue = jsonReader.parse(recvString);
							if(jsonValue!=null){
								if (jsonValue.get("login") != null) {
									
									String name = jsonValue.get("login")
											.get("name").asString();
									if (userMap.get(name) == null) {
										userMap.put(name, session);
										System.out.println(name + "login:"
												+ session.toString());
	
									}else{
										System.out.println(name+" already logged in!");
									}
									
								} else if (jsonValue.get("getUser") != null) {
									String name = jsonValue.get("getUser")
											.get("name").asString();
									Session session1 = userMap.get(name);
									if (session1 != null) {
										responseString = "{user:{"
												+ "ip:'"
												+ session1.getContactorAddress()
														.getAddress().toString()
												+ "',port:"
												+ session1.getContactorAddress()
														.getPort() + "}}";
										udpServer.send(responseString.getBytes(),
												session);
										System.out.println("response:"+responseString);
									} else {
										System.out.println(name + " no login!");
									}
	
								} else if (jsonValue.get("getUsers") != null) {
	
								} else if (jsonValue.get("userServerStop") != null) {
									stoped = true;
								}
							}
						}

					}
				

				}	
			} catch (ConcurrentModificationException e) {

				// e.printStackTrace();
				// System.out.println("不知道什么问题，先不管。。");
			}
		}
		udpServer.stop();
	}
}
