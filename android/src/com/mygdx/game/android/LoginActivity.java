package com.mygdx.game.android;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {
	private static final String APP_ID = "1104673105";
	Tencent mTencent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
		// 初始化视图

		Button button1 = (Button) findViewById(R.id.new_login_btn);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickLogin();
			}
		});
	}
	public void onClickLogin() {
		// TODO Auto-generated method stub
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", loginListener);
			Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		}
	}

	IUiListener loginListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
			Intent intent=getIntent();
			setResult(RESULT_OK,intent);
			try {
				intent.putExtra("openId",values.getString("openid"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finish();
		}
	};

	private class BaseUiListener implements IUiListener {

		public void onComplete(JSONObject response) {
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			System.out.println(
					"onError:" + "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			System.out.println("onCancel" + "");
		}

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject) response);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
		/*
		 * if(requestCode == Constants.REQUEST_API) { if(resultCode ==
		 * Constants.RESULT_LOGIN) { Tencent.handleResultData(data,
		 * loginListener); } }
		 */
		super.onActivityResult(requestCode, resultCode, data);
	}
}
