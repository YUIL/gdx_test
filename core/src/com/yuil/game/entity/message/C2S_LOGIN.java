package com.yuil.game.entity.message;

import com.yuil.game.entity.info.B2dBoxBaseInformation;
import com.yuil.game.net.message.Message;
import com.yuil.game.util.ByteUtil;

public class C2S_LOGIN extends Message{
	
	public final int openIdLength=32;
	public String openId=null;

	public C2S_LOGIN() {
		super();
		this.type=GameMessageType.C2S_LOGIN.ordinal();
	}
	
	public C2S_LOGIN(String openId){
		super();
		if(openId.length()!=32){
			throw new IllegalArgumentException("openId's length must equels 32");
		}else{
			this.type=GameMessageType.C2S_LOGIN.ordinal();
			this.init(openId.getBytes());
		}
	}
	public C2S_LOGIN(byte[] src) {
		super();
		this.type=GameMessageType.C2S_LOGIN.ordinal();
		this.init(src);
	}

	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		//int offset=0;
		this.openId=new String(src);
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		int offset=0;
		byte[] dest=new byte[openIdLength+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=openId.getBytes();
		System.arraycopy(src, 0, dest, offset, src.length);	
		return dest;
	}

}
