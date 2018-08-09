package com.carel.supervisor.dispatcher.engine.sound;

import java.util.Date;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.ProfileException;
import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.io.CioEVT;


public class DispWinAlarm
{
    private static DispWinAlarm winAlarm = new DispWinAlarm();
    private static final String CHANEL = "WEB";
    private static int START = 1;
    private static int STOP = 0;
    private int state = 0;
    private boolean isTest = false;
    private DispWindows win = null;

    private DispWinAlarm()
    {
        this.state = 0;
    }

    public static DispWinAlarm getInstance()
    {
        return winAlarm;
    }

    public boolean isActive()
    {
        boolean ret = true;

        switch (this.state)
        {
        case 0:
            ret = false;

            break;

        case 1:
            ret = true;

            break;
        }

        return ret;
    }

    public boolean startWinAlarm(int num, String soundPath, String l1, String l2, String l3,
        String l4)
    {
        boolean ris = false;

        try
        {
            if (!this.isActive())
            {
                this.win = new DispWindows(soundPath, l1, l2, l3, l4);
                this.win.dw_display();
                this.state = START;
            }

            if (this.win != null)
            {
                this.win.dw_setNumAl(num);
                ris = true;
            }
        }
        catch (Exception e)
        {
            this.state = STOP;

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return ris;
    }
    public boolean startWinAlarmTest(int num, String soundPath, String l1, String l2, String l3,
            String l4)
        {
            boolean ris = false;
            this.isTest = true;
            try
            {
                if (!this.isActive())
                {
                    this.win = new DispWindows(soundPath, l1, l2, l3, l4);
                    this.win.dw_display();
                    this.state = START;
                }

                if (this.win != null)
                {
                    this.win.dw_setNumAl(num);
                    ris = true;
                }
            }
            catch (Exception e)
            {
                this.state = STOP;

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }

            return ris;
        }
    public void stopWinAlarm()
    {
        if (this.win != null)
        {
            this.win.dw_close();
        }

        this.win = null;
        this.state = STOP;
    }

    public void checkUserAck(String login, String pass)
    {
        try
        {
            UserCredential userCredential = new UserCredential(login, pass, CHANEL);
            ProfilingMgr.getInstance().getUserProfile(userCredential);
            this.stopWinAlarm();

            Object[] obj = 
                {
                    login,
                    DateUtils.date2String(new Date(System.currentTimeMillis()),
                        "yyyy/MM/dd HH:mm:ss")
                };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Start",
                EventDictionary.TYPE_INFO, "D013", obj);
            if(isTest == true)
            {
            	CioEVT ioEvt = new CioEVT(-1);
            	ioEvt.setTestResult(true);
            }
        }
        catch (ProfileException pe)
        {
        }
        catch (Exception ex)
        {
        }
    }
}
