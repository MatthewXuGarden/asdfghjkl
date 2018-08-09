package com.carel.supervisor.presentation.vscheduler;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;

public class VSBase {
	public static final String SYMBOL_PATH = "images/scheduler/symbols/";
	
	protected int idSite;
	protected LangService lang;
	protected int nScreenWidth = 1024;
	protected int nScreenHeight = 768;

	
	public VSBase()
	{
		idSite = 0;
		lang = null;
	}
	
	
	public VSBase(int idSite, String language)
	{
		this.idSite = idSite;
		this.lang = LangMgr.getInstance().getLangService(language);
	}
	
	
	public int getIdSite()
	{
		return idSite;
	}
	
	
	public String getLanguage()
	{
		return lang != null ? lang.getLanguage() : "EN_en";
	}
	
	
	public void setScreenSize(int nWidth, int nHeight)
	{
		nScreenWidth = nWidth;
		nScreenHeight = nHeight;
	}
	
	
    static public String xmlEscape(String str)
    {
    	str = str.replaceAll("&", "&amp;"); // this should be always at the first . 
    	str = str.replaceAll("<", "&lt;");
    	str = str.replaceAll(">", "&gt;");
    	str = str.replaceAll("'", "&#039;");
    	str = str.replaceAll("\"", "&quot;");
    	return str;
    }


    static public String xmlUnEscape(String str)
    {
    	str = str.replaceAll("&lt;", "<");
    	str = str.replaceAll("&gt;", ">");
    	str = str.replaceAll("&#039;", "'");
    	str = str.replaceAll("&quot;", "\"");
    	str = str.replaceAll("&amp;", "&");
    	return str;
    }
}
