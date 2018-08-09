package com.carel.supervisor.presentation.bo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.plugin.parameters.ParametersMgr;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEvent;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersNotificationList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;


public class BParameters extends BoMaster
{
	private static final long serialVersionUID = 1L;
	private static final int REFRESH_TIME = 30000;
	
	private static final int RELOAD_PHOTO_NUM = 400;
	private static final int RELOAD_PHOTO_MILLISEC = 20000;

    public BParameters(String l)
    {
     //   super(l, REFRESH_TIME);
    	super(l);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        if (ParametersMgr.isPluginRegistred())
        {
	        p.put("tab1name", "enableAction(1);window.setInterval('reloadPhoto()',"+RELOAD_PHOTO_MILLISEC+");");
	        p.put("tab3name", "enableAction(1);");   
	        p.put("tab4name", "enableAction(1);");   
	        p.put("tab5name", "onParamLoaded();reload_actions(LOAD_REPORT_TO_MODIFY);setMaxVar("+ParametersMgr.getParametersCFG().getPlimit()+");enableAction(1);enableAction(2);");
        }
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "parameters.js;");
        p.put("tab2name", "parameters.js;keyboard.js;");
        p.put("tab3name", "dbllistbox.js;parameters.js;");
        p.put("tab4name", "dbllistbox.js;parameters.js;keyboard.js;");
        p.put("tab5name", "parameters_varlist.js;dataselect.js;");
        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    { 
    	//controllo variabile command (residuo del trapianto della pagina delle variabili dei report...)
    	String command=us.getPropertyAndRemove("command");
	    	if(command!=null && command.compareTo("modifica_variabili")==0){
	    		int[] idvars=null;
	    		String variables_string = prop.getProperty("variables");
	    		String[] variables_string_array = variables_string.split(";");
	    		idvars=new int[variables_string_array.length];
	    		for (int i = 0; i < variables_string_array.length; i++) {
	    			try{
					String variables_string_id = variables_string_array[i].split("-")[1];
					idvars[i]=new Integer(variables_string_id).intValue();
	    			}catch (Exception e) {
						//nothing to do
					}
				}
	    		
	    		//una volta ricostruite le variabili, le salvo...
	    		ParametersMgr.getParametersPhoto().setVariableList(idvars);
	    		
	    		//Log Modifiche
				EventMgr.getInstance().info(1, us.getUserName(), "parameters", "PM007", "");

	    		
	    		ParametersMgr.getInstance().restart();
	    	}
	    	if(command!=null && command.compareTo("cancella_tutto")==0){
	    		ParametersMgr.getParametersPhoto().clearVariableList();
	    	}
	    	
    	
    	String cmd = prop.getProperty("CMD");
    	if (cmd==null) cmd="";
    	
    	if (cmd.equalsIgnoreCase("update_general_cfg")){
    		//salvo configurazione genereale
    		
    		ParametersMgr.getInstance().setCfgChanging(true);
    		ParametersMgr.getInstance().stop();
    		
    		boolean enabled=false;
    		String s =  (String) prop.getProperty("enabledstatus");
    		if (s!=null)
    			enabled=true;
    		
    		int checkinterval = new Integer ( prop.getProperty("checkinterval")).intValue();
    		String usertastiera =  (String) prop.getProperty("usertastiera");
    		
    		boolean notification  = ParametersMgr.getParametersCFG().getEnablenotification();

    		ParametersMgr.getParametersCFG().setEnabled(enabled);
    		ParametersMgr.getParametersCFG().setCheckinterval(checkinterval);
    		ParametersMgr.getParametersCFG().setUserTastiera(usertastiera);
    		ParametersMgr.getParametersCFG().setEnablenotification(notification);
    		ParametersMgr.getParametersCFG().save();

			EventMgr.getInstance().info(1, us.getUserName(), "parameters", "PM008", "");

    	}
    	else if (cmd.equalsIgnoreCase("update_param_notify")){
    		//modifica dei destinatari delle notifiche
    		
    		boolean enablednotification=false;
    		
    		String s =  (String) prop.getProperty("enablednotification");
    		if (s!=null)
    			enablednotification=true;
    		
    		String s1 =  (String) prop.getProperty("aggregatenotification");
    		if (s1.equalsIgnoreCase("true")){
    			ParametersMgr.getParametersCFG().setAggregatenotification(true);
    		}
    		else
    		{
    			ParametersMgr.getParametersCFG().setAggregatenotification(false);
    		}
    		
    		int checkinterval = new Integer ( prop.getProperty("checkinterval")).intValue();
    		String usertastiera =  (String) prop.getProperty("usertastiera");
    		ParametersMgr.getParametersCFG().setEnablenotification(enablednotification);
    		ParametersMgr.getParametersCFG().setCheckinterval(checkinterval);
    		ParametersMgr.getParametersCFG().setUserTastiera(usertastiera);
    		ParametersMgr.getParametersCFG().save();
    		
    		//aggiornamento destinatari di notifica
    		String elenco_notifiche_string =  (String) prop.getProperty("elenco_notifiche");
    		if ((elenco_notifiche_string.trim().length()>0)){
    			if (elenco_notifiche_string.charAt(elenco_notifiche_string.length()-1)==';'){
    				elenco_notifiche_string=elenco_notifiche_string.substring(0, elenco_notifiche_string.length()-1);
    			}
    			String[] elenco_notifiche= elenco_notifiche_string.split(";");
    			int[] elenco_codici_notifiche = new int[elenco_notifiche.length];
    			for (int i = 0; i < elenco_notifiche.length; i++) {
    				elenco_codici_notifiche[i] = new Integer(elenco_notifiche[i]).intValue();
				}
    			ParametersNotificationList.setNotifications(elenco_codici_notifiche);
    		}
    		else{
    			//lista notifiche vuota
    			ParametersNotificationList.setNotifications(new int[0]);
    		}
    		
    		
    		//aggiornamento profili da non controllare
    		String elenco_profili_auth_string =  (String) prop.getProperty("elenco_profili_auth");
    		
    		int[] oldProf = ParametersMgr.getParametersCFG().getAuthProfiles();
    		Arrays.sort(oldProf);

    		int[] elenco_codici_profili = new int[0];
    		if (elenco_profili_auth_string!=null &&(elenco_profili_auth_string.trim().length()>0))
    		{
    			if (elenco_profili_auth_string.charAt(elenco_profili_auth_string.length()-1)==';'){
    				elenco_profili_auth_string=elenco_profili_auth_string.substring(0, elenco_profili_auth_string.length()-1);
    			}
    			String[] elenco_profili= elenco_profili_auth_string.split(";");
    			elenco_codici_profili = new int[elenco_profili.length];
    			for (int i = 0; i < elenco_profili.length; i++) {
    				elenco_codici_profili[i] = new Integer(elenco_profili[i]).intValue();
				}
    		}
			Arrays.sort(elenco_codici_profili);
			ParametersMgr.getParametersCFG().setAuthProfiles(elenco_codici_profili);
			for (int i = 0; i < oldProf.length; i++) {
				if (Arrays.binarySearch(elenco_codici_profili, oldProf[i]) <0){
					//trovato profilo cancellato
					ProfileBean pb = ProfileBeanList.retrieveProfile(oldProf[i], 1);
					EventMgr.getInstance().warning(1, us.getUserName(), "parameters", "PM011", pb.getCode());
				}
			}
			
			for (int i = 0; i < elenco_codici_profili.length; i++) {
				if (Arrays.binarySearch(oldProf, elenco_codici_profili[i]) <0){
					//trovato profilo aggiunto
					ProfileBean pb = ProfileBeanList.retrieveProfile(elenco_codici_profili[i], 1);
					EventMgr.getInstance().warning(1, us.getUserName(), "parameters", "PM010", pb.getCode());
				}
			}
			
			//ParametersMgr.getInstance().setCfgChanging(false);
			ParametersMgr.getInstance().start();
    		Thread.sleep(1000);
    		
			EventMgr.getInstance().info(1, us.getUserName(), "parameters", "PM009", "");
    	}
    	else if (cmd.equalsIgnoreCase("rollback_event")) {
    		// rollback di eventi... quindi setto un qualche parametro con il suo starting value
    		String idevent_s= prop.getProperty("idevent");
    		int idevent = new Integer(idevent_s).intValue();
    		ParametersEvent pe = new ParametersEvent(us.getLanguage(), idevent);
    		ParametersMgr.getInstance().setManualRollback(us.getUserName(), pe);
    	}
    	else if (cmd.equalsIgnoreCase("start_stop")) {
    		String action= prop.getProperty("param_action");
    		
    		if (action.equalsIgnoreCase("start")){
    			EventMgr.getInstance().info(1, us.getUserName(), "parameters", "PM006", "");
    			ParametersMgr.getInstance().start();
    			Thread.sleep(1000);
    		}
    		else if (action.equalsIgnoreCase("stop")){
    			EventMgr.getInstance().warning(1, us.getUserName(), "parameters", "PM004", "");
    			ParametersMgr.getInstance().manualStop();
    			Thread.sleep(1000);
			
    		} else if (action.equalsIgnoreCase("restart")){
    			EventMgr.getInstance().info(1, us.getUserName(), "parameters", "PM012", "");
    			ParametersMgr.getInstance().restart();
    			Thread.sleep(1000);
    		}
    	}else if (cmd.equalsIgnoreCase("parameters_photo")){
    		EventMgr.getInstance().info(1, us.getUserName(), "parameters", "PM005", "");
    		ParametersMgr.getParametersPhoto().makePhoto(us);
    		ParametersMgr.getInstance().restart();
    		Thread.sleep(2000);
    	}
    }


