package mrpan.android.loveproject.sdk;

import org.json.JSONObject;

import android.content.Context;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class BaseUiListener implements IUiListener {
	Context mContext;
	String mScope;
	public static JSONObject obj;

	public BaseUiListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public BaseUiListener(Context mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
	}

	protected void doComplete(JSONObject jsonObj) {
		//Log.d("JSON", jsonObj.toString());
		jsonObj=obj;
		// try {
		// // JSONObject jsonObj = new JSONObject(values.toString());
		// OPENID = jsonObj.getString("openid");
		// PFKEY = jsonObj.getString("pfkey");
		// ACCESS_TOKEN = jsonObj.getString("access_token");
		// // System.out.println("222222openid"+OPENID);
		// mTencent.setAccessToken(ACCESS_TOKEN, null);
		// mTencent.setOpenId(OPENID);
		//
		// } catch (JSONException e) {
		// System.out.println("Json parse error");
		// e.printStackTrace();
		// }
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
