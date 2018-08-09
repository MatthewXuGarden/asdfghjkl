package com.carel.supervisor.dispatcher.memory;

//Classe per BMS - allarmi
public class BAMemory extends ZMemory
{

    public void storeConfiguration() throws Exception
    {

    }

	@Override
	public String getFisicDeviceId() {
		
		return "#BA";
	}

}
