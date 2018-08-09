package com.carel.supervisor.base.io;


/**
Original pseudocode   : Thomas Weidenfeller
Implementation        : Aki Nieminen

http://www.unicode.org/unicode/faq/utf_bom.html
BOMs:
  00 00 FE FF    = UTF-32, big-endian
  FF FE 00 00    = UTF-32, little-endian
  FE FF          = UTF-16, big-endian
  FF FE          = UTF-16, little-endian
  EF BB BF       = UTF-8

Win2k Notepad save formats:
  'Unicode'  = UTF-16LE
  'UTF-8'    = UTF-8 with BOM mark
***/
import java.io.*;


/**
* This inputstream will recognize unicode BOM marks
* and will skip bytes if getEncoding() method is called
* before any of the read(...) methods. Invoking read methods
* without first calling getEncoding will not skip BOM mark.
*
* Usage pattern:
    String enc = "ISO-8859-1"; // or put NULL to use systemdefault
    FileInputStream fis = new FileInputStream(file);
    UnicodeInputStream uin = new UnicodeInputStream(fis, enc);
    enc = uin.getEncoding(); // check for BOM mark and skip bytes
    InputStreamReader in;
    if (enc == null) in = new InputStreamReader(uin);
    else in = new InputStreamReader(uin, enc);
*/
public class UnicodeInputStream extends InputStream
{
    private static final int BOM_SIZE = 4;
    PushbackInputStream internalIn;
    boolean isInited = false;
    String defaultEnc;
    String encoding;
    boolean skipBOM = true;

    public UnicodeInputStream(InputStream in, String defaultEnc)
    {
        internalIn = new PushbackInputStream(in, BOM_SIZE);
        this.defaultEnc = defaultEnc;
    }

    public boolean isBOMSkipped()
    {
        return skipBOM;
    }

    public void setSkipBOM(boolean b)
    {
        skipBOM = b;
    }

    public String getDefaultEncoding()
    {
        return defaultEnc;
    }

    /**
     * Read encoding based on BOM mark and skip bytes. All
     * non-mark bytes are unread back to the stream.
     */
    public String getEncoding()
    {
        if (!isInited)
        {
            try
            {
                init();
            }
            catch (IOException ex)
            {
                throw new IllegalStateException("Init method failed." + ex.getMessage());

                //              (Throwable)ex);
            }
        }

        return encoding;
    }

    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are
     * unread back to the stream, only BOM bytes are skipped.
     */
    protected void init() throws IOException
    {
        if (isInited)
        {
            return;
        }

        byte[] bom = new byte[BOM_SIZE];
        int n;
        int unread;
        n = internalIn.read(bom, 0, bom.length);

        if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF))
        {
            encoding = "UTF-8";
            unread = n - 3;
        }
        else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF))
        {
            encoding = "UTF-16BE";
            unread = n - 2;
        }
        else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE))
        {
            encoding = "UTF-16LE";
            unread = n - 2;
        }
        else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) &&
                (bom[3] == (byte) 0xFF))
        {
            encoding = "UTF-32BE";
            unread = n - 4;
        }
        else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) &&
                (bom[3] == (byte) 0x00))
        {
            encoding = "UTF-32LE";
            unread = n - 4;
        }
        else
        {
            // Unicode BOM mark not found, unread all bytes
            encoding = defaultEnc;
            unread = n;
        }

        //     System.out.println("read=" + n + ", unread=" + unread);
        // always unread all bytes if skipBOM=false
        if (!skipBOM)
        {
            unread = n;
        }

        if (unread > 0)
        {
            internalIn.unread(bom, (n - unread), unread);
        }

        isInited = true;
    }

    public void close() throws IOException
    {
        //init();
        isInited = true; // after this init() does nothing
        internalIn.close();
    }

    public int read() throws IOException
    {
        //init();
        isInited = true; // after this init() does nothing

        return internalIn.read();
    }
}
