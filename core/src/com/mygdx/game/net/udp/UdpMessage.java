package com.mygdx.game.net.udp;

import java.net.DatagramPacket;
import java.util.Arrays;

import com.mygdx.game.util.JavaDataConverter;

public class UdpMessage {
	public long sessionId;
	public int sequenceId;
	public int type;// 0：退出，1：順序消息，2：确认,3：错误
	public int length;
	public byte[] data;

	public UdpMessage() {

	}
	public UdpMessage(long sessionId,int sequenceId) {
		this.sessionId=sessionId;
		this.sequenceId=sequenceId;
	}
	public UdpMessage(DatagramPacket recvPacket) {
		initUdpMessageByDatagramPacket(this, recvPacket);
	}

	public void initUdpMessageByDatagramPacket(UdpMessage message,
			DatagramPacket recvPacket) {
		byte[] data = recvPacket.getData();
		initUdpMessageByDatagramPacket(message, data);
	}
	
	public void initUdpMessageByDatagramPacket(UdpMessage message,
			byte[] data) {
		
		int offset = 0;
		message.setSessionId(JavaDataConverter.bytesToLong(JavaDataConverter
				.subByte(data, 8, offset)));
		offset+=8;
		message.setSequenceId(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(data, 4, offset)));
		offset+=4;
		message.setType(JavaDataConverter.bytesToInt(JavaDataConverter.subByte(
				data, 4, offset)));
		offset+=4;
		message.setLength(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(data, 4, offset)));
		offset+=4;
		message.setData(JavaDataConverter.subByte(data,message.length,offset));
	}

	public void initUdpMessageByDatagramPacket(DatagramPacket recvPacket) {
		byte[] data = recvPacket.getData();
		int offset = 0;
		this.setSessionId(JavaDataConverter.bytesToLong(JavaDataConverter
				.subByte(data, 8, offset)));
		offset+=8;
		this.setSequenceId(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(recvPacket.getData(), 4, offset)));
		offset+=4;
		this.setType(JavaDataConverter.bytesToInt(JavaDataConverter.subByte(
				recvPacket.getData(), 4, offset)));
		offset+=4;
		this.setLength(JavaDataConverter.bytesToInt(JavaDataConverter
				.subByte(recvPacket.getData(), 4, offset)));
		offset+=4;
		this.initDateFromUdpbytes(JavaDataConverter.subByte(recvPacket.getData(),this.length,offset));
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int lenth) {
		this.length = lenth;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void initDateFromUdpbytes(byte[] data) {
		this.data = new byte[this.length];
		System.arraycopy(data, 20, this.data, 0, this.length);
	}

	public byte[] toBytes() {
		byte[] dest = new byte[20 + data.length];
		System.arraycopy(JavaDataConverter.longToBytes(sessionId), 0, dest, 0,8);		
		System.arraycopy(JavaDataConverter.intToBytes(sequenceId), 0, dest, 8,4);
		System.arraycopy(JavaDataConverter.intToBytes(type), 0, dest, 12, 4);
		System.arraycopy(JavaDataConverter.intToBytes(length), 0, dest, 16, 4);
		System.arraycopy(data, 0, dest, 20, data.length);
		return dest;
	}
	@Override
	public String toString() {
		return "UdpMessage [sessionId=" + sessionId + ", sequenceId="
				+ sequenceId + ", type=" + type + ", length=" + length
				+ ", data=" + Arrays.toString(data) + "]";
	}

}
