package com.carel.supervisor.base.test;

import com.carel.supervisor.base.profiling.SectionProfile;
import com.carel.supervisor.base.profiling.UserProfile;
import junit.framework.TestCase;


public class UserProfileTest extends TestCase
{
	private UserProfile up = null;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(UserProfileTest.class);
    }

    public void testUserProfile()
    {
    } //UserProfile

    public void testGetValue()
    {
        up = new UserProfile();
        up.getSection("");
        up.getSection(null);
        up.getSection("Led");
    }

    public void testSetValue()
    {
        SectionProfile sp = new SectionProfile();
        up = new UserProfile();
        up.addSection("prova", sp);
        up.addSection("prova", null);
    }

    public void testValues()
    {
        up = new UserProfile();
        up.sections();
    }

    public void testToString()
    {
        SectionProfile sp = new SectionProfile();
        up = new UserProfile();
        up.toString();
        up.addSection("prova", sp);
        up.toString();
    }
}
