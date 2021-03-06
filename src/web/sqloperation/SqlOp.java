package web.sqloperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.Random;
import me.gacl.domain.DayCount;
import me.gacl.domain.DayHistory;
import me.gacl.domain.Switch;
import me.gacl.domain.TotalCount;
import me.gacl.domain.User;
import me.gacl.domain.authToken;
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
		 String start="me.gacl.mapping.userMapper.select_userID_and_userPWD";
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
			 String start="me.gacl.mapping.userMapper.select_ALL_serverIP";	
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
		     String start="me.gacl.mapping.userMapper.select_profname";	
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
		public String makeWorkForNode(String nodeID,String nodeManage,String nadeName)
		 {
			 SqlSession session = sessionFactory.openSession(); 
		     String start="me.gacl.mapping.userMapper.select_nodeID";	
			 String ret="";
			 try
			 {
				 List<server> lstUsers = session.selectList(start,nodeID); 
				 if(lstUsers.toString()!="[]")
				 {
					 ret="e:The device already exsts"; 
				 }
				 else 
				 {
					 start="me.gacl.mapping.userMapper.add_inworknodes";	
					 inWorkNodes inwork=new inWorkNodes();
					 inwork.setNodeID(nodeID);
					 inwork.setNodeManage(nodeManage);
					 SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
					 inwork.setNodeCreTime(df.format(new Date()));
					 inwork.setNodeState("1");
					 inwork.setNodeName(nadeName);
					 int retResult = session.update(start,inwork);
				 	 session.commit();
				 	 if(retResult==1)
				 	 {
				 		 ret="1";
				 	 }
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
		     String start="me.gacl.mapping.userMapper.select_nodeID_and_nodeManage";	
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
		     String start="me.gacl.mapping.userMapper.delete_inworknodes";	
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
		     String start="me.gacl.mapping.userMapper.select_gatewayID";	
			 String ret="";			 
			 try
			 {
				 List<server> lstUsers = session.selectList(start,gateID); 
				 if(lstUsers.toString()!="[]")
				 {
					 ret="e:The device already exsts"; 
				 }
				 else
				 {
					 start="me.gacl.mapping.userMapper.add_inWorkGateways";	
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
		     String start="me.gacl.mapping.userMapper.delete_inWorkGateways";	
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
		 
		 public String hasManageGateway(String userID,String gatewayID)	
		 {
			 SqlSession session = sessionFactory.openSession(); 	 
		     String start="me.gacl.mapping.userMapper.select_gatewayID_and_gatewayManage";	
			 String ret="";
			 inWorkGateways inw=new inWorkGateways();
			 try
			 {
				 inw.setGatewayManage(userID);
				 inw.setGatewayID(gatewayID);
				 List<User> shuchu=session.selectList(start, inw);
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
			public String setNodeLastCommu(String nodeID,String lastTime,String lastServer)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.up_lastServer_and_lastTime";	
			     String ret="";
			     try {
					 inWorkNodes inw =new inWorkNodes();
					 inw.setNodeID(nodeID);
					 inw.setLastTime(lastTime);
					 inw.setLastServer(lastServer);
					 int retResult = session.update(start,inw);
					 session.commit();
					 if(retResult==1)
					 {
						 ret="1";
					 }
					 else
					 {
						 ret="0";
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
			public String getServerIPofDevEui(String nodeID)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_lastServer";	
				 String ret="";
				 try
				 {
					 inWorkNodes inw =session.selectOne(start, nodeID);
					 if(inw==null)
					 {
						 ret="0";
					 }
					 else
					 {
						 ret=inw.toString();
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
			public String getAPI(String userID)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_userAPIKey_and_tokenCreTime";	
				 String ret="";
				 try
				 {
					 User user = session.selectOne(start, userID);
					 if(user!=null)
					 {
						 ret=user.toString();
					 }
					 else
					 {
						 ret="0";
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
			
			public String newdate(String time,String t)
			{
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
		        Date date = sdf.parse(time, new ParsePosition(0));
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
		        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
		        calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(t));
				String dateStr=sdf.format(calendar.getTimeInMillis());
				return dateStr;
			}
			
			@SuppressWarnings("finally")
			public String hasClient_id(String client_ID,String APIKey)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_accCreTime_and_effectiveTime";	
			     String ret="";
			     try {
			    	 authToken aut =new authToken();
			    	 aut.setClient_ID(client_ID);
			    	 aut.setAPIKey(APIKey);
			    	 List<authToken> shu= session.selectList(start,aut);
					 session.commit();
					 if(!shu.toString().equals("[]"))
					 {
					    String []shuzhu=(shu.toString().substring(1,shu.toString().length()-1)).split(",");
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
						String dateStr=newdate(shuzhu[0],shuzhu[1]);
						String newtime=sdf.format(new Date());
						ret=dateStr;
			            Date dt1 = sdf.parse(dateStr);
			            Date dt2 = sdf.parse(newtime);
			            if(dt2.after(dt1))
			            {
			            	ret="0";
			            }
			            else
			            {
			            	ret="1";
			            }
					 }
					 else
					 {
						 ret="2";
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
			public String createTempAuth(String APIKey,String userID,String client_ID,String accessToken,String effectiveTime)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.add_authToken";	
			     String ret="";
			     try {
			    	 authToken aut =new authToken();
			    	 aut.setAPIKey(APIKey);
			    	 aut.setClient_ID(client_ID);
			    	 aut.setUserID(userID);
			    	 aut.setEffectiveTime(effectiveTime);
			    	 aut.setAccessToken(accessToken);
			    	 aut.setTokenState("0");
				     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
				     aut.setAccCreTime(sdf.format(new Date()));   
					 int retResult = session.update(start,aut);
					 session.commit();
					 if(retResult==1)
					 {
						 ret=newdate(sdf.format(new Date()),effectiveTime);
					 }
					 else
					 {
						 ret="0";
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
			public String hasTempToken(String accessToken,String userID,String client_ID)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_accCreTime_and_effectiveTime_1";	
			     String ret="";
			     try {
			    	 authToken aut =new authToken();
			    	 aut.setClient_ID(client_ID);
			    	 aut.setAccessToken(accessToken);
			    	 aut.setUserID(userID);
			    	 List<authToken> shu= session.selectList(start,aut);
					 session.commit();
					 if(shu.toString()!="[]")
					 {
					    String []shuzhu=(shu.toString().substring(1,shu.toString().length()-1)).split(",");
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
						String dateStr=newdate(shuzhu[0],shuzhu[1]);
						String newtime=sdf.format(new Date());
			            Date dt1 = sdf.parse(dateStr);
			            Date dt2 = sdf.parse(newtime);
			            if (dt1.getTime() > dt2.getTime())
			            {
			            	ret="1";
			            }
			            else
			            {
			            	ret="e:This AccessToken has expired!";
			            }
					 }
					 else
					 {
			            	ret="e:This AccessToken has expired!";
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
			public String refreshAPIKey(String userID,String newKey)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.up_userAPIKey_and_tokenCreTime";	
			     String ret="";
			     try {
			    	 User use=new User();
			    	 use.setUserID(userID);
			    	 use.setUserAPIKey(newKey);
			    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			    	 use.setTokenCreTime(sdf.format(new Date()));
					 int retResult = session.update(start,use);
					 session.commit();
					 if(retResult==1)
					 {
						 ret="1";
					 }
					 else
					 {
						 ret="0";
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
			public String getTotalCount()
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_ALL_TotalCount";	
			     String start_1="me.gacl.mapping.userMapper.select_Smoke_Temperature_Humidity_Parklot_Safety";	
			     String start_2="me.gacl.mapping.userMapper.up_DayDataCount_for_DayDataCount";	
			     String ret="";
			     try {
					 List<TotalCount> lstUsers = session.selectList(start); 
					 String []data=lstUsers.toString().substring(1,lstUsers.toString().length()-1).split(",");
					 List<DayCount> lstUsers_1 = session.selectList(start_1); 
					 String[] shuju=lstUsers_1.toString().substring(1,lstUsers_1.toString().length()-1).split(",");
					 int sum=0;
					 for(String i:shuju)
					 {
						 sum+=Integer.parseInt(i);
					 }
					 data[3]=""+sum;
					 data[2]=""+(Integer.parseInt(data[2])+sum);
					 
					 session.update(start_2,data[3]);
					 session.commit();
					 
					 ret="1:"+data[0]+","+data[1]+","+data[2]+","+data[3];
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
			public String ifNewDate()
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_Time_for_DayCount";	
				 String ret="";
				 try
				 {
					 DayCount day =session.selectOne(start);
					 String time=day.toString();
			    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			    	 String newtime=sdf.format(new Date());
			    	 if(time.equals(newtime))
			    	 {
			    		 ret="0";
			    	 }
			    	 else
			    	 {
			    		 ret="1";
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
			public String insertToTotalData()
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_Smoke_Temperature_Humidity_Parklot_Safety";	
			     String start_1="me.gacl.mapping.userMapper.up_DayDataCount";	
			     String start_2="me.gacl.mapping.userMapper.up_DayCount";	
				 String ret="";
				 try
				 {
					 List<DayCount> lstUsers = session.selectList(start); 
					 String[] shuju=lstUsers.toString().substring(1,lstUsers.toString().length()-1).split(",");
					 int sum=0;
					 for(String i:shuju)
					 {
						 sum+=Integer.parseInt(i);
					 }
					 
					 session.update(start_1,sum);
					 session.commit();
					 
					 DayCount day=new DayCount();
					 day.setHumidity("0");
					 day.setParklot("0");
					 day.setSafety("0");
					 day.setSmoke("0");
					 day.setTemperature("0");
			    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					 day.setTime(sdf.format(new Date()));
					 session.update(start_2,day);
					 session.commit();
					 ret="1";
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
			public String getDayCount()
			{
				 SqlSession session = sessionFactory.openSession(); 	 //88
			     String start="me.gacl.mapping.userMapper.select_Smoke_Temperature_Humidity_Parklot_Safety";	
			     String start_1="me.gacl.mapping.userMapper.up_DayCount_2";	
			     String start_2="me.gacl.mapping.userMapper.select_all_for_DayHistory";	
			     String start_3="me.gacl.mapping.userMapper.up_TimeCount_for_DayHistory";	
			     String start_4="me.gacl.mapping.userMapper.add_DayHistory";	
				 String ret="1";
				 try
				 {
					 Random r=new Random();
					 List<DayCount> lstUsers = session.selectList(start); 
					 String[] data=lstUsers.toString().substring(1,lstUsers.toString().length()-1).split(",");
					 String da="";
					 int ran=r.nextInt(2);
					 data[0]=""+(Integer.parseInt(data[0])+ran);
					 data[1]=""+(Integer.parseInt(data[1]));
					 data[2]=""+(Integer.parseInt(data[2]));
					 data[3]=""+(Integer.parseInt(data[3]));
					 data[4]=""+(Integer.parseInt(data[4])+ran);
					 ret="1:"+data[0]+","+data[1]+","+data[2]+","+data[3]+","+data[4];
					 DayCount day=new DayCount();
					 
					 day.setHumidity(data[2]);
					 day.setParklot(data[3]);
					 day.setSafety(data[4]);
					 day.setSmoke(data[0]);
					 day.setTemperature(data[1]);
					 session.update(start_1,day);
					 session.commit();
					 
			    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH");
					 List<DayHistory> shu=session.selectList(start_2,sdf.format(new Date()));
					 if(shu.toString().equals("[]"))
					 {
						 DayHistory dh=new DayHistory();
						 dh.setTime(sdf.format(new Date()));
						 dh.setTimeCount(""+ran);
						 session.update(start_4,dh);
						 session.commit();
					 }
					 else
					 {
						 String[] data_1=shu.toString().substring(1,shu.toString().length()-1).split(",");
						 DayHistory dh=new DayHistory();
						 dh.setTime(sdf.format(new Date()));
						 dh.setTimeCount(""+(Integer.parseInt(data_1[1])+ran));
						 session.update(start_3,dh);
						 session.commit();
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
			public String getDayHistory()
			{
				 SqlSession session = sessionFactory.openSession(); 	 //454
			     String start="me.gacl.mapping.userMapper.select_all_for_DayHistory";	
			     String []date=new String[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
				 String ret="1:";
				 try
				 {
			    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					 List<DayHistory> shuzhu=session.selectList(start,sdf.format(new Date())+"%");
					 for(DayHistory i:shuzhu)
					 {
						 String []t=i.toString().split(",");
						 int da=Integer.parseInt(t[0].substring(t[0].length()-2, t[0].length()));
						 date[da]=t[1];
					 }
		    	     SimpleDateFormat sdf_1 = new SimpleDateFormat("HH");
					 int da=Integer.parseInt(sdf_1.format(new Date()));
					 for(int i=0;i<=da;i++)
					 {
						 ret+=date[i]+",";
					 }
					 ret=ret.substring(0,ret.length()-1);
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
			public String getSwitches()
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.select_all_for_Switch";	
				 String ret="";
				 try
				 {
					 List<Switch> lstUsers = session.selectList(start);
					 ret="1:"+lstUsers.toString().substring(1,lstUsers.toString().length()-1);
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
			public String setLight(String s)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.up_light_for_Switch";	
				 String ret="";
				 try
				 {
					 int retResult = session.update(start,s);
					 session.commit();
					 if(retResult==1)
					 {
						 ret="1";
					 }
					 else
					 {
						 ret="0";
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
			public String setWar(String s)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.up_war_for_Switch";	
				 String ret="";
				 try
				 {
					 int retResult = session.update(start,s);
					 session.commit();
					 if(retResult==1)
					 {
						 ret="1";
					 }
					 else
					 {
						 ret="0";
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
			public String setLinkWar(String s)
			{
				 SqlSession session = sessionFactory.openSession(); 	 
			     String start="me.gacl.mapping.userMapper.up_link_war_for_Switch";	
				 String ret="";
				 try
				 {
					 int retResult = session.update(start,s);
					 session.commit();
					 if(retResult==1)
					 {
						 ret="1";
					 }
					 else
					 {
						 ret="0";
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
