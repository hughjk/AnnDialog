package mrpan.android.loveproject;

import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DialogActivity extends Activity {

	Tencent mTencent;
	private final static String APP_ID = "1104753484";
	private final static String SCOPE = "all";
	BaseUiListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_main);
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
	}
	
	public void login()
	{
		//mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		if (!mTencent.isSessionValid())
		{
			mTencent.login(this, SCOPE, listener);
	}
	}

	class BaseUiListener implements IUiListener {
		protected void doComplete(JSONObject values) {
		}

		@Override
		public void onError(UiError e) {
			// showResult("onError:", "code:" + e.errorCode + ", msg:"
			// + e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			// showResult("onCancel", "");
		}

		@Override
		public void onComplete(Object arg0) {
			// TODO Auto-generated method stub
			doComplete((JSONObject) arg0);
			System.out.println(arg0.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}
}
