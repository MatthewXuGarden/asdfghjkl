package com.carel.supervisor.presentation.bean.rule;

import java.util.Map;

import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.database.RuleListBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class RuleNoCondBeanHelper extends RuleListBean
{
	private int screenw = 1024;
	private int screenh = 768;
	
    public RuleNoCondBeanHelper(Integer idSite) throws DataBaseException
    {
        super();
        loadNoCondRules(idSite);
    }

    public String getHTMLtable(int idsite, String plantId, String language,
        String title, int height, int width, boolean isScheduled,
        boolean isPermict) throws Exception
    {
        TimeBandList timebandslist = new TimeBandList(null, plantId,
                new Integer(idsite));
        
        ActionBeanList actionlist = new ActionBeanList(idsite, language,
                isScheduled);

        LangService lan = LangMgr.getInstance().getLangService(language);
        String[] ClickRowFunction = new String[size()];
        String[] DBLClickRowFunction = new String[size()];
        String[] RowClass = new String[size()];

        HTMLElement[][] dati = new HTMLElement[size()][];
        RuleBean tmp_rule = null;
        String s_action = "";
        String s_timeband = "";
        Map codemap = actionlist.getActionCodeMap();
        
        for (int i = 0; i < size(); i++)
        {
            tmp_rule = get(i);
            s_action = actionlist.getDescription(idsite,tmp_rule.getActionCode().intValue());

            if (tmp_rule.getIdTimeband().intValue() != 0)
            {
                s_timeband = timebandslist.get(tmp_rule.getIdTimeband())
                                          .getTimecode();
            }

            if (codemap.get(tmp_rule.getActionCode()).toString().contains("X"))
            {
                RowClass[i] = "statoAllarme1_b";
            }
            else
            {
                RowClass[i] = i%2==0?"Row1":"Row2";
            }

            dati[i] = new HTMLElement[5];

            if (tmp_rule.getIsenabled().equals("FALSE"))
            {
                dati[i][0] = new HTMLSimpleElement("");
            }
            else
            {
                dati[i][0] = new HTMLSimpleElement("<img src='images/ok.gif' >");
            }

            dati[i][1] = new HTMLSimpleElement(tmp_rule.getRuleCode());
            dati[i][2] = new HTMLSimpleElement(s_timeband);
            dati[i][3] = new HTMLSimpleElement(s_action);
            dati[i][4] = new HTMLSimpleElement(String.valueOf(
                        tmp_rule.getDelay() / 60));

            ClickRowFunction[i] = String.valueOf(tmp_rule.getIdRule());
        }

        //header tabella
        String[] headerTable = new String[5];
        headerTable[0] = lan.getString("alrsched", "enable");
        headerTable[1] = lan.getString("alrsched", "description");
        headerTable[2] = lan.getString("alrsched", "timebands");
        headerTable[3] = lan.getString("alrsched", "action");
        headerTable[4] = lan.getString("alrsched", "delay");

        HTMLTable table = new HTMLTable("rules", headerTable, dati);

        if (isPermict)
        {
            table.setSgClickRowAction("selectedLineRule('$1')");
            table.setSnglClickRowFunction(ClickRowFunction);
            table.setDbClickRowAction("modifyRule('$1')");
            table.setDlbClickRowFunction(ClickRowFunction);
        }

        table.setAlignType(new int[] { 1, 1, 1, 1, 1 });
        table.setTableTitle(title);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setColumnSize(0, 78);
        table.setColumnSize(1, 232);
        table.setColumnSize(2, 194);
        table.setColumnSize(3, 194);
        table.setColumnSize(4, 72);
        table.setRowHeight(18);
        table.setWidth(width);
        table.setHeight(height);
        table.setRowsClasses(RowClass);

        String htmlTable = table.getHTMLText();

        return htmlTable;
    }

    public static String getRuleDescription(int idsite, int idrule)
        throws DataBaseException
    {
        String sql = "select rulecode from cfrule where idsite=? and idrule=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idrule) });

        if (rs.size() > 0)
        {
            return rs.get(0).get("rulecode").toString();
        }
        else
        {
            return "";
        }
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }

}
