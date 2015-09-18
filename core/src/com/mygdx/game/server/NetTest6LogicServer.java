package com.mygdx.game.server;

import java.net.BindException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.GameWorldB2D;
import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.entity.message.GameMessageConverter;
import com.mygdx.game.entity.message.GameMessageType;
import com.mygdx.game.entity.message.GameMessage_c2s_ago;
import com.mygdx.game.entity.message.GameMessage_c2s_ggo;
import com.mygdx.game.entity.message.GameMessage_c2s_rgo;
import com.mygdx.game.entity.message.GameMessage_c2s_rpc;
import com.mygdx.game.entity.message.GameMessage_s2c_gago;
import com.mygdx.game.entity.message.GameMessage_s2c_ggo;
import com.mygdx.game.entity.message.GameMessage_s2c_rgo;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessage;
import com.mygdx.game.net.udp.UdpMessageListener;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.util.JavaDataConverter;

public class NetTest6LogicServer implements UdpMessageListener {

	volatile UdpServer udpServer;
	volatile UserServer userServer;
	// volatile Session session;
	// String recvString;
	// String responseString;
	JsonReader jsonReader = new JsonReader();
	volatile boolean stoped = false;
	volatile GameWorldB2D gameWorld;
	int autoBoardCastInterval = 100;
	long nextAutoBoardCastTime = 0;
	volatile int boardCastCound = 0;
	volatile int boardCastNum = 0;
	long disoposeMessageCount = 0;
	MessageProcessor messageProcessor;
	ExecutorService threadPool = Executors.newSingleThreadExecutor();
	volatile Thread gameWorldThread;

	volatile int autoAddIterval = 10000;
	volatile long nextAutoAddTime = 0;
	volatile int autoNum = 0;

	public class GameWorldLogic implements Runnable {

		public GameWorldLogic() {
			// TODO Auto-generated constructor stub
		}

		int updateInterval = 10;
		long nextUpdateTime = 0;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			nextAutoAddTime = System.currentTimeMillis();
			nextUpdateTime = System.currentTimeMillis();
			boolean needBoard = false;
			// nextAutoBoardCastTime = System.currentTimeMillis();
			while (!stoped) {
				if (System.currentTimeMillis() > nextUpdateTime) {
					nextUpdateTime += updateInterval;
					/*if (gameWorld.getGameObjectArray().size < 3) {
						if (System.currentTimeMillis() > nextAutoAddTime) {
							nextAutoAddTime += autoAddIterval;
							gameWorld.addBoxGameObject(GameObjectCreation.random(autoNum));
							autoNum++;
							needBoard = true;
						}
					}*/

					for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
						B2DGameObject gameObject = gameWorld.getGameObjectArray().get(i);
						if (gameObject.getBody().getPosition().y < -50) {
							gameWorld.getGameObjectRemoveQueue().add(gameObject);
							needBoard = true;
						}
					}
					gameWorld.update(updateInterval / 1000f);

					if (needBoard) {
						boardCastNum++;
					}
					/*
					 * if (System.currentTimeMillis()-nextAutoBoardCastTime>
					 * autoBoardCastInterval) {
					 * nextAutoBoardCastTime+=autoBoardCastInterval;
					 * boardCast("{gago:"+str+"}"); }
					 */
					if (boardCastNum > boardCastCound) {
						boardCast(GameMessage_s2c_gago.getBytesFromB2dGameObjecArray(gameWorld.getGameObjectArray()));
						boardCastCound++;
					}

				} else {
					try {
						Thread.sleep(updateInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				needBoard = false;
			}
			System.out.println("worldLogic stoped");
		}

	}

	public class MessageProcessor implements Runnable {

		GameMessageConverter gameMessageConverter = new GameMessageConverter();
		Session session;

		public Session getSession() {
			return session;
		}

		public void setSession(Session session) {
			this.session = session;
		}

		public UdpMessage getMessage() {
			return udpMessage;
		}

		public void setMessage(UdpMessage message) {
			this.udpMessage = message;
		}

		UdpMessage udpMessage;

