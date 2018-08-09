package com.carel.supervisor.dispatcher.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;


public class HSActionQBeanList
{
    List list = null;

    public HSActionQBeanList()
    {
        list = new ArrayList();
    }

    public void addAction(HSActionQBean bean)
    {
        this.list.add(bean);
    }

    public boolean insertActions() throws Exception
    {
        String sql = "insert into hsactionqueue values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        HSActionQBean bean = null;
        boolean state = true;
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String device = "";

        for (int i = 0; i < this.list.size(); i++)
        {
            try
            {
                bean = (HSActionQBean) this.list.get(i);
                device = bean.getChannel();

                if ((device != null) && !device.equalsIgnoreCase(""))
                {
                    Object[] params = 
                        {
                            new Integer(bean.getKey()), bean.getPvcode(),
                            new Integer(bean.getSite()), new Integer(bean.getPriority()),
                            new Integer(bean.getRetrynum() + 1),
                            new Integer((bean.getRetryafter() == 0) ? 1 : bean.getRetryafter()),
                            time, time, time, bean.getChannel(), bean.getType(),
                            new Integer(HSActionBeanList.INSERT), bean.getPath(), bean.getMessage(),
                            bean.getReceivers(), bean.getActionsid()
                        };

                    DatabaseMgr.getInstance().executeStatement(null, sql, params);
                }
                else
                {
                    state = false;

                    try
                    {
                        EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                            EventDictionary.TYPE_ERROR, "D046",
                            new Object[]
                            {
                                DispatcherMgr.getInstance().decodeActionType(bean.getType())
                            });
                        EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                            EventDictionary.TYPE_ERROR, "D047",
                            new Object[]
                            {
                                DispatcherMgr.getInstance().decodeActionType(bean.getType())
                            });
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
                state = false;
            }
        }

        return state;
    }

