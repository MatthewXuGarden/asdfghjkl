package com.carel.supervisor.base.util;

public class UtilityString
{
    private UtilityString()
    {	
    }
    
	public static String loadConvert(char[] in, int off, int len)
    {
        char[] convtBuf = new char[1024];
        int offTmp = off;
        if (convtBuf.length < len)
        {
            int newLen = len * 2;

            if (newLen < 0)
            {
                newLen = Integer.MAX_VALUE;
            }

            convtBuf = new char[newLen];
        }

        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = offTmp + len;

        while (offTmp < end)
        {
            aChar = in[offTmp++];

            if (aChar == '\\')
            {
                aChar = in[offTmp++];

                if (aChar == 'u')
                {
                    // Read the xxxx
                    int value = 0;

                    for (int i = 0; i < 4; i++)
                    {
                        aChar = in[offTmp++];

                        switch (aChar)
                        {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = ((value << 4) + aChar) - '0';

                            break;

                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = ((value << 4) + 10 + aChar) - 'a';

                            break;

                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = ((value << 4) + 10 + aChar) - 'A';

                            break;

                        default:
                            break;
                        }
                    }

                    out[outLen++] = (char) value;
                }
                else
                {
                    if (aChar == 't')
                    {
                        aChar = '\t';
                    }
                    else if (aChar == 'r')
                    {
                        aChar = '\r';
                    }
                    else if (aChar == 'n')
                    {
                        aChar = '\n';
                    }
                    else if (aChar == 'f')
                    {
                        aChar = '\f';
                    }

                    out[outLen++] = aChar;
                }
            }
            else
            {
                out[outLen++] = (char) aChar;
            }
        }

        return new String(out, 0, outLen);
    }
	public static String substring(String value, int length)
	{
		if(value != null)
		{
			if(value.length()<=length)
				return value;
			else
				return value.substring(0,length);
		}
		else
			return null;
	}
	
	public static String replaceBadChars4XML(String str){
	 	if(str==null ||str.trim().equals("")){
	 		return "";
	 	}
    	str = str.replaceAll("&", "&amp;");
    	str = str.replaceAll("<", "&lt;");
    	str = str.replaceAll(">", "&gt;");
    	str = str.replaceAll("'", "&#039;");
    	str = str.replaceAll("\"", "&quot;");
    	return str;
    }
	
	 
}
