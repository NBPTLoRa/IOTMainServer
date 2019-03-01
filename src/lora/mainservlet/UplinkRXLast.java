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
 * Servlet implementation class DeviceRXLast
 */
public class UplinkRXLast extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public UplinkRXLast() {
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

		LoginObj loginObj=Auth.tokenLogin(request);

		String userID=request.getParameter("userID");//�û�ID
		String devList=request.getParameter("devList");//�豸ID�б�
		
		String retData="{0}";
		String retError="CreateNull";
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		if(devList==null)
		{
			devList="";
		}
		//�жϽڵ��б��Ƿ���ϱ�׼
		Boolean inputFormat=true;
		String[] devListarr=devList.split("_");
		for(int i=0;i<devListarr.length;i++)
		{
			//�ж�ID�ǲ���16λ��hex��
			if(!devListarr[i].matches("^[a-f0-9]{16}"))
			{
				inputFormat=false;
				retError+="Your NodeID="+devListarr[i]+" is not up to standard!";	//���صĴ�����Ϣ��ID��ʽ����ȷ
			}
		}
		//�ж��û���Ȩ
		Boolean authFlag=false;
		if(loginObj.getLoginSta())
		{
			authFlag=true;
		}else
		{
			retError+="Your account does not have permission!"+loginObj.getException();
		}
		
		//�жϽڵ��Ƿ�ȫ�����û�
		Boolean manegeFlag=false;
		if(authFlag)
		{
			manegeFlag=true;
			for(int i=0;i<devListarr.length;i++)
			{
				if(!sqlOp.hasManageNode(userID, devListarr[i]).equals("1"))
				{//�������û�
					manegeFlag=false;
					retError+="You do not own the node:"+devListarr[i]+"!"+loginObj.getException();
				}
			}
		}
		
		//������
		if(manegeFlag)
		{
			for(int i=0;i<devListarr.length;i++)
			{
				String lastServer=sqlOp.getServerIPofDevEui(devListarr[i]);
				if(!(lastServer.substring(0, 1).equals("0"))&&!(lastServer.substring(0, 1).equals("e")))
				{//ȡIP�������Ҳ���
					Map<String, String> data=new HashMap<String,String>();
					data.put("devEui",devListarr[i]);
					data.put("doOper", "getUplinkLastPackage");
					String distReturn="e:deisReturnCreateNone";
					try
					{
						if(DeviceADD.devMode)
						{
							distReturn=UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/setIns", data);//���Ծ������
						}else
						{
							distReturn=UrlApi.urltoDist("http://"+lastServer+":8090/LoRaServletTest/setIns", data);//���о������
						}
						
						if(distReturn.equals("00"))
						{//û����
							
						}else if(distReturn.substring(0, 2).equals("e:"))
						{//����
							
						}else
						{//����
							
						}
					}
					catch(Exception e)
					{
						devListarr[i]+=":error";
						retError+=devListarr[i]+":"+e.getMessage();
					}
				}
				else
				{
					retError+="";;
				}
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
