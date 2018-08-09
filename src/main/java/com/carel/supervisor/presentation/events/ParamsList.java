package com.carel.supervisor.presentation.events;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.script.EnumerationMgr;

public class ParamsList 
{
	private int screenw = 1024;
    private int screenh = 768;
    private int width = 0;
    private int height = 0;
    private HTMLElement[][] list = null;
    private HsParam[] paramList=null;
    private final static int NUM_PARAMS_4_PAGE = 100;
    private int pageNumber = 1;
    private int pageTotal = 1;
    
	public ParamsList() 
	{
	}
	
	 public void loadFromDataBase(UserSession userSession, int numPage, Integer id)
    {
		 userSession.setProperty("IDPARAM",id.toString());
		 loadFromDataBase(userSession, numPage);
		 
    }
	 
	 public void loadFromDataBase(UserSession userSession, int numPage)
	 {
		 	paramList = loadFromDataBasePRV(userSession, numPage);
		 	
			list = new HTMLElement[paramList.length][];
			
			String label = "";
			String lang = userSession.getLanguage();
			//visualizzo parametri nell'ordine in cui sono stati settati sul campo
			for(int i = 0; i < paramList.length; i++)
			{
				list[i] = new HTMLElement[5];
				
				HsParam p = paramList[i];
				
				list[i][0] = new HTMLSimpleElement(p.getDeviceDesc());
				list[i][1] = new HTMLSimpleElement(p.getVariableDesc());
				
				label = EnumerationMgr.getInstance().getEnumCode(p.getIdVarMdl(),  p.getOldValue().floatValue() , lang);
				if (!"".equals(label))
					list[i][2] = new HTMLSimpleElement(label);
				else
					list[i][2] = new HTMLSimpleElement(p.getOldValue().toString());
				
				label = EnumerationMgr.getInstance().getEnumCode(p.getIdVarMdl(),  p.getNewValue().floatValue() , lang);
				if (!"".equals(label))
					list[i][3] = new HTMLSimpleElement(label);
				else
					list[i][3] = new HTMLSimpleElement(p.getNewValue().toString());
				
				list[i][4] = new HTMLSimpleElement(p.getCodeDesc());
			}
	 }
	 
	 private HsParam[] loadFromDataBasePRV(UserSession userSession, int numPage)
	    {
	        try
	        {
	        	Integer id = new Integer(userSession.getProperty("IDPARAM"));
	            list = null;
	            StringBuffer sql = new StringBuffer("select dev.description as descdev,var.description as descvar,hsparams.code,hsparams.oldvalue,hsparams.newvalue,v.idvarmdl from " +
	            		"hsparams,cftableext as dev,cftableext as var,cfvariable as v" +
				" where dev.tablename='cfdevice' and dev.tableid=hsparams.iddevice and dev.idsite=? and dev.languagecode=? and " +
				" var.tablename='cfvariable' and var.tableid=hsparams.idvariable and var.idsite=? and var.languagecode=? and " +
				"hsparams.id=? and v.idvariable=hsparams.idvariable order by descvar");
		
	            if (numPage == 0)
	            {
	                RecordSet recordSet = null;

	                String sqlTmp = "select count(1) as count from hsparams where hsparams.id=?";
	                    recordSet = DatabaseMgr.getInstance().executeQuery(null, sqlTmp, new Object[]{id});
	                
	                if (recordSet.size() > 0){
	                    pageNumber = ((Integer) recordSet.get(0).get("count")).intValue() / NUM_PARAMS_4_PAGE;
	                }else{
	                    pageNumber = 1;
	                }

	                numPage = pageNumber + 1;
	                pageNumber++;
	            } //page
	            
	            //simon add for get total count. at 2009-2-27
                String sqlTmp = "select count(1) as count from hsparams where hsparams.id=?";
                RecordSet totSet = DatabaseMgr.getInstance().executeQuery(null, sqlTmp, new Object[]{id});
            
	            if (totSet.size() > 0){
	            	this.pageTotal = ((Integer) totSet.get(0).get("count")).intValue() / NUM_PARAMS_4_PAGE+1;
	            }else{
	            	this.pageTotal = 1;
	            }
	            //end. simon add
	            
	            Object[] param;
	            if (numPage>-1)
	            {
	            sql.append(" limit ? offset ? ");
	            param = new Object[]{userSession.getIdSite(), 
	            		userSession.getLanguage(),userSession.getIdSite(),userSession.getLanguage(),id, new Integer(NUM_PARAMS_4_PAGE),
                        new Integer((numPage - 1) * NUM_PARAMS_4_PAGE)};
	            
	            }
	            else{
		            param = new Object[]{userSession.getIdSite(), 
		            		userSession.getLanguage(),userSession.getIdSite(),userSession.getLanguage(),id};
	            }
	            
	            RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), param);
				Record r = null;
				int num = recordset.size();

