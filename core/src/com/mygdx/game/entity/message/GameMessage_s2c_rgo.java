package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.ByteUtil;

public class GameMessage_s2c_rgo extends GameMessage {
	public long gameObjectId;
	
	
	public GameMessage_s2c_rgo() {
		this.type=GameMessageType.S2C_B2D_REMOVE_GAMEOBJECT.ordinal();
	}
	public GameMessage_s2c_rgo(byte[] src) {
		this.type=GameMessageType.S2C_B2D_REMOVE_GAMEOBJECT.ordinal();
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+GameMessage.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessage.TYPE_BYTE_LENGTH);offset+=GameMessage.TYPE_BYTE_LENGTH;
		src=ByteUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);
		return dest;
	}
	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		this.gameObjectId=ByteUtil.bytesToLong(ByteUtil.subByte(src, 8, 0));

	}
}
