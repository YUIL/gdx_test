package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.net.message.Message;
import com.mygdx.game.util.ByteUtil;

public class S2C_LOGIN_SUCCESS extends Message{
	int userId;
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
		this.userId=ByteUtil.bytesToInt(src);
	}

	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[4+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=ByteUtil.intToBytes(this.userId);
		System.arraycopy(src, 0, dest, offset, 4);	
		return dest;
	}
}
