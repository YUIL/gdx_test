package com.mygdx.game.server;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.GameObject;
import com.mygdx.game.entity.GameWorld;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;

public class LogicServer {
	volatile UdpServer udpServer;
	volatile Session session;
	String recvString;
	String responseString;
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	boolean stoped = false;
	GameWorld gameWorld=new GameWorld();
	public static void main(String[] args){
		/*GameObject obj1=new GameObject();
		obj1.getTransform().setTranslation(new Vector3(1, 2, 3));
		System.out.println(obj1.getTransform().getTranslation(obj1.getPosition()));
		System.out.println(obj1.getPosition());*/
		LogicServer logicServer=new LogicServer(9093);
		logicServer.start();
	}
	public LogicServer(int port){
		udpServer=new UdpServer(port);
		
	}
	
	public void disposeMessage(){
		jsonValue = jsonReader.parse(recvString);
		if(jsonValue!=null){
			if (jsonValue.get("addGameObject") != null) {
					GameObject gameObject=new GameObject(jsonValue.get("addGameObject").get("name").asString());
					gameObject.setPosition(new Vector3(jsonValue.get("addGameObject").get("x").asFloat(), jsonValue.get("addGameObject").get("y").asFloat(), 0));
					gameWorld.addGameObject(gameObject);
					udpServer.send(recvString.getBytes(), session);
				
			} else if (jsonValue.get("changeGameObject") != null) {
				GameObject gameObject=gameWorld.findGameObject(jsonValue.get("changeGameObject").getString("name"));
				if(gameObject!=null){
					if (jsonValue.get("changeGameObject").get("position")!=null) {
						gameObject.setPosition(new Vector3(jsonValue.get("changeGameObject").get("position").getFloat("x"), jsonValue.get("changeGameObject").get("position").getFloat("y"), 0));
						udpServer.send(recvString.getBytes(), session);
					}
				}
			} else if (jsonValue.get("getGameObject") != null) {
				GameObject gameObject=gameWorld.findGameObject(jsonValue.get("getGameObject").getString("name"));
				if(gameObject!=null){
					udpServer.send(("{getGameObject:"+gameObject.toJson()+"}").getBytes(), session);
				}
			} else if (jsonValue.get("removeGameObject") != null) {
				GameObject gameObject=gameWorld.findGameObject(jsonValue.get("removeGameObject").getString("name"));
				if(gameObject!=null){
					gameWorld.getGameObjectArray().removeValue(gameObject, true);
					udpServer.send(recvString.getBytes(), session);					
				}

			} else if (jsonValue.get("stopServer") != null) {
				stoped = true;
			}
		}
	}
	
	public void start(){
		System.out.println("LogicServer start!");
		udpServer.start();
		while (!stoped) {
			try {
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
