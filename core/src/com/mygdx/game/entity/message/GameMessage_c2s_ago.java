package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.ByteUtil;

public class GameMessage_c2s_ago extends GameMessage {
	public B2dBoxBaseInformation b2dBoxBaseInformation=new B2dBoxBaseInformation();
	
	

	public GameMessage_c2s_ago() {
		this.type=GameMessageType.c2s_b2d_add_gameobject;

	}
	public GameMessage_c2s_ago(byte[] src) {
		this.type=GameMessageType.c2s_b2d_add_gameobject;
		this.initFromBytes(src);

	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+GameMessageType.length];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessageType.length);offset+=GameMessageType.length;
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
