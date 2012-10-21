package com.nathanpc.streamjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Check if tablet or phone, then check if 10" and populate with the correct layout.
        setContentView(R.layout.main_10inch);
        
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
	    private ProgressBar progress;
	    
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
			progress.setVisibility(View.GONE);

	    	try {
	 			videos = json.getJSONArray("videos");

	 			for (int i = 0; i < videos.length(); ++i) {
	 				JSONObject video = videos.getJSONObject(i);
	 				String title = video.getString("title");
	 				String service = video.getString("service");
	 				String url = video.getString("video_url");
	 			    
	 			    titleArray.add(title); // TODO: Match with settings if this was already downloaded (prevents redownloading)
	 			    if (service.toString().equalsIgnoreCase("youtube")) {
	 			    	serviceArray.add("YouTube");
	 			    } else {
	 			    	serviceArray.add(service);
	 			    }
	 			    urlArray.add(url);
	 			}
	 		} catch (JSONException e) {
	 			Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	 		}
	 		
	 		listView = getListView();
	        listView.setTextFilterEnabled(true);
	 		
	 		titles = new String[titleArray.size()];
	 		services = new String[serviceArray.size()];
	 		url = new String[urlArray.size()];
	 		
	 		titleArray.toArray(titles);
	 		serviceArray.toArray(services);
	 		urlArray.toArray(url);
	 		
	 		populateList(titles, services);
	     }
	}
	
	private ArrayList<Map<String, String>> buildData(String[] t, String[] s) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		for (int i = 0; i < t.length; ++i) {
			list.add(putData(t[i], s[i]));
		}

		return list;
	}

	private HashMap<String, String> putData(String title, String service) {
		HashMap<String, String> item = new HashMap<String, String>();

		item.put("title", title);
		item.put("service", service);

		return item;
	}
	
	private class getDownloadLinkTask extends AsyncTask<String, Void, JSONObject> {
		private String video_title;
		private String service_url; 
		
		protected JSONObject doInBackground(String... params) {
			video_title = params[2];
			service_url = params[1];
			
	    	return RESTClient.GET(Fields.restify_server + "/download/" + params[0] + "/" + params[1]);
	    }

	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			/**
			 * TODO: Add a loading dialog here, because on slow conn it takes some time to get
			 */
			
			//progress = (ProgressBar)findViewById(R.id.progress_down_load);
			//progress.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(JSONObject json) {
			try {
	 			String video_url = json.getString("video_url");
	 			String video_file = json.getString("file");
	 			
	 			downloadFile downloadFile = new downloadFile();
	 			downloadFile.execute(video_url, video_file, video_title, service_url);
	 		} catch (JSONException e) {
	 			Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	 		}
	    }
	}
}
