package com.mygdx.game.entity.info;

import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.util.ByteUtil;

public class B2dBoxBaseInformation {
	public long gameObjectId;
	public float x;
	public float y;
	public float angle;
	public float angularVelocity;
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
	
	
	public B2dBoxBaseInformation(long gameObjectId, float x, float y, float angle, float angularVelocity, float width,
			float height, float density, float lx, float ly) {
		super();
		this.gameObjectId = gameObjectId;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.angularVelocity = angularVelocity;
		this.width = width;
		this.height = height;
		this.density = density;
		this.lx = lx;
		this.ly = ly;
	}
	public void initFromBytes(byte[] src){
		int offset=0;
		gameObjectId=ByteUtil.bytesToLong(ByteUtil.subByte(src, 8, offset));offset+=8;
		x=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		y=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		angle=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		angularVelocity=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		width=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		height=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		density=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		lx=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
		ly=ByteUtil.bytesToFloat(ByteUtil.subByte(src, 4, offset));offset+=4;
	}
	public void initFromObj(B2DGameObject obj){
		gameObjectId=obj.getId();
		x=obj.getPosition().x;
		y=obj.getPosition().y;
		angle=obj.getBody().getAngle();
		angularVelocity=obj.getBody().getAngularVelocity();
		width=obj.getHeight();
		height=obj.getHeight();
		density=obj.getDensity();
		lx=obj.getBody().getLinearVelocity().x;
		ly=obj.getBody().getLinearVelocity().y;
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
		src = ByteUtil.floatToBytes(this.width);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.height);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.density);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.lx);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(this.ly);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		return dest;
	}
	public static byte[] getBytesFromB2dGameObject(B2DGameObject obj){
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength];
		byte[] src=ByteUtil.longToBytes(obj.getId());
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src = ByteUtil.floatToBytes(obj.getPosition().x);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getPosition().y);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getBody().getAngle());
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getBody().getAngularVelocity());
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getWidth());
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getHeight());
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getDensity());
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getBody().getLinearVelocity().x);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		src = ByteUtil.floatToBytes(obj.getBody().getLinearVelocity().y);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		return dest;
	}
	@Override
	public String toString() {
		return "B2dBoxBaseInformation [gameObjectId=" + gameObjectId + ", x=" + x + ", y=" + y + ", angle=" + angle
				+ ", angularVelocity=" + angularVelocity + ", width=" + width + ", height=" + height + ", density=" + density + ", lx=" + lx
				+ ", ly=" + ly + "]";
	}
	
	
}
