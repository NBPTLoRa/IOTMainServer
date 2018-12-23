package me.gacl.test;

import web.sqloperation.SqlOp;

public class test {

    public static void main(String[] args){
    	SqlOp sql=new SqlOp();
    	String[]a=sql.getDisServIP();
    	System.out.println(a[0]);
    	
    	String[]b=sql.getDisServIP();
    	System.out.println(b[0]);
    }

}
