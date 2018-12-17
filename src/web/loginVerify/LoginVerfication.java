package web.loginVerify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginVerfication {
	
	public String veriLogin(String userID,String pwd) {
		JsonObject retJ=new JsonObject();

			String loginFlag="e:create no catch";
			//校对数据库↓
			
			if(loginFlag.equals("1"))
			{
				//校对成功返回{"login":"success","error":"0"}
				retJ=new JsonParser().parse("{\"login\":\"success\",\"error\":\"0\"}").getAsJsonObject();
			}
			else if(loginFlag.equals("0"))
			{
				//校对失败返回{"login":"failed","error":"用户名或密码不正确"}
				retJ=new JsonParser().parse("{\"login\":\"failed\",\"error\":\""+"The userID or password incorrect!"+"\"}").getAsJsonObject();
			}
			else 
			{
				//校对失败并且SQL内部异常
				retJ=new JsonParser().parse("{\"login\":\"error\",\"error\":\"SQLE----"+loginFlag+"\"}").getAsJsonObject();
			}
			
			return loginFlag;
	}
}
