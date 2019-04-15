package me.gacl.domain;

public class TotalCount {
	private String TotalCount;
	private String NetWorkCount;
	private String TotalDataCount;
	private String DayDataCount;
	
	public String getTotalCount() {
		return TotalCount;
	}
	public void setTotalCount(String totalCount) {
		TotalCount = totalCount;
	}
	public String getNetWorkCount() {
		return NetWorkCount;
	}
	public void setNetWorkCount(String netWorkCount) {
		NetWorkCount = netWorkCount;
	}
	public String getTotalDataCount() {
		return TotalDataCount;
	}
	public void setTotalDataCount(String totalDataCount) {
		TotalDataCount = totalDataCount;
	}
	public String getDayDataCount() {
		return DayDataCount;
	}
	public void setDayDataCount(String dayDataCount) {
		DayDataCount = dayDataCount;
	}
	
	public String toString()
	{
		return TotalCount+","+NetWorkCount+","+TotalDataCount+","+DayDataCount;
	}
}
