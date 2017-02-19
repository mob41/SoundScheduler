package com.github.mob41.soundsche;

public class TrackTime {
	
	private final int min;
	
	private final int sec;
	
	private final int ms;

	public TrackTime(int min, int sec, int ms) {
		this.min = min;
		this.sec = sec;
		this.ms = ms;
	}
	
	public int getMin(){
		return min;
	}
	
	public int getSec(){
		return sec;
	}
	
	public int getMs(){
		return ms;
	}

}
