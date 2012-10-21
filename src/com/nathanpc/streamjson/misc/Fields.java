package com.nathanpc.streamjson.misc;

import android.app.Activity;
import android.content.SharedPreferences;

public class Fields {
	public static final String PREFS_NAME = "StreamJSON";
	public SharedPreferences settings;
	
	public final String server_location;
	
	public Fields(Activity activity) throws Exception {
		// = "http://192.168.10.14:4881";
		this.settings = activity.getSharedPreferences(PREFS_NAME, 0);
		this.server_location = settings.getString("server_location", null);
		
		if (server_location == null) {
			throw new Exception("The server location wasn't specified");
		}
	}
}
