package mrpan.android.loveproject.DB;

import java.util.ArrayList;

import mrpan.android.loveproject.bean.Dialog;
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
	
	public long InsertDialog(Dialog dialog){
		ContentValues values = new ContentValues();

		values.put("dia_title", dialog.getTitle());
		values.put("dia_author", dialog.getAuthor());
		values.put("dia_islock", dialog.isIsLock() == true ? 1 : 0);
		values.put("dia_content", dialog.getContent());
		values.put("dia_img", dialog.getImage());
		values.put("dia_date", dialog.getDate());
		values.put("dia_user", dialog.getUser());

		return mDb.insert("Dialog", null, values);
	}
	
	public long UpdateDialog(Dialog dialog,String ID){
		ContentValues values = new ContentValues();

		values.put("dia_title", dialog.getTitle());
		values.put("dia_author", dialog.getAuthor());
		values.put("dia_islock", dialog.isIsLock() == true ? 1 : 0);
		values.put("dia_content", dialog.getContent());
		values.put("dia_img", dialog.getImage());
		values.put("dia_date", dialog.getDate());
		values.put("dia_user", dialog.getUser());

		return mDb.update("Dialog", values, "dia_id='" + ID + "'", null);
	}
	
	public long deleteDialog(String ID) {
		return mDb.delete("Dialog", "dia_id='" + ID + "'", null);
	}
	
	private Cursor DialogCursorByUser(String User,int Index){
		return mDb.query("Dialog", null, "dia_user='"+User+"'", null, null, null, "dia_id limit 10 offset 10*"+(Index-1));
	}
	
	private Cursor DialogCursorAll(int Index){
		return mDb.query("Dialog", null, null, null, null, null, "dia_id limit 10 offset 10*"+(Index-1));
	}
	
	public ArrayList<Dialog> getDialog(String User,int Index){
		ArrayList<Dialog> dialogs=new ArrayList<Dialog>();
		Cursor cs;
		if(User.trim().equals(""))
		{
			cs=DialogCursorAll(Index);
		}
		else{
			cs=DialogCursorByUser(User,Index);
		}
		if(cs!=null){
			while(cs.moveToNext()){
				Dialog d=new Dialog();
				d.setID(cs.getInt(cs.getColumnIndex("dia_id")));
				d.setTitle(cs.getString(cs.getColumnIndex("dia_title")));
				d.setAuthor(cs.getString(cs.getColumnIndex("dia_author")));
				d.setContent(cs.getString(cs.getColumnIndex("dia_content")));
				d.setImage(cs.getBlob(cs.getColumnIndex("dia_img")));
				d.setIsLock(cs.getInt(cs.getColumnIndex("dia_islock"))==1?true:false);
				d.setDate(cs.getString(cs.getColumnIndex("dia_date")));
				d.setUser(cs.getString(cs.getColumnIndex("dia_user")));
				dialogs.add(d);
			}
		}
		
		return dialogs;
	}

}
