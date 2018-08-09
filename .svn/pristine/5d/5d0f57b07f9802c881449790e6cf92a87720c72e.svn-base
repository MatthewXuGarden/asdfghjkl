package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.xml.XMLNode;

public class CheckUser {
	private final static String USER="userName";
	private final static String PASSWORD="password";
	private UserCredential userCredential=null;
	
	public CheckUser(){
		userCredential = new UserCredential("","","");
	}
	
	public  boolean verifyUserCredential(XMLNode loginNode){
		try{
			userCredential = new UserCredential(loginNode.getAttribute(USER), loginNode.getAttribute(PASSWORD), "");
	        ProfilingMgr.getInstance().getUserProfile(userCredential);
	        return true;
		}//try
		catch(Exception e){
			return false;	
		}//catch
	}//CheckUser

	public String getUserName(){
		return userCredential.getUserName();
	}//getUserName
	
	
}//CheckUser
