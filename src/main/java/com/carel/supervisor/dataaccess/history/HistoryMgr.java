package com.carel.supervisor.dataaccess.history;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.dataaccess.db.*;
import java.sql.*;
import java.util.*;


public class HistoryMgr
{
    private static boolean initialized = false;
    private static HistoryMgr me = new HistoryMgr();
    private Map mappa = new HashMap();
    private Connection connection = null;
    private ILocalTimer execTimer = TimerMgr.getTimer("[HISTORYMGR][execute]", TimerMgr.HIGH);

    private HistoryMgr()
    {
    }

    public static HistoryMgr getInstance()
    {
        return me;
    }

    public void load() throws Exception
    {
        connection = DatabaseMgr.getInstance().getConnection(null);
        initialized = true;
    }

    public void close() throws Exception
    {
        try {
            DatabaseMgr.getInstance().closeConnection(null, connection);
        }
        catch(Exception e){
        }
        
        connection = null;
        if(mappa != null)
            mappa.clear();
        initialized = false;
    }

    public void executeMultiUpdateAsData(String dbId, String[] statement, Object[][] values)
        throws Exception
    {
        
    	if (null  == connection)
    	{
    		load(); //Nel caso in cui la connessione, ogni tanto, venga persa
    		//come per esempio la caduta del server Postgres
    		LoggerMgr.getLogger(HistoryMgr.class).warn("Retrying to create db connection");
    	}
    	
    	if (!initialized)
        {
            throw new InvalidConfigurationException("NOT YET INITILIZED");
        }

        
        execTimer.start();

        PreparedStatement preparedStatement = null;
        int j = 0;

        try
        {
            for (j = 0; j < statement.length; j++)
            {
                preparedStatement = getPreparedStatement(dbId, connection, statement[j]);

                for (int i = 0; i < values[j].length; i++)
                {
                    preparedStatement.setObject(i + 1, values[j][i]);
                }

                preparedStatement.execute();
            }

            connection.commit();
        }
        catch (Exception e)
        {
            try
            {
                LoggerMgr.getLogger(this.getClass()).error(e);
            	close(); //Se la connessione cade, la chiudiamo
            }
            catch (Exception e1)
            {   
            	LoggerMgr.getLogger(this.getClass()).error(e1);
            }
            throw new DataBaseException(CoreMessages.format("DTCE0005",
                    new Object[] { dbId, statement[j] }), e);
        }

        execTimer.stop();
    }

    private PreparedStatement getPreparedStatement(String name, Connection connection,
        String statement) throws DataBaseException
    {
       


    	// Modifica a seguito del cambiamento di storico che porta gli statements da 64 a circa 2000 nel caso peggiore
    	  try
        {
            PreparedStatement prep = (PreparedStatement) mappa.get(statement);

            if (null == prep)
             {
                prep = connection.prepareStatement(statement);
                mappa.put(statement, prep);
            }

            return prep;
        }
        catch (Exception e)
        {
            try
            {
                connection.rollback();
                throw new DataBaseException(CoreMessages.format("DTCE0002"), e);
            }
            catch (Exception e2)
            {
                throw new DataBaseException(CoreMessages.format("DTCE0003"), e);
            }
        }
    }
}
