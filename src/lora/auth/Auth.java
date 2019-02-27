package lora.auth;

import javax.servlet.http.HttpServletRequest;

import web.loginVerify.LoginObj;
import web.loginVerify.LoginVerfication;

public class Auth {
	
	public static LoginObj authLogin(HttpServletRequest request)
	{
		LoginVerfication loginVerfication=new LoginVerfication();
		String userID=request.getParameter("userID");
		String pwd=request.getParameter("pwd");
		LoginObj loginObj=loginVerfication.veriLogin(userID,pwd);
		return loginObj;
	}//
	
	
	public static LoginObj tokenLogin(HttpServletRequest request)
	{
		LoginVerfication loginVerfication=new LoginVerfication();
		String userID=request.getParameter("userID");
		String accessToken=request.getParameter("accessToken");
		String client_ID=request.getParameter("client_ID");
		LoginObj loginObj=loginVerfication.veriAuth(accessToken, userID, client_ID);
		return loginObj;
	}
}
