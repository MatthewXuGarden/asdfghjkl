package com.carel.supervisor.director.test.operativitanormalea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.controller.rule.RuleMgr;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.history.FDBQueue;
import com.carel.supervisor.dataaccess.history.HistoryMgr;
import com.carel.supervisor.dataaccess.history.Writer;
import com.carel.supervisor.dataaccess.reorder.ReorderFrequency;
import com.carel.supervisor.dataaccess.reorder.ReorderInformation;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.director.SetPollingVarList;
import com.carel.supervisor.director.SetPollingVarListFactory;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;
import com.carel.supervisor.field.dataconn.DataCollector;


public class WriteDataTest extends Poller
{
    private static int NUM_QUEUE = 5000; //LDAC TO DO da emttere su file
    private static int NUM_REORDER_QUEUE = 100; //LDAC TO DO da emttere su file
    private static int NUMERO_DI_GIRI=1000;
    private static WriteDataTest me = new WriteDataTest();
    private SetPollingVarList setPollingVarList = null;
    private int freqGCD = 0;
    private Logger logger = null;
    private DataCollector dataCollector = null;
    private FDBQueue queue = new FDBQueue(NUM_QUEUE); //Coda che contiene elementi da scrivere nelle tabelle buffere ed hsvariable
    private Writer writer = null;
    private ReorderInformation reorderQueue = new ReorderInformation(NUM_REORDER_QUEUE);
    private ReorderFrequency reorderFrequency = null;
    private long debug = 0;

    //Time scheduler is implemented using idvariable = 0 and evaluated each 30 seconds
    //
    int conta = 0;

    private WriteDataTest()
    {
    }

    public static WriteDataTest getInstance()
    {
        return me;
    }

    public void loadConfiguration() throws Exception
    {
        HistoryMgr.getInstance().load();
        dataCollector = FieldConnectorMgr.getInstance().getDataCollector();

        SetPollingVarListFactory setPollingVarListFactory = new SetPollingVarListFactory();
        setPollingVarListFactory.init();

        ControllerMgr.getInstance().load();

        Map functions = ControllerMgr.getInstance().getFunctions();
        DeviceStatusMgr.getInstance().loadPhysic(dataCollector);
        DeviceStatusMgr.getInstance().loadLogic(functions);

        setPollingVarListFactory.assign(dataCollector, functions);

        logger = LoggerMgr.getLogger(this.getClass());
        setPollingVarList = setPollingVarListFactory.createSetPollingVarList();
        freqGCD = setPollingVarListFactory.getGCDFreq();

        writer = new Writer(queue);
        reorderFrequency = new ReorderFrequency(reorderQueue);
    }

    public void reloadConfiguration() throws Exception
    {
        VariableMgr.getInstance().clear();
        RuleMgr.getInstance().clear();
        ControllerMgr.getInstance().clear();

        DataConfigMgr.getInstance().reload();

        SetPollingVarListFactory setPollingVarListFactory = new SetPollingVarListFactory();
        setPollingVarListFactory.init();
        ControllerMgr.getInstance().load();

        Map functions = ControllerMgr.getInstance().getFunctions();
        DeviceStatusMgr.getInstance().loadPhysic(dataCollector);
        DeviceStatusMgr.getInstance().loadLogic(functions);

        setPollingVarListFactory.assign(dataCollector, functions);

        setPollingVarList = setPollingVarListFactory.createSetPollingVarList();
        freqGCD = setPollingVarListFactory.getGCDFreq();
    }

    public ReorderInformation getReorderInformation()
    {
        return reorderQueue;
    }

