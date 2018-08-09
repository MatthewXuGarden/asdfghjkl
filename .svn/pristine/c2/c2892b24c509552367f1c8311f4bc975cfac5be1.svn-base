package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

public class HTMLSimpleElement extends HTMLElement
{
    private String text = null;

    public HTMLSimpleElement(String text)
    {
        this.text = text;
    }

    public String getHTMLText()
    {
        return text;
    }

    public StringBuffer getHTMLTextBuffer()
    {
        return new StringBuffer(text);
    }

    public int getLength()
    {
        int n = 0;

        if (text != null)
        {
            n = text.length();
        }

        int lenght = 0;
        
        if(text.indexOf("</tr>") != -1)
        {
        	String[] temps = text.split("</tr>");
        	for(int j=0;j<temps.length;j++)
        	{
        		String temp = temps[j];
        		n = temp.length();
        		int lenghtTemp = 2;
		        for (int i = 0; i < n; i++)
		        {
		            if ((temp.charAt(i) == '<') && (temp.contains(">")))
		            {
		                while ((temp.charAt(i) != '>') && (i < n))
		                {
		                    i++;
		                }
		            } //if
		            else
		            {
		            	lenghtTemp++;
		            }
		        } //for	
		        if(lenghtTemp>lenght)
		        {
		        	lenght = lenghtTemp;
		        }
        	}
        }
        else
        {
        	for (int i = 0; i < n; i++)
	        {
	            if ((text.charAt(i) == '<') && (text.contains(">")))
	            {
	                while ((text.charAt(i) != '>') && (i < n))
	                {
	                    i++;
	                }
	            } //if
	            else
	            {
	                lenght++;
	            }
	        } //for	
        }
        return lenght;
    }
}
