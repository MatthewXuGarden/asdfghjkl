package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.presentation.defaultconf.DefGraphVar;
import com.carel.supervisor.presentation.defaultconf.Defaulter;
import com.carel.supervisor.presentation.graph.LoadGraph;
import com.carel.supervisor.presentation.session.Transaction;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;


public class ConfigurationGraphBeanList
{
    private static final String ID_VARIABLE = "idvariable";
    public static final int NUM_VARIABLE = 20;
    private static final int FIRST_SAVE=0;
    private static final int UPDATE=1;
    
    
    private int[] varsId = new int[NUM_VARIABLE];
    private int n = 0;
    private ConfigurationGraphBean[] configurationGraphBeans = null;
    private  String typeGraph=null;

    public ConfigurationGraphBeanList(){
    	
    }//ConfigurationGraphBeanList
    
    public ConfigurationGraphBeanList(String typeGraph){
    	this.typeGraph=typeGraph;
    }//ConfigurationGraphBeanList
    
    
    public void loadConfigurationPage(String dbId, String language,
        Integer idSite, Integer idProfile, Integer idGroup, Integer idDevice)
        throws DataBaseException
    {
        StringBuffer sql = new StringBuffer();
 
        
        Object[] objects = new Object[]
            {
                idSite, idProfile, idGroup!=null?idGroup:idDevice,typeGraph.endsWith(GraphConstant.TYPE_HACCP)?"TRUE":"FALSE"
            };

        sql.append("SELECT ");
        for(int  i=0;i<NUM_VARIABLE-1;i++){
        	sql.append("idvariable");
        	sql.append(i+1);
        	sql.append(",");
        }//for 	
    	sql.append("idvariable");
        sql.append(NUM_VARIABLE);
        sql.append(" ");
                
        sql.append("FROM cfpagegraph ");
        sql.append("WHERE idsite= ? ");
        sql.append("AND   idprofile= ? ");
        sql.append("AND   ");
        sql.append(idGroup!=null?"idgroup":"iddevice");
        sql.append("= ? ");
        sql.append("AND ishaccp= ? ");
        

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,
                sql.toString(), objects);

