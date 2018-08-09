package com.carel.supervisor.controller.setfield;

import java.util.*;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.dataaccess.db.*;


// class used to create a sequence of SetContext objects
// based on parameter priorities (cfvarmdlext table)
// call start to begin execution
public class SetParam {
	private static final short DEFAULT_PRIORITY = 0;
	private static final short MAX_PRIORITY = 10;
	private SetContext[] aSetContext = new SetContext[MAX_PRIORITY + 1];
	private int iSetContext = DEFAULT_PRIORITY - 1;
	private Map<Integer, Short> mapVarPriority = new HashMap<Integer, Short>(); 
	
	private String strUserName;
	private String strLanguage;
	private String strNote;
	private int[] anVars;
	String[] astrValues;
	
	
	public SetParam(String userName, String language, String note,
		int[] vars, String[] values)
	{
		strUserName = userName;
		strLanguage = language;
		strNote = note;
		anVars = vars;
		astrValues = values;
		loadPriorities();
	}
	
	
	public void start()
	{
		if( mapVarPriority.isEmpty() ) {
        	SetContext setContext = new SetContext();
    		setContext.setUser(strUserName);
    		setContext.setLanguagecode(strLanguage);
            setContext.setNote(strNote);
    		setContext.setCallback(new OnLineCallBack());
    		setContext.addVariable(anVars, astrValues);
            SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
		}
		else {
			// create contexts by priority
			for(int i = 0; i <  anVars.length; i++) {
				Short nPriority = mapVarPriority.get(anVars[i]);
				if( nPriority == null )
					nPriority = DEFAULT_PRIORITY;
				SetContext setContext = aSetContext[nPriority];
				if( setContext == null ) {
		        	setContext = new SetContext();
		    		setContext.setUser(strUserName);
		    		setContext.setLanguagecode(strLanguage);
		            setContext.setNote(strNote);
		            OnLineCallBack callback = new OnLineCallBack();
		            callback.registerSetParam(this);
		            setContext.setCallback(callback);
		    		aSetContext[nPriority] = setContext;
				}
				setContext.addVariable(anVars[i], Float.parseFloat(astrValues[i]));
			}
			// release init data
			anVars = null;
			astrValues = null;
			// add first context to dequeuer
			next();
		}
	}
	
	
	public void next()
	{
		while( iSetContext < MAX_PRIORITY) {
			SetContext setContext = aSetContext[++iSetContext];
			if( setContext != null ) {
				SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
				break;
			}
		}
	}
	
	
	public boolean isComplete()
	{
		for(int i = iSetContext + 1; i <= MAX_PRIORITY; i++)
			if( aSetContext[i] != null )
				return false;
		return true;
	}
	
	
	private void loadPriorities()
	{
		StringBuffer sbVars = new StringBuffer();
		for(int i = 0; i < anVars.length; i++) {
			if( i > 0 )
				sbVars.append(",");
			sbVars.append(String.valueOf(anVars[i]));
		}
		String sql = "select cfvariable.idvariable, cfvarmdlext.priority from cfvariable inner join cfvarmdlext"
			+ " on cfvarmdlext.idvarmdl = cfvariable.idvarmdl"
			+ " where idvariable in (" + sbVars.toString() + ");";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer idVariable = (Integer)r.get("idvariable");
				Short nPriority = (Short)r.get("priority");
				if( nPriority != null )
					mapVarPriority.put(idVariable, nPriority);
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
}
