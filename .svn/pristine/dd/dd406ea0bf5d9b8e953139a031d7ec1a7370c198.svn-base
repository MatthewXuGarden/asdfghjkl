package com.carel.supervisor.presentation.bo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.dataaccess.datalog.impl.TableExtBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.bean.GroupVarBean;
import com.carel.supervisor.presentation.bean.GroupVarBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.groupmng.GroupManager;
import com.carel.supervisor.presentation.session.UserSession;


public class BSetGroup extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BSetGroup(String l)
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
        p.put("tab1name", "enableAction(1);setFocus();");
        p.put("tab2name", "setdefault();");
        p.put("tab3name", "setdefault();");
        p.put("tab4name", "setdefault();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "dbllistbox.js;group.js;keyboard.js;");
        p.put("tab2name", "dbllistbox.js;vargroups.js;keyboard.js;");
        p.put("tab3name", "dbllistbox.js;alrgroups.js;");
        p.put("tab4name", "note.js;keyboard.js;");

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        int idsite = us.getIdSite();
        int idgroup = Integer.parseInt(prop.getProperty("group"));
        int idglobalgroup = 1;
        String language = us.getLanguage();
        String cmd = prop.getProperty("cmd");
        GroupManager groupMg = new GroupManager();
        String group_desc = GroupManager.getDescriptionOfGroup(idsite, idgroup,
                us.getDefaultLanguage());

        if (tabName.equals("tab1name"))
        {
            if (cmd.equals("removegroup"))
            {
                groupMg.removeGroup(idsite, idgroup, idglobalgroup);

                //LOG remove group
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                    "Config", "W005", new Object[] { group_desc });
            }

            else if (cmd.equals("setgroup"))
            {
                String[] dev1 = prop.getProperty("devices1").split(",");
                String[] dev2 = prop.getProperty("devices2").split(",");

                //retrieve lingue, descrizioni in lingua dalle properties,e inserimento in HashMap
                String sql = "select languagecode from cfsiteext where idsite = ?";
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { new Integer(idsite) });
                Map description = new HashMap();

                for (int i = 0; i < rs.size(); i++)
                {
                    description.put(rs.get(i).get("languagecode").toString(),
                        prop.getProperty(rs.get(i).get("languagecode").toString()));
                }

                sql = "update cftableext set description = ?, lastupdate = ? where idsite = ? and languagecode = ? and tablename = ? and tableid = ? ";

                for (int i = 0; i < rs.size(); i++)
                {
                    Object[] values = new Object[6];
                    values[0] = description.get(rs.get(i).get("languagecode").toString());
                    values[1] = new Timestamp(System.currentTimeMillis());
                    values[2] = new Integer(idsite);
                    values[3] = rs.get(i).get("languagecode");
                    values[4] = "cfgroup";
                    values[5] = Integer.valueOf(idgroup);

                    DatabaseMgr.getInstance().executeStatement(null, sql, values);
                }

                //Update dispositivi del gruppo globale area globale
                if (!dev1[0].equals(""))
                {
                    for (int i = 0; i < dev1.length; i++)
                    {
                        groupMg.updateIdGroupInDevPhy(idsite, Integer.parseInt(dev1[i]),
                            idglobalgroup);
                    }
                }

                //Upadate dispositivi del gruppo in questione
                if (!dev2[0].equals(""))
                {
                    for (int i = 0; i < dev2.length; i++)
                    {
                        groupMg.updateIdGroupInDevPhy(idsite, Integer.parseInt(dev2[i]), idgroup);
                    }
                }

                //LOG modify group
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                    "Config", "W006", new Object[] { group_desc });
            }

            us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
        }
        else if (tabName.equals("tab2name"))
        {
            if (cmd.equals("add")) //nuova vargroup
            {
                String[] vars = prop.getProperty("varsList").split(",");
                GroupVarBean tmp = new GroupVarBean();
                tmp.setIdSite(idsite);
                tmp.setIdGroup(Integer.parseInt(us.getProperty("group")));

                String params = "";

                for (int i = 0; i < vars.length; i++)
                {
                    params = params + "pk" + vars[i] + ";";
                }

                params = params.substring(0, params.length() - 1);
                tmp.setType(Integer.parseInt(prop.getProperty("variableType")));
                tmp.setParameters(params);
                tmp.setMeasureunit(prop.getProperty("measureunit").toString());

                int idvargroup = tmp.save(); //salvataggio in cfvargroup

                //retrieve lingue e metto in description le descrizioni x lingua
                String sql = "select languagecode from cfsiteext where idsite = ?";
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { new Integer(idsite) });
                Map description = new HashMap();

                String defLang = us.getDefaultLanguage();

                for (int i = 0; i < rs.size(); i++)
                {
                    if (!prop.getProperty(rs.get(i).get("languagecode").toString()).equals(""))
                    {
                        description.put(rs.get(i).get("languagecode").toString(),
                            prop.getProperty(rs.get(i).get("languagecode").toString()));
                    }
                    else
                    {
                        description.put(rs.get(i).get("languagecode").toString(),
                            prop.getProperty(defLang));
                    }
                }

                for (int i = 0; i < rs.size(); i++)
                {
                    TableExtBean.insertTableExt(us.getIdSite(),
                        rs.get(i).get("languagecode").toString(), "cfgroupvar",
                        description.get(rs.get(i).get("languagecode").toString()).toString(),
                        idvargroup);
                }

                //LOG insert vargroup
                EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Config",
                    "W007", new Object[] { description.get(us.getDefaultLanguage()), group_desc });
            }

            else if (cmd.equals("rem")) //rimozione vargroup
            {
                int idVarGroup = Integer.parseInt(prop.getProperty("idvar"));
                GroupVarBeanList listVarGroup = new GroupVarBeanList();

                GroupVarBean current_var = listVarGroup.retrieveGroupVarById(idsite, idVarGroup,
                        us.getDefaultLanguage());

                //Retrieve lingue
                String sql = "select languagecode from cfsiteext where idsite = ?";
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { new Integer(idsite) });

                for (int i = 0; i < rs.size(); i++)
                {
                    listVarGroup.removeGroupVar(idsite, idVarGroup,
                        rs.get(i).get("languagecode").toString());
                }

                //          LOG remove group
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                    "Config", "W008", new Object[] { current_var.getDescription(), group_desc });
            }

            else if (cmd.equals("mod")) //modifica vargroup
            {
                int idVarGroup = Integer.parseInt(prop.getProperty("idvar"));

                //       	Retrieve lingue
                String sql = "select languagecode from cfsiteext where idsite = ?";
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { new Integer(idsite) });

                Map description = new HashMap();

                for (int i = 0; i < rs.size(); i++)
                {
                    description.put(rs.get(i).get("languagecode").toString(),
                        prop.getProperty(rs.get(i).get("languagecode").toString()));
                }

                String langcode = "";

                for (int i = 0; i < rs.size(); i++)
                {
                    langcode = rs.get(i).get("languagecode").toString();
                    TableExtBean.updateTableExt(idsite, langcode, "cfgroupvar", idVarGroup,
                        description.get(langcode).toString());
                }

                int type = Integer.parseInt(prop.getProperty("variableType"));
                String params = prop.getProperty("varsList");
                String measureunit = prop.getProperty("measureunit");
                GroupVarBeanList groupvar = new GroupVarBeanList();
                String desc_vargroup = groupvar.retrieveGroupVarById(idsite, idVarGroup,
                        us.getDefaultLanguage()).getDescription();
                groupvar.updateGroupVar(idsite, idVarGroup, type, params, measureunit);

                //LOG update vargroup
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                    "Config", "W009", new Object[] { desc_vargroup, group_desc });
            }

            if (!cmd.equals("reload"))
            {
                us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
            }
        }
        else if (tabName.equals("tab3name"))
        {
            if (cmd.equals("add_alr")) //nuovo allarme di gruppo
            {
                String[] vars = prop.getProperty("varsList").split(",");
                GroupVarBean tmp = new GroupVarBean();
                tmp.setIdSite(idsite);
                tmp.setIdGroup(idgroup);

                String params = "";

                for (int i = 0; i < vars.length; i++)
                {
                    params = params + "pk" + vars[i] + ";";
                }

                params = params.substring(0, params.length() - 1);
                tmp.setType(4);
                tmp.setParameters(params);

                int idvargroup = tmp.save(); //salvataggio in cfvargroup

                //retrieve lingue e metto in description le descrizioni x lingua
                String sql = "select languagecode from cfsiteext where idsite = ?";
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { new Integer(idsite) });
                Map description = new HashMap();
                String defLang = us.getDefaultLanguage();

                for (int i = 0; i < rs.size(); i++)
                {
                    if (!prop.getProperty(rs.get(i).get("languagecode").toString()).equals(""))
                    {
                        description.put(rs.get(i).get("languagecode").toString(),
                            prop.getProperty(rs.get(i).get("languagecode").toString()));
                    }
                    else
                    {
                        description.put(rs.get(i).get("languagecode").toString(),
                            prop.getProperty(defLang));
                    }
                }

                for (int i = 0; i < rs.size(); i++)
                {
                    TableExtBean.insertTableExt(us.getIdSite(),
                        rs.get(i).get("languagecode").toString(), "cfgroupvar",
                        description.get(rs.get(i).get("languagecode").toString()).toString(),
                        idvargroup);
                }

                //LOG insert alarm group
                EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Config",
                    "W010", new Object[] { description.get(us.getDefaultLanguage()), group_desc });
            }

            else if (cmd.equals("rem_alr")) //rimozione allarme di gruppo
            {
                //Retrieve lingue
                String sql = "select languagecode from cfsiteext where idsite = ?";
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { new Integer(idsite) });

                int idVarGroup = Integer.parseInt(prop.getProperty("idvar"));
                GroupVarBeanList listVarGroup = new GroupVarBeanList();
                GroupVarBean current_var = listVarGroup.retrieveGroupVarById(idsite, idVarGroup,
                        us.getDefaultLanguage());

                for (int i = 0; i < rs.size(); i++)
                {
                    listVarGroup.removeGroupVar(idsite, idVarGroup,
                        rs.get(i).get("languagecode").toString());
                }

                //LOG remove alarm group
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                    "Config", "W011", new Object[] { current_var.getDescription(), group_desc });
            }

            else if (cmd.equals("mod_alr")) //modifica allarme di gruppo
            {
                int idvar = Integer.parseInt(prop.getProperty("idvar"));

                //       	Retrieve lingue
                String sql = "select languagecode from cfsiteext where idsite = ?";
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { new Integer(idsite) });

                Map description = new HashMap();

                for (int i = 0; i < rs.size(); i++)
                {
                    description.put(rs.get(i).get("languagecode").toString(),
                        prop.getProperty(rs.get(i).get("languagecode").toString()));
                }

                String langcode = "";

                for (int i = 0; i < rs.size(); i++)
                {
                    langcode = rs.get(i).get("languagecode").toString();
                    TableExtBean.updateTableExt(idsite, langcode, "cfgroupvar", idvar,
                        description.get(langcode).toString());
                }

                String params = prop.getProperty("varsList");
                String measureunit = prop.getProperty("measureunit");

                GroupVarBeanList groupvar = new GroupVarBeanList();
                groupvar.updateGroupVar(idsite, idvar, 4, params, measureunit);

                String desc_vargroup = groupvar.retrieveGroupVarById(idsite, idvar,
                        us.getDefaultLanguage()).getDescription();

                //LOG update alarmgroup
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                    "Config", "W012", new Object[] { desc_vargroup, group_desc });
            }

            if (!cmd.equals("reload_alr"))
            {
                us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
            }
        }
    }
}
