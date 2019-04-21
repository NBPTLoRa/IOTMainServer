package lora.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
 * Servlet implementation class MulticastQueuePOST
 */
public class MulticastQueuePOST extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public MulticastQueuePOST() {
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
				String retSuccess="failed";
				String retError="CreateError";
				
				String data=request.getParameter("data");
				
				//鉴权
				LoginObj loginObj=Auth.tokenLogin(request);
				
				//返回的json
				JsonObject retJ=new JsonObject();
				JsonParser jsonParser=new JsonParser();
				
				//判断用户信息
				if(loginObj.getLoginSta())
				{//用户信息通过
					String[] ips=sqlOp.getDisServIP();
					if(ips[0].equals("e"))
					{
						retError+=ips[1];
					}
					else
					{
						//获取成功
						for(int i=0;i<ips.length;i++)
						{
							Map<String, String> dataP=new HashMap<String,String>();
							dataP.put("doOper", "multicast-group-queue-post");
							dataP.put("data", data);
							
							String distReturn="e:DistReturnCreate";
							try {
								if(DeviceADD.devMode)
								{
									distReturn=UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/do", dataP);//调试就用这个
								}else
								{
									distReturn=UrlApi.urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", dataP);//运行就用这个
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
								//如果报错:{"success":"failed","error":"0","doCount":"-1","doFServer":"-1"}
								retSuccess="failed";
								retError+=e.getMessage()+"-"+ips[i];
							}
							
							if(distReturn.substring(0, 1).equals("e"))
							{//如果分服务器报错
								retSuccess="failed";
								retError+="DistServerError"+distReturn;
							}else
							{//如果不报错
								retSuccess="success";
								retError="0";
							}
						}
					}
				}else
				{
					//账户不通过
					//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
					retSuccess="failed";
					retError+="Your account does not have permission!"+loginObj.getException();
				}
				retJ=jsonParser.parse("{\"success\":\""+retSuccess
						+"\",\"error\":\""+retError.replace("\"","#").replace("CreateError", "")//
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
