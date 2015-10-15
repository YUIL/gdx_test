package com.yuil.game.entity.info;

import com.yuil.game.util.DataUtil;

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
		gameObjectId=DataUtil.bytesToLong(DataUtil.subByte(src, 8, offset));offset+=8;
		x=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));offset+=4;
		y=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));offset+=4;
		angle=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));offset+=4;
		angularVelocity=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));offset+=4;
		lx=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));offset+=4;
		ly=DataUtil.bytesToFloat(DataUtil.subByte(src, 4, offset));offset+=4;
	}
	
	public byte[] toBytes(){
		int offset=0;
		byte[] dest=new byte[this.informationLength];
		byte[] src=DataUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src = DataUtil.floatToBytes(this.x);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = DataUtil.floatToBytes(this.y);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = DataUtil.floatToBytes(this.angle);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = DataUtil.floatToBytes(this.angularVelocity);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = DataUtil.floatToBytes(this.lx);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = DataUtil.floatToBytes(this.ly);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		return dest;
	}
}
