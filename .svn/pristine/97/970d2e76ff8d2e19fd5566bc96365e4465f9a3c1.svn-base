package com.carel.supervisor.presentation.dbllistbox;

public class ListBoxElement implements Comparable
{
    private String description = null;
    private String value = null;

    public ListBoxElement(String desc, String val)
    {
        description = desc;
        value = val;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
	public int compareTo(Object value) 
	{
		ListBoxElement list = (ListBoxElement)value;
		return this.description.compareTo(list.description);
	}
}
