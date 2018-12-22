package me.gacl.test;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import me.gacl.domain.server;
import web.sqloperation.SqlOp;



public class test {
    public static void main(String[] args){
    	SqlOp sql=new SqlOp();
    	String[]a=sql.getDisServIP();
    	System.out.println(a[0]+"   "+a[1]);
    }
    
}