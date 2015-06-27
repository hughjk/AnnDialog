package mrpan.android.loveproject.DB;

import mrpan.android.loveproject.bean.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAdapter {

	private static SQLiteDatabase mDb = null;
	private Context mContext = null;

	public DataBaseAdapter(Context context) {
		this.mContext = context;
		mDb = DatabaseHelper.getDB(context);
	}

	public long InsertUser(User user) {
		ContentValues values = new ContentValues();

		values.put("user_name", user.getName());
		values.put("user_password", user.getPassword());
		values.put("user_sex", user.isSex() == true ? 1 : 0);
		values.put("user_age", user.getAge());
		values.put("user_sign", user.getSign());
		values.put("user_info", user.getInfo());
		values.put("user_photo", user.getPhoto());
		values.put("user_level", user.getLevel());
		//values.put("time_last", user.getTime_last());

		return mDb.insert("User", null, values);
	}

	public long UpdateUser(User user, int ID) {
		ContentValues values = new ContentValues();

		values.put("user_name", user.getName());
		values.put("user_password", user.getPassword());
		values.put("user_sex", user.isSex() == true ? 1 : 0);
		values.put("user_age", user.getAge());
		values.put("user_sign", user.getSign());
		values.put("user_info", user.getInfo());
		values.put("user_photo", user.getPhoto());
		values.put("user_level", user.getLevel());
		values.put("time_last", user.getTime_last());

		return mDb.update("User", values, "where user_id='" + ID + "'", null);
	}

	public long deleteUser(String ID) {
		return mDb.delete("User", "user_id='" + ID + "'", null);
	}
	
	public User getUser(String ID)
	{
		User user=new User();
		Cursor cs=mDb.query("User", null, "where user_id='"+ID+"'", null, null, null, null);
		if(cs!=null)
		{
			while(cs.moveToNext()){
				user.setID(cs.getInt(cs.getColumnIndex("user_id")));
				user.setName(cs.getString(cs.getColumnIndex("user_name")));
				user.setSex(cs.getInt(cs.getColumnIndex("user_sex"))==1?true:false);
				user.setSign(cs.getString(cs.getColumnIndex("user_sign")));
				user.setInfo(cs.getString(cs.getColumnIndex("user_info")));
				user.setLevel(cs.getInt(cs.getColumnIndex("user_level")));
				user.setPhoto(cs.getBlob(cs.getColumnIndex("user_photo")));
				user.setTime_last(cs.getString(cs.getColumnIndex("time_last")));
			}
		}
		return user;
	}

}
