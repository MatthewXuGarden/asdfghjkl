package com.carel.supervisor.field;

import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.history.FDBQueue;
import com.carel.supervisor.device.DeviceStatus;
import com.carel.supervisor.device.DeviceStatusEmpty;
import com.carel.supervisor.device.DeviceStatusMgr;
import java.text.DecimalFormat;


public class Variable
{
    //Pointer to the configuration
    private VariableInfo variableInfo = null;
    private boolean volatileVar = false;

    //Pointer to the retriever object
    private IRetriever retriever = new RetrieveDummy();
    private Float currentValue = null;
    private boolean blockingError = false;
    private DeviceStatus deviceStatus = new DeviceStatusEmpty();
    private DataDynamicSaveMember saver = null;

    //true: get the value of the whole vardimension in case of vardimension<>varlength
    //false: get the real value by varlength, varposition in case of vardimension<>varlenth
    //the reason to get the value of whole vardimension is: when set the zipped variables value. need the whole value of vardimension
    private boolean isValueOfDimension = false;
    /**
	 * @return: DataDynamicSaveMember
	 */
	
	public DataDynamicSaveMember getSaver() {
		return saver;
	}

	//Volatile variables
    public Variable(VariableInfo variableInfo)
    {
        this(variableInfo, true, 0);
    }

    //Polled variables
    public Variable(VariableInfo variableInfo, int maxStepNumber)
    {
        this(variableInfo, false, maxStepNumber);
    }

    public Variable(VariableInfo variableInfo, boolean volatileVar,
        int maxStepNumber)
    {
        this.variableInfo = variableInfo;
        this.volatileVar = volatileVar;

        if (!volatileVar)
        {
            saver = new DataDynamicSaveMember(variableInfo, maxStepNumber);
        }
    }

    public void changeTime()
    {
    	saver.changeTime();
    }
    
  
    
    
    
    public void setRetriever(IRetriever retriever)
    {
        this.retriever = retriever;
        this.deviceStatus = DeviceStatusMgr.getInstance().getDeviceStatus(variableInfo.getDevice());
        if (null == deviceStatus)
        {
        	deviceStatus = new DeviceStatusEmpty();
        }
    }

    public VariableInfo getInfo()
    {
        return variableInfo;
    }

    //****************************************
    //  RETURN CODE FROM ACQUISITION
    //****************************************    
    public boolean isBlockingError()
    {
        return blockingError;
    }

    public void activeBlockingError()
    {
        this.blockingError = true;
    }

    //  **************************************** 
    public IRetriever getRetriever()
    {
        return retriever;
    }

    //If the device is offline, the Retriever set the variable to NULL
    public void retrieveAndSaveValue(long time, FDBQueue queue,boolean isFirstTime)
    {
        if (!retriever.isBlockingError()) //Blocking error for device
        {
            if (!blockingError) //Blocking error for variable
            {
                retriever.retrieve(this);

                if (!volatileVar)
                {
                    saver.setNewValue(currentValue);
                    saver.setTime(time);
                    saver.setStatus(!deviceStatus.isDisabled());
                    if(isFirstTime)
                    {
						saver.setExternalForceSaveData();
					}
                    saver.createRecordToEnqueue(queue);
                }
            }
        }
    }

    public void retrieveAndForceSaveValue(long time, FDBQueue queue)
    {
        if (!retriever.isBlockingError()) //Blocking error for device
        {
            if (!blockingError) //Blocking error for variable
            {
                retriever.retrieve(this);
                saver.createRecordToEnqueueBeforeStop(queue);
                // modifica architettura per datatransfer
                try
                {
                	FileMgr.getInstance().putValue(variableInfo.getId(), currentValue);
                }
                catch(Exception e){}
           }
        }
    }

    public float getCurrentValue()
    {
        if (null == currentValue)
        {
            return Float.NaN;
        }

        return currentValue.floatValue();
    }

    public boolean isDeviceOffLine()
    {
        return (!deviceStatus.getStatus());
    }

    public boolean isDeviceDisabled()
    {
        return deviceStatus.isDisabled();
    }

    public void setValue(Float value)
    {
        this.currentValue = value;
    }

    public String getFormattedValue()
    {
        String curVal = null;
        float tmp = 0;
        tmp = getCurrentValue();

        if (variableInfo.getVarTypeForAcquisition() == VariableInfo.TYPE_DIGITAL)
        {
            if (0 == tmp)
            {
                curVal = "0";
            }
            else if (1 == tmp)
            {
                curVal = "1";
            }
            else
            {
                curVal = "***";
            }
        }
        else if (0 == variableInfo.getDecimal())
        {
            //Variabili intere
            if (Float.isNaN(tmp))
            {
                curVal = "***";
            }
            else
            {
                DecimalFormat format = new DecimalFormat();
                format.setMinimumFractionDigits(variableInfo.getDecimal());
                format.setMaximumFractionDigits(variableInfo.getDecimal());
                curVal = format.format(tmp);
            }
        }
        else
        {
            if (Float.isNaN(tmp))
            {
                curVal = "***";
            }
            else
            {
            	//new version
                DecimalFormat format = new DecimalFormat();
                format.setMinimumFractionDigits(variableInfo.getDecimal());
                format.setMaximumFractionDigits(variableInfo.getDecimal());
                curVal = format.format(tmp);
                // old version
                //curVal = String.valueOf(tmp);
            }
        }

        return curVal;
    }

    public boolean isValueOfDimension() {
		return isValueOfDimension;
	}

	public void setIsValueOfDimension(boolean isValueOfDimension) {
		this.isValueOfDimension = isValueOfDimension;
	}
    
}
