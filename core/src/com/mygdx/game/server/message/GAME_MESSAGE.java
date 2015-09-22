package com.mygdx.game.server.message;

import com.mygdx.game.net.message.Message;

public class GAME_MESSAGE extends Message{

	
	
	public GAME_MESSAGE() {
		super();
		this.type=MessageType.GAME_MESSAGE.ordinal();
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
