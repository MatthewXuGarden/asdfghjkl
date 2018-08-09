package com.carel.supervisor.controller.function;

import com.carel.supervisor.base.xml.XMLNode;


public class FunctionInfo
{
    private static final String SYMBOL = "symbol";
    private static final String CODE = "code";
    private static final String IN = "in";
    private static final String OUT = "out";
    private static final String PARS = "pars";
    private static final String WEB = "web";
    private String symbol = null;
    private String code = null;
    private String in = null;
    private String out = null;
    private String pars = null;
    private boolean web = false;

    public FunctionInfo(XMLNode xmlNode)
    {
        symbol = xmlNode.getAttribute(SYMBOL);
        code = xmlNode.getAttribute(CODE);
        in = xmlNode.getAttribute(IN);
        out = xmlNode.getAttribute(OUT);
        pars = xmlNode.getAttribute(PARS);
        web = xmlNode.getAttribute(WEB, true);
    }

    /**
     * @return: String
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @return: String
     */
    public String getIn()
    {
        return in;
    }

    /**
     * @return: String
     */
    public String getOut()
    {
        return out;
    }

    /**
     * @return: String
     */
    public String getPars()
    {
        return pars;
    }

    /**
     * @return: String
     */
    public String getSymbol()
    {
        return symbol;
    }

    /**
     * @return: boolean
     */
    public boolean isWeb()
    {
        return web;
    }
}
