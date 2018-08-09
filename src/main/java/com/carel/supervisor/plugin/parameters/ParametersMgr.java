package com.carel.supervisor.plugin.parameters;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.OnLineCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersCFG;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEvent;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersUtils;
import com.carel.supervisor.presentation.session.UserSession;


public class ParametersMgr {
	//debug
	public static int intControl=0;
	
	
	
	private static ParametersMgr me;
	private boolean imrunning=false;
	private ParametersThread pt;
	private static ParametersCFG parametersCFG;
	private static ParametersPhoto parametersPhoto;
	private static Properties properties;
	public static final long defaultCheckIntervall = 120;
	
	public static final String EmailCode = "EX";
	public static final String FaxCode = "FX";
	public static final String SmsCode = "SX";
	
	public static ArrayList<Integer> actionIds= null;
	public static Integer startingActionId=null;
	public static String queryPerStartingId="";
	
	private final static String defaultUsername ="System"; 
	
	public final static String FLOAT_NAN_REPLACER="..."; 

	//Codici degli eventi
	public final static String MODIFYDBCODE			="MOD"; 
	public final static String TAKENPHOTOGRAPHYCODE	="PHO"; 
	public final static String ROLLBACKCODE 		="ROL"; 

	public static boolean isPluginRegistred (){
		return true;
	}
	
	public static ParametersMgr getInstance() {
		if (me == null)
		{
			queryPerStartingId="select idaction from cfaction where actiontype in ('"+ParametersMgr.EmailCode+"','"+ParametersMgr.FaxCode+"','"+ParametersMgr.SmsCode+"')";
			me = new ParametersMgr();
			
			try {
				parametersCFG = new ParametersCFG();
				parametersPhoto = new ParametersPhoto();
			} catch (DataBaseException e) {
				Logger logger = LoggerMgr.getLogger(ParametersMgr.class);
				logger.error(e);
			}
		}
		return me;
	}

