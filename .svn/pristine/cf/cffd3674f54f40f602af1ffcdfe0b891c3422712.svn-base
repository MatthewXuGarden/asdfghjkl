package com.carel.supervisor.dispatcher.comm.external;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherID;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;


public abstract class External
{
    protected static final int START = -99;
    private String device = "";
    private String type = "";
    protected String pathex = "";
    private int id = 0;

    public External(int id, String device, String type)
    {
        this.id = id;
        this.device = device;
        this.type = type;
        this.pathex = DispatcherMgr.getInstance().getServicesPath();
    }

    protected abstract String[] getParams();

    protected abstract String getLogFile();

    public boolean send()
    {
        int returnCode = START;
        ScriptInvoker inv = new ScriptInvoker();

        try
        {
            String[] param = getParams();
            returnCode = inv.execute(param, this.pathex + getLogFile());
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return this.decode(returnCode);
    }

    protected boolean decode(int retcode)
    {
        boolean ris = false;

        switch (id)
        {
        case CommID.SERVICE_FAX:
            ris = decodeFax(retcode);

            break;

        case CommID.SERVICE_SMS:
            ris = decodeSms(retcode);

            break;

        case CommID.SERVICE_DIALER:
            ris = decodeMail(retcode);

            break;

        case CommID.SERVICE_DIAL:
            ris = decodeDial(retcode);

            break;
        }

        return ris;
    }

    private boolean decodeFax(int retcode)
    {
        boolean ris = false;

        switch (retcode)
        {
	        case START:
	            insertEvent(type, "D047");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("START_ERROR"));
	            break;
	
	        case DispatcherID.PARAM_NUM_ERR:
	            insertEvent(type, "D047");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("PARAM_NUM_ERR"));
	            break;
	
	        case DispatcherID.TIMEOUT_SERVICE:
	            insertEvent(type, "D044");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("TIMEOUT_SERVICE"));
	            break;
	
