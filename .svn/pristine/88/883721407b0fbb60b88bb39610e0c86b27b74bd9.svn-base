package com.carel.supervisor.presentation.alarms;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateDifference;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLog;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bo.helper.SiteListHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AlarmCalledOfList
{
    private static final int columns = 10;
    private int rows;
    private List alarmsCalledOf = new ArrayList();
    private List alarmsIdVar = new ArrayList();
    private int width = 0;
    private int height = 0;
    private int pageNumber = 1;
    private int pageTotal = 1;
    private boolean visibility = true;
    private String titleTable = "";
	private Integer page_rows = null;
    private int screenw = 1024;
    private int screenh = 768;

    private int nPriorityCol = -1;
    
    
    public AlarmCalledOfList()
    {
    }

    public AlarmCalledOfList(String titleTab)
    {
        this.titleTable = titleTab;
    }

    public void loadCalledOfFromDataBase(UserSession userSession, int numPage)
    {
        try
        {
            this.visibility = userSession.localVisibility();

            AlarmLogList alarmLogList = null;
            DeviceStructureList deviceStructureList = userSession.getGroup()
                                                                 .getDeviceStructureList();

            int[] idDev = userSession.getTransaction().getIdDevices();

            if (!this.visibility)
            {
                alarmLogList = page_rows != null 
                	? new AlarmLogList(userSession.getLanguage(),
                        null, BaseConfig.getPlantId(), false, page_rows.intValue(), numPage)
                	: new AlarmLogList(userSession.getLanguage(),
                        null, BaseConfig.getPlantId(), false, numPage);
            }
            else
            {
                alarmLogList = page_rows != null
                	? new AlarmLogList(userSession.getLanguage(),
                        null, BaseConfig.getPlantId(), userSession.getIdSite(),
                        false, page_rows.intValue(), idDev, numPage)
            		: new AlarmLogList(userSession.getLanguage(),
                        null, BaseConfig.getPlantId(), userSession.getIdSite(),
                        false, idDev, numPage);
            }

            int[] idDevice = alarmLogList.getDeviceId();
            pageNumber = alarmLogList.getPageNumber();
            pageTotal	= alarmLogList.getPageTotal();
            if (null != idDevice)
            {
                AlarmLog alarmLog = null;
                Alarm alarm = null;
                int[] idvar = new int[alarmLogList.size()];

                for (int i = 0; i < alarmLogList.size(); i++)
                {
                    alarmLog = alarmLogList.getByPosition(i);

                    if (!this.visibility)
                    {
                        alarm = new Alarm(alarmLog, alarmLog.getDeviceDesc());
                    }
                    else
                    {
                        alarm = new Alarm(alarmLog,
                                deviceStructureList.get(idDevice[i]));
                    }

                    idvar[i] = alarmLog.getIdvariable();
                    addAlarmCalledOf(alarm, idvar[i]);
                }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }

    
    private String[] getHeaderAlarmCalledOfTable(String LanguageUsed,
        boolean group_info)
    {
        LangService temp = LangMgr.getInstance().getLangService(LanguageUsed);
        String[] intestation = null;
        int idx = 0;

        int group_col = 0;

        if (!group_info)
        {
            group_col = -1;
        }

        if (!this.visibility)
        {
            intestation = new String[columns + group_col + 1];
        }
        else
        {
            intestation = new String[columns + group_col];
        }

        intestation[idx++] = temp.getString("alarmCalledOf", "start");
        intestation[idx++] = temp.getString("alarmCalledOf", "end");
        intestation[idx++] = temp.getString("alarmCalledOf", "elapsed");

        if (!this.visibility)
        {
            intestation[idx++] = temp.getString("alarmTable", "localname");
        }

        intestation[idx++] = temp.getString("alarmCalledOf", "device");
        intestation[idx++] = temp.getString("alarmCalledOf", "description");

        if (group_info)
        {
            intestation[idx++] = temp.getString("alarmCalledOf", "group");
        }

        nPriorityCol = idx;
        intestation[idx++] = temp.getString("alarmCalledOf", "state");
        intestation[idx++] = temp.getString("alarmCalledOf", "ack");
        intestation[idx++] = temp.getString("alarmCalledOf", "cancel");
        intestation[idx++] = temp.getString("alarmCalledOf", "reset");

        return intestation;
    }

    private HTMLElement[][] getAlarmCalledOfTable(String LanguageUsed,
        boolean group_info)
    {
        HTMLElement[][] tableData = null;
        String tmp = "";
        String nota = "";

        SiteListHelper help = new SiteListHelper();

        int group_col = 0;

        if (!group_info)
        {
            group_col = -1;
        }

        try
        {
            help.load();
        }
        catch (Exception e)
        {
        }

        rows = alarmsCalledOf.size();

        int idx = 0;

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            LangService lan = LangMgr.getInstance().getLangService(LanguageUsed);

            for (int i = 0; i < rows; i++)
            {
                idx = 0;

                Alarm temp = ((Alarm) alarmsCalledOf.get(i));

                if (!this.visibility)
                {
                    tableData[i] = new HTMLElement[columns + group_col + 1];
                }
                else
                {
                    tableData[i] = new HTMLElement[columns + group_col];
                }

                tableData[i][idx++] = new HTMLSimpleElement(
                        "<div style='visibility:hidden;display:none'>" +
                        getLongDate(temp.getStarttime(), null) + "</div>" +
                        temp.formatDate(temp.getStarttime()));
                tableData[i][idx++] = new HTMLSimpleElement(
                        "<div style='visibility:hidden;display:none'>" +
                        getLongDate(temp.getEndtime(), null) + "</div>" +
                        temp.formatDate(temp.getEndtime()));

                if (temp.getEndtime() != null)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(
                            "<div style='visibility:hidden;display:none'>" +
                            getLongDate(temp.getStarttime(), temp.getEndtime()) +
                            "</div>" +
                            (new DateDifference(temp.getStarttime(),
                                temp.getEndtime())).format(LanguageUsed));
                }
                else
                {
                    tableData[i][idx++] = new HTMLSimpleElement("");
                }

                if (!this.visibility)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(help.getName(
                                new Integer(temp.getSite())));
                }

                tableData[i][idx++] = new HTMLSimpleElement(temp.getAlarmDevice());
                //tableData[i][idx++] = new HTMLSimpleElement(temp.getAlarmVariable());

                tmp = temp.getAlarmVariable();
                
                try
                {
					NoteLogList listaNote = new NoteLogList();
					listaNote.retrieve(temp.getSite(), "hsalarm", temp.getId());
					
					if (listaNote.size() > 0)
					{
						nota = Replacer.replace(listaNote.getNote(listaNote.size() - 1).getNote(), "%!", " ");
						tmp = "<img src='images/event/mininote.gif' title='" + nota + "' /> " + tmp;
					}
				}
                catch (Exception e)
                {
                	Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
				}
                
                tableData[i][idx++] = new HTMLSimpleElement(tmp);
                
                
                if (group_info)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(temp.getGroup());
                }

                String priority = "";

                int StatePr1 = temp.getPriority();
                if(StatePr1 > 4)
                    StatePr1 = 4;
                switch (StatePr1)
                {
                case (1):
                    priority = lan.getString("alrview", "alarmstate1");

                    break;

                case (2):
                    priority = lan.getString("alrview", "alarmstate2");

                    break;

                case (3):
                    priority = lan.getString("alrview", "alarmstate3");

                    break;

                case (4):
                    priority = lan.getString("alrview", "alarmstate4");

                    break;
                }

                tableData[i][idx++] = new HTMLSimpleElement(priority);

                if (temp.getAckuser() != null)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(
                            "<img src='images/ok.gif'/>");
                }
                else
                {
                    tableData[i][idx++] = new HTMLSimpleElement("");
                }

                if (temp.getDelelactionuser() != null)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(
                            "<img src='images/ok.gif'/>");
                }
                else
                {
                    tableData[i][idx++] = new HTMLSimpleElement("");
                }

                if (temp.getResetuser() != null)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(
                            "<img src='images/ok.gif'/>");
                }
                else
                {
                    tableData[i][idx++] = new HTMLSimpleElement("");
                }
            }
        }

        return tableData;
    }

    private String getLongDate(Date in, Date out)
    {
        String ret = "";

        if (in != null)
        {
            ret = String.valueOf(in.getTime());

            if (out != null)
            {
                ret = String.valueOf((out.getTime() - in.getTime()));
            }
        }

        return ret;
    }

    private HTMLTable getHTMLAlarmCalledOfTablePrv(String tableName,
        String language, UserSession us)
    {
        String pv_type = BaseConfig.getProductInfo("type");
        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
            ? true : false;

        HTMLElement[][] data = getAlarmCalledOfTable(language, group_info);
        HTMLTable alarmTable = new HTMLTable(tableName,
                getHeaderAlarmCalledOfTable(language, group_info), data, true,
                true);

        alarmTable.setPage(true);
        alarmTable.setPageNumber(pageNumber);
        alarmTable.setPageTotal(pageTotal);

        alarmTable.setScreenH(screenh);
        alarmTable.setScreenW(screenw);
        
        alarmTable.setColumnSize(0, 130);
        alarmTable.setColumnSize(1, 120);
        alarmTable.setColumnSize(2, 110);
        alarmTable.setColumnSize(3, 140);
        
        if (!group_info)
        {
        alarmTable.setColumnSize(5, 95);
        alarmTable.setColumnSize(7, 15);
        }
        
        if ((null == data) || (0 == data.length))
        {
            alarmTable.setColumnSize(3, 140);
            alarmTable.setColumnSize(4, 240);
            alarmTable.setColumnSize(5, 97);
        }

        alarmTable.setHeight(height);
        alarmTable.setWidth(width);
        alarmTable.setTableId(2);
        if (group_info)
        {
        	alarmTable.setAlignType(new int[] { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 });
        }
        else
        {
        	alarmTable.setAlignType(new int[] {1,1,1,0,0,1,1,1,1 });
        }
        alarmTable.setTableTitle(this.titleTable);
        alarmTable.setTypeColum(0, HTMLTable.TDATA);
        alarmTable.setTypeColum(1, HTMLTable.TDATA);
        alarmTable.setTypeColum(2, HTMLTable.TDATA);

        String[] dblclick = new String[numAlarm()];
        String[] rowClasses = new String[numAlarm()];
        Alarm tmp = null;

        for (int i = 0; i < numAlarm(); i++)
        {
            tmp = (Alarm) alarmsCalledOf.get(i);

            if (tmp != null)
            {
                dblclick[i] = new String(tmp.getId() + "_" + tmp.getSite());
            }
            else
            {
                dblclick[i] = new String("nop");
            }

            //classe row
            int StatePr1 = tmp.getPriority();
            if(StatePr1 > 4)
                StatePr1 = 4;
//            if ((tmp.getAckuser() == null) &&
//                    SystemConfMgr.getInstance()
//                                     .get("priority_" + StatePr1)
//                                     .getValue().equals("TRUE"))
//            {
//                rowClasses[i] = "Row1_toAck";
//            }
//            else
//            {
//                rowClasses[i] = "Row1";
//            }
            
            if ((tmp.getEndtime() != null) || (tmp.getResettime() != null))
            {
                //statesAlarm[i] = "Row1";
                if ((tmp.getAckuser() == null) &&
                        SystemConfMgr.getInstance().get("priority_" + StatePr1)
                                         .getValue().equals("TRUE"))
                {
                    rowClasses[i] = "statoAllarme" + String.valueOf(StatePr1) +
                        "End_w";
                }
                else
                {
                    rowClasses[i] = "statoAllarme" + String.valueOf(StatePr1) +
                        "End_b";
                }
            }
            else
            {
                if ((tmp.getAckuser() == null) &&
                        SystemConfMgr.getInstance().get("priority_" + StatePr1)
                                         .getValue().equals("TRUE"))
                {
                    rowClasses[i] = "statoAllarme" + String.valueOf(StatePr1) +
                        "_w";
                }
                else
                {
                    rowClasses[i] = "statoAllarme" + String.valueOf(StatePr1) +
                        "_b";
                }
            }
            
            
            
            
            
        }

        if (dblclick.length == 0)
        {
            dblclick = new String[] { "nop" };
        }

        
        if (us.isMenuActive("alrview"))
        {
        	alarmTable.setSgClickRowAction(
            "top.frames['manager'].loadTrx('nop&folder=alrview&bo=BAlrView&type=click&id=$1&desc=ncode07');");
        	alarmTable.setSnglClickRowFunction(dblclick);
        }
        alarmTable.setRowsClasses(rowClasses);

        return alarmTable;
    }

    public String getHTMLAlarmCalledOfTable(String tableName, String language, UserSession us)
    {
        return (getHTMLAlarmCalledOfTablePrv(tableName, language,us)).getHTMLText();
    }

    public String getHTMLAlarmCalledOfTableRefresh(String tableName,
        String language,UserSession us)
    {
        return ((getHTMLAlarmCalledOfTablePrv(tableName, language,us)).getHTMLTextBufferRefresh()).toString();
    }

    public void addAlarmCalledOf(Alarm a, int idVar)
    {
        alarmsCalledOf.add(a);
        alarmsIdVar.add(new Integer(idVar));
    }

    public Alarm getAlarm(int i)
    {
        return (Alarm) alarmsCalledOf.get(i);
    }

    public int numAlarm()
    {
        return alarmsCalledOf.size();
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public List getListaAllarmi()
    {
        return alarmsCalledOf;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }
    
    public void setPageNumber(int page)
    {
    	this.pageNumber=page;
    }
    public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	
	public int getPriorityCol()
	{
		return nPriorityCol;
	}

	public void setPageRows(Integer page_rows)
	{
		this.page_rows = page_rows;
	}

	public Integer getPageRows()
	{
		return this.page_rows;
	}
}
