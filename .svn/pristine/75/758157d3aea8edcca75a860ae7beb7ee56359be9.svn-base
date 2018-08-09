package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

public class HTMLButton extends HTMLElement
{
    private String titleButton = null;
    private String onClickEvent = null;

    public HTMLButton(String titleButton, String onClickEvent)
    {
        this.titleButton = titleButton;
        this.onClickEvent = onClickEvent;
    } //HTMLButton

    public StringBuffer getHTMLTextBuffer()
    {
        StringBuffer html = new StringBuffer();
        html.append("<button " + this.getAttributeHtml() + " type=\"button\" ");
        html.append("onclick=\"");
        html.append(onClickEvent);
        html.append("\">");
        html.append(titleButton);
        html.append("</button>");

        return html;
    }

    public int getLength()
    {
        return titleButton.length();
    }
} //Class HTMLButton
