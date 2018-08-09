package com.carel.supervisor.presentation.bean;

import java.util.*;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class DevMdlExtBean {
	
	protected HashMap<Integer, Record> map = new HashMap<Integer, Record>();
	private final String DEFAULT_FILTER = "CAREL,SNMPv1,CAN";
	public static final String MODBUS = "MODBUS";
	
	public DevMdlExtBean() throws DataBaseException
	{
		String sql = "select * from cfdevmdlext";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	if( r != null )
        		map.put(Integer.parseInt(r.get("iddevmdl").toString()), r);
        }
	}

	
	public String getExtParam(int iddevmdl, String extparam)
	{
		Record r = map.get(iddevmdl);
		String str = r != null && r.hasColumn(extparam) ? r.get(extparam).toString() : "";
		return str;
	}
	
	
	public String getFilter(int iddevmdl)
	{
		String filter = getExtParam(iddevmdl, "filter"); 
		return filter.length() > 0 ? filter : DEFAULT_FILTER;
	}

	
	// return used to create the filter array on the client side
	public String getProtocolFilter(int iddevmdl)
	{
		StringBuffer buff = new StringBuffer(); 
		buff.append("[");
		String filter = getFilter(iddevmdl);
		String protocols[] = filter.split(",");
		for(int i = 0; i < protocols.length; i++) {
			if( i > 0 )
				buff.append(",");
			if( protocols[i].length() > 0 ) {
				buff.append("'");
				buff.append(protocols[i]);
				buff.append("'");
			}
		}
		buff.append("]");
		return buff.toString();
	}
	
}
