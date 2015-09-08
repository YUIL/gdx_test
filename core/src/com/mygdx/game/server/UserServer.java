package com.mygdx.game.server;


import java.util.ConcurrentModificationException;
import java.util.Map;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;

public class UserServer {

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserServer userSerser = new UserServer(9092);
		userSerser.start();
	}*/

	volatile Array<User> userArray = new Array<User>();
	volatile UdpServer udpServer;
	volatile Session session;
	String recvString;
	String responseString;
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	volatile boolean stoped = false;

	public UserServer(int port) {
		udpServer = new UdpServer(port);

	}
	
	public User getUser(String name){
		for (int i = 0; i < userArray.size; i++) {
			if(userArray.get(i).name.equals(name)){
				return userArray.get(i);
			}
		}
		return null;
	}

	public void disposeMessage(){
		jsonValue = jsonReader.parse(recvString);
		if(jsonValue!=null){
			if (jsonValue.get("login") != null) {
				
				String name = jsonValue.get("login")
						.get("name").asString();
				if (getUser(name) == null) {
					userArray.add(new User(name, session));
					System.out.println(name + "login:"
							+ session.toString());

				}else{
					System.out.println(name+" already logged in!");
				}
				
			} else if (jsonValue.get("getUser") != null) {
				String name = jsonValue.get("getUser")
						.get("name").asString();
				Session session1 = getUser(name).getSession();
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

			} else if (jsonValue.get("stopServer") != null) {
				stoped = true;
			}
		}
	}
	
	public void start() {
		System.out.println("UserServer start!");
		udpServer.start();
		while (!stoped) {
			try {
				if (!(userArray.size==0)) {
					
					for (int i = 0; i < userArray.size; i++) {
						session=userArray.get(i).getSession();
						if (udpServer.sessionMap.get(session.getId()) == null) {
							userArray.removeIndex(i);
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
							if(recvString!=null){
								disposeMessage();
							}	
							
						}

					}
				

				}	
			} catch (ConcurrentModificationException e) {

				// e.printStackTrace();
				// System.out.println("不知道什么问题，先不管。。");
			}
			try {
				Thread.currentThread();
				Thread.sleep(0,10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		udpServer.stop();
	}
}
