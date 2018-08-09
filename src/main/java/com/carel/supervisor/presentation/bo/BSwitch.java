package com.carel.supervisor.presentation.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.plugin.switchtech.Switch;
import com.carel.supervisor.plugin.switchtech.SwitchMgr;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.switchtech.SwitchConfig;
import com.carel.supervisor.presentation.switchtech.SwitchConfigList;
import com.carel.supervisor.presentation.switchtech.SwitchDevList;
import com.carel.supervisor.presentation.switchtech.SwitchStatus;
import com.carel.supervisor.presentation.switchtech.SwitchStatusList;
import com.carel.supervisor.presentation.switchtech.SwitchVar;
import com.carel.supervisor.presentation.switchtech.SwitchVarList;


public class BSwitch extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BSwitch(String lang)
    {
        super(lang, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "load_switchstatus();");
        p.put("tab2name", "load_switchconfig();");
        p.put("tab3name","onDevPageLoad();");
        p.put("tab4name", "onVarPageLoad();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
    	String virtkey = "";
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
            virtkey = ";keyboard.js;";
        }
        Properties p = new Properties();
        p.put("tab1name", "switchstatus.js;");
        p.put("tab2name", "switchconfig.js;");
        p.put("tab3name", "switchdev.js;dbllistbox.js");
        p.put("tab4name", "switchvar.js;dbllistbox.js"+virtkey);

        return p;
    }

    public String executeDataAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        String cmd = prop.getProperty("cmd");
        int idsite = us.getIdSite();
        String language = us.getLanguage();

        if (tabName.equalsIgnoreCase("tab1name"))
        {
            if (cmd.equalsIgnoreCase("load_status"))
            {
                int id_switch = Integer.parseInt(prop.getProperty("switch_id"));
                SwitchConfig conf = SwitchConfigList.retrieveById(idsite,
                        id_switch);
                SwitchStatus status = SwitchStatusList.retriveSwitchStatusById(idsite,
                        id_switch);
                SwitchMgr mgr = SwitchMgr.getInstance();
                Switch tmp = null;
                String enabled = "TRUE";
                String type= "";
                String mode = "";
                if (mgr!=null)
                {
                	tmp = mgr.getSwitch(id_switch-1); 
                	if (tmp!=null)
                	{
                		if (tmp.isForce_tev()||tmp.isOnAlarmStatusForPage())
                		enabled = "FALSE";
                		type = tmp.getSt_type();
                		mode = tmp.getAutoswitch();
                	}
                	else
                	{
                		type = status.getType();
                		mode = conf.getAutoswitch();
                	}
                }
                
                
                StringBuffer xmlresponse = new StringBuffer();
                xmlresponse.append("<response>");
                xmlresponse.append("<info>");
                xmlresponse.append("<![CDATA[" + status.getRunning() +
                    "]]>");
                xmlresponse.append("<![CDATA[" + type + "]]>");
                xmlresponse.append("<![CDATA[" + mode +
                    "]]>");
                xmlresponse.append("<![CDATA[" + conf.getHour() + "]]>");
                xmlresponse.append("<![CDATA[" + status.getLastswitch() +
                    "]]>");
                xmlresponse.append("<![CDATA[" + status.getNextswitch() +
                    "]]>");
                xmlresponse.append("<![CDATA[" +
                    SwitchConfigList.getNumberOfDeviceForSwitch(idsite,
                        id_switch) + "]]>");
                xmlresponse.append("<![CDATA[" +enabled + "]]>");
                xmlresponse.append("</info>");
                xmlresponse.append("</response>");

                return xmlresponse.toString();
            }
        }
        else if (tabName.equalsIgnoreCase("tab2name"))
        {
            if (cmd.equalsIgnoreCase("load_conf"))
            {
                int id_switch = Integer.parseInt(prop.getProperty("switch_id"));
                SwitchConfig switch_conf = SwitchConfigList.retrieveById(idsite,
                        id_switch);
                SwitchStatus status = SwitchStatusList.retriveSwitchStatusById(idsite,
                        id_switch);

                StringBuffer xmlresponse = new StringBuffer();
                xmlresponse.append("<response>");
                xmlresponse.append("<auto>");
                xmlresponse.append("<![CDATA[" +
                    switch_conf.getAutoswitch() + "]]>");
                xmlresponse.append("<![CDATA[" + switch_conf.getHour() +
                    "]]>");
                xmlresponse.append("<![CDATA[" +
                    switch_conf.getStarthour() + "]]>");
                xmlresponse.append("<![CDATA[" +
                    switch_conf.getStarttype() + "]]>");
                xmlresponse.append("<![CDATA[" +
                    switch_conf.getAlarmwait() + "]]>");
                xmlresponse.append("</auto>");
                xmlresponse.append("<manual>");
                xmlresponse.append("<![CDATA[" +
                    switch_conf.getManualtype() + "]]>");
                xmlresponse.append("</manual>");
                xmlresponse.append("<autoresume>");
                xmlresponse.append("<![CDATA[" +
                    switch_conf.getAutoresume() + "]]>");
                xmlresponse.append("</autoresume>");
                xmlresponse.append("<running>");
                xmlresponse.append("<![CDATA[" + status.getRunning() +
                    "]]>");
                xmlresponse.append("</running>");
                xmlresponse.append("<skipalarm>");
                xmlresponse.append("<![CDATA[" +
                    switch_conf.getSkipalarm() + "]]>");
                xmlresponse.append("</skipalarm>");
                xmlresponse.append("</response>");

                return xmlresponse.toString();
            }
        }
        else if (tabName.equalsIgnoreCase("tab3name"))
        {
            if (cmd.equalsIgnoreCase("load_devices"))
            {
                int id_switch = Integer.parseInt(prop.getProperty("idswitch"));

                //id che device gia usati
                int[] ids_used = SwitchDevList.retrieveIdsDevicesUsed(idsite);

                //lista device disponibili alla configurazione
                DeviceBean[] list_tmp = SwitchDevList.getDeviceOfSwitch(idsite,
                        id_switch, language);
                DeviceBean dev = null;
                List list_sx = new ArrayList();

                //devo togliere fda quelli trovati quelli gia usati, se ce ne sono di gua confiugurati
                for (int i = 0; i < list_tmp.length; i++)
                {
                    boolean add = true;
                    dev = list_tmp[i];

                    if (ids_used != null)
                    {
                        for (int j = 0; j < ids_used.length; j++)
                        {
                            if (ids_used[j] == dev.getIddevice())
                            {
                                add = false;

                                break;
                            }
                        }
                    }

                    if (add)
                    {
                        list_sx.add(dev);
                    }
                }

                //retrieve descrizione
                SwitchConfig switchconf = SwitchConfigList.retrieveById(idsite,
                        id_switch);
                String desc = ((switchconf == null) ? ("SWITCH" + id_switch)
                                                    : switchconf.getDescription());

                StringBuffer xmlresponse = new StringBuffer();
                xmlresponse.append("<response>");
                xmlresponse.append("<description id='0'><![CDATA[" + desc +
                    "]]></description>");

                for (int i = 0; i < list_sx.size(); i++)
                {
                    dev = (DeviceBean) list_sx.get(i);
                    xmlresponse.append("<device_sx id='" + dev.getIddevice() +
                        "'>");
                    xmlresponse.append("<![CDATA[" + dev.getDescription() +
                        "]]>");
                    xmlresponse.append("</device_sx>");
                }

                //lista device gia configurati per l'istanza di switch
                int[] ids = SwitchDevList.retrieveIdsDevicesOfSwitch(idsite,
                        id_switch);

                if (ids != null)
                {
                    DeviceBean[] list = new DeviceBean[ids.length];

                    for (int i = 0; i < ids.length; i++)
                    {
                        list[i] = DeviceListBean.retrieveSingleDeviceById(idsite,
                                ids[i], language);
                    }

                    DeviceBean tmp = null;

                    for (int i = 0; i < list.length; i++)
                    {
                        tmp = list[i];
                        xmlresponse.append("<device id='" +
                            tmp.getIddevice() + "'>");
                        xmlresponse.append("<![CDATA[" +
                            tmp.getDescription() + "]]>");
                        xmlresponse.append("</device>");
                    }
                }

                xmlresponse.append("</response>");

                return xmlresponse.toString();
            }

            return "";
        }
        else if (tabName.equalsIgnoreCase("tab4name")) //pagina amministratore per settare modelli per eev
        {
        	
        	if (cmd.equalsIgnoreCase("load_switch"))
            {
        		int id_switch = Integer.parseInt(prop.getProperty("switch_id"));
                SwitchVarList switch_conf_list = new SwitchVarList(idsite,
                        id_switch, language);

                SwitchVar[] var_list = switch_conf_list.getList(idsite,
                        id_switch, false);
                Map<Integer,VarMdlBean> varMdlBeanMap = getVarMdlBeanMap(var_list);
                SwitchVar[] alr_list = switch_conf_list.getList(idsite,
                        id_switch, true);

                //creare xml per riempire 2 tabella
                StringBuffer xmlresponse = new StringBuffer();
                xmlresponse.append("<response>");

                SwitchVar tmp = null;
                DevMdlBeanList dev_list = new DevMdlBeanList();
                DevMdlBean dev = null;
                String min = null;
                String max = null;
                
                for (int i = 0; i < var_list.length; i++)
                {
                    tmp = var_list[i];
                    dev = dev_list.retrieveById(idsite, language,
                            tmp.getIddevmdl().intValue());
                    xmlresponse.append("<variable id=\"" + //idvar
                        tmp.getIdvarmdl().intValue() + "\">");
                    VarMdlBean var = varMdlBeanMap.get(tmp.getIdvarmdl());
                    if(var != null)
                    {
                    	String eev = String.valueOf(tmp.getEev());
                    	String tev = String.valueOf(tmp.getTev());
                        if(var.getType() == 1 || var.getDecimal()<=0)
                        {
                        	eev = eev.length()>=3?eev.substring(0,eev.length()-2):eev;
                        	tev = tev.length()>=3?tev.substring(0,tev.length()-2):tev;
                        }
                        xmlresponse.append("<![CDATA[" + tmp.getIddevmdl() + "," +
                            eev + "," + //dev,eev,tev
                            tev + "]]>");
                        xmlresponse.append("<![CDATA[" + dev.getDescription() +
                            " - " + tmp.getDescription() + "]]>");
                        
	                    xmlresponse.append("<![CDATA[" + var.getType() + "]]>");
	                    min = var.getMinvalue();
	                    if (min!=null && !min.contains("pk"))
	                    {
	                    	xmlresponse.append("<![CDATA[" + min + "]]>");
	                    }
	                    else
	                    {
	                    	xmlresponse.append("<![CDATA[]]>");
	                    }
	                    max = var.getMaxvalue();
	                    if (max!=null && !max.contains("pk"))
	                    {
	                    	xmlresponse.append("<![CDATA[" + max + "]]>");
	                    }
	                    else
	                    {
	                    	xmlresponse.append("<![CDATA[]]>");
	                    }
	                    xmlresponse.append("<![CDATA[" + var.getDecimal() + "]]>");
                    }
                    
                    xmlresponse.append("</variable>");
                }

                for (int i = 0; i < alr_list.length; i++)
                {
                    tmp = alr_list[i];
                    dev = dev_list.retrieveById(idsite, language,
                            tmp.getIddevmdl().intValue());
                    xmlresponse.append("<alarm id=\"" +
                        tmp.getIdvarmdl().intValue() + "\">"); //idvar
                    xmlresponse.append("<![CDATA[" + tmp.getIddevmdl() +
                        "]]>"); //dev
                    xmlresponse.append("<![CDATA[" + dev.getDescription() +
                        " - " + tmp.getDescription() + "]]>");
                    xmlresponse.append("</alarm>");
                }

                xmlresponse.append("</response>");

                return xmlresponse.toString();
            }
            else if (cmd.equalsIgnoreCase("load_varmdl"))
            {
                StringBuffer xmlresponse = new StringBuffer();
                int id_devmdl = Integer.parseInt(prop.getProperty("iddev"));

                VarMdlBean[] varmdls = VarMdlBeanList.retrieve(idsite,
                        id_devmdl, language);

                xmlresponse.append("<response>");

                VarMdlBean tmp = null;
                String code = "";
                for (int i = 0; i < varmdls.length; i++)
                {
                    tmp = varmdls[i];
                    String min = null;
                    String max = null;
                    if (tmp.getType().intValue() == 4) // modello allarme
                    {
                        xmlresponse.append("<alarm id=\"" +
                            tmp.getIdvarmdl().intValue() + "\">");
                        code = tmp.getShortdescription();
                        if (!"".equalsIgnoreCase(code))
                        {
                        	code = code + " - ";
                        }
                        xmlresponse.append("<![CDATA[" + code + tmp.getDescription() +
                            "]]>");
                        xmlresponse.append("</alarm>");
                    }
                    else if (!tmp.getReadwrite().trim().equalsIgnoreCase("1")) //modello variabile tutti i tipi tranne 1, quindi no  le read
                    {
                        xmlresponse.append("<variable id=\"" +
                            tmp.getIdvarmdl().intValue() + "\">");
                        code = tmp.getShortdescription();
                        if (!"".equalsIgnoreCase(code))
                        {
                        	code = code + " - ";
                        }
                        xmlresponse.append("<![CDATA[" + code + tmp.getDescription() +
                            "]]>");
                        xmlresponse.append("<![CDATA[" + tmp.getType() + "]]>");
	                    min = tmp.getMinvalue();
	                    if (min!=null && !min.contains("pk"))
	                    {
	                    	xmlresponse.append("<![CDATA[" + min + "]]>");
	                    }
	                    else
	                    {
	                    	xmlresponse.append("<![CDATA[]]>");
	                    }
	                    max = tmp.getMaxvalue();
	                    if (max!=null && !max.contains("pk"))
	                    {
	                    	xmlresponse.append("<![CDATA[" + max + "]]>");
	                    }
	                    else
	                    {
	                    	xmlresponse.append("<![CDATA[]]>");
	                    }
	                    xmlresponse.append("<![CDATA[" + tmp.getDecimal() + "]]>");
                        xmlresponse.append("</variable>");
                    }
                }

                xmlresponse.append("</response>");

                return xmlresponse.toString();
            }
            else if (cmd.equalsIgnoreCase("set_default"))
            {
            	int id_switch = Integer.parseInt(prop.getProperty("switch_id"));
            	//ripristino condizioni di default
            	SwitchVarList.restoreDefault(idsite,id_switch);
            	StringBuffer xmlresponse = new StringBuffer();
            	xmlresponse.append("<switch id=\"" + id_switch + "\">");
                xmlresponse.append("</switch>");
                return xmlresponse.toString();
            }
        }

        return "";
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        String cmd = prop.getProperty("cmd");
        int idsite = us.getIdSite();
        String language = us.getLanguage();
        String user = us.getUserName();
        if (tabName.equalsIgnoreCase("tab2name"))
        {
            int id_switch = Integer.parseInt(prop.getProperty("switch_id"));
            us.getCurrentUserTransaction().setProperty("selected_switch_id", String.valueOf(id_switch));
            if (cmd.equalsIgnoreCase("save_conf"))
            {
                boolean isauto = (prop.getProperty("isauto").equalsIgnoreCase("TRUE"))
                    ? true : false;

                String autoresume = (prop.getProperty("autoresume") != null)
                    ? "TRUE" : "FALSE";
                String skipalarm = (prop.getProperty("obj_skipalarm") != null)
                    ? "TRUE" : "FALSE";
                
                int alarmwait = Integer.parseInt(prop.getProperty(
                "obj_alarm_wait")) * 60;

                if (!isauto)
                {
                    String manualtype = prop.getProperty("obj_manualtype");
                    SwitchConfigList.setManualSwitch(idsite, id_switch,
                        manualtype, autoresume, skipalarm,alarmwait);
                }
                else
                {
                    int hour = Integer.parseInt(prop.getProperty("obj_hour")) * 3600;
                    int starthour = Integer.parseInt(prop.getProperty(
                                "obj_starthour"));
                    

                    Timestamp t_starthour = SwitchConfigList.getStartTimestamp(starthour);
                    String starttype = prop.getProperty("obj_starttype");
                    SwitchConfigList.setAutoSwitch(idsite, id_switch, hour,
                        t_starthour, alarmwait, starttype, autoresume, skipalarm);
                }
                SwitchStatus status = SwitchStatusList.retriveSwitchStatusById(idsite,
                        id_switch);
                String descr = SwitchConfigList.retrieveById(idsite,id_switch).getDescription();
                EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "switch",
                        "SW16", new Object[]{descr,});
                if("TRUE".equalsIgnoreCase(status.getRunning()))
                {
                	//Kevin add two sleep here to have different event insert time for "save","stop","start"
                	Thread.sleep(50);
                	SwitchMgr.getInstance().stopSwitch(id_switch, user);
                	Thread.sleep(50);
                	Switch obj_switch = new Switch();
                    obj_switch.reload(idsite, id_switch);
                    SwitchMgr.getInstance().startSwitch(id_switch, obj_switch, user);
                }
            }
            else if (cmd.equalsIgnoreCase("start"))
            {
                Switch obj_switch = new Switch();
                obj_switch.reload(idsite, id_switch);
                SwitchMgr.getInstance().startSwitch(id_switch, obj_switch, user);
            }
            else if (cmd.equalsIgnoreCase("stop"))
            {
                SwitchMgr.getInstance().stopSwitch(id_switch, user);
            }
        }
        else if (tabName.equalsIgnoreCase("tab3name"))
        {
            int id_switch = Integer.parseInt(prop.getProperty("switch_id"));
            String description = prop.getProperty("switch_description");
            String[] param = prop.getProperty("param").split(",");
            int[] ids = null;

            if (!param[0].equals(""))
            {
                ids = new int[param.length];

                for (int i = 0; i < param.length; i++)
                {
                    ids[i] = Integer.parseInt(param[i]);
                }
            }

            if (ids == null)
            {
                //salvataggio in switchdev (in realt� cancella e basta)
                SwitchDevList.saveSwitchDevConf(idsite, id_switch, ids, language,description,user);

                //delete switchconf e switchstatus
                SwitchConfigList.removeSwitchConfig(idsite, id_switch);
                SwitchStatusList.removeSwitchStatus(idsite, id_switch);
                
                //log rimozione configurazione
                EventMgr.getInstance().info(new Integer(idsite), user, "switch",
                        "SW17", new Object[]{description});
            }
            else
            {
                //salvataggio in switchdev
                SwitchDevList.saveSwitchDevConf(idsite, id_switch, ids, language,description,user);

                if (SwitchConfigList.existSwitch(idsite, id_switch)) //se switch esiste gia in switchconf e quindi switchstatus
                {
                    SwitchConfigList.updateDesc(idsite, id_switch, description); //update descrizione
                }
                else
                {
                    SwitchConfigList.insertDummySwitchConf(idsite, id_switch,
                        description); //insert dummy x switchconf
                    SwitchStatusList.insertDummySwitchStatus(idsite, id_switch); //insert dummy x switchstatus
                }
            }
            us.getCurrentUserTransaction().setProperty("restart_required", "1");
        }
        else if (tabName.equalsIgnoreCase("tab4name"))
        {
            int id_switch = Integer.parseInt(prop.getProperty("switch_id"));

            String var_conf = prop.getProperty("var_to_post");
            String alr_conf = prop.getProperty("alr_to_post");
            SwitchVarList.saveSwitchMdlConf(idsite, id_switch, var_conf,
                alr_conf, language);
            //log salavataggio variabili
            EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "switch",
                    "SW14", new Object[]{"Switch"+id_switch});
            us.getCurrentUserTransaction().setProperty("restart_required", "1");
        }
    }
    private Map getVarMdlBeanMap(SwitchVar[] var_list) throws DataBaseException
    {
    	Map varMdlBeanMap = new HashMap<Integer,VarMdlBean>();
    	int[] ids = new int[var_list.length];
    	for(int i=0;i<var_list.length;i++)
    	{
    		ids[i] = var_list[i].getIdvarmdl();
    	}
    	VarMdlBean[] varMdlBean = VarMdlBeanList.retrieveByIds(ids);
    	for(int i=0;i<varMdlBean.length;i++)
    	{
    		varMdlBeanMap.put(varMdlBean[i].getIdvarmdl(), varMdlBean[i]);
    	}
    	return varMdlBeanMap;
    }
}
