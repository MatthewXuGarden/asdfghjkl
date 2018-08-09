package com.carel.supervisor.dispatcher.engine.sms;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SMSProviderMgr
{
    private final static String OPTIONS = "[OPTIONS]";
    private final static String CON_TIMEOUT = "CONNECT_TIMEOUT=65";
    private final static String END_PROVIDER = "END";
    private Map memProvider;

    public SMSProviderMgr()
    {
        memProvider = new HashMap();
    }

    public void loadProvider()
    {
        String sql = "select labelprovider,infoprovider from cfprovidersms";
        Record r = null;

        String label = "";
        String info = "";

        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
            int numrows = 0;

            if (rs != null)
            {
                numrows = rs.size();
            }

            for (int i = 0; i < numrows; i++)
            {
                r = rs.get(i);
                label = UtilBean.trim(r.get("labelprovider"));
                info = UtilBean.trim(r.get("infoprovider"));

                this.memProvider.put(label, info);
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public void writeProvider(String path, String file)
    {
        File f = new File(path);
        FileOutputStream fos = null;

        String key = "";
        String val = "";

        boolean ris = false;

        try
        {
            if (!f.exists())
            {
                ris = f.mkdirs();
            }

            if (ris)
            {
                fos = new FileOutputStream(f.getPath() + File.separatorChar + file);
                fos.write(OPTIONS.getBytes());
                fos.write("\r\n".getBytes());
                fos.write(CON_TIMEOUT.getBytes());
                fos.write("\r\n".getBytes());
                fos.write("\r\n".getBytes());

                Iterator i = this.memProvider.keySet().iterator();

                while (i.hasNext())
                {
                    key = (String) i.next();
                    val = (String) this.memProvider.get(key);
                    key = "[" + key + "]";
                    fos.write(key.getBytes());
                    fos.write("\r\n".getBytes());
                    fos.write(val.getBytes());
                    fos.write("\r\n".getBytes());
                    fos.write(END_PROVIDER.getBytes());
                    fos.write("\r\n".getBytes());
                    fos.write("\r\n".getBytes());
                }

                fos.flush();
                fos.close();
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
}
