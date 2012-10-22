package com.nathanpc.streamjson;

import com.nathanpc.streamjson.restful.RESTClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

public class AddActivity extends Activity {
	private EditText id;
	private EditText title;
	private EditText poster;
	private EditText location;
	private EditText description;
	
	private RadioGroup radioPoster;
	private RadioGroup radioLocation;
	
	private String server_location;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        server_location = getIntent().getExtras().getString("server_location");
        
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
    			String id = this.id.getText().toString();
    			String title = this.title.getText().toString();
    			
    			String poster = this.poster.getText().toString();
    			String poster_location = null;
    			int poster_location_id = this.radioPoster.getCheckedRadioButtonId();
    			if (poster_location_id == R.id.poster_local) {
    				poster_location = "false";
    			} else if (poster_location_id == R.id.poster_remote) {
    				poster_location = "true";
    			}
    			
    			String location = this.location.getText().toString();
    			String video_location = null;
    			int video_location_id = this.radioLocation.getCheckedRadioButtonId();
    			if (video_location_id == R.id.video_local) {
    				video_location = "false";
    			} else if (video_location_id == R.id.video_remote) {
    				video_location = "true";
    			}
    			
    			String desc_format = "text";
    			String description = this.description.getText().toString();
    			
    			
    			new addTask().execute(id, title, poster_location, poster, video_location, location, desc_format, description);
    			break;
    		case R.id.discart_menu:
    			finish();
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
	
	private class addTask extends AsyncTask<String, Void, String> {
	    //private ProgressBar progress;
	    
	    protected String doInBackground(String... params) {
	    	String[] names = { "id", "title", "poster_remote", "poster_location", "file_remote",
	    			"file_location", "description_format", "description" };
			String[] values = params;
			
			Log.i("Before Add POST", values.toString());
			return RESTClient.POST(server_location + "/add", names, values);
	    }

	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
			//progress = (ProgressBar)findViewById(R.id.login_progress);
			//progress.setVisibility(View.VISIBLE);
		}

	    protected void onPostExecute(String res) {
			//progress.setVisibility(View.GONE);
	    	Log.i("After Add POST", res);
	    	finish();
	     }
	 }
}
