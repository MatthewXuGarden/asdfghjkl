package com.carel.supervisor.controller.setfield;
import com.carel.supervisor.dispatcher.tech.clock.*;

// TechClock synchronization callback
public class TCCallBack extends BackGroundCallBack {

	private TechClock tc;
	private SetClock sc;
	
	
	public TCCallBack(TechClock tc, SetClock sc)
	{
		this.tc = tc;
		this.sc = sc;
	}
	

	public void executeOnError(SetContext setContext, SetWrp wrp)
	{
		int idDevice = wrp.getVar().getInfo().getDevice();
		tc.onSetError(idDevice);
	}
	

	public boolean continueOnError(SetContext setContext, SetWrp wrp)
	{
		super.continueOnError(setContext, wrp);
		return false; // all variables required for operation success; no need to continue
	}
	

	public int onEnd(SetContext setContext)
	{
		int nRet = super.onEnd(setContext);
		sc.goAhead();
		return nRet;
	}
}
