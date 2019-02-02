package lora.auth;

import javax.servlet.http.HttpServletRequest;

import web.loginVerify.LoginObj;
import web.loginVerify.LoginVerfication;

public class Auth {
	public static LoginObj auth(HttpServletRequest request)
	{
		LoginVerfication loginVerfication=new LoginVerfication();
		String userID=request.getParameter("userID");
		LoginObj loginObj=loginVerfication.veriLogin(userID,request.getParameter("pwd"));
		return loginObj;
	}
}
