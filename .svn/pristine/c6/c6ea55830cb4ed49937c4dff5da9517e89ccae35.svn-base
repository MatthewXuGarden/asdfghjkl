package com.carel.supervisor.plugin.parameters;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEvent;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersPhotoRow;
import com.carel.supervisor.presentation.session.UserSession;

public class ParametersPhoto {
	private static final String IDVARIABLE = "idvariable";
	private static final String PHOTOVALUE = "photovalue";
	private static final int idsite = 1;

	// Lista delle variabili da controllare
	// IMPORTANTE: La lista deve essere ordinata (per fare le ricerche)
	// Per operare sulla lista usare i setter/getter che mantengono la
	// situazione coerente
	private int[] variableList = null;

	// foto del sistema
	// TODO verificare efficienza della struttura dati della foto
	private Map<Integer, Float> photo;

	public ParametersPhoto() {
		reloadPhotoFromDB();
	}

	// Set di un nuovo valore per un parametro (sincronizzato per evitare
	// problemi di concorrenza)
	public synchronized void setParameter(int idvariable, float value) {

		// recupero vecchio valore del parametro
		float old = getParameter(idvariable);

		if (old != value) {
			// se valore diverso, aggiorno la foto in memoria...
			photo.put(idvariable, value);

			// ... e aggiorno la foto anche su DB
			String sql = "update parameters_variable set photovalue = ? where idsite = ? and idvariable = ? ";
			Object[] par = new Object[3];
			par[0] = value;
			par[1] = idsite;
			par[2] = idvariable;

			try {
				DatabaseMgr.getInstance().executeStatement(sql, par);
			} catch (DataBaseException e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
	}

	// Recupero il valore di un parametro
	public synchronized float getParameter(int idvariable) {
		Float f = photo.get(idvariable);
		// se il valore è null (es. prima lettura) ritorno NaN, così posso
		// riconoscere i casi da non segnalare
		return f != null ? f.floatValue() : Float.NaN;
	}

	private synchronized void reloadPhotoFromDB() {
		// carico la lista dei parametri da controllare da DB
		// e poi carico l'ultima foto da db (se c'è)
		photo = Collections.synchronizedMap(new HashMap<Integer, Float>());
		String sql = "select * from parameters_variable ";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
					null);
			int[] listaVariabili = new int[rs.size()];
			for (int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);

				int v = ((Integer) r.get(IDVARIABLE)).intValue();
				listaVariabili[i] = v;

				Float d = (Float) r.get(PHOTOVALUE);
				if (d != null) {
					photo.put(v, d.floatValue());
				}

			}

			// salvo la lista delle variabili
			setVariableListPRV(listaVariabili);

		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	public synchronized void clearPhotoValue() {
		// ripulisco la foto. svuoto tutto così il sistema ritornerà tutti
		// valori non validi
		// che saranno sovrascritti alla prima lettura
		photo.clear();
	}

	public synchronized void makePhoto(UserSession us) {
		// faccio la foto al sistema		
		try {
			photo.clear();
			int[] vars = getVariableList();
			for (int i = 0; i < vars.length; i++) {
				Variable v;
				v = ControllerMgr.getInstance().getFromField(vars[i]);

				float currentValue = v.getCurrentValue();

				setParameter(vars[i], currentValue);

			}
			Integer id = SeqMgr.getInstance().next(null, "parameters_events",
					"id");
			ParametersEvent pe = new ParametersEvent(id, // nuovo ID tabella
															// eventi
					new Timestamp(System.currentTimeMillis()), // Timestamp in
																// cui ci si è
																// accorti della
																// modifica
					new Integer(1), // id del sito (sempre 1)
					new Integer(1), // id variabile modificata (per photo non serve)
					ParametersMgr.TAKENPHOTOGRAPHYCODE, // codice dell'evento (PHO = fotografia)
					us.getUserName(), // utente che ha modificato il parametro
					new Boolean(false), // True = evento da notiifcare
					new Boolean(false), //non si può fare rollback
					new Float(0.0), // vecchio valore della foto del parametro
					new Float(0.0) // nuovo valore del parametro
			);
			pe.save();
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	private synchronized int[] getVariableListPRV() {
		return variableList;
	}

	private synchronized void setVariableListPRV(int[] variableList) {
		// importante per velocizzare ricerche, lista ordinata!!!
		Arrays.sort(variableList);
		this.variableList = variableList;
	}

	public synchronized int[] getVariableList() {
		return getVariableListPRV();
	}

	public synchronized void setVariableList(int[] variableList) {
		
		//recupero vecchie variabili
		int[] oldvariableList = getVariableList();
		// set della lista delle variabili da controllare
		setVariableListPRV(variableList);

		// sql da usare in controllo (se la variabile era già in lista), in
		// inserimento e in cancellazione
//		String sqlCheck = "select * from parameters_variable where idsite= 1 and idvariable = ? ";
		String sqlInsert = "insert into parameters_variable values(1,?,'NaN')";
//		String sqlDelete = "delete from parameters_variable where idvariable not in (";
//		String sqlDelete2 = ")";
		
		String sqlCheckAll = "select idvariable from parameters_variable where idsite= 1 ";
		String sqlDeleteSingle = "delete from parameters_variable where idvariable = ? ";
		
		//arraylist per cercare elementi
		ArrayList<Integer> varsInDB;
		ArrayList<Integer> varsInRequest = new ArrayList<Integer>();
		for (int i = 0; i < variableList.length; i++) {
			varsInRequest.add(variableList[i]);
		}

		try {
			varsInDB = new ArrayList<Integer>();
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sqlCheckAll, null);
			
			for (int i = 0; i < rs.size(); i++) {
				Integer id= (Integer) rs.get(i).get("idvariable");
				varsInDB.add(id);
			}
			
		} catch (Exception e) {
			varsInDB = new ArrayList<Integer>();
			
		}

		//String lista_per_delete = "";
		//RecordSet rs;
		Object[] o = new Object[1];
		//Object[] o2 = new Object[2];
		for (int i = 0; i < variableList.length; i++) {
			o[0] = new Integer(variableList[i]);
		//	o2[0] = new Integer(variableList[i]);
		//	o2[1] = null;

			// construisco lista di variabili per cancellare quelle che non
			// fanno parte del nuovo insieme
			//lista_per_delete += variableList[i] + ", ";
			try {
				// controllo se la variabile fa già parte delle controllate (se
				// si, non faccio nulla)
				
				//rs = DatabaseMgr.getInstance().executeQuery(null, sqlCheck, o);
				boolean b = varsInDB.contains(variableList[i]);
				//if (rs.size() == 0) {
				if (!b) {
					// se non c'è, la aggiungo
					DatabaseMgr.getInstance().executeStatement(null, sqlInsert,
							o);
				}
			} catch (DataBaseException e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
		
//		OLD Cancellazione
//		if (lista_per_delete.trim().length() > 0) {
//			lista_per_delete = lista_per_delete.substring(0, lista_per_delete
//					.length() - 1);
//
//			try {
//				// alla fine cancello tutte quelle variabili che non ci sono più
//				DatabaseMgr.getInstance().executeStatement(null,
//						sqlDelete + lista_per_delete + sqlDelete2, null);
//			} catch (DataBaseException e) {
//				Logger logger = LoggerMgr.getLogger(this.getClass());
//				logger.error(e);
//			}
//
//		}
		
		for (int i = 0; i < varsInDB.size(); i++) {
			if (!varsInRequest.contains(varsInDB.get(i)))
			{
				try {
					DatabaseMgr.getInstance().executeStatement(null,
							sqlDeleteSingle, new Object[] {varsInDB.get(i)} );
				} catch (DataBaseException e) {
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
				}
			}
		}
		
		
		// una volta risistemata la lista delle variabili ricarico la foto dal
		// DB
		reloadPhotoFromDB();
	}

	public synchronized ParametersPhotoRow[] readPhoto(String language) {
		ParametersPhotoRow[] r = null;
		String sql = "select parameters_variable.*,cfdevice.iddevice, a_desc.description as dev_descr,b_desc.description as var_descr "
				+ " from parameters_variable "
				+ " inner join cfvariable on parameters_variable.idvariable = cfvariable.idvariable "
				+ " inner join cfdevice on cfdevice.iddevice = cfvariable.iddevice "
				+

				"     inner join cftableext a_desc " +
				"         on a_desc.idsite=1 " +
				"        and a_desc.tablename='cfdevice' " +
				"        and a_desc.tableid = cfdevice.iddevice  " +
				"        and a_desc.languagecode= ? " 
				+

				"     inner join cftableext b_desc " +
				"         on b_desc.idsite=1 " +
				"        and b_desc.tablename='cfvariable' " +
				"        and b_desc.tableid = cfvariable.idvariable " +
				"        and b_desc.languagecode= ? " +
				"     order by cfdevice.code, b_desc.description ";

		
		
		try {

	
				Map<Integer, ArrayList<ParametersPhotoRow>> parking = new HashMap<Integer, ArrayList<ParametersPhotoRow>>();
	
	
			
			try {
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {language, language });
				int dim = rs.size();
				r = new ParametersPhotoRow[dim];
				for (int i = 0; i < rs.size(); i++) {
					Record rec = rs.get(i);
					ParametersPhotoRow ppr = new ParametersPhotoRow(rec);
					r[i]=ppr;
				}
	
			} catch (DataBaseException e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		} catch (Exception e1) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e1);
		}

		return r;
	}
	
	public synchronized void clearVariableList(){
		setVariableList(new int[0]);
		reloadPhotoFromDB();
	}
}
