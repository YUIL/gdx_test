package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.JavaDataConverter;

public class GameMessage_s2c_ago extends GameMessage {
	public B2dBoxBaseInformation b2dBoxBaseInformation=new B2dBoxBaseInformation();
	
	
	public GameMessage_s2c_ago() {
		this.type=GameMessageType.s2c_ago;
	}
	public GameMessage_s2c_ago(byte[] src) {
		this.type=GameMessageType.s2c_ago;
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+4];
		byte[] src=JavaDataConverter.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
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
