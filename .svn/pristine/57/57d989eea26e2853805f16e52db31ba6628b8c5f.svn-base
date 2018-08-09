package com.carel.supervisor.presentation.bean.rule;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.setfield.BackGroundCallBack;
import com.carel.supervisor.controller.setfield.OnLineCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.HsRelayBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class RelayBeanList
{
    private Map relayMap = new HashMap();
    private int[] ids = null;
    private int numRelaysToShow = 0;
    private int screenw = 1024;
    private int screenh = 768;

    /*
     * Costruttore Empty
     * Server per poter utilizzare i metodi di update del bean
     */
    public RelayBeanList()
    {
    }

    public RelayBeanList(int idsite, String language) throws DataBaseException
    {
        String sql = "";
        RecordSet rs = null;
        numRelaysToShow = 0;

        sql = "select a.*,b.description,d.description as device "+
				"from cfrelay as a,cftableext as b,cfvariable as c, cftableext as d "+
				"where a.idsite=? and a.idsite=b.idsite and c.idvariable = a.idvariable and "+ 
				"b.tablename='cfvariable' and a.iscancelled='FALSE' and b.tableid=a.idvariable and "+
				"b.languagecode=? and c.iddevice = d.tableid and d.tablename='cfdevice' and "+
				"d.languagecode=? "+
				"order by device, b.description";


        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), language, language });
        ids = new int[rs.size()];

        RelayBean tmp = null;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = new RelayBean(rs.get(i));
            ids[i] = tmp.getIdrelay();
            relayMap.put(new Integer(tmp.getIdrelay()), tmp);
            if(tmp.getShow())
            	numRelaysToShow ++;
        }
    }

    /*
     * Costruttore FULL
     * Con questo costrutture viene recuperata anche la descrizione del device di appartenenza del relay.
     * Tale descrizione viene mantenuta in RelayBean.
     */
    public RelayBeanList(int idsite, String language, boolean full)
        throws DataBaseException
    {
        String sql = "";
        RecordSet rs = null;
        numRelaysToShow = 0;

        sql = "select a.*,d.description as device,b.description from cfrelay as a,cftableext as b,cfvariable as c," +
            "cftableext as d where a.idsite=? and a.idsite=b.idsite and c.idvariable = a.idvariable and " +
            "b.tablename='cfvariable' and a.iscancelled='FALSE' and b.tableid=a.idvariable and " +
            "b.languagecode=? and c.iddevice = d.tableid and d.tablename='cfdevice' and " +
            "d.languagecode=? order by device,b.description";

        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), language, language });
        ids = new int[rs.size()];

        RelayBean tmp = null;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = new RelayBean(rs.get(i));
            ids[i] = tmp.getIdrelay();
            relayMap.put(new Integer(tmp.getIdrelay()), tmp);
            if(tmp.getShow())
            	numRelaysToShow ++;
        }
    }
    
    //new constructor with boolean flag to hide InternalIO
    public RelayBeanList(int idsite, String language, boolean full, boolean hideInternalIO)
    throws DataBaseException
    {
    	String sql = "";
        RecordSet rs = null;
        numRelaysToShow = 0;

        sql = "select a.*,d.description as device,b.description from cfrelay as a,cftableext as b,cfvariable as c," +
            "cftableext as d where a.idsite=? and a.idsite=b.idsite and c.idvariable = a.idvariable and " +
            "b.tablename='cfvariable' and a.iscancelled='FALSE' and b.tableid=a.idvariable and " +
            "b.languagecode=? and c.iddevice = d.tableid and d.tablename='cfdevice' and " +
            "d.languagecode=?"; 
   	
    	if(hideInternalIO)
    	{
    		sql += " and a.idvariable not in (select idvariable from cfvariable where idvarmdl in (select idvarmdl from cfvarmdl where iddevmdl=(select iddevmdl from cfdevmdl where code = 'Internal IO')))";
    	}
    	
    	sql += "order by device,b.description";
    	
    	rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), language, language });
        ids = new int[rs.size()];

        RelayBean tmp = null;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = new RelayBean(rs.get(i));
            ids[i] = tmp.getIdrelay();
            relayMap.put(new Integer(tmp.getIdrelay()), tmp);
            if(tmp.getShow())
            	numRelaysToShow ++;
        } 
    }

    public static RelayBean[] getInternalIORelayBeans(int idsite,String language)
    throws DataBaseException
    {
    	RelayBean[] internalIORelayBean = null;
    	
    	String sql = "";
        RecordSet rs = null;

        sql = "select a.*,d.description as device,b.description from cfrelay as a,cftableext as b,cfvariable as c," +
            "cftableext as d,cfvarmdl as e, cfdevmdl as f where a.idsite=? and a.idsite=b.idsite and c.idvariable = a.idvariable and " +
            "b.tablename='cfvariable' and a.iscancelled='FALSE' and b.tableid=a.idvariable and " +
            "b.languagecode=? and c.iddevice = d.tableid and d.tablename='cfdevice' and " +
            "d.languagecode=? " +
            "and c.idvarmdl=e.idvarmdl and e.iddevmdl=f.iddevmdl "+
            "and f.code = 'Internal IO' "+
            "order by device,b.description";
    	
    	
    	rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), language, language });
        
    	internalIORelayBean = new RelayBean[rs.size()];

        RelayBean tmp = null;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = new RelayBean(rs.get(i));
            internalIORelayBean[i] = tmp;
        } 

    	return internalIORelayBean;
    }
    
    
    public static RelayBean getRelayBeanByVariableid(int idsite,String language,int idvariable)
    {
        String sql = "";
        RecordSet rs = null;
        RelayBean tmp = null;
        sql = "select a.*,d.description as device,b.description from cfrelay as a,cftableext as b,cfvariable as c," +
            "cftableext as d where a.idsite=? and a.idsite=b.idsite and c.idvariable = a.idvariable and " +
            "b.tablename='cfvariable' and a.iscancelled='FALSE' and b.tableid=a.idvariable and " +
            "b.languagecode=? and c.iddevice = d.tableid and d.tablename='cfdevice' and " +
            "d.languagecode=? and a.idvariable=? order by device,b.description";

        try
        {
	        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	                new Object[] { new Integer(idsite), language, language,new Integer(idvariable) });
	        for (int i = 0; i < rs.size(); i++)
	        {
	            tmp = new RelayBean(rs.get(i));
	            break;
	        }
        }
        catch(Exception ex)
        {}
        return tmp;
    }
    public void updateRelayConfiguration(int idRelay, String resetType,
        String activeState, String timereset, String showrelay)
    {
        String sql = "update cfrelay set resettype=?,resettime=?,activestate=?,show=? where idrelay=?";
        
        boolean showFlag = true;
        if(showrelay.equals("on"))
        	showFlag = true;
        else
        	showFlag = false;
        
        Object[] param = 
            {
                resetType, new Integer(timereset), new Integer(activeState),
                showFlag, new Integer(idRelay)
            };

        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, param);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    
    
    public int size()
    {
        return relayMap.size();
    }
    
    public int relaysToShow()
    {
    	return numRelaysToShow;
    }

    public int[] getIds()
    {
        return ids;
    }

    public RelayBean getRelayBean(int i)
    {
        return (RelayBean) relayMap.get(new Integer(ids[i]));
    }

    public RelayBean getRelayBeanById(int i)
    {
        if (relayMap.get(new Integer(i)) != null)
        {
            return (RelayBean) relayMap.get(new Integer(i));
        }
        else
        {
            return null;
        }
    }

    public void resetRelay(int idsite, String lang, String user,
        boolean online, int[] idsrelay)
    {
        RelayBean tmp = null;
        int[] idsvariable = new int[idsrelay.length];
        String[] values = new String[idsrelay.length];

        for (int i = 0; i < values.length; i++)
        {
            tmp = (RelayBean) relayMap.get(new Integer(idsrelay[i]));
            idsvariable[i] = tmp.getIdvariable();

            if (tmp.getActivestate() == 0)
            {
                values[i] = "1";
            }
            else
            {
                values[i] = "0";
            }
        }

        SetContext setContext = new SetContext();
		setContext.setLanguagecode(lang);
    	setContext.addVariable(idsvariable, values);
    	if (online)
    	{
    		setContext.setCallback(new OnLineCallBack());
    	}
    	else
    	{
    		setContext.setCallback(new BackGroundCallBack());
    	}
		setContext.setUser(user);
        SetDequeuerMgr.getInstance().add(setContext);

        // Rimuovo dalla memoria-tabella degli automatici.
        HsRelayBean.getInstance().reset(idsrelay);
    }
    
    
    // get relay table with the same 'format' of the PVPRO standard tables
    // this table is periodically refreshed (see relay.js)
    public String getHTMLRelayTableRefresh(int width, int height, int [] columnsSize ,int idsite, String language)
    throws Exception
    {
		LangService multiLanguage = LangMgr.getInstance().getLangService(language);
		StringBuffer html = new StringBuffer();
		StringBuffer html_restart = new StringBuffer();
	    html_restart.append((new StringBuilder("<p class='standardTxt' ><font color='#FF0000'>")).append(multiLanguage.getString("relaymgr","restartmsg")).append("</p><br>").toString());
        DeviceListBean devices = new DeviceListBean(idsite, language);
        RelayBean relay = null;
        String isactive = "";
	    String value_from_field = "";
	    String idsActive = "";
        boolean restart = true;
        VarphyBean tmp = null;
        String device_desc = "";
        int rows = numRelaysToShow;  
        int totrelays = size();
        int numrow = 0;
        
        if(rows > 0)
	    {
            // table header
            LangService langService = LangMgr.getInstance().getLangService(language);
            String[] header = new String[]
                {
                    langService.getString("relaymgr", "relay"),
                    langService.getString("relaymgr", "status"),
                    langService.getString("relaymgr", "setidle"),
                    
                };

            HTMLElement[][] data = null;
            
            data = new HTMLElement[rows][3];

            for (int i = 0; i < totrelays; i++)
            {  
            	relay = getRelayBean(i);
	            
            	//show relay only if "show" column (cfrelay) is true
            	if(relay.getShow()) 
	            {
	            	try
		            {
		                value_from_field = ControllerMgr.getInstance().getFromField(relay.getIdvariable()).getFormattedValue();
		            }
		            catch(Exception e)
		            {
		                value_from_field = "norestart";
		                restart = false;
		            }
	            	
		            // if  value is correctly retrieved and the relay status is 'active'
		            if(value_from_field != null && !value_from_field.equalsIgnoreCase("***") && !value_from_field.equalsIgnoreCase("norestart") && Integer.parseInt(value_from_field) == relay.getActivestate())
		            {
		                isactive = (new StringBuilder("<b><span id='reltab_").append(numrow).append("_1' >").append("<font color='red'> ").append(multiLanguage.getString("relaymgr", "active")).append(" </font></span></b>").toString());
		                idsActive = (new StringBuilder(String.valueOf(idsActive))).append(relay.getIdrelay()).append(";").toString();
		            } 
		            else
		            {
		            	// if  value is correctly retrieved and the relay status is 'offline'
		            	if (!value_from_field.equalsIgnoreCase("***"))
		            	{
		            	isactive = (new StringBuilder("<b><span id='reltab_").append(numrow).append("_1' >").append("<font color='green'> ").append(multiLanguage.getString("relaymgr", "noactive")).append("</font></span></b>").toString());
		            	}
		            	// if  value is correctly retrieved and the relay status is 'idle'
		            	else
		            	{
		            	isactive = (new StringBuilder("<b><span id='reltab_").append(numrow).append("_1' >").append("<font color='gray'> ").append(multiLanguage.getString("code", "-101")).append("</font></span></b>").toString());
		            	}
		            }
		            
		            tmp = VarphyBeanList.retrieveVarById(idsite, relay.getIdvariable(), language);
		            device_desc = devices.getDevice(tmp.getDevice().intValue()).getDescription();
		            
		            data[numrow] = new HTMLElement[3];
		            
		            data[numrow][0] = new HTMLSimpleElement((new StringBuilder("<br> <span id='reltab_").append(numrow).append("_0' >").append(device_desc).append(" -> ").append(relay.getDescription()).append("</span>")).toString());
		            
		            // if  value is NOT correctly retrieved
		            // it is the case on which PVPRO has new device/s with relay variables and needs an engine restart to show them correctly
		            // before restart all new relay configured to be shown on relaymgr page are displayed as 'offline' 
		            if(!value_from_field.equalsIgnoreCase("norestart"))
		           	{
		           		
		            	data[numrow][1] = new HTMLSimpleElement((new StringBuilder("<br>").append(isactive)).toString());
		           		
		            	if(value_from_field != null && !value_from_field.equalsIgnoreCase("***") && !value_from_field.equalsIgnoreCase("norestart") && Integer.parseInt(value_from_field) == relay.getActivestate())
		                   {
		            		data[numrow][2] = new HTMLSimpleElement((new StringBuilder("<br> <span id='reltab_").append(numrow).append("_2' >").append("<input ")).append(!value_from_field.equalsIgnoreCase("***") && Integer.parseInt(value_from_field) == relay.getActivestate() ? "" : "disabled").append(" type='checkbox' name='chr_").append(relay.getIdrelay()).append("' /></span>").toString());
		                   }
		           		else
		           		{
		           			data[numrow][2] = new HTMLSimpleElement((new StringBuilder("<br> <span id='reltab_").append(numrow).append("_2' >  </span>")).toString()); //empty row
		           		}
		           	}
		            else
		            {
		            	isactive = (new StringBuilder("<b><span id='reltab_").append(numrow).append("_1' >").append("<font color='gray'> ").append(multiLanguage.getString("code", "-101")).append("</font></span></b>").toString());
		            	data[numrow][1] = new HTMLSimpleElement((new StringBuilder("<br>").append(isactive)).toString());
		            	data[numrow][2] = new HTMLSimpleElement((new StringBuilder("<br> <span id='reltab_").append(numrow).append("_2' >  </span>")).toString()); //empty row
		            	
		            }
		            numrow ++;   
	            }	
            }
        
            HTMLTable relayTable = new HTMLTable("relaytable", header, data, false, false);
            relayTable.setRowHeight(45);
            relayTable.setWidth(900);
            relayTable.setHeight(340);

            relayTable.setAlignType(2,HTMLTable.CENTER);
            relayTable.setScreenH(screenh);
            relayTable.setScreenW(screenw);
            //relayTable.setHeight(height);
            relayTable.setWidth(width);
            relayTable.setColumnSize(0, columnsSize[0]);
            relayTable.setColumnSize(1, columnsSize[1]);
            relayTable.setColumnSize(2, columnsSize[2]);
           
            html.append(relayTable.getHTMLText());
            
            // insert 'active relays' ids on a 'hidden' input type
            // managed by BRelayMgr
            if(!idsActive.equals(""))
	        {
	            idsActive = idsActive.substring(0, idsActive.length() - 1);
	            html.append((new StringBuilder("<input type='hidden' name='idsactive' value='")).append(idsActive).append("'>").toString());
	        } else
	        {
	            html.append("<input type='hidden' name='idsactive' value='noactive'>");
	        }
	    }
        else
	    {
	        html.append((new StringBuilder("<table align='center'><tr><td><b>")).append(multiLanguage.getString("relaymgr", "norelay")).append("</b></td></tr></table>").toString());
	        html.append("<input type='hidden' name='idsactive' value='noactive'>");
	    }
	    if(!restart)
	    {
	        html_restart.append(html);
	        return html_restart.toString();
	    } 
	    else
	    {
	        return html.toString();
	    }
    }
    
    public void setScreenW(int screenW)
    {
    	screenw = screenW;
    }
    
    public void setScreenH(int screenH)
    {
    	screenh = screenH;
    }
}
