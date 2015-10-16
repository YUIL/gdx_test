package com.yuil.game.net.message;


import com.yuil.game.util.DataUtil;

public class USER_MESSAGE extends Message {
	public byte[] data;
	
	
	
	public USER_MESSAGE() {
		super();
		this.type=MessageType.USER_MESSAGE.ordinal();

		// TODO Auto-generated constructor stub
	}
	
	public USER_MESSAGE(byte[] src) {
		super();
		this.type=MessageType.USER_MESSAGE.ordinal();
		this.init(src);
		// TODO Auto-generated constructor stub
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
		byte[] dest=new byte[data.length+Message.TYPE_LENGTH];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_LENGTH);offset+=Message.TYPE_LENGTH;
		src=data;
		System.arraycopy(src, 0, dest, offset, src.length);	
		return dest;
	}

}
