package com.mygdx.game.entity.message;

import com.mygdx.game.util.JavaDataConverter;

public class GameMessage_c2s_gago extends GameMessage{

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[4];
		byte[] src=JavaDataConverter.intToBytes(GameMessageType.c2s_ago);
		System.arraycopy(src, 0, dest, offset, 4);
		return dest;
	}
}
