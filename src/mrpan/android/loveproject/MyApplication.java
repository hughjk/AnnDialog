package mrpan.android.loveproject;

import org.json.JSONObject;

import android.app.Application;

public class MyApplication extends Application {  
    
    private boolean log;  
    private String Name;
    private JSONObject obj;

	public JSONObject getObj() {
		return obj;
	}

	public void setObj(JSONObject obj) {
		this.obj = obj;
	}

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
	    public void onCreate()
	    {
	        super.onCreate();
	        log=false;
	    	Name="";
	    }
}  
