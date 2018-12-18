package web.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import web.sqloperation.SqlOp;

/**
 * Servlet implementation class LoginEntrance
 */
public class LoginEntrance extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginEntrance() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String userID;
		String pwd;
		SqlOp sqlOp=new SqlOp();
		JsonObject retJ=new JsonObject();
		try
		{
			userID=request.getParameter("userID");
			pwd=request.getParameter("pwd");
			String loginFlag="e:create no catch";
			//校对数据库↓
			loginFlag=sqlOp.login(userID,pwd);
			//判断是否通过
			if(loginFlag.substring(0, 1).equals("1"))
			{
				//校对成功返回{"login":"success","error":"0","classfy":"******"}
				retJ=new JsonParser().parse("{\"login\":\"success\",\"error\":\"0\",\"classfy\":\""+loginFlag.substring(2)+"\"}").getAsJsonObject();
			}
			else if(loginFlag.substring(0, 1).equals("0"))
			{
				//校对失败返回{"login":"failed","error":"用户名或密码不正确","classfy":"-1"}
				retJ=new JsonParser().parse("{\"login\":\"failed\",\"error\":\""+"The userID or password incorrect!"+"\",\"classfy\":\"-1\"}").getAsJsonObject();
			}
			else 
			{
				//校对失败并且SQL内部异常
				retJ=new JsonParser().parse("{\"login\":\"error\",\"error\":\"SQLE----"+loginFlag+"\",\"classfy\":\"-1\"}").getAsJsonObject();
			}
		}
		catch (Exception e) {
			//报错：
			//返回：{"login":"error","error":"e.toString()"}
			retJ=new JsonParser().parse("{\"login\":\"error\",\"error\":\""+e.toString()+"\"}").getAsJsonObject();
		}finally {
			out.println(retJ);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
