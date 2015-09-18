package com.mygdx.game.entity.message;

public class GameMessageType {
	public static final int length=2;
	
	public static final short c2s_b2d_apply_force=1;
	public static final short c2s_b2d_add_gameobject=2;
	public static final short c2s_b2d_remove_gameobject=3;
	public static final short c2s_b2d_get_gameobject=4;
	public static final short c2s_b2d_get_all_gameobject=5;
	
	public static final short s2c_b2d_apply_force=6;
	public static final short s2c_b2d_add_gameobject=7;
	public static final short s2c_b2d_remove_gameobject=8;
	public static final short s2c_b2d_get_gameobject=9;
	public static final short s2c_b2d_get_all_gameobject=10;
}
