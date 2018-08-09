package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.remote.bean.SynchBeanList;


public class BR_SiteList extends BoMaster
{
    private static final int REFRESH_TIME = -1;
    private static int screenw = 1024;
    private static int screenh = 768;
    
    public BR_SiteList(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "r_site_onload();resizeTableSiteConfig();");
        p.put("tab2name", "initialize();resizeTableDataSync();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "r_sitelist.js;");
        p.put("tab2name", "r_sitelist2.js;");

        return p;
    }

    private static String[] getTableSyncHeader(String language)
    {
        LangService lang = LangMgr.getInstance().getLangService(language);
        String s_name = lang.getString("r_sitelist", "name");
        String s_type = lang.getString("r_sitelist", "type");
        String s_connectiontype = lang.getString("r_sitelist", "connectiontype");
        String s_synch = lang.getString("r_sitelist", "synch");

        return new String[] { s_name, s_type, s_connectiontype, s_synch };
    }

    public static String getHTMLSyncTable(int width, int height, String language)
        throws DataBaseException
    {
        //stringhe multilingua
        LangService lang = LangMgr.getInstance().getLangService(language);
        String s_nosync = lang.getString("r_sitelist", "nosync");
        String s_plantsync = lang.getString("r_sitelist", "plantsync");
        String s_alarmsync = lang.getString("r_sitelist", "alarmsync");
        String s_eventsync = lang.getString("r_sitelist", "eventsync");
        String s_notesync = lang.getString("r_sitelist", "notesync");

        SiteInfo[] siteList = SiteInfoList.retriveRemoteSite();
        int rows = siteList.length;

        HTMLElement[][] tabledata = new HTMLElement[rows][];
        String[] DBLClickRowFunction = new String[rows];
        SiteInfo tmp_site = null;
        String[] sync_info = null;
        String sync_param = "";
        String sync_param_table = "";

        for (int i = 0; i < rows; i++)
        {
            tmp_site = siteList[i];
            tabledata[i] = new HTMLElement[4];
            tabledata[i][0] = new HTMLSimpleElement(tmp_site.getName());

            if (tmp_site.getType().equalsIgnoreCase("PVP"))
            {
                tabledata[i][1] = new HTMLSimpleElement("PlantVisorPRO");
            }
            else if (tmp_site.getType().equalsIgnoreCase("PVE"))
            {
                tabledata[i][1] = new HTMLSimpleElement("PlantVisor Enhanced");
            }
            else if (tmp_site.getType().equalsIgnoreCase("PW1"))
            {
                tabledata[i][1] = new HTMLSimpleElement("PlantWatch");
            }

            tabledata[i][2] = new HTMLSimpleElement(tmp_site.getConnectiontype());

            sync_info = SynchBeanList.getTableId(null, tmp_site.getId());
            sync_param = "";
            sync_param_table = "";

            if (sync_info.length > 0)
            {
                for (int j = 0; j < sync_info.length; j++) //creazione param da passare tramite js
                {
                    sync_param = sync_param + sync_info[j] + ",";
                }

                sync_param = sync_param.substring(0, sync_param.length() - 1);

                //creazione stringhe da mostrare in tabella
                if (sync_param.contains("1"))
                {
                    sync_param_table = s_plantsync + ", ";
                }

                if (sync_param.contains("5"))
                {
                    sync_param_table = sync_param_table + s_alarmsync + ", ";
                }

                if (sync_param.contains("6"))
                {
                    sync_param_table = sync_param_table + s_eventsync + ", ";
                }

                if (sync_param.contains("7"))
                {
                    sync_param_table = sync_param_table + s_notesync + ", ";
                }

                sync_param_table = sync_param_table.substring(0,
                        sync_param_table.length() - 2);
            }
            else
            {
                sync_param_table = s_nosync;
            }

            tabledata[i][3] = new HTMLSimpleElement(sync_param_table);

            DBLClickRowFunction[i] = String.valueOf(tmp_site.getId() + ";" +
                    tmp_site.getName() + ";" + sync_param + ";" +
                    tmp_site.getType());
        }

        HTMLTable table = new HTMLTable("sites", getTableSyncHeader(language),
                tabledata);
        table.setDbClickRowAction("sync_reload('$1');");
        table.setDlbClickRowFunction(DBLClickRowFunction);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setWidth(900);
        table.setHeight(200);
        table.setAlignType(1, 1);
        table.setAlignType(2, 1);
        table.setColumnSize(0, 250);
        table.setColumnSize(1, 100);
        table.setColumnSize(2, 100);
        table.setColumnSize(3, 356);
        table.setRowSelectColor(false);

        String htmlTable = table.getHTMLText();

        return htmlTable;
    }

