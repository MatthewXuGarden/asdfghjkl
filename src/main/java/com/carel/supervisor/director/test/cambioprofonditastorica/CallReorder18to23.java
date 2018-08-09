package com.carel.supervisor.director.test.cambioprofonditastorica;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.reorder.ReorderFrequency;
import com.carel.supervisor.dataaccess.reorder.ReorderInformation;

import com.carel.supervisor.field.DataDynamicSaveMember;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class CallReorder18to23
{
    private static int NUM_REORDER_QUEUE = 100;

    public static void main(String[] args) throws Exception
    {
        String xmlInit =
            "<component class=\"com.carel.supervisor.dataaccess.db.DatabaseMgr\">" +
            "<manager name=\"typemgr\">" +
            "<element type=\"java.lang.String\" javatype=\"1\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBVarchar\"/>" +
            "<element type=\"java.lang.String\" javatype=\"12\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBVarchar\"/>" +
            "<element type=\"java.lang.Long\" javatype=\"4\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBLong\"/>" +
            "<element type=\"java.lang.Integer\" javatype=\"4\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBInteger\"/>" +
            "<element type=\"java.lang.Double\" javatype=\"8\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBDouble\"/>" +
            "<element type=\"java.lang.Double\" javatype=\"2\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBDouble\"/>" +
            "<element type=\"java.lang.Float\" javatype=\"7\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBFloat\"/>" +
            "<element type=\"java.lang.Short\" javatype=\"5\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBShort\"/>" +
            "<element type=\"java.util.Date\" javatype=\"91\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBDate\"/>" +
            "<element type=\"java.sql.Date\" javatype=\"91\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBDate\"/>" +
            "<element type=\"java.sql.Time\" javatype=\"92\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBTime\"/>" +
            "<element type=\"java.sql.Timestamp\" javatype=\"93\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBTimeStamp\"/>" +
            "<element type=\"null\" javatype=\"0\" class=\"com.carel.supervisor.dataaccess.db.types.dbregistry.DBNull\"/>" +
            "</manager>" +
            "<manager name=\"connectionmgr\" default=\"POSTGRES\">" +
            "<connection name=\"POSTGRES\" type=\"com.carel.supervisor.dataaccess.db.connection.impl.PoolConnection\">" +
            "<element type=\"driver\" value=\"org.postgresql.Driver\"/>" +
            "<element type=\"url\" value=\"jdbc:postgresql://localhost/development\"/>" +
            "<element type=\"user\" value=\"postgres\"/>" +
            "<element type=\"password\" value=\"postgres\"/>" +
            "<element type=\"numconnections\" value=\"10\"/>" +
            "</connection>" + "</manager>" + "</component>";
        DatabaseMgr databaseMgr = DatabaseMgr.getInstance();
        databaseMgr.init(XMLNode.parse(xmlInit));

        ReorderInformation reorderQueue = new ReorderInformation(NUM_REORDER_QUEUE);
        ReorderFrequency reorderFrequency = null;
        Object[] object = new Object[4];

        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader myInput = new BufferedReader(reader);
        String str = new String();
        System.out.print("Introdurre idSite ");
        str = myInput.readLine();
        object[0] = new Integer(str);
        System.out.print("Introdurre idVariable ");
        str = myInput.readLine();
        object[1] = new Integer(str);
        object[2] = new Integer(1);
        System.out.print("Introdurre nuovo Kmax ");
        str = myInput.readLine();
        object[3] = new Long(new Long(str).longValue() * DataDynamicSaveMember.VALUES);

        long init;
        long end;
        init = System.currentTimeMillis();
        reorderQueue.enqueRecord(object);
        reorderFrequency = new ReorderFrequency(reorderQueue);
        reorderFrequency.startReorderHistorical();
        end = System.currentTimeMillis();
        System.out.println("Ordinato...Time:" + (end - init));
    }
}
