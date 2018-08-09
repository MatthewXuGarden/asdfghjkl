package com.carel.supervisor.plugin.energy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.vscheduler.VSCategory;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.base.config.BaseConfig;

public class EnergyProfile {
	public static final int TIMESLOT_NO = 8;
	public static final int TIMEPERIOD_NO = 12;
	
	EPTimeSlot aTimeSlots[];
	EPTimePeriod aTimePeriods[];
	EPTimePeriod tpException;
	private Vector<GregorianCalendar> objExceptions;

	private String strActiveXML = "default.etsc";

	
	public EnergyProfile()
	{
		// init time slots with defaults
		aTimeSlots = new EPTimeSlot[TIMESLOT_NO];		
		aTimeSlots[0] = new EPTimeSlot("T1", 0, "#FF0000");	// red
		aTimeSlots[1] = new EPTimeSlot("T2", 0, "#C00000");	// dark red
		aTimeSlots[2] = new EPTimeSlot("T3", 0, "#FFC000");	// orange
		aTimeSlots[3] = new EPTimeSlot("T4", 0, "#FFFF00");	// yellow
		aTimeSlots[4] = new EPTimeSlot("T5", 0, "#00B050");	// green
		aTimeSlots[5] = new EPTimeSlot("T6", 0, "#92D050");	// light green
		aTimeSlots[6] = new EPTimeSlot("T7", 0, "#A5A5A5");	// gray
		aTimeSlots[7] = new EPTimeSlot("T8", 0, "#D8D8D8");	// light gray
		// init time periods
		GregorianCalendar calNow = new GregorianCalendar();
		aTimePeriods = new EPTimePeriod[TIMEPERIOD_NO];
		for(int i = 0; i < TIMEPERIOD_NO; i++) {
			aTimePeriods[i] = new EPTimePeriod();
			GregorianCalendar calBegin = null, calEnd = null;
			switch(i) {
			case 0:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 0, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 0, 31);
				break;
			case 1:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 1, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 1, 28);
				break;
			case 2:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 2, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 2, 31);
				break;
			case 3:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 3, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 3, 30);
				break;
			case 4:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 4, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 4, 31);
				break;
			case 5:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 5, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 5, 30);
				break;
			case 6:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 6, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 6, 31);
				break;
			case 7:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 7, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 7, 31);
				break;
			case 8:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 8, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 8, 30);
				break;
			case 9:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 9, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 9, 31);
				break;
			case 10:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 10, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 10, 30);
				break;
			case 11:
				calBegin = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 11, 1); 
				calEnd = new GregorianCalendar(calNow.get(GregorianCalendar.YEAR), 11, 31);
				break;
			}
			aTimePeriods[i].setBeginPeriod(calBegin);
			aTimePeriods[i].setEndPeriod(calEnd);
		}
		tpException = new EPTimePeriod();
		// init exception list
		objExceptions = new Vector<GregorianCalendar>();
	}
	
	// parse XML and map it on internal data structures
	private boolean parseXML(String strXML)
	{
		try	{
			XMLNode xmlEnergyProfile = XMLNode.parse(strXML);
			if( xmlEnergyProfile.getNodeName().equals("EnergyProfile") ) {
				XMLNode axmlNodes[] = xmlEnergyProfile.getNodes();
				if( axmlNodes == null )
					return false;
				for(int i = 0; i < axmlNodes.length; i++) {
					String strNodeName = axmlNodes[i].getNodeName();
					if( strNodeName.equals("TimeSlots") ) {
						XMLNode axmlTimeSlots[] = axmlNodes[i].getNodes();
						if( axmlTimeSlots != null ) {
							for(int j = 0; j < axmlTimeSlots.length; j++) {
								if( !axmlTimeSlots[j].getNodeName().equals("TimeSlot") )
									continue;
								String strI = axmlTimeSlots[j].getAttribute("i");
								String strCost = axmlTimeSlots[j].getAttribute("cost");
								try {
									int iTimeSlot = Integer.parseInt(strI);
									double nCost = Double.parseDouble(strCost);
									aTimeSlots[iTimeSlot].setCost(nCost);
									String strName = axmlTimeSlots[j].getAttribute("name");
									if( strName != null )
										aTimeSlots[iTimeSlot].setName(strName);
									String strColor = axmlTimeSlots[j].getAttribute("color");
									if( strColor != null )
										aTimeSlots[iTimeSlot].setColor(strColor);
								} catch(NumberFormatException e) {
									LoggerMgr.getLogger(this.getClass()).error(e);
								}
							}
						}
					} // if( strNodeName.equals("TimeSlots") )
					else if( strNodeName.equals("TimePeriods") ) {
						XMLNode axmlTimePeriods[] = axmlNodes[i].getNodes();
						if( axmlTimePeriods != null ) {
							for(int j = 0; j < axmlTimePeriods.length; j++) {
								if( !axmlTimePeriods[j].getNodeName().equals("TimePeriod") )
									continue;
								String strI = axmlTimePeriods[j].getAttribute("i");
								EPTimePeriod obj = null;
								if( !strI.equals("ex") ) {
									try {
										int iTimePeriod = Integer.parseInt(strI);
										obj = aTimePeriods[iTimePeriod];
										String str = axmlTimePeriods[j].getAttribute("monthBegin");
										int nMonth = Integer.parseInt(str);
										str = axmlTimePeriods[j].getAttribute("dayBegin");
										int nDay = Integer.parseInt(str);
										GregorianCalendar calBegin = new GregorianCalendar();
										calBegin.set(GregorianCalendar.MONTH, nMonth);
										calBegin.set(GregorianCalendar.DAY_OF_MONTH, nDay);
										calBegin.set(GregorianCalendar.HOUR_OF_DAY, 0);
										calBegin.set(GregorianCalendar.MINUTE, 0);
										calBegin.set(GregorianCalendar.SECOND, 0);
										calBegin.set(GregorianCalendar.MILLISECOND, 0);
										obj.setBeginPeriod(calBegin);
										str = axmlTimePeriods[j].getAttribute("monthEnd");
										nMonth = Integer.parseInt(str);
										str = axmlTimePeriods[j].getAttribute("dayEnd");
										nDay = Integer.parseInt(str);
										GregorianCalendar calEnd = new GregorianCalendar();
										calEnd.set(GregorianCalendar.MONTH, nMonth);
										calEnd.set(GregorianCalendar.DAY_OF_MONTH, nDay);
										calEnd.set(GregorianCalendar.HOUR_OF_DAY, 23);
										calEnd.set(GregorianCalendar.MINUTE, 59);
										calEnd.set(GregorianCalendar.SECOND, 59);
										calEnd.set(GregorianCalendar.MILLISECOND, 999);
										obj.setEndPeriod(calEnd);
									} catch(NumberFormatException e) {
										LoggerMgr.getLogger(this.getClass()).error(e);
									}
								} // if( !strI.equals("ex") )
								else {
									obj = tpException;
								}
								XMLNode axmlDays[] = axmlTimePeriods[j].getNodes();
								if( axmlDays != null ) {
									for(int k = 0; k < axmlDays.length; k++) {
										if( !axmlDays[k].getNodeName().equals("Day") )
											continue;
										String strIWeekDay = axmlDays[k].getAttribute("i");
										try {
											int nWeekDay = Integer.parseInt(strIWeekDay);
											for(int nHour = 0; nHour < 24; nHour++) {
												String strTS = axmlDays[k].getAttribute("h" + nHour);
												int iTimeSlot = Integer.parseInt(strTS);
												obj.setTimeSlot(nWeekDay, nHour, iTimeSlot);
											}
										} catch(NumberFormatException e) {
											LoggerMgr.getLogger(this.getClass()).error(e);
										}
									}
								}
							}
						}
					} // if( strNodeName.equals("TimePeriods") )
					else if( strNodeName.equals("ExceptionDays") ) {
						objExceptions.removeAllElements();
						XMLNode axmlExceptionDays[] = axmlNodes[i].getNodes();
						if( axmlExceptionDays != null ) {
							for(int j = 0; j < axmlExceptionDays.length; j++) {
								if( !axmlExceptionDays[j].getNodeName().equals("ExceptionDay") )
									continue;
								String strMonth = axmlExceptionDays[j].getAttribute("month");
								String strDay = axmlExceptionDays[j].getAttribute("day");
								try {
									int nMonth = Integer.parseInt(strMonth);
									int nDay = Integer.parseInt(strDay);
									GregorianCalendar gc = new GregorianCalendar();
									gc.set(GregorianCalendar.MONTH, nMonth);
									gc.set(GregorianCalendar.DAY_OF_MONTH, nDay);
									gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
									gc.set(GregorianCalendar.MINUTE, 0);
									gc.set(GregorianCalendar.SECOND, 0);
									gc.set(GregorianCalendar.MILLISECOND, 0);
									objExceptions.add(gc);
								} catch(NumberFormatException e) {
									LoggerMgr.getLogger(this.getClass()).error(e);
								}
							}
						}
					} // if( strNodeName.equals("ExceptionDays") )
				}
			} // if( xmlEnergyProfile.getNodeName().equals("EnergyProfile") )
			else {
				return false;
			}
		}
		catch (Exception e)	{
			LoggerMgr.getLogger(this.getClass()).error(e);
			LoggerMgr.getLogger(this.getClass()).info(strXML);
			return false;
		}
		return true;
	}
	
	
	// load xml from internal configuration folder
	// called during engine startup
	public synchronized boolean loadXML(String strFileName)
	{
		strActiveXML = strFileName;
		String strFilePath = BaseConfig.getCarelPath() + "Config" + File.separator
			+ strActiveXML;
		// read xml file
		StringBuffer strbufImpExp = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(strFilePath)), "UTF8"));
			String strLine;
			while( (strLine = br.readLine()) != null )
				strbufImpExp.append(strLine);
			br.close();
			
			return parseXML(strbufImpExp.toString());
		}
		catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return false;
	}
	
	
	// called from web UI, active xml file it is used to keep changes
	public synchronized boolean saveXML(String strXML)
	{
		if( parseXML(strXML) ) {
			String strFilePath = BaseConfig.getCarelPath() + "Config\\"
			+ strActiveXML;
			return exportXML(strFilePath);
		}
		return false;
	}

	
	public synchronized boolean importXML(String strFileName)
	{
		// read xml file
		StringBuffer strbufImpExp = new StringBuffer();
		try {
			File file = new File(strFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			String strLine;
			while( (strLine = br.readLine()) != null )
				strbufImpExp.append(strLine);
			br.close();
			
			boolean bParse = parseXML(strbufImpExp.toString());
			if( bParse ) {
				String strFilePath = BaseConfig.getCarelPath() + "Config";
				File folder = new File(strFilePath);
				folder.mkdir();
				strFilePath += "\\" + file.getName();
				if( exportXML(strFilePath) ) {
					strActiveXML = file.getName();
					EnergyMgr.getInstance().setProperty("energyprofile", strActiveXML);
					return true;
				}
			}
		}
		catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		return false;
	}
	
	
	// XMLNode class not used because the code was ported from JS side
	// and it looks better on text file
	public synchronized boolean exportXML(String strFileName)
	{
		// prepare xml data		
		StringBuffer strXML = new StringBuffer();
		strXML.append("<?xml version=\"1.0\"?>\r\n");
		// energy profile
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		strXML.append("<EnergyProfile date=\"" + sdf.format(Calendar.getInstance().getTime()) + "\">\r\n");
		
		// time slots
		strXML.append("\t<TimeSlots>\r\n");
		for(int iTimeSlot = 0; iTimeSlot < TIMESLOT_NO; iTimeSlot++) {
			strXML.append("\t\t<TimeSlot i=\"" + iTimeSlot + "\" name=\""
				+ aTimeSlots[iTimeSlot].getName() + "\" cost=\""
				+ aTimeSlots[iTimeSlot].getCost() + "\" color=\""
				+ aTimeSlots[iTimeSlot].getColor() + "\" />\r\n");
		}
		strXML.append("\t</TimeSlots>\r\n");

		// time periods
		strXML.append("\t<TimePeriods>\r\n");
		for(int iTimePeriod = 0; iTimePeriod < TIMEPERIOD_NO + 1; iTimePeriod++) {
			strXML.append("\t\t<TimePeriod i=\"" + (iTimePeriod < TIMEPERIOD_NO ? iTimePeriod : "ex") + "\"");
			EPTimePeriod obj = null;
			if( iTimePeriod < TIMEPERIOD_NO )
				obj = aTimePeriods[iTimePeriod];
			else
				obj = tpException;
			if( iTimePeriod < TIMEPERIOD_NO ) {
				GregorianCalendar calBegin = obj.getBeginPeriod();
				strXML.append(" monthBegin=\"" + calBegin.get(GregorianCalendar.MONTH) + "\"");
				strXML.append(" dayBegin=\"" + calBegin.get(GregorianCalendar.DAY_OF_MONTH) + "\"");
				GregorianCalendar calEnd = obj.getEndPeriod();
				strXML.append(" monthEnd=\"" + calEnd.get(GregorianCalendar.MONTH) + "\"");
				strXML.append(" dayEnd=\"" + calEnd.get(GregorianCalendar.DAY_OF_MONTH) + "\"");
			}
			strXML.append(">\r\n");
			for(int iRow = 0; iRow < 7; iRow++) { // 7 week days
				strXML.append("\t\t\t<Day i=\"" + iRow + "\"");			
				for(int iCol = 0; iCol < 24; iCol++) // 24 day hours
					strXML.append(" h" + iCol + "=\"" + obj.getTimeSlot(iRow, iCol) + "\"");
				strXML.append("/>\r\n");
			}
			strXML.append("\t\t</TimePeriod>\r\n");	
		}
		strXML.append("\t</TimePeriods>\r\n");
		
		// exception days
		strXML.append("\t<ExceptionDays>\r\n");
		for(int i = 0; i < objExceptions.size(); i++) {
			GregorianCalendar calExDay = objExceptions.get(i);
			strXML.append("\t\t<ExceptionDay month=\"" + calExDay.get(GregorianCalendar.MONTH)
				+ "\" day=\"" +	calExDay.get(GregorianCalendar.DAY_OF_MONTH) + "\" />\r\n");
		}
		strXML.append("\t</ExceptionDays>\r\n");
		
		strXML.append("</EnergyProfile>\r\n");
		
		// write xml file
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(strFileName)), "UTF8"), true);
			pw.write(strXML.toString());
			pw.close();
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return false;
		}
		
		return true;
	}
	
	
	public synchronized EPTimeSlot getTimeSlot(int iTimeSlot)	{ return aTimeSlots[iTimeSlot]; }
	public synchronized double getCost(int iTimeSlot)			{ return aTimeSlots[iTimeSlot].getCost(); }
	
	
	public synchronized void addException(GregorianCalendar cal)
	{
		if( !isException(cal) )
			objExceptions.add(cal);
	}
	
	
	public synchronized void removeAllExceptions()
	{
		objExceptions.removeAllElements();
	}
	
	
	public synchronized boolean isException(GregorianCalendar cal)
	{
		if( objExceptions.isEmpty() )
			return false;
		
		int nMonth = cal.get(GregorianCalendar.MONTH);
		int nDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
		for(int i = 0; i < objExceptions.size(); i++) {
			GregorianCalendar cal_i = objExceptions.get(i);
			if( nMonth == cal_i.get(GregorianCalendar.MONTH)
				&& nDay == cal_i.get(GregorianCalendar.DAY_OF_MONTH) )
				return true;
		}
		
		return false;
	}
	
	
	public EPTimeSlot getCurrentTimeSlot()
	{
		GregorianCalendar calNow = new GregorianCalendar();
		return getTimeSlot(calNow);
	}
	
	
	public int getCurrentTimeSlotIndex()
	{
		GregorianCalendar calNow = new GregorianCalendar();
		return getTimeSlotIndex(calNow);
	}
	
	
	public EPTimeSlot getTimeSlot(GregorianCalendar cal)
	{
		return aTimeSlots[getTimeSlotIndex(cal)];
	}
	

	public synchronized int getTimeSlotIndex(GregorianCalendar cal)
	{
		if( isException(cal) ) {
			return tpException.getTimeSlot(cal);
		}
		else {
			for(int i = 0; i < aTimePeriods.length; i++)
				if( aTimePeriods[i].isTimePeriod(cal) )
					return aTimePeriods[i].getTimeSlot(cal);
		}
		return -1;
	}

	
	
	public synchronized String getTimePeriodsJS()
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("var aTimePeriods = new Array(\n");
		for(int i = 0; i < aTimePeriods.length; i++) {
			if( i > 0 )
				strbuff.append(",");
			strbuff.append(aTimePeriods[i].getTimePeriodJS());
		}
		strbuff.append(",");
		strbuff.append(tpException.getTimePeriodJS());
		strbuff.append(");\n");
		return strbuff.toString();
	}
	
	
	public String getExDaysHtmlTable(String language)
	{
		LangService lang = LangMgr.getInstance().getLangService(language);
		String astrMonths[] = {
			lang.getString("cal","january"),
			lang.getString("cal","february"),
			lang.getString("cal","march"),
			lang.getString("cal","april"),
			lang.getString("cal","may"),
			lang.getString("cal","june"),
			lang.getString("cal","july"),
			lang.getString("cal","august"),
			lang.getString("cal","september"),
			lang.getString("cal","october"),
			lang.getString("cal","november"),
			lang.getString("cal","december")
		};
		
		// data
		HTMLElement[][] data = new HTMLElement[objExceptions.size()][];
		String[] astrClickRowFunction = new String[objExceptions.size()];
		for(int i = 0; i < objExceptions.size(); i++) {
			GregorianCalendar cal = objExceptions.get(i);
			data[i] = new HTMLElement[3];
			data[i][0] = new HTMLSimpleElement("" + cal.get(GregorianCalendar.DAY_OF_MONTH));
			data[i][1] = new HTMLSimpleElement(astrMonths[cal.get(GregorianCalendar.MONTH)]);
			data[i][2] = new HTMLSimpleElement("" + cal.get(GregorianCalendar.MONTH));
			astrClickRowFunction[i] = "" + i;			
		}
		// header
		String[] headerTable = new String[2];
        headerTable[0] = lang.getString("energy", "day");
        headerTable[1] = lang.getString("energy", "month");
        // table
        HTMLTable table = new HTMLTable("tableExDays", headerTable, data);
        table.setTableId(1);
        table.setSgClickRowAction("onSelectExDay('$1')");
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onDeleteExDay('$1')");
        table.setDlbClickRowFunction(astrClickRowFunction);
        table.setWidth(290);
        table.setHeight(275);
        table.setColumnSize(0, 50);
        table.setColumnSize(1, 200);
        table.setAlignType(new int[] { 1, 0, 0 });
        return table.getHTMLText();
	}
	
}