	public void start() throws DataBaseException{
		
		//se il plugin non è abilitato non faccio nulla!
		if (!ParametersMgr.getParametersCFG().getEnabled())
			return;

		actionIds = new ArrayList<Integer>();
		
		ParametersCFG pcfg = new ParametersCFG();
		
		if (pcfg.getEnabled())
		{
			imrunning=true;
			if (pt==null)
				pt = new ParametersThread();
			else{
				//se avvio ma sta ancora andando, fermo e riparto
				pt.stopThread();
				pt=null;
				pt = new ParametersThread();
			}
			//all'avvio ricarico le configurazioni
			parametersCFG = new ParametersCFG();
			parametersPhoto = new ParametersPhoto();
			
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, queryPerStartingId);
			for(int i = 0; i<rs.size(); i++)
			{
				actionIds.add((Integer) rs.get(i).get(0));
			}
						
			pt.start();
			
		}
	}
	
	public void stop()
	{
		if (!ParametersMgr.getParametersCFG().getEnabled())
			return;
		imrunning=false;
		if (pt!=null) 
		{
			pt.stopThread();
			pt=null;
		}

	}
	
	public void manualStop()
	{
		if (!ParametersMgr.getParametersCFG().getEnabled())
			return;
		imrunning=false;
		if (pt!=null) 
		{
			pt.stopThread();
			pt=null;
		}
	}
	
	public void restart(){
		if (!ParametersMgr.getParametersCFG().getEnabled())
			return;
		stop();
		
		try {
			start();
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
	}
	
	public void setCfgChanging(boolean cfgChanging)
	{
		if (pt!=null)
			pt.setCfgChanging(cfgChanging);
	}
	
	public void reloadCFG(){
		try {
			//impongo un reload delle configurazioni
			parametersCFG = new ParametersCFG();
			parametersPhoto = new ParametersPhoto();
			
			actionIds.clear();
			
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, queryPerStartingId);
			for(int i = 0; i<rs.size(); i++)
			{
				actionIds.add((Integer) rs.get(i).get(0));
			}
			
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	public static boolean isMotorStopped(){
		boolean r = false;
		if (isPluginRegistred()){
			if (ParametersMgr.getParametersCFG().getEnabled()){
				if (!ParametersMgr.getInstance().isRunning()){
					// se il plugin � registrato, � abilitato ma il motore non gira, allora ritorno true
					r=true;
				}
			}
		}
		return r;
	}

	public void checkVariationAndLog(int idvariable, float currentValue, String user) throws DataBaseException{
		checkVariationAndLog(idvariable, currentValue, user, !parametersCFG.getAggregatenotification(),false) ;
	}
	
	public void checkVariationAndLog(int idvariable, float currentValue, String user, Boolean notifyImmediatly) throws DataBaseException{
		checkVariationAndLog(idvariable, currentValue, user, notifyImmediatly, false) ;
	}
	
	@SuppressWarnings("static-access")
	public void checkVariationAndLog(int idvariable, float currentValue, String user, Boolean notifyImmediatly, Boolean isRollback) throws DataBaseException{
		// controllo se ci sono state variazioni di valore di una certa variabile
		
		//controllo che la variabile faccia parte di quelle da controllare
		int trovato = Arrays.binarySearch(getParametersPhoto().getVariableList() , idvariable);
		
		if (trovato>-1)
		{
			Integer idprofile = null;
			
			//recupero nome utente e profilo di chi ha fatto le modifiche
			//se utente = null -> modifica da tastiera dispositivo
			String nomeutente;
			boolean auth;
			if (user !=null)
			{
				//Se il nome utente è != null allora utente reale o di sistema (non è modifica da tastierino dispositivo)
				
				//recupero il profilo (gli utenti di sistema non hanno profilo)
				idprofile = ParametersUtils.getProfileId(user);
				nomeutente=user;
				
				if (idprofile==null)
				{
					// Utente di Sistema -> Sempre autorizzato
					auth = true;
				}
				else
				{
					//Utente reale -> Verifico se fa parte degli utenti da non loggare
					if (ParametersMgr.getInstance().getParametersCFG().isProfileAuthorized(idprofile))
					{
						auth = true;
					}
					else
					{
						auth = false;
					}
				}
			}
			else 
				{ 
				// se user==null -> modifica da tastierino dispositivo e quindi da segnalare sempre
				auth=false;
				
				//il nome da usare per modifiche da tastierino è quello configurato nel plugin
				nomeutente = ParametersMgr.getParametersCFG().getUserTastiera();
				}
			
				//recupero ultimo valore fotografato
				float photoValue = ParametersMgr.getInstance().getParametersPhoto().getParameter(idvariable);
				
				//se la foto e il valore attuale sono buoni entrambi procedo
				//  (così evito controlli su prima fotografia e su dispositivi andati offline)
				if ( !Float.isNaN(currentValue))
				{
					//se il valore è cambiato...
					if (currentValue!=photoValue){
						
						// ...e se l'utente non è autorizzato...
						// ...e se il vecchio valore era valido..
						// ...oppure se è un Rollback (che loggo SEMPRE negli eventi)
						if ((!auth && !Float.isNaN(photoValue))||(isRollback)) 
						{
							Integer id = SeqMgr.getInstance().next(null, "parameters_events", "id");
							ParametersEvent pe = new ParametersEvent(
								id,											//nuovo ID tabella eventi
								new Timestamp(System.currentTimeMillis()),	//Timestamp in cui ci si � accorti della modifica
								new Integer(1), 							//id del sito (sempre 1)
								new Integer(idvariable), 					//id variabile modificata
								isRollback ? ROLLBACKCODE: MODIFYDBCODE ,	//codice dell'evento (MOD = Modifica)
								nomeutente, 								//utente che ha modificato il parametro
								new Boolean(true),							//True = evento da notiifcare
								isRollback 	? new Boolean(false) 
											: new Boolean(true),			//Evento su cui si pu� fare rollback
								photoValue, 								//vecchio valore della foto del parametro
								currentValue								//nuovo valore del parametro
								);
							
							//salvo l'evento!
							pe.save();
							
							if (notifyImmediatly){
								//se selezionato, invio immediatamente la notifica al Dispatcher
								notifyEvent(pe);
							}
						}
						
						//e alla fine aggiorno la foto del sistema
						ParametersMgr.getInstance().getParametersPhoto().setParameter(idvariable, currentValue);
						
					}
				}
		}
	}
	
	public void notifyEvent(ParametersEvent pe) throws DataBaseException {
		if(actionIds.size() == 0) return;
		
		pe.notified();

		for(int i=0; i<actionIds.size(); i++)
		{
			Integer nuovoid = SeqMgr.getInstance().next(null, "hsaction",
			"idhsaction");
			Object[] o = new Object[12];
			o[0] = nuovoid;
			o[1] = "firstPV";
			o[2] = 1;
			o[3] = actionIds.get(i);
			o[4] = new Integer(pe.getId());
			o[5] = "FALSE";
			o[6] = null;
			o[7] = null;
			o[8] = 1;
			o[9] = 1;
			o[10] = new Date(System.currentTimeMillis());
			o[11] = new Date(System.currentTimeMillis());
		
			DatabaseMgr.getInstance().executeStatement(
					"insert into hsaction values(?,?,?,?,?,?,?,?,?,?,?,?);", o);
		}
	}
	
	//procedura per il rollback di una modifica di parametri
	public void setManualRollback(String user, ParametersEvent pe){
        SetContext setContext = new SetContext(); 

        String lang = "EN_en";
		try
		{
			lang = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch (Exception e)
		{
		}
		setContext.setLanguagecode(lang);
        //user che fa la modifica
        setContext.setUser(user);
        
        //funzione di callback al termine della modifica (OnLineCallBack() per il popup di conferma)
        setContext.setCallback(new OnLineCallBack());
        
        //variabile da modificare
        SetWrp wrp = setContext.addVariable(pe.getIdvariable() , pe.getStartingvalue());
		wrp.setCheckChangeValue(false);
        
		//accodo la modifica!
		SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getDefaultPriority(),true );

		pe.setRollbackable(false);
	}

	//determina se l'utente può effettuare modifiche...
	public static boolean canModify(UserSession us) {
		boolean r = false;
		
		r = us.isButtonActive("siteview", "tab2name", "Save");
			
		return r;
	}
	
	public boolean isRunning(){
		return imrunning && pt!=null && pt.isRunning();
	}
	
	public void reloadPhoto(){
		parametersPhoto = new ParametersPhoto();
	}

	public static ParametersCFG getParametersCFG() {
		if (parametersCFG==null) 
		{
			try
			{
				parametersCFG=new ParametersCFG();
			}
			catch (Exception e) {
				Logger logger = LoggerMgr.getLogger(ParametersMgr.class);
				logger.error(e);
			}
		}
		return parametersCFG;
	}

	public static void setParametersCFG(ParametersCFG parametersCFG) {
		ParametersMgr.parametersCFG = parametersCFG;
	}

	public static ParametersPhoto getParametersPhoto() {
		if (parametersPhoto==null) parametersPhoto=new ParametersPhoto();
		return parametersPhoto;
	}

	public static void setParametersPhoto(ParametersPhoto parametersPhoto) {
		ParametersMgr.parametersPhoto = parametersPhoto;
	}

	public static String getDefaultUsername() {
		return defaultUsername;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		ParametersMgr.properties = properties;
	}


	
}
