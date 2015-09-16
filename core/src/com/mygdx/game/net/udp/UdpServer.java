package com.mygdx.game.net.udp;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.util.JavaDataConverter;

public class UdpServer {

	int maxSessionDelayTime = 30000;
	//public volatile int currentSendMessageNum = 0;
	public DatagramSocket serverSocket;
	public volatile boolean stoped = false;
	// public volatile Map<Long, Session> sessionMap = new HashMap<Long,
	// Session>();
	public volatile Array<Session> sessionArray = new Array<Session>();
	// SendServicer sendServicer = null;
	ReceiveServicer receiveServicer = null;
	ReportStatus reportStatus = null;
	ExecutorService sendThreadPool;
	Thread sendThread;
	Thread reciveThread;
	Thread reportThread;
	UdpMessageListener udpMessageListener;

	public UdpMessageListener getUdpMessageListener() {
		return udpMessageListener;
	}

	public void setUdpMessageListener(UdpMessageListener udpMessageListener) {
		this.udpMessageListener = udpMessageListener;
	}

	volatile long recvCount = 0;
	volatile long sendCount = 0;
	volatile long recvDataLength = 0;
	volatile long sendDataLength = 0;

	public synchronized Session findSession(long sessionId) {

		for (Iterator<Session> iterator = sessionArray.iterator(); iterator.hasNext();) {
			Session session = iterator.next();
			if (session.id == sessionId) {
				return session;
			}
		}
		return null;
	}

	public synchronized Session createSession(long sessionId, InetSocketAddress address) {
		Session session = new Session(sessionId);
		session.setContactorAddress(address);
		session.setSendThread(new SendServicer(session));
		sessionArray.add(session);
		return session;
	}

	public UdpServer(int port) throws BindException {
		init(port,10);
	}
	
	public UdpServer(int port,int maximumConections) throws BindException {
		init(port,maximumConections);
	}
	public void init(int port,int maximumConections)throws BindException{
		try {
			serverSocket = new DatagramSocket(port);

		} catch (BindException e) {
			throw e;
		} catch (SocketException e) {

			e.printStackTrace();
		}
		sendThreadPool=Executors.newFixedThreadPool(10);
	}

	public synchronized void removeSession(long sessionId) {
		Session session = findSession(sessionId);
		removeSession(session);

	}

	public synchronized void removeSession(Session session) {
		if (session != null) {
		/*	if (session.currentSendUdpMessage(null) != null) {
				currentSendMessageNum--;
			}*/
			sessionArray.removeValue(session, true);
		}

	}

	public void start() {

		// sendServicer = new SendServicer();
		receiveServicer = new ReceiveServicer();
		reportStatus = new ReportStatus();

		// sendThread = new Thread(sendServicer);
		reciveThread = new Thread(receiveServicer);
		reportThread = new Thread(reportStatus);

		// sendThread.start();
		reciveThread.start();
		reportThread.start();

	}

