package com.mygdx.game.entity.message;

import com.mygdx.game.util.ByteUtil;

public class GameMessageConverter {
	int type=0;
	/*public void getGameMessageFromBytes(byte[] src,GameMessage message){
		int offset=0;
		type=JavaDataConverter.bytesToInt(JavaDataConverter.subByte(src, 4, offset));offset+=4;
		message.type=type;
		switch (type) {
		case GameMessageType.c2s_rpc:
			GameMessage_c2s_rpc gameMessage_c2s_rpc=(GameMessage_c2s_rpc)message;
			gameMessage_c2s_rpc.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, offset));offset+=8;
			gameMessage_c2s_rpc.forceX=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));offset+=4;
			gameMessage_c2s_rpc.forceY=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(src, 4, offset));

			break;
		case GameMessageType.c2s_ago:
			GameMessage_c2s_ago gameMessage_c2s_ago=(GameMessage_c2s_ago)message;
			gameMessage_c2s_ago.b2dBoxBaseInformation.initFromBytes(src,offset);
			break;
		case GameMessageType.c2s_ggo:
			GameMessage_c2s_ggo gameMessage_c2s_ggo=(GameMessage_c2s_ggo)message;
			gameMessage_c2s_ggo.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, offset));offset+=8;
			break;
		case GameMessageType.c2s_rgo:
			GameMessage_c2s_rgo gameMessage_c2s_rgo=(GameMessage_c2s_rgo)message;
			gameMessage_c2s_rgo.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, offset));offset+=8;
			break;
		case GameMessageType.c2s_gago:
			GameMessage_c2s_gago gameMessage_c2s_gago=(GameMessage_c2s_gago)message;
			break;
			
			
		
		case GameMessageType.s2c_ago:
			GameMessage_s2c_ago gameMessage_s2c_ago=(GameMessage_s2c_ago)message;
			gameMessage_s2c_ago.b2dBoxBaseInformation.initFromBytes(src, offset);
			break;
		case GameMessageType.s2c_ggo:
			GameMessage_s2c_ggo gameMessage_s2c_ggo=(GameMessage_s2c_ggo)message;
			gameMessage_s2c_ggo.b2dBoxBaseInformation.initFromBytes(src, offset);
			break;
		case GameMessageType.s2c_rgo:
			GameMessage_s2c_rgo gameMessage_s2c_rgo=(GameMessage_s2c_rgo)message;
			gameMessage_s2c_rgo.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(src, 8, offset));offset+=8;
			break;
		case GameMessageType.s2c_gago:
			GameMessage_s2c_gago gameMessage_s2c_gago=(GameMessage_s2c_gago)message;
			gameMessage_s2c_gago.initB2dBoxBaseInformationArrayFromBytes(src, offset);
			break;
		default:
			break;
		}
	}*/
	
	/*public static byte[] getBytesFromGameMessage(GameMessage message){
		switch (message.type) {
		case GameMessageType.:
			
			break;

		default:
			break;
		}
		
		return null;
	}*/
}
