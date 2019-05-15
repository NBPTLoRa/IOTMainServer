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
 * Servlet implementation class setLinkWar
 */
@WebServlet("/setLinkWar")
public class setLinkWar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public setLinkWar() {
        super();
        sqlOp=new SqlOp();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();

		//鉴权
		LoginObj loginObj=Auth.tokenLogin(request);

		String s=request.getParameter("s"); //开关值
		
		//返回的json
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String retSuccess="0";
		String retError="CreateNull";	//返回的错误信息
		
		if(loginObj.getLoginSta())
		{
			String retSql="e:create";
			if(s.equals("1"))
			{
				retSql=sqlOp.setLinkWar("1");
			}else if(s.equals("0"))
			{
				retSql=sqlOp.setLinkWar("0");
			}else
			{
				retSql="e:S:INPUT ERROR";
			}
			if(retSql.substring(0, 1).equals("e"))
			{//报错
				retError+=retSql;
			}else
			{
				retSuccess="1";
			}
		}else
		{
			retError+=loginObj.getException();
		}
		
		retJ=jsonParser.parse("{\"Success\":\""+retSuccess+"\","
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
