package com.carel.supervisor.remote.engine;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.remote.bean.IncomingBean;
import com.carel.supervisor.remote.bean.IncomingBeanList;
import com.carel.supervisor.remote.manager.RasServerMgr;

public class RasServer extends Poller 
{
	private String[] modemList = null;
	private String user = "";
	private String pass = "";
	private String proto = "";
	private String port = "";
	private String root = "";
	private String path = "";
	private String check = "";
	private String after = "";
	private String smodemlist = "";
	private String cert = "";
	
	public RasServer(String check,String after,String user,String pass,String proto,String port,String root,String path,String cert)
	{
		this.check = check;
		this.after = after;
		this.user = user;
		this.pass = pass;
		this.proto = proto;
		this.port = port;
		this.root = root;
		this.path = path;
		this.cert = cert;
		
		try {
			this.loadModemIn();
		}
		catch(Exception e) {
		}
	}
	
	public void run()
	{
		long a = 5000L;
		try {
			a = Long.parseLong(this.after);
			Thread.sleep(a);
		}
		catch(Exception e) {
			try {
				Thread.sleep(a);
			}
			catch(Exception e1) {}
		}
		
		int returnCode = 99;
		ScriptInvoker inv = new ScriptInvoker();
		
		try
		{
			String[] par = new String[]{"java","-classpath",this.path+"RasService.jar;"+this.path+"DispatcherLight.jar;",
				    "com.carel.supervisor.service.Starter",check,user,pass,smodemlist,proto,port,root,cert};
			
			if(smodemlist != null && smodemlist.length() > 1)
			{
				returnCode = inv.execute(par,this.path+"RasServer.log");
				System.out.println("RAS SERVER " + returnCode);
				
				try {
					RasServerMgr.getInstance().startUpServerRas();
				}
				catch(Exception e){}
			}
		}
		catch(Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	private void loadModemIn()
	{
		IncomingBean[] inBean = IncomingBeanList.getEnableIncomingDevice(null);
		if(inBean != null) 
		{
			modemList = new String[inBean.length];
			for(int i=0; i<inBean.length; i++)
			{
				this.modemList[i] = inBean[i].getIdModem();
				this.smodemlist +=  inBean[i].getIdModem()+";";
			}
		}
		
		if(this.smodemlist != null && smodemlist.length() > 1)
			this.smodemlist = this.smodemlist.substring(0,this.smodemlist.length()-1);
	}
}
