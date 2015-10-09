package com.mygdx.game.android;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
//import com.tencent.tauth.Tencent;

public class AndroidLauncher extends AndroidApplication {
	MyGdxGame game;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i=new  Intent("ACTION_LOGIN");
		//i.setClass(this, LoginActivity.class);
		startActivityForResult(i, 1);
		//setContentView(R.layout.activity_main);
		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
		// 其中APP_ID是分配给第三方应用的appid，类型为String。
		//mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
		// 初始化视图
		//initViews();
		startGame();
		
	}
	
	void startGame(){
		System.out.println("start game");
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game=new MyGdxGame();
		initialize(game, config);
	}
	public void login(String openId){
		game.openId=openId;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.i("yuil","OnActivittyResult");
		if(resultCode==RESULT_OK){
			Log.i("yuil", data.getStringExtra("openId"));
			login(data.getStringExtra("openId"));
		}
			
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
