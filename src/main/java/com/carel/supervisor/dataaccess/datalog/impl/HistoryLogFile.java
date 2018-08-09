package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.io.FileUtil;
import com.carel.supervisor.dataaccess.datalog.*;
import java.io.*;


public class HistoryLogFile extends HistoryLogBase
{
    private FileUtil file = null;

    public HistoryLogFile() throws Exception
    {
        super();
        file = new FileUtil(new File("variabili.txt"), true);
    }

    public void saveHistory(HistoryContext historyContext)
        throws Exception
    {
        Object[][] values = historyContext.getData();

        if (null != values)
        {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < values.length; i++)
            {
                for (int j = 0; j < values[i].length; j++)
                {
                    if (null != values[i][j])
                    {
                        buffer.append(values[i][j].toString());
                    }
                    else
                    {
                        buffer.append("null");
                    }

                    buffer.append("  ");
                }

                buffer.append("\n");
            }

            buffer.append("---------------------------------\n");
            file.appendFile(buffer.toString());
        }
    }

    public void retrieveHistory(HistoryContext historyContext)
        throws Exception
    {
    }
}
