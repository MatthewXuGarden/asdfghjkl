package com.carel.supervisor.base.profiling.impl;

import java.util.ArrayList;
import java.util.Properties;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.profiling.*;
import com.carel.supervisor.base.xml.XMLNode;


public class DummyProfiler implements IProfiler
{

	public UserProfile getUserProfile(UserCredential userCredential) throws ProfileException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(XMLNode xmlStatic) throws InvalidConfigurationException {
		// TODO Auto-generated method stub
		
	}

	public String getUserNameLabelField() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProfileNameLabelField() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList[] getUserInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeUser(Properties properties) {
		// TODO Auto-generated method stub
		
	}

	public boolean addUser(Properties properties) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean modifyUser(Properties properties) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeProfile(Properties properties) {
		// TODO Auto-generated method stub
		return false;
	}

	public void addProfile(Properties properties) {
		// TODO Auto-generated method stub
		
	}

	public void modifyProfile(Properties properties) {
		// TODO Auto-generated method stub
		
	}
	public String encryptPassword(String password)
	{
		return null;
	}
	public String manageUserXmlData(int idsite, String xdata)
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String checkRemoteCredential(String sIdent, String sPassw) {
		return null;
	}

	public boolean isRemoteManagementActive() {
		// TODO Auto-generated method stub
		return false;
	}
}
