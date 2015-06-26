package mrpan.android.loveproject.DB;

import mrpan.android.loveproject.bean.User;
import android.content.ContentValues;
import android.content.Context;
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

}
