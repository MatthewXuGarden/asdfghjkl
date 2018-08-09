package com.carel.supervisor.controller.function;

import java.util.List;

import com.carel.supervisor.base.config.BaseConfig;

/*
import com.carel.supervisor.base.config.BaseConfig;
*/
public class TestFunction
{
    
    public static void main(String[] argv) throws Throwable
    {
    	BaseConfig.init();
    	List list = FunctionMgr.getInstance().getFunctionList(FunctionMgr.REAL);
    	for(int i = 0; i < list.size(); i++)
    	{
    		System.out.println(((FunctionInfo)list.get(i)).getCode());
    	}
    	System.out.println("------------------");
    	list = FunctionMgr.getInstance().getFunctionList(FunctionMgr.BOOLEAN);
    	for(int i = 0; i < list.size(); i++)
    	{
    		System.out.println(((FunctionInfo)list.get(i)).getCode());
    	}
    }
}
