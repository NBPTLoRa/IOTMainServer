package lora.mainservlet;

import java.io.IOException;
import java.io.PrintWriter;

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

		//��Ȩ
		LoginObj loginObj=Auth.tokenLogin(request);

		String userID=request.getParameter("userID");		//�û�ID
		String devEui=request.getParameter("devEui");		//�豸ID
		String snCode=request.getParameter("snCode").toLowerCase();	//�豸sn��
		String app=request.getParameter("app");				//�豸����
		String descrip=request.getParameter("descrip");		//�豸��ע
		String devName=request.getParameter("devName");		//�豸����
		
		//���ص�json
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String retSuccess="failed";		//ADD�Ƿ���ȫ�ɹ�
		String retError="CreateNull";	//���صĴ�����Ϣ
		int retDoCount=-1;			//���óɹ��ķ���������
		String retDoFServer="-1";		//����ʧ�ܵķ�����IP
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
