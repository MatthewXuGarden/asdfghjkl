package com.carel.supervisor.presentation.menu;

import java.util.*;


public class Group
{
    private List sections = new ArrayList();

    public Group()
    {
    }
    
    public void addSection(Section section)
    {
        sections.add(section);
    }

    public Section getSection(int pos)
    {
        return (Section) sections.get(pos);
    }

    public int size()
    {
        return sections.size();
    }
}
