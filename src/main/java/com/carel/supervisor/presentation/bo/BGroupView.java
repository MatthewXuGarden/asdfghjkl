package com.carel.supervisor.presentation.bo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.dataaccess.datalog.impl.TableExtBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.groupmng.GroupManager;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.UserSession;


public class BGroupView extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BGroupView(String l)
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
        p.put("tab1name", "disableAction(1);resizeTableGroupView();");
        p.put("tab2name", "enableAction(1);setFocus();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
    	
    	String virtkey = "";
        //determino se � abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
        	virtkey = "keyboard.js;";
        }
        
        Properties p = new Properties();
        p.put("tab1name", "group.js;resizeTable.js");
        p.put("tab2name", virtkey+"dbllistbox.js;group.js");

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        String cmd = prop.getProperty("cmd");
        GroupManager groupMg = new GroupManager();
        int idsite = us.getIdSite();
        String language = us.getLanguage();

        if (cmd.equals("add"))
        {
            //		    	//retrieve lingua default
            String sql = "select cflanguage.languagecode,cflanguage.description from cfsiteext,cflanguage where cflanguage.languagecode = cfsiteext.languagecode and cfsiteext.idsite = ? and cfsiteext.isdefault = ? ";
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[] { new Integer(idsite), "TRUE" });
            String langDefcode = rs.get(0).get("languagecode").toString();

            GroupListBean groups = us.getGroup();
            sql = "select languagecode from cfsiteext where idsite = ?";
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(1) });

            Map description = new HashMap();

            for (int i = 0; i < rs.size(); i++)
            {
                if (prop.getProperty(rs.get(i).get("languagecode").toString()).equals(""))
                {
                    description.put(rs.get(i).get("languagecode").toString(),
                        prop.getProperty(langDefcode));
                }
                else
                {
                    description.put(rs.get(i).get("languagecode").toString(),
                        prop.getProperty(rs.get(i).get("languagecode").toString()));
                }
            }

            //INSERT NEW GROUP
            int idgroup = groupMg.insertNewGroup(idsite);

            for (int i = 0; i < rs.size(); i++)
            {
                TableExtBean.insertTableExt(idsite, rs.get(i).get("languagecode").toString(),
                    "cfgroup", description.get(rs.get(i).get("languagecode")).toString(), idgroup);
            }

            //UPDATE CFDEVPHY
            String[] dev = prop.getProperty("devices").split(",");

            if (!dev[0].equals(""))
            {
                for (int i = 0; i < dev.length; i++)
                {
                    groupMg.updateIdGroupInDevPhy(idsite, Integer.parseInt(dev[i]), idgroup);
                }
            }

            //LOG insert group
            EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Config", "W004",
                new Object[] { description.get(us.getDefaultLanguage()) });
        }

        else if (cmd.equals("rem")) //rimozione gruppo
        {
            int idgroup = Integer.parseInt(prop.getProperty("removeGroup"));
            int idglobalGroup = 1;

            String group_desc = GroupManager.getDescriptionOfGroup(idsite, idgroup,
                    us.getDefaultLanguage());

            //eliminazione gruppo da cfgroup,cftableext e update cfdevphy iddev su gruppo globale globale
            groupMg.removeGroup(us.getIdSite(), idgroup, idglobalGroup);

            //LOG remove group
            EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(), "Config",
                "W005", new Object[] { group_desc });
        }

        us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
    }
}
