package web.loginVerify;

public class LoginObj {
	public Boolean getLoginSta() {
		return loginSta;
	}
	public void setLoginSta(Boolean loginSta) {
		this.loginSta = loginSta;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	Boolean loginSta=false;
	String exception="none";
}
