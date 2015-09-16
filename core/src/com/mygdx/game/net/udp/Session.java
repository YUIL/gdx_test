package com.mygdx.game.net.udp;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.mygdx.game.net.udp.UdpServer.SendServicer;


public class Session {
	long id;
	int timeOut=15;
	int maxResendTimes=10;
	SendServicer sendThread;

	public SendServicer getSendThread() {
		return sendThread;
	}

	public void setSendThread(SendServicer sendThread) {
		this.sendThread = sendThread;
	}

	public short getTimeOutMultiple() {
		return timeOutMultiple;
	}

	public void setTimeOutMultiple(short timeOutMultiple) {
		this.timeOutMultiple = timeOutMultiple;
	}

	short timeOutMultiple=0;
	long lastSendTime;
	InetSocketAddress contactorAddress;
	UdpMessage currentSendMessage;
	public  int lastSendSequenceId;
	public  int lastRecvSequenceId;

	//volatile Queue<UdpMessage> recvMessageQueue;

	public Session(){
		init(new Random().nextLong());
	}
	
	public Session(long id){
		init(id);
	}
	private void init(long id){
		System.out.println("new session,id:"+id);
		this.id=id;
		lastSendSequenceId=-1;
		lastRecvSequenceId=-1;
/*		this.setLastSendMessage(new UdpMessage(id, -1));
		this.setLastresponseMessage(new UdpMessage(id, -1));*/
		//recvMessageQueue=new LinkedList<UdpMessage>();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public InetSocketAddress getContactorAddress() {
		return contactorAddress;
	}
	public void setContactorAddress(InetSocketAddress contactorAddress) {
		this.contactorAddress = contactorAddress;
	}
	public UdpMessage getCurrentSendMessage() {
		return currentSendMessage;
	}
	public void setCurrentSendMessage(UdpMessage currentSendMessage) {
		this.currentSendMessage = currentSendMessage;
	}
/*	public synchronized UdpMessage getLastSendMessage() {
		return lastSendMessage;
	}
	public synchronized void setLastSendMessage(UdpMessage lastSendMessage) {
		this.lastSendMessage = lastSendMessage;
	}*/
	public long getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
/*	public   Queue<UdpMessage> getRecvMessageQueue() {
		return recvMessageQueue;
	}
	public   void setRecvMessageQueue(Queue<UdpMessage> responseMessageQueue) {
		this.recvMessageQueue = responseMessageQueue;
	}
	*/
	public synchronized UdpMessage currentSendUdpMessage(UdpMessage message){
		if (message==null){
			return this.currentSendMessage;
		}else{
			this.currentSendMessage=message;
			return null;
		}
		
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", timeOut=" + timeOut + ", maxResendTimes=" + maxResendTimes + ", sendThread="
				+ sendThread + ", timeOutMultiple=" + timeOutMultiple + ", lastSendTime=" + lastSendTime
				+ ", contactorAddress=" + contactorAddress + ", currentSendMessage=" + currentSendMessage
				+ ", lastSendSequenceId=" + lastSendSequenceId + ", lastRecvSequenceId=" + lastRecvSequenceId + "]";
	}

	


}
