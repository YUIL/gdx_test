package com.mygdx.game.entity.message;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.net.message.Message;
import com.mygdx.game.util.ByteUtil;

public class S2C_B2D_GET_ALL_GAMEOBJECT extends Message {
	public Array<B2dBoxBaseInformation> b2dBoxBaseInformationArray=new Array<B2dBoxBaseInformation>();
	
	public S2C_B2D_GET_ALL_GAMEOBJECT() {
		this.type=GameMessageType.S2C_B2D_GET_ALL_GAMEOBJECT.ordinal();
	}
	public S2C_B2D_GET_ALL_GAMEOBJECT(byte[] src) {
		this.type=GameMessageType.S2C_B2D_GET_ALL_GAMEOBJECT.ordinal();
		this.initFromBytes(src);
	}
	
	public void initB2dBoxBaseInformationArrayFromBytes(byte[] src){
		for (int i = 0; i < (src.length)/B2dBoxBaseInformation.informationLength; i++) {
			byte[] temp=ByteUtil.subByte(src, B2dBoxBaseInformation.informationLength, i*B2dBoxBaseInformation.informationLength);
			b2dBoxBaseInformationArray.add(new B2dBoxBaseInformation(temp));
		}
	}
	
	public byte[] toBytes(){
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength*b2dBoxBaseInformationArray.size+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		
		for (int i = 0; i < b2dBoxBaseInformationArray.size; i++) {
			src=b2dBoxBaseInformationArray.get(i).toBytes();
			System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);
		}
		return dest;
	}

	@Override
	public void initFromBytes(byte[] src) {
		// TODO Auto-generated method stub
		this.initB2dBoxBaseInformationArrayFromBytes(src);
	}
	
	public static byte[] getBytesFromB2dGameObjecArray(Array<B2DGameObject> array){
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength*array.size+Message.TYPE_BYTE_LENGTH];
		byte[] src=ByteUtil.intToBytes(GameMessageType.S2C_B2D_GET_ALL_GAMEOBJECT.ordinal());
		System.arraycopy(src, 0, dest, offset, Message.TYPE_BYTE_LENGTH);offset+=Message.TYPE_BYTE_LENGTH;
		
		for (int i = 0; i < array.size; i++) {
			src=B2dBoxBaseInformation.getBytesFromB2dGameObject(array.get(i));
			System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);offset+= B2dBoxBaseInformation.informationLength;
		}
		return dest;
	}
}
