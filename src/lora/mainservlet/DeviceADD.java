package lora.mainservlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTMLEditorKit.Parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import web.loginVerify.LoginObj;
import web.loginVerify.LoginVerfication;
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
    //���������б��µĽڵ�
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out=response.getWriter();
		LoginVerfication loginVerfication=new LoginVerfication();
		LoginObj loginObj=loginVerfication.veriLogin(request.getParameter("userID"),request.getParameter("pwd"));
		JsonObject retJ=new JsonObject();
		JsonParser jsonParser=new JsonParser();
		
		//�ж��û���Ϣ
		if(loginObj.getLoginSta())
		{
			//ͨ����������еķַ����������url
			String[] ips =sqlOp.getDisServIP();
			if(ips[0].equals("e"))
			{//��ȡ�쳣
				retJ=jsonParser.parse("").getAsJsonObject();
			}
			else
			{//��ȡ�ɹ�
				Map<String, String> data=new HashMap<String,String>();
				data.put("doOper", "deviceADD");
				try {
					String aString=test("http://localhost:8080/LoRaServletTest/do", data);
					if(aString.substring(0, 1).equals("e"))
					{//����ַ���������
						//{"success":"failed","error":"***","doCount":"-1","doFServer":"-1"}
						retJ=jsonParser.parse("{\"success\":\"failed\",\"error\":\""+aString+"\",\"doCount\":\"-1\",\"doFServer\":\"-1\"}").getAsJsonObject();
					}else
					{//���������
						//{"success":"failed","error":"0","doCount":"-1","doFServer":"-1"}
						retJ=jsonParser.parse("{\"success\":\"success\",\"error\":\"0\",\"doCount\":\"-1\",\"doFServer\":\"-1\"}").getAsJsonObject();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		else
		{//�˻���ͨ��
			//{"success":"failed","error":"Your account does not have permission!","doCount":"-1","doFServer":"-1"}
			retJ=jsonParser.parse("{\"success\":\"failed\",\"error\":\"Your account does not have permission!\",\"doCount\":\"-1\",\"doFServer\":\"-1\"}").getAsJsonObject();
		}
		out.println(retJ);
	}

	public static String test(String url1, Map data)throws Exception 
	{
		//�Ѳ���ƴ�ӵ�URL����
		for (Object obj : data.entrySet()) {
			
			url1+="?";

			Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;
			String key = entry.getKey().toString();
			String value = null;
			if (entry.getValue() == null) 
			{
				value = "";
			} 
			else
			{
				value = entry.getValue().toString();
			}
			url1+=key;
			url1+='=';
			url1+=URLEncoder.encode(value, "UTF-8");
		}
		//����URL����
		URL url = new URL(url1);
		
		URLConnection connection=url.openConnection();
		
		InputStream in = connection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

		//���շ�����Ӧ��Ϣ
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
