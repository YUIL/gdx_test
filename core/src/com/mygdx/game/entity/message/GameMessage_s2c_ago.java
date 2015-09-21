package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.ByteUtil;

public class GameMessage_s2c_ago extends GameMessage {
	public B2dBoxBaseInformation b2dBoxBaseInformation=new B2dBoxBaseInformation();
	
	
	public GameMessage_s2c_ago() {
		this.type=GameMessageType.S2C_B2D_ADD_GAMEOBJECT.ordinal();
	}
	public GameMessage_s2c_ago(byte[] src) {
		this.type=GameMessageType.S2C_B2D_ADD_GAMEOBJECT.ordinal();
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+GameMessage.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessage.TYPE_BYTE_LENGTH);offset+=GameMessage.TYPE_BYTE_LENGTH;
		src=b2dBoxBaseInformation.toBytes();
		System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);
		return dest;
	}
	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		this.b2dBoxBaseInformation.initFromBytes(src);

	}
}
