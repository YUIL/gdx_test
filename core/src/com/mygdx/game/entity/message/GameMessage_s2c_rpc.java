package com.mygdx.game.entity.message;

public class GameMessage_s2c_rpc extends GameMessage {

	
	
	
	public GameMessage_s2c_rpc() {
		this.type=GameMessageType.S2C_B2D_APPLY_FORCE.ordinal();
	}
	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		
	}
	
}
