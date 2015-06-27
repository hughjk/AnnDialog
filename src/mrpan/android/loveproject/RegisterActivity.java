package mrpan.android.loveproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener{

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	
	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private ImageView photo;
	private TextView tvForgetpwd;
	EditText password, user, Password;
	String pwd, pwd2;
	String fileName;
	String[] items=new String[]{"����","ѡ�񱾵�ͼƬ"};
	String filepath=Environment.getExternalStorageDirectory()
			 + "/LoveProject/image";
	
	private static final int SCALE = 5;//��Ƭ��С����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mContext = this;
		findViewById();
		checkFile(filepath);
	}
	void findViewById(){
		photo=(ImageView)findViewById(R.id.zhuce_photo);photo.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.zhuce_photo:
			showPicturePicker(this,false);
			break;
		}
		
	}
	
	void checkFile(String path)
	{
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	//��ͼ
	public void startPhotoZoom(Uri uri,int width,int heigth,int requestCode) {
	                Intent intent = new Intent("com.android.camera.action.CROP");
	                intent.setDataAndType(uri, "image/*");
	                // ���òü�
	                intent.putExtra("crop", "true");
	                // aspectX aspectY �ǿ�ߵı���
	                intent.putExtra("aspectX", 1);
	                intent.putExtra("aspectY", 1);
	                // outputX outputY �ǲü�ͼƬ���
	                intent.putExtra("outputX", width);
	                intent.putExtra("outputY", heigth);
	                intent.putExtra("return-data", true);
	                startActivityForResult(intent, requestCode);
	        }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				//�������ڱ��ص�ͼƬȡ������С����ʾ�ڽ�����
				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				//����Bitmap�ڴ�ռ�ýϴ�������Ҫ�����ڴ棬����ᱨout of memory�쳣
				bitmap.recycle();
				
				//���������ͼƬ��ʾ�ڽ����ϣ������浽����
				photo.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, filepath, String.valueOf(System.currentTimeMillis()));
				
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				//��Ƭ��ԭʼ��Դ��ַ
				Uri originalUri = data.getData(); 
	            try {
	            	//ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					if (photo != null) {
						//Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
						//�ͷ�ԭʼͼƬռ�õ��ڴ棬��ֹout of memory�쳣����
						photo.recycle();
						
						this.photo.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
					System.out.println("Data");
				}else {
					System.out.println("File");
					String fileName = getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
					uri = Uri.fromFile(new File(filepath,fileName));
				}
				startPhotoZoom(uri,320,320,2);
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
		                photo = (Bitmap)extra.get("data");  
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

	public void showPicturePicker(Context context,boolean isCrop){
		final boolean crop = isCrop;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ͼƬ��Դ");
		builder.setNegativeButton("ȡ��", null);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			//������
			int REQUEST_CODE;
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TAKE_PICTURE:
					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (crop) {
						REQUEST_CODE = CROP;
						//ɾ����һ�ν�ͼ����ʱ�ļ�
						SharedPreferences sharedPreferences = getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE);
						ImageTools.deletePhotoAtPathAndName(filepath, sharedPreferences.getString("tempName", ""));
						
						//���汾�ν�ͼ��ʱ�ļ�����
						fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
						Editor editor = sharedPreferences.edit();
						editor.putString("tempName", fileName);
						editor.commit();
					}else {
						REQUEST_CODE = TAKE_PICTURE;
						fileName = "image.jpg";
					}
					imageUri = Uri.fromFile(new File(filepath,fileName));
					//ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;
					
				case CHOOSE_PICTURE:
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					if (crop) {
						REQUEST_CODE = CROP;
					}else {
						REQUEST_CODE = CHOOSE_PICTURE;
					}
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
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
