package com.carel.supervisor.presentation.refresh;



import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.session.UserSession;

@Deprecated
public class RefreshFlashLed extends RefreshMaster
{
   

	Integer idDevice=null;
	
	
    public RefreshFlashLed()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
        if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
        	try{
        	idDevice = new Integer(userSession.getProperty("iddev"));
        	}
        	catch (Exception e) {
				idDevice=null;
				e.printStackTrace();
			}
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (idDevice != null)
        {       	
        	StringBuffer script=new StringBuffer();
        	script.append("<script>");
        	script.append("var led=top.frames['body'].frames['bodytab'].document.getElementById('LedStato');");
        	script.append("if( led ) {");
        	int name =new Integer(UtilDevice.getLedColor(idDevice)).intValue();
        	
        	switch(name){
        		case 0:
        		case 1:
        			script.append("led.SetVariable('Value',");
        			script.append(name);
        			script.append(");");
        			script.append("led.SetVariable('ONColor', 0x00C000);");
        			script.append("led.SetVariable('OFFColor', 0xEAEAEA);");	
        		break;
        		case 2:
          			script.append("led.SetVariable('Value', 1);");
					script.append("led.SetVariable('ONColor', 0xC00000);");
					script.append("led.SetVariable('OFFColor', 0xEAEAEA);");	
      		
        			break;
        		case 3:
        			script.append("led.SetVariable('Value', 1);");
					script.append("led.SetVariable('ONColor', 0x0000C0);");
					script.append("led.SetVariable('OFFColor', 0xEAEAEA);");	
        			break;
        	}//switch
        	script.append("}");
        	script.append("</script>");
        	return script.toString();
        }//if
        return "";
    }//getHtmlData
}//Class RefreshFlashLed
