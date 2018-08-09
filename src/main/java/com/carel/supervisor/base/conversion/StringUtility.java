package com.carel.supervisor.base.conversion;

import java.util.StringTokenizer;


public class StringUtility
{
	
	private static String[] badGfxChars = {";", "<", "\"", "'", "&lt"}; //bad chars not allowed by FlashGfxObj
	private static String[] badOSChars = {"/", ">", "*", ":", "?", "|", "\\", "\"", "<"}; //bad chars not allowed by OS in dir/file name

	
	private StringUtility()
    {
    }

    public static String[] split(String value, String token)
    {
        StringTokenizer tokenizer = new StringTokenizer(value, token);
        int count = tokenizer.countTokens();
        String[] tokens = new String[count];
        int pos = 0;

        while (tokenizer.hasMoreTokens())
        {
            tokens[pos] = tokenizer.nextToken();
            pos++;
        }

        return tokens;
    }
    
	/**
	 * @param strIn = descrizione var/dev per grafico flash
	 * @return strOut = descrizione epurata da chars non ben interpretati dal FlashGfxObj
	 */
	public static String clrBadGfxChars(String strIn)
    {
		String strOut = strIn;
		
		for (int i = 0; i < badGfxChars.length; i++)
		{
			strOut = Replacer.replace(strOut, badGfxChars[i], "");
		}
		
		return strOut;
    }
	
	/**
	 * @param strIn = code dev per i/o file parametri
	 * @return strOut = code epurato da chars non permessi dal SO in nomi dir/file
	 */
	public static String clrBadOSChars(String strIn)
    {
		String strOut = strIn;
		
		for (int i = 0; i < badOSChars.length; i++)
		{
			strOut = Replacer.replace(strOut, badOSChars[i], "");
		}
		
		return strOut;
    }
}
