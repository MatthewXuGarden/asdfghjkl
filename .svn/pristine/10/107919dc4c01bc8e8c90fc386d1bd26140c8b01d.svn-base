package com.carel.supervisor.presentation.assistance;

import java.util.Properties;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class Variables
{
    private final static String DEVICE = "device";
    private final static String LANGUAGE = "language";
    StringBuffer varTable = new StringBuffer();
    StringBuffer bodyTable = new StringBuffer();

    public Variables(Properties properties) throws Exception
    {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT cfvariable.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr ");
        sql.append("FROM  cfvariable,cftableext ");
        sql.append("WHERE  cfvariable.idvariable=cftableext.tableid ");
        sql.append("AND    cfvariable.iddevice=? ");
        sql.append("AND    cfvariable.iscancelled=? ");
        sql.append("AND    cftableext.tablename='cfvariable' ");
        sql.append("AND    cftableext.languagecode=? and cfvariable.idhsvariable is not null order by type, addressin");

        Object[] objects = new Object[]
            {
                new Integer(properties.getProperty(DEVICE)), "FALSE",
                properties.getProperty(LANGUAGE)
            };
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), objects);
        Record record = null;
        VarphyBean varBean = null;
        String tmp = null;
        for (int i = 0; i < recordSet.size(); i++)
        {
        	record = recordSet.get(i);
        	varBean = new VarphyBean(record);
            bodyTable.append("<tr>\n");
            bodyTable.append("<td width='10%' align='center'>");
            tmp = varBean.getShortDesc();
            bodyTable.append(((null != tmp) && (!tmp.equals("")))?tmp:"&nbsp;");
            bodyTable.append("</td>");
            bodyTable.append("<td width='10%' align='center'>");
            bodyTable.append(varBean.getType());
            bodyTable.append("</td>");
            bodyTable.append("<td width='10%' align='center'>");
            bodyTable.append(varBean.getAddressIn());
            bodyTable.append("</td>");
            bodyTable.append("<td width='50%'>");
            bodyTable.append(varBean.getShortDescription());
            bodyTable.append("</td>");
            bodyTable.append("<td width='10%' align='center'>");
            bodyTable.append(ControllerMgr.getInstance().getFromField(varBean)
                                          .getFormattedValue());
            bodyTable.append("</td>");
            bodyTable.append("<td width='10%' align='center'>");
            tmp = varBean.getMeasureUnit();
            bodyTable.append(((null != tmp) && (!tmp.equals("")))?tmp:"&nbsp;");
            bodyTable.append("</td>");
        } //for
    } //Variables

    public String getHTMLVarTable()
    {
        varTable.append("<html>");
        varTable.append("<head>");
        varTable.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" >");
        varTable.append("</head>");
        varTable.append("<body bgcolor='#eaeaea'>");
        varTable.append("<div style='height:380px;width:1003px;background-color:eaeaea'>\n");
        varTable.append("    <div>\n");
        varTable.append(
            "        <table border='1' class='table' width='100%' cellspacing='1' cellpadding='1'>\n");
        varTable.append("            <tr >\n");
        varTable.append(
            "                <td style='background-color:808080' align='center' width='10%' >Code</td>\n");
        varTable.append(
            "                <td style='background-color:808080' align='center' width='10%' >Type</td>\n");
        varTable.append(
        "                <td style='background-color:808080' align='center' width='10%' >Address In</td>\n");
        varTable.append(
            "                <td style='background-color:808080' align='center' width='50%' >Description</td>\n");
        varTable.append(
            "                <td style='background-color:808080' align='center' width='10%' >Value</td>\n");
        varTable.append(
            "                <td style='background-color:808080' align='center' width='10%' >Measure Unit</td>\n");
        varTable.append("             </tr>\n");
        varTable.append("        </table>\n");
        varTable.append("    </div>\n");
        varTable.append(
            "    <div id='variable' style='height:380px;width:1020px;overflow: auto;'>\n");
        varTable.append("        <table border='1' width='100%' cellspacing='1' cellpadding='1'>\n");
        varTable.append("            <tbody>\n");
        varTable.append(bodyTable);
        varTable.append("            </tbody>\n");
        varTable.append("        </table>\n");
        varTable.append("    </div>\n");
        varTable.append("    </div>\n");
        varTable.append("</body>");
        varTable.append("</html>");

        return varTable.toString();
    } //getHTMLVarTable
} //Class Variables
