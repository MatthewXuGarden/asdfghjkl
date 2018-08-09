package com.carel.supervisor.presentation.bo.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;


public class VarDependencyList {
	
	List devList = null;
	List<VarDependency> varList = null;
	
	private List<VarDpdHelper> getAllDependencies(int idsite, String langcode) throws DataBaseException {
		  List totalList = new ArrayList<VarDpdHelper>();
		  totalList.addAll(new ArrayList());
		  totalList.addAll( getVariablesInAction(idsite ,  langcode) );
		  totalList.addAll( getVariablesInAlarmCondition(idsite ,  langcode) );
		  totalList.addAll( getVariablesInEventCondition(idsite ,  langcode) );
		  totalList.addAll( getVariablesInLogicVar(idsite ,  langcode) );
		  totalList.addAll( getVariablesInLogicDev(idsite ,  langcode) );
		  totalList.addAll( getVariablesInInstantReport(idsite ,  langcode) );
		  totalList.addAll( getVariablesInHistorReport(idsite ,  langcode) );
		  totalList.addAll( getVariablesInBooklet(idsite ,  langcode) );
		  totalList.addAll( getVariablesInGuardian(idsite ,  langcode) );
		  totalList.addAll( getVariablesInRemote(idsite ,  langcode) );
		  totalList.addAll( getVariablesInParametersControl(idsite ,  langcode) );
		  totalList.addAll( getVariablesInEnergy(idsite ,  langcode) );
		  totalList.addAll( getVariablesInLightsOnOff(idsite ,   langcode) );
		  totalList.addAll( getVariablesInKPI(idsite) );
		  totalList.addAll( getVariablesInDewPoint(idsite ,   langcode) );
		  totalList.addAll( getVariablesInOpt(idsite ,   langcode) );
		  totalList.addAll( getVariablesInTechSwitch(idsite ,   langcode) );
		  totalList.addAll( getVariablesInFSP(idsite ,   langcode) );
		  totalList.addAll( getVariablesInCO2(idsite ,   langcode) );
		  totalList.addAll( getVariablesInModbusSlave(idsite ,   langcode) );
		  return totalList;
	}
	
