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
		System.out.println("authLogin:"+"userID="+userID+" pwd="+pwd);
		LoginObj loginObj=loginVerfication.veriLogin(userID,pwd);
		return loginObj;
	}//
	
	
	public static LoginObj tokenLogin(HttpServletRequest request)
	{
		LoginVerfication loginVerfication=new LoginVerfication();
		String userID=request.getParameter("userID");
		if(userID==null)
		{
			userID="";
		}
		String accessToken=request.getParameter("accessToken");
		if(accessToken==null)
		{
			accessToken="";
		}
		String client_ID=request.getParameter("client_id");
		if(client_ID==null)
		{
			client_ID="";
		}
		System.out.println("userID="+userID+" client_id"+client_ID+" accessToken"+accessToken);
		LoginObj loginObj=loginVerfication.veriAuth(accessToken, userID, client_ID);
		return loginObj;
	}
}
