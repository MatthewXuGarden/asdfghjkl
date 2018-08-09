package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.crypter.Crypter;
import junit.framework.TestCase;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;


public class CrypterTest extends TestCase
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(CrypterTest.class);
    }

    public void testEncryptMD()
        throws InvalidConfigurationException, TestException
    {
        try
        {
            Crypter.encryptMD("sd", "SHA-1");
        }
        catch (NoSuchAlgorithmException e)
        {
            FatalHandler.manage(this, "", e);
        }

        try
        {
            Crypter.encryptMD("sd", "SHA1221213213-1");
            throw new TestException("Comportqamento non corretto");
        }
        catch (TestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            assertNotNull(e);
        }
    }

    public void testDecryptRSA() throws Exception
    {
        BigInteger biClear = null;
        BigInteger biCrypted = null;

        for (int i = 0; i < 100; i++)
        {
            String s = "sadasdasdasdasdasdsad" + i;
            byte[] b = s.getBytes();
            biClear = Crypter.encryptRSA(new BigInteger(b));
            biCrypted = Crypter.decryptRSA(biClear);

            if (b.equals(biCrypted.toByteArray()))
            {
                throw new TestException("Comportamento non corretto");
            }
        } //for
    }
}
