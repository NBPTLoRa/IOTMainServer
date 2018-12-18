package web.sqloperation;

import java.io.InputStream;
import java.util.List;

import Mybatis.domin.Userinf;
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
		String start="SYT.mapping.userMapper.login";
	    Userinf use =new Userinf();
	    try
	    {
	    	use.setUserID(ID);
	    	use.setUserPWD(PWD);
	        List<Userinf> lstUsers = session.selectList(start,use);
	        if(lstUsers!=null)
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
        	return "e"+ex.toString();
	    }
	    finally
	    {
	    	session.close();
	    }
	}
	
}
