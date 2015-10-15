package com.yuil.game.net.message;

import com.yuil.game.util.DataUtil;

public class GAME_MESSAGE_ARRAY extends Message {
	
	byte messageNum;
	short[] messageLengths;
	byte[] gameMessages;
	
	public GAME_MESSAGE_ARRAY(){
		this.type=MessageType.GAME_MESSAGE_ARRAY.ordinal();
	}
	
	public GAME_MESSAGE_ARRAY(byte[] src){
		this.type=MessageType.GAME_MESSAGE_ARRAY.ordinal();
		this.init(src);
	}
	
	@Override
	public void init(byte[] src) {
		int offset=0;
		messageNum=DataUtil.subByte(src, 1,offset )[0];offset++;
		messageLengths=new short[messageNum];
		for (int i = 0; i <messageNum; i++) {
			messageLengths[i]=DataUtil.bytesToShort(DataUtil.subByte(src, 2, offset));
		}
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}
