package com.carel.supervisor.plugin.switchtech;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.dataaccess.event.EventMgr;

import java.sql.Timestamp;


public class SwitchClock extends Poller
{
    private long sleep = 60000L;
    private SwitchMgr myManager = null;

    public SwitchClock(long slp, SwitchMgr mgr)
    {
    	setName("SwitchClock");
        sleep = slp;
        this.myManager = mgr;
    }

    public void run()
    {
    	try {
			Thread.sleep(60000);
			LoggerMgr.getLogger(this.getClass()).info(this.getClass()+" plugin start");
		} catch (InterruptedException e1) {
			LoggerMgr.getLogger(this.getClass()).error(e1);
		}
        while (this.isStarted())
        {
            try
            {
                for (int i = 0; i < myManager.getSwitchNumber(); i++)
                    manageSwitch(myManager.getSwitch(i));
            }
            catch (Exception e)
            {
            	LoggerMgr.getLogger(this.getClass()).error(e);
            }

            // Sleep
            try
            {
                Thread.sleep(this.sleep);
            }
            catch (InterruptedException e)
            {
            	LoggerMgr.getLogger(this.getClass()).error(e);
            }
        }
    }

    private void manageSwitch(Switch sw_instance) throws Exception
    {
        if (sw_instance != null)
        {
            //cariacamento dati switch, fatto solo all'avvio del processo
            if (!sw_instance.isSw_loaded())
            {
                sw_instance.reload(sw_instance.getIdsite(),
                    sw_instance.getIdswitch());
                sw_instance.initializeSwitchVariable();
                sw_instance.initializeSwitchAlarms();
                sw_instance.setSw_loaded(true);
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());

            //##########    AUTOSWITCH     #############
            if (sw_instance.getAutoswitch().equalsIgnoreCase("TRUE") &&
                    (now.after(sw_instance.getStarthour())))
            {
                String now_tech = sw_instance.getNowTecnologyByClock(now);

                //inizializzazione autoswitch, va fatta una sola volta,valida fino stop switch 
                if (!sw_instance.isStart_autoswitch())
                {
                    sw_instance.startAutoswitch();
                }

                if (sw_instance.isForce_tev())
                {
                    //setto stato corrente che sar� TEV
                    sw_instance.setSwitchValues("tev",
                        sw_instance.isFirst_set_after_change());

                    //verificare se sono ancora in stato di allarme
                    sw_instance.isOnAlarmStatus();
                }
                else
                {
                    if (sw_instance.isOnAlarmStatus()) //STATO ALLARME
                    {
                        if ((sw_instance.getTime_alarm_counter() -
                                sw_instance.getWait_time()) < (SwitchMgr.getInstance()
                                                                            .getSleep() / 1000))
                        {
                            //cambio stato attuale -> tev
                            sw_instance.changeTech("tev");

                            //LOG cambio tecnologia su allarme
                            EventMgr.getInstance().info(new Integer(
                                    sw_instance.getIdsite()), "System",
                                "switch", "SW10",
                                new Object[] { sw_instance.getDescription() });
                        }

                        //set variabili tev
                        sw_instance.setSwitchValues("tev",
                            sw_instance.isFirst_set_after_change());

                        //if autoresume
                        if (sw_instance.getAutoresume().equalsIgnoreCase("TRUE"))
                        {
                            if (sw_instance.isTimeToChangeTech(now))
                            {
                                //cambio tecnologia
                                sw_instance.changeTech();
                            }
                        }
                        else
                        {
                            sw_instance.setForce_tev(true);

                            //Log forza a tev quindi in rosso
                            EventMgr.getInstance().error(new Integer(
                                    sw_instance.getIdsite()), "System",
                                "switch", "SW18",
                                new Object[] { sw_instance.getDescription() });
                        }
                    } //end stato di allarme
                    else //STATO NORMALE
                    {
                        String tmp = sw_instance.getSt_type(); //salvo stato prima switch x log

                        //se � l'ora di cambiare tecnologia
                        if (sw_instance.isTimeToChangeTech(now))
                        {
                            sw_instance.changeTech(now_tech);

                            //LOG cambio tecnologia programmato
                            EventMgr.getInstance().info(new Integer(
                                    sw_instance.getIdsite()), "System",
                                "switch", "SW09",
                                new Object[]
                                {
                                    sw_instance.getDescription(), tmp,
                                    sw_instance.getSt_type(),
                                });
                        }
                        else if (!tmp.equalsIgnoreCase(now_tech)) //non � ora di cambiare tecnologia, ma sono nella tech sbagliata x rientro da allarme
                        {
                            sw_instance.changeTech(now_tech);

                            //log ritorno a autoswitch dopo allarme
                            EventMgr.getInstance().info(new Integer(
                                    sw_instance.getIdsite()), "System",
                                "switch", "SW19",
                                new Object[] { sw_instance.getDescription() });
                        }

                        //set variabili 
                        sw_instance.setSwitchValues(sw_instance.getSt_type(),
                            sw_instance.isFirst_set_after_change());
                    } //end stato normale

                    sw_instance.setStart_autoswitch(true);
                } //end caso non forzato a TEV
            } //end autoswitch

            //##########    MANUALSWITCH     #############
            else
            {
                if (sw_instance.isOnAlarmStatus()) //STATO ALLARME
                {
                    //forzare TEV
                    if ((sw_instance.getTime_alarm_counter() -
                            sw_instance.getWait_time()) < (SwitchMgr.getInstance()
                                                                        .getSleep() / 1000))
                    {
                        sw_instance.changeTech("tev");

                        //LOG cambio tecnologia su allarme
                        EventMgr.getInstance().info(new Integer(
                                sw_instance.getIdsite()), "System", "switch",
                            "SW10",
                            new Object[] { sw_instance.getDescription() });
                    }

                    //set var con valori TEV
                    sw_instance.setSwitchValues("tev",
                        sw_instance.isFirst_set_after_change());

                    if (sw_instance.getAutoresume().equalsIgnoreCase("FALSE") &&
                            !sw_instance.isForce_tev())
                    {
                        sw_instance.setForce_tev(true);
                        EventMgr.getInstance().error(new Integer(
                                sw_instance.getIdsite()), "System", "switch",
                            "SW18",
                            new Object[] { sw_instance.getDescription() });
                    }
                }
                else //STATO NORMALE
                {
                    if (sw_instance.getAutoresume().equalsIgnoreCase("TRUE"))
                    {
                        //set variabili con tecnologia settata
                        if (!sw_instance.getSt_type().equalsIgnoreCase(sw_instance.getManual_type()))
                        {
                            sw_instance.changeTech();

                            //eventuale log cambio tech x rientro allarme
                        }

                        sw_instance.setSwitchValues(sw_instance.getManual_type(),
                            sw_instance.isFirst_set_after_change());
                    }
                    else
                    {
                        if (sw_instance.isForce_tev())
                        {
                            sw_instance.setSwitchValues("tev",
                                sw_instance.isFirst_set_after_change());
                        }
                        else
                        {
                            //set variabili tecnologia corrente
                            sw_instance.setSwitchValues(sw_instance.getSt_type(),
                                sw_instance.isFirst_set_after_change());
                        }
                    }
                }
            }
        }
    }
}
