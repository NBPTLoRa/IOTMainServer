package me.gacl.test;

import web.sqloperation.SqlOp;

public class test {

    public static void main(String[] args){
    	SqlOp sql=new SqlOp();
    	System.out.println(sql.getServerIPofDevEui("d896e0ff00000240"));
    }
}
