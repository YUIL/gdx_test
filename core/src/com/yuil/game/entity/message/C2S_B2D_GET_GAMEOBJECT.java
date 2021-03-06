package com.yuil.game.entity.message;

import com.yuil.game.entity.B2dGameObject;
import com.yuil.game.entity.info.B2dBoxBaseInformation;
import com.yuil.game.net.message.Message;
import com.yuil.game.util.DataUtil;

public class C2S_B2D_GET_GAMEOBJECT extends Message {
	public long gameObjectId;

	
	public C2S_B2D_GET_GAMEOBJECT() {
		this.type=GameMessageType.C2S_B2D_GET_GAMEOBJECT.ordinal();
	}
	public C2S_B2D_GET_GAMEOBJECT(byte [] src) {
		this.type=GameMessageType.C2S_B2D_GET_GAMEOBJECT.ordinal();
		this.init(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[8+Message.TYPE_LENGTH];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_LENGTH);offset+=Message.TYPE_LENGTH;
		src=DataUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);
		return dest;
	}

	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		this.gameObjectId=DataUtil.bytesToLong(DataUtil.subByte(src, 8, 0));

	}
	
}
