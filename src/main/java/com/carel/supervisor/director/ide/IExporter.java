package com.carel.supervisor.director.ide;

import com.carel.supervisor.base.xml.XMLNode;


public interface IExporter
{
    public abstract XmlStream exporter(String language)
        throws Exception;

    public abstract void importer(XMLNode xmlNode) throws Exception;
}
