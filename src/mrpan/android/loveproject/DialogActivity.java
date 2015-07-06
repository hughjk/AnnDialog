package mrpan.android.loveproject;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class DialogActivity extends Activity {

	Tencent mTencent;
	private final static String APP_ID = "1104753484";
	private final static String SCOPE = "all";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_main);
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		Button next_btn=(Button)findViewById(R.id.next_btn);
		next_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				login();
			}
			
		});
	}
	
	public void login()
	{
		//mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		if (!mTencent.isSessionValid())
		{
			mTencent.login(this, SCOPE, new BaseUiListener());
	}
	}

	class BaseUiListener implements IUiListener {
		protected void doComplete(JSONObject values) {
			Log.d("JSON", values.toString());
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
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.REQUEST_API) {
			if(resultCode == Constants.RESULT_LOGIN) {
			    mTencent.handleLoginData(data, new BaseUiListener());
		    }
		    super.onActivityResult(requestCode, resultCode, data);
		
		}
	}
}
