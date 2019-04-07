package lora.mainservlet.datashow;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class DevicePercent
 */
public class DevicePercent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DevicePercent() {
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
		
		String Smoke="";
		String Temperature="";
		String Humidity="";
		String Parklot="";
		String Safety="";
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
			Smoke="30.30";
			Temperature="20.00";
			Humidity="10.30";
			Parklot="30.00";
			Safety="9.40";
		}
		
		String retJsonS="{"
				+"\"Smoke\":\""+Smoke+"\","
				+"\"Temperature\":\""+Temperature+"\","
				+"\"Humidity\":\""+Humidity+"\","
				+"\"Parklot\":\""+Parklot+"\","
				+"\"Safety\":\""+Safety+"\","
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
