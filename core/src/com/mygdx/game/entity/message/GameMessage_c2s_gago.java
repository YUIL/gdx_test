package com.mygdx.game.entity.message;

import com.mygdx.game.util.ByteUtil;

public class GameMessage_c2s_gago extends GameMessage{

	
	public GameMessage_c2s_gago(){
		this.type=GameMessageType.C2S_B2D_GET_ALL_GAMEOBJECT.ordinal();
	}
	
	public GameMessage_c2s_gago(byte[] src){
		this.type=GameMessageType.C2S_B2D_GET_ALL_GAMEOBJECT.ordinal();
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[GameMessage.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessage.TYPE_BYTE_LENGTH);
		return dest;
	}

	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub

	}
}
