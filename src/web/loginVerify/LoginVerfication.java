package web.loginVerify;


import web.sqloperation.SqlOp;

public class LoginVerfication {
	
	
	//�����ж��û��Ƿ�ƥ��
	public String veriLogin(String userID,String pwd) {
		String retS=new String();
			SqlOp sqlOp=new SqlOp();
			String loginFlag="e:create no catch";
			//У�����ݿ��
			loginFlag=sqlOp.login(userID,pwd);
			if(loginFlag.substring(0, 1).equals("1"))
			{
				//У�Գɹ�����{"login":"success","error":"0","classfy":"******"}
				retS="{\"login\":\"success\",\"error\":\"0\",\"classfy\":\""+loginFlag.substring(2)+"\"}";
			}
			else if(loginFlag.substring(0, 1).equals("0"))
			{
				//У��ʧ�ܷ���{"login":"failed","error":"�û��������벻��ȷ","classfy":"-1"}
				retS="{\"login\":\"failed\",\"error\":\""+"The userID or password incorrect!"+"\",\"classfy\":\"-1\"}";
			}
			else 
			{
				//У��ʧ�ܲ���SQL�ڲ��쳣
				retS="{\"login\":\"error\",\"error\":\"SQLE----"+loginFlag+"\",\"classfy\":\"-1\"}";
			}
			
			return retS;
	}
}
