package lora.mainservlet.datashow;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.print.attribute.standard.DateTimeAtCompleted;
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
		
		//�ж��û���Ȩ
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
			//�����شš�����
			String[] name=new String[] {"����","�ش�","����"};
			//ID 1000-4000
			//����1 ����9
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//�������ڸ�ʽ
			Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�

			int hour = c.get(Calendar.HOUR_OF_DAY); 
			int minute = c.get(Calendar.MINUTE); 
			int second = c.get(Calendar.SECOND);
			
			DecimalFormat df=new DecimalFormat("00");
			for(int i=0;i<5;i++)
			{
				String warn="����";
				String n=name[r.nextInt(3)];
				if(r.nextInt(10)==1)
					if(n.equals("�ش�"))
						warn="ռ��";
					else
						warn="����";
				retEvents+="\"Event"+(i+1)+"\":\""
				+"λ��,"
				+n+(r.nextInt(3000)+1000)+","
				+warn+","
				+sdf.format(new Date())+"-"+df.format(r.nextInt(hour))+":"+df.format(r.nextInt(minute))+":"+df.format(r.nextInt(second))+"\",";
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
