package com.carel.supervisor.plugin.switchtech;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;


public class SwitchMgr
{
    private static SwitchMgr myInstance = new SwitchMgr();
    private long sleep = 60000L;
    private Switch[] childs = null;
    private SwitchClock clock = null;

    private SwitchMgr()
    {
        this.sleep = Long.parseLong(ProductInfoMgr.getInstance().getProductInfo()
                                                  .get("switchpolling"));
        this.childs = new Switch[Integer.parseInt(ProductInfoMgr.getInstance()
                                                                .getProductInfo()
                                                                .get("switchnumber"))];
    }

    public static SwitchMgr getInstance()
    {
        return myInstance;
    }

    public void startSwitch(int idx, Switch obj, String user)
    {
        try
        {
            this.childs[idx - 1] = obj;

            if (clock == null)
            {
                clock = new SwitchClock(this.sleep, this);
            }

            if (clock.isStopped())
            {
                clock.startPoller();
            }

            if (clock.isStarted())
            {
                obj.updateSwitchStatusRun("TRUE");
                if (obj.getAutoswitch().equals("TRUE"))
                {
                	//log start istanza
                    EventMgr.getInstance().info(new Integer(obj.getIdsite()), user, "switch",
                            "SW13", new Object[]{obj.getDescription(),"Autoswitch"});
                }
                else
                {
                //log start istanza
                EventMgr.getInstance().info(new Integer(obj.getIdsite()), user, "switch",
                        "SW03", new Object[]{obj.getDescription(),"Manual", obj.getManual_type()});
                }
            }
        }
        catch (Exception e)
        {
        	//log errore durante caricamento istanza switch x
        	EventMgr.getInstance().info(new Integer(obj.getIdsite()), user, "switch",
                    "SW05", obj.getDescription());
        }
    }

    public Switch stopSwitch(int idx,String user)
    {
    	return stopSwitch(idx,user,true);
    }
    public Switch stopSwitch(int idx, String user,boolean stopStatus)
    {
        Switch tmp = null;

        try
        {
            tmp = this.childs[idx - 1];
            this.childs[idx - 1] = null;

            boolean empty = true;

            for (int i = 0; i < childs.length; i++)
            {
                if (childs[i] != null)
                {
                    empty = false;

                    break;
                }
            }

            if(stopStatus)
            	tmp.updateSwitchStatusRun("FALSE");
            //log stop istanza di switch
            EventMgr.getInstance().error(new Integer(tmp.getIdsite()), user, "switch",
                    "SW04", tmp.getDescription());
            
            if (empty)
            {
            	/*   commento perch� log non richiesto
            	//log stop plugin switch
                EventMgr.getInstance().info(new Integer(tmp.getIdsite()), user, "switch",
                        "SW02",null);*/
                
            	clock.stopPoller();
                clock = null;
            }
        }
        catch (Exception e)
        {
        	/* commmento log non richiesto
        	//log errore durante stop istanza di switch
            EventMgr.getInstance().info(new Integer(tmp.getIdsite()), user, "switch",
                    "SW06", tmp.getDescription()); */
        }

        return tmp;
    }

    public Switch getSwitch(int idx)
    {
        Switch tmp = null;

        try
        {
            tmp = this.childs[idx];
        }
        catch (Exception e)
        {
        }

        return tmp;
    }

    public int getSwitchNumber()
    {
        return this.childs.length;
    }

    public long getSleep()
    {
        return sleep;
    }
}
