package com.carel.supervisor.presentation.bo.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.assistance.GuardianConfig;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;
import com.carel.supervisor.presentation.session.UserSession;


public class GuardianHelper
{
    public GuardianHelper()
    {
    }

    public static String createCombo(String language, int iddev)
        throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        // sql query excludes 'Internal IO' device
        // Nicola Compagno 24032010
        String sql = "select cfdevice.iddevice, cftableext.description " +
            " from cfdevice inner join cftableext on cftableext.tableid=cfdevice.iddevice and" +
            " cftableext.tablename='cfdevice' and cftableext.idsite=1 and cftableext.languagecode = ?" +
            " and cfdevice.iscancelled='FALSE' and cfdevice.code != '-1.000'";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language });

        Record record = null;
        buffer.append(
            "<select id='combo' name='combo' onchange='reloadGuardian();' class='standardTxt'>");
        buffer.append("<option value='0'>-------------</option>");

        Integer id = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            buffer.append("<option ");

            id = (Integer) record.get(0);

            if (id.intValue() == iddev)
            {
                buffer.append("selected");
            }

            buffer.append(" value='");
            buffer.append(id);
            buffer.append("'>");
            buffer.append(record.get(1));
            buffer.append("</option>");
        }

        buffer.append("</select>");

        return buffer.toString();
    }
    public static String getOptionList(String language, int iddev, UserSession us)
    throws Exception
    {
    	String sql = "select cfvariable.idvariable, cftableext.description " +
        " from cfvariable inner join cftableext on cftableext.tableid=cfvariable.idvariable and" +
        " cftableext.tablename='cfvariable' and cftableext.idsite=1 and cftableext.languagecode = ?" +
        " and cfvariable.iscancelled='FALSE' and cfvariable.iddevice=? and cfvariable.type=2" +
        " and cfvariable.idhsvariable is not null order by cftableext.description";
	    RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
	            new Object[] { language, new Integer(iddev) });
	    StringBuffer optionList = new StringBuffer();
	    Record record = null;
	    for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            optionList.append("<option class='standardTxt' value=\"" + String.valueOf(record.get(0)) + "\" >" +
            		(String) record.get(1) + "</option>\n");
        }
	    return optionList.toString();
    }
    public static String getOptionListXML(String language, int iddev, UserSession us)
    throws Exception
    {
    	String sql = "select cfvariable.idvariable, cftableext.description " +
        " from cfvariable inner join cftableext on cftableext.tableid=cfvariable.idvariable and" +
        " cftableext.tablename='cfvariable' and cftableext.idsite=1 and cftableext.languagecode = ?" +
        " and cfvariable.iscancelled='FALSE' and cfvariable.iddevice=? and cfvariable.type=2" +
        //2011-4-5, Kevin Ge, only show read only variables in "verify probe variation"
        " and cfvariable.readwrite='1' "+
        //--
        " and cfvariable.idhsvariable is not null order by cftableext.description";
	    RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
	            new Object[] { language, new Integer(iddev) });
	    StringBuffer optionList = new StringBuffer();
	    Record record = null;
	    for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            optionList.append("<var><v><![CDATA["+String.valueOf(record.get(0))+"]]></v><s><![CDATA["+(String) record.get(1)+"]]></s></var>");
        }
	    return optionList.toString();
    }
    public static String getOptionListConf(String language,int iddev,UserSession us)
    throws Exception
    {
    	String sql = "select cfvarguardian.idvariable, c1.description as desc1, c2.description as desc2 from cfvarguardian," +
        "cftableext as c1,cftableext as c2 where c1.tableid=cfvarguardian.idvariable and " +
        "c1.tablename='cfvariable' and c1.idsite=1 and c1.languagecode = ? and " +
        "c2.tablename='cfdevice' and c2.idsite=1 and c2.languagecode = ? and " +
        "c2.tableid=cfvarguardian.iddevice";
    	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
            new Object[] { language, language });
    	StringBuffer optionListConf = new StringBuffer();
	    Record record = null;
	    for (int i = 0; i < recordset.size(); i++)
        {
	    	record = recordset.get(i);
            optionListConf.append("<option class='standardTxt' value=\"" + String.valueOf(record.get(0)) + "\" >" +
            		(String) record.get(2) + "->" +(String) record.get(1) + "</option>\n");
        }
	    return optionListConf.toString();
    }
    public static String getOptionListConfReload(String language, int iddev, String params, UserSession us)
    throws Exception
    {
    	VarphyBeanList varlist = new VarphyBeanList();
	    VarphyBeanList.clearMap();
	    DeviceListBean devices = new DeviceListBean(1,language);
		String dev_desc = "";
		StringBuffer optionListConfReload = new StringBuffer();
		if (!params.equals(""))
		{	
			String[] s_vars = params.split(",");
			int[] ids_vars = new int[s_vars.length];
			for (int i=0;i<s_vars.length;i++)
			{
				ids_vars[i] = Integer.parseInt(s_vars[i]);
			}		
			varlist.getListVarByIds(1,language,ids_vars);
			for (int i=0;i<ids_vars.length;i++)
			{
				VarphyBean aux = varlist.getVarById(ids_vars[i]);   //prevedere il caso in cui la variabile non venga trovata
				if (aux!=null)
				{
					dev_desc = devices.getDevice(aux.getDevice().intValue()).getDescription();
					optionListConfReload.append("<option class='standardTxt' value=\"" + String.valueOf(aux.getId()) + "\" >" +
					dev_desc+" -> "+aux.getShortDescription() + "</option>\n");
				}
			}
		}
		return optionListConfReload.toString();
    }
    public static String getListBox(String language, int iddev, UserSession us)
        throws Exception
    {
        String sql = "select cfvariable.idvariable, cftableext.description " +
            " from cfvariable inner join cftableext on cftableext.tableid=cfvariable.idvariable and" +
            " cftableext.tablename='cfvariable' and cftableext.idsite=1 and cftableext.languagecode = ?" +
            " and cfvariable.iscancelled='FALSE' and cfvariable.iddevice=? and cfvariable.type=2" +
            " and cfvariable.idhsvariable is not null";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, new Integer(iddev) });

        List var1 = new ArrayList();
        List var2 = new ArrayList();

        ListBoxElement tmp = null;

        Record record = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            tmp = new ListBoxElement((String) record.get(1),
                    String.valueOf(record.get(0)));
            var1.add(tmp);
        }

        sql = "select cfvarguardian.idvariable, c1.description as desc1, c2.description as desc2 from cfvarguardian," +
            "cftableext as c1,cftableext as c2 where c1.tableid=cfvarguardian.idvariable and " +
            "c1.tablename='cfvariable' and c1.idsite=1 and c1.languagecode = ? and " +
            "c2.tablename='cfdevice' and c2.idsite=1 and c2.languagecode = ? and " +
            "c2.tableid=cfvarguardian.iddevice";
        recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, language });

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            tmp = new ListBoxElement((String) record.get(2) + "->" +
                    (String) record.get(1), String.valueOf(record.get(0)));
            var2.add(tmp);
        }

        DblListBox guardianlist = new DblListBox(var1, var2,false,true,true,"combo",true);
        guardianlist.setScreenH(us.getScreenHeight());
        guardianlist.setScreenW(us.getScreenWidth());
        //guardianlist.setSrcButton2("images/dbllistbox/delete_on.png");
        guardianlist.setIdlistbox("variable");
        //guardianlist.setFncButton1("to2notRemove1(variable1);return false;");
        //guardianlist.setFncButton2("to1Rem(variable2);return false;");
        
        guardianlist.setLeftRowsListBox(100);
        guardianlist.setRightRowsListBox(100);
        guardianlist.setWidthListBox(400);

        LangService lan = LangMgr.getInstance().getLangService(language);

        guardianlist.setHeaderTable1(lan.getString("guardian", "header1"));
        guardianlist.setHeaderTable2(lan.getString("guardian", "header2"));

        return guardianlist.getHtmlDblListBox();
    }
    
    public static String getListBoxReload(String language, int iddev, String params, UserSession us)
    throws Exception
	{
	    String sql = "select cfvariable.idvariable, cftableext.description " +
	        " from cfvariable inner join cftableext on cftableext.tableid=cfvariable.idvariable and" +
	        " cftableext.tablename='cfvariable' and cftableext.idsite=1 and cftableext.languagecode = ?" +
	        " and cfvariable.iscancelled='FALSE' and cfvariable.iddevice=? and cfvariable.type=2" +
	        " and cfvariable.idhsvariable is not null";
	    RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
	            new Object[] { language, new Integer(iddev) });
	
	    List var1 = new ArrayList();
	    List var2 = new ArrayList();
	
	    ListBoxElement tmp = null;
	
	    Record record = null;
	
	    for (int i = 0; i < recordset.size(); i++)
	    {
	        record = recordset.get(i);
	        tmp = new ListBoxElement((String) record.get(1),
	                String.valueOf(record.get(0)));
	        var1.add(tmp);
	    }
	
	    VarphyBeanList varlist = new VarphyBeanList();
	    VarphyBeanList.clearMap();
	    DeviceListBean devices = new DeviceListBean(1,language);
		String dev_desc = "";
		if (!params.equals(""))
		{	
			String[] s_vars = params.split(",");
			int[] ids_vars = new int[s_vars.length];
			for (int i=0;i<s_vars.length;i++)
			{
				ids_vars[i] = Integer.parseInt(s_vars[i]);
			}		
			varlist.getListVarByIds(1,language,ids_vars);
			for (int i=0;i<ids_vars.length;i++)
			{
				VarphyBean aux = varlist.getVarById(ids_vars[i]);   //prevedere il caso in cui la variabile non venga trovata
				if (aux!=null)
				{
					dev_desc = devices.getDevice(aux.getDevice().intValue()).getDescription();
					tmp = new ListBoxElement(dev_desc+" -> "+aux.getShortDescription(), String.valueOf(aux.getId()));
					var2.add(tmp);
				}
			}
		}
		
	
	    DblListBox guardianlist = new DblListBox(var1, var2,false,true,true,"combo",true);
	    guardianlist.setScreenH(us.getScreenHeight());
	    guardianlist.setScreenW(us.getScreenWidth());
	    //guardianlist.setSrcButton2("images/dbllistbox/delete_on.png");
	    guardianlist.setIdlistbox("variable");
	   // guardianlist.setFncButton1("to2notRemove1(variable1);return false;");
	   // guardianlist.setFncButton2("to1Rem(variable2);return false;");
	    guardianlist.setLeftRowsListBox(100);
	    guardianlist.setRightRowsListBox(100);
	    guardianlist.setWidthListBox(400);
	
	    LangService lan = LangMgr.getInstance().getLangService(language);
	
	    guardianlist.setHeaderTable1(lan.getString("guardian", "header1"));
	    guardianlist.setHeaderTable2(lan.getString("guardian", "header2"));
	
	    return guardianlist.getHtmlDblListBox();
	}
    
    public static void writeMsgWinClose(String user) {
    	EventMgr.getInstance().info(new Integer(1), user, "Action", "G008", null);
    }
    
    public static int[] getGuardianMessageCode()
    {
    	int[] result = new int[2];
    	int jsFuncG = 3;
    	if(!GuardianConfig.userConfGuiChannel())
    		jsFuncG = 1;
    	else if(!GuardianConfig.userConfGuiVariable())
    		jsFuncG = 2;
    	
    	// Snooze guardian in days
    	String gsdays = ProductInfoMgr.getInstance().getProductInfo().get("gsnooze");
    	int daysbefore = 0;
    	if(gsdays != null)
    	{
    		long days = 0;
    		try {
    			days = Long.parseLong(gsdays);
    		}
    		catch(Exception e) {
    			days = 0;
    		}
    			
    		long gstime = ProductInfoMgr.getInstance().getProductInfo().getTime("gsnooze");
    		gstime = (gstime + (long)(86400000L * days));
    		
    		GregorianCalendar today = new GregorianCalendar();
    		today.setTimeInMillis(System.currentTimeMillis());
    		
    		GregorianCalendar expired = new GregorianCalendar();
    		expired.setTimeInMillis(gstime);
    		daysbefore = (expired.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR));
    		int yearsBefore = (expired.get(Calendar.YEAR)-today.get(Calendar.YEAR));
    		if(daysbefore > 0 || yearsBefore > 0)
    		{
    			jsFuncG = 4;
    		}
    		else
    		{
    			// Devo rimuovere il blocco
    			try
    			{
    				ProductInfoMgr.getInstance().getProductInfo().remove("gsnooze");
    			}
    			catch(Exception ex)
    			{}
    		}
    	}
    	result[0] = jsFuncG;
    	result[1] = daysbefore;
    	return result;
    }
