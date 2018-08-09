package com.carel.supervisor.presentation.fs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class FSUtilBean
{
    public static final Integer DEFAULT_DC = 70;
    public static final Integer MAX_UTILS = 300;
    
	//restituisce i banchi istanziati nell'impianto
    public static FSUtilAux[] retrieveUtils(String lang) throws DataBaseException
    {
    	FSUtilAux[] utils = null;
    	
        String sql = "select cfdevice.*,cftableext.description,fsdevmdl.var1 from cfdevice,cftableext,fsdevmdl where " +
        		"cfdevice.iddevmdl in (select iddevmdl from cfdevmdl where code in " +
        		"(select devcode from fsdevmdl where israck=?)) and cfdevice.iscancelled=? and cftableext.tablename=? and " +
        		"cftableext.languagecode=? and cftableext.tableid=cfdevice.iddevice and cftableext.idsite=1 and " +
        		"fsdevmdl.devcode=(select code from cfdevmdl where iddevmdl=cfdevice.iddevmdl) order by cfdevice.iddevice";
        Object[] params = new Object[4];
        params[0] = "FALSE";
        params[1] = "FALSE";
        params[2] = "cfdevice";
        params[3] = lang;
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        
        utils = new FSUtilAux[rs.size()];
       
        for (int i = 0; i < utils.length; i++)
        {
            utils[i] = new FSUtilAux(new DeviceBean(rs.get(i), lang),rs.get(i).get("var1").toString());
            
        }

        return utils;
    }
    

    public static FSUtilAux[] retrieveFreeUtils(String lang) throws DataBaseException
    {
    	FSUtilAux[] utils = null;
    	
        String sql = "select cfdevice.*,cftableext.description,fsdevmdl.var1 from cfdevice,cftableext,fsdevmdl where" 
        	+ " cfdevice.iddevmdl in (select iddevmdl from cfdevmdl where code in"
        	+ " (select devcode from fsdevmdl where israck='FALSE')) and cfdevice.iscancelled='FALSE' and cftableext.tablename='cfdevice' and"
        	+ " cftableext.languagecode=? and cftableext.tableid=cfdevice.iddevice and cftableext.idsite=1 and" 
        	+ " fsdevmdl.devcode=(select code from cfdevmdl where iddevmdl=cfdevice.iddevmdl)"
        	+ " and cfdevice.iddevice not in (select idutil from fsutil where idrack <> -1)"
        	+ " order by cfdevice.iddevice";

        Object[] params = new Object[1];
        params[0] = lang;
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        
        utils = new FSUtilAux[rs.size()];
       
        for (int i = 0; i < utils.length; i++)
        {
            utils[i] = new FSUtilAux(new DeviceBean(rs.get(i), lang),rs.get(i).get("var1").toString());
        }

        return utils;
    }
    

    private static Map<Integer,Integer> getUtilAssociation() throws DataBaseException
    {
    	Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    	String sql = "select idutil,idrack from fsutil";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,null);
    	Integer idutil = null;
    	Integer idrack = null;
    	
    	if (rs!=null&&rs.size()>0)
    	{
    		for (int i=0;i<rs.size();i++)
    		{
    			idutil = (Integer) rs.get(i).get("idutil");
    			idrack = (Integer) rs.get(i).get("idrack");
    			map.put(idutil,idrack);
    		}
    		return map;
    	}
    	else
    	return null;	
    	
    	
    	
    }

    public static String getHTMLTable(FSUtilAux[] utils,String language) throws DataBaseException
    {
        StringBuffer html = new StringBuffer();
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	// retrieve centrali salvate
        String sql = "select fsrack.idrack,t1.description as rdesc,t2.description as vdesc from " +
        		"fsrack,cftableext as t1,cftableext as t2,cfdevice,cfvariable where " +
        		"fsrack.iddevice=cfdevice.iddevice and cfdevice.iscancelled=? and " +
        		"t1.languagecode=? and t1.tablename='cfdevice' and t1.tableid=fsrack.iddevice and t1.idsite=1" +
        		"and cfvariable.iddevice=fsrack.iddevice and cfvariable.idvariable=fsrack.setpoint " +
        		"and cfvariable.iscancelled=? and t2.tableid=cfvariable.idvariable and " +
        		"t2.tablename='cfvariable' and t2.languagecode=? and t2.idsite=1";
        Object[] params = new Object[4];
        params[0] = "FALSE";
        params[1] = language;
        params[2] = "FALSE";
        params[3] = language;

        RecordSet rs_racks = DatabaseMgr.getInstance().executeQuery(null, sql, params);

        //se ho almeno una centrale mostro la tabella altrimenti messaggio di avviso
        if (rs_racks == null)
        {
            return lan.getString("fs","noracks");
        }
        else
        {
            html.append("<table width='100%' class='table' cellpadding='1' cellspacing='1' border='0'>\n");
            html.append("<tr class='th' height='22'>");
            html.append("<td><b>"+lan.getString("fsdetail","utils")+"</b></td>");
            html.append("<td><b>"+lan.getString("fs","racks")+"</b></td>");
            html.append("</tr>\n");
        	
            Map<Integer,Integer> map = getUtilAssociation();
            Integer current_util = null;
            DeviceBean util = null;
            String ids = "";
            for (int i = 0; i < utils.length; i++)
            {
                util = utils[i].getUtil();
            	current_util = util.getIddevice();
            	ids =ids+current_util+";";
            	
            	html.append("<tr class='Row1'>\n");
            	html.append("<td width='50%' class='standardTxt'>"+util.getDescription()+"<input type='hidden' name='solen_"+current_util+"' value='"+utils[i].getSolenoid()+"'</td>\n");
            	html.append("<td width='50%' class='standardTxt'><select style='width:100%' id='util_"+current_util+"' name='util_"+current_util+"'>"+ getOptionRacks(rs_racks, current_util,map)+"</select></td>\n");
            	html.append("</tr>\n");
            }
            html.append("</table>");
                       
            if (!"".equalsIgnoreCase(ids)) ids = ids.substring(0,ids.length()-1);
            String input_hidden = "<input type='hidden' id='ids_util' name='ids_util' value='"+ids+"'/><input type='hidden' id='max_util' value='"+MAX_UTILS+"'/>";
           
            return html.toString()+input_hidden;
        }
    }
    
    private static String getOptionRacks(RecordSet rs_racks, Integer idutil, Map association)
    {
    	int num_racks = rs_racks.size();
        StringBuffer combo_rack = new StringBuffer();
        combo_rack.append("<option value='-1'>------------</option>");
        Record r = null;
        String sel = "";
        Integer idrack = null;
        String descr_rack = null;
        String sp_code = null;
        for (int i = 0; i < num_racks; i++)
        {
        	r = rs_racks.get(i);
        	idrack = (Integer) r.get("idrack");
        	descr_rack = r.get("rdesc").toString();
        	sp_code = r.get("vdesc").toString();
        	if (association!=null&&association.get(idutil)!=null&&association.get(idutil).equals(idrack))
        	{
        		sel = "selected";
        	}
        	else
        	{
        		sel = "";
        	}
        	combo_rack.append("<option "+sel+" value='"+idrack+"'>"+descr_rack+" ("+sp_code+")</option>");
        }
        
        return combo_rack.toString();
    }
    
    public static void saveUtilAssociation(UserSession us,Properties prop) throws DataBaseException
    {
    	Integer idrack = Integer.parseInt(prop.getProperty("idRackSelected"));
    	boolean bNewAlg = prop.getProperty("new_alg") != null;
    	String[] str_ids= prop.getProperty("ids_util").split(";");
    	if(bNewAlg && prop.getProperty("ids_util") != null && prop.getProperty("ids_util").length()>0)
    	{
    		Object[] param = new Object[str_ids.length];
    		String sql = "select count(*) from cfdevice "+
    		"inner join cfdevmdl on cfdevmdl.iddevmdl=cfdevice.iddevmdl "+
    		"where cfdevice.iddevice in(";
    		for(int i=0;i<str_ids.length;i++)
    		{
    			sql += "?,";
    			param[i] = Integer.valueOf(str_ids[i]);
    		}
    		sql = sql.substring(0,sql.length()-1);
    		sql += ") and cfdevmdl.code in('mpxprostep2','mpxpro_modbus','mpxprov4','mpxprov4_modbus')";
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
        	Record r = null;
        	if (rs!=null&&rs.size()>0)
        	{
        		r = rs.get(0);
    	    	int size = (Integer)r.get(0);
    	    	if(size<str_ids.length)
    	    	{
    	    		us.getCurrentUserTransaction().setAttribute("onlympxpro", " ");
    	    		return;
    	    	}
        	}
    	}
    	String sql = "update fsrack set new_alg = ? where idrack = ?;";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { bNewAlg, idrack });
    	
    	sql = "delete from fsutil where idrack=" + idrack + " or idrack=-1";
    	DatabaseMgr.getInstance().executeStatement(null,sql,null);
    	
    	if(prop.getProperty("ids_util") == null || prop.getProperty("ids_util").length()==0)
    		return;
    	Integer idutil = -1;
    	Object[][] params = new Object[str_ids.length][bNewAlg ? 9 : 6];
    	// fsutil: idutil, idsolenoid, idrack, maxdc, idtsh
    	// idtsh it is used with new algorithm 
    	sql = bNewAlg
    		? "insert into fsutil values (?,(select idvariable from cfvariable where idsite=? and iddevice=? and code=(select var5 from fsdevmdl where devcode=(select code from cfdevmdl where iddevmdl=(select iddevmdl from cfdevice where iddevice = ?))) and idhsvariable is not null),?,?,(select idvariable from cfvariable where idsite=? and iddevice=? and code=(select var6 from fsdevmdl where devcode=(select code from cfdevmdl where iddevmdl=(select iddevmdl from cfdevice where iddevice = ?))) and idhsvariable is not null))"
    		: "insert into fsutil values (?,(select idvariable from cfvariable where idsite=? and iddevice=? and code=(select var1 from fsdevmdl where devcode=(select code from cfdevmdl where iddevmdl=(select iddevmdl from cfdevice where iddevice = ?))) and idhsvariable is not null),?,?)";
    	for (int i=0;i<str_ids.length;i++)
    	{
    		idutil = new Integer(str_ids[i]);
    		
    		params[i][0] = idutil;
    		params[i][1] = new Integer(1);  //idsite
    		params[i][2] = idutil;
    		params[i][3] = idutil;
    		params[i][4] = new Integer(idrack);
    		params[i][5] = DEFAULT_DC;
    		if( bNewAlg ) {
        		params[i][6] = new Integer(1);  //idsite
        		params[i][7] = idutil;
        		params[i][8] = idutil;
    		}
    	}
    	
    	DatabaseMgr.getInstance().executeMultiStatement(null,sql,params);
    }
    
    public static Map<Integer,String> getUtilsDescription(String lang) throws DataBaseException
    {
    	Map<Integer,String> map = null;
    	String sql = "select fsutil.idutil,cftableext.description from fsutil,cftableext where cftableext.idsite=? and cftableext.languagecode=? and cftableext.tablename=? and cftableext.tableid=fsutil.idutil";
    	Object[] param = new Object[3];
    	param[0] = new Integer(1);
    	param[1] = lang;
    	param[2] = "cfdevice";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
    	Record r = null;
    	if (rs!=null&&rs.size()>0)
    	{
	    	map = new HashMap<Integer,String>();
    		for (int i=0;i<rs.size();i++)
	    	{
	    		r = rs.get(i);
	    		map.put((Integer)r.get("idutil"),r.get("description").toString());
	    	}
    	}
	    return map;
    }
    
    
    public static String getHTMLRackTable(UserSession session) throws DataBaseException
    {
    	LangService lang = LangMgr.getInstance().getLangService(session.getLanguage());

    	// data
        String sql = "select fsrack.idrack,t1.description as rdesc,t2.description as vdesc,fsrack.new_alg from fsrack,cftableext as t1,cftableext as t2, " +
        		"cfdevice,cfvariable where fsrack.iddevice=cfdevice.iddevice and cfdevice.iscancelled=? and " +
        		"t1.languagecode =? and t1.tablename='cfdevice' and t1.tableid=fsrack.iddevice and t1.idsite=1 and " +
        		"cfvariable.iddevice=fsrack.iddevice and cfvariable.idvariable=fsrack.setpoint and cfvariable.iscancelled='FALSE' and " +
        		"t2.tableid=cfvariable.idvariable and t2.tablename='cfvariable' and t2.languagecode=? and t2.idsite=1";
        Object[] params = new Object[3];
        params[0] = "FALSE";
        params[1] = session.getLanguage();
        params[2] = session.getLanguage();

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        HTMLElement[][] data = new HTMLElement[rs.size()][];
		String[] astrClickRowFunction = new String[rs.size()];        
		for(int i = 0; i < rs.size(); i++) {
			Record r = rs.get(i);
        	Integer idrack = (Integer)r.get("idrack");
        	String descr_rack = r.get("rdesc").toString();
        	String sp_code = r.get("vdesc").toString();
			data[i] = new HTMLElement[3];
			data[i][0] = new HTMLSimpleElement(descr_rack + " (" + sp_code + ")");
			RecordSet rsCount = DatabaseMgr.getInstance().executeQuery(null, "select count(*) from fsutil where idrack=" + idrack);
			data[i][1] = new HTMLSimpleElement(rsCount.size() > 0 ? rsCount.get(0).get(0).toString() : "0");
			if(Boolean.valueOf(r.get("new_alg").toString()))
			{
				data[i][2] = new HTMLSimpleElement("x"); 
			}
			astrClickRowFunction[i] = idrack.toString();
		}
		
		// header
		String[] headerTable = new String[3];
        headerTable[0] = lang.getString("fs","racks");
        headerTable[1] = lang.getString("fs","n_link_util");
        headerTable[2] = lang.getString("fs","new_alg");

        // table
        HTMLTable table = new HTMLTable("racksTable", headerTable, data);
        table.setTableId(1);
        table.setSgClickRowAction("onSelectRack('$1')");
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onSelectRack('$1')");
        table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(session.getScreenWidth());
        table.setScreenH(session.getScreenHeight());
        table.setHeight(156);
        table.setColumnSize(0, 350);
        table.setColumnSize(1, 30);
        table.setColumnSize(1, 100);
        table.setWidth(870);
        table.setAlignType(new int[] { 0, 1 ,1});
        
        return table.getHTMLText();        
    }
    
    
    public static String getHTMLRackUtilities(UserSession session) throws DataBaseException
    {
    	LangService lang = LangMgr.getInstance().getLangService(session.getLanguage());

    	DblListBox dblListBox = new DblListBox(new ArrayList(), new ArrayList(),
			false, true, true, null, true);
		dblListBox.setHeaderTable1(lang.getString("fs", "all_util"));
		dblListBox.setHeaderTable2(lang.getString("fs", "rack_util"));
		dblListBox.setScreenW(session.getScreenWidth());
		dblListBox.setScreenH(session.getScreenHeight());
		dblListBox.setHeight(450);
		dblListBox.setWidthListBox(400);
		return dblListBox.getHtmlDblListBox();		    	
    }
    

    public static Integer getOtherUtils(Integer idRack) throws DataBaseException
    {
    	RecordSet rsCount = DatabaseMgr.getInstance().executeQuery(null, "select count(*) from fsutil where idrack <> " + idRack);
    	return rsCount.size() == 1 ? (Integer)rsCount.get(0).get(0) : 0;
    }
}
