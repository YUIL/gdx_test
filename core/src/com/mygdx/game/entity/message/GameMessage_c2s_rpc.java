package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.ByteUtil;

public class GameMessage_c2s_rpc extends GameMessage{
	public long gameObjectId;
	public float forceX;
	public float forceY;
	
	public GameMessage_c2s_rpc() {
		this.type=GameMessageType.C2S_B2D_APPLY_FORCE.ordinal();
	}
	public GameMessage_c2s_rpc(byte[] src) {
		this.type=GameMessageType.C2S_B2D_APPLY_FORCE.ordinal();
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+4];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessage.TYPE_BYTE_LENGTH);offset+=GameMessage.TYPE_BYTE_LENGTH;
		src=ByteUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src=ByteUtil.floatToBytes(forceX);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src=ByteUtil.floatToBytes(forceY);
		System.arraycopy(src, 0, dest, offset, 4);
		return dest;
	}


	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		int offset=0;
		this.gameObjectId=ByteUtil.bytesToLong(ByteUtil.subByte(src, 8, offset));offset+=8;
		this.forceX=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		this.forceY=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));
	}

}
