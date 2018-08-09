package com.carel.supervisor.dataaccess.reorder;

import com.carel.supervisor.base.util.queue.Queue;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;




public class ReorderInformation extends Queue
{
    //Object [] da accodare è cosi fatto {idSite,idVariable,samplingPeriod,historicalPeriod}
    public final static int INDEX_ID_SITE = 0;
    public final static int INDEX_ID_VARIABLE = 1;
    public final static int INDEX_SAMPLING_PERIOD = 2;
    public final static int INDEX_HISTORICAL_PERIOD = 3;
    
    
    public final static String SITE = "idsite";
    public final static String VARIABLE = "idvariable";
    public final static String SAMPLING_PERIOD = "samplingperiod";
    public final static String HISTORICAL_PERIOD = "historicalperiod";
    
    
    public final static int COUNT_INDEX = 5;
    private final static short DEFAULT_KEY_MAX = 1000;
    private final static int VALUES=64; //DataDynamicSaveMember.VALUES
    
    public ReorderInformation(int length)
    {
        super(length);

        //recupero dati da reorder
        RecordSet recordSet;

        try
        {
            recordSet = DatabaseMgr.getInstance().executeQuery(null,
                    "SELECT idsite,idvariable,samplingperiod,historicalperiod FROM reorder");

            int numRecords = recordSet.size();

            if (numRecords != 0)
            {
                Object[] informations = null;

                for (int i = 0; i < numRecords; i++)
                {
                	informations = new Object[COUNT_INDEX];
                    informations[INDEX_ID_SITE] = recordSet.get(i).get(SITE);
                    informations[INDEX_ID_VARIABLE] = recordSet.get(i).get(VARIABLE);
                    informations[INDEX_SAMPLING_PERIOD] = recordSet.get(i)
                                                                      .get(SAMPLING_PERIOD);
                    informations[INDEX_HISTORICAL_PERIOD] = recordSet.get(i)
                    		.get(HISTORICAL_PERIOD);

                    this.enqueRecord(informations);
                } //for

                DatabaseMgr.getInstance().executeStatement("DELETE FROM reorder",
                    null);
            } //if
        }
        catch (DataBaseException e)
        {
           
            e.printStackTrace();
        }
    } //ReorederInformation

    public static short calculateNewKeyMax(Integer idVariable,
        Integer historicalPeriod, Integer samplingPeriod) throws DataBaseException
    {
        
    	
    	RecordSet recordSet=DatabaseMgr.getInstance().executeQuery(null,"SELECT type FROM cfvariable WHERE idvariable=? ",new Object[]{idVariable});
    	int type =((Integer)recordSet.get(0).get(0)).intValue();

        return calculateNewKeyMax(new Short((short) type), historicalPeriod,
            samplingPeriod);
    } //calculateKeyMax

    public static short calculateNewKeyMax(Short Type, Integer historicalPeriod,
        Integer samplingPeriod)
    {
        long hp = historicalPeriod.longValue();
        long sp = samplingPeriod.longValue();
        long sps = (long) VALUES;

        long newkeyMax = (long) Math.round((1.1 * hp) / (sp * sps));

        switch (Type.shortValue())
        {
        case VariableInfo.TYPE_ALARM:
            return 100;

        case VariableInfo.TYPE_DIGITAL:
        case VariableInfo.TYPE_INTEGER:
        case VariableInfo.TYPE_ANALOGIC:
            return (short) newkeyMax;

        default:
            return DEFAULT_KEY_MAX;
        } //switch
    } //calculateNewKeyMax
} //Class ReorderInformation
