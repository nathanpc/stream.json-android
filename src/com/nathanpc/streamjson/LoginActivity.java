package com.nathanpc.streamjson;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private EditText serverLocation;
	private Button btLogin;
	public static final String PREFS_NAME = "StreamJSON";
	public SharedPreferences settings;
	private Boolean isRetry;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        settings = getSharedPreferences(PREFS_NAME, 0);
		String server_loc = settings.getString("server_location", null);
        
        setupUI();
        
        if (server_loc != null) {
        	try {
            	isRetry = getIntent().getExtras().getBoolean("retry");
            	
            	if (!isRetry) {
    				showMain();
    			}
            } catch (NullPointerException e) {
            	showMain();
            }
		}
    }
	
	protected void onResume(Bundle savedInstanceState) {
		super.onResume();
		finish();
	}
	
	private void setupUI() {
		serverLocation = (EditText)findViewById(R.id.server_location);

		btLogin = (Button)findViewById(R.id.login);
		btLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login(serverLocation.getText().toString());
			}
		});
	}
	
	private void login(String location) {
		if (!location.startsWith("http://")) {
			location = "http://" + location;
		}
		
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("server_location", location);
		editor.commit();
		
		showMain();
	}
	
	public void showMain() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    	startActivity(intent);
	}
}
