package com.carel.supervisor.presentation.https2xml;

import java.util.Enumeration;
import java.util.Hashtable;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.bean.XMLBean;
import com.carel.supervisor.presentation.bean.XMLBeanList;

public class RequestParametersList implements IXMLRequest
{
    private final static String ID_DEVICE = "idDevice";
    private final static String ID_VARIABLE = "idsVariable";
    
    private StringBuffer response = new StringBuffer();
	private String username;
	private boolean bBriefList;
	private Hashtable<Integer,Integer> vartypesHash;
	//private ControllerMgr cm = ControllerMgr.getInstance();
	
	public RequestParametersList()
	{
		bBriefList = false;
	}
	
	public RequestParametersList(boolean bBriefList, Hashtable<Integer,Integer> vartypesHash)
	{
		this.bBriefList = bBriefList;
		this.vartypesHash = vartypesHash;
	}
	
    public void startRequest(XMLNode node) throws Exception
    {
    	String language = node.getAttribute("language");
        XMLNode childNode = null;
        
        Hashtable<String,String> hash = new Hashtable<String,String>();
        
        for (int v = 0; v < node.size(); v++)
        {     	
        	childNode = node.getNode(v);

            String idDevice = childNode.getAttribute(ID_DEVICE);
            String idvar = childNode.getAttribute(ID_VARIABLE);
            
            if( idDevice.isEmpty() )
            	idDevice = "LD"; // logic device
            if(hash.containsKey(idDevice))
            {
            	hash.put(idDevice, hash.get(idDevice)+","+idvar);
            }
            else
            {
            	hash.put(idDevice, idvar);
            }
        }       
        
        if( bBriefList ) {
        	requestBriefList(hash);
        	return;
        }
        
        for (Enumeration<String> en = hash.keys() ; en.hasMoreElements() ;)
        {       	           
        	String idDevice = en.nextElement();

            if (idDevice != null && !idDevice.equals("LD"))
            {
                Integer idDev = new Integer(idDevice);
                       
                /*
                 * Travaglin - 02/02/2007
                 * Utilizo nuovo costruttore in seguito al cambio di specifiche
                 * relativo alla visualizzazione delle variabili dello strumento.
                 * In base all'ID delle strumento viene restituito:
                 * - Lista di tutte le variabili in sola lettura configurate come HOME e MAIN
                 * - Lista di tutte le variabili di tipo 4 (Allarmi)
                 * ovviamente accompagnate con il loro valore corrente.
                 */ 
                XMLBeanList beanList = new XMLBeanList(idDev.intValue(),language,hash.get(idDevice));
                XMLBean[] beans = beanList.getXMLBean();
                
                if (beans != null)
                {
                	if(beans.length>0)
                	{
	                    response.append("\n<device name=\"");
	                    response.append(beans[0].getDeviceDescription());
	                    response.append("\" ");
	                    response.append("iddevice=\""+beans[0].getIdDevice()+"\" ");
	                    response.append(" >");
                	}
                    for (int j = 0; j < beans.length; j++)
                    {
                        response.append("\n    <variable name=\"");
                        response.append(beans[j].getVariableDescription());
                        response.append("\" value=\"");
                        try
                        {
                            response.append(ControllerMgr.getInstance()
                                    .getFromField(beans[j].getIdVariable())
                                    .getCurrentValue());
                        }
                        catch (Exception e)
                        {
                            response.append("");
                        }
                        response.append("\" type=\"");
                        response.append(beans[j].getTypeVariable()); 
                        response.append("\" ");
                        
                        response.append("idvar=\"");
                        response.append(beans[j].getIdVariable());
                        response.append("\" ");
                        
                        //response.append("iddev=\""+ beans[j].getIdDevice()+"\" ");
                        
                        response.append("islogic=\"");
                        response.append(beans[j].getIslogic());
                        response.append("\" ");
                        
                        //response.append("priority=\""+ beans[j].getPriority()+"\" ");
                        
                        response.append("readwrite=\"");
                        response.append(beans[j].getReadwrite().trim());
                        response.append("\" ");
                        
                        response.append("minvalue=\"");
                        response.append(beans[j].getMin());
                        response.append("\" ");
                        
                        response.append("maxvalue=\"");
                        response.append(beans[j].getMax());
                        response.append("\" ");
                        
                        response.append("shortdescr=\"");
                        response.append(beans[j].getShortd());
                        response.append("\" ");
                        
                        response.append("longdescr=\"");
                        response.append(beans[j].getLongd());
                        response.append("\" ");
                        
                        //response.append("grp=\""+ beans[j].getGrpcat()+"\" ");
                       
                        response.append(" />");
                      
                    }
                    if(beans.length>0)
                	{
                    	response.append("\n</device>");
                	}
                }
            }
        }
    }

