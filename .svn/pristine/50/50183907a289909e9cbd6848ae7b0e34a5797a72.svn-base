package com.carel.supervisor.controller.pagelinks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.menu.MainMenu;
import com.carel.supervisor.presentation.session.UserSession;

public class PageLinksMgr extends InitializableBase //perche' inserita in SuperVisorConfig.xml
{
	private final int maxlinks = 4;
	private final int maxlinksxrow = 2;
	private final int maxlinklength = 15;
	private final String sectioncode = "linkbar";
	private final String def_TabFolder = "default"; //stesso valore nel db
	private final String def_TabResource = "SubTab0.jsp"; //stesso valore nel db
	private final int def_pos = 0;
	
	private final String SRC_TAB = "srctab";
	public static final String LINK_POS = "linkpos";
	public static final String DEST_TAB = "targettab";
	public static final String LINK_PERM = "linkpermission";
	public static final String LINK_DESCRCODE = "linkdescrcode";
	
    private static PageLinksMgr instance = new PageLinksMgr();
    
    public String linkLabel = "";
    public String currFolder = "";
    public String currResource = "";

	// tutti i links da tutti i tab:
    private HashMap<String,PageLinks> pageslinkslist = new HashMap<String,PageLinks>();
	
    private PageLinksMgr()
    {
    }
    
    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
    	load();
    }
    
    public static PageLinksMgr getInstance()
    {
        return instance;
    }
    
    public void load()
    {
        String srcTab = null;
        PageLinks pglnk = null;
    	RecordSet rs = null;
    	List<Record> pagelinks = null;
        String sql = "select * from cfpageslinks order by srctab, linkpos";
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(PageLinksMgr.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            srcTab = "";
        	pagelinks = new ArrayList<Record>();
        	
        	for (int i = 0; i < rs.size(); i++)
        	{
        		Record rec = rs.get(i);
        		
        		String dbSrcTab = "";
        		
				try
				{
					dbSrcTab = (String)rec.get(SRC_TAB);
				}
				catch (Exception e)
				{
					dbSrcTab = "";
				}
				
				if (srcTab.equals(""))
					srcTab = dbSrcTab;
				
				if  ((srcTab.equals(dbSrcTab)) && (!dbSrcTab.equals("")))
					pagelinks.add(rec);
				else
				{
					pglnk = new PageLinks(srcTab, pagelinks);
					this.pageslinkslist.put(srcTab, pglnk);
					
					pagelinks = new ArrayList<Record>();
					srcTab = dbSrcTab;
					pagelinks.add(rec);
				}
        	}
        	
        	pglnk = new PageLinks(srcTab, pagelinks);
			this.pageslinkslist.put(srcTab, pglnk);
        }
    }
	
	public boolean containSrcTab(String folder, String curRes)
	{
		String searchKey = folder + ";" + curRes; 
		
		if ((this.pageslinkslist != null) && (this.pageslinkslist.size() > 0) && (this.pageslinkslist.containsKey(searchKey)))
			return true;
		else
			return false;
	}
	
	public PageLinks getLinksForTab(String folder, String curRes)
	{
		String searchKey = folder + ";" + curRes;
		return this.pageslinkslist.get(searchKey);
	}
	public String getPageLinksDiv(String tabFolder, String tabResource, UserSession sessionUser)
	{
		Integer currLink = null;
		String link = "";
		String destTab = "";
		StringBuffer tableHTML = new StringBuffer();

		this.currFolder = sessionUser.getProperty("folder");
		this.currResource = sessionUser.getProperty("resource");
		
		PageLinks pglnks = getLinksForTab(this.def_TabFolder, this.def_TabResource);
		LangService lang = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
		
		try
		{
			link = getLink(pglnks.getLinkPos(this.def_pos).getTarget(), sessionUser.getLanguage(), sessionUser.getProperty("iddev"), this.def_TabFolder);
		}
		catch (Exception e1)
		{
			//PVPro-generated catch block
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e1);
			
			link = "";
		}
		String txtQuickLinks = lang.getString("menu", "quicklink");
		tableHTML.append("<div id='divQuickLink' class='divMenuLeftL2' style='display:none;'>");
		tableHTML.append("<table border='0' width='100%' height='100%' cellspacing='0' cellpadding='0'>");
		tableHTML.append("<tbody>");
		tableHTML.append("<tr><td colspan=2 align='center' class='itemHeadPVPRO2' height='40px'>"+txtQuickLinks+"</td></tr>");
		tableHTML.append("<tr class='itemPVPRO2' onmouseover=top.frames[\'manager\'].evid(this,'itemoverPVPRO2'); onmouseout=top.frames[\'manager\'].evid(this,'itemPVPRO2');><td width='5%'></td><td height='30px' width='95%'>");
		tableHTML.append("<div onclick=quicklink('1') >");
		tableHTML.append("<input type='hidden' id='pos_1' value='"+link+"' />");
		tableHTML.append("<nobr>"+this.linkLabel+"</nobr>");
		tableHTML.append("</div>");
		tableHTML.append("</td></tr>");
		
		this.linkLabel = "";
		int nl = 2;
		for (; nl <= maxlinks; nl++)
		{
			if (containSrcTab(tabFolder, tabResource))
			{
				currLink = new Integer(nl - 1);
				
				pglnks = getLinksForTab(tabFolder, tabResource);
				SingleLink singlnk = null;
			
				if (pglnks.containLink(currLink))
				{
					singlnk = pglnks.getLinkPos(currLink.intValue());
					destTab = singlnk.getTarget();
					
					try {
						//if (sessionUser.getPermission(singlnk.getPerm().intValue()) != ProfileBean.PERMISSION_NONE)
						if (sessionUser.isMenuActive(singlnk.getTarget().split(";")[0]) && sessionUser.isTabActive(singlnk.getTarget().split(";")[0], singlnk.getTarget().split(";")[3])) 
						{
							//tableHTML += "<td onclick='linkk()' style='cursor:pointer'>" + nl + ">link " + nl;
							//tableHTML += "<td title='"+destTab+"' onclick='linkk("+nl+")' style='cursor:pointer' class='classetd'>" + nl + "-" + tabResource;

							try
							{
								link = getLink(destTab, sessionUser.getLanguage(), sessionUser.getProperty("iddev"), tabFolder);
							}
							catch (Exception e)
							{
								//PVPro-generated catch block
								Logger logger = LoggerMgr.getLogger(this.getClass());
								logger.error(e);
								
								link = "";
							}
							
							if (!link.equals(""))
							{
								tableHTML.append("<tr class='itemPVPRO2' onmouseover=top.frames[\'manager\'].evid(this,'itemoverPVPRO2') onmouseout=top.frames[\'manager\'].evid(this,'itemPVPRO2')><td></td><td height='30px'>");
								tableHTML.append("<div onclick=quicklink("+nl+") >");
								tableHTML.append("<input type='hidden' id='pos_"+nl+"' value='"+link+"' />");
								tableHTML.append("<nobr>"+this.linkLabel+"</nobr>");
								tableHTML.append("</div>");
								tableHTML.append("</td></tr>");
								this.linkLabel = "";
							}
							else //link non realizzabile
							{
								tableHTML.append("<tr><td></td><td height='30px'>");
								tableHTML.append("<div class='itemPVPRO2'>");
								tableHTML.append("<nobr>"+this.linkLabel+"</nobr>");
								tableHTML.append("</div>");
								tableHTML.append("</td></tr>");
								this.linkLabel = "";
							}
						}
						else //permesso profilo non valido
						{
							tableHTML.append("<tr><td></td><td height='30px'>");
							tableHTML.append("<div class='itemPVPRO2'>");
							tableHTML.append("<nobr>"+this.linkLabel+"</nobr>");
							tableHTML.append("</div>");
							tableHTML.append("</td></tr>");
							this.linkLabel = "";
						}
					} catch (Exception e) {//permesso profilo non esiste in sessione
						//PVPro-generated catch block
						Logger logger = LoggerMgr.getLogger(this.getClass());
						logger.error(e);
						
						tableHTML.append("<tr><td></td><td height='30px'>");
						tableHTML.append("<div class='itemPVPRO2'>");
						tableHTML.append("<nobr>"+this.linkLabel+"</nobr>");
						tableHTML.append("</div>");
						tableHTML.append("</td></tr>");
						this.linkLabel = "";
					}
				}
				else //link non presente
				{
					tableHTML.append("<tr><td></td><td height='30px'>");
					tableHTML.append("<div class='itemPVPRO2'>");
					tableHTML.append("<nobr>"+this.linkLabel+"</nobr>");
					tableHTML.append("</div>");
					tableHTML.append("</td></tr>");
					this.linkLabel = "";
				}
			}
			else //solo link di default
			{
				tableHTML.append("<tr><td></td><td height='30px'>");
				tableHTML.append("<div class='itemPVPRO2'>");
				tableHTML.append("<nobr>"+this.linkLabel+"</nobr>");
				tableHTML.append("</div>");
				tableHTML.append("</td></tr>");
				this.linkLabel = "";
			}
		}
		int height= MainMenu.MENU_HEIGHT-MainMenu.HEAD_HEIGHT-4*MainMenu.ITEM_HEIGHT;	
		tableHTML.append("<tr><td colspan=2 height='"+height+"px' align='center' valign='bottom'><table cellspacing='0' cellpadding='0' style='margin-bottom:20px;'><tr><td width='31px' height='3px' style='background-image:url(images/menusx/bg/navy_l.png);background-repeat: repeat-y;'></td><td style='background-color:#394990;width:160px;'></td><td width='31px' style='background-image:url(images/menusx/bg/navy_r.png);background-repeat: repeat-y;'></td></tr></table></td></tr>");
		tableHTML.append("</tbody>");
		tableHTML.append("</table>");
		tableHTML.append("</div>");
		return tableHTML.toString();
	}
	
	public String getLink(String destPath, String language, String iddev, String srcFolder)
	{
		LangService mlang = LangMgr.getInstance().getLangService(language);
		
		//destPath = "dtlview;BDtlView;SubTab1.jsp;tab1name;dettView"; //es. x test
		String targetPath = "";
		
		int folder = 0;
		int bo = 1;
		int res = 2;
		int curtab = 3;
		int desc = 4;
		
		String items[] = destPath.split(";");
		if (items.length > 0)
		{
			if (items[bo].equalsIgnoreCase("BDtlView"))
			{
				targetPath = "dtlview/FramesetTab.jsp";
			}
			else
			{
				targetPath = "nop";
			}
			
			targetPath += "&folder=" + items[folder];
			targetPath += "&bo=" + items[bo];
			
			//tab di default non crea loop nel breadcrumb-menu:
			if ((items[folder].equalsIgnoreCase(this.currFolder)) && (items[res].equalsIgnoreCase(this.currResource)))
				targetPath += "&navid=0";

			targetPath += "&type=click";
			targetPath += "&resource=" + items[res];
			targetPath += "&curTab=" + items[curtab];
			
			//targetPath += "&desc=" + items[desc];
			
			this.linkLabel = mlang.getString(this.sectioncode, items[desc]);
			if ((this.linkLabel == null) || (this.linkLabel.equals("")))
				this.linkLabel = items[desc];
			
			targetPath += "&desc=" + this.linkLabel;
			
			try {
				if (this.linkLabel.length() > this.maxlinklength)
					this.linkLabel = this.linkLabel.substring(0, this.maxlinklength);
			} catch (Exception e) {
				// PVPro-generated catch block:
	            Logger logger = LoggerMgr.getLogger(PageLinksMgr.class);
	            logger.error(e);
			}
			
			if (this.linkLabel.length() < this.maxlinklength)
				while (this.linkLabel.length() < this.maxlinklength) {
					this.linkLabel = this.linkLabel + " ";
					}

			String exxxtra = getExtra(items[bo],iddev,srcFolder);
			
			targetPath += exxxtra;
		}
		
		return targetPath;
	}
	
	// gestione casi particolari:
	private String getExtra(String bo, String iddev, String srcfold)
	{
		String extra = "";
		
		if (("BDtlView".equalsIgnoreCase(bo)) || ("BDevDetail".equalsIgnoreCase(bo)))
		{
			if (iddev != null)
				extra += "&iddev=" + iddev;
			else
				extra = "&iddev=-1"; //link non realizzabile
		}
		
		if (("BAlrSched".equalsIgnoreCase(bo)) || ("setaction".equalsIgnoreCase(srcfold)))
			extra += "&sched=false";
		else
		if (("BActSched".equalsIgnoreCase(bo)) || ("setaction2".equalsIgnoreCase(srcfold)))
			extra += "&sched=true";
		
		return extra;
	}
}