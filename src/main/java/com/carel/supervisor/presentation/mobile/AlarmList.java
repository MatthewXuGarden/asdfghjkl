package com.carel.supervisor.presentation.mobile;


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
import com.carel.supervisor.presentation.alarms.Alarm;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bo.helper.SiteListHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.mobile.HTMLTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlarmList
{
    private static final int columns = 8;
    private static final int columns_find = 11;
    private static final int NUM_ALARMS_4_PAGE = 22;
    private int rows;
    private int num_ack;
    private List alarms = new ArrayList();
    private List alarmsIdVar = new ArrayList();
    private int width = 0;
    private int height = 0;
    private String title = "";
    private int pageNumber = 1;
    private int pageTotal = 1;
	private boolean visibility = true;
	private boolean isLink=true;//simon add for device dtl page,2009-5-8
	private boolean showAckCancel = true; // by Kevin
	//private boolean addLinkInDeviceName = false; //by Kevin

	private int screenw = 1024;
    private int screenh = 768;
    
    private int nPriorityCol = -1;

    public AlarmList()
    {
    }

    public AlarmList(String title)
    {
        this.title = title;
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
        Alarm alarm = null;
        int[] idvar = new int[alarmLogList.size()];
        Map map = new HashMap();

        for (int i = 0; i < alarmLogList.size(); i++)
        {
            alarmLog = alarmLogList.getByPosition(i);
            alarm = new Alarm(alarmLog,
                    deviceStructureList.get(alarmLog.getIddevice()));
            addAlarm(alarm);
            idvar[i] = alarmLog.getIdvariable();
            map.put(new Integer(idvar[i]), alarm);
        }

        return alarmLogList;
    }
    
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
                        null, BaseConfig.getPlantId(), true, NUM_ALARMS_4_PAGE, numPage);
            }
            else
            {
                alarmLogList = new AlarmLogList(userSession.getLanguage(),
                        null, BaseConfig.getPlantId(), userSession.getIdSite(),
                        true, NUM_ALARMS_4_PAGE, idDev, numPage);
            }

            int[] idDevice = alarmLogList.getDeviceId();
            pageNumber = alarmLogList.getPageNumber();
            pageTotal = alarmLogList.getPageTotal();

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
                    addAlarm(alarm, idvar[i]);
                }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public List getListaAllarmi()
    {
        return alarms;
    }

    public void addAlarm(Alarm a)
    {
        alarms.add(a);
    }

    public void addAlarm(Alarm a, int idVar)
    {
        alarms.add(a);
        alarmsIdVar.add(new Integer(idVar));
    }

    public int numAlarm()
    {
        return alarms.size();
    }

    public Alarm getAlarm(int i)
    {
        return (Alarm) alarms.get(i);
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
        int linkCol=0;
        if(isLink==false){
        	linkCol=-1;
        }
        int ack_cancel_info = 0;
        if(!this.showAckCancel)
        {
        	ack_cancel_info = -2;
        }
        if (!this.visibility)
        {
            intestation = new String[columns + group_col + 1+linkCol+ack_cancel_info];
        }
        else
        {
            intestation = new String[columns + group_col+linkCol+ack_cancel_info];
        }

        intestation[idx++] = temp.getString("alarmTable", "date");

        if (!this.visibility)
        {
            intestation[idx++] = temp.getString("alarmTable", "localname");
        }

        intestation[idx++] = temp.getString("alarmTable", "alarmZone");
        if(isLink==true){
        	intestation[idx++] = temp.getString("alarmTable", "link");
        }
        intestation[idx++] = temp.getString("alarmTable", "alarmDescription");

        nPriorityCol = idx;
        intestation[idx++] = temp.getString("alarmTable", "state");
        
        if(this.showAckCancel)
        {
	        intestation[idx++] = temp.getString("alarmTable", "ack");
	        intestation[idx++] = temp.getString("alarmTable", "cancel");
        }

        if (group_info)
        {
            intestation[idx++] = temp.getString("alarmTable", "group");
        }

        return intestation;
    }

    private HTMLElement[][] getAlarmTable(boolean group_info, UserSession us)
    {
    	String language = us.getLanguage();
        HTMLElement[][] tableData = null;
        SiteListHelper help = new SiteListHelper();
        LangService lan = LangMgr.getInstance().getLangService(language);
        String priority = null;
        String tmp = "";
        String nota = "";
        num_ack = 0;

        int group_col = 0;

        if (!group_info)
        {
            group_col = -1;
        }
        int linkCol=0;
        if(isLink==false){
        	linkCol=-1;
        }
        int ack_cancel_info = 0;
        if(!this.showAckCancel)
        {
        	ack_cancel_info = -2;
        }

        try
        {
            help.load();
        }
        catch (Exception e)
        {
        }

        rows = alarms.size();

        int idx = 0;

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            for (int i = 0; i < rows; i++)
            {
                idx = 0;

                Alarm temp = ((Alarm) alarms.get(i));
                
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
                    tableData[i] = new HTMLElement[columns + group_col + 1+linkCol+ack_cancel_info];
                }
                else
                {
                    tableData[i] = new HTMLElement[columns + group_col+linkCol+ack_cancel_info];
                }
                //datetime
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
                //link
                if(isLink==true){
	                if(temp.getAlarmIDDevice() != -1 && us.isMenuActive("dtlview")){
	                	String tooltip = lan.getString("alarmTable", "dtltooltip");
	                	tableData[i][idx++] = new HTMLSimpleElement("<img src='images/system/right.png' class='imglink' title='" + tooltip + "' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+temp.getAlarmIDDevice()+"&desc=ncode01') />");
	                }else{
	                	tableData[i][idx++] = new HTMLSimpleElement("");
	                }
                }
                
                //description
                tmp = temp.getAlarmVariable();
				NoteLogList listaNote = new NoteLogList();
                try
                {
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
                
               

                //priority
                tableData[i][idx++] = new HTMLSimpleElement(priority);

                if(this.showAckCancel)
                {
	                //ack
	                if (temp.getAckuser() != null)
	                {
	                    tableData[i][idx++] = new HTMLSimpleElement(
	                            "<img src='images/ok.gif'/>");
	                    this.num_ack++;
	                }
	                else
	                {
	                	String ack="";
	                	if ((temp.getEndtime()!=null) || (temp.getResettime()!=null)){
	        				ack="<img title='"+lan.getString("alrmng", "noterequired")+"' src='images/genericdanger.gif'/>";
	                	}
	                    tableData[i][idx++] = new HTMLSimpleElement(ack);
	                }
	                
	                //??
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
                
                if (group_info)
                {
                    tableData[i][idx++] = new HTMLSimpleElement(temp.getGroup());
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
        Alarm tmp = null;

        for (int i = 0; i < rows; i++)
        {
            tmp = (Alarm) alarms.get(i);

            int State = tmp.getPriority();
            // Se priorita maggiore di 4 allora la risetto al default (4)
            if(State > 4)
                State = 4;

            if ((tmp.getEndtime() != null) || (tmp.getResettime() != null))
            {
                //statesAlarm[i] = "Row1";
                if ((tmp.getAckuser() == null) &&
                        SystemConfMgr.getInstance().get("priority_" + State)
                                         .getValue().equals("TRUE"))
                {
                    statesAlarm[i] = "statoAllarme" + String.valueOf(State) +
                        "End_w";
                }
                else
                {
                    statesAlarm[i] = "statoAllarme" + String.valueOf(State) +
                        "End_b";
                }
            }
            else
            {
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
            }
        }

        return statesAlarm;
    }
    private HTMLTable getHTMLAlarmTablePrv(String tableName,UserSession us)
    {
    	return this.getHTMLAlarmTablePrv(tableName, us, 0,true);
    }
    private HTMLTable getHTMLAlarmTablePrv(String tableName, UserSession us,int rows,boolean setPage)
    {
    	String language = us.getLanguage();
        String pv_type = BaseConfig.getProductInfo("type");
        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
            ? true : false;

        HTMLElement[][] data = getAlarmTable(group_info, us);
        HTMLTable alarmTable = new HTMLTable(tableName,
                getHeaderAlarmTable(language, group_info), data, true, true);
        alarmTable.setPage(setPage);
        alarmTable.setPageNumber(pageNumber);
        alarmTable.setPageTotal(pageTotal);
        alarmTable.setRowsClasses(getStateAlarmString());
        if(rows != 0 && data != null && rows<data.length)
        {
        	alarmTable.setRows(rows);
        }
        
        alarmTable.setScreenH(this.screenh);
        alarmTable.setScreenW(this.screenw);
        
        int colspan=-1;
        if(isLink == true)
        {
        	alarmTable.setColumnSize(++colspan, 90);//0
        }
        else
        {
        	alarmTable.setColumnSize(++colspan, 120);//0
        }
        if(group_info){
        	if(isLink == true)
        	{
        		alarmTable.setColumnSize(++colspan, 124);//1
        	}
        	else
        	{
        		alarmTable.setColumnSize(++colspan, 200);//1
        	}
        }else{
        	if(isLink == true)
        	{
        		alarmTable.setColumnSize(++colspan, 229);
        	}
        	else
        	{
        		alarmTable.setColumnSize(++colspan, 300);
        	}
        }
        if(isLink==true){
        	alarmTable.setColumnSize(++colspan, 23);//2
        }
        if(isLink == true)
        {
        	if(this.showAckCancel)
        	{
        		alarmTable.setColumnSize(++colspan, 200);//3
        	}
        	else
        	{
        		alarmTable.setColumnSize(++colspan, 300);//3
        	}
        }
        else
        {
        	alarmTable.setColumnSize(++colspan, 300);//3
        }
        
    	alarmTable.setAlignType(++colspan, 1);//4
    	alarmTable.setColumnSize(colspan, 50);//4
    	if(!group_info){
        	alarmTable.setAlignType(colspan, 1);
        }
        
    	if(this.showAckCancel)
    	{
	        if(group_info){
		        alarmTable.setColumnSize(++colspan, 120);//5
		    }else{
	            alarmTable.setColumnSize(++colspan, 90);	
	            alarmTable.setAlignType(colspan, 1);
	        }
	        
	        if(group_info){
		       alarmTable.setColumnSize(++colspan, 100);//6
	        }else{
	            alarmTable.setColumnSize(++colspan, 50);	
	            alarmTable.setAlignType(colspan, 1);
	        }
    	}
 
        if (group_info){
            alarmTable.setColumnSize(++colspan, 50);//7	
            alarmTable.setAlignType(colspan, 1);//7
        }
        

        alarmTable.setHeight(height);
        alarmTable.setWidth(width);
        alarmTable.setTableId(1);
        alarmTable.setTableTitle(this.title);
        alarmTable.setTypeColum(0, HTMLTable.TDATA);

        String[] dblclick = new String[numAlarm()];
        Alarm tmp = null;

        for (int i = 0; i < numAlarm(); i++)
        {
            tmp = (Alarm) alarms.get(i);

            if (tmp != null)
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
	        alarmTable.setDbClickRowAction(
	            "top.frames['manager'].loadTrx('alarmdescription/AlarmFrameset.jsp&folder=alrview&bo=BAlrView&type=click&id=$1&desc=ncode02');");
	        alarmTable.setDlbClickRowFunction(dblclick);
        }
        
        return alarmTable;
    }

    public String getHTMLAlarmTable(String tableName, String language,UserSession us)
    {
    	return getHTMLAlarmTable(tableName,language,us,0,true);
    }
    public String getHTMLAlarmTable(String tableName, String language,UserSession us,int row,boolean setPage)
    {
    	return getHTMLAlarmTablePrv(tableName, us,row,setPage).getHTMLText().toString();
    }
    public String getHTMLAlarmTableRefresh(String tableName, String language,UserSession us)
    {
        return this.getHTMLAlarmTableRow(tableName, language,us,0,true);
    }
    public String getHTMLAlarmTableRow(String tableName, String language,UserSession us, int row, boolean setPage)
    {
        return getHTMLAlarmTablePrv(tableName,us,row,setPage)
                   .getHTMLTextBufferRefresh().toString();
    }

    //########################
    //tabella allarmi trovati
    //#######################
    public String getHTMLAlarmFindTableRefresh(String tableName, String language,UserSession us)
    {
        return getHTMLAlarmFindTablePrv(tableName, language,us)
                   .getHTMLTextBufferRefresh().toString();
    }

    
    public String getHTMLAlarmFindTable(String tableName, String language,UserSession us)
    {
        return getHTMLAlarmFindTablePrv(tableName, language,us).getHTMLText()
                   .toString();
    }

    private HTMLTable getHTMLAlarmFindTablePrv(String tableName, String language,UserSession us)
    {
        String pv_type = BaseConfig.getProductInfo("type");
        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
            ? true : false;

        HTMLElement[][] data = getAlarmFindTable(group_info, us);
        HTMLTable alarmTable = new HTMLTable(tableName,
                getHeaderAlarmFindTable(language, group_info), data, true, true);

        alarmTable.setScreenH(screenh);
        alarmTable.setScreenW(screenw);
        alarmTable.setPage(true);
        alarmTable.setPageNumber(pageNumber);
        alarmTable.setPageTotal(pageTotal);
        alarmTable.setColumnSize(0, 130);
        alarmTable.setColumnSize(1, 120);
        alarmTable.setColumnSize(2, 110);
        alarmTable.setColumnSize(3, 140);

        alarmTable.setColumnSize(6, 95);
        alarmTable.setColumnSize(8, 15);

        if ((null == data) || (0 == data.length))
        {
            alarmTable.setColumnSize(3, 140);
            alarmTable.setColumnSize(5, 240);
            alarmTable.setColumnSize(6, 97);
        }

        alarmTable.setHeight(height);
        alarmTable.setWidth(width);
        alarmTable.setTableId(1);
        if (group_info)
        	alarmTable.setAlignType(new int[] { 0, 0, 0, 0, 1,0, 0, 0, 1, 1, 1, 1, 1 }); // the fifth 1 is add by kevin
        else
        	alarmTable.setAlignType(new int[] { 1,1,1,0,1,0,1,1,1,1 });
       
        alarmTable.setTableTitle(this.title);
        alarmTable.setTypeColum(0, HTMLTable.TDATA);
        alarmTable.setTypeColum(1, HTMLTable.TDATA);
        alarmTable.setTypeColum(2, HTMLTable.TDATA);

        String[] dblclick = new String[numAlarm()];
        String[] rowClasses = new String[numAlarm()];
        Alarm tmp = null;

        for (int i = 0; i < numAlarm(); i++)
        {
            tmp = (Alarm) alarms.get(i);

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

        if (dblclick.length == 0)
        {
            dblclick = new String[] { "nop" };
        }

        if (us.isMenuActive("alrview"))
        {
	        alarmTable.setDbClickRowAction(
	            "top.frames['manager'].loadTrx('nop&folder=alrview&bo=BAlrView&type=click&id=$1&desc=ncode07');");
	        alarmTable.setDlbClickRowFunction(dblclick);
        }
        
        //alarmTable.setRowsClasses(rowClasses);
        alarmTable.setRowsClasses(getStateAlarmString());

        return alarmTable;
    }

    private HTMLElement[][] getAlarmFindTable(boolean group_info, UserSession us)
    {
    	String language = us.getLanguage();
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

        rows = alarms.size();

        int idx = 0;

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            LangService lan = LangMgr.getInstance().getLangService(language);

            for (int i = 0; i < rows; i++)
            {
                idx = 0;

                Alarm temp = ((Alarm) alarms.get(i));

                if (!this.visibility)
                {
                    tableData[i] = new HTMLElement[columns_find + group_col + 1];
                }
                else
                {
                    tableData[i] = new HTMLElement[columns_find + group_col];
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
                                temp.getEndtime())).format(language));
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
                
                //add by kevin, create a img link to detail page
                if(temp.getAlarmIDDevice() != -1 && us.isMenuActive("dtlview"))
                {
                	String tooltip = lan.getString("alarmTable", "dtltooltip");
                	tmp = "<img src='images/system/right.png' class='imglink' title='" + tooltip + "' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+temp.getAlarmIDDevice()+"&desc=ncode01') />";
                }
                else
                {
                	tmp = "";
                }
                tableData[i][idx++] = new HTMLSimpleElement(tmp);
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

    private String[] getHeaderAlarmFindTable(String LanguageUsed,
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
            intestation = new String[columns_find + group_col + 1];
        }
        else
        {
            intestation = new String[columns_find + group_col];
        }

        intestation[idx++] = temp.getString("alarmCalledOf", "start");
        intestation[idx++] = temp.getString("alarmCalledOf", "end");
        intestation[idx++] = temp.getString("alarmCalledOf", "elapsed");

        if (!this.visibility)
        {
            intestation[idx++] = temp.getString("alarmTable", "localname");
        }

        intestation[idx++] = temp.getString("alarmCalledOf", "device");
        intestation[idx++] = temp.getString("alarmTable", "link");//add by kevin, a img link to device detail 
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

	public boolean isLink() {
		return isLink;
	}

	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
	
	public int getNumAck()
	{
		return this.num_ack;
	}

	public void setShowAckCancel(boolean withAckCancel) {
		this.showAckCancel = withAckCancel;
	}
//	public void setAddLinkInDeviceName(boolean addLinkInDeviceName) {
//		this.addLinkInDeviceName = addLinkInDeviceName;
//	}
	
	public int getPriorityCol()
	{
		return nPriorityCol;
	}

}
