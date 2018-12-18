package web.sqloperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import me.gacl.domain.User;
import me.gacl.test.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlOp {
	SqlSession session;
	public SqlOp()
	{
		String resource = "conf.xml";	      
		InputStream is = SqlOp.class.getClassLoader().getResourceAsStream(resource);	        
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		session = sessionFactory.openSession(); 
	}
	
	public String login(String ID,String PWD)
	{
		 String start="me.gacl.mapping.userMapper.login";
		 User use =new User();
		 try
		 {
			 use.setUserID(ID);
			 use.setUserPWD(PWD);
			 List<User> shuchu=session.selectList(start, use);
			 if(shuchu.toString()!="[]")
			 {
				 session.close();
				 return "1:"+shuchu.toString().substring(1,shuchu.toString().length()-1);
			 }
			 else
			 {			 
				 session.close();
				 return "0";
			 }
		 } 
		 catch(Exception ex)
		 {
			 session.close();
			 return "e"+ex.toString();
		 }
	}
	
}
