package lora.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import web.loginVerify.LoginObj;
import web.loginVerify.LoginVerfication;
import web.sqloperation.SqlOp;

/**
 * Servlet implementation class DeviceADD
 */
public class DeviceADD extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DeviceADD() {
        super();
        // TODO Auto-generated constructor stub
        sqlOp=new SqlOp();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //用于增加列表下的节点
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();
		LoginVerfication loginVerfication=new LoginVerfication();
		LoginObj loginObj=loginVerfication.veriLogin(request.getParameter("userID"),request.getParameter("pwd"));
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		//判断用户信息
		if(loginObj.getLoginSta())
		{
			//通过后调用所有的分服务器的添加url
			String[] ips = null;//sqlOp.getDistServIP();
			if(ips[0].equals("e"))
			{//获取异常
				retJ=jsonParser.parse("").getAsJsonObject();
			}
			else
			{//获取成功
				
			}
			
		}
		else
		{//账户不通过
			//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
			retJ=jsonParser.parse("{\"success\":\"failed\",\"error\":\"Your account does not have permission!\",\"doCount\":\"-1\",\"doFServer\":\"-1\"}").getAsJsonObject();
		}
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
