package com.carel.supervisor.base.test;

import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.base.xml.XMLNode;
import junit.framework.TestCase;


public class ProfilingMgrTest extends TestCase
{
    private ProfilingMgrTest()
    {	
    }
    
	public static void main(String[] args) throws Exception
    {
        //junit.textui.TestRunner.run(ProfilingMgrTest.class);
        //Init Profiling Manager --> Init LDAPProfiler
        ProfilingMgr pm = ProfilingMgr.getInstance();
        XMLNode node = new XMLNode();
        UserCredential uc = new UserCredential("admin", "admin", "web");
        UserProfile up = null;
        String xml =
            "<component name=\"profilermgr\" class=\"com.carel.supervisor.base.profiling.ProfilingMgr\">" +
            "<component type=\"ldap\" class=\"com.carel.supervisor.base.profiling.impl.LDAPProfiler\">" +
            "<element value=\"cn=root,dc=carel,dc=com\" name=\"adminName\"></element>" +
            "<element value=\"{CRYP}1969aa2a51428d977725f0a0e2a779039756733718db5716c864ed33f9a6fe6ae18ad19ffc38951b8865c69204f3a9b556dc10d389efe44d08c3653a850ca69\" name=\"adminPassword\"></element>" +
            "<element value=\"ldap://localhost:389\" name=\"ldapURL\"></element>" +
            "<element value=\"simple\" name=\"securtyAuthentication\"></element>" +
            "<element value=\"com.sun.jndi.ldap.LdapCtxFactory\" name=\"contextFactory\"></element>" +
            "<element value=\"SHA-1\" name=\"cryptingMethod\"></element>" +
            "<element value=\"demo\" name=\"ldapNode\"></element>" +
            "</component>" + "</component>";

        node = XMLNode.parse(xml);
        pm.init(node);
        up = pm.getUserProfile(uc);
        up.toString();
       
    }

    /*Metodi Testati
            public void testGetInstance()
            public void testInit()
            public void testGetUserProfile()
    */
}
