package com.carel.supervisor.presentation.bo;

import java.sql.Timestamp;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.database.RuleListBean;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBean;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.FunctionBean;
import com.carel.supervisor.dataaccess.datalog.impl.FunctionBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarlogBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.bean.rule.ConditionBeanListPres;
import com.carel.supervisor.presentation.bean.rule.RuleBeanHelper;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;


public class BAlrSched extends BoMaster
{
	private static final long serialVersionUID = -6804880907539134007L;
	private static final int REFRESH_TIME = -1;
    private final static String ADD = "add";
    private final static String DELETE = "del";
    private final static String MODIFY = "mod";

    public BAlrSched(String l)
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
        p.put("tab6name", "resizeTableSchedulerDashboard();writeTimeBandsTable(arg);");
        p.put("tab1name", "onLoadRule();resizeTableRule();");
        p.put("tab2name", "CondloadTrx();isremoved();resizeTableAlrmCond();");
        p.put("tab5name", "CondGeLoad();isremoved();resizeTableEventCond();");
        p.put("tab3name", "initialize();resizeTableEvnCond();loadTimeBands(arg);");
        p.put("tab4name", "setFocus_alrsched();resizeTableAction();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab6name", "alrsched.js;");
        //20091126-simon.zhang
        //append the virtual keyboard
        p.put("tab1name", "alrsched.js;keyboard.js;");
        p.put("tab2name", "condition.js;dbllistbox.js;keyboard.js;");
        p.put("tab5name", "condition.js;keyboard.js;");
        p.put("tab3name", "timebands.js;calendar.js;keyboard.js;");
        p.put("tab4name", "alrsched.js;keyboard.js;");