	public void stop() {
		System.out.println("stop server");
		UdpMessage responseMessage = new UdpMessage();
		Session session;
		responseMessage.setType((byte) 0);
		responseMessage.setLength(4);
		responseMessage.setData(JavaDataConverter.intToBytes(1));
		if (sessionArray.size > 0) {

			try {
				for (int i = 0; i < sessionArray.size; i++) {
					session = sessionArray.get(i);
					if (send(responseMessage, session)) {
						try {
							Thread.currentThread();
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (ConcurrentModificationException e) {

				// e.printStackTrace();
				// System.out.println("不知道什么问题，先不管。。");
			}
		}

		stoped = true;
		serverSocket.close();
	}

	public synchronized boolean send(byte[] data, Session session) {

		UdpMessage message = new UdpMessage();
		message.setSessionId(session.getId());
		message.setType((byte) 1);
		message.setLength(data.length);
		// System.out.println("send data.lenght:"+data.length);
		message.setData(data);
		return send(message, session);
	}

	public synchronized boolean send(UdpMessage message, Session session) {

		// System.out.println("send(UdpMessage message, Session session)");
		if (session.currentSendUdpMessage(null) == null) {
			message.setSessionId(session.getId());
			message.setSequenceId(session.lastSendSequenceId + 1);
			//currentSendMessageNum++;
			session.currentSendUdpMessage(message);
			sendThreadPool.execute(session.getSendThread());
			// threadPool.execute(sendThread);
			return true;
		} else {
			// System.out.println("正在发送中……");
			return false;
		}

	}

	public class ReportStatus implements Runnable {
		int interval = 10000;
		long nextReportTime;
		long reportTimes;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			nextReportTime = System.currentTimeMillis();
			while (!stoped) {
				if (System.currentTimeMillis() >= nextReportTime) {
					nextReportTime += interval;
					report();
					try {
						Thread.currentThread();
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}

		private void report() {
			reportTimes++;
			System.out.print(reportTimes);
			System.out.print("{");
			System.out.print("sessionArray.size():" + sessionArray.size);
		//	System.out.print("  |  currentSendMessageNum:" + currentSendMessageNum);
			System.out.print("  |  recvCount:" + recvCount);
			System.out.print("  |  sendCount:" + sendCount);
			System.out.print("  |  recvDataLength:" + recvDataLength);
			System.out.print("  |  sendDataLength:" + sendDataLength);
			System.out.print("}");
			System.out.println();
		}

	}

	public class SendServicer implements Runnable {
		volatile Session session;

		public SendServicer(Session session) {
			this.session = session;
		}

		@Override
		public void run() {
			// System.out.println("send Thread run");
			while ((session.currentSendUdpMessage(null) != null)) {
						if (session.currentSendUdpMessage(null).getSequenceId() != session.lastSendSequenceId) {// 如果对方还没收到这条消息
							if (session.timeOutMultiple > session.maxResendTimes) {// 如果单条消息重发次数超过maxResendTimes，删掉session
								removeSession(session);
								break;
							} else {
								if (System.currentTimeMillis() - session.lastSendTime > session.getTimeOut()
										* session.timeOutMultiple) {
									session.timeOutMultiple += 1;
									// System.out.println("send");
									sendUdpMessage(session, session.currentSendUdpMessage(null));
									if (session.currentSendUdpMessage(null).getType() != 1) {
										session.setCurrentSendMessage(null);
									}

								}else{
									try {
										Thread.sleep(session.getTimeOut()
												* session.timeOutMultiple);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}

						} else {
							session.timeOutMultiple = 0;
							session.setCurrentSendMessage(null);
						}



			}

		}

		public synchronized void sendUdpMessage(UdpMessage message) {
			// System.out.println("timeOutMulti:"+session.timeOutMultiple);
			session.setLastSendTime(System.currentTimeMillis());
			sendUdpMessage(serverSocket, session.getContactorAddress(), message);
		}

		public synchronized void sendUdpMessage(Session session, UdpMessage message) {
			// System.out.println("timeOutMulti:"+session.timeOutMultiple);
			session.setLastSendTime(System.currentTimeMillis());
			sendUdpMessage(serverSocket, session.getContactorAddress(), message);
		}

		public synchronized void sendUdpMessage(DatagramSocket sendSocket, SocketAddress address, UdpMessage message) {
			// System.out.println("Udp send, message:"+message.toString());
			sendCount++;
			if (message.getData()!=null) {
				sendDataLength += message.getData().length;
			}
			try {
				byte[] temp = message.toBytes();
				DatagramPacket sendPacket = new DatagramPacket(temp, temp.length, address);
				try {
					sendSocket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (NullPointerException e) {
				// TODO: handle exception
			}

		}

	}

	public class ReceiveServicer implements Runnable {

		volatile Session session;
		final int bytesLength = 65515;
		byte[] bytes1 = new byte[bytesLength];
		byte[] recvBuf = new byte[bytesLength];
		UdpMessage recvMessageBuf =new UdpMessage();
		UdpMessage responseMessage = new UdpMessage();
		
		//UdpMessage responseMessage;
		@Override
		public void run() {
			while (true) {
				if (stoped) {
					break;
				}

				if (serverSocket == null) {
					// System.out.println("serverSocket == null!");
					break;
				}
				System.arraycopy(bytes1, 0, recvBuf, 0, bytesLength);

				DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);

				try {
					// System.out.println("recv...");
					serverSocket.receive(recvPacket);
					recvCount++;

				} catch (IOException e) {
					// System.out.println("recvTread终止！");
					break;
				}

				if (JavaDataConverter.bytesToInt(JavaDataConverter.subByte(recvPacket.getData(), 4, 13)) > 65515) {
					System.out.println("data too long");

				} else {
					recvMessageBuf.setData(null);
					recvMessageBuf.initUdpMessageByDatagramPacket(recvPacket);
					System.out.println("udp recv:"+recvMessageBuf.toString());
					//UdpMessage recvMessageBuf = new UdpMessage(recvPacket);
					//recvDataLength += recvMessageBuf.getData().length;
					/*
					 * String recvString=new String(message.getData());
					 * JsonValue jsonValue; JsonReader jsonReader = new
					 * JsonReader(); jsonValue = jsonReader.parse(recvString);
					 */
					session = findSession(recvMessageBuf.getSessionId());
					if (session == null) {
						 System.out.println("add session");
						session = createSession(recvMessageBuf.getSessionId(),
								new InetSocketAddress(recvPacket.getAddress(), recvPacket.getPort()));
						// System.out.println(session.toString());

					}
					
					responseMessage.setSessionId(session.getId());
					switch (recvMessageBuf.getType()) {
					case 0:
						removeSession(recvMessageBuf.getSessionId());
					case 1:

						
						if (recvMessageBuf.getSequenceId() == session.lastRecvSequenceId+ 1) {
							//session.getRecvMessageQueue().add(recvMessageBuf);
							session.lastRecvSequenceId=recvMessageBuf.getSequenceId();
							
							udpMessageListener.disposeUdpMessage(session, recvMessageBuf);
							responseMessage.setSequenceId(recvMessageBuf.getSequenceId());
							responseMessage.setType((byte) 2);

							// session.setLastSendMessage(responseMessage);
							session.getSendThread().sendUdpMessage(responseMessage);
							// System.out.println("session
							// size:"+sessionMap.size());
						} else if (recvMessageBuf.getSequenceId() == session.lastRecvSequenceId) {
							// System.out.println("lastrecv:" +
							// session.getLastresponseMessage());
							responseMessage.setSequenceId(session.lastRecvSequenceId);
							responseMessage.setType((byte) 3);
							session.getSendThread().sendUdpMessage(responseMessage);

						}

						break;
					case 2:
						if (session != null && session.currentSendUdpMessage(null) != null) {
							if (recvMessageBuf.getSequenceId() == session.currentSendUdpMessage(null).sequenceId) {
								// System.out.println("发送成功");
								session.lastSendSequenceId=session.currentSendUdpMessage(null).getSequenceId();

							}
						} else {
							// System.out.println("回的啥，跟我没关系！");
						}
						break;
					case 3:
						// System.out.println("消息SequenceId不对");
						if (session != null && session.currentSendUdpMessage(null) != null) {
							if (recvMessageBuf.getSequenceId() == session.currentSendUdpMessage(null).getSequenceId()) {
								// System.out.println("发送成功");
								session.lastSendSequenceId=session.currentSendUdpMessage(null).getSequenceId();

							} else {
								// System.out.println("真不对！");
							}
						} else {
							// System.out.println("回你妹，早发完了");

						}
						break;
					}

					// System.out.println("recv:"+session.getLastresponseMessage());
				}

			}
		}

	}
}
