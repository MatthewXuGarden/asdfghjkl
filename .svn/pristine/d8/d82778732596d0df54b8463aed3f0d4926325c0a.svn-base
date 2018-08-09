package com.carel.supervisor.base.timer;

import java.util.Date;
import java.util.Properties;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;


public class TimerMgr extends InitializableBase implements ITimerController,
    IInitializable
{
    public static final TIMER_LEVEL_TYPE LOW = new TIMER_LEVEL_TYPE(3);
    public static final TIMER_LEVEL_TYPE MEDIUM = new TIMER_LEVEL_TYPE(2);
    public static final TIMER_LEVEL_TYPE HIGH = new TIMER_LEVEL_TYPE(1);
    private static TimerMgr meTimerMgr = new TimerMgr();
    private final static String NAME = "name";
    private final static String VALUE = "value";
    private final static String MANAGER = "manager";
    private final static String LEVEL = "level";
    private final static String AUTOSTART = "autostart";
    private boolean initialized = false;
    private ITimerMgr timerMgrImpl = null;
    private static final Logger logger = LoggerMgr.getLogger(TimerMgr.class);

    private TimerMgr()
    {
    }

    public static TimerMgr getInstance()
    {
        return meTimerMgr;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            Properties prop = retrieveProperties(xmlStatic, NAME, VALUE,
                    "BSSE0002");
            String className = retrieveAttribute(prop, MANAGER, "BSSE0002");
            String level = retrieveAttribute(prop, LEVEL, "BSSE0002");
            String autostart = retrieveAttribute(prop, AUTOSTART, "BSSE0002");

            try
            {
                timerMgrImpl = (ITimerMgr) FactoryObject.newInstance(className);
            }
            catch (Exception ex)
            {
                logger.error(ex);
                throw new InvalidConfigurationException("");
            }

            if (level.equalsIgnoreCase("low"))
            {
                setGlobalLevel(LOW);
            }
            else if (level.equalsIgnoreCase("medium"))
            {
                setGlobalLevel(MEDIUM);
            }
            else if (level.equalsIgnoreCase("high"))
            {
                setGlobalLevel(HIGH);
            }
            else
            {
                setGlobalLevel(HIGH);
            }

            if (autostart.equalsIgnoreCase("Y"))
            {
                activate();
            }
            else
            {
                deActivate();
            }

            initialized = true;
        }
    }

    public static final ILocalTimer getTimer(String name,
        TIMER_LEVEL_TYPE timerLevelType)
    {
        return meTimerMgr.timerMgrImpl.getTimer(name, timerLevelType);
    }

    public final ITimerController getController()
    {
        return timerMgrImpl;
    }

    public final ITimerContainer getContainer()
    {
        return timerMgrImpl.getContainer();
    }

    public static final double getPrecision()
    {
        long l = System.currentTimeMillis();

        while (System.currentTimeMillis() == l)
        {
        }

        return System.currentTimeMillis() - l;
    }

    public void setGlobalLevel(TIMER_LEVEL_TYPE timerLevelType)
    {
        timerMgrImpl.setGlobalLevel(timerLevelType);
    }

    public boolean isActivated()
    {
        return timerMgrImpl.isActivated();
    }

    public void activate()
    {
        timerMgrImpl.activate();
    }

    public Date getActivationTime()
    {
        return timerMgrImpl.getActivationTime();
    }

    public long getActivationPeriod()
    {
        return timerMgrImpl.getActivationPeriod();
    }

    public void deActivate()
    {
        timerMgrImpl.deActivate();
    }

    //Da implementare
    public void clear()
    {
        timerMgrImpl.clear();
    }

    public boolean checkCondition(TIMER_LEVEL_TYPE timerLevelType)
    {
        return timerMgrImpl.checkCondition(timerLevelType);
    }

    public static final class TIMER_LEVEL_TYPE
    {
        private int level = -1;

        private TIMER_LEVEL_TYPE(int levelPar)
        {
            level = levelPar;
        }

        public boolean equals(TIMER_LEVEL_TYPE timerLevelType)
        {
            return (timerLevelType.level == level);
        }
        

        @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + level;
			return result;
		}

		public boolean lessAndEqual(TIMER_LEVEL_TYPE timerLevelType)
        {
            return (timerLevelType.level >= level);
        }
    }
}
