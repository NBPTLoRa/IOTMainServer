package me.gacl.domain;

public class DayCount {
	private String Smoke;
	private String Temperature;
	private String Humidity;
	private String Parklot;
	private String Safety;
	private String Time;
	public String getSmoke() {
		return Smoke;
	}
	public void setSmoke(String smoke) {
		Smoke = smoke;
	}
	public String getTemperature() {
		return Temperature;
	}
	public void setTemperature(String temperature) {
		Temperature = temperature;
	}
	public String getHumidity() {
		return Humidity;
	}
	public void setHumidity(String humidity) {
		Humidity = humidity;
	}
	public String getParklot() {
		return Parklot;
	}
	public void setParklot(String parklot) {
		Parklot = parklot;
	}
	public String getSafety() {
		return Safety;
	}
	public void setSafety(String safety) {
		Safety = safety;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	
	public String toString()
	{
		if(Time!=null) {
		return Time;
		}
		else {
		return Smoke+","+Temperature+","+Humidity+","+Parklot+","+Safety;
		}
	}
}
