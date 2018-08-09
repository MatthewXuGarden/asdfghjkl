package com.carel.supervisor.dataaccess.db;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import java.util.*;


public class SeqMgr implements IInitializable
{
    private static final String TABLENAME = "tablename";
    private static final String COLNAME = "colname";
    private static final String VALUE = "value";
    private static boolean initialized = false;
    private static SeqMgr me = new SeqMgr();
    private int buffer = 100;
    private Map seq = new Hashtable();

    private SeqMgr()
    {
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            try
            {
                innerLoad();
            }
            catch (DataBaseException e)
            {
                throw new InvalidConfigurationException("", e);
            }

            initialized = true;
        }
    }

    public static SeqMgr getInstance()
    {
        return me;
    }

    public synchronized void refresh() throws DataBaseException
    {
    	seq.clear();
    	innerLoad();
    }
    
    private void innerLoad() throws DataBaseException
    {
    	String sql = "Select tablename, colname, value from sykeys";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null,
                sql);
        Record record = null;
        String key = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            key = UtilBean.trim(record.get(TABLENAME)) + "-" +
                UtilBean.trim(record.get(COLNAME));
            try {
            	String fieldsql = "SELECT " +
            			"data_type " +
            			"from " +
            			"information_schema.columns " +
            			"where " +
            			"table_name='"+UtilBean.trim(record.get(TABLENAME))+"' " +
            			"and column_name='"+UtilBean.trim(record.get(COLNAME))+"';"; 
            	RecordSet fieldtype = DatabaseMgr.getInstance().executeQuery(null,fieldsql);
            	if("integer".equalsIgnoreCase((String)fieldtype.get(0).get("data_type"))){
	            	Record maxrecord = DatabaseMgr.getInstance().executeQuery(null,"select max("+record.get(COLNAME)+") as maxid from "+record.get(TABLENAME)).get(0);
	            	Integer keyval = new Integer(Math.max((Integer) record.get(VALUE), (Integer) maxrecord.get("maxid")));
					seq.put(key, new Counter(keyval+1));        		
            	} else {
            		LoggerMgr.getLogger(this.getClass()).info((String)fieldtype.get(0).get("data_type"));
            		throw new Exception();
            	}
			} catch (Exception e) {
	            seq.put(key, new Counter((Integer) record.get(VALUE)));
			}
        }
        close();
    }
    
    public synchronized void close() throws DataBaseException
    {
    	String[] keys = new String[seq.size()];
    	keys = (String[])seq.keySet().toArray(keys);
    	String[] tokens = null;
    	Counter count = null;
    	String sql = "update sykeys set value = ? where tablename = ? and colname = ?";
    	for(int i = 0; i < keys.length; i++)
    	{
    		tokens = StringUtility.split(keys[i],"-");
    		count = (Counter) seq.get(keys[i]);
    		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Integer(count.getActual()+5),tokens[0],tokens[1]});
    	}
    }
    
    //Generare blocchi di N
    public synchronized Integer next(String dbId, String table, String column)
        throws DataBaseException
    {
        String key = table + "-" + column;
        Counter count = (Counter) seq.get(key);

        if ((null != count) && (count.isNext()))
        {
            return count.next();
        }
        else
        {
            SeqCommand seqCommand = new SeqCommand(table, column, buffer);
            RecordSet recordSet = DatabaseMgr.getInstance().executeCommand(dbId,
                    seqCommand);

            if (recordSet != null)
            {
                int value = ((Integer) recordSet.get(0).get(0)).intValue();

                if (null == count)
                {
                    count = new Counter(new Integer(value - buffer));
                    seq.put(table + "-" + column, count);
                }
                else
                {
                    count.setValue(value - buffer);
                }

                count.setBuffer(buffer);
            }

            return count.next();
        }
    }

    class Counter
    {
        private int buffer = 0;
        private int value = 0;

        Counter(Integer value)
        {
            this.value = value.intValue();
        }

        public boolean isNext()
        {
            return (0 < buffer);
        }

        public Integer next()
        {
            buffer--;
            value++;
            return new Integer(value);
        }
        
        protected int getActual()
        {
        	return value;
        }
        
        /**
         * @param buffer
         */
        public void setBuffer(int buffer)
        {
            this.buffer = buffer;
        }

        /**
         * @param value
         */
        public void setValue(int value)
        {
            this.value = value;
        }
    }
}