	private List getVariablesInModbusSlave(int idsite, String langcode) throws DataBaseException{
		// TODO Auto-generated method stub
		String sql = "select idvariable from cfvariable where idvariable in (select idvariable from mbslavevar)";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Integer idvar =  null;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
        	list.add(new VarDpdHelper(idvar,null,"[Modbus Slave]"));
        }
		return list;
	}

	private List getVariablesInOpt(int idsite, String langcode) throws DataBaseException {
		
		String sql = "select name , value from opt_startstop_settings where name like 'Var%' ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        int tmp_id = 0;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++)
        {	
        	tmp_id = (Integer) Integer.parseInt((String)rs.get(i).get("value") );
        	if(tmp_id !=0){
        		list.add(new VarDpdHelper(tmp_id,null,"[opt_startstop]"));
        	}
        }
        // opt_nightfreecooling_settings
        sql = "select name , value from opt_nightfreecooling_settings where name like 'Var%' ";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for (int i = 0; i < rs.size(); i++)
        {	
        	tmp_id = (Integer) Integer.parseInt((String)rs.get(i).get("value") );
        	//repcode = rs.get(i).get("code").toString();
        	if(tmp_id !=0){
        		list.add(new VarDpdHelper(tmp_id,null,"[opt_nightfreecooling]"));
        	}
        }
        // opt_lights_settings
        sql = "select name , value from opt_lights_settings where name like 'Var%' ";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for (int i = 0; i < rs.size(); i++)
        {	
        	tmp_id = (Integer) Integer.parseInt((String)rs.get(i).get("value") );
        	//repcode = rs.get(i).get("code").toString();
        	if(tmp_id !=0){
        		list.add(new VarDpdHelper(tmp_id,null,"[opt_nightfreecooling]"));
        	}
        }
        return list;
	}
	

	private List getVariablesInTechSwitch(int idsite, String langcode) throws DataBaseException {
		String sql = "select idvariable, iddevice from cfvariable where "
			+ "iddevice in (select iddevice from switchdev) and idvarmdl in("
			+ "select distinct idvarmdl from switchvar where iddevmdl in "
			+ "(select distinct iddevmdl from cfdevice where iddevice in (select iddevice from switchdev))"
			+ ") order by priority;";			
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>();
        int idDevicePrev = 0;
        String idSwitch = "";
        for (int i = 0; i < rs.size(); i++)
        {	
        	Record r = rs.get(i);
        	int idVariable = (Integer)r.get("idvariable");
        	int idDevice = (Integer)r.get("iddevice");
        	if( idDevice != idDevicePrev ) {
        		String sql2 = "select idswitch from switchdev where iddevice = " + r.get("iddevice");
        		idSwitch = DatabaseMgr.getInstance().executeQuery(null, sql2).get(0).get(0).toString();
        		idDevicePrev = idDevice;
        	}
        	list.add(new VarDpdHelper(idVariable, null, "SWITCH" + idSwitch + " -> [Tech Switch]"));
        }
        return list;
	}
	

	private List getVariablesInFSP(int idsite, String langcode) throws DataBaseException {
		
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>();
        // racks
        String sql = "select setpoint, minset, maxset, gradient from fsrack where idrack > 0;";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	Integer idVariable = ((Float)r.get("setpoint")).intValue();
        	list.add(new VarDpdHelper(idVariable, null, "[FSP]"));
        	idVariable = ((Float)r.get("minset")).intValue();
        	list.add(new VarDpdHelper(idVariable, null, "[FSP]"));
        	idVariable = ((Float)r.get("maxset")).intValue();
        	list.add(new VarDpdHelper(idVariable, null, "[FSP]"));        	
        	idVariable = ((Float)r.get("gradient")).intValue();
        	list.add(new VarDpdHelper(idVariable, null, "[FSP]"));        	
        }
        // utilities
        sql = "select solenoid, idtsh from fsutil;";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	Integer idVariable = Integer.parseInt(r.get("solenoid").toString());
        	list.add(new VarDpdHelper(idVariable, null, "[FSP]"));
        	Object tsh = r.get("idtsh");
        	if( tsh != null ) {
        		idVariable = Integer.parseInt(tsh.toString());
            	list.add(new VarDpdHelper(idVariable, null, "[FSP]"));        		
        	}
        }
        return list;
	}
	
	
	private List getVariablesInCO2(int idsite, String langcode) throws DataBaseException {
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>();
        
        // racks
        String sql = "select idvariable, idbackupdevice, idbackupvariable, offline, (select idvariable from cfvariable where cfvariable.iddevice = co2_rack.idbackupdevice and cfvariable.code='OFFLINE') AS idoffline from co2_rack;";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	int idVariable = (Integer)r.get("idvariable");
        	list.add(new VarDpdHelper(idVariable, null, "[Safe Restore] rack master variable"));
        	int idBackupDevice = (Integer)r.get("idbackupdevice");
        	if( idBackupDevice > 0 ) {
        		boolean bOffline = (Boolean)r.get("offline");
        		if( bOffline ) {
        			int idOfflineVariable = (Integer)r.get("idoffline");
        			list.add(new VarDpdHelper(idOfflineVariable, null, "[Safe Restore] rack backup offline"));
        		}
        		else {
        			int idBackupVariable = (Integer)r.get("idbackupvariable");
        			list.add(new VarDpdHelper(idBackupVariable, null, "[Safe Restore] rack backup variable"));
        		}
        	}
        }

        // utilities
        sql = "select iddevice from co2_grouputilities;";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	int idDevice = (Integer)r.get("iddevice");
        	String sqlMdl = "SELECT * FROM co2_devmdl WHERE devcode="
    			+ "(SELECT code FROM cfdevmdl WHERE iddevmdl = (SELECT iddevmdl FROM cfdevice WHERE iddevice=?))"
    			+ " AND israck=FALSE;";
    		String sqlVar = "SELECT idvariable FROM cfvariable WHERE iddevice=? AND code=?"
    			+ " ORDER BY idvariable;";
			RecordSet rsMdl = DatabaseMgr.getInstance().executeQuery(null, sqlMdl, new Object[] { idDevice });
			if( rsMdl.size() > 0 ) {
				Record rMdl = rsMdl.get(0);
				// 1st variable
				String varCode = rMdl.get("var1").toString();
				RecordSet rsVar = DatabaseMgr.getInstance().executeQuery(null, sqlVar, new Object[] { idDevice, varCode });
				if( rsVar.size() > 0 ) {
					int idVariable1 = (Integer)rsVar.get(0).get(0);
					list.add(new VarDpdHelper(idVariable1, null, "[Safe Restore] slave master variable"));
				}
				// 2nd variable
				varCode = (String)rMdl.get("var4");
				if( varCode != null ) {
					rsVar = DatabaseMgr.getInstance().executeQuery(null, sqlVar, new Object[] { idDevice, varCode });
					if( rs.size() > 0 ) {
						int idVariable2 = (Integer)rsVar.get(0).get(0);
						list.add(new VarDpdHelper(idVariable2, null, "[Safe Restore] slave master variable"));
					}
				}
			}
        }
        
        return list;
	}
	
	
	private List prepareDevList(List<VarDpdHelper> list, int idsite, String langcode) throws DataBaseException {
		ArrayList<DevDependency> dlist = new ArrayList<DevDependency>();
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (VarDpdHelper obj : list) {
			if(obj.getVarId()!=-1)
				sb.append(obj.getVarId()+",");
		}
		String vars = sb.substring(0,sb.length()-1)+")";		
		String sql ="select cfdevice.iddevice, cfdevice.code ,cftableext.description ,cfline.code as b ,cfdevice.idline from "+ 
	
					" cfdevice  , cftableext  ,cfline  where "+ 
					" cfdevice.iddevice in  "+
					" ( "+
					" select distinct (iddevice ) from cfvariable where  "+
					" cfvariable.idvariable in  "+ vars +
					" and cfvariable.iscancelled='FALSE' and cfvariable.idsite=1 "+
					" ) and  "+
					" cfdevice.idsite = "+idsite+" and "+
					" cftableext.languagecode =  '"+langcode+"' and "+
					" cftableext.tablename='cfdevice' and  "+
					" cftableext.tableid= cfdevice.iddevice and  "+
					" cfline.idline=cfdevice.idline and  "+
					" cfdevice.idline >= 0    and cftableext.idsite = 1 "+
					" group by cfdevice.iddevice, cfdevice.code ,cftableext.description ,cfdevice.idline ,cfline.code ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++)
        {
        	int iddev = (Integer)rs.get(i).get(0) ;
        	String devcode =rs.get(i).get(1).toString();
        	String devdesc = rs.get(i).get(2).toString();
        	String linecode = rs.get(i).get(3).toString();
        	String idline =rs.get(i).get(4).toString(); 
        	dlist.add(new DevDependency(iddev ,devcode,devdesc,idline,linecode ));
        }
        
        // add the booklet cabinet devices info (which just use device no variable)
        sb = new StringBuffer();
        for (VarDpdHelper obj : list) {
			if(obj.getDevId()!=-1)
				sb.append(obj.getDevId()+",");
		}
        if(sb.length()>0){
	        String devs = sb.substring(0,sb.length()-1);
	        sql ="select cfdevice.iddevice, cfdevice.code ,cftableext.description ,cfline.code as b ,cfdevice.idline from "+ 
				"   cfdevice  , cftableext  ,cfline  where "+ 
				" cfdevice.iddevice in  "+
				" ( "+devs +" ) and  "+
				" cfdevice.idsite = "+idsite+" and "+
				" cftableext.languagecode =  '"+langcode+"' and "+
				" cftableext.tablename='cfdevice' and  "+
				" cftableext.tableid= cfdevice.iddevice and  "+
				" cfline.idline=cfdevice.idline and  "+
				" cfdevice.idline >= 0  and cftableext.idsite = 1 "+
				" group by cfdevice.iddevice, cfdevice.code ,cftableext.description ,cfdevice.idline ,cfline.code ";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++)
			{
				int iddev = (Integer)rs.get(i).get(0) ;
				String devcode =rs.get(i).get(1).toString();
				String devdesc = rs.get(i).get(2).toString();
				String linecode = rs.get(i).get(3).toString();
				String idline =rs.get(i).get(4).toString(); 
				boolean contained = false;
				for (DevDependency obj : dlist) {
					if(obj.getDevid() == iddev){
						contained = true;
						break;
					}
				}
				if(!contained){ // make sure just add once.
					dlist.add(new DevDependency(iddev ,devcode,devdesc,idline,linecode ));
				}
			}
		}
        
        this.setDevList(dlist);
		return dlist;
	}


	private List prepareVarList(List<VarDpdHelper> vdhlist, int idsite, String langcode) throws DataBaseException {
		List vlist = new ArrayList<VarDpdHelper>();
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (VarDpdHelper obj : vdhlist) {
			sb.append(obj.getVarId()+",");
		}
		String vars = sb.substring(0,sb.length()-1)+")";
		String sql = "select d.iddevice , d.code ,  t.description , v.idvariable from cfvariable v ,cfdevice d ,cftableext t where v.idvariable in "+vars+
						" and v.idsite = '"+idsite+"' and  t.languagecode = '"+langcode+"'  and v.iddevice = d.iddevice and t.tablename = 'cfvariable' " +
						" and t.tableid = v.idvariable and t.idsite = 1 and d.idline >=0 and v.iscancelled='FALSE'  ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++)
        {
        	int devid = (Integer)rs.get(i).get(0);
        	String devCode = rs.get(i).get(1).toString();
        	String vardesc = rs.get(i).get(2).toString();
        	int idvar = (Integer)rs.get(i).get(3);
        	ArrayList<VarDependency> vardpList = VarDependency.getVardpList( devid,devCode, vardesc,vdhlist,getDevList(idsite,langcode) ,idvar);
        	for(VarDependency vd :vardpList){
        		vlist.add(vd );
        	}
        	
        }
        
        // add the booklet cabinet devices info (which just use device no variable) 
        sb = new StringBuffer();
        for (VarDpdHelper obj : vdhlist) {
        	if(obj.getDevId()!=-1)
    			sb.append(obj.getDevId()+",");
		}
        if(sb.length()>0){
			String devs = sb.substring(0,sb.length()-1);
			sql = "select d.iddevice,d.code,t.description from cfdevice d ,cftableext t where d.iddevice in ( "+devs+
					" ) and   t.languagecode = '"+langcode+"'  and  t.tablename = 'cfdevice' " +
					" and t.tableid = d.iddevice  and t.idsite = 1 and d.idline >=0  ";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
	        for(int i = 0; i < rs.size(); i++)
	        {
	        	int devid = (Integer)rs.get(i).get(0);
	        	String devCode = rs.get(i).get(1).toString();
	        	String devDesc = rs.get(i).get(2).toString();
	        	ArrayList<VarDependency> vardpList = VarDependency.getVardpList( devid,devCode, null,vdhlist,getDevList(idsite,langcode) ,-1);
	        	for(VarDependency vd :vardpList){
	        		vlist.add(vd );
	        	}
	        }
        }
        this.setVarList(vlist);
		return vlist;
	}
	

	public  List getVariablesInAction(int idsite, String langcode) throws DataBaseException
	{

        // query on 'cfaction' table
		// get all actions with type v (Variable)
		//String sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and (actiontype = 'L' or actiontype = 'V')";
		String sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and actiontype = 'V'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite)});
        String[] params = null;
        int tmp_id_1 = 0;
        int tmp_id_2 = 0;
        String actcode = "";
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        //cycle on records
        for (int i = 0; i < rs.size(); i++)
        {
            params = rs.get(i).get("parameters").toString().split(";");
            actcode = rs.get(i).get("code").toString();
            //cycle on params
            for (int j = 0; j < params.length; j++)
            {
                String[] parameters = params[j].split("=");
                
                tmp_id_1 = Integer.parseInt(parameters[0]);
                list.add(new VarDpdHelper(tmp_id_1,actcode,"[action]"));
                
                // if action involves that the variable value to set is taken from another variable
                // there are two parameters to consider
                if(parameters[1].contains("id"))
                {
                	tmp_id_2 = Integer.parseInt(params[j].split("=")[1].replace("id", ""));
                	 if (tmp_id_2 != 0 ){
                		 list.add(new VarDpdHelper(tmp_id_2,actcode,"[action]"));
                	 }
                }
            }
        }
        // get all actions with type L (Relay)
		sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and actiontype = 'L'";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite)});
        //cycle on records
        for (int i = 0; i < rs.size(); i++)
        {
            params = rs.get(i).get("parameters").toString().split(";");
            //cycle on params
            for (int j = 0; j < params.length; j++)
            {
                String[] parameters = params[j].split("=");
                tmp_id_1 = Integer.parseInt(parameters[0]);
                tmp_id_2 = getVarIdFromRelayId(idsite, tmp_id_1);
                list.add(new VarDpdHelper(tmp_id_2,actcode,"[action]"));
            }
        }
        return list;
	}
	
	
	

	private  int getVarIdFromRelayId(int idsite, int idrelay) throws DataBaseException
	{
		String sql = "select idvariable from cfrelay where idsite = ? and idrelay = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite), new Integer(idrelay)});
        if( rs.size() == 1 )
        	return Integer.parseInt(rs.get(0).get("idvariable").toString());
        else
        	return -1;
	}
	
	public  List getVariablesInAlarmCondition(int idsite,  String langcode) throws DataBaseException
	{
		 
		//query on 'cfvarcondition' and 'cfcondition' tables
		String sql = "select cfvarcondition.idvariable,cfcondition.condcode from cfvarcondition,cfcondition where cfvarcondition.idsite = ? " +
	    	"and cfcondition.idsite = cfvarcondition.idsite and cfcondition.idcondition=cfvarcondition.idcondition and cfcondition.condType!=?";

        // action type P is excluded from search
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite),"P" });
        
		int tmp_id = 0;
        String condcode = "";
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        
        for (int i = 0; i < rs.size(); i++)
        {
            tmp_id = ((Integer) rs.get(i).get("idvariable")).intValue();
            condcode = rs.get(i).get("condcode").toString();
            list.add(new VarDpdHelper(tmp_id,condcode,"[AlarmCondition]"));

        }
        
        return list;
	}
	
	public  List getVariablesInEventCondition(int idsite, String langcodes) throws DataBaseException
	{
		
		//query on 'cfvariable' and 'cffunction'tables
		String sql = "select cfvariable.idvariable, cfvariable.iddevice, cfvariable.functioncode, cfvariable.code, cffunction.parameters from cfvariable, cffunction " + 
		"where cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.code = ? and cfvariable.iscancelled = ? and cfvariable.functioncode = cffunction.functioncode";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite), new Integer(0), "LOGIC_CONDITION", "FALSE"});
		
		int tmp_id = 0;
		String tmpstring = "";
		String condcode = "";
		String[] tmpparams;
		ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
		for (int i = 0; i < rs.size(); i++)
		{
			tmpstring = (String) rs.get(i).get("parameters");
			tmpparams = tmpstring.split(";");
			
			for(int j=0; j<tmpparams.length; j++)
			{
				if(tmpparams[j].contains("pk"))
				{
					
					tmp_id = getMainVariableID(Integer.parseInt((tmpparams[j].replace("pk", ""))));
					
					//get the condition description
					int varid = ((Integer)rs.get(i).get("idvariable"));
					sql = "select cfcondition.condcode from cfcondition, cfvarcondition where idvariable = ? and cfcondition.idcondition = cfvarcondition.idcondition and cfcondition.idsite = ?";
					RecordSet record = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(varid), new Integer(1)});
					
					if(record.size() > 0)
					{
						condcode = (String)record.get(0).get("condcode");
					}
					list.add(new VarDpdHelper(tmp_id,condcode,"[EventCondition]"));
				}	
			}	
		}
		
		return list;
	}
	
	
	public  List getVariablesInInstantReport(int idsite,  String langcode) throws DataBaseException
	{
		//query on 'cfreportdetail' and 'cfreportkernel' tables
		String sql = "select cfreportdetail.idreport,cfreportdetail.idvariable, cfreportkernel.code from cfreportdetail, cfreportkernel where cfreportkernel.idreport = cfreportdetail.idreport";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int tmp_id = 0;
        String repcode = "";
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	tmp_id = (Integer) rs.get(i).get("idvariable");
        	repcode = rs.get(i).get("code").toString();
        	list.add(new VarDpdHelper(tmp_id,repcode,"[InstantReport]"));
        }
        return list;
	}
	
	public  List getVariablesInHistorReport(int idsite,  String langcode) throws DataBaseException
	{
		
		//query on 'cfreportdetail' and 'cfreportkernel' tables
		String sql = "select cfreportdetail.idvariable, cfreportkernel.code from cfreportdetail, cfreportkernel ,cfvariable " +
				" where cfvariable.iscancelled = 'FALSE' and cfreportkernel.idreport = cfreportdetail.idreport and cfvariable.idhsvariable = cfreportdetail.idvariable  ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int tmp_id = 0;
        String repcode = "";
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	tmp_id = (Integer) rs.get(i).get("idvariable");
        	repcode = rs.get(i).get("code").toString();
        	list.add(new VarDpdHelper(tmp_id,repcode,"[HistorReport]"));
        }
        
        return list;
	}
	
	public  List getVariablesInLogicVar(int idsite,  String langcode) throws DataBaseException
	{

		//query on 'cfvariable' and 'cffunction' tables
		String sql =  "select cffunction.parameters, cfvariable.code ,cftableext.description from cffunction , cfvariable ,cftableext where cfvariable.iscancelled = ? " + 
			"and cfvariable.functioncode is not null and  cftableext.languagecode = '"+langcode+"' and cftableext.tablename ='cfvariable' and cftableext.tableid = cfvariable.idvariable and  cfvariable.functioncode = cffunction.functioncode and cfvariable.code != ? and cfvariable.idsite = ? and cfvariable.idvarmdl is null";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { "FALSE", "LOGIC_CONDITION", new Integer(idsite)});
     
        int tmp_id = 0;
        String logicId = "";
        String actName = "";
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	logicId = (String)rs.get(i).get("code");
        	actName =(String)rs.get(i).get("description");
        	String[] params = ((String)rs.get(i).get("parameters")).split(";");
        	
        	for(int j=0; j<params.length; j++)
        	{
        		if(((String)params[j]).startsWith("pk"))
        		{
        			tmp_id = Integer.parseInt(params[j].replace("pk", ""));
        			list.add(new VarDpdHelper(tmp_id,actName,"[LogicVar]"));
        		}
        	}
        }
        
        return list;
	}
	
	public  List getVariablesInLogicDev(int idsite,  String langcode) throws DataBaseException
	{
		//query on 'cfvariable' and 'cffunction' tables
		String sql =  "select cffunction.parameters, cfvariable.code, cftableext.description from cffunction , cfvariable ,cftableext where cfvariable.iscancelled = ? " + 
			"and cfvariable.functioncode is not null and cfvariable.iddevice = cftableext.tableid and cftableext.tablename = 'cfdevice' and cftableext.languagecode = '"+langcode+"' and cfvariable.functioncode = cffunction.functioncode and cfvariable.code != ? and cfvariable.idsite = ? and cfvariable.idvarmdl is not null";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { "FALSE", "LOGIC_CONDITION", new Integer(idsite)});
     
        int tmp_id = 0;
        String logicDevId = "";
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	logicDevId = rs.get(i).get("description").toString();
        	String[] params = ((String)rs.get(i).get("parameters")).split(";");
        	
        	for(int j=0; j<params.length; j++)
        	{
        		if(((String)params[j]).startsWith("pk")){
        			tmp_id = Integer.parseInt(params[j].replace("pk", ""));
        			list.add(new VarDpdHelper(tmp_id,logicDevId,"[LogicDevice]"));
        		}
        		
        	}
        }
		
		return list;
	}
	
	public  List getVariablesInGuardian(int idsite,  String langcode) throws DataBaseException
	{
		//query on 'cfguardian' table
		String sql = "select idvariable from cfvarguardian";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Integer idvar =  null;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
        	list.add(new VarDpdHelper(idvar,null,"[Guardian]"));
        }
		return list;
	}
	
	public  List getVariablesInRemote(int idsite,  String langcode) throws DataBaseException
	{
		//query on 'cfvariable' and 'cftransfervar' tables
		String sql = "select idvariable from cfvariable where idhsvariable in (select idvar from cftransfervar)";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Integer idvar =  null;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
        	list.add(new VarDpdHelper(idvar,null,"[Remote]"));
        }
		return list;
	}
	
	public  List getVariablesInBooklet(int idsite,  String langcode) throws DataBaseException
	{
		LangService lan = LangMgr.getInstance().getLangService(langcode);
		//query on 'bookletdevvar' table
		String sql = "select idvariable from bookletdevvar";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Integer idvar =  null;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
        	list.add(new VarDpdHelper(idvar,null,lan.getString("booklet","tab1name")));
        }
        
		//booklet cabinet
        sql = "select cabinet,iddevice  from booklet_cabinet_dev , booklet_cabinet where booklet_cabinet_dev.idcabinet = booklet_cabinet.id";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for (int i = 0; i < rs.size(); i++) 
        {
        	String cabinet = (String)rs.get(i).get(0);
        	int iddev = (Integer)rs.get(i).get(1);
        	list.add(new VarDpdHelper(iddev,-1,cabinet,lan.getString("booklet","tab1name")+"->"+lan.getString("booklet","tab2name")));
        }
		return list;
	}
	
	public  List getVariablesInParametersControl(int idsite,  String langcode) throws DataBaseException
	{
		//query on 'parameters_variable' table
		String sql = "select idvariable from parameters_variable";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Integer idvar =  null;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
        	list.add(new VarDpdHelper(idvar,null,"[ParametersControl]"));
        }
		return list;
	}
	
	public  List getVariablesInEnergy(int idsite, String langcode) throws DataBaseException
	{
		// query on 'energyactive' table
		String sql = "select idgroup, idvariable from energyactive";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		List<Integer> list = null; 
		Integer idvar = null;
		ArrayList<VarDpdHelper> list2 = new ArrayList<VarDpdHelper>(); 
		for (int i = 0; i < rs.size(); i++)
		{
			idvar = (Integer)rs.get(i).get(1);
			int groupid = (Integer)rs.get(i).get(0);
			String groupname = ""; 
			if(groupid != -1)
			{
				String query = "select name from energygroup where idgroup = ?";
				RecordSet result = DatabaseMgr.getInstance().executeQuery(null, query, new Object[] { new Integer(groupid)});
			
				if(result.size() > 0)
				{
					groupname = (String)result.get(0).get(0);
				}
			}
			list2.add(new VarDpdHelper(idvar,groupname,"[Energy]"));
		}
		
		sql = "select idgroup, idkw, idkwh from energyconsumer";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		for (int i = 0; i < rs.size(); i++)
		{
			list = new ArrayList<Integer>();
			
			if((Integer)rs.get(i).get(1) != null)
			{
				idvar = getMainVariableID((Integer)rs.get(i).get(1));
				
				if(idvar != -1)
					list.add(idvar);
			}
			
			if((Integer)rs.get(i).get(2) != null)
			{
				idvar = getMainVariableID((Integer)rs.get(i).get(2));
				
				if(idvar != -1)
					list.add(idvar);
			}
		    for (int varid : list) {
				int groupid = (Integer)rs.get(i).get(0);
				String groupname = ""; 
				if(groupid != -1)
				{
					String query = "select name from energygroup where idgroup = ?";
					RecordSet result = DatabaseMgr.getInstance().executeQuery(null, query, new Object[] { new Integer(groupid)});
				
					if(result.size() > 0)
					{
						groupname = (String)result.get(0).get(0);
					}
				}
				list2.add(new VarDpdHelper(varid,groupname,"[Energy]"));
	        }
		}		
		
		return list2;
	}
	
	public  List getVariablesInLightsOnOff(int idsite, String langcode ) throws DataBaseException
	{
		//check if the model is used in lightsOnOff configuration
		//query on ln_varmdl' table
		
		String sql = "select idvariable from cfvariable where iddevice in (select iddev from ln_devlcnt where iddev in (select iddevice from cfdevice )) and idvarmdl in (select idvarmdl from ln_varmdl ) and idhsvariable is not null";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
		Integer idvar =  null;
		ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
		for (int i = 0; i < rs.size(); i++) 
        {
			idvar = (Integer)rs.get(i).get(0);
			list.add(new VarDpdHelper(idvar,null,"[LightsOnOff]"));
        }
		
		
        //check if an instantiated variable is used in 'remote commands' section
        //query on 'ln_fieldvars' table
        sql = "select idvardig from ln_fieldvars";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
        	list.add(new VarDpdHelper(idvar,null,"[LightsOnOff-RemoteCommand]"));
        }
		return list;
	}
	
	public  List getVariablesInKPI(int idsite ) throws DataBaseException
	{
		//get all idvarmdls related to ids_variables
		//List<Integer> idvarmdlList = getVarmdlList(ids_variables);
		List<Integer> varconfids = new ArrayList<Integer>();
		ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
		String sql = "select mastervarmdl, defvarmdl, solenoidvarmdl from kpiconf ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		for (int i = 0; i < rs.size(); i++) 
        {
			Integer idvarmdl1 = (Integer)rs.get(i).get("mastervarmdl");
			if(idvarmdl1 != null && !idvarmdl1.equals("") && !varconfids.contains(idvarmdl1))
				varconfids.add(idvarmdl1);
			
			Integer idvarmdl2 = (Integer)rs.get(i).get("defvarmdl"); 
			if(idvarmdl2 != null && !idvarmdl2.equals("") && !varconfids.contains(idvarmdl2))
				varconfids.add(idvarmdl2);
			
			Integer idvarmdl3 = (Integer)rs.get(i).get("solenoidvarmdl");
			if(idvarmdl3 != null && !idvarmdl3.equals("") && !varconfids.contains(idvarmdl3))
				varconfids.add(idvarmdl3);
        }
		
		
		sql = "select iddevice from kpidevices where iddevice in (select iddevice from cfdevice)";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		if( varconfids.size() > 0 && rs.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for (int i = 0; i < rs.size(); i++) 
	        {
				Integer iddevice = ((Integer)rs.get(i).get(0));
				sql += iddevice+",";
	        }
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < varconfids.size(); i++)
			{
				sql += varconfids.get(i)+",";
			}
			sql = sql.substring(0, sql.length() -1);
			sql += ") and idhsvariable is not null";
			
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			Set<Integer> hashset = new HashSet<Integer>();
			Integer idvar =  null;
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
				list.add(new VarDpdHelper(idvar,null,"[KPI]"));
	        }
		}
		return list;
	}
	
	public  List getVariablesInDewPoint(int idsite,  String langcode) throws DataBaseException
	{
		DevMdlBeanList devmdlbean = new DevMdlBeanList();
//		DevMdlBean devmdl = devmdlbean.retrieveById(idsite, langcode, iddevmdl);
//		String devmdlcode =  devmdl.getCode();
		
		//query on 'ac_master_mdl', 'ac_extra_vars', 'ac_slave_mdl', 'ac_heartbit_mdl' tables
		//get codes of all varmdl involved on DewPoint configuration 
		ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
		String sql = "select vcode from ac_master_mdl ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
		ArrayList<String> slaveVarCodeList = new ArrayList<String>();
		
		ArrayList<String> masterVarCodeList = new ArrayList<String>();
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
		}
		
		sql = "select varcode from ac_extra_vars";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  null);
				
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(!slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
		}
		
		sql = "select vcode from ac_slave_mdl ";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  null);
		
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
		}
		
				
		sql = "select digvar, vcode from ac_heartbit_mdl ";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  null);
		
		if(rs != null && rs.size() > 0)
		{
			String devcode = (String)rs.get(0).get(0);
			if(devcode != null && !masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(devcode != null && !slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
			
			devcode = (String)rs.get(0).get(1);
			if(devcode != null && !masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(devcode != null && !slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
			
		}
		
		ArrayList<Integer> masterVarmdlIdsList = getVarmdlIds(masterVarCodeList);
		ArrayList<Integer> slaveVarmdlIdsList = getVarmdlIds(slaveVarCodeList);
		
		sql = "select iddevmaster from ac_master where iddevmaster in (select iddevice from cfdevice ) group by iddevmaster";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		ArrayList<Integer> masterDevId = new ArrayList<Integer>();
		for(int i = 0; i < rs.size(); i++)
		{
			masterDevId.add((Integer)rs.get(i).get(0));
		}
		
		sql = "select iddevslave from ac_slave where iddevslave in (select iddevice from cfdevice ) group by iddevslave";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		ArrayList<Integer> slaveDevId = new ArrayList<Integer>();
		for(int i = 0; i < rs.size(); i++)
		{
			slaveDevId.add((Integer)rs.get(i).get(0));
		}
		
//		Set<Integer> hashset = new HashSet<Integer>();
		Integer idvar =  null;
		
		if(masterDevId.size() > 0 && masterVarmdlIdsList.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for(int i = 0; i < masterDevId.size(); i++)
			{
				sql += masterDevId.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < masterVarmdlIdsList.size(); i++)
			{
				sql += masterVarmdlIdsList.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idhsvariable is not null";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
				list.add(new VarDpdHelper(idvar,null,"[DewPoint]"));
	        }
		}
		
		if(slaveDevId.size() > 0 && slaveVarmdlIdsList.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for(int i = 0; i < slaveDevId.size(); i++)
			{
				sql += slaveDevId.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < slaveVarmdlIdsList.size(); i++)
			{
				sql += slaveVarmdlIdsList.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idhsvariable is not null";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
				list.add(new VarDpdHelper(idvar,null,"[DewPoint]"));
	        }
		}
		return list;
	}
	
	private  int getMainVariableID(int idhistorvaiable) throws DataBaseException
	{
		//this query to get the id of the main variable (now we have the id of the logged variable)
		String sql = "select idvariable from cfvariable where idhsvariable = ?";
		int tmp_id = -1;
		RecordSet record = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idhistorvaiable)});
		
		if(record.size() > 0)
		{
			tmp_id = ((Integer)record.get(0).get("idvariable"));
		}
		
		return tmp_id;
	}
	
	private  ArrayList<Integer> getVarmdlList(int[] ids_variables) throws DataBaseException
	{
		String sql = "select idvarmdl from cfvariable where idvariable in(";
        for(int i=0 ; i<ids_variables.length; i++)
        {
        	sql +=ids_variables[i]+",";
        }
        sql = sql.substring(0, sql.length()-1);
        sql += ") group by idvarmdl";
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        
        ArrayList<Integer> idvarmdlList = new ArrayList<Integer>();
        
        Integer idvarmdl =  null;
        for(int i = 0; i < rs.size(); i++)
        {
        	idvarmdl = (Integer)rs.get(i).get(0);
        	idvarmdlList.add(idvarmdl);
        }
        
        return idvarmdlList;
	}
	
	
	private  ArrayList<Integer> getVarmdlIds(ArrayList<String> varmdlcodes) throws DataBaseException
	{
		ArrayList<Integer> varmdlids = new ArrayList<Integer>();
		
		if (varmdlcodes.size() == 0)
			return varmdlids;
		
		String sql = "select idvarmdl from cfvarmdl  where code in ('";
		
		for(int i = 0; i < varmdlcodes.size(); i++)
		{
			sql += varmdlcodes.get(i)+"','";
		}
		
		sql = sql.substring(0, sql.length() -2);
		sql = sql +")";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		for(int i = 0; i < rs.size(); i ++)
		{
			varmdlids.add((Integer)rs.get(i).get(0));
		}
		return varmdlids;
		
	}

	
	public  void prepareDevsVarsList (int idsite, String langcode) throws DataBaseException{
		List<VarDpdHelper> tList = getAllDependencies(idsite,langcode);
		List dList = prepareDevList(tList,idsite,langcode);
		List vList = prepareVarList(tList,idsite,langcode);
	}


	

	public VarDependencyList() {
		this.devList = null;
		this.varList = null;
	}
	
	public List getDevList(int idsite, String langcode) throws DataBaseException {
		if(devList ==null){
			prepareDevsVarsList( idsite,  langcode);
		}
		return devList;
	}

	public void setDevList(List devList) {
		this.devList = devList;
	}


	public List<VarDependency> getVarList(int idsite, String langcode) throws DataBaseException {
		if(varList==null){
			prepareDevsVarsList( idsite,  langcode);
		}
		return varList;
	}

	public void setVarList(List varList) {
		this.varList = varList;
	}
	
	
}

