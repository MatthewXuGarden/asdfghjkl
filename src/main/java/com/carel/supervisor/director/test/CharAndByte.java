package com.carel.supervisor.director.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class CharAndByte
{
    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        for (int i = 0; i < 64; i++)
        {
            RecordSet r = DatabaseMgr.getInstance().executeQuery(null,
                    "SELECT n" + (i + 1) + " FROM hsvariable");
            Record record = r.get(0);
            Byte b = (Byte) record.get("n" + (i + 1));
            System.out.print("Il valore è " + b);
        }
    }
}
