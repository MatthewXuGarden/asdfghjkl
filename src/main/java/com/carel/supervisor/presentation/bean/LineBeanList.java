package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.Event;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.fs.FSManager;
import com.carel.supervisor.plugin.fs.FSRack;
import com.carel.supervisor.presentation.bo.BSiteView;
import com.carel.supervisor.presentation.bo.helper.DevDependency;
import com.carel.supervisor.presentation.bo.helper.GraphVariable;
import com.carel.supervisor.presentation.bo.helper.LineConfig;
import com.carel.supervisor.presentation.bo.helper.VarDependency;
import com.carel.supervisor.presentation.bo.helper.VarDependencyCheck;
import com.carel.supervisor.presentation.bo.helper.VarDependencyList;
import com.carel.supervisor.presentation.bo.helper.VarDependencyState;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;
import com.carel.supervisor.presentation.fs.FSStatus;
import com.carel.supervisor.presentation.groupmng.GroupManager;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class LineBeanList
{
    private static final String ID_LINE = "idline";
    private List<Record> lineList = new ArrayList<Record>();
    private HashMap<Integer, LineBean> lineMap = new HashMap<Integer, LineBean>();
    private int screenw = 1024;
    private int screenh = 768;
    private static ProtocolBean protBean = null;
    private int depdtLength = 0;
    private Event[] list = null;

    
    public LineBeanList() throws DataBaseException
    {
    	if( protBean == null )
    		protBean = new ProtocolBean();
    }

    
    public LineBean[] retrieveLines(int idsite) throws DataBaseException
    {
        String sql = "select * from cfline where idsite = ? order by code";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });
        LineBean[] lines = new LineBean[rs.size()];
        LineBean tmp = null;

        for (int i = 0; i < rs.size(); i++)
        {
            lineList.add(rs.get(i));
            lines[i] = new LineBean(rs.get(i));

            //aggiunta mappa
            tmp = new LineBean(rs.get(i));
            lineMap.put(tmp.getIdline(), tmp);
        }

        return lines;
    }

    public LineBean getLineById(int idline)
    {
        return lineMap.get(new Integer(idline));
    }

    //public String[] getComPortsUsed(int idsite) throws DataBaseException
    public Collection<String> getComPortsUsed(int idsite) throws DataBaseException
    {
        String sql = "select comport from cfline where idsite = ? order by comport";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });
        LinkedList<String> comports = new LinkedList<String>();

        for (int i = 0; i < rs.size(); i++)
        {
            comports.add(UtilBean.trim(rs.get(i).get("comport").toString()));
        }

        return comports;
    }

    public LineBean retrieveLineById(int idsite, int idline)
        throws DataBaseException
    {
        String sql = "select * from cfline where idsite = ? and idline = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idline) });

        return new LineBean(rs.get(0));
    }
    
    public  String getHTMLDepdtTable(UserSession us,VarDependencyList  vdl) throws DataBaseException
    {
    	int idsite = us.getIdSite();
        String language = us.getLanguage();
    	List<DevDependency> list = vdl.getDevList(idsite, language);
    	String devids = us.getProperty("DevIds2dlt");
    	List devsList = new ArrayList();
    	List devs2dlt = new ArrayList();
    	ListBoxElement tmp = null;
    	if(devids != null){
    		String[] ids = devids.split(";");
    		for (DevDependency ss : list) {
    			boolean todlt = false;
    			for(int i=0;i<ids.length;i++){
        			if(ids[i].equalsIgnoreCase(ss.getDevid()+"")){
        				tmp = new ListBoxElement(ss.getDevCode() +" -> " +ss.getDevDesc(),ss.getDevid()+"");
        				devs2dlt.add(tmp);
        				todlt = true;
        			}
        		}
    			if(!todlt){
    				tmp = new ListBoxElement(ss.getDevCode() +" -> " +ss.getDevDesc(),ss.getDevid()+"");
            		devsList.add(tmp);
    			}
        		
    		}
    	}else{
    		for (DevDependency ss : list) {
    			tmp = new ListBoxElement(ss.getDevCode() +" -> " +ss.getDevDesc(),ss.getDevid()+"");
        		devsList.add(tmp);
    		}
    	}
    	LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
    	DblListBox dblListBox = new DblListBox(devsList, devs2dlt,false, true, true, null, true,true,"freshDependencisList()");
		dblListBox.setHeaderTable1(lang.getString("vardpd","dpddev"));
		dblListBox.setHeaderTable2(lang.getString("vardpd", "dpddlt"));
		dblListBox.setScreenW(us.getScreenWidth());
		dblListBox.setScreenH(us.getScreenHeight());
		dblListBox.setLeftRowsListBoxNoLimit(10);
		dblListBox.setRightRowsListBoxNoLimit(10);
		dblListBox.setLeftSize(10);
		dblListBox.setRightSize(10);
		//dblListBox.setWidthListBox(400); 
		dblListBox.setIdlistbox("depdevs");
		return dblListBox.getHtmlDblListBox();		    	
    }
    
    
    public String getVarDpdt(UserSession us,LangService multiLanguage,VarDependencyList  vdl)throws Exception
	{
    	int idsite = us.getIdSite();
        String language = us.getLanguage();
    	String devids = us.getPropertyAndRemove ("DevIds2dlt");
    	List<VarDependency> list  = vdl.getVarList(idsite,language);
		HTMLElement[][] data = null;
		if(devids!=null){
			list = pickBydevids(devids.split(";"),list);
		}else{
			list = null;
		}
		String tdclass = "";
		if(list!=null)
		{
			data = new HTMLElement[list.size()][5];
			String s ="";
			for(int i=0;i<list.size();i++)
			{
				VarDependency str = list.get(i);
				data[i][0] = new HTMLSimpleElement(str.getLine());
				String desc = UtilityString.replaceBadChars4XML(str.getDevDescription());
				data[i][1] = new HTMLSimpleElement(str.getDeviceCode()+" -> "+desc);
				data[i][2] = new HTMLSimpleElement(str.getVarDescription());
				if(str.getActionName()!=null && str.getActionName().trim()!=""){
					s= str.getActionName()+" -> "+str.getActionType();
				}else{
					s=str.getActionType();
				}
				data[i][3] = new HTMLSimpleElement(s);
				if(str.getVarId()!=-1){
					s=str.getLineId()+"-"+str.getDevId()+"-"+str.getVarId();
				}else{
					s=str.getLineId()+"-"+str.getDevId();
				}
				data[i][4] = new HTMLSimpleElement(s);
			}
		}
		HTMLTable condTable = null;
		String[] hCol = {multiLanguage.getString("linetable", "line"),multiLanguage.getString("dtlview", "description")
			,multiLanguage.getString("devdetail", "descvars"),multiLanguage.getString("vardpd", "dpdmnt"),multiLanguage.getString("vardpd", "ids")};
		condTable = new HTMLTable("VarDPlist",hCol,data,false,true);
		condTable.setTableTitle(multiLanguage.getString("vardpd", "dpdlist"));
		condTable.setScreenH(us.getScreenHeight());
		condTable.setScreenW(us.getScreenWidth());
		condTable.setHeight(270);
		condTable.setWidth(888);
		condTable.setColumnSize(0, 70);
		condTable.setColumnSize(1, 170);
		condTable.setColumnSize(2, 280);
		condTable.setColumnSize(3, 150);
		condTable.setColumnSize(4, 120);
		condTable.setAlignType(new int[]{0,0,0,0,0});
		condTable.setTableId(2);
//		return condTable.getHTMLText();
		return condTable.getHTMLTextBufferNoWidthCalc().toString();
	}
    public String allVariablesDependencies(UserSession us,VarDependencyList  vdl ) throws Exception{ 
    	
    	int idsite = us.getIdSite();
        String language = us.getLanguage();  	
    	StringBuffer sb = new StringBuffer();
		String devids = us.getPropertyAndRemove ("DevIds2dlt");
		List<VarDependency> list  = vdl.getVarList(idsite,language);
		if(devids!=null &&  !devids.trim().equals("")){
			list = pickBydevids(devids.split(";"),list);
		}else{
			list = null;
		}
		int i =0;
		if(list!=null){
			for (VarDependency str : list) {
				sb.append("<tr >");
				for ( int j=0;j<=4;j++) {
					String s ="";
					if(j==0){
						s= str.getLine();
					}
					if(j==1){
						String desc = UtilityString.replaceBadChars4XML(str.getDevDescription());
						s = str.getDeviceCode()+" -> "+desc;
					}
					if(j==2){
						s= str.getVarDescription();
					}
					if(j==3){
						if(str.getActionName()!=null && str.getActionName().trim()!=""){
							s= str.getActionName()+" -> "+str.getActionType();
						}else{
							s=str.getActionType();
						}
					}
					if(j==4){
						if(str.getVarId()!=-1){
							s=str.getLineId()+"-"+str.getDevId()+"-"+str.getVarId();
						}else{
							s=str.getLineId()+"-"+str.getDevId();
						}
					}
					sb.append("<td>"+s+"</td>");
				}
				sb.append("</tr>");
				i++;
			}
		}
    	String r = sb.toString();
		return r;
    }
    List<VarDependency> pickBydevids(String[] ss,List<VarDependency> list){
    	
    	ArrayList<VarDependency> temp = new ArrayList<VarDependency>();
    	for (VarDependency vd : list) {
    		for(int i=0;i<ss.length;i++){
    			if(  vd.getDevId() == Integer.parseInt(ss[i]) ){
    				temp.add(vd);
    			}
    		}
		}
    	
    	return temp;
    }
   
    
     
    public boolean removeLine(UserSession us, int idline)
        throws Exception
    {
        int idsite = us.getIdSite();
        String language = us.getLanguage();
        LangService lang_s = LangMgr.getInstance().getLangService(language);
        
        
        boolean have = LineConfig.haveLineDependence(idsite, idline, LineConfig.ALL_CHECK, language,us);
        //RemoveState state = LineConfig.verifyLineDependence(idsite, idline, language);

        if (!have)
        {        	
            //retrieve code corrispondente alla linea da eliminare
            String sql = "select code from cfline where idsite = ? and idline = ?";
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[] { new Integer(idsite), new Integer(idline) });
            int code = Integer.parseInt(rs.get(0).get(0).toString().trim());

            //retrive idline delle linee con code > code linea eliminata
            sql = "select idline from cfline where idsite = ? and code > ? order by code";
            rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[] { new Integer(idsite), new Integer(code) });

            int[] ids = new int[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                ids[i] = ((Integer) rs.get(i).get(ID_LINE)).intValue();
            }
            
        	//Alessandro: se non ci sono dipendenze controllo se ci sono centrali abilitate per il FS
        	FSRack[] racks = FSRack.getRacks(language);
        	if (racks!=null&&racks.length!=0){
    			// spengo il plugin se avviato
        		FSManager fsManager = FSManager.getInstance();
        		if (fsManager.isRunning()) fsManager.stopFS(us.getUserName());        		
        		for(int i=0;i<racks.length;i++){
        			// se il rack corrente è nella linea da cancellare devo eliminarlo
        			if (isDeviceInLine(idsite,idline,racks[i].getId_rack())){
        				FSStatus.removeRack(racks[i].getId_rack());
        			}
        		}
        	}            

            //elimino la linea
            sql = "delete from cfline where idsite = ? and idline = ?";
            DatabaseMgr.getInstance().executeStatement(null, sql,
                new Object[] { new Integer(idsite), new Integer(idline) });

            //shifto il "code" di tutte le linee con code > code linea eliminata
            if (ids.length > 0)
            {
            	DatabaseMgr.getInstance().executeStatement(
            			"update cfline set code = (code -1) where idsite = 1 and typeprotocol='CAREL' and code>"+code+";",null);
            }

            //update device legati alla linea eliminata
            removeDevicesFromLine(idsite, idline, language);
            LineConfig.alignDeviceCode();
        }
        else
        {
//        	String str =  lang_s.getString("importctrl", "nooperation")+"\n"; 
//        	str += state.getMessagesAsString();
//        	us.setProperty("control", str);

            return false;
        }

        return true;
    }

    public void removeDevicesFromLine(int idsite, int idline, String language)
        throws Exception
    {
        int[] idsdevices = getIdsDeviceOfLine(idsite, idline);

        //elimino dati grafico
        for (int i = 0; i < idsdevices.length; i++)
        {
            GraphVariable.removeGraphVariableOfDevice(idsite, idsdevices[i],
                language);
        }

        //prima di settare idline=null in device, setto le variabili iscancelled=TRUE
        //altrimenti perdo l'idline su iddevice
        StringBuffer query = new StringBuffer();
        ArrayList<Object> params = new ArrayList<Object>();
        params.add("TRUE");
        params.add(new Timestamp(System.currentTimeMillis()));
        params.add(new Integer(idsite));
        query.append(
            "update cfvariable set iscancelled = ?, lastupdate = ? where idsite = ? and iddevice in (");

        for (int i = 0; i < (idsdevices.length - 1); i++)
        {
            params.add(new Integer(idsdevices[i]));
            query.append("?,");
        }

        params.add(new Integer(idsdevices[idsdevices.length - 1]));
        query.append("?)");
        DatabaseMgr.getInstance().executeStatement(null, query.toString(),
            params.toArray());

        //settare iscancelled le variabili in cfrelay
        String sql = "update cfrelay set iscancelled = ? where idsite = ? and idvariable in (select idvariable from cfvariable where idsite = ? and iddevice in (select iddevice from cfdevice where idsite = ? and idline = ?))";
        Object[] param = new Object[5];
        param[0] = "TRUE";
        param[1] = new Integer(idsite);
        param[2] = param[1];
        param[3] = param[1];
        param[4] = new Integer(idline);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        GroupVarBeanList.cleanGroupVar(idsite); //ripulire eventuali variabili di gruppo vuote

        //setto idline =null e is cancelled = true
        sql = "update cfdevice set iscancelled = ?, idline = ?,lastupdate = ? where idsite = ? and idline = ?";

        param = new Object[5];
        param[0] = "TRUE";
        param[1] = null;
        param[2] = new Timestamp(System.currentTimeMillis());
        param[3] = new Integer(idsite);
        param[4] = new Integer(idline);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        GroupManager groupMg = new GroupManager();

        GroupBean[] groups = new GroupListBean().retrieveAllGroupsNoGlobal(idsite,
                language);

        for (int i = 0; i < groups.length; i++)
        {
            if (groupMg.numOfDeviceOfGroup(idsite, groups[i].getGroupId()) == 0)
            {
                groupMg.removeEmptyGroup(idsite, groups[i].getGroupId());
            }
        }
    }
    
    
    
    public void removeDevicesFromLineByIddev(int idsite, int iddevice, String language) throws Exception{

    	// rimozione eventuali dipendenze dirette del device (per ora solo dal plugin FS)
    	// TODO: inserire la rimozione delle dipendenze delle variabili da settaggi e plugin
    	LineConfig.removeDeviceDependence(idsite, iddevice, LineConfig.ALL_CHECK, language);
    	
        //rimozione dati grafico
        GraphVariable.removeGraphVariableOfDevice(idsite, iddevice, language);

        //prima di settare idline=null in device, setto le variabili iscancelled=TRUE
        //altrimenti perdo l'idline su iddevice
        String sql = "update cfvariable set iscancelled = ?, lastupdate = ? where idsite = ? and iddevice = ?";
        Object[] param = new Object[4];
        param[0] = "TRUE";
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(iddevice);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        //settare iscancelled le variabili in cfrelay
        sql = "update cfrelay set iscancelled = ? where idsite = ? and idvariable in (select idvariable from cfvariable where idsite = ? and iddevice in (select iddevice from cfdevice where idsite = ?  and iddevice = ?))";
        param = new Object[5];
        param[0] = "TRUE";
        param[1] = new Integer(idsite);
        param[2] = param[1];
        param[3] = param[1];
        param[4] = new Integer(iddevice);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        //pulizia variabili di gruppo
        GroupVarBeanList.cleanGroupVar(idsite); //ripulire eventuali variabili di gruppo vuote

        //setto device iscancelled=TRUE
        sql = "update cfdevice set iscancelled = ?, idline = ?,lastupdate = ? where idsite = ? and iddevice = ?";
        param = new Object[5];
        param[0] = "TRUE";
        param[1] = null;
        param[2] = new Timestamp(System.currentTimeMillis());
        param[3] = new Integer(idsite);
        param[4] = new Integer(iddevice);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        GroupManager groupMg = new GroupManager();

        GroupBean[] groups = new GroupListBean().retrieveAllGroupsNoGlobal(idsite,
                language);

        for (int i = 0; i < groups.length; i++)
        {
            if (groupMg.numOfDeviceOfGroup(idsite, groups[i].getGroupId()) == 0)
            {
                groupMg.removeEmptyGroup(idsite, groups[i].getGroupId());
            }
        }

    
    }
    
    public boolean removeDeviceFromLineByAddress(UserSession us, int idline,
        int address) throws Exception
    {
        int idsite = us.getIdSite();
        String language = us.getLanguage();
        LangService lang_s = LangMgr.getInstance().getLangService(language);

        int iddevice = getIdDeviceOfLineByAddress(idsite, idline, address);
        VarDependencyState state = LineConfig.checkDeviceDependence(idsite, iddevice, LineConfig.ALL_CHECK, language);

        if (state.dependsOn() == false)
        {
        	// rimozione eventuali dipendenze dirette del device (per ora solo dal plugin FS)
        	// TODO: inserire la rimozione delle dipendenze delle variabili da settaggi e plugin
        	LineConfig.removeDeviceDependence(idsite, iddevice, LineConfig.ALL_CHECK, language);
        	
            //rimozione dati grafico
            GraphVariable.removeGraphVariableOfDevice(idsite, iddevice, language);

            //prima di settare idline=null in device, setto le variabili iscancelled=TRUE
            //altrimenti perdo l'idline su iddevice
            String sql = "update cfvariable set iscancelled = ?, lastupdate = ? where idsite = ? and iddevice = ?";
            Object[] param = new Object[4];
            param[0] = "TRUE";
            param[1] = new Timestamp(System.currentTimeMillis());
            param[2] = new Integer(idsite);
            param[3] = new Integer(iddevice);

            DatabaseMgr.getInstance().executeStatement(null, sql, param);

            //settare iscancelled le variabili in cfrelay
            sql = "update cfrelay set iscancelled = ? where idsite = ? and idvariable in (select idvariable from cfvariable where idsite = ? and iddevice in (select iddevice from cfdevice where idsite = ? and idline = ? and address = ?))";
            param = new Object[6];
            param[0] = "TRUE";
            param[1] = new Integer(idsite);
            param[2] = param[1];
            param[3] = param[1];
            param[4] = new Integer(idline);
            param[5] = new Integer(address);

            DatabaseMgr.getInstance().executeStatement(null, sql, param);

            //pulizia variabili di gruppo
            GroupVarBeanList.cleanGroupVar(idsite); //ripulire eventuali variabili di gruppo vuote

            //setto device iscancelled=TRUE
            sql = "update cfdevice set iscancelled = ?, idline = ?,lastupdate = ? where idsite = ? and iddevice = ?";
            param = new Object[5];
            param[0] = "TRUE";
            param[1] = null;
            param[2] = new Timestamp(System.currentTimeMillis());
            param[3] = new Integer(idsite);
            param[4] = new Integer(iddevice);

            DatabaseMgr.getInstance().executeStatement(null, sql, param);

            GroupManager groupMg = new GroupManager();

            GroupBean[] groups = new GroupListBean().retrieveAllGroupsNoGlobal(idsite,
                    language);

            for (int i = 0; i < groups.length; i++)
            {
                if (groupMg.numOfDeviceOfGroup(idsite, groups[i].getGroupId()) == 0)
                {
                    groupMg.removeEmptyGroup(idsite, groups[i].getGroupId());
                }
            }

            return true;
        }
        else
        {
        	String str =  lang_s.getString("importctrl", "nooperation")+"\n";
        	str += state.getMessagesAsString();
        	us.setProperty("control", str);

            return false;
        }
    }

    public int size()
    {
        return lineList.size();
    }

    /**
     * Check if a device is in a line
     * @param idsite Site ID
     * @param idline Line ID
     * @param iddevice ID of the device to check
     * @return true if the device is in the line, false otherwise
     * @throws DataBaseException
     */
    public boolean isDeviceInLine(int idsite, int idline, int iddevice)
        throws DataBaseException
    {
        String sql = "select iddevice from cfdevice where idsite = ? and idline = ? and iddevice = ?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idline);
        param[2] = new Integer(iddevice);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        int[] result = new int[rs.size()];

        if (result.length > 0)
        	return true;
        else
        	return false;
    }
    
    public int[] getIdsDeviceOfLine(int idsite, int idline) throws DataBaseException
{
    String sql = "select iddevice from cfdevice where idsite = ? and idline = ?";
    Object[] param = new Object[2];
    param[0] = new Integer(idsite);
    param[1] = new Integer(idline);

    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

    int[] ids = new int[rs.size()];

    for (int i = 0; i < ids.length; i++)
    {
        ids[i] = ((Integer) rs.get(i).get("iddevice")).intValue();
    }

    return ids;
}    

    public int getIdDeviceOfLineByAddress(int idsite, int idline, int address)
        throws DataBaseException
    {
        String sql = "select iddevice from cfdevice where idsite = ? and idline = ? and address = ?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idline);
        param[2] = new Integer(address);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        int id = ((Integer) rs.get(0).get("iddevice")).intValue();

        return id;
    }
    
    public static String[] getDevInfosOfLineByAddress(int idsite, int idline, int address ,String language)
    throws DataBaseException
	{
	    String sql = "select iddevice , code , description from cfdevice , cftableext where cfdevice.idsite = ? and idline = ? and address = ? " +
	    		" and cfdevice.iddevice = cftableext.tableid and cftableext.tablename = 'cfdevice' and languagecode= ?" ;
	    Object[] param = new Object[4];
	    param[0] = new Integer(idsite);
	    param[1] = new Integer(idline);
	    param[2] = new Integer(address);
	    param[3] = language;

	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
	    String id =  rs.get(0).get("iddevice").toString();
	    String code = rs.get(0).get("code").toString();
	    String description = rs.get(0).get("description").toString();
		return new String[] {id,code,description};
	}

    public DeviceBean[] getDeviceOfLine(int idsite, int idline, String lang)
        throws DataBaseException
    {
        String sql =
            "select cfdevice.*, cftableext.description from (cfdevice inner join cfline on cfline.idline = cfdevice.idline)" +
            "inner join cftableext on " +
            "cftableext.tableid=cfdevice.iddevice where cftableext.tablename='cfdevice' and cftableext.languagecode=? " +
            "and cfline.idsite = ? and cfline.idline= ? and cfdevice.iscancelled=? and cftableext.idsite = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    lang, new Integer(idsite), new Integer(idline), "FALSE",
                    new Integer(idsite)
                });

        DeviceBean[] devices = new DeviceBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            devices[i] = new DeviceBean(rs.get(i), lang);
        }

        return devices;
    }

    public Map<Integer, DeviceBean> loadDeviceByAddress(int idsite, int idline, String lang)
        throws DataBaseException
    {
        String sql =
            "select cfdevice.*, cftableext.description from (cfdevice inner join cfline on cfline.idline = cfdevice.idline)" +
            "inner join cftableext on " +
            "cftableext.tableid=cfdevice.iddevice where cftableext.tablename='cfdevice' and cftableext.languagecode=? " +
            "and cfline.idsite = ? and cfline.idline= ? and cfdevice.iscancelled='FALSE' and cfdevice.islogic = 'FALSE' and cftableext.idsite = ? order by cfdevice.address";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    lang, new Integer(idsite), new Integer(idline),
                    new Integer(idsite)
                });

        DeviceBean device = null;
        HashMap<Integer, DeviceBean> map = new HashMap<Integer, DeviceBean>();

        for (int i = 0; i < rs.size(); i++)
        {
            device = new DeviceBean(rs.get(i), lang);
            map.put(new Integer(device.getAddress()), device);
        }

        return map;
    }
    public static int getCode(String typeprotocol) throws Exception{
    	Integer code = -1;
    	if(typeprotocol.equalsIgnoreCase("CAREL")){
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
    				"select max(code) from cfline where code>="+LineBean.FIRST_CAREL+" and code<="+LineBean.LAST_CAREL);
    		if(rs!=null){
    			code = (Integer)rs.get(0).get(0) +1;
    			if(code>LineBean.LAST_CAREL)
    				return -2;
    		}
    	} 
    	if(typeprotocol.equalsIgnoreCase("MODBUS") || typeprotocol.equalsIgnoreCase("CAN")){
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
    				"select max(code) from cfline where code>="+LineBean.FIRST_MODBUS+" and code<="+LineBean.LAST_MODBUS);
    		if(rs!=null){
    			code = (Integer)rs.get(0).get(0);
    			if(code.equals(0))
    				code = LineBean.FIRST_MODBUS;
    			else
    				code++;
    			if(code>LineBean.LAST_MODBUS)
    				return -3;
    		}
    	}
    	if(typeprotocol.equalsIgnoreCase("LAN")){
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
    				"select max(code) from cfline where code>="+LineBean.FIRST_SNMP);
    		if(rs!=null){
    			code = (Integer)rs.get(0).get(0);
    			if(code.equals(0))
    				code = LineBean.FIRST_SNMP;
    			else
    				code++;
    		}
    	}
    	return code;
    }
    public String updateLine(int idsite, int idline, String comport,
        int baudrate, String protocol, String typeprotocol)
    throws Exception
    {
    	LineBean oldLine = retrieveLineById(idsite,idline);
        String sql = "update cfline set comport = ?, baudrate = ?, protocol = ?, typeprotocol = ?, lastupdate = ? where idsite = ? and idline = ?";
        Object[] param = new Object[7];
        param[0] = comport;
        param[1] = new Integer(baudrate);
        param[2] = protocol;
        param[3] = typeprotocol;
        param[4] = new Timestamp(System.currentTimeMillis());
        param[5] = new Integer(idsite);
        param[6] = new Integer(idline);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        if(oldLine.getTypeprotocol().equals(typeprotocol) && oldLine.getProtocol().equals(protocol))
        	return String.valueOf(oldLine.getCode());
        else
        {
        	int code = protocol.startsWith("LAN") ? getCode("LAN") : getCode(typeprotocol);
        	sql = "update cfline set code = ? where idsite = ? and idline = ?";
            param = new Object[3];
            param[0] = new Integer(code);
            param[1] = new Integer(idsite);
            param[2] = new Integer(idline);
            DatabaseMgr.getInstance().executeStatement(null, sql, param);
            return String.valueOf(code);
        }
    }

    public String getHTMLtable(String language, String title, int height, int width)
    	throws DataBaseException
    {
        // rows is size-2 because the line corresponding to the 'Internal IO board' and 'Supervisor'
    	// are excluded on PVPRO 2.0 and higher
    	int rows = size()-2;

        LangService lan = LangMgr.getInstance().getLangService(language);
        String[] ClickRowFunction = new String[rows];
        String[] DBLClickRowFunction = new String[rows];
        HTMLElement[][] dati = new HTMLElement[rows][];
        
        Map<Integer,Integer> rs485map = LineConfig.getSerial485COM();

        int rownum = 0;
        for (int i = 0; i < size(); i++)
        {
            LineBean tmp = new LineBean(lineList.get(i));
            
            // devices corresponding to Internal IO board and Supervisor are exluded
            // on PVPRO 2.0 and higher
            if( !(tmp.getProtocol().equals("FTD2IO") || tmp.getProtocol().equals("SUPERVISOR")) )
            {

            	dati[rownum] = new HTMLElement[6];
	            dati[rownum][0] = new HTMLSimpleElement(String.valueOf(tmp.getCode()));
	            
	            int comNum = -1;
	            if( tmp.isSerial() ) {
		            String comport = tmp.getComport();
		            comNum = Integer.parseInt(comport.replace("COM", ""));
		            if( comNum == 0 ) {
		            	dati[rownum][1] = new HTMLSimpleElement("----------");
		            }
		            else if(rs485map.containsKey(comNum))
		            {
		            	dati[rownum][1] = new HTMLSimpleElement("RS485 - "+rs485map.get(comNum));
		            }
		            else
		            {
		            	dati[rownum][1] = new HTMLSimpleElement(comport);
		            }
	            }
	            else {
	            	dati[rownum][1] = new HTMLSimpleElement(tmp.getIpAddress());
	            }
	            dati[rownum][2] = new HTMLSimpleElement(String.valueOf(tmp.getBaudrate()));
	            dati[rownum][3] = new HTMLSimpleElement(transCode(tmp));
	            dati[rownum][4] = new HTMLSimpleElement(String.valueOf(tmp.getNumberofdevice()));
	            dati[rownum][5] = new HTMLSimpleElement(String.valueOf(tmp.getIdline()));
	            
            
	            if (!tmp.getProtocol().equalsIgnoreCase("VIRTUAL"))
	            {
	            	ClickRowFunction[rownum] = String.valueOf(tmp.getIdline());
	            }
	            else
	            {
	            	ClickRowFunction[rownum] = "nop";
	            }
	            DBLClickRowFunction[rownum] = String.valueOf(tmp.getIdline());
	            
	            if( comNum == 0 )
	            	for(int k = 0; k < dati[rownum].length; k++) {
	            		String strText = dati[rownum][k].getHTMLText();
	            		dati[rownum][k] = new HTMLSimpleElement("<span style='color:#FF0000'>" + strText + "</span>");
	            	}
	            rownum++;
            }
        }

        //header tabella
        String[] headerTable = new String[6];
        headerTable[0] = lan.getString("linetable", "line");
        headerTable[1] = lan.getString("linetable", "comport") + " / " + lan.getString("linetable", "ipaddress");
        headerTable[2] = lan.getString("linetable", "baudrate");
        headerTable[3] = lan.getString("linetable", "protocol");
        headerTable[4] = lan.getString("linetable", "numdev");
        headerTable[5] = "Id";

        HTMLTable table = new HTMLTable("lines", headerTable, dati);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setColumnSize(0, 65);
        table.setColumnSize(1, 135);
        table.setColumnSize(2, 135);
        table.setColumnSize(3, 165);
        table.setColumnSize(4, 185);
        table.setColumnSize(5, 55);

        table.setSgClickRowAction("selectedLine('$1')");
        table.setSnglClickRowFunction(ClickRowFunction);
        table.setDbClickRowAction("top.frames['manager'].loadTrx('nop&folder=siteview&bo=BSiteView&type=redirect&line=$1&desc=ncode03')");
        //    "top.frames['manager'].loadTrx('nop&folder=setline&bo=BSetLine&type=click&line=$1&desc=ncode03')");
        table.setDlbClickRowFunction(DBLClickRowFunction);
        table.setAlignType(new int[] { 1, 1, 1, 1, 1, 1 });
        table.setTableTitle(title);
        table.setWidth(width);
        table.setHeight(height);

        String htmlTable = table.getHTMLText();

        return htmlTable;
    }

    private String transCode(LineBean line)
    {
        String type = line.getTypeprotocol();
        String protocol = line.getProtocol();
        String result = protBean.getName(type, protocol);

        // protocol exceptions
        if( result.length() <= 0 ) {
        	if( type.equalsIgnoreCase("JBUS") )
        		result = type + " - " + protocol + " - " + "PCGATE/USB";
            else if( type.equalsIgnoreCase("VIRTUAL") )
            	result = type;
            else
            	result = type + " - " + protocol;
        }
 
        return result;
    }

    public static int getIdMdlOfDeviceAtAddressOfLine(int idsite, int idline,
        int address) throws DataBaseException
    {
        String sql = "select iddevmdl from cfdevice where idsite=? and idline=? and address=? and iscancelled='FALSE'";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idline);
        param[2] = new Integer(address);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() > 0)
        {
            return ((Integer) rs.get(0).get("iddevmdl")).intValue();
        }
        else
        {
            return -1;
        }
    }

    public static int[] getIdMdlOfDeviceOfLine(int idsite, int idline, int nAddr)
        throws DataBaseException
    {
        String sql = "select address,iddevmdl from cfdevice where idsite=? and idline=? and iscancelled='FALSE' order by address";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idline);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        int[] ids = new int[nAddr];

        for (int i = 0; i < nAddr; i++)
        {
            ids[i] = -1;
        }

        int address = -1;
        int iddevmdl = -1;
        Record r = null;

        if (rs.size() > 0)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                r = rs.get(i);
                address = ((Integer) r.get("address")).intValue();
                iddevmdl = ((Integer) r.get("iddevmdl")).intValue();
                ids[address] = iddevmdl;
            }
        }

        return ids;
    }

    public static int retrieveNumberDevOfLine(int idsite, int idline)
        throws DataBaseException
    {
        String sql = "select count(1) from cfdevice where idsite = ? and idline = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idline) });

        return ((Integer) rs.get(0).get("count")).intValue();
    }

    public void setScreenH(int height)
    {
        this.screenh = height;
    }

    public void setScreenW(int width)
    {
        this.screenw = width;
    }
}
