package com.carel.supervisor.presentation.bo.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class VarDependencyState{

	private ArrayList<String> messages = null;
    private boolean dependence = false;
    private HashMap<Integer, ArrayList<String>> sectionTable = null;
    private HashMap<Integer, String> sectionTitles = null;

    public VarDependencyState()
    {
    	dependence = false;
    	messages = new ArrayList<String>();
        sectionTable = new HashMap<Integer, ArrayList<String>>();
        sectionTitles = new HashMap<Integer, String>();
    }
    
    /*public DependenceState(String message, boolean dependence)
    {
        this.dependence = dependence;
        messages = new ArrayList<String>();

        messages.add(message);
    }*/

    public boolean dependsOn()
    {
        return dependence;
    }

    public void setDependence(boolean dependence)
    {
        this.dependence = dependence;
    }

    public ArrayList<String> getMessages()
    {
        return messages;
    }

    public String getMessagesAsString()
    {
        String msgstr = "";
    	
    	Set<Integer> sectIds = sectionTable.keySet();
        ArrayList<Integer> keyList = new ArrayList<Integer>();
        Iterator it = sectIds.iterator();
        while(it.hasNext())
        {
        	int id = (Integer)it.next();
        	keyList.add(id);
        }
        Collections.sort(keyList);
    	
    	for(int i = 0 ; i < keyList.size(); i++)
    	{
    		String title = sectionTitles.get(keyList.get(i));
    		ArrayList<String> msgList = sectionTable.get(keyList.get(i));
    		if(msgList != null)
    		{
    			msgstr += title+"\n";
    		
    			for (int j = 0; j < msgList.size(); j ++)
    			{
    				msgstr += "- "+msgList.get(j)+"\n";
    			}
    		}
    	}
    	
    	return msgstr;
    }
    
    public String getMessagesAsHTMLText()
    {
        String msgstr = "";
    	
    	Set<Integer> sectIds = sectionTable.keySet();
        ArrayList<Integer> keyList = new ArrayList<Integer>();
        Iterator it = sectIds.iterator();
        while(it.hasNext())
        {
        	int id = (Integer)it.next();
        	keyList.add(id);
        }
        Collections.sort(keyList);
    	
    	for(int i = 0 ; i < keyList.size(); i++)
    	{
    		String title = sectionTitles.get(keyList.get(i));
    		ArrayList<String> msgList = sectionTable.get(keyList.get(i));
    		if(msgList != null)
    		{
    			msgstr +="<br><i>"+ title+ "</i><br>";
    		
    			for (int j = 0; j < msgList.size(); j ++)
    			{
    				msgstr += "- "+msgList.get(j)+"<br>";
    			}
    		}
    	}
    	
    	return msgstr;
    }

    
    public void addMessage(int sectionOrder, String message)
    {
        ArrayList<String> msgList = sectionTable.get(sectionOrder);
        if(msgList == null)
        {	
        	//sectionTitles.put(sectionOrder, ""); 
        	ArrayList<String> tmpList = new ArrayList<String>();
        	tmpList.add(message);
        	sectionTable.put(sectionOrder, tmpList);
        }
        else
        {	
        	msgList.add(message);
        	sectionTable.put(sectionOrder, msgList);
        }
    }

    public void addMsgSection(int sectionOrder, String sectionTitle)
    {
    	sectionTitles.put(sectionOrder, sectionTitle);
    }
}
