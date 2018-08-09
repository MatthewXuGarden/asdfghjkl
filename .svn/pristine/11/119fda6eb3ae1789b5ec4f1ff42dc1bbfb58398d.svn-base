package com.carel.supervisor.dispatcher.comm.field.impl;

import com.carel.supervisor.dispatcher.comm.field.CommReturnCode;
import com.carel.supervisor.dispatcher.comm.field.DeviceIDs;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.ICommConfig;


public class CommPingService extends CommBase
{
    private int systemID = DeviceIDs.PING;
    
    public boolean isBlockingError() {
        return blockingError;
    }

    public int getCommunicationID() {
        return systemID;
    }
    
    public synchronized CommReturnCode loadDLLSubsystem()
    {
    	CommReturnCode commRetCode = null;
        try
        {
            short returnCode = 0;
            returnCode = cloadSubSystem();
            commRetCode = new CommReturnCode((short) returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            e.printStackTrace();
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }
    
    public synchronized CommReturnCode unloadDLLSubsystem()
    {
        CommReturnCode commRetCode = null;
        try
        {
        	short returnCode = 0;
            returnCode = cunloadSubSystem();
            commRetCode = new CommReturnCode((short) returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }
    
    public synchronized CommReturnCode initSubSystem()
    {
    	CommReturnCode commRetCode = null;
        try
        {
            short returnCode = cinitSubSystem();

            if(0 != returnCode)
            {
                blockingError = true;
                commRetCode = new CommReturnCode(returnCode, "", true);
            }
            else
            	commRetCode = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }
    
    public synchronized CommReturnCode doneSubSystem()
    {
    	CommReturnCode commRetCode = null;
        try
        {
            short returnCode = cdoneSubSystem();

            if (0 != returnCode) 
            {
                blockingError = true;
                commRetCode = new CommReturnCode(returnCode, "", true);
            }
            else
            	commRetCode = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }

    public synchronized CommReturnCode runDefaultCommand(String device)
    {
    	CommReturnCode commRetCode = null;

        try
        {
            short returnCode = crunDefaultCommand(device);

            if (0 != returnCode)
            	commRetCode = new CommReturnCode(returnCode, "", true);
            else
            	commRetCode = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }

    public synchronized CommReturnCode getConfigObject(ICommConfig conf)
    {
    	CommReturnCode commRetCode = null;
        try
        {
        	short returnCode = cgetConfigObject(conf);

            if(0 != returnCode)
            	commRetCode = new CommReturnCode(returnCode, "", true);
            else
            	commRetCode = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }

    public synchronized CommReturnCode getSubSystemMessages(DevicesMessages deviceMessages)
    {
    	CommReturnCode commRetCode = null;
        try
        {
        	short returnCode = cgetSubSystemMessages(deviceMessages);
        	commRetCode = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }

    public synchronized CommReturnCode setSubSystemMessages(DevicesMessages deviceMessages)
    {
    	CommReturnCode commRetCode = null;
        try
        {
        	short returnCode = csetSubSystemMessages(deviceMessages);

            if (0 != returnCode)
            	commRetCode = new CommReturnCode(returnCode, "", true);
            else
            	commRetCode = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            commRetCode = new CommReturnCode(e);
        }
        return commRetCode;
    }
}