		@Override
		public void run() {
			if (udpMessage.length>4) {
				System.out.println("processMessage");
				int type = JavaDataConverter.bytesToInt(JavaDataConverter.subByte(udpMessage.getData(), 4, 0));
				System.out.println("type:"+type);
				byte[] src = JavaDataConverter.subByte(udpMessage.getData(), udpMessage.getData().length - 4, 4);
				long id;
				B2DGameObject gameObject;
				switch (type) {
				case GameMessageType.c2s_rpc:
					System.out.println("c2s_rpc");
					GameMessage_c2s_rpc gameMessage_c2s_rpc = new GameMessage_c2s_rpc(src);
					id = gameMessage_c2s_rpc.gameObjectId;
					System.out.println("id:"+id);
					gameObject = gameWorld.findGameObject(id);
					if (gameObject != null) {
						float forceX = gameMessage_c2s_rpc.forceX;
						float forceY = gameMessage_c2s_rpc.forceY;
						if (gameObject.getSpeed() < gameObject.getMaxSpeed()) {
							if (forceX == 0) {
								if (gameObject.getBody().getLinearVelocity().y < 1
										&& gameObject.getBody().getLinearVelocity().y > -1) {
									gameObject.applyForce(forceX, forceY);
									// boardCast(recvString);
									System.out.println("boardCastNum++");
									boardCastNum++;
								}
							} else {
								if (forceX > 0 && gameObject.getBody().getLinearVelocity().x < 10
										|| forceX < 0 && gameObject.getBody().getLinearVelocity().x > -10) {
									gameObject.applyForce(forceX, forceY);
									// boardCast(recvString);
									System.out.println("boardCastNum++");
									boardCastNum++;
								}
							}

						}
					}
					break;
				case GameMessageType.c2s_ago:
					GameMessage_c2s_ago gameMessage_c2s_ago = new GameMessage_c2s_ago(src);
					id = gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId;
					gameObject = gameWorld.findGameObject(id);
					if (gameObject != null) {
						
					}else{
						//System.out.println("create Box, id:"+gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId);
						gameWorld.getGameObjectCreationQueue().add(gameMessage_c2s_ago.b2dBoxBaseInformation);
						boardCastNum++;
						/*GameMessage_s2c_ago gameMessage_s2c_ago=new GameMessage_s2c_ago();
						gameMessage_s2c_ago.b2dBoxBaseInformation=gameMessage_c2s_ago.b2dBoxBaseInformation;
						udpServer.send(gameMessage_s2c_ago.toBytes(), session);*/
					}
					break;
				case GameMessageType.c2s_gago:
					udpServer.send(GameMessage_s2c_gago.getBytesFromB2dGameObjecArray(gameWorld.getGameObjectArray()), session);
					break;
				case GameMessageType.c2s_rgo:
					GameMessage_c2s_rgo gameMessage_c2s_rgo=new GameMessage_c2s_rgo(src);
					gameObject=gameWorld.findGameObject(gameMessage_c2s_rgo.gameObjectId);
					if(gameObject!=null){
						gameWorld.getGameObjectRemoveQueue().add(gameObject);
						GameMessage_s2c_rgo gameMessage_s2c_rgo=new GameMessage_s2c_rgo();
						gameMessage_s2c_rgo.gameObjectId=gameObject.getId();
						udpServer.send(gameMessage_s2c_rgo.toBytes(), session);
					}					
					break;
				case GameMessageType.c2s_ggo:
					GameMessage_c2s_ggo gameMessage_c2s_ggo=new GameMessage_c2s_ggo(src);
					gameObject=gameWorld.findGameObject(gameMessage_c2s_ggo.gameObjectId);
					if(gameObject!=null){
						udpServer.send(GameMessage_s2c_ggo.getBytesFromB2dGameObject(gameObject), session);
						
					}else{
						GameMessage_s2c_rgo gameMessage_s2c_rgo=new GameMessage_s2c_rgo();
						gameMessage_s2c_rgo.gameObjectId=gameMessage_c2s_ggo.gameObjectId;
						udpServer.send(gameMessage_s2c_rgo.toBytes(), session);
					}
					break;
				default:
					break;
				}
			}
			

			// TODO Auto-generated method stub
			/*
			 * System.out.print("disposeMessage:");
			 * System.out.println(message.toString());
			 */

			/*
			 * String recvString = new String(udpMessage.getData()); if
			 * (!recvString.equals("")) { JsonValue jsonValue =
			 * jsonReader.parse(recvString); if (jsonValue.get("rpc") != null) {
			 * jsonValue = jsonValue.get("rpc"); if (jsonValue.get("af") !=
			 * null) { jsonValue = jsonValue.get("af"); String name =
			 * jsonValue.getString("n"); B2DGameObject gameObject =
			 * gameWorld.findGameObject(name); if (gameObject != null) { float
			 * forceX = jsonValue.getFloat("fx"); float forceY =
			 * jsonValue.getFloat("fy"); if (gameObject.getSpeed() <
			 * gameObject.getMaxSpeed()) { if (forceX == 0) { if
			 * (gameObject.getBody().getLinearVelocity().y < 1 &&
			 * gameObject.getBody().getLinearVelocity().y > -1) {
			 * gameObject.applyForce(forceX, forceY); // boardCast(recvString);
			 * boardCastNum++; } } else { if (forceX > 0 &&
			 * gameObject.getBody().getLinearVelocity().x < 10 || forceX < 0 &&
			 * gameObject.getBody().getLinearVelocity().x > -10) {
			 * gameObject.applyForce(forceX, forceY); // boardCast(recvString);
			 * boardCastNum++; } }
			 * 
			 * } }
			 * 
			 * }
			 * 
			 * } else { if (jsonValue.get("gago") != null) { String str =
			 * gameWorld.gameObjectArrayToString(); boardCast("{gago:" + str +
			 * "}"); } else if (jsonValue.get("ago") != null) { jsonValue =
			 * jsonValue.get("ago"); String name = jsonValue.getString("n");
			 * float x = jsonValue.get("t").get("p").getFloat("x"); float y =
			 * jsonValue.get("t").get("p").getFloat("y"); float angle =
			 * jsonValue.get("t").getFloat("a"); float angularVelocity =
			 * jsonValue.getFloat("av"); float width =
			 * jsonValue.get("s").getFloat("w"); float height =
			 * jsonValue.get("s").getFloat("h"); float density =
			 * jsonValue.getFloat("d"); float lx =
			 * jsonValue.get("l").getFloat("x"); float ly =
			 * jsonValue.get("l").getFloat("y"); B2DGameObject gameObject =
			 * gameWorld.findGameObject(name); if (gameObject != null) {
			 * 
			 * } else { gameWorld.getGameObjectCreationQueue().add(new
			 * GameObjectCreation(name, x, y, angle, angularVelocity, width,
			 * height, density, lx, ly)); boardCast(recvString); } } else if
			 * (jsonValue.get("rgo") != null) { jsonValue =
			 * jsonValue.get("rgo"); String name = jsonValue.getString("n");
			 * B2DGameObject gameObject = gameWorld.findGameObject(name); if
			 * (gameObject != null) { gameWorld.removeGameObject(name);
			 * boardCast(recvString); } } else if (jsonValue.get("ggo") != null)
			 * {
			 * 
			 * jsonValue=jsonValue.get("ggo"); String
			 * name=jsonValue.getString("n"); B2DGameObject
			 * gameObject=gameWorld.findGameObject(name); if(gameObject!=null) {
			 * //boardCast("{ggo:"+gameObject.toJson()+"}");
			 * udpServer.send(("{ggo:"+gameObject.toJson()+"}").getBytes(),
			 * session); } else{
			 * udpServer.send(("{rgo:{n:"+name+"}}").getBytes(), session); } }
			 * else if (jsonValue.get("stopServer") != null) {
			 * //udpServer.stop(); stoped=true; } } }
			 */

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
	
	public synchronized void boardCast(byte[] bytes) {
		Session session;
		for (int i = 0; i < udpServer.sessionArray.size; i++) {
			session = udpServer.sessionArray.get(i);
			udpServer.send(bytes, session);
		}
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
		// System.out.println("send:"+str);
		boardCast(str.getBytes());
	}

	/*
	 * public void disposeMessage(Session session,UdpMessage message) {
	 * disoposeMessageCount++; if (!recvString.equals("")) { //
	 * System.out.println("recv:" + recvString); JsonValue jsonValue =
	 * jsonReader.parse(recvString); if (jsonValue.get("rpc") != null) {
	 * jsonValue = jsonValue.get("rpc"); if(jsonValue.get("af")!=null){
	 * jsonValue = jsonValue.get("af"); String name=jsonValue.getString("n");
	 * GameObjectB2D gameObject=gameWorld.findGameObject(name); if
	 * (gameObject!=null){ float forceX=jsonValue.getFloat("fx"); float
	 * forceY=jsonValue.getFloat("fy");
	 * if(gameObject.getSpeed()<gameObject.getMaxSpeed()){ if(forceX==0){
	 * if(gameObject.getBody().getLinearVelocity().y<1&&gameObject.getBody().
	 * getLinearVelocity().y>-1){ gameObject.applyForce(forceX, forceY); //
	 * boardCast(recvString); boardCastNum++; } }else{ if
	 * (forceX>0&&gameObject.getBody().getLinearVelocity().x<10||forceX<0&&
	 * gameObject.getBody().getLinearVelocity().x>-10) {
	 * gameObject.applyForce(forceX, forceY); // boardCast(recvString);
	 * boardCastNum++; } }
	 * 
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * }else{ if (jsonValue.get("gago") != null) { String
	 * str=gameWorld.gameObjectArrayToString(); boardCast("{gago:"+str+"}");
	 * }else if(jsonValue.get("ago") != null) { jsonValue=jsonValue.get("ago");
	 * String name=jsonValue.getString("n"); float
	 * x=jsonValue.get("t").get("p").getFloat("x"); float
	 * y=jsonValue.get("t").get("p").getFloat("y"); float
	 * angle=jsonValue.get("t").getFloat("a"); float
	 * angularVelocity=jsonValue.getFloat("av"); float
	 * width=jsonValue.get("s").getFloat("w"); float
	 * height=jsonValue.get("s").getFloat("h"); float
	 * density=jsonValue.getFloat("d"); float
	 * lx=jsonValue.get("l").getFloat("x"); float
	 * ly=jsonValue.get("l").getFloat("y"); GameObjectB2D
	 * gameObject=gameWorld.findGameObject(name); if (gameObject!=null) {
	 * 
	 * }else { gameWorld.addBoxGameObject(name, x, y, angle,angularVelocity,
	 * width, height, density, lx, ly); boardCast(recvString); } }else
	 * if(jsonValue.get("rgo") != null) { jsonValue=jsonValue.get("rgo"); String
	 * name=jsonValue.getString("n"); GameObjectB2D
	 * gameObject=gameWorld.findGameObject(name); if (gameObject!=null) {
	 * gameWorld.removeGameObject(name); boardCast(recvString); } }else
	 * if(jsonValue.get("ggo") != null) { jsonValue=jsonValue.get("ggo"); String
	 * name=jsonValue.getString("n"); GameObjectB2D
	 * gameObject=gameWorld.findGameObject(name); if (gameObject!=null) {
	 * boardCast("{ggo:"+gameObject.toJson()+"}"); } String
	 * str=gameWorld.gameObjectArrayToString(); boardCast("{gago:"+str+"}"); } }
	 * }
	 * 
	 * 
	 * 
	 * }
	 */

	float delta = 0;
	boolean needSleep = true;

	@SuppressWarnings("deprecation")
	public void start() {
		System.out.println("LogicServer start!");
		Runnable gameWorldLogic = new GameWorldLogic();
		gameWorldThread = new Thread(gameWorldLogic);

		messageProcessor = new MessageProcessor();

		gameWorldThread.start();
		udpServer.setUdpMessageListener(this);
		udpServer.start();
		/*
		 * while (!stoped) { if(udpServer.type1Count>disoposeMessageCount){ try
		 * { if (udpServer.sessionArray.size!=0) { for (int i = 0; i <
		 * udpServer.sessionArray.size; i++) { session=
		 * udpServer.sessionArray.get(i); if(session==null) break; while
		 * (!session.getRecvMessageQueue().isEmpty()) { recvString = new
		 * String(session.getRecvMessageQueue().poll().getData()); if
		 * (recvString != null) { disposeMessage();
		 * 
		 * } } } } } catch (ConcurrentModificationException e) {
		 * 
		 * // e.printStackTrace(); // System.out.println("不知道什么问题，先不管。。"); }
		 * }else{ try { Thread.currentThread(); Thread.sleep(1); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 * 
		 * }
		 */

	}

	@Override
	public void disposeUdpMessage(Session session, UdpMessage message) {
		// TODO Auto-generated method stub
		messageProcessor.setSession(session);
		messageProcessor.setMessage(message);
		threadPool.execute(messageProcessor);
	}

}
