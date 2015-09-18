package com.mygdx.game.entity.message;

import com.mygdx.game.entity.message.information.B2dBoxBaseInformation;
import com.mygdx.game.util.JavaDataConverter;

public class GameMessage_c2s_ago extends GameMessage {
	public B2dBoxBaseInformation b2dBoxBaseInformation;
	
	

	public GameMessage_c2s_ago() {
		this.type=GameMessageType.c2s_ago;
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+4];
		byte[] src=JavaDataConverter.intToBytes(GameMessageType.c2s_ago);
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
