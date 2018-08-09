package com.carel.supervisor.presentation.fs;

import java.util.HashMap;
import java.util.Map;

import bsh.This;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.Event;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.fs.FSManager;
import com.carel.supervisor.plugin.fs.FSRack;
import com.carel.supervisor.plugin.fs.FSUtil;
import com.carel.supervisor.presentation.bean.search.SearchEvent;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class FSStatus
{
	// images used to show D0 state
	private static final String imgD0undef = "images/led/L0.gif";
	private static final String imgD0true = "images/led/L2.gif";
	private static final String imgD0false = "images/led/L1.gif";
	
	private static final String imgNULL = "images/led/L0.gif";
	private static final String imgGREEN = "images/led/L1.gif";
	private static final String imgYELLOW = "images/led/L5.gif";
	private static final String imgORANGE = "images/led/L4.gif";
	private static final String imgRED = "images/led/L2.gif";
	
	
    public static String getStatusTable(FSRack rack, String lang, int height, int width, UserSession us, int anCounters[]) throws DataBaseException
    {
        //se motore è partito prendo rack da cache
        if (FSManager.getInstance().getRacks() != null)
        {
            rack = FSManager.getInstance().getRackByIdRack(rack.getId_rack());
        }
        boolean bNewAlg = rack.isNewAlg();

        LangService lan = LangMgr.getInstance().getLangService(lang);

        //header tabella
        String[] header = new String[bNewAlg ? FSManager.SB_STATUS + 1 : 6];
        header[0] = lan.getString("fsdetail", "utils");
        if( bNewAlg ) {
            header[1] = "T";
        	for(int i = 1; i < FSManager.SB_STATUS; i++)
        		header[i + 1] = "T-" + i;
        }
        else {
            header[1] = lan.getString("fsdetail", "dcset");
        	header[2] = lan.getString("fsdetail", "dccurrent");
        	header[3] = lan.getString("fsdetail", "maxdcrec");
        	header[4] = lan.getString("fsdetail", "mindcrec");
        	header[5] = lan.getString("fsdetail", "reset");
        }
        
        try
        {
            FSUtil[] utils = rack.getUtils();

            //descrizioni per i banchi in mappa
            Map map = FSUtilBean.getUtilsDescription(lang);
            FSUtil util = null;
            HTMLElement[][] data = new HTMLElement[utils.length][bNewAlg ? FSManager.SB_STATUS + 1 : 6];
            String[] row_class = new String[utils.length];
            String[] row_function = new String[utils.length];

            String act = null;
            String min = null;
            String max = null;
            
            boolean dtlviewEnabled = us.isMenuActive("dtlview");
            for(int i = 0; i < utils.length; i++)
            {
                util = utils[i];
                if( bNewAlg ) {
                	int[] status = util.getDStatus();
                	if(dtlviewEnabled)
                		data[i][0] = new HTMLSimpleElement("<b><a href='javascript:void(0)' style='font-weight:normal' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+util.getIdutil()+"&desc=ncode01') >" + map.get(util.getIdutil()) + "</a></b>");
                	else
                		data[i][0] = new HTMLSimpleElement("<b>"+ map.get(util.getIdutil()) + "</b>");
	                for(int j = 1; j < header.length; j++) {
	                	Integer s = j-1<status.length?status[j-1]:FSManager.STATUS_NULL;
	                	String image = "";
	                	if(s.intValue() == FSManager.STATUS_NULL.intValue() ||
	                		s.intValue() == FSManager.STATUS_YELLOW_OFFLINE ||
	                		s.intValue() == FSManager.STATUS_ORANGE_OFFLINE ||
	                		s.intValue() == FSManager.STATUS_RED_OFFLINE)
	                		image = imgNULL;
	                	else if(s.intValue() == FSManager.STATUS_GREEN.intValue())
	                		image = imgGREEN;
	                	else if(s.intValue() == FSManager.STATUS_YELLOW.intValue())
	                		image = imgYELLOW;
	                	else if(s.intValue() == FSManager.STATUS_ORANGE.intValue())
	                		image = imgORANGE;
	                	else if(s.intValue() == FSManager.STATUS_RED.intValue())
	                		image = imgRED;
	                	if(!image.equals(""))
	                		data[i][j] = new HTMLSimpleElement("<img src='" + image + "'>");
	                	else
	                		data[i][j] = new HTMLSimpleElement("N.A");
	                }
                }
                else {
	                act = (util.getActualDC() == null) ? "***" : util.getActualDC().toString();
	                max = (util.getMaxDcRecorded().intValue() < 0) ? "***" : util.getMaxDcRecorded().toString();
	                min = (util.getMinDCRecorded().intValue() > 100) ? "***" : util.getMinDCRecorded().toString();
	                row_class[i] = "Row1";
	                row_function[i] = util.getIdutil().toString();
	
	                if ((util.getActualDC() != null) && (util.getActualDC() > util.getMaxDC()))
	                {
	                    row_class[i] = "statoAllarme2_b";
	                }
	                if(dtlviewEnabled)
                		data[i][0] = new HTMLSimpleElement("<b><a href='javascript:void(0)' style='font-weight:normal' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+util.getIdutil()+"&desc=ncode01') >" + map.get(util.getIdutil()) + "</a></b>");
                	else
                		data[i][0] = new HTMLSimpleElement("<b>"+ map.get(util.getIdutil()) + "</b>");
	                data[i][1] = new HTMLSimpleElement(util.getMaxDC() + " %");
	                data[i][2] = new HTMLSimpleElement("<b>" + act + " %</b>");
	                data[i][3] = new HTMLSimpleElement(max + " %");
	                data[i][4] = new HTMLSimpleElement(min + " %");
	               	data[i][5] = new HTMLSimpleElement("<img onclick='reset_one(this);' name='res_" + util.getIdutil() + "' id='res_" + util.getIdutil() + "' style='cursor:pointer' src='images/actions/rst_on_black.png'/>");
                }
            }

            HTMLTable table = new HTMLTable("tabUtil", header, data);
            if( bNewAlg ) {
            	int anAlignType[] = new int[header.length];
            	anAlignType[0] = 0;
            	for(int i = 1; i < anAlignType.length; i++)
            		anAlignType[i] = 1;
            	table.setAlignType(anAlignType);
            }
            else {
	            table.setRowsClasses(row_class);
		        table.setRowHeight(24);
	            table.setAlignType(new int[] { 0, 1, 1, 1, 1, 1 });
            }

            int tab_width = (width * 65) / 100;
            table.setColumnSize(0, (tab_width * 30) / 100);
            table.setHeight(height - 650);
            table.setWidth(tab_width);

            return table.getHTMLText();
        }
        catch (Exception e)
        {
        	LoggerMgr.getLogger("FSStatus").error(e);
            return "ERROR";
        }
    }

    public static String getOverviewTable(FSRack[] racks, String lang, int height, int width) throws Exception
    {
        LangService lan = LangMgr.getInstance().getLangService(lang);
        StringBuffer html = new StringBuffer();
        int tables = racks.length;
        int column_number = 2;
        String[] descriptions = getRacksDesc(racks, lang);
        int t_h = height - 550;
        int t_w = width - 180;
        html.append("<table border='0' cellpadding='0' cellspacing='1' width='" + t_w + "' align='center' valign='middle'>");

        int i = 0;
        FSRack rack = null;
        Integer idrack = null;

        String ids = "";

        int gauge_w = width / 8;
        int gauge_h = (gauge_w * 9) / 16;

        while (i < tables)
        {
            html.append("<tr>");

            for (int j = 0; j < column_number; j++)
            {
                if (i < tables)
                {
                    rack = racks[i];
                    idrack = rack.getId_rack();
                    ids = ids + idrack + ";";

                    String set_act = "";
                    String set_measure = "";
                    try
                    {
                    	set_act = ControllerMgr.getInstance().getFromField(rack.getId_setpoint().intValue()).getFormattedValue();
                        set_measure = ControllerMgr.getInstance().getFromField(rack.getId_setpoint().intValue()).getInfo().getMeasureunit();
                    }
                    catch (Exception e) {
                    	set_act = "***";
                    	set_measure = "";
					}
                    
                    String min_act = "";
                    String max_act = "";
                    String min_measure = "";
                    String max_measure = "";

                    boolean old_rack = "old".equalsIgnoreCase(rack.getAux()) ? true : false;

                    if (old_rack)
                    {
                        min_act = rack.getId_minset().toString();
                        min_measure = "bar/°C";
                        max_act = rack.getId_maxset().toString();
                        max_measure = "bar/°C";
                    }
                    else
                    {
                    	try
                    	{
                        min_act = ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getFormattedValue();
                        min_measure = ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getInfo().getMeasureunit();
                        max_act = ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getFormattedValue();
                        max_measure = ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getInfo().getMeasureunit();
                    	}
                    	catch (Exception e) {
                    		 min_act = "***";
                             min_measure = "";
                             max_act = "***";
                             max_measure = "";
						}
                    }

                    html.append("<TD align='center' >");
                    html.append("<TABLE id='rack" + idrack + "' onclick=\"go_to_detail(" + idrack + ");\" onmouseover=\"rack_onmouseover(" + idrack + ")\" onmouseout=\"rack_onmouseout(" + idrack + ")\" width=\"80%\" height=\"90%\"   class='table' border='0' cellpadding=\"0\" cellspacing=\"0\" >");
                    html.append("<TR class='standardTxt' height=\"15%\"><TD width='100%' align='center' colspan='5' style='color:#476ab0'><b>" + descriptions[i] + "<b></TD></TR>");
                    html.append("<TR class='standardTxt' height=\"10%\"><TD width='100%' align='center' colspan='5'><b>" + lan.getString("fsdetail", "currentset") + "</b></TD></TR>");
                    html.append("<TR height=\"15%\"><input type='hidden' id='gset_" + idrack + "' value='" + set_act + "'/><TD class='standardTxt' colspan='5' align='center'><b>" + set_act + " " + set_measure +"</b></TD></TR>");
                    html.append("<TR class='standardTxt' height=\"50%\">");
                    html.append("<TD>" + lan.getString("fsdetail", "setmin") + "</TD><input type='hidden' id='gmin_" + idrack + "' value='" + min_act + "'/><TD align='center'>" + min_act + " " + min_measure +"</TD>");

                    html.append("<TD align=\"center\" valign=\"middle\" ><object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\"  id=\"gauge_" + rack.getId_rack() + "\" AllowScriptAccess=\"always\" width=\"" + gauge_w + "\" height=\"" + gauge_h + "\" wmode=\"opaque\">");
                    html.append("<param name=\"movie\" Value=\"flash/maps/Ramp.swf\">");
                    html.append("<param name=\"quality\" Value=\"high\">");
                    //html.append("<param name=\"wmode\" value=\"transparent\">");

                    // <object> tag alone is not OK in FF and Chrome. The <embed> tag must always be added
                    html.append("<embed src=\"flash/maps/Ramp.swf\" swLiveConnect=\"true\" allowScriptAccess=\"always\" menu=\"false\" quality=\"high\" allowFullScreen=\"false\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" wmode=\"opaque\"");
                    html.append(" name=\"gauge_" + rack.getId_rack() + "\" width=\"" + gauge_w + "\" height=\"" + gauge_h + "\"/>");
                    //<embed src="flash/maps/Ramp.swf" bgcolor="#ffffff" width="900" height="600" name="objVisualScheduler" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" wmode="opaque"/>
                    html.append("</object></TD>");
                    
                    

                    html.append("<TD>" + lan.getString("fsdetail", "setmax") + "</TD><input type='hidden' id='gmax_" + idrack + "' value='" + max_act + "'/><TD align='center'>" + max_act + " " + max_measure +"</TD>");
                    html.append("</TR><TR valign='top'><TD colspan='5' align='center' class='standardTxt'><b>" + lan.getString("fs", rack.isNewAlg() ? "new_alg" : "old_alg") + "</b><br><br></TD></TR></TABLE>");
                    html.append("</TD>");

                    i++;

                    if (j == (column_number - 1))
                    {
                        html.append("</tr>");
                    }
                }
                else
                {
                    html.append("</tr>");

                    break;
                }
            }
        }

        html.append("</table>");
        ids = ids.substring(0, ids.length() - 1);
        html.append("<input type='hidden' id='ids_racks' value='" + ids + "'/>");

        return html.toString();
    }

    
    public static String getRackStatusTable(int idTab, FSRack rack, String lang, int height, int width, UserSession us) throws DataBaseException
    {
        LangService lan = LangMgr.getInstance().getLangService(lang);
        
    	// table header
        String[] header = new String[FSManager.SB_STATUS + 1];
        header[0] = lan.getString("fsdetail", "setpoint");
        header[1] = "T";
    	for(int i = 1; i < FSManager.SB_STATUS; i++)
    		header[i+1] = "T-" + i;
    	
        // data
        HTMLElement[][] data = null;
        if( rack != null ) {
        	data = new HTMLElement[1][header.length];
        	boolean dtlviewEnabled = us.isMenuActive("dtlview");
        	if(dtlviewEnabled)
        		data[0][0] = new HTMLSimpleElement("<b><a href='javascript:void(0)' style='font-weight:normal' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+rack.getIdDevice()+"&desc=ncode01') >"+ getSetpointDesc(rack, lang) + "</a></b>");
        	else
        		data[0][0] = new HTMLSimpleElement("<b>"+ getSetpointDesc(rack, lang) + "</b>");
	        for(int j = 1; j < header.length; j++) {
	        	Float setpoint = rack.getSetpoint(j-1);
	        	data[0][j] = new HTMLSimpleElement(setpoint != null ? setpoint.toString() : "***");
	        }
        }
        
        HTMLTable table = new HTMLTable("tabRacks", header, data);
        table.setTableId(idTab);
        int tab_width = (width * 65) / 100;
        table.setColumnSize(0, (tab_width * 30) / 100);
        table.setWidth(tab_width);
        table.setHeight(140);
        int anAlignType[] = new int[header.length];
    	anAlignType[0] = 0;
    	for(int i = 1; i < anAlignType.length; i++)
    		anAlignType[i] = 1;
    	table.setAlignType(anAlignType);
        return table.getHTMLText();
    }

    
    private static String[] getRacksDesc(FSRack[] racks, String language) throws DataBaseException
    {
        String[] result = new String[racks.length];
        String sql = "select t1.description,t2.description as vdesc from cftableext as t1, cftableext as t2,fsrack,cfvariable where t1.idsite=? and " +
        		"t1.tablename='cfdevice' and t1.languagecode=? and t1.tableid=fsrack.iddevice and fsrack.idrack=? and " +
        		"fsrack.iddevice=? and cfvariable.iddevice=fsrack.iddevice and cfvariable.idvariable=? and t2.tablename='cfvariable' and " +
        		"t2.languagecode=? and t2.tableid=cfvariable.idvariable and t2.idsite=?";
        Object[] params = new Object[7];

        for (int i = 0; i < racks.length; i++)
        {
            params[0] = new Integer(1);
            params[1] = language;
            params[2] = racks[i].getId_rack();
            params[3] = racks[i].getIdDevice();
            params[4] = racks[i].getId_setpoint();
            params[5] = language;
            params[6] = new Integer(1);

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
            if( rs.size() > 0 )
            	result[i] = rs.get(0).get("description").toString() + " ("+rs.get(0).get("vdesc")+")";
        }

        return result;
    }


    private static String getSetpointDesc(FSRack rack, String language) throws DataBaseException
    {
        String result = "";
        String sql = "select t1.description,t2.description as vdesc from cftableext as t1, cftableext as t2,fsrack,cfvariable where t1.idsite=? and " +
        		"t1.tablename='cfdevice' and t1.languagecode=? and t1.tableid=fsrack.iddevice and fsrack.idrack=? and " +
        		"fsrack.iddevice=? and cfvariable.iddevice=fsrack.iddevice and cfvariable.idvariable=? and t2.tablename='cfvariable' and " +
        		"t2.languagecode=? and t2.tableid=cfvariable.idvariable and t2.idsite=?";
        Object[] params = new Object[7];
        params[0] = new Integer(1);
        params[1] = language;
        params[2] = rack.getId_rack();
        params[3] = rack.getIdDevice();
        params[4] = rack.getId_setpoint();
        params[5] = language;
        params[6] = new Integer(1);
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        if( rs.size() > 0 )
        	result = rs.get(0).get("vdesc").toString();
        return result;
    }
    
    
    public static void cleanDBTables() throws DataBaseException
    {
        String sql = "delete from fsutil where idutil not in (select iddevice from cfdevice where iscancelled = ?)";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]
            {
                "FALSE"
            });
        
        sql = "delete from fsutil where idrack in (select idrack from fsrack where iddevice not in " +
        		"( select iddevice from cfdevice where iscancelled = ?) and idrack <> -1) ";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]
            {
                "FALSE"
            });

        sql = "delete from fsrack where iddevice not in (select iddevice from cfdevice where iscancelled = ?) and idrack <> -1";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]
            {
                "FALSE"
            });
    }
    
    /**
     * Remove a rack from the fsrack table and its reference from the fsutil table 
     * @param idrack
     * @throws DataBaseException
     */
    public static void removeRack(int idRack) throws DataBaseException
    {
        String sql = "delete from fsutil where idrack = ? and idrack <> -1";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]
            {
                idRack
            });

        sql = "delete from fsrack where idrack = ? and idrack <> -1";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]
            {
                idRack
            });
    }
    
    /**
     * Remove a device from a specific rack
     * @param iddevice
     * @param idRack
     * @throws DataBaseException
     */
    public static void removeDeviceFromRack(int iddevice, int idRack) throws DataBaseException {
    	String sql = "delete from fsutil where idutil = ? and idrack = ? and idrack <> -1";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]
            {
        		iddevice,
                idRack
            });    	
    }
}
