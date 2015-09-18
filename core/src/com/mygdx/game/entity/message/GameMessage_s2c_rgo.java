package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.ByteUtil;

public class GameMessage_s2c_rgo extends GameMessage {
	public long gameObjectId;
	
	
	public GameMessage_s2c_rgo() {
		this.type=GameMessageType.s2c_b2d_remove_gameobject;
	}
	public GameMessage_s2c_rgo(byte[] src) {
		this.type=GameMessageType.s2c_b2d_remove_gameobject;
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+GameMessageType.length];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessageType.length);offset+=GameMessageType.length;
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
