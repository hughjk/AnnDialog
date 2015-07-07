package mrpan.android.loveproject.DB;

import java.io.File;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * @author Administrator
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSON = 1;// Ĭ�ϵ����ݿ�汾
	private static DatabaseHelper myDataBase = null;
	private static SQLiteDatabase mDb = null;
	public static final String DATABASE_NAME = "loveproject.db";
	public static final String DB_PATH =Environment.getExternalStorageDirectory()
	 + "/LoveProject/DataBases/"; // ���ֻ��������ݿ��λ��
	Context context;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSON);
		checkFile(DB_PATH);
		this.context = context;
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int verson) {
		super(context, name, factory, verson);
	}

	// �ù��캯����3����������Ϊ�������溯���ĵ�3�������̶�Ϊnull��
	public DatabaseHelper(Context context, String name, int verson) {
		this(context, name, null, verson);
	}

	// �ù��캯��ֻ��2�������������溯�� �Ļ���ɽ���汾�Ź̶���
	public DatabaseHelper(Context context, String name) {
		this(context, name, VERSON);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create a sqlite database");
		db.execSQL("CREATE TABLE IF NOT EXISTS User(user_id integer primary key autoincrement, user_name varchar(50),user_password varchar(200),user_sex INTEGER, user_age INTEGER,user_sign varchar(300),user_info varchar(500),user_photo blob,user_level integer,time_last date)");   
		db.execSQL("CREATE TABLE IF NOT EXISTS Dialog(dia_id integer primary key autoincrement,dia_title varchar(50),dia_author varchar(50),dia_content text,dia_img blob,dia_islock integer,dia_date varchar(50),dia_user varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a sqlite database");
	}

	public static synchronized DatabaseHelper getDataBase(Context context) {
		if (myDataBase == null) {
			myDataBase = new DatabaseHelper(context);
		}
		return myDataBase;
	}
	
	public static synchronized SQLiteDatabase getDB(Context context) {
		if (mDb == null) {
			mDb = SQLiteDatabase.openOrCreateDatabase(DB_PATH+DATABASE_NAME, null);
		}
		return mDb;
	}
	
	public static void checkFile(String path)
	{
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}
