package com.carel.supervisor.presentation.dateselect;

public class DateSelect
{
    public DateSelect()
    {
    }

    public static String getDataSelectObject(int id_object,boolean enab,boolean doubledate, int day, int month, int year)
    {
        StringBuffer html = new StringBuffer("");
        String enabled = ((enab)?"":"disabled");
        String s_doubledate = ((doubledate)?"_b":"");
        
        html.append("<TABLE><TR><TD>");
        html.append("<SELECT "+enabled+" style='width:65;' onchange='save_year"+s_doubledate+"("+id_object+");changeMonthYear"+s_doubledate+"(" +
                id_object + ");' class='standardTxt' id='aaaa" + id_object +
                "' name='aaaa" + id_object + "'>");
            html.append(getYearEntry(year,enab));
        
        html.append("</SELECT></TD>\n");
        html.append("<TD>/</TD>");
        html.append("<TD><SELECT "+enabled+" style='width:45;' onchange='save_month"+s_doubledate+"("+id_object+");changeMonthYear"+s_doubledate+"(" +
            id_object + ")' class='standardTxt' id='mm" + id_object +
            "' name='mm" + id_object + "'>");
        html.append(getMonthEntry(month,enab));
        html.append("</SELECT></TD>\n");
        html.append("<TD>/</TD><TD>");
        html.append("<SELECT "+enabled+" onchange='save_day"+s_doubledate+"("+id_object+")' style='width:45;' class='standardTxt' id='gg" +
                id_object + "' name='gg" + id_object + "'>");
            html.append(getDayEntry(day,enab));
        
        html.append("</SELECT></TD>\n");
        html.append("</TR></TABLE>");

        return html.toString();
    }

    private static String getMonthEntry(int month,boolean enab)
    {
        StringBuffer html = new StringBuffer("");
        String sel = "";
        html.append("<OPTION value='0'></OPTION>\n");
        for (int i = 1; i < 13; i++)
        {
            if ((i == month)&&enab)
            {
                sel = "selected";
            }
            else
            {
                sel = "";
            }

            html.append("<OPTION " + sel + " value='" + i + "'>" + i +
                "</OPTION>\n");
        }

        return html.toString();
    }

    private static String getYearEntry(int year,boolean enab)
    {
        StringBuffer html = new StringBuffer("");
        String sel = "";
        html.append("<OPTION value='0'></OPTION>\n");
        for (int i = 2006; i < 2021; i++)
        {
            if ((i==year)&&enab)
            {
                sel = "selected";
            }
            else
            {
                sel = "";
            }

            html.append("<OPTION " + sel + " value='" + i + "'>" + i + "</OPTION>\n");
        }

        return html.toString();
    }
    
    private static String getDayEntry(int day,boolean enab)
    {
        StringBuffer html = new StringBuffer("");
        String sel = "";
        html.append("<OPTION value='0'></OPTION>\n");
        for (int i = 1; i < 32; i++)
        {
            if ((i == day)&&enab)
            {
                sel = "selected";
            }
            else
            {
                sel = "";
            }

            html.append("<OPTION " + sel + " value='" + i + "'>" + i +
                "</OPTION>\n");
        }

        return html.toString();
    }
}
