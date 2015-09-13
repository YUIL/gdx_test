package com.mygdx.game.server;

import java.net.BindException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.GameObjectB2D;
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
	int autoBoardCastInterval=32;
	long nextAutoBoardCastTime=0;


	public class GameWorldLogic implements Runnable {

		public GameWorldLogic() {
			// TODO Auto-generated constructor stub
		}

		int interval = 1;
		long nextUpdateTime = 0;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			nextUpdateTime = System.currentTimeMillis();
			while (true) {
				if (System.currentTimeMillis() - nextUpdateTime >= interval) {
					nextUpdateTime +=interval;
					if (gameWorld.getGameObjectArray().size != 0) {
						gameWorld.update(interval / 1000f);
						String str=gameWorld.gameObjectArrayToString();
						if (System.currentTimeMillis()-nextAutoBoardCastTime>autoBoardCastInterval) {
							nextAutoBoardCastTime=System.currentTimeMillis();
							boardCast("{gago:"+str+"}");
						}
						
					}
				}
				try {
					Thread.currentThread();
					Thread.sleep((long) interval);
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
		if (udpServer.sessionArray.size!=0) {
			for (Iterator<Session> iterator = udpServer.sessionArray.iterator(); iterator.hasNext();) {
				session =  iterator.next();
				udpServer.send(str.getBytes(), session);
			}
		}
	}

	public void disposeMessage() {
		
		 if (!recvString.equals("")) { 
			 System.out.println("recv:" + recvString);
			 jsonValue = jsonReader.parse(recvString);
			 if (jsonValue.get("rpc") != null) {
				 jsonValue = jsonValue.get("rpc");
				 if(jsonValue.get("af")!=null){
					 jsonValue = jsonValue.get("af");
					 String name=jsonValue.getString("n");
					 GameObjectB2D gameObject=gameWorld.findGameObject(name);
					 if (gameObject!=null){
						 float forceX=jsonValue.getFloat("fx");
						 float forceY=jsonValue.getFloat("fy");
						 if(gameObject.getSpeed()<gameObject.getMaxSpeed()){
							 if(forceX==0){
								 if(gameObject.getBody().getLinearVelocity().y<1&&gameObject.getBody().getLinearVelocity().y>-1){
									 gameObject.applyForce(forceX, forceY);
									// boardCast(recvString);
								 }
							 }else{
								 if (forceX>0&&gameObject.getBody().getLinearVelocity().x<10||forceX<0&&gameObject.getBody().getLinearVelocity().x>-10) {
									 gameObject.applyForce(forceX, forceY);
									// boardCast(recvString);
								}
							 }
							
							 
						 }
					 }
						
				 }	
				 
			 }else{
				if (jsonValue.get("gago") != null) {
					String str=gameWorld.gameObjectArrayToString();
					boardCast("{gago:"+str+"}");
				}else if(jsonValue.get("ago") != null) {
					jsonValue=jsonValue.get("ago");
					String name=jsonValue.getString("n");
					float x=jsonValue.get("t").get("p").getFloat("x");
					float y=jsonValue.get("t").get("p").getFloat("y");
					float angle=jsonValue.get("t").getFloat("a");
					float angularVelocity=jsonValue.getFloat("av");
					float width=jsonValue.get("s").getFloat("w");
					float height=jsonValue.get("s").getFloat("h");
					float density=jsonValue.getFloat("d");
					float lx=jsonValue.get("l").getFloat("x");
					float ly=jsonValue.get("l").getFloat("y");
					GameObjectB2D gameObject=gameWorld.findGameObject(name);
					if (gameObject!=null) {
						
					}else {
						gameWorld.addBoxGameObject(name, x, y, angle,angularVelocity, width, height, density, lx, ly);
						boardCast(recvString);
					}
				}else if(jsonValue.get("rgo") != null) {
					jsonValue=jsonValue.get("rgo");
					String name=jsonValue.getString("n");
					GameObjectB2D gameObject=gameWorld.findGameObject(name);
					if (gameObject!=null) {
						gameWorld.removeGameObject(name);
						boardCast(recvString);
					}
				}else if(jsonValue.get("ggo") != null) {
					jsonValue=jsonValue.get("ggo");
					String name=jsonValue.getString("n");
					GameObjectB2D gameObject=gameWorld.findGameObject(name);
					/*if (gameObject!=null) {
						boardCast("{ggo:"+gameObject.toJson()+"}");
					}*/
					String str=gameWorld.gameObjectArrayToString();
					//boardCast("{gago:"+str+"}");
				}
		 	}
		 }
		 

		
	}

	float delta = 0;
	boolean needSleep = true;

	@SuppressWarnings("deprecation")
	public void start() {
		System.out.println("LogicServer start!");
		Runnable gameWorldLogic = new GameWorldLogic();
		Thread gameWorldThread = new Thread(gameWorldLogic);
		gameWorldThread.start();
		udpServer.start();
		while (!stoped) {
			// System.out.println("while
			// time:"+(System.nanoTime()-lastWhileTime));
			// lastWhileTime=System.nanoTime();

			// delta=((System.nanoTime()-lastWhileTime)/10000f);
			// gameWorld.update(delta);
			try {
				if (udpServer.sessionArray.size!=0) {
					for (Iterator<Session> iterator = udpServer.sessionArray.iterator(); iterator
							.hasNext();) {
						session = iterator.next();
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
