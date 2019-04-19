package me.gacl.domain;

public class DayHistory {
	private String Time;
	private String TimeCount;
	
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getTimeCount() {
		return TimeCount;
	}
	public void setTimeCount(String timeCount) {
		TimeCount = timeCount;
	}
	
	public String toString()
	{
		return Time+","+TimeCount;
	}
}