//    public static String getGuardianMessage(String language)
//    throws Exception
//    {
//    	String result = "";
//    	LangService lan = LangMgr.getInstance().getLangService(language);
//    	if(!GuardianConfig.userConfGuiChannel() ||!GuardianConfig.userConfGuiVariable())
//    		result = lan.getString("top","message3");
//    	
//    	// Snooze guardian in days
//    	String gsdays = ProductInfoMgr.getInstance().getProductInfo().get("gsnooze");
//    	int daysbefore = 0;
//    	if(gsdays != null)
//    	{
//    		long days = 0;
//    		try {
//    			days = Long.parseLong(gsdays);
//    		}
//    		catch(Exception e) {
//    			days = 0;
//    		}
//    			
//    		long gstime = ProductInfoMgr.getInstance().getProductInfo().getTime("gsnooze");
//    		gstime = (gstime + (long)(86400000L * days));
//    		
//    		GregorianCalendar today = new GregorianCalendar();
//    		today.setTimeInMillis(System.currentTimeMillis());
//    		
//    		GregorianCalendar expired = new GregorianCalendar();
//    		expired.setTimeInMillis(gstime);
//    		
//    		daysbefore = (expired.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR));
//    		if(daysbefore > 0)
//    		{
//    			result = "guardianPRO countdown "+lan.getString("guardian","countdown")+daysbefore;
//    		}
//    		else
//    		{
//    			// Devo rimuovere il blocco
//    			ProductInfoMgr.getInstance().getProductInfo().remove("gsnooze");
//    		}
//    	}
//    	return result;
//    }
}
