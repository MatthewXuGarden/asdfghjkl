package com.carel.supervisor.presentation.alarms;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class AlarmMngTable {
	
	private static Logger logger = LoggerMgr.getLogger(AlarmMngTable.class);

	public AlarmMngTable() {
		
	}
	
	public static String getHtmlAlarmMngTable(int width, int height,String language, UserSession us)
	{
		// Alessandro: added virtual key support
		boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
		String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");
		
		LangService lang = LangMgr.getInstance().getLangService(language);
				
		String[] header = {lang.getString("alrmng","h1"),lang.getString("alrmng","h2"),lang.getString("alrmng","h3"),lang.getString("alrmng","h4")};
		HTMLElement[][] data = new HTMLElement[4][4];
		int freq = 0;
		String ack = "";
		String noteack = "";
		for (int i=0;i<4;i++)
		{
			freq = (int)SystemConfMgr.getInstance().get("priority_"+(i+1)).getValueNum();
			ack = (SystemConfMgr.getInstance().get("priority_"+(i+1)).getValue().equals("TRUE"))?"checked":"";
			noteack = (SystemConfMgr.getInstance().get("noteack_"+(i+1)).getValue().equals("TRUE"))?"checked":"";
			data[i][0] = new HTMLSimpleElement(lang.getString("alrview","alarmstate"+(i+1)));
			data[i][1] = new HTMLSimpleElement("<input class='standardTxt"+cssVirtualKeyboardClass+"' style='width:60;' type='text' name='p_"+(i+1)+"' value='"+freq+"'/> "+lang.getString("siteview","minuts"));
			data[i][2] = new HTMLSimpleElement("<input class='standardTxt' "+ack+"  name='c_"+(i+1)+"' id='c_"+(i+1)+"' type='checkbox' onclick='decheckMandNote("+(i+1)+");decheckSticky("+(i+1)+");' />");
			data[i][3] = new HTMLSimpleElement("<input class='standardTxt' "+noteack+"  name='na_"+(i+1)+"' id='na_"+(i+1)+"' type='checkbox' onclick='checkAck("+(i+1)+");'/>");
		}
		
		HTMLTable table = new HTMLTable("alarmMng",header,data);
		table.setScreenW(us.getScreenWidth());
		table.setScreenH(us.getScreenHeight());
		table.setAlignType(1,1);
		table.setColumnSize(0,130);
		table.setColumnSize(1,200);
		table.setColumnSize(2,200);
		table.setColumnSize(3,200);
		table.setAlignType(0,1);
		table.setAlignType(1,1);
		table.setAlignType(2,1);
		table.setAlignType(3,1);
		table.setHeight(height);
		table.setWidth(width);
		table.setRowHeight(20);
		return table.getHTMLText();
		
	}

	//return true if the "Sticky Alarms" function is enabled
	public static boolean stickyEnabled(){
		boolean result = false;
		
		try{
			result = SystemConfMgr.getInstance().get("stickyalarms").getValue().equals("TRUE");
		}
		catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}
	
	//return true if the multi Ack with note function is enabled
	public static boolean multiAckNoteEnabled(){
		boolean result = false;
		
		try{
			result = SystemConfMgr.getInstance().get("multiacknote").getValue().trim().length()>0;
		}
		catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}

	//return the multi
	public static String multiAckNote(){
		String result = "";
		
		try{
			result = SystemConfMgr.getInstance().get("multiacknote").getValue().trim();
		}
		catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}
	
	//return true if the "Sticky Alarms" function is enabled
	public static boolean mandatoryNote(int priority){
		boolean result = false;
		
		try{
			if (priority>4) priority=4;
			result = SystemConfMgr.getInstance().get("noteack_"+priority).getValue().equals("TRUE");
		}
		catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}

}
