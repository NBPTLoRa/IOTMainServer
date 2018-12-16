package web.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String userID;
		String pwd;
		JsonObject retJ=new JsonObject();
		try
		{
			userID=request.getParameter("userID");
			pwd=request.getParameter("pwd");
			Boolean loginFlag=false;
			//У�����ݿ��
			
			if(loginFlag)
			{
				//У�Գɹ�����{"login":"success","error":"0"}
				retJ=new JsonParser().parse("{\"login\":\"success\",\"error\":\"0\"}").getAsJsonObject();
			}else
			{
				//У��ʧ�ܷ���{"login":"failed","error":"�û��������벻��ȷ"}
				retJ=new JsonParser().parse("{\"login\":\"failed\",\"error\":\""+"�û��������벻��ȷ"+"\"}").getAsJsonObject();
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
