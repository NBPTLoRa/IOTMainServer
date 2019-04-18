package lora.mainservlet;

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
 * Servlet implementation class DeviceList
 */
public class DeviceList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
	
    public DeviceList() {
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
		LoginObj loginObj=Auth.tokenLogin(request);

		String userID=request.getParameter("userID");		//用户ID
		String devEui=request.getParameter("devEui");		//设备ID
		String snCode=request.getParameter("snCode").toLowerCase();	//设备sn码
		String app=request.getParameter("app");				//设备类型
		String descrip=request.getParameter("descrip");		//设备备注
		String devName=request.getParameter("devName");		//设备名称
		
		//返回的json
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String retSuccess="failed";		//ADD是否完全成功
		String retError="CreateNull";	//返回的错误信息
		int retDoCount=-1;			//作用成功的服务器数量
		String retDoFServer="-1";		//作用失败的服务器IP
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
