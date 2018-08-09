package com.carel.supervisor.plugin.parameters;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEvent;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEventsList;

public class ParametersThread extends Poller {

	private boolean corri = false;
	private boolean configChanging = false;

	public ParametersThread()
	{
		setName("ParametersThread");
	}
	
	@SuppressWarnings("static-access")
	public void run() {

		// se il plugin non è abilitato non faccio nulla!
		if (!ParametersMgr.getParametersCFG().getEnabled())
			return;

		setCorri(true);

		try {
			// aspetto un po' prima di partire, per risolvere eventuali problemi
			// di concorrenza con altri thread
			// fratelli nel caso di riavvio del plugin
			sleep(10000);
		} catch (InterruptedException e1) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e1);
		}

		// init del thread
		long interval = ParametersMgr.getInstance().getParametersCFG()
				.getCheckinterval();

		// finchè devo andare.. vado
		while (getCorri()) {
			if (interval > 0) {
				// inizio nuovo ciclo di controllo
				// e recupero la lista delle variabili da controllare
				int[] param = ParametersMgr.getInstance().getParametersPhoto()
						.getVariableList();

				// se c'è roba da controllare..
				if ((param != null) && (param.length > 0)) {
					// calcolo quanto dormire (suddivido il tempo totale tra le
					// varie variabili e cos� ne controllo una ogni tanto)
					long toSleep = (long) (interval * 1000 * 60)
							/ (param.length + 1);
					
					ParametersMgr.intControl=(int) (toSleep /1000);
					for (int i = 0; i < param.length; i++) {

						// qui, ogni tanto, leggo i parametri ed eventualmente
						// loggo le modifiche
						try {
							// Leggo il valore attuale e poi il ParametersMgr lo
							// confronta con la foto e agisce di conseguenza
							Variable v = ControllerMgr.getInstance()
									.getFromField(param[i]);
							float currentValue = v.getCurrentValue();

							// il metodo controlla il nuovo valore e se serve
							// logga le modifiche (inviando la notifica se non c'� l'aggregazione)
							ParametersMgr.getInstance().checkVariationAndLog(
									param[i], currentValue, null, !ParametersMgr.getParametersCFG().getAggregatenotification() );
						} catch (Exception e1) {
							Logger logger = LoggerMgr
									.getLogger(this.getClass());
							logger.error(e1);
						}

						if (getCorri()) {
							// controllo sempre se il thread deve continuare a
							// girare

							try {
								sleep(toSleep);
							} catch (InterruptedException e) {
								// se mi interrompono, il thread deve spegnersi
								// subito...
								setCorri(false);
								break;
							}

						} else {
							setCorri(false);
							break;
						}
					}
				}// Finito il giro di controllo
				else {
					// se non ci sono parametri da controllare, ... dormo e
					// basta!
					try {
						sleep(interval);
					} catch (InterruptedException e) {
						setCorri(false);
						break;
					}
				}

				// if (getCorri()) //commentata perchè così invio le notifiche
				// anche se il thread è stato interrotto
				
			
				// avoid sending notification in case of plugin 'reboot' (start + stop) due to the 
				// plugin configuration changing (or receiver changing)
				if(!configChanging)
				{
					// Notifica delle modifiche!!! (se presenti)
	
					try {
						// carico gli eventi da notificare (in lingua di default)
						String language = LangUsedBeanList.getDefaultLanguage(1);
						ParametersEventsList pel = new ParametersEventsList(
								language, null, 1, -1, true, -1);
	
						for (int j = 0; j < pel.size(); j++) {
							ParametersEvent pe = pel.getByPosition(j);
	
							// notifico l'evento
							try {
								ParametersMgr.getInstance().notifyEvent(pe);
							} catch (DataBaseException e) {
								Logger logger = LoggerMgr
										.getLogger(this.getClass());
								logger.error(e);
							}
	
						}
	
					} catch (DataBaseException e) {
						Logger logger = LoggerMgr.getLogger(this.getClass());
						logger.error(e);
	
					}
				}
			} //end if intervalllo <>0
			else {
				// se non c'è nessun intervallo dormo 10 secondi, poi
				// ricontrollo
				try {
					if (getCorri())
						sleep(10000);
					else
						break;
				} catch (InterruptedException e) {
					setCorri(false);
					break;
				}
			}
		}// end while corri

	}

	public boolean isRunning() {
		return getCorri();
	}

	public void stopThread() {
		// se fermo il thread, prima segnalo col setCorri(false), poi stoppo il
		// thread che si fermerà...
		setCorri(false);
		this.interrupt();
	}

	public void setCfgChanging(boolean cfgChanging)
	{
		configChanging = cfgChanging; 
	}
	
	private synchronized void setCorri(boolean b) {
		corri = b;
	}

	private synchronized boolean getCorri() {
		return corri;
	}



}
