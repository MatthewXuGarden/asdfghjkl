package com.carel.supervisor.director.maintenance;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.AgeFileFilter;
import java.io.File;
import java.sql.Timestamp;


public class DirElement implements IElement
{
    private static String NAME = "name";
    private static String TIME = "time";
    private String name = "";
    private Integer innerTime = null;

    public DirElement(XMLNode xmlNode)
    {
        name = xmlNode.getAttribute(NAME);

        String timeTmp = xmlNode.getAttribute(TIME);

        try
        {
            innerTime = Integer.valueOf(timeTmp);
        }
        catch (Exception e)
        {
            LoggerMgr.getLogger(this.getClass()).error(e);
            innerTime = new Integer(60);
        }
    }

    public boolean activate(Timestamp time) throws Exception
    {
        long cutoff = System.currentTimeMillis() - (innerTime.intValue() * 86400000L);

        AbstractFileFilter filter = new AgeFileFilter(cutoff);
        cleanDirectory(BaseConfig.getCarelPath() + name, filter);
        LoggerMgr.getLogger(this.getClass()).info("Pulita directory " + BaseConfig.getCarelPath() + name);
        return false;
    }

    private void cleanDirectory(String path, AbstractFileFilter filter)
    {
        File dir = new File(path);
        String[] files = dir.list(filter);
        File file = null;
        if (null != files)
        {
	        for (int i = 0; i < files.length; i++)
	        {
	            try
	            {
	            	file = new File(path + File.separator + files[i]);
	            	file.delete();
	            }
	            catch(Exception e)
	            {
	            	LoggerMgr.getLogger(this.getClass()).error(e);
	            }
	        }
        }
    }

    public String getName()
    {
        return name;
    }

    public String getAction()
    {
        return "";
    }
}
