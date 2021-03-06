package com.nathanpc.streamjson.restful;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.nathanpc.streamjson.restful.Convert;

public class RESTClient {

	private static HttpResponse response;
	private static String finalJSON;

	/* This is a test function which will connects to a given
	 * rest service and prints it's response to Android Log with
	 * labels "RESTClient".
	 */
	public static JSONObject GET(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url); 
		HttpResponse response;

		try {
			response = httpclient.execute(httpget);
			Log.v("response code", response.getStatusLine().getStatusCode() + ""); 

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result = Convert.StreamToString(instream);

				// A Simple JSONObject Creation
				JSONObject json = new JSONObject(result);
				instream.close();
				
				return json;
			}


		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String rawGET(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url); 
		HttpResponse response;

		try {
			response = httpclient.execute(httpget);
			Log.v("response code", response.getStatusLine().getStatusCode() + ""); 

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result = Convert.StreamToString(instream);
				instream.close();
				
				return result;
			}


		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String POST(String url, String[] argNames, String[] argValues) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(argNames.length);
		    
		    for (int i = 0; i < argNames.length; i++) {
		    	nameValuePairs.add(new BasicNameValuePair(argNames[i], argValues[i]));
		    }
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		    // Execute HTTP Post Request
		    response = httpclient.execute(httppost);
		    finalJSON = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return finalJSON;
	}

}
