package com.carel.supervisor.presentation.bean;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class BookletCabinetListPres extends BookletCabinetList
{	
	private static int screenw = 1024;
	private static int screenh = 768;
	private int width = 0;
    private int height = 0;
	
    public BookletCabinetListPres()
    {
        super(); 
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
    
    public static String getHTMLCabinetTable(int idsite, String language, String title) throws Exception
    {
    	List<BookletCabinetBean> cabinets=null;
		try {
			cabinets = retrieveCabinetWithDeviceNumber();
		} catch (DataBaseException e) {
			
			e.printStackTrace();
		}
    
        LangService lang = LangMgr.getInstance().getLangService(language);
        String[] ClickRowFunction = new String[cabinets.size()];

        HTMLElement[][] dati = new HTMLElement[cabinets.size()][];
        for (int i=0;i<cabinets.size();i++) {
        	BookletCabinetBean cabinet = cabinets.get(i);
            dati[i] = new HTMLElement[4];
            dati[i][0] = new HTMLSimpleElement("<div class='tableTouchCellImg'><input type=radio class='bigRadio' id='rdio"+cabinet.getId()+"'></div>");
            dati[i][1] = new HTMLSimpleElement("<div class='tableTouchCell'>"+cabinet.getCabinet()+"</div>");
            dati[i][2] = new HTMLSimpleElement("<div class='tableTouchCell'>"+cabinet.getFileName()+"</div>");
            dati[i][3] = new HTMLSimpleElement("<div class='tableTouchCell'>"+cabinet.getDeviceNumber()+"</div>");
            ClickRowFunction[i] = String.valueOf(cabinet.getId());
        }

        //    	header tabella
        String[] headerTable = new String[4];
        headerTable[0] = "";
        headerTable[1] = lang.getString("booklet", "cabinet");
        headerTable[2] = lang.getString("booklet", "file");
        headerTable[3] = lang.getString("booklet", "devicenumber");
        

        HTMLTable table = new HTMLTable("cabinetTable", headerTable, dati);
        table.setSgClickRowAction("selectedBookletCabinet('$1')");
        table.setSnglClickRowFunction(ClickRowFunction);
        table.setDbClickRowAction("modifyBookletCabinet('$1')");
        table.setDlbClickRowFunction(ClickRowFunction);
        table.setAlignType(new int[] {1, 0, 0, 1 });
        table.setTableTitle(title);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setColumnSize(0, 43);
        table.setColumnSize(1, 300);
        table.setColumnSize(2, 300);
        table.setColumnSize(3, 80);
        table.setRowHeight(25);
        table.setWidth(880);
        table.setHeight(148);
       
        String htmlTable = table.getHTMLText();

        return htmlTable;
    }
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }
}
