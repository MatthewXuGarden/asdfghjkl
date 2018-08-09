package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dataaccess.datalog.impl.TableExtBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;


public class GroupVarBeanList
{
    private Map groupVarList = new HashMap();
    private int[] ids = null;

    public GroupVarBeanList()
    {
    }

    public GroupVarBean retrieveGroupVarById(int idsite, int idgroupvar,
        String language) throws DataBaseException
    {
        String sql = "select cfgroupvar.*, cftableext.description " +
            "from cfgroupvar inner join cftableext on cfgroupvar.idgroupvar=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfgroupvar' and cfgroupvar.idsite = ?" +
            " and cfgroupvar.idgroupvar = ? and cftableext.idsite = ? ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(idsite), new Integer(idgroupvar),
                    new Integer(idsite)
                });

        GroupVarBean tmp = new GroupVarBean();

        tmp = new GroupVarBean(rs.get(0), idsite, language);
        groupVarList.put(new Integer(tmp.getIdGroupVar()), tmp);

        return tmp;
    }

    public GroupVarBean[] retrieveGroupVarByIdGroup(int idsite, int idgroup,
        String language) throws DataBaseException
    {
        String sql = "select cfgroupvar.*, cftableext.description " +
            "from cfgroupvar inner join cftableext on cfgroupvar.idgroupvar=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfgroupvar' and cfgroupvar.idsite = ? " +
            "and cfgroupvar.idgroup = ? and cfgroupvar.type!=4 and cftableext.idsite = ?" +
            " order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(idsite), new Integer(idgroup),
                    new Integer(idsite)
                });
        GroupVarBean[] tmp = new GroupVarBean[rs.size()];
        ids = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            tmp[i] = new GroupVarBean(rs.get(i), idsite, language);
            ids[i] = tmp[i].getIdGroupVar();
            groupVarList.put(new Integer(tmp[i].getIdGroupVar()), tmp[i]);
        }

        return tmp;
    }

    public GroupVarBean[] retrieveGroupAlrByIdGroup(int idsite, int idgroup,
        String language) throws DataBaseException
    {
        String sql = "select cfgroupvar.*, cftableext.description " +
            "from cfgroupvar inner join cftableext on cfgroupvar.idgroupvar=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfgroupvar' and cfgroupvar.idsite = ? and cfgroupvar.idgroup = ? " +
            "and cfgroupvar.type=4 and cftableext.idsite = ? order by cftableext.description ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(idsite), new Integer(idgroup),
                    new Integer(idsite)
                });
        GroupVarBean[] tmp = new GroupVarBean[rs.size()];
        ids = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            tmp[i] = new GroupVarBean(rs.get(i), idsite, language);
            ids[i] = tmp[i].getIdGroupVar();
            groupVarList.put(new Integer(tmp[i].getIdGroupVar()), tmp[i]);
        }

        return tmp;
    }

    public void removeGroupVar(int idsite, int idvargroup, String langcode)
        throws Exception
    {
        String sql = "delete from cfgroupvar where idsite = ? and idgroupvar = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite), new Integer(idvargroup) });

        TableExtBean.removeTableExt(idsite, langcode, "cfgroupvar", idvargroup);
    }

    public void updateGroupVar(int idsite, int idgroupvar, int type,
        String params, String measureunit) throws DataBaseException
    {
        String paramspk = "pk" + Replacer.replace(params, ",", ";pk");

        String sql = "update cfgroupvar set type = ?, parameters = ?, measureunit=?, lastupdate = ? where idsite = ? and idgroupvar = ?";
        Object[] values = new Object[6];
        values[0] = new Integer(type);
        values[1] = paramspk;
        values[2] = measureunit;
        values[3] = new Timestamp(System.currentTimeMillis());
        values[4] = new Integer(idsite);
        values[5] = new Integer(idgroupvar);

        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public int size()
    {
        return groupVarList.size();
    }

    public int[] getIds()
    {
        return ids;
    }

    public static void cleanGroupVar(int idsite) throws DataBaseException
    {
        Map group_var = new HashMap();
        String sql = "select idgroupvar,parameters from cfgroupvar where idsite = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });

        //Fill della mappa group_var:idgroupvar -> parameters
        for (int i = 0; i < rs.size(); i++)
        {
            group_var.put(rs.get(i).get("idgroupvar"),
                rs.get(i).get("parameters").toString());
        }

        sql = "select idvariable from cfvariable where idsite = ? and iscancelled = ?";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), "FALSE" });

        //Array con id di tutte le variabili non cancellate presenti nel sito
        int[] idsvariables = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            idsvariables[i] = ((Integer) rs.get(i).get("idvariable")).intValue();
        }

        //Per ogni groupvar devo fare il controllo di tutti i parametri, e in caso rimuoverne
        Iterator iter = group_var.keySet().iterator();

        while (iter.hasNext())
        {
            //creo array di parametri per variabile di gruppo
            Integer key = (Integer) iter.next();
            String param = group_var.get(key).toString();
            String[] param_split = param.split(";");
            List param_int = new ArrayList();

            for (int i = 0; i < param_split.length; i++)
            {
                param_split[i] = param_split[i].replace("pk", "");
                param_int.add(param_split[i]);
            }

            //ora ho id delle variabili attive e i parametri della vargroup
            List param_int_aux = new ArrayList();

            for (int i = 0; i < param_int.size(); i++) //scorro param
            {
                for (int j = 0; j < idsvariables.length; j++) //scorro variabili
                {
                    if (Integer.parseInt(param_int.get(i).toString()) == idsvariables[j])
                    {
                        param_int_aux.add(param_int.get(i));

                        break;
                    }
                }
            }

            //se ho param="" rimuovo la variabile di gruppo	
            if (param_int_aux.size() == 0)
            {
                //rimuovo da db la groupvar
                sql = "delete from cfgroupvar where idsite = ? and idgroupvar = ?";
                DatabaseMgr.getInstance().executeStatement(null, sql,
                    new Object[] { new Integer(idsite), key });
            }
            else
            {
                //riscrivo param a dalla lista param.
                StringBuffer par_to_save = new StringBuffer();

                for (int z = 0; z < param_int_aux.size(); z++)
                {
                    par_to_save.append("pk" + param_int_aux.get(z) + ";");
                }

                sql = "update cfgroupvar set parameters = ? where idsite = ? and idgroupvar = ? ";

                Object[] param_sql = new Object[3];
                param_sql[0] = par_to_save.substring(0, par_to_save.length()-1);
                param_sql[1] = new Integer(idsite);
                param_sql[2] = key;
                DatabaseMgr.getInstance().executeStatement(null, sql, param_sql);
            }
        }
    }

    public String getHTMLGroupVarTable(int width, int height, String language)
    {
        LangService lang = LangMgr.getInstance().getLangService(language);
        // Alessandro Vianello: aggiunto supporto alla tastiera virtuale
        boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
        String cssVirtualKeyboardClass = "";

        //header table
        String desc = lang.getString("groupvar", "desc");
        String newval = lang.getString("groupvar", "newval");
        String um = lang.getString("groupvar", "um");
        String[] headerTable = new String[] { desc, newval, um };
        HTMLElement[][] dati = new HTMLElement[size()][];

        for (int i = 0; i < size(); i++)
        {
        	String mask = "";
        	GroupVarBean tmp = getGroupVar(ids[i]);
            dati[i] = new HTMLElement[3];
            dati[i][0] = new HTMLSimpleElement(tmp.getDescription());
            if (tmp.getType()==1)   //digitali
            {
            	mask = " onkeydown='checkOnlyDigit(this,event);' onblur='checkOnlyDigitOnBlur(this);' ";
            }
            else if (tmp.getType()==2)  //interi
            {
            	mask = " onkeydown='checkOnlyAnalog(this,event);' onblur='checkOnlyAnalogOnBlur(this);' ";
            }
            else if(tmp.getType()==3) //analogiche
            {
            	mask = " onkeydown='checkOnlyNumber(this,event);' onblur='onlyNumberOnBlur(this,event);' ";
            }
            // se la tastiera � abilitata aggiungo la classe css
            if (OnScreenKey) cssVirtualKeyboardClass = ","+VirtualKeyboard.getInstance().getCssClass();
            dati[i][1] = new HTMLSimpleElement("<INPUT class='standardTxt"+cssVirtualKeyboardClass+"' type='text' id='gv" +
                    ids[i] + "' name='gv" + ids[i] + "' "+mask+" />");
            dati[i][2] = new HTMLSimpleElement(tmp.getMeasureunit());
        }
       
        HTMLTable table = new HTMLTable("groupvar", headerTable, dati);
        table.setAlignType(new int[] { 0, 1, 1 });
        table.setColumnSize(0, (int)Math.floor((width*0.4)));
        table.setColumnSize(1, (int)Math.floor((width*0.15)));
        table.setColumnSize(2, (int)Math.floor((width*0.1)));
        table.setRowHeight(20);
        table.setWidth((int)Math.floor((width*0.75)));
        table.setHeight((int)Math.floor(height*0.48));

        String htmlTable = table.getHTMLText();

        return htmlTable;
    }

    public GroupVarBean getGroupVar(int idgroupvar)
    {
        return (GroupVarBean) groupVarList.get(new Integer(idgroupvar));
    }
}
