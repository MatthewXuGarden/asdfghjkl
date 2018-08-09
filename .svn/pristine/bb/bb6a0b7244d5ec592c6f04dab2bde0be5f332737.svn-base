package com.carel.supervisor.presentation.logicdevice;


import java.util.ArrayList;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.LogicVariableBean;
import com.carel.supervisor.presentation.bean.LogicVariableBeanList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class LogicVariable
{
	private static final int columns = 2;
    private int rows=10;
    private int width = 0;
    private int height = 0;
    private String title = "";
    private int pageNumber = 1;
    
    private ArrayList idVariable=null;
    private ArrayList idDevice=null;
    private ArrayList description=null;
    private ArrayList type=null;
    private ArrayList typeTranslate=null;
    private ArrayList functionCode=null;
    
    private int screenw = 1024;
    private int screenh = 768;
    

    public LogicVariable()
    {
    }

    public LogicVariable(String title)
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

    public void loadData(UserSession userSession) throws Exception{
    	LogicVariableBeanList logicVariableList= new LogicVariableBeanList();
    	logicVariableList.loadVariableComplete(null, userSession.getIdSite(),userSession.getLanguage());
    	LogicVariableBean variableBean=null; 
        idVariable= new ArrayList(); 
        idDevice= new ArrayList();
    	description= new ArrayList();
        type= new ArrayList(); 
        typeTranslate= new ArrayList();
        functionCode= new ArrayList();
        for(int i=0;i<logicVariableList.size();i++){
        	variableBean=logicVariableList.getLogicVariable(i);
        	idVariable.add(new Integer(variableBean.getIdVariable()));
        	idDevice.add(variableBean.getIdDevice()!=null?variableBean.getIdDevice():new Integer(-1));
        	description.add(variableBean.getDescription());
        	type.add(new Integer(variableBean.getType()));
        	typeTranslate.add(variableBean.getTypeTranslate(userSession.getLanguage()));
        	functionCode.add(new Integer(variableBean.getFunctionCode()));
        	
        }//for
    }//loadData
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
    
    
    private HTMLElement[][] getLogicVariableTable()
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
                tableData[i][1] = new HTMLSimpleElement(((String)typeTranslate.get(i)));
            }//for
        }//if

        return tableData;
    }//getLogicVariableTable

    
    private HTMLTable getHTMLLogicVariableTablePrv(String tableName, String language)
    {
        HTMLElement[][] data = getLogicVariableTable();
        HTMLTable logicVariableTable = new HTMLTable(tableName,getHeaderTable(language), data, false,false);
   
        logicVariableTable.setScreenH(screenh);
        logicVariableTable.setScreenW(screenw);
        logicVariableTable.setColumnSize(0, 410);
        logicVariableTable.setColumnSize(1, 410);
              
        logicVariableTable.setHeight(height);
        logicVariableTable.setWidth(width);
        logicVariableTable.setTableId(1);
        
        logicVariableTable.setTableTitle(this.title);

        int numRows=description.size();
        if(numRows!=0){
      
        	String[] dblclick = new String[numRows];
       for (int i = 0; i < numRows; i++)
        {
            dblclick[i] = new String(idVariable.get(i)+";;"+i+";;"+type.get(i)+";;"+typeTranslate.get(i)+";;"+functionCode.get(i)+";;"+idDevice.get(i));
            //new String(idVariable.get(i)+";;"+description.get(i)+";;"+type.get(i)+";;"+typeTranslate.get(i)+";;"+functionCode.get(i)+";;"+idDevice.get(i));
        }

        if (dblclick.length == 0)
        {
            dblclick = new String[] { "nop" };
        }

        
        logicVariableTable.setDbClickRowAction("loadVariable('$1');");
        logicVariableTable.setDlbClickRowFunction(dblclick);

        logicVariableTable.setSgClickRowAction("selectVariable('$1');");
        logicVariableTable.setSnglClickRowFunction(dblclick);
     
        }
        
        return logicVariableTable;
    }


	public String getHTMLLogicVariableTable(String tableName, String language)
    {
        return getHTMLLogicVariableTablePrv(tableName, language).getHTMLText().toString();
    }

    public String getHTMLLogicVariableTableRefresh(String tableName, String language)
    {
        return getHTMLLogicVariableTablePrv(tableName, language)
                   .getHTMLTextBufferRefresh().toString();
    }

    private String[] getHeaderTable(String language) {
    	LangService langService = LangMgr.getInstance().getLangService(language);
   	 	String []header= new String[]{langService.getString("logicvariableTable","desc"),langService.getString("logicvariableTable","type")};
		return header;
	}

   
    
}//Class LogicVariable
