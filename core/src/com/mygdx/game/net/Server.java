package com.mygdx.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import com.badlogic.gdx.utils.ObjectMap;

public class Server {

	public ObjectMap<String, Client> clientMap = new ObjectMap<String, Client>();
	public DatagramSocket serverSocket;
	public Thread sendThread;
	public Thread reciveThread;
	public volatile boolean stoped = false;

	public void stop() {
		stoped = true;
		serverSocket.close();
	}

	public class SendServicer implements Runnable {
		volatile Client client;

		// public DatagramSocket sendSocket;
		public SendServicer() {
			client = clientMap.get("server");
		}

		public SendServicer(Client client) {
			this.client = client;
			System.out.println(client.getSocketAddress().toString());
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (true) {
				if (stoped) {
					break;
				}

				if (client.getCurrentSendMessage() != null) {
					if (System.currentTimeMillis() - client.lastSendTime > client
							.getTimeOut()) {
						System.out.println("send");
						SendUdpMessage(serverSocket, client.getSocketAddress(),
								client.currentSendMessage);
						client.setLastSendTime(System.currentTimeMillis());
					}
				}

			}
		}

		public void SendUdpMessage(DatagramSocket sendSocket,
				SocketAddress address, UdpMessage message) {
			try {
				byte[] temp = message.toBytes();
				DatagramPacket sendPacket = new DatagramPacket(temp, temp.length,
						address);
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
		volatile Client client;

		// public DatagramSocket receiveSocket;

		public ReceiveServicer() {
			client = clientMap.get("server");
			/*
			 * try { receiveSocket=new DatagramSocket(9091); } catch
			 * (SocketException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}

		public ReceiveServicer(Client client) {
			this.client = client;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				if (stoped) {
					break;
				}
				try {
					if (serverSocket == null) {
						System.out.println("serverSocket == null!");
						break;
					}

					byte[] recvBuf = new byte[65515];
					DatagramPacket recvPacket = new DatagramPacket(recvBuf,
							recvBuf.length);
					serverSocket.receive(recvPacket);
					UdpMessage message = new UdpMessage(recvPacket);
					System.out.println("recive:" + message.toString());
					if (message.getType() == 2) {
						if (message.getSequenceId() == client.currentSendMessage
								.getSequenceId()) {
							client.lastSequenceId++;
							client.setCurrentSendMessage(null);
						}
					} else if (message.getType() == 1) {
						if (message.getSequenceId() == client.lastSequenceId + 1) {
							client.messageQueue.add(message);
						}
					} else if (message.getType() == 3) {
						try {
							if (message.getSequenceId() >= client
									.getCurrentSendMessage().getSequenceId()) {
								client.setCurrentSendMessage(null);
							}
						} catch (NullPointerException e) {
							// TODO: handle exception
						}
						
						System.out.println("诶呀，发错了！");
						// client.currentSendMessage.setSequenceId(message.sequenceId+1);
					}
				} catch (SocketException e) {
					System.out.println("socket closed!");

				} catch (Exception e) {
					// TODO: handle exception

					e.printStackTrace();
				}
			}

		}

	}

	public void startService(Client client) {
		Runnable s1 = new SendServicer(client);
		Runnable s2 = new ReceiveServicer(client);

		sendThread = new Thread(s1);
		reciveThread = new Thread(s2);

		sendThread.start();
		reciveThread.start();
	}

	public void startService(int port) throws Exception {

		/*
		 * Servicer s1 = new Servicer(port);
		 * 
		 * new Thread(s1).start();
		 */

		SendServicer s1 = new SendServicer();
		ReceiveServicer s2 = new ReceiveServicer();

		sendThread = new Thread(s1);
		reciveThread = new Thread(s2);

		sendThread.start();
		reciveThread.start();

	}

	/*
	 * public class Servicer implements Runnable {
	 * 
	 * int lastPackageId = 0; int port = 9091;
	 * 
	 * public Servicer() throws SocketException {
	 * 
	 * serverSocket = new DatagramSocket(port); }
	 * 
	 * public Servicer(int port) throws SocketException { this.port = port;
	 * 
	 * serverSocket = new DatagramSocket(port);
	 * 
	 * }
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * 
	 * udpService();
	 * 
	 * }
	 * 
	 * private void udpService() { try {
	 * 
	 * byte[] recvBuf = new byte[65515]; DatagramPacket recvPacket = new
	 * DatagramPacket(recvBuf, recvBuf.length);
	 * 
	 * serverSocket.receive(recvPacket); String str =
	 * recvPacket.getSocketAddress().toString(); UdpMessage message = new
	 * UdpMessage(recvPacket); Client client = clientMap.get(str); if (client ==
	 * null) { client = new Client(); clientMap.put(str, client); } if
	 * (message.getSequenceId() != client.getLastSequenceId() + 1) {
	 * System.out.println("sequenceId 错误！");
	 * System.out.println(message.getSequenceId()); UdpMessage responds = new
	 * UdpMessage(); responds.sequenceId = 0; responds.type = 3; responds.length
	 * = 4; responds.data = JavaDataConverter.intToBytes(message
	 * .getSequenceId()); send(recvPacket.getSocketAddress(), responds); } else
	 * { System.out.println("sequenceId 正确！");
	 * System.out.println(message.toString());
	 * client.getMessageQueue().add(message); client.lastSequenceId++; }
	 * 
	 * serverSocket.close(); } catch (Exception e) { // TODO: handle exception
	 * 
	 * e.printStackTrace(); } }
	 * 
	 * public void setPort(int port) { this.port = port; }
	 * 
	 * public void send(SocketAddress address, UdpMessage message) {
	 * 
	 * DatagramPacket sendPacket = new DatagramPacket(message.toBytes(),
	 * message.toBytes().length, address); try { serverSocket.send(sendPacket);
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }
	 */

	/*
	 * public void SendUdpMessage(int src, Client client, UdpMessage message) {
	 * 
	 * byte[] temp = message.toBytes(); DatagramPacket sendPacket = new
	 * DatagramPacket(temp, temp.length, client.getSocketAddress()); try {
	 * serverSocket = new DatagramSocket(src); serverSocket.send(sendPacket);
	 * serverSocket.close(); } catch (IOException e) { e.printStackTrace(); } }
	 * 
	 * public void SendUdpMessage(SocketAddress address, UdpMessage message) {
	 * 
	 * byte[] temp = message.toBytes(); DatagramPacket sendPacket = new
	 * DatagramPacket(temp, temp.length, address);
	 * 
	 * try { serverSocket.send(sendPacket); // byte[] recvBuf = new byte[65515];
	 * // DatagramPacket recvPacket = new //
	 * DatagramPacket(recvBuf,recvBuf.length); // UdpMessage recvMessage=new
	 * UdpMessage(); // if // VerifySend(message.getSequenceId(),recvPacket,
	 * recvMessage);
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } }
	 * 
	 * public boolean VerifySend(int sequenceId, DatagramPacket recvPacket,
	 * UdpMessage recvMessage) throws IOException {
	 * serverSocket.receive(recvPacket);
	 * recvMessage.initUdpMessageByDatagramPacket(recvPacket); if
	 * (recvMessage.getType() == 2) { if (recvMessage.getSequenceId() ==
	 * sequenceId) { return true; } } return false;
	 * 
	 * }
	 * 
	 * public void send(int src, int dst, UdpMessage message) {
	 * 
	 * try { serverSocket = new DatagramSocket(src); DatagramPacket sendPacket =
	 * new DatagramPacket(message.toBytes(), message.toBytes().length,
	 * InetAddress.getLocalHost(), dst);
	 * 
	 * serverSocket.send(sendPacket); serverSocket.close(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 */
}
