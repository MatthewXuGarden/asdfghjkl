package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.profiling.UserCredential;
import junit.framework.TestCase;


public class UserCredentialTest extends TestCase
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(UserCredentialTest.class);
    }

    public void testUserCredentialStringStringString()
        throws Throwable
    {
        BaseConfig.init();

        UserCredential uc = new UserCredential("a", "b", "c");
        uc = new UserCredential(null, "b", "c");
        uc.toString();
        uc = new UserCredential("a", null, "c");
        uc.toString();
        uc = new UserCredential("a", "b", null);
        uc.toString();
    }

    public void testGetUserName() throws InvalidConfigurationException
    {
        UserCredential uc = new UserCredential("a", "b", "c");
        uc.getUserName();
    }

    public void testGetUserPassword() throws InvalidConfigurationException
    {
        UserCredential uc = new UserCredential("a", "b", "c");
        uc.getUserPassword();
    }

    public void testGetUserChannel() throws InvalidConfigurationException
    {
        UserCredential uc = new UserCredential("a", "b", "c");
        uc.getUserChannel();
    }
}
