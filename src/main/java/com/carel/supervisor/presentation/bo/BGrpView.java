package com.carel.supervisor.presentation.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.GroupCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList;
import com.carel.supervisor.presentation.bean.GroupVarBean;
import com.carel.supervisor.presentation.bean.GroupVarBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.devices.DeviceList;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;


public class BGrpView extends BoMaster
{
    private static final int REFRESH_TIME = 30000;
    private static final String PREX = "gv";

    public BGrpView(String l)
    {
        super(l, 0);
    }

    protected Map initializeRefresh()
    {
        Map map = new HashMap();
        RefreshBean[] rb = 
            {
                new RefreshBean("TDevice", IRefresh.R_DEVICE, 9, ""),
                new RefreshBean("TAlarm", IRefresh.R_ALARMS, 1)
            };
        RefreshBeanList rbl = new RefreshBeanList(REFRESH_TIME, 2);
        rbl.setRefreshObj(rb);

        RefreshBean[] rb2 = 
            {
                new RefreshBean("TAlarm", IRefresh.R_ALARMS, 1),
                new RefreshBean("TAlarmCalledOf", IRefresh.R_ALARMSCALL, 2)
            };
        RefreshBeanList rbl2 = new RefreshBeanList(REFRESH_TIME, 2);
        rbl2.setRefreshObj(rb2);

        map.put("tab1name", rbl);
        map.put("tab3name", rbl2);

        return map;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        
        p.put("tab1name", "grpviewLoad();");
        p.put("tab2name", "grpviewLoadTab2();");
        p.put("tab3name", "grpviewLoadTab3();");
       
        p.put("tab4name", "initialize();");
        //p.put("tab4name", "initialize()");        
        p.put("tab5name", "initialize();");
        //p.put("tab5name", "initialize()");
        
        p.put("tab6name", "initialize();");
        p.put("tab7name", "setdefault();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        
        String virtkey = "";
        //determino se 锟�abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
            virtkey = ";keyboard.js;";
        }
        
        Properties p = new Properties();
        
        p.put("tab1name", "grpview.js"+virtkey);
        p.put("tab2name", "grpview.js"+virtkey);
        p.put("tab3name", "grpview.js"+virtkey);
        p.put("tab4name", "graphdialog_support.js;../jquery/jquery-1.11.1.min.js;graphconf.js;../arch/jscolor/jscolor.js;showModalDialog.js;graph.js"+virtkey);
        p.put("tab5name", "graphdialog_support.js;../jquery/jquery-1.11.1.min.js;graphconf.js;../arch/jscolor/jscolor.js;showModalDialog.js;graph.js"+virtkey);
        p.put("tab6name", "graphconf.js"+virtkey);
        
        p.put("tab7name", "note.js"+virtkey);

        return p;
    }
    public String executeDataAction(UserSession us, String tabName,
            Properties prop) throws Exception
    {
    	StringBuffer response = new StringBuffer("<response>");
    	String cmd = prop.getProperty("cmd");
    	if("print_allDevices".equalsIgnoreCase(cmd )) {
    		String language = us.getLanguage();
    		String sessionName="grpview";
    		LangService multiLanguage = LangMgr.getInstance().getLangService(language);
    		String title = multiLanguage.getString(sessionName,"device");
    		DeviceList devList = new DeviceList(us,title,true);
    		response.append(devList.getAllDevices4Print(language));
    	}
    	if("print_allAlarms".equalsIgnoreCase(cmd )) {
    		String language = us.getLanguage();
    		String sessionName="grpview";
    		LangService multiLanguage = LangMgr.getInstance().getLangService(language);
    		String title = multiLanguage.getString(sessionName,"alarms");
    		AlarmList alarmList = new AlarmList(title);
    		alarmList.setLink(false);
    		alarmList.loadAllFromDataBase(us);
    		response.append(alarmList.getAllAlarms4Print(language,multiLanguage,us));
    	}
    	return response.append("</response>").toString() ;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        for (;;)
        {
        	// ENHANCEMENT 20090211 - Merged the sixth tab (graph conf) in the fourth/fifth tabs (haccp/historical graph pages).
        	if (tabName.equals("tab4name")) {// HACCP
        		// stores on DB the graph layout config
        		if(prop.getProperty("save") != null) {
        			saveGeneralGraphConfiguration(us, prop);
        		}        		
        	}
        	if (tabName.equals("tab5name")) {// Historical
        		// stores on DB the graph layout config
        		if(prop.getProperty("save") != null) {
        			saveGeneralGraphConfiguration(us, prop);
        		}        		
        	}
        	// deprecated
//            if (tabName.equals("tab6name"))
//            {
//                this.saveGeneralGraphConfiguration(us, prop);
//
//                break;
//            }

            if (tabName.equals("tab2name"))
            {
                this.execTab2(us, prop);

                break;
            }

            break;
        } //forswitch
    } //executePostAction

    private void saveGeneralGraphConfiguration(UserSession us, Properties prop)
    {
        us.getTransaction().setGraphParameter(prop);
        ConfigurationGraphBeanList graphBeanList = new ConfigurationGraphBeanList();
        try
        {
            graphBeanList.saveCosmetic(us, prop);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

    } //saveGraphConfiguration

    private void execTab2(UserSession us, Properties prop)
    {
        GroupVarBeanList gvarlist = new GroupVarBeanList();

        GroupVarBean tmp_gv = null;
        int idvar = -1;
        String key = "";
        String val = "";
        Iterator i = prop.keySet().iterator();
        List listVar = new ArrayList();
        List listValue = new ArrayList();

        while (i.hasNext())
        {
            key = (String) i.next();

            if (key.startsWith(PREX))
            {
                val = prop.getProperty(key);
                key = key.substring(PREX.length());

                if (!val.equals(""))
                {
                    try
                    {
                        idvar = Integer.parseInt(key);
                    }
                    catch (Exception e)
                    {
                    }

                    //per ogni variabile di gruppo prendo i param, cio锟�gli id delle variabili coinvolte
                    try
                    {
                        tmp_gv = gvarlist.retrieveGroupVarById(us.getIdSite(), idvar,
                                us.getLanguage());

                        String tmp_params = tmp_gv.getParameters().replace("pk", "");
                        String[] id_vars = tmp_params.split(";");

                        for (int j = 0; j < id_vars.length; j++)
                        {
                            try
                            {
                                idvar = Integer.parseInt(id_vars[j]);
                                listVar.add(new Integer(idvar));
                                listValue.add(val);
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
                    }
                }
            }
        }

        int[] idVariable = new int[listVar.size()];

        for (int j = 0; j < listVar.size(); j++)
        {
            idVariable[j] = ((Integer) listVar.get(j)).intValue();
        }

        String[] values = (String[]) listValue.toArray(new String[listValue.size()]);
        
        SetContext setContext = new SetContext();
        setContext.setLanguagecode(us.getLanguage());
        setContext.addVariable(idVariable, values);
        setContext.setCallback(new GroupCallBack());
        setContext.setUser(us.getUserName());
        SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
  
    } //execTab6
} //Class BGrpView
