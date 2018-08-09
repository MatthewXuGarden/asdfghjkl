package com.carel.supervisor.dispatcher.comm.field.impl;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.comm.field.CommConfig;
import com.carel.supervisor.dispatcher.comm.field.CommReturnCode;
import com.carel.supervisor.dispatcher.comm.field.DeviceIDs;
import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.ICommConfig;
import java.util.Calendar;
import java.util.Vector;


public class CommDialUp extends CommBase
{
    private int systemID = DeviceIDs.DIAL;

    /*static{
            System.loadLibrary("commdial");


    }*/
    public int getCommunicationID()
    {
        return systemID;
    }

    //Qui devo avere la mappa dei messaggi che arrvivano dal campo.
    public boolean isBlockingError()
    {
        return blockingError;
    }

    public synchronized CommReturnCode initSubSystem()
    {
        short returnCode = 0;

        try
        {
            //System.loadLibrary("commrasservice");
            returnCode = cinitSubSystem(); //Inizializza la dll

            if (0 != returnCode) //Error during dll load
            {
                blockingError = true;
                EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                    "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll",
                    getName());

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("COME0008",
                        "dll ras  init error.", String.valueOf(returnCode)));

                return new CommReturnCode(returnCode, "", true);
            }
            else
            {

                return new CommReturnCode(returnCode);
            }
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public synchronized CommReturnCode doneSubSystem()
    {
        short returnCode = 0;

        try
        {
            //System.loadLibrary("commrasservice");
            returnCode = cdoneSubSystem(); //Inizializza la dll

            if (0 != returnCode) //Error during dll load
            {
                blockingError = true;
                EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                    "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll",
                    getName());

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("COME0007",
                        "dll ras  done error.", String.valueOf(returnCode)));

                return new CommReturnCode(returnCode, "", true);
            }
            else
            {

                return new CommReturnCode(returnCode);
            }
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public synchronized CommReturnCode runDefaultCommand(String device)
    {
        short returnCode = 0;

        try
        {
            returnCode = crunDefaultCommand(device); //Invia il comando al sottosistema

            if (0 != returnCode) //Error during dll load
            {
                EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                    "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll",
                    getName());

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("COME0009",
                        "unable to send command to device " + device,
                        String.valueOf(returnCode)));

                return new CommReturnCode(returnCode, "", true);
            }
            else
            {

                return new CommReturnCode(returnCode);
            }
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public synchronized CommReturnCode loadDLLSubsystem()
    {
        try
        {
            //in seguito la dll da caricare sarà quella di interfaccia, la quale a sua volta caricherà tutte le
            //dll dal file system che iniziano con comm* e che non sono essa stessa.
            //System.loadLibrary("commdial");
            short returnCode = 0;
            returnCode = cloadSubSystem();

            return new CommReturnCode((short) 0);
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public synchronized CommReturnCode unloadDLLSubsystem()
    {
        short returnCode = 0;

        try
        {
            //in seguito la dll da caricare sarà quella di interfaccia, la quale a sua volta caricherà tutte le
            //dll dal file system che iniziano con comm* e che non sono essa stessa.
            returnCode = cunloadSubSystem();

            return new CommReturnCode((short) returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public synchronized CommReturnCode getConfigObject(ICommConfig conf)
    {
        short returnCode = 0;

        try
        {
            returnCode = cgetConfigObject(conf); //Invia il comando al sottosistema

            if (0 != returnCode) //Error during dll load
            {
                EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                    "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll",
                    getName());

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("COME0010",
                        "unable to read configuration from subsystem",
                        String.valueOf(returnCode)));

                return new CommReturnCode(returnCode, "", true);
            }
            else
            {

                return new CommReturnCode(returnCode);
            }
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public synchronized CommReturnCode getSubSystemMessages(
        DevicesMessages deviceMessages)
    {
        short returnCode = 0;

        try
        {
            returnCode = cgetSubSystemMessages(deviceMessages); //Invia il comando al sottosistema

            /*if (0 != returnCode) //Error during dll load
            {

                EventMgr.getInstance().log(new Integer(1), "COMMUNICATION", "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("COME0011", "unable to read configuration from subsystem", String.valueOf(returnCode)));
                return new CommReturnCode(returnCode, "", true);
            }
            else*/
            return new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public synchronized CommReturnCode setSubSystemMessages(
        DevicesMessages deviceMessages)
    {
        short returnCode = 0;

        try
        {
            returnCode = csetSubSystemMessages(deviceMessages); //Invia il comando al sottosistema

            if (0 != returnCode) //Error during dll load
            {
                EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                    "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll",
                    getName());

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("COME0011",
                        "unable to read configuration from subsystem",
                        String.valueOf(returnCode)));

                return new CommReturnCode(returnCode, "", true);
            }
            else
            {

                return new CommReturnCode(returnCode);
            }
        }
        catch (Throwable e)
        {
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), "COMMUNICATION",
                "CONNECTION", EventDictionary.TYPE_ERROR, "loaddll", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new CommReturnCode(e);
        }
    }

    public static void main(String[] args)
    {
        CommDialUp cs = new CommDialUp();
        DevicesMessages devicemsg;
        DeviceMessage dm0;
        String deviceToSend = "";
        boolean loop = true;
        boolean first = true;
        boolean connected = false;
        int ret = 0;

        cs.loadMe();

        if ((ret = cs.loadDLLSubsystem().getReturnCode()) != 0)
        {
            System.out.println("Unable to load module commdialer ErrCode = " +
                Integer.valueOf(ret).toString());

            return;
        }

        Calendar Timeout = Calendar.getInstance();
        Timeout.add(Calendar.SECOND, 300);

        if (cs.initSubSystem().getReturnCode() == 0)
        {
            devicemsg = new DevicesMessages();

            short res = cs.cgetSubSystemMessages(devicemsg);
            dm0 = new DeviceMessage();

            boolean hasMore = false;

            while ((hasMore = devicemsg.HasMore(dm0)) || loop)
            {
                if (hasMore)
                {
                    System.out.println(dm0.message_s);

                    //dopo l'init ok esegue una sola volta il caricamento dei messaggi e l'invio dell'sms
                    if ((dm0.message_id == CommID.SERVICE_DIAL) &&
                            (dm0.message_sub == CommID.SERVICE_DIAL_INIT_OK))
                    {
                        ICommConfig commc = new CommConfig();

                        if (cs.getConfigObject(commc).getReturnCode() == 0)
                        {
                            Vector devices = commc.getCommunicationConfig();

                            for (int j = 0; j < devices.size(); j++)
                            {
                                String device = (String) devices.elementAt(j);
                                System.out.println(device);

                                if (device.charAt(0) == 'A') //cerco il modem D-Link
                                {
                                    deviceToSend = device;
                                }
                            } //fine for
                        } //fine if
                    }

                    if ((deviceToSend != "") && first)
                    {
                        first = false;

                        DevicesMessages dms = new DevicesMessages();
                        DeviceMessage dm1 = new DeviceMessage();
                        System.out.println("Preparing configuration ....");
                        dm1.message_id = CommID.SERVICE_DIAL;
                        dm1.message_sub = CommID.SERVICE_DIAL_USER;
                        dm1.message_s = "PVRemote";
                        dms.add(dm1);
                        dm1 = new DeviceMessage();
                        dm1.message_id = CommID.SERVICE_DIAL;
                        dm1.message_sub = CommID.SERVICE_DIAL_PASSWORD;
                        dm1.message_s = "PD35010";
                        dms.add(dm1);
                        dm1 = new DeviceMessage();
                        dm1.message_id = CommID.SERVICE_DIAL;
                        dm1.message_sub = CommID.SERVICE_DIAL_TEL;
                        dm1.message_s = "42";
                        dms.add(dm1);
                        dm1 = new DeviceMessage();
                        dm1.message_id = CommID.SERVICE_DIAL;
                        dm1.message_sub = CommID.SERVICE_DIAL_REQ;
                        dm1.message_s = "";
                        dms.add(dm1);

                        if (cs.setSubSystemMessages(dms).getReturnCode() >= 0)
                        {
                            System.out.println("...Done");
                        }
                        else
                        {
                            System.out.println("...error in send configuration");
                        }

                        System.out.println("Dialing...");

                        if (cs.runDefaultCommand(deviceToSend).getReturnCode() != 0)
                        {
                            loop = false;
                        }
                    }
                }

                if (((dm0.message_id == CommID.SERVICE_DIAL) &&
                        (dm0.message_sub == CommID.SERVICE_DIAL_CONNECTED)))
                {
                    connected = true;
                    System.out.println("Modem Connected.");
                }

                if (((dm0.message_id == CommID.SERVICE_DIAL) &&
                        (dm0.message_sub == CommID.SERVICE_DIAL_ERROR)))
                {
                    System.out.println("Modem error.");
                    connected = true;
                }

                if (connected && !Timeout.after(Calendar.getInstance()))
                {
                    System.out.println("Trascorsi tre minuti.");

                    DevicesMessages dms = new DevicesMessages();
                    DeviceMessage dm1 = new DeviceMessage();

                    dm1 = new DeviceMessage();
                    dm1.message_id = CommID.SERVICE_DIAL;
                    dm1.message_sub = CommID.SERVICE_DIAL_HANG_REQ;
                    dm1.message_s = "";
                    dms.add(dm1);

                    if (cs.setSubSystemMessages(dms).getReturnCode() >= 0)
                    {
                        System.out.println("...Done");
                        cs.runDefaultCommand(deviceToSend).getReturnCode();
                        loop = false;
                        connected = false;
                    }
                    else
                    {
                        System.out.println("...error in send configuration");
                    }
                }

                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                devicemsg = new DevicesMessages();
                res = cs.getSubSystemMessages(devicemsg).getReturnCode(); //prendo il prossimo messaggio	
            }
        }

        cs.doneSubSystem();
        cs.unloadDLLSubsystem();
    }
}
