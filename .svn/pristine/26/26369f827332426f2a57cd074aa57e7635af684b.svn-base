package com.carel.supervisor.presentation.fs;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.fs.FSRack;
import com.carel.supervisor.plugin.fs.FSUtil;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class FSConfig {

	public static String getConfigUtilsTable(UserSession us,FSRack rack, String lang,int height,int width) throws DataBaseException
	{
		Map<Integer,Float> max_dc = rack.isNewAlg()
			? getTshMap(rack)
			: FSRackBean.getMaxDCFromDB(rack.getId_rack());
		
		boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
		String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");
		
		LangService lan = LangMgr.getInstance().getLangService(lang);
		FSUtil[] utils = rack.getUtils();
		int rowHeight = 20;
		String[] code = null;
		
		//header tabella
		String[] header = new String[4];
		header[0] = lan.getString("fsdetail","utils");
		header[1] = lan.getString("fsdetail","line");
		header[2] = lan.getString("fsdetail","address");
		header[3] = lan.getString("fsdetail", rack.isNewAlg() ? "tsh" : "maxdc");
		
		
		Map descriptions = FSUtilBean.getUtilsDescription(lang);
		HTMLElement[][] data = new HTMLElement[utils.length][4];
		
		int i, j;
		boolean dtlviewEnabled = us.isMenuActive("dtlview");
		for (i=0; i < data.length; i++)
		{
			code = StringUtility.split(utils[i].getCode(),".");
			j = utils[i].getIdutil().intValue();
			Float nValue = max_dc.get(utils[i].getIdutil());
			String strValue = "";
			if(rack.isNewAlg())
				strValue = nValue != null ? nValue.toString() : "";
			else
				strValue = 	nValue != null ? String.valueOf((int)nValue.floatValue()):"";
			if(dtlviewEnabled)
				data[i][0] = new HTMLSimpleElement("<a href='javascript:void(0)' style='font-weight:normal' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+utils[i].getIdutil()+"&desc=ncode01') >"+descriptions.get(j).toString()+"</a>");
			else
				data[i][0] = new HTMLSimpleElement(descriptions.get(j).toString());
			data[i][1] = new HTMLSimpleElement(code[0]);
			data[i][2] = new HTMLSimpleElement(code[1]);
			data[i][3] = new HTMLSimpleElement("<input size=\"4\" type=\"text\" id=\"maxdcid"+i+"\" " +
				" name=\"maxdcid"+i+"\" " +
				" class=\"standardTxt"+cssVirtualKeyboardClass+"\" value=\""+ strValue +"\" " +
				( rack.isNewAlg()
						? "/>"
						: " onkeydown=\"checkOnlyNumber(this,event);\" onblur=\"onlyNumberOnBlur(this);\"/>"
				)
			);
		}
		
		
		HTMLTable table = new HTMLTable("",header,data);
		table.setRowHeight(rowHeight);
		table.setAlignType(new int[]{0,1,1,1});
		int tab_width = width*65/100; 
		
		table.setColumnSize(0,tab_width*50/100);
		table.setColumnSize(1,tab_width*11/100);
		table.setColumnSize(2,tab_width*12/100);
		table.setColumnSize(3,tab_width*16/100);
		
		table.setHeight(height-550);
		table.setWidth(tab_width);		
		String tabella = table.getHTMLText() + "<input type='hidden' id='numdc' name='numdc' value='" + i + "' />";
		int idxerr;
		while((idxerr = tabella.indexOf("'err'"))!=-1)
			tabella = tabella.substring(0, idxerr)+"\\\"err\\\""+tabella.substring(idxerr+5);
		return tabella;
	}

	
	private static Map<Integer,Float> getTshMap(FSRack rack)
	{
		Map<Integer,Float> map = new HashMap<Integer,Float>();
		FSUtil[] utils = rack.getUtils();
		for(int i = 0; i < utils.length; i++) {
			int idTSH = utils[i].getIdTSH();
			try {
				float fValue = ControllerMgr.getInstance().getFromField(idTSH).getCurrentValue();
				map.put(utils[i].getIdutil(), fValue);
			} catch(Exception e) {
				LoggerMgr.getLogger(FSConfig.class).error(e);
			}
		}
		return map;
	}

}
