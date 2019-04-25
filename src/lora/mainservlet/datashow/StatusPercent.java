package lora.mainservlet.datashow;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lora.auth.Auth;
import web.loginVerify.LoginObj;
import web.sqloperation.SqlOp;

/**
 * Servlet implementation class StatusPercent
 */
public class StatusPercent extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
 /**
  * @see HttpServlet#HttpServlet()
  */
	SqlOp sqlOp;
 public StatusPercent() {
     super();
     // TODO Auto-generated constructor stub
     sqlOp=new SqlOp();
 }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		
		LoginObj loginObj=Auth.tokenLogin(request);
		
		String Normal="";
		String Warning="";
		String Alarm="";
		String LowPower="";
		String retError="CreateNull";
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		//≈–∂œ”√ªßº¯»®
		Boolean authFlag=false;
		if(loginObj.getLoginSta())
		{
			authFlag=true;
		}else
		{
			retError+="Your account does not have permission!"+loginObj.getException();
		}
		
		if(authFlag)
		{
			try
			{
				DecimalFormat df=new DecimalFormat("#.00");
				Normal=df.format(60.00+new Random().nextDouble()*10);
				Warning=df.format(87-Double.valueOf(Normal));
				Alarm="00";
				LowPower="13.00";
			}
			catch (Exception e) {
				// TODO: handle exceptio
				e.printStackTrace();
				retError+=e.getMessage();
			}
		}
		
		String retJsonS="{"
				+"\"Normal\":\""+Normal+"\","
				+"\"Warning\":\""+Warning+"\","
				+"\"Alarm\":\""+Alarm+"\","
				+"\"LowPower\":\""+LowPower+"\","
				+"\"error\":\""+retError.replace("\"","#").replace("CreateNull", "")
				+"\"}";
		retJ=jsonParser.parse(retJsonS).getAsJsonObject();
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
