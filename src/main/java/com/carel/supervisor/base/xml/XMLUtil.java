package com.carel.supervisor.base.xml;

import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.*;
import javax.xml.parsers.*;


public class XMLUtil
{
    private XMLUtil()
    {
    }

    protected static final NodeList extractNodes(String xml)
        throws Exception
    {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
        Reader reader = new StringReader(xml);
        InputSource inputSource = new InputSource(reader);
        Document document = documentBuilder.parse(inputSource);

        return document.getChildNodes();
    }

    protected static final NodeList extractNodes(InputStream inputStream)
        throws Exception
    {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);

        return document.getChildNodes();
    }

    protected static final NodeList extractNodes(Reader reader)
        throws Exception
    {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
        InputSource inputSource = new InputSource(reader);
        Document document = documentBuilder.parse(inputSource);

        return document.getChildNodes();
    }
}
