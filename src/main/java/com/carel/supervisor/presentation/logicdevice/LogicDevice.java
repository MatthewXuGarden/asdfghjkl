package com.carel.supervisor.presentation.logicdevice;


import java.util.ArrayList;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.LogicDeviceBean;
import com.carel.supervisor.presentation.bean.LogicDeviceBeanList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class LogicDevice
{
	private static final int columns = 3;
    private int rows=10;
    private int width = 0;
    private int height = 0;
    @SuppressWarnings("unused")
	private String title = "";
    @SuppressWarnings("unused")
	private int pageNumber = 1;
    
    private ArrayList<Integer> idDevice=null;
    private ArrayList<String> description=null;
    private ArrayList<Integer> numVars=null;
   
    private int screenw = 1024;
    private int screenh = 768;
    
    public LogicDevice()
    {
    }

    public LogicDevice(String title)
    {
        this.title = title;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void setTitle(String tit)
    {
        this.title = tit;
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
    
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }

    
    public void loadData(UserSession userSession) throws Exception{
    	LogicDeviceBeanList logicDeviceList= new LogicDeviceBeanList();
    	logicDeviceList.loadDeviceComplete(null, userSession.getIdSite(),userSession.getLanguage());
    	LogicDeviceBean deviceBean=null; 
        idDevice= new ArrayList<Integer>(); 
    	description= new ArrayList<String>();
        numVars= new ArrayList<Integer>(); 
        for(int i=0;i<logicDeviceList.size();i++){
        	deviceBean=logicDeviceList.getLogicDevice(i);
        	idDevice.add(new Integer(deviceBean.getIddevice()));
        	description.add(deviceBean.getDescription());
        	numVars.add(new Integer(deviceBean.getNumVars()));
        }//for
    }//loadData
    
    private HTMLElement[][] getLogicDeviceTable()
    {
        HTMLElement[][] tableData = null;
        
        rows=description.size();
       

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            for (int i = 0; i < rows; i++)
            {
               
                tableData[i] = new HTMLElement[columns];

                tableData[i][0] = new HTMLSimpleElement((String)description.get(i));
                tableData[i][1] = new HTMLSimpleElement(((Integer) numVars.get(i)).toString());
                tableData[i][2] = new HTMLSimpleElement("<img src=\"images/button/settings_on_black.png\" style='cursor:pointer;' onClick=top.frames['manager'].loadTrx('nop&folder=devdetail&bo=BDevDetail&type=click&iddev='+aValue["+i+"][2].split(';')[0]+'&desc=ncode10');>");
    
            }//for
        }//if

        return tableData;
    }//getLogicDeviceTable

    
    private HTMLTable getHTMLLogicDeviceTablePrv(String tableName, String language)
    {
        HTMLElement[][] data = getLogicDeviceTable();
        HTMLTable logicDeviceTable = new HTMLTable(tableName,getHeaderTable(language), data, false,false);
   
        logicDeviceTable.setScreenH(screenh);
        logicDeviceTable.setScreenW(screenw);
        logicDeviceTable.setColumnSize(0, 390);
        logicDeviceTable.setColumnSize(1, 390);
        logicDeviceTable.setColumnSize(2, 69);
        logicDeviceTable.setAlignType(1,1);
        logicDeviceTable.setAlignType(2,1);
        logicDeviceTable.setRowHeight(22);
        
        
        logicDeviceTable.setHeight(height);
        logicDeviceTable.setWidth(width);
        logicDeviceTable.setTableId(1);
        
        //logicDeviceTable.setTableTitle(this.title);

        int numRows=description.size();
        if(numRows!=0){
      
        	String[] dblclick = new String[numRows];
       for (int i = 0; i < numRows; i++)
        {
            dblclick[i] = new String(idDevice.get(i) + ";;" + i);
            // new String(idDevice.get(i)+";;"+description.get(i));
        }

        if (dblclick.length == 0)
        {
            dblclick = new String[] { "nop" };
        }

        
        logicDeviceTable.setDbClickRowAction("loadDevice('$1');");
        logicDeviceTable.setDlbClickRowFunction(dblclick);

        logicDeviceTable.setSgClickRowAction("selectDevice('$1');");
        logicDeviceTable.setSnglClickRowFunction(dblclick);
     
        }
        
        return logicDeviceTable;
    }


	public String getHTMLLogicDeviceTable(String tableName, String language)
    {
        return getHTMLLogicDeviceTablePrv(tableName, language).getHTMLText().toString();
    }

    public String getHTMLLogicDeviceTableRefresh(String tableName, String language)
    {
        return getHTMLLogicDeviceTablePrv(tableName, language)
                   .getHTMLTextBufferRefresh().toString();
    }

    private String[] getHeaderTable(String language) {
    	LangService langService = LangMgr.getInstance().getLangService(language);
   	 	String []header= new String[]{langService.getString("logicdeviceTable","desc"),langService.getString("logicdeviceTable","numvar"),langService.getString("siteview","devconfig")};
		return header;
	}

   
    
}//Class LogicDevice
