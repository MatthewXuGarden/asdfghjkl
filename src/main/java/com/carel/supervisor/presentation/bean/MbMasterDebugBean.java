package com.carel.supervisor.presentation.bean;

import java.io.*;
import java.io.InputStream.*;
import java.nio.charset.Charset;
import java.util.*;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.vscheduler.VSCategory;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.presentation.session.UserSession;


public class MbMasterDebugBean
{
	public static final String logFileName = "C:\\Windows\\System32\\PVProModbus.log";
	private static final int logSize = 500; // max rows to render
	private Vector<String> objDebugLog;
	
	private UserSession us;
	private LangService lang;
	
	
	public MbMasterDebugBean(UserSession us)
	{
		this.us = us;
		lang = LangMgr.getInstance().getLangService(us.getLanguage());
	}
	
	
	public String renderDebugLog()
	{
		// data
		loadDebugLog();
		int nSize = objDebugLog.size() > logSize ? logSize : objDebugLog.size(); 
		HTMLElement[][] data = new HTMLElement[nSize][];
		for(int i = 0; i < data.length; i++) {
			String strLine = objDebugLog.elementAt(objDebugLog.size() - i - 1);
			data[i] = new HTMLElement[3];
			data[i][0] = new HTMLSimpleElement(strLine.substring(0, 19));
			int k = strLine.indexOf(":", 20);
			data[i][1] = new HTMLSimpleElement(strLine.substring(20, k));
			data[i][2] = new HTMLSimpleElement(strLine.substring(k + 1));
		}
		unloadDebugLog();
		
		// header
		String[] headerTable = new String[3];
        headerTable[0] = lang.getString("debug", "date_time");
		headerTable[1] = lang.getString("debug", "mb_debug_type");
        headerTable[2] = lang.getString("debug", "mb_debug_desc");
        
        // table
        HTMLTable table = new HTMLTable("debugTable", headerTable, data);
        table.setScreenW(us.getScreenWidth());
        table.setScreenH(us.getScreenHeight());
        table.setColumnSize(0, 60);
        table.setColumnSize(1, 60);
        table.setColumnSize(2, 700);
        table.setWidth(900);
        table.setHeight(450);
        table.setAlignType(new int[] { 0, 1, 0 });
        return table.getHTMLText();
	}
	
	
	public void exportDebugLog(String strFileName)
	{
		try {
			
			PrintWriter fw = new PrintWriter(new FileOutputStream(new File(strFileName), false));
			loadDebugLog();
			for(int i = 0; i < objDebugLog.size(); i++)
				fw.println(objDebugLog.get(i));
			unloadDebugLog();
			fw.close();
		}
		catch(IOException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void loadDebugLog()
	{
		objDebugLog = new Vector<String>();
		
		try {
			File f = new File(logFileName);
			FileInputStream fis = new FileInputStream(f); 
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			String strLine;
			while( (strLine = br.readLine()) != null ) {
				objDebugLog.add(strLine);
				if( objDebugLog.size() > logSize )
					objDebugLog.remove(0); // to avoid wasting memory
			}
			br.close();
			isr.close();
			fis.close();
			f = null;
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void unloadDebugLog()
	{
		objDebugLog = null;
	}
}
