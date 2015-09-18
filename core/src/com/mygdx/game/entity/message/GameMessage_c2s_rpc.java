package com.mygdx.game.entity.message;

import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.JavaDataConverter;

public class GameMessage_c2s_rpc extends GameMessage{
	public long gameObjectId;
	public float forceX;
	public float forceY;
	
	public GameMessage_c2s_rpc() {
		this.type=GameMessageType.c2s_rpc;
	}
	public GameMessage_c2s_rpc(byte[] src) {
		this.type=GameMessageType.c2s_rpc;
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+4];
		byte[] src=JavaDataConverter.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src=JavaDataConverter.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src=JavaDataConverter.floatToBytes(forceX);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src=JavaDataConverter.floatToBytes(forceY);
		System.arraycopy(src, 0, dest, offset, 4);
		return dest;
	}


	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		int offset=0;
		this.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, offset));offset+=8;
		this.forceX=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		this.forceY=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));
	}

}
