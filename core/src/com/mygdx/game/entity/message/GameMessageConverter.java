package com.mygdx.game.entity.message;

import com.mygdx.game.util.JavaDataConverter;

public class GameMessageConverter {
	int type=0;
	public void getMessageFromBytes(byte[] bytes,GameMessage message){
		int offset=0;
		type=JavaDataConverter.bytesToInt(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
		message.type=type;
		switch (type) {
		case GameMessageType.rpc:
			GameMessage_rpc gameMessage_rpc=(GameMessage_rpc)message;
			gameMessage_rpc.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(bytes, 8, offset));offset+=8;
			gameMessage_rpc.forceX=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_rpc.forceY=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));

			break;
		case GameMessageType.ago:
			GameMessage_c2s_ago gameMessage_c2s_ago=(GameMessage_c2s_ago)message;
			gameMessage_c2s_ago.gameObjectId=JavaDataConverter.bytesToLong(JavaDataConverter.subByte(bytes, 8, offset));offset+=8;
			gameMessage_c2s_ago.x=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.y=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.angle=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.av=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.width=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.height=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.density=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.lx=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			gameMessage_c2s_ago.ly=JavaDataConverter.bytesToFloat(JavaDataConverter.subByte(bytes, 4, offset));offset+=4;
			break;
		case GameMessageType.ggo:
			GameMessage_ggo gameMessage_ggo=(GameMessage_ggo)message;
			break;
		case GameMessageType.gago:
			break;
		default:
			break;
		}
	}
}
