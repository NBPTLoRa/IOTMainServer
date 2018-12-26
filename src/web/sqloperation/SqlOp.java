package web.sqloperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import me.gacl.domain.User;
import me.gacl.domain.profComparison;
import me.gacl.domain.server;
import me.gacl.test.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlOp {
	SqlSessionFactory sessionFactory;
	public SqlOp()
	{
		String resource = "conf.xml";	      
		InputStream is = SqlOp.class.getClassLoader().getResourceAsStream(resource);	        
		sessionFactory = new SqlSessionFactoryBuilder().build(is);
	}
	
	@SuppressWarnings("finally")
	public String login(String ID,String PWD)
	{
		SqlSession session = sessionFactory.openSession(); 
		 String start="me.gacl.mapping.userMapper.login";
		 String ret="";
		 User use =new User();
		 try
		 {
			 use.setUserID(ID);
			 use.setUserPWD(PWD);
			 List<User> shuchu=session.selectList(start, use);
			 if(shuchu.toString()!="[]")
			 {
				 ret="1:"+shuchu.toString().substring(1,shuchu.toString().length()-1);
			 }
			 else
			 {			 
				 ret= "0";
			 }
		 } 
		 catch(Exception ex)
		 {
			 ret= "e:"+ex.toString();
			 ex.printStackTrace();
		 }
		 finally
		 {
			 session.close();
			 return ret;
		 }
		}
		 
		 @SuppressWarnings("finally")
		public String[] getDisServIP()
		 {	
			 SqlSession session = sessionFactory.openSession(); 
			 String start="me.gacl.mapping.userMapper.DistServIP";	
			 String []ret=null;
			 try 
			 { 
				 List<server> lstUsers = session.selectList(start); 
				 ret=lstUsers.toString().substring(1,lstUsers.toString().length()-1).split(",");

			 }	
			 catch(Exception ex)
			 {
				 ret=new String[1];
				 ret[0]="e:"+ex.toString();
				 ex.printStackTrace();
			 }	
			 finally
			 {
				 session.close();
				 return ret;
			 }
		 }
		 
		 @SuppressWarnings("finally")
		 public String getProfNameforappName(String name)
		 {	  
			 SqlSession session = sessionFactory.openSession(); 	 
		     String start="me.gacl.mapping.userMapper.ProfComparison";	
			 String ret="";
			 try
			 {
				 profComparison pr=session.selectOne(start, name);
				 if(pr!=null)
				 {
					 ret=pr.toString();
				 }
			 }
			 catch(Exception ex)
			 {
				 ret="e:"+ex.toString();
				 ex.printStackTrace();
			 } 
			 finally
			 {
				 session.close();
				 return ret;
			 }
		 }
}
