package com.yuil.game.entity;

import com.badlogic.gdx.utils.Array;
import com.yuil.game.entity.message.C2S_B2D_ADD_GAMEOBJECT;
import com.yuil.game.entity.message.C2S_B2D_APPLY_FORCE;
import com.yuil.game.entity.message.S2C_B2D_GET_GAMEOBJECT;
import com.yuil.game.net.message.GAME_MESSAGE;
import com.yuil.game.server.NetTest7LogicServer;

public class NetTest7GameWorld {
	Array<NetTest7GameObject> gameObjectArray=new Array<NetTest7GameObject>();
	GameWorldB2d gameWorld=new GameWorldB2d();
	NetTest7LogicServer logicServer;
	
	public NetTest7GameWorld(NetTest7LogicServer logicServer){
		this.logicServer=logicServer;
	}
	
	public void update(float delta){
		
	}
	
	public boolean deathLineDetect(){
		
		return false;
	}
	
	
	public boolean addGameObject(C2S_B2D_ADD_GAMEOBJECT gameMessage){
		long id = gameMessage.b2dBoxBaseInformation.gameObjectId;
		B2dGameObject gameObject = gameWorld.findGameObject(id);
		if (gameObject != null) {
			return false;
		}else{
			//System.out.println("create Box, id:"+gameMessage.b2dBoxBaseInformation.gameObjectId);
			gameWorld.getGameObjectCreationQueue().add(gameMessage.b2dBoxBaseInformation);
			return true;
			/*GameMessage_s2c_ago gameMessage_s2c_ago=new GameMessage_s2c_ago();
			gameMessage_s2c_ago.b2dBoxBaseInformation=gameMessage.b2dBoxBaseInformation;
			udpServer.send(gameMessage_s2c_ago.toBytes(), session);*/
		}
	}
	
	public boolean removeGameObject(){
		return false;
	}
	
	public boolean applyForce(C2S_B2D_APPLY_FORCE gameMessage){
		long id = gameMessage.gameObjectId;
		B2dGameObject gameObject = gameWorld.findGameObject(id);
		if (gameObject != null) {
			float forceX = gameMessage.forceX;
			float forceY = gameMessage.forceY;
			if (gameObject.getSpeed() < gameObject.getMaxSpeed()) {
				if (forceX == 0) {
					if (gameObject.getBody().getLinearVelocity().y < 1
							&& gameObject.getBody().getLinearVelocity().y > -1) {
						gameObject.applyForce(forceX, forceY);
						// boardCast(recvString);
						return true;
					}
				} else {
					if (forceX > 0 && gameObject.getBody().getLinearVelocity().x < 10
							|| forceX < 0 && gameObject.getBody().getLinearVelocity().x > -10) {
						gameObject.applyForce(forceX, forceY);
						// boardCast(recvString);
						return true;
					}
				}

			}
		}
		return false;
	}
}
