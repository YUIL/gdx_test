package com.mygdx.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
//import com.tencent.tauth.Tencent;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i=new  Intent();
		i.setClass(this, LoginActivity.class);
		startActivity(i);
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
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(), config);
	}
/*	public void login()
	{
	mTencent = Tencent.createInstance(AppId, this.getApplicationContext());
	if (!mTencent.isSessionValid())
	{
	mTencent.login(this, Scope, listener);
	}
	}*/
}
