package com.carel.supervisor.presentation.helper;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

import org.apache.commons.io.FileSystemUtils;

import java.io.IOException;


public class SpaceHistoricalHelper
{
    private static final int PURE_SPACE = 406; //byte per riga
    private static final int BLANK_ROW_SPACE = 40; //byte riga vuota postgres 
    private static final int INDEX_SPACE = 500; //byte indice 

    public SpaceHistoricalHelper()
    {
    }

    private static long calculateActualSpaceOccupedOnDisk(int idsite) //somma tutti keyactual delle variabili attive
        throws DataBaseException
    {
        long space_occuped = 0;
        String sql =
            "select buffer.keyactual, buffer.isturn, buffer.keymax from buffer,cfvariable where buffer.idsite=?" +
            " and cfvariable.idsite = buffer.idsite and cfvariable.idvariable=buffer.idvariable and " +
            "cfvariable.iscancelled='FALSE'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });

        if (rs.size() > 0)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                if (((Boolean) rs.get(i).get("isturn")).booleanValue() == true)
                {
                    space_occuped = space_occuped +
                        Long.parseLong(rs.get(i).get("keymax").toString());
                }
                else
                {
                    if (!rs.get(i).get("keyactual").toString().equals("-1"))
                    {
                        space_occuped = space_occuped +
                            Long.parseLong(rs.get(i).get("keyactual").toString());
                    }
                }
            }

            return space_occuped;
        }
        else
        {
            return 0;
        }
    }

    public static long actualSpaceRequiredForHist(int idsite)
        throws DataBaseException
    {
        //keymax delle master -> haccp
        String sql =
            "select buffer.keymax from buffer,cfvariable where buffer.idsite=? and " +
            "cfvariable.idsite= buffer.idsite and cfvariable.idvariable = buffer.idvariable and " +
            "cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable=-1 and cfvariable.ishaccp='TRUE'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });

        long totKeyMax = 0;

        for (int i = 0; i < rs.size(); i++) //somma keymax tranne di quelle cancellate
        {
            totKeyMax = totKeyMax +
                Long.parseLong(rs.get(i).get("keymax").toString());
        }

        // keymax delle variabili storico
        sql = "select buffer.keymax from buffer,cfvariable where buffer.idsite=? and " +
            "cfvariable.idsite= buffer.idsite and cfvariable.idvariable = buffer.idvariable and " +
            "cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is null";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });

        for (int i = 0; i < rs.size(); i++) //somma keymax tranne di quelle cancellate
        {
            totKeyMax = totKeyMax +
                Long.parseLong(rs.get(i).get("keymax").toString());
        }

        //      keymax delle variabili d'allarme
        sql = "select buffer.keymax from buffer,cfvariable where buffer.idsite=? and " +
            "cfvariable.idsite= buffer.idsite and cfvariable.idvariable = buffer.idvariable and " +
            "cfvariable.iscancelled='FALSE' and cfvariable.type=4 and cfvariable.isactive='TRUE'";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });

        for (int i = 0; i < rs.size(); i++) //somma keymax tranne di quelle cancellate
        {
            totKeyMax = totKeyMax +
                Long.parseLong(rs.get(i).get("keymax").toString());
        }

        long totKeyActual = calculateActualSpaceOccupedOnDisk(idsite);

        //      DEBUG System.out.println("Somma keymax: "+totKeyMax);
        //      DEBUG System.out.println("Spazio libero prenotato: "+ (totKeyMax - totKeyActual));
        return totKeyMax - totKeyActual;
    }

    public static long calculateFreeDiskSpace() throws IOException
    {
        long space = 0;

        try
        {
            space = FileSystemUtils.freeSpace(BaseConfig.getProperty("dbdisk"));
        }
        catch (Exception e)
        {
            e.printStackTrace(); // TODO: handle exception
        }

        return space;
    }

    public static boolean confirmHistorical(int idsite,
        long newSpaceForHistorical) throws DataBaseException, IOException
    {
        long totalSpaceRequired = actualSpaceRequiredForHist(idsite) +
            newSpaceForHistorical;

        //DEBUG System.out.println("Spazio nuovo richiesto: "+newSpaceForHistorical);
        //DEBUG System.out.println("Spazio prenotato + nuovo richiesto: "+totalSpaceRequired);
        //moltiplico il numero in righe, per lo spazio medio previsto per una riga di database
        totalSpaceRequired = totalSpaceRequired * (PURE_SPACE +
            BLANK_ROW_SPACE + INDEX_SPACE);

        if (calculateFreeDiskSpace() > totalSpaceRequired)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean confirmHistoricalCiclic(long actualSpaceRequired,
        long freeDiskSpace, long KeyMaxSum)
    {
        long totalSpaceRequired = actualSpaceRequired + KeyMaxSum;
        totalSpaceRequired = totalSpaceRequired * (PURE_SPACE +
            BLANK_ROW_SPACE + INDEX_SPACE);

        if (freeDiskSpace > totalSpaceRequired)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static long keyMaxSumToByte(long keymax)
    {
        long spaceByte = 0;
        spaceByte = keymax * (PURE_SPACE + BLANK_ROW_SPACE + INDEX_SPACE);

        return spaceByte;
    }
}