        if (rs.size() == 1)
        {
            Record record = rs.get(0);
          

            for (int i = 0; i < NUM_VARIABLE; i++)
            {
                if (record.hasColumn(ID_VARIABLE + (i + 1)))
                {
                    Object object = record.get(ID_VARIABLE + (i + 1));

                    if (((Integer) object).intValue() !=0)
                    {
                        varsId[n] = ((Integer) object).intValue();
                        n++;
                    } //if
                } //if
            } //for

            if (n > 0)
            {
              
                configurationGraphBeans = new ConfigurationGraphBean[n];
                objects = new Object[] {
                						 idSite,"cfdevice",language,idSite,"cfvariable",null,language,
                						 idSite,idProfile,idGroup!=null?idGroup:idDevice,typeGraph.endsWith(GraphConstant.TYPE_HACCP)?"TRUE":"FALSE"
	             };
                for (int i = 0; i < n; i++)
                {
                	objects[5]=new Integer(varsId[i]);
                    sql = new StringBuffer();
                   	sql.append("SELECT a.*,b.color,b.ymin,b.ymax ");
                 	sql.append("FROM ");
             		sql.append("(SELECT dev.description as devicedescription,cfvariable.iddevice,cfvariable.idvariable,var.description as variabledescription,cfvariable.type,cfvariable.measureunit ");
                    sql.append("FROM cfvariable,cftableext as var,cftableext as dev ");
                    sql.append("WHERE ");
                    sql.append("      dev.idsite=? ");
                    sql.append("AND cfvariable.iscancelled='FALSE' "); // get only active variables, bug 7369
                    sql.append("AND   dev.tablename=? ");
                    sql.append("AND	  dev.tableid=cfvariable.iddevice ");
                    //if (idGroup==null)	sql.append("AND	  cfvariable.iddevice="+idDevice.intValue()+" ");
                    sql.append("AND   dev.languagecode=? ");
                    sql.append("AND   var.idsite=? ");
                    sql.append("AND   var.tablename=? ");
                    sql.append("AND   var.tableid=? ");
                    sql.append("AND   var.languagecode=? ");
                    sql.append("AND   var.tableid=cfvariable.idvariable ) AS a ");
                 	sql.append("LEFT OUTER JOIN ");
                    sql.append("(SELECT idvariable,color,ymin,ymax ");    
                    sql.append("FROM cfgraphconf ");
                    sql.append("WHERE idsite= ? ");
                    sql.append("AND   idprofile= ? ");
                    sql.append("AND   ");
                    sql.append(idGroup!=null?"idgroup":"iddevice");
                    sql.append("= ? ");
                    sql.append("AND ishaccp= ?) AS b ");
                    sql.append("ON ");
                    sql.append("a.idvariable=b.idvariable ");
                    
                    rs = DatabaseMgr.getInstance().executeQuery(dbId,sql.toString(),objects);

                    if (rs.size() != 1)
                        return;
                    
                    configurationGraphBeans[i] = new ConfigurationGraphBean(rs.get(0));
                 
                }//for
            } //if 
        } //if
    } //loadConfigurationPage

    
    public void loadConfigurationPagePeriod(String dbId, String language,
            Integer idSite, Integer idProfile, Integer idGroup, Integer idDevice)
            throws DataBaseException
        {
    	  StringBuffer sql = new StringBuffer();
    	  
          
          Object[] objects = new Object[]
              {
                  idSite, idProfile, idGroup!=null?idGroup:idDevice,typeGraph.endsWith(GraphConstant.TYPE_HACCP)?"TRUE":"FALSE"
              };

          sql.append("SELECT periodcode ");
                  
          sql.append("FROM cfpagegraph ");
          sql.append("WHERE idsite= ? ");
          sql.append("AND   idprofile= ? ");
          sql.append("AND   ");
          sql.append(idGroup!=null?"idgroup":"iddevice");
          sql.append("= ? ");
          sql.append("AND ishaccp= ? ");
          

          RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,
                  sql.toString(), objects);

          if (rs.size() == 1)
          {
              Record record = rs.get(0);
              configurationGraphBeans= new ConfigurationGraphBean[1];
              configurationGraphBeans[0]= new ConfigurationGraphBean(record);
          }
           
        }//loadConfigurationPagePeriod
   
    
    
    public void loadConfigurationPageCosmeticGraph(String dbId, String language,
            Integer idSite, Integer idProfile, Integer idGroup, Integer idDevice)
            throws DataBaseException
        {
    	  StringBuffer sql = new StringBuffer();
    	  
          
          Object[] objects = new Object[]
              {
                  idSite, idProfile, idGroup!=null?idGroup:idDevice,typeGraph.endsWith(GraphConstant.TYPE_HACCP)?"TRUE":"FALSE"
              };
      
          sql.append("SELECT viewfindercheck,xgridcheck,ygridcheck,viewfindercolorbg,viewfindercolorfg,gridcolor,bggraphcolor,axiscolor ");
                  
          sql.append("FROM cfpagegraph ");
          sql.append("WHERE idsite= ? ");
          sql.append("AND   idprofile= ? ");
          sql.append("AND   ");
          sql.append(idGroup!=null?"idgroup":"iddevice");
          sql.append("= ? ");
          sql.append("AND ishaccp= ? ");
          

          RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,
                  sql.toString(), objects);

          if (rs.size() == 1)
          {
              Record record = rs.get(0);
              configurationGraphBeans= new ConfigurationGraphBean[1];
              configurationGraphBeans[0]= new ConfigurationGraphBean(record);
          }
           
        }//loadConfigurationPageCosmeticGraph
   
   
    
    public  void automaticSaveConfiguration(Properties properties, UserSession userSession) throws Exception {
		StringBuffer sql= new StringBuffer();
		Transaction transaction=userSession.getTransaction();
		UserTransaction trxUserLoc = userSession.getCurrentUserTransaction();
		
		String saveData = properties.getProperty("save_data");
		String[] varIds = saveData.split(";");		
		String group = trxUserLoc.getProperty("group");
		
		sql.append("SELECT 1 FROM cfpagegraph ");
		sql.append("WHERE idsite = ? ");
		sql.append("AND idprofile = ? "); 
			
		Integer idGroupOrDevice=null;
		Integer idGroup=null;
		Integer idDevice=null;
		if(!group.equals("")){
			idGroup=idGroupOrDevice= new Integer(group);
			sql.append("AND idgroup = ? "); 
		}//if
		else{
				if (properties.getProperty("deviceList") != null)
				{
					idDevice = idGroupOrDevice = new Integer(properties.getProperty("deviceList"));
				}
				else
				{
					idDevice = idGroupOrDevice = new Integer(transaction.getIdDevices()[0]);
				}
			
			sql.append("AND iddevice = ? "); 
			}//else
		sql.append("AND ishaccp = ? "); 
		
				  Object[] objects = new Object[]{
						  new Integer(userSession.getIdSite()), new Integer(userSession.getProfile()),idGroupOrDevice,typeGraph.endsWith(GraphConstant.TYPE_HACCP)?"TRUE":"FALSE"}; 
		
		RecordSet rs=DatabaseMgr.getInstance().executeQuery(null,sql.toString(), objects);
		sql=new StringBuffer();
		
		switch(rs.size()){
			case FIRST_SAVE:
				objects=new Object[NUM_VARIABLE+6];
				objects[0]=new Integer(userSession.getIdSite());
				objects[1]=new Integer(userSession.getProfile());
				objects[2]=idGroup;
				objects[3]=idDevice;
				objects[4]=typeGraph.endsWith(GraphConstant.TYPE_HACCP)?"TRUE":"FALSE";
				objects[5]=new Short((String) properties.get("timeperiod"));
								
				sql.append("INSERT INTO cfpagegraph VALUES(?,?,?,?,?,?");
				for(int i=0;i<NUM_VARIABLE;i++){
					sql.append(",?");
					
					if(varIds!=null)
						if(varIds.length>i)
							objects[6+i]= (varIds[i] !=null && !"0".equals(varIds[i])) ? Integer.parseInt(varIds[i]) : null;
						else
							objects[6+i]=null;
					else
						objects[6+i]=null;
				}//for
				sql.append(")");
				DatabaseMgr.getInstance().executeStatement(null,sql.toString(), objects);	
			break;
			case UPDATE:
				sql=new StringBuffer();
				objects=new Object[NUM_VARIABLE+5];
				for(int i=0;i<NUM_VARIABLE;i++){
					if(varIds!=null)
						if(varIds.length>i)
							objects[i]= (varIds[i] !=null && !"0".equals(varIds[i])) ? Integer.parseInt(varIds[i]) : null;
						else
							objects[i]=null;
					else
						objects[i]=null;
				}//for
				objects[NUM_VARIABLE]= new Short((String) properties.get("timeperiod"));
				objects[NUM_VARIABLE+1]=new Integer(userSession.getIdSite());
				objects[NUM_VARIABLE+2]=new Integer(userSession.getProfile());
				objects[NUM_VARIABLE+3]=idGroupOrDevice;
				objects[NUM_VARIABLE+4]=typeGraph.endsWith(GraphConstant.TYPE_HACCP)?"TRUE":"FALSE";
									
				sql.append("UPDATE cfpagegraph SET ");
				for(int  i=0;i<NUM_VARIABLE-1;i++){
			        	sql.append("idvariable");
			        	sql.append(i+1);
			        	sql.append("=?,");
			        }//for 	
				 sql.append("idvariable");
			     sql.append(NUM_VARIABLE);
			     sql.append("=? ");
			     sql.append(",periodcode=? ");
			     sql.append("WHERE idsite=? ");
			     sql.append("AND idprofile=? ");
			     if(!group.equals(""))
			    	 sql.append("AND idgroup=? ");
			     else
			    	 sql.append("AND iddevice=? ");
			     sql.append("AND ishaccp = ? "); 
					
			     
			     DatabaseMgr.getInstance().executeStatement(null,sql.toString(), objects);	
			break; 
			default:
				throw new Exception("Duplicate row in table cfpagegraph");
		}//switch
	}//automaticSaveConfiguration
    
    
    
    public  void saveCosmetic(UserSession us, Properties prop) throws Exception {
    	//String []info=prop.getProperty("infoSave").split(";");
    	
    	if(prop.getProperty("typeGraphCosmetic").equals("haccp")){
    		// save graph layout properties
    		saveCosmeticGraph(us,prop,true);
    		// ENHANCEMENT 20090213 - The following line won't be executed any more since infoSave is always null.
    		// save color and y range
//    	    if(!info[0].equals(""))
//    	    	saveCosmeticVariables(us,prop,info,true);
    	}//if
    	else{
    		// save graph layout properties
     		saveCosmeticGraph(us,prop,false);
     		// save color and y range
//     	   	if(!info[0].equals(""))
//     	   		saveCosmeticVariables(us,prop,info,false);
    	}//else
    	
    }//saveCosmetic

    
    private void saveCosmeticGraph(UserSession us, Properties prop,boolean isHaccp) throws DataBaseException{
    	Transaction transaction=us.getTransaction();
		UserTransaction trxUserLoc = us.getCurrentUserTransaction();
		String group = trxUserLoc.getProperty("group");
		StringBuffer sqlSearch=new StringBuffer();
		StringBuffer sqlAddUpdate=new StringBuffer();
			
		Object [] objectsSearch=new Object[4];
		Object [] objectsAddUpdate=new Object[8+4];
		
		objectsSearch[0]= new Integer(us.getIdSite());
		objectsSearch[1]= new Integer(us.getProfile());
		objectsSearch[2]= isHaccp?new String("TRUE"):new String("FALSE");
		
		
		sqlSearch.append("SELECT COUNT(1) as count FROM cfpagegraph ");
		sqlSearch.append("WHERE idsite=? ");
		sqlSearch.append("AND   idprofile=? ");
		sqlSearch.append("AND   ishaccp=? ");
		
		if(!group.equals("")){
			objectsSearch[3]= new Integer(group);
			sqlSearch.append("AND idgroup = ? "); 
			
		}//if
		else{
			//objectsSearch[3]= new Integer(transaction.getIdDevices()[0]);
			
			if (prop.getProperty("deviceSelect") != null)
			{
				objectsSearch[3] = new Integer(prop.getProperty("deviceSelect"));
			}
			else
			{
				objectsSearch[3] = new Integer(transaction.getIdDevices()[0]);
			}
			
			sqlSearch.append("AND iddevice = ? "); 
			}//else
	    
	    RecordSet recordSet=DatabaseMgr.getInstance().executeQuery(null,sqlSearch.toString(), objectsSearch);
	    
	    String []infoGraphCosmetic=prop.getProperty("cosmeticGraphInformation").split(";");
	    		
	    	if(infoGraphCosmetic.length<8)
	    		return;
	    infoGraphCosmetic[0]=infoGraphCosmetic[0].equals("true")?new String("TRUE"):new String("FALSE");
  	    infoGraphCosmetic[1]=infoGraphCosmetic[1].equals("true")?new String("TRUE"):new String("FALSE");
  	    infoGraphCosmetic[2]=infoGraphCosmetic[2].equals("true")?new String("TRUE"):new String("FALSE");
		     
	    if(((Integer)recordSet.get(0).get("count")).intValue()==1){
	    	
	    	
	 	     
	 	    for(int i=0;i<8;i++)
	 	    	objectsAddUpdate[i]=infoGraphCosmetic[i];
	 	    objectsAddUpdate[8]= new Integer(us.getIdSite());
	 	    objectsAddUpdate[9]= new Integer(us.getProfile());
	 	    objectsAddUpdate[10]= isHaccp?new String("TRUE"):new String("FALSE");
	    	
	 	    sqlAddUpdate.append("UPDATE cfpagegraph SET viewfindercheck=?, ");
	    	sqlAddUpdate.append("xgridcheck=?, ");
	    	sqlAddUpdate.append("ygridcheck=?, ");
	    	sqlAddUpdate.append("viewfindercolorbg=?, ");
	    	sqlAddUpdate.append("viewfindercolorfg=?, ");
	    	sqlAddUpdate.append("gridcolor=?, ");
	    	sqlAddUpdate.append("bggraphcolor=?, ");
	    	sqlAddUpdate.append("axiscolor=? ");
	    	sqlAddUpdate.append("WHERE	 idsite=? ");
	    	sqlAddUpdate.append("AND   idprofile=? ");
	    	sqlAddUpdate.append("AND   ishaccp=? ");
	    	if(!group.equals("")){
	    		objectsAddUpdate[11]= new Integer(group);
    			sqlAddUpdate.append("AND idgroup = ? "); 
	    	}//if
	    	else{
	    		//objectsAddUpdate[11]= new Integer(transaction.getIdDevices()[0]);
	    		
	    		if (prop.getProperty("deviceSelect") != null)
				{
	    			objectsAddUpdate[11] = new Integer(prop.getProperty("deviceSelect"));
				}
				else
				{
					objectsAddUpdate[11] = new Integer(transaction.getIdDevices()[0]);
				}
	    		
	    		sqlAddUpdate.append("AND iddevice = ? "); 
	    	}//else
	    	DatabaseMgr.getInstance().executeStatement(null,sqlAddUpdate.toString(), objectsAddUpdate);
	     }//if di update 
	     else{
	    	 
	    	 sqlAddUpdate.append("INSERT INTO cfpagegraph (idsite,idprofile,ishaccp,");
	    	 if(!group.equals("")){
	    		 	objectsAddUpdate[3]= new Integer(group);
	    			sqlAddUpdate.append("idgroup"); 
		    	}//if
		    	else{
		    		//objectsAddUpdate[3]= new Integer(transaction.getIdDevices()[0]);
		    		
		    		if (prop.getProperty("deviceSelect") != null)
					{
		    			objectsAddUpdate[3] = new Integer(prop.getProperty("deviceSelect"));
					}
					else
					{
						objectsAddUpdate[3] = new Integer(transaction.getIdDevices()[0]);
					}
		    		
		    		sqlAddUpdate.append("iddevice"); 
		    	}//else 		
	    	 sqlAddUpdate.append(",viewfindercheck,xgridcheck,ygridcheck,viewfindercolorbg,viewfindercolorfg,gridcolor,bggraphcolor,axiscolor) ");
	    	 sqlAddUpdate.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
	    	 objectsAddUpdate[0]= new Integer(us.getIdSite());
	    	 objectsAddUpdate[1]= new Integer(us.getProfile());
	    	 objectsAddUpdate[2]= isHaccp?new String("TRUE"):new String("FALSE");
	    	  for(int i=0;i<8;i++)
	    		  objectsAddUpdate[i+4]=infoGraphCosmetic[i];
	    	  DatabaseMgr.getInstance().executeStatement(null,sqlAddUpdate.toString(), objectsAddUpdate); 
	     }//insert
	     
    }//saveCosmeticGraph
    
    public void saveVariableColorAndRange(Properties prop, UserSession us) throws Exception {
		String variableList = prop.getProperty("variableList");
		boolean isHaccp = prop.getProperty("typeGraph") != null && GraphConstant.TYPE_HACCP.equals(prop.getProperty("typeGraph"));
    	String[] tmpList = null;
    	String[] info = null;
    	if (variableList != null) {
    		tmpList=variableList.split(";");
			int n=tmpList.length%LoadGraph.NUM_INFO_4_ROW;
			int num=tmpList.length/LoadGraph.NUM_INFO_4_ROW;
			if((n==0)&&(tmpList.length!=0)){   
				info = new String[num * 4];
				for(int i=0;i<num;i++){
					info[i] = tmpList[0+i*LoadGraph.NUM_INFO_4_ROW]; // varId
					info[i+1] = tmpList[4+i*LoadGraph.NUM_INFO_4_ROW]; // color
					info[i+2] = tmpList[6+i*LoadGraph.NUM_INFO_4_ROW]; // range min
					info[i+3] = tmpList[7+i*LoadGraph.NUM_INFO_4_ROW]; // range max	
				}
			}
    	}
    	
    	Transaction transaction=us.getTransaction();
		UserTransaction trxUserLoc = us.getCurrentUserTransaction();
		String group = trxUserLoc.getProperty("group");
		StringBuffer sqlDelete=new StringBuffer();
		StringBuffer sqlAdd=new StringBuffer();
		Object [] objectsDelete=new Object[5];
		Object [] objectsAdd=new Object[9];
		
		objectsAdd[2]=null;
		objectsAdd[3]=null;
		objectsAdd[0]=objectsDelete[0]= new Integer(us.getIdSite());
		objectsAdd[1]=objectsDelete[1]= new Integer(us.getProfile());
		objectsAdd[4]=objectsDelete[2]= isHaccp?new String("TRUE"):new String("FALSE");
		
		
		sqlDelete.append("DELETE FROM cfgraphconf ");
		sqlDelete.append("WHERE idsite=? ");
		sqlDelete.append("AND   idprofile=? ");
		sqlDelete.append("AND   ishaccp=? ");
		sqlDelete.append("AND   idvariable=? ");
		
		if(!group.equals("")){
			objectsAdd[2]=objectsDelete[4]= new Integer(group);
			sqlDelete.append("AND idgroup = ? "); 
					
		}//if
		else{
			//objectsAdd[3]=objectsDelete[4]= new Integer(transaction.getIdDevices()[0]);
			
			if (prop.getProperty("deviceSelect") != null)
			{
				objectsAdd[3] = objectsDelete[4] = new Integer(prop.getProperty("deviceSelect"));
			}
			else
			{
				objectsAdd[3] = objectsDelete[4] = new Integer(transaction.getIdDevices()[0]);
			}
			
			sqlDelete.append("AND iddevice = ? "); 
			}//else
		
		sqlAdd.append("INSERT INTO cfgraphconf VALUES(?,?,?,?,?,?,?,?,?)");
		
		boolean insert=false;
		// ENHANCEMENT 20090213 - I leave the code as is, even if now we only store one variable at a time, 
		// since we may choose to revert to multi-variable storing in a future time.
		for(int i=0;i<info.length;i++){
			insert=false;
			objectsAdd[5]=objectsDelete[3]=new Integer(info[i++]);
			DatabaseMgr.getInstance().executeStatement(null,sqlDelete.toString(),objectsDelete);
			objectsAdd[6]=null;
			objectsAdd[7]=null;
			objectsAdd[8]=null;
			
			if(!info[i].equals("null")){//Colore
				objectsAdd[6]=info[i];
				insert=true;
				
			}i++;
			if(!info[i].equals("null")){//ymin
				objectsAdd[7]=new Float(info[i]);
				insert=true;
			}i++;
			
			if(!info[i].equals("null")){//ymax
				objectsAdd[8]= new Float(info[i]);
				insert=true;
			}
			if(insert)
				DatabaseMgr.getInstance().executeStatement(null,sqlAdd.toString(),objectsAdd);
			
    	}//for
    	
    }
    
    
    public void saveCosmeticVariablesBeforePlot(UserSession us, Properties prop, ArrayList idVariables ) 
        throws Exception
    {
        int iddevice = 0;
        int idvariable = 0;
        String shaccp = "FALSE";
        
    	Transaction transaction=us.getTransaction();
		UserTransaction trxUserLoc = us.getCurrentUserTransaction();
		String group = trxUserLoc.getProperty("group");
		
		Object [] objectsSearch=new Object[5];
		Object [] objectsAdd=new Object[9];
		
		StringBuffer sqlSearch=new StringBuffer();
		String sqlAdd="INSERT INTO cfgraphconf VALUES(?,?,?,?,?,?,?,?,?)";
		sqlSearch.append("SELECT COUNT(1) FROM cfgraphconf WHERE idsite=? AND idprofile=? AND ishaccp=? AND idvariable=? ");
		
		objectsAdd[2]=null;
		objectsAdd[3]=null;
		objectsAdd[0]= objectsSearch[0]=new Integer(us.getIdSite());
		objectsAdd[1]= objectsSearch[1]=new Integer(us.getProfile());
		objectsAdd[4]= objectsSearch[2]=typeGraph.equals(GraphConstant.TYPE_HACCP)?new String("TRUE"):new String("FALSE");
		
		
		
		if(!group.equals(""))
		{
			objectsAdd[2]= objectsSearch[4]=new Integer(group);
			sqlSearch.append(" AND idgroup=? ");		
		}//if
		else
		{
			//objectsAdd[3]= objectsSearch[4]=new Integer(transaction.getIdDevices()[0]);
			
			if (prop.getProperty("deviceList") != null)
			{
				objectsAdd[3] = objectsSearch[4] = new Integer(prop.getProperty("deviceList"));
			}
			else
			{
				objectsAdd[3] = objectsSearch[4] = new Integer(transaction.getIdDevices()[0]);
			}
			
			sqlSearch.append(" AND iddevice=? ");	
		}//else
		
        DefGraphVar dgv = null;
		for(int i=0; i<idVariables.size(); i++)
        {
			objectsAdd[5]=objectsSearch[3]=(Integer)idVariables.get(i++);
			objectsAdd[6]=(String)idVariables.get(i);
			objectsAdd[7]=null;
			objectsAdd[8]=null;
		
            try
            {
                iddevice = ((Integer)objectsAdd[3]).intValue();
                idvariable = ((Integer)objectsAdd[5]).intValue();
                shaccp = (String)objectsAdd[4];
                dgv = Defaulter.checkDefaultForGraphVariable(iddevice,idvariable,shaccp);
                if(dgv != null && dgv.isDefault())
                {
                    objectsAdd[6] = dgv.getColor();
                    objectsAdd[7] = new Float(dgv.getMin());
                    objectsAdd[8] = new Float(dgv.getMax());
                }
            }
            catch(Exception e) {
            }
			
            RecordSet recordSet=DatabaseMgr.getInstance().executeQuery(null,sqlSearch.toString(),objectsSearch);
			if(((Integer)recordSet.get(0).get(0)).intValue()==0)
				DatabaseMgr.getInstance().executeStatement(null,sqlAdd,objectsAdd);
    	}
   }
    
    
	public ConfigurationGraphBean[] getConfigurationGraphBeans() {
		return configurationGraphBeans;
	}//getConfigurationGraphBeans


	/**********************
	 * MULTIGRAPH SECTION
	 **********************/
	
	public static boolean storeGraphBlock(UserSession us, String blockName, String blockData,boolean isGlobal)
	{
		boolean ret = false;
		
		String sql = "insert into cfgraphconfblock values (?,?,?,?,?,?,?)";
		
		int idSite = us.getIdSite();
		int idProfile = us.getProfile();
		
		int idGroup = -1; 
		try {
			idGroup = Integer.parseInt(us.getCurrentUserTransaction().getProperty("group"));
		}catch(Exception e) {
			idGroup = -1;
		}
		
		blockName = blockName.toUpperCase();
		
		String[] blocks = StringUtility.split(blockData, ";");
		String[] datas = null;
		if(blocks != null)
		{
			Object[][] params = new Object[blocks.length][7];
			Object[][] paramsDel = new Object[blocks.length][6];
			
			for (int i = 0; i < blocks.length; i++) 
			{
				datas = StringUtility.split(blocks[i], "-");
				
				params[i] = new Object[]{
						new Integer(idSite),
						new Integer(idProfile),
						(idGroup == -1?null:new Integer(idGroup)),
						Integer.parseInt(datas[0]),
						"FALSE",
						Integer.parseInt(datas[1]),
						blockName
				};
				
				paramsDel[i] = new Object[]{
						new Integer(idSite),
						new Integer(idProfile),
						(idGroup == -1?null:new Integer(idGroup)),
						Integer.parseInt(datas[0]),
						"FALSE",
						blockName
				};
			}
			
			try 
			{
				// First remove
				String sqlDel = "delete from cfgraphconfblock where idsite=? and idprofile=? and " +
				 			 	"(idgroup=? or idgroup is null) and (iddevice=? or iddevice is null) and " +
				 			 	"ishaccp=? and blocklabel=?";
	
				DatabaseMgr.getInstance().executeMultiStatement(null, sqlDel, paramsDel);
				
				// Re-insert
				DatabaseMgr.getInstance().executeMultiStatement(null, sql, params);
				ret = true;
			} 
			catch (Exception e) {
				LoggerMgr.getLogger(ConfigurationGraphBeanList.class).error(e);
				ret = false;
			}
		}
		
		return ret;
	}
	
	public static boolean removeGraphBlock(UserSession us, String blockName,boolean isGlobal)
	{
		boolean ret = false;
		
		int idSite = us.getIdSite();
		int idProfile = us.getProfile();
		
		int idGroup = -1; 
		try {
			idGroup = Integer.parseInt(us.getCurrentUserTransaction().getProperty("group"));
		}catch(Exception e) {
			idGroup = -1;
		}
		
		int idDev = -1; 
		try {
			idDev = Integer.parseInt(us.getCurrentUserTransaction().getProperty("iddev"));
		}catch(Exception e) {
			idDev = -1;
		}
		
		blockName = blockName.toUpperCase();
		
		String sqlDel = "delete from cfgraphconfblock where idsite=? and idprofile=? and " +
		 	"(idgroup=? or idgroup is null) and (iddevice=? or iddevice is null) and " +
		 	"ishaccp=? and blocklabel=?";
		
		Object[] params = new Object[]{
				new Integer(idSite),
				new Integer(idProfile),
				(idGroup == -1?null:new Integer(idGroup)),
				(idDev == -1?null:new Integer(idDev)),
				"FALSE",
				blockName
		};
		
		try {
			DatabaseMgr.getInstance().executeStatement(null, sqlDel, params);
			ret = true;
		} catch (Exception e) {
			LoggerMgr.getLogger(ConfigurationGraphBeanList.class).error(e);
			ret = false;
		}
		
		return  ret;
	}
	
	public static String retrieveGraphBlocks(UserSession us,String blockNameInput)
	{
		StringBuffer xmlRet = new StringBuffer();
		
		if(blockNameInput == null)
			blockNameInput = "";
		blockNameInput = blockNameInput.toUpperCase();
		
		int idSite = us.getIdSite();
		int idProfile = us.getProfile();
		
		int idGroup = -1; 
		try {
			idGroup = Integer.parseInt(us.getCurrentUserTransaction().getProperty("group"));
		}catch(Exception e) {
			idGroup = -1;
		}
		
		int idDevice = -1; 
		try {
			idDevice = Integer.parseInt(us.getCurrentUserTransaction().getProperty("iddev"));
		}catch(Exception e) {
			idDevice = -1;
		}
		
		String sql = "select blocklabel,iddevice,idvariable from cfgraphconfblock where idsite=? and " +
					 "idprofile=? and (idgroup=? or idgroup is null) and (iddevice=? or iddevice is null) and ishaccp=? "+
					 "order by blocklabel asc";
		
		Object[] params = new Object[]{
				new Integer(idSite),
				new Integer(idProfile),
				(idGroup == -1?null:new Integer(idGroup)),
				(idDevice == -1?null:new Integer(idDevice)),
				"FALSE"
		};
		
		TreeMap result = new TreeMap();
		StringBuffer sb = new StringBuffer();
		String blockName = null;
		String blockValu = null;
		Integer idDev = null;
		Integer idVar = null;
		
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
			Record r = null;
			for(int i=0; i<rs.size(); i++)
			{
				r = rs.get(i);
				
				blockName = UtilBean.trim(r.get("blocklabel"));
				idDev = (Integer)r.get("iddevice");
				idVar = (Integer)r.get("idvariable");
				
				sb = (StringBuffer)result.get(blockName);
				if(sb == null)
				{
					sb = new StringBuffer();
					sb.append(idDev+"-"+idVar);
				}
				else
				{
					sb.append(";"+idDev+"-"+idVar);
				}
				result.put(blockName, sb);
			}
		} 
		catch (Exception e) {
			result = new TreeMap();
			LoggerMgr.getLogger(ConfigurationGraphBeanList.class).error(e);
		}
		
		xmlRet.append("<blocks>");
		Iterator i = result.keySet().iterator();
		while(i.hasNext())
		{
			try 
			{
				blockName = (String)i.next();
				blockValu = ((StringBuffer)result.get(blockName)).toString();
				
				xmlRet.append("<block>");
					xmlRet.append("<name><![CDATA["+blockName+"]]></name>");
					xmlRet.append("<data><![CDATA["+blockValu+"]]></data>");
					if(blockNameInput.equals(blockName))
						xmlRet.append("<check>T</check>");
					else
						xmlRet.append("<check>F</check>");
				xmlRet.append("</block>");
			}
			catch(Exception e) {
			}
		}
		
		xmlRet.append("</blocks>");
		
		return xmlRet.toString();
	}
    
}
