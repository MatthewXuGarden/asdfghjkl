package com.carel.supervisor.presentation.devices;

import java.util.ArrayList;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;


public class ReadOnlyDtlDevice extends AbstractDtlDevice
{
	private static final String READONLY = "1";
	private static final String MAIN = "MAIN";
	
//	private static final int TW = 700;
//	private static final int TH = 200;
//	private static final int C1 = 500;
//	private static final int C2 = 90;
//	private static final int C3 = 90;
	
//	private int screenw = 1024;
//	private int screenh = 768;

    public ReadOnlyDtlDevice(UserSession session,String lang,int idDevice)
    {
        super(session,lang,idDevice);
    }

    public String renderVariables(String tableName)
    {
    	/*
    	HTMLTable table = new HTMLTable(tableName,getHeaderTable(this.lang),getDataTable(),false,false);
    	
    	table.setScreenW(session.getScreenWidth());
    	table.setScreenH(session.getScreenHeight());
    	
    	table.setWidth(TW);
    	table.setHeight(TH);
    	table.setColumnSize(0,C1);
    	table.setColumnSize(1,C2);
    	table.setColumnSize(1,C3);
        return table.getHTMLText();
    	*/
    	ArrayList<VarphyBean> vv = (ArrayList<VarphyBean>)variables;
    	StringBuffer table = new StringBuffer("<span id='readonlyvartablebo'>");
    	table.append("<table id='readonlyvartablebo1' width='98%' cellspacing='1px' cellpadding='0px' class='table'>");
    	table.append("<thead>");
    	table.append("<tr>");
    	table.append("<th class='th' width='18%' height='18px' colspan='2'>"+getHeaderTable(lang)[2]+"</th>");
    	table.append("<th class='th' width='15%' height='18px' >"+getHeaderTable(lang)[1]+"</th>");
    	table.append("<th class='th' width='*' height='18px' >"+getHeaderTable(lang)[0]+"</th>");
    	table.append("</tr>");
    	table.append("</thead>");
    	table.append("<tbody>");
    	for(int i = 0; i<vv.size();i++)
    	{
    		if (i%2 == 0) {
    			table.append("<tr class='Row1' height='21px'>");
    		}
    		else {
    			table.append("<tr class='Row2' height='21px'>");
    		}
    		table.append("<td class='td10pt' id=\"var_"+idDevice+"_"+vv.get(i).getId().intValue()+"\""+
    						((vv.get(i).getType()==1) || ("".equals(vv.get(i).getMeasureUnit()))?" width='18%' colspan='2' align='center'":" width='9%' align='right'")+">"+
    						(vv.get(i).getType()==1?buildLedStatus((String)this.values.get(i),""):"<b><nobr>"+this.values.get(i)+"</nobr></b>")+"</td>");
	    	if ((vv.get(i).getType()!=1) && (!"".equals(vv.get(i).getMeasureUnit())))
	    		table.append("<td class='td10pt' width='9%' align='left'><nobr>"+vv.get(i).getMeasureUnit()+"</nobr></td>");
	    	table.append("<td class='td10pt' width='15%' align='center'>"+vv.get(i).getShortDesc()+"</td>");
	    	table.append("<td class='td10pt'>"+vv.get(i).getShortDescription()+"</td>");
    		table.append("</tr>");
    	}
    	table.append("</tbody>");
    	table.append("</table>");
    	table.append("</span>");
    	
    	
    	return table.toString();
    }
    
    public String refreshVariables(String tableName)
    {
    	StringBuffer ris = new StringBuffer();
		String type = "";
		VarphyBean tmp = null;
		for(int i=0; i<this.variables.size(); i++)
		{
			tmp = (VarphyBean)this.variables.get(i);
			type = tmp.getType()==1?"D":"A";
			ris.append("<var id='var_"+idDevice+"_"+tmp.getId().intValue()+"' type='"+type+"'>"+(String)this.values.get(i)+"</var>");
		}
        return ris.toString();
    }

    public boolean check(VarphyBean variable)
    {
    	String rwStatus = null;
    	String toDisplay = null;
    	
        if (variable != null)
        {
        	rwStatus = variable.getReadwrite();
        	toDisplay = variable.getDisplay();
        }
            
        if (rwStatus != null && toDisplay != null)
        {
            rwStatus = rwStatus.trim();
            toDisplay = toDisplay.trim();

            if (rwStatus.equalsIgnoreCase(READONLY) && toDisplay.equalsIgnoreCase(MAIN))
            	return true;
        }
        return false;
    }
    
    private String[] getHeaderTable(String lang)
    {
        LangService temp = LangMgr.getInstance().getLangService(lang);
        return new String[]
                   {temp.getString("dtlview", "detaildevicecol3"),temp.getString("dtlview","col5"),
        			temp.getString("dtlview", "detaildevicecol0")};
    }
    
    /*
    private HTMLElement[][] getDataTable()
    {
        HTMLElement[][] data = new HTMLSimpleElement[this.variables.size()][3];
        VarphyBean tmp = null;
        
        for(int i=0; i<data.length; i++)
        {
        	tmp = (VarphyBean)this.variables.get(i);
        	
        	if(tmp != null)
        	{
        		if(tmp.getMeasureUnit()!=null&&tmp.getMeasureUnit().length() > 0)
        			data[i][0] = new HTMLSimpleElement(tmp.getShortDescription()+" ["+tmp.getMeasureUnit()+"]");
        		else
        			data[i][0] = new HTMLSimpleElement(tmp.getShortDescription());
        		
        		data[i][1] = new HTMLSimpleElement(tmp.getShortDesc());
        		if(tmp.getType() == 1)
        			data[i][2] = new HTMLSimpleElement(buildLedStatus((String)this.values.get(i)));
        		else
        			data[i][2] = new HTMLSimpleElement((String)this.values.get(i));
        	}
        }
        
        return data;
    }
    */
}
