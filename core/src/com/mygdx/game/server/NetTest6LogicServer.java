package com.mygdx.game.server;

import java.net.BindException;
import java.util.ConcurrentModificationException;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.GameWorldB2D;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;


public class NetTest6LogicServer {

	volatile UdpServer udpServer;
	volatile UserServer userServer;
	volatile Session session;
	String recvString;
	String responseString;
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	boolean stoped = false;
	volatile GameWorldB2D gameWorld;

	public class GameWorldLogic implements Runnable {

		public GameWorldLogic() {
			// TODO Auto-generated constructor stub
		}

		float interval = 1;
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
					}
				}
				try {
					Thread.currentThread();
					Thread.sleep(1);
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
		NetTest6LogicServer logicServer = new NetTest6LogicServer(9093);
		// logicServer.userServer=new UserServer(9092);
		// logicServer.userServer.start();

		logicServer.start();

	}

	public NetTest6LogicServer(int port) {
		try {
			udpServer = new UdpServer(port);
		} catch (BindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Box2D.init();
		gameWorld = new GameWorldB2D();
	}

	public synchronized void boardCast(String str) {

		// System.out.println("send:" + str);

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
			 jsonValue = jsonReader.parse(recvString);
				if (jsonValue.get("gago") != null) {
					String str=gameWorld.gameObjectArrayToString();
					boardCast("{gago:"+str+"}");
				}
		 }
		 

		
	}

	long lastWhileTime = 0;
	float delta = 0;
	boolean needSleep = true;

	@SuppressWarnings("deprecation")
	public void start() {
		System.out.println("LogicServer start!");
		Runnable gameWorldLogic = new GameWorldLogic();
		Thread gameWorldThread = new Thread(gameWorldLogic);
		gameWorldThread.start();
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
		gameWorldThread.stop();
	}

}
