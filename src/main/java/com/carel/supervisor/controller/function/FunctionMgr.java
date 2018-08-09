package com.carel.supervisor.controller.function;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.xml.*;
import java.util.*;


public class FunctionMgr extends InitializableBase
{
	public static final String REAL = "real";
	public static final String BOOLEAN = "boolean";
    private static final String CLASS = "class";
    private static final String CODE = "code";
    private static final String ELEMENT = "element";
    private static boolean initialized = false;
    private static final FunctionMgr me = new FunctionMgr();
    private CalcElementFactory calcFactory = null;
    private List functionDescr = new ArrayList();
    
    private FunctionMgr()
    {
    }

    public static FunctionMgr getInstance()
    {
        return me;
    }

    public boolean initialized()
    {
        return initialized;
    }

    //LDAC: TO DO : SISTEMARE GLI ERRORI
    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            Map registry = new HashMap();
            String type = null;
            String className = null;
            XMLNode xmlTmp = null;
            
            for (int i = 0; i < xmlStatic.size(); i++)
            {
                xmlTmp = xmlStatic.getNode(i);

                if (!xmlTmp.getNodeName().equals(ELEMENT))
                {
                    FatalHandler.manage(this,
                        CoreMessages.format("DTCE0010", xmlTmp.getNodeName(),
                            ELEMENT));
                }

                type = retrieveAttribute(xmlTmp, CODE, "DTCE0013",
                        String.valueOf(i));
                className = retrieveAttribute(xmlTmp, CLASS, "DTCE0013",
                        String.valueOf(i));
                registry.put(type, className);
                functionDescr.add(new FunctionInfo(xmlTmp));
            }

            calcFactory = new CalcElementFactory(registry);
            initialized = true;
        }
    }

    public Function create(CalcElementData elementData)
        throws Exception
    {
        return (Function) calcFactory.create(elementData);
    }
    
    public void clearCache()
    throws Exception
{
    	calcFactory.clearCache();
}
    
    public List getFunctionList()
    {
    	return functionDescr;
    }
    
    public List getFunctionList(String type)
    {
		List list = new ArrayList();
		FunctionInfo funcInfo = null; 
		for(int i = 0; i < functionDescr.size(); i++)
		{
			funcInfo = (FunctionInfo)functionDescr.get(i);
			if (funcInfo.getIn().equalsIgnoreCase(type) && funcInfo.isWeb())
			{
				list.add(funcInfo);
			}
		}
		return list;
    }
}
