package mrpan.android.loveproject;

import mrpan.android.loveproject.DB.DataBaseAdapter;
import mrpan.android.loveproject.bean.Dialog;
import mrpan.android.loveproject.sdk.QQShareDemo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogActivity extends Activity {

	private Context mContext;
	private DataBaseAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_main);
		db=new DataBaseAdapter(this);
		Button next_btn = (Button) findViewById(R.id.share_btn);
		Bundle data = this.getIntent().getExtras();
		String ID= data.getString("id");
		Dialog dialog=db.getDialogByID(ID);
		System.out.println("ID:"+ID);
		initData(dialog);
		mContext = this;
		next_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				QQShareDemo qq=new QQShareDemo(DialogActivity.this,mContext);
				qq.login();
			}

		});
		Button last_btn = (Button) findViewById(R.id.back_btn);
		last_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}

		});
	}
	
	void initData(Dialog dialog){
		ImageView dialog_img=(ImageView)findViewById(R.id.dialog_img);
		TextView dialog_title=(TextView)findViewById(R.id.dialog_title);
		TextView dialog_author=(TextView)findViewById(R.id.dialog_author);
		TextView dialog_date_day=(TextView)findViewById(R.id.dialog_date_day);
		TextView dialog_date_year_month=(TextView)findViewById(R.id.dialog_date_year_month);
		TextView chatto_content=(TextView)findViewById(R.id.chatto_content);
		
		if(dialog!=null)
		{
			System.out.println("2222222222222222222222222");
			dialog_title.setText(dialog.getTitle());
			dialog_author.setText(dialog.getAuthor());
			String[] Date=dialog.getDate().split("-");
			String day=Date[1];
			String year_month=Date[0];
			dialog_date_day.setText(day);
			dialog_date_year_month.setText(year_month);
			chatto_content.setText(dialog.getContent());	
			byte[] photo=dialog.getImage();
			if (null != photo && photo.length > 0) {
				Bitmap bitmap = ImageTools.byteToBitmap2(photo);
				dialog_img.setImageBitmap(bitmap);
			}
			else
			{
				dialog_img.setImageResource(R.drawable.back);
			}
		}
	}
	


}
