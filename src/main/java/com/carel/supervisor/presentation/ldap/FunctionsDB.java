package com.carel.supervisor.presentation.ldap;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.presentation.bean.ProfileBean;

import java.sql.Timestamp;


public class FunctionsDB
{
    private final static String INSERT = "insert into profilemaps values (?,?,?,?,?,?,?,?)";
    private final static String DELETE = "delete from profilemaps where idprofile = ? and code = ?";

    private FunctionsDB()
    {
    }

    /*
    //configurazione sito 
    public static void noAccessConfiguration(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_SITE_CONF);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        //    	Tolgo Configurazione sito -> nuova linea
        //    	Tolgo Configurazione sito -> pulsanti 
        Object[][] values = new Object[][]
            {
                { idprofile, code, "siteview", "", "", "", "", now },
                { idprofile, code, "logicdevice", "", "", "", "", now },
                { idprofile, code, "devloglist", "", "", "", "", now },
                { idprofile, code, "setline", "", "", "", "", now },
                { idprofile, code, "devdetail", "", "", "", "", now },
                { idprofile, code, "groupview", "", "", "", "", now },
                { idprofile, code, "setgroup", "", "", "", "", now },
                { idprofile, code, "setarea", "", "", "", "", now },
                { idprofile, code, "areaview", "", "", "", "", now },
                { idprofile, code, "setio", "", "", "", "", now },
                { idprofile, code, "datatransfer", "", "", "", "", now },
                { idprofile, code, "plugin", "", "", "", "", now },
                
                //Profilatura Plugin Controllo Parametri
                { idprofile, code, "parameters", "tab3name", "", "", "", now },
                { idprofile, code, "parameters", "tab4name", "", "", "", now },
                { idprofile, code, "parameters", "tab5name", "", "", "", now },
                { idprofile, code, "parameters", "tab1name", "Photo", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeConfiguration(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_SITE_CONF) });
    }

    public static void activeReadOnlyConfiguration(Integer idprofile)
        throws Exception
    {
        //Tutte le widget read-only
        //Note read-only
        //Da Configurazione sito vado a Visualizza linea
        //Visualizza linea ->Note readonly
        //Configurazione dispositivo ->
        //     tolgo pulsanti
        //     read-only
        //su tutti e 5 i tab
        Integer code = new Integer(ProfileBean.FUNCT_SITE_CONF);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        //    	Tolgo Configurazione sito -> nuova linea
        //    	Tolgo Configurazione sito -> pulsanti 
        Object[][] values = new Object[][]
            {
                { idprofile, code, "siteview", "tab2name", "", "", "", now },
                { idprofile, code, "siteview", "tab1name", "Save", "", "", now },
                { idprofile, code, "siteview", "tab1name", "Rem", "", "", now },
                { idprofile, code, "siteview", "tab3name", "Add", "", "", now },
                { idprofile, code, "siteview", "tab3name", "Rem", "", "", now },
                { idprofile, code, "siteview", "tab3name", "Save", "", "", now },
                { idprofile, code, "siteview", "tab3name", "", "", "P", now },
                { idprofile, code, "setline", "tab2name", "", "", "", now },
                { idprofile, code, "setline", "tab3name", "Add", "", "", now },
                { idprofile, code, "setline", "tab3name", "Rem", "", "", now },
                { idprofile, code, "setline", "tab3name", "Save", "", "", now },
                { idprofile, code, "setline", "tab3name", "", "", "P", now },
                { idprofile, code, "devdetail", "tab1name", "Save", "", "", now },
                { idprofile, code, "devdetail", "tab2name", "Save", "", "", now },
                { idprofile, code, "devdetail", "tab3name", "Save", "", "", now },
                { idprofile, code, "devdetail", "tab4name", "Save", "", "", now },
                { idprofile, code, "devdetail", "tab5name", "Add", "", "", now },
                { idprofile, code, "devdetail", "tab5name", "Rem", "", "", now },
                { idprofile, code, "devdetail", "tab5name", "Save", "", "", now },
                { idprofile, code, "devdetail", "tab5name", "Save", "", "", now },
                { idprofile, code, "devdetail", "tab5name", "", "", "P", now },
                { idprofile, code, "siteview", "tab1name", "", "", "P", now },
                { idprofile, code, "siteview", "tab3name", "", "", "P", now },
                { idprofile, code, "setline", "tab3name", "", "", "P", now },
                { idprofile, code, "setline", "tab3name", "", "", "P", now },
                { idprofile, code, "logicdevice", "", "", "", "", now },
                { idprofile, code, "devdetail", "tab1name", "", "", "P", now },
                { idprofile, code, "devdetail", "tab2name", "", "", "P", now },
                { idprofile, code, "devdetail", "tab3name", "", "", "P", now },
                { idprofile, code, "devdetail", "tab4name", "", "", "P", now },
                { idprofile, code, "devdetail", "tab5name", "", "", "P", now },
                { idprofile, code, "groupview", "tab2name", "", "", "", now },
                { idprofile, code, "groupview", "tab1name", "Rem", "", "", now },
                { idprofile, code, "setgroup", "tab1name", "Save", "", "", now },
                { idprofile, code, "setgroup", "tab2name", "Add", "", "", now },
                { idprofile, code, "setgroup", "tab2name", "Rem", "", "", now },
                { idprofile, code, "setgroup", "tab2name", "Save", "", "", now },
                { idprofile, code, "setgroup", "tab3name", "Add", "", "", now },
                { idprofile, code, "setgroup", "tab3name", "Rem", "", "", now },
                { idprofile, code, "setgroup", "tab3name", "Save", "", "", now },
                { idprofile, code, "setgroup", "tab4name", "Add", "", "", now },
                { idprofile, code, "setgroup", "tab4name", "Rem", "", "", now },
                { idprofile, code, "setgroup", "tab4name", "Save", "", "", now },
                { idprofile, code, "setgroup", "tab4name", "", "", "P", now },
                { idprofile, code, "setgroup", "tab1name", "", "", "P", now },
                { idprofile, code, "setgroup", "tab2name", "", "", "P", now },
                { idprofile, code, "setgroup", "tab3name", "", "", "P", now },
                { idprofile, code, "setgroup", "tab4name", "", "", "P", now },
                { idprofile, code, "areaview", "tab2name", "", "", "", now },
                { idprofile, code, "areaview", "tab1name", "Rem", "", "", now },
                { idprofile, code, "setarea", "tab1name", "Save", "", "", now },
                { idprofile, code, "setarea", "tab2name", "Add", "", "", now },
                { idprofile, code, "setarea", "tab2name", "Rem", "", "", now },
                { idprofile, code, "setarea", "tab2name", "Save", "", "", now },
                { idprofile, code, "setarea", "tab2name", "", "", "P", now },
                { idprofile, code, "setarea", "tab1name", "", "", "P", now },
                { idprofile, code, "setio", "tab1name", "SaveConfIO", "", "", now },
                { idprofile, code, "setio", "tab2name", "SaveConfIO", "", "", now },
                { idprofile, code, "setio", "tab3name", "SaveConfIO", "", "", now },
                { idprofile, code, "setio", "tab4name", "SaveConfIO", "", "", now },
                { idprofile, code, "setio", "tab5name", "SaveConfIO", "", "", now },
                { idprofile, code, "setio", "tab6name", "SaveConfIO", "", "", now },
                { idprofile, code, "datatransfer", "", "", "", "", now },
                { idprofile, code, "plugin", "", "", "", "", now },

                //Profilatura Plugin Controllo Parametri                
                { idprofile, code, "parameters", "tab3name", "", "", "", now },
                { idprofile, code, "parameters", "tab4name", "", "", "", now },
                { idprofile, code, "parameters", "tab5name", "", "", "", now },
                { idprofile, code, "parameters", "tab1name", "Photo", "", "", now }

            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    //acknowledge allarmi
    public static void noAccessAck(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_ALARM_ACK);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "alrview", "tab1name", "Add", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
        values = new Object[][]
			{
			   { idprofile, code, "alrglb", "tab1name", "ackall", "", "", now }
			};
        
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeAck(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_ALARM_ACK) });
    }

    //cancellazione azioni 
    public static void noAccessCancel(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_ALARM_CANC);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "alrview", "tab1name", "Rem", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
        values = new Object[][]
		{
		   { idprofile, code, "alrglb", "tab1name", "delete_all", "", "", now }
		};
      
       DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeCancel(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_ALARM_CANC) });
    }

    //reset allarmi 
    public static void noAccessReset(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_ALARM_RESET);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "alrview", "tab1name", "Save", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
        values = new Object[][]
  		{
  		   { idprofile, code, "alrglb", "tab1name", "reset_all", "", "", now }
  		};
        
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeReset(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_ALARM_RESET) });
    }

    //configurazione report
    public static void noAccessReportConf(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_REPORT_CONF);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "report", "tab2name", "", "", "", now },
                { idprofile, code, "hsprint", "tab1name", "Del", "", "", now },
                { idprofile, code, "hsreport", "tab1name", "Del", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeReportConf(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_REPORT_CONF) });
    }

    //stampa report
    public static void noAccessReportPrint(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_REPORT_PRINT);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "report", "", "", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeReportPrint(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_REPORT_PRINT) });
    }

    //  pagina di sistema: 07 --simon modify for [reboot]
    public static void noManualSystem(Integer idprofile,int reboot)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_SYSTEM_PAGE);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(reboot==ProfileBean.PERMISSION_NONE){
        	//do not show page
	        Object[][] values = new Object[][]
	            {
	                { idprofile, code, "mgr", "", "", "", "", now }
	            };
	        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
        }else if(reboot==ProfileBean.PERMISSION_READ_WRITE){
        	//just only show [restart] button in page
        	Object[][] values = new Object[][]
            {
        			{ idprofile, code, "mgr", "tab1name", "", "", "", now },
        			{ idprofile, code, "mgr", "tab3name", "", "", "", now },
        			{ idprofile, code, "mgr", "tab4name", "", "", "", now },
        			{ idprofile, code, "mgr", "tab5name", "", "", "", now },
        			{ idprofile, code, "mgr", "tab6name", "", "", "", now },
        			{ idprofile, code, "mgr", "tab7name", "", "", "", now },
        	};
        	DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
        }
    }

    public static void activeSystem(Integer idprofile,int reboot)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_SYSTEM_PAGE) });
    }

    public static void activeReadOnlySystem(Integer idprofile,int reboot)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_SYSTEM_PAGE);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(reboot==ProfileBean.PERMISSION_NONE){
        	//do not show page
            Object[][] values = new Object[][]
	             {
	                 { idprofile, code, "mgr", "tab1name", "sendRegistration", "", "", now },
	                 { idprofile, code, "mgr", "tab2name", "", "", "", now },
	                 { idprofile, code, "mgr", "tab3name", "", "", "", now },
	                 { idprofile, code, "mgr", "tab5name", "sendGuardVars", "", "", now },
	                 { idprofile, code, "mgr", "tab6name", "sendGuardConf", "", "", now },
	                 { idprofile, code, "mgr", "tab7name", "sendRegPlugin", "", "", now },
	             };
	
	         DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
        }else if(reboot==ProfileBean.PERMISSION_READ_WRITE){
        	//just only show [restart] button in page
            Object[][] values = new Object[][]
	             {
	                 { idprofile, code, "mgr", "tab1name", "sendRegistration", "", "", now },
	                 //{ idprofile, code, "mgr", "tab2name", "", "", "", now },
	                 { idprofile, code, "mgr", "tab3name", "", "", "", now },
	                 { idprofile, code, "mgr", "tab5name", "sendGuardVars", "", "", now },
	                 { idprofile, code, "mgr", "tab6name", "sendGuardConf", "", "", now },
	                 { idprofile, code, "mgr", "tab7name", "sendRegPlugin", "", "", now },
	             };
	
	         DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
        }
    }

    //accesso HACCP code:8
    public static void noAccessHACCP(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_HACCP);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "grpview", "tab4name", "", "", "", now },
                { idprofile, code, "dtlview", "tab4name", "", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeHACCP(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_HACCP) });
    }

    //accesso storico code:09
    public static void noAccessHistorical(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_HISTORICAL);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "grpview", "tab5name", "", "", "", now },
                { idprofile, code, "dtlview", "tab5name", "", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeHistorical(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_HISTORICAL) });
    }

    //  accesso configurazione grafico  
    public static void noAccessConfGraph(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_CONF_GRAPH);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "grpview", "tab6name", "", "", "", now },
                { idprofile, code, "dtlview", "tab6name", "", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeConfGraph(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_CONF_GRAPH) });
    }

    //  accesso pagina gestione utenti code: 10
    public static void activeUserMng(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_USER_MNG) });
    }

    public static void noAccessUserMng(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_USER_MNG);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "setldap", "", "", "", "", now },
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }
	
	*/
    
