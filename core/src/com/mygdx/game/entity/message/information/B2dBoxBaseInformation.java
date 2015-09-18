package com.mygdx.game.entity.message.information;

import com.mygdx.game.util.JavaDataConverter;

public class B2dBoxBaseInformation {
	public long gameObjectId;
	public float x;
	public float y;
	public float angle;
	public float av;
	public float width;
	public float height;
	public float density;
	public float lx;
	public float ly;
	
	public static final int informationLength=44;
	
	
	public B2dBoxBaseInformation(){
		
	}
	public B2dBoxBaseInformation(byte[] src){
		this.initFromBytes(src);
	}
	
	public void initFromBytes(byte[] src){
		int offset=0;
		gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, offset));offset+=8;
		x=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		y=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		angle=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		av=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		width=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		height=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		density=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
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
		src = JavaDataConverter.floatToBytes(this.av);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.width);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.height);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.density);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.lx);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = JavaDataConverter.floatToBytes(this.ly);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		return dest;
	}
}
