package com.mygdx.game.net;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.Queue;


public class Client {
	int lastSequenceId=0;
	SocketAddress socketAddress;
	long lastSendTime;
	int timeOut=50;
	UdpMessage currentSendMessage;
	UdpMessage lastSendMessage;
	
	public boolean isSending=false;

	public Queue<UdpMessage> messageQueue=new LinkedList<UdpMessage>();
	public boolean send(byte[] data){
		System.out.println(lastSequenceId);
		UdpMessage message=new UdpMessage();
		message.sequenceId=lastSequenceId+1;
		message.type=1;
		message.length=data.length;
		message.data=data;
		return send(message);
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public long getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	public int getLastSequenceId() {
		return lastSequenceId;
	}
	public void setLastSequenceId(int lastSequenceId) {
		this.lastSequenceId = lastSequenceId;
	}
	public  UdpMessage getCurrentSendMessage() {
		//currentSendMessageLocked=true;
		return currentSendMessage;
	}

	public synchronized UdpMessage currentSendUdpMessage(UdpMessage message){
		if (message==null){
			return this.currentSendMessage;
		}else{
			this.currentSendMessage=message;
			return null;
		}
		
	}

	public boolean isSending() {
		return isSending;
	}
	public void setSending(boolean isSending) {
		this.isSending = isSending;
	}
	public synchronized boolean send(UdpMessage message) {
		if(currentSendUdpMessage(null)==null){
			currentSendUdpMessage(message);
			return true;
		}else{
			return false;
		}
		
		
	}
	public  void setCurrentSendMessage(UdpMessage currentSendMessage) {

			this.currentSendMessage = currentSendMessage;
	}
	public  UdpMessage getLastSendMessage() {
		return lastSendMessage;
	}
	public  void setLastSendMessage(UdpMessage lastSendMessage) {
		this.lastSendMessage = lastSendMessage;
	}
	public SocketAddress getSocketAddress() {
		return socketAddress;
	}
	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}
	public Queue<UdpMessage> getMessageQueue() {
		return messageQueue;
	}
	public void setMessageQueue(Queue<UdpMessage> messageQueue) {
		this.messageQueue = messageQueue;
	}
	
	
}