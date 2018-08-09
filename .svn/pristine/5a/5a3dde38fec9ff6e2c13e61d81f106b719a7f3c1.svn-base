package com.carel.supervisor.presentation.ldap;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.menu.ActionObj;
import com.carel.supervisor.presentation.menu.MenuAction;
import com.carel.supervisor.presentation.menu.MenuSection;
import com.carel.supervisor.presentation.menu.MenuTab;
import com.carel.supervisor.presentation.menu.MenuVoce;
import com.carel.supervisor.presentation.menu.TabObj;
import com.carel.supervisor.presentation.menu.configuration.MenuActionMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.tabmenu.TabList;


public class FunctionalityList
{
	private int width = 900;
    private int height = 400;
    private int screenw = 1024;
    private int screenh = 768;
    

    public FunctionalityList()
    {
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

    public int getTableWidth()
    {
    	return (int)Math.round(((double)(this.width*this.screenw)/1024));
    }
    
    public int getTableHeight()
    {
    	return (int)Math.round(((double)(this.height*this.screenh)/768));
    }

    public String getHTMLFunctTable(String tableName, String language, UserSession us) throws Exception
    {
    	FunctionalityHelper.init();
    	
    	MenuSection[] listSection = MenuConfigMgr.getInstance().getMenuSection();
    	
    	//add "global" (not in configuration.xml)
    	MenuVoce global_voce = new MenuVoce("grpview","nop&folder=grpview&bo=BGrpView&type=menu",-1);
    	
    	LangService lang =  LangMgr.getInstance().getLangService(us.getLanguage());
    	
    	String section_list = "<input type='hidden' id='section_list' value='";
    	
    	String html = "<div style='width:"+getTableWidth()+";height:"+getTableHeight()+";overflow:auto'>";
    	html+="<table class='table' cellpadding=1 cellspacing=1 width='100%'>\n";
    	
    	html+=addMenuRow(global_voce.getName(), us,lang);
		section_list+=global_voce.getName()+";";
    	
    	for(int i=0; i<listSection.length; i++)
		{
    		MenuSection mn = listSection[i];
    		
    		for (int j=0;j<mn.getListVoci().length;j++)
    		{
    			MenuVoce section = mn.getListVoci()[j];
    			if (FunctionalityHelper.isVisible(section.getName()))
    			{
    				html+=addMenuRow(section.getName(), us,lang);
    				section_list+=section.getName()+";";
    			}
    		}
		}
    	html+="</table>\n";
    	html+="</div>";
    	section_list = section_list.substring(0,section_list.length()-1);
    	section_list+="' />";
    	html+="\n"+section_list;

    	return html;
    }
    
    private String addMenuRow(String section_name, UserSession us, LangService lang)
    {
    	String html = "";
    	String root=section_name;;
    	String tmp[] = null;
    	String descr_code = "";
    	String descr_subcode = "";
    	html+=buildMenuRow(section_name, us,lang, null,null);
    	while (FunctionalityHelper.hasChild(section_name)!=null)
		{
			tmp = FunctionalityHelper.hasChild(section_name).split(";");
			section_name = tmp[0];
			descr_code = tmp[1];
			descr_subcode = tmp[2];
			html+=buildMenuRow(section_name, us, lang,descr_code,descr_subcode);
			html+="<input type='hidden' id='has_child_"+root+"' value='"+section_name+"' />";
		}
    	return html;
    }

    private String buildMenuRow(String caption, UserSession us, LangService lang, String descr_code, String descr_subcode)
    {
    	MenuTab mt = MenuTabMgr.getInstance().getTabMenuFor(caption);
    	String html = "";
    	
    	String description = "";
    	boolean write_button = false;
    	boolean indented_row = false;
    	
    	for (int i=0;i<mt.getNumTab();i++)
    	{
    		String tabname = "tab"+(i+1)+"name";
    		MenuAction ma = MenuActionMgr.getInstance().getActMenuFor(caption, tabname);
    		if (ma!=null)
    		{
    			if (ma.getNumAct()>0)
    			{
    				write_button = true;
    				break;
    			}
    		}
    	}
    	
    	if (descr_code!=null)
	    	indented_row = true;
    	
    	if (!indented_row)
    		description = lang.getString("menu", caption.equals("grpview")?"entry1":caption);
    	else
    		description = lang.getString(descr_code, descr_subcode);
    	
    	// start ROW
    	html+="<tr class='Row1' style='height:25px'>\n"; 
    	
    			// column 1 & column 2
    			if (!indented_row)
    			{
    				html+="<td width='1%' align='center'><img style='cursor:pointer;width:25px;height:25px;' onclick=\"change_view('"+caption+"')\" src=\"images/button/dx_on.png\" id='img_"+caption+"'/>";
    				String disabled = "";
    				if(TabList.TECH_TAB.equals(caption))
    					disabled = "disabled";
        			html+="<input type='checkbox' class='checkboxBig' name='check_"+caption+"' id='check_"+caption+"' "+disabled+" onclick=\"check_group('"+caption+"')\" /></td>";
    			}
    			else
    			{
    				html+="<td></td>";
    			}
    			
    			//  column 3
    			if (!indented_row)
    			{
    				html+="<td colspan=2 width=\"35%\" class=\"standardTxt\" style='cursor:pointer' id='d_"+caption+"' ><b> " +description+"</b></td>";
    			}
    			else
    			{
    				html+="<td width=\"5%\" ><img style='cursor:pointer;width:25px;height:25px;' onclick=\"change_view('"+caption+"')\" src=\"images/button/dx_on.png\" id='img_"+caption+"'/>" +
					"<input type='checkbox' class='checkboxBig' name='check_"+caption+"' id='check_"+caption+"' onclick=\"check_group('"+caption+"')\" /></td>" +
							"<td class=\"standardTxt\" width=\"30%\" style='cursor:pointer' id='d_"+caption+"' ><b> " +description+"</b></td>";
    			}
    			//  column 4
    			if (write_button)  //if there are buttons -> Write All Function
	    		{
	    			html+="<td width=\"1%\" align='center' class='standardTxt' style='cursor:pointer' onclick=\"set_group_buttons('"+caption+"','on')\"><img src='images/actions/allcommand.png' /></td>";
	    		}
	    		else  //blank TD
	    		{
	    			html+="<td width=\"1%\">&nbsp;</td>";
	    		}
    			//  column 5
    			html+="<td></td>";
    			
    			
    	// end ROW
    			html+="</tr>\n";
    	html+= buildSubTabRow(mt, caption,us,lang, indented_row);
    	return html;
    }
    
    
    public String buildSubTabRow(MenuTab menutab, String caption, UserSession us,  LangService lang, boolean indented)
    {
    	String html = "";
    	//boolean tab_active = false;
    	String tabname = "";
    	MenuAction ma = null;
    	int action_number = 0;
    	TabObj to = null;
    	
    	for (int i=0;i<menutab.getNumTab();i++)
    	{
    		to = menutab.getTab(i);  //tab retrieve
    		tabname = to.getIdTab();  //get tabname
    		
    		ma = MenuActionMgr.getInstance().getActMenuFor(caption, tabname);
    		action_number = (ma==null?0:ma.getNumAct());
    		
    		// start ROW
    		html+="<tr class='statoAllarme4_b' id='"+caption+"_"+i+"' style='height:25px;visibility:hidden;display:none'>\n";
    		
	    		//column 1
    			if (!indented)
    			{
    				String disabled = "";
    				if(TabList.TECH_TAB.equals(caption) && "tab1name".equals(tabname))
    					disabled = "disabled";
    				html+="<td width=\"1%\"><input type='checkbox' class='checkboxBig_w' name='check_"+caption+"_"+i+"' id='check_"+caption+"_"+i+"' "+disabled+" onclick=\"change_check_subgroup('"+caption+"',"+i+");uncheck_master('"+caption+"');\" /></td>";
    			}
    			else
    			{
    				html+="<td></td>";
    			}
	    			    		
	    		// column 3
    			if (!indented)
    				html+="<td colspan=2 class='standardTxt' style='cursor:pointer' id='d_"+caption+"_"+tabname+"'  >"+lang.getString(caption, tabname)+"</td>"; 
    			else
    				html+="<td class='standardTxt'><input type='checkbox' class='checkboxBig_w' name='check_"+caption+"_"+i+"' id='check_"+caption+"_"+i+"' onclick=\"change_check_subgroup('"+caption+"',"+i+");uncheck_master('"+caption+"');\" /></td>" +
    						"<td style='cursor:pointer' id='d_"+caption+"_"+tabname+"' >"+lang.getString(caption, tabname)+"</td>";
    				
	    		// column 4
	    		if (action_number!=0)  //if there are buttons -> Write All Function
	    		{
	    			html+="<td align='center' class='standardTxt' style='cursor:pointer' onclick=\"set_subgroup_buttons('"+caption+"',"+i+",'on')\"><img src='images/actions/allcommand.png' /></td>";
	    		}
	    		else  //blank TD
	    		{
	    			html+="<td></td>";
	    		}
	    		
	    		// column 5
	    		html+="<td>"+buildActionButtons(ma, caption, tabname,lang,us,i)+"</td>";
			
    		//end ROW
    		html+="</tr>\n";	
    	}
    	// TAB Number in Hidden FIELD
    	html+="<input type='hidden' id='num_tab_"+caption+"' value='"+menutab.getNumTab()+"' />";
    	
    	return html;
    	
    }
    
    private String buildActionButtons(MenuAction ma, String caption, String tabname,LangService lang, UserSession us, int tab_number)
    {
    	String html = "";
    	
    	// Check if exist button preset for read-only configuration
    	String preset = FunctionalityHelper.hasButtonsPreset(caption+"_"+tab_number);     	

    	if (ma!=null)
    	{
    		ActionObj ao = null;
    		//boolean enabled = false;
    		html+="<table cellpadding='3px' align='center'>";
			html+="<tr valign='middle'>";
			
	    	for (int i=0;i<ma.getNumAct();i++)
	    	{
	    		ao = ma.getAct(i);
	    		html+="<td class='standardTxt'><img id='button_"+caption+"_tab"+tab_number+"name_"+i+"' onclick=\"change_button_status(this,'"+caption+"',"+tab_number+");\" " +
	    				"src='"+ao.getIconDisable()+"'/></td>" +
	    						"<td class='standardTxt' >"+lang.getString("button", ao.getDescription())+"</td>";
	    		// VALUE OF BUTTON (on/off)
	    		html+="<input type='hidden' name='s_button_"+caption+"_tab"+tab_number+"name_"+i+"' id='val_button_"+caption+"_tab"+tab_number+"name_"+i+"' value='off' />";
	    	}
	    	
	    	html+="</tr>";
	    	html+="</table>";
	    	// BUTTON Number in Hidden FIELD
	    	html+="<input type='hidden' id='num_button_"+caption+tab_number+"' value='"+ma.getNumAct()+"' />";
	    	// PRESET for read only configuration (hidden field used by js)
	    	if (preset!=null)
	    	{
	    		html+="<input type='hidden' id='preset_"+caption+tab_number+"' value='"+preset+"' />";
	    	}
    	}
    	return html;
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
    
    public int voidgetScreenH() {
    	return this.screenh;
    }
    
    public int setScreenW() {
    	return this.screenw;
    }
    
    

}