    private boolean isIdentifyUsed(String identify) throws DataBaseException
    {
        String sql = "select count(1) from cfsite where code=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { identify });
        int count = Integer.parseInt(rs.get(0).get("count").toString());

        if (count == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        String cmd = us.getProperty("cmd");

        if (tabName.equals("tab1name"))
        {
            String s_site_to_mod = us.getProperty("site_to_modify");

            if (cmd.equals("save")) //salvataggio nuovo sito
            {
                String identify = us.getProperty("identify");

                if (!isIdentifyUsed(identify))
                {
                    SiteInfo tmp = new SiteInfo();
                    tmp.setName(us.getProperty("name"));
                    tmp.setType(us.getProperty("type"));
                    tmp.setCode(identify);
                    tmp.setPassword(us.getProperty("password"));
                    tmp.setConnectiontype(us.getProperty("connectiontype"));
                    tmp.setPhone(us.getProperty("phonenumber"));

                    tmp.save(us.getDefaultLanguage());
                    
                    
                    //log aggiunta sito
                    String type = "";
                    if (tmp.getType().equalsIgnoreCase("PVP"))
                    	type = "PlantVisorPRO";
            		else if (tmp.getType().equalsIgnoreCase("PVE"))
            			type = "PlantVisor Enhanced";
            		else if (tmp.getType().equalsIgnoreCase("PW1"))
            			type = "PlantWatch";
                    EventMgr.getInstance().info(new Integer(us.getIdSite()),
                        us.getUserName(), "Config", "R010",
                        new Object[] {us.getProperty("name"),type});
                    
                    
                }
                else
                {
                	us.setProperty("cmd","double");
                }
            }
            else if (cmd.equals("remove"))
            {
                int site_to_mod = Integer.parseInt(s_site_to_mod);
                SiteInfo site = SiteInfoList.retrieveSiteById(site_to_mod);
                SiteInfoList.removeSiteById(site_to_mod);
                
                String type = "";
                if (site.getType().equalsIgnoreCase("PVP"))
                	type = "PlantVisorPRO";
        		else if (site.getType().equalsIgnoreCase("PVE"))
        			type = "PlantVisor Enhanced";
        		else if (site.getType().equalsIgnoreCase("PW1"))
        			type = "PlantWatch";
                
                //log rimozione sito
                EventMgr.getInstance().info(new Integer(us.getIdSite()),
                        us.getUserName(), "Config", "R012",
                        new Object[] {site.getName(),type});
                    
                
            }
            else if (cmd.equals("savemodify"))
            {
                int site_to_mod = Integer.parseInt(s_site_to_mod);
                String phone = us.getProperty("phonenumber");

                if (phone == null)
                {
                    phone = "";
                }

                SiteInfoList.updateSite(site_to_mod, us.getProperty("name"),
                    us.getProperty("type"), us.getProperty("identify"),
                    us.getProperty("password"),
                    us.getProperty("connectiontype"), phone);
                
                //log modifica sito
                EventMgr.getInstance().info(new Integer(us.getIdSite()),
                        us.getUserName(), "Config", "R011",
                        new Object[] {us.getProperty("name")});
                    
                
            }
        }
        else if (tabName.equals("tab2name"))
        {
            int idsite_to_mod = Integer.parseInt(prop.getProperty(
                        "idsite_to_set"));
            String sync_params = prop.getProperty("sync_params");

            if (sync_params.equals(""))
            {
                SynchBeanList.delTableConf(null, idsite_to_mod);
            }
            else
            {
                String[] param_splitted = sync_params.split(",");
                int idtable = -1;
                SynchBeanList.delTableConf(null, idsite_to_mod);

                for (int i = 0; i < param_splitted.length; i++)
                {
                    idtable = Integer.parseInt(param_splitted[i]);
                    SynchBeanList.insTableConf(null, idsite_to_mod, idtable);
                }
            }
        }
    }
    
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }

}
