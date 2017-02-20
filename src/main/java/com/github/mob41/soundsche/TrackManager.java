package com.github.mob41.soundsche;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	public Track getBeforeTrack(TrackTime time){
		Track[] tracks = getAllTracks();
		Track last = null;
		
		if (tracks.length < 2){
			return null;
		}
		
		for (int i = 0; i < tracks.length; i++){
			Track track = tracks[i];
			if (last == null && track.getTrackTime().isBefore(time)){
				last = track;
				continue;
			}
			
			if (track.getTrackTime().isBefore(time) &&
					track.getTrackTime().isAfter(last.getTrackTime())){
				last = track;
				
			}
		}
		
		return last;
	}
	
	public Track getNextTrack(TrackTime time){
		Track[] tracks = getAllTracks();
		Track last = null;
		
		if (tracks.length < 2){
			return null;
		}
		
		for (int i = 0; i < tracks.length; i++){
			Track track = tracks[i];
			if (last == null && track.getTrackTime().isAfter(time)){
				last = track;
				continue;
			}
			
			if (track.getTrackTime().isAfter(time) &&
					track.getTrackTime().isBefore(last.getTrackTime())){
				last = track;
				
			}
		}
		
		return last;
	}
	
	public Track getLastTrack(){
		Track[] tracks = getAllTracks();
		Track last = null;
		
		for (int i = 0; i < tracks.length; i++){
			Track track = tracks[i];
			if (last == null){
				last = track;
				continue;
			}
			
			if (last.getTrackTime().isBefore(track.getTrackTime())){
				last = track;
			}
		}
		
		return last;
	}
	
	public Track[] getAllTracks(){
		JSONObject tj = getTracksJson();
		
		List<Track> tracks = new ArrayList<Track>(150);
		
		Iterator<String> tj_it = tj.keys();
		while (tj_it.hasNext()){
			String tj_key = tj_it.next();
			
			JSONObject minj = tj.getJSONObject(tj_key);
			
			Iterator<String>  minj_it = minj.keys();
			while (minj_it.hasNext()){
				String minj_key = minj_it.next();
				
				JSONObject secj = minj.getJSONObject(minj_key);
				
				Iterator<String> secj_it = secj.keys();
				while (secj_it.hasNext()){
					String secj_key = secj_it.next();
					
					JSONArray msarr = secj.getJSONArray(secj_key);
					for (int i = 0; i < msarr.length(); i++){
						tracks.add(new Track(
								new TrackTime(
										Integer.parseInt(tj_key),
										Integer.parseInt(minj_key),
										Integer.parseInt(secj_key)
										),
								msarr.getString(i)));
					}
				}
			}
		}
		
		Track[] out = new Track[tracks.size()];
		for (int i = 0; i < out.length; i++){
			out[i] = tracks.get(i);
		}
		return out;
	}
	
	public Track[] getTracksAtPos(TrackTime time){
		JSONObject tj = getTracksJson();
		
		final Track[] emptyTracks = {};
		
		int min = time.getMin();
		int sec = time.getSec();
		int ms = time.getMs();
		
		JSONObject minJson = tj.isNull(Integer.toString(min)) ? null : tj.getJSONObject(Integer.toString(min));
		
		if (minJson == null){
			return emptyTracks; 
		}
		
		JSONObject secJson = minJson.isNull(Integer.toString(sec)) ? null : minJson.getJSONObject(Integer.toString(sec));
		
		if (secJson == null){
			return emptyTracks; 
		}
		
		JSONArray msJson = secJson.isNull(Integer.toString(ms)) ? null : secJson.getJSONArray(Integer.toString(ms));
		
		if (msJson == null){
			return emptyTracks; 
		}
		
		Track[] out = new Track[msJson.length()];
		for (int i = 0; i < out.length; i++){
			out[i] = new Track(time, msJson.getString(i));
		}
		
		return out;
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
		return true;
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
