package com.carel.supervisor.dispatcher.comm.field;

public interface ICommConnector {
	public abstract boolean isBlockingError();
	public abstract CommReturnCode loadDLLSubsystem();
	public abstract CommReturnCode unloadDLLSubsystem();
	
	
	public abstract CommReturnCode initSubSystem();//Non posso passare all'init la classe CommConfig in quanto
												   //la lista dei modem(devices) per esempio la posso conoscere solo
												   //dopo che le classi tapi sono state inizializzate.
												   //Per cui prima inizializzo, poi mi ricavo la configurazione,
												   //poi per ogni canale imposto la lista dei modem(devices) che
												   //ho scelto, poi posso esegure il comando del canale.
	public abstract CommReturnCode doneSubSystem();
	public abstract CommReturnCode runDefaultCommand(String device);
	public abstract CommReturnCode getConfigObject(ICommConfig conf);
	public abstract CommReturnCode getSubSystemMessages(DevicesMessages deviceMessages);
	public abstract CommReturnCode setSubSystemMessages(DevicesMessages deviceMessages);
	
	 //Get e Set for Data Connector Logical Name
    public abstract void setName(String name);
    public abstract String getName();
    public abstract int getCommunicationID();
	 
}
