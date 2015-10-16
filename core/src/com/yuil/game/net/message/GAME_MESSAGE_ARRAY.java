package com.yuil.game.net.message;

import com.badlogic.gdx.utils.Json;

import com.yuil.game.util.DataUtil;

public class GAME_MESSAGE_ARRAY extends Message {
	
	public short messageNum;
	public int messageLength;
	public int[] messageLengths;
	public byte[][] gameMessages;
	
	public GAME_MESSAGE_ARRAY(){
		this.type=MessageType.GAME_MESSAGE_ARRAY.ordinal();
	}
	
	public GAME_MESSAGE_ARRAY(byte[] src){
		this.type=MessageType.GAME_MESSAGE_ARRAY.ordinal();
		this.init(src);
	}
	
	public GAME_MESSAGE_ARRAY(Message[] messages){
		this.type=MessageType.GAME_MESSAGE_ARRAY.ordinal();
		this.init(messages);
	}
	
	public void init(Message[] messages){
		if( messages.length>255){
			throw new IllegalArgumentException("message's length must <255");
		}
		
		messageNum=(byte) messages.length;
		messageLengths=new int[messageNum];
		gameMessages=new byte[messageNum][];
		for (int i = 0; i < messages.length; i++) {
			byte[] src=messages[i].toBytes();
			messageLength+=src.length;
			messageLengths[i]=src.length;
			gameMessages[i]=src;
		}
		
		
	}
	
	@Override
	public void init(byte[] src) {
		int offset=0;
		messageNum=(short) DataUtil.getUnsignedNum(DataUtil.subByte(src, 1,offset )[0]);offset++;
		messageLengths=new int[messageNum];
		for (int i = 0; i <messageNum; i++) {
			messageLengths[i]=DataUtil.getUnsignedNum(DataUtil.bytesToShort(DataUtil.subByte(src, 2, offset)));offset+=2;
			messageLength+=messageLengths[i];
		}
		gameMessages=new byte[messageNum][];
		for (int i = 0; i < messageNum; i++) {
			gameMessages[i]=DataUtil.subByte(src, messageLengths[i], offset);offset+=messageLengths[i];
		}
	}

	@Override
	public byte[] toBytes() {
		int offset=0;

		byte[] dest=new byte[1+2*messageNum+messageLength+Message.TYPE_LENGTH];
		
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_LENGTH);offset+=Message.TYPE_LENGTH;
		
		src=new byte[1];
		src[0]=(byte) messageNum;
		System.arraycopy(src, 0, dest, offset, src.length);	offset+=src.length;
		
		for (int i = 0; i < messageNum; i++) {
			src=DataUtil.shortToBytes((short)messageLengths[i]);
			System.arraycopy(src, 0, dest, offset, src.length);	offset+=src.length;
		}
		
		for (int i = 0; i < messageNum; i++) {
			src=gameMessages[i];
			System.arraycopy(src, 0, dest, offset, src.length);	offset+=src.length;
		}
		return dest;
	}



}
