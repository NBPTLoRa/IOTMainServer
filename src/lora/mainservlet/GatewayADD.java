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

import web.loginVerify.LoginObj;
import web.loginVerify.LoginVerfication;
import web.md5.MD5Utils;
import web.sqloperation.SqlOp;

/**
 * Servlet implementation class GatewayADD
 */
public class GatewayADD extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public GatewayADD() {
        super();
        // TODO Auto-generated constructor stub
        sqlOp=new SqlOp();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter out=response.getWriter();
					
				LoginVerfication loginVerfication=new LoginVerfication();
				String userID=request.getParameter("userID");
				LoginObj loginObj=loginVerfication.veriLogin(userID,request.getParameter("pwd"));
				
				String gatewayID=request.getParameter("gatewayID");		//����ID
				String snCode=request.getParameter("snCode").toLowerCase();	//�豸sn��
				String descrip=request.getParameter("descrip");			//�豸��ע
				String devName=request.getParameter("devName");			//�豸����
				
				
				JsonObject retJ=new JsonObject();
				JsonParser jsonParser=new JsonParser();
				
				String retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
				String retError="CreateNull";	//���صĴ�����Ϣ
				int retSucServer=-1;//�ɹ��ķַ���������
				String retDoFServer="-1";		//����ʧ�ܵķ�����IP
				
				Boolean inputFormat=false;
				//�ж�ID�ǲ���16λ��hex��
				if(gatewayID.matches("^[a-f0-9]{16}"))
				{//�����
					inputFormat=true;
				}
				else
				{//����
					retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
					retError+="e:Your NodeID is not up to standard!";	//���صĴ�����Ϣ
					retSucServer=0;			//���óɹ��ķ���������
					retDoFServer="0";		//����ʧ�ܵķ�����IP
				}
				//�ж��û���Ϣ
				if(loginObj.getLoginSta()&&inputFormat)
				{
					//ͨ����������еķַ�����������url
					String[] ips =sqlOp.getDisServIP();
					if(ips[0].equals("e"))
					{//��ȡ�쳣
						//{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
						retSuccess="failed";
						retError+="e:getServerIPError"+ips[1];
						retSucServer=0;
						retDoFServer="0";
					}
					else
					{//��ȡ�ַ������б��ɹ�
						//У��sn��
						if(MD5Utils.getSaltMD5ofGate(gatewayID).toLowerCase().equals(snCode))
						{//sn��ͨ��
							//�趨д�뵽url��map
							Map<String, String> data=new HashMap<String,String>();
							data.put("doOper", "gatewayADD");
							data.put("gatewayID",gatewayID);
							data.put("descrip", descrip);
							data.put("devName", devName);
							
							//�����зַ�����ָ��
							for(int i=0;i<ips.length;i++)
							{
								try {
									String distReturn=urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", data);//���о������
									//String distReturn=urltoDist("http://localhost:8080/LoRaServletTest/do", data);//���Ծ������
									if(distReturn.substring(0, 1).equals("e"))
									{//����ַ��������� 
										//{"success":"failed","error":"***","doCount":"i��ֵ","doFServer":"��ǰ�ķַ�IP"}
										retSuccess="failed";
										retError+=distReturn;
										retDoFServer+=","+ips[i];
									}else
									{//���������
										retSucServer++;
										//{"success":"success","error":"0","doCount":"123","doFServer":"0"}
										retSuccess="success";
										retError+="0";
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
							//�������зַ�����������֮�����ܷ�������inWorkGateway������
							String RetS="makeWorkForGateway CreateError";
							if(retSucServer!=-1)
							{
								RetS=sqlOp.makeWorkGateway(gatewayID,userID);
							}
							if(!RetS.equals("1"))
							{//���ӱ���
								//{"success":"failed","error":"e:Make Work In Base ERROR","doCount":"-1","doFServer":"-1"}
								retSuccess="failed";
								retError+="Make Work In Base ERROR:"+RetS;
							}
						}
						else
						{//sn�벻ƥ��
							//{"success":"failed","error":"Your SnCode does not have permission!","doCount":"-1","doFServer":"-1"}
							retSuccess="failed";
							retError+="Your SnCode does not have permission!";
							retSucServer=-1;
							retDoFServer="-1";
						}
					}
					
				}
				else
				{//�˻���ͨ��
					//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
					retSuccess="failed";
					retError+="Your account does not have permission!"+loginObj.getException();
					retSucServer=-1;
					retDoFServer="-1";
				}
				
				retJ=jsonParser.parse("{\"success\":\""+retSuccess
						+"\",\"error\":\""+retError
						+"\",\"doCount\":\""+retSucServer
						+"\",\"doFServer\":\""+retDoFServer
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
}