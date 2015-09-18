package com.mygdx.game.entity.message;



public abstract class GameMessage{

	
	public int type;
	
	

	@Override
	public String toString() {
		return "GameMessage [type=" + type + "]";
	}
	
	
	public abstract void initFromBytes(byte[] src);
	public abstract byte[] toBytes();

}
