package lora.mainservlet.datashow;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
 * Servlet implementation class EventPush
 */

public class EventPush extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public EventPush() {
        super();
        sqlOp=new SqlOp();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		
		LoginObj loginObj=Auth.tokenLogin(request);
		
		String retEvents="";
		String retError="CreateNull";
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		//判断用户鉴权
		Boolean authFlag=false;
		if(loginObj.getLoginSta())
		{
			authFlag=true;
		}else
		{
			retError+="Your account does not have permission!"+loginObj.getException();
		}
		try {
		if(authFlag)
		{
			Random r=new Random();
			//烟雾、安防//
			String[] name=new String[] {"烟雾","安防"};
			//ID 1000-4000
			//报警1 正常9
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");//设置日期格式

			for(int i=0;i<5;i++)
			{
				String warn="正常";
				String n=name[r.nextInt(2)];
				//if(r.nextInt(10)==1)
					//warn="报警";
				retEvents+="\"Event"+(i+1)+"\":\"";
				if(n.equals("安防"))
				{
					retEvents+="公共,";
				}else 
				{
					retEvents+=(r.nextInt(20)+1)+"号楼,";
				}
				retEvents+=n+(r.nextInt(3000)+1000)+","
				+warn+","
				+sdf.format(new Date())+"\",";
			}
		}
		}catch(Exception e)
		{
			retError+=e.toString();
		}
		String retJsonS="{"
				+retEvents
				+"\"error\":\""+retError.replace("\"","#").replace("CreateNull", "")
				+"\"}";
		retJ=jsonParser.parse(retJsonS).getAsJsonObject();
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
