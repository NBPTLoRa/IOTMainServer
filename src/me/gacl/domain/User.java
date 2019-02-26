package me.gacl.domain;

public class User {
	private String userID;
	private String userPWD;
	private String userClas;
	private String userCreTime;
	private String userAPIKey;
	private String tokenCreTime;
	
	public String getUserClas() {
		return userClas;
	}
	public void setUserClas(String userClas) {
		this.userClas = userClas;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserCreTime() {
		return userCreTime;
	}
	public void setUserCreTime(String userCreTime) {
		this.userCreTime = userCreTime;
	}
	public String getUserAPIKey() {
		return userAPIKey;
	}
	public void setUserAPIKey(String userAPIKey) {
		this.userAPIKey = userAPIKey;
	}
	public String getTokenCreTime() {
		return tokenCreTime;
	}
	public void setTokenCreTime(String tokenCreTime) {
		this.tokenCreTime = tokenCreTime;
	}
	public String getUserPWD() {
		return userPWD;
	}
	public void setUserPWD(String userPWD) {
		this.userPWD = userPWD;
	}
	
    public String toString() {
    	if(userClas!=null)
    	{
    		return userClas;
    	}
    	else
    	{
    		return tokenCreTime+":"+userAPIKey;
    	}
    }
}