    private void execute()
    {
        //---------------------------------------
        //    	0.  Evaluate devices status
        //---------------------------------------
        DeviceStatusMgr.getInstance().pingAll();

        //------------------------------------------------------------------
        //      1.  Extract variables that must be retrieved in this time
        //------------------------------------------------------------------
        List variables = setPollingVarList.extract(freqGCD);
        long time = System.currentTimeMillis();

        //The list is ordered: first physical variable, then logical
        Variable variable = null;

        for (int i = 0; i < variables.size(); i++)
        {
            variable = (Variable) variables.get(i);

            //------------------------------------------------------------------------
            //      2.  Evaluate variables from field and save variables into queue
            //------------------------------------------------------------------------
            variable.retrieveAndSaveValue(time, queue,false);
        }

        RuleMgr ruleMgr = RuleMgr.getInstance();
        Rule rule = null;
        Timestamp now = new Timestamp(time);

        for (int i = 0; i < variables.size(); i++)
        {
            variable = (Variable) variables.get(i);

            if (!variable.isDeviceDisabled()) //We  don't evaluate rules if the device is disabled
            {
                Integer id = variable.getInfo().getId();
                List rules = ruleMgr.getRulesList(id);
                if (null != rules)
                {
                    for (int j = 0; j < rules.size(); j++)
                    {
                        rule = (Rule)rules.get(j);
                        checkRule(rule, now, time);
                    }
                }
            }
        }

        Integer id = new Integer(0);

        if (ruleMgr.hasRules(id))
        {
            List rules = ruleMgr.getRulesList(id);
            if (null != rules)
            {
                for (int j = 0; j < rules.size(); j++)
                {
                    rule = (Rule)rules.get(j);
//                  9.  Evaluate rules for time scheduler
                    checkRule(rule, now, time);
                }
            }   
        }

        try
        {
            writer.write(20);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (isStopped())
            executeBeforeStop();
    }

    private void executeBeforeStop()
    {
        try
        {
            writer.writeAll(); //LDAC TO DO :Dump anche dei dati che non sarebbero variati all'interno di DataDynamicSaveMember
            checkChangeFrequency();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void checkChangeFrequency() throws Exception
    {
        if (reorderQueue.elementsSize() != 0)
            reorderFrequency.startReorderHistorical();
    } //checkChangeFrequency

    public void executeLoop()
    {
        try
        {
            //Thread.sleep(60000); //Sleep per consentire il caricamento dei dati
            Thread.sleep(4000);
        }
        catch (InterruptedException e)
        {
        }

        logger.info("[FIELD STARTING ACQUISITION]");

        ILocalTimer localTimerTot = TimerMgr.getTimer("[FIELD][ACQUISIZIONE TOTALE]",
                TimerMgr.MEDIUM);

        try
        {
            //        	Startup: leggo tutte le variabili di tutti i dispositivi
            long l = System.currentTimeMillis();

            execute();

            long l1 = System.currentTimeMillis() - l;

            if (!isStopped())
            {
                long lDelta = (freqGCD * 1000) - l1;

                if (lDelta > 0)
                {
                    Thread.sleep(lDelta);
                } //if

                //Altrimenti sono già in ritardo!!!!!!!!!!
            }
            else
                executeBeforeStop();

            int sleeping = 0;

            do
            {
                l = System.currentTimeMillis();
                localTimerTot.start();
                sleeping++;

                execute();
                System.out.println("Ciclo:" + (++conta));
                localTimerTot.stop();

                if (!isStopped())
                {
                    //-1 msec perdo 0.8 msec ogni giro
                    long lDelta = ((freqGCD * 1000) -
                        System.currentTimeMillis() + l) - 1;

                    if (lDelta > 0)
                    {
                        Thread.sleep(30); //lDelta);
                    } //if

                    // sono già in ritardo!!!!!!!!!!
                }
                else
                    executeBeforeStop();

                debug++;

                if (debug == NUMERO_DI_GIRI)
                    System.exit(1);
            }
            while (!isStopped());
        }
        catch (InterruptedException eInt)
        {
        }

        logger.info("[FIELD ACQUISITION STOPPED]");
    }

    private void checkRule(Rule rule, Date now, long time)
    {
    	try
        {
            rule.executeActions(time);
        }
        catch (Exception e) //Gestire meglio
        {
            logger.fatal("ERRORE CODE", e);
        }
    }

    private static void request() throws Exception
    {
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader myInput = new BufferedReader(reader);
        String str = new String();

        try
        {
            System.out.print(
                "Sto per cancellare tutto lo storico e riorganizzare il buffer y\\n? ");
            str = myInput.readLine();

            if (str.equals("y") || str.equals("Y"))
            {
                Connection connection = DatabaseMgr.getInstance().getConnection(null);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "delete from hsvariable");
                preparedStatement.execute();
                preparedStatement = connection.prepareStatement(
                        "update buffer set keyactual=-1,isturn=false");
                preparedStatement.execute();
                preparedStatement.close();
                connection.commit();
                connection.close();
                System.out.print("Stato DB OK\n");
               
            } //if
          
            System.out.print(
            "Settare numero di valori ");
            str = myInput.readLine();
            NUMERO_DI_GIRI=Integer.parseInt(str);
            
        } //try 
        catch (IOException e)
        {
            System.out.println("Error: " + e);
            System.exit(-1);
        } //catch
    } //request

    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();
        request();

        WriteDataTest mainthread = WriteDataTest.getInstance();
        mainthread.startPoller();
        mainthread.loadConfiguration();
        mainthread.executeLoop();
    }
}
