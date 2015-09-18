package com.mygdx.game.entity.message;

import com.mygdx.game.util.ByteUtil;

public class GameMessage_c2s_gago extends GameMessage{

	
	public GameMessage_c2s_gago(){
		this.type=GameMessageType.c2s_b2d_get_all_gameobject;
	}
	
	public GameMessage_c2s_gago(byte[] src){
		this.type=GameMessageType.c2s_b2d_get_all_gameobject;
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[GameMessageType.length];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessageType.length);
		return dest;
	}

	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub

	}
}
