package com.nathanpc.streamjson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddActivity extends Activity {
	private EditText id;
	private EditText title;
	private EditText poster;
	private EditText location;
	private EditText description;
	
	private RadioGroup radioPoster;
	private RadioGroup radioLocation;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        setupUI();
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.edit, menu);
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.accept_menu:
    			// TODO: Log first, then go for the real shit.
    			break;
    		case R.id.discart_menu:
    			cancel();
    			break;
    	}

    	return true;
    }
	
	private void setupUI() {
		id = (EditText)findViewById(R.id.txt_id);
		title = (EditText)findViewById(R.id.txt_title);
		poster = (EditText)findViewById(R.id.txt_poster);
		location = (EditText)findViewById(R.id.txt_video_location);
		description = (EditText)findViewById(R.id.txt_description);
		
		radioPoster = (RadioGroup)findViewById(R.id.poster_location_radio);
		radioLocation = (RadioGroup)findViewById(R.id.video_location_radio);
	}
}