    //GESTIONE PROFILATURA  GRUPPI
    public static void disableGroup(Integer idprofile, Integer idsite,
        Integer idarea, Integer idgroup) throws DataBaseException
    {
        String sql = "insert into profilegroups values (?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { idprofile, idsite, idarea, idgroup });
    }
    
    

    public static void newLocalProfile(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(-1); //E' tecnico
        Timestamp now = new Timestamp(System.currentTimeMillis());

        //    	Tolgo Configurazione sito -> nuova linea
        //    	Tolgo Configurazione sito -> pulsanti 
        Object[][] values = new Object[][]
            {
                { idprofile, code, "r_siteaccess", "", "", "", "", now },
                { idprofile, code, "r_alrevnsearch", "", "", "", "", now },
                { idprofile, code, "r_sitelist", "", "", "", "", now },
                { idprofile, code, "r_datatransfer", "", "", "", "", now },
                { idprofile, code, "heartbeat", "", "", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }
    

    public static void removeLocalProfile(Integer idprofile)
        throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(-1) });
    }
    
    /*

    //  Gestione note
    public static void noNote(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_NOTE);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "note", "", "", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeNote(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_NOTE) });
    }

    public static void activeReadOnlyNote(Integer idprofile)
        throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_NOTE);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "note", "note", "Add", "", "", now },
                { idprofile, code, "note", "note", "Rem", "", "", now },
                { idprofile, code, "note", "note", "Save", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }
    
    

    //parametri servizio
    public static void activeReadOnlyServiceParam(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_SERV_PARAM);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                
                { idprofile, code, "alrsched", "tab1name", "Add", "", "", now },
                { idprofile, code, "alrsched", "tab1name", "Rem", "", "", now },
                { idprofile, code, "alrsched", "tab1name", "Save", "", "", now },
                { idprofile, code, "alrsched", "tab2name", "Add", "", "", now },
                { idprofile, code, "alrsched", "tab2name", "Rem", "", "", now },
                { idprofile, code, "alrsched", "tab2name", "Save", "", "", now },
                { idprofile, code, "alrsched", "tab3name", "Add", "", "", now },
                { idprofile, code, "alrsched", "tab3name", "Rem", "", "", now },
                { idprofile, code, "alrsched", "tab3name", "Save", "", "", now },
                { idprofile, code, "alrsched", "tab4name", "Add", "", "", now },
                { idprofile, code, "alrsched", "tab4name", "Rem", "", "", now },
                { idprofile, code, "alrsched", "tab4name", "Save", "", "", now },
                { idprofile, code, "alrsched", "tab5name", "Add", "", "", now },
                { idprofile, code, "alrsched", "tab5name", "Rem", "", "", now },
                { idprofile, code, "alrsched", "tab5name", "Save", "", "", now },
                { idprofile, code, "actsched", "tab1name", "Add", "", "", now },
                { idprofile, code, "actsched", "tab1name", "Rem", "", "", now },
                { idprofile, code, "actsched", "tab1name", "Save", "", "", now },
                { idprofile, code, "actsched", "tab2name", "Add", "", "", now },
                { idprofile, code, "actsched", "tab2name", "Rem", "", "", now },
                { idprofile, code, "actsched", "tab2name", "Save", "", "", now },
                { idprofile, code, "actsched", "tab3name", "Add", "", "", now },
                { idprofile, code, "actsched", "tab3name", "Rem", "", "", now },
                { idprofile, code, "actsched", "tab3name", "Save", "", "", now },
                { idprofile, code, "setaction", "tab1name", "Save", "", "", now },
                { idprofile, code, "setaction", "tab2name", "Save", "", "", now },
                { idprofile, code, "setaction", "tab3name", "Save", "", "", now },
                { idprofile, code, "setaction", "tab4name", "Save", "", "", now },
                { idprofile, code, "setaction", "tab5name", "Save", "", "", now },
                { idprofile, code, "setaction", "tab6name", "Save", "", "", now },
                { idprofile, code, "setaction", "tab7name", "Save", "", "", now },
                { idprofile, code, "setaction2", "tab1name", "Save", "", "", now },
                { idprofile, code, "setaction2", "tab2name", "Save", "", "", now },
                { idprofile, code, "setaction2", "tab3name", "Save", "", "", now },
                { idprofile, code, "setaction2", "tab4name", "Save", "", "", now },
                { idprofile, code, "setaction2", "tab5name", "Save", "", "", now },
                { idprofile, code, "setaction2", "tab6name", "Save", "", "", now },
                { idprofile, code, "setaction2", "tab7name", "Save", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeServiceParam(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_SERV_PARAM) });
    }
    
    //gestione periferiche I/O  OK
    public static void noAccessIO(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_IO_MGR);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][]
            {
                { idprofile, code, "testio", "", "", "", "", now },
                { idprofile, code, "relaymgr", "", "", "", "", now }
            };

        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void activeIO(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE,
            new Object[] { idprofile, new Integer(ProfileBean.FUNCT_IO_MGR) });
    }

    // modulo Kpi:
    public static void activeKpi(Integer idprofile) throws Exception
	{
		DatabaseMgr.getInstance().executeStatement(null, DELETE, new Object[] { idprofile, new Integer(ProfileBean.FUNCT_CONF_KPI) });
	}

	public static void activeReadOnlyKpi(Integer idprofile) throws Exception
	{
		Integer code = new Integer(ProfileBean.FUNCT_CONF_KPI);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Object[][] values = new Object[][] {
				{ idprofile, code, "kpi", "tab2name", "", "", "", now },
				{ idprofile, code, "kpi", "tab3name", "", "", "", now }, };
		DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
	}

	public static void noKpi(Integer idprofile) throws Exception
	{
		Integer code = new Integer(ProfileBean.FUNCT_CONF_KPI);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Object[][] values = new Object[][] { { idprofile, code, "kpi", "", "", "", "", now }, };
		DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
	}
    
	// modulo AC:
	public static void activeAC(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE, new Object[] { idprofile, new Integer(ProfileBean.FUNCT_CONF_AC) });
    }

    public static void activeReadOnlyAC(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_CONF_AC);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][] {
                { idprofile, code, "ac", "tab2name", "", "", "", now },
                { idprofile, code, "ac", "tab3name", "", "", "", now },
                { idprofile, code, "ac", "tab4name", "", "", "", now }, };
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void noAC(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_CONF_AC);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][] { { idprofile, code, "ac", "", "", "", "", now }, };
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }
    
    // modulo LightNight:
    public static void activeLightNight(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE, new Object[] { idprofile, new Integer(ProfileBean.FUNCT_CONF_LIGHTNIGHT) });
    }

    public static void activeReadOnlyLightNight(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_CONF_LIGHTNIGHT);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][] {
                { idprofile, code, "lucinotte", "tab2name", "", "", "", now },
                { idprofile, code, "lucinotte", "tab3name", "", "", "", now },
                { idprofile, code, "lucinotte", "tab4name", "", "", "", now },
                { idprofile, code, "lucinotte", "tab5name", "", "", "", now },
                { idprofile, code, "lucinotte", "tab6name", "", "", "", now }, };
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void noLightNight(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_CONF_LIGHTNIGHT);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][] { { idprofile, code, "lucinotte", "", "", "", "", now }, };
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }
    
 // Booklet:
    public static void activeBooklet(Integer idprofile) throws Exception
    {
        DatabaseMgr.getInstance().executeStatement(null, DELETE, new Object[] { idprofile, new Integer(ProfileBean.FUNCT_BOOKLET) });
    }

    public static void activeReadOnlyBooklet(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_BOOKLET);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][] {
                { idprofile, code, "booklet", "tab1name", "LoadDefault", "", "", now },
                { idprofile, code, "booklet", "tab1name", "Save", "", "", now },
                };
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }

    public static void noBooklet(Integer idprofile) throws Exception
    {
        Integer code = new Integer(ProfileBean.FUNCT_BOOKLET);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Object[][] values = new Object[][] { { idprofile, code, "booklet", "", "", "", "", now }, };
        DatabaseMgr.getInstance().executeMultiStatement(null, INSERT, values);
    }
    
    */
    
    /*
	public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();

        activeReadOnlyConfiguration(new Integer(0));
    }
    */
}
