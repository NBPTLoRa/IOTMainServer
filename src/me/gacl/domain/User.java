package me.gacl.domain;

public class User {
	private String userID;
	private String userPWD;
	private String userClas;
	
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
	public String getUserPWD() {
		return userPWD;
	}
	public void setUserPWD(String userPWD) {
		this.userPWD = userPWD;
	}
	
    public String toString() {
        return userClas;
    }
}
