package com.carel.supervisor.presentation.devices;

import java.util.List;


public class Device
{
    private Integer id = null;
    private String description = null;
    private String code = null;
    private String group = null;
    private int state = -1;
    private List variables = null;

    //Costruttori
    public Device(int id, String code, String description,
        String group, int state, List variables)
    {
        this.code = code;
        this.id = new Integer(id);
        this.description = description;
        this.group = group;
        this.state = state;
        this.variables = variables;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int extraColumns()
    {
        if (null == variables)
        {
            return 0;
        }
        return variables.size();
    }

    public String getVariable(int i)
    {
        return (String) variables.get(i);
    }

    public void setGrandezze(List variables)
    {
        this.variables = variables;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = new Integer(id);
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    //Methods
    public void addVariables(String a)
    {
        variables.add(a);
    }

    public void removeVariables(int i)
    {
        variables.remove(i);
    }

    /**
     * @return: String
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code)
    {
        this.code = code;
    }
}
