package com.carel.supervisor.director.test.operativitanormalea;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.field.Status;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;


public class ReadDataTest9
{
    private static final String INSERT_TIME = "inserttime";
    private static final String STATUS = "status";
    private static final String VALUE = "value";
    private static final String N = "n";
    private final static int NUM_ROWS_FETCH = 100;
    private static short idSite = 1;
    private static int idVariable = 1;
    static File fileOutput = new File(
            "C:\\swdept_prj\\plantvisorpro\\developments\\applications\\Director\\test data file\\ReadDataTest9.txt");
    static FileOutputStream fileOutputStream = null;
    static PrintStream printStream = null;

    public static void main(String[] args) throws Throwable
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
        System.out.println("DB Manager Run Ok");

        try
        {
            fileOutputStream = new FileOutputStream(fileOutput);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        printStream = new PrintStream(fileOutputStream);

        Connection connection = DatabaseMgr.getInstance().getConnection(null);

        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * from hsvariable WHERE idSite=? AND idVariable=? ORDER BY inserttime");
        preparedStatement.setShort(1, idSite);
        preparedStatement.setInt(2, idVariable);
        preparedStatement.setFetchSize(NUM_ROWS_FETCH);

        ResultSet resultSet = preparedStatement.executeQuery();

        Timestamp insertTime = null;
        Status status = null;
        Float value = null;
        Float[] values = new Float[63];
        Integer[] n = new Integer[63];

        int record = 0;
        printStream.print("Valore | Stato | n\n");

        while (resultSet.next())
        {
            insertTime = resultSet.getTimestamp(INSERT_TIME);
            status = new Status(resultSet.getLong(STATUS));
            value = new Float(resultSet.getFloat(VALUE));

            //printStream.print(value + "|" + status.getStatus((byte) 0) + "\n");
            for (int i = 1; i < 64; i++)
            {
                Object tmpValue = resultSet.getObject(VALUE + i);

                if (tmpValue == null)
                {
                    values[i - 1] = null;
                }
                else
                {
                    values[i - 1] = (Float) tmpValue;

                    String str = resultSet.getString(N + i);

                    if (str == null)
                    {
                        System.exit(1); //print("Cambio riga o fine Record");
                    }
                    else
                    {
                        if (str.length() == 0)
                        {
                            n[i - 1] = new Integer(0); //leggo il valore 0 che corrisponde a 128
                        }
                        else
                        {
                            n[i - 1] = new Integer((int) str.getBytes()[0]);
                        }
                    } //else

                    printStream.print(values[i - 1].floatValue() + "|" +
                        status.getStatus((byte) i) + "|" + n[i - 1] + "\n");
                } //else
            } //for

            record++;
        } //while

        System.out.println("Fine");
    } //main
} //Class Read
