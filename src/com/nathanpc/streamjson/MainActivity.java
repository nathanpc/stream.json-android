package com.nathanpc.streamjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nathanpc.streamjson.misc.Fields;
import com.nathanpc.streamjson.restful.RESTClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	private ActionBar actionBar;
	private ListView listView;
	
	private JSONArray videos;
	private List<HashMap<String, String>> videoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Check if tablet or phone, then check if 10" and populate with the correct layout.
        setContentView(R.layout.main_10inch);
        
        // Initialize variables
        videoList = new ArrayList<HashMap<String, String>>();
        
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void setupUI() {
    	actionBar = getActionBar();
        
        listView = getListView();
        listView.setTextFilterEnabled(true);
    }
    
    // The mess to do HTTP requests in Android:
    private class getVideoListTask extends AsyncTask<String, Void, JSONObject> {
		//private ProgressBar progress;
	    
	    protected JSONObject doInBackground(String... arg) {
	    	return RESTClient.GET(Fields.server_location + "/list");
	    }

	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
			//progress = (ProgressBar)findViewById(R.id.progress_down_load);
			//progress.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(JSONObject json) {
			//progress.setVisibility(View.GONE);

	    	try {
	 			videos = json.getJSONArray("video");

	 			for (int i = 0; i < videos.length(); ++i) {
	 				JSONObject video = videos.getJSONObject(i);

	 				String title = video.getString("title");
	 				String poster = Fields.server_location +  "/getPoster/" + video.getString("id");
	 				String description = video.getJSONObject("description").getString("text");
	 			    
	 				HashMap<String, String> tmp_map = new HashMap<String,String>();
	 	            tmp_map.put("title", title);
	 	            tmp_map.put("description", description);
	 	            tmp_map.put("poster", poster);
	 	            videoList.add(tmp_map);
	 			}
	 		} catch (JSONException e) {
	 			Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	 		}
	 		
	 		//populateList(titles, services);
	     }
	}
}
