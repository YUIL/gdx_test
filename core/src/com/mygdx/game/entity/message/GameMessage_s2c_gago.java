package com.mygdx.game.entity.message;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entity.message.information.B2dBoxBaseInformation;
import com.mygdx.game.util.JavaDataConverter;

public class GameMessage_s2c_gago extends GameMessage {
	public Array<B2dBoxBaseInformation> b2dBoxBaseInformationArray=new Array<B2dBoxBaseInformation>();
	
	public GameMessage_s2c_gago() {
		this.type=GameMessageType.s2c_gago;
	}
	public void initB2dBoxBaseInformationArrayFromBytes(byte[] src){
		for (int i = 0; i < (src.length)/B2dBoxBaseInformation.informationLength; i++) {
			byte[] temp=JavaDataConverter.subByte(src, B2dBoxBaseInformation.informationLength, i*B2dBoxBaseInformation.informationLength);
			b2dBoxBaseInformationArray.add(new B2dBoxBaseInformation(temp));
		}
	}
	
	public byte[] toBytes(){
		int offset=0;
		byte[] dest=new byte[B2dBoxBaseInformation.informationLength*b2dBoxBaseInformationArray.size+4];
		byte[] src=JavaDataConverter.intToBytes(GameMessageType.s2c_gago);
		System.arraycopy(src, 0, dest, offset, 4);offset+=4;
		
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
}
