package com.carel.supervisor.base.test;

import com.carel.supervisor.base.conversion.Replacer;
import junit.framework.TestCase;


public class ReplacerTest extends TestCase
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(ReplacerTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.conversion.Replacer.replace(String, String, String)'
     */
    public void testReplaceStringStringString()
    {
        String s = Replacer.replace("SaaaS", "aaa", "bbb");

        if (!"SbbbS".equals(s))
        {
            throw new TestRunException("Comportamento errato");
        }

        s = Replacer.replace("SaaaS", "S", "X");

        if (!"XaaaX".equals(s))
        {
            throw new TestRunException("Comportamento errato");
        }

        s = Replacer.replace("Saaaa", "S", "X");

        if (!"Xaaaa".equals(s))
        {
            throw new TestRunException("Comportamento errato");
        }

        s = Replacer.replace("\"\"\"\"\"", "\"", "1");

        if (!"11111".equals(s))
        {
            throw new TestRunException("Comportamento errato");
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.conversion.Replacer.createStringBufferOfChar(String, int)'
     */
    public void testCreateStringBufferOfChar()
    {
        String s = Replacer.createStringOfChar("1", 9);

        if (!"111111111".equals(s))
        {
            throw new TestRunException("Comportamento errato");
        }
    }
}
