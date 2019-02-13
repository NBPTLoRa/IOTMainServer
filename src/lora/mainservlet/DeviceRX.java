package lora.mainservlet;

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
 * Servlet implementation class DeviceRX
 */
public class DeviceRX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DeviceRX() {
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

		//鉴权
		LoginObj loginObj=Auth.auth(request);

		
		String devEui=request.getParameter("devEui");		//设备ID
		String pull_mode=request.getParameter("pull_mode");	//请求模式
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String errorReturn="CreateNullError";
		
		
		//账户鉴权：是否是平台用户
		if(!loginObj.getLoginSta())
		{
			errorReturn="ERROR Incorrect username or password";
		}
		
		Boolean inputFormat=false;
		//判断节点ID是不是16位的hex码
		if(devEui.matches("^[a-f0-9]{16}")&&loginObj.getLoginSta())
		{//如果是
			inputFormat=true;
		}
		else
		{//报错
			errorReturn="Your NodeID is not up to standard";
		}
		
		
		
		
		//返回Json↓
		if(errorReturn.equals("CreateNullError"))
		{//如果没有错误
			
		}
		else
		{//如果出错了
			
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
