package com.carel.supervisor.base.profiling;

import java.util.ArrayList;
import java.util.Properties;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;


public interface IProfiler
{
    
	public static final String USER_SECTION="User";
	public static final int INDEX_USER_INFORMATION=0;
	public static final int INDEX_PROFILE_INFORMATION=1;
	public static final int INDEX_USER_BLOCKED=2;
	
	 public static final int FUNCTION_NUMBER = 19;
	 public  final String INPUT_USER = "user";
	 public static final String INPUT_USER_TO_MODIFY = "user_to_modify";
	 public static final String INPUT_PROFILE = "profile";
	 public static final String INPUT_PASSWORD = "password";
	 public static final String PROFILE_DESCRIPTION = "desc";
	 public static final String INPUT_ROW_SELECTED = "rowselected";
	
	public abstract UserProfile getUserProfile(UserCredential userCredential)
        throws ProfileException, Exception;

    public abstract void init(XMLNode xmlStatic)
        throws InvalidConfigurationException;

    //label del campo che contiene il nome dell'utente e del profilo
    //es in ldap username � etichettato un
    public abstract String getUserNameLabelField();
    public abstract String getProfileNameLabelField();
    
    //i due array list devo essere logicamente correlati all'utente di indice 0 corrisponde il profilo di indice 0
    public abstract ArrayList[] getUserInformation() throws Exception;

    public abstract void removeUser(Properties properties) throws Exception;
    public abstract boolean addUser(Properties properties) throws Exception;
    public abstract boolean modifyUser(Properties properties) throws Exception;
    public abstract boolean removeProfile(Properties properties) throws Exception;
    public abstract void addProfile(Properties properties) throws Exception;
    public abstract void modifyProfile(Properties properties) throws Exception;
    public abstract String encryptPassword(String password) throws Exception;
    
    // Method used for users management from Remote
    public String manageUserXmlData(int idsite, String xdata);
    public String checkRemoteCredential(String sIdent, String sPassw);
    public boolean isRemoteManagementActive();
}
