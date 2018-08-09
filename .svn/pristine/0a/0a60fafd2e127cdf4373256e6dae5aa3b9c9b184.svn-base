package com.carel.supervisor.remote.bean;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class IncomingBean 
{
	private int idSite = 0;
	private String idModem = "";
	private String stateModem = "";
	
	public IncomingBean(Record r)
	{
		try {
			this.idSite = ((Integer)(r.get("idsite"))).intValue();
			this.idModem = UtilBean.trim(r.get("idmodem"));
			this.stateModem = UtilBean.trim(r.get("state"));
		}
		catch(Exception e){
			
		}
	}

	public String getIdModem() {
		return idModem;
	}

	public int getIdSite() {
		return idSite;
	}

	public String getStateModem() {
		return stateModem;
	}
}
