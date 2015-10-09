package com.mygdx.game.entity.message;

import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.net.message.Message;
import com.mygdx.game.util.ByteUtil;

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
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=ByteUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);
		return dest;
	}

	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		this.gameObjectId=ByteUtil.bytesToLong(ByteUtil.subByte(src, 8, 0));

	}
	
}
