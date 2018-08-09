package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.profiling.UserProfile;
import junit.framework.TestCase;


public class LDAPProfilerTest extends TestCase
{
    private static final Logger logger = LoggerMgr.getLogger(LDAPProfilerTest.class);

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(LDAPProfilerTest.class);
    }

    public void testGetUserProfile() throws Throwable
    {
        BaseConfig.init();

        UserCredential uc = new UserCredential("user0", "password", "web");
        ProfilingMgr pm = ProfilingMgr.getInstance();
        UserProfile userProfile = pm.getUserProfile(uc);
        logger.info(userProfile.toString());
    }
}
