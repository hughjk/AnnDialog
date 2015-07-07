package mrpan.android.loveproject;

import mrpan.android.loveproject.DB.DataBaseAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Context mContext;
	SharedPreferences preferences;
	private RelativeLayout rl_user;
	private Button mLogin,mZhuce;
	private TextView tvForgetpwd;
	EditText password, user, Password;
	String pwd, pwd2;
	
	private ImageView login_picture;

	private DataBaseAdapter db = null;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		db=new DataBaseAdapter(this);
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		login_picture=(ImageView)findViewById(R.id.login_picture);
		mZhuce=(Button)findViewById(R.id.zhuce);
		password = (EditText) findViewById(R.id.user_password);
		tvForgetpwd = (TextView) findViewById(R.id.forgetpwd);
		user = (EditText) findViewById(R.id.user_name);
		user.addTextChangedListener(new TextWatcher(){
		
			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.length() != 0) {
					String name=user.getText().toString().trim();
					// mHandler.sendEmptyMessage(CHANGE_INPUT);
					if(db.IsHaveUser(name)){
						byte[] photo=db.getUser(name).getPhoto();
						
						if (null != photo && photo.length > 0) {
							Bitmap bitmap = ImageTools.byteToBitmap(photo);
							login_picture.setImageBitmap(bitmap);
						}
						else
						{
							login_picture.setImageResource(R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
						}
					}
					else
					{
						login_picture.setImageResource(R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
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
				
			}});
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
						Intent intent=new Intent();
						intent.setClass(mContext, RegisterActivity.class);
						startActivity(intent);
			   //finish();	
			}
		});
		mLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name=user.getText().toString().trim();
				String pwd=password.getText().toString().trim();
				if(db.Login(name, pwd))
					{
					//System.out.println("11111111");
						MyApplication myapp = (MyApplication) getApplication();
						myapp.setLog(true);
						myapp.setName(name);
						Intent intent=new Intent();
						intent.setClass(mContext, MainActivity.class);
						startActivity(intent);
						finish();
						System.exit(0);
					}
			//	System.out.println("22222222");
					
				
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
