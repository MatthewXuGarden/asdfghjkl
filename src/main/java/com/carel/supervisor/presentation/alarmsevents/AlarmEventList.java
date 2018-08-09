package com.carel.supervisor.presentation.alarmsevents;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateDifference;
import com.carel.supervisor.base.conversion.DateUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlarmEventList
{
    private static final int columns = 7;
    private static final int columns_find = 6;
    private int rows;
    private List alarmsevents = new ArrayList();
    private List alarmsIdVar = new ArrayList();
    private int width = 0;
    private int height = 0;
    private String title = "";
    private int pageNumber = 1;
    private int pageTotal = 1;
	private boolean visibility = true;
	private int size = 0;
    
    private int screenw = 1024;
    private int screenh = 768;

    public AlarmEventList()
    {
    }

    public AlarmEventList(String title)
    {
        this.title = title;
    }
    
    public int getSize()
    {
    	return this.alarmsevents.size();
    }
    
    public void setScreenH(int a) {
    	this.screenh = a;
    }
    
    public void setScreenW(int a) {
    	this.screenw = a;
    }
    
    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void setTitle(String tit)
    {
        this.title = tit;
    }

    public AlarmLogList loadFromDataBaseByIdAlarm(UserSession userSession,
        int idAlarm) throws Exception
    {
        return loadFromDataBaseByIdAlarm(userSession, idAlarm,
            userSession.getIdSite());
    }

    public AlarmLogList loadFromDataBaseByIdAlarm(UserSession userSession,
        int idAlarm, int idSite) throws Exception
    {
        this.visibility = userSession.localVisibility();

        DeviceStructureList deviceStructureList = userSession.getGroup()
                                                             .getDeviceStructureList();

        AlarmLogList alarmLogList = new AlarmLogList();
        alarmLogList.retriveByAlarmId(userSession.getLanguage(), null,
            BaseConfig.getPlantId(), idSite, true, idAlarm);

        AlarmLog alarmLog = null;
        AlarmEvent alarmevent = null;
        int[] idvar = new int[alarmLogList.size()];
        Map map = new HashMap();

        for (int i = 0; i < alarmLogList.size(); i++)
        {
            alarmLog = alarmLogList.getByPosition(i);
            alarmevent = new AlarmEvent(alarmLog,
                    deviceStructureList.get(alarmLog.getIddevice()));
            addAlarmEvent(alarmevent);
            idvar[i] = alarmLog.getIdvariable();
            map.put(new Integer(idvar[i]), alarmevent);
        }

        return alarmLogList;
    }
    
    /*private int loadTotalNumberOfAlarms(boolean isActive)
    {
    	int totalNumber =0;
    	String sql = "select count(1) from hsalarm,cfdevice where cfdevice where " +
    			"hsalarm.iddevice = cfdevice.iddevice and cfdevice.iscancelled='FALSE' and " +
    			"(hsalarm.endtime is null) and (hsalarm.resettime is null)";
    	if (isActive)
        {
            sql+= " and (hsalarm.endtime is null) and (hsalarm.resettime is null)";
        }
        else
        {
            sql+= " and ((hsalarm.endtime is not null) or (hsalarm.resettime is not null))";
        }
    	return totalNumber;
    }
    */
    public void loadFromDataBase(UserSession userSession, int numPage)
    {
        this.visibility = userSession.localVisibility();

        try
        {
            AlarmLogList alarmLogList = null;
            DeviceStructureList deviceStructureList = userSession.getGroup()
                                                                 .getDeviceStructureList();

            int[] idDev = userSession.getTransaction().getIdDevices();

            if (!this.visibility)
            {
                alarmLogList = new AlarmLogList(userSession.getLanguage(),
                        null, BaseConfig.getPlantId(), true, numPage);
            }
            else
            {
                alarmLogList = new AlarmLogList(userSession.getLanguage(),
                        null, BaseConfig.getPlantId(), userSession.getIdSite(),
                        true, idDev, numPage);
            }

            int[] idDevice = alarmLogList.getDeviceId();
            pageNumber = alarmLogList.getPageNumber();
            pageTotal = alarmLogList.getPageTotal();

            if (null != idDevice)
            {
                AlarmLog alarmLog = null;
                AlarmEvent alarmevent = null;
                int[] idvar = new int[alarmLogList.size()];

                for (int i = 0; i < alarmLogList.size(); i++)
                {
                    alarmLog = alarmLogList.getByPosition(i);

                    if (!this.visibility)
                    {
                        alarmevent = new AlarmEvent(alarmLog, alarmLog.getDeviceDesc());
                    }
                    else
                    {
                        alarmevent = new AlarmEvent(alarmLog,
                                deviceStructureList.get(idDevice[i]));
                    }

                    idvar[i] = alarmLog.getIdvariable();
                    addAlarmEvent(alarmevent, idvar[i]);
                }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public List getListaAllAlarmsEvents()
    {
        return alarmsevents;
    }

    public void addAlarmEvent(AlarmEvent a)
    {
    	alarmsevents.add(a);
    }

    public void addAlarmEvent(AlarmEvent a, int idVar)
    {
    	alarmsevents.add(a);
        alarmsIdVar.add(new Integer(idVar));
    }

    public int numAlarmEvent()
    {
        return alarmsevents.size();
    }

    public AlarmEvent getAlarmEvent(int i)
    {
        return (AlarmEvent) alarmsevents.get(i);
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    private String[] getHeaderAlarmTable(String LanguageUsed, boolean group_info)
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

        intestation[idx++] = temp.getString("alarmTable", "date");

        if (!this.visibility)
        {
            intestation[idx++] = temp.getString("alarmTable", "localname");
        }

        intestation[idx++] = temp.getString("alarmTable", "alarmZone");
        intestation[idx++] = temp.getString("alarmTable", "alarmDescription");
        intestation[idx++] = temp.getString("alarmTable", "ack");

        if (group_info)
        {
            intestation[idx++] = temp.getString("alarmTable", "group");
        }

        intestation[idx++] = temp.getString("alarmTable", "state");
        intestation[idx++] = temp.getString("alarmTable", "cancel");

        return intestation;
    }

    private HTMLElement[][] getAlarmTable(String language, boolean group_info)
    {
        HTMLElement[][] tableData = null;
        SiteListHelper help = new SiteListHelper();
        LangService lan = LangMgr.getInstance().getLangService(language);
        String priority = null;
        String tmp = "";
        String nota = "";

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

        rows = alarmsevents.size();

        int idx = 0;

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            for (int i = 0; i < rows; i++)
            {
                idx = 0;

                AlarmEvent temp = ((AlarmEvent) alarmsevents.get(i));
                
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
                        getLongDate(temp.getDate()) + "</div>" +
                        temp.formatDate(temp.getDate()));

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
                
                if (temp.getAckuser() != null)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(
                            "<img src='images/ok.gif'/>");
                }
                else
                {
                    tableData[i][idx++] = new HTMLSimpleElement("");
                }
                
                if (group_info)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(temp.getGroup());
                }

                tableData[i][idx++] = new HTMLSimpleElement(priority);

                if (temp.getDelelactionuser() != null)
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

    private String getLongDate(Date in)
    {
        if (in != null)
        {
            return String.valueOf(in.getTime());
        }
        else
        {
            return "";
        }
    }

    public String[] getStateAlarmString()
    {
        String[] statesAlarm = new String[rows];
        AlarmEvent tmp = null;

        for (int i = 0; i < rows; i++)
        {
            tmp = (AlarmEvent) alarmsevents.get(i);

            int State = tmp.getPriority();
            // Se priorita maggiore di 4 allora la risetto al default (4)
            if(State > 4)
                State = 4;

//            if ((tmp.getEndtime() != null) || (tmp.getResettime() != null))
//            {
//                statesAlarm[i] = "Row1";
//            }
//            else
            if(tmp.getPriority()>0){
                if ((tmp.getAckuser() == null) &&
                        SystemConfMgr.getInstance().get("priority_" + State)
                                         .getValue().equals("TRUE"))
                {
                    statesAlarm[i] = "statoAllarme" + String.valueOf(State) +
                        "_w";
                }
                else
                {
                    statesAlarm[i] = "statoAllarme" + String.valueOf(State) +
                        "_b";
                }
            }else{
            	statesAlarm[i] = "";
            }
            
        }

        return statesAlarm;
    }

    private HTMLTable getHTMLAlarmEventTablePrv(String tableName, String language, UserSession us)
    {
        String pv_type = BaseConfig.getProductInfo("type");
        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
            ? true : false;

        HTMLElement[][] data = getAlarmTable(language, group_info);
        HTMLTable alevTable = new HTMLTable(tableName,
                getHeaderAlarmTable(language, group_info), data, true, true);
        alevTable.setPage(true);
        alevTable.setPageNumber(pageNumber);
        alevTable.setPageTotal(pageTotal);
        alevTable.setRowsClasses(getStateAlarmString());
        
        alevTable.setScreenH(this.screenh);
        alevTable.setScreenW(this.screenw);
        
        alevTable.setColumnSize(0, 130);
        alevTable.setColumnSize(1, 120);
        alevTable.setColumnSize(2, 110);
        alevTable.setColumnSize(3, 140);
        alevTable.setColumnSize(4, 140);
        alevTable.setAlignType(3, 1);
 
       // if ((null == data) || (0 == data.length))
        //{
        //    alevTable.setColumnSize(3, 140);
        //    alevTable.setColumnSize(4, 240);
       //     alevTable.setColumnSize(5, 97);
      //  }

        alevTable.setHeight(height);
        alevTable.setWidth(width);
        alevTable.setTableId(9);
        alevTable.setTableTitle(this.title);
        alevTable.setTypeColum(0, HTMLTable.TDATA);

        String[] dblclick = new String[numAlarmEvent()];
        AlarmEvent tmp = null;

        for (int i = 0; i < numAlarmEvent(); i++)
        {
            tmp = (AlarmEvent) alarmsevents.get(i);

            if (tmp!=null && tmp.getT()!=null && !tmp.getT().equals("P"))
            {
                dblclick[i] = new String(tmp.getId() + "_" + tmp.getSite());
            }
            else
            {
                dblclick[i] = new String("nop");
            }
        }

        if (dblclick.length == 0)
        {
            dblclick = new String[] { "nop" };
        }
        
        if (us.isMenuActive("alrview"))
        {
        alevTable.setSgClickRowAction(
            "top.frames['manager'].loadTrx('alarmdescription/AlarmFrameset.jsp&folder=alrview&bo=BAlrView&type=click&id=$1&desc=ncode02');");
        alevTable.setSnglClickRowFunction(dblclick);
        }
        return alevTable;
    }

    public String getHTMLAlarmTable(String tableName, String language, UserSession us)
    {
        return getHTMLAlarmEventTablePrv(tableName, language,us).getHTMLText().toString();
    }

    public String getHTMLAlarmTableRefresh(String tableName, String language,UserSession us)
    {
        return getHTMLAlarmEventTablePrv(tableName, language,us)
                   .getHTMLTextBufferRefresh().toString();
    }

    //########################
    //tabella allarmi trovati
    //#######################
    public String getHTMLAlarmEventFindTableRefresh(String tableName, UserSession us)
    {
        return getHTMLAlarmEventFindTablePrv(tableName, us)
                   .getHTMLTextBufferRefresh().toString();
    }

    
    public String getHTMLAlarmEventFindTable(String tableName, UserSession us)
    {
        return getHTMLAlarmEventFindTablePrv(tableName, us).getHTMLText()
                   .toString();
    }

    private HTMLTable getHTMLAlarmEventFindTablePrv(String tableName, UserSession us)
    {
    	String language = us.getLanguage();
        HTMLElement[][] data = getAlarmEventFindTable(us);
        HTMLTable alevTable = new HTMLTable(tableName,
        		getHeaderAlarmEventFindTable(language), data, true, true);

        alevTable.setScreenH(screenh);
        alevTable.setScreenW(screenw);
        alevTable.setPage(true);
        alevTable.setPageNumber(pageNumber);
        alevTable.setPageTotal(pageTotal);
        alevTable.setColumnSize(0, 105);
        alevTable.setColumnSize(1, 85);
        alevTable.setColumnSize(2, 85);
        alevTable.setColumnSize(3, 50);
        alevTable.setColumnSize(4, 75);
        alevTable.setColumnSize(5, 105);
        
        alevTable.setHeight(height);
        alevTable.setWidth(width);
        alevTable.setTableId(9);
        alevTable.setAlignType(new int[] { 1, 0, 0, 1, 0, 1 });
       
        alevTable.setTableTitle(this.title);
        alevTable.setTypeColum(0, HTMLTable.TDATA);
        alevTable.setTypeColum(1, HTMLTable.TSTRING);
        alevTable.setTypeColum(2, HTMLTable.TSTRING);
        alevTable.setTypeColum(3, HTMLTable.TSTRING);
        alevTable.setTypeColum(4, HTMLTable.TSTRING);
        alevTable.setTypeColum(5, HTMLTable.TDATA);

        String[] dblclick = new String[numAlarmEvent()];
        String[] rowClasses = new String[numAlarmEvent()];
        AlarmEvent tmp = null;

        for (int i = 0; i < numAlarmEvent(); i++)
        {
            tmp = (AlarmEvent) alarmsevents.get(i);

            if (tmp!=null && tmp.getT()!=null && !tmp.getT().equals("P"))
            {
            	if (tmp.getAlarmDevice().equalsIgnoreCase(""))   //EVENT
            	{
            		if (us.isMenuActive("evndtl"))   //check user right
            		{
            			dblclick[i] = new String(""+tmp.getId() + ",0");
            		}
            		else
            			dblclick[i] = new String("nop" );
            	}
            	else    // ALARM
            	{
            		if (us.isMenuActive("alrview"))
            		{
            			dblclick[i] = new String(""+tmp.getId() + ",1");
            		}
            		else
            			dblclick[i] = new String("nop" );
            	}
            }
            else
            {
                dblclick[i] = new String("nop" );
            }

            //classe row
            int StatePr1 = tmp.getPriority();
            if(StatePr1 > 4)
                StatePr1 = 4;
            if(tmp.getPriority()>0){
	            if ((tmp.getAckuser() == null) &&
	                    SystemConfMgr.getInstance()
	                                     .get("priority_" + StatePr1)
	                                     .getValue().equals("TRUE"))
	            {
	                rowClasses[i] = "Row1_toAck";
	            }
	            else
	            {
	                rowClasses[i] = "Row1";
	            }
            }
        }

        if (dblclick.length == 0)
        {
            dblclick = new String[] { "nop" };
        }

        alevTable.setSgClickRowAction("goToAlarmOrEvent($1)");
        alevTable.setSnglClickRowFunction(dblclick);
        
//        alevTable.setRowsClasses(rowClasses);
        alevTable.setRowsClasses(getStateAlarmString());

        return alevTable;
    }

    private HTMLElement[][] getAlarmEventFindTable(UserSession us)
    {
    	String language = us.getLanguage();
        HTMLElement[][] tableData = null;
        String tmp = "";
        String nota = "";
        
        SiteListHelper help = new SiteListHelper();

        try
        {
            help.load();
        }
        catch (Exception e)
        {
        }

        rows = alarmsevents.size();

        int idx = 0;

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            LangService lan = LangMgr.getInstance().getLangService(language);

            for (int i = 0; i < rows; i++)
            {
                idx = 0;

                AlarmEvent temp = ((AlarmEvent) alarmsevents.get(i));

		        if(temp.getPriority()>0)
		        { //se � allarme
		                
		        	String priority = "";
		        	switch (temp.getPriority())
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
		        		if (!this.visibility)
		                {
		                    tableData[i] = new HTMLElement[columns_find + 1];
		                }
		                else
		                {
		                    tableData[i] = new HTMLElement[columns_find];
		                }
		                //from
		                tableData[i][idx++] = new HTMLSimpleElement(
		                        "<div style='visibility:hidden;display:none'>" +
		                        getLongDate(temp.getStarttime(), null) + "</div>" +
		                        temp.formatDate(temp.getStarttime()));
		                
		                if (!this.visibility)
		                {
		                    tableData[i][idx++] = new HTMLSimpleElement(help.getName(
		                                new Integer(temp.getSite())));
		                }
		                
		                //description
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
		                
		                //device
		                tableData[i][idx++] = new HTMLSimpleElement(temp.getAlarmDevice());
		                
		                //create a img link to detail page
		                if(temp.getAlarmDeviceID() != -1 && us.isMenuActive("dtlview"))
		                {
		                	String tooltip = lan.getString("alarmTable", "dtltooltip");
		                	tmp = "<img src='images/system/right.png' class='imglink' title='" + tooltip + "' onclick=event.stopPropagation();top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+temp.getAlarmDeviceID()+"&desc=ncode01') />";
		                }
		                else
		                {
		                    tmp = "";
		                }
		                tableData[i][idx++] = new HTMLSimpleElement(tmp);
		                
		                //user
		                /*
		                if (temp.getAckuser() != null)
		                {
		                    tableData[i][idx++] = new HTMLSimpleElement(
		                            "<img src='images/ok.gif'/>");
		                }
		                else
		                {
		                    tableData[i][idx++] = new HTMLSimpleElement("");
		                }
		                */
		                tableData[i][idx++] = new HTMLSimpleElement("<div style='visibility:hidden;display:none'>"+priority+"</div>");
		                
		                //to
		                tableData[i][idx++] = new HTMLSimpleElement(
		                        "<div style='visibility:hidden;display:none'>" +
		                        getLongDate(temp.getEndtime(), null) + "</div>" +
		                        temp.formatDate(temp.getEndtime()));
		                
		          }else{ // se � evento
		        	  try
		              {
		                tableData[i] = new HTMLElement[columns_find];
		                //from
		                tableData[i][idx++]= new HTMLSimpleElement(
		                        "<div style='visibility:hidden;display:none'>" +
		                        getLongDate(temp.getLastupdate()) + "</div>" +
		                        DateUtils.date2String(temp.getLastupdate(),
		                            "yyyy/MM/dd HH:mm:ss"));
		                //description
		                tmp = temp.getMessage();
		                NoteLogList listaNote = new NoteLogList();
		                listaNote.retrieve(temp.getIdSite(), "hsevent",temp.getIdevent());
		                
		                if (listaNote.size() > 0)
		                {
		                	nota = Replacer.replace(listaNote.getNote(listaNote.size() - 1).getNote(), "%!", " ");
		                	tmp = "<img src='images/event/mininote.gif' title='"+ nota +"' /> " + tmp;
		                }
		                tableData[i][idx++] = new HTMLSimpleElement(tmp);
		                
		                //category
		                tableData[i][idx++] = new HTMLSimpleElement(temp.getCategory());
		                
		                //al posto del link al device
		                tableData[i][idx++] = new HTMLSimpleElement("");
		                
		                //user
		                tableData[i][idx++] = new HTMLSimpleElement(temp.getUser()+"<div style='visibility:hidden;display:none'></div>");
		                tableData[i][idx++] = new HTMLSimpleElement("");
		                
		              }
		              catch (Exception e)
		              {
		                  Logger logger = LoggerMgr.getLogger(this.getClass());
		                  logger.error(e);
		              }
		          }
               
            }
        }

        return tableData;
    }

    private String[] getHeaderAlarmEventFindTable(String LanguageUsed)
    {
        LangService temp = LangMgr.getInstance().getLangService(LanguageUsed);
        String[] intestation = null;
        int idx = 0;

        if (!this.visibility)
        {
            intestation = new String[columns_find + 1];
        }
        else
        {
            intestation = new String[columns_find];
        }

        intestation[idx++] = temp.getString("alarmCalledOf", "start");
        if (!this.visibility)
        {
            intestation[idx++] = temp.getString("alarmTable", "localname");
        }
        intestation[idx++] = temp.getString("alarmCalledOf", "description");
        intestation[idx++] = temp.getString("alarmCalledOf", "device");
        intestation[idx++] = temp.getString("alarmTable", "link");
        intestation[idx++] = temp.getString("evnview", "user");
        intestation[idx++] = temp.getString("alarmCalledOf", "end");

        return intestation;
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
    public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
}
