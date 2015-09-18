package com.mygdx.game.entity.message;

import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.ByteUtil;

public class GameMessage_c2s_ggo extends GameMessage {
	public long gameObjectId;

	
	public GameMessage_c2s_ggo() {
		this.type=GameMessageType.c2s_b2d_get_gameobject;
	}
	public GameMessage_c2s_ggo(byte [] src) {
		this.type=GameMessageType.c2s_b2d_get_gameobject;
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
