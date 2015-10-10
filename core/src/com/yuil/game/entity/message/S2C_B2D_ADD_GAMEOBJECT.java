package com.yuil.game.entity.message;

import com.yuil.game.entity.info.B2dBoxBaseInformation;
import com.yuil.game.net.message.Message;
import com.yuil.game.util.ByteUtil;

public class S2C_B2D_ADD_GAMEOBJECT extends Message {
	public B2dBoxBaseInformation b2dBoxBaseInformation=new B2dBoxBaseInformation();
	
	
	public S2C_B2D_ADD_GAMEOBJECT() {
		this.type=GameMessageType.S2C_B2D_ADD_GAMEOBJECT.ordinal();
	}
	public S2C_B2D_ADD_GAMEOBJECT(byte[] src) {
		this.type=GameMessageType.S2C_B2D_ADD_GAMEOBJECT.ordinal();
		this.init(src);
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=b2dBoxBaseInformation.toBytes();
		System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);
		return dest;
	}
	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		this.b2dBoxBaseInformation.initFromBytes(src);

	}
}
