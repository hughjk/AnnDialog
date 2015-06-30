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
	
	private int mLocationPosition;// ��ǰѡ��ĵ���λ�����б��λ��
	private boolean mLocationIsShowing = true;// ��ǰ�Ƿ���ʾ����λ��
	private boolean mPictureIsExist = false;// �Ƿ��ϴ�ͼƬ
	
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
				// �رյ�ǰ����
				finish();
			}
		});
		mSubmit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// ��ȡ��ǰ���������
				String content = mContent.getText().toString().trim();
				// ����Ϊ��ʱ��ʾ��ʾ�Ի���,��Ϊ���򷵻ظ�����Ϣ
				if (TextUtils.isEmpty(content)) {
					// ��ʾ��ʾ�Ի���
					new AlertDialog.Builder(mContext)
							.setTitle("������")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setMessage("��¼��Ϣ����Ϊ��")
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).create().show();
				} else {
					// ��ʾ��ʾ��Ϣ���رյ�ǰ����
					Toast.makeText(mContext, "�ϴ���¼�ɹ�",
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		mLocation.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// ����λ�öԻ���
				locationDialog();
			}
		});
		mFaceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// ��ʾ����Ի���
				// showFace(mParent);
				Toast.makeText(mContext, "��ʾ����Ի���",
						Toast.LENGTH_SHORT).show();
			}
		});
		// mFaceGridView.setOnItemClickListener(new OnItemClickListener() {
		//
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // ��ȡ��ǰ�������λ��
		// int currentPosition = mContent.getSelectionStart();
		// // ��Ӻ��б�����ı�
		// mContent.setText(new TextUtil(mKXApplication).replace(mContent
		// .getText().insert(currentPosition,
		// mKXApplication.mFacesText.get(position))));
		// // �رձ���Ի���
		// dismissFace();
		// Toast.makeText(WriteRecordActivity.this, "��Ӻ��б�����ı�",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		// mFaceClose.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// // �رձ���Ի���
		// dismissFace();
		// }
		// });
//		mPhotoButton.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// �ж��Ƿ��Ѿ����ͼƬ,����������ʾ��Ϣ��������ʾͼƬ�Ի���
//				if (mPictureIsExist) {
//					Toast.makeText(mContext, "���ֻ�����һ��ͼƬ",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					// ͼƬ�Ի���
//					//PhotoDialog();
//				}
//			}
//		});
		mLocationButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// ���ݵ���λ����ʾ״̬,��ʾ������,���޸�ͼ��
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
		// ��ȡ����λ������
		getLocation();
		// ��ʼ����ʾ����
		// mLocation.setText(mKXApplication.mMyLocationResults.get(0).getName());
		// mCompetence.setText(mCompetenceItems[mCompetencePosition]);
		Toast.makeText(mContext, "��ʼ����ʾ����λ������",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��ȡ����λ��
	 */
	private void getLocation() {
		Toast.makeText(this, "��ȡ����λ��", Toast.LENGTH_SHORT).show();
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
	 * ����λ�öԻ���
	 */
	private void locationDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("ѡ��ǰλ��");
		// builder.setAdapter(new LocationAdapter(),
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int which) {
		// // mLocationPosition = which;
		// // mLocation.setText(mKXApplication.mMyLocationResults
		// // .get(which).getName());
		// // dialog.dismiss();
		// Toast.makeText(WriteRecordActivity.this, "ѡ��ǰλ��",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		builder.setPositiveButton("ˢ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
