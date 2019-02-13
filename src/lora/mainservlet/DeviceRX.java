package lora.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
		//��Ȩ
		LoginObj loginObj=Auth.auth(request);

		String userID=request.getParameter("userID");		//�û�ID
		String devEui=request.getParameter("devEui");		//�豸ID
		String pull_mode=request.getParameter("pull_mode");	//����ģʽ
		String t=request.getParameter("t");			//ʱ��
		String st=request.getParameter("st");		//��ʼʱ��
		String et=request.getParameter("et");		//����ʱ��
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String errorReturn="CreateNullError";
		ArrayList<String> urlList=new ArrayList<>();
		
		//�˻���Ȩ���Ƿ���ƽ̨�û�
		if(!loginObj.getLoginSta())
		{
			errorReturn+="ERROR Incorrect username or password.";
		}
		
		Boolean inputFormat=false;
		//�жϽڵ�ID�ǲ���16λ��hex��
		if(devEui.matches("^[a-f0-9]{16}")&&loginObj.getLoginSta())
		{//�����
			inputFormat=true;
		}
		else
		{//����
			errorReturn+="Your NodeID is not up to standard.";
		}
		
		//�Ƿ�ӵ�нڵ�
		String rets=sqlOp.hasManageNode(userID,devEui);
		if(rets.substring(0,1).equals("e"))
		{//�����쳣
			errorReturn+="e:Error of hasManaSQL "+rets+".";
		}else if(rets.equals("1"))
		{//��Ȩ��
			//�����������͵��ַ�����
			//��ȡ�ַ������б�
			String[] ips =sqlOp.getDisServIP();
			if(ips[0].equals("e"))
			{//��ȡ�쳣
				errorReturn+="e:getServerIPError"+ips[1];
			}
			else
			{
				Map<String, String> data=new HashMap<String,String>();
				data.put("insID", "");
				data.put("userID",userID);
				data.put("operKey","");
				data.put("devEui",devEui);
				if(pull_mode.equals("1"))
				{//��ȡģʽ1
					data.put("hwOPT","deviceRX1");
					data.put("t", t);
				}else if(pull_mode.equals("2"))
				{//��ȡģʽ2
					data.put("hwOPT","deviceRX2");
					data.put("st", st);
					data.put("et", et);
				}
				
				//�趨ָ���
				String errormsg="";
				for(int i=0;i<ips.length;i++)
				{
					try
					{
						String distReturn="e:deisReturnCreateNone";
						if(DeviceADD.devMode)
						{
							distReturn=UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/setIns", data);//���Ծ������
						}else
						{
							distReturn=UrlApi.urltoDist("http://"+ips[i]+":8090/LoRaServletTest/setIns", data);//���о������
						}
						//�趨ָ����֮������ָ��
						if(!distReturn.substring(0,1).equals("e"))
						{
							urlList.add(distReturn);
						}else
						{
							errormsg+=distReturn+" ";
						}
					}
					catch (Exception e) 
					{
						e.printStackTrace();
						errorReturn+="DistError"+e.getMessage();
					}
				}
			}
		}else if(rets.equals("0"))
		{//��Ȩ��
			errorReturn+="You do not own the node!";
		}
		
		//����Json��
		errorReturn=errorReturn.replace("CreateNullError", "");
		if(errorReturn.equals(""))
		{//���û�д���
			
		}
		else
		{//���������
			
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
