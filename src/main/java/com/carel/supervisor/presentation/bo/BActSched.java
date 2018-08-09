package com.carel.supervisor.presentation.bo;

import java.sql.Timestamp;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.database.RuleListBean;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.bean.rule.RuleBeanHelper;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;


public class BActSched extends BoMaster
{
    private static final int REFRESH_TIME = -1;
    private final static String ADD = "add";
    private final static String DELETE = "del";
    private final static String MODIFY = "mod";

    public BActSched(String l)
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
        p.put("tab4name", "resizeTableSchedulerDashboard();writeTimeBandsTable(arg);");
        p.put("tab1name", "onLoadRule();resizeTableRule();");
        p.put("tab2name", "initialize();resizeTableEvnCond();loadTimeBands(arg);");
        p.put("tab3name", "setFocus_actsched();resizeTableAction();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab4name", "alrsched.js;");
        //20091126-simon.zhang
        //append the virtual keyboard
        p.put("tab1name", "actsched.js;keyboard.js;");
        p.put("tab2name", "timebands.js;calendar.js;keyboard.js;");
        p.put("tab3name", "actsched.js;keyboard.js;");

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        for (;;)
        {
            if (tabName.equals("tab1name"))
            {
                this.execTab1(us, prop);

                break;
            }

            if (tabName.equals("tab3name"))
            {
                this.execTab3(us, prop);

                break;
            }

            if (tabName.equals("tab2name"))
            {
                this.execTab2(us, prop);

                break;
            } //if 

            break;
        } //forswitch
    }

    private void execTab2(UserSession us, Properties prop)
        throws DataBaseException
    {
        for (;;)
        {
            if (prop.get("action").equals(ADD))
            {
                AddTimebind(us,prop);
                break;
            } //if add

            if (prop.get("action").equals(DELETE))
            {
                UserTransaction ut = us.getCurrentUserTransaction();
                Integer idtimeband = new Integer((String) prop.get("idTimeBands"));

                //impedisco rimozione TimeBand di default:
                if ((idtimeband != null) && (idtimeband.intValue() > 3) && (!TimeBandList.isTimebandInRule(us.getIdSite(), idtimeband)))
                {
                    String desc = TimeBandList.getDescription(us.getIdSite(), idtimeband.intValue());
                    TimeBandList.deleteRecord(new Object[] { idtimeband, new Integer(us.getIdSite()) });

                    //Log rimozione timeband
                    EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                        "Config", "W022", new Object[] { desc });

                    DirectorMgr.getInstance().mustRestart();
                }
                else if((idtimeband != null) && (idtimeband.intValue() <= 3 && idtimeband.intValue() >=1)){
                	ut.setProperty("notremovetimeband", "NoDefault");
                }else 
                {
                    ut.setProperty("notremovetimeband", "NO");
                }

                break;
            } //if delete

            if (prop.get("action").equals(MODIFY))
            {
                Object[] objects = new Object[8];
                objects[7] = new Integer((String) prop.get("idTimeBands"));
                objects[0] = BaseConfig.getPlantId();
                objects[1] = new Integer(us.getIdSite());
                objects[2] = (String) prop.get("desc");
                objects[3] = new Integer((String) prop.get("type"));
                objects[4] = (String) prop.get("timeBandValue");
                objects[5] = ((((Integer) objects[3]).intValue() == TimeBandBean.YEAR_ONE_SHOT)
                    ? "false" : "true");
                objects[6] = new Timestamp(System.currentTimeMillis());

                TimeBandList.updateRecord(objects);

                DirectorMgr.getInstance().mustRestart();

                break;
            } //if modify

            break;
        } //forswitch
    } //execTab3

    private void execTab3(UserSession us, Properties prop)
        throws Exception
    {
        String cmd = us.getProperty("cmd");
        UserTransaction ut = us.getCurrentUserTransaction();
        String t = us.getProperty("sched");
        boolean isScheduled = t.equalsIgnoreCase("TRUE");

        if (cmd.equals("new_action"))
        {
            String descr = prop.getProperty("description_txt");
            ActionBeanList abl = new ActionBeanList();
            if(!abl.checkCodeUnic(descr,"TRUE"))
            {
            	abl.insertActionX(us.getIdSite(), descr, isScheduled);
            	
            	EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(), "Config",
            								"W019", new Object[] { descr });

            	DirectorMgr.getInstance().mustRestart();
            }
            else
            	us.setProperty("duplicatecode","T");
        }
        else if (cmd.equals("remove_action"))
        {
            int actioncode = Integer.parseInt(prop.getProperty("action_to_remove"));
            
            //controllo che l'azione da eliminare non sia coinvolta in una regola
            if ((!ActionBeanList.isDefaultAction(us.getIdSite(), actioncode)) && (!ActionBeanList.isActionInRule(us.getIdSite(), actioncode)))
            {
                String descr = ActionBeanList.getDescription(us.getIdSite(), actioncode);

                new ActionBeanList().deleteAllActionByActioncode(us.getIdSite(), actioncode);

                //Log rimozione azione
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                    "Config", "W020", new Object[] { descr });

                DirectorMgr.getInstance().mustRestart();
            }
            else
            {
                ut.setProperty("isremoved", "NO");
            }
        }
        else if (cmd.equals("set_description"))
        {
            //update code
            int actioncode = Integer.parseInt(prop.getProperty("action_to_remove"));
            String descr = prop.getProperty("description_txt");
            ActionBeanList abl = new ActionBeanList();
            if(!abl.checkCodeUnic(descr,"TRUE"))
            	ActionBeanList.updateCodeOfAction(us.getIdSite(), actioncode, descr);
            else
            	us.setProperty("duplicatecode","T");
        }
    }

    private void execTab1(UserSession us, Properties prop)
        throws DataBaseException
    {
        String cmd = us.getProperty("cmd");
        int idsite = us.getIdSite();

        for (;;)
        {
            if (cmd.equals("new_rule"))
            {
                String desc = prop.getProperty("description_txt");
                if(checkRuleNameDouble(desc))
                {
                    us.setProperty("actschedchkdb","T");
                    break;
                }
                
                RuleBean new_rule = new RuleBean();
                new_rule.setIdsite(new Integer(idsite));
                new_rule.setRuleCode(desc);
                new_rule.setActionCode(new Integer(prop.getProperty("idaction")));
                new_rule.setIdCondition(null);

                //per timeband idem vedi sopra
                if (prop.getProperty("idtimeband").equals("0"))
                {
                    new_rule.setIdTimeband(null);
                }
                else
                {
                    new_rule.setIdTimeband(new Integer(prop.getProperty("idtimeband")));
                }

                String enable = ""; //regola abilitata o no 

                if (prop.getProperty("enabled") != null)
                {
                    enable = "TRUE";
                }
                else
                {
                    enable = "FALSE";
                }

                new_rule.setDelay(Integer.parseInt(prop.getProperty("delay")) * 60);
                new_rule.setIsenabled(enable);
                new_rule.saveRule();

                String msg = desc + " (" + (enable.equals("TRUE")?"enabled":"disabled") + ")";
                
                //log inserimento regola
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),"Config", "W025", new Object[] { msg });

                DirectorMgr.getInstance().mustRestart();

                break;
            }

            if (cmd.equals("remove_rule"))
            {
                int idrule = Integer.parseInt(prop.getProperty("idrule"));
                String desc = RuleBeanHelper.getRuleDescription(us.getIdSite(), idrule);
                
                // impedisco rimozione regole di default:
                if ((idrule > 2) && (!RuleBeanHelper.isDefaultRule(us.getIdSite(), idrule)))
                {
	                RuleListBean.deleteRule(idsite, idrule);
	
	                //log rimozione regola
	                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
	                    "Config", "W026", new Object[] { desc });
	
	                DirectorMgr.getInstance().mustRestart();
                }
                else
                {
                	us.setProperty("isremoved", "NO");
                }

                break;
            }

            if (cmd.equals("set_rule"))
            {
                String descr = prop.getProperty("description_txt");
                
                Integer idrule = new Integer(prop.getProperty("idrule"));
                
                String enable = "";

                if (prop.getProperty("enabled") != null)
                {
                    enable = "TRUE";
                }
                else
                {
                    enable = "FALSE";
                }

                Integer idtimeband = null;

                if (!prop.getProperty("idtimeband").equals("0"))
                {
                    idtimeband = new Integer(prop.getProperty("idtimeband"));
                }

                Integer idaction = new Integer(prop.getProperty("idaction"));
                int tmp_delay = Integer.parseInt(prop.getProperty("delay")) * 60;
                Integer delay = new Integer(tmp_delay);

                RuleListBean.updateRule(new Integer(idsite), idrule, enable, descr, null,
                    idtimeband, idaction, delay, us.getUserName());

                DirectorMgr.getInstance().mustRestart();

                break;
            }

            if (cmd.equals("modify_rule"))
            {
                break;
            }

            break;
        }
    }

    private boolean checkRuleNameDouble(String desc)
    {
        boolean ris = false;
        String sql = "select * from cfrule where rulecode=? and idcondition is null";
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new String[]{desc});
            if(rs != null && rs.size() > 0)
                ris = true;
        } catch(Exception e){}
        return ris; 
    }
    
    public int AddTimebind(UserSession us, Properties prop)
    	throws DataBaseException
    {
    	SeqMgr seqMgr = SeqMgr.getInstance();
        Object[] objects = new Object[8];
        objects[0] = seqMgr.next(null, "cftimeband", "idtimeband");
        objects[1] = BaseConfig.getPlantId();
        objects[2] = new Integer(us.getIdSite());
        objects[3] = (String) prop.get("desc");
        objects[4] = new Integer((String) prop.get("type"));
        objects[5] = (String) prop.get("timeBandValue");
        objects[6] = ((((Integer) objects[4]).intValue() == TimeBandBean.YEAR_ONE_SHOT)
            ? "false" : "true");
        objects[7] = new Timestamp(System.currentTimeMillis());

        TimeBandList.addRecord(objects);

        //Log aggiunta timeband
        EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
            "Config", "W021", new Object[] { (String) prop.get("desc") });

        DirectorMgr.getInstance().mustRestart();
        return Integer.parseInt(objects[0].toString());
    }
}
