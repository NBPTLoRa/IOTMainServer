package lora.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
 * Servlet implementation class DeviceQueue
 */
public class DeviceQueue extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DeviceQueue() {
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
		String retSuccess="e:CreateSuccess";
		String retError="e:CreateError";
		String devEui=request.getParameter("devEui");
		String data=request.getParameter("data");
		String userID=request.getParameter("userID");
		
		//��Ȩ
		LoginObj loginObj=Auth.tokenLogin(request);
		
		//���ص�json
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		Boolean inputFormat=false;
		//�ж�ID�ǲ���16λ��hex��
		if(devEui.matches("^[a-f0-9]{16}"))
		{//�����
			inputFormat=true;
		}
		else
		{//����
			retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
			retError+="Your NodeID is not up to standard!";	//���صĴ�����Ϣ
		}
		//�ж��û���Ϣ
		if(loginObj.getLoginSta()&&inputFormat)
		{//�û���Ϣͨ��
			
			//�ж��Ƿ�ӵ�иýڵ�
			String tempS=sqlOp.hasManageNode(userID, devEui);
			if(tempS.equals("1"))
			{//ӵ��
				//��ȡ�ڵ����һ��ͨѶ�ķַ�������ַ
				String lastServer=sqlOp.getServerIPofDevEui(devEui);
				if(lastServer.equals("0"))
				{//���û�з��͹�����
					retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
					retError+="There is no data under this node. Use Method:DeviceRX first~";	//���صĴ�����Ϣ
				}else
				{
					String[] ips=sqlOp.getDisServIP();
					retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
					retError="No communicating server was found   ";	//���صĴ�����Ϣ
					for(int i=0;i<ips.length;i++)
					{//�ڷַ������б��в���
						if(lastServer.equals(ips[i]))
						{
							Map<String, String> dataP=new HashMap<String,String>();
							dataP.put("doOper", "queueadd");
							dataP.put("devEui", devEui);
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
							}catch (Exception e) {
								e.printStackTrace();
								//�������:{"success":"failed","error":"0","doCount":"-1","doFServer":"-1"}
								retSuccess="failed";
								retError+=e.getMessage()+"  "+ips[i];
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
							break;
						}
					}
				}
				
			}else if(tempS.equals("0"))
			{
				retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
				retError+="You do not own the gateway!";	//���صĴ�����Ϣ
			}else {
				retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
				retError+=tempS;	//���صĴ�����Ϣ
			}
		}else
		{
			//�˻���ͨ��
			//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
			retSuccess="failed";
			retError+="Your account does not have permission!"+loginObj.getException();
		}
		retJ=jsonParser.parse("{\"success\":\""+retSuccess
				+"\",\"error\":\""+retError.replace("\"","#").replace("CreateNull", "")//
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
