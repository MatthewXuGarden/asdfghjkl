package com.carel.supervisor.dataaccess.db.connection;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.factory.*;
import com.carel.supervisor.base.xml.*;
import java.util.*;


/**
 * @author Loris D'Acunto <br>
 * Carel S.p.A. <br>
 *<br>
 * 2-nov-2005 11.48.04<br>
 * <br>
 * Registry class for all the connections to databases defined inside architectural configuration file<br>
 * The syntax is the following:<br>
 * <br>
 * <code>
 * &lt;connection name="DB2" type="com.carel.visor.dataaccess.db.connection.impl.DirectConnection"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&lt;element type="driver" value="COM.ibm.db2.jdbc.net.DB2Driver"/&gt;<br>
 * &nbsp;&nbsp;&nbsp;&lt;element type="url" value="jdbc:db2:localhost:6789:CAREL"/&gt;<br>
 * &nbsp;&nbsp;&nbsp;&lt;element type="user" value="xxxx"/&gt;<br>
 * &nbsp;&nbsp;&nbsp;&lt;element type="password" value="xxxx"/&gt;<br>
 * &nbsp;&nbsp;&nbsp;&lt;/connection&gt;<br></code>
 * <br>
 * <i>connection name</i> is the connection database identifier used when an object try to accesso to the database (dbId in executeStatement or executeQuery)<br>
 * <i>connection type</i> is the class that implement IConnection interface <br>
 * <i>element type driver</i> depends on the database used<br>
 * <i>element type url</i> is the url used to connect to the database<br>
 * <i>element type user</i> is the user authorized to connect to the databse<br>
 * <i>element type password</i> is the user password<br>
 * <br>
 * This is olny an example of the dataaccess layer configuration, because it depends on the specific implementation od the IConneciton interface.<br>
 */
public class ConnectionRegistry extends InitializableBase
{
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String CONNECECTION = "connection";
    private static final String DEFAULT = "default";
    private Map connection = new HashMap();
    private String defaultName = null;

    /**
     * @param xmlStatic: XMLNode
     * @throws InvalidConfigurationException
     * ConnectionRegistry
     */
    public ConnectionRegistry(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        XMLNode xmlTmp = null;
        String nameConn = null;
        String typeConn = null;
        IConnection connectionImpl = null;
        defaultName = retrieveAttribute(xmlStatic, DEFAULT, "DTCE0008");

        Properties properties = null;

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xmlTmp = xmlStatic.getNode(i);

            if (!xmlTmp.getNodeName().equals(CONNECECTION))
            {
                FatalHandler.manage(this,
                    CoreMessages.format("DTCE0010", xmlTmp.getNodeName(),
                        CONNECECTION));
            }

            nameConn = retrieveAttribute(xmlTmp, NAME, "DTAC0011",
                    String.valueOf(i));
            typeConn = retrieveAttribute(xmlTmp, TYPE, "DTAC0011",
                    String.valueOf(i));
            properties = retrieveProperties(xmlTmp, TYPE, VALUE, "DTCE0008");

            try
            {
                connectionImpl = (IConnection) FactoryObject.newInstance(typeConn,
                        new Class[] { java.util.Properties.class },
                        new Object[] { properties });
            }
            catch (Exception e)
            {
                FatalHandler.manage(this,
                    CoreMessages.format("DTCE0012", typeConn), e);
            }

            connection.put(nameConn, connectionImpl);
        }
    }

    public IConnection getConnection(String name) throws InvalidConnection
    {
        IConnection connectionImpl = (IConnection) connection.get(name);

        if (null == connectionImpl)
        {
            throw new InvalidConnection(name);
        }

        return connectionImpl;
    }

    public IConnection getConnection()
    {
        return (IConnection) connection.get(defaultName);
    }
}
