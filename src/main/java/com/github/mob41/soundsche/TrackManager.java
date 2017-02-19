package com.github.mob41.soundsche;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackManager {
	
	private JSONObject json;
	
	public TrackManager() {
		json = new JSONObject();
		json.put("tracks", new JSONObject());
	}
	
	public JSONObject getTracksJson(){
		return json.getJSONObject("tracks");
	}
	
	public void addTrack(Track track){
		JSONObject tj = getTracksJson();
		
		TrackTime tt = track.getTrackTime();
		
		int min = tt.getMin();
		int sec = tt.getSec();
		int ms = tt.getMs();
		
		JSONObject minJson = tj.isNull(Integer.toString(min)) ? new JSONObject() : tj.getJSONObject(Integer.toString(min));
		JSONObject secJson = minJson.isNull(Integer.toString(sec)) ? new JSONObject() : minJson.getJSONObject(Integer.toString(sec));
		JSONArray msJson = secJson.isNull(Integer.toString(ms)) ? new JSONArray() : secJson.getJSONArray(Integer.toString(ms));
		
		msJson.put(track.getFilePath());
		
		secJson.put(Integer.toString(ms), msJson);
		minJson.put(Integer.toString(sec), secJson);
		
		tj.put(Integer.toString(min), minJson);
		
		json.put("tracks", tj);
	}
	
	public boolean removeTrack(Track track){
		JSONObject tj = getTracksJson();
		
		TrackTime tt = track.getTrackTime();
		
		int min = tt.getMin();
		int sec = tt.getSec();
		int ms = tt.getMs();
		
		JSONObject minJson = tj.isNull(Integer.toString(min)) ? new JSONObject() : tj.getJSONObject(Integer.toString(min));
		JSONObject secJson = minJson.isNull(Integer.toString(sec)) ? new JSONObject() : minJson.getJSONObject(Integer.toString(sec));
		JSONArray msJson = secJson.isNull(Integer.toString(ms)) ? new JSONArray() : secJson.getJSONArray(Integer.toString(ms));
		
		String filePath = track.getFilePath();
		
		int index = searchString(msJson, filePath);
		
		if (index == -1){
			return false;
		}
		
		msJson.remove(index);
		
		secJson.put(Integer.toString(ms), msJson);
		minJson.put(Integer.toString(sec), secJson);
		
		tj.put(Integer.toString(min), minJson);
		
		json.put("tracks", tj);
	}
	
	public static int searchString(JSONArray arr, String str){
		for (int i = 0; i < arr.length(); i++){
			if (arr.getString(i).equals(str)){
				return i;
			}
		}
		return -1;
	}
	
	public void load(String filePath) throws IOException{
		File file = new File(filePath);
		
		if (!file.exists()){
			throw new IOException("No such file: " + filePath);
		}
		
		FileInputStream in = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		String data = "";
		String line;
		
		while((line = reader.readLine()) != null){
			data += line;
		}
		
		in.close();
		
		try {
			json = new JSONObject(data);
		} catch (JSONException e){
			throw new IOException("Invalid JSON", e);
		}
	}
	
	public void save(String filePath) throws IOException{
		File file = new File(filePath);
		
		if (!file.exists()){
			file.createNewFile();
		}
		
		FileOutputStream out = new FileOutputStream(file);
		
		PrintWriter writer = new PrintWriter(out, true);
		writer.println(json.toString(5));
	}

}
