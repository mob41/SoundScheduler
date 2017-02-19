package com.github.mob41.soundsche;

public class Track {
	
	private final TrackTime trackTime;
	
	private final String filePath;

	public Track(TrackTime trackTime, String filePath) {
		this.trackTime = trackTime;
		this.filePath = filePath;
	}

	public TrackTime getTrackTime() {
		return trackTime;
	}

	public String getFilePath() {
		return filePath;
	}

}
