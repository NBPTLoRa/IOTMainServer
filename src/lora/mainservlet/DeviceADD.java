package lora.mainservlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
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
 * Servlet implementation class DeviceADD
 */
public class DeviceADD extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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

		LoginVerfication loginVerfication=new LoginVerfication();
		String userID=request.getParameter("userID");
		LoginObj loginObj=loginVerfication.veriLogin(userID,request.getParameter("pwd"));
		
		String devEui=request.getParameter("devEui");		//设备ID
		String snCode=request.getParameter("snCode").toLowerCase();	//设备sn码
		String app=request.getParameter("app");				//设备类型
		String descrip=request.getParameter("descrip");			//设备备注
		String devName=request.getParameter("devName");			//设备名称
		
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String retSuccess="failed";		//ADD是否完全成功
		String retError="CreateNull";	//返回的错误信息
		String retDoCount="-1";			//作用成功的服务器数量
		String retDoFServer="-1";		//作用失败的服务器IP
		
		Boolean inputFormat=false;
		//判断ID是不是16位的hex码
		if(devEui.matches("^[a-f0-9]{16}"))
		{//如果是
			inputFormat=true;
		}
		else
		{//报错
			retSuccess="failed";		//ADD是否完全成功
			retError+="e:Your NodeID is not up to standard!";	//返回的错误信息
			retDoCount="0";			//作用成功的服务器数量
			retDoFServer="0";		//作用失败的服务器IP
		}
		//判断用户信息
		if(loginObj.getLoginSta()&&inputFormat)
		{
			//通过后调用所有的分服务器的添加url
			String[] ips =sqlOp.getDisServIP();
			if(ips[0].equals("e"))
			{//获取异常
				//{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
				retSuccess="failed";
				retError+="e:getServerIPError"+ips[1];
				retDoCount="0";
				retDoFServer="0";
			}
			else
			{//获取分服务器列表成功
				//校验sn码
				if(MD5Utils.getSaltMD5(devEui).toLowerCase().equals(snCode))
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
					
					int sucServer=0;//成功的分服务器数量
					//向所有分服发送指令
					for(int i=0;i<ips.length;i++)
					{
						
						try {
							String distReturn=urltoDist("http://"+ips[i]+":8080/LoRaServletTest/do", data);//运行就用这个
							//String distReturn=urltoDist("http://localhost:8080/LoRaServletTest/do", data);//调试就用这个
							if(distReturn.substring(0, 1).equals("e"))
							{//如果分服务器报错 
								//{"success":"failed","error":"***","doCount":"i的值","doFServer":"当前的分服IP"}
								retSuccess="failed";
								retError+=distReturn;
								retDoCount=sucServer+"";
								retDoFServer+=","+ips[i];
							}else
							{//如果不报错
								sucServer++;
								//{"success":"success","error":"0","doCount":"123","doFServer":"0"}
								retSuccess="success";
								retError+="0";
								retDoCount=sucServer+"";
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
					//在向所有分服务器发送完之后在总服务器的inWorkNodes加数据
					String RetS=sqlOp.makeWorkForNode(devEui,userID);
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
					retDoCount="-1";
					retDoFServer="-1";
				}
			}
			
		}
		else
		{//账户不通过
			//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
			retSuccess="failed";
			retError+="Your account does not have permission!"+loginObj.getException();
			retDoCount="-1";
			retDoFServer="-1";
		}
		
		retJ=jsonParser.parse("{\"success\":\""+retSuccess
				+"\",\"error\":\""+retError
				+"\",\"doCount\":\""+retDoCount
				+"\",\"doFServer\":\""+retDoFServer
				+"\"}").getAsJsonObject();
		out.println(retJ);
	}

	public String urltoDist(String url1, Map<String,String> data)throws Exception 
	{
		//把参数拼接到URL后面
		url1+="?";
			for(Entry<String, String> vo : data.entrySet()){
			  	url1+=vo.getKey()+"="+vo.getValue()+"&";
			}
			  
		
		//创建URL对象
		URL url = new URL(url1);
		
		URLConnection connection=url.openConnection();
		
		InputStream in = connection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

		//接收返回响应信息
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
