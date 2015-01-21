package com.milton.gotcha;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.util.Log;
import java.sql.Timestamp;


public class DataFetcher {
	public String myID;
	public String newTarget = "";
	public String currTarget = "";
	int tries = 0;
	
	public DataFetcher(String ID)
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		myID = ID;
	}
	
	public boolean tagOut()
	{
		JSONObject json = readServer("tagOut" ,myID);
		int result = 0;
		try {
		    result = json.getInt("success");
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		if(result == 0)
			return false;
		else
			return true;
	}
	
	public int[] getTime()
	{
        int[] dateInfo = new int[4];
		long unixTime = System.currentTimeMillis();
        long elapsedTime = unixTime/1000 - 1361077726;
        
        dateInfo[0] = (int)(elapsedTime % 60);//second
        dateInfo[1] = (int)((elapsedTime/60) % 60);//minute
        dateInfo[2] = (int)((elapsedTime/3600) % 24);//hour
        dateInfo[3] = (int)(elapsedTime/86400);//day
        
        return dateInfo;
	}
	
	public int[] getTimeSurvived()
	{
        int[] dateInfo = new int[4];
		long unixTime = 0;
		
		JSONObject json = readServer("getUnixTime" ,myID);
		int result = 0;
		try {
		    unixTime = json.getLong("unixTime") - 1361077726;
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		
        dateInfo[0] = (int)(unixTime % 60);//second
        dateInfo[1] = (int)((unixTime/60) % 60);//minute
        dateInfo[2] = (int)((unixTime/3600) % 24);//hour
        dateInfo[3] = (int)(unixTime/86400);//day
        
        return dateInfo;
	}
	
	public int getTries()
	{
		JSONObject json = readServer("getTries" ,myID);
		int result = 0;
		try {
		    result = json.getInt("tries");
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	public int getOut()
	{
		JSONObject json = readServer("getOut", myID);
		int result = 0;
		try {
			JSONArray a = json.getJSONArray("out");
			result = a.length();
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	public int numberTagged()
	{
		JSONObject json = readServer("numberTagged" ,myID);
		int result = 0;
		try {
		    result = json.getInt("tries");
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	public int getTotal()
	{
		JSONObject json = readServer("listNames", myID);
		int result = 0;
		try {
			JSONArray a = json.getJSONArray("ids");
			result = a.length();
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return result;
	}

	public boolean setTries()
	{
		boolean re = false;
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://gotcha.gearheadlabs.com/android.php?a=setTries"  + "&id=" + myID + "&tries=" + tries + "&newTarget=" + newTarget);
	    try {
	      HttpResponse response = client.execute(httpGet);
	      re = true;
	    }
	    catch(Exception e){}
	    return re;
	}

	public List<String> getList()
	{
		JSONObject json = readServer("listNames" ,myID);
		 List<String> list = new ArrayList<String>();
		 try {
			JSONArray a = json.getJSONArray("ids");
			for(int i=0;i<a.length();i++){
				list.add((String) a.get(i));
			}
		}
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public List<String> getRandom()
	{
		JSONObject json = readServer("getRandomOptions" ,myID);
		 List<String> list = new ArrayList<String>();
		 try {
			JSONArray a = json.getJSONArray("ids");
			for(int i=0;i<a.length();i++){
				try{
				list.add((String) a.get(i));
				}
				catch(Exception e){list.add("");}
			}
		}
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public String getName(String id)
	{
		JSONObject json = readServer("getName" ,id);
		String name = null;
		try {
		    name = json.getString("name");
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return name;
	}
	
	public String getTarget()
	{
		JSONObject json = readServer("getTarget", myID);
		String id = null;
		try {
		    id = json.getString("id");
		    currTarget = id;
		}
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return id;
	}
	
	public List<Integer> topTen()
	{
		JSONObject json = readServer("topTen" ,myID);
		 List<Integer> list = new ArrayList<Integer>();
		 try {
			JSONArray a = json.getJSONArray("tags");
			for(int i=0;i<a.length();i++){
				list.add(Integer.parseInt((String) a.get(i)));
			}
		}
		 catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public JSONObject readServer(String action, String id) {
			JSONObject jObj = null;
		    StringBuilder builder = new StringBuilder();
		    HttpClient client = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet("http://gotcha.gearheadlabs.com/android.php?a=" + action + "&id=" + id + "&tries=" + tries + "&newTarget=" + newTarget);
		    try {
		      HttpResponse response = client.execute(httpGet);
		      StatusLine statusLine = response.getStatusLine();
		      int statusCode = statusLine.getStatusCode();
		      if (statusCode == 200) {
		        HttpEntity entity = response.getEntity();
		        InputStream content = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          builder.append(line);
		        }
		      } else {
		        Log.e("Failed to download file", "");
		      }
		    } catch (ClientProtocolException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		      try {
            jObj = new JSONObject(builder.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
	}

}
