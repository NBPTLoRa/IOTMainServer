package Mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import Mybatis.domin.Userinf;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Test {
	 public static void main(String[] args) throws IOException {
			String resource = "conf.xml";	      
			InputStream is = Test.class.getClassLoader().getResourceAsStream(resource);	        
			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
			SqlSession session = sessionFactory.openSession(); 
	        
	        String statement = "Mybatis.mapping.userMapper.ALL";//Ó³ÉäsqlµÄ±êÊ¶×Ö·û´®
	        List<Userinf> lstUsers = session.selectList(statement);
	        session.close();
	        System.out.println(lstUsers);
	    }
}
