package web.loginVerify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginVerfication {
	
	public String veriLogin(String userID,String pwd) {
		JsonObject retJ=new JsonObject();

			String loginFlag="e:create no catch";
			//У�����ݿ��
			
			if(loginFlag.equals("1"))
			{
				//У�Գɹ�����{"login":"success","error":"0"}
				retJ=new JsonParser().parse("{\"login\":\"success\",\"error\":\"0\"}").getAsJsonObject();
			}
			else if(loginFlag.equals("0"))
			{
				//У��ʧ�ܷ���{"login":"failed","error":"�û��������벻��ȷ"}
				retJ=new JsonParser().parse("{\"login\":\"failed\",\"error\":\""+"The userID or password incorrect!"+"\"}").getAsJsonObject();
			}
			else 
			{
				//У��ʧ�ܲ���SQL�ڲ��쳣
				retJ=new JsonParser().parse("{\"login\":\"error\",\"error\":\"SQLE----"+loginFlag+"\"}").getAsJsonObject();
			}
			
			return loginFlag;
	}
}
