package SYT.domain;

public class User {
	
	private String userID;
	private String userPWD;
	private String userCreTime;
	private String userClas;	
	
	public String getUserCreTime() {
		return userCreTime;
	}
	public void setUserCreTime(String userCreTime) {
		this.userCreTime = userCreTime;
	}
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
	
}