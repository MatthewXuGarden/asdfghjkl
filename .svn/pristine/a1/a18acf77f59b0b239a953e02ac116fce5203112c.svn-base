package com.carel.supervisor.presentation.graph;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.director.graph.GraphConstant;

public class SelectColor
{
    private StringBuffer color = new StringBuffer();


    public SelectColor(String selectId,String onChangeJSS)
    {
        color.append("<select class='standardTxt' id=\"");
        color.append(selectId);
        color.append("\" name=\"\" onchange=\"document.body.focus();");
        color.append(onChangeJSS);
        color.append("\">");

        for (int i = 0; i < GraphConstant.colors.length; i++)
        {
            color.append("\n<option id=\"");
            color.append(selectId);
            color.append(GraphConstant.colors[i]);
            color.append("\" value=\"");
            color.append(i);
            color.append("\" ");
            color.append(" style=\"background-color:");
            color.append(GraphConstant.colors[i]);
            color.append(";\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>");
        } //for

        color.append("</select>");
    } //SelectColor

    public SelectColor(String selectId, String initColor,String onChangeJSS)
    {
        if (initColor==null) 
        	initColor = "";
        if(initColor.equals("")){
        	initColor=GraphConstant.createCiclicColor();
        }//if
        
        initColor = "#" + initColor;
    	String sel = "";
    	color.append("<select onchange='document.body.focus();");
    	color.append(onChangeJSS);
    	color.append("' class='standardTxt' id=\"");
        color.append(selectId);
        color.append("\" name=\"\">");

        for (int i = 0; i < GraphConstant.colors.length; i++)
        {
            if (initColor.equalsIgnoreCase(GraphConstant.colors[i]))
            {
            	sel = "selected";
            }
            else
            {
            	sel = "";
            }
        	color.append("\n<option "+sel+" id=\"");
            color.append(selectId);
            color.append(GraphConstant.colors[i]);
            color.append("\" value=\"");
            color.append(i);
            color.append("\" ");
            color.append(" style=\"background-color:");
            color.append(GraphConstant.colors[i]);
            color.append(";\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>");
        } //for
        
        try
        {
            if(sel != null && sel.equalsIgnoreCase(""))
            {
                // Non ho trovato il mio colore
                // Lo aggiungo
                color.append("\n<option selected id=\"");
                color.append(selectId);
                color.append(initColor);
                color.append("\" value=\"");
                color.append(GraphConstant.colors.length);
                color.append("\" ");
                color.append(" style=\"background-color:");
                color.append(initColor);
                color.append(";\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>");
            }
        }
        catch(Exception e) {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        color.append("</select>");
    }

    public String getHTMLSelectColor()
    {
        return color.toString();
    }
} //Class SelectColor
