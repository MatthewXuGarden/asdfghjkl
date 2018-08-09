package com.carel.supervisor.presentation.bean;

import java.util.*;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class ProtocolBean {
	
	protected Vector<Record> protocols = new Vector<Record>();
	private static final int BEGIN_ADDR_DEFAULT = 1;
	private static final int END_ADDR_DEFAULT = 207;
	
	
	public ProtocolBean() throws DataBaseException
	{
		String sql = "select * from cfprotocol order by idprotocol";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	if( r != null )
        		protocols.add(r);
        }
	}
	

	public String getParam(String code, String param)
	{
		for(int i = 0; i < protocols.size(); i++) {
			Record r = protocols.get(i);
			if( r.get("code").toString().equals(code) )
				if( r.hasColumn(param) )
					return r.get(param).toString();
		}
		return "";
	}
	
	
	public String getProtocolCode(String protocol)
	{
		for(int i = 0; i < protocols.size(); i++) {
			Record r = protocols.get(i);
			if( r.get("protocol").toString().equals(protocol) )
				return r.get("code").toString();
		}
		return "";
	}
	
	
	public String getName(String code)
	{
		return getParam(code, "name");
	}
	
	
	public String getName(String typeprotocol, String protocol)
	{
		for(int i = 0; i < protocols.size(); i++) {
			Record r = protocols.get(i);
			if( r.get("typeprotocol").toString().equals(typeprotocol)
				&& r.get("protocol").toString().equals(protocol) )
				return r.get("name").toString();
		}
		
		return "";
	}

	
	public String getConnectionType(String code)
	{
		return getParam(code, "connectiontype");
	}
	
	
	public String getTypeProtocol(String code)
	{
		return getParam(code, "typeprotocol");
	}

	
	public String getProtocol(String code)
	{
		return getParam(code, "protocol");
	}
	
	
	public int getBeginAddr(String code)
	{
		try {
			return Integer.parseInt(getParam(code, "beginaddr"));
		} catch(NumberFormatException e) {
			return BEGIN_ADDR_DEFAULT;
		}
	}	
	

	public int getEndAddr(String code)
	{
		try {
			return Integer.parseInt(getParam(code, "endaddr"));
		} catch(NumberFormatException e) {
			return END_ADDR_DEFAULT;
		}
	}	

	
	public Vector<String> getProtocolList(String connectionType)
	{
		Vector<String> protocolList = new Vector<String>();

		for(int i = 0; i < protocols.size(); i++) {
			Record r = protocols.get(i);
			if( connectionType.length() <= 0 || r.get("connectiontype").toString().equals(connectionType) )
				protocolList.add(r.get("code").toString());
		}
		
		return protocolList;
	}
}
