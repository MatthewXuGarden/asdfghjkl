package com.carel.supervisor.presentation.events;

import java.util.*;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class DevicesList {
	private List<String> listDevices = new ArrayList<String>();
	private String language;
	
	
	public static boolean containsDevices(String messagecode)
	{
		return messagecode.equals("FS11")||messagecode.equals("FS12");
	}
	
	
	public static int logDevices(List<Integer> listDevices)
	{
    	int id = 0;
    	try {
    		id = SeqMgr.getInstance().next(null, "hsdevices", "id");
    	} catch(DataBaseException e) {
    		LoggerMgr.getLogger(DevicesList.class).error(e);
    	}
    	String sql = "insert into hsdevices values(?, ?);";
    	for(int i = 0; i < listDevices.size(); i++) {
    		try {
    			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { id, listDevices.get(i) });
        	} catch(DataBaseException e) {
        		LoggerMgr.getLogger(DevicesList.class).error(e);
        	}
    	}
    	return id;
	}
	
	
	public void loadFromDataBase(int id, int idsite, String language)
	{
		this.language = language;
		String sql = "select description from hsdevices inner join cftableext"
			+ " on idsite=? and languagecode=? and tablename='cfdevice'"
			+ " and cftableext.tableid=hsdevices.iddevice where id=?;";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { idsite, language, id });
			for(int i = 0; i < rs.size(); i++) {
				String strDevice = rs.get(i).get(0).toString(); 
				listDevices.add(strDevice);
			}
    	} catch(DataBaseException e) {
    		LoggerMgr.getLogger(DevicesList.class).error(e);
    	}
	}
	

	public String getHtmlDeviceTable(int nScreenWidth, int nScreenHeight)
	{
		LangService lang = LangMgr.getInstance().getLangService(language);
		
		// data
		HTMLElement[][] data = new HTMLElement[listDevices.size()][];
		for(int i = 0; i < listDevices.size(); i++) {
			data[i] = new HTMLElement[1];
			data[i][0] = new HTMLSimpleElement(listDevices.get(i));
		}
			
		// header
		String[] headerTable = new String[1];
        headerTable[0] = lang.getString("Param", "Device");
		
        // table
        HTMLTable table = new HTMLTable("devicesTable", headerTable, data);
        table.setTableId(1);
        table.setScreenW(nScreenWidth);
        table.setScreenH(nScreenHeight);
        table.setWidth(900);
        table.setHeight(260);
        table.setColumnSize(0, 500);
        return "<BR>" + table.getHTMLText();
	}
}
