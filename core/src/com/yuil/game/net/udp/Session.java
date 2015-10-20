package com.yuil.game.net.udp;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.yuil.game.net.udp.UdpServer.SendServicer;


public class Session {
	
	volatile boolean isSending=false;
	long id;
	int timeOut=10;
	int maxResendTimes=20;
	SendServicer sendThread;
	volatile short resendTimes=0;
	long lastSendTime;
	InetSocketAddress contactorAddress;
	volatile UdpMessage currentSendMessage;
	public  int lastSendSequenceId;
	public  int lastRecvSequenceId;
	volatile Queue<UdpMessage> sendMessageBuffer = new LinkedList<UdpMessage>();
	public volatile int sendMessageBufferMaxSize=5;
	
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
	}

	public SendServicer getSendThread() {
		return sendThread;
	}

	public void setSendThread(SendServicer sendThread) {
		this.sendThread = sendThread;
	}

	public short getTimeOutMultiple() {
		return resendTimes;
	}

	public void setTimeOutMultiple(short timeOutMultiple) {
		this.resendTimes = timeOutMultiple;
	}

	
	public int getSendMessageBufferMaxSize() {
		return sendMessageBufferMaxSize;
	}

	public void setSendMessageBufferMaxSize(int sendMessageBufferMaxSize) {
		this.sendMessageBufferMaxSize = sendMessageBufferMaxSize;
	}

	public Queue<UdpMessage> getSendMessageBuffer() {
		return sendMessageBuffer;
	}

	public void setSendMessageBuffer(Queue<UdpMessage> sendMessageBuffer) {
		this.sendMessageBuffer = sendMessageBuffer;
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

	public long getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", timeOut=" + timeOut + ", maxResendTimes=" + maxResendTimes + ", sendThread="
				+ sendThread + ", timeOutMultiple=" + resendTimes + ", lastSendTime=" + lastSendTime
				+ ", contactorAddress=" + contactorAddress + ", currentSendMessage=" + currentSendMessage
				+ ", lastSendSequenceId=" + lastSendSequenceId + ", lastRecvSequenceId=" + lastRecvSequenceId + "]";
	}

}
