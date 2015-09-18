package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.JavaDataConverter;

public class GameMessage_c2s_rgo extends GameMessage {
	public long gameObjectId;
	
	
	
	public GameMessage_c2s_rgo() {
		this.type=GameMessageType.c2s_rgo;
	}
	public GameMessage_c2s_rgo(byte[] src) {
		this.type=GameMessageType.c2s_rgo;
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+4];
		byte[] src=JavaDataConverter.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src=JavaDataConverter.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);
		return dest;
	}
	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		this.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, 0));

	}
}
