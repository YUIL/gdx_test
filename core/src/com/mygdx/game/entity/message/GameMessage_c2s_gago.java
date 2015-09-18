package com.mygdx.game.entity.message;

import com.mygdx.game.util.JavaDataConverter;

public class GameMessage_c2s_gago extends GameMessage{

	
	public GameMessage_c2s_gago(){
		this.type=GameMessageType.c2s_gago;
	}
	
	public GameMessage_c2s_gago(byte[] src){
		this.type=GameMessageType.c2s_gago;
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[4];
		byte[] src=JavaDataConverter.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, 4);
		return dest;
	}

	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub

	}
}