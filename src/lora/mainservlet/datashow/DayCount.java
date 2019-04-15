package lora.mainservlet.datashow;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class DayCount
 */
public class DayCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DayCount() {
        super();
        // TODO Auto-generated constructor stub
        sqlOp=new SqlOp();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		
		LoginObj loginObj=Auth.tokenLogin(request);
		
		String Smoke="";
		String Temperature="";
		String Humidity="";
		String Parklot="";
		String Safety="";
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
		
		if(authFlag)
		{
			//���ж��Ƿ����µ�һ��
			String retNew=sqlOp.ifNewDate();
			/*
			if(retNew.equals("1"))
			{//�µ�һ��
				//�����ݼӵ�totalCount���TotalDataCount�����
				sqlOp.insertToTotalData();
			}
			else
			{
				retError+="NewDate Get Error!"+retNew;
			}
			//��ȡÿ������
			String dayCount=sqlOp.getDayCount();
			if(dayCount.split(":")[0].equals("e"))
			{//�������ݼ������ֵ��д��ȥ
				retError+="DayCount Get Error!"+dayCount;
			}
			else
			{
				dayCount=	dayCount.split(":")[1];
				Smoke=		dayCount.split(",")[0];
				Temperature=dayCount.split(",")[1];
				Humidity=	dayCount.split(",")[2];
				Parklot=	dayCount.split(",")[3];
				Safety=		dayCount.split(",")[4];
			}
			*/
		}
		
		String retJsonS="{"
				+"\"Smoke\":\""+Smoke+"\","
				+"\"Temperature\":\""+Temperature+"\","
				+"\"Humidity\":\""+Humidity+"\","
				+"\"Parklot\":\""+Parklot+"\","
				+"\"Safety\":\""+Safety+"\","
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
