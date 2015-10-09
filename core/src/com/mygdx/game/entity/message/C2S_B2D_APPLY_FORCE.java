package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.net.message.Message;
import com.mygdx.game.util.ByteUtil;

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
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=ByteUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src=ByteUtil.floatToBytes(forceX);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src=ByteUtil.floatToBytes(forceY);
		System.arraycopy(src, 0, dest, offset, 4);
		return dest;
	}


	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		int offset=0;
		this.gameObjectId=ByteUtil.bytesToLong(ByteUtil.subByte(src, 8, offset));offset+=8;
		this.forceX=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		this.forceY=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));
	}

}
