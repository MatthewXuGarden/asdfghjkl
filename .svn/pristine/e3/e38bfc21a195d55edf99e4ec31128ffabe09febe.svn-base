package com.carel.supervisor.presentation.graph;

import java.util.ArrayList;
import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList;
import com.carel.supervisor.presentation.bean.GraphBean;
import com.carel.supervisor.presentation.bean.GraphBeanList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;


public class LoadDevice
{
	private StringBuffer invisibleVariablesList = new StringBuffer();
    private ArrayList idVariablesListToSaveColor= new ArrayList();

    public LoadDevice(Properties properties, UserSession userSession)
    {
        //ENHANCEMENT 20090205 - GUI parameters are no longer necessary
    	
    	UserTransaction trxUserLoc = userSession.getCurrentUserTransaction();
        String group = trxUserLoc.getProperty("group");
        Integer idDeviceSelect = new Integer((String) properties.get("deviceList"));
        Integer idSito = new Integer(userSession.getIdSite());
        Integer idProfile = new Integer(userSession.getProfile());

        String language = userSession.getLanguage();
      
        GraphBeanList graphBeanList = new GraphBeanList();
        String typeGraph = properties.getProperty("typeGraph");
        boolean isHaccp = typeGraph.equals(GraphConstant.TYPE_HACCP) ? true : false;

        Integer idGroup = null;
        Integer idDevice = null;

        if (!group.equals(""))
        {
            idGroup = new Integer(group);
        }
        else
        {
        	idDevice = idDeviceSelect;
        }

        try
        {
            graphBeanList.loadVariableCosmeticList(null, idSito, idDeviceSelect,
                isHaccp ? "TRUE" : "FALSE", language, idProfile, idGroup, idDevice);
        } //try
        catch (Exception e)
        {
           LoggerMgr.getLogger(this.getClass()).error(e);
        } //catch
        
        // ENHANCEMENT 20090203 HTML rendering should be done in the JSP, not here
    	GraphBean[] graphBeans = graphBeanList.getGraphBeans();
        if (graphBeans != null)
        {
        	invisibleVariablesList.append("<vars>"); 
            for (int j = 0; j < graphBeans.length; j++)
            {
                
                // ENHANCEMENT 20090205 - HTML should be built in the JSP.
                // From now on, data is sent to the client in XML-format
                // NOTE: former XHTML actually was a good XML, but I'd rather use
                // a new non-HTML format to avoid misunderstandings
                // NOTE2: XML tags are very short, in order to reduce the amount of bytes
                
                // <r> = row
                invisibleVariablesList.append("<r>"); 
	                // <id> = variable id
	                invisibleVariablesList.append("<id>"); 
	                invisibleVariablesList.append(graphBeans[j].getIdVariable());
	                invisibleVariablesList.append("</id>");
	                
	                // <de> = description
	                invisibleVariablesList.append("<de>"); 
	                invisibleVariablesList.append(graphBeans[j].getVariableDescription());
	                invisibleVariablesList.append("</de>");
	                
	                // <ty> = type
	                invisibleVariablesList.append("<ty>"); 
	                invisibleVariablesList.append(graphBeans[j].getVariableType());
	                invisibleVariablesList.append("</ty>");
	                
	                // <um> = unit of measure
	                invisibleVariablesList.append("<um>"); 
	                invisibleVariablesList.append((graphBeans[j].getUnitOfmeasure() != null)
	                    ? graphBeans[j].getUnitOfmeasure() : "");
	                invisibleVariablesList.append("</um>");
	                
	                // <co> = colour
	                invisibleVariablesList.append("<co>"); 
	                String color=graphBeans[j].getColor();
	                if(color==null){
	                	// It's the first time the user plotted this variable. We choose a random color and save
	                	// this choice for the future. This color'll be employed each time the user plots this variabile, until
	                	// the the user explicitly changes the color schema using the configuration feature.
	                	idVariablesListToSaveColor.add(new Integer(graphBeans[j].getIdVariable()));
	                	color= GraphConstant.createCiclicColor();
	                	idVariablesListToSaveColor.add(color);
	                }
	                invisibleVariablesList.append(color);
	                invisibleVariablesList.append("</co>");
	                
	                // <yl> = min Y
	                invisibleVariablesList.append("<yl>"); 
	                invisibleVariablesList.append(graphBeans[j].getYMin());
	                invisibleVariablesList.append("</yl>");
	                
	                // <yu> = max Y
	                invisibleVariablesList.append("<yu>"); 
	                invisibleVariablesList.append(graphBeans[j].getYMax());
	                invisibleVariablesList.append("</yu>");
                
                invisibleVariablesList.append("</r>"); // row end
            } //for
        	invisibleVariablesList.append("</vars>"); 
        } //if
        
        // ENHANCEMENT 20090211 - We shan't modify the following code, since it's sensible to
        // store a specific color for a given variable even if the user hasn't explicitly set its color yet.
		if(!idVariablesListToSaveColor.isEmpty()){
			try {
				new ConfigurationGraphBeanList(typeGraph).saveCosmeticVariablesBeforePlot(userSession,properties,idVariablesListToSaveColor);
			}//try
			catch (Exception e) {
				  LoggerMgr.getLogger(this.getClass()).error(e);
			}//catch
		}
    } //LoadDevice


    public String getHTML()
    {
        return invisibleVariablesList.toString();
    } //getHTML

} //Class LoadDevice
