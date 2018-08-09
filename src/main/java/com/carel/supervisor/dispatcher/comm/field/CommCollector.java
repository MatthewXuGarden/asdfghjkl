package com.carel.supervisor.dispatcher.comm.field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.xml.XMLNode;

public class CommCollector extends InitializableBase {
	private Map communicators = new HashMap();
	//private Map messages      = new HashMap();
	private static final String NAME = "name";
	private static final String VALUE = "value";
	private static final String TYPE = "type";
	private static final String CLASSCONN = "classConn";
	private static final String COMPONENT = "component";
	private static final String CONFIG = "config";
	public  CommCollector()
    {        
    }
    
   
	public void init(XMLNode xmlStatic) throws InvalidConfigurationException {
		// TODO Auto-generated method stub
		String name = null;
        String type = null;
        String classConn = null;
        String classProt = null;
        XMLNode xmlTmp = null;

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xmlTmp = xmlStatic.getNode(i);
            //Carica nella mappa tutte le interfacce di comunicazione
            if (xmlTmp.getNodeName().equals(COMPONENT))
            {
                name = retrieveAttribute(xmlTmp, NAME, "COME0001",
                        String.valueOf(i));
                classConn = retrieveAttribute(xmlTmp, CLASSCONN, "COME0001",
                        String.valueOf(i));
                type = retrieveAttribute(xmlTmp, TYPE, "COME0001",
                        String.valueOf(i));

                try
                {
                    ICommConnector commBase = (ICommConnector) FactoryObject.newInstance(classConn);
                    ((IInitializable) commBase).init(xmlTmp);
                    commBase.setName(name);                 
                    communicators.put(type, commBase);
                }
                catch (Exception e)
                {
                    FatalHandler.manage(this,
                        CoreMessages.format("COME0003", classConn), e);
                }
            }
            /*else if (xmlTmp.getNodeName().equals(CONFIG))
            {
                Properties properties = retrieveProperties(xmlTmp, NAME, VALUE,"FLDE0001");
                String tmp = retrieveAttribute(properties, "timeForceLog", "FLDE0001");

                try
                {
                    timeForceLog = Long.parseLong(tmp);
                }
                catch (NumberFormatException e)
                {
                	FatalHandler.manage(this, CoreMessages.format("FLDE0004", tmp), e);
                }
            }*/
        }
	}
	//Questa funzione legge i messaggi dei  sottosistemi
	public CommReturnCode[] getAllMessages(DevicesMessages deviceMessages)
	{
		if (null != communicators) 
		 {
			CommReturnCode[] returnCodes = new CommReturnCode[communicators.size()];
			//cancello la richiesta precedente
			
			Iterator iterator = communicators.keySet().iterator();
			String key = null;
			ICommConnector commConnector = null;
			int pos = 0;

			//LDAC: Strutturato in questo modo il ciclo, non si capisce chi è il ReturnCode di chi
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				commConnector = (ICommConnector) communicators.get(key);

				if (null != commConnector) {
					//in questo modo ogni sottosistema riempie la proria mappa di messaggi
					
					returnCodes[pos] = commConnector.getSubSystemMessages(deviceMessages);
					returnCodes[pos].setCommunicationName(commConnector.getName());

					if (returnCodes[pos].isExceptionPresent()) {
						//    			TO DO: Loggo l'eccezione ma vado avanti a caricare gli altri driver configurati
					} else if (returnCodes[pos].isErrorPresent()) {
						//TO DO: Loggo l'eccezione ma vado avanti sugli altri protocolli
					}
				}

				pos++;
			}

			return returnCodes;
		} else
			return null; 
	}
