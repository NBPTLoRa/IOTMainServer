package web.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lora.auth.Auth;
import web.loginVerify.LoginObj;
import web.sqloperation.SqlOp;

/**
 * Servlet implementation class RefreshAPIKey
 */
public class RefreshAPIKey extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RefreshAPIKey() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String userID=request.getParameter("userID");
		String pwd=request.getParameter("userID");
		
		
		String retSuccess="failed";
		String retNewAPI="0";
		String retError="e:CreateNullError";
		
		JsonObject retJ =new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		//查找有没有符合的用户
		LoginObj loginObj=Auth.authLogin(request);
		
		if(!loginObj.getLoginSta())
		{//登录失败
			retSuccess="failed";
			retNewAPI="0";
			retError+=loginObj.getException();
		}
		else
		{//登陆成功
			String newKey=GetAccToken.randomHexString(32);
			String retS="";//SqlOp.refreshAPIKEy(userID,newKey);
			if(retS.substring(0, 1).equals("e"))
			{
				retSuccess="failed";
				retNewAPI="0";
				retError="0";
			}
			else
			{
				retSuccess="success";
				retNewAPI=newKey;
				retError+=retS;
			}
			
		}
		
		//返回accessToken
		retJ=jsonParser.parse("{\"success\":\""+retSuccess
				+"\",\"newAPIKEy\":\""+retNewAPI
				+"\",\"error\":\""+retError.replace("\"","#").replace("e:CreateNullError", "")//
				+"\"}").getAsJsonObject();
		out.println(retJ);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
