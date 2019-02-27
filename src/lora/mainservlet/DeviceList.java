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
		//鉴权
		LoginObj loginObj=Auth.tokenLogin(request);

		String userID=request.getParameter("userID");		//用户ID
		String devEui=request.getParameter("devEui");		//设备ID
		String pull_mode=request.getParameter("pull_mode");	//请求模式
		String t=request.getParameter("t");			//时间
		String et=request.getParameter("et");		//结束时间
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String errorReturn="CreateNullError";
		ArrayList<String> urlList=new ArrayList<>();

		String errormsg="";
		
		//账户鉴权：是否是平台用户
		if(!loginObj.getLoginSta())
		{
			errorReturn+="ERROR Incorrect username or password.";
		}
		
		Boolean inputFormat=false;
		//判断节点ID是不是16位的hex码
		if(devEui.matches("^[a-f0-9]{16}")&&loginObj.getLoginSta())
		{//如果是
			inputFormat=true;
		}
		else
		{//报错
			errorReturn+="Your NodeID is not up to standard.";
		}
		
		//是否拥有节点
		String rets=sqlOp.hasManageNode(userID,devEui);
		if(rets.substring(0,1).equals("e"))
		{//出现异常
			errorReturn+="e:Error of hasManaSQL "+rets+".";
		}
		else if(rets.equals("1")&&inputFormat)
		{//有权限
			//把数据请求发送到分服务器
			//获取分服务器列表
			String[] ips =sqlOp.getDisServIP();
			if(ips[0].equals("e"))
			{//获取异常
				errorReturn+="e:getServerIPError"+ips[1];
			}
			else
			{
				Map<String, String> data=new HashMap<String,String>();
				data.put("userID",userID);
				data.put("devEui",devEui);
				data.put("t", t);
				if(pull_mode.equals("1"))
				{//获取模式1
					data.put("hwOPT","deviceRX1");
				}else if(pull_mode.equals("2"))
				{//获取模式2
					data.put("hwOPT","deviceRX2");
					data.put("et", et);
				}
				
				//设定指令部分
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
							distReturn=UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/setIns", data);//调试就用这个
						}else
						{
							distReturn=UrlApi.urltoDist("http://"+ips[i]+":8090/LoRaServletTest/setIns", data);//运行就用这个
						}
						
						String tempS=distReturn.split(";")[0];
						//获取在该分服务器的数据的时间
						if(sLastTime.equals("0"))
						{//如果初始S=0
							if(!tempS.substring(0,1).equals("e")&&!tempS.equals("0"))
							{//且获取的时间不是0或报错
								sLastTime=tempS;
							}
						}
						else
						{//如果初始sLastTime是时间
							if((!tempS.substring(0,1).equals("e")&&!tempS.equals("0"))
									&&simpleDateFormat.parse(tempS).after(simpleDateFormat.parse(sLastTime)))
							{//且获取的时间不是0或报错&&获取的时间比老时间大
								sLastTime=tempS;
								sLastServer=ips[i];
							}
						}
						
						//设定指令完之后生成指令
						if(!distReturn.substring(0,1).equals("e"))
						{
							if(DeviceADD.devMode)
							{
								urlList.add("http://localhost:8080/LoRaServletTest/do?"+distReturn.split(";")[1]);//调试就用这个
							}else
							{
								//运行就用这个
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
		{//无权限
			errorReturn+="You do not own the node!";
		}
		
		//返回Json↓
		errorReturn=errorReturn.replace("CreateNullError", "");
		if(errorReturn.equals(""))
		{//如果没有错误
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
		{//如果出错了
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
