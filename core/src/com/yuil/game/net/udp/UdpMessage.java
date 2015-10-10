package com.yuil.game.net.udp;

import java.net.DatagramPacket;
import java.util.Arrays;

import com.yuil.game.util.ByteUtil;

public class UdpMessage {
	public long sessionId;
	public int sequenceId;
	public byte type;// 0：退出，1：順序消息，2：确认,3：错误
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
		message.setSessionId(ByteUtil.bytesToLong(ByteUtil
				.subByte(data, 8, offset)));
		offset+=8;
		message.setSequenceId(ByteUtil.bytesToInt(ByteUtil
				.subByte(data, 4, offset)));
		offset+=4;
		message.setType(ByteUtil.subByte(
				data, 1, offset)[0]);
		offset+=1;
		message.setLength(ByteUtil.bytesToInt(ByteUtil
				.subByte(data, 4, offset)));
		offset+=4;
		message.setData(ByteUtil.subByte(data,message.length,offset));
	}

	public void initUdpMessageByDatagramPacket(DatagramPacket recvPacket) {
		byte[] data = recvPacket.getData();
		int offset = 0;
		this.setSessionId(ByteUtil.bytesToLong(ByteUtil
				.subByte(data, 8, offset)));
		offset+=8;
		this.setSequenceId(ByteUtil.bytesToInt(ByteUtil
				.subByte(recvPacket.getData(), 4, offset)));
		offset+=4;
		this.setType(ByteUtil.subByte(
				recvPacket.getData(), 1, offset)[0]);
		offset+=1;
		this.setLength(ByteUtil.bytesToInt(ByteUtil
				.subByte(recvPacket.getData(), 4, offset)));
		offset+=4;
		//if(this.length>0){
			//System.out.println("offset:"+offset);
			this.initDateFromUdpbytes(ByteUtil.subByte(recvPacket.getData(),this.length,offset));
		//}
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

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
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
		//System.out.println("initdate:"+this.toString());
		//System.out.println("data.length:"+data.length);

		this.data = new byte[this.length];
		System.arraycopy(data, 0, this.data, 0, this.length);
	}

	public byte[] toBytes() {
		byte[] dest = new byte[17 + length];
		System.arraycopy(ByteUtil.longToBytes(sessionId), 0, dest, 0,8);		
		System.arraycopy(ByteUtil.intToBytes(sequenceId), 0, dest, 8,4);
		System.arraycopy(ByteUtil.intToBytes(type), 0, dest, 12, 1);
		System.arraycopy(ByteUtil.intToBytes(length), 0, dest, 13, 4);
		if (data!=null) {
			System.arraycopy(data, 0, dest, 17, length);
		}
		return dest;
	}
	@Override
	public String toString() {
		return "UdpMessage [sessionId=" + sessionId + ", sequenceId="
				+ sequenceId + ", type=" + type + ", length=" + length
				+ ", data=" + Arrays.toString(data) + "]";
	}

}
