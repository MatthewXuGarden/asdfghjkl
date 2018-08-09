package com.carel.supervisor.dispatcher.book;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;
import com.carel.supervisor.presentation.assistance.GuardianConfig;


public class DispatcherBookList
{
    private static DispatcherBookList book = new DispatcherBookList();
    private Map memory = null;

    private DispatcherBookList()
    {
        this.memory = new HashMap();
    }

    public static DispatcherBookList getInstance()
    {
        return book;
    }

    public void reloadReceivers()
    {
        int[] key = new int[0];
        reloadReceivers(key);
    }

    public void reloadReceivers(int[] key)
    {
        DispatcherBook[] db = loadReceivers(key);
        this.memory.clear();

        for (int i = 0; i < db.length; i++)
        {
            this.addReceiver(db[i]);
        }
    }

    public DispatcherBook[] loadReceivers(int[] key)
    {
        DispatcherBook[] tmpBook = null;

        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, getSql(key.length),
                    getParams(key));

            if (rs != null)
            {
                if (rs.size() == 0)
                {
                	/*
                	 * TOLTO.
                	 * Troppo rindondante questo messaggio
                	 * 
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Config",
                        EventDictionary.TYPE_INFO, "D003", null);
                    */
                }

                tmpBook = new DispatcherBook[rs.size()];

                for (int i = 0; i < rs.size(); i++)
                {
                    try
                    {
                        tmpBook[i] = new DispatcherBook(rs.get(i));
                    }
                    catch (Exception e)
                    {
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
                    }
                }
            }
            else
            {
                tmpBook = new DispatcherBook[0];
            }
        }
        catch (Exception e)
        {
            tmpBook = new DispatcherBook[0];
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return tmpBook;
    }

    public void addReceiver(DispatcherBook db)
    {
        if (db != null)
        {
            this.memory.put(new Integer(db.getKey()), db);
        }
        else
        {
            System.out.println("DispatcherBook object null. Can't insert in memory");
        }
    }

    public DispatcherBook getReceiver(int key)
    {
        return (DispatcherBook) this.memory.get(new Integer(key));
    }

    public DispatcherBook[] getReceiversByType(String type)
    {
        Iterator i = this.memory.keySet().iterator();
        Integer key = null;
        List list = new ArrayList();
        DispatcherBook[] ret = null;
        DispatcherBook tmp = null;

        while (i.hasNext())
        {
            key = (Integer) i.next();
            tmp = (DispatcherBook) this.memory.get(key);

            if ((tmp != null) && tmp.getType().equalsIgnoreCase(type))
            {
                list.add(tmp);
            }
        }

        ret = new DispatcherBook[list.size()];

        for (int j = 0; j < ret.length; j++)
        {
            ret[j] = (DispatcherBook) list.get(j);
        }

        return ret;
    }

    public boolean updateAddressBook(DispatcherBook[] list)
    {
        boolean ret = true;

        if (list != null)
        {
            for (int i = 0; i < list.length; i++)
            {
                switch (list[i].getState())
                {
                case 2:
                    insertNewRecepient(list[i]);

                    break;

                case 4:
                    updateRecepient(list[i]);

                    break;

                case 3:
                    ret = deleteRecepient(list[i]);

                    break;
                }
            }

            reloadReceivers();
        }

        return ret;
    }

    private void insertNewRecepient(DispatcherBook dispBook)
    {
        String sql = "insert into cfaddrbook values(?,?,?,?,?,?,?)";

        try
        {
            Integer key = SeqMgr.getInstance().next(null, "cfaddrbook", "idaddrbook");
            Object[] params = 
                {
                    key, dispBook.getPvcode(), new Integer(dispBook.getIdsite()), dispBook.getType(),
                    dispBook.getAddress(), dispBook.getReceiver(),
                    new Timestamp(System.currentTimeMillis())
                };
            DatabaseMgr.getInstance().executeStatement(null, sql, params);

            DataHs dataHs = CreateSqlHs.getInsertData("cfaddrbook", params);
            DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());
        }
        catch (DataBaseException e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    public int addNewRecepient(String pvcode,String idsite,String atype,String addr,String receiver)
    {
        String sql = "insert into cfaddrbook values(?,?,?,?,?,?,?)";

        try
        {
            Integer key = SeqMgr.getInstance().next(null, "cfaddrbook", "idaddrbook");
            Object[] params = {key, pvcode, new Integer(idsite), atype,addr, receiver,new Timestamp(System.currentTimeMillis())};
            DatabaseMgr.getInstance().executeStatement(null, sql, params);

            DataHs dataHs = CreateSqlHs.getInsertData("cfaddrbook", params);
            DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());
            return key;
        }
        catch (DataBaseException e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        return 0;
    }

    private void updateRecepient(DispatcherBook dispBook)
    {
        String sql = "update cfaddrbook set address=?,receiver=?,lastupdate=? where idaddrbook=? and type=?";

        try
        {
            Object[] params = 
                {
                    dispBook.getAddress(), dispBook.getReceiver(),
                    new Timestamp(System.currentTimeMillis()), new Integer(dispBook.getKey()),
                    dispBook.getType()
                };
            DatabaseMgr.getInstance().executeStatement(null, sql, params);

            DataHs dataHs = CreateSqlHs.getUpdateData("cfaddrbook",
                    new String[] { "idaddrbook", "pvcode", "idsite", "type", "address", "receiver" },
                    new Object[] { new Integer(dispBook.getKey()), dispBook.getType() },
                    new String[] { "=", "=" }, new String[] { "idaddrbook", "type" });
            DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());
        }
        catch (DataBaseException e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private boolean deleteRecepient(DispatcherBook dispBook)
    {
        boolean ret = false;

        if (canIDelReceiver(dispBook) && !existInGuardian(dispBook) )
        {
            String sql = "delete from cfaddrbook where idaddrbook =? and type=?";

            try
            {
                Object[] params = { new Integer(dispBook.getKey()), dispBook.getType() };
                DataHs dataHs = CreateSqlHs.getDeleteData("cfaddrbook",
                        new String[] { "idaddrbook", "pvcode", "idsite", "type", "address", "receiver" },
                        params, new String[] { "=", "=" }, new String[] { "idaddrbook", "type" });
                DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
                    dataHs.getObjects());

                DatabaseMgr.getInstance().executeStatement(null, sql, params);
                ret = true;
            }
            catch (DataBaseException e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }

        return ret;
    }
    public boolean deleteRecepient(int idaddrbook,String type){
        boolean ret = false;


        String sql = "delete from cfaddrbook where idaddrbook =? and type=?";

        try{
            Object[] params = { idaddrbook, type };
            DataHs dataHs = CreateSqlHs.getDeleteData("cfaddrbook",
                    new String[] { "idaddrbook", "pvcode", "idsite", "type", "address", "receiver" },
                    params, new String[] { "=", "=" }, new String[] { "idaddrbook", "type" });
            DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
                dataHs.getObjects());

            DatabaseMgr.getInstance().executeStatement(null, sql, params);
            ret = true;
        }catch (DataBaseException e){
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
     return ret;
    }
    private boolean existInGuardian(DispatcherBook dispBook){
    	StringBuffer sb = new StringBuffer();
		String pathFile = BaseConfig.getCarelPath()+"guardian"+File.separator+"Guardian.xml";
		boolean exist = false;
		
		try
		{
			File file = new File(pathFile);
			URL url = file.toURL();
			XMLNode xmlNode = null;
			XMLNode device  = null;
			XMLNode xmlNodeRoot = null;
			DispatcherBook tmp = null;
			String att = "";
			String val = "";
			int idx = -1;
			
			if(url != null)
			{
				xmlNodeRoot = XMLNode.parse(url.openStream());
				xmlNode = xmlNodeRoot.getNode("notifylist");
				String notify = xmlNode.getAttribute("enabled");
				if(notify == null)
					notify = "TRUE";
				for(int i=0; i<xmlNode.size(); i++)
				{
					device = xmlNode.getNode(i);
					for(int j=0; j<device.size(); j++)
					{
						att = device.getNode(j).getAttribute("name");
						if(att != null && att.equalsIgnoreCase("pvpro"))
						{
							val = device.getNode(j).getAttribute("value");
							try {
								idx = Integer.parseInt(val);
								tmp = DispatcherBookList.getInstance().getReceiver(idx);
								if(tmp != null && idx==dispBook.getKey())
								{
									exist = true ;
									break;
								}
							}
							catch(Exception e) {
								Logger logger = LoggerMgr.getLogger(this.getClass());
								logger.error(e);
							}
						}
					}
				}
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
    	return exist;
    }
    private boolean canIDelReceiver(DispatcherBook dispBook)
    {
        boolean ret = true;
        String sql = "select parameters from cfaction where actiontype=?";
        String sId = String.valueOf(dispBook.getKey());
        StringBuffer sb = new StringBuffer(";");

        try
        {
            Object[] params = { dispBook.getType() };
            RecordSet rs = null;
            if(dispBook.getType().equalsIgnoreCase("E")){
            	sql +=" or actiontype=? or actiontype=?";
            	String [] params2 = { dispBook.getType(),"Pr","P" };
            	rs = DatabaseMgr.getInstance().executeQuery(null, sql, params2);
            }else{
            	rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
            }
            

            if (rs != null)
            {
                Record r = null;

                for (int i = 0; i < rs.size(); i++)
                {
                    r = rs.get(i);
                    sb.append(UtilBean.trim(r.get("parameters")));
                    sb.append(";");
                }
            }
        }
        catch (DataBaseException e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        if (sb.toString().indexOf(";" + sId + ";") != -1)
        {
            ret = false;
        }

        return ret;
    }

    private String getSql(int number)
    {
        String sql = "select idaddrbook,pvcode,idsite,type,address,receiver,ioteststatus from cfaddrbook";

        if (number > 0)
        {
            sql += " where idaddrbook in (";

            for (int i = 0; i < number; i++)
            {
                sql += "?,";
            }

            //modify by kevin
            sql = (sql.substring(0, sql.length() - 1) + ")");
            //sql += (sql.substring(0, sql.length() - 1) + ")");
        }

        return sql;
    }

    private Object[] getParams(int[] par)
    {
        Object[] o = new Object[par.length];

        for (int i = 0; i < o.length; i++)
        {
            o[i] = new Integer(par[i]);
        }

        return o;
    }
    public int getAddressId(String type,String address)
    {
    	Iterator i = this.memory.keySet().iterator();
        Integer key = null;
        DispatcherBook tmp = null;

        while (i.hasNext())
        {
            key = (Integer) i.next();
            tmp = (DispatcherBook) this.memory.get(key);

            if ((tmp != null) && tmp.getType().equalsIgnoreCase(type) && tmp.getAddress().equalsIgnoreCase(UtilBean.trim(address)))
            {
                return tmp.getKey();
            }
        }
        return -1;
    }
}
