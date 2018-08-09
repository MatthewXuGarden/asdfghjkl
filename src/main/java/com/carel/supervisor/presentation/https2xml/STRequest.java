package com.carel.supervisor.presentation.https2xml;


import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.VariablesAccess;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.DefaultCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;
import com.carel.supervisor.director.bms.Subset;
import com.carel.supervisor.director.bms.SubsetKey;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;

public class STRequest implements IXMLRequest {

	private StringBuffer response;
	private String username;
	
	public String getNameRequest() {
		return "ST";
	}

	public String getResponse() {
		return response.toString();
	}

	@SuppressWarnings("static-access")
	public void startRequest(XMLNode node) throws Exception {
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		response = new StringBuffer ();
		response.append("<rs t='ST'>\n");
		StringBuffer response2 = new StringBuffer();
		
		int waittime = node.getAttribute("waittime", 5000);
		
		// SET delle Variabili Richieste!
		XMLNode[] nodes = node.getNodes("e");

		SetContext setContext = new SetContext();
		String lang = "EN_en";
		try
		{
			lang = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch (Exception e)
		{
		}
		setContext.setLanguagecode(lang);
    	setContext.setCallback(new DefaultCallBack());
		setContext.setUser(getUsername());
		
		int []idsVars= null;
		String[] values=null;
		boolean[] ok = null;
		boolean[] inSubset = null;
		
		if(nodes!=null){
			idsVars= new int[nodes.length];
			values= new String[nodes.length];
			ok = new boolean[nodes.length];
			inSubset = new boolean[nodes.length];
		}//if
			
		for (int i = 0; i < nodes.length; i++) {
			// per ogni variabile della richiesta...
			int idvar = new Integer( nodes[i].getAttribute("idvar")).intValue();
			int val = new Integer( nodes[i].getAttribute("val")).intValue();
			idsVars[i]=idvar;
			values[i]=Integer.toString(val);
			
			//... recupero info su variabile e device... 
			VarphyBean v = VarphyBeanList.retrieveVarById(1, idvar, bmsc.getLanguage());
			DeviceListBean dlb = new DeviceListBean(1, bmsc.getLanguage());		
			
			if (v==null)
			{
				ok[i] = false;
				continue;
			}
			DeviceBean db = dlb.retrieveSingleDeviceById(1, v.getDevice(), bmsc.getLanguage());
			
			//...recupero il subset di cui fa parte il modello			
			String conf = bmsc.getDevicemappingsmap().get(v.getDevice());
			
			Subset sst = bmsc.getSubsetsmap().get(new SubsetKey(db.getIddevmdl(),conf)) ;
			
			//... e controllo se la variabile fa parte del subset modificabile
			if (sst!=null && sst.contains(v.getModel()) )
			{
				inSubset[i]=true;
				try
				{
	            	setContext.addVariable(idvar, val);
	        		ok[i] = true;
				}//try
				catch(Exception e){
					ok[i] = false;
				}//catch
	        
			}
			else
			{
				//la variabile � fuori dal subset
				inSubset[i]=false;
			}
		}
		
		//accodo le modifiche!
		SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
       
		//una volta settate le variabili aspetto waittime...
       	try
		{
			Thread.sleep(waittime);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		for (int i = 0; i < nodes.length; i++) {
			response2.append("\n<v idvar=\"");
			response2.append(idsVars[i]);
			response2.append("\" ");
			
			try
			{
				if (inSubset[i]){
					if (ok[i]){
						float v = ControllerMgr.getInstance().getFromField(idsVars[i]).getCurrentValue();
						if(v == Float.parseFloat(values[i]))
							response2.append("state=\"ok\" ");
						else
							response2.append("state=\"ko\"");
					}
					else
						response2.append("state=\"ko\" ");
				}
				else
					response2.append("state=\"denied\" ");
			}//try
			catch(Exception e){
				response2 = new StringBuffer("<error ");
			}//catch
			response2.append(">");
		}

		
		
		response.append(response2.toString());
		response.append("</rs>\n");
	}


	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}
