package com.carel.supervisor.presentation.bean;

import java.util.*;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

// class used to manage parameter set priorities (cfvarmdlext table)
public class ParamBean {
	public static final short DEFAULT_PRIORITY = 0; 
	public static final short MAX_PRIORITY = 10;
	private int idSite;
	private int idDevMdl;
	private int idVarGroup;
	private String language;
	
	private List<Integer> listVars = new ArrayList<Integer>();						// list with model variables
	private List<Integer> listGroups = new ArrayList<Integer>();					// list with model groups
	private Map<Integer, Integer> mapVarGroup = new HashMap<Integer, Integer>();	// map var -> group
	private Map<Integer, String> mapVarDesc = new HashMap<Integer, String>();		// map var -> description 
	private Map<Integer, String> mapVarShortDesc = new HashMap<Integer, String>();  // map var -> short description
	private Map<Integer, Short> mapVarPriority = new HashMap<Integer, Short>();		// map var -> priority
	private Map<Integer, String> mapGroupDesc = new HashMap<Integer, String>();		// map group -> description
	
	
	public ParamBean(int idSite, int idDevMdl, String language)
	{
		this.idSite = idSite;
		this.idDevMdl = idDevMdl;
		this.language = language;
		
		// add "All parameters" as 1st group
		Integer nAllParams = 0;
		String strAllParams = LangMgr.getInstance().getLangService(language).getString("dtlview", "s_allparams");
		listGroups.add(nAllParams);
		mapGroupDesc.put(nAllParams, strAllParams);
		
		loadGroups();
		loadVariables();
		loadPriorities();
	}
	
	
	public void setPriorities(String csvIdVarMdl, String csvPriority)
	{
		if( csvIdVarMdl.isEmpty() || csvPriority.isEmpty() )
			return;

		String sql = "delete from cfvarmdlext where idvarmdl in (" + csvIdVarMdl + ");";
		try {
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}

		sql = "insert into cfvarmdlext values(?, ?);";
		String[] astrIdVarMdl = csvIdVarMdl.split(",");
		String[] astrPriority = csvPriority.split(",");
		for(int i = 0; i < astrIdVarMdl.length; i++) {
			Integer idVarMdl = Integer.parseInt(astrIdVarMdl[i]);
			Short nPriority = Short.parseShort(astrPriority[i]);
			if( nPriority > DEFAULT_PRIORITY ) {
				try {
					DatabaseMgr.getInstance().executeStatement(null, sql,
						new Object[] { idVarMdl, nPriority });
					mapVarPriority.put(idVarMdl, nPriority);
				} catch(DataBaseException e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
			else {
				mapVarPriority.remove(idVarMdl);
			}
		}
	}
	
	
	public Integer getIdDevMdl()
	{
		return idDevMdl;
	}
	

	public Integer getIdVarGroup()
	{
		return idVarGroup;
	}
	
	
	public void setVarGroup(int idVarGroup)
	{
		this.idVarGroup = idVarGroup;
	}
	
	
	public String getHtmlParamGroups(int nScreenWidth)
	{
		StringBuffer sbHtml = new StringBuffer();
		int cell_width = 170;
		int cell_height = 30;
		int column_number = (nScreenWidth - 124) / cell_width;
		int col = 0;

        sbHtml.append("<table border='0' cellpadding='0' cellspacing='4'  align='left' valign='top'>");
        for(int i = 0; i < listGroups.size(); i++) {
        	Integer id = listGroups.get(i); 
        	if( col == 0 )
        		sbHtml.append("<tr class='Row1'>");
        	
        	sbHtml.append("<td onclick='loadParams(" +
        		+ id + ")' align='center' width='"
        		+ cell_width + "px' height='" + cell_height
        		+ "px' class='" + (id == idVarGroup ? "groupCategorySelected" : "groupCategory")
        		+ "' id='td_grp_" + id
                + "'>" + mapGroupDesc.get(id) + "</td>");
        	
        	if( col < column_number - 1 ) {
        		col++;
        	}
        	else {
				col = 0;
				sbHtml.append("</tr>");
        	}
       }
       if( col != 0 )
    	   sbHtml.append("</tr>");
       sbHtml.append("</table>");

       return sbHtml.toString();
	}
	
	
	public String getHtmlParamTable(int nScreenWidth, int nScreenHeight)
	{
		LangService lang = LangMgr.getInstance().getLangService(language);

		// data
		List<Integer> listVarGroup = idVarGroup > 0 ? new ArrayList<Integer>() : listVars;
		if( idVarGroup > 0 ) {
			for(int i = 0; i < listVars.size(); i++) {
				Integer idVarMdl = listVars.get(i);
				Integer id = mapVarGroup.get(idVarMdl);
				if( id == idVarGroup )
					listVarGroup.add(idVarMdl);
			}
		}
		HTMLElement[][] data = new HTMLElement[listVarGroup.size()][];
		String[] astrClickRowFunction = new String[listVarGroup.size()];
		for(int i = 0; i < listVarGroup.size(); i++) {
			Integer idVarMdl = listVarGroup.get(i);
			astrClickRowFunction[i] = idVarMdl.toString();
			data[i] = new HTMLElement[3];
			data[i][0] = new HTMLSimpleElement(mapVarShortDesc.get(idVarMdl));
			data[i][1] = new HTMLSimpleElement(mapVarDesc.get(idVarMdl));
			Short nPriority = mapVarPriority.get(idVarMdl);
			if( nPriority == null )
				nPriority = DEFAULT_PRIORITY;
			data[i][2] = new HTMLSimpleElement(getHtmlPriority(idVarMdl, nPriority));
		}
		
		// header
		String[] headerTable = new String[3];
        headerTable[0] = lang.getString("siteview", "param_short_desc");
        headerTable[1] = lang.getString("siteview", "param_desc");
        headerTable[2] = lang.getString("siteview", "param_priority");

        // table
        HTMLTable table = new HTMLTable("parametersTable", headerTable, data);
        table.setTableId(1);
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setScreenW(nScreenWidth);
        table.setScreenH(nScreenHeight);
        table.setRowHeight(22);
        table.setHeight(250);
        table.setColumnSize(0, 120);
        table.setColumnSize(1, 400);
        table.setColumnSize(2, 50);
        table.setWidth(900);
        table.setAlignType(new int[] { 0, 0, 1 });
        return table.getHTMLText();
	}
	
	
	private void loadGroups()
	{
		String sql = "select idvargroup, description from cfvarmdlgrp inner join cftableext on"
			+ " cftableext.idsite = cfvarmdlgrp.idsite and cftableext.tableid = cfvarmdlgrp.idvargroup"
			+ " and cftableext.tablename='cfvarmdlgrp' and languagecode = ?"
			+ " where cfvarmdlgrp.idsite = ?;";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	    		new Object[] { language, idSite });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer idVarGroup = (Integer)r.get("idvargroup");
				String strDescription = (String)r.get("description");
				mapGroupDesc.put(idVarGroup, strDescription);
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void loadVariables()
	{
		String sql = "select idvarmdl, idvargroup, readwrite, description, shortdescr from cfvarmdl inner join cftableext on"
			+ " cftableext.idsite = cfvarmdl.idsite and cftableext.tableid = cfvarmdl.idvarmdl"
			+ " and cftableext.tablename='cfvarmdl' and languagecode = ?"
			+ " where iddevmdl = ? and cfvarmdl.idsite = ? and cfvarmdl.type < 4 and cfvarmdl.readwrite <> '1'"
			+ " order by priority, idvargroup;";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	    		new Object[] { language, idDevMdl, idSite });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer idVarMdl = (Integer)r.get("idvarmdl");
				Integer idVarGroup = (Integer)r.get("idvargroup");
				String strDescription = (String)r.get("description");
				String strShortDesc = (String)r.get("shortdescr");
				listVars.add(idVarMdl);
				if( !listGroups.contains(idVarGroup) )
					listGroups.add(idVarGroup);
				mapVarGroup.put(idVarMdl, idVarGroup);
				mapVarDesc.put(idVarMdl, strDescription);
				mapVarShortDesc.put(idVarMdl, strShortDesc);
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void loadPriorities()
	{
		String sql = "select cfvarmdl.idvarmdl, cfvarmdlext.priority from cfvarmdl inner join cfvarmdlext"
			+ " on cfvarmdlext.idvarmdl = cfvarmdl.idvarmdl"
			+ " where iddevmdl = ?;";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	    		new Object[] { idDevMdl });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer idVarMdl = (Integer)r.get("idvarmdl");
				Short nPriority = (Short)r.get("priority");
				if( nPriority != null )
					mapVarPriority.put(idVarMdl, nPriority);
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private String getHtmlPriority(Integer idVarMdl, Short nPriority)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='pri" + idVarMdl + "' class='standardTxt'>");
		for(int i = DEFAULT_PRIORITY; i <= MAX_PRIORITY; i++) {
			sb.append("<option value='" + i + (i == nPriority ? "' selected>" : "'>"));
			if( i > DEFAULT_PRIORITY )
				sb.append(String.valueOf(i));
			sb.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
}
