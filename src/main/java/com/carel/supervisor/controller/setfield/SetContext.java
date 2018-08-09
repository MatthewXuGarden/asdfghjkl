package com.carel.supervisor.controller.setfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.VarCmd;
import com.carel.supervisor.controller.database.zipped.NoZippedVariableException;
import com.carel.supervisor.controller.database.zipped.VariableZippedManager;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.Variable;

public class SetContext 
{
	private HashMap<Integer, List<SetWrp>> variables = new HashMap<Integer, List<SetWrp>>();
	private String user = null;
	private String languagecode = "EN_en";
	private ISetCallBack callback = new DefaultCallBack();
	private Integer id = null;
	private boolean isLoggable = true;
	private boolean isTest = false;
	private String note = null;
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	private String sql = "";
	private ArrayList<Object[]> values = new ArrayList<Object[]>();
	
	public SetContext() 
	{
		try
		{
			id = SeqMgr.getInstance().next(null, "hsparams","id");
		}
		catch(Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			id = new Integer(0);
		}
	}
	
	public void setIsTest(boolean isTest)
	{
		this.isTest = isTest;
	}
	public boolean getIsTest()
	{
		return this.isTest;
	}
	public Integer getID()
	{
		return id;
	}
	
	 public Integer getFirstDevice()
	 {
		 try
		 {
			 Iterator<Integer> iterator = keys();
			 Integer idDevice = iterator.next();
			 return idDevice;
		 }
		 catch(Exception e)
		 {
			return new Integer(0);
		 }
	 }
	 
	//Solo in questo caso devo gestire errori
	public SetWrp addVariable(int idVariable, float newValue, Object objectForCallBack)
	{
		Variable tmp = null;
		try
		{
			tmp = ControllerMgr.getInstance().retrieve(idVariable);
		}
		catch(Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			
			EventMgr.getInstance().error(new Integer(1), getUser(), "Action",
	                    "W051", new Object[] { ""+getID()});
			
			return null;
		}
    	return addVariable(tmp, newValue, objectForCallBack);	
	}
	
	public SetWrp addVariable(int idVariable, float newValue)
	{
		return addVariable(idVariable, newValue, null);
	}
	
	public void addVariable(int[] idVariable, String[] newValue, Object[] objectForCallBack)
	{
		Variable[] tmp = null;
		try
		{
			tmp = ControllerMgr.getInstance().retrieve(idVariable);
		}
		catch(Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			EventMgr.getInstance().error(new Integer(1), getUser(), "Action",
                    "W051", new Object[] { ""+getID()});
			return;
		}
		
		for(int i = 0; i < idVariable.length; i++)
		{
			if (null == objectForCallBack)
			{
				addVariable(tmp[i], Float.parseFloat(newValue[i]));
			}
			else
			{
				addVariable(tmp[i], Float.parseFloat(newValue[i]), objectForCallBack[i]);
			}
		}
	}
	
	public void addVariable(int[] idVariable, String[] newValue)
	{
		addVariable(idVariable, newValue, null);
	}
	
	//Raggruppo le variabili in funzione del dispositivo, in modo tale da essere pi� veloce
	public SetWrp addVariable(Variable var, float newValue) 
	{
		return addVariable(var, newValue, null);
	}
	
	public SetWrp addVariable(Variable var, float newValue, Object objectForCallBack)
	{
		
		//	Modifica per set variabili logiche identit�
        if (var.getInfo().isLogic())
        {
        	try
        	{
        		var = ControllerMgr.getInstance().retrieve(var.getInfo().getModel().intValue());
        	}
        	catch(Exception e)
        	{
        		LoggerMgr.getLogger(this.getClass()).error(e);
        		String devDescr = "";
        		
        		try
        		{
        			devDescr = var.getInfo().getDeviceInfo().getDescription();
        		}
        		catch(Exception e1)
        		{
        			devDescr = "Logic device " + var.getInfo().getDevice().intValue();
        		}
        		
        		EventMgr.getInstance().error(new Integer(1), getUser(), "Action",
                        "W054", new Object[] { ""+getID(), devDescr});
    			
				return null;
        	}
        }
        
		//Accodo le variabili raggruppandole per dispositivo
		SetWrp wrp = new SetWrp(var, newValue, objectForCallBack);
		
        if (VarCmd.getInstance().contains(var.getInfo().getModel()))
        {
            wrp.setCheckChangeValue(false);
        }

		Integer idDevice = var.getInfo().getDevice();
		List<SetWrp> list = variables.get(idDevice);
		if (list == null)
		{
			list = new ArrayList<SetWrp>();
			list.add(wrp);
			variables.put(idDevice, list);
		}
		else
		{
			list.add(wrp);
		}
		return wrp;
	}

	public int numDevice()
	{
		return variables.size();
	}
	
	public Iterator<Integer> keys()
	{
		return variables.keySet().iterator();
	}
	
	public List<SetWrp> get(Integer idDevice)
	{
		return variables.get(idDevice);
	}
	/**
	 * @return Returns the callback.
	 */
	public ISetCallBack getCallback() 
	{
		return callback;
	}

	/**
	 * @param callback The callback to set.
	 */
	public void setCallback(ISetCallBack callback) 
	{
		this.callback = callback;
	}
	
	/**
	 * @return Returns the user.
	 */
	public String getUser() 
	{
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) 
	{
		this.user = user;
	}

	/**
	 * @return Returns the isLoggable.
	 */
	public boolean isLoggable() 
	{
		return isLoggable;
	}

	/**
	 * @param isLoggable The isLoggable to set.
	 */
	public void setLoggable(boolean isLoggable) 
	{
		this.isLoggable = isLoggable;
	}

	public void logFirstTime(SetWrp wrp, float oldValue)
	{
		if (isLoggable)
		{
			sql = "insert into hsparams values (?,?,?,?,?,?)";
			Object[] value = new Object[6];
			Variable var = wrp.getVar();
			value[0] = var.getInfo().getDevInfo().getId();
			value[1] = var.getInfo().getId();
			value[2] = id;
			value[3] = new Integer(wrp.getCode()); //codice di invio
			value[4] = new Float(oldValue);
			value[5] = new Float(wrp.getNewValue());
			values.add(value);	
		}
	}
	
	public void logCode(SetWrp wrp)
	{
		if (isLoggable)
		{
			sql = "update hsparams set code=? where iddevice=? and idvariable=? and id=?";
			Object[] value = new Object[4];
			Variable var = wrp.getVar();
			value[0] = new Integer(wrp.getCode());
			value[1] = var.getInfo().getDevInfo().getId();
			value[2] = var.getInfo().getId();
			value[3] = id;
			values.add(value);
		}
	}
	
	public void execute()
	{
		try
		{
			if ((values != null) && (values.size() > 0))
			{
				DatabaseMgr.getInstance().executeMultiStatement(null, sql, values);
				sql = "";
				values.clear();
			}
		}
		catch(Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	/**
	 * @return Returns the languagecode.
	 */
	public String getLanguagecode() 
	{
		return languagecode;
	}

	/**
	 * @param languagecode The languagecode to set.
	 */
	public void setLanguagecode(String languagecode) 
	{
		this.languagecode = languagecode;
	}
	
}
