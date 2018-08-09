package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class XMLBeanList {
	
	private XMLBean[] xmlBeans = null;
	private int size = 0;
	
	public XMLBeanList(Integer idDev,Integer []idsVar,String language) throws DataBaseException{
		StringBuffer sql= new StringBuffer();
		Object []objects=new Object[(idsVar!=null?idsVar.length:0)+6];
		objects[0]="cfvariable";
		if(idsVar!=null)
			for(int i=0;i<idsVar.length;i++)
				objects[1+i]=idsVar[i];
		objects[objects.length-5]=idDev;
		objects[objects.length-4]="TRUE";
		objects[objects.length-3]=language;
		objects[objects.length-2]="cfdevice";
		objects[objects.length-1]=language;
		
		
		sql.append("SELECT cftableext.description as devicedescription,a.description as variabledescription, a.idvariable, a.type as variabletype FROM cftableext,(");
			
			sql.append(" SELECT iddevice,idvariable,description,type ");
			sql.append(" FROM cfvariable,cftableext WHERE ");
			sql.append(" cfvariable.idvariable=cftableext.tableid ");
			sql.append(" AND cftableext.tablename=? ");
			if(idsVar!=null){
				sql.append(" AND (");
				for(int i=0;i<idsVar.length-1;i++)
					sql.append(" cfvariable.idvariable=?  OR ");
				sql.append(" cfvariable.idvariable=?  ");
				sql.append(")");
			}
			sql.append(" AND cfvariable.iddevice=? ");
			sql.append(" AND cfvariable.isactive=? ");
			sql.append(" AND cftableext.languagecode=?  ) as a WHERE ");
		
			sql.append(" a.iddevice=cftableext.tableid ");
			sql.append(" AND cftableext.tablename=? ");
			sql.append(" AND cftableext.languagecode=? ");
		
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql.toString(), objects);

        if (rs.size() > 0)
        {
            size = rs.size();

            xmlBeans = new XMLBean[size];

            Record record = null;

            for (int i = 0; i < rs.size(); i++)
            {
                record = rs.get(i);
                xmlBeans[i] = new XMLBean(record);
            }//for
        }//if
		
	}//XMLBeanList
	
	public XMLBeanList(String idDevice,String start,String length, String languagecode) throws DataBaseException
	{
		StringBuffer sql= new StringBuffer();
		idDevice=idDevice==null?"":idDevice;
		start=start==null?"0":start;
		length=length==null?"10":length;
		int add = !idDevice.equals("")?1:0;
		Object[] objects = new Object[add+5];

		if(!idDevice.equals(""))
			objects[0]=new Integer(idDevice);
		
		objects[0+add]="cfvariable";
		objects[1+add]="cfdevice";
		objects[2+add]="alrview";
		objects[3+add]=new Integer(length.equals("")?"10":length);
		objects[4+add]=new Integer(start.equals("")?"0":start);
		
		sql.append("SELECT a.idalarm, a.iddevice, a.idvariable, a.islogic, a.starttime, a.endtime, " +
				"a.ackuser, a.acktime, a.delactionuser as deluser, a.delactiontime as deltime, a.resetuser, a.resettime, " +
				"b.description as aldesc, " +
				"c.description as devdesc, " +
				"d.description as priodesc " +
				"FROM hsalarm as a, cftableext as b, cftableext as c, cftext as d " +
				"WHERE " +
				( !idDevice.equals("") ? " a.iddevice=? AND " : " " ) +
				"b.idsite=1 AND b.tablename=? AND a.idvariable = b.tableid AND b.languagecode='"+languagecode+"' AND "+
				"c.idsite=1 AND c.tablename=? AND a.iddevice = c.tableid AND c.languagecode='"+languagecode+"' AND "+
				"d.idsite=1 AND d.languagecode='"+languagecode+"' AND d.code=? AND ('alarmstate'||a.priority) = d.subcode "+
				"ORDER BY starttime DESC " +
				"LIMIT ? " +
				"OFFSET ? ");
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql.toString(), objects);

	        if (rs.size() > 0)
	        {
	            size = rs.size();

	            xmlBeans = new XMLBean[size];

	            Record record = null;

	            for (int i = 0; i < rs.size(); i++)
	            {
	                record = rs.get(i);
	                xmlBeans[i] = new XMLBean(record);
	            }//for
	        }//if
		
	}//XMLBeanList
	
	/*
	 * Costruttore
	 * Nuovo costrutture per la gestione della richiesta:
	 * - devicesList
	 */
	public XMLBeanList(int idSite,String language, Integer[] ids) throws DataBaseException
	{
		String sql = "select b.description as devicedescription, " +
				"a.iddevice as iddevice, a.idline, a.address, a.code, a.isenabled, " +
				"c.comport, c.baudrate, c.typeprotocol " +
				"from cfdevice as a, cftableext as b, cfline as c "+
				"where " + (ids[0].intValue()==-1?"":"a.iddevice in ("+toIdList(ids)+") and ") +
				"a.idsite=? and a.iscancelled=? and b.idsite=? and b.languagecode=? and b.tablename=? and a.iddevice=b.tableid and a.idline = c.idline";
		
		RecordSet rs = null;
		Object[] params = new Object[]{new Integer(idSite),"FALSE",new Integer(idSite),language,"cfdevice"};
		
		rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
		if(rs != null)
		{
			this.size = rs.size();
			this.xmlBeans = new XMLBean[size];
			for (int i = 0; i < rs.size(); i++)
				this.xmlBeans[i] = new XMLBean(rs.get(i));
		}
	}
	
	public XMLBeanList(int idSite,Integer[] ids) throws DataBaseException
	{
		 String sql =
	            "select cfdevice.iddevice,cfdevmdl.code from cfdevmdl,cfdevice where cfdevice.idsite=? and cfdevice.iddevice in ("+toIdList(ids)+")" +
	            " and cfdevmdl.iddevmdl=cfdevice.iddevmdl";
		RecordSet rs = null;
		Object[] params = new Object[] { new Integer(idSite)};
		
		rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
		if(rs != null)
		{
			this.size = rs.size();
			this.xmlBeans = new XMLBean[size];
			for (int i = 0; i < rs.size(); i++)
				this.xmlBeans[i] = new XMLBean(rs.get(i));
		}
	}
	
	private String toIdList(Integer[] ids)
	{
		String toret = "";
		for(int i=0; i < ids.length-1; i++)
		{
			toret += ids[i].intValue()+",";
		}
		toret+=ids[ids.length-1];
		return toret;
	}
	
    /*
     * Travaglin - 02/02/2007
     * Utilizo nuovo costruttore in seguito al cambio di specifiche
     * relativo alla visualizzazione delle variabili dello strumento.
     * In base all'ID delle strumento viene restituito:
     * - Lista di tutte le variabili in sola lettura configurate come HOME e MAIN
     * - Lista di tutte le variabili di tipo 4 (Allarmi)
     * ovviamente accompagnate con il loro valore corrente.
     */
    public XMLBeanList(int idDevice,String language,String empty) throws DataBaseException
    {
        String devDesc = getDeviceDescription(idDevice,language);
        boolean varlist = empty.contains(",")||(Integer.parseInt(empty)>=0)?true:false;
        String filter = empty;
        if(!varlist)
        {
        	switch (Integer.parseInt(empty))
			{
				case -1 :
					filter = "%";
					break;
				case -2 :
					filter = "HOME";
					break;
				case -3 :
					filter = "MAIN";
					break;
				case -4 :
					filter = "STAT";
					break;
				default :
					filter = "%";
			}
        }
        
        String sql = "select v.iddevice, v.idvariable, v.idsite, v.islogic, v.type as variabletype, " +
        		"v.priority, v.readwrite, v.minvalue, v.maxvalue, v.grpcategory, l.description as variabledescription, " +
        		"l.shortdescr as shortvariabledescription, l.longdescr as longvariabledescription from " +
        		"cfvariable as v, cftableext as l where " +
        		"v.iddevice=? and v.iscancelled=? and (v.idhsvariable is not null or v.idhsvariable!=?) and " +
        		"l.idsite=1 and l.tablename=? and l.tableid=v.idvariable and l.languagecode=? " +
        		(varlist?" and idvariable in ("+filter+") ":"and todisplay like '%"+filter+"%'") +
        		"order by priority, variabledescription, type";

        Object[] params = new Object[]{new Integer(idDevice),"FALSE",new Integer(-1),"cfvariable", language};

        RecordSet rs = null;
        rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
        
        if(rs != null)
        {
            this.size = rs.size();
            this.xmlBeans = new XMLBean[size];
            for (int i = 0; i < rs.size(); i++)
            {
                this.xmlBeans[i] = new XMLBean(rs.get(i));
                this.xmlBeans[i].setDeviceDescription(devDesc);
            }
        }
    }
    
    /*
     * Utilizzato per il recupero della descrizione del dispositivo
     */
    private String getDeviceDescription(int iddevice,String language)
    {
        String desc = "select description from cfdevice, cftableext as t where iddevice=? and t.idsite=1 "+
                      "and tablename=? and tableid=? and languagecode=?";
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,desc,
                           new Object[]{new Integer(iddevice),"cfdevice",new Integer(iddevice),language});
            
            if(rs != null && rs.size() == 1)
                desc = UtilBean.trim((rs.get(0)).get("description"));
            
        } 
        catch (DataBaseException e) {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            
        }
        return desc;
    }
    
	public XMLBean []getXMLBean(){
		return xmlBeans;
	}
}