    public HSActionQBean dequeAction(String fisDev)
    {
    	String str = ProductInfoMgr.getInstance().getProductInfo().get("needRemoteACK");
        boolean needRemoteACK = Boolean.valueOf(str!=null?str:"false");
        String sql = 
        	"select * from hsactionqueue where channel=? and timetogo <= ? and " +
        	"retrynum != 0 and status=? order by priority asc, inserttime asc, retrynum desc";
        //don't need ACK from remote. old way
        if(!needRemoteACK)
        	sql += " limit 1";

        HSActionQBean bean = null;

        try
        {
            Object[] params = 
                {
                    fisDev, new Timestamp(System.currentTimeMillis()),
                    new Integer(HSActionBeanList.INSERT)
                };
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

            if ((rs != null) && (rs.size() > 0))
            {
            	if(needRemoteACK)
            	{
	            	for(int i=0;i<rs.size();i++)
	            	{
	            		bean = new HSActionQBean(rs.get(i));
	            		//for remote action(D), if retry number is 1 and it is not the first time to dispatch(inserttime<>lastupdate)
	            		//we can say the remote action is wait for timeout check, we will return action really need to dispatch
	            		if(bean.getType().equals("D") && bean.getInsertime().before(bean.getLastupdate()) && bean.getRetrynum() == 1)
	            			continue;
	            		else
	            			return bean;
	            	}
            	}
            	else
            		bean = new HSActionQBean(rs.get(0));
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return bean;
    }
    
    public HSActionQBean[] getRemoteActionForTimeoutCheck()
    {
        String sql = 
        	"select * from hsactionqueue where actiontype='D' and timetogo <= ? and " +
        	"retrynum =1 and status=? order by priority asc, inserttime asc, retrynum desc";

        List<HSActionQBean> list = new ArrayList<HSActionQBean>();
        try
        {
            Object[] params = 
                {
                    new Timestamp(System.currentTimeMillis()),
                    new Integer(HSActionBeanList.INSERT)
                };
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

            if ((rs != null) && (rs.size() > 0))
            {
            	for(int i=0;i<rs.size();i++)
            	{
            		list.add(new HSActionQBean(rs.get(i)));	
            	}
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        return (HSActionQBean[])list.toArray(new HSActionQBean[list.size()]);
    }
    
    public HSActionQBean getLastRemoteAction(String channel)
    {
        String sql = 
        	"select * from hsactionqueue where actiontype='D' and channel=? and " +
        	"retrynum >0 and status=? order by timetogo limit 1";

        List<HSActionQBean> list = new ArrayList<HSActionQBean>();
        try
        {
            Object[] params = 
                {
                    channel,
                    new Integer(HSActionBeanList.INSERT)
                };
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

            if ((rs != null) && (rs.size() > 0))
            {
            	return new HSActionQBean(rs.get(0));
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        return null;
    }

    public void updatePriorityScheduledAction(long timebefore, String fisDev)
        throws Exception
    {
        String sql = "update hsactionqueue set priority=priority-1 where channel=? and status=? and priority >1 and inserttime <= ?";
        long curTime = System.currentTimeMillis() - timebefore;
        Timestamp time = new Timestamp(curTime);
        Object[] params = { fisDev, new Integer(HSActionBeanList.INSERT), time };
        DatabaseMgr.getInstance().executeStatement(null, sql, params);
    }

    public void requeueScheduledAction(int idactionq, int numretry, Timestamp timetogo,
        String fisDev) throws Exception
    {
        String sql = "update hsactionqueue set retrynum=?,lastupdate=?,timetogo=? where channel=? and idhsactionqueue=?";

        try
        {
            Object[] params = 
                {
                    new Integer(numretry), new Timestamp(System.currentTimeMillis()), timetogo,
                    fisDev, new Integer(idactionq)
                };
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            throw new Exception(e.getMessage());
        }
    }

    public void sendedScheduledAction(HSActionQBean action, String fisDev)
        throws Exception
    {
        deleteScheduledAction(action, HSActionBeanList.SEND, fisDev);
        new HSActionBeanList().updateToSendActionList(getIdActions(action));
    }

    public void discardScheduledAction(HSActionQBean action, String fisDev)
        throws Exception
    {
        updatestateScheduledAction(action, HSActionBeanList.DISCARD, fisDev);
        new HSActionBeanList().updateToDiscardActionList(getIdActions(action));
    }

    public void cancelScheduledAction(HSActionQBean action, String fisDev)
        throws Exception
    {
        deleteScheduledAction(action, HSActionBeanList.CANCEL, fisDev);
        new HSActionBeanList().updateToCancelActionList(getIdActions(action));
    }

    public void cancelChangeFisDevice(String type, String fisDev)
    {
        if ((type != null) && (fisDev != null))
        {
            String sql = "select * from hsactionqueue where actiontype=? and status=? and channel=?";

            HSActionQBean[] bean = null;

            try
            {
                Object[] params = { type, new Integer(HSActionBeanList.INSERT), fisDev };
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

                if ((rs != null) && (rs.size() > 0))
                {
                    bean = new HSActionQBean[rs.size()];

                    for (int i = 0; i < bean.length; i++)
                    {
                        bean[i] = new HSActionQBean(rs.get(i));
                    }
                }
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }

            if (bean != null)
            {
                for (int i = 0; i < bean.length; i++)
                {
                    try
                    {
                        cancelScheduledAction(bean[i], fisDev);
                    }
                    catch (Exception e)
                    {
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
                    }
                }
            }
        }
    }

    public void reAllignTimeToGo(String device, long delta)
    {
        delta = (delta / 1000L);
        delta = (delta / 60L);

        String sql = "update hsactionqueue set timetogo=(timetogo + interval '" + delta +
            " minutes') where channel=? and status=?";

        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql,
                new Object[] { device, new Integer(1) });
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public void AllignTimeToGo(String device)
    {
        String sql = "update hsactionqueue set timetogo=? where channel=? and status=?";

        try
        {
            Object[] param = { new Timestamp(System.currentTimeMillis()), device, new Integer(1) };
            DatabaseMgr.getInstance().executeStatement(null, sql, param);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void deleteScheduledAction(HSActionQBean action, int state, String fisDev)
        throws Exception
    {
        String sql = "delete from hsactionqueue where channel=? and idhsactionqueue=?";

        try
        {
            Object[] params = { fisDev, new Integer(action.getKey()) };
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            throw new Exception(e.getMessage());
        }
    }

    private void updatestateScheduledAction(HSActionQBean action, int state, String fisDev)
        throws Exception
    {
        String sql = "update hsactionqueue set status=?,retrynum=0,lastupdate=? where channel=? and idhsactionqueue=?";

        try
        {
            Object[] params = 
                {
                    new Integer(state), new Timestamp(System.currentTimeMillis()), fisDev,
                    new Integer(action.getKey())
                };
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            throw new Exception(e.getMessage());
        }
    }

    private int[] getIdActions(HSActionQBean action)
    {
        String sActions = action.getActionsid();
        String[] arActions = null;
        int[] actionsId = new int[0];

        if (sActions != null)
        {
            if (sActions.indexOf(",") != -1)
            {
                arActions = sActions.split(",");
            }
            else
            {
                arActions = new String[] { sActions };
            }

            if (arActions != null)
            {
                actionsId = new int[arActions.length];

                for (int i = 0; i < actionsId.length; i++)
                {
                    try
                    {
                        actionsId[i] = Integer.parseInt(arActions[i]);
                    }
                    catch (Exception e)
                    {
                        actionsId[i] = -1;
                    }
                }
            }
        }

        return actionsId;
    }
    public HSActionQBean getHsactionqueueByReceiversid(Integer idsite,String actiontype,String receiversid,String status,String actionsid)
    {
        String sql = 
        	"select * from hsactionqueue where idsite=? and actiontype= ? and " +
        	"receiversid=? and status=? and actionsid=? order by priority asc, inserttime asc, retrynum desc limit 1";

        HSActionQBean bean = null;

        try
        {
            Object[] params = {idsite, actiontype,receiversid,status,actionsid};
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

            if ((rs != null) && (rs.size() > 0))
            {
                bean = new HSActionQBean(rs.get(0));
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return bean;
    }
}
