package com.carel.supervisor.presentation.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.io.ReadFile;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class BRDataTransfer extends BoMaster
{
    private static final int REFRESH_TIME = -1;
    private static int screenw = 1024;
    private static int screenh = 768;
    
    public BRDataTransfer(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "enableAction(1);resizeTableSiteList();");
        p.put("tab3name", "enableAction(1);");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "r_datatransfer.js");
        p.put("tab3name", "r_datatransfer.js");

        return p;
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        if (tabName.equalsIgnoreCase("tab1name"))
        {
            Iterator iter = prop.keySet().iterator();
            int[] ids = new int[prop.size()];
            int cont = 0;
            String tmp_tot = "";
            String s_id = "";

            while (iter.hasNext())
            {
                tmp_tot = iter.next().toString();

                if (tmp_tot != null)
                {
                    s_id = tmp_tot.split("_")[1];
                }

                ids[cont] = Integer.parseInt(s_id);
                cont++;
            }

            resetSiteTransfer(ids);
			try
			{
				Socket s = new Socket(InetAddress.getLocalHost(), 10001);
				OutputStream sos = s.getOutputStream();
				String cmdd = "update";
				sos.write(cmdd.getBytes());
				sos.flush();
				sos.close();
			} catch (Exception e)
			{
				//e.printStackTrace();
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
        }
        else if (tabName.equalsIgnoreCase("tab3name"))
        {
        	updateConfigFile(prop);
        	
			try
			{
				Socket s = new Socket(InetAddress.getLocalHost(), 10001);
				OutputStream sos = s.getOutputStream();
				String cmdd = "update";
				sos.write(cmdd.getBytes());
				sos.flush();
				sos.close();
			} catch (Exception e)
			{
				//e.printStackTrace();
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
        }
    }

    public String execute(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        return "";
    }

    public String getSiteTable(String language) throws DataBaseException
    {
        //lista di tutti i siti configurati
        SiteInfo[] siteList = SiteInfoList.retriveRemoteSite();

        //retrieve siti "on"
        String sql = "select idsite from cftransfersite";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        int num = rs.size();
        Map active = new HashMap();
        Record r = null;

        for (int i = 0; i < num; i++)
        {
            r = rs.get(i);
            active.put(r.get("idsite"), "on");
        }

        LangService lang = LangMgr.getInstance().getLangService(language);

        String s_site = lang.getString("r_datatransfer", "sites");
        String s_onoff = lang.getString("r_datatransfer", "onoff");

        int rows = siteList.length;
        String[] header = new String[] { s_site, s_onoff };
        HTMLElement[][] tabledata = new HTMLElement[rows][];
        SiteInfo site = null;

        for (int i = 0; i < rows; i++)
        {
            site = siteList[i];
            tabledata[i] = new HTMLElement[2];
            tabledata[i][0] = new HTMLSimpleElement(site.getName());

            if (active.containsKey(new Integer(site.getId())))
            {
                tabledata[i][1] = new HTMLSimpleElement(
                        "<input type='checkbox' id='on_" + site.getId() +
                        "' name='on_" + site.getId() + "' checked />");
            }
            else
            {
                tabledata[i][1] = new HTMLSimpleElement(
                        "<input type='checkbox' id='on_" + site.getId() +
                        "' name='on_" + site.getId() + "'/>");
            }
        }

        HTMLTable table = new HTMLTable("sites", header, tabledata);

        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setWidth(880);
        table.setHeight(350);
        table.setAlignType(1, 1);
        table.setColumnSize(0, 756);
        table.setColumnSize(1, 80);
        table.setRowHeight(20);

        return table.getHTMLText();
    }

    private void resetSiteTransfer(int[] ids) throws DataBaseException
    {
        String sql = "delete from cftransfersite";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        if (ids.length > 0)
        {
            sql = "insert into cftransfersite values (?,?)";

            Object[] param = new Object[2];
            param[1] = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < ids.length; i++)
            {
                param[0] = new Integer(ids[i]);
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
            }
        }
    }

    public String readFromLogFile() throws IOException
    {
        String CarelPath = BaseConfig.getCarelPath();
        String logpath = CarelPath + File.separator + "scheduler" +
            File.separator + "log" + File.separator + "Scheduler.log";
        if(new File(logpath).exists())
        	return ReadFile.readFromFile(logpath);
        else
        	return "";
    }

    public Properties getProperties() throws FileNotFoundException, IOException
    {
        String CarelPath = BaseConfig.getCarelPath();
        String confpath = CarelPath + File.separator + "scheduler" +
            File.separator + "conf" + File.separator + "manager.properties";
        Properties prop = new Properties();
        prop.load(new FileInputStream(confpath));

        return prop;
    }

    public int getStarthour()
        throws InvalidPropertiesFormatException, FileNotFoundException, 
            IOException
    {
        String CarelPath = BaseConfig.getCarelPath();

        //String confpath = BaseConfig.getCarelPath()+"guardian"+File.separator+"Guardian.xml";
        String confpath = CarelPath + "scheduler" + File.separator + "conf" +
            File.separator + "configscheduler.xml";

        File file = new File(confpath);
        URL url = file.toURL();
        XMLNode xmlNode = null;
        XMLNode xmlRoot= null;
        
        if (url != null)
        {
            try
            {
            	xmlRoot = XMLNode.parse(url.openStream());
                xmlNode = xmlRoot.getNode("job");
                xmlNode = xmlNode.getNode("trigger");
                xmlNode = xmlNode.getNode("cron");
                xmlNode = xmlNode.getNode("cron-expression");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        String expression = xmlNode.getTextValue();
        String hour = StringUtility.split(expression, " ")[2];
        int i = 0;

        if ((hour != null) && !hour.equalsIgnoreCase(""))
        {
            i = Integer.parseInt(hour);
        }

        return i;
    }

    private void updateConfigFile(Properties prop) throws Exception
    {
    	int starthour  = Integer.parseInt(prop.getProperty("starthour"));
    	int tmp = 0;
        String CarelPath = BaseConfig.getCarelPath();
        
        String confpath = CarelPath + File.separator + "scheduler" +
            File.separator + "conf" + File.separator + "manager.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(confpath));
        String xmlpath = CarelPath + "scheduler" + File.separator + "conf" +
        File.separator + "configscheduler.xml";
        
        tmp = Integer.parseInt(prop.getProperty("failsleeptime"))*60000;
        properties.setProperty("failsleeptime", String.valueOf(tmp));
        tmp = Integer.parseInt(prop.getProperty("successsleeptime"))*60000;
        properties.setProperty("successsleeptime",String.valueOf(tmp));
        tmp = Integer.parseInt(prop.getProperty("pausesleeptime"))*60000;
        properties.setProperty("pausesleeptime",String.valueOf(tmp));
        properties.setProperty("pausesitecount",prop.getProperty("pausesitecount"));
        properties.setProperty("siteretrytimes",prop.getProperty("siteretrytimes"));
        //properties.setProperty("processretrytimes",prop.getProperty("processretrytimes"));
        tmp = Integer.parseInt(prop.getProperty("maxconnectiontime"))*60000;
        properties.setProperty("maxconnectiontime",String.valueOf(tmp));
        //tmp = Integer.parseInt(prop.getProperty("chunksize"))*1024;
        //properties.setProperty("chunksize",String.valueOf(tmp));
        properties.store(new FileOutputStream(confpath),"");
        
        File file = new File(xmlpath);
		URL url = file.toURL();
		XMLNode xmlNode = null;
		XMLNode xmlRoot = null;
		if(url != null)
		{
			xmlRoot = XMLNode.parse(url.openStream());
            xmlNode = xmlRoot.getNode("job");
            xmlNode = xmlNode.getNode("trigger");
            xmlNode = xmlNode.getNode("cron");
            xmlNode = xmlNode.getNode("cron-expression");
            xmlNode.setTextValue("<![CDATA[00 00 "+starthour+" * * ?]]>");
		}
		
		
		FileOutputStream fos = new FileOutputStream(xmlpath);
		fos.write(xmlRoot.toString().getBytes());
		fos.flush();
		fos.close();
    }
    
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }


}
