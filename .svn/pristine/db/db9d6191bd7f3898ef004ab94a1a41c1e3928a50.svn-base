package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.energy.EnergyModel;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class ClockBeanList {

	public ClockBean getClockByIddevmdl(int iddevmdl,String language)
	{
		String sql = "select cfclock.*,cfdevmdl.iddevmdl,devext.description as devd, masterext.description as masterd, yearext.description as yeard "+
		", monthext.description as monthd, dayext.description as dayd, weekdayext.description as weekdayd, hourext.description as hourd "+
		", minuteext.description as minuted "+
		"from cfclock "+
		"inner join cfdevmdl on cfdevmdl.code=cfclock.devcode "+
		"inner join cftableext as devext on devext.idsite=1 and devext.tablename='cfdevmdl' and devext.tableid=cfdevmdl.iddevmdl and devext.languagecode=? "+
		"left join cfvarmdl as master on master.iddevmdl=cfdevmdl.iddevmdl and master.code=cfclock.master "+
		"left join cftableext as masterext on masterext.idsite=1 and masterext.tablename='cfvarmdl' and masterext.tableid=master.idvarmdl and masterext.languagecode=? "+
		"left join cfvarmdl as year on year.iddevmdl=cfdevmdl.iddevmdl and year.code=cfclock.year "+
		"left join cftableext as yearext on yearext.idsite=1 and yearext.tablename='cfvarmdl' and yearext.tableid=year.idvarmdl and yearext.languagecode=? "+
		"left join cfvarmdl as month on month.iddevmdl=cfdevmdl.iddevmdl and month.code=cfclock.month "+
		"left join cftableext as monthext on monthext.idsite=1 and monthext.tablename='cfvarmdl' and monthext.tableid=month.idvarmdl and monthext.languagecode=? "+
		"left join cfvarmdl as day on day.iddevmdl=cfdevmdl.iddevmdl and day.code=cfclock.day "+
		"left join cftableext as dayext on dayext.idsite=1 and dayext.tablename='cfvarmdl' and dayext.tableid=day.idvarmdl and dayext.languagecode=? "+
		"left join cfvarmdl as weekday on weekday.iddevmdl=cfdevmdl.iddevmdl and weekday.code=cfclock.weekday "+
		"left join cftableext as weekdayext on weekdayext.idsite=1 and weekdayext.tablename='cfvarmdl' and weekdayext.tableid=weekday.idvarmdl and weekdayext.languagecode=? "+
		"left join cfvarmdl as hour on hour.iddevmdl=cfdevmdl.iddevmdl and hour.code=cfclock.hour "+
		"left join cftableext as hourext on hourext.idsite=1 and hourext.tablename='cfvarmdl' and hourext.tableid=hour.idvarmdl and hourext.languagecode=? "+
		"left join cfvarmdl as minute on minute.iddevmdl=cfdevmdl.iddevmdl and minute.code=cfclock.minute "+
		"left join cftableext as minuteext on minuteext.idsite=1 and minuteext.tablename='cfvarmdl' and minuteext.tableid=minute.idvarmdl and minuteext.languagecode=? "+
		"where cfdevmdl.iddevmdl=? ";
		try{
			Object[] params = new Object[]{language,language,language,language,language,language,language,language,iddevmdl};
			RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			Record record = null;
			for (int i = 0; i < recordset.size(); i++)
			{
				record = recordset.get(i);
				ClockBean bean = new ClockBean(true,record);
				boolean hasVar = false;
				for(String varCode:bean.varCode)
				{
					if(varCode != null && varCode.length()>0)
					{
						hasVar = true;
						break;
					}
				}
				if(hasVar)
					return bean;
			}
		}
		catch(DataBaseException ex)
		{}
		return null;
	}
	public List<ClockBean> load()
	{
		List<ClockBean> clockList = new ArrayList<ClockBean>();
		String sql = "select * from cfclock";
		try{
			RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
			Record record = null;
			for (int i = 0; i < recordset.size(); i++)
			{
				record = recordset.get(i);
				ClockBean bean = new ClockBean(record);
				boolean hasVar = false;
				for(String varCode:bean.varCode)
				{
					if(varCode != null && varCode.length()>0)
					{
						hasVar = true;
						break;
					}
				}
				if(hasVar)
				clockList.add(bean);
			}
		}
		catch(DataBaseException ex)
		{}
		return clockList;
	}
	
	public List<ClockBean> loadWithDescription(String language)
	{
		List<ClockBean> clockList = new ArrayList<ClockBean>();
		String sql = "select cfclock.*,cfdevmdl.iddevmdl,devext.description as devd, masterext.description as masterd, yearext.description as yeard "+
				", monthext.description as monthd, dayext.description as dayd, weekdayext.description as weekdayd, hourext.description as hourd "+
				", minuteext.description as minuted "+
				"from cfclock "+
				"inner join cfdevmdl on cfdevmdl.code=cfclock.devcode "+
				"inner join cftableext as devext on devext.idsite=1 and devext.tablename='cfdevmdl' and devext.tableid=cfdevmdl.iddevmdl and devext.languagecode=? "+
				"left join cfvarmdl as master on master.iddevmdl=cfdevmdl.iddevmdl and master.code=cfclock.master "+
				"left join cftableext as masterext on masterext.idsite=1 and masterext.tablename='cfvarmdl' and masterext.tableid=master.idvarmdl and masterext.languagecode=? "+
				"left join cfvarmdl as year on year.iddevmdl=cfdevmdl.iddevmdl and year.code=cfclock.year "+
				"left join cftableext as yearext on yearext.idsite=1 and yearext.tablename='cfvarmdl' and yearext.tableid=year.idvarmdl and yearext.languagecode=? "+
				"left join cfvarmdl as month on month.iddevmdl=cfdevmdl.iddevmdl and month.code=cfclock.month "+
				"left join cftableext as monthext on monthext.idsite=1 and monthext.tablename='cfvarmdl' and monthext.tableid=month.idvarmdl and monthext.languagecode=? "+
				"left join cfvarmdl as day on day.iddevmdl=cfdevmdl.iddevmdl and day.code=cfclock.day "+
				"left join cftableext as dayext on dayext.idsite=1 and dayext.tablename='cfvarmdl' and dayext.tableid=day.idvarmdl and dayext.languagecode=? "+
				"left join cfvarmdl as weekday on weekday.iddevmdl=cfdevmdl.iddevmdl and weekday.code=cfclock.weekday "+
				"left join cftableext as weekdayext on weekdayext.idsite=1 and weekdayext.tablename='cfvarmdl' and weekdayext.tableid=weekday.idvarmdl and weekdayext.languagecode=? "+
				"left join cfvarmdl as hour on hour.iddevmdl=cfdevmdl.iddevmdl and hour.code=cfclock.hour "+
				"left join cftableext as hourext on hourext.idsite=1 and hourext.tablename='cfvarmdl' and hourext.tableid=hour.idvarmdl and hourext.languagecode=? "+
				"left join cfvarmdl as minute on minute.iddevmdl=cfdevmdl.iddevmdl and minute.code=cfclock.minute "+
				"left join cftableext as minuteext on minuteext.idsite=1 and minuteext.tablename='cfvarmdl' and minuteext.tableid=minute.idvarmdl and minuteext.languagecode=? "+
				"order by devext.description";
		try{
			Object[] params = new Object[]{language,language,language,language,language,language,language,language};
			RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,params);
			Record record = null;
			for (int i = 0; i < recordset.size(); i++)
			{
				record = recordset.get(i);
				ClockBean bean = new ClockBean(true,record);
				boolean hasVar = false;
				for(String varCode:bean.varCode)
				{
					if(varCode != null && varCode.length()>0)
					{
						hasVar = true;
						break;
					}
				}
				if(hasVar)
				clockList.add(bean);
			}
		}
		catch(DataBaseException ex)
		{
			LoggerMgr.getLogger(this.getClass().getName()).error(ex);
		}
		return clockList;
	}
	
	public void removeClock(int iddevmdl)
	{
		String sql = "delete from cfclock where devcode in(select code from cfdevmdl where iddevmdl=?)";
		Object[] params = new Object[]{iddevmdl};
		try{
			DatabaseMgr.getInstance().executeStatement(sql, params);
		}catch(Exception ex)
		{
			LoggerMgr.getLogger(this.getClass()).error(ex);
		}
	}
	public void save(ClockBean clockBean) throws DataBaseException
	{
		String sql = "insert INTO cfclock(devcode, master,year, month, day, weekday, hour, minute)" 
			+ " VALUES (?,?, ?, ?, ?, ?, ?,?)";
		Object[] params = new Object[1+clockBean.getVarCode().length];
		params[0] = clockBean.getDevCode();
		for(int i=0;i<clockBean.getVarCode().length;i++)
			params[i+1] = clockBean.getVarCode()[i];
		DatabaseMgr.getInstance().executeStatement(null, sql, params);
	}
	public String getDeviceCombo(String lang)
	{
		StringBuffer combo = new StringBuffer();
		combo.append("<select name=\"devCode\" onchange=\"clock.loadVar(this);\"><option value=\"-1\">---------------------------------------------</option>");
		String sql = "select cftableext.tableid,cftableext.description from cfdevmdl "
				+ "inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.tablename='cfdevmdl' and cftableext.languagecode=? "
				+ "where cfdevmdl .code not in "
				+ "(select devcode from cfclock) " + "order by description";
		try{
			Object[] params = new Object[]{lang};
			RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,params);
			Record record = null;
			for (int i = 0; i < recordset.size(); i++)
			{
				record = recordset.get(i);
				int iddevmdl = (Integer)record.get("tableid");
				String description = (String)record.get("description");
				combo.append("<option value=\""+iddevmdl+"\">"+description+"</option>");
			}
		}catch(Exception ex)
		{
			
		}
		combo.append("</select>");
		return combo.toString();
	}
	public String getHtmlModelTable(UserSession session)
	{
		String language = session.getLanguage();
		LangService lang = LangMgr.getInstance().getLangService(language);

		// data
		List<ClockBean> clocks = loadWithDescription(language);
		HTMLElement[][] data = new HTMLElement[0][];
		String[] astrClickRowFunction = new String[0];
		if( clocks != null ) {
			data = new HTMLElement[clocks.size()][];
			astrClickRowFunction = new String[clocks.size()];
			for(int i = 0; i < clocks.size(); i++) {
				ClockBean clock = clocks.get(i);
				String[] varDescription = clock.getVarDescription();
				data[i] = new HTMLElement[8];
				astrClickRowFunction[i] = String.valueOf(clock.getIddevmdl());
				data[i][0] = new HTMLSimpleElement(clock.getDevDescription());
				data[i][1] = new HTMLSimpleElement(varDescription[ClockBean.MASTER]);
				data[i][2] = new HTMLSimpleElement(varDescription[ClockBean.YEAR]);
				data[i][3] = new HTMLSimpleElement(varDescription[ClockBean.MONTH]);
				data[i][4] = new HTMLSimpleElement(varDescription[ClockBean.DAY]);
				data[i][5] = new HTMLSimpleElement(varDescription[ClockBean.WEEKDAY]);
				data[i][6] = new HTMLSimpleElement(varDescription[ClockBean.HOUR]);
				data[i][7] = new HTMLSimpleElement(varDescription[ClockBean.MINUTE]);
			}
		}
		
		// header
		String[] headerTable = new String[8];
        headerTable[0] = lang.getString("clock", "device");
        headerTable[1] = lang.getString("clock", "master");
        headerTable[2] = lang.getString("clock", "year");
        headerTable[3] = lang.getString("clock", "month");
        headerTable[4] = lang.getString("clock", "day");
        headerTable[5] = lang.getString("clock", "weekday");
        headerTable[6] = lang.getString("clock", "hour");
        headerTable[7] = lang.getString("clock", "minute");
        
        // table
        HTMLTable table = new HTMLTable("modelTable", headerTable, data);
        table.setTableId(1);
        table.setSgClickRowAction("clock.onSelectModel('$1')");
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("clock.onModifyModel('$1')");
        table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(session.getScreenWidth());
        table.setScreenH(session.getScreenHeight());
        table.setHeight(220);
        table.setColumnSize(0, 10);
        table.setColumnSize(1, 10);
        table.setColumnSize(2, 10);
        table.setColumnSize(3, 10);
        table.setColumnSize(4, 10);
        table.setColumnSize(5, 10);
        table.setColumnSize(6, 10);
        table.setColumnSize(7, 10);
        table.setWidth(900);
        return table.getHTMLText();
	}
}
