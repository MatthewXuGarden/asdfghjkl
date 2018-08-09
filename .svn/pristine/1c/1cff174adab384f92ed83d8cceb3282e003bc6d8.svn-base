package com.carel.supervisor.presentation.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionalityHelper {
	
	private static List<String> voiceToHide = new ArrayList<String>();
	private static Map<String,String> childMap = new HashMap<String,String>();
	private static Map<String,String> readOnlyPreset = new HashMap<String,String>();
	
	public FunctionalityHelper()
	{
		init();
	}
	
	public static void init()
	{
		// Menu rows not visible in ACL functionality list
		voiceToHide.add("r_siteaccess");
		voiceToHide.add("r_alrevnsearch");
		voiceToHide.add("r_sitelist");
		voiceToHide.add("r_datatransfer");
		//voiceToHide.add("wizard");
		voiceToHide.add("heartbeat");
		
		
		// Dependencies (childMap: key=menu , value=submenu; code for cftext;subcode for cftext)
		childMap.put("siteview", "devdetail;navbar;ncode04");
		childMap.put("deviceview", "dtlview;navbar;ncode01");
		childMap.put("alrglb", "alrview;navbar;ncode02");
		childMap.put("groupview", "setgroup;navbar;ncode05");
		childMap.put("areaview", "setarea;navbar;ncode06");
		childMap.put("evnview", "evndtl;navbar;ncode09");
		childMap.put("alrsched", "setaction;navbar;ncode08");
		childMap.put("actsched", "setaction2;navbar;ncode08");
		childMap.put("sitebooklet_cat", "sitebooklet;navbar;sitebooklet");
		
		// Button preset for "read-only" functionality
		readOnlyPreset.put("dtlview_0", "on;off;off");
		readOnlyPreset.put("dtlview_1", "on;off;off;off;off;off");
		
		
	}
	
	public static boolean isVisible(String menu)
	{
		if (voiceToHide.contains(menu))
			return false;
		else
			return true;
	}
	
	public static String hasChild(String menu)
	{
		if (childMap.get(menu)!=null)
			return childMap.get(menu);
		else
			return null;
	}
	
	public static String hasButtonsPreset(String submenu)
	{
		if (readOnlyPreset.get(submenu)!=null)
			return readOnlyPreset.get(submenu);
		else
			return null;
	}

}
