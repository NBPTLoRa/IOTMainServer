package me.gacl.test;

import web.sqloperation.SqlOp;

public class test {

    public static void main(String[] args){
    	SqlOp sql=new SqlOp();
    	System.out.println(sql.hasClient_id("00000005","hhh"));
    }
}
