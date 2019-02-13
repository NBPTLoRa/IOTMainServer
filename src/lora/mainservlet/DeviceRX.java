package lora.mainservlet;

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
 * Servlet implementation class DeviceRX
 */
public class DeviceRX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	SqlOp sqlOp;
    public DeviceRX() {
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
		LoginObj loginObj=Auth.auth(request);

		
		String devEui=request.getParameter("devEui");		//�豸ID
		String pull_mode=request.getParameter("pull_mode");	//����ģʽ
		
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		String errorReturn="CreateNullError";
		
		
		//�˻���Ȩ���Ƿ���ƽ̨�û�
		if(!loginObj.getLoginSta())
		{
			errorReturn="ERROR Incorrect username or password";
		}
		
		Boolean inputFormat=false;
		//�жϽڵ�ID�ǲ���16λ��hex��
		if(devEui.matches("^[a-f0-9]{16}")&&loginObj.getLoginSta())
		{//�����
			inputFormat=true;
		}
		else
		{//����
			errorReturn="Your NodeID is not up to standard";
		}
		
		
		
		
		//����Json��
		if(errorReturn.equals("CreateNullError"))
		{//���û�д���
			
		}
		else
		{//���������
			
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
