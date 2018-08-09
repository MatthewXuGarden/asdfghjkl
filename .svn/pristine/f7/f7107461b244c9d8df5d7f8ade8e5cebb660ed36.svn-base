package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

public class HTMLDiv extends HTMLElement
{
    private String divText = null;

    public HTMLDiv(String divText)
    {
        this.divText = divText;
    }

    public StringBuffer getHTMLTextBuffer()
    {
        StringBuffer html = new StringBuffer();
        html.append("<div " + this.getAttributeHtml() + ">");
        html.append(divText);
        html.append("</div>");

        return html;
    }

    public int getLength()
    {
        return divText.length();
    }
}
