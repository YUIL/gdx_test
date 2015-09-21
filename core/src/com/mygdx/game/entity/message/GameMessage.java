package com.mygdx.game.entity.message;

public abstract class GameMessage{

	public static final int TYPE_BYTE_LENGTH=1;
	public int type;
	

	@Override
	public String toString() {
		return "GameMessage [type=" + type + "]";
	}
	
	
	public abstract void initFromBytes(byte[] src);
	public abstract byte[] toBytes();

}
