package com.yuil.game.entity.message;

import com.yuil.game.net.message.Message;
import com.yuil.game.util.DataUtil;

public class S2C_B2D_CHANGE_APPLY_FORCE_STATE extends Message {
	public long gameObjectId;
	public byte applyForceState;
	
	
	public S2C_B2D_CHANGE_APPLY_FORCE_STATE(){
		this.type=GameMessageType.S2C_B2D_CHANGE_APPLY_FORCE_STATE.ordinal();
	}
	public S2C_B2D_CHANGE_APPLY_FORCE_STATE(byte [] src) {
		this.type=GameMessageType.S2C_B2D_CHANGE_APPLY_FORCE_STATE.ordinal();
		this.init(src);
	}
	public S2C_B2D_CHANGE_APPLY_FORCE_STATE(byte applyForceState){
		this.type=GameMessageType.S2C_B2D_CHANGE_APPLY_FORCE_STATE.ordinal();
		this.applyForceState=applyForceState;
	}
	
	@Override
	public void init(byte[] src) {
		int offset=0;
		this.gameObjectId=DataUtil.bytesToLong(DataUtil.subByte(src, 8, offset));offset+=8;
		this.applyForceState=DataUtil.subByte(src, 1, offset)[0];
	}

	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[1+8+Message.TYPE_LENGTH];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_LENGTH);offset+=Message.TYPE_LENGTH;
		src=DataUtil.longToBytes(this.gameObjectId);
		System.arraycopy(src, 0, dest, offset, 8);offset+=8;
		src=new byte[1];
		src[0]=applyForceState;
		System.arraycopy(src, 0, dest, offset,1);
		return dest;
	}

}
