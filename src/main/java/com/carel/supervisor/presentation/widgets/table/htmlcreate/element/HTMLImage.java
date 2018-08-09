package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

public class HTMLImage extends HTMLElement
{
    private String pathImage = null;
    private int height = 0;

    public HTMLImage(String pathImage, int height)
    {
        this.pathImage = pathImage;
        this.height = height;
    }

    public StringBuffer getHTMLTextBuffer()
    {
        StringBuffer html = new StringBuffer();
        html.append("<img " + this.getAttributeHtml() + "height=\" ");
        html.append(String.valueOf(height));
        html.append("\" src=\"");
        html.append(pathImage);
        html.append("\"/>");

        return html;
    }

    //???
    public int getLength()
    {
        return 0;
    }
} //Class HTMLImage
