package com.yuil.game.net.message;



public abstract class Message implements java.io.Serializable{

	public static final int TYPE_LENGTH=1;
	public int type;
	

	@Override
	public String toString() {
		return "Message [type=" + type + "]";
	}
	
	
	public abstract void init(byte[] src);
	public abstract byte[] toBytes();

}
