package web.loginVerify;


import web.sqloperation.SqlOp;

public class LoginVerfication {
	
	
	//�����ж��û��Ƿ�ƥ��
	public LoginObj veriLogin(String userID,String pwd) {
		LoginObj loginObj=new LoginObj();
		SqlOp sqlOp=new SqlOp();
		String loginFlag="e:create no catch";
			//У�����ݿ��
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
				//У��ʧ�ܲ���SQL�ڲ��쳣
				loginObj.setException("e:"+loginFlag);
			}
			
			return loginObj;
	}

	public LoginObj veriAuth(String accessToken,String userID,String client_ID) {
		LoginObj loginObj=new LoginObj();
		SqlOp sqlOp=new SqlOp();
		String loginFlag="e:create no catch veriAuth";
			//У�����ݿ��
			loginFlag="";//sqlOp.hasTempToken(accessToken,userID,client_ID);
			if(loginFlag.substring(0, 1).equals("1"))
			{
				loginObj.setLoginSta(true);
			}
			else 
			{
				//У��ʧ�ܲ���SQL�ڲ��쳣
				loginObj.setException(loginFlag);
			}
			
			return loginObj;
	}
}
