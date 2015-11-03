package com.yuil.game.net.message;

import com.yuil.game.util.DataUtil;

public abstract class Message{

	public static final int TYPE_LENGTH=1;
	public int type;
	
	@Override
	public String toString() {
		return "Message [type=" + type + "]";
	}
	
	public static int getType(byte[] src){
		return DataUtil.bytesToInt(DataUtil.subByte(src, Message.TYPE_LENGTH, 0)); 
	}
	
	public abstract void init(byte[] src);
	public abstract byte[] toBytes();

}
