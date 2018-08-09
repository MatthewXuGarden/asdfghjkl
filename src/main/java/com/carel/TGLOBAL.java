package com.carel;


//import com.carel.supervisor.dataaccess.field.*;
//import com.carel.supervisor.base.math.*;
import com.carel.supervisor.base.config.BaseConfig;


//import com.carel.supervisor.base.timer.*;
//import com.carel.supervisor.base.timer.impl.*;
public class TGLOBAL
{
    //private final static int m_NVar = 10;
    //private final static int m_NCicli = 10;

    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        /*int[] iVar = creaChiavi();
        int[] iVal = genera();
        DataTransfer oDataTransfer = null;
        DataLog oDataLog = new DataLog();
        long l1 = System.currentTimeMillis();
        long lTime = 0;
        for (int i = 0; i < m_NCicli; i++)
        {
            lTime = System.currentTimeMillis();
            oDataTransfer = new DataTransfer(iVar, iVal, new Date(lTime));
            oDataLog.saveLog(oDataTransfer);
        }
        long l2 = System.currentTimeMillis();
        System.out.println("Globale  " + Long.toString(l2 - l1));*/

        //DatabaseMgr.getInstance().executeStatement("POSTGRES", "INSERT INTO \"CAREL\".\"SYKEYS\" VALUES (?,?,?,?)", new Object[]{new Integer(4), "PROVA","ID",new Integer(4)});
        //SeqMgr o = SeqMgr.getInstance();
        //System.out.println(o.next("POSTGRES", "cfdevmdl", "iddevmdl"));
        //System.out.println(o.next("POSTGRES", "cfvarmdl", "idvarmdl"));
        //System.out.println(o.next("POSTGRES", "cfline", "idline"));

        /*DataTransfer oD = new DataTransfer(new Date());
        for(int i = 1; i <= 400; i++)
        {
                oD.add(i,i);
        }
        DataLog dataLog = new DataLog();

        ITimerController oTimerControl = TimerMgr.getInstance().getController();
        oTimerControl.activate();
        oTimerControl.setGlobalLevel(TimerMgr.LOW);
        ILocalTimer t = TimerMgr.getTimer("INSERT", TimerMgr.LOW);

        for(int i = 0; i < 1000; i++)
        {
                t.start();
                dataLog.saveHistory("POSTGRES", oD);
                t.stop();
        }
        SimpleTimerFormatter oSimpleTimerFormatter = new SimpleTimerFormatter();

        System.out.println(oSimpleTimerFormatter.format(TimerMgr.getInstance().getContainer()));
        */
    }

    /*private static int[] creaChiavi()
    {
        int[] iVar = new int[m_NVar];
        for (int i = 0; i < m_NVar; i++)
        {
            iVar[i] = i;
        }
        return iVar;

    }

    private static int[] genera()
    {
        int[] iVar = new int[m_NVar];
        for (int i = 0; i < m_NVar; i++)
        {
            iVar[i] = Randomizer.returnNumber((int)1000);
        }
        return iVar;
    }*/
}