	            HsParam[] result = new HsParam[num];
	            
				list = new HTMLElement[num][];
				LangService lan = LangMgr.getInstance().getLangService(userSession.getLanguage());
				//visualizzo parametri nell'ordine in cui sono stati settati sul campo
				for(int i = 0; i < num; i++)
				{
					result[i]= new HsParam();
					r = recordset.get(i);
					result[i].setDeviceDesc((String)r.get(0));
					result[i].setVariableDesc((String)r.get(1));
					result[i].setOldValue(((Double)r.get(3)));
					result[i].setNewValue(((Double)r.get(4)));
					Integer code = ((Integer)r.get(2));
					result[i].setCode(code);
					result[i].setCodeDesc(lan.getString("code", code.toString()));
					result[i].setIdVarMdl((Integer) r.get(5));
				}
				return result;
	        }
	        catch (Exception e)
	        {
	            Logger logger = LoggerMgr.getLogger(this.getClass());
	            logger.error(e);
	            return new HsParam[0];
	        }
	    }
	 
	public static boolean containsParams(String messagecode, UserSession userSession)
	{
		if( messagecode.equals("W050") || messagecode.equals("W051") || 
			messagecode.equals("W052") || messagecode.equals("W057") ||
			messagecode.equals("W054") || messagecode.equals("W038") ||
			messagecode.equals("W055") || messagecode.equals("W056") || 
			messagecode.equals("W058")
			|| messagecode.equals("VS01") || messagecode.equals("VS02") || messagecode.equals("VS03") )
		{
			if (userSession.localVisibility())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
		
			return false;
		}
	}
	
	private HTMLTable getHtmlInner(UserSession session)
	{
		
		LangService lan = LangMgr.getInstance().getLangService(session.getLanguage());
		String tableName = "PARAM";
        String[] header = null;
        header = new String[]
            {
                lan.getString("Param", "Device"), lan.getString("Param", "Var"),
                lan.getString("Param", "Viniz"), lan.getString("Param", "Vfin"),
                lan.getString("Param", "Stato")
            };
		
		HTMLTable table = new HTMLTable(tableName, header, list, false, false);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setWidth(width);
        table.setHeight(height);
        table.setPage(true);
        table.setPageNumber(pageNumber);
        table.setPageTotal(pageTotal);
        table.setColumnSize(0, 200);
        table.setColumnSize(1, 200);
        table.setColumnSize(2, 50);
        table.setColumnSize(3, 50);
        table.setColumnSize(4, 100);
        table.setAlignType(3, 1);
        table.setAlignType(2, 1);
        table.setRowHeight(18);
        table.setTypeColum(0, HTMLTable.TDATA); 
		return table;
	}
	
	public String getHtml(UserSession session)
	{
		return getHtmlInner(session).getHTMLTextBuffer().toString();
	}
	
	public String getHtmlRefresh(UserSession session)
	{
		return getHtmlInner(session).getHTMLTextBufferRefresh().toString();
	}
	
	 public void setScreenH(int height) {
	    	this.screenh = height;
	    }
	    
	    public void setScreenW(int width) {
	    	this.screenw = width;
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

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public HsParam[] getParamList() {
		return paramList;
	}
}
