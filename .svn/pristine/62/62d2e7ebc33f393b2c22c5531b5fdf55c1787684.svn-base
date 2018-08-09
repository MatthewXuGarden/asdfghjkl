package com.carel.supervisor.dispatcher.comm;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dispatcher.comm.field.CommCollector;
import com.carel.supervisor.dispatcher.comm.field.CommReturnCode;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import java.util.Vector;


public class CommConnectorMgr implements IInitializable
{
    private static CommConnectorMgr meCommunicationConnectorMgr = new CommConnectorMgr();
    private static boolean initialized = false;
    private static boolean loaded = false;
    private Logger logger = null;
    private CommCollector commCollector = null;

    private CommConnectorMgr()
    {
        logger = LoggerMgr.getLogger(this.getClass());
    }

    public static CommConnectorMgr getInstance()
    {
        return meCommunicationConnectorMgr;
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        if (!initialized)
        {
            commCollector = new CommCollector();

            //qui carica le classi di comunicazione
            commCollector.init(xmlStatic);
            initialized = true;
        }
    }

    //	This method must call from a client in order to distinguish configuration step (method init) 
    //	from drivers initialization 
    public synchronized void load() throws Exception
    {
        logger.info("[COMMUNICATION LOADING PROTOCOLS]");

        //CARICO TUTTI I COMMUNICATION DEVICES
        CommReturnCode[] returnCode = commCollector.loadDLLCommunication();

        for (int i = 0; i < returnCode.length; i++)
        {
            if (null == returnCode[i])
            {
                logger.fatal("ERRORE CARICAMENTO");
            }
            else
            {
                if (returnCode[i].isExceptionPresent())
                {
                    logger.fatal("ERRORE CARICAMENTO", returnCode[i].getException());
                }

                if (returnCode[i].isErrorPresent())
                {
                    logger.warn("ERRORE CARICAMENTO: " +
                        String.valueOf(returnCode[i].getReturnCode()));
                }
            }
        }

        logger.info("[COMUNICATOR LOADED COMMUNICATION PROTOCOLS]");
        logger.info("[COMUNICATOR INITIALIZING DRIVERS]");
        returnCode = commCollector.initSubSystem();

        for (int i = 0; i < returnCode.length; i++)
        {
            if (null == returnCode[i])
            {
                logger.fatal("ERRORE INIZIALIZZAZIONE");
            }
            else
            {
                if (returnCode[i].isExceptionPresent())
                {
                    logger.fatal("ERRORE INIZIALIZZAZIONE", returnCode[i].getException());
                }

                if (returnCode[i].isErrorPresent())
                {
                    logger.warn("ERRORE INIZIALIZZAZIONE: " +
                        String.valueOf(returnCode[i].getReturnCode()));
                }
            }
        }

        logger.info("[COMUNICATOR DRIVERS INITIALIZED]");
        logger.info("[COMMUNICATION MANAGER STARTED]");
        loaded = true;
    }

    public synchronized boolean isLoaded()
    {
        return loaded;
    }

    public synchronized boolean getAllMessages(DevicesMessages deviceMessages)
    {
        boolean thereismessages = false;

        CommReturnCode[] returnCode = commCollector.getAllMessages(deviceMessages);

        for (int i = 0; i < returnCode.length; i++)
        {
            if (null == returnCode[i])
            {
                logger.fatal("ERRORE LETTURA MESSAGGI");
            }
            else
            {
                if (returnCode[i].isExceptionPresent())
                {
                    logger.fatal("ERRORE LETTURA MESSAGGI", returnCode[i].getException());
                }

                if (returnCode[i].isErrorPresent())
                {
                    logger.warn("ERRORE LETTURA MESSAGGI: " +
                        String.valueOf(returnCode[i].getReturnCode()));
                }

                if (returnCode[i].getReturnCode() == 1)
                {
                    thereismessages = true; //almeno un sotto sistema ha dei messaggi da consumare.
                }
            }
        }

        return thereismessages;
    }

    public synchronized void setAllMessages(DevicesMessages deviceMessages)
    {
        CommReturnCode[] returnCode = commCollector.setAllMessages(deviceMessages);

        for (int i = 0; i < returnCode.length; i++)
        {
            if (null == returnCode[i])
            {
                logger.fatal("ERRORE SCRITTURA MESSAGGI");
            }
            else
            {
                if (returnCode[i].isExceptionPresent())
                {
                    logger.fatal("ERRORE SCRITTURA MESSAGGI", returnCode[i].getException());
                }

                if (returnCode[i].isErrorPresent())
                {
                    logger.warn("ERRORE SCRITTURA MESSAGGI: " +
                        String.valueOf(returnCode[i].getReturnCode()));
                }
            }
        }
    }

    public synchronized void runDefaultCommand(Vector devices)
    {
        CommReturnCode[] returnCode = commCollector.runDefaultCommand(devices);

        for (int i = 0; i < returnCode.length; i++)
        {
            if (null == returnCode[i])
            {
                logger.fatal("ERRORE SCRITTURA MESSAGGI");
            }
            else
            {
                if (returnCode[i].isExceptionPresent())
                {
                    logger.fatal("ERRORE SCRITTURA MESSAGGI", returnCode[i].getException());
                }

                if (returnCode[i].isErrorPresent())
                {
                    logger.warn("ERRORE SCRITTURA MESSAGGI: " +
                        String.valueOf(returnCode[i].getReturnCode()));
                }
            }
        }
    }
}
