package com.carel.supervisor.presentation.ldap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.crypter.Crypter;
import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfileException;
import com.carel.supervisor.base.profiling.SectionProfile;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.presentation.bean.ProfileBeanList;


public class LDAPProfiler extends InitializableBase implements IProfiler
{
    //Xml parameter e costant
    private static final String ADMIN_NAME = "adminName";
    private static final String ADMIN_PASSWORD = "adminPassword";
    private static final String LDAP_URL = "ldapURL";
    private static final String SECURTY_AUTENTICATION = "securtyAuthentication";
    private static final String CONTEXT_FACTORY = "contextFactory";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String CRYPTING_METHOD = "cryptingMethod";
    private static final String LDAP_NODE = "ldapNode";
    private static final int HEXADECIMAL = 16;
   
    
  

    //private static final String INPUT_CONFIRM_PASSWORD="cpassword";
    //private static final String INPUT_USER_CAREL="user_carel";
    //private static final String INPUT_PASSWORD_CAREL="password_carel";
    
    private boolean initialized = false;
    private String adminName;
    private String adminPassword;
    private String ldapURL;
    private String ldapNode;
    private String securtyAuthentication;
    private String contextFactory;
    private String cryptingMethod;
    private DirContext ctx = null;

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            Properties properties = retrieveProperties(xmlStatic, NAME, VALUE,
                    "BSSE0002");
            XMLNode xmlTmp = null;
            String name = null;
            adminName = retrieveAttribute(properties, ADMIN_NAME, "BSSE0002");
            adminPassword = retrieveAttribute(properties, ADMIN_PASSWORD,
                    "BSSE0002");

            if (adminPassword.subSequence(0, 6).equals("{CRYP}"))
            {
                adminPassword = new String(Crypter.decryptRSA(
                            new BigInteger(adminPassword.substring(6),
                                HEXADECIMAL)).toByteArray());
            } //if
            else
            {
                for (int i = 0; i < xmlStatic.size(); i++)
                {
                    xmlTmp = xmlStatic.getNode(i);
                    name = xmlTmp.getAttribute(NAME);

                    if (name.equals(ADMIN_PASSWORD))
                    {
                        xmlTmp.setAttribute(VALUE,
                            "{CRYP}" +
                            Crypter.encryptRSA(
                                new BigInteger(adminPassword.getBytes()))
                                   .toString(HEXADECIMAL));

                        break;
                    } //if
                } //for
            } //else

            ldapURL = retrieveAttribute(properties, LDAP_URL, "BSSE0002");
            ldapNode = retrieveAttribute(properties, LDAP_NODE, "BSSE0002");

            securtyAuthentication = retrieveAttribute(properties,
                    SECURTY_AUTENTICATION, "BSSE0002");
            contextFactory = retrieveAttribute(properties, CONTEXT_FACTORY,
                    "BSSE0002");
            cryptingMethod = retrieveAttribute(properties, CRYPTING_METHOD,
                    "BSSE0002");

            Map env = new Hashtable();

