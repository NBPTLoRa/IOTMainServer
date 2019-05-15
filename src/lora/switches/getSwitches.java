package lora.switches;

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
 * Servlet implementation class getSwitches
 */
public class getSwitches extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public getSwitches() {
        super();
        sqlOp=new SqlOp();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();

		//鉴权
		LoginObj loginObj=Auth.tokenLogin(request);

		//返回的json
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String retLight="0";
		String retWar="0";
		String retLinkWar="0";
		String retError="CreateNull";	//返回的错误信息
		
		if(loginObj.getLoginSta())
		{
			String retSql=sqlOp.getSwitches();
			if(retSql.substring(0, 1).equals("e"))
			{//报错
				retError+=retSql;
			}else
			{
				retLight=retSql.split(":")[1].split(",")[0];
				retWar=retSql.split(":")[1].split(",")[1];
				retLinkWar=retSql.split(":")[1].split(",")[2];
			}
		}else
		{
			retError+=loginObj.getException();
		}
		
		retJ=jsonParser.parse("{"
				+ "\"Light\":\""+retLight+"\","
				+ "\"War\":\""+retWar+"\","
				+ "\"LinkWar\":\""+retLinkWar+"\","
				+"\"error\":\""+retError.replace("\"","#").replace("CreateNull", "")//
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
