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

/**
 * Servlet implementation class DeviceRXLast
 */
public class UplinkRXLast extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UplinkRXLast() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();

		LoginObj loginObj=Auth.tokenLogin(request);

		String userID=request.getParameter("userID");//用户ID
		String devList=request.getParameter("devList");//设备ID列表
		
		String retData="{0}";
		String retError="CreateNull";
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		if(devList==null)
		{
			devList="";
		}
		//判断节点列表是否符合标准
		Boolean inputFormat=true;
		String[] devListarr=devList.split("_");
		for(int i=0;i<devListarr.length;i++)
		{
			//判断ID是不是16位的hex码
			if(!devListarr[i].matches("^[a-f0-9]{16}"))
			{
				inputFormat=false;
				retError+="Your NodeID="+devListarr[i]+" is not up to standard!";	//返回的错误信息：ID格式不正确
			}
		}
		
		
		
		
		retJ=jsonParser.parse("{\"data\":["+retData
				+"],\"error\":\""+retError.replace("\"","#").replace("CreateNull", "")//
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
