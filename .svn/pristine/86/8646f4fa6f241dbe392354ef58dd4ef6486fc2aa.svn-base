package com.carel.supervisor.dataaccess.event;

import com.carel.supervisor.base.config.BaseConfig;


public class TestEvent
{
    private TestEvent()
    {
        super();

        // TODO Auto-generated constructor stub
    }

    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();

        for (int i = 0; i < 300; i++)
        {
            EventMgr.getInstance().log(new Integer(1), "CONTROLLER", "",
                EventDictionary.TYPE_ERROR, "???", null);
        }
    }
}
