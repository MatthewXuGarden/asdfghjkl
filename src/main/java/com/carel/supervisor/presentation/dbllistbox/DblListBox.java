package com.carel.supervisor.presentation.dbllistbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.BookletDevVarBean;
import com.carel.supervisor.presentation.rule.RuleConstants;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class DblListBox
{
    private List listbox1 = new ArrayList();
    private List listbox2 = new ArrayList();
    private int left_rows = 15;
    private int right_rows = 15;
    private int width = 150;
    private int width_select = 600;
    private int height = 150;
    private String idlistbox = "listbox";
    private String srcButton1 = "images/dbllistbox/arrowdx_on.png";
    private String srcButton2 = "images/dbllistbox/arrowsx_on.png";
    private String fncButton1 = "";
    private String fncButton2 = "";
    private String objectVal = "";
    private String headerTable1 = "";
    private String headerTable2 = "";
    private String idtable = "";
    private boolean read_only = false;
    private boolean orderDx = true;
    private boolean orderSx = true;
    //device combo object, jsp page
    private String devobj = null;
    private boolean multiple = false;
    private int maximumItem = -1;
    private String leftitemNo=""; //that is selected.
    
    private int screenw = 1024;
    private int screenh = 768;
    private boolean addFnc = false;
    private String addFncName ="";
    private boolean  customFunction = false;
    private String  customFuncName = "";
    
    private String leftSize = ""; 
    private String rightSize = ""; 
    
    public DblListBox(List listbox1, List listbox2, boolean read_only, boolean orderDx,
        boolean orderSx)
    {
        this.listbox1 = listbox1;
        this.listbox2 = listbox2;
        fncButton1 = "to2(" + idlistbox + "1);return false;";
        fncButton2 = "to1(" + idlistbox + "2);return false;";
        this.read_only = read_only;
        this.orderDx = orderDx;
        this.orderSx = orderSx;
    }
    public DblListBox(List listbox1, List listbox2, boolean read_only, boolean orderDx,
            boolean orderSx,String devobj,boolean multiple,String leftItemNo)
        {
            this.listbox1 = listbox1;
            this.listbox2 = listbox2;
            fncButton1 = "to2(" + idlistbox + "1);return false;";
            fncButton2 = "to1(" + idlistbox + "2);return false;";
            this.read_only = read_only;
            this.orderDx = orderDx;
            this.orderSx = orderSx;
            if(devobj != null && devobj.trim().equals("") == false)
            {
            	this.devobj = devobj;
            }
            this.multiple = multiple;
            this.leftitemNo=leftItemNo;
        }
    public DblListBox(List listbox1, List listbox2, boolean read_only, boolean orderDx,
            boolean orderSx,String devobj,boolean multiple,boolean addFnc,String addFncName)
        {
            this.listbox1 = listbox1;
            this.listbox2 = listbox2;
            fncButton1 = "to2(" + idlistbox + "1);return false;";
            fncButton2 = "to1(" + idlistbox + "2);return false;";
            this.read_only = read_only;
            this.orderDx = orderDx;
            this.orderSx = orderSx;
            if(devobj != null && devobj.trim().equals("") == false)
            {
            	this.devobj = devobj;
            }
            this.multiple = multiple;
            this.addFnc = addFnc;
            this.addFncName = addFncName;
        }

    public DblListBox(List listbox1, List listbox2, boolean read_only, boolean orderDx,
            boolean orderSx,String devobj,boolean multiple)
        {
            this.listbox1 = listbox1;
            this.listbox2 = listbox2;
            fncButton1 = "to2(" + idlistbox + "1);return false;";
            fncButton2 = "to1(" + idlistbox + "2);return false;";
            this.read_only = read_only;
            this.orderDx = orderDx;
            this.orderSx = orderSx;
            if(devobj != null && devobj.trim().equals("") == false)
            {
            	this.devobj = devobj;
            }
            this.multiple = multiple;
        }
   
    public DblListBox(List listbox1, List listbox2, boolean read_only, boolean orderDx,
            boolean orderSx,String devobj,boolean multiple,int maximumItem)
        {
            this.listbox1 = listbox1;
            this.listbox2 = listbox2;
            fncButton1 = "to2(" + idlistbox + "1);return false;";
            fncButton2 = "to1(" + idlistbox + "2);return false;";
            this.read_only = read_only;
            this.orderDx = orderDx;
            this.orderSx = orderSx;
            if(devobj != null && devobj.trim().equals("") == false)
            {
            	this.devobj = devobj;
            }
            this.multiple = multiple;
            this.maximumItem = maximumItem;
        }
    public DblListBox(List listbox1, List listbox2, boolean read_only)
    {
        this(listbox1, listbox2, read_only, true, true);
    }

    public DblListBox(List listbox1, List listbox2)
    {
        this(listbox1, listbox2, false);
    }

    public String getIdlistbox()
    {
        return idlistbox;
    }

    public void setIdlistbox(String idlistbox)
    {
        this.idlistbox = idlistbox;
        fncButton1 = "to2(" + idlistbox + "1);return false;";
        fncButton2 = "to1(" + idlistbox + "2);return false;";
    }

    public void setSrcButton1(String srcButton1)
    {
        this.srcButton1 = srcButton1;
    }

    public void setSrcButton2(String srcButton2)
    {
        this.srcButton2 = srcButton2;
    }

    public void setFncButton1(String fncButton1)
    {
        this.fncButton1 = fncButton1;
    }

    public void setFncButton2(String fncButton2)
    {
        this.fncButton2 = fncButton2;
    }

    public void setRowsListBox(int rows)
    {
        if (rows<15) rows=15;
    	this.left_rows = rows;
        this.right_rows = rows;
    }

    public void setLeftRowsListBox(int left_rows)
    {
    	if (left_rows<15) left_rows=15;
    	this.left_rows = left_rows;
    }

    public void setRightRowsListBox(int right_rows)
    {
    	if (right_rows<15) right_rows=15;
    	this.right_rows = right_rows;
    }
    
    public void setLeftRowsListBoxNoLimit(int left_rows)
    {
    	this.left_rows = left_rows;
    }

    public void setRightRowsListBoxNoLimit(int right_rows)
    {
    	this.right_rows = right_rows;
    }

    public int getWidthListBox()
    {
        return width;
    }

    public void setWidthListBox(int width)
    {
        this.width = recalWidthLB(width);
    }

    public String getObjectVal()
    {
        return objectVal;
    }

    public void setObjectVal(String objectVal)
    {
        this.objectVal = objectVal;
    }

    public String getHeaderTable1()
    {
        return headerTable1;
    }

    public void setHeaderTable1(String headerTable1)
    {
        this.headerTable1 = headerTable1;
    }

    public String getHeaderTable2()
    {
        return headerTable2;
    }

    public void setHeaderTable2(String headerTable2)
    {
        this.headerTable2 = headerTable2;
    }

    public String getIdtable()
    {
        return idtable;
    }
    
    public int getWidth_select() {
		return width_select;
	}
    public void setDeviceHTMLid(String deviceHTMLid)
    {
    	this.devobj = deviceHTMLid;
    }
    public String getDeviceHTMLid()
    {
    	return this.devobj;
    }
    public void setMultiple(boolean multiple)
    {
    	this.multiple = multiple;
    }
    public boolean getMultiple()
    {
    	return this.multiple;
    }
	public void setWidth_select(int width_select) {
		this.width_select = recalWidthSL(width_select);
	}

	public void setIdtable(String idtable)
    {
        this.idtable = idtable;
    }

    public void addToList1(ListBoxElement element)
    {
        listbox1.add(element);
    }

    public void addToList2(ListBoxElement element)
    {
        listbox2.add(element);
    }

    public void remFromList1(int i)
    {
        listbox1.remove(i);
    }

    public void remFromList2(int i)
    {
        listbox2.remove(i);
    }
    
	public void setHeight(int height) {
		this.height = recalHeight(height);
	}

	public ListBoxElement getElementFromList1(int i)
    {
        return (ListBoxElement) listbox1.get(i);
    }

    public ListBoxElement getElementFromList2(int i)
    {
        return (ListBoxElement) listbox2.get(i);
    }

    public String getHtmlDblListBox()
    {
    	//kevin
    	//remove Element from listbox1 which already in listbox2(compared by element value)
    	for(int i=listbox1.size()-1;i>=0;i--)
    	{
    		ListBoxElement a = (ListBoxElement)listbox1.get(i);
    		for(int j=0;j<listbox2.size();j++)
    		{
    			ListBoxElement b = (ListBoxElement)listbox2.get(j);
    			if(a.getValue().equals(b.getValue()))
    			{
    				listbox1.remove(i);
    				break;
    			}
    		}
    	}
    	if(customFunction){
    		fncButton1 = customFuncName+";return false;";
            fncButton2 = "multipleto1(" + idlistbox + "2,"+devobj+");"+";return false;";
    	}else 
    	{
    		//if(devobj != null && devobj.equals("")==false&&multiple == true)
    		if(multiple == true && this.maximumItem == -1)
	    	{
	            fncButton1 = "multipleto2(" + idlistbox + "1,"+devobj+");return false;";
	            fncButton2 = "multipleto1(" + idlistbox + "2,"+devobj+");return false;";
	            if(addFnc==true){
	        		fncButton1 = "multipleto2(" + idlistbox + "1,"+devobj+");"+addFncName+";return false;";
	                fncButton2 = "multipleto1(" + idlistbox + "2,"+devobj+");"+addFncName+";return false;";
	            }
	    	}
	    	else if(multiple == true && this.maximumItem != -1)
	    	{
	    		fncButton1 = "multipleto2MaxItemsChk(" + idlistbox + "1,"+devobj+","+maximumItem+");return false;";
	            fncButton2 = "multipleto1(" + idlistbox + "2,"+devobj+");return false;";
	    	}
    	}

    	String multipleStr1 = "";
    	String multipleStr2 = "";
    	if(this.multiple == true)
    	{
    		multipleStr1 = "multiple ondblclick=\""+fncButton1+"\" ";
    		multipleStr2 = "multiple ondblclick=\""+fncButton2+"\" ";
    	}

        StringBuffer html = new StringBuffer();

        html.append("<table border='0' width='100%' height='100%'><tr align='center' class='th'>");

        if (orderDx)
        {
//            ListBoxElement[] list1 = (ListBoxElement[]) (listbox1.toArray(new ListBoxElement[listbox1.size()]));
//            Arrays.sort(list1);
//            listbox1.clear();
//
//            for (int i = 0; i < list1.length; i++)
//            {
//                listbox1.add(list1[i]);
//            }
        	//modidfy kevin.ge 
        	Collections.sort(listbox1);
        }

        if (orderSx)
        {
//            ListBoxElement[] list2 = (ListBoxElement[]) (listbox2.toArray(new ListBoxElement[listbox2.size()]));
//            Arrays.sort(list2);
//            listbox2.clear();
//
//            for (int i = 0; i < list2.length; i++)
//            {
//                listbox2.add(list2[i]);
//            }
        	//modidfy kevin.ge 
        	Collections.sort(listbox2);
        }

        //prima listbox
        
        // Intevento tecnico
        //left_rows = 10;
        
        html.append("<td align='center' width='40%' height='3%'><b>" + headerTable1 +
            "</b></td><td style='background-color:white;' width='7%'>&nbsp;</td><td style='background-color:white;' width='5%'>&nbsp;</td><td style='background-color:white;' width='7%'>&nbsp;</td><td width='40%' align='center' height='3%'><b>" +
            headerTable2 + "</b></td></tr>");
        
        //html.append("<tr valign='top' height='*'><td><div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:"+height+"' onscroll=\"OnDivScroll("+idlistbox+"1,"+ left_rows + ");\">");
        html.append("<tr valign='top' height='*'><td><div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:100%;HEIGHT:100%;border:0px solid grey;' onscroll=\"OnDivScroll("+idlistbox+"1,"+ left_rows + ");\">");
        /*html.append("<select class='standardTxt' size=\"" + left_rows + "\" name=\"" + idlistbox + "1\" id=\"" + idlistbox + "1\"  onscroll='OnSelectFocus(this,"+ left_rows + ");' style=\"width:" + width_select +
            "px;\" ");
          */  
        html.append("<select class='standardTxt' name=\"" + idlistbox + "1\" id=\"" + idlistbox + "1\" onscroll='OnSelectFocus(this,"+ left_rows + ");'   size= '"+left_rows+"' style=\"width:100%;height:100%;\" "+multipleStr1+" ");
        if (!read_only)
        {
            html.append("onfocus=\"resetIndex2(this)\"");
        }

        html.append(">\n");

        for (int i = 0; i < listbox1.size(); i++)
        {
            ListBoxElement tmp = this.getElementFromList1(i);
            html.append("<option class='"+(i%2==0?"Row1":"Row2")+"' value=\"" + tmp.getValue() + "\" >" +
                tmp.getDescription() + "</option>\n");
        }

        html.append("</select></div>\n");
       
        html.append("</td>\n");

        //objectVal
        html.append("<td>" + objectVal + "</td>");

        //bottoni
        html.append("<td valign='middle'><table width='100%' border='0' celpadding='1' cellspacing='1'>");
        html.append("<tr><td><img onclick=\"" + fncButton1 + "\" src=\"" + srcButton1 +
            "\"  /></td></tr>");
        html.append("<tr><td>&nbsp;</td></tr>\n");
        html.append("<tr><td><img onclick=\"" + fncButton2 + "\" src=\"" + srcButton2 +
            "\" /></td></tr>");

        html.append("</table></td>\n");

        //seconda listbox
        if (objectVal.equals(""))
        {
            html.append("<td>&nbsp;</td><td>");
            //html.append("<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:"+height+"' onscroll=\"OnDivScroll("+idlistbox+"2,"+ right_rows + ");\">");
            html.append("<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:100%;HEIGHT:100%;border:0px solid grey;' onscroll=\"OnDivScroll("+idlistbox+"2,"+ right_rows + ");\">");
            /*html.append("<select class='standardTxt' size=\"" + right_rows + "\" name=\"" +
                idlistbox + "2\" id=\"" + idlistbox + "2\" onscroll='OnSelectFocus(this,"+ right_rows + ");' style=\"width:" + width_select +"px;\" ");
                */
            html.append("<select class='standardTxt' name=\"" +
                    idlistbox + "2\" id=\"" + idlistbox + "2\" onscroll='OnSelectFocus(this,"+ right_rows + ");' size= '"+right_rows+"' style=\"width:100%;height:100%;\" "+multipleStr2+" ");
            if (!read_only)
            {
                html.append("onfocus=\"resetIndex1(this)\"");
            }

            html.append(">\n");

            for (int i = 0; i < listbox2.size(); i++)
            {
                ListBoxElement tmp = this.getElementFromList2(i);
                html.append("<option class='"+(i%2==0?"Row1":"Row2")+"' value=\"" + tmp.getValue() + "\"  >" + tmp.getDescription() + 
                    "</option>\n");
            }

            html.append("</select>\n");
            html.append("</div>");
            html.append("</td></tr></table>\n");
        }
        else
        {
            html.append("<td valign=\"top\">");
            html.append("<table id=\"" + idtable + "\" name=\"" + idtable +
                "\" class=\"table\"><tr class=\"th\"><td align=\"center\" width=\"400px\">" +
                headerTable1 + "</td><td>" + headerTable2 + "</td></tr></table>");
            html.append("</td></tr></table>\n");
        }

        return html.toString();
    }
    
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }

    private int recalWidthLB(int value)
    {
        return (int)Math.round(((double)(value*(this.screenw-124))/900));
    }

    private int recalWidthSL(int value)
    {
        return (int)Math.round(((double)(value*(this.screenw-124))/400));
    }
    
    private int recalHeight(int value)
    {
    	return (int)Math.round(((double)(value*(this.screenh-200))/768));
    }
    
    public String getHtmlDevVarListBox()
    {
    	for(int i=listbox1.size()-1;i>=0;i--)
    	{
    		ListBoxElement a = (ListBoxElement)listbox1.get(i);
    		for(int j=0;j<listbox2.size();j++)
    		{
    			ListBoxElement b = (ListBoxElement)listbox2.get(j);
    			if(a.getValue().equals(b.getValue()))
    			{
    				listbox1.remove(i);
    				break;
    			}
    		}
    	}
    	if(multiple == true)
    	{
            fncButton1 = "reload(this);return false;";
            fncButton2 = "";
    	}

    	String multipleStr1 = "";
    	String multipleStr2 = "";
    	if(this.multiple == true)
    	{
    		multipleStr1 = "multiple ondblclick=\""+fncButton1+"\" ";
    		multipleStr2 = "multiple ondblclick=\""+fncButton2+"\" ";
    	}

        StringBuffer html = new StringBuffer();

        html.append("<table border='0' width='100%' height='100%'><tr align='center' class='th'>");

        if (orderDx)
        {

        	Collections.sort(listbox1);
        }

        if (orderSx)
        {

        	Collections.sort(listbox2);
        }

        
        html.append("<td align='center' width='40%' height='3%'><b>" + headerTable1 +
            "</b></td><td style='background-color:transparent' width='7%'>&nbsp;</td><td style='background-color:transparent' width='5%'>&nbsp;</td><td style='background-color:transparent' width='7%'>&nbsp;</td><td width='40%' align='center' height='3%'><b>" +
            headerTable2 + "</b></td></tr>");
        
        //html.append("<tr valign='top' height='*'><td><div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:"+height+"' onscroll=\"OnDivScroll("+idlistbox+"1,"+ left_rows + ");\">");
        html.append("<tr valign='top' height='*'><td><div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:100%' onscroll=\"OnDivScroll("+idlistbox+"1,"+ left_rows + ");\">");
        /*html.append("<select class='standardTxt' size=\"" + left_rows + "\" name=\"" + idlistbox + "1\" id=\"" + idlistbox + "1\"  onscroll='OnSelectFocus(this,"+ left_rows + ");' style=\"width:" + width_select +
            "px;\" ");
          */  
        html.append("<select class='standardTxt' size=\"" + left_rows + "\" name=\"" + idlistbox + "1\" id=\"" + idlistbox + "1\" onscroll='OnSelectFocus(this,"+ left_rows + ");'  style=\"width:110%;\" "+multipleStr1+" ");
        if (!read_only)
        {
            html.append("onfocus=\"resetIndex2(this)\"");
        }

        html.append(">\n");

        for (int i = 0; i < listbox1.size(); i++)
        {
            ListBoxElement tmp = this.getElementFromList1(i);
            html.append("<option class='standardTxt' value=\"" + tmp.getValue() + "\"");
            if(tmp.getValue().equals(this.leftitemNo)){
            	html.append("selected='selected'");	
            }
            html.append(" >"+tmp.getDescription() + "</option>\n");
        }

        html.append("</select></div>\n");
       
        html.append("</td>\n");

        //objectVal
        html.append("<td>" + objectVal + "</td>");

        //bottoni
        html.append("<td valign='middle'></td>\n");

        //seconda listbox
        if (objectVal.equals(""))
        {
            html.append("<td>&nbsp;</td><td>");
            //html.append("<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:"+height+"' onscroll=\"OnDivScroll("+idlistbox+"2,"+ right_rows + ");\">");
            html.append("<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:100%' onscroll=\"OnDivScroll("+idlistbox+"2,"+ right_rows + ");\">");
            /*html.append("<select class='standardTxt' size=\"" + right_rows + "\" name=\"" +
                idlistbox + "2\" id=\"" + idlistbox + "2\" onscroll='OnSelectFocus(this,"+ right_rows + ");' style=\"width:" + width_select +"px;\" ");
                */
            html.append("<select class='standardTxt' size=\"" + right_rows + "\" name=\"" +
                    idlistbox + "2\" id=\"" + idlistbox + "2\" onscroll='OnSelectFocus(this,"+ right_rows + ");' style=\"width:110%;\" "+multipleStr2+" ");
            if (!read_only)
            {
                html.append("onfocus=\"resetIndex1(this)\"");
            }

            html.append(">\n");

            for (int i = 0; i < listbox2.size(); i++)
            {
                ListBoxElement tmp = this.getElementFromList2(i);
                html.append("<option value=\"" + tmp.getValue() + "\"  >" + tmp.getDescription() +
                    "</option>\n");
            }

            html.append("</select>\n");
            html.append("</div>");
            html.append("</td></tr></table>\n");
        }
        else
        {
            html.append("<td valign=\"top\">");
            html.append("<table id=\"" + idtable + "\" name=\"" + idtable +
                "\" class=\"table\"><tr class=\"th\"><td align=\"center\" width=\"400px\">" +
                headerTable1 + "</td><td>" + headerTable2 + "</td></tr></table>");
            html.append("</td></tr></table>\n");
        }

        return html.toString();
    }
    public String getHTMLBookletTable(String language,List lst)
    {
    	LangService lang = LangMgr.getInstance().getLangService(language);
    	BookletDevVarBean report=null;
    	
    	int size = 0;
    	if(lst != null)
    		size = lst.size();
    	
        HTMLElement[][] dati = new HTMLElement[size][3];

        String devices = lang.getString("booklet","devices");
        String variables = lang.getString("booklet","variables");

        for (int i = 0; i < size; i++)
        {
        	report = (BookletDevVarBean)lst.get(i);
            
            dati[i][0] = new HTMLSimpleElement(report.getDevDesc());
            dati[i][1] = new HTMLSimpleElement(report.getVarDesc());
            dati[i][2] = new HTMLSimpleElement("<img src=\'images/actions/removesmall_on_black.png\' onclick=deleteItem("+report.getIdSite()+","+report.getIdDev()+","+report.getIdVar()+");>");
        }

        String[] headerTable = new String[3];
        headerTable[0] = lang.getString("booklet","devices");
        headerTable[1] = lang.getString("booklet","variables");
        headerTable[2] = "";


        HTMLTable table = new HTMLTable("booklet",headerTable,dati);
//        table.setTableTitle(lang.getString("hsreport", "tablename"));
        table.setScreenH(screenh);
//        table.setScreenW(screenw);
        table.setWidth(width);
        table.setHeight(height);
        table.setColumnSize(0,400);
        table.setColumnSize(1,screenw-660);
        table.setColumnSize(2,30);
        table.setRowHeight(18);
        
//        table.setSgClickRowAction("hsSelectRow('$1')");
//        table.setSnglClickRowFunction(ClickRowFunction);

        return table.getHTMLText();
    }

	public String getLeftitemNo() {
		return leftitemNo;
	}

	public void setLeftitemNo(String leftitemNo) {
		this.leftitemNo = leftitemNo;
	}
	
	//Plugin Controllo Parametri: HTML per selezione Profili
    public String getHtmlDblListBox3Column()
    {
    	for(int i=listbox1.size()-1;i>=0;i--)
    	{
    		ListBoxElement a = (ListBoxElement)listbox1.get(i);
    		for(int j=0;j<listbox2.size();j++)
    		{
    			ListBoxElement b = (ListBoxElement)listbox2.get(j);
    			if(a.getValue().equals(b.getValue()))
    			{
    				listbox1.remove(i);
    				break;
    			}
    		}
    	}
    	if(multiple == true && this.maximumItem == -1)
    	{
            fncButton1 = "multipleto2(" + idlistbox + "1,"+devobj+");return false;";
            fncButton2 = "multipleto1(" + idlistbox + "2,"+devobj+");return false;";
    	}
    	else if(multiple == true && this.maximumItem != -1)
    	{
    		fncButton1 = "multipleto2MaxItemsChk(" + idlistbox + "1,"+devobj+","+maximumItem+");return false;";
            fncButton2 = "multipleto1(" + idlistbox + "2,"+devobj+");return false;";
    	}

    	String multipleStr1 = "";
    	String multipleStr2 = "";
    	if(this.multiple == true)
    	{
    		multipleStr1 = "multiple ondblclick=\""+fncButton1+"\" ";
    		multipleStr2 = "multiple ondblclick=\""+fncButton2+"\" ";
    	}

        StringBuffer html = new StringBuffer();

        html.append("<table border='0' width='100%' height='100%'><tr align='center' class='th'>");

        if (orderDx)
        {
//            ListBoxElement[] list1 = (ListBoxElement[]) (listbox1.toArray(new ListBoxElement[listbox1.size()]));
//            Arrays.sort(list1);
//            listbox1.clear();
//
//            for (int i = 0; i < list1.length; i++)
//            {
//                listbox1.add(list1[i]);
//            }
        	//modidfy kevin.ge 
        	Collections.sort(listbox1);
        }

        if (orderSx)
        {
//            ListBoxElement[] list2 = (ListBoxElement[]) (listbox2.toArray(new ListBoxElement[listbox2.size()]));
//            Arrays.sort(list2);
//            listbox2.clear();
//
//            for (int i = 0; i < list2.length; i++)
//            {
//                listbox2.add(list2[i]);
//            }
        	//modidfy kevin.ge 
        	Collections.sort(listbox2);
        }

        //prima listbox
        
        // Intevento tecnico
        //left_rows = 10;
        
        html.append("<td align='center' width='45%' height='3%'><b>" + headerTable1 +"</b></td>" +
        		"<td style='background-color:transparent' width='1%'>&nbsp;</td>" +
        		"<td style='background-color:transparent' width='5%'>&nbsp;</td>" +
        		"<td style='background-color:transparent' width='1%'>&nbsp;</td>" +
        		"<td width='45%' align='center' height='3%'><b>" + headerTable2 + "</b></td></tr>");
        
        //html.append("<tr valign='top' height='*'><td><div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:"+height+"' onscroll=\"OnDivScroll("+idlistbox+"1,"+ left_rows + ");\">");
        html.append("<tr valign='top' height='*'><td><div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:100%' onscroll=\"OnDivScroll("+idlistbox+"1,"+ left_rows + ");\">");
        /*html.append("<select class='standardTxt' size=\"" + left_rows + "\" name=\"" + idlistbox + "1\" id=\"" + idlistbox + "1\"  onscroll='OnSelectFocus(this,"+ left_rows + ");' style=\"width:" + width_select +
            "px;\" ");
          */  
        html.append("<select class='standardTxt' size=\"" + left_rows + "\" name=\"" + idlistbox + "1\" id=\"" + idlistbox + "1\" onscroll='OnSelectFocus(this,"+ left_rows + ");' "+leftSize+" style=\"width:100%;height:100%;\" "+multipleStr1+" ");
        if (!read_only)
        {
            html.append("onfocus=\"resetIndex2(this)\"");
        }

        html.append(">\n");

        for (int i = 0; i < listbox1.size(); i++)
        {
            ListBoxElement tmp = this.getElementFromList1(i);
            html.append("<option class='standardTxt' value=\"" + tmp.getValue() + "\" >" +
                tmp.getDescription() + "</option>\n");
        }

        html.append("</select></div>\n");
       
        html.append("</td>\n");

        //objectVal
        html.append("<td>" + objectVal + "</td>");

        //bottoni
        html.append("<td valign='middle'><table width='100%' border='0' celpadding='1' cellspacing='1'>");
        html.append("<tr><td><img onclick=\"" + fncButton1 + "\" src=\"" + srcButton1 +
            "\"  /></td></tr>");
        html.append("<tr><td>&nbsp;</td></tr>\n");
        html.append("<tr><td><img onclick=\"" + fncButton2 + "\" src=\"" + srcButton2 +
            "\" /></td></tr>");

        html.append("</table></td>\n");

        //seconda listbox
        if (objectVal.equals(""))
        {
            html.append("<td>&nbsp;</td><td>");
            //html.append("<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:"+height+"' onscroll=\"OnDivScroll("+idlistbox+"2,"+ right_rows + ");\">");
            html.append("<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:"+width+";HEIGHT:100%' onscroll=\"OnDivScroll("+idlistbox+"2,"+ right_rows + ");\">");
            /*html.append("<select class='standardTxt' size=\"" + right_rows + "\" name=\"" +
                idlistbox + "2\" id=\"" + idlistbox + "2\" onscroll='OnSelectFocus(this,"+ right_rows + ");' style=\"width:" + width_select +"px;\" ");
                */
            html.append("<select class='standardTxt' size=\"" + right_rows + "\" name=\"" +
                    idlistbox + "2\" id=\"" + idlistbox + "2\" onscroll='OnSelectFocus(this,"+ right_rows + ");' "+rightSize+" style=\"width:100%;height:100%;\" "+multipleStr2+" ");
            if (!read_only)
            {
                html.append("onfocus=\"resetIndex1(this)\"");
            }

            html.append(">\n");

            for (int i = 0; i < listbox2.size(); i++)
            {
                ListBoxElement tmp = this.getElementFromList2(i);
                html.append("<option value=\"" + tmp.getValue() + "\"  >" + tmp.getDescription() +
                    "</option>\n");
            }

            html.append("</select>\n");
            html.append("</div>");
            html.append("</td></tr></table>\n");
        }
        else
        {
            html.append("<td valign=\"top\">");
            html.append("<table id=\"" + idtable + "\" name=\"" + idtable +
                "\" class=\"table\"><tr class=\"th\"><td align=\"center\" width=\"400px\">" +
                headerTable1 + "</td><td>" + headerTable2 + "</td></tr></table>");
            html.append("</td></tr></table>\n");
        }

        return html.toString();
    }
	public boolean isAddFnc() {
		return addFnc;
	}
	public void setAddFnc(boolean addFnc) {
		this.addFnc = addFnc;
	}
	public String getAddFncName() {
		return addFncName;
	}
	public void setAddFncName(String addFncName) {
		this.addFncName = addFncName;
	}
	public String getLeftSize() {
		return leftSize;
	}
	public void setLeftSize(int leftSize) {
		this.leftSize = " size = '"+leftSize+"'";
	}
	public String getRightSize() {
		return rightSize;
	}
	public void setRightSize(int rightSize) {
		this.rightSize = " size = '"+rightSize+"'";
	}
	public boolean isCustomFunction() {
		return customFunction;
	}
	public void setCustomFunction(boolean customFunction) {
		this.customFunction = customFunction;
	}
	public String getCustomFuncName() {
		return customFuncName;
	}
	public void setCustomFuncName(String customFuncName) {
		this.customFuncName = customFuncName;
	}
    
}
