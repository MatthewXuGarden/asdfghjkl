package com.carel.supervisor.presentation.https2xml;

import java.sql.Timestamp;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.Event;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;

public class ELRequest implements IXMLRequest {

	private StringBuffer response;
	private String username;
	
	public String getNameRequest() {
		return "EL";
	}

	public String getResponse() {
		return response.toString();
	}

	public void startRequest(XMLNode node) throws Exception {
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		Timestamp ts = new Timestamp(0l);
		Timestamp tsnow = new Timestamp(System.currentTimeMillis());
		response = new StringBuffer();
		response.append("<rs t='EL'>\n");
		StringBuilder response2 = new StringBuilder();
		try{
			try{
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,"select * from productinfo where key='bmslast'");
				if(rs==null || rs.size()==0){
					Object[] o = new Object[2];
					o[0]=0L;
					o[1]=tsnow;
					DatabaseMgr.getInstance().executeStatement(null,"insert into productinfo values ('bmslast', ?,?)",o);
				}
				else
					ts=new Timestamp( new Long((String) rs.get(0).get("value")));
			}catch (Exception e) {
				e.printStackTrace();
			}
			RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, 
					" select hsevent.*, cfmessage.description as mess, cfcategory.description as cat "+ 
					" from hsevent,cfmessage,cfcategory "+
					" where idsite = 1 and hsevent.lastupdate >= ? and "+ 
					" hsevent.categorycode=cfcategory.categorycode and cfcategory.languagecode=? and "+
					" hsevent.messagecode=cfmessage.messagecode and cfmessage.languagecode=? "+
					" order by hsevent.lastupdate desc ",
					new Object[]{ts,bmsc.getLanguage(),bmsc.getLanguage()});
			if(rs2!=null){
				for(int i=0;i<rs2.size();i++){
					Record r = rs2.get(i);
					Event e = new Event(r);
					e.setCategory((String)r.get("cat"));
					e.setMessage((String)r.get("mess"));
					response2.append("<evt id='"+e.getIdevent()+
							"' type='"+e.getType()+
							"' ccode='"+e.getCategorycode()+
							"' mcode='"+e.getMessagecode()+
							"' user='"+e.getUser()+
							"' ts='"+e.getLastupdate()+"'>\n");
					response2.append("<![CDATA["+e.getMessage()+"]]>\n");
					response2.append("</evt>\n");
				}
			}
		}catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			response2= new StringBuilder();
		}
		response.append(response2.toString());
		response.append("</rs>\n");
		DatabaseMgr.getInstance().executeStatement(null,"update productinfo set value=? where key ='bmslast'", new Object[] {tsnow.getTime()});		
	}


	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}
