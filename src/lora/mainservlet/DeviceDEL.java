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
 * Servlet implementation class DeviceDEL
 */
public class DeviceDEL extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DeviceDEL() {
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

				LoginVerfication loginVerfication=new LoginVerfication();
				
				//用户名密码鉴权
				String userID=request.getParameter("userID");
				LoginObj loginObj=loginVerfication.veriLogin(userID,request.getParameter("pwd"));
				
				String devEui=request.getParameter("devEui");//设备ID
				
				JsonObject retJ=new JsonObject();
				JsonParser jsonParser=new JsonParser();
				
				String retSuccess="failed";		//ADD是否完全成功
				String retError="CreateNull";	//返回的错误信息
				String retDoCount="-1";			//作用成功的服务器数量
				String retDoFServer="-1";		//作用失败的服务器IP
				
				Boolean inputFormat=false;
				//判断snCode是不是16位的hex码
				if(devEui.matches("^[a-f0-9]{16}"))
				{//如果是
					inputFormat=true;
				}
				else
				{//报错
					retSuccess="failed";	//ADD是否完全成功
					retError+="Your NodeID is not up to standard!";	//返回的错误信息：ID格式不正确
				}
				//判断用户信息
				if(loginObj.getLoginSta()&&inputFormat)
				{
					//判断用户是否有该节点的权限
					String rets=sqlOp.hasManageNode(userID,devEui);
					if(rets.substring(0,1).equals("e"))
					{//出现异常
					 //{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
						retSuccess="failed";
						retError+="e:Error of hasManaSQL "+rets;
					}else if(rets.equals("1"))
					{//有权限
						//删除inWorkNodes库内的记录 
						String retS=sqlOp.deleteNode(devEui);
						
						if(!retS.equals("1"))
						{//添加报错
						//{"success":"failed","error":"Delete Node In Base ERROR","doCount":"-1","doFServer":"-1"}
							retSuccess="failed";
							retError+="Delete Node In Base ERROR"+retS;
						}
						
						//调用所有的分服务器的删除url
						String[] ips =sqlOp.getDisServIP();
						if(ips[0].equals("e"))
						{//获取异常
							//{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
							retSuccess="failed";
							retError+="e:getServerIPError"+ips[1];
						}
						else
						{//获取分服务器列表成功
								//设定写入到url的map
								Map<String, String> data=new HashMap<String,String>();
								data.put("doOper", "deviceDEL");
								data.put("devEui",devEui);
								
								int sucServer=0;//成功的分服务器数量
								//向所有分服发送指令
								for(int i=0;i<ips.length;i++)
								{
									
									try {
										String distReturn=urltoDist("http://"+ips[i]+":8090/LoRaServletTest/do", data);//生产模式
										//String distReturn=urltoDist("http://localhost:8080/LoRaServletTest/do", data);
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
								
							}
					}else if(rets.equals("0"))
					{//无权限
						//{"success":"failed","error":"You do not own the node!","doCount":"-1","doFServer":"-1"}
						retSuccess="failed";
						retError+="You do not own the node!";
					}
					
				}
				else
				{//账户不通过
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
