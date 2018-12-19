package web.loginVerify;


import web.sqloperation.SqlOp;

public class LoginVerfication {
	
	
	//用于判断用户是否匹配
	public String veriLogin(String userID,String pwd) {
		String retS=new String();
			SqlOp sqlOp=new SqlOp();
			String loginFlag="e:create no catch";
			//校对数据库↓
			loginFlag=sqlOp.login(userID,pwd);
			if(loginFlag.substring(0, 1).equals("1"))
			{
				//校对成功返回{"login":"success","error":"0","classfy":"******"}
				retS="{\"login\":\"success\",\"error\":\"0\",\"classfy\":\""+loginFlag.substring(2)+"\"}";
			}
			else if(loginFlag.substring(0, 1).equals("0"))
			{
				//校对失败返回{"login":"failed","error":"用户名或密码不正确","classfy":"-1"}
				retS="{\"login\":\"failed\",\"error\":\""+"The userID or password incorrect!"+"\",\"classfy\":\"-1\"}";
			}
			else 
			{
				//校对失败并且SQL内部异常
				retS="{\"login\":\"error\",\"error\":\"SQLE----"+loginFlag+"\",\"classfy\":\"-1\"}";
			}
			
			return retS;
	}
}
