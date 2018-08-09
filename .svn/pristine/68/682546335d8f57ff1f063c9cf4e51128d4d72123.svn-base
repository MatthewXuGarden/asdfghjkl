package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.device.DeviceStatus;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.presentation.devices.DeviceList;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.script.EnumerationMgr;


public class DeviceListBean
{
    private static final int column_number = 8;
    private static final String IDDEVICE = "iddevice";
    private HashMap<Integer, DeviceBean> deviceList = new HashMap<Integer, DeviceBean>();
    private int[] ids = null;
    private int idSite = 1;
    private String language = "EN_en";
	
    private static int exceptioncounter = 0;


	public DeviceListBean(int site, String language) throws Exception
    {
        String sql =
            "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
            "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
            " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.iscancelled = ?" +
            " and cftableext.idsite = ? order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(site), "FALSE", new Integer(site)
                });
        load(site, language, rs);
    }

	// this constructor can 'hide' the Internal IO device, on device list loaded
	// Nicola Compagno 24032010
	public DeviceListBean(int site, String language, boolean hideInternalIO) throws Exception
    {
    	String sql = "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
        "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
        " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.iscancelled = ?" +
        " and cftableext.idsite = ? "; 
    	
    	if(hideInternalIO)
    	{
            sql += "and cfdevice.code != '-1.000' ";
    	}
    	
    	sql += "order by cftableext.description";
    	
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(site), "FALSE", new Integer(site)
                });
        load(site, language, rs);	
    }
	
	public DeviceListBean(int site, String language, boolean hasLogic,boolean hideInternalIO,boolean orderByDesc) throws Exception
    {
    	String sql = "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
        "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
        " cftableext.languagecode = ? and cfdevice.idsite = ? "+(!hasLogic?" and cfdevice.islogic='FALSE'":"")+" and cfdevice.iscancelled = ?" +
        " and cftableext.idsite = ? "; 
    	
    	if(hideInternalIO)
    	{
            sql += "and cfdevice.code != '-1.000' ";
    	}
    	if(orderByDesc)
    		sql += "order by cftableext.description";
    	else
    		sql += "order by cfdevice.islogic, cfdevice.idline,cfdevice.address,cfdevice.code ";
    	
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(site), "FALSE", new Integer(site)
                });
        load(site, language, rs);	
    }
	public DeviceListBean(int site, String language, Integer iddevice)
        throws Exception
    {
        //Tira su dispositivi dello stesso modello del dispositivo dato
        DeviceBean dev = retrieveSingleDeviceById(site,
                iddevice.intValue(), language);
        int iddevmdl = dev.getIddevmdl();

        String sql =
            "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
            "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
            " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.iscancelled = ?" +
            " and cfdevice.iddevmdl = ? and cfdevice.iddevice != ? and cftableext.idsite = ? order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(site), "FALSE", new Integer(iddevmdl),
                    iddevice, new Integer(site)
                });
        load(site, language, rs);
    }

    public DeviceListBean(String language, ArrayList<Integer> ids) throws Exception {
        String sql = "SELECT cfdevice.*, cftableext.description " +
        		" from cfdevice, cftableext " +
        		" where " +
        		" cfdevice.iddevice in (";
        for(Iterator<Integer> itr = ids.iterator();itr.hasNext();){
        	sql+=itr.next();
        	if(itr.hasNext())
        		sql+=",";
        }
        sql += ") and " +
        		" cftableext.idsite=1 and " +
        		" cftableext.tablename='cfdevice' and " +
        		" cftableext.tableid=cfdevice.iddevice and " +
        		" cftableext.languagecode='"+language+"' and " +
        		" cfdevice.iscancelled='FALSE' " +
        		" order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        if (rs.size() > 0)
        {
            load(1, language, rs);
        }    	
    }
    
    public DeviceListBean(int site, String language, int group)
        throws Exception
    {
        //ATTENZIONE: Tiro ora su solo device fisici
        String sql =
            "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
            "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
            " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.idgroup = ? and cfdevice.islogic='FALSE' " +
            "and cfdevice.iscancelled = ? and cftableext.idsite = ? order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(site), new Integer(group), "FALSE",
                    new Integer(site)
                });

        if (rs.size() > 0)
        {
            load(site, language, rs);
        }
    }

    public DeviceListBean(int site, String language, int group,
        boolean fisic_logic) // 4 parametro fasullo 
        throws Exception
    {
        //ATTENZIONE: Tiro su device fisici e logici
        String sql =
            "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
            "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
            " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.idgroup = ? " +
            "and cfdevice.iscancelled = ? and cftableext.idsite = ? order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(site), new Integer(group), "FALSE",
                    new Integer(site)
                });

        if (rs.size() > 0)
        {
            load(site, language, rs);
        }
    }
    
    public DeviceListBean(int site, String language, int[] group)
    	throws Exception
    {
    	this(site,language,group,false,null,null);
    }
    
    public DeviceListBean(int site, String language, int[] group,boolean orderByDesc)
    	throws Exception
    {
    	this(site,language,group,orderByDesc,null,null);
    }
    
    public DeviceListBean(int site, String language, int[] group,String lim,String off)
		throws Exception
	{
		this(site,language,group,false,lim,off);
	}
    
    public DeviceListBean(int site, String language, int[] group,boolean orderByDesc,String lim,String off)
        throws Exception
    {
        this.idSite = site;
        this.language = language;

        StringBuffer sql = new StringBuffer(
                "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
                "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
                " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.iscancelled = ? " +
                " and cftableext.idsite = ? and cfdevice.idgroup in(");
        Object[] values = new Object[group.length + 4];
        values[0] = language;
        values[1] = new Integer(site);
        values[2] = "FALSE";
        values[3] = new Integer(site);

        for (int i = 0; i < group.length; i++)
        {
            values[i + 4] = new Integer(group[i]);
            sql.append("?");

            if (i < (group.length - 1))
            {
                sql.append(",");
            }
        }
        
        if(orderByDesc)
        	sql.append(") order by cfdevice.islogic, cftableext.description ");
        else
        	sql.append(") order by cfdevice.islogic, cfdevice.idline,cfdevice.address,cfdevice.code ");
        
        if(lim != null && off != null)
        {
        	try
        	{
        		int iLim = Integer.parseInt(lim);
        		int iOff = Integer.parseInt(off);
        		sql.append(" limit "+iLim+" offset "+iOff);
        	}
        	catch(Exception e){
        	}
        }
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
                sql.toString(), values);

        load(site, language, rs, orderByDesc);
    }
    
    public DeviceListBean(int site, String language, int[] group,boolean orderByDesc,String lim,String off,
    		String idLine) throws Exception
    {
    this.idSite = site;
    this.language = language;

    StringBuffer sql = new StringBuffer(
            "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
            "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
            " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.iscancelled = ? " +
            " and cftableext.idsite = ? and cfdevice.idline=? and cfdevice.idgroup in(");
    Object[] values = new Object[group.length + 5];
    values[0] = language;
    values[1] = new Integer(site);
    values[2] = "FALSE";
    values[3] = new Integer(site);
    values[4] = new Integer(idLine);
    	
    for (int i = 0; i < group.length; i++)
    {
        values[i + 5] = new Integer(group[i]);
        sql.append("?");

        if (i < (group.length - 1))
        {
            sql.append(",");
        }
    }
    
    if(orderByDesc)
    	sql.append(") order by cfdevice.islogic, cftableext.description ");
    else
    	sql.append(") order by cfdevice.islogic, cfdevice.idline,cfdevice.address,cfdevice.code ");
    
    if(lim != null && off != null)
    {
    	try
    	{
    		int iLim = Integer.parseInt(lim);
    		int iOff = Integer.parseInt(off);
    		sql.append(" limit "+iLim+" offset "+iOff);
    	}
    	catch(Exception e){
    	}
    }
    
    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
            sql.toString(), values);

    load(site, language, rs, orderByDesc);
    }
    
    
    private void load(int site,String language,RecordSet rs)
    	throws Exception
    {
    	load(site,language,rs,false);
    }
    
    private void load(int site, String language, RecordSet rs,boolean logicByDesc)
        throws Exception
    {
        if (rs.size() > 0)
        {
            Integer iddevice = null;
            DeviceBean deviceBean = null;
            ids = new int[rs.size()];

            Record record = null;

            ArrayList<String> codeFisDev = new ArrayList<String>();
            ArrayList<String> codeLogDev = new ArrayList<String>();
            int iddev = -1;

            HashMap<String, Integer> codesId = new HashMap<String, Integer>();

            for (int i = 0; i < rs.size(); i++)
            {
                record = rs.get(i);
                iddevice = (Integer) record.get(IDDEVICE);
                iddev = iddevice.intValue();
                deviceBean = new DeviceBean(iddev, record, site, language);
                
                //logic device, add true code
                if (deviceBean.islogic())
                {
                    codeLogDev.add(deviceBean.getCode());
                    codesId.put(deviceBean.getCode(), new Integer(iddev));
                }
                //physical device, add 00001.001 as device code
                else
                {
                	String code = deviceBean.getCode();
                    if(code != null && code.indexOf(".")>0)
                    {
    	                int line = Integer.valueOf(code.substring(0,code.indexOf(".")));
    	                String lineStr = String.format("%05d", line);
    	                code = lineStr + code.substring(code.indexOf("."),code.length());
                    }
                    codesId.put(code, new Integer(iddev));
                    codeFisDev.add(code);
                }

                deviceList.put(iddevice, deviceBean);
            }

            String[] sCodeF = new String[codeFisDev.size()];

            for (int i = 0; i < sCodeF.length; i++)
                sCodeF[i] = (String) codeFisDev.get(i);

            String[] sCodeL = new String[codeLogDev.size()];

            for (int i = 0; i < sCodeL.length; i++)
                sCodeL[i] = (String) codeLogDev.get(i);

            Arrays.sort(sCodeF);
            if(!logicByDesc)
            	Arrays.sort(sCodeL);

            int idx = -1;

            for (int i = 0; i < sCodeF.length; i++)
                ids[++idx] = ((Integer) codesId.get(sCodeF[i])).intValue();

            for (int i = 0; i < sCodeL.length; i++)
                ids[++idx] = ((Integer) codesId.get(sCodeL[i])).intValue();
        }
    }

    public DeviceBean getDevice(int i)
    {
    	return deviceList.get(new Integer(i));
    }
    
    public DeviceBean retrieveDeviceOwner(int idVariable)
    	throws DataBaseException
    {
    	String sql = "select iddevice from cfvariable where idvariable = ?;";
    	int idDevice = (Integer)DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idVariable }).get(0).get(0);
    	return getDevice(idDevice);
    }

    public int size()
    {
        return deviceList.size();
    }

    public int[] getIds()
    {
        return ids;
    }

    public static DeviceBean retrieveSingleDeviceById(int idsite, int iddevice,
        String language) throws DataBaseException
    {
        String sql =
            "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
            "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
            " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.iddevice = ? and cfdevice.iscancelled = ? and cftableext.idsite = ? order by cftableext.description";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(idsite), new Integer(iddevice),
                    "FALSE", new Integer(idsite)
                });

        return new DeviceBean(rs.get(0), language);
    }
    
    public static DeviceBean retrieveSingleDeviceByCode(int idsite, String code,
            String language) throws DataBaseException
        {
            String sql =
                "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
                "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
                " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.code = ? and cfdevice.iscancelled = ? and cftableext.idsite = ? order by cftableext.description";

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[]
                    {
                        language, new Integer(idsite), code,
                        "FALSE", new Integer(idsite)
                    });
            
            if (rs!=null&&rs.size()>0)
            	return new DeviceBean(rs.get(0), language);
            else
            	return null;
        }

    public String getHTMLDeviceView(int idsite, String language)
        throws DataBaseException
    {
        StringBuffer html = new StringBuffer();
        int tables = ids.length;
        DeviceBean dev = null;

        html.append(
            "<table border='0' cellpadding='0' cellspacing='0' width='97%' height='100%' align='left' valign='top'>");

        for (int i = 0; i < tables; i++)
        {
            dev = deviceList.get(new Integer(ids[i]));

            html.append("<tr>");

            html.append(
                "<td width='33%' valign='top' class='td' onclick='redirect(" +
                dev.getIddevice() + ")'>");
            html.append(
                "<table cellpadding='0' cellspacing='1' width='100%' height='100%' class='ajaxdevtable' onmouseover='dwhighlight(this,true);' onmouseout='dwhighlight(this,false);'>");
            html.append("<tr><td width='8%' align='left'>" +
                getLed(dev.getIddevice()) +
                "</td><td class='standardTxt' width='*'><b>" +
                dev.getDescription() +
                "</b></td><td width='15%' class='standardTxt'><b>" +
                dev.getCode() + "</b></td></tr>");
            html.append(getMainVarToDisplayOfDevice(idsite, dev.getIddevice(),
                    language));
            html.append("</table>");
            html.append("</td>");

            i++;

            if (i < tables)
            {
                dev = deviceList.get(new Integer(ids[i]));

                html.append(
                    "<td width='33%' valign='top' class='td' onclick='redirect(" +
                    dev.getIddevice() + ")'>");
                html.append(
                    "<table cellpadding='0' cellspacing='1' width='100%' height='100%' class='ajaxdevtable' onmouseover='dwhighlight(this,true);' onmouseout='dwhighlight(this,false);'>");
                html.append("<tr><td width='8%' align='left'>" +
                    getLed(dev.getIddevice()) +
                    "</td><td class='standardTxt'><b>" + dev.getDescription() +
                    "</b></td><td width='15%' class='standardTxt'><b>" +
                    dev.getCode() + "</b></td></tr>");
                html.append(getMainVarToDisplayOfDevice(idsite,
                        dev.getIddevice(), language));
                html.append("</table>");
                html.append("</td>");

                i++;

                if (i < tables)
                {
                    dev = deviceList.get(new Integer(ids[i]));

                    html.append(
                        "<td width='33%' valign='top' class='td' onclick='redirect(" +
                        dev.getIddevice() + ")'>");
                    html.append(
                        "<table cellpadding='0' cellspacing='1' width='100%' height='100%' class='ajaxdevtable' onmouseover='dwhighlight(this,true);' onmouseout='dwhighlight(this,false);'>");
                    html.append("<tr><td width='8%' align='left'>" +
                        getLed(dev.getIddevice()) +
                        "</td><td class='standardTxt'><b>" +
                        dev.getDescription() +
                        "</b></td><td width='15%' class='standardTxt'><b>" +
                        dev.getCode() + "</b></td></tr>");
                    html.append(getMainVarToDisplayOfDevice(idsite,
                            dev.getIddevice(), language));
                    html.append("</table>");
                    html.append("</td>");
                    html.append("</tr>");
                }
                else
                {
                    html.append("</tr>");
                }
            }
            else
            {
                html.append("</tr>");
            }
        }

        html.append("</table>");

        return html.toString();
    }

    public String getMixedHTMLDeviceView(int idsite, String language)
    throws DataBaseException
    {
    	StringBuffer html = new StringBuffer();
        int[] ids_tot = new int[ids.length];
        int[] ids_std = null;
        int w = 0;
        int t = 0;
        
        LangService l = LangMgr.getInstance().getLangService(language);
        String winery = l.getString("deviceviewn", "tab1name");
        String devices = l.getString("deviceview", "tab1name");
        
        html.append("<table border='0' cellpadding='0' cellspacing='0' width='99%' height='100%' align='left' valign='top'>");
        
		// ***** sezione Devices *****
		//se ho almeno un dev fuori Winary oppure nessuno in Winery:
		if ((t > 0) || ((t == 0) && (w == 0)))
		{
			if (t > 0)
				this.ids = ids_std;
			
			html.append("<tr><td>");
			String stdHTML = this.getHTMLDeviceView(idsite, language);
			html.append(stdHTML);
			html.append("</td></tr>");
		}
        
        html.append("</table>");

        return html.toString();
    }
    
    private String getLed(int idDev)
    {
        StringBuffer buffer = new StringBuffer();
        String imgDecod = UtilDevice.getLedColor(new Integer(idDev));

        buffer.append("<div id='DLed" + idDev + "' style='");
        buffer.append("background-image:url(images/led/L" + imgDecod +
            ".gif);background-repeat:no-repeat;background-position:center' ");
        buffer.append("><div style='visibility:hidden;'>" + imgDecod +
            "</div></div>");

        return buffer.toString();
    }

    private static String retrieveValue(VarphyBean varphyBean, int idDevice)
    {
        StringBuffer buffer = new StringBuffer();
        int maxLung = 32;
        String varDesc = "";
		String varValue = "";
		String varUM = varphyBean.getMeasureUnit();
        
		varDesc = varphyBean.getShortDescription();

        if (DeviceStatusMgr.getInstance().isOffLineDevice(varphyBean.getDevice()))
        {
            //buffer.append("***");
			varValue = "***";
        }
        else
        {
            try
            {
                //buffer.append(ControllerMgr.getInstance()
                //                           .getFromField(varphyBean)
                //                           .getFormattedValue());
				varValue = ControllerMgr.getInstance().getFromField(varphyBean).getFormattedValue();
            }
            catch (Exception e)
            {
                varValue = "***";
				Logger logger = LoggerMgr.getLogger(DeviceList.class);
                logger.error(e);
            }
        }

		int lungD = ((varDesc != null) ? varDesc.length() : 0);
		int lungV = varValue.length();
		int lungU = ((varUM != null) ? varUM.length() : 0);
		
		if ((lungD + lungV + lungU) > maxLung)
        {
        	//devDesc = devDesc.substring(0, 30) + "...";
        	String cutDescr = varDesc.substring(0, maxLung - lungV - lungU + 1);
            varDesc = cutDescr + "<A title='"+varDesc+"'>...</A>";
        }

        buffer.append(
            "<table border='0' width='100%' cellspacing='0' cellpadding='0'><tr>");
        buffer.append("<td class='td' width='*' align='left'><nobr>" + varDesc +
            "</nobr></td>");
        buffer.append(
            "<td class='td' width='10%' align='right'><nobr><div id='var_" +
            idDevice + "_" + varphyBean.getId().intValue() + "'>");
		
		buffer.append(varValue);
		
        buffer.append("</div><nobr></td>");
        buffer.append("<td class='td' width='10%' align='right'><nobr>" +
            varUM + "<nobr></td>");
        buffer.append("</tr></table>");

        return buffer.toString();
    }

    private String getMainVarToDisplayOfDevice(int idsite, int iddevice,
        String language) throws DataBaseException
    {
        StringBuffer html = new StringBuffer();
        RecordSet rs = getMainVarToDisplayOfDevicePrv(idsite, iddevice, language);

        VarphyBean tmp = null;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = new VarphyBean(rs.get(i));
            html.append(
                "<tr><td width='8%'></td><td colspan='2' class='standardTxt'>" +
                retrieveValue(tmp, iddevice) + "</td></tr>");
        }

        if (rs.size() < 4)
        {
            for (int i = rs.size(); i < 4; i++)
            {
                html.append(
                    "<tr><td width='8%'></td><td colspan='2' class='standardTxt'>&nbsp;</td></tr>");
            }
        }

        return html.toString();
    }

    public static Object countActiveDevice() throws DataBaseException
    {
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
        	"select count(1) from cfdevice where iscancelled='FALSE' and (idline > 0 or idline is null)"); // no need to count internalIO
        return recordSet.get(0).get(0);
    }


    public static Object countActiveDevice(int idLineToExclude) throws DataBaseException
    {
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
       		"select count(1) from cfdevice where iscancelled='FALSE' and ((idline > 0 and idline <> ?) or idline is null)",
       		new Object[] { idLineToExclude });
        return recordSet.get(0).get(0);
    }
    
    
    public String getXmlRefreshDevice()
    {
    	return getXmlRefreshDevice(false);
    }

    public String getXmlRefreshDevice(Boolean extrainfo)
    {
        RecordSet rs = null;
        VarphyBean var = null;
        StringBuffer s = new StringBuffer("<devices>");
        int tables = 0;

        if (ids != null)
        {
            tables = ids.length;
        }

        DeviceBean dev = null;
        String sVal = "";
        String label = ""; 

        for (int i = 0; i < tables; i++)
        {
            dev = (DeviceBean) deviceList.get(new Integer(ids[i]));

            if (dev != null)
            {
                s.append("<device id='" + dev.getIddevice() + "' status='" +
                    UtilDevice.getLedColor(new Integer(dev.getIddevice())) +
                    "'>");
                if(extrainfo){
	                s.append("<dn><![CDATA["+dev.getDescription()+"]]></dn>");
	                
	                // modified to hide device code in case of Internal IO device
	                // Nicola Compagno 29032010
	                if(!dev.getCode().equals("-1.000"))
	                {
	                	s.append("<c><![CDATA["+dev.getCode()+"]]></c>");
	                }
	                else
	                {
	                	s.append("<c><![CDATA["+""+"]]></c>");
	                }
                }
                rs = getMainVarToDisplayOfDevicePrv(this.idSite, dev.getIddevice(), this.language);

                if (rs != null)
                {
                    for (int j = 0; j < rs.size(); j++)
                    {
                        var = new VarphyBean(rs.get(j));

                        if (var != null)
                        {
                            if (DeviceStatusMgr.getInstance().isOffLineDevice(var.getDevice()))
                            {
                                sVal = "***";
                            }
                            // bug fix by longbow.liu,  bug id 10374 : Smart HP error in chinese language
                            else if(DeviceStatusMgr.getInstance().getDeviceStatus(var.getDevice()) == null){
                            	sVal = "***";
                            }
                            else 
                            {
                                try
                                {
                                    sVal = ControllerMgr.getInstance()
                                                        .getFromField(var)
                                                        .getFormattedValue();
                                    // label management
                                    if (!sVal.equals("***"))
                                    {
                                    	try
                                    	{
                                    		label = EnumerationMgr.getInstance().getEnumCode(var.getIdMdl(), Float.parseFloat(sVal), language);
                                    	}
                                    	catch(NumberFormatException e)
                                    	{
                                    		sVal = sVal.replace(",", "");
                                    		label = EnumerationMgr.getInstance().getEnumCode(var.getIdMdl(), Float.parseFloat(sVal), language);
                                    	}
                                    }
                                    if (!"".equals(label))
                                    {
                                    	sVal = label;
                                    }
                                    // label management
                                }
                                catch (Exception e)
                                {
                                	if(exceptioncounter%1000==0){
                                		exceptioncounter ++;
                                		Logger logger = LoggerMgr.getLogger(DeviceList.class);
                                		logger.error(e);
                                		logger.error("-- ERROR INFO -- idvarmdl:"+var.getIdMdl()  + " value:"+sVal);
                                		logger.error("Exceptioncounter = "+exceptioncounter);
                                	}
                                }
                            }

                            String varid = "var_" + dev.getIddevice() + "_" + var.getId().intValue();
                            s.append("<var id='"+varid+"'>");
                        	s.append(sVal);
                            s.append("</var>");
                            if(extrainfo){
                                s.append("<n" + varid + "><![CDATA[" + var.getShortDescription() + "]]></n" + varid + ">");
                                s.append("<m" + varid + "><![CDATA[" + var.getMeasureUnit() + "]]></m" + varid + ">");
                            }
                        }
                    }
                }

                s.append("</device>");
            }
        }

        s.append("</devices>");

        return s.toString();
    }

    private RecordSet getMainVarToDisplayOfDevicePrv(int idsite, int iddevice,
        String language)
    {
        RecordSet rs = null;

        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr " +
            "from cfvariable inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and " +
            "cftableext.languagecode = ? and cfvariable.idsite=? and cfvariable.iscancelled=? and " +
            "cfvariable.idhsvariable is not null and cftableext.idsite = ? and cfvariable.todisplay=? and " +
            "cfvariable.iddevice = ? order by cfvariable.priority";
        Object[] values = new Object[6];
        values[0] = language;
        values[1] = new Integer(idsite);
        values[2] = "FALSE";
        values[3] = new Integer(idsite);
        values[4] = "HOME";
        values[5] = new Integer(iddevice);

        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return rs;
    }
    
    /*
     * SNMP dll loader method
     */
    public static LinkedList<DeviceBean> getDevicesByLine(int idsite, int idline){
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select *, ''::character varying as description  from cfdevice "); //for devicebean compatibility
    	sql.append(" where idline = ");
    	sql.append(idline);
    	sql.append(" and iscancelled='FALSE' order by code");//and isenabled='TRUE' ");
    	
    	LinkedList<DeviceBean> ll = new LinkedList<DeviceBean>();
    	
    	RecordSet rs;
    	try {
    		rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString());
    		for (int i = 0; i < rs.size(); i++) {
    			ll.add(new DeviceBean(rs.get(i),""));
    		}
    	} catch (DataBaseException e) {
    		LoggerMgr.getLogger(DeviceListBean.class).error(e);
    	}
    	
    	return ll;
    }
    
        //Ing. Gilioli Manuel Start 4 report
    //carica tutti i device dato il modello long per modificare la firma
    public  DeviceListBean(int site, String language,int idDevMdl,int dummy) throws Exception
    {
        String sql =
            "SELECT cfdevice.*, cftableext.description from cfdevice inner join " +
            "cftableext on cftableext.tableid = cfdevice.iddevice and cftableext.tablename='cfdevice' and" +
            " cftableext.languagecode = ? and cfdevice.idsite = ? and cfdevice.iscancelled = ?" +
            " and cftableext.idsite = ? and cfdevice.iddevmdl = ?  order by cftableext.description ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(site), "FALSE", new Integer(site), new Integer((int)idDevMdl)
                });
        load(site, language, rs);
    }
    
    //Ing. Gilioli Manuel Fine 4 report
}
