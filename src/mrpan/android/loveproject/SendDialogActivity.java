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
	private static final int SCALE = 5;// 照片缩小比例
	
	String filepath = Environment.getExternalStorageDirectory()
			+ "/LoveProject/image";
	
	String[] items = new String[] { "拍照", "选择本地图片" };
	
	private int mLocationPosition;// 当前选择的地理位置在列表的位置
	private boolean mLocationIsShowing = true;// 当前是否显示地理位置
	private boolean mPictureIsExist = false;// 是否上传图片
	
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
							.setTitle("Love")
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
					if(publishDialog())
					{
						Toast.makeText(mContext, "上传记录成功",
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
				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();

				// 将处理过的图片显示在界面上，并保存到本地
				photo.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, filepath,
						String.valueOf(System.currentTimeMillis()));

				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						// 释放原始图片占用的内存，防止out of memory异常发生
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
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			// 类型码
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
						// 删除上一次截图的临时文件
						SharedPreferences sharedPreferences = getSharedPreferences(
								"temp", Context.MODE_WORLD_WRITEABLE);
						ImageTools.deletePhotoAtPathAndName(filepath,
								sharedPreferences.getString("tempName", ""));

						// 保存本次截图临时文件名字
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
					// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
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
