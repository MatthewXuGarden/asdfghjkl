package com.carel.supervisor.test;

import com.carel.supervisor.base.config.BaseConfig;


public class TestFunction
{
	private TestFunction()
    {
        super();
    }

    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();
    }
}
