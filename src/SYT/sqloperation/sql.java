package SYT.sqloperation;

import java.util.List;

import SYT.domain.userinf;
import java.io.InputStream;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class sql {
	SqlSession session;
	public sql(){
		String resource = "conf.xml";	      
		InputStream is = sql.class.getClassLoader().getResourceAsStream(resource);	        
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		session = sessionFactory.openSession(); 
	}
	
	public String login(String id,String pwd)
	{
	    String start="SYT.mapping.userMapper.login";
		userinf use=new userinf();
		try
		{
			use.setUserID(id);
			use.setUserPWD(pwd);
			int i=session.selectOne(start, use);
	        session.commit();
			if(i==1)
			{
				return "1";
			}
			else
			{
				return "0";
			}
		}
	    catch(Exception ex)
	    {
	    	return "e"+ex;
	    }
	    finally
	    {
	    	session.close();
	    }
	}
}