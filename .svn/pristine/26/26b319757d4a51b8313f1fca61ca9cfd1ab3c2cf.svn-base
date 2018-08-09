package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.bean.BookletListBean;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;


public class BSetAction extends BoMaster
{
	private static final long serialVersionUID = -5663647760233229686L;
	
	private static final int REFRESH_TIME = -1;
	//pdf alive template
	public static final String SCHEDULERTEMPLATE = "-100";
	//pdf alarm template
	public static final String UNSCHEDULERTEMPLATE = "-101";
    public BSetAction(String l)
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
        p.put("tab1name", "enableAction(1);testAction();");
        p.put("tab2name", "enableAction(1);testAction();");
        p.put("tab3name", "enableAction(1);testAction();");
        p.put("tab4name", "enableAction(1);testAction();");
        p.put("tab5name", "enableAction(1);enableAction(2);testAction();");
        p.put("tab6name", "enableAction(1);testAction();");
        p.put("tab7name", "enableAction(1);testAction();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        //20091126-simon.zhang
        //append the virtual keyboard        
        p.put("tab1name", "dbllistbox.js;setaction.js;");
        p.put("tab2name", "dbllistbox.js;setaction.js;");
        // Alessandro : virtual keyboard added
        p.put("tab3name", "dbllistbox.js;setaction.js;keyboard.js;");
        p.put("tab4name", "dbllistbox.js;setaction.js;");
        p.put("tab5name", "setaction.js;keyboard.js;");
        p.put("tab6name", "dbllistbox.js;setaction.js;");
        p.put("tab7name", "dbllistbox.js;setaction.js;");
//        p.put("tab8name", "setaction.js;");

        return p;
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        String actiontype = us.getProperty("cmd");
        UserTransaction ut = us.getCurrentUserTransaction();
        String action_description = prop.getProperty("action_description");
        int idsite = us.getIdSite();
        String s_param = prop.getProperty("param");
        String t = us.getProperty("sched");
        boolean isScheduled = t.equalsIgnoreCase("TRUE");
        int actioncode = Integer.parseInt(prop.getProperty("actioncode"));
        int state = 1;

        ActionBeanList actionlist = new ActionBeanList();
        String[] tmp = null;

        /*
         * refactoring per set var da var 
         */
        if(actiontype.equals("save_setvar_action")) {
        	s_param = s_param.replaceAll(" ", "");
        	StringBuffer sp = new StringBuffer();
        	if(s_param!=null && s_param.contains("_mdl")){
        		//mdl25543=6;
        		String ss[] = s_param.split(";");
        		String iddevice = null;
        		String idvarmdl = null;
        		String dest  = null;
        		String idvariable = null;
        		for(int i=0;i<ss.length;i++){
        			if(ss[i].contains("_mdl")){
        				
        				dest= ss[i].split("=")[0];
        				iddevice = dest.split("_")[0];
            			idvarmdl = dest.split("_")[1].substring(3);
            			
            			
            			String sql=" select idvariable from cfvariable where iddevice = "+iddevice+" and idvarmdl ="+idvarmdl+" and iscancelled = 'FALSE' AND idhsvariable is not null";
            	    	RecordSet rs=null;
            	    	rs=DatabaseMgr.getInstance().executeQuery(null,sql,null);
            	    	if(rs.size()>0){
            	    		idvariable = ""+(Integer)rs.get(0).get(0);
            	    		sp.append(idvariable+"="+ss[i].split("=")[1]+";");
            	    	}
        			}else{
        				sp.append(ss[i]+";");
        			}
        			
        		}
        		s_param = sp.toString().substring(0,sp.length()-1);
        	}
        	int count = actionlist.countActionByActioncode(idsite, actioncode);
            if (actionlist.existAction(idsite, actioncode, "V")) { 
                if (s_param == null || s_param.equals("")) {
                    if (ActionBeanList.isActionInRule(idsite, actioncode)) {
                    	ut.setProperty("notremoveaction", "NO");
                    } else {
                    	if (count > 1) {
                    		actionlist.deleteAction(idsite, actioncode, "V");
                    	} else {
                    		actionlist.replaceActionWithXaction(idsite, actioncode);
                    	}
                        DirectorMgr.getInstance().mustRestart();
                    }
                } else {
                	actionlist.updateAction(idsite, actioncode, s_param, "V", "");
                    DirectorMgr.getInstance().mustRestart();
                }
            } else {
            	if(s_param != null && !s_param.equals("")) {
	                if (actionlist.existAction(idsite, actioncode, "X")) {
	                    actionlist.replaceXActionWithAction(idsite, actioncode, "V", "", s_param);
	                } else {
	                    actionlist.insertAction(idsite, actioncode, s_param, "V", "", action_description, isScheduled);
	                }
	                DirectorMgr.getInstance().mustRestart();
            	}
            }
            return;
        }
        /* *** end refactoring per set var da var *** */
        
        if (actiontype.equals("save_print_window"))
        {
            String chk_window = prop.getProperty("chk_window"); //CASO WINDOW
            tmp = actionlist.getActiontypeByActionCode(idsite, actioncode);

            if (actionlist.countActionByActioncode(idsite, actioncode) == 1)
            {
                if ((tmp[0]).equals("X"))
                {
                    state = 0;
                }
                else
                {
                    state = 1;
                }
            }

            if (actionlist.existAction(idsite, actioncode, "W")) //se esiste gia STAMPA
            {
                if (chk_window == null) //se param vuoto 
                {
                    if (actionlist.countActionByActioncode(idsite, actioncode) == 1) //se ultima action
                    {
                        if (!ActionBeanList.isActionInRule(idsite, actioncode))
                        {
                            actionlist.replaceActionWithXaction(idsite,
                                actioncode);
                            
                            DirectorMgr.getInstance().mustRestart();
                            
                        }
                        else
                        {
                            ut.setProperty("notremoveaction", "NO");
                        }
                    }
                    else //se non �?l'ultima action la rimuovo semplicemente
                    {
                        actionlist.deleteAction(idsite, actioncode, "W");
                        
                        DirectorMgr.getInstance().mustRestart();
                    }
                }
            }
            else if (chk_window != null) //se non esiste gia riga window
            {
                if (state == 0) //se non avevo nulla configurato,elimino l'action fasullo
                {
                    actionlist.replaceXActionWithAction(idsite, actioncode,
                        "W", "", "");
                    
                    DirectorMgr.getInstance().mustRestart();
                }
                else
                {
                    actionlist.insertAction(idsite, actioncode, "", "W", "",
                        action_description,isScheduled);
                    
                    DirectorMgr.getInstance().mustRestart();
                }
            }        	
        	
            String chk_print = prop.getProperty("chk_print"); //CASO STAMPA
            tmp = actionlist.getActiontypeByActionCode(idsite, actioncode);

            if (actionlist.countActionByActioncode(idsite, actioncode) == 1)
            {
                if ((tmp[0]).equals("X"))
                {
                    state = 0;
                }
                else
                {
                	state = 1;
                }
            }

            if (actionlist.existAction(idsite, actioncode, "P")) //se esiste gia STAMPA
            {
                if (chk_print == null) //se param vuoto 
                {
                    if (actionlist.countActionByActioncode(idsite, actioncode) == 1) //se ultima action
                    {
                        if (!ActionBeanList.isActionInRule(idsite, actioncode))
                        {
                            actionlist.replaceActionWithXaction(idsite,
                                actioncode);
                            
                            DirectorMgr.getInstance().mustRestart();
                            
                        }
                        else
                        {
                            ut.setProperty("notremoveaction", "NO");
                        }
                    }
                    else //se non �?l'ultima action la rimuovo semplicemente
                    {
                        actionlist.deleteAction(idsite, actioncode, "P");
                        
                        DirectorMgr.getInstance().mustRestart();
                    }
                }
            }
            else if (chk_print != null) //se non esiste gia riga stampa
            {
                if (state == 0) //se non avevo nulla configurato,elimino l'action fasullo
                {
                    actionlist.replaceXActionWithAction(idsite, actioncode, "P", "-101", ""); //pdf alarm template
                    DirectorMgr.getInstance().mustRestart();
                }
                else
                {
                    actionlist.insertAction(idsite, actioncode, "", "P", "-101", action_description,isScheduled); //pdf alarm template
                    DirectorMgr.getInstance().mustRestart();
                    
                }
            }
        
            // print report on alarm condition
            // Pr used as the action type (changed to P on PAction class)
            // Pr it is used as an workaround because on alarm condition P it is used to print alarm report
            // and on scheduler P it is used to print reports based on selected template
        	String report		= prop.getProperty("report");
        	String report_store	= prop.getProperty("report_store") != null ? "1" : "0";
        	String report_send	= prop.getProperty("report_send") != null ? "1" : "0";
        	String param;
        	if( s_param.length() > 0 )
        		param = report_store + ";" + report_send + ";" + s_param;
        	else
        		param = report_store + ";0";
        	
        	if (report !=null && report.equals("0"))
        		report = null;
            tmp = actionlist.getActiontypeByActionCode(idsite, actioncode);

            if (actionlist.countActionByActioncode(idsite, actioncode) == 1)
            {
                if ((tmp[0]).equals("X"))
                {
                    state = 0;
                }
                else
                {
                	state = 1; 
                }
            }
            String midnightMode = prop.getProperty("midnight_mode");
            IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
            String available = p_info.get("daily_report_midnight_style");
            if(available != null)
            {
            	if(midnightMode != null)
	            	p_info.set("daily_report_midnight_style", "true");
	            else
	            	p_info.set("daily_report_midnight_style", "false");
            }
            else
            {
	            if(midnightMode != null)
	            	p_info.store("daily_report_midnight_style", "true");
	            else
	            	p_info.store("daily_report_midnight_style", "false");
            }
            
            if (actionlist.existAction(idsite, actioncode, "Pr")) //se esiste gia REPORT
            {
                if (report == null) //se param vuoto 
                {
                    if (actionlist.countActionByActioncode(idsite, actioncode) == 1) //se ultima action
                    {
                        if (!ActionBeanList.isActionInRule(idsite, actioncode))
                        {
                            actionlist.replaceActionWithXaction(idsite, actioncode, report, param);
                            DirectorMgr.getInstance().mustRestart();
                            
                        }
                        else
                        {
                            ut.setProperty("notremoveaction", "NO");
                        }
                    }
                    else //se non �?l'ultima action la rimuovo semplicemente
                    {
                        actionlist.deleteAction(idsite, actioncode, "Pr");
                        DirectorMgr.getInstance().mustRestart();
                    }
                }
                else
                {
                	actionlist.updateAction(idsite, actioncode, param, "Pr", report);
                	DirectorMgr.getInstance().mustRestart();
                }
            }
            else if (param != null) //se non esiste gia riga report
            {
            	if(report !=null){
	                if (state == 0) //se non avevo nulla configurato,elimino l'action fasullo
	                {
                		actionlist.replaceXActionWithAction(idsite, actioncode, "Pr", report, param);
                		DirectorMgr.getInstance().mustRestart();
	                }
	                else
	                {	
                		actionlist.insertAction(idsite, actioncode, param, "Pr", report, action_description,isScheduled);
                		DirectorMgr.getInstance().mustRestart();
	                }
            	}
            }
        }
        else if (actiontype.equals("save_report"))
        {
        	String report		= prop.getProperty("report");
        	String report_store	= prop.getProperty("report_store") != null ? "1" : "0";
        	String report_send	= prop.getProperty("report_send") != null ? "1" : "0";
        	String param;
        	if( s_param.length() > 0 )
        		param = report_store + ";" + report_send + ";" + s_param;
        	else
        		param = report_store + ";0";
        	
        	if (report !=null && report.equals("0"))
        		report = null;
            tmp = actionlist.getActiontypeByActionCode(idsite, actioncode);

            if (actionlist.countActionByActioncode(idsite, actioncode) == 1)
            {
                if ((tmp[0]).equals("X"))
                {
                    state = 0;
                }
            }
            String midnightMode = prop.getProperty("midnight_mode");
            IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
            String available = p_info.get("daily_report_midnight_style");
            if(available != null)
            {
            	if(midnightMode != null)
	            	p_info.set("daily_report_midnight_style", "true");
	            else
	            	p_info.set("daily_report_midnight_style", "false");
            }
            else
            {
	            if(midnightMode != null)
	            	p_info.store("daily_report_midnight_style", "true");
	            else
	            	p_info.store("daily_report_midnight_style", "false");
            }

            if (actionlist.existAction(idsite, actioncode, "P")) //se esiste gia REPORT
            {
                if (report == null) //se param vuoto 
                {
                    if (actionlist.countActionByActioncode(idsite, actioncode) == 1) //se ultima action
                    {
                        if (!ActionBeanList.isActionInRule(idsite, actioncode))
                        {
                            actionlist.replaceActionWithXaction(idsite, actioncode, report, param);
                            DirectorMgr.getInstance().mustRestart();
                            
                        }
                        else
                        {
                            ut.setProperty("notremoveaction", "NO");
                        }
                    }
                    else //se non �?l'ultima action la rimuovo semplicemente
                    {
                        actionlist.deleteAction(idsite, actioncode, "P");
                        DirectorMgr.getInstance().mustRestart();
                    }
                }
                else
                {
                	actionlist.updateAction(idsite, actioncode, param, "P", report);
                	DirectorMgr.getInstance().mustRestart();
                }
            }
            else if (param != null) //se non esiste gia riga report
            {
            	if(report !=null){
            		if (state == 0) //se non avevo nulla configurato,elimino l'action fasullo
                    {
                        actionlist.replaceXActionWithAction(idsite, actioncode, "P", report, param);
                        DirectorMgr.getInstance().mustRestart();
                    }
                    else
                    {
                        actionlist.insertAction(idsite, actioncode, param, "P", report, action_description,isScheduled);
                        DirectorMgr.getInstance().mustRestart();
                    }
            	}
                
            }

        }
        else if (!actiontype.equals("reload"))
        {
            tmp = actionlist.getActiontypeByActionCode(idsite, actioncode);

            if (actionlist.countActionByActioncode(idsite, actioncode) == 1)
            {
                if ((tmp[0]).equals("X"))
                {
                    state = 0;
                }
                else
                {
                    state = 1;
                }
            }

            if (actionlist.existAction(idsite, actioncode, actiontype)) //se esiste gia fax
            {
                if (s_param.equals("")) //se param vuoto 
                {
                    if (actionlist.countActionByActioncode(idsite, actioncode) == 1) //se ultima action
                    {
                        if (!ActionBeanList.isActionInRule(idsite, actioncode))
                        {
                            actionlist.replaceActionWithXaction(idsite,
                                actioncode);
                            DirectorMgr.getInstance().mustRestart();
                        }
                        else
                        {
                            ut.setProperty("notremoveaction", "NO");
                        }
                    }
                    else //se non �?l'ultima action la rimuovo semplicemente
                    {
                        actionlist.deleteAction(idsite, actioncode, actiontype);
                        DirectorMgr.getInstance().mustRestart();
                    }
                }
                else
                {
                	String template = "";
                	if (actiontype.equalsIgnoreCase("E"))
                    {
                		if(isScheduled)
                    		template = SCHEDULERTEMPLATE; //pdf alive template
                    	else
                    		template = UNSCHEDULERTEMPLATE; //pdf alarm template
                    }
                    else if (actiontype.equalsIgnoreCase("F"))
                    {
                        template = "PVSendFax.rtf";
                    }
                	actionlist.updateAction(idsite, actioncode, s_param,
                        actiontype,template); //se c'�?parametro update
                	
                	DirectorMgr.getInstance().mustRestart();
                }
            }
            else //se non esiste gia riga fax
            {
                // TODO: Togliere cablatura template
            	if (!s_param.equals(""))
            	{
	            	String template = "";
	
	                if (actiontype != null)
	                {
	                    if (actiontype.equalsIgnoreCase("E"))
	                    {
	                    	if(isScheduled)
	                    		template = "-100"; //pdf alive template
	                    	else
	                    		template = "-101"; //pdf alarm template
	                    }
	                    else if (actiontype.equalsIgnoreCase("F"))
	                    {
	                        template = "PVSendFax.rtf";
	                    }
	                }
	
	                if (state == 0) //se non avevo nulla configurato,elimino l'action fasullo
	                {
	                    actionlist.replaceXActionWithAction(idsite, actioncode,
	                        actiontype, template, s_param);
	                    
	                    DirectorMgr.getInstance().mustRestart();
	                }
	                else
	                {
	                    actionlist.insertAction(idsite, actioncode, s_param,
	                        actiontype, template, action_description,isScheduled);
	                    
	                    DirectorMgr.getInstance().mustRestart();
	                }
            	}
            }
        } //chiuso caso save actions varie
    }
    private StringBuffer getDevMdl(BookletListBean bdvBean, UserSession us, int idsite, String language, int model) {
		StringBuffer ris = new StringBuffer();
		String ss="";
		try {
			DevMdlBeanList devmdllist = new DevMdlBeanList();
			DevMdlBean[] devmdl = devmdllist.retrieveOnLine(idsite, language);

			for (int i = 0; i < devmdl.length; i++) {
				DevMdlBean tmp = devmdl[i];
				if (tmp.getIddevmdl() == model)
					ss = "selected";
				else
					ss = "";
				ris.append("<devMdl>\n");
				ris.append("<value><![CDATA[" + tmp.getIddevmdl() + "]]></value>\n");
				ris.append("<text><![CDATA[" + tmp.getDescription() + "]]></text>\n");
				ris.append("<selected><![CDATA[" + ss + "]]></selected>\n");
				ris.append("<idx><![CDATA[" + i + "]]></idx>\n");
				ris.append("</devMdl>\n");
			}
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

    private StringBuffer getDevList(BookletListBean bdvBean, UserSession us, int dev, int model, int idsite,
			String language) {
		StringBuffer ris = new StringBuffer();
		try {
			ris.append("<![CDATA[");
			
			//new constructor called. 3rd parameter indicates hiding of Internal IO device
			// Nicola Compagno 24032010
			DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage(), true);
			DeviceBean tmp_dev = null;
			int[] ids = devs.getIds();
			ris.append("<select id='devLst' size='10' onclick='dblClickDevList(this);' ondblclick='dblClickDevList(this);' class='selectB' " +
					(model!=-1?" multiple ":"")+">");
			int device=0;
			for (int i=0;i<devs.size();i++){
				tmp_dev = devs.getDevice(ids[i]);
				if ((model == -1) || (model == tmp_dev.getIddevmdl())) {
					ris.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+
							" value='"+tmp_dev.getIddevice()+
							"' id='dev"+tmp_dev.getIddevice()+"' class='"+(i%2==0?"Row1":"Row2")+"'>"+
							tmp_dev.getDescription()+"</OPTION>\n");
				}
			}
			ris.append("</select>");
			ris.append("]]>");	

		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

    private StringBuffer getParams(BookletListBean bdvBean, UserSession us,
			/*int idx, int count, */int dev, int model, int idsite, String language) {
		StringBuffer ris = new StringBuffer();
		try {
			VarphyBeanList varlist = new VarphyBeanList();
			VarphyBean[] vars = null;
			VarMdlBeanList varMdlBeanList = new VarMdlBeanList();
			VarMdlBean[] varMdlBeans = null;
			String la= us.getLanguage();
			//modifica per tirar su solo i parametri
			if(dev!=-1) {
				vars = varlist.getParameterOfDevice(la,1,dev);
			} else if(model!=-1) {
				varMdlBeans = varMdlBeanList.retrieveOrderedIfDevIsPresent(language,1,model,"FALSE",0, true );
			}
			ris.append("<![CDATA[");
			ris.append("<select id='variable_combo_dest' size='10' multiple onchange=''  class='selectB'>");
			if(vars!=null && vars.length>0){
				for (int i=0;i<vars.length;i++) {
					VarphyBean aux = vars[i];
					ris.append("<option value=\"");
					ris.append(String.valueOf(aux.getId()));
					ris.append("\" id='var");
					ris.append(String.valueOf(aux.getId()));
					ris.append("'");
					ris.append(" vartype = '"+aux.getType()+"'");
					ris.append(" class='"+(i%2==0?"Row1":"Row2")+"' >");
					ris.append(aux.getShortDescription());
					ris.append("</option>");
				}
			} else if(varMdlBeans!=null && varMdlBeans.length>0) {
				for (int i=0;i<varMdlBeans.length;i++) {
					VarMdlBean aux = varMdlBeans[i];
					ris.append("<option value=\"");
					ris.append("mdl"+String.valueOf(aux.getIdvarmdl ()));
					ris.append("\" id='var");
					ris.append(String.valueOf(aux.getIdvarmdl()));
					ris.append("'");
					ris.append(" vartype = '"+aux.getType()+"'");
					ris.append(" class='"+(i%2==0?"Row1":"Row2")+"' >");
					ris.append(aux.getDescription());
					ris.append("</option>");
				}
			}
			ris.append("</select>");
			ris.append("]]>");	
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}
    
   
    
	@Override
	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception {
		String section = prop.getProperty("section");
		String method = prop.getProperty("method");
		if(section != null && method!=null){
			String language = us.getLanguage();
			int idsite = us.getIdSite();
			
			String temp = prop.getProperty("model");
			int model = -1;
			if (temp != null && Integer.parseInt(temp) > 0) {
				model = Integer.parseInt(temp);
			}
			int dev = -1;
			temp = prop.getProperty("dev");
			if (temp != null && Integer.parseInt(temp) > 0) {
				dev = Integer.parseInt(temp);
			}
			
			StringBuffer ris = new StringBuffer();
			ris.append("<response>");
			ris.append("<method>" + method + "</method>");
			ris.append("<variableInAction>");
			BookletListBean bdvBean = new BookletListBean(idsite, us.getUserName());
			if("devMdl".equals(section)  ) {
				ris.append(getDevMdl(bdvBean, us, idsite, language, model));
			}
			if ("deviceslist".equals(section)) {
				ris.append(getDevList(bdvBean, us, dev, model, idsite, language));
			}
			if ("parameterslist".equals(section)) {
				ris.append(getParams(bdvBean, us, dev, model, idsite, language));
			}
			
			ris.append("</variableInAction>");
			ris.append("</response>");
			return ris.toString();
		}
		else{
			StringBuffer toret = new StringBuffer();
	        String rw = null;
	        if(prop.getProperty("rw").equalsIgnoreCase("rw")){
	        	rw = "1";
	        }
			String sql =
	            "select cfvariable.idvariable,cfvariable.type,cftableext.description " +
	            "from cfvariable, cftableext " +
	            "where cfvariable.iddevice="+prop.getProperty("iddevice")+" and " +
	            "cfvariable.iscancelled='FALSE' and " +
	            "cfvariable.idhsvariable is not null and " +
	            "type!=4 and " +
	            (rw!=null?"cfvariable.readwrite!='1' and ":"") +
	            "cftableext.idsite=1 and " +
	            "cftableext.tablename='cfvariable' and " +
	            "cftableext.tableid=cfvariable.idvariable and " +
	            "languagecode='"+us.getLanguage()+"' " +
	            "order by description";
			
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			toret.append("<result>");
			toret.append("<target><![CDATA["+prop.getProperty("target")+"]]></target>");
			toret.append("<vars>");
			for (int i = 0; i < rs.size(); i++) {
				toret.append("<v vid='"+rs.get(i).get("idvariable")+"' tp='"+rs.get(i).get("type")+"'>" +
						"<![CDATA["+rs.get(i).get("description")+"]]>" +
						"</v>");
				
			}
			toret.append("</vars>");
			toret.append("</result>");
			return toret.toString();
		}
		
	}
}
