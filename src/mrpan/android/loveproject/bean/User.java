package mrpan.android.loveproject.bean;

import java.util.Date;

import android.graphics.Bitmap;

public class User {
	int ID;
	String Name;
	String Password;
	boolean Sex;
	int Age;
	String Sign;
	String Info;
	byte[] Photo;
	int Level;
	String Time_last;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public boolean isSex() {
		return Sex;
	}

	public void setSex(boolean sex) {
		Sex = sex;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public String getInfo() {
		return Info;
	}

	public void setInfo(String info) {
		Info = info;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public byte[] getPhoto() {
		return Photo;
	}

	public void setPhoto(byte[] photo) {
		Photo = photo;
	}

	public String getTime_last() {
		return Time_last;
	}

	public void setTime_last(String time_last) {
		Time_last = time_last;
	}

}
