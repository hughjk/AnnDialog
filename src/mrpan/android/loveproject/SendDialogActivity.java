package mrpan.android.loveproject;

import mrpan.android.loveproject.DB.DataBaseAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SendDialogActivity extends Activity {

	private Context mContext;
	
	private DataBaseAdapter db = null;
	
	private LinearLayout mParent;
	private Button mCancel;
	private Button mSubmit;
	private EditText mContent;
	private Button mLocation;
	private ImageButton mFaceButton;
	private ImageButton mLocationButton;
	
	private int mLocationPosition;// 当前选择的地理位置在列表的位置
	private boolean mLocationIsShowing = true;// 当前是否显示地理位置
	private boolean mPictureIsExist = false;// 是否上传图片
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_send);
		mContext = this;
		db=new DataBaseAdapter(this);
		findViewById();
		setListener();
		init();
	}
	
	private void setListener() {
		mCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// 关闭当前界面
				finish();
			}
		});
		mSubmit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 获取当前输入框内容
				String content = mContent.getText().toString().trim();
				// 内容为空时显示提示对话框,不为空则返回更新信息
				if (TextUtils.isEmpty(content)) {
					// 显示提示对话框
					new AlertDialog.Builder(mContext)
							.setTitle("开心网")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setMessage("记录信息不能为空")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).create().show();
				} else {
					// 显示提示信息并关闭当前界面
					Toast.makeText(mContext, "上传记录成功",
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		mLocation.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 地理位置对话框
				locationDialog();
			}
		});
		mFaceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 显示表情对话框
				// showFace(mParent);
				Toast.makeText(mContext, "显示表情对话框",
						Toast.LENGTH_SHORT).show();
			}
		});
		// mFaceGridView.setOnItemClickListener(new OnItemClickListener() {
		//
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // 获取当前光标所在位置
		// int currentPosition = mContent.getSelectionStart();
		// // 添加含有表情的文本
		// mContent.setText(new TextUtil(mKXApplication).replace(mContent
		// .getText().insert(currentPosition,
		// mKXApplication.mFacesText.get(position))));
		// // 关闭表情对话框
		// dismissFace();
		// Toast.makeText(WriteRecordActivity.this, "添加含有表情的文本",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		// mFaceClose.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// // 关闭表情对话框
		// dismissFace();
		// }
		// });
//		mPhotoButton.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// 判断是否已经添加图片,如果添加则提示消息否则则显示图片对话框
//				if (mPictureIsExist) {
//					Toast.makeText(mContext, "最多只能添加一张图片",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					// 图片对话框
//					//PhotoDialog();
//				}
//			}
//		});
		mLocationButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 根据地理位置显示状态,显示或隐藏,并修改图标
				if (mLocationIsShowing) {
					mLocationIsShowing = false;
					mLocation.setVisibility(View.GONE);
					mLocationButton
							.setImageResource(R.drawable.write_function_location_button);
				} else {
					mLocationIsShowing = true;
					mLocation.setVisibility(View.VISIBLE);
					mLocationButton
							.setImageResource(R.drawable.write_function_locationremove_button);
				}
			}
		});
	}

	private void init() {
		// 获取地理位置数据
		getLocation();
		// 初始化显示数据
		// mLocation.setText(mKXApplication.mMyLocationResults.get(0).getName());
		// mCompetence.setText(mCompetenceItems[mCompetencePosition]);
		Toast.makeText(mContext, "初始化显示地理位置数据",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取地理位置
	 */
	private void getLocation() {
		Toast.makeText(this, "获取地理位置", Toast.LENGTH_SHORT).show();
		// if (mKXApplication.mMyLocationResults.isEmpty()) {
		// InputStream inputStream;
		// try {
		// inputStream = getAssets().open("data/my_location.KX");
		// String json = new TextUtil(mKXApplication)
		// .readTextFile(inputStream);
		// JSONArray array = new JSONArray(json);
		// LocationResult result = null;
		// for (int i = 0; i < array.length(); i++) {
		// result = new LocationResult();
		// result.setName(array.getJSONObject(i).getString("name"));
		// result.setLocation(array.getJSONObject(i).getString(
		// "location"));
		// mKXApplication.mMyLocationResults.add(result);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
	}

	/**
	 * 地理位置对话框
	 */
	private void locationDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("选择当前位置");
		// builder.setAdapter(new LocationAdapter(),
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int which) {
		// // mLocationPosition = which;
		// // mLocation.setText(mKXApplication.mMyLocationResults
		// // .get(which).getName());
		// // dialog.dismiss();
		// Toast.makeText(WriteRecordActivity.this, "选择当前位置",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		builder.setPositiveButton("刷新", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();
	}

	// private class LocationAdapter extends BaseAdapter {
	//
	// public int getCount() {
	// return mKXApplication.mMyLocationResults.size();
	// }
	//
	// public Object getItem(int position) {
	// return mKXApplication.mMyLocationResults.get(position);
	// }
	//
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder holder = null;
	// if (convertView == null) {
	// convertView = LayoutInflater.from(WriteRecordActivity.this)
	// .inflate(R.layout.writerecord_activity_location_item,
	// null);
	// holder = new ViewHolder();
	// holder.icon = (ImageView) convertView
	// .findViewById(R.id.writerecord_activity_location_item_icon);
	// holder.name = (TextView) convertView
	// .findViewById(R.id.writerecord_activity_location_item_name);
	// holder.location = (TextView) convertView
	// .findViewById(R.id.writerecord_activity_location_item_location);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// LocationResult result = mKXApplication.mMyLocationResults
	// .get(position);
	// if (mLocationPosition == position) {
	// holder.icon.setVisibility(View.VISIBLE);
	// } else {
	// holder.icon.setVisibility(View.INVISIBLE);
	// }
	// holder.name.setText(result.getName());
	// holder.location.setText(result.getLocation());
	// return convertView;
	// }
	//
	// class ViewHolder {
	// ImageView icon;
	// TextView name;
	// TextView location;
	// }
	// }


	private void findViewById() {
		mParent = (LinearLayout) findViewById(R.id.writerecord_parent);
		mCancel = (Button) findViewById(R.id.writerecord_cannel);
		mSubmit = (Button) findViewById(R.id.writerecord_submit);
		mContent = (EditText) findViewById(R.id.writerecord_content);
		mLocation = (Button) findViewById(R.id.writerecord_location);
		//mPicture = (Button) findViewById(R.id.writerecord_picture);
		mFaceButton = (ImageButton) findViewById(R.id.writerecord_photo_button);
		mLocationButton = (ImageButton) findViewById(R.id.writerecord_location_button);
	}
}
