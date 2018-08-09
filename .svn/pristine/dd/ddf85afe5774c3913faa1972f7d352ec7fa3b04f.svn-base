package com.carel.supervisor.field.dataconn.impl;

import java.util.List;

import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.math.BitManipulation;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.function.DeviceFunz;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.IDataConnector;


public abstract class DataConnBase extends InitializableBase implements IDataConnector
{
    private String name = null;
    private boolean blockingError = false;
    public static final int SET_ON_QUEUE_OK = 0;
    public static final int SET_OK = 900;
    public static final int SET_QUEUE_FULL = -100;
    public static final int SET_DEVICE_OFFLINE = -101;
    public static final int SET_TIMEOUT = -102;
    public static final int BLOCKING_ERROR = -103;
    public static final int FATAL_ERROR = -104;
    public static final int READ_TIMEOUT = -105;
    public static final int SET_MAX = -106;
    public static final int SET_MIN = -107;
    
    public DataConnBase()
    {
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setBlockingError()
    {
        blockingError = true;
    }

    public void removeBlockingError()
    {
        blockingError = false;
    }

    public boolean isBlockingError()
    {
        return blockingError;
    }

    public void writeProtocol() throws Exception
    {
    }
    
    public void writeProtocol(int[] selectedDeviceid) throws Exception
    {	
    }
    /*
     * Metodo utilizzato solo da DataConnMODBUS
     */
    public void valueFromDriver(VariableInfo variableInfo, Variable variable, byte[] value)
        throws Exception
    {
    	//overridden by dataconnmodbus

    	/*long val = value[0];

        if (1 != (variableInfo.getVarDimension() / 8))
        {
            // Fusione dei bits LittleEndian/BigEndian
            //val = BitManipulation.fusionNumber(value, !variableInfo.getDeviceInfo().isLittleEndian(), variableInfo.isSigned());
        	if(value.length==4)
        	{
	            val = 	((value[3] & 0xFF) << 24) +
			            ((value[2] & 0xFF) << 16) +
			            ((value[1] & 0xFF) << 8) +
			            (value[0] & 0xFF);
        	}
//        	if(value.length==2)
//        	{
//	            val = 	((value[0] & 0xFF) << 8) +
//			            (value[1] & 0xFF);
//        	}
            if(variableInfo.isSigned())
            {
            	if(value.length==2)
            	{
            		val = (long)((short)val);
            	}
            	if(value.length==4)
            	{
            		val = (long)((int)val);            		
            	}
            }
        }

        if (variableInfo.getVarLength() != variableInfo.getVarDimension())
        {
            /*
             * In caso di variabili compresse in word estreggo il loro valore a partire
             * dall'offset e dalla lunghezza.
             * Nel Gavazzi sono:
             * 2 su 16 bit da 8 bit ciascuna (PF)
             * 2 su 8 bit da 1 bit ciascuna  (Alarm)
             * /
            val = BitManipulation.extractNumber(val, variableInfo.getBitPosition(), variableInfo.getVarLength());
        }

        //gestisco variabili dopo la virgola
        float decimal = pow(variableInfo.getDecimal());
        
        float dpValue = ((float) val / decimal);
        variable.setValue(new Float(dpValue));*/
    }

    protected float pow(int decimals)
    {
    	return (float)(Math.pow(10.0, decimals));
    	/*
        if (0 == decimals)
        {
            return 1;
        }
        else
        {
            int d = 1;

            for (int i = 0; i < decimals; i++)
            {
                d = d * 10;
            }

            return d;
        }
        */
    }

   /*
    * Utilizzato solo dal protocollo CAREL /* e da snmp *\
    */
    public void valueFromDriver(VariableInfo variableInfo, Variable variable, int value)
    {
        long val = value;

		//isValueOfDimension() is to check if get the value of whole vardimension
        if (!variable.isValueOfDimension() && variableInfo.getVarLength() != variableInfo.getVarDimension())
        {
            // Devo tagliare il valore
            val = BitManipulation.extractNumberCarel(value, variableInfo.getBitPosition(),
                    variableInfo.getVarLength());
        }
        
        /*
         * Al valore recuperato dal campo viene applicato una funzione tramite
         * l'oggetto DeviceFunz.
         * Controlla se per
         * IDDEVMDL,IDVARMDL esiste una funzione. In caso afferamati la applica
         * Intervento per:
         * - MpxPRO
         */
        // 20100531 - CarelFunz was meaningful only for MPXPRO, to fix a firmware problem in a "year" field after A.D.2016
        // Removed after interview with Diego malimpensa/ Serena Ometto.
        // 2010-05-27 Additional controls are suppressed for performance reasons, and because
        // they were scarcely useful
//        try {
//            val = DeviceFunz.getInstance().applyFunction(
//                  variableInfo.getDevInfo().getModel().intValue(),variableInfo.getModel().intValue(),val);
//        }
//        catch(Exception e){}
        // End
        
        // 'decimal' field management
        // it is ignored if the variable is read on all the 16bits to manage the 'compact vars' writing
        float decimal = variable.isValueOfDimension()?1:pow(variableInfo.getDecimal());
        variable.setValue(new Float((float) val / decimal));
    }

    public double valueToDriver(VariableInfo variableInfo, Variable variable)
    {
        //gestisco variabili dopo la virgola
        float decimal = pow(variableInfo.getDecimal());

        return variable.getCurrentValue() * decimal;
    }

    @SuppressWarnings("unchecked")
	public void retrieve(List vars)
    {
        Variable value = null;

        for (int j = 0; j < vars.size(); j++)
        {
            value = (Variable) vars.get(j);
            retrieve(value);
        }
    }
    public static void saveAlarmGuardian(Variable variable)
    {
//    	Se � una variabile di allarme, allora la salvo nella coda degli allarmi
    	if ((variable.getInfo().getType() == VariableInfo.TYPE_ALARM) && (variable.getCurrentValue() >= 1))
    	{
    		if (variable.getInfo().isActive() && (!variable.isDeviceDisabled()))
            {
    			AlarmCtrl.getInstance().insert(variable.getInfo().getId(), variable.getInfo().getPriority());
            }
    	}
    }
    
    public static void saveOfflineGuardian(Variable variable)
    {
//    	Se � una variabile di allarme, allora la salvo nella coda degli allarmi
    	if ((variable.getInfo().getType() == VariableInfo.TYPE_ALARM))
    	{
    		if (variable.getInfo().isActive() && (!variable.isDeviceDisabled()))
            {
    			AlarmCtrl.getInstance().insert(variable.getInfo().getId(), variable.getInfo().getPriority());
            }
    	}
    }
}
