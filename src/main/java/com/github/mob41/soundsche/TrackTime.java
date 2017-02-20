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
	
	public String toString(){
		return addZero(min) + ":" + addZero(sec) + ":" + addZero(ms);
	}
	
	public static String addZero(int data){
		return data < 10 ? "0" + data : Integer.toString(data);
	}
	
	public TrackTime minus(TrackTime arg0){
		return new TrackTime(min - arg0.min, sec - arg0.sec, Math.abs(ms - arg0.ms));
	}
	
	public boolean equals(TrackTime arg0){
		return arg0.min == min && arg0.sec == sec && arg0.ms == ms;
	}
	
	public boolean isBefore(TrackTime arg0){
		if (arg0.min > min){
			return true;
		} else if (arg0.min < min){
			return false;
		}

		if (arg0.sec > sec){
			return true;
		} else if (arg0.sec < sec){
			return false;
		}
		
		if (arg0.ms > ms){
			return true;
		} else if (arg0.ms < ms){
			return false;
		}
		
		return false;
	}
	
	public boolean isAfter(TrackTime arg0){
		if (arg0.min < min){
			return true;
		} else if (arg0.min > min){
			return false;
		}

		if (arg0.sec < sec){
			return true;
		} else if (arg0.sec > sec){
			return false;
		}
		
		if (arg0.ms < ms){
			return true;
		} else if (arg0.ms > ms){
			return false;
		}
		
		return false;
	}
	
	public static TrackTime fromString(String str){
		String[] strs = str.split(":");
		
		if (strs.length < 3){
			return null;
		}
		
		int[] intArr = new int[strs.length];
		for (int i = 0; i < 2; i++){
			try {
				intArr[i] = Integer.parseInt(strs[i]);
			} catch (NumberFormatException e){
				return null;
			}
		}
		
		return new TrackTime(intArr[0], intArr[1], intArr[2]);
	}

}
