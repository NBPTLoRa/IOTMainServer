package web.sqloperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.gacl.domain.User;
import me.gacl.domain.inWorkGateways;
import me.gacl.domain.inWorkNodes;
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
		 
		 @SuppressWarnings("finally")
		public String makeWorkForNode(String nodeID,String nodeManage)
		 {
			 SqlSession session = sessionFactory.openSession(); 	 
		     String start="me.gacl.mapping.userMapper.addinworknodes";	
			 String ret="";
			 try
			 {
				 inWorkNodes inwork=new inWorkNodes();
				 inwork.setNodeID(nodeID);
				 inwork.setNodeManage(nodeManage);
				 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
				 inwork.setNodeCreTime(df.format(new Date()));
				 inwork.setNodeState("1");
				 int retResult = session.update(start,inwork);
				 session.commit();
				 if(retResult==1)
				 {
					 ret="1";
				 }
				 else if(retResult==0)
				 {
					 ret="e:The device already exsts";
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
		 
		 @SuppressWarnings("finally")
		public String hasManageNode(String userID,String nodeID)
		 {
			 SqlSession session = sessionFactory.openSession(); 	 
		     String start="me.gacl.mapping.userMapper.hasinworknodes";	
			 String ret="";
			 try
			 {
				 inWorkNodes inwork=new inWorkNodes();
				 inwork.setNodeID(nodeID);
				 inwork.setNodeManage(userID);
				 List<inWorkNodes> shuchu=session.selectList(start, inwork);
				 if(shuchu.toString()!="[]")
				 {
					 ret="1";
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
		public String deleteNode(String nodeID)
		 {
			 SqlSession session = sessionFactory.openSession(); 	 
		     String start="me.gacl.mapping.userMapper.deleteinworknodes";	
			 String ret="";
			 try
			 {
				 int retResult = session.delete(start,nodeID);
				 session.commit();
				 ret=""+retResult;				 
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
		public String makeWorkGateway(String gateID,String GateManage)
		 {
			 SqlSession session = sessionFactory.openSession(); 	 
		     String start="me.gacl.mapping.userMapper.addinWorkGateways";	
			 String ret="";
			 try
			 {
				 inWorkGateways inwork=new inWorkGateways();
				 inwork.setGatewayID(gateID);
				 inwork.setGatewayManage(GateManage);
				 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
				 inwork.setGatewayCreTime(df.format(new Date()));
				 inwork.setGatewayState("1");
				 int retResult = session.update(start,inwork);
				 session.commit();
				 if(retResult==1)
				 {
					 ret="1";
				 }
				 else if(retResult==0)
				 {
					 ret="e:The device already exsts";
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
		 
		 @SuppressWarnings("finally")
		public String deleteGateway(String gateID)
		 {
			 SqlSession session = sessionFactory.openSession(); 	 
		     String start="me.gacl.mapping.userMapper.deleteinWorkGateways";	
			 String ret="";
			 try
			 {
				 int retResult = session.delete(start,gateID);
				 session.commit();
				 ret=""+retResult;				 
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
}
