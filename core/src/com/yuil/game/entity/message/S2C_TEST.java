package com.yuil.game.entity.message;

import com.yuil.game.net.message.Message;
import com.yuil.game.util.DataUtil;

public class S2C_TEST extends Message {
	
	
	public S2C_TEST(){
		this.type=GameMessageType.S2C_TEST.ordinal();
	}

	public S2C_TEST(byte[] src){
		this.type=GameMessageType.S2C_TEST.ordinal();
		this.init(src);
	}

	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[Message.TYPE_LENGTH];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_LENGTH);offset+=Message.TYPE_LENGTH;
		return dest;
	}

}
