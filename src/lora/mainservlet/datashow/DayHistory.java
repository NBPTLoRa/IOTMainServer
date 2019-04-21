package lora.mainservlet.datashow;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * Servlet implementation class DayHistory
 */
@WebServlet("/DayHistory")
public class DayHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DayHistory() {
        super();
        // TODO Auto-generated constructor stub
        sqlOp=new SqlOp();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter out=response.getWriter();
				
				LoginObj loginObj=Auth.tokenLogin(request);
				
				String retHistorys="";
				String retError="CreateNull";
				
				JsonObject retJ=new JsonObject();
				JsonParser jsonParser=new JsonParser();
				
				//判断用户鉴权
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
					//获取数据
					String retSql=sqlOp.getDayHistory();
					if(retSql.substring(0,1).equals("1"))
					{//通过
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-");
						String d=sdf.format(new Date());
						
						//分析数据
						String[] hiStrings=new String[24];
						hiStrings=retSql.split(":")[1].split(",");
						for(int i=0;i<24;i++)
						{
							retHistorys+="\""+d+String.format("%02d", i)+","+hiStrings[i]+"\",";
						}
						retHistorys=retHistorys.substring(0,retHistorys.length()-1);
					}
					else 
					{
						retError+="Error in sql:"+retSql;
					}
				}
				String retJsonS="{"
						+"\"History\":["+retHistorys+"],"
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
