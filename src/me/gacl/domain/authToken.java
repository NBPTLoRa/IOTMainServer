package me.gacl.domain;

public class authToken {
	private String APIKey;
	private String client_ID;
	private String accessToken;
	private String tokenState;
	private String accCreTime;
	private String effectiveTime;
	
	
	public String getAPIKey() {
		return APIKey;
	}
	public void setAPIKey(String aPIKey) {
		APIKey = aPIKey;
	}
	public String getClient_ID() {
		return client_ID;
	}
	public void setClient_ID(String client_ID) {
		this.client_ID = client_ID;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenState() {
		return tokenState;
	}
	public void setTokenState(String tokenState) {
		this.tokenState = tokenState;
	}
	public String getAccCreTime() {
		return accCreTime;
	}
	public void setAccCreTime(String accCreTime) {
		this.accCreTime = accCreTime;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
    public String toString() {
    	return accCreTime+","+effectiveTime;
    }
}
