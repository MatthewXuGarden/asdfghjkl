package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class TestC implements IDumpable
{
    private C variabileC = new C();
    private static final Logger logger = LoggerMgr.getLogger(TestC.class);

    public DumpWriter getDumpWriter()
    {
        DumpWriter o = DumperMgr.createDumpWriter("TestC", this);
        o.print("Test", variabileC);

        return o;
    }

    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        TestC testC1 = new TestC();
        logger.info(testC1.getDumpWriter().getStringBuffer().toString());
    }

    public class A implements IDumpable
    {
        public DumpWriter getDumpWriter()
        {
            DumpWriter o = DumperMgr.createDumpWriter("A", this);
            o.print("1", "1");

            return o;
        }
    }

    public class B extends A implements IDumpable
    {
        public DumpWriter getDumpWriter()
        {
            DumpWriter o = DumperMgr.createDumpWriter("B", this);
            o.print("2", "2");

            return o;
        }
    }

    public class C extends B implements IDumpable
    {
        public DumpWriter getDumpWriter()
        {
            DumpWriter o = DumperMgr.createDumpWriter("C", this);
            o.print("3", "3");

            return o;
        }
    }
}
