package com.carel.supervisor.director;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.history.FDBQueue;
import com.carel.supervisor.field.Variable;


public class SetPollingVarList implements IDumpable
{
    private PollingVarList[] setPollingVarList = null;

    public SetPollingVarList(int size)
    {
        setPollingVarList = new PollingVarList[size];
    }

    public void setPollingVarList(int pos, PollingVarList pollingVarList)
    {
        setPollingVarList[pos] = pollingVarList;
    }

    public PollingVarList getPolligVarList(int pos)
    {
        return setPollingVarList[pos];
    }

    public List<Variable> extract(int freqGCD)
    {
    	ArrayList<Variable> list = new ArrayList<Variable>();
        Variable variable = null;

        //Manuel Gilioli
        for (int i = 0; i < setPollingVarList.length; i++)
        {
            setPollingVarList[i].updateCountFrequencyCicle(freqGCD);

            if (setPollingVarList[i].getCountFrequencyCicle() == 0)
            {
                for (int j = 0; j < setPollingVarList[i].size(); j++)
                {
                    variable = setPollingVarList[i].getVariable(j);
                    list.add(variable);
                } //for
            } //if
        } //for

        return list;

        //Manuel Gilioli		
    }

    public List<Variable> extractBeforeStop(int freqGCD){
    	ArrayList<Variable> list = new ArrayList<Variable>();
         Variable variable = null;
         for (int i = 0; i < setPollingVarList.length; i++)
         {
             for (int j = 0; j < setPollingVarList[i].size(); j++)
                 {
                     variable = setPollingVarList[i].getVariable(j);
                     list.add(variable);
                 } //for
            
         } //for

         return list;

    }//extractBeforeStop
    
    public void changeTime()
    {
    	Variable variable = null;
    	 for (int i = 0; i < setPollingVarList.length; i++)
         {
    		 setPollingVarList[i].resetCountFrequencyCicle();
             for (int j = 0; j < setPollingVarList[i].size(); j++)
             {
                 variable = setPollingVarList[i].getVariable(j);
                 variable.changeTime();
             }//for
         }//for
    }//changeTime
    
    
    
    public void changeTime(FDBQueue queue)
    {
    	Variable variable = null;
    	 for (int i = 0; i < setPollingVarList.length; i++)
         {
    		 setPollingVarList[i].resetCountFrequencyCicle();
             for (int j = 0; j < setPollingVarList[i].size(); j++)
             {
                 variable = setPollingVarList[i].getVariable(j);
                 variable.getSaver().createRecordToEnqueueBeforeStop(queue);
             }//for
         }//for
    }//changeTime
   
    
    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[FIELD]", this);
        dumpWriter.print("setPollingVarList", setPollingVarList);

        return dumpWriter;
    }

	
}
