package lora.mainservlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
 * Servlet implementation class GatewayDEL
 */
public class GatewayDEL extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public GatewayDEL() {
        super();
        // TODO Auto-generated constructor stub
        sqlOp=new SqlOp();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();

		LoginObj loginObj=Auth.auth(request);

		String userID=request.getParameter("userID");		//�û�ID
		String gatewayID=request.getParameter("gatewayID");	//����ID
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String retSuccess="failed";		//DEL�Ƿ���ȫ�ɹ�
		String retError="CreateNull";	//���صĴ�����Ϣ
		int retDoCount=-1;				//���óɹ��ķ���������
		String retDoFServer="-1";		//����ʧ�ܵķ�����IP
		
		Boolean inputFormat=false;
		//�ж�gatewayID�ǲ���16λ��hex��
		if(gatewayID.matches("^[a-f0-9]{16}"))
		{//�����
			inputFormat=true;
		}
		else
		{//����
			retSuccess="failed";	//DEL�Ƿ���ȫ�ɹ�
			retError+="Your NodeID is not up to standard!";	//���صĴ�����Ϣ��ID��ʽ����ȷ
		}
		//�ж��û���Ϣ
		if(loginObj.getLoginSta()&&inputFormat)
		{
			//�ж��û��Ƿ��и����ص�Ȩ��
			String rets=sqlOp.hasManageGateway(userID,gatewayID);
			if(rets.substring(0,1).equals("e"))
			{//�����쳣
			 //{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
				retSuccess="failed";
				retError+="e:Error of hasManaSQL "+rets;
			}else if(rets.equals("1"))
			{//��Ȩ��
				//ɾ��inWorkGateways���ڵļ�¼ 
				String retS=sqlOp.deleteGateway(gatewayID);
				
				if(!retS.equals("1"))
				{//��ӱ���
				//{"success":"failed","error":"Delete Gateway In Base ERROR","doCount":"-1","doFServer":"-1"}
					retSuccess="failed";
					retError+="Delete Gateway In Base ERROR"+retS;
				}
				
				//�������еķַ�������ɾ��url
				String[] ips =sqlOp.getDisServIP();
				if(ips[0].equals("e"))
				{//��ȡ�쳣
					//{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
					retSuccess="failed";
					retError+="e:getServerIPError"+ips[1];
				}
				else
				{//��ȡ�ַ������б�ɹ�
						//�趨д�뵽url��map
						Map<String, String> data=new HashMap<String,String>();
						data.put("doOper", "gatewayDEL");
						data.put("gatewayID",gatewayID);
						
						int sucServer=0;//�ɹ��ķַ���������
						//�����зַ�����ָ��
						for(int i=0;i<ips.length;i++)
						{
							
							try {
								String distReturn="e:deisReturnCreateNone";
								if(DeviceADD.devMode)
								{
									distReturn=urltoDist("http://localhost:8080/LoRaServletTest/do", data);//���Ծ������
								}else
								{
									distReturn=urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", data);//���о������
								}
								if(distReturn.substring(0, 1).equals("e"))
								{//����ַ���������
									//{"success":"failed","error":"***","doCount":"i��ֵ","doFServer":"��ǰ�ķַ�IP"}
									retSuccess="failed";
									retError+=distReturn;
									retDoCount=sucServer;
									retDoFServer+=","+ips[i];
								}else
								{//���������
									sucServer++;
									//{"success":"success","error":"0","doCount":"123","doFServer":"0"}
									retSuccess="success";
									retError+="0";
									retDoCount=sucServer;
								}
							} 
							catch (Exception e)
							{
								e.printStackTrace();
								//�������:{"success":"failed","error":"0","doCount":"-1","doFServer":"-1"}
								retSuccess="failed";
								retError+=e.getMessage();
								retDoFServer+=","+ips[i];
							}
						}
						
					}
			}else if(rets.equals("0"))
			{//��Ȩ��
				//{"success":"failed","error":"You do not own the node!","doCount":"-1","doFServer":"-1"}
				retSuccess="failed";
				retError+="You do not own the gateway!";
			}
			
		}
		else
		{//�˻���ͨ��
			//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
			retSuccess="failed";
			retError+="Your account does not have permission!"+loginObj.getException();
		}
		
		retJ=jsonParser.parse("{\"success\":\""+retSuccess
				+"\",\"error\":\""+retError.replace("\"","#")
				+"\",\"doCount\":\""+retDoCount
				+"\",\"doFServer\":\""+retDoFServer
				+"\"}").getAsJsonObject();
		out.println(retJ);
	}
	
	public String urltoDist(String url1, Map<String,String> data)throws Exception 
	{
		//�Ѳ���ƴ�ӵ�URL����
		url1+="?";
			for(Entry<String, String> vo : data.entrySet()){
			  	url1+=vo.getKey()+"="+vo.getValue()+"&";
			}
			  
		
		//����URL����
		URL url = new URL(url1);
		
		URLConnection connection=url.openConnection();
		
		InputStream in = connection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

		//���շ�����Ӧ��Ϣ
		String response = new String();
		try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[] = out.toByteArray();
        response=new String(b,"utf-8");

		return response.toString();

	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
