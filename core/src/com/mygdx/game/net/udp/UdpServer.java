package com.mygdx.game.net.udp;


import com.mygdx.game.util.JavaDataConverter;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UdpServer {

    int maxSessionDelayTime = 30000;
    public volatile int currenSendMessageNum = 0;
    public DatagramSocket serverSocket;
    public volatile boolean stoped = false;
    public Map<Long, Session> sessionMap = new HashMap<Long, Session>();

    SendServicer sendServicer = null;
    ReceiveServicer receiveServicer = null;
    Thread sendThread;
    Thread reciveThread;
    long sleepM = 1;
    int sleepN = 0;

    public UdpServer(int port) throws BindException {
        try {
            serverSocket = new DatagramSocket(port);
            
        }catch(BindException e){
        	throw e;
        } 
        catch (SocketException e) {

            e.printStackTrace();
        }
    }
    
    public synchronized void removeSession(long id){
    	if(sessionMap.get(id)!=null){
			sessionMap.remove(id);
		}
    	
    }

    public void start() {

        sendServicer = new SendServicer();
        receiveServicer = new ReceiveServicer();

        sendThread = new Thread(sendServicer);
        reciveThread = new Thread(receiveServicer);

        sendThread.start();
        reciveThread.start();

    }

    public void stop() {
    	System.out.println("stop server");
    	UdpMessage responseMessage = new UdpMessage();
    	Session session;
    	responseMessage.setType((byte)0);
    	responseMessage.setLength(4);
    	responseMessage.setData(JavaDataConverter.intToBytes(1));
    	if (!sessionMap.isEmpty()) {
			try {
				for (Map.Entry<Long, Session> entry : sessionMap
						.entrySet()) {
					session = entry.getValue();
					if(send(responseMessage, session)){
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
        message.setType((byte)1);
        message.setLength(data.length);
        //System.out.println("send data.lenght:"+data.length);
        message.setData(data);
        return send(message, session);
    }

    public synchronized boolean send(UdpMessage message, Session session) {

        if (session.currentSendUdpMessage(null) == null) {
        	message.setSessionId(session.getId());
            message.setSequenceId(session.lastSendMessage.sequenceId + 1);
            currenSendMessageNum++;

            session.currentSendUdpMessage(message);
            return true;
        } else {
            // System.out.println("正在发送中……");
            return false;
        }

    }


    public class SendServicer implements Runnable {
        volatile Session session;

        @Override
        public void run() {

            while (true) {
                if (stoped) {
                    break;
                }
                // System.out.println(currenSendMessageNum);
                if (currenSendMessageNum == 0) {//没消息就歇会儿
                    try {
                        Thread.sleep(sleepM, sleepN);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {

                        for (Entry<Long, Session> entry : sessionMap.entrySet()) {
                            session = entry.getValue();

                            if (session.lastSendTime != 0
                                    && (System.currentTimeMillis() - session.lastSendTime) > maxSessionDelayTime) {
                                //System.out.println("Session:" + session.getId()+ " time out!");
                            	removeSession(session.id);
                                continue;
                            }

                            if (session.currentSendUdpMessage(null) != null) {
                                if (session.currentSendUdpMessage(null)
                                        .getSequenceId() != session
                                        .getLastSendMessage().getSequenceId()) {
                                    if (System.currentTimeMillis()
                                            - session.lastSendTime > session
                                            .getTimeOut() * session.timeOutMultiple) {
                                        session.timeOutMultiple+=1;
                                        // System.out.println("send");
                                        sendUdpMessage(
                                                session,
                                                session.currentSendUdpMessage(null));
                                        if (session.currentSendUdpMessage(null)
                                                .getType() != 1) {

                                            session.setCurrentSendMessage(null);
                                            currenSendMessageNum--;
                                        }
                                        
                                    }
                                } else {
                                    session.timeOutMultiple = 0;
                                    session.setCurrentSendMessage(null);
                                    currenSendMessageNum--;
                                }

                            }
                        }
                    } catch (ConcurrentModificationException e) {

                        // e.printStackTrace();
                        //System.out.println("不知道什么问题，先不管。。");
                    }
                }
            }

        }

        public synchronized void sendUdpMessage(Session session, UdpMessage message) {
            session.setLastSendTime(System.currentTimeMillis());
            sendUdpMessage(serverSocket, session.getContactorAddress(), message);
        }

        public synchronized void sendUdpMessage(DatagramSocket sendSocket,
                                                SocketAddress address, UdpMessage message) {
        	//System.out.println("send, message:"+message.toString());
            try {
                byte[] temp = message.toBytes();
                DatagramPacket sendPacket = new DatagramPacket(temp,
                        temp.length, address);
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
        final  int  bytesLength=65515;
        byte[] bytes1=new byte[bytesLength];
        byte[] recvBuf = new byte[bytesLength];
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
                System.arraycopy(bytes1,0,recvBuf,0,bytesLength);

                DatagramPacket recvPacket = new DatagramPacket(recvBuf,
                        recvBuf.length);

                try {
                    // System.out.println("recv...");
                    serverSocket.receive(recvPacket);

                } catch (IOException e) {
                    //System.out.println("recvTread终止！");
                    break;
                }
                
                if(JavaDataConverter.bytesToInt(JavaDataConverter
        				.subByte(recvPacket.getData(), 4, 13))>65515){
                	System.out.println("data too long");
                	
                }else{
                	UdpMessage message = new UdpMessage(recvPacket);
                    /*  String recvString=new String(message.getData());
                     JsonValue jsonValue;
                     JsonReader jsonReader = new JsonReader();
                     jsonValue = jsonReader.parse(recvString);*/
                    //System.out.println("recive:" + message.toString());

                     session = sessionMap.get(message.getSessionId());
                     if (session == null) {
                        // System.out.println("add session");
                         session = new Session(message.getSessionId());
                         session.setContactorAddress(new InetSocketAddress(
                                 recvPacket.getAddress(), recvPacket.getPort()));
                         sessionMap.put(session.getId(), session);
                        // System.out.println(session.toString());

                     }

                     UdpMessage responseMessage = new UdpMessage();
                     responseMessage.setSessionId(session.getId());
                     switch (message.getType()) {
                     	case 0:
                     		removeSession(message.getSessionId());
                         case 1:

                             responseMessage.setLength(4);
                             responseMessage.setData(JavaDataConverter.intToBytes(1));
                             if (message.getSequenceId() == session.getLastresponseMessage()
                                     .getSequenceId() + 1) {
                                 session.getRecvMessageQueue().add(message);
                                 session.setLastresponseMessage(message);
                                 responseMessage.setSequenceId(message.getSequenceId());
                                 responseMessage.setType((byte)2);

                                 //session.setLastSendMessage(responseMessage);
                                 sendServicer.sendUdpMessage(session, responseMessage);
                                 // System.out.println("session size:"+sessionMap.size());
                             } else if(message.getSequenceId() ==session.getLastresponseMessage()
                                     .getSequenceId()){              
                                // System.out.println("lastrecv:" + session.getLastresponseMessage());
                                 responseMessage.setSequenceId(session.lastRecvMessage.getSequenceId());
                                 responseMessage.setType((byte)3);
                                 sendServicer.sendUdpMessage(session, responseMessage);

                             }

                             break;
                         case 2:
                             if (session != null && session.currentSendUdpMessage(null) != null) {
                                 if (message.getSequenceId() == session
                                         .currentSendUdpMessage(null).sequenceId) {
                                     // System.out.println("发送成功");
                                     session.setLastSendMessage(session
                                             .currentSendUdpMessage(null));

                                 }
                             } else {
                                // System.out.println("回的啥，跟我没关系！");
                             }
                             break;
                         case 3:
                             //System.out.println("消息SequenceId不对");
                             if (session != null && session.currentSendUdpMessage(null) != null) {
                                 if (message.getSequenceId() == session
                                         .currentSendUdpMessage(null)
                                         .getSequenceId()) {
                                     // System.out.println("发送成功");
                                     session.setLastSendMessage(session
                                             .currentSendUdpMessage(null));

                                 } else {
                                     //System.out.println("真不对！");
                                 }
                             } else {
                                 //System.out.println("回你妹，早发完了");

                             }
                             break;
                     }

                     //System.out.println("recv:"+session.getLastresponseMessage());
                }
                
            }
        }

    }
}
