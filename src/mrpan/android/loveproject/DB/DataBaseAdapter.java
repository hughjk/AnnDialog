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

	public long UpdateUser(User user, int Name) {
		ContentValues values = new ContentValues();

		//values.put("user_name", user.getName());
		if(!user.getPassword().isEmpty())
			values.put("user_password", user.getPassword());
		values.put("user_sex", user.isSex() == true ? 1 : 0);
		values.put("user_age", user.getAge());
		if(!user.getSign().isEmpty())
			values.put("user_sign", user.getSign());
		if(!user.getInfo().isEmpty())
			values.put("user_info", user.getInfo());
		if(user.getPhoto().length>0)
			values.put("user_photo", user.getPhoto());
		values.put("user_level", user.getLevel());
		values.put("time_last", user.getTime_last());

		return mDb.update("User", values, "user_name='" + Name + "'", null);
	}

	public long deleteUser(String Name) {
		return mDb.delete("User", "user_name='" + Name + "'", null);
	}
	
	public boolean Login(String name,String password){
		if(IsHaveUser(name))
		{
			Cursor cs=UserCusor(name);
			if(cs!=null){
				while(cs.moveToNext()){
					String pwd=cs.getString(cs.getColumnIndex("user_password"));
					if(password.equals(pwd.trim()))
						return true;
				}
			}
		}
		return false;
	}
	
	public boolean IsHaveUser(String name){
		Cursor cs=UserCusor(name);
		if(cs.getCount()<1)
			return false;
		return true;
	}
	
	public User getUser(String Name)
	{
		User user=new User();
		Cursor cs=UserCusor(Name);
	
		if(cs!=null)
		{
			while(cs.moveToNext()){
				user.setID(cs.getInt(cs.getColumnIndex("user_id")));
				user.setName(cs.getString(cs.getColumnIndex("user_name")));
				user.setSex(cs.getInt(cs.getColumnIndex("user_sex"))==1?true:false);
				user.setSign(cs.getString(cs.getColumnIndex("user_sign")));
				user.setInfo(cs.getString(cs.getColumnIndex("user_info")));
				user.setLevel(cs.getInt(cs.getColumnIndex("user_level")));
//				byte[] photo=cs.getBlob(cs.getColumnIndex("user_photo"));
				//System.out.println("photo_size:"+photo.length);
//				if(photo.length>0)
					user.setPhoto(cs.getBlob(cs.getColumnIndex("user_photo")));
				user.setTime_last(cs.getString(cs.getColumnIndex("time_last")));
			}
		}
		return user;
	}
	
	private Cursor UserCusor(String Name)
	{
		Cursor cs=mDb.query("User", null, "user_name='"+Name+"'", null, null, null, null);
		return cs;
	}

}
