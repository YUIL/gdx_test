package com.mygdx.game.entity.message;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.util.ByteUtil;

public class GameMessage_s2c_gago extends GameMessage {
	public Array<B2dBoxBaseInformation> b2dBoxBaseInformationArray=new Array<B2dBoxBaseInformation>();
	
	public GameMessage_s2c_gago() {
		this.type=GameMessageType.s2c_b2d_get_all_gameobject.ordinal();
	}
	public GameMessage_s2c_gago(byte[] src) {
		this.type=GameMessageType.s2c_b2d_get_all_gameobject.ordinal();
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
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength*b2dBoxBaseInformationArray.size+GameMessage.typeLength];
		byte[] src=ByteUtil.intToBytes(this.type);
		System.arraycopy(src, 0, dest, offset, GameMessage.typeLength);offset+=GameMessage.typeLength;
		
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
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength*array.size+GameMessage.typeLength];
		byte[] src=ByteUtil.intToBytes(GameMessageType.s2c_b2d_get_all_gameobject.ordinal());
		System.arraycopy(src, 0, dest, offset, GameMessage.typeLength);offset+=GameMessage.typeLength;
		
		for (int i = 0; i < array.size; i++) {
			src=B2dBoxBaseInformation.getBytesFromB2dGameObject(array.get(i));
			System.arraycopy(src, 0, dest, offset, B2dBoxBaseInformation.informationLength);offset+= B2dBoxBaseInformation.informationLength;
		}
		return dest;
	}
}
