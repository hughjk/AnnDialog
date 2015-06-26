package mrpan.android.loveproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Context mContext;
	SharedPreferences preferences;
	private RelativeLayout rl_user;
	private Button mLogin;
	private TextView tvForgetpwd;
	EditText password, user, Password;
	String pwd, pwd2;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		password = (EditText) findViewById(R.id.user_password);
		tvForgetpwd = (TextView) findViewById(R.id.forgetpwd);
		user = (EditText) findViewById(R.id.user_name);
		preferences = getSharedPreferences("ganbuname", MODE_WORLD_READABLE);
		String name = preferences.getString("ganbuname", null);
		pwd2 = preferences.getString("ganbupassword", null);
		user.setText(name);
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);
		SpannableString content = new SpannableString("忘记密码？");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		tvForgetpwd.setText(content);
		mLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(user.getText().equals("123")&&password.getText().equals("123"))
					{
						Intent intent=new Intent();
						intent.setClass(mContext, MainActivity.class);
						startActivity(intent);
					}
					
				
			}
		});
		tvForgetpwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "忘记密码啦？联系相关人员解决吧~(=.=)", Toast.LENGTH_LONG)
						.show();
			}
		});

	}
}
