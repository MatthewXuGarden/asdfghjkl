package com.carel.supervisor.dataaccess.language;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;


public class LocaleToCharset
{
    private static Map registry;

    static
    {
        registry = new Hashtable();
        registry.put("ar", "ISO-8859-6");
        registry.put("be", "ISO-8859-5");
        registry.put("bg", "ISO-8859-5");
        registry.put("ca", "ISO-8859-1");
        registry.put("cs", "ISO-8859-2");
        registry.put("da", "ISO-8859-1");
        registry.put("de", "ISO-8859-1");
        registry.put("el", "ISO-8859-7");
        registry.put("en", "ISO-8859-1");
        registry.put("es", "ISO-8859-1");
        registry.put("et", "ISO-8859-1");
        registry.put("fi", "ISO-8859-1");
        registry.put("fr", "ISO-8859-1");
        registry.put("he", "ISO-8859-8");
        registry.put("hr", "ISO-8859-2");
        registry.put("hu", "ISO-8859-2");
        registry.put("is", "ISO-8859-1");
        registry.put("it", "ISO-8859-1");
        registry.put("iw", "ISO-8859-8");
        registry.put("ja", "Shift_JIS");
        registry.put("ko", "EUC-KR"); // Requires JDK 1.1.6
        registry.put("lt", "ISO-8859-2");
        registry.put("lv", "ISO-8859-2");
        registry.put("mk", "ISO-8859-5");
        registry.put("nl", "ISO-8859-1");
        registry.put("no", "ISO-8859-1");
        registry.put("pl", "ISO-8859-2");
        registry.put("pt", "ISO-8859-1");
        registry.put("ro", "ISO-8859-2");
        registry.put("ru", "ISO-8859-5");
        registry.put("sh", "ISO-8859-5");
        registry.put("sk", "ISO-8859-2");
        registry.put("sl", "ISO-8859-2");
        registry.put("sq", "ISO-8859-2");
        registry.put("sr", "ISO-8859-5");
        registry.put("sv", "ISO-8859-1");
        registry.put("tr", "ISO-8859-9");
        registry.put("uk", "ISO-8859-5");
        registry.put("zh", "GB2312");
        registry.put("zh_TW", "Big5");
    }

    private LocaleToCharset()
    {
    }

    public static String getCharset(Locale locale)
    {
        String charset;
        charset = (String) registry.get(locale.toString());

        if (null != charset)
        {
            return charset;
        }

        charset = (String) registry.get(locale.getLanguage());

        return charset;
    }
}