            env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);

            //set security credentials
            env.put(Context.SECURITY_AUTHENTICATION, securtyAuthentication);
            env.put(Context.SECURITY_PRINCIPAL, adminName);
            env.put(Context.SECURITY_CREDENTIALS, adminPassword);

            //connect to my domain controller
            env.put(Context.PROVIDER_URL, ldapURL);

            // Create the initial directory context
            try
            {
                ctx = new InitialLdapContext((Hashtable) env, null);
            }
            catch (NamingException e)
            {
                e.printStackTrace();
            }

            initialized = true;
        } //if
    } //init

    public UserProfile getUserProfile(UserCredential userCredential)
        throws Exception
    {
        //TO DO: la connessione potrebbe essere messa in cache
        UserProfile up = new UserProfile();

        //Create the search controls 		
        SearchControls searchCtls = new SearchControls();

        //Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //Autenticazione
        //Specify the attributes to return
        String[] returnedAtts = { "password" };
        searchCtls.setReturningAttributes(returnedAtts);

        //specify the LDAP search filter
        String searchFilter = "(objectclass=*)";

        //Specify the Base for the search
        String searchBase = "un=" + userCredential.getUserName() +
            ",usn=users,lnn=" + ldapNode + ",dc=carel,dc=com";
        NamingEnumeration answer = null;

        try
        {
            answer = ctx.search(searchBase, searchFilter, searchCtls);
        } //try
        catch (Exception e)
        {
            throw new ProfileException("UserName or Password Incorrect");
        } //catch

        if (answer.hasMoreElements())
        {
            SearchResult sr = (SearchResult) answer.next();
            Attributes attrs = sr.getAttributes();

            //            
            if (!Crypter.encryptMD(userCredential.getUserPassword(),
                        cryptingMethod).equals(attrs.get("password").get()
                                                        .toString()))
                throw new ProfileException("UserName or Password Incorrect");

            //Loggato OK
        } //if
        else
        {
            throw new ProfileException("UserName or Password Incorrect");
        } //else

        //Inserisco i dati personali
        searchCtls.setReturningAttributes(null);
        searchFilter = "(&(objectclass=user))";
        answer = ctx.search(searchBase, searchFilter, searchCtls);

        SectionProfile tmp = null;

        while (answer.hasMoreElements())
        {
            SearchResult sr = (SearchResult) answer.next();

            tmp = new SectionProfile();
            up.addSection(IProfiler.USER_SECTION, tmp);

            // Print out some of the attributes, catch the exception if the attributes have no values
            Attributes attrs = sr.getAttributes();

            if (attrs != null)
            {
                NamingEnumeration n = attrs.getAll();
                String temp;
                String[] temp1;

                while (n.hasMore())
                {
                    temp = (String) n.next().toString();
                    temp1 = temp.split(":");

                    /*if (temp1[0].equals("gra"))
                    {
                        temp1[1] = searchGroup(temp1[1],
                                userCredential.getUserChannel());
                    } //if*/
                    tmp.setValue(temp1[0], temp1[1]);
                } //while
            } //if
        } //while

        return up;
    } //getUserProfile

    public String getUserNameLabelField()
    {
        return "un";
    } //getUserNameLabelField

    public String getProfileNameLabelField()
    {
        return "gra";
    } //getProfileNameLabelField

    public void removeUser(Properties properties) throws Exception
    {
        String userName = ((String) properties.getProperty(INPUT_ROW_SELECTED)).split(
                ";")[0];

        if ((userName != null) && (!userName.equals("")))
        {
            String dUserName = "un=" + userName + ",usn=users,lnn=" + ldapNode +
                ",dc=carel,dc=com";
            ctx.destroySubcontext(dUserName);
        }
    } //removeUser

    public boolean addUser(Properties properties) throws Exception
    {
        String un = properties.getProperty(INPUT_USER);

        //verifico che il nome utente non sia gia presente su ldap
        String dUserName = "un=" + un + ",usn=users,lnn=" + ldapNode +
            ",dc=carel,dc=com";

        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String[] returnedAtts = { "un" };
        searchCtls.setReturningAttributes(returnedAtts);

        NamingEnumeration answer = null;

        try
        {
            answer = ctx.search(dUserName, "(objectclass=*)", searchCtls);
        }
        catch (Exception e)
        {
            answer = null;
        }

        if (answer == null)
        {
            String clearPassword = properties.getProperty(INPUT_PASSWORD);

            String password = Crypter.encryptMD(clearPassword, cryptingMethod);

            String profile = properties.getProperty(INPUT_PROFILE);

            Attributes attrs = new BasicAttributes(true);
            attrs.put("un", un);
            attrs.put("objectClass", "user");
            attrs.put("telephoneNumber", "666");
            attrs.put("un", un);
            attrs.put("mail", "satan@666.gl");
            attrs.put("password", password);
            attrs.put("gra", profile);
            attrs.put("note", "asd");
            ctx.createSubcontext(dUserName, attrs);

            return true;
        }

        return false;
    } //addUser

    public boolean modifyUser(Properties properties) throws Exception
    {
        String un = properties.getProperty(INPUT_USER_TO_MODIFY);
        String dUserName = "un=" + un + ",usn=users,lnn=" + ldapNode +
            ",dc=carel,dc=com";

        String clear_password = properties.getProperty(INPUT_PASSWORD);
        String password = Crypter.encryptMD(clear_password, //passw nuova criptata
                cryptingMethod);
        String gra = properties.getProperty(INPUT_PROFILE); //nuovo profilo associato all'utente "un"

        if (gra == null)
            gra = "0"; //se non vien postato � perch� � admin e la combo � disabilitata

        Attributes attr = new BasicAttributes(true);
        attr.put("password", password);
        attr.put("gra", gra);
        ctx.modifyAttributes(dUserName, DirContext.REPLACE_ATTRIBUTE, attr);
        return true;
    } //modifyUser

    public boolean removeProfile(Properties properties)
        throws Exception
    {
        String s_idprofile = properties.getProperty("rowselected");
        int idprofile = Integer.parseInt(s_idprofile);
        int idsite = ((Integer) properties.get("idSite")).intValue();

        String dUserName = "usn=users,lnn=" + ldapNode + ",dc=carel,dc=com";

        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String[] returnedAtts = { "gra" };
        searchCtls.setReturningAttributes(returnedAtts);

        NamingEnumeration answer = null;

        try
        {
            answer = ctx.search(dUserName, "(&(objectclass=user))", searchCtls);
        }
        catch (Exception e)
        {
            answer = null;
        }

        String gra = null;

        while (answer.hasMoreElements())
        {
            Attributes tmp = null;
            SearchResult sr = (SearchResult) answer.next();
            tmp = sr.getAttributes();
            gra = tmp.get("gra").toString();
            gra = UtilBean.trim(gra.split(":")[1]);

            if (gra.equals(s_idprofile))
                return false;
        }

        FunctionsDB.removeLocalProfile(new Integer(idprofile));
        ProfileBeanList.deleteProfile(idprofile, idsite);

        return true;
    } //removeProfile

    
    
    public void addProfile(Properties properties) throws Exception
    {
    	/*
        int idsite = ((Integer) properties.get("idSite")).intValue();
        String desc = properties.getProperty(PROFILE_DESCRIPTION);
        String lang = properties.getProperty("language");
        AreaDbBeanList areas = new AreaDbBeanList();
        GroupListBean groups = new GroupListBean();
        areas.retrieveAreas(null, idsite, lang); //retrieve aree

        StringBuffer param = new StringBuffer();
        String tmp = "";

        for (int i = 0; i < FUNCTION_NUMBER; i++)
        {
            tmp = properties.getProperty("funct_" + i);
            param.append(i + "=" + tmp + ";");
        }

        String s_param = param.toString();
        s_param = s_param.substring(0, s_param.length() - 1);

        String s_area = "";
        String s_group = "";
        int idarea = -1;

        for (int i = 0; i < areas.size(); i++)
        {
            idarea = areas.getArea(i).getIdArea();

            if ((idarea != 1) &&
                    (properties.getProperty("a" + idarea) == null))
                //profilegroups: insert idprofile,site,idarea, null   
                //profilegroups: non faccio nulla sui gruppi
                s_area = s_area + idarea + ";";
            else
            {
                GroupBean[] group = groups.retrieveGroupsNoGlobalOfArea(idsite,
                        lang, idarea);
                int idgroup = -1;

                for (int j = 0; j < group.length; j++)
                {
                    idgroup = group[j].getGroupId();

                    if (properties.getProperty("g" + idgroup) == null)
                        //profilegroups: insert idprofile,site,idarea,idgroup
                        s_group = s_group + idgroup + ";";
                }
            }
        }

        if (!s_area.equals(""))
            s_area = s_area.substring(0, s_area.length() - 1);

        if (!s_group.equals(""))
            s_group = s_group.substring(0, s_group.length() - 1);

        s_param = s_param + "-" + s_area + "-" + s_group; //parametri profilo  funct+aree disab+gruppi disab

        //      aggiunta profilo
        int idprofile = ProfileBeanList.addProfile(idsite, desc, s_param);
        ProfileBean profile = ProfileBeanList.retrieveProfile(idprofile, idsite);
        int permission = 0;
        FunctionsDB.newLocalProfile(new Integer(idprofile));

        //		CARATTERISTICHE PROFILO
        //		configurazione sito
        permission = profile.getFunction(ProfileBean.FUNCT_SITE_CONF);

        if (permission == ProfileBean.PERMISSION_NONE)
            FunctionsDB.noAccessConfiguration(new Integer(idprofile));
        else if (permission == ProfileBean.PERMISSION_READ_ONLY)
            FunctionsDB.activeReadOnlyConfiguration(new Integer(idprofile));
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeConfiguration(new Integer(idprofile));

        //		 acknowledge allarmi
        permission = profile.getFunction(ProfileBean.FUNCT_ALARM_ACK);

        if (permission == ProfileBean.PERMISSION_NONE)
            FunctionsDB.noAccessAck(new Integer(idprofile));
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeAck(new Integer(idprofile));

//      cancel
        permission = profile.getFunction(ProfileBean.FUNCT_ALARM_CANC);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeCancel(new Integer(idprofile));
            FunctionsDB.noAccessCancel(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeCancel(new Integer(idprofile));
        }

        //      reset allarmi
        permission = profile.getFunction(ProfileBean.FUNCT_ALARM_RESET);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeReset(new Integer(idprofile));
            FunctionsDB.noAccessReset(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeReset(new Integer(idprofile));
        }

        // system page
        permission = profile.getFunction(ProfileBean.FUNCT_SYSTEM_PAGE);
        int reboot = profile.getFunction(ProfileBean.FUNCT_CONF_REBOOT);
        if (permission == ProfileBean.PERMISSION_NONE)
            FunctionsDB.noManualSystem(new Integer(idprofile),reboot);
        else if (permission == ProfileBean.PERMISSION_READ_ONLY)
            FunctionsDB.activeReadOnlySystem(new Integer(idprofile),reboot);
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeSystem(new Integer(idprofile),reboot);

        boolean confgraph = false;

        //    haccp
        permission = profile.getFunction(ProfileBean.FUNCT_HACCP);

        if (permission == ProfileBean.PERMISSION_NONE)
            FunctionsDB.noAccessHACCP(new Integer(idprofile));
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeHACCP(new Integer(idprofile));
            confgraph = true;
        }

        //    historical
        permission = profile.getFunction(ProfileBean.FUNCT_HISTORICAL);

        if (permission == ProfileBean.PERMISSION_NONE)
            FunctionsDB.noAccessHistorical(new Integer(idprofile));
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeHistorical(new Integer(idprofile));
            confgraph = true;
        }

        if (confgraph) //se ho almeno storico o haccp
            FunctionsDB.activeConfGraph(new Integer(idprofile));
        else
            FunctionsDB.noAccessConfGraph(new Integer(idprofile));

        //      usermng
        permission = profile.getFunction(ProfileBean.FUNCT_USER_MNG);

        if (permission == ProfileBean.PERMISSION_NONE)
            FunctionsDB.noAccessUserMng(new Integer(idprofile));
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeUserMng(new Integer(idprofile));

        //GESTIONE AREE E GRUPPI PER IL PROFILO
        //ciclo su aree e vedo se sono da disattivare
        for (int i = 0; i < areas.size(); i++)
        {
            idarea = areas.getArea(i).getIdArea();

            if ((idarea != 1) &&
                    (properties.getProperty("a" + idarea) == null))
                //profilegroups: insert idprofile,site,idarea, null   
                //profilegroups: non faccio nulla sui gruppi
                FunctionsDB.disableAreaOrGroup(new Integer(idprofile),
                    new Integer(idsite), new Integer(idarea), null);
            else
            {
                GroupBean[] group = groups.retrieveGroupsNoGlobalOfArea(idsite,
                        lang, idarea);
                int idgroup = -1;

                for (int j = 0; j < group.length; j++)
                {
                    idgroup = group[j].getGroupId();

                    if (properties.getProperty("g" + idgroup) == null)
                        //profilegroups: insert idprofile,site,idarea,idgroup
                        FunctionsDB.disableAreaOrGroup(new Integer(idprofile),
                            new Integer(idsite), new Integer(idarea),
                            new Integer(idgroup));
                }
            }
        }
        */
    } //addProfile
    
    
    
    public void modifyProfile(Properties properties) throws Exception
    {
    	/*
        String desc = properties.getProperty(PROFILE_DESCRIPTION);
        int idprofile = Integer.parseInt(properties.getProperty("rowselected"));
        String lang = properties.getProperty("language");
        StringBuffer param = new StringBuffer();
        String tmp = "";
        int idsite = ((Integer) properties.get("idSite")).intValue();

        for (int i = 0; i < FUNCTION_NUMBER; i++)
        {
            tmp = properties.getProperty("funct_" + i);
            param.append(i + "=" + tmp + ";");
        }

        String s_param = param.toString();
        s_param = s_param.substring(0, s_param.length() - 1);

        AreaDbBeanList areas = new AreaDbBeanList();
        GroupListBean groups = new GroupListBean();
        areas.retrieveAreas(null, idsite, lang); //retrieve aree

        String s_area = "";
        String s_group = "";
        int idarea = -1;

        for (int i = 0; i < areas.size(); i++)
        {
            idarea = areas.getArea(i).getIdArea();

            if ((idarea != 1) &&
                    (properties.getProperty("a" + idarea) == null))
                //profilegroups: insert idprofile,site,idarea, null   
                //profilegroups: non faccio nulla sui gruppi
                s_area = s_area + idarea + ";";
            else
            {
                GroupBean[] group = groups.retrieveGroupsNoGlobalOfArea(idsite,
                        lang, idarea);
                int idgroup = -1;

                for (int j = 0; j < group.length; j++)
                {
                    idgroup = group[j].getGroupId();

                    if (properties.getProperty("g" + idgroup) == null)
                        //profilegroups: insert idprofile,site,idarea,idgroup
                        s_group = s_group + idgroup + ";";
                }
            }
        }

        if (!s_area.equals(""))
            s_area = s_area.substring(0, s_area.length() - 1);

        if (!s_group.equals(""))
            s_group = s_group.substring(0, s_group.length() - 1);

        s_param = s_param + "-" + s_area + "-" + s_group; //parametri profilo  funct+aree disab+gruppi disab

        ProfileBeanList.updateProfile(idsite, idprofile, desc, s_param);

        //		CARATTERISTICHE PROFILO
        ProfileBean profile = ProfileBeanList.retrieveProfile(idprofile, idsite);
        int permission = 0;

        //		configurazione sito
        permission = profile.getFunction(ProfileBean.FUNCT_SITE_CONF);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeConfiguration(new Integer(idprofile));
            FunctionsDB.noAccessConfiguration(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_ONLY)
        {
            FunctionsDB.activeConfiguration(new Integer(idprofile));
            FunctionsDB.activeReadOnlyConfiguration(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeConfiguration(new Integer(idprofile));

        //		 acknowledge allarmi
        permission = profile.getFunction(ProfileBean.FUNCT_ALARM_ACK);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeAck(new Integer(idprofile));
            FunctionsDB.noAccessAck(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeAck(new Integer(idprofile));

//      cancel
        permission = profile.getFunction(ProfileBean.FUNCT_ALARM_CANC);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeCancel(new Integer(idprofile));
            FunctionsDB.noAccessCancel(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeCancel(new Integer(idprofile));
        }

        //      reset allarmi
        permission = profile.getFunction(ProfileBean.FUNCT_ALARM_RESET);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeReset(new Integer(idprofile));
            FunctionsDB.noAccessReset(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeReset(new Integer(idprofile));
        }


        // system page
        permission = profile.getFunction(ProfileBean.FUNCT_SYSTEM_PAGE);
        int reboot = profile.getFunction(ProfileBean.FUNCT_CONF_REBOOT);
        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeSystem(new Integer(idprofile),reboot);
            FunctionsDB.noManualSystem(new Integer(idprofile),reboot);
        }
        else if (permission == ProfileBean.PERMISSION_READ_ONLY)
        {
            FunctionsDB.activeSystem(new Integer(idprofile),reboot);
            FunctionsDB.activeReadOnlySystem(new Integer(idprofile),reboot);
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeSystem(new Integer(idprofile),reboot);

        boolean confgraph = false;

        //    haccp
        permission = profile.getFunction(ProfileBean.FUNCT_HACCP);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeHACCP(new Integer(idprofile));
            FunctionsDB.noAccessHACCP(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeHACCP(new Integer(idprofile));
            confgraph = true;
        }

        //    historical
        permission = profile.getFunction(ProfileBean.FUNCT_HISTORICAL);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeHistorical(new Integer(idprofile));
            FunctionsDB.noAccessHistorical(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
        {
            FunctionsDB.activeHistorical(new Integer(idprofile));
            confgraph = true;
        }

        if (confgraph) //se attivo o haccp o storico
            FunctionsDB.activeConfGraph(new Integer(idprofile));
        else //altrimenti pulisci e fa insert
        {
            FunctionsDB.activeConfGraph(new Integer(idprofile));
            FunctionsDB.noAccessConfGraph(new Integer(idprofile));
        }

        //      usermng
        permission = profile.getFunction(ProfileBean.FUNCT_USER_MNG);

        if (permission == ProfileBean.PERMISSION_NONE)
        {
            FunctionsDB.activeUserMng(new Integer(idprofile));
            FunctionsDB.noAccessUserMng(new Integer(idprofile));
        }
        else if (permission == ProfileBean.PERMISSION_READ_WRITE)
            FunctionsDB.activeUserMng(new Integer(idprofile));

        //UPDATE SEZIONE AREE E GRUPPI
        String sql = "delete from profilegroups where idsite=? and idprofile=?";
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite), new Integer(idprofile) });

        //      ciclo su aree e vedo se sono da disattivare
        for (int i = 0; i < areas.size(); i++)
        {
            idarea = areas.getArea(i).getIdArea();

            if ((idarea != 1) &&
                    (properties.getProperty("a" + idarea) == null))
                //profilegroups: insert idprofile,site,idarea, null   
                //profilegroups: non faccio nulla sui gruppi
                FunctionsDB.disableAreaOrGroup(new Integer(idprofile),
                    new Integer(idsite), new Integer(idarea), null);
            else
            {
                GroupBean[] group = groups.retrieveGroupsNoGlobalOfArea(idsite,
                        lang, idarea);
                int idgroup = -1;

                for (int j = 0; j < group.length; j++)
                {
                    idgroup = group[j].getGroupId();

                    if (properties.getProperty("g" + idgroup) == null)
                        //profilegroups: insert idprofile,site,idarea,idgroup
                        FunctionsDB.disableAreaOrGroup(new Integer(idprofile),
                            new Integer(idsite), new Integer(idarea),
                            new Integer(idgroup));
                }
            }
        }
        */
    } //modifyProfile
    

    public ArrayList[] getUserInformation() throws Exception
    {
        //		Inizia la ricerca utenti
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        //Autenticazione
        //Specify the attributes to return
        String[] returnedAtts = { "un", "gra", "password" };
        searchCtls.setReturningAttributes(returnedAtts);

        //specify the LDAP search filter
        String searchFilter = "(objectclass=*)";

        //Specify the Base for the search
        String searchBase = "usn=users,lnn=" + ldapNode + ",dc=carel,dc=com";
        NamingEnumeration answer = null;

        try
        {
            answer = ctx.search(searchBase, searchFilter, searchCtls);
        } //try
        catch (Exception e)
        {
            throw new ProfileException("Error in user search");
        } //catch

        ArrayList user = new ArrayList();
        ArrayList profile = new ArrayList();

        //password= new ArrayList();
        while (answer.hasMoreElements())
        {
            SearchResult sr = (SearchResult) answer.next();
            Attributes attrs = sr.getAttributes();
            user.add(attrs.get("un").get().toString());
            profile.add(attrs.get("gra").get().toString());

            // password.add(attrs.get("password").get().toString());  
        } //if

        return new ArrayList[] { user, profile };
    }
    public String encryptPassword(String password)
		throws Exception
	{
		return Crypter.encryptMD(password, cryptingMethod);
	}
    public String manageUserXmlData(int idsite, String xdata)
	{
		return "";
	}
    public String checkRemoteCredential(String sIdent, String sPassw) {
		return null;
	}

	public boolean isRemoteManagementActive() {
		// TODO Auto-generated method stub
		return false;
	}
} //LDAPProfiler Class