        return p;
    }
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		StringBuilder response= new StringBuilder();
		response.append("<response>");

		Integer action = Integer.parseInt((String) prop.get("action"));
		Integer idDevice = Integer.parseInt((String) prop.get("iddevice"));
		
		switch( action.intValue() ){
			case 0: // LOAD_DEVICE_VAR
				response.append("<variable>");
				response.append("<![CDATA[");
				VarphyBeanList varlist = new VarphyBeanList();
				VarphyBean[] vars = varlist.getActiveAlarmVarPhy(us.getLanguage(),us.getIdSite(),idDevice);
				response.append("<select onchange=\"onSelectAlarms()\" ondblclick=\"addAlarmToVarSel();\" id='varsel' name='varsel' multiple size='10' class='standardTxt' style='width: 100%;'>");
				for (int i=0;i<vars.length;i++)	{
					VarphyBean aux = vars[i];
					response.append("<option value=\"");
					response.append(String.valueOf(aux.getId()));
					response.append("\"");
					response.append(" class='"+( (i%2==0?"Row1":"Row2"))+"'>");
					response.append(aux.getShortDescription());
					response.append("</option>");
				}
				response.append("</select>");
				response.append("]]>");	
				response.append("</variable>");
			break;

			case 1: // LOAD_DEVICE
				response.append("<device>");
				response.append("<![CDATA[");
				DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage());
				DeviceBean tmp_dev = null;
				int[] ids = devs.getIds();
 				response.append("<select onclick=\"reload_actions(0);\" id=\"devsel\" name='devsel' size='10' class='standardTxt' style='width:100%;' >");
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					response.append("<OPTION value='"+tmp_dev.getIddevice()+"' class='"+( (i%2==0?"Row1":"Row2"))+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				response.append("</select>");
				response.append("]]>");	
				response.append("</device>");
			break;
			
			case 2: // LOAD_DEVICE_MDL
				response.append("<device>");
				response.append("<![CDATA[");
				
				devs = new DeviceListBean(us.getIdSite(),us.getLanguage(),idDevice,1);
				tmp_dev = null;
				ids = devs.getIds();
				StringBuffer div_dev = new StringBuffer();
				div_dev.append("<select multiple id=\"devsel\" name='devsel' onchange=\"reload_actions(0);\" size='10' class='standardTxt' style='width: 100%;' >");
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					div_dev.append("<OPTION value='"+tmp_dev.getIddevice()+"' class='"+( i%2==0?"Row1":"Row2" )+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				div_dev.append("</select>");
				response.append(div_dev.toString());
				response.append("]]>");	
				response.append("</device>");
			break;
			case 10: // LOAD_DEVICE_VAR
				response.append("<variable>");
				response.append("<![CDATA[");
				VarMdlBean[] varmdls = VarMdlBeanList.retrieveAlarms(us.getIdSite(),idDevice,us.getLanguage());
				response.append("<select onchange=\"onSelectAlarms()\" ondblclick=\"addAlarmToVarSel();\" id='varsel' name='varsel' multiple size='10' class='standardTxt' style='width: 100%;'>");
				for (int i=0;i<varmdls.length;i++)	{
					VarMdlBean aux = varmdls[i];
					response.append("<option value='");
					response.append(String.valueOf(aux.getIdvarmdl()));
					response.append("'");
					response.append(" class='"+( i%2==0?"Row1":"Row2" )+"' >");
					response.append(aux.getDescription());
					response.append("</option>");
				}
				response.append("</select>");
				response.append("]]>");	
				response.append("</variable>");
			break;
		}
		
		response.append("</response>");
		return response.toString();// super.executeDataAction(us, tabName, prop);
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

            if (tabName.equals("tab4name"))
            {
                this.execTab4(us, prop);

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
            }

            if (tabName.equals("tab5name"))
            {
                this.execTab5(us, tabName, prop);

                break;
            }

            break;
        }
    }

    private void execTab2(UserSession us, Properties prop)
        throws Exception
    {
        UserTransaction ut = us.getCurrentUserTransaction();
        String command = prop.getProperty("hcondcmd");

        if ((command != null) && (command.length() > 0) && !command.equalsIgnoreCase("nop"))
        {
            ut.setProperty("hcondcmd", command);
            ut.setProperty("hcondtype", prop.getProperty("hcondtype"));
            ut.setProperty("hconddesc", prop.getProperty("hconddesc"));
            ut.setProperty("hcombovalue", prop.getProperty("hcombovalue"));
            ut.setProperty("hconddata", prop.getProperty("hconddata"));
            ut.setProperty("hcondactcmd", prop.getProperty("hcondactcmd"));
            ut.setProperty("hcondid", prop.getProperty("hcondid"));
        }
        else
        {
            command = prop.getProperty("hcondactcmd");
            ut.setProperty("hcondactcmd", prop.getProperty("hcondactcmd"));

            if ((command != null) && (command.length() > 0))
            {
                ConditionBeanListPres condList = new ConditionBeanListPres(us.getIdSite(),
                        us.getLanguage());

                if (command.equalsIgnoreCase("add"))
                {
                    String data = prop.getProperty("hcondvariabili");
                    String type = prop.getProperty("condtype");
                    String desc = prop.getProperty("conddesc");
                    String pr=prop.getProperty("priority");  //aggiunto passaggio valore della priorit�
                    condList.insertAlarmCondition(BaseConfig.getPlantId(), desc, type, data,pr);

                    //Log insert condition
                    EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                        "Config", "W023", new Object[] { desc });
                    DirectorMgr.getInstance().mustRestart();
                }
                else if (command.equalsIgnoreCase("rem"))
                {
                    String idcond = prop.getProperty("hcondid");
                    String desc = condList.loadCondition(idcond).getCodeCondition();

                    if ((idcond != null) && (idcond.length() > 0))
                    {
                        //impedisco rimozione Condizione di default:
                    	if ((!idcond.equals("1")) && (condList.deleteAlarmCondition(idcond)))
                        {
                            //Log remove condition
                            EventMgr.getInstance().info(new Integer(us.getIdSite()),
                                us.getUserName(), "Config", "W024", new Object[] { desc });
                            DirectorMgr.getInstance().mustRestart();
                        }
                    	 else if(idcond.equals("1")){
                         	ut.setProperty("notremovecond", "NoDefault");
                         }
                        else
                        {
                        	//settare su ut che condition � coinvolta in una regola
                        	ut.setProperty("notremovecond", "NO");
                        }
                    }
                }
                else if (command.equalsIgnoreCase("upd"))
                {
                    String idcond = prop.getProperty("hcondid");
                    String data = prop.getProperty("hcondvariabili");
                    String type = prop.getProperty("condtype");
                    String desc = prop.getProperty("conddesc");
                    String pr=prop.getProperty("priority");  //aggiunto passaggio valore della priorit�

                    if ((idcond != null) && (idcond.length() > 0))
                    {
                        condList.updateAlarmCondition(idcond, data, type, desc,pr);
                        DirectorMgr.getInstance().mustRestart();
                    }
                }
                else if (command.equalsIgnoreCase("get"))
                {
                    ConditionBean cb = condList.loadCondition(prop.getProperty("hcondid"));

                    if (cb != null)
                    {
                        ut.setProperty("hcondid", prop.getProperty("hcondid"));
                        ut.setProperty("hcondtype", decodeType(cb));
                        ut.setProperty("hconddesc", cb.getCodeCondition());
                        ut.setProperty("hconddata", cb.getDataForClient());
                    }
                }
            }
        }
    }

    private String decodeType(ConditionBean cb)
    {
        String ret = "1";

        if (cb.getTypeCondition().equalsIgnoreCase("V"))
        {
            if (cb.getVariable().length > 0)
            {
                ret = "3";
            }
        }
        else if (cb.getTypeCondition().equalsIgnoreCase("G"))
        {
            ret = "2";
        }
        else if (cb.getTypeCondition().equalsIgnoreCase("S"))
        {
            ret = "4";
        }
        else if (cb.getTypeCondition().equalsIgnoreCase("P"))
        {
        	ret = "6";
        }

        return ret;
    }

    private void execTab3(UserSession us, Properties prop) //TIMEBAND
        throws DataBaseException
    {
        for (;;)
        {
            if (prop.get("action").equals(ADD))
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
                    ? "FALSE" : "TRUE");
                objects[7] = new Timestamp(System.currentTimeMillis());

                try
                {
					TimeBandList.addRecord(objects);

					//Log aggiunta timeband
					EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
					    "Config", "W021", new Object[] { (String) prop.get("desc") });

					DirectorMgr.getInstance().mustRestart();
				}
                catch (Exception e)
                {
					//PVPro-generated catch block
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
					
					UserTransaction ut = us.getCurrentUserTransaction();
					ut.setProperty("notremovetimeband", "noAdd");
				}

                break;
            } //if add

            if (prop.get("action").equals(DELETE))
            {
                UserTransaction ut = us.getCurrentUserTransaction();
                Integer idtimeband = new Integer((String) prop.get("idTimeBands"));

                if ((idtimeband != null) && (idtimeband.intValue() < 0))
                {
                    ut.setProperty("notremovetimeband", "-1");
                }
                else
                {
                	//impedisco rimozione TimeBand di default:
                	if ((idtimeband != null) && (idtimeband.intValue() > 3) && (!TimeBandList.isTimebandInRule(us.getIdSite(), idtimeband)))
                    {
                        String desc = TimeBandList.getDescription(us.getIdSite(),
                                idtimeband.intValue());
                        TimeBandList.deleteRecord(new Object[]
                            {
                                idtimeband, new Integer(us.getIdSite())
                            });

                        //Log rimozione timeband
                        EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                            "Config", "W022", new Object[] { desc });

                        DirectorMgr.getInstance().mustRestart();
                    } else if((idtimeband != null) && (idtimeband.intValue() <= 3 && idtimeband.intValue() >= 1)){
                    	ut.setProperty("notremovetimeband", "NoDefault");
                    }else
                    {
                        ut.setProperty("notremovetimeband", "NO");
                    }
                }

                break;
            } //if delete

            if (prop.get("action").equals(MODIFY))
            {
                UserTransaction ut = us.getCurrentUserTransaction();
                Integer idtimeband = new Integer((String) prop.get("idTimeBands"));

                if (idtimeband.intValue() < 0)
                {
                    ut.setProperty("notremovetimeband", "-1");
                }
                else{
	                    Object[] objects = new Object[8];
	                    objects[7] = idtimeband;
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
	                }

                break;
            } //if modify

            break;
        } //forswitch
    } //execTab3

    private void execTab4(UserSession us, Properties prop)
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
            if(!abl.checkCodeUnic(descr,"FALSE"))
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
            if(!abl.checkCodeUnic(descr,"FALSE"))
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
                // Trava - Check double name
                if(checkRuleNameDouble(desc))
                {
                    us.setProperty("alrschedchkdb","T");
                    break;
                }
                // End
                
                RuleBean new_rule = new RuleBean();
                new_rule.setIdsite(new Integer(idsite));
                new_rule.setRuleCode(desc);
                new_rule.setActionCode(new Integer(prop.getProperty("idaction")));

                //se condition = 0, su db metto a null
                if (prop.getProperty("idcondition").equals("0"))
                {
                    new_rule.setIdCondition(null);
                }
                else
                {
                    new_rule.setIdCondition(new Integer(prop.getProperty("idcondition")));
                }

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

                Integer idcondition = null;

                if (idrule.intValue() > 0)
                {
                    if (!prop.getProperty("idcondition").equals("0"))
                    {
                        idcondition = new Integer(prop.getProperty("idcondition"));
                    }

                    Integer idtimeband = null;

                    if (!prop.getProperty("idtimeband").equals("0"))
                    {
                        idtimeband = new Integer(prop.getProperty("idtimeband"));
                    }

                    Integer idaction = new Integer(prop.getProperty("idaction"));
                    int tmp_delay = Integer.parseInt(prop.getProperty("delay")) * 60;
                    Integer delay = new Integer(tmp_delay);

                    RuleListBean.updateRule(new Integer(idsite), idrule, enable, descr,
                        idcondition, idtimeband, idaction, delay, us.getUserName());

                    DirectorMgr.getInstance().mustRestart();
                }
                else if (idrule.intValue() < 0)
                {
                    RuleListBean.updateIdeRule(new Integer(idsite), idrule, enable);

                    DirectorMgr.getInstance().mustRestart();
                }

                break;
            }

            if (cmd.equals("modify_rule"))
            {
                break;
            }

            break;
        }
    }

    private void execTab5(UserSession us, String tabName, Properties prop)
    {
        UserTransaction ut = us.getCurrentUserTransaction();
        String clcmd = (String) prop.remove("hcondgecmd");

        if (clcmd != null)
        {
            if (clcmd.equalsIgnoreCase("dev"))
            {
                ut.setProperty("hcondgecmd", clcmd);
                ut.setProperty("hcondgeid", prop.getProperty("hcondgeid"));
                ut.setProperty("hcondgecurdev", prop.getProperty("condgedev"));
                ut.setProperty("conddesc", prop.getProperty("conddesc"));
            }
            else if (clcmd.equalsIgnoreCase("devfun"))
            {
                ut.setProperty("conddesc", prop.getProperty("conddesc"));
                ut.setProperty("hcondgeid", prop.getProperty("hcondgeid"));
                ut.setProperty("hcondgecmd", clcmd);
                ut.setProperty("hcondgecurdev", prop.getProperty("condgedev"));
                ut.setProperty("hcondgecurvar", prop.getProperty("condgevar"));
                ut.setProperty("condgetype", prop.getProperty("condgetype"));
                ut.setProperty("hcondgeisdigit", prop.getProperty("hcondgeisdigit"));

                String isDig = ut.getProperty("hcondgeisdigit");

                String funcVal = "";

                if ((isDig != null) && isDig.equalsIgnoreCase("T"))
                {
                    funcVal = prop.getProperty("condgeoptdigit");
                }
                else
                {
                    funcVal = prop.getProperty("condgeoptanal");
                }

                ut.setProperty("hcondgecurfun", funcVal);

                ut.setProperty("hcondgecurdevfun", prop.getProperty("condgedev2"));
                ut.setProperty("hcondgecurvarfun", prop.getProperty("condgevar2"));
            }
            else if (clcmd.equalsIgnoreCase("add"))
            {
                String condDesc = prop.getProperty("conddesc");
                String condVar = prop.getProperty("condgevar");
                String type = prop.getProperty("condgetype");
                String svarval = prop.getProperty("condgevar2");
                String functio = "";
                String costval = "";
                String sVal = "";

                boolean isvar = true;
                boolean isDigit = ((condVar != null) && condVar.endsWith("D"));
                int logicType = 0;

                if (isDigit)
                {
                    logicType = 1;
                    functio = prop.getProperty("condgeoptdigit");
                    costval = prop.getProperty("condgecostvd");
                    condVar = condVar.substring(0, condVar.length() - 1);

                    if ((svarval != null) && svarval.endsWith("D"))
                    {
                        svarval = svarval.substring(0, svarval.length() - 1);
                    }
                }
                else
                {
                    functio = prop.getProperty("condgeoptanal");
                    costval = prop.getProperty("condgecostva");
                }

                if ((type != null) && type.equalsIgnoreCase("5"))
                {
                    isvar = false;
                    sVal = costval;
                }
                else
                {
                    sVal = svarval;
                }

                ConditionBeanList condBean = new ConditionBeanList(us.getIdSite(), us.getLanguage());

                try
                {
                    condBean.insertGlobalCondition(BaseConfig.getPlantId(), condDesc, type,
                        functio, condVar, sVal, isvar, logicType);

                    DirectorMgr.getInstance().mustRestart();
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
            else if (clcmd.equalsIgnoreCase("rem"))
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                ConditionBeanList condBean = new ConditionBeanList(us.getIdSite(), us.getLanguage());

                try
                {
                    int iIdVar = 0;
                    String sIdVar = prop.getProperty("hcondgeid");

                    if (sIdVar != null)
                    {
                        try
                        {
                            iIdVar = Integer.parseInt(sIdVar);
                        }
                        catch (Exception e2)
                        {
                            logger.error(e2);
                        }
                    }

                    if (!condBean.deleteGeneralCondition(iIdVar))
                    {
                    	//settare ut perch� condizione coinvolta in una regola
                    	ut.setProperty("notremovecond", "NO");
                    }
                    else
                    {
                    	DirectorMgr.getInstance().mustRestart();
                    }
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
            }
            else if (clcmd.equalsIgnoreCase("get"))
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());

                ConditionBeanList condList = new ConditionBeanList(us.getIdSite(), us.getLanguage());
                ConditionBean conBean = null;
                FunctionBeanList funcList = new FunctionBeanList();
                FunctionBean funBean = null;

                int iIdCon = 0;
                int[] funCode = null;
                Object[] decodeParam = null;
                int idVar1 = 0;
                float idVar2 = 0;
                boolean isconvar = false;
                int idDev1 = 0;
                int idDev2 = 0;
                String sIdCon = "";

                try
                {
                    sIdCon = prop.getProperty("hcondgeid");

                    if (sIdCon != null)
                    {
                        try
                        {
                            iIdCon = Integer.parseInt(sIdCon);
                        }
                        catch (Exception e2)
                        {
                            logger.error(e2);
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error(e);
                }

                try
                {
                    conBean = condList.loadGeneralCondition(iIdCon);

                    if (conBean != null)
                    {
                        VarphyBeanList varList = new VarphyBeanList();
                        VarlogBeanList logList = new VarlogBeanList();

                        funCode = logList.retriveLogicVariable(conBean.getIdVarGen());

                        if (funCode[0] != 0)
                        {
                            funBean = funcList.getFunctionByCode(funCode[0]);
                        }

                        if (funBean != null)
                        {
                            decodeParam = funBean.decodeParamField();
                        }

                        if (decodeParam != null)
                        {
                            if (((Integer)decodeParam[1]).intValue() == 1)
                            {
                                idVar1 = (Integer) decodeParam[0];
                                idDev1 = varList.getDeviceOfVariable(idVar1, us.getIdSite());
                            }

                            idVar2 = (Float)decodeParam[2];

                            if (((Integer) decodeParam[3]) == 1)
                            {
                                isconvar = true;
                                int idVar2_int = (int) idVar2;
                                
                                idDev2 = varList.getDeviceOfVariable(idVar2_int, us.getIdSite());
                            }

                            // Param to client
                            ut.setProperty("hcondgeid", sIdCon);
                            ut.setProperty("conddesc", conBean.getCodeCondition());
                            ut.setProperty("hcondgecmd", clcmd);
                            ut.setProperty("hcondgecurdev", String.valueOf(idDev1));
                            ut.setProperty("hcondgecurvar", String.valueOf(idVar1));
                            ut.setProperty("hcondgecurfun", funBean.getOperator());

                            if (isconvar)
                            {
                                ut.setProperty("condgetype", "1");
                                ut.setProperty("hcondgecurdevfun", String.valueOf(idDev2));
                                ut.setProperty("hcondgecurvarfun", String.valueOf((int)idVar2));

                                if (funCode[1] == 1)
                                {
                                    ut.setProperty("hcondgeisdigit", "T");
                                }
                                else
                                {
                                    ut.setProperty("hcondgeisdigit", "F");
                                }
                            }
                            else
                            {
                                ut.setProperty("condgetype", "5");
                                ut.setProperty("disablecondgedev2","disabled");
                                if (funCode[1] == 1)
                                {
                                    ut.setProperty("hcondgeisdigit", "T");
                                    ut.setProperty("condgecostvd", String.valueOf(idVar2));
                                }
                                else
                                {
                                    ut.setProperty("condgecostva", String.valueOf(idVar2));
                                    ut.setProperty("hcondgeisdigit", "F");
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
            }
            else if (clcmd.equalsIgnoreCase("upd"))
            {
                String sIdCond = prop.getProperty("hcondgeid");
                String condDesc = prop.getProperty("conddesc");
                String condVar = prop.getProperty("condgevar");
                String type = prop.getProperty("condgetype");
                String svarval = prop.getProperty("condgevar2");
                String functio = "";
                String costval = "";
                String sVal = "";

                boolean isvar = true;
                boolean isDigit = ((condVar != null) && condVar.endsWith("D"));

                if (isDigit)
                {
                    functio = prop.getProperty("condgeoptdigit");
                    costval = prop.getProperty("condgecostvd");
                    condVar = condVar.substring(0, condVar.length() - 1);

                    if ((svarval != null) && svarval.endsWith("D"))
                    {
                        svarval = svarval.substring(0, svarval.length() - 1);
                    }
                }
                else
                {
                    functio = prop.getProperty("condgeoptanal");
                    costval = prop.getProperty("condgecostva");
                }

                if ((type != null) && type.equalsIgnoreCase("5"))
                {
                    isvar = false;
                    sVal = costval;
                }
                else
                {
                    sVal = svarval;
                }

                ConditionBeanList condBean = new ConditionBeanList(us.getIdSite(), us.getLanguage());
                condBean.updateGeneralCondition(sIdCond, condDesc, type, functio, condVar, sVal,
                    isvar, isDigit);

                DirectorMgr.getInstance().mustRestart();
            }
        }
    }
    
    private boolean checkRuleNameDouble(String desc)
    {
        boolean ris = false;
        String sql = "select * from cfrule where rulecode=? and idcondition is not null";
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new String[]{desc});
            if(rs != null && rs.size() > 0)
                ris = true;
        } catch(Exception e){}
        return ris; 
    }
}
