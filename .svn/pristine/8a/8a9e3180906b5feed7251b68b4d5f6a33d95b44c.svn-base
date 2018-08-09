package com.carel.supervisor.director;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.controller.LiveStatus;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.controller.rule.RuleMgr;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;
import com.carel.supervisor.dataaccess.dataconfig.ProductInfo;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.history.FDBQueue;
import com.carel.supervisor.dataaccess.history.Writer;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.field.FileMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;
import com.carel.supervisor.presentation.helper.Buzzer;

public class MainThread extends Poller
{
	private Logger logger = LoggerMgr.getLogger(this.getClass());
	private SetPollingVarList setPollingVarList = null;
	private int freqGCD = 0;
	private FDBQueue queue = null;
	private Writer writer = null;
	private boolean alive = false;
	private PolicyDequeue policy = null;
	private int countWriter = 0;
	private ILocalTimer pingTimer = TimerMgr.getTimer("[DIRECTOR][PING]", TimerMgr.HIGH);
	private ILocalTimer extractTimer = TimerMgr.getTimer("[DIRECTOR][EXTRACT]", TimerMgr.MEDIUM);
	private ILocalTimer evalTimer = TimerMgr.getTimer("[DIRECTOR][EVAL]", TimerMgr.HIGH);
	private ILocalTimer ruleTimer = TimerMgr.getTimer("[DIRECTOR][RULE]", TimerMgr.HIGH);
	private ILocalTimer schedulerTimer = TimerMgr.getTimer("[DIRECTOR][SCHEDULER]", TimerMgr.HIGH);
	private ILocalTimer dequeueTimer = TimerMgr.getTimer("[DIRECTOR][DEQUEUE]", TimerMgr.HIGH);
	private long counter = 0;
	private long lastWork = 0;
	private FunctionHistorical functionHistorical = FunctionHistorical.getInstance();
	private Map variableGuardian = null;
	private long lastCheck = 0;
	private boolean fhsfiles = false; //di default non vengono creati i files FHS
	
	private AlarmLedMgr alarmLedMgr = new AlarmLedMgr();
	private boolean ended; //finito thread

	// Identificativo progressivo per il processo MainThread
	private static int threadcounter = 0;
	
	public MainThread()
	{
		setName("MainThread_"+MainThread.threadcounter);
		MainThread.threadcounter++;
		ended=false;
	}