	        case DispatcherID.COMMBASE_NOTLOADED:
	            insertEvent(this.device, "D033");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("COMMBASE_NOTLOADED"));
	            break;
	
	        case DispatcherID.DLLFAX_NOTLOADED:
	            insertEvent(this.device, "D033");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("DLLFAX_NOTLOADED"));
	            break;
	
	        case DispatcherID.CMDFAX_NOTRUN:
	            insertEvent(this.device, "D042");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("CMDFAX_NOTRUN"));
	            break;
	
	        case CommID.FEI_BUSY:
	        case CommID.SERVICE_FAX_LINE_ALREADY_IN_USE:
	        	LoggerMgr.getLogger(" - FAX Service - ").error(new String("FEI_BUSY"));
	            insertEvent(type, "D034");
	            break;
	
	        case CommID.FEI_NO_ANSWER:
	            insertEvent(type, "D036");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("FEI_NO_ANSWER"));
	            break;
	
	        case CommID.FEI_BAD_ADDRESS:
	            insertEvent(type, "D037");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("FEI_BAD_ADDRESS"));
	            break;
	
	        case CommID.FEI_NO_DIAL_TONE:
	            insertEvent(type, "D035");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("FEI_NO_DIAL_TONE"));
	            break;
	
	        case CommID.FEI_DISCONNECTED:
	            insertEvent(type, "D038");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("FEI_DISCONNECTED"));
	            break;
	
	        case CommID.FEI_ABORTING:
	            insertEvent(type, "D051");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("FEI_ABORTING"));
	            break;
	           
	            
	        case CommID.FEI_FATAL_ERROR:
	        case CommID.FEI_NOT_FAX_CALL:
	        case CommID.FEI_CALL_DELAYED:
	        case CommID.FEI_CALL_BLACKLISTED:
	            insertEvent(type, "D039");
	            LoggerMgr.getLogger(" - FAX Service - ").error(new String("FEI_NOT_FAX_CALL"));
	            break;
	
	        case CommID.FEI_COMPLETED:
	            ris = true;
	            break;
	
	        default:
	            break;
        }

        return ris;
    }

    private boolean decodeSms(int retcode)
    {
        boolean ris = false;

        switch (retcode)
        {
        case START:
            insertEvent(type, "D047");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("START_ERROR"));
            break;

        case DispatcherID.PARAM_NUM_ERR:
            insertEvent(type, "D047");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("PARAM_NUM_ERR"));
            break;

        case DispatcherID.TIMEOUT_SERVICE:
            insertEvent(type, "D044");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("TIMEOUT_SERVICE"));
            break;

        case DispatcherID.COMMBASE_NOTLOADED:
            insertEvent(this.device, "D033");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("COMMBASE_NOTLOADED"));
            break;

        case DispatcherID.DLLSMS_NOTLOADED:
            insertEvent(this.device, "D033");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("DLLSMS_NOTLOADED"));
            break;

        case DispatcherID.CMDSMS_NOTRUN:
            insertEvent(this.device, "D042");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("CMDSMS_NOTRUN"));
            break;

        case CommID.SERVICE_SMS_LINE_ALREADY_IN_USE:
            insertEvent(type, "D034"); //TODO: modificare traduzione
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("SERVICE_SMS_LINE_ALREADY_IN_USE"));
            break;

        case CommID.SERVICE_SMS_SEND_ERROR:
            insertEvent(this.device, "D039");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("SERVICE_SMS_SEND_ERROR"));
            break;

        case CommID.SERVICE_SMS_EXCEPTION:
            insertEvent(this.device, "D039");
            LoggerMgr.getLogger(" - SMS Service - ").error(new String("SERVICE_SMS_EXCEPTION"));
            break;

        case CommID.SERVICE_SMS_SENT:
            ris = true;
            break;

        case DispatcherID.PING_OK:
            ris = true;
            break;

        case DispatcherID.PING_KO:
        	LoggerMgr.getLogger(" - SMS Service - ").error(new String("PING_KO"));
            break;

        default:
            break;
        }

        return ris;
    }

    private boolean decodeMail(int retcode)
    {
        boolean ris = false;

        switch (retcode)
        {
        case START:
            insertEvent(type, "D047");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("START_ERROR"));
            break;

        case DispatcherID.PARAM_NUM_ERR:
            insertEvent(type, "D047");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("PARAM_NUM_ERR"));
            break;

        case DispatcherID.TIMEOUT_SERVICE:
            insertEvent(type, "D044");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("TIMEOUT_SERVICE"));
            break;

        case DispatcherID.COMMBASE_NOTLOADED:
            insertEvent(this.device, "D033");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("COMMBASE_NOTLOADED"));
            break;

        case DispatcherID.DLLMAIL_NOTLOADED:
            insertEvent(this.device, "D033");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("DLLMAIL_NOTLOADED"));
            break;

        case DispatcherID.CMDMAIL_NOTRUN:
            insertEvent(this.device, "D042");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("CMDMAIL_NOTRUN"));
            break;

        case DispatcherID.MAIL_NOT_SEND:
            insertEvent(this.device, "D048");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("MAIL_NOT_SEND"));
            break;

        case CommID.SERVICE_DIALER_ERROR:
            insertEvent(type, "D039");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("SERVICE_DIALER_ERROR"));
            break;

        case CommID.SERVICE_DIALER_LINE_USED_ERROR:
            insertEvent(type, "D034");
            LoggerMgr.getLogger(" - MAIL Service - ").error(new String("SERVICE_DIALER_LINE_USED_ERROR"));
            break;

        case CommID.SERVICE_DIALER_CONNECTED:
            ris = true;

            break;

        default:
            break;
        }

        return ris;
    }

    private boolean decodeDial(int retcode)
    {
        boolean ris = false;

        switch (retcode)
        {
        case START:
            insertEvent(type, "D047");
            LoggerMgr.getLogger(" - DIAL Service - ").error(new String("START_ERROR"));
            break;

        case DispatcherID.PARAM_NUM_ERR:
            insertEvent(type, "D047");
            LoggerMgr.getLogger(" - DIAL Service - ").error(new String("PARAM_NUM_ERR"));
            break;

        case DispatcherID.TIMEOUT_SERVICE:
            insertEvent(type, "D044");
            LoggerMgr.getLogger(" - DIAL Service - ").error(new String("TIMEOUT_SERVICE"));
            break;

        case DispatcherID.CMDDIAL_NOTRUN:
            insertEvent(this.device, "D042");
            LoggerMgr.getLogger(" - DIAL Service - ").error(new String("CMDDIAL_NOTRUN"));
            break;

        case CommID.SERVICE_DIAL_ERROR:
            insertEvent(this.device, "D049");
            LoggerMgr.getLogger(" - DIAL Service - ").error(new String("SERVICE_DIAL_ERROR"));
            break;

        case CommID.SERVICE_DIAL_LINE_USED_ERROR:
            insertEvent(type, "D034");
            LoggerMgr.getLogger(" - DIAL Service - ").error(new String("SERVICE_DIAL_LINE_USED_ERROR"));
            break;

        case CommID.SERVICE_DIAL_CONNECTED:
            ris = true;
            
            break;

        case CommID.SERVICE_DIAL_HANG_OK:
            ris = true;

            break;

        case CommID.SERVICE_DIAL_IP_REMOTE:
            ris = true;

            break;

        case CommID.SERVICE_DIAL_IP_LOCAL:
            ris = true;

            break;
        }

        return ris;
    }
    
    private void insertEvent(String typeAction, String IdMsg)
    {
        Object[] p = null;

        try
        {
            if ((typeAction != null) && (typeAction.length() > 1))
            {
                p = new Object[] { typeAction };
            }
            else
            {
                p = new Object[] { DispatcherMgr.getInstance().decodeActionType(typeAction) };
            }

            EventMgr.getInstance().log(EventDictionary.TYPE_ERROR, "Dispatcher", "Action",
                EventDictionary.TYPE_ERROR, IdMsg, p);
        }
        catch (Exception e)
        {
        }
    }
}
