package com.yuil.game.entity.info;

import com.yuil.game.util.ByteUtil;

public class B2dUpdateInformation {
	public long gameObjectId;
	public float x;
	public float y;
	public float angle;
	public float angularVelocity;
	public float lx;
	public float ly;
	
	public static final int informationLength=30;
	
	
	public B2dUpdateInformation(){
		
	}
	public B2dUpdateInformation(byte[] src,int offset){
		this.initFromBytes(src, offset);
	}
	
	public void initFromBytes(byte[] src,int offset){
		gameObjectId=ByteUtil.bytesToLong(ByteUtil.subByte(src, 8, offset));offset+=8;
		x=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		y=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		angle=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		angularVelocity=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		lx=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		ly=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
	}
	
	public byte[] toBytes(){
		int offset=0;
		byte[] dest=new byte[this.informationLength];
		byte[] src=ByteUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src = ByteUtil.floatToBytes(this.x);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.y);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.angle);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.angularVelocity);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.lx);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.ly);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		return dest;
	}
}
