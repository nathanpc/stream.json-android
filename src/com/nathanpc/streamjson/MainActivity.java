package com.nathanpc.streamjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fedorvlasov.lazylist.ImageLoader;
import com.nathanpc.streamjson.adapter.ImageSimpleAdapter;
import com.nathanpc.streamjson.misc.Fields;
import com.nathanpc.streamjson.restful.RESTClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {
	//private ActionBar actionBar;
	private ListView listView;
	
	private RelativeLayout noVideoSelectedView;
	private RelativeLayout detailView;
	
	private ImageView videoPoster;
	private TextView videoTitle;
	private TextView videoDescription;
	private Button playButton;
	
	public Fields fields;
	public ImageLoader imageLoader;
	private JSONArray videos;
	private List<HashMap<String, String>> videoList;
	public String currentVideoID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Check if tablet or phone, then check if 10" and populate with the correct layout.
        setContentView(R.layout.main_10inch);
        
        // Initialize variables
        imageLoader = new ImageLoader(getApplicationContext());
        videoList = new ArrayList<HashMap<String, String>>();
        try {
			fields = new Fields(this);
		} catch (Exception e) {
			e.printStackTrace();
			generateAskModal("ERROR: " + e.getMessage(), "Login");
		}
        
        setupUI();
        new getVideoListTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setupUI() {
    	//actionBar = getActionBar();
        
        listView = getListView();
        listView.setTextFilterEnabled(true);
        
        noVideoSelectedView = (RelativeLayout)findViewById(R.id.no_video_view);
        detailView = (RelativeLayout)findViewById(R.id.detail_view);
        
        videoPoster = (ImageView)findViewById(R.id.poster_image);
        videoTitle = (TextView)findViewById(R.id.video_title);
        videoDescription = (TextView)findViewById(R.id.video_description);
        
        playButton = (Button)findViewById(R.id.bt_watch);
        playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new getVideoURLTask().execute(currentVideoID);
			}
        });
        
        detailView.setVisibility(View.INVISIBLE);
        noVideoSelectedView.setVisibility(View.VISIBLE);
    }
    
    private void populateList() {
        String[] from = { "poster", "title", "description" };
        int[] to = { R.id.item_video_poster, R.id.item_video_title, R.id.item_video_description };

        ImageSimpleAdapter adapter = new ImageSimpleAdapter(getBaseContext(), imageLoader, videoList, R.layout.list_item, from, to);
        listView.setAdapter(adapter);
        
        Log.i("LIST", "Populated");
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(getApplicationContext(), videoList.get(position).get("title"), Toast.LENGTH_SHORT).show();
				populateDetailView(videoList.get(position).get("id"), videoList.get(position).get("poster"),
						videoList.get(position).get("title"), videoList.get(position).get("description"));
			}
        });
    }
    
    private void populateDetailView(String id, String poster, String title, String description) {
    	noVideoSelectedView.setVisibility(View.INVISIBLE);
    	detailView.setVisibility(View.VISIBLE);
    	
    	imageLoader.DisplayImage(fields.server_location + "/getPoster/" + id, videoPoster);
    	videoTitle.setText(title);
    	videoDescription.setText(description);
    	
    	currentVideoID = id;
    }
    
    public void playVideo(String videoLocation) {
		Intent play = new Intent(Intent.ACTION_VIEW);
		play.setDataAndType(Uri.parse(videoLocation), "video/*");
		
		startActivity(play);
	}
    
    // The mess to do HTTP requests in Android:
    private class getVideoListTask extends AsyncTask<String, Void, JSONObject> {
		//private ProgressBar progress;
	    
	    protected JSONObject doInBackground(String... arg) {
	    	Log.i("REST", "Started");
	    	
	    	JSONObject json = RESTClient.GET(fields.server_location + "/list");
	    	return json;
	    }

	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
			//progress = (ProgressBar)findViewById(R.id.progress_down_load);
			//progress.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(JSONObject json) {
			//progress.setVisibility(View.GONE);
			Log.i("REST", "Parsing");

	    	try {
	 			videos = json.getJSONArray("video");

	 			for (int i = 0; i < videos.length(); ++i) {
	 				JSONObject video = videos.getJSONObject(i);

	 				String id = video.getString("id");
	 				String title = video.getString("title");
	 				String poster = fields.server_location +  "/getPoster/" + video.getString("id");
	 				String description = video.getJSONObject("description").getString("text");
	 			    
	 				HashMap<String, String> tmp_map = new HashMap<String,String>();
	 				tmp_map.put("id", id);
	 				tmp_map.put("title", title);
	 	            tmp_map.put("description", description);
	 	            tmp_map.put("poster", poster);
	 	            videoList.add(tmp_map);
	 			}
	 		} catch (JSONException e) {
	 			Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	 			Log.e("JSON Parse", e.getMessage());
	 		} catch (NullPointerException e) {
	 			e.printStackTrace();
				generateAskModal("Looks like you entered the incorrect location to your stream.json server", "Login", "Try Again");
	 		}
	 		
	 		populateList();
	     }
	}
    
    public void generateAskModal(String message, String positive, String neutral) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(message).setPositiveButton(positive, dialogClickListener).setNeutralButton(neutral, dialogClickListener).show();
    }
    
    public void generateAskModal(String message, String positive) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(message).setPositiveButton(positive, dialogClickListener).show();
    }
    
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
            	Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            	intent.putExtra("retry", true);
            	
		    	startActivity(intent);
                break;
            case DialogInterface.BUTTON_NEUTRAL:
            	new getVideoListTask().execute();
            	break;
            case DialogInterface.BUTTON_NEGATIVE:
                finish();
                break;
            }
        }
    };
    
    private class getVideoURLTask extends AsyncTask<String, Void, String> {
		//private ProgressBar progress;
	    
	    protected String doInBackground(String... arg) {
	    	Log.i("REST", "Started");
	    	
	    	String url = RESTClient.rawGET(fields.server_location + "/getVideo/" + arg[0]);
	    	return url;
	    }

	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
			//progress = (ProgressBar)findViewById(R.id.progress_down_load);
			//progress.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(String url) {
			//progress.setVisibility(View.GONE);
			playVideo(url);
	     }
	}
}
