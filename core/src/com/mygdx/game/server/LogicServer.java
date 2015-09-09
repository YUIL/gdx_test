package com.mygdx.game.server;

import java.net.BindException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;

import sun.misc.GC.LatencyRequest;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.GameObject;
import com.mygdx.game.entity.GameWorld;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;

public class LogicServer {
	volatile UdpServer udpServer;
	volatile UserServer userServer;
	volatile Session session;
	String recvString;
	String responseString;
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	boolean stoped = false;
	volatile GameWorld gameWorld = new GameWorld();

	public class GameWorldThread implements Runnable {

		public GameWorldThread() {
			// TODO Auto-generated constructor stub
		}

		float interval = 10;
		long lastUpdateTime = 0;
		long upDateTime = 0;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			lastUpdateTime = System.currentTimeMillis();
			while (true) {
				upDateTime = System.currentTimeMillis();
				if (upDateTime - lastUpdateTime >= interval) {
					lastUpdateTime = upDateTime;
					if (gameWorld.getGameObjectArray().size != 0) {
						gameWorld.update(interval / 1000);

						// System.out.println("collision
						// size:"+gameWorld.getBeCollidedGameObjectArray().size);
						/*for (int i = 0; i < gameWorld.getBeCollidedGameObjectArray().size; i++) {
							String name=gameWorld.getBeCollidedGameObjectArray().get(i).getName();
							gameWorld.getGameObjectArray().removeValue(gameWorld.getBeCollidedGameObjectArray().get(i),
									true);
							
							boardCast("{rgo:{name:"+name+"}}");
						}*/
						gameWorld.getBeCollidedGameObjectArray().clear();

					}
				}
				try {
					Thread.currentThread().sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) {
		/*
		 * GameObject obj1=new GameObject();
		 * obj1.getTransform().setTranslation(new Vector3(1, 2, 3));
		 * System.out.println(obj1.getTransform().getTranslation(obj1.
		 * getPosition())); System.out.println(obj1.getPosition());
		 */
		LogicServer logicServer = new LogicServer(9093);
		// logicServer.userServer=new UserServer(9092);
		// logicServer.userServer.start();

		logicServer.start();

	}

	public LogicServer(int port) {
		try {
			udpServer = new UdpServer(port);
		} catch (BindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void boardCast(String str) {

		System.out.println("send:" + str);

		/*
		 * for (int i = 0; i < userServer.userArray.size; i++) { Session
		 * session=new Session();
		 * session.setContactorAddress(userServer.userArray.get(i).session.
		 * getContactorAddress()); udpServer.send(str.getBytes(),session );
		 * 
		 * }
		 */
		Session session;
		if (!udpServer.sessionMap.isEmpty()) {
			for (Map.Entry<Long, Session> entry : udpServer.sessionMap.entrySet()) {
				session = entry.getValue();
				udpServer.send(str.getBytes(), session);
			}
		}
	}

	public void disposeMessage() {
		if (!recvString.equals("")) {
			System.out.println("recv:" + recvString);
		}

		jsonValue = jsonReader.parse(recvString);
		if (jsonValue != null) {
			if (jsonValue.get("ago") != null) {
				String name =jsonValue.get("ago").get("name").asString();
				GameObject gameObject=gameWorld.findGameObject(name);
				if (gameObject==null) {
					gameObject =new GameObject(name);
					gameObject.setPosition(new Vector3(jsonValue.get("ago").get("p").get("x").asFloat(),
							jsonValue.get("ago").get("p").get("y").asFloat(), 0));
					gameObject.setRectangle(gameObject.getPosition().x, gameObject.getPosition().y,
							jsonValue.get("ago").get("r").get("width").asFloat(),
							jsonValue.get("ago").get("r").get("height").asFloat());
					gameWorld.addGameObject(gameObject);
					// udpServer.send(recvString.getBytes(), session);
					boardCast(recvString);
				}else{
					udpServer.send(("{ggo:" + gameObject.toJson() + "}").getBytes(), session);
				}
				

			} else if (jsonValue.get("cgo") != null) {
				GameObject gameObject = gameWorld.findGameObject(jsonValue.get("cgo").getString("name"));
				if (gameObject != null) {
					if (jsonValue.get("cgo").get("p") != null) {
						gameObject.setPosition(new Vector3(jsonValue.get("cgo").get("p").getFloat("x"),
								jsonValue.get("cgo").get("p").getFloat("y"), 0));
						gameObject.setInertiaForce(new Vector3(jsonValue.get("cgo").get("i").getFloat("x"),
								jsonValue.get("cgo").get("i").getFloat("y"), 0));
						boardCast(recvString);
					} else if (jsonValue.get("cgo").get("i") != null) {
						gameObject.setInertiaForce(new Vector3(jsonValue.get("cgo").get("i").getFloat("x"),
								jsonValue.get("cgo").get("i").getFloat("y"), 0));
						boardCast(recvString);
					}
				}
			} else if (jsonValue.get("ggo") != null) {
				String name=jsonValue.get("ggo").getString("name");
				GameObject gameObject = gameWorld.findGameObject(name);
				if (gameObject != null) {
					boardCast("{ggo:" + gameObject.toJson() + "}");
				}else{
					boardCast("{rgo:{name:"+name+"}}");
				}
			} else if (jsonValue.get("rgo") != null) {
				GameObject gameObject = gameWorld.findGameObject(jsonValue.get("rgo").getString("name"));
				if (gameObject != null) {
					gameWorld.getGameObjectArray().removeValue(gameObject, true);
					boardCast(recvString);

				}

			} else if (jsonValue.get("stopServer") != null) {
				stoped = true;
			}
		}
	}

	long lastWhileTime = 0;
	float delta = 0;
	boolean needSleep = true;

	public void start() {
		System.out.println("LogicServer start!");
		Runnable gameWorldThread = new GameWorldThread();
		Thread thread = new Thread(gameWorldThread);
		thread.start();
		udpServer.start();
		lastWhileTime = System.nanoTime();
		while (!stoped) {
			// System.out.println("while
			// time:"+(System.nanoTime()-lastWhileTime));
			// lastWhileTime=System.nanoTime();

			// delta=((System.nanoTime()-lastWhileTime)/10000f);
			// gameWorld.update(delta);
			try {
				if (!udpServer.sessionMap.isEmpty()) {
					for (Map.Entry<Long, Session> entry : udpServer.sessionMap.entrySet()) {
						session = entry.getValue();
						while (!session.getRecvMessageQueue().isEmpty()) {
							recvString = new String(session.getRecvMessageQueue().poll().getData());
							if (recvString != null) {
								disposeMessage();
								needSleep = false;
							}
						}
					}
				}
			} catch (ConcurrentModificationException e) {

				// e.printStackTrace();
				// System.out.println("不知道什么问题，先不管。。");
			}
			if (needSleep) {
				try {
					Thread.currentThread();
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			needSleep = true;
			// lastWhileTime=System.nanoTime();
		}
		udpServer.stop();
		thread.stop();
	}
}
