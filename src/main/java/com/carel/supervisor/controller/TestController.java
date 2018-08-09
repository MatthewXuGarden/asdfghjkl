package com.carel.supervisor.controller;

import com.carel.supervisor.base.config.BaseConfig;


public class TestController
{
    private TestController()
    {
    }

    /**
     * @param args
     */
    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();
        ControllerMgr.getInstance().load();
        Thread.sleep(400000);
    }
}
