package com.mygdx.game.server.message;

import com.mygdx.game.net.message.Message;

public class C2S_LOGIN extends Message{
	
	

	public C2S_LOGIN() {
		super();
		this.type=MessageType.C2S_LOGIN.ordinal();
	}

	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}
