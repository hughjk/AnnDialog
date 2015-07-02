package mrpan.android.loveproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import mrpan.android.loveproject.DB.DataBaseAdapter;
import mrpan.android.loveproject.DB.DatabaseHelper;
import mrpan.android.loveproject.bean.Dialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SendDialogActivity extends Activity {

	private Context mContext;
	
	private DataBaseAdapter db = null;
	//DatabaseHelper d;
	private LinearLayout mParent;
	private Button mCancel;
	private Button mSubmit;
	private EditText mContent,title,author;
	private Button mLocation;
	private ImageButton mFaceButton;
	private ImageButton mLocationButton;
	private ImageView photo;
	
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	private static final int SCALE = 5;// ��Ƭ��С����
	
	String filepath = Environment.getExternalStorageDirectory()
			+ "/LoveProject/image";
	
	String[] items = new String[] { "����", "ѡ�񱾵�ͼƬ" };
	
	private int mLocationPosition;// ��ǰѡ��ĵ���λ�����б��λ��
	private boolean mLocationIsShowing = true;// ��ǰ�Ƿ���ʾ����λ��
	private boolean mPictureIsExist = false;// �Ƿ��ϴ�ͼƬ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_send);
		mContext = this;
		//d=DatabaseHelper.getDataBase(this);
		//SQLiteDatabase dbb=d.getReadableDatabase();
		
		db=new DataBaseAdapter(this);
		findViewById();
		setListener();
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
							.setTitle("Love")
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
					if(publishDialog())
					{
						Toast.makeText(mContext, "�ϴ���¼�ɹ�",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		mFaceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showPicturePicker(mContext, false);
			}
		});
		
	}
	
	private boolean publishDialog(){
		String Title=title.getText().toString().trim();
		String Author=author.getText().toString().trim();
		String Content=mContent.getText().toString().trim();
		Bitmap bitmap=ImageTools.getBitmap(photo);
		byte[] Photo=ImageTools.bitmapToBytes(bitmap);
		Dialog d=new Dialog();
		d.setTitle(Title);
		d.setAuthor(Author);
		d.setContent(Content);
		d.setImage(Photo);
		long result=db.InsertDialog(d);
		if(result==-1)
			return false;
		return true;
	}
	
	private void findViewById() {
		mParent = (LinearLayout) findViewById(R.id.writerecord_parent);
		mCancel = (Button) findViewById(R.id.writerecord_cannel);
		mSubmit = (Button) findViewById(R.id.writerecord_submit);
		mContent = (EditText) findViewById(R.id.writerecord_content);
		photo=(ImageView)findViewById(R.id.dialog_img);
		mFaceButton = (ImageButton) findViewById(R.id.writerecord_photo_button);
		mLocationButton = (ImageButton) findViewById(R.id.writerecord_location_button);
		title=(EditText)findViewById(R.id.dialog_title);
		author=(EditText)findViewById(R.id.dialog_author);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				// �������ڱ��ص�ͼƬȡ������С����ʾ�ڽ�����
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				// ����Bitmap�ڴ�ռ�ýϴ�������Ҫ�����ڴ棬����ᱨout of memory�쳣
				bitmap.recycle();

				// ���������ͼƬ��ʾ�ڽ����ϣ������浽����
				photo.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, filepath,
						String.valueOf(System.currentTimeMillis()));

				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				// ��Ƭ��ԭʼ��Դ��ַ
				Uri originalUri = data.getData();
				try {
					// ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						// Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						// �ͷ�ԭʼͼƬռ�õ��ڴ棬��ֹout of memory�쳣����
						photo.recycle();

						this.photo.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case CROP_PICTURE:
				Bitmap photo = null;
				Uri photoUri = data.getData();
				if (photoUri != null) {
					photo = BitmapFactory.decodeFile(photoUri.getPath());
				}
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photo = (Bitmap) extra.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					}
				}
				this.photo.setImageBitmap(photo);
				break;
			default:
				break;
			}
		}

	}

	public void showPicturePicker(Context context, boolean isCrop) {
		final boolean crop = isCrop;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ͼƬ��Դ");
		builder.setNegativeButton("ȡ��", null);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			// ������
			int REQUEST_CODE;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TAKE_PICTURE:
					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					if (crop) {
						REQUEST_CODE = CROP;
						// ɾ����һ�ν�ͼ����ʱ�ļ�
						SharedPreferences sharedPreferences = getSharedPreferences(
								"temp", Context.MODE_WORLD_WRITEABLE);
						ImageTools.deletePhotoAtPathAndName(filepath,
								sharedPreferences.getString("tempName", ""));

						// ���汾�ν�ͼ��ʱ�ļ�����
						fileName = String.valueOf(System.currentTimeMillis())
								+ ".jpg";
						Editor editor = sharedPreferences.edit();
						editor.putString("tempName", fileName);
						editor.commit();
					} else {
						REQUEST_CODE = TAKE_PICTURE;
						fileName = "image.jpg";
					}
					imageUri = Uri.fromFile(new File(filepath, fileName));
					// ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
					openCameraIntent
							.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;

				case CHOOSE_PICTURE:
					Intent openAlbumIntent = new Intent(
							Intent.ACTION_GET_CONTENT);
					if (crop) {
						REQUEST_CODE = CROP;
					} else {
						REQUEST_CODE = CHOOSE_PICTURE;
					}
					openAlbumIntent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(openAlbumIntent, REQUEST_CODE);
					break;

				default:
					break;
				}
			}
		});
		builder.create().show();
	}
	
}