    private void requestBriefList(Hashtable<String,String> hashDevVar) throws Exception
    {
    	for(Enumeration<String> en = hashDevVar.keys(); en.hasMoreElements();) {       	           
    		String idDevice = en.nextElement();
    		String idsVariable = hashDevVar.get(idDevice);
    		if( idDevice.equals("LD") )
    			idDevice = "";
    		response.append("<device iddevice=\"").append(idDevice).append("\">\n");
    		
    		try {
	    		  			
    			String[] varsids = idsVariable.split(",");
    			// 20140416 - I change the way variables' values are fetched from field
    			// Instead of calling .getFromField(var-i) in a cycle, I use the
    			// array form of getFromField(varArray[]). This reduces the DB access a lot,
    			// the tested performances are much better, especially when dealing with devices with many vars
    			int[] varsidsAsInt = new int[varsids.length];
    			
    			// varIDs must be converted to int
    			for (int i = 0; i < varsids.length; i++) {
					varsidsAsInt[i] = Integer.parseInt(varsids[i]);
				}
    			Variable[] variables = ControllerMgr.getInstance().getFromField(varsidsAsInt);
    			
    			
	    	    for(int i = 0 ; i < varsids.length; i++) {
	    	    	
	    	    	int idVariable = Integer.parseInt(varsids[i]);
	    	    	 
	    	    	Integer vartype = vartypesHash.get(idVariable);
	    	    	if(vartype == null)	    	    	
	    	    	{
	    	    		String sql = "select idvariable, type, vardimension, varlength from cfvariable where idsite = 1 and idvariable in ("
	    	    			+ idsVariable + ")";
	    	    	    
	    	    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
	    	    	
	    	    	    for(int j = 0 ; j < rs.size(); j++) {
	    	    	    	Record r = rs.get(j);
	    	    	    	int varid = (Integer)r.get("idvariable");
	    	    	    								
							int type = (Integer)r.get("type");							
							if( type == 3 ) {
								int nVarDimension = (Integer)r.get("vardimension");
								int nVarLength = (Integer)r.get("varlength");
								if( nVarLength == 32 && nVarDimension == 32 )
								type = 32; // 32 bits integer
							}
							
	    	    	       	vartypesHash.put(varid, type);
	    	    	    }
	    	    		vartype = vartypesHash.get(idVariable);
	    	    	}
	    	    	response.append("\t<variable idvar=\"").append(idVariable).append("\" type=\"").append(vartype.toString()).append("\" value=\"");
	    	    	try {
                    	response.append(variables[i].getCurrentValue());
                    } catch(Exception e) {
                    	// nothing to do
                    }
	    	    	response.append("\"/>\n");
	    	    }
    		}
    		catch(DataBaseException e) {
                LoggerMgr.getLogger(this.getClass()).error(e);
    		}
    		response.append("</device>\n");
    	}
    }
    
    public String getResponse() {
    	return response.toString();
    }

    public String getNameRequest() {
        return bBriefList
        	? RequestFactory.REQUEST_PARAM_BRIEF_LIST
        	: RequestFactory.REQUEST_PARAMETERS_LIST;
    }

	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}
