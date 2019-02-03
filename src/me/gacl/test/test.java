package me.gacl.test;

import web.sqloperation.SqlOp;

public class test {

    public static void main(String[] args){
    	SqlOp sql=new SqlOp();
    	System.out.println(sql.hasManageGateway("8888","0000000000000001" ));
    }
}
