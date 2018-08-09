package com.carel.supervisor.presentation.bean.rule;

import java.util.Map;
import java.util.TreeMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.database.RuleListBean;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBean;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bo.BSetAction;
import com.carel.supervisor.presentation.io.CioEVT;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class RuleBeanHelper extends RuleListBean
{
	private int screenw = 1024;
	private int screenh = 768;
	private boolean status = true;
	
	String timebandArg = "";
    public RuleBeanHelper(Integer idSite) throws DataBaseException
    {
        super();
        loadAllRules(idSite);
    }
    
    public RuleBeanHelper(Integer idSite, boolean isScheduled, boolean isEnabled) throws DataBaseException
    {
        super();
        if( isScheduled )
        {
        	if( isEnabled )
        		loadEnabledNoCondRules(idSite);
        	else
        		loadNoCondRules(idSite);
        }
        else
        {
        	if( isEnabled )
        		loadEnabledCondRules(idSite);
        	else	
        		loadAllRules(idSite);	
        }
    }
 
    /*   
    // type 0: all (cond) rules
    // type 1: all enabled rules
    // type 2: all no cond rules
    // type 3: enabled cond rules
    // type 4: enabled no cond rules
    public RuleBeanHelper(Integer idSite, int type) throws DataBaseException
    {
        super();
        switch( type ) {
        case 0:
        	loadAllRules(idSite);
        	break;
        case 1:
        	loadEnabledRules(idSite);
        	break;
        case 2:
        	loadNoCondRules(idSite);
        	break;
        case 3:
        	loadEnabledCondRules(idSite);
        	break;
        case 4:
        	loadEnabledNoCondRules(idSite);
        	break;
        }
    }
 	*/
    
    private String getAddressBookLink(Map<String, ActionBean> actions,String[] types,UserSession us)
    {
    	String language = us.getLanguage();
    	StringBuffer buffer = new StringBuffer();
    	for(int i=0;i<types.length;i++)
    	{
    		String type = types[i];
    		ActionBean action = actions.get(type);
    		if(action != null)
			{
				String temp = action.getParameters();
				if(temp != null && !temp.equals(""))
				{	
					String[] temp_s = temp.split(";");
					int[] temp_i = new int[temp_s.length];
					for(int p=0;p<temp_i.length;p++)
					{
						temp_i[p] = Integer.parseInt(temp_s[p]);
					}
					DispatcherBook[] book = DispatcherBookList.getInstance().loadReceivers(temp_i);
					boolean first = true;
					for(int p=0;p<book.length;p++)
					{
						if(status && !book[p].getIoteststatus().equalsIgnoreCase("OK"))
						{
							status = false;
						}
						if(first == true)
						{

							buffer.append("<tr><td class='td' width='10%'>");
							
							if (us.isMenuActive("setaction"))
							{
								buffer.append("<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('"+
								"nop&folder=setaction"+(action.isScheduled()?"2":"")+"&bo=BSetAction&type=click&resource=SubTab"+getPageNumber(type)+".jsp&actioncode="+action.getActioncode()+"&curTab=tab"+getPageNumber(type)+"name&sched="+(action.isScheduled()?"true":"false")+"&desc=ncode08');>"+
								getActionDescrition(type,language)+"</a>");
							}
							else
								buffer.append(getActionDescrition(type,language));
							
							buffer.append("</td><td class='td'>");
							buffer.append(book[p].getAddress());
							buffer.append("</td></tr>");
							first = false;
						}
						else
						{
							//buffer.append("<font color='#EFF1FE'>"+getActionDescrition(type,language)+":</font>"+book[p].getAddress()+"<br>");
							buffer.append("<tr><td class='td'>&nbsp</td><td class='td'>");
							buffer.append(book[p].getAddress());
							buffer.append("</td></tr>");
						}
					}
				}
			}
    	}
    	return buffer.toString();
    }
    private String getActionDescrition(String type,String language)
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	if(type.equals("F"))
    		return lan.getString("action", "fax");
    	else if(type.equals("S"))
    		return lan.getString("action", "sms");
    	else if(type.equals("E"))
    		return lan.getString("action", "email");
    	else if(type.equals("L"))
    		return lan.getString("action","relay");
    	else if(type.equals("V"))
    		return lan.getString("action","variable");
    	else if(type.equals("D"))
    		return lan.getString("action","remote");
    	else if(type.equals("P"))
    		return lan.getString("action","print");
    	else if(type.equals("W"))
    		return lan.getString("action","window");
    	else
    		return "";
    }
    private String getActionDescrition(String type,String language,boolean isschedule)
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	if(type.equals("F"))
    		return lan.getString("action", "fax");
    	else if(type.equals("S"))
    		return lan.getString("action", "sms");
    	else if(type.equals("E"))
    		return lan.getString("action", "email");
    	else if(type.equals("L"))
    		return lan.getString("action","relay");
    	else if(type.equals("V"))
    		return lan.getString("action","variable");
    	else if(type.equals("D"))
    		return lan.getString("action","remote");
    	else if(type.equals("P") && isschedule)
    		return lan.getString("setaction2","tab7name");
    	else if(type.equals("P") && !isschedule)
    		return lan.getString("action","print");
    	else if(type.equals("W"))
    		return lan.getString("action","window");
    	else
    		return "";
    }
    private int getPageNumber(String type)
    {
    	if(type.equals("F"))
    		return 1;
    	else if(type.equals("S"))
    		return 2;
    	else if(type.equals("E"))
    		return 3;
    	else if(type.equals("L"))
    		return 4;
    	else if(type.equals("V"))
    		return 5;
    	else if(type.equals("D"))
    		return 6;
    	else if(type.equals("P"))
    		return 7;
    	else if(type.equals("W"))
    		return 7;
    	else
    		return 1;
    }
    
    private String getRelayLink(Map<String, ActionBean> actions,RelayBeanList relays,int idsite, UserSession us ,boolean isScheduled)
    throws Exception
    {
    	String language = us.getLanguage();
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	ActionBean action = actions.get("L");
    	StringBuffer buffer = new StringBuffer();
    	if(action != null)
    	{
    		String temp = action.getParameters();
    		if(temp != null && !temp.equals(""))
			{	
    			String active = "Active"; //lan.getString("relaymgr", "active");
    			String noactive = "Not active"; //lan.getString("relaymgr", "noactive");
				String[] temp_s = temp.split(";");
				boolean first = true;
				for(int j=0;j<temp_s.length;j++)
				{
					String[] temp_s2 = temp_s[j].split("=");
					int idrelay = Integer.parseInt(temp_s2[0]);
					String value = temp_s2[1].equals("0")?noactive:active;
					
					RelayBean relay = relays.getRelayBeanById(idrelay);
					if(status && !relay.getIoteststatus().equalsIgnoreCase("OK"))
					{
						status = false;
					}
					if(first == true)
					{
						buffer.append("<tr><td class='td' width='10%'>");
						if(isScheduled == false)
						{
							if (us.isMenuActive("setaction"))
							{
								buffer.append("<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('"+
										"nop&folder=setaction&bo=BSetAction&type=click&resource=SubTab"+getPageNumber("L")+".jsp&curTab=tab"+getPageNumber("L")+"name&actioncode="+action.getActioncode()+"&sched=false&desc=ncode08');>"+getActionDescrition("L",language)+"</a>");
							}
							else
								buffer.append(getActionDescrition("L",language));
						}
						else
						{
							if (us.isMenuActive("setaction2"))
							{
								buffer.append("<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('"+
										"nop&folder=setaction2&bo=BSetAction&type=click&resource=SubTab"+getPageNumber("L")+".jsp&curTab=tab"+getPageNumber("L")+"name&actioncode="+action.getActioncode()+"&sched=true&desc=ncode08');>"+getActionDescrition("L",language)+"</a>");
							}
							else
								buffer.append(getActionDescrition("L",language));
						}
						buffer.append("</td><td class='td'>");
						buffer.append(relay.getDescription()+"("+relay.getDeviceDesc()+")"+"->"+value);
						buffer.append("</td></tr>");
						first = false;
					}
					else
					{
//						buffer.append("<font color='#EFF1FE'>"+
//								getActionDescrition("L",language)+":</font>"+relay.getDeviceDesc()+"->"+relay.getDescription()+"->"+value+"<br>");
						buffer.append("<tr><td class='td'>&nbsp</td><td class='td'>");
						buffer.append(relay.getDescription()+"("+relay.getDeviceDesc()+")"+"->"+value);
						buffer.append("</td></tr>");
					}
				}
			}
    	}
    	return buffer.toString();
    }
    private String getVariableLink(Map<String, ActionBean> actions,DeviceListBean devices,int idsite,UserSession us,boolean isScheduled)
    throws Exception
    {
    	String language = us.getLanguage();
    	StringBuffer buffer = new StringBuffer();
//    	LangService lan = LangMgr.getInstance().getLangService(language);
    	ActionBean action = actions.get("V");
    	if(action != null)
    	{
    		String temp = action.getParameters();
    		if(temp != null && !temp.equals(""))
			{	
    			//clock syncronize
    			if(action.getIdAction() == 1)
    			{
    				buffer.append("<tr><td class='td' colspan=2>");
					buffer.append(action.getDescription());
					buffer.append("</td></tr>");
					return buffer.toString();
    			}
				String[] temp_s = temp.split(";");
				boolean first = true;
				for(int j=0;j<temp_s.length;j++)
				{
					String[] temp_s2 = temp_s[j].split("=");
					int idvar = Integer.parseInt(temp_s2[0]);
					String value = temp_s2[1];
					VarphyBean varBean = VarphyBeanList.retrieveVarById(idsite, idvar, language);
					DeviceBean device = devices.getDevice(varBean.getDevice());
					if(first == true)
					{
						buffer.append("<tr><td class='td' width='10%'>");
						
						if (us.isMenuActive("setaction"))
						{
							buffer.append("<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('"+
									//"nop&folder=setaction"+(isScheduled?"2":"")+"&bo=BSetAction&type=click&resource=SubTab"+getPageNumber("V")+".jsp&curTab=tab"+getPageNumber("V")+"name&actioncode="+action.getActioncode()+"&sched="+(isScheduled?"true":"false")+"&desc=ncode08');>"+
									"nop&folder=setaction&bo=BSetAction&type=click&resource=SubTab"+getPageNumber("V")+".jsp&curTab=tab"+getPageNumber("V")+"name&actioncode="+action.getActioncode()+"&sched="+(isScheduled?"true":"false")+"&desc=ncode08');>"+
									getActionDescrition("V",language)+"</a>");
						}
						else
							buffer.append(getActionDescrition("V",language));
						
						buffer.append("</td><td class='td'>");
						if(value.startsWith("id") == false)
						{
							buffer.append(varBean.getShortDescription()+"("+device.getDescription()+")"+"->"+value);
						}
						else
						{
							int idFromVar = Integer.parseInt(value.substring(2));
							VarphyBean fromVarBean = VarphyBeanList.retrieveVarById(idsite, idFromVar, language);
							DeviceBean fromDevice = devices.getDevice(fromVarBean.getDevice());
							buffer.append(varBean.getShortDescription()+"("+device.getDescription()+")"+"->"+
										  fromVarBean.getShortDescription()+"("+fromDevice.getDescription()+")");
						}
						buffer.append("</td></tr>");
						first = false;
					}
					else
					{
//						buffer.append("<font color='#476AB0'>"+
//								getActionDescrition("V",language)+":</font>"+varBean.getDeviceInfo().getDescription()+"->"+varBean.getShortDescription()+"->"+value+"<br>");
						buffer.append("<tr><td class='td'>&nbsp</td><td class='td'>");
						if(value.startsWith("id") == false)
						{
							buffer.append(varBean.getShortDescription()+"("+device.getDescription()+")"+"->"+value);
						}
						else
						{
							int idFromVar = Integer.parseInt(value.substring(2));
							VarphyBean fromVarBean = VarphyBeanList.retrieveVarById(idsite, idFromVar, language);
							DeviceBean fromDevice = devices.getDevice(fromVarBean.getDevice());
							buffer.append(varBean.getShortDescription()+"("+device.getDescription()+")"+"->"+
										  fromVarBean.getShortDescription()+"("+fromDevice.getDescription()+")");
						}
						buffer.append("</td></tr>");
					}
				}
			}
    	}
    	return buffer.toString();
    }
    private String getScheduledReboot(Map<String, ActionBean> actions,String language)
    {
//    	LangService lan = LangMgr.getInstance().getLangService(language);
    	ActionBean action = actions.get("T");
    	StringBuffer buffer = new StringBuffer();
    	if(action != null)
    	{
    		buffer.append("<tr><td class='td' colspan=2>");
			buffer.append(action.getDescription());
			buffer.append("</td></tr>");
    	}
    	return buffer.toString();
    }
    private String getPrintWindowLink(Map<String, ActionBean> actions,int idsite,String[] types,UserSession us,boolean isScheduled)
    {
    	String language  = us.getLanguage();
    	StringBuffer buffer = new StringBuffer();
    	for(int i=0;i<types.length;i++)
    	{
    		String type = types[i];
    		ActionBean action = actions.get(type);
    		if(action != null)
			{
    			if(type.equalsIgnoreCase("W"))
    			{
    				CioEVT ioEvt = new CioEVT(idsite);
    				ioEvt.loadTestStatus();
    				if(ioEvt.getIoteststatus() == null || ioEvt.getIoteststatus().equalsIgnoreCase("") 
    						|| !ioEvt.getIoteststatus().equalsIgnoreCase("OK"))
    				{
    					status = false;
    				}
    			}
    			buffer.append("<tr><td class='td' width='10%'>");
    			if (us.isMenuActive("setaction"))
    			{
    			buffer.append("<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('"+
    					"nop&folder=setaction"+(isScheduled?"2":"")+"&bo=BSetAction&type=click&resource=SubTab"+getPageNumber(type)+".jsp&curTab=tab"+getPageNumber(type)+"name&actioncode="+action.getActioncode()+"&sched="+(isScheduled?"true":"false")+"&desc=ncode08');>"+
    					getActionDescrition(type,language,isScheduled)+"</a>");
    			}
    			else
    			{
    					buffer.append(getActionDescrition(type,language,isScheduled));
    			}
    			buffer.append("</td><td class='td'>");
    			//buffer.append("<img src='images/ok.gif'/>");
				buffer.append("</td></tr>");
			}
    	}
    	return buffer.toString();
    }
    private String getTableHead()
    {
    	return "<table width=100%>";
    }
    private String getTableTail()
    {
    	return "</table>"; 
    }
    private String getConditionPriority(int priority,String language)
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	switch(priority)
    	{
	    	case 0:
	    		return lan.getString("alrview","alarmstate1");
	    	case 1:
	    		return lan.getString("alrview","alarmstate2");
	    	case 2:
	    		return lan.getString("alrview","alarmstate3");
	    	case 3:
	    		return lan.getString("alrview","alarmstate4");
    		default:
    			return "";
    	}
    }
    public String getScheculerDashboardHTMLtable(int idsite, String plantId, UserSession us,
            String title, int height, int width, boolean isScheduled,
            boolean protect) throws Exception
        {
    	String language = us.getLanguage();
        TimeBandList timebandslist = new TimeBandList(null, plantId,
                new Integer(idsite));
        ActionBeanList actionlist = new ActionBeanList(idsite, language,
                isScheduled);
        ConditionBeanList condlist = new ConditionBeanList(idsite, language);
        RelayBeanList relays = new RelayBeanList(idsite,language,true);
        
        LangService lan = LangMgr.getInstance().getLangService(language);
//        String[] ClickRowFunction = new String[size()];
        String[] RowClass = new String[size()];

        HTMLElement[][] dati = new HTMLElement[size()][];
        RuleBean tmp_rule = null;
        String s_condition = "";
        String s_action = "";
        String s_timeband = "";
        String s_rule = "";
        String s_status = "";
        Map<Integer, StringBuffer> codemap = actionlist.getActionCodeMap();
//        int j = 0;
        int cols ;
        if(isScheduled == true)
    	{
    		cols = 4;
    	
    	}
        else
    	{
        	cols= 5;
    	}

        TreeMap<String, RuleBean> sortedrule = new TreeMap<String, RuleBean>();
        for (int i = 0; i < size(); i++)
        {	RuleBean ruletmp=get(i);
        	sortedrule.put(ruletmp.getRuleCode()+"_"+i,ruletmp);
        }
        DeviceListBean devices = new DeviceListBean(idsite,language);
        for (int i = 0; i < size(); i++)
        {
            //tmp_rule = get(i);
        	String k =sortedrule.firstKey();
        	tmp_rule = sortedrule.get(k);
            status = true;
            
            //rule
            
            if(isScheduled == false)
            {
            	if (us.isMenuActive("alrsched"))
            	{
            		s_rule = "<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('nop&folder=alrsched&bo=BAlrSched&type=menu&resource=SubTab1.jsp&curTab=tab1name&cmd=modify_rule&idrule="+tmp_rule.getIdRule()+"&sched=false&desc=ncode13');>"+tmp_rule.getRuleCode()+"</a>";
            	}
            	else
            		s_rule = tmp_rule.getRuleCode();	
            }
            else
            {
            	if (us.isMenuActive("actsched"))
            	{
            		s_rule = "<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('nop&folder=actsched&bo=BActSched&type=menu&resource=SubTab1.jsp&curTab=tab1name&cmd=modify_rule&idrule="+tmp_rule.getIdRule()+"&sched=true&desc=ncode13');>"+tmp_rule.getRuleCode()+"</a>";
            	}
            	else
            		s_rule = tmp_rule.getRuleCode();
            }
            
            //condition
            if (tmp_rule.getIdCondition().intValue() != 0)
            {
                try
                {
					ConditionBean conditionBean = condlist.loadCondition(String.valueOf(
				            tmp_rule.getIdCondition()));
					s_condition = conditionBean.getCodeCondition();
					boolean link_active = us.isMenuActive("alrsched");
					if(conditionBean.getIsgeneral() == false)
					{
						if(conditionBean.getTypeCondition().equalsIgnoreCase("P"))
						{
							String sql="select idvariable from cfvarcondition where idcondition=?";
							RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(conditionBean.getIdCondition())});
							
							int prioritypos = -1;
							
							if (rs!=null && rs.size()>0)
							{
								prioritypos = ((Integer)rs.get(0).get("idvariable")).intValue() - 1;
								
								if ((prioritypos>=0)&&(prioritypos<=3))
									{
										if (link_active)
											s_condition = "<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('nop&folder=alrsched&resource=SubTab2.jsp&curTab=tab2name&bo=BAlrSched&type=menu&fromdashboard=true&hcondactcmd=GET&hcondid="+tmp_rule.getIdCondition()+"&desc=ncode13');>"+lan.getString("devdetail", "priority")+":"+getConditionPriority(prioritypos,language)+"</a>";
										else
											s_condition = getConditionPriority(prioritypos,language);
									}
							}
						}
						else
						{
							if (link_active)
								s_condition = "<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('nop&folder=alrsched&resource=SubTab2.jsp&curTab=tab2name&bo=BAlrSched&type=menu&fromdashboard=true&hcondactcmd=GET&hcondid="+tmp_rule.getIdCondition()+"&desc=ncode13');>"+s_condition+"</a>";
						}
					}
					else
					{
						if (link_active)
							s_condition = "<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('nop&folder=alrsched&resource=SubTab5.jsp&curTab=tab5name&bo=BAlrSched&type=menu&fromdashboard=true&hcondgecmd=get&hcondgeid="+tmp_rule.getIdCondition()+"&desc=ncode13');>"+s_condition+"</a>";
					}
				}
                catch (Exception e)
                {
					s_condition = "";
					
					Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
				}
            }
            
            //timeband
            if (tmp_rule.getIdTimeband().intValue() != 0)
            {
                try
                {
					s_timeband = timebandslist.get(tmp_rule.getIdTimeband()).getTimecode();
					TimeBandBean timeBandBean = timebandslist.get(tmp_rule.getIdTimeband());
					String arg = timeBandBean.getTimecode()+";;"
						+timeBandBean.getTimetype()+";;"+timeBandBean.getTimeband()+";;"+timeBandBean.getIdtimeband();
					this.timebandArg +=arg +";;;;";
					arg = arg.replaceAll("[+]", "1654sdfgklasdkaiekj9393938ij464646");
					arg = arg.replaceAll(" ", "1asjksdf933893hyfhlsa9339089sidfjh");
					boolean link_active = us.isMenuActive("alrsched");
					if(isScheduled == false)
					{
						if (link_active)
							s_timeband = "<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('nop&folder=alrsched&bo=BAlrSched&type=menu&resource=SubTab3.jsp&curTab=tab3name&sched=false&desc=ncode13&arg="+arg+"');>"+s_timeband+"</a>"+
								"<div id='divTimeband"+i+"' name='divTimeband"+i+"'/>";
						
					}
					else
					{
						if (link_active)
							s_timeband = "<a href='javascript:void(0)' onclick=top.frames['manager'].loadTrx('nop&folder=actsched&bo=BActSched&type=menu&resource=../alrsched/SubTab3.jsp&curTab=tab2name&sched=true&desc=ncode13&arg="+arg+"');>"+s_timeband+"</a>"+
								"<div id='divTimeband"+i+"' name='divTimeband"+i+"'/>";
					}	
				}
                catch (Exception e)
                {
					s_timeband = "";
					
					Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
				}
            }
            
            //action
        	try
        	{
				//s_action = ActionBeanList.getDescription(idsite, tmp_rule.getActionCode().intValue());
				Map<String, ActionBean> actions = actionlist.getActionBeansByActionCode(idsite,tmp_rule.getActionCode().intValue(),language);
				
				s_action = getTableHead();

				if(isScheduled)
				{
					String[] types = new String[] { "F", "S", "E","D"};
					s_action += getAddressBookLink(actions,types,us);
					s_action += getRelayLink(actions,relays,idsite,us,isScheduled);
					s_action += getVariableLink(actions,devices,idsite,us,isScheduled);
					s_action += getScheduledReboot(actions,language);
					types = new String[] {"P"};
					s_action += getPrintWindowLink(actions,idsite,types,us,isScheduled);
				}
				else
				{
					String[] types = new String[] { "F", "S", "E","D"};
					s_action += getAddressBookLink(actions,types,us);
					s_action += getRelayLink(actions,relays,idsite,us,isScheduled);
					s_action += getVariableLink(actions,devices,idsite,us,isScheduled);
					types = new String[] {"P", "W"};
					s_action += getPrintWindowLink(actions,idsite,types,us,isScheduled);
				}
				
				s_action += getTableTail();
			}
        	catch (Exception e)
        	{
				s_action = "";
				e.printStackTrace();
				Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
			}
        	
        	//status
        	if(status == true)
        	{
        		s_status = "<img src='images/led/L1.gif'>";
        	}
        	else
        	{
        		s_status = "<img src='images/led/L2.gif'>";
        	}
            String tmpActionCode = "";
			
            if (codemap.get(tmp_rule.getActionCode()) != null)
			{
				tmpActionCode = codemap.get(tmp_rule.getActionCode()).toString();
			}
            
            if ((tmpActionCode != null) && (tmpActionCode.contains("X")))
            {
                RowClass[i] = "statoAllarme1_b";
            }
            else
            {
                RowClass[i] = i%2==0?"Row1":"Row2";
            }

            dati[i] = new HTMLElement[cols];
           

            dati[i][0] = new HTMLSimpleElement(s_status);      
            if(isScheduled == false)
            {
	            dati[i][1] = new HTMLSimpleElement(s_rule);
	            dati[i][2] = new HTMLSimpleElement(s_condition);
	            dati[i][3] = new HTMLSimpleElement(s_timeband);
	            dati[i][4] = new HTMLSimpleElement(s_action);
            }
            else
            {
            	dati[i][1] = new HTMLSimpleElement(s_rule);
	            dati[i][2] = new HTMLSimpleElement(s_timeband);
	            dati[i][3] = new HTMLSimpleElement(s_action);           	
            }

            sortedrule.remove(k);
        }

        //header tabella
        String[] headerTable = new String[cols];
        if(isScheduled == false)
        {
	        headerTable[0] = lan.getString("alrsched", "status");
	        headerTable[1] = lan.getString("alrsched", "rule");
	        headerTable[2] = lan.getString("alrsched", "condition");
	        headerTable[3] = lan.getString("alrsched", "timebands");
	        headerTable[4] = lan.getString("alrsched", "action");
        }
        else
        {
	        headerTable[0] = lan.getString("alrsched", "status");
	        headerTable[1] = lan.getString("alrsched", "rule");
	        headerTable[2] = lan.getString("alrsched", "timebands");
	        headerTable[3] = lan.getString("alrsched", "action");
        }
        
        HTMLTable table = new HTMLTable("rules", headerTable, dati);

        if(isScheduled == false)
        {
        	table.setAlignType(new int[] { 1, 1, 1, 1, 0});
        	table.setColumnSize(0, 10);
            table.setColumnSize(1, 100);
            table.setColumnSize(2, 100);
            table.setColumnSize(3, 100);
            table.setColumnSize(4, 100);
        }
        else
        {
        	table.setAlignType(new int[] { 1, 1, 1, 0});
        	table.setColumnSize(0, 10);
            table.setColumnSize(1, 100);
            table.setColumnSize(2, 100);
            table.setColumnSize(3, 100);
        }
        
        table.setTableTitle(title);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        
        //add by kevin, row height=2008 means no height property
        table.setRowHeight(2008);
        table.setWidth(width);
        table.setHeight(height);
        table.setRowsClasses(RowClass);

        String htmlTable = table.getHTMLText();

        return htmlTable;
        }
    public String getHTMLtable(int idsite, String plantId, String language,
        String title, int height, int width, boolean isScheduled,
        boolean protect) throws Exception
    {
        TimeBandList timebandslist = new TimeBandList(null, plantId,
                new Integer(idsite));
        ActionBeanList actionlist = new ActionBeanList(idsite, language,
                isScheduled);
        ConditionBeanList condlist = new ConditionBeanList(idsite, language);

        LangService lan = LangMgr.getInstance().getLangService(language);
        String[] ClickRowFunction = new String[size()];
        String[] RowClass = new String[size()];

        HTMLElement[][] dati = new HTMLElement[size()][];
        RuleBean tmp_rule = null;
        String s_condition = "";
        String s_action = "";
        String s_timeband = "";
        Map<Integer, StringBuffer> codemap = actionlist.getActionCodeMap();
//        int j = 0;
        int cols = 6;

        TreeMap<String, RuleBean> sortedrule = new TreeMap<String, RuleBean>();
        for (int i = 0; i < size(); i++)
        {	RuleBean ruletmp=get(i);
        	sortedrule.put(ruletmp.getRuleCode()+"_"+i,ruletmp);
        }
        
        
        for (int i = 0; i < size(); i++)
        {
            //tmp_rule = get(i);
        	String k =sortedrule.firstKey();
        	tmp_rule = sortedrule.get(k);
            
        	try
        	{
				s_action = ActionBeanList.getDescription(idsite, tmp_rule.getActionCode().intValue());
			}
        	catch (Exception e)
        	{
				s_action = "";
				
				Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
			}

            if (tmp_rule.getIdTimeband().intValue() != 0)
            {
                try
                {
					s_timeband = timebandslist.get(tmp_rule.getIdTimeband()).getTimecode();
				}
                catch (Exception e)
                {
					s_timeband = "";
					
					Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
				}
            }

            if (tmp_rule.getIdCondition().intValue() != 0)
            {
                try
                {
					s_condition = condlist.loadCondition(String.valueOf(
					            tmp_rule.getIdCondition())).getCodeCondition();
				}
                catch (Exception e)
                {
					s_condition = "";
					
					Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
				}
            }

            String tmpActionCode = "";
			
            if (codemap.get(tmp_rule.getActionCode()) != null)
			{
				tmpActionCode = codemap.get(tmp_rule.getActionCode()).toString();
			}
            
            if ((tmpActionCode != null) && (tmpActionCode.contains("X")))
            {
                RowClass[i] = "statoAllarme1_b";
            }
            else
            {
                RowClass[i] = i%2==0?"Row1":"Row2";
            }

            dati[i] = new HTMLElement[cols];
           
            if (tmp_rule.getIsenabled().equals("FALSE"))
            {
                dati[i][0] = new HTMLSimpleElement("");
            }
            else
            {
            	//gestione eventuale errore sulla regola:
            	if (("".equals(s_action)) || ("".equals(s_condition)) || ("".equals(s_timeband)))
            	{
            		dati[i][0] = new HTMLSimpleElement("");
            		tmp_rule.setIsenabled("FALSE");
            		
            		try
            		{
						String sql = "update cfrule set isenabled='FALSE' where idrule = ?";
						DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{tmp_rule.getIdRule()});
					}
            		catch (Exception e)
            		{
            			Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
					}
            	}
            	else
	            {
	                dati[i][0] = new HTMLSimpleElement("<img src='images/ok.gif' >");
	            }
            }

            dati[i][1] = new HTMLSimpleElement(tmp_rule.getRuleCode());
            dati[i][2] = new HTMLSimpleElement(s_condition);
            dati[i][3] = new HTMLSimpleElement(s_timeband);
            dati[i][4] = new HTMLSimpleElement(s_action);
            dati[i][5] = new HTMLSimpleElement(String.valueOf(
                        tmp_rule.getDelay() / 60));

            ClickRowFunction[i] = String.valueOf(tmp_rule.getIdRule());
            sortedrule.remove(k);
        }

        //header tabella
        String[] headerTable = new String[cols];
        headerTable[0] = lan.getString("alrsched", "enable");
        headerTable[1] = lan.getString("alrsched", "description");
        headerTable[2] = lan.getString("alrsched", "condition");
        headerTable[3] = lan.getString("alrsched", "timebands");
        headerTable[4] = lan.getString("alrsched", "action");
        headerTable[5] = lan.getString("alrsched", "delay");
        
        HTMLTable table = new HTMLTable("rules", headerTable, dati);
        if (!protect)
        {
        	table.setSgClickRowAction("selectedLineRule('$1')");
        	table.setDbClickRowAction("modifyRule('$1')");
        }
        
        table.setSnglClickRowFunction(ClickRowFunction);
        
        table.setDlbClickRowFunction(ClickRowFunction);
        table.setAlignType(new int[] { 1, 1, 1, 1, 1, 1 });
        table.setTableTitle(title);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setColumnSize(0, 78);
        table.setColumnSize(1, 152);
        table.setColumnSize(2, 150);
        table.setColumnSize(3, 150);
        table.setColumnSize(4, 149);
        table.setColumnSize(5, 72);

        table.setRowHeight(18);
        table.setWidth(width);
        table.setHeight(height);
        table.setRowsClasses(RowClass);

        String htmlTable = table.getHTMLText();

        return htmlTable;
    }

    public static String getRuleDescription(int idsite, int idrule)
        throws DataBaseException
    {
        String sql = "select rulecode from cfrule where idsite=? and idrule=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idrule) });

        if (rs.size() > 0)
        {
            return rs.get(0).get("rulecode").toString();
        }
        else
        {
            return "";
        }
    }
    
    public static boolean isDefaultRule(int idsite, int idrule)
    {
    	boolean isDef = false;
    	String defTemplate = BSetAction.UNSCHEDULERTEMPLATE; //Print on all Alarms
    	
    	isDef = (idrule < 3);
    	
    	if (!isDef)
    	{
	    	String sql = "select idrule from cfrule where actioncode in (select actioncode from cfaction where template in"+
	    	"(select idreport from cfreportkernel where templatefile='"+defTemplate+"') and actiontype='P')";
	    	
	    	try
	    	{
	    		RecordSet rs = null;
	    		rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
	    		
				if (rs != null)
					isDef = (rs.size() == 1 && new Integer(idrule).equals(rs.get(0).get(0))); //last print action
			}
	    	catch (Exception e)
	    	{
				//PVPro-generated catch block
				Logger logger = LoggerMgr.getLogger(ActionBeanList.class);
				logger.error(e);
			}
    	}
    	
    	return isDef;
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
    
    public String getTimeBandArg()
    {
    	return this.timebandArg;
    }

}
