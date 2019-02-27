package lora.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		//��Ȩ
		LoginObj loginObj=Auth.tokenLogin(request);

		String userID=request.getParameter("userID");		//�û�ID
		String devEui=request.getParameter("devEui");		//�豸ID
		String pull_mode=request.getParameter("pull_mode");	//����ģʽ
		String t=request.getParameter("t");			//ʱ��
		String et=request.getParameter("et");		//����ʱ��
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String errorReturn="CreateNullError";
		ArrayList<String> urlList=new ArrayList<>();

		String errormsg="";
		
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
		}
		else if(rets.equals("1")&&inputFormat)
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
				data.put("userID",userID);
				data.put("devEui",devEui);
				data.put("t", t);
				if(pull_mode.equals("1"))
				{//��ȡģʽ1
					data.put("hwOPT","deviceRX1");
				}else if(pull_mode.equals("2"))
				{//��ȡģʽ2
					data.put("hwOPT","deviceRX2");
					data.put("et", et);
				}
				
				//�趨ָ���
				String sLastTime="0";
				String sLastServer="0";
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd-HHmmss");
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
						
						String tempS=distReturn.split(";")[0];
						//��ȡ�ڸ÷ַ����������ݵ�ʱ��
						if(sLastTime.equals("0"))
						{//�����ʼS=0
							if(!tempS.substring(0,1).equals("e")&&!tempS.equals("0"))
							{//�һ�ȡ��ʱ�䲻��0�򱨴�
								sLastTime=tempS;
							}
						}
						else
						{//�����ʼsLastTime��ʱ��
							if((!tempS.substring(0,1).equals("e")&&!tempS.equals("0"))
									&&simpleDateFormat.parse(tempS).after(simpleDateFormat.parse(sLastTime)))
							{//�һ�ȡ��ʱ�䲻��0�򱨴�&&��ȡ��ʱ�����ʱ���
								sLastTime=tempS;
								sLastServer=ips[i];
							}
						}
						
						//�趨ָ����֮������ָ��
						if(!distReturn.substring(0,1).equals("e"))
						{
							if(DeviceADD.devMode)
							{
								urlList.add("http://localhost:8080/LoRaServletTest/do?"+distReturn.split(";")[1]);//���Ծ������
							}else
							{
								//���о������
								urlList.add("http://"+ips[i]+":8090/LoRaServletTest/do?"+distReturn.split(";")[1]);
							}
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

				sqlOp.setNodeLastCommu(devEui, sLastTime, sLastServer);
			}
		}else if(rets.equals("0"))
		{//��Ȩ��
			errorReturn+="You do not own the node!";
		}
		
		//����Json��
		errorReturn=errorReturn.replace("CreateNullError", "");
		if(errorReturn.equals(""))
		{//���û�д���
			String temp="{"
					+ "\"urlCount\":\""+urlList.size()+"\"";
			for(int i=1;i<urlList.size()+1;i++)
			{
				temp+=",\"url"+i+"\":"
						+ "\""+urlList.get(i-1)+"\"";
			}
			temp+= ",\"error\":"
					+"\""+errormsg+"\"}";
			retJ=jsonParser.parse(temp).getAsJsonObject();
			out.println(retJ);
			
		}
		else
		{//���������
			String temp="{"
					+ "\"error\":"
					+"\""+errorReturn+"\""
					+ "}";
			retJ=jsonParser.parse(temp).getAsJsonObject();
			out.println(retJ);
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
