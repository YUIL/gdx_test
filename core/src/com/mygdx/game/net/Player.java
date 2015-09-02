package com.mygdx.game.net;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by i008 on 2015/8/28.
 */
public class Player {
    String name;
    Rectangle rectangle;
    boolean move=false;
    public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public Player(String name){
        this.name=name;
        this.rectangle=new Rectangle();
    }

    public String getName() {
        return name;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
