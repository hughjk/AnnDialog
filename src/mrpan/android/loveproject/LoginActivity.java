package mrpan.android.loveproject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import mrpan.android.loveproject.DB.DataBaseAdapter;
import mrpan.android.loveproject.bean.Util;
import mrpan.android.loveproject.sdk.BaseUiListener;
import mrpan.android.loveproject.sdk.QQShareDemo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Context mContext;
	SharedPreferences preferences;
	private LinearLayout rl_user;
	private Button mLogin, mZhuce;
	private TextView tvForgetpwd;
	EditText password, user, Password;
	String pwd, pwd2;
	private ImageButton qq_login;

	private MyApplication myapp;

	private Tencent mTencent;
	private UserInfo mInfo;

	private ImageView login_picture;

	private DataBaseAdapter db = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		myapp = (MyApplication) getApplication();
		db = new DataBaseAdapter(this);
		mTencent = Tencent.createInstance(QQShareDemo.APP_ID, this);
		mInfo = new UserInfo(this, mTencent.getQQToken());
		rl_user = (LinearLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		login_picture = (ImageView) findViewById(R.id.login_picture);
		qq_login = (ImageButton) findViewById(R.id.qq_login);

		// login_picture.setImageBitmap(bitmap);

		mZhuce = (Button) findViewById(R.id.zhuce);
		password = (EditText) findViewById(R.id.user_password);
		tvForgetpwd = (TextView) findViewById(R.id.forgetpwd);
		user = (EditText) findViewById(R.id.user_name);

		user.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.length() != 0) {
					String name = user.getText().toString().trim();
					// mHandler.sendEmptyMessage(CHANGE_INPUT);
					if (db.IsHaveUser(name)) {
						byte[] photo = db.getUser(name).getPhoto();

						if (null != photo && photo.length > 0) {
							Bitmap bitmap = ImageTools.byteToBitmap(photo);
							login_picture.setImageBitmap(bitmap);
						} else {
							login_picture
									.setImageResource(R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
						}
					} else {
						login_picture
								.setImageResource(R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}
		});
		qq_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				login();
				// System.out.println("11111111111111111111111111:"+BaseUiListener.obj.toString());

				// Log.d("Obejct2", BaseUiListener.obj.toString());

				// Log.v("11111111111111111111111",obj);

			}

		});
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);
		SpannableString content = new SpannableString("忘记密码？");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		tvForgetpwd.setText(content);
		mZhuce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, RegisterActivity.class);
				startActivity(intent);
				// obj="";
				// getInfo();
				// Log.v("222222222222222222222222",obj);
				// finish();
				// getInfo();
			}
		});
		mLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = user.getText().toString().trim();
				String pwd = password.getText().toString().trim();
				if (db.Login(name, pwd)) {
					// System.out.println("11111111");
					myapp.setLog(true);
					myapp.setName(name);
					Intent intent = new Intent();
					intent.setClass(mContext, MainActivity.class);
					startActivity(intent);
					finish();
					// System.exit(0);
				}
				// System.out.println("22222222");

			}
		});
		tvForgetpwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "忘记密码啦？联系相关人员解决吧~(=.=)",
						Toast.LENGTH_LONG).show();
				// t.start();
			}
		});

	}

	public void login() {
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, QQShareDemo.SCOPE,
					new BaseUiListener(mContext));
		} else {

		}
	}

	public void getInfo() {
		mInfo.getUserInfo(new BaseUiListener(mContext, "get_vip_rich_info"));
	}

	public void logout() {
		mTencent.logout(mContext);
	}

	void Close() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// try {
				// Bitmap bitmap
				// =getHttpBitmap("http://q.qlogo.cn/qqapp/1104753484/99DBA73F843D182925EDB37159A40F7D/100");
				// login_picture.setImageBitmap(bitmap);
				// qq_login.setImageBitmap(bitmap);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// Log.v("main", "error:"+e.getMessage());
				// }
				finish();
			}

		});
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.RESULT_LOGIN) {
				Tencent mTencent = Tencent.createInstance(QQShareDemo.APP_ID,
						mContext);
				mTencent.handleLoginData(data, new BaseUiListener(mContext));
				finish();
				
			}
			getInfo();
			if (resultCode == 0) {
				
				// Log.v("main",
				// "result:"+resultCode+",requestCode:"+requestCode);
				myapp.setLog(true);
				myapp.setName("qq_User");
				 System.out.println("User:"+myapp.getName());
				 Log.v("ddddddddddddd", ""+myapp.isLog());
				Intent intent = new Intent();
				intent.setClass(mContext, MainActivity.class);
				this.setResult(9);
				startActivity(intent);
				Close();
			}
			
			
		}
		Log.v("main", "result:"+resultCode+",requestCode:"+requestCode);
		super.onActivityResult(requestCode, resultCode, data);

	}
}
