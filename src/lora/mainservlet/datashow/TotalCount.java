package lora.mainservlet.datashow;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
 * Servlet implementation class TotalCount
 */
public class TotalCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	 /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public TotalCount() {
        super();
        // TODO Auto-generated constructor stub
        sqlOp=new SqlOp();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stubresponse.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		
		LoginObj loginObj=Auth.tokenLogin(request);
		
		String TotalDevice="";
		String NetWorkedDevice="";
		String TotalData="";
		String DayData="";
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
			//String total=sqlOp.getTotalCount();
			if(true)//total.split(":")[0].equals("1"))
			{
				//String s=total.substring(2);
				TotalDevice="";		//s.split(",")[0];
				NetWorkedDevice="208";
				TotalData="300000";
				DayData="7754";
			}
			else
			{
				//retError=total.split(":")[1];
			}
		}
		
		String retJsonS="{"
				+"\"TotalDevice\":\""+TotalDevice+"\","
				+"\"NetWorkedDevice\":\""+NetWorkedDevice+"\","
				+"\"TotalData\":\""+TotalData+"\","
				+"\"DayData\":\""+DayData+"\","
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
