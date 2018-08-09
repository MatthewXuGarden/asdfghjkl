package com.carel.supervisor.controller.setfield;

public interface ISetCallBack 
{
	public void executeOnError(SetContext setContext, SetWrp wrp);
	public void executeOnOk(SetContext setContext, SetWrp wrp);
	public boolean continueOnError(SetContext setContext, SetWrp wrp);
	public void onStart(SetContext setContext); //All'inizio dello scodamento
	public int onEnd(SetContext setContext); //Alla fine dello scodamento
	public void onStop(SetContext setContext); //In caso di stop del motore
}
