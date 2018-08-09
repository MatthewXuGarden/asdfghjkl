package com.carel.supervisor.base.conversion;

public class Replacer
{
	private Replacer()
	{	
	}
	
    public static final StringBuffer replace(StringBuffer sourcePar,
        String find, String replace)
    {
        String source = new String(sourcePar);

        return replace(sourcePar, source, find, replace);
    }

    public static final String replace(String sourcePar, String find,
        String replace)
    {
        StringBuffer source = new StringBuffer(sourcePar);

        return new String(replace(source, sourcePar, find, replace));
    }

    private static final StringBuffer replace(StringBuffer sourceBufferPar,
        String sourceNamePar, String find, String replace)
    {
        StringBuffer dest = new StringBuffer();
        int start = sourceNamePar.indexOf(find, 0);

        if ((0 != start) && (-1 != start))
        {
            dest.append(sourceNamePar.substring(0, start));
        }
        else if (-1 == start)
        {
            dest.append(sourceBufferPar);
        }

        int end;
        char[] charTmp = null;
        start = 0;

        int lenFind = find.length();

        while (start != -1)
        {
            start = sourceNamePar.indexOf(find, start);

            if (-1 != start)
            {
                end = sourceNamePar.indexOf(find, start + lenFind);

                if (-1 == end)
                {
                    end = sourceNamePar.length();
                }

                charTmp = new char[end - start - lenFind];
                sourceBufferPar.getChars(start + lenFind, end, charTmp, 0);
                dest.append(replace);
                dest.append(charTmp);
                start++;
            }
        }

        return dest;
    }

    public static final String createStringOfChar(String charPar, int len)
    {
        return new String(createStringBufferOfChar(charPar, len));
    }

    public static final StringBuffer createStringBufferOfChar(String charPar,
        int len)
    {
        int i = 0;
        StringBuffer tmp = new StringBuffer();

        for (i = 0; i < len; i++)
        {
            tmp.append(charPar);
        }

        return tmp;
    }
}
