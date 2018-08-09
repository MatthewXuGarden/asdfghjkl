package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public abstract class HTMLElement implements ITagElement
{
    private HashMap attributes = new HashMap();
    private StringBuffer attributesHtml = null;

    public void addAttribute(String key, String value)
    {
        attributes.put(key, value);
    }

    public void setAttribute(String key, String value)
    {
    }

    public String getAttribute(String key)
    {
        return null;
    }

    protected String getAttributeHtml()
    {
        Set set = attributes.keySet();
        Iterator iterator = set.iterator();
        attributesHtml = new StringBuffer();

        while (iterator.hasNext())
        {
            String attribute = (String) iterator.next();
            attributesHtml.append(attribute + "='" + attributes.get(attribute) +
                "' ");
        } //while

        return attributesHtml.toString();
    } //createAttribute

    public String getHTMLText()
    {
        return getHTMLTextBuffer().toString();
    }
}
