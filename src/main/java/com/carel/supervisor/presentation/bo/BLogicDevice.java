package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.bean.LogicDeviceBeanList;
import com.carel.supervisor.presentation.bean.LogicVariableBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;


public class BLogicDevice extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    //Costanti per i tab 1 e 2
    public final static String ADD = "add";
    public final static String DELETE = "del";
    public final static String MODIFY = "mod";

    //Costanti per i dispositivi logici tab 1
    public final static String STATUS_ALL_ACTION_1 = "statusAllAction1"; //Carica la lista delle variabili dalla riga selezionata dalla tabella master Logic Device 
    public final static String STATUS_MODIFY_ACTION_INTEGER = "statusModifyActionInteger"; //Carica la lista delle variabili dalla riga selezionata dalla tabella master Logic Device 
    public final static String STATUS_ADD_ACTION_INTEGER = "statusAddActionInteger"; //Carica la lista delle variabili del dipsositivo fisico selezionato dai controlli interni alla pagina

    //Costanti per le variabili logiche tab 2 
    public final static String STATUS_ADD_ACTION_VARIABLE_TYPE = "statusAddActionVariableType";
    public final static String STATUS_MOD_ACTION_VARIABLE_TYPE = "statusModActionVariableType";

    //public final static String  
    //public final static String 
    public BLogicDevice(String l)
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
        p.put("tab1name", "initialize();resizeTableLogicDevice();");
        p.put("tab2name", "initialize();resizeTableVarLogicDevice();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "logicdevice.js;resizeTable.js;keyboard.js;");
        p.put("tab2name", "logicvariable.js;resizeTable.js;keyboard.js;");

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

            if (tabName.equals("tab2name"))
            {
                this.execTab2(us, prop);

                break;
            }

            break;
        } //forswitch
    }

    private void execTab1(UserSession us, Properties prop)
        throws Exception
    {
        String actionType = (String) prop.get("action");

        for (;;)
        {
            if (actionType.equals(ADD))
            {
                LogicDeviceBeanList.addDevice(us, prop);
                us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
                DirectorMgr.getInstance().mustRestart();
                break;
            } //if case ADD

            if (actionType.equals(DELETE))
            {
                boolean candelete = LogicDeviceBeanList.deleteDevice(us,new Integer((String) prop.getProperty("idDevice")).intValue());
                if(candelete)
                {
                	us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
                	DirectorMgr.getInstance().mustRestart();
                }
                break;
            } //if case DELETE 

            if (actionType.equals(MODIFY))
            {
            	
                boolean canmodify = LogicDeviceBeanList.modifyDevice(us, prop);
                if(canmodify)
                {
                	us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
                	DirectorMgr.getInstance().mustRestart();
                }
                break;
            } //if case MODIFY

            if (actionType.equals(STATUS_ALL_ACTION_1) ||
                    actionType.equals(STATUS_MODIFY_ACTION_INTEGER) ||
                    actionType.equals(STATUS_ADD_ACTION_INTEGER))
            {
                us.getCurrentUserTransaction().setLogicDeviceParameter(prop);

                break;
            } //if case UPDATE_PAGE_MODE_1 || UPDATE_PAGE_MODE_2 || UPDATE_PAGE_MODE_2

            //defaultcase:
            break;
        } //forswitch

    } //execTab1

    private void execTab2(UserSession us, Properties prop)
        throws Exception
    {
        String actionType = (String) prop.get("action");

        for (;;)
        {
            if (actionType.equals(ADD))
            {
                LogicVariableBeanList.addVariable(us, prop);

                break;
            } //if case ADD

            if (actionType.equals(DELETE))
            {
                LogicVariableBeanList.deleteVariable(us, prop);

                break;
            } //if case DELETE 

            if (actionType.equals(MODIFY))
            {
                LogicVariableBeanList.modifyVariable(us, prop);
                //add by Kevin, if the logic variable belongs to a device, then must restart the engine
                if(!"0".equals(prop.getProperty("variabledeviceid")))
                	DirectorMgr.getInstance().mustRestart();
                break;
            } //if case MODIFY

            if (actionType.equals(STATUS_ADD_ACTION_VARIABLE_TYPE) ||
                    actionType.equals(STATUS_MOD_ACTION_VARIABLE_TYPE))
            {
                us.getCurrentUserTransaction().setLogicVariableParameter(prop);

                break;
            } //if case STATUS_ADD_ACTION_VARIABLE_TYPE

            //defaultcase:
            break;
        } //forswitch

        //      aggiornamento sessione
        us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
    } //execTab2

	@Override
	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		LangService l = LangMgr.getInstance().getLangService(us.getLanguage());
		String toReturn = "";
		if ("tab1name".equals(tabName.trim()))
		{
			if ("chkdeplogicdev".equalsIgnoreCase(prop.getProperty("cmd")))
			{
				try{
					toReturn = "<MSG>OK</MSG>";
					StringBuffer sql = new StringBuffer(); 
					sql.append("SELECT ");
					sql.append("a.idvariable, a.iddevice, a.functioncode, a.code, a.idsite, ");
					sql.append("cffunction.idfunction, cffunction.parameters, cfcondition.idcondition, cfcondition.condcode ");
					sql.append("FROM ");
					sql.append("cfvariable AS a, cffunction, cfvariable AS b, cfvarcondition, cfcondition ");
					sql.append("WHERE a.iddevice=? AND ");
					sql.append("a.iscancelled='FALSE' AND ");
					sql.append("cffunction.parameters LIKE '%'||a.idvariable||'%' AND ");
					sql.append("b.functioncode=cffunction.idfunction AND ");
					sql.append("cfvarcondition.idvariable = b.idvariable AND ");
					sql.append("cfcondition.idcondition=cfvarcondition.idcondition AND ");
                    //Start alarmP 2007/06/27 Fixing 
                    sql.append("cfcondition.condType!=?");
                    //End 
					//Object[] parms = {new Integer(prop.getProperty("iddev"))};
                    Object[] parms = {new Integer(prop.getProperty("iddev")),"P"};
					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), parms);
					if(rs.size()>0)
					{
						toReturn="<MSG>";
	                    toReturn += l.getString("control", "condition2");
	                    String logvar = DatabaseMgr.getInstance().executeQuery(null, 
	        	                    	"select description from cftableext where idsite=1 and languagecode=? and tablename=? and tableid=?",
	        	                    	new Object[]{us.getLanguage(), "cfvariable", rs.get(0).get("idvariable")}).get(0).get("description").toString();
	                    String logdev = DatabaseMgr.getInstance().executeQuery(null, 
	        	                    	"select description from cftableext where idsite=1 and languagecode=? and tablename=? and tableid=?",
	        	                    	new Object[]{us.getLanguage(), "cfdevice", rs.get(0).get("iddevice")}).get(0).get("description").toString();
	                    String cond = rs.get(0).get("condcode").toString();
	                    toReturn = toReturn.replace("$1", logvar); //logvar
	                    toReturn = toReturn.replace("$2", logdev);//logdev
	                    toReturn = toReturn.replace("$3", cond); //cond
	                    toReturn += "</MSG>";
					}
				} catch (Exception e)
				{
					toReturn = "<MSG>BAD</MSG>";
					LoggerMgr.getLogger(this.getClass()).error("submitVars", e);
				}
			}
		}
		return toReturn;
	}
} //Class BLogicDevice
