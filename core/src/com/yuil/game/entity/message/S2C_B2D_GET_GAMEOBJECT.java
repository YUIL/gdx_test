package com.yuil.game.entity.message;

import com.yuil.game.entity.B2DGameObject;
import com.yuil.game.entity.info.B2dBoxBaseInformation;
import com.yuil.game.net.message.Message;
import com.yuil.game.util.DataUtil;

public class S2C_B2D_GET_GAMEOBJECT extends Message {
	public B2dBoxBaseInformation b2dBoxBaseInformation=new B2dBoxBaseInformation();

	
	
	public S2C_B2D_GET_GAMEOBJECT() {
		this.type=GameMessageType.S2C_B2D_GET_GAMEOBJECT.ordinal();
	}
	public S2C_B2D_GET_GAMEOBJECT(byte[] src) {
		this.type=GameMessageType.S2C_B2D_GET_GAMEOBJECT.ordinal();
		this.init(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+4];
		byte[] src=DataUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, 4);
		src=b2dBoxBaseInformation.toBytes();
		System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);
		return dest;
	}

	@Override
	public void init(byte[] src) {
		// TODO Auto-generated method stub
		this.b2dBoxBaseInformation.initFromBytes(src);

	}
	public static byte[] getBytes(B2DGameObject obj){
		
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+Message.TYPE_LENGTH];
		byte[] src=DataUtil.intToBytes(GameMessageType.S2C_B2D_GET_GAMEOBJECT.ordinal());
		System.arraycopy(src, 0, dest, offset, Message.TYPE_LENGTH);offset+=Message.TYPE_LENGTH;
		src=B2dBoxBaseInformation.getBytesFromB2dGameObject(obj);
		System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);
		return dest;
	}
	
	                                                  
}
