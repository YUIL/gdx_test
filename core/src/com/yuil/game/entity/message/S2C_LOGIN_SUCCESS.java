package com.yuil.game.entity.message;

import com.yuil.game.entity.info.B2dBoxBaseInformation;
import com.yuil.game.net.message.Message;
import com.yuil.game.util.DataUtil;

public class S2C_LOGIN_SUCCESS extends Message{
	public int userId;
	public S2C_LOGIN_SUCCESS(){
		super();
		this.type=GameMessageType.S2C_LOGIN_SUCCESS.ordinal();
	}
	public S2C_LOGIN_SUCCESS(byte[] src){
		super();
		this.type=GameMessageType.S2C_LOGIN_SUCCESS.ordinal();
		this.init(src);
	}


	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		//int offset=0;
		this.userId=DataUtil.bytesToInt(src);
	}

	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[4+Message.TYPE_LENGTH];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_LENGTH);offset+=Message.TYPE_LENGTH;
		src=DataUtil.intToBytes(this.userId);
		System.arraycopy(src, 0, dest, offset, 4);	
		return dest;
	}
}
