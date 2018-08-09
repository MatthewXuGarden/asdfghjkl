package com.carel.supervisor.director;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.field.Variable;
import java.util.*;


public class PollingVarList implements IDumpable
{
    private int frequency = 0;
    private int countFrequencyCicle = -1; //è importante che sia a -1 per il primo ciclo
    private List varDataList = new ArrayList();

    public PollingVarList(int frequency)
    {
        this.frequency = frequency;
    }

    public void addVarData(Variable variable)
    {
        varDataList.add(variable);
    }

    public Variable getVariable(int pos)
    {
        return (Variable) varDataList.get(pos);
    }

    public int size()
    {
        return varDataList.size();
    }

    public int getFrequency()
    {
        return frequency;
    }

    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[FIELD]", this);
        dumpWriter.print("frequency", frequency);
        dumpWriter.print("varDataList", varDataList);

        return dumpWriter;
    }

    public int getCountFrequencyCicle()
    {
        return countFrequencyCicle;
    }

    public void resetCountFrequencyCicle()
    {
        countFrequencyCicle=-1;
    }//resetCountFrequencyCicle

    
    public void updateCountFrequencyCicle(int freqGCD)
    {
        countFrequencyCicle++;
        countFrequencyCicle %= (frequency / freqGCD);
    }
} //Class PollingVarList
