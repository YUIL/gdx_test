package com.yuil.game.net.message;

import com.yuil.game.util.ByteUtil;

public class GAME_MESSAGE extends Message{

	public byte[] data;
	
	public GAME_MESSAGE() {
		super();
		this.type=MessageType.GAME_MESSAGE.ordinal();
	}
	
	public GAME_MESSAGE(byte[] src) {
		super();
		this.type=MessageType.GAME_MESSAGE.ordinal();
		this.init(src);
	}


	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		this.data=src;
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[data.length+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=data;
		System.arraycopy(src, 0, dest, offset, src.length);	
		return dest;
	}
	public static byte[] getBytes(byte[] data){
		int offset=0;
		byte[] dest=new byte[data.length+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(MessageType.GAME_MESSAGE.ordinal());
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=data;
		System.arraycopy(src, 0, dest, offset, src.length);	
		return dest;
	}
}
