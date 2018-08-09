package com.carel.supervisor.controller.pagelinks;

import java.util.HashMap;
import java.util.List;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.Record;

public class PageLinks {
	
	// tutti i links da uno stesso tab:
	private HashMap<Integer, SingleLink> pagelinks = new HashMap<Integer, SingleLink>();
	
	public PageLinks(String srcTab, List<Record> paglinks)
	{
		Integer linkpos = null;
		String dstTab = "";
		String linkDescrCode = "";
		Integer lnkPerm = null;
		SingleLink singlnk = null;
		
		if ((paglinks != null) && (paglinks.size() > 0))
		{
			for (int i=0; i < paglinks.size(); i++)
			{
				linkpos = null;
				try {
					linkpos = (Integer) paglinks.get(i).get(PageLinksMgr.LINK_POS);
				} catch (Exception e) {
					// PVPro-generated catch block:
		            Logger logger = LoggerMgr.getLogger(PageLinks.class);
		            logger.error(e);
				}
				
				dstTab = null;
				try {
					dstTab = (String) paglinks.get(i).get(PageLinksMgr.DEST_TAB);
				} catch (Exception e) {
					// PVPro-generated catch block:
		            Logger logger = LoggerMgr.getLogger(PageLinks.class);
		            logger.error(e);
				}
				
				linkDescrCode = null;
				try {
					linkDescrCode = (String) paglinks.get(i).get(PageLinksMgr.LINK_DESCRCODE);
					dstTab = dstTab + ";" + linkDescrCode;
				} catch (Exception e) {
					// PVPro-generated catch block:
		            Logger logger = LoggerMgr.getLogger(PageLinks.class);
		            logger.error(e);
				}
				
				lnkPerm = null;
				try {
					lnkPerm = (Integer) paglinks.get(i).get(PageLinksMgr.LINK_PERM);
				} catch (Exception e) {
					// PVPro-generated catch block:
		            Logger logger = LoggerMgr.getLogger(PageLinks.class);
		            logger.error(e);
				}
				
				singlnk = new SingleLink(dstTab, lnkPerm);
				pagelinks.put(linkpos, singlnk);
			}
		}
	}
	
	public boolean containLink(Integer linkPos)
	{
		if ((this.pagelinks != null) && (this.pagelinks.size() > 0) && (this.pagelinks.containsKey(linkPos)))
			return true;
		else
			return false;
	}
	
	public SingleLink getLinkPos(Integer pos)
	{
		if (containLink(pos))
		{
			return pagelinks.get(pos);
		}
		else
		{
			return null;
		}
	}

}
