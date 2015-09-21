package com.mygdx.game.entity.message;

import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.net.message.Message;
import com.mygdx.game.util.ByteUtil;

public class S2C_B2D_GET_GAMEOBJECT extends Message {
	public B2dBoxBaseInformation b2dBoxBaseInformation=new B2dBoxBaseInformation();

	
	
	public S2C_B2D_GET_GAMEOBJECT() {
		this.type=GameMessageType.S2C_B2D_GET_GAMEOBJECT.ordinal();
	}
	public S2C_B2D_GET_GAMEOBJECT(byte[] src) {
		this.type=GameMessageType.S2C_B2D_GET_GAMEOBJECT.ordinal();
		this.initFromBytes(src);
	}
	@Override
	public byte[] toBytes() {
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+4];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, 4);
		src=b2dBoxBaseInformation.toBytes();
		System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);
		return dest;
	}

	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		this.b2dBoxBaseInformation.initFromBytes(src);

	}
	public static byte[] getBytesFromB2dGameObject(B2DGameObject obj){
		
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(GameMessageType.S2C_B2D_GET_GAMEOBJECT.ordinal());
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		src=B2dBoxBaseInformation.getBytesFromB2dGameObject(obj);
		System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);
		return dest;
	}
	
	                                                  
}