	public void init(SetPollingVarList setPollingVarList, int freqGCD, FDBQueue queue, Writer writer, PolicyDequeue policy)
	{
		this.setPollingVarList = setPollingVarList;
		this.freqGCD = (int) SystemConfMgr.getInstance().get("clockDirector").getValueNum();
		this.queue = queue;
		this.writer = writer;
		this.policy = policy;
		
		logger.info("*********************************************************************");
		logger.info("*********************************************************************");
		logger.info("*********              PLANTVISORPRO STARTED                *********");
		logger.info("*********************************************************************");
		logger.info("*********************************************************************");
		IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
		
		try
		{
			//p_info.load();
			String fhsyesno = p_info.get(ProductInfo.FHSFILES);
			
			if (fhsyesno != null)
			{
				//se è presente nel db, setto il valore letto in memoria:
				this.fhsfiles = ("yes".equals(fhsyesno));
			}
			else
			{
				//se non è presente nel db, salvo il valore di default nel db stesso:
				p_info.store(ProductInfo.FHSFILES, "no");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			this.fhsfiles = false;
		}
	}

	public boolean isAliveThread()
	{
		return alive;
	}

	public void stopThread()
	{
		MainThreadMonitor.getInstance().isRunning = false;
		alive = false;
	}
	
	public void stopAlrmLedMgr()
	{
		// STOP of AlarmCheck for External Led
		alarmLedMgr.stopCheckAlarms();
	}

	public long getLastWork()
	{
		return lastWork;
	}

	// Time scheduler is implemented using idvariable = 0 and evaluated each 30
	// seconds
	//
	private void execute(boolean isFirstTime) throws IOException
	{
		// ---------------------------------------
		// 0. Evaluate devices status
		// ---------------------------------------
		MainThreadMonitor.getInstance().start(MainThreadMonitor.PINGALL);
		pingTimer.start();
		DeviceStatusMgr.getInstance().pingAll();
		pingTimer.stop();
		MainThreadMonitor.getInstance().stop(MainThreadMonitor.PINGALL);

		// ------------------------------------------------------------------
		// 1. Extract variables that must be retrieved in this time
		// ------------------------------------------------------------------
		extractTimer.start();

		List<Variable> variables = null;

		variables = setPollingVarList.extract(freqGCD);

		extractTimer.stop();

		long time = System.currentTimeMillis();

		// The list is ordered: first physical variable, then logical
		Variable variable = null;
		evalTimer.start();
		MainThreadMonitor.getInstance().start(MainThreadMonitor.RETSAVEFIELD);
		for (int i = 0; i < variables.size(); i++)
		{
			variable = variables.get(i);

			// ------------------------------------------------------------------------
			// 2. Evaluate variables from field and save variables into queue
			// ------------------------------------------------------------------------
			variable.retrieveAndSaveValue(time, queue, isFirstTime);

			// Se è una funzione la loggo nel file di storico funzioni
			try
			{
				if (variable.getInfo().isLogic())
				{
					// Se è una variabile logica di allarme, allora la salvo
					// nella coda degli allarmi
					DataConnBase.saveAlarmGuardian(variable);

					// N.B.: x evitare file di grandi dim. su disco e spreco di cpu
					//generazione file "*.fhs" dipendente da flag nel db:
					if (this.fhsfiles)
						functionHistorical.write(time, variable);
				}
			} // try
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error(e);
			} // catch
		}
		MainThreadMonitor.getInstance().stop(MainThreadMonitor.RETSAVEFIELD);

		try
		{
			functionHistorical.flush();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		} 

		MainThreadMonitor.getInstance().start(MainThreadMonitor.GPCHKVAR);
		try
		{
			AlarmCtrl.getInstance().commitInsert();
		} catch (Exception e)
		{
			e.printStackTrace();			
			logger.error(e);
		}

		// Guardian vars are checked every 30 sec
		// TODO: R.F. consider increasing this period to 60'' or 120'' for performance improvement
		if ((System.currentTimeMillis() - lastCheck) >= 30000L)
		{
			try
			{
				if (null != variableGuardian)
				{
					lastCheck = System.currentTimeMillis();

					Iterator i = variableGuardian.keySet().iterator();
					Integer key = null;
					Variable var = null;

					while (i.hasNext())
					{
						key = (Integer) i.next();
						var = (Variable) variableGuardian.get(key);
						var.retrieveAndSaveValue(time, null, false);
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error(e);
			}
		}
		MainThreadMonitor.getInstance().stop(MainThreadMonitor.GPCHKVAR);
		evalTimer.stop();

		// -----------------------------------------------------------------------------------
		// 3. Find rules that must be evaluated (connected with the variables
		// find)
		// -----------------------------------------------------------------------------------
		MainThreadMonitor.getInstance().start(MainThreadMonitor.EVLALR);
		ruleTimer.start();
		
		RuleMgr ruleMgr = RuleMgr.getInstance();
		Rule rule = null;
		Timestamp now = new Timestamp(time);
		
		for (int i = 0; i < variables.size(); i++)
		{
			variable = variables.get(i);

			if (!variable.isDeviceDisabled()) // We don't evaluate rules if
			// the device is disabled
			{
				Integer id = variable.getInfo().getId();
				List rules = ruleMgr.getRulesList(id);

				if (null != rules)
				{
					for (int j = 0; j < rules.size(); j++)
					{
						rule = (Rule) rules.get(j);
						checkRule(rule, now, time);
					}
				}
			}
		}

		ruleTimer.stop();
		MainThreadMonitor.getInstance().stop(MainThreadMonitor.EVLALR);

		// -----------------------------------------------
		// 4. Special rules (only time scheduled)
		// -----------------------------------------------
		MainThreadMonitor.getInstance().start(MainThreadMonitor.EVLSCHD);
		schedulerTimer.start();

		Integer id = new Integer(0);

		if (ruleMgr.hasRules(id))
		{
			List rules = ruleMgr.getRulesList(id);

			if (null != rules)
			{
				for (int j = 0; j < rules.size(); j++)
				{
					rule = (Rule) rules.get(j);

					// 9. Evaluate rules for time scheduler
					checkRule(rule, now, time);
				}
			}
		}

		schedulerTimer.stop();
		MainThreadMonitor.getInstance().stop(MainThreadMonitor.EVLSCHD);

		MainThreadMonitor.getInstance().start(MainThreadMonitor.DEQUEUE);
		dequeue();
		MainThreadMonitor.getInstance().stop(MainThreadMonitor.DEQUEUE);
	}

	private void dequeue()
	{
		countWriter++;
		
		// datatransfer addon
		try
		{
			FileMgr.getInstance().submitToFile();
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		// datatransfer addon/
		
		// Se sono in emergenza
		if (policy.getThreasholdEmergency() <= queue.elementsSize())
		{
			if (countWriter >= policy.getEmergencyNumGCDWriter())
			{
				countWriter = 0;

				// --------------------------------
				// 5. Dequeue variables
				// --------------------------------
				dequeueTimer.start();

				try
				{
					writer.write(policy.getEmergencyNumDequeue());
				} catch (Exception e)
				{
					e.printStackTrace();
					logger.error(e);
				}

				dequeueTimer.stop();
			}
		} else
		// Non sono in emergenza
		{
			if (countWriter >= policy.getNormalNumGCDWriter())
			{
				countWriter = 0;

				// --------------------------------
				// 5. Dequeue variables
				// --------------------------------
				dequeueTimer.start();

				try
				{
					writer.write(policy.getNormalNumDequeue());
				} catch (Exception e)
				{
					e.printStackTrace();
					logger.error(e);
				}

				dequeueTimer.stop();
			}
		}
	}

	private void executeBeforeStop(int freqGCD)
	{
		try
		{
			Thread.sleep(((long) freqGCD * 1000));

			if (null != setPollingVarList) // Se è locale
			{
				List variables = setPollingVarList.extractBeforeStop(freqGCD);

				long time = System.currentTimeMillis();
				Variable variable = null;
				writer.writeAll();

				for (int i = 0; i < variables.size(); i++)
				{
					variable = (Variable) variables.get(i);
					variable.retrieveAndForceSaveValue(time, queue);
				} // for

				writer.writeAll();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
	}

	public void run()
	{
		logger.info(this.getName()+"[START]");
		long oldTime = System.currentTimeMillis();
		LiveStatus.update();

		int NUM = BaseConfig.getLiveTime() / freqGCD;
		NUM = NUM / 5;

		if (NUM == 0)
		{
			NUM = 3;
		}

		int i = 0;
		long l = 0;

		try
		{
			// Sleep per consentire il caricamento dei
			// dati
			Thread.sleep(4000);
		} catch (InterruptedException e)
		{
		}

		// RECOVERY
		try
		{
			RecoveryData recoveryData = new RecoveryData();
			recoveryData.startRecovery(RecoveryData.RECOVERY_NOREGULAR_STOP);
		} // try
		catch (Exception e1)
		{
			e1.printStackTrace();			
			logger.error("Problem in RecoveryData Class Exception " + e1);
		} // catch
		// RECOVERY
		
		logger.info("[FIELD STARTING ACQUISITION]");

		try
		{
			counter++;

			l = System.currentTimeMillis();
			lastWork = l;

			execute(true);

			lastWork = System.currentTimeMillis();

			long l1 = System.currentTimeMillis() - l;

			if (!isStopped())
			{
				long lDelta = (freqGCD * 1000) - l1;

				if (lDelta > 0)
				{
					Thread.sleep(lDelta);
				} 
				// if lDelta <= 0 the system is late!
				else {
					logger.warn("*** CST_WARN: MAIN THREAD IS RUNNING LATE. ARE SYS RESOURCES OK? CHECK GUARDIAN LOG ***" );
				}
			} 
			else
			{
				
				/*
				 * Messo controllo all'inizio del DO-WHILE
				 * Nel caso in cui il motore sia stato fermato in fase di INIT non entra nel ciclo ed
				 * esegue subito il metodo executeBeforeStop, uscendo dal metodo RUN terminando il thread. 
				 */
				//executeBeforeStop(freqGCD);
			}
			
			//load buzzer enabling status from database, and init InfoService
			Buzzer.init();
			
			// START of AlarmCheck for External Led
			alarmLedMgr.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		
		if (!isStopped())
		{
			do
			{
				try
				{
					// Salva il tempo ad ogni giro
					MainThreadMonitor.getInstance().setLoopTime();
					MainThreadMonitor.getInstance().isRunning = true;
					alive = true;
					l = System.currentTimeMillis();
	
					// Modifica dell'ora da parte dell'utente OR Passaggio all'ora
					// legale dall'ora solare e viceversa
	
					int timeCtrl=TimeControl.checkChangeClock(oldTime, l, freqGCD);
					if ( timeCtrl!=TimeControl.NO_CHANCHED )
					{
						writer.writeAll();
						setPollingVarList.changeTime(queue);
						writer.writeAll();
		
						try
						{
							RecoveryData recoveryData = new RecoveryData();
							switch(timeCtrl){
								case TimeControl.USER_CHANGE_TIME_FORWARD:
								case TimeControl.USER_CHANGE_TIME_BACK:	
									recoveryData.startRecovery(RecoveryData.RECOVERY_CHANGE_TIME_FORWARD);
								break;
							}//switch
						} // try
						catch (Exception e1)
						{
							e1.printStackTrace();
							logger.error("ERROR in RecoveryData Class Exception " + e1);
						} // catch
					
					} // if
					
					oldTime = l;
	
					lastWork = System.currentTimeMillis();
	
					execute(false);
	
					lastWork = System.currentTimeMillis();
	
					counter++;
	
					i++;
	
					if (i >= NUM)
					{
						LiveStatus.update();
						i = 0;
					}
	
					if (!isStopped())
					{
						long lDelta = ((freqGCD * 1000) - System.currentTimeMillis() + l);
	
						if (lDelta > 0)
						{
							Thread.sleep(lDelta);
						}
						// Nel caso lDelta sia minore di zero il sistema è in ritardo
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.error(e);
				}
			} while (!isStopped());
		}

		executeBeforeStop(freqGCD);

		logger.info("[DEVICES READING STOPPED]");
		
		
		// Nuova variabile di controllo per sincronizzare STOPeSTART del motore.
		ended = true;
		logger.info(this.getName()+"[STOP]");
		logger.info("*********************************************************************");
		logger.info("*********************************************************************");
		logger.info("*********            --STOPPING PlantVisorPRO--             *********");
		logger.info("*********************************************************************");
		logger.info("*********************************************************************");		
	}

	private void checkRule(Rule rule, Date now, long time)
	{
		try
		{
			rule.executeActions(time);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.fatal("MainThread. CHECK RULE ERROR", e);
		}
	}

	public void setVarGuardian(Map variableGuardian)
	{
		this.variableGuardian = variableGuardian;
	}

	public boolean ended() {
		return ended;
	}
}
