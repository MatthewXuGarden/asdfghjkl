package com.carel.supervisor.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.status.*;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import java.util.List;


public class TestStateMachine
{
	private static final String TEXT = "ActiveState state";
    private TestStateMachine()
    {
    }

    public static void main(String[] argv) throws Throwable
    {
        TestStateMachine o = new TestStateMachine();
        BaseConfig.init();
        DatabaseMgr.getInstance().executeStatement(null,
            "delete from rulestate", null);
        o.executeAllState();
        o.executeStoreDataFlow();
    }

    public void executeAllState() throws TestException,Exception
    {
        AbstractStatus actualStatus = StatusRegistry.invalidStatus;
        AbstractStatus resultStatus = null;
        ContextStatus contextStatus = new ContextStatus();
        contextStatus.setIdRule(new Integer(1));
        contextStatus.setIdVar(new Integer(0));

        //TRANSIZIONE n. 1    	
        changeData(contextStatus, false, 0);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof InvalidStatus))
        {
            throw new TestException("InvalidState state");
        }

        changeData(contextStatus, false, 0);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof InvalidStatus))
        {
            throw new TestException("InvalidState state");
        }

        changeData(contextStatus, false, 1000);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof InvalidStatus))
        {
            throw new TestException("InvalidState state");
        }

        changeData(contextStatus, false, 0);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof InvalidStatus))
        {
            throw new TestException("InvalidState state");
        }

        //    	TRANSIZIONE n. 2
        actualStatus = StatusRegistry.invalidStatus;
        changeData(contextStatus, true, 1000);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        resultStatus = retrieve();

        if (!(resultStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        //    	TRANSIZIONE n. 4
        changeData(contextStatus, false);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof InvalidStatus))
        {
            throw new TestException("InvalidState state");
        }

        resultStatus = retrieve();

        if (null != resultStatus)
        {
            throw new TestException("InvalidState state");
        }

        //		TRANSIZIONE n. 3  
        actualStatus = StatusRegistry.invalidStatus;
        changeData(contextStatus, true, 1000);
        actualStatus = actualStatus.next(contextStatus);
        changeData(contextStatus, true);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        changeData(contextStatus, true);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        changeData(contextStatus, true);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        Thread.sleep(300);
        changeData(contextStatus, true);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        resultStatus = retrieve();

        if (!(resultStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        //		TRANSIZIONE n. 5   
        Thread.sleep(2000);
        changeData(contextStatus, true);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ToManageStatus))
        {
            throw new TestException("ToNotifyState state");
        }

        resultStatus = retrieve();

        if (!(resultStatus instanceof ToManageStatus))
        {
            throw new TestException("ToNotifyState state");
        }

        //		TRANSIZIONE n. 8   
        Thread.sleep(2000);

        AbstractStatus doubleState = actualStatus;
        changeData(contextStatus, false);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof CalledOffStatus))
        {
            throw new TestException("CalledOffState state");
        }

        resultStatus = retrieve();

        if (null != resultStatus)
        {
            throw new TestException("CalledOffState state");
        }

        //		TRANSIZIONE n. 7   
        Thread.sleep(2000);
        changeData(contextStatus, true);
        actualStatus = doubleState;
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof AlreadyManagedStatus))
        {
            throw new TestException("AlreadyNotifyState state");
        }

        resultStatus = retrieve();

        if (null != resultStatus)
        {
            throw new TestException("AlreadyNotifyState state");
        }

        //		TRANSIZIONE n. 9   
        Thread.sleep(2000);
        changeData(contextStatus, true);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof AlreadyManagedStatus))
        {
            throw new TestException("AlreadyNotifyState state");
        }

        Thread.sleep(1000);
        changeData(contextStatus, true);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof AlreadyManagedStatus))
        {
            throw new TestException("AlreadyNotifyState state");
        }

        //		TRANSIZIONE n. 10
        changeData(contextStatus, false);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof CalledOffStatus))
        {
            throw new TestException("CalledOffState state");
        }

        //		TRANSIZIONE n. 11
        doubleState = actualStatus;
        changeData(contextStatus, true, 0);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ToManageStatus))
        {
            throw new TestException("ToNotifyState state");
        }

        changeData(contextStatus, true, 0);
        actualStatus = actualStatus.next(contextStatus);
        changeData(contextStatus, false, 0);
        actualStatus = actualStatus.next(contextStatus);

        //		TRANSIZIONE n. 12
        actualStatus = doubleState;
        changeData(contextStatus, true, 1000);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        resultStatus = retrieve();

        if (!(resultStatus instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        changeData(contextStatus, false, 0);
        actualStatus = actualStatus.next(contextStatus);

        //		TRANSIZIONE n. 13
        actualStatus = doubleState;
        changeData(contextStatus, false, 1000);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof InvalidStatus))
        {
            throw new TestException("InvalidState state");
        }

        //    	TRANSIZIONE n. 6
        changeData(contextStatus, true, 0);
        actualStatus = actualStatus.next(contextStatus);

        if (!(actualStatus instanceof ToManageStatus))
        {
            throw new TestException("ToNotifyState state");
        }

        resultStatus = retrieve();

        if (!(resultStatus instanceof ToManageStatus))
        {
            throw new TestException("ToNotifyState state");
        }

        changeData(contextStatus, false, 0);
        actualStatus = actualStatus.next(contextStatus);
    }

    public AbstractStatus retrieve() throws TestException,Exception
    {
        List list = RuleStateBean.retrieveStatus();

        if (null == list)
        {
            return null;
        }

        if (0 == list.size())
        {
            return null;
        }

        SavedStatus savedState = (SavedStatus) list.get(0);

        return savedState.getState();
    }

    //To verify data insert into the table
    public void executeStoreDataFlow() throws TestException,Exception
    {
        AbstractStatus actualState = StatusRegistry.invalidStatus;
        ContextStatus contextState = new ContextStatus();
        contextState.setIdRule(new Integer(1));
        contextState.setIdVar(new Integer(0));

        //		TRANSIZIONE n. 1    	
        changeData(contextState, false, 0);
        actualState = actualState.next(contextState);

        //    	TRANSIZIONE n. 2
        changeData(contextState, true, 1000);
        actualState = actualState.next(contextState);

        actualState = null; //Simulazione crash
        actualState = retrieve(); //rispristino

        if (!(actualState instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        //		TRANSIZIONE n. 3   
        changeData(contextState, true, 1000);
        actualState = actualState.next(contextState);

        actualState = null; //Simulazione crash
        actualState = retrieve(); //rispristino

        if (!(actualState instanceof ActiveStatus))
        {
            throw new TestException(TEXT);
        }

        //		TRANSIZIONE n. 5   
        Thread.sleep(2000);
        changeData(contextState, true);
        actualState = actualState.next(contextState);

        actualState = null; //Simulazione crash
        actualState = retrieve(); //rispristino

        if (!(actualState instanceof ToManageStatus))
        {
            throw new TestException("ToNotifyState state");
        }

        //		TRANSIZIONE n. 7   
        Thread.sleep(2000);
        changeData(contextState, true);
        actualState = actualState.next(contextState);

        //		TRANSIZIONE n. 9   
        Thread.sleep(2000);
        changeData(contextState, true);
        actualState = actualState.next(contextState);

        //		TRANSIZIONE n. 10
        changeData(contextState, false);
        actualState = actualState.next(contextState);

        //		TRANSIZIONE n. 13
        changeData(contextState, false, 1000);
        actualState = actualState.next(contextState);
    }

    //  To verify data reload data from table after a crash
    public void executeCrashFlow() throws TestException
    {
    }

    public void changeData(ContextStatus contextState, boolean status)
    {
        contextState.setStatus(status);
        contextState.setActualTime(System.currentTimeMillis());
    }

    public void changeData(ContextStatus contextState, boolean status,
        long delay)
    {
        contextState.setStatus(status);
        contextState.setDelay(delay);
        contextState.setActualTime(System.currentTimeMillis());
    }
}
