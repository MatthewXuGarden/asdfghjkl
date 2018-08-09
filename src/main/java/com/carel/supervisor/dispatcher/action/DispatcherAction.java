package com.carel.supervisor.dispatcher.action;

import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.ZMemory;


public abstract class DispatcherAction
{
    private ArrayList<Integer> keyAction = new ArrayList<Integer>();
    private ArrayList<Integer> idAction = new ArrayList<Integer>();
    private ArrayList<Integer> idVariable = new ArrayList<Integer>();
    private ArrayList<Integer> status = new ArrayList<Integer>();
    private ArrayList<Timestamp> lastupdate = new ArrayList<Timestamp>();
    private ArrayList<String> pathFile = new ArrayList<String>();
    protected String[] recepients = null;
    protected String typeAction = "";
    private String template = "";
    private boolean isAlarm = false;
    private int idSite = 0;
    private String nameSite = "";
    private int lowpriority = 0;
    private Timestamp instime = null;
    private ArrayList<Timestamp> startTime = new ArrayList<Timestamp>();
    private ArrayList<Timestamp> endTime = new ArrayList<Timestamp>();
    private int retryNum = 0;
    private int retryAfter = 0;
    private String fisicDevice = "";
    private String actionName = "";

    public DispatcherAction(Integer key, Integer id, Integer pri, Integer sta, String rec,
        Timestamp itime, Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar,
        Timestamp startTime, Timestamp endTime)
    {
        this.keyAction.add(key);
        this.idAction.add(id);
        this.status.add(sta);
        this.idVariable.add(idvar);
        this.lastupdate.add(utime);
        this.startTime.add(startTime);
        this.endTime.add(endTime);
        this.lowpriority = pri.intValue();
        this.instime = itime;

        // Compare params
        this.template = tmpl;
        this.typeAction = type;
        this.recepients = initializedRecepients(rec);
        this.isAlarm = isalarm.booleanValue();

        ZMemory zMem = DispMemMgr.getInstance().readConfiguration(this.typeAction);

        if (zMem != null)
        {
            this.retryNum = zMem.getRetryNum();
            this.retryAfter = zMem.getRetryAfter();

            try
            {
                this.fisicDevice = zMem.getFisicDeviceId();
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }
        else
        {
            if (DispatcherMgr.getInstance().typeHasMemory(this.typeAction))
            {
                Object[] param = { DispatcherMgr.getInstance().decodeActionType(this.typeAction) };
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_WARNING, "D028", param);
            }
        }
    }

    public void includeAction(DispatcherAction da)
    {
        this.keyAction.add(da.getKeyAction().get(0));
        this.idAction.add(da.getIdAction().get(0));
        this.status.add(da.getStatus().get(0));
        this.lastupdate.add(da.getLastupdate().get(0));
        this.idVariable.add(da.getIdVariable().get(0));
        this.startTime.add(da.getStartTime().get(0));
        this.endTime.add(da.getEndTime().get(0));

        if (!comparePriority(da.getPriority(), da.getInsertTime()))
        {
            this.lowpriority = da.getPriority();
            this.instime = da.getInsertTime();
        }
    }

    private boolean comparePriority(int pri, Timestamp t)
    {
        boolean ret = false;

        if (this.getPriority() > pri)
        {
            ret = true;
        }
        else if ((this.getPriority() == pri) && (this.getInsertTime().getTime() < t.getTime()))
        {
            ret = true;
        }

        return ret;
    }

    public boolean containsRecepient(String rec)
    {
        boolean ret = false;

        for (int i = 0; i < this.recepients.length; i++)
        {
            if (this.recepients[i].equalsIgnoreCase(rec))
            {
                ret = true;

                break;
            }
        }

        return ret;
    }

    public boolean compareAction(DispatcherAction da)
    {
        String[] dest = da.getRecepients();
        boolean ret = false;

        // Only if Alarm and not S -> SMS
        if (da.isAlarm() && !da.getTypeAction().equalsIgnoreCase("S") &&
                !da.getTypeAction().equalsIgnoreCase("V") &&
                !da.getTypeAction().equalsIgnoreCase("L"))
        {
            // Only if the same action and have the same template
            if (this.getTypeAction().equalsIgnoreCase(da.getTypeAction()) &&
                    (this.getTemplate() != null) &&
                    this.getTemplate().equalsIgnoreCase(da.getTemplate()))
            {
                // Only if recepients are the same
                if (dest.length == this.recepients.length)
                {
                    for (int i = 0; i < dest.length; i++)
                    {
                        ret = this.containsRecepient(dest[i]);

                        if (!ret)
                        {
                            break;
                        }
                    }
                }
            }
        }

        return ret;
    }

