package com.yuil.game.entity.message;

import com.yuil.game.net.message.Message;
import com.yuil.game.util.DataUtil;

public class C2S_B2D_GET_ALL_GAMEOBJECT extends Message{

	
	public C2S_B2D_GET_ALL_GAMEOBJECT(){
		this.type=GameMessageType.C2S_B2D_GET_ALL_GAMEOBJECT.ordinal();
	}
	
	public C2S_B2D_GET_ALL_GAMEOBJECT(byte[] src){
		this.type=GameMessageType.C2S_B2D_GET_ALL_GAMEOBJECT.ordinal();
		this.init(src);
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[Message.TYPE_BYTE_LENGTH];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);
		return dest;
	}

	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub

	}
}
