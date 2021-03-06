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
import web.md5.MD5Utils;
import web.sqloperation.SqlOp;

/**
 * Servlet implementation class DeviceADD
 */
public class DeviceADD extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//开发模式，如果调试就写true
    public static Boolean devMode=false;
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DeviceADD() {
        super();
        // TODO Auto-generated constructor stub
        sqlOp=new SqlOp(); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //用于增加列表下的节点
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();

		//判断字段输入是否完整
		boolean inputF=true;
		 /*
		if(userID==null||devEui==null||snCode==null||app==null||descrip==null||devName==null||appKey==null||nwkKey==null)
		{
			retError+="e:Incomplete field entry!!!!";
		}else
		{
			inputF=true;
		}
		*/
		
		//鉴权
		LoginObj loginObj=Auth.tokenLogin(request);

		String userID=request.getParameter("userID");		//用户ID
		String devEui=request.getParameter("devEui");		//设备ID
		String snCode=request.getParameter("snCode").toLowerCase();	//设备sn码
		String app=request.getParameter("app");				//设备类型
		String descrip=request.getParameter("descrip");		//设备备注
		String devName=request.getParameter("devName");		//设备名称
		String appKey=request.getParameter("appKey");		//三元组秘钥1
		String nwkKey=request.getParameter("nwkKey");		//三元组秘钥3

		//返回的json
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String retSuccess="failed";		//ADD是否完全成功
		String retError="CreateNull";	//返回的错误信息
		int retDoCount=-1;			//作用成功的服务器数量
		String retDoFServer="-1";		//作用失败的服务器IP

		Boolean inputFormat=false;
		//判断ID是不是16位的hex码
		if(devEui.matches("^[a-fA-F0-9]{16}"))
		{//如果是
			inputFormat=true;
		}
		else
		{//报错
			retSuccess="failed";		//ADD是否完全成功
			retError+="e:Your NodeID is not up to standard!";	//返回的错误信息
			retDoCount=-1;			//作用成功的服务器数量
			retDoFServer="0";		//作用失败的服务器IP
		}
		//判断用户信息
		if(loginObj.getLoginSta()&&inputFormat&&inputF)
		{
			//通过后调用所有的分服务器的添加url
			String[] ips =sqlOp.getDisServIP();
			if(ips[0].equals("e"))
			{//获取异常
				//{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
				retSuccess="failed";
				retError+="e:getServerIPError"+ips[1];
				retDoCount=0;
				retDoFServer="0";
			}
			else
			{//获取分服务器列表成功
				//校验sn码
				if(MD5Utils.getSaltMD5ofDevice(devEui).toLowerCase().equals(snCode))
				{//sn码通过
					//设定写入到url的map
					Map<String, String> data=new HashMap<String,String>();
					data.put("doOper", "deviceADD");
					data.put("devEui",devEui);
					//通过总服务器的app获取profName
					String ProfName=sqlOp.getProfNameforappName(app);
					data.put("ProfName", ProfName);
					data.put("descrip", descrip);
					data.put("devName", devName);
					data.put("userID", userID);
					data.put("appKey","077ee45c6e4564d96d76ae55afd3aa89");
					data.put("nwkKey","077ee45c6e4564d96d76ae55afd3aa89");
					
					int sucServer=0;//成功的分服务器数量
					//向所有分服发送指令
					for(int i=0;i<ips.length;i++)
					{
						
						try {
							String distReturn="e:deisReturnCreateNone";
							Map<String, String> dataDel=new HashMap<String,String>();
							dataDel.put("doOper", "deviceDEL");
							dataDel.put("devEui",devEui);
							dataDel.put("userID", "Admin");
							if(DeviceADD.devMode)//先删除再添加
							{
								UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/do", dataDel);
								distReturn=UrlApi.urltoDist("http://localhost:8080/LoRaServletTest/do", data);//调试就用这个
							}else
							{
								UrlApi.urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", dataDel);
								distReturn=UrlApi.urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", data);//运行就用这个
							}
							if(distReturn.substring(0, 1).equals("e"))
							{//如果分服务器报错 
								//{"success":"failed","error":"***","doCount":"i的值","doFServer":"当前的分服IP"}
								retSuccess="failed";
								retError+=distReturn;
								retDoCount=sucServer;
								retDoFServer+=","+ips[i];
							}else
							{//如果不报错
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
							//如果报错:{"success":"failed","error":"0","doCount":"-1","doFServer":"-1"}
							retSuccess="failed";
							retError+=e.getMessage();
							retDoFServer+=","+ips[i];
						}
					}
					//删除数据库内的记录--
					sqlOp.deleteNode(devEui);
					//在向所有分服务器发送完之后在总服务器的inWorkNodes加数据
					String RetS="makeWorkForNode CreateError";
					if(retDoCount!=-1)
					{
						RetS=sqlOp.makeWorkForNode(devEui,userID,devName);
					}
					if(!RetS.equals("1"))
					{//添加报错
						//{"success":"failed","error":"e:Make Work In Base ERROR","doCount":"-1","doFServer":"-1"}
						retSuccess="failed";
						retError+="Make Work In Base ERROR:"+RetS;
					}
				}
				else
				{//sn码不匹配
					//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
					retSuccess="failed";
					retError+="Your SnCode does not have permission!";
					retDoCount=-1;
					retDoFServer="-1";
				}
			}
			
		}
		else
		{//账户不通过
			//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
			retSuccess="failed";
			retError+="Your account does not have permission!"+loginObj.getException();
			retDoCount=-1;
			retDoFServer="-1";
		}
		
		retJ=jsonParser.parse("{\"success\":\""+retSuccess
				+"\",\"error\":\""+retError.replace("\"","#").replace("CreateNull", "")//
				+"\",\"doCount\":\""+retDoCount
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

}
