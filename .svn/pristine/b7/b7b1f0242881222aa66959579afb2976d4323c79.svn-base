package com.carel.supervisor.presentation.menu;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.ldap.FunctionalityHelper;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.session.UserSession;


public class DescriptionTree
{
    private int width = 900;
    private int height = 400;
    private int screenw = 1024;
    private int screenh = 768;
    

    public DescriptionTree()
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
    

    public String getHTMLFunctTable(String tableName, String language, UserSession us,boolean vk, int width, int height) throws Exception
    {
    	FunctionalityHelper.init();
    	
    	MenuSection[] listSection = MenuConfigMgr.getInstance().getMenuSection();
    	LangService lang =  LangMgr.getInstance().getLangService(us.getLanguage());
    	
    	String section_list = "<input type='hidden' id='section_list' value='";
    	this.height = height;
    	this.width = width;
    	
    	String html = "<div style='width:"+getTableWidth()+";height:"+getTableHeight()+";overflow:auto'>";
    	html+="<table class='table' cellpadding=1 cellspacing=1 border=0>\n";
    	
    	for(int i=0; i<listSection.length; i++)
		{
    		MenuSection mn = listSection[i];
    		
    		for (int j=0;j<mn.getListVoci().length;j++)
    		{
    			MenuVoce section = mn.getListVoci()[j];
    			if (FunctionalityHelper.isVisible(section.getName()))
    			{
    				html+=addMenuRow(section.getName(), us,lang,vk);
    				section_list+=section.getName()+";";
    			}
    			// "global" section is editable; this section isn't take from XML configuration file 
    			if (section.getName().equalsIgnoreCase("mstrmaps"))
    			{
    				html+=addGlobalRow(vk,lang);
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
    
    private String addGlobalRow(boolean vk, LangService lang)
    {
    	//onclick=\"style='cursor:pointer' change_view('entry1');save_last(this);\"
    	String html = "";
    	String description = lang.getString("menu", "entry1");
    	html+="<tr class='Row1' style='height:25px' >\n";
    	html+="<td width='1%' align='center'><img  src=\"images/button/dx_on.png\" id='img_entry1'/></td>";
    	html+="<td align='center'><img style='cursor:pointer' onclick=\"d_edit('d_entry1',true);"+(vk?"buildKeyboardInputs();":"")+" \" src='images/actions/Edit_on_black.png' /></td>"+
		"<td colspan=2 width=\"35%\" class=\"standardTxt\"  id='d_entry1' onclick='save_last(this)' ondblclick=\"d_edit('d_entry1',true);"+(vk?"buildKeyboardInputs();":"")+"\"><b> " +description+"</b></td>" +
		"<input type='hidden' name='d_entry1_val' id='d_entry1_val' value=\""+description+"\" />";
    	html+="</tr>\n";
    	return html;
    }
    
    private String addMenuRow(String section_name, UserSession us, LangService lang,boolean vk)
    {
    	String html = "";
    	String root=section_name;;
    	String tmp[] = null;
    	String descr_code = "";
    	String descr_subcode = "";
    	html+=buildMenuRow(section_name, us,lang, null,null,vk);
    	while (FunctionalityHelper.hasChild(section_name)!=null)
		{
			tmp = FunctionalityHelper.hasChild(section_name).split(";");
			section_name = tmp[0];
			descr_code = tmp[1];
			descr_subcode = tmp[2];
			html+=buildMenuRow(section_name, us, lang,descr_code,descr_subcode,vk);
			html+="<input type='hidden' id='has_child_"+root+"' value='"+section_name+"' />";
		}
    	return html;
    }

    private String buildMenuRow(String caption, UserSession us, LangService lang, String descr_code, String descr_subcode,boolean vk)
    {
    	MenuTab mt = MenuTabMgr.getInstance().getTabMenuFor(caption);
    	String html = "";
    	
    	String description = "";
    	boolean indented_row = false;
    	
    	if (descr_code!=null)
	    	indented_row = true;
    	
    	if (!indented_row)
    		description = lang.getString("menu", caption);
    	else
    		description = lang.getString(descr_code, descr_subcode);
    	
    	// start ROW
    	html+="<tr class='Row1' style='height:25px' >\n"; 
    	
    			// column 1 & column 2
    			if (!indented_row)
    			{
    				html+="<td width='1%' align='center'><img style='cursor:pointer'  onclick=\"change_view('"+caption+"');save_last(this);\" src=\"images/button/dx_on.png\" id='img_"+caption+"'/></td>";
    			}
    			else
    			{
    				html+="<td></td>";
    			}
    			
    			//  column 3
    			if (!indented_row)
    			{
    				html+="<td align='center'><img style='cursor:pointer' onclick=\"d_edit('d_"+caption+"',true);"+(vk?"buildKeyboardInputs();":"")+" \" src='images/actions/Edit_on_black.png' /></td>"+
    						"<td colspan=2 width=\"35%\" class=\"standardTxt\"  id='d_"+caption+"' onclick='save_last(this)' ondblclick=\"d_edit('d_"+caption+"',true);"+(vk?"buildKeyboardInputs();":"")+"\"><b> " +description+"</b></td>" +
    						"<input type='hidden' name='d_"+caption+"_val' id='d_"+caption+"_val' value=\""+description+"\" />";	
    			}
    			else
    			{
    				html+="<td width=\"1%\" align='center'><img style='cursor:pointer'  onclick=\"change_view('"+caption+"');save_last(this);\" src=\"images/button/dx_on.png\" id='img_"+caption+"'/></td>" +
    						"<td align='center' width=\"1%\" ><img style='cursor:pointer' onclick=\"d_edit('d_"+caption+"',true);"+(vk?"buildKeyboardInputs();":"")+" \" src='images/actions/Edit_on_black.png' /></td>" +
    						"<td class=\"standardTxt\" width=\"30%\" id='d_"+caption+"' onclick='save_last(this)' ondblclick=\"d_edit('d_"+caption+"',true);"+(vk?"buildKeyboardInputs();":"")+"\"><b> " +description+"</b></td>" +
						    "<input type='hidden' name='d_"+caption+"_val' id='d_"+caption+"_val' value=\""+description+"\" />";
    			}
    			
    			
    			
    	// end ROW
    			html+="</tr>\n";
    	html+= buildSubTabRow(mt, caption,us,lang, indented_row,vk);
    	return html;
    }
    
    
    public String buildSubTabRow(MenuTab menutab, String caption, UserSession us,  LangService lang, boolean indented,boolean vk)
    {
    	String html = "";
    	String tabname = "";
    	TabObj to = null;
    	
    	for (int i=0;i<menutab.getNumTab();i++)
    	{
    		to = menutab.getTab(i);  //tab retrieve
    		tabname = to.getIdTab();  //get tabname
    		
    		// start ROW
    		html+="<tr class='statoAllarme4_b' id='"+caption+"_"+i+"' style='height:25px;visibility:hidden;display:none;'>\n";
    		
	    		//column 1
    			if (!indented)
    			{
    				html+="<td width=\"1%\"></td>";
    			}
    			else
    			{
    				html+="<td></td>";
    				
    			}
	    		
	    		
	    		
	    		// column 3
    			if (!indented)
    				html+="<td align='center'><img style='cursor:pointer' onclick=\"d_edit('d_"+caption+"_"+tabname+"',false);"+(vk?"buildKeyboardInputs();":"")+" \" src='images/actions/Edit_on_black.png' /></td>" +
    						"<td colspan=2 class='standardTxt' id='d_"+caption+"_"+tabname+"' onclick='save_last(this)' ondblclick=\"d_edit('d_"+caption+"_"+tabname+"',false);"+(vk?"buildKeyboardInputs();":"")+"\">"+lang.getString(caption, tabname)+"</td>" +
    						"<input type='hidden' name='d_"+caption+"_"+tabname+"_val' id='d_"+caption+"_"+tabname+"_val' value=\""+lang.getString(caption, tabname)+"\" />";
    			else
    				html+="<td class='standardTxt'></td>" +
    						"<td align='center'><img style='cursor:pointer' onclick=\"d_edit('d_"+caption+"_"+tabname+"',false);"+(vk?"buildKeyboardInputs();":"")+" \" src='images/actions/Edit_on_black.png' /></td>" +
    						"<td  id='d_"+caption+"_"+tabname+"' onclick='save_last(this)' ondblclick=\"d_edit('d_"+caption+"_"+tabname+"',false);"+(vk?"buildKeyboardInputs();":"")+"\" >"+lang.getString(caption, tabname)+"</td>" +
    						"<input type='hidden' name='d_"+caption+"_"+tabname+"_val' id='d_"+caption+"_"+tabname+"_val' value=\""+lang.getString(caption, tabname)+"\" />";
    				
	    		//end ROW
    		html+="</tr>\n";	
    		
    		
    		
    	}
    	// TAB Number in Hidden FIELD
    	html+="<input type='hidden' id='num_tab_"+caption+"' value='"+menutab.getNumTab()+"' />";
    	
    	return html;
    	
    }
    
        
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
    
    public int getScreenH() {
    	return this.screenh;
    }
    
    public int setScreenW() {
    	return this.screenw;
    }
    
    

}
