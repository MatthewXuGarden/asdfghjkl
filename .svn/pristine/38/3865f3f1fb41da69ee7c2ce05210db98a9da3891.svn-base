package com.carel.supervisor.director;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.math.MathExt;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfoList;
import com.carel.supervisor.field.IRetriever;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;
import com.carel.supervisor.field.dataconn.DataCollector;


public class SetPollingVarListFactory
{
    private Map varMap = new HashMap();
    private int freqGCD = 0;
    private int maxFreq = 0;
    private Logger logger = null;

    public SetPollingVarListFactory()
    {
        logger = LoggerMgr.getLogger(this.getClass());
    }

    public void init()
    {
        init(128);
    }

    public void init(int maxStepNumber)
    {
        VariableInfoList variableInfoList = (VariableInfoList) DataConfigMgr.getInstance()
                                                                            .getConfig("cfvars");
        VariableInfo variableInfo = null;

        for (int i = 0; i < variableInfoList.size(); i++)
        {
            variableInfo = variableInfoList.get(i);

            Variable variable = new Variable(variableInfo, maxStepNumber);

            if (null == variableInfo.getDeviceInfo())
            {
                if (variableInfo.isLogic())
                {
	                int frequency = variableInfo.getFrequency().intValue();
	                addPollingVar(frequency, variable);
	        		VariableMgr.getInstance().add(variable);
                }
                else
                {
                	logger.warn("DEVICE FOR VARIABLE " + variableInfo.toString() +
                    " NOT ALLOWED");
                }
            }
            else
            {
                if ((null == variableInfo.getDeviceInfo().getLineInfo()) &&
                        (!variableInfo.isLogic()))
                {
                    logger.warn("LINE FOR VARIABLE " + variableInfo.toString() +
                        " NOT ALLOWED");
                }
                else
                {
                    int frequency = variableInfo.getFrequency().intValue();

                    if (frequency <= 0)
                    {
                        logger.warn("VARIABLE " + variableInfo.getId() +
                            " FREQUENCY: " + frequency);
                    }
                    else
                    {
                        if (variableInfo.getType() != VariableInfo.TYPE_ALARM)
                        {
                            addPollingVar(frequency, variable);
                            VariableMgr.getInstance().add(variable);
                        }
                        else //We remove alarms not to notify
                        {
                        	if (variableInfo.isActive())
                    		{
                        		addPollingVar(frequency, variable);
                        		VariableMgr.getInstance().add(variable);
                    		}
                        }

                        //Frequency in seconds
                    }
                } //else
            } //else
        } //for
    } //init

    public void assign(DataCollector dataCollector, Map functions)
    {
        VariableInfoList variableInfoList = (VariableInfoList) DataConfigMgr.getInstance()
                                                                            .getConfig("cfvars");
        VariableInfo variableInfo = null;
        VariableMgr vrbMgr = VariableMgr.getInstance();
        Variable variable = null;
        IRetriever retriever = null;

        for (int i = 0; i < variableInfoList.size(); i++)
        {
            variableInfo = variableInfoList.get(i);
            variable = vrbMgr.getById(variableInfo.getId());

            if (variableInfo.isLogic())
            {
                retriever = (IRetriever) functions.get(variableInfo.getId());
            }
            else
            {
            	// Travaglin - Winery
            	// Modifiche per gestione allarmi tecnici.
            	if(variableInfo.getAddressIn().intValue() == -1)
            	{
            		retriever = findTechVariable(variableInfo);
            	}
            	else
            		retriever = (IRetriever) dataCollector.getDataConnector(variableInfo.getProtocolType());
            }

            if (null != variable)
            {
                variable.setRetriever(retriever);
            }
        }
    }

    public SetPollingVarList createSetPollingVarList()
    {
        SetPollingVarList setPollingVarList = new SetPollingVarList(varMap.size());

        if (varMap.size() > 0)
        {
            // Estraggo tutte le frequenze
            int[] freq = new int[varMap.size()];
            Iterator iterator = varMap.keySet().iterator();
            int i = -1;

            while (iterator.hasNext())
            {
                i++;
                freq[i] = ((Integer) iterator.next()).intValue();
            }

            freqGCD = MathExt.gcd(freq);

            //Le ordino dalla più piccola alla più grande
            Arrays.sort(freq);

            maxFreq = 1;

            //Manuel Gilioli
            maxFreq = freq[freq.length - 1];

            /*for (int f = 0; f < freq.length; f++)
                maxFreq *= freq[f];*/

            //Manuel Gilioli
            //creo il SetPollingVarList usando tale ordine
            for (i = 0; i < freq.length; i++)
            {
                setPollingVarList.setPollingVarList(i,
                    (PollingVarList) varMap.get(new Integer(freq[i])));
            }
        }

        return setPollingVarList;
    }

    public int getGCDFreq()
    {
        return freqGCD;
    }

    public int getMaxFreq()
    {
        return maxFreq;
    }

    //Method protected only for debug purpouse
    protected void addPollingVar(int frequency, Variable variable)
    {
        PollingVarList pollingVarList = (PollingVarList) varMap.get(new Integer(
                    frequency));

        if (null == pollingVarList)
        {
            pollingVarList = new PollingVarList(frequency);
            varMap.put(new Integer(frequency), pollingVarList);
        }

        pollingVarList.addVarData(variable);
    }
    
    private IRetriever findTechVariable(VariableInfo variableInfo)
    {
    	IRetriever customRetriever = null;
    	return customRetriever;
    }
}
