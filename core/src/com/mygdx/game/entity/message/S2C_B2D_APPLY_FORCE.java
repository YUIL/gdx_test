package com.mygdx.game.entity.message;

import com.mygdx.game.net.message.Message;

public class S2C_B2D_APPLY_FORCE extends Message {

	
	
	
	public S2C_B2D_APPLY_FORCE() {
		this.type=GameMessageType.S2C_B2D_APPLY_FORCE.ordinal();
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		
	}
	
}