	@Override
	public String executeDataAction(UserSession us, String tabName,
			Properties prop) throws Exception {
		//gestione lista variabili (copiata dalla pagina dei report...)
		
		Integer action=Integer.parseInt((String) prop.get("action"));
		Integer idDevice=null;
		try{
			idDevice = Integer.parseInt((String) prop.get("iddevice"));
		}
		catch (Exception e) {
			idDevice=-1;
		}
		
		String lan = us.getLanguage();
		StringBuilder response= new StringBuilder();
		response.append("<response>");
		
		switch(action.intValue()){
			case 0:
				response.append("<![CDATA[");
				VarphyBeanList varlist = new VarphyBeanList();
				VarphyBean[] vars ;
				String la= us.getLanguage();
				//modifica per tirar su solo i parametri
				vars= varlist.getParameterOfDevice(la,1,idDevice);
				response.append("<select ondblclick=\"addDevVar();\"  name=sections multiple size=10  id='var' class='selectB'>");
				for (int i=0;i<vars.length;i++)
					{
						VarphyBean aux = vars[i];
						response.append("<option value=\"");
						response.append(String.valueOf(aux.getId()));
						response.append("\" id='var");
						response.append(String.valueOf(aux.getId()));
						response.append("' class='"+(i%2==0?"Row1":"Row2"));
						response.append("'>");
						response.append(aux.getShortDescription());
						response.append("</option>");
					}
				response.append("</select>");
				response.append("]]>");	
				
			break;
			case 1:
				response.append("<![CDATA[");
				// method invocation changed to hide 'Internal IO' device
				// Nicola Compagno 25032010
				DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage(),true);
				DeviceBean tmp_dev = null;
				int[] ids = devs.getIds();
				response.append("<select onchange=\"\" ondblclick=\"reload_actions(0);\" id=\"dev\" name=sections size=10 class='selectB'>");
				int device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					response.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" class='"+ (i%2==0?"Row1":"Row2") +"'"+
							" value='"+tmp_dev.getIddevice()+"' id='dev"+tmp_dev.getIddevice()+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				response.append("</select>");
				response.append("]]>");	
			break;
			case 2:
				response=new StringBuilder();
				response.append("<response>");
				response.append("<variable>");
				response.append("<![CDATA[");
				VarMdlBeanList varMdlBeanList = new VarMdlBeanList();
				VarMdlBean[] varMdlBeans ;
				varMdlBeans= varMdlBeanList.retrieveOrderedIfDevIsPresent(lan,1,idDevice,"FALSE",0, true );
				response.append("<select ondblclick=\"addDevVar();\"  name=sections multiple size=10  id='var' class='selectB'>");
				for (int i=0;i<varMdlBeans.length;i++)
					{
						response.append("<option value=\"");
						//response.append(String.valueOf(varMdlBeans[i].getIddevmdl()));
						//response.append("-");
						response.append(String.valueOf(varMdlBeans[i].getIdvarmdl()));
						response.append("\" id='var");
						response.append(String.valueOf(varMdlBeans[i].getIdvarmdl()));
						response.append( "' class= '"+(i%2==0?"Row1":"Row2") );
						response.append("'>");
						response.append(varMdlBeans[i].getDescription());
						response.append("</option>");
					}
				response.append("</select>");
				response.append("]]>");	
				response.append("</variable>");
				response.append("<device>");
				response.append("<![CDATA[");
				
				devs = new DeviceListBean(us.getIdSite(),us.getLanguage(),idDevice,1);
				tmp_dev = null;
				ids = devs.getIds();
				StringBuffer div_dev = new StringBuffer();
				div_dev.append("<select multiple id=\"dev\" name=sections  size=10 class='selectB'>");
				device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					div_dev.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"'"
							+" id='dev"+tmp_dev.getIddevice()+"' class='"+(i%2==0?"Row1":"Row2")+"' >"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				div_dev.append("</select>");
				response.append(div_dev.toString());
				response.append("]]>");	
				response.append("</device>");
			break;	
			case 3:
				response.append("<k>");
				response.append("<![CDATA[");
				response.append(VarphyBeanList.getMappingIdDevIdVarMdlIdDevIdVar(lan, prop.getProperty("devices"), prop.getProperty("variables"),prop.getProperty("exclude")));
				response.append("]]>");	
				response.append("</k>");
			break;	
			case 4:
				String sql1="select idvariable from parameters_variable";
				RecordSet rs =  DatabaseMgr.getInstance().executeQuery(null, sql1, null);
				
				int[] idvars = new int[rs.size()];
				for (int i = 0; i < rs.size(); i++) {
					idvars[i] = ((Integer) rs.get(i).get("idvariable")).intValue();
				}

			String c = "select cfvariable.iddevice, cfvariable.idvariable, b.description,a.description as description1 "+
			"from parameters_variable inner join "+
			"( "+
			"select idsite, iddevice, idvariable "+
			"from cfvariable "+
			"where idsite=1 " +
			") cfvariable " +
			"inner join cfdevice ON cfvariable.iddevice = cfdevice.iddevice " +
			"    on cfvariable.idsite=1 and cfvariable.idvariable = parameters_variable.idvariable "+
			"inner join "+
			"	cftableext as a "+
			"	on "+
			"		a.idsite=1 "+
			"		and a.tablename='cfvariable' "+
			"		and a.tableid = cfvariable.idvariable "+
			"		and a.languagecode = ? "+
			"inner join "+
			"	cftableext as b "+
			"	on "+
			"		b.idsite=1 "+
			"		and b.tablename='cfdevice' "+
			"		and b.tableid= cfvariable.iddevice "+
			"		and b.languagecode = ? " +
			"order by cfdevice.code, a.description";	
			rs=DatabaseMgr.getInstance().executeQuery(null, c, new Object[]{us.getLanguage(),us.getLanguage()});
				
			response.append("<rsp>");
			response.append("<![CDATA[");
			response.append("<table id='table_global_vars' cellspacing='1' cellpadding='0' width='100%'>");
			response.append("<tbody id='table_body_global_vars'>");
				for(int i=0;i<rs.size();i++){
					String myIdVar=rs.get(i).get(0)+"-"+rs.get(i).get(1);
					
					response.append("<tr id='");
					response.append(myIdVar);
					response.append("' class='"+(i%2==0?"Row1":"Row2")+"' style='cursor:pointer;'>"); 
					response.append("<td class='standardTxt' style='display:none'>");
					response.append(myIdVar);
					response.append("</td>");
					response.append("<td class='standardTxt' width='47.5%'><div>");
					response.append(rs.get(i).get(2));
					response.append("</div></td>");
					response.append("<td class='standardTxt' width='47.5%'><div>");
					response.append(rs.get(i).get(3));
					response.append("</div></td>");
					response.append("<td class='standardTxt' width='5%'><div><img src=\"images/actions/removesmall_on_black.png\" style=\"cursor:pointer;\" onclick=\"setModUser();deleteRow('table_global_vars','");
					response.append(myIdVar);
					response.append("');\"/></div></td>");
					response.append("</tr>");
				
				}
				response.append("</tbody>");
				response.append("</table>");
				response.append("]]>");	
				response.append("</rsp>");
				break;	
			case -1:
			{ 
				Integer from;
				try {
					from=Integer.parseInt((String) prop.get("from"));
				} catch (Exception e) {
					from=0;
				}
				if (from>=ParametersMgr.getParametersPhoto().getVariableList().length)
					from = 0;
				
				response.append("<photo>");
				
					int[] variables = ParametersMgr.getParametersPhoto().getVariableList();
					Float f = null;
					int end = from+RELOAD_PHOTO_NUM;
					if (end>=ParametersMgr.getParametersPhoto().getVariableList().length)
						end = ParametersMgr.getParametersPhoto().getVariableList().length;
					for (int i = from; i < end; i++) {
						response.append("<var_"+variables[i]+">");
						
						f=ParametersMgr.getParametersPhoto().getParameter(variables[i]) ;
						response.append(f.equals(Float.NaN) ? ParametersMgr.FLOAT_NAN_REPLACER : f);
						
						response.append("</var_"+variables[i]+">");
					}
				
				response.append("<end>"+end+"</end>");
				response.append("</photo>");
				break;
			}
				
				
		}
		
		response.append("</response>");
		return  response.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map initializeRefresh() {
        Map map = new HashMap();

        RefreshBean[] rb = 
            {
                new RefreshBean("PEventTable", IRefresh.R_PARAMETERS, 0)
            };
        RefreshBeanList rbl = new RefreshBeanList(REFRESH_TIME, rb.length);
        rbl.setRefreshObj(rb);

        map.put("tab2name", rbl);

       return map;
	}
	

}
