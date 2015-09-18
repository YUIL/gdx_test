package com.mygdx.game.net;

import java.net.DatagramPacket;
import java.util.Arrays;

import com.mygdx.game.util.ByteUtil;

public class UdpMessage {
	public int sequenceId;
	public int type;//0：退出，1：順序消息，2：确认,3：错误
	public int length;
	public byte[] data;

	public UdpMessage(){
		
	}
	public UdpMessage(DatagramPacket recvPacket) {
		initUdpMessageByDatagramPacket(this, recvPacket);
	}

	public void initUdpMessageByDatagramPacket(UdpMessage message,
			DatagramPacket recvPacket) {
		message.setSequenceId(ByteUtil.bytesToInt(ByteUtil
				.subByte(recvPacket.getData(), 4, 0)));
		message.setType(ByteUtil.bytesToInt(ByteUtil.subByte(
				recvPacket.getData(), 4, 4)));
		message.setLength(ByteUtil.bytesToInt(ByteUtil
				.subByte(recvPacket.getData(), 4, 8)));
		message.initDateFromUdpbytes(recvPacket.getData());
	}
	public void initUdpMessageByDatagramPacket(DatagramPacket recvPacket) {
		this.setSequenceId(ByteUtil.bytesToInt(ByteUtil
				.subByte(recvPacket.getData(), 4, 0)));
		this.setType(ByteUtil.bytesToInt(ByteUtil.subByte(
				recvPacket.getData(), 4, 4)));
		this.setLength(ByteUtil.bytesToInt(ByteUtil
				.subByte(recvPacket.getData(), 4, 8)));
		this.initDateFromUdpbytes(recvPacket.getData());
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

	@Override
	public String toString() {
		return "UdpMessage [sequenceId=" + sequenceId + ", type=" + type
				+ ", lenth=" + length + ", data=" + Arrays.toString(data) + "]";
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
		System.arraycopy(data, 12, this.data, 0, this.length);
	}

	public byte[] toBytes() {
		byte[] dest = new byte[12 + data.length];
		System.arraycopy(ByteUtil.intToBytes(sequenceId), 0, dest, 0,4);
		System.arraycopy(ByteUtil.intToBytes(type), 0, dest, 4, 4);
		System.arraycopy(ByteUtil.intToBytes(length), 0, dest, 8, 4);
		System.arraycopy(data, 0, dest, 12, data.length);
		return dest;
	}
}
