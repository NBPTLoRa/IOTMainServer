package web.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import web.sqloperation.SqlOp;

/**
 * Servlet implementation class LoginEntrance
 */
public class LoginEntrance extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginEntrance() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String userID;
		String pwd;
		SqlOp sqlOp=new SqlOp();
		JsonObject retJ=new JsonObject();
		try
		{
			userID=request.getParameter("userID");
			pwd=request.getParameter("pwd");
			String loginFlag="e:create no catch";
			//У�����ݿ��
			loginFlag=sqlOp.login(userID,pwd);
			//�ж��Ƿ�ͨ��
			if(loginFlag.substring(0, 1).equals("1"))
			{
				//У�Գɹ�����{"login":"success","error":"0","classfy":"******"}
				retJ=new JsonParser().parse("{\"login\":\"success\",\"error\":\"0\",\"classfy\":\""+loginFlag.substring(2)+"\"}").getAsJsonObject();
			}
			else if(loginFlag.substring(0, 1).equals("0"))
			{
				//У��ʧ�ܷ���{"login":"failed","error":"�û��������벻��ȷ","classfy":"-1"}
				retJ=new JsonParser().parse("{\"login\":\"failed\",\"error\":\""+"The userID or password incorrect!"+"\",\"classfy\":\"-1\"}").getAsJsonObject();
			}
			else 
			{
				//У��ʧ�ܲ���SQL�ڲ��쳣
				retJ=new JsonParser().parse("{\"login\":\"error\",\"error\":\"SQLE----"+loginFlag+"\",\"classfy\":\"-1\"}").getAsJsonObject();
			}
		}
		catch (Exception e) {
			//����
			//���أ�{"login":"error","error":"e.toString()"}
			retJ=new JsonParser().parse("{\"login\":\"error\",\"error\":\""+e.toString()+"\"}").getAsJsonObject();
		}finally {
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
