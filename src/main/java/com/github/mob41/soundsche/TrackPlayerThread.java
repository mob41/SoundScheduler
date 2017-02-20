package com.github.mob41.soundsche;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.player.media.callback.nonseekable.FileInputStreamMedia;
import uk.co.caprica.vlcj.player.media.callback.seekable.RandomAccessFileMedia;

public class TrackPlayerThread extends Thread {

	private boolean running = false;
	
	private final TrackManager mgr;
	
	private final AudioMediaPlayerComponent mediaComp;
	
	private final List<TickHandler> tickHandlers;
	
	private TrackTime offset = null;
	
	public TrackPlayerThread(TrackManager mgr) {
		this.mgr = mgr;
		tickHandlers = new ArrayList<TickHandler>(50);
		mediaComp = new AudioMediaPlayerComponent();
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void addTickHandler(TickHandler handler){
		tickHandlers.add(handler);
	}
	
	public void removeTickHandler(TickHandler handler){
		tickHandlers.remove(handler);
	}
	
	public void removeAllTickHandlers(){
		tickHandlers.clear();
	}
	
	public void setOffset(TrackTime time){
		this.offset = time;
	}
	
	public TrackTime getOffset(){
		return offset;
	}

	@Override
	public void run() {
		while (true){
			if (running){
				offset = offset != null ? offset : new TrackTime(0,0,0);
				Track[] allTracks = mgr.getAllTracks();
				
				if (allTracks.length == 0){
					running = false;
					System.out.println("No tracks defined.");
					continue;
				}
				
				Media media;
				
				TrackTime lastTime = mgr.getLastTrack().getTrackTime();
				int min;
				int sec;
				int ms;
				Track[] tracks;
				while (running && offset.isBefore(lastTime)){
					min = offset.getMin();
					sec = offset.getSec();
					ms = offset.getMs();
					
					for (TickHandler handler : tickHandlers){
						handler.handle();
					}
					
					tracks = mgr.getTracksAtPos(offset);
					
					for (int i = 0; i < tracks.length; i++){
						mediaComp.getMediaPlayer().stop();
						Track track = tracks[i];
						File file = new File(track.getFilePath());
						
						if (!file.exists()){
							System.out.println("File not exist: " + file.getAbsolutePath());
							continue;
						}
						
						mediaComp.getMediaPlayer().playMedia(file.getAbsolutePath());
					}
					
					try {
						sleep(10);
					} catch (InterruptedException e) {
					}
					
					ms++;
					
					if (ms >= 100){
						sec++;
						ms = 0;
						
						if (sec >= 60){
							min++;
							sec = 0;
						}
					}
					
					offset = new TrackTime(min,sec,ms);
				}
				mediaComp.getMediaPlayer().stop();
				running = false;
			}
		}
	}
	
	public void startThread(){
		running = true;
	}
	
	public void pauseMedia(){
		running = false;
		mediaComp.getMediaPlayer().stop();
	}
	
	public void stopMedia(){
		pauseMedia();
		
		running = false;
		mediaComp.getMediaPlayer().stop();
		offset = null;
	}

}
