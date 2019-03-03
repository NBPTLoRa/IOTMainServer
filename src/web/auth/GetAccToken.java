package web.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import web.sqloperation.SqlOp;

/**
 * Servlet implementation class GetAccToken
 */
public class GetAccToken extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public GetAccToken() {
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
		String userID=request.getParameter("userID");
		String ApiKey=request.getParameter("openAPIKey");
		String Client_ID=request.getParameter("client_id");
		
		
		String retToken="0";
		String retTime="0";
		String retError="e:CreateNullError";
		
		JsonObject retJ =new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		//������û�з��ϵ��û�
		String retS=sqlOp.getAPI(userID);
		
		if(retS.equals("0"))
		{
			retToken="0";
			retTime="0";
			retError+="Your account does not enable third-party development!";
		}else if(retS.substring(0, 1).equals("e"))
		{
			retToken="0";
			retTime="0";
			retError+="Sql Get errors-"+retS;
		}
		else
		{//����APIKey��ȡ����
			if(retS.split(":")[1].equals(ApiKey))
			{//API����
				//�鿴clien_id�Ƿ��ظ�
				String tempS=sqlOp.hasClient_id(Client_ID,ApiKey);
				if(tempS.equals("1"))
				{//������ʹ�õ�client_ID

					retToken="0";
					retTime="0";
					retError+="Client_id is in use!";
				}
				else if(tempS.substring(0, 1).equals("e"))
				{//����
					retToken="0";
					retTime="0";
					retError+="Sql Get hasClient_id errors-"+tempS;
				}
				else if(tempS.equals("0")||tempS.equals("2"))
				{//û���ظ�����ʹ�õ�client_ID
					//��authToken�ⴴ��һ��APIkey
					String accessToken=randomHexString(64);
					String r="e:CreateR";
					r=sqlOp.createTempAuth(ApiKey,userID,Client_ID,accessToken,1+"");
					
					if(!r.substring(0,1).equals("e"))
					{//��������
						retToken=accessToken;
						retTime=r;
						retError+="";
					}
					else
					{//����ʧ��

						retToken="0";
						retTime="0";
						retError+="Sql Get errors-"+retS;
					}
				}else {
					retToken="0";
					retTime="0";
					retError+="null";
				}
				
			}else
			{
				retToken="0";
				retTime="0";
				retError+="APIKey Error!";
			}
			
		}
		
		//����accessToken
		retJ=jsonParser.parse("{\"accessToken\":\""+retToken
				+"\",\"error\":\""+retError.replace("\"","#").replace("e:CreateNullError", "")//
				+"\",\"ExpirationTime\":\""+retTime
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
	public static String randomHexString(int len)  {
		try {
			StringBuffer result = new StringBuffer();
			for(int i=0;i<len;i++) {
				result.append(Integer.toHexString(new Random().nextInt(16)));
			}
			return result.toString().toUpperCase();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}

}
