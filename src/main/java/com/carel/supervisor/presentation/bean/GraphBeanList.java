package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class GraphBeanList
{
    private GraphBean[] graphBeans = null;
    private int size = 0;

    public GraphBeanList()
    {
    }//CharBeanList

    public void loadDeviceList(String dbId, int idSite, int[] idDevices,
        String language) throws Exception
    {
        StringBuffer sql = new StringBuffer();
        Object[] objects = new Object[3+idDevices.length];// {"FALSE","cfdevice"};
        objects[0]="FALSE";
        objects[1]="cfdevice";
        objects[2]=language;
        
       
        
        sql.append("SELECT cfdevice.iddevice,cftableext.description as devicedescription ");
        sql.append("FROM   cfdevice,cftableext ");
        sql.append("WHERE  cfdevice.iddevice=cftableext.tableid ");
        sql.append("AND cfdevice.iscancelled=? ");
        sql.append("AND cftableext.tablename=? ");
        sql.append("AND cftableext.languagecode=? ");
        
        
        if(idDevices!=null){
        	if(idDevices.length>0){
	        	for(int i=0;i<idDevices.length;i++)
	        		objects[i+3]= new Integer(idDevices[i]);
	        	sql.append("AND (");
		        for(int i=0;i<idDevices.length-1;i++){
		        	
		        	sql.append(" cfdevice.iddevice=? ");
			        sql.append(" OR ");
		        }//for
		    	sql.append(" cfdevice.iddevice=? ");
		        sql.append(") order by cfdevice.idline, cfdevice.address");
        	}//if
        }//if
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,sql.toString(), objects);

        if (rs.size() > 0)
        {
            size = rs.size();

            graphBeans = new GraphBean[size];

            Record record = null;

            for (int i = 0; i < rs.size(); i++)
            {
                record = rs.get(i);
                graphBeans[i] = new GraphBean(record);
            }//for
        }//if
        
    } //loadDeviceList
    
    public void loadVariableList(String dbId, int idSite, int idDevice,String isHACCP,
            String language) throws Exception
        {
            StringBuffer sql = new StringBuffer();
            
            Object[] objects =null;
            if(isHACCP.equals("TRUE")){
            	objects =new Object[] {new Integer(idSite),"FALSE","cfvariable",new Integer(idDevice),language,isHACCP};
                sql.append("SELECT cfvariable.idvariable,cfvariable.type,cfvariable.measureunit,cftableext.description as variabledescription ");
	            sql.append("FROM   cfvariable,cftableext ");
	            sql.append("WHERE  cfvariable.idvariable=cftableext.tableid ");
	            sql.append("AND    cfvariable.idsite=? ");
	            sql.append("AND    cfvariable.iscancelled=? ");
	            sql.append("AND    cftableext.tablename=? ");
	            sql.append("AND    cfvariable.iddevice=? ");
	            sql.append("AND    cftableext.languagecode=? ");
	            sql.append("AND    cfvariable.ishaccp=? ORDER BY type");
            }//if
            else{
            	objects =new Object[] {new Integer(idSite),"FALSE","cfvariable",new Integer(idDevice),language,isHACCP};
            	sql.append("SELECT cfvarmaster.idvariable,cfvarmaster.type,cfvarmaster.measureunit,cftableext.description as variabledescription ");
            	sql.append("FROM   cfvariable as cfvarmaster,cfvariable as cfvardesc,cftableext ");
            	sql.append("WHERE  cfvarmaster.idvariable=cfvardesc.idhsvariable ");
            	sql.append("AND    cfvardesc.idvariable=cftableext.tableid ");
            	sql.append("AND    cfvarmaster.idsite=? ");
            	sql.append("AND    cfvarmaster.iscancelled=? ");
            	sql.append("AND    cftableext.tablename=? ");
            	sql.append("AND    cfvarmaster.iddevice=? ");
            	sql.append("AND    cftableext.languagecode=? ");
            	sql.append("AND    cfvarmaster.ishaccp=? ORDER BY type");
            }//else
            

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,sql.toString(), objects);

            if (rs.size() > 0)
            {
                size = rs.size();

                graphBeans = new GraphBean[size];

                Record record = null;

                for (int i = 0; i < rs.size(); i++)
                {
                    record = rs.get(i);
                    graphBeans[i] = new GraphBean(record);
                }//for
            }//if
            
        } //loadVariableList

    

    
    public void loadVariableCosmeticList(String dbId, Integer idSite, Integer idDeviceSelect,String isHACCP,String language,Integer idProfile, Integer idGroup, Integer idDevice) 
    	throws Exception
    {
    	StringBuffer sql = new StringBuffer();
    	
         
    	Object[] objects =null;
    	if(isHACCP.equals("TRUE"))
    	{
    		objects =new Object[] {idSite,"FALSE","cfvariable",idDeviceSelect,language,isHACCP, idSite,idProfile,idGroup!=null?idGroup:idDevice,isHACCP};
             	
         	sql.append("SELECT a.*,b.color,b.ymin,b.ymax ");
         	sql.append("FROM ");
	         	{
         		sql.append("(SELECT cfvariable.idvariable,cfvariable.type,cfvariable.measureunit,cftableext.description as variabledescription ");
		        sql.append("FROM   cfvariable,cftableext ");
	            sql.append("WHERE  cfvariable.idvariable=cftableext.tableid ");
	            sql.append("AND    cfvariable.idsite=? ");
	            sql.append("AND    cfvariable.iscancelled=? ");
	            sql.append("AND    cftableext.tablename=? ");
	            sql.append("AND    cfvariable.iddevice=? ");
	            sql.append("AND    cftableext.languagecode=? ");
	            sql.append("AND    cfvariable.ishaccp=? ORDER BY variabledescription) AS a ");
         		}//Prima Tabella
            sql.append("LEFT OUTER JOIN ");
            	{
            		sql.append("(SELECT idvariable,color,ymin,ymax ");
            		sql.append("FROM cfgraphconf ");
                    sql.append("WHERE idsite= ? ");
                    sql.append("AND   idprofile= ? ");
                    sql.append("AND   ");
                    sql.append(idGroup!=null?"idgroup":"iddevice ");
                    sql.append("= ? ");
                    sql.append("AND ishaccp= ? ) AS b ");
            		
            	}//Second Tabella
            sql.append("ON ");
            sql.append("a.idvariable=b.idvariable ");
         }
         else
         {
        	objects = new Object[] { 
        			idSite,"cfvariable",language,idDeviceSelect,idSite,"FALSE",isHACCP,
        			idSite,idProfile,idGroup!=null?idGroup:idDevice,isHACCP };
        	
        	sql.append("SELECT a.*,b.color,b.ymin,b.ymax ");
          	sql.append("FROM ");
         	sql.append("(SELECT cfvarmaster.idvariable,cfvarmaster.type,cfvarmaster.measureunit,cftableext.description as variabledescription ");
         	sql.append("FROM   cfvariable as cfvarmaster,cfvariable as cfvardesc,cftableext ");
         	sql.append("WHERE  ");
         	sql.append("	   cftableext.idsite=? ");
         	sql.append("AND    cftableext.tablename=? ");
         	sql.append("AND    cftableext.tableid=cfvardesc.idvariable ");
         	sql.append("AND    cftableext.languagecode=? ");
         	sql.append("AND    cfvarmaster.iddevice=? ");
         	sql.append("AND    cfvarmaster.idvariable=cfvardesc.idhsvariable ");
         	sql.append("AND    cfvarmaster.idsite=? ");
         	sql.append("AND    cfvarmaster.iscancelled=? ");
         	sql.append("and cfvardesc.iscancelled='FALSE' ");
         	sql.append("AND    cfvarmaster.ishaccp=? ORDER BY variabledescription) AS a ");
         	sql.append("LEFT OUTER JOIN ");
         	sql.append("(SELECT idvariable,color,ymin,ymax ");
         	sql.append("FROM cfgraphconf ");
         	sql.append("WHERE idsite= ? ");
         	sql.append("AND   idprofile= ? ");
         	sql.append("AND   ");
         	sql.append(idGroup!=null?"idgroup":"iddevice ");
         	sql.append("= ? ");
         	sql.append("AND ishaccp= ? ) AS b ");
         	sql.append("ON ");
         	sql.append("a.idvariable=b.idvariable ");
         }
         

         RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,sql.toString(), objects);

         if (rs.size() > 0)
         {
             size = rs.size();

             graphBeans = new GraphBean[size];

             Record record = null;

             for (int i = 0; i < rs.size(); i++)
             {
                 record = rs.get(i);
                 graphBeans[i] = new GraphBean(record);
             }//for
         }//if
         
            
        } //loadVariableCosmeticList
    
	public GraphBean[] getGraphBeans() {
		return graphBeans;
	}//getChartBeans



}//Class CharBeanList
