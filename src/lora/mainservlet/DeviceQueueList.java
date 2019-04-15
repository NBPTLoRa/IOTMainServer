package lora.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import com.sun.org.apache.xml.internal.serializer.ToSAXHandler;

import lora.auth.Auth;
import web.loginVerify.LoginObj;
import web.sqloperation.SqlOp;

/**
 * Servlet implementation class DeviceQueueList
 */
public class DeviceQueueList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DeviceQueueList() {
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
		String retV="0";
		
		String devEui=request.getParameter("devEui");
		String userID=request.getParameter("userID");
		
		//鉴权
		LoginObj loginObj=Auth.tokenLogin(request);
		
		//返回的json
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		Boolean inputFormat=false;
		//判断ID是不是16位的hex码
		if(devEui.matches("^[a-f0-9]{16}"))
		{//如果是
			inputFormat=true;
		}
		else
		{//报错
			retSuccess="failed";		//ADD是否完全成功
			retError+="Your NodeID is not up to standard!";	//返回的错误信息
		}
		//判断用户信息
		if(loginObj.getLoginSta()&&inputFormat)
		{//用户信息通过
			
			//判断是否拥有该节点
			String tempS=sqlOp.hasManageNode(userID, devEui);
			if(tempS.equals("1"))
			{//拥有
				//获取节点最后一次通讯的分服务器地址
				String lastServer=sqlOp.getServerIPofDevEui(devEui);
				if(lastServer.equals("0"))
				{//如果没有发送过数据
					retSuccess="failed";		//ADD是否完全成功
					retError+="There is no data under this node. Use Method:DeviceRX first~";	//返回的错误信息
				}else
				{
					String[] ips=sqlOp.getDisServIP();
					retSuccess="failed";		//ADD是否完全成功
					retError="No communicating server was found   ";	//返回的错误信息
					for(int i=0;i<ips.length;i++)
					{//在分服务器列表中查找
						if(lastServer.equals(ips[i]))
						{
							Map<String, String> dataP=new HashMap<String,String>();
							dataP.put("doOper", "queueget");
							dataP.put("devEui", devEui);
							String distReturn="e:DistReturnCreate";
							try {
							if(DeviceADD.devMode)
							{
								distReturn=UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/do", dataP);//调试就用这个
							}else
							{
								distReturn=UrlApi.urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", dataP);//运行就用这个
							}
							}catch (Exception e) {
								e.printStackTrace();
								retSuccess="failed";
								retError+=e.getMessage()+"  "+ips[i];
							}
							if(distReturn.substring(0, 1).equals("e"))
							{//如果分服务器报错
								retSuccess="failed";
								retError+="DistServerError"+distReturn;
							}else
							{//如果不报错
								retSuccess="success";
								distReturn=toStringHex(distReturn);
								retV=distReturn.split("\\[")[1].split("\\]")[0];
								if(retV.equals(""))
								{
									retV="0";
								}else
								{
									retV=strTo16(retV);
								}
							}
							break;
						}
					}
				}
				
			}else if(tempS.equals("0"))
			{
				retSuccess="failed";		//ADD是否完全成功
				retError+="You do not own the node!";	//返回的错误信息
			}else {
				retSuccess="failed";		//ADD是否完全成功
				retError+=tempS;	//返回的错误信息
			}
		}else
		{
			//账户不通过
			//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
			retSuccess="failed";
			retError+="Your account does not have permission!"+loginObj.getException();
		}
		retJ=jsonParser.parse("{\"success\":\""+retSuccess
				+"\",\"value\":\""+retV
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
	
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			s="e:"+e1.toString();
		}
		return s;
	}

    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
}
