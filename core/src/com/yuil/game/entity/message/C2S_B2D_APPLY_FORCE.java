package com.yuil.game.entity.message;

import com.yuil.game.entity.info.B2dBoxBaseInformation;
import com.yuil.game.net.message.Message;
import com.yuil.game.util.DataUtil;

public class C2S_B2D_APPLY_FORCE extends Message{
	public long gameObjectId;
	public float forceX;
	public float forceY;
	
	public C2S_B2D_APPLY_FORCE() {
		this.type=GameMessageType.C2S_B2D_APPLY_FORCE.ordinal();
	}
	public C2S_B2D_APPLY_FORCE(byte[] src) {
		this.type=GameMessageType.C2S_B2D_APPLY_FORCE.ordinal();
		this.init(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+Message.TYPE_BYTE_LENGTH];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=DataUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src=DataUtil.floatToBytes(forceX);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src=DataUtil.floatToBytes(forceY);
		System.arraycopy(src, 0, dest, offset, 4);
		return dest;
	}


	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		int offset=0;
		this.gameObjectId=DataUtil.bytesToLong(DataUtil.subByte(src, 8, offset));offset+=8;
		this.forceX=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));offset+=4;
		this.forceY=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));
	}

}
