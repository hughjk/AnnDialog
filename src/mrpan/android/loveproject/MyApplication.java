package mrpan.android.loveproject;

import android.app.Application;

public class MyApplication extends Application {  
    
    private boolean log;  
    private String Name;
    
    public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	@Override  
    public void onCreate() {    
        super.onCreate();  
    }  
}  
