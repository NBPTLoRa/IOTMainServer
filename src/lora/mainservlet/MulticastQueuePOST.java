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
				
				//��Ȩ
				LoginObj loginObj=Auth.tokenLogin(request);
				
				//���ص�json
				JsonObject retJ=new JsonObject();
				JsonParser jsonParser=new JsonParser();
				
				//�ж��û���Ϣ
				if(loginObj.getLoginSta())
				{//�û���Ϣͨ��
					String[] ips=sqlOp.getDisServIP();
					if(ips[0].equals("e"))
					{
						retError+=ips[1];
					}
					else
					{
						//��ȡ�ɹ�
						for(int i=0;i<ips.length;i++)
						{
							Map<String, String> dataP=new HashMap<String,String>();
							dataP.put("doOper", "multicast-group-queue-post");
							dataP.put("data", data);
							
							String distReturn="e:DistReturnCreate";
							try {
								if(DeviceADD.devMode)
								{
									distReturn=UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/do", dataP);//���Ծ������
								}else
								{
									distReturn=UrlApi.urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", dataP);//���о������
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
								//�������:{"success":"failed","error":"0","doCount":"-1","doFServer":"-1"}
								retSuccess="failed";
								retError+=e.getMessage()+"-"+ips[i];
							}
							
							if(distReturn.substring(0, 1).equals("e"))
							{//����ַ���������
								retSuccess="failed";
								retError+="DistServerError"+distReturn;
							}else
							{//���������
								retSuccess="success";
								retError="0";
							}
						}
					}
				}else
				{
					//�˻���ͨ��
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
