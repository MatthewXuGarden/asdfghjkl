package com.carel.supervisor.presentation.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jfree.data.general.DefaultPieDataset;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.bo.master.IMaster;


public class UserTransaction extends Transaction
{
    private int idTrx = 0;
    private String nameTrx = "";
    private String labelTrx = "";
    private String tabToLoad = "";
    private boolean isCurrent = false;
    private transient List listDevices = null;
    private transient Map descrDevices = null;
    private transient List listDevicesCombo = null;
    private transient Map descrDevicesCombo = null;
    private transient Properties properties = null;
    private transient IMaster boTrx = null;
    private transient UserTransaction childTrx = null;
    private boolean protectedTab = false;
    
    private transient IMaster boCustom = null;
    private String trxForUseBoCustom = "";
    private String tabForUseBoCustom = "";
	private HashMap<String, Object> attributes;

    public UserTransaction(String nameTrx)
    {
        this.idTrx = 1;
        this.nameTrx = nameTrx;
        this.isCurrent = true;
        this.listDevices = new ArrayList();
        this.descrDevices = new HashMap();
        this.listDevicesCombo = new ArrayList();
        this.descrDevicesCombo = new HashMap();
        this.properties = new Properties();
        this.childTrx = null;
        this.tabToLoad = "-1";
    }

    public UserTransaction(String nameTrx, String labelTrx)
    {
        this(nameTrx);

        /*try
        {
            byte[] utf8 = labelTrx.getBytes();

            // Convert from UTF-8 to Unicode
            this.labelTrx = new String(utf8, "UTF-8");
        }
        catch (Exception e)
        {*/
        this.labelTrx = labelTrx;

        //}
        attributes = new HashMap<String, Object>();
    }

    /*
     * Tab to load function - START
     */
    public void setTabToLoad(String t)
    {
        this.tabToLoad = t;
    }

    public String getTabToLoad()
    {
        return this.tabToLoad;
    }

    public void resetTabToLoad()
    {
        this.tabToLoad = "-1";
    }

    // END
    public void setBoTrx(IMaster bo)
    {
        this.boTrx = bo;
    }

    public IMaster getBoTrx()
    {
        return this.boTrx;
    }

    public boolean hasBoTrx()
    {
        return this.boTrx != null;
    }

    public void setTrxId(int i)
    {
        this.idTrx = i;
    }

    public int getTrxId()
    {
        return this.idTrx;
    }

    public String getTrxName()
    {
        return this.nameTrx;
    }

    public void setTrxName(String name)
    {
        this.nameTrx = name;
    }

    public String getTrxLabel()
    {
        return this.labelTrx;
    }

    public void setTrxLabel(String label)
    {
        this.labelTrx = label;
    }

    public boolean isCurrentTrx()
    {
        return this.isCurrent;
    }

    public boolean isTabProtected()
    {
        return protectedTab;
    }

    /**
         * @param protectedTab
         */
    public void setProtectedTab(boolean protectedTab)
    {
        this.protectedTab = protectedTab;
    }

    public String getCurrentTab()
    {
        String curTabName = this.getProperty("curTab");

        try
        {
            if (((curTabName == null) || (curTabName.length() == 0)))
            {
                if (hasBoTrx())
                {
                    curTabName = getBoTrx().getDefaulTabName();
                }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return curTabName;
    }

    public String remProperty(String sKey)
    {
        String ret = (String) this.properties.remove(sKey);

        if (ret == null)
        {
            ret = "";
        }

        return ret;
    }

    public String getProperty(String sKey)
    {
        String ret = this.properties.getProperty(sKey);

        if (ret == null)
        {
            ret = "";
        }

        return ret;
    }
    
    public Object getObjProperty(String sKey)
    {
        return this.properties.get(sKey);
    }

    public void setObjProperty(String key,Object obj)
    {
        this.properties.put(key,obj);
    }
    
    public Properties getProperties()
    {
        return this.properties;
    }

    public void setProperty(String key, String value)
    {
        this.properties.put(key, value);
    }

    public void setProperties(Properties prop)
    {
        this.properties = prop;
    }

    public void setIdDevices(int[] idDevices)
    {
        listDevices.clear();
        descrDevices.clear();
        for (int i = 0; i < idDevices.length; i++)
        {
            listDevices.add(new Integer(idDevices[i]));
            descrDevices.put(new Integer(idDevices[i]),"");
        }
    }
    
    public void setIdDevicesCombo(int[] idDevices)
    {
    	listDevicesCombo.clear();
        descrDevicesCombo.clear();
        for (int i = 0; i < idDevices.length; i++)
        {
        	listDevicesCombo.add(new Integer(idDevices[i]));
            descrDevicesCombo.put(new Integer(idDevices[i]),"");
        }
    }
    
    public int[] getIdDevicesCombo()
    {
        int[] retDev = new int[this.listDevicesCombo.size()];

        for(int i = 0; i < listDevicesCombo.size(); i++)
        {
        	retDev[i] = ((Integer)listDevicesCombo.get(i)).intValue();
        }

        return retDev;
    }
    
    

    public void setIdDevices(int[] idDevices, String[] description)
        throws Exception
    {
        listDevices.clear();
        descrDevices.clear();
        if (idDevices.length == description.length)
        {
            throw new Exception("device.length mismatch description.length");
        }

        for (int i = 0; i < idDevices.length; i++)
        {
        	listDevices.add(new Integer(idDevices[i]));
        	descrDevices.put(new Integer(idDevices[i]), description[i]);
        }
    }

    public int[] getIdDevices()
    {
        int[] retDev = new int[this.listDevices.size()];

        for(int i = 0; i < listDevices.size(); i++)
        {
        	retDev[i] = ((Integer)listDevices.get(i)).intValue();
        }

        return retDev;
    }

    public void addChild(UserTransaction trx)
    {
        trx.setTrxId(getTrxId() + 1);

        if (hasChild())
        {
            getChild().addChild(trx);
        }
        else
        {
            this.childTrx = trx;
        }
    }

    public UserTransaction getChild()
    {
        return this.childTrx;
    }

    public void removeChild()
    {
        this.childTrx = null;
    }

    public boolean hasChild()
    {
        return this.childTrx != null;
    }
    
    public void setBoCustom(IMaster bo,String folder,String tab)
    {
    	this.boCustom = bo;
    	this.trxForUseBoCustom = folder;
    	this.tabForUseBoCustom = tab;
    }
    
    public boolean canUseCustomBo()
    {
    	String folder = this.properties.getProperty("folder");
    	String resour = this.properties.getProperty("resource");
    	
    	if(this.trxForUseBoCustom.equalsIgnoreCase(folder) && this.boCustom != null &&
    	   (this.tabForUseBoCustom.equalsIgnoreCase(resour) || resour == null))
    	{
    		return true;
    	}
    	else
    		return false;
    }
    
    public IMaster getCustomBo() {
    	return this.boCustom;
    }

	public Object getAttribute(String id) {
		return attributes.get(id);
	}

	public void setAttribute(String id, Object obj) {
		attributes.put(id, obj);
	}

	public Object removeAttribute(String id) {
		return attributes.remove(id);
	}
}