//	Questa funzione scrive i messaggi dei  sottosistemi
	public CommReturnCode[] setAllMessages(DevicesMessages deviceMessages)
	{
		if (null != communicators) 
		 {
			CommReturnCode[] returnCodes = new CommReturnCode[communicators.size()];
			//cancello la richiesta precedente
			
			Iterator iterator = communicators.keySet().iterator();
			String key = null;
			ICommConnector commConnector = null;
			int pos = 0;

			//LDAC: Strutturato in questo modo il ciclo, non si capisce chi è il ReturnCode di chi
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				commConnector = (ICommConnector) communicators.get(key);

				if (null != commConnector) {
					//in questo modo ogni sottosistema riempie la proria mappa di messaggi
					
					returnCodes[pos] = commConnector.setSubSystemMessages(deviceMessages);
					returnCodes[pos].setCommunicationName(commConnector.getName());

					if (returnCodes[pos].isExceptionPresent()) {
						//    			TO DO: Loggo l'eccezione ma vado avanti a caricare gli altri driver configurati
					} else if (returnCodes[pos].isErrorPresent()) {
						//TO DO: Loggo l'eccezione ma vado avanti sugli altri protocolli
					}
				}

				pos++;
			}

			return returnCodes;
		} else
			return null; 
	}

//	Questa funzione esegue i comandi del  sottosistema
	public CommReturnCode[] runDefaultCommand(Vector devices)
	{
		if (null != communicators) 
		 {
			CommReturnCode[] returnCodes = new CommReturnCode[communicators.size()];
			//cancello la richiesta precedente
			
			Iterator iterator = communicators.keySet().iterator();
			String key = null;
			ICommConnector commConnector = null;
			int pos = 0;

			//LDAC: Strutturato in questo modo il ciclo, non si capisce chi è il ReturnCode di chi
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				commConnector = (ICommConnector) communicators.get(key);

				if ((null != commConnector) && (pos<devices.size())){
					//in questo modo ogni sottosistema riempie la proria mappa di messaggi
					
					returnCodes[pos] = commConnector.runDefaultCommand((String)devices.elementAt(pos));
					returnCodes[pos].setCommunicationName(commConnector.getName());

					if (returnCodes[pos].isExceptionPresent()) {
						//    			TO DO: Loggo l'eccezione ma vado avanti a caricare gli altri driver configurati
					} else if (returnCodes[pos].isErrorPresent()) {
						//TO DO: Loggo l'eccezione ma vado avanti sugli altri protocolli
					}
				}

				pos++;
			}

			return returnCodes;
		} else
			return null; 
	}

	public CommReturnCode[] loadDLLCommunication()
	{
		 if (null != communicators) 
		 {
			CommReturnCode[] returnCodes = new CommReturnCode[communicators.size()];
			Iterator iterator = communicators.keySet().iterator();
			String key = null;
			ICommConnector commConnector = null;
			int pos = 0;

			//LDAC: Strutturato in questo modo il ciclo, non si capisce chi è il ReturnCode di chi
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				commConnector = (ICommConnector) communicators.get(key);

				if (null != commConnector) {
					returnCodes[pos] = commConnector.loadDLLSubsystem();
					returnCodes[pos].setCommunicationName(commConnector.getName());

					if (returnCodes[pos].isExceptionPresent()) {
						//    			TO DO: Loggo l'eccezione ma vado avanti a caricare gli altri driver configurati
					} else if (returnCodes[pos].isErrorPresent()) {
						//TO DO: Loggo l'eccezione ma vado avanti sugli altri protocolli
					}
				}

				pos++;
			}

			return returnCodes;
		} else

			return null;
	}
	 
	public  CommReturnCode[] initSubSystem()
	{
		if (null != communicators)
        {
			CommReturnCode[] returnCodes = new CommReturnCode[communicators.size()];
            Iterator iterator = communicators.keySet().iterator();
            String key = null;
            ICommConnector commConnector = null;
            int pos = 0;

            while (iterator.hasNext())
            {
                key = (String) iterator.next();
                commConnector = (ICommConnector) communicators.get(key);

                if ((null != commConnector) &&
                        (!commConnector.isBlockingError()))
                {
                    if (!commConnector.isBlockingError())
                    {
	                	returnCodes[pos] = commConnector.initSubSystem();
	                    returnCodes[pos].setCommunicationName(commConnector.getName());
	
	                    if (returnCodes[pos].isExceptionPresent())
	                    {
	                        //    			TO DO: Loggo l'eccezione ma vado avanti a caricare gli altri driver configurati
	                    }
	                    else if (returnCodes[pos].isErrorPresent())
	                    {
	                        //TO DO: Loggo l'eccezione ma vado avanti sugli altri protocolli
	                    }
                    }
                }

                pos++;
            }

            return returnCodes;
        }
        else

            return null;
	}
}
