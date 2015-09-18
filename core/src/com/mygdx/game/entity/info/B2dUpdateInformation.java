package com.mygdx.game.entity.info;

import com.mygdx.game.util.JavaDataConverter;

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
		gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, offset));offset+=8;
		x=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		y=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		angle=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		angularVelocity=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		lx=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		ly=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
	}
	
	public byte[] toBytes(){
		int offset=0;
		byte[] dest=new byte[this.informationLength];
		byte[] src=JavaDataConverter.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src = JavaDataConverter.floatToBytes(this.x);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.y);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.angle);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.angularVelocity);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.lx);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.ly);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		return dest;
	}
}
