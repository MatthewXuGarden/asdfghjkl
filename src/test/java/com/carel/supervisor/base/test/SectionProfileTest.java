package com.carel.supervisor.base.test;

import com.carel.supervisor.base.profiling.SectionProfile;
import junit.framework.TestCase;


public class SectionProfileTest extends TestCase
{
	private SectionProfile sp = null;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(SectionProfileTest.class);
    }

    public void testSectionProfile()
    {
        sp = new SectionProfile();
    }

    public void testGetValue()
    {
        sp = new SectionProfile();
        sp.getValue("sadasd");
        sp.getValue(null);
    }

    public void testSetValue()
    {
        sp = new SectionProfile();
        sp.setValue("sadasd", null);
        sp.setValue("sad", "");
    }

    public void testValues()
    {
        sp = new SectionProfile();
        sp.values();
    }
}
