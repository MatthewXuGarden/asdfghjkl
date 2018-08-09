package com.carel.supervisor.presentation.devices;

import java.util.List;

import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.bo.helper.VarDependency;
import com.carel.supervisor.presentation.bo.helper.VarDependencyList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.script.EnumerationMgr;

public class ReadOnlyAllDtlDevice extends ReadOnlyDtlDevice
{
	private static final String READONLY = "1";

    public ReadOnlyAllDtlDevice(UserSession session,String lang,int idDevice)
    {
        super(session,lang,idDevice);
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

            if (rwStatus.equalsIgnoreCase(READONLY))
            	return true;
        }
        return false;
    }

	public void profileVariables(VarphyBean[] listVarphy)
	{
		variables.clear();
		for(int i = 0; i < listVarphy.length; i++)
		{
			if( listVarphy[i].getReadwrite().trim().equals(READONLY) )
				variables.add(listVarphy[i]);
		}
	}

	public String refreshVariables(String tableName)
	{
		return super.refreshVariables(tableName);
	}

	public String renderVariables(String tableName)
	{
		StringBuffer sb = new StringBuffer();
		String value = "";
		sb.append("<div style=\"width:100%;height:330pt; overflow:auto;\">");
		sb.append("<table id=\"vartable\" class=\"table\" cellpadding=\"1\" cellspacing=\"1\" align=\"left\">");
		sb.append("<tbody>");
		
		ControllerMgr c = ControllerMgr.getInstance();
		int j = 0;
		for(int i = 0; i < variables.size(); i++)
		{
			VarphyBean v = (VarphyBean)variables.get(i);
			Variable varvar;
			value = "";
			
			if(v.getReadwrite().trim().equals("1"))
			{
				if (j%2 == 0) {
					sb.append("<tr class='Row1'>");
				}
				else {
					sb.append("<tr class='Row2'>");
				}
				j++;
				//sb.append("<tr class='Row1' onMouseOver='this.className=\"selectedRow\"' onMouseOut='this.className=\"Row1\"'>");
				sb.append("<td class='td'><div style='width:600px;overflow:hidden;'>");
				sb.append(v.getShortDescription());
				sb.append("</div></td>");
				sb.append("<td class='td'><div style='width:100px;overflow:hidden;'>");
				sb.append(v.getShortDesc());
				sb.append("</div></td>");
				
				if(v.getType()!=1)
				{
					String val= "";
					float vv = 0;
					
					try
					{
						varvar = c.getFromField(v.getId());
						vv = varvar.getCurrentValue();
					
						val = !new Float(vv).equals(new Float(Float.NaN))?""+varvar.getFormattedValue():"***";
					}
					catch (Exception e)
					{
						val = "***";
					}
					
					value = EnumerationMgr.getInstance().getEnumCode( v.getIdMdl(), vv,lang);
					
					if (val.equals("***") || val.equals("") || ("".equals(value)))
					{
						sb.append("<td class='td' align='right'><div style='width:100px'>");
						sb.append(val);//values.get(i).toString());//"<INPUT type=\"radio\" id=\"STAT\"+vars[i].getId()+\"" name="radioro"+vars[i].getId()+"" value="STAT" "+(vars[i].getDisplay().trim().equals("STAT")?"checked="checked"":"")+" "+(vars[i].getType()==1?"":"disabled="disabled"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
						sb.append("</div></td>");
						sb.append("<td class='td' align='left'><div style='width:100px'>");
						sb.append(v.getMeasureUnit());//"<INPUT type="radio" id="HOME"+vars[i].getId()+"" name="radioro"+vars[i].getId()+"" value="HOME" "+(vars[i].getDisplay().trim().equals("HOME")?"checked="checked"":"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
					}
					else
					{
						sb.append("<td class='td' align='center' colspan=2><div style='width:200px'><b>");
						sb.append(value);
						sb.append("</b>");
					}
					
					sb.append("</div></td>");
				}
				else
				{
					sb.append("<td class='td' colspan=2 align='center'><div style='width:200px'>");
					int vv = -1;
					
					try
					{
						vv = (int)c.getFromField(v.getId()).getCurrentValue();
					}
					catch (Exception e)
					{
						vv = -1;
					}
					
					sb.append("<img src='images/led/"+(vv==1?"L1.gif":"L0.gif")+"'/>");
					sb.append("</div></td>");
				}
				
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}
	
	public String renderVariables2(String tableName, UserSession us)throws Exception
	{
		LangService multiLanguage = LangMgr.getInstance().getLangService(us.getLanguage());
		HTMLElement[][] data = null;
		data = new HTMLElement[variables.size()][3];
		ControllerMgr c = ControllerMgr.getInstance();
		for(int i = 0; i < variables.size(); i++)
		{
			VarphyBean v = (VarphyBean)variables.get(i);
			Variable varvar;
			String value = "";
			if(v.getShortDescription().contains("\n ") || v.getShortDescription().contains("\r") ){
				System.out.println( v.getCode()+" ## "+v.getShortDescription());
			}
//			-- bug fix : Paride's mail Fwd: #156135 --- Re: Power+ model . line break (\n) make the read only page doesn't work.
			data[i][0] = new HTMLSimpleElement(v.getShortDescription().replace("\n", "\\n"));
			data[i][1] = new HTMLSimpleElement(v.getShortDesc().replace("\n", "\\n"));
//			data[i][0] = new HTMLSimpleElement(v.getShortDescription());
//			data[i][1] = new HTMLSimpleElement(v.getShortDesc());
			
			if(v.getType()!=1)
			{
				String val= "";
				float vv = 0;
				try
				{
					varvar = c.getFromField(v.getId());
					vv = varvar.getCurrentValue();
					val = !new Float(vv).equals(new Float(Float.NaN))?""+varvar.getFormattedValue():"***";
				}
				catch (Exception e)
				{
					val = "***";
				}
				value = EnumerationMgr.getInstance().getEnumCode( v.getIdMdl(), vv,lang);
				
				if (val.equals("***") || val.equals("") || ("".equals(value)))
				{
					data[i][2] = new HTMLSimpleElement(val +" "+ v.getMeasureUnit());
				}
				else
				{
					data[i][2] = new HTMLSimpleElement(value);
				}
			}
			else
			{
				int vv = -1;
				try
				{
					vv = (int)c.getFromField(v.getId()).getCurrentValue();
				}
				catch (Exception e)
				{
					vv = -1;
				}
				data[i][2] = new HTMLSimpleElement("<img src='images/led/"+(vv==1?"L1.gif":"L0.gif")+"'/>");
			}
		}
		HTMLTable condTable = null;
		String[] hCol = {multiLanguage.getString("dtlview", "detaildevicecol3"),
							multiLanguage.getString("dtlview", "col5")
							,multiLanguage.getString("dtlview", "detaildevicecol0")};
		condTable = new HTMLTable(tableName,hCol,data,false,true);
		condTable.setScreenH(us.getScreenHeight());
		condTable.setScreenW(us.getScreenWidth());
		condTable.setHeight(340);
		condTable.setWidth(900);
		condTable.setColumnSize(0, 450);
		condTable.setColumnSize(1, 200);
		condTable.setColumnSize(2, 200);
		condTable.setAlignType(new int[]{0,0,1});
		condTable.setTableId(1);
		return condTable.getHTMLTextBufferNoWidthCalc().toString();
	}
	
	public String renderVariablesOldMethod(String tableName)
	{
		StringBuffer sb = new StringBuffer("<table border = 1>");
		String value = "";
		
		sb.append("<table id=\"vartable\" class=\"table\" cellpadding=\"1\" cellspacing=\"1\" width=\"98%\" align=\"center\">");
		sb.append("<thead>");
		sb.append("<tr height='18px'>");
		sb.append("<th class=\"th\" align=\"center\">");
		sb.append(LangMgr.getInstance().getLangService(session.getLanguage()).getString("dtlview", "detaildevicecol3"));
		sb.append("</th>");
		sb.append("<th class=\"th\" align=\"center\">");
		sb.append(LangMgr.getInstance().getLangService(session.getLanguage()).getString("dtlview","col5"));
		sb.append("</th>");
		sb.append("<th class=\"th\" align=\"center\" colspan=\"2\">");
		sb.append(LangMgr.getInstance().getLangService(session.getLanguage()).getString("dtlview", "detaildevicecol0"));
		sb.append("</th>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		
		ControllerMgr c = ControllerMgr.getInstance();
		int j = 0;
		for(int i = 0; i < variables.size(); i++)
		{
			VarphyBean v = (VarphyBean)variables.get(i);
			Variable varvar;
			value = "";
			
			if(v.getReadwrite().trim().equals("1"))
			{
				if (j%2 == 0) {
					sb.append("<tr class='Row1'>");
				}
				else {
					sb.append("<tr class='Row2'>");
				}
				j++;
				//sb.append("<tr class='Row1' onMouseOver='this.className=\"selectedRow\"' onMouseOut='this.className=\"Row1\"'>");
				sb.append("<td class='standardTxt' width=\"*\" >");
				sb.append(v.getShortDescription());
				sb.append("</td>");
				sb.append("<td class='standardTxt' width=\"10%\" >");
				sb.append(v.getShortDesc());
				sb.append("</td>");
				
				if(v.getType()!=1)
				{
					String val= "";
					float vv = 0;
					
					try
					{
						varvar = c.getFromField(v.getId());
						varvar.getCurrentValue();
					
						val = !new Float(vv).equals(new Float(Float.NaN))?""+varvar.getFormattedValue():"***";
					}
					catch (Exception e)
					{
						val = "***";
					}
					
					value = EnumerationMgr.getInstance().getEnumCode( v.getIdMdl(), vv,lang);
					
					if (val.equals("***") || val.equals("") || ("".equals(value)))
					{
						sb.append("<td class='standardTxt' width=\"7%\" align='right'>");
						sb.append(val);//values.get(i).toString());//"<INPUT type=\"radio\" id=\"STAT\"+vars[i].getId()+\"" name="radioro"+vars[i].getId()+"" value="STAT" "+(vars[i].getDisplay().trim().equals("STAT")?"checked="checked"":"")+" "+(vars[i].getType()==1?"":"disabled="disabled"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
						sb.append("</td>");
						sb.append("<td class='standardTxt' width=\"7%\" align='left'>");
						sb.append(v.getMeasureUnit());//"<INPUT type="radio" id="HOME"+vars[i].getId()+"" name="radioro"+vars[i].getId()+"" value="HOME" "+(vars[i].getDisplay().trim().equals("HOME")?"checked="checked"":"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
					}
					else
					{
						sb.append("<td class='standardTxt' width=\"14%\" align='center' colspan=2><b>");
						sb.append(value);
						sb.append("</b>");
					}
					
					sb.append("</td>");
				}
				else
				{
					sb.append("<td class='standardTxt' colspan=2 width=\"14%\" align='center'>");
					int vv = -1;
					
					try
					{
						vv = (int)c.getFromField(v.getId()).getCurrentValue();
					}
					catch (Exception e)
					{
						vv = -1;
					}
					
					sb.append("<img src='images/led/"+(vv==1?"L1.gif":"L0.gif")+"'/>");
					sb.append("</td>");
				}
				
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		return sb.toString();
	}
}
