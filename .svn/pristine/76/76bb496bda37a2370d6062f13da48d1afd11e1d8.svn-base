package com.carel.supervisor.dispatcher.action;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.SMemory;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;


public class SAction extends DispatcherAction
{
    public static final String SPLIT = ";";
    private String message = "";

    public SAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar, Timestamp start,
        Timestamp end)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, start, end);
    }

    protected String[] initializedRecepients(String recepient)
    {
        String[] recRet = new String[0];

        if (recepient != null)
        {
            recRet = recepient.split(SPLIT);
        }

        Arrays.sort(recRet);

        return recRet;
    }

    public void buildTemplate(String pathDir) throws Exception
    {
        String[] infoSender = this.getInfoSender(this.getIdSite());

        /*
         * If action due to alarm then ...
         * else send alive signal
         */
        if (this.isAlarm())
        {
            // Alarm SMS Format: Site_name START_STOP:MM/dd HH:mm Device_name:Alarm_description
        	// collect alarm fields: strSiteName, strLabel, strTimeStamp, strDeviceName, strAlarmDescription
        	int[] idVar = { ((Integer) this.getIdVariable().get(0)).intValue() };
            String[] infos = new String[0];
            Map description = VariableHelper.getDescriptions(infoSender[2], this.getIdSite(), idVar);

            String strSiteName = infoSender[0];
  
            if ((idVar != null) && (idVar.length > 0))
            {
                infos = (String[]) description.get(new Integer(idVar[0]));
            }

            String strLabel = "END";
            Timestamp time = (Timestamp) this.getEndTime().get(0);
            if( time == null ) {
            	strLabel = "START";
                time = (Timestamp) this.getStartTime().get(0);
            }
            String strTimeStamp = DateUtils.date2String(time, "MM/dd HH:mm");
            String strDeviceName = infos != null ? infos[1] : "DEVICE";
            String strAlarmDescription = infos != null ? infos[0] : "ALARM";
        	String[] astrFields = new String[] { strSiteName, strLabel, strTimeStamp, strDeviceName, strAlarmDescription };
        	int[] anSizes; 
            
            SMemory zMem = (SMemory) DispMemMgr.getInstance().readConfiguration("S");
            if( zMem.getProviderLb().equals("Direct SMS - Extended charset") ) {
            	// 70 chars
            	anSizes = new int[] { 10, 5, 11, 15, 25 };
            }
            else {
            	// 159 chars
            	anSizes = new int[] { 30, 5, 11, 45, 64 };
            }

            optimizeFieldSizes(astrFields, anSizes);
        	this.message = astrFields[0] + " " + astrFields[1] + ":" + astrFields[2] + " " + astrFields[3] + ":" + astrFields[4];
        }
        else
        {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            this.message = infoSender[0] + " life clock at " +
                DateUtils.date2String(time, "yyyy/MM/dd HH:mm:ss");
        }
    }

    
    private void optimizeFieldSizes(String[] astrFields, int[] anSizes)
    {
    	// compute differences
    	int anDiffs[] = new int[anSizes.length];
    	boolean bFlag = false;
    	for(int i = 0; i < anSizes.length; i++) {
    		anDiffs[i] = anSizes[i] - astrFields[i].length();
    		if( anDiffs[i] < 0 )
    			bFlag = true;
    	}
    	// abacus optimization
    	for(int i = 0; bFlag == true && i < anSizes.length; i++) {
			while( anDiffs[i] > 0 && bFlag ) {
				bFlag = false;
				for(int j = 0; j < anSizes.length; j++) {
   					if( anDiffs[j] < 0 && anDiffs[i] > 0 ) {
   						anSizes[j]++;
   						anDiffs[j]++;
   						anDiffs[i]--;
   					}
   		    		if( anDiffs[j] < 0 )
   		    			bFlag = true;
				}
    		}
    	}
    	// truncate fields
    	for(int i = 0; i < anSizes.length; i++) {
    		if( anDiffs[i] < 0 )
    			astrFields[i] = astrFields[i].substring(0, anSizes[i]); 
    	}
    }
    
    
    public int[] putActionInQueue() throws Exception
    {
        Integer keyact = ((Integer) this.getKeyAction().get(0));
        String actkey = String.valueOf(keyact.intValue());

        HSActionQBeanList actionQList = new HSActionQBeanList();

        HSActionQBean actionQ = null;
        Integer key = null;

        boolean allOk = true;

        String[] receiver = this.getRecepients();
        String path = DispatcherMgr.getInstance().getProviderPath() +
            DispatcherMgr.getInstance().getProviderName();

        if (receiver != null)
        {
            for (int i = 0; i < receiver.length; i++)
            {
                try
                {
                    key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                    actionQ = new HSActionQBean(key.intValue(), this.getNameSite(),
                            this.getIdSite(), this.getPriority(), this.getRetryNum(),
                            this.getRetryAfter(), this.getFisicDevice(), this.getTypeAction(), 1,
                            path, this.message, receiver[i], actkey);

                    actionQList.addAction(actionQ);
                }
                catch (Exception e)
                {
                    allOk = false;

                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }

            if (allOk)
            {
                allOk = actionQList.insertActions();
            }
        }

        if (allOk)
        {
            return new int[] { keyact.intValue() };
        }
        else
        {
            return new int[0];
        }
    }
 }
