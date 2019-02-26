package web.loginVerify;


import web.sqloperation.SqlOp;

public class LoginVerfication {
	
	
	//用于判断用户是否匹配
	public LoginObj veriLogin(String userID,String pwd) {
		LoginObj loginObj=new LoginObj();
		SqlOp sqlOp=new SqlOp();
		String loginFlag="e:create no catch";
			//校对数据库↓
			loginFlag=sqlOp.login(userID,pwd);
			if(loginFlag.substring(0, 1).equals("1"))
			{
				loginObj.setLoginSta(true);
			}
			else if(loginFlag.substring(0, 1).equals("0"))
			{
				loginObj.setException("The userID or password incorrect!");
			}
			else 
			{
				//校对失败并且SQL内部异常
				loginObj.setException("e:"+loginFlag);
			}
			
			return loginObj;
	}

	public LoginObj veriAuth(String accessToken,String userID,String client_ID) {
		LoginObj loginObj=new LoginObj();
		SqlOp sqlOp=new SqlOp();
		String loginFlag="e:create no catch veriAuth";
			//校对数据库↓
			loginFlag="";//sqlOp.hasTempToken(accessToken,userID,client_ID);
			if(loginFlag.substring(0, 1).equals("1"))
			{
				loginObj.setLoginSta(true);
			}
			else 
			{
				//校对失败并且SQL内部异常
				loginObj.setException(loginFlag);
			}
			
			return loginObj;
	}
}