    public void addPathFile(String fp)
    {
        this.pathFile.add(fp);
    }
    
    public String[] getPathFiles()
    {
        String[] p = new String[0];

        if (this.pathFile != null)
        {
            if (this.pathFile.size() != this.recepients.length)
            {
                p = new String[] { (String) this.pathFile.get(0) };
            }
            else
            {
                p = new String[this.recepients.length];

                for (int i = 0; i < p.length; i++)
                {
                    if (this.pathFile != null)
                    {
                        p[i] = (String) this.pathFile.get(i);
                    }
                    else
                    {
                        p[i] = "";
                    }
                }
            }
        }

        return p;
    }

    public void setActionName(String act)
    {
        this.actionName = act;
    }

    public String getActionName()
    {
        return this.actionName;
    }

    public String getNameSite()
    {
        return nameSite;
    }

    public void setNameSite(String nameSite)
    {
        this.nameSite = nameSite;
    }

    public int getIdSite()
    {
        return idSite;
    }

    public void setIdSite(int idSite)
    {
        this.idSite = idSite;
    }

    public ArrayList<Integer> getIdVariable()
    {
        return idVariable;
    }

    public String getTypeAction()
    {
        return this.typeAction;
    }

    public String getTemplate()
    {
        return this.template;
    }

    public String[] getRecepients()
    {
        return this.recepients;
    }

    public ArrayList<Integer> getIdAction()
    {
        return idAction;
    }

    public Timestamp getInsertTime()
    {
        return instime;
    }

    public List<Integer> getKeyAction()
    {
        return keyAction;
    }

    public ArrayList<Timestamp> getLastupdate()
    {
        return lastupdate;
    }

    public int getPriority()
    {
        return lowpriority;
    }

    public ArrayList<Integer> getStatus()
    {
        return status;
    }

    public ArrayList<Timestamp> getStartTime()
    {
        return this.startTime;
    }

    public ArrayList<Timestamp> getEndTime()
    {
        return this.endTime;
    }

    public boolean isAlarm()
    {
        return isAlarm;
    }

    public void setAlarm(boolean isAlarm)
    {
        this.isAlarm = isAlarm;
    }

    public int getRetryNum()
    {
        return this.retryNum;
    }

    public int getRetryAfter()
    {
        return this.retryAfter;
    }

    public String getFisicDevice()
    {
        return this.fisicDevice;
    }

    protected byte[] loadTemplate(String path, String name)
        throws Exception
    {
        URL url = ResourceLoader.fileFromResourcePath(path, name);
        InputStream is = url.openStream();

        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        return buffer;
    }
    
    protected String getResourcePath(String path, String name)
    	throws Exception
    {
    	URL url = ResourceLoader.fileFromResourcePath(path, name);
    	return url.getPath();
    }

    /*
     * Called for save action in hsactionq table
     * Empty implementation
     */
    public int[] putActionInQueue() throws Exception
    {
        return new int[0];
    }

    /*
     * Called for build template
     * Empty implementation
     */
    public void buildTemplate(String pathDir) throws Exception
    {
    }

    /*
     * Get info site installation
     */
    protected String[] getInfoSender(int idsite)
    {
        String[] ret = new String[3];

        try
        {
            String sql = "select a.name as uno,a.phone as due,b.languagecode as tre from cfsite as a," +
                "cfsiteext as b where a.idsite=? and a.idsite=b.idsite and b.isdefault='TRUE'";

            Object[] param = { new Integer(idsite) };

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

            if (rs != null)
            {
                Record r = rs.get(0);
                ret[0] = UtilBean.trim(r.get("uno"));
                ret[1] = UtilBean.trim(r.get("due"));
                ret[2] = UtilBean.trim(r.get("tre"));
            }
        }
        catch (Exception e)
        {
            ret[0] = "";
            ret[1] = "";
            ret[2] = "EN_en";
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_WARNING, "D025", null);
        }

        return ret;
    }

    protected abstract String[] initializedRecepients(String recepient);
}
