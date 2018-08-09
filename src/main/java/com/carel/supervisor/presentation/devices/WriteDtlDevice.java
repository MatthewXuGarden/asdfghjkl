package com.carel.supervisor.presentation.devices;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.comboset.ComboParam;
import com.carel.supervisor.presentation.comboset.ComboParamMap;
import com.carel.supervisor.presentation.comboset.OptionParam;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.UserSession;


public class WriteDtlDevice extends AbstractDtlDevice
{
//  private static final int TW = 700;
//  private static final int TH = 300;
//  private static final int C1 = 515;
    
    private List<Boolean> inputAvailable = new ArrayList<Boolean>();
    private ComboParamMap cpm = new ComboParamMap();
    
    public WriteDtlDevice(UserSession session, String lang, int idDevice)
    {
        super(session,lang,idDevice);
        
        //inizializzo oggetto x stringhe in combobox
        cpm.loadDeviceConf(idDevice,lang);
    }
    
    public String renderVariables(String tableName)
    {
    	
        ArrayList<VarphyBean> vv = (ArrayList<VarphyBean>)variables;
        
        //cpm = new ComboParamMap();
        //cpm.loadDeviceConf(idDevice,lang);
        
        StringBuffer table = new StringBuffer("<table width='98%' cellspacing='1px' cellpadding='0px' class='table' id='tblRW'>");
        
        table.append("<thead>");
        //table.append("<tr onMouseDown='dtlRenderTabParam(this);'>");
        table.append("<tr>");
        table.append("<th class='th' width='18%' height='18px' colspan='2'>"+getHeaderTable(lang)[3]+"</th>");
        table.append("<th class='th' width='15%' height='18px' >"+getHeaderTable(lang)[2]+"</th>");
        table.append("<th class='th' width='15%' height='18px' >"+getHeaderTable(lang)[1]+"</th>");
        table.append("<th class='th' width='*' height='18px' >"+getHeaderTable(lang)[0]+"</th>");
        table.append("</tr>");
        table.append("</thead>");
        table.append("<tbody>");
        
        //EnumerationMgr enumMgr = this.session.getCurrentUserTransaction().getBoTrx().getEnumerationMgr();
        
        for (int i = 0; i < vv.size(); i++)
        {
        	if (i%2 == 0) {
        		table.append("<tr class='Row1'>");
        	}
        	else {
        		table.append("<tr class='Row2'>");
        	}
            table.append("<td id='var_"+idDevice+"_"+vv.get(i).getId().intValue()+"' class='td10pt' ");
            
            if ((vv.get(i).getType() == 1) || isStringOfChars((String)this.values.get(i)) || (cpm.containComboForVar(vv.get(i).getId())))
            	table.append(" width='18%' colspan='2' align='center'");
            else
            	table.append(" width='8%' align='right'");
            table.append(">");
            
            if (cpm.containComboForVar(vv.get(i).getId()))
            	table.append("<b>"+cpm.getComboForVar(vv.get(i).getId()).getDescFromValue((String)this.values.get(i))+"</b>");
       	 	else 
            {
       	 		if (vv.get(i).getType() == 1){
       	 			try{
       	 				new Integer((String)this.values.get(i));
       	 				table.append(buildLedStatus((String)this.values.get(i),""));
       	 			}catch (NumberFormatException e){
       	 				table.append("<b><nobr>"+(String)this.values.get(i)+"</nobr></b>");
       	 			}
       	 		}
       	 		else
            		 table.append("<b><nobr>"+(String)this.values.get(i)+"</nobr></b>");
             }
             table.append("</td>");
            
            if ((vv.get(i).getType() != 1) && (!cpm.containComboForVar(vv.get(i).getId())) && !isStringOfChars((String)this.values.get(i)))
                table.append("<td class='td10pt' height='15px' width='10%' align='left'><nobr>"+vv.get(i).getMeasureUnit()+"</nobr></td>");
            
            table.append("<td class='td10pt' width='15%' align='center'>"+buildInput(vv.get(i),i)+"</td>");//"cccc</td>");//
            table.append("<td class='td10pt' width='15%' align='center'>"+vv.get(i).getShortDesc()+"</td>");
            table.append("<td class='td10pt' width='*'>"+vv.get(i).getShortDescription()+"</td>");
            table.append("</tr>");
        }
        
        table.append("</tbody>");
        table.append("</table>");
        
        return table.toString();        
    }
    
    public String refreshVariables(String tableName)
    {
        StringBuffer ris = new StringBuffer();
        String type = "";
        VarphyBean tmp = null;
        
        for(int i = 0; i < this.variables.size(); i++)
        {
            tmp = (VarphyBean)this.variables.get(i);
            type = tmp.getType()==1?"D":"A";
            ris.append("<var id='var_"+idDevice+"_"+tmp.getId().intValue()+"' type='"+type+"'>");
            
            if ((cpm != null) && (cpm.containComboForVar(((VarphyBean)this.variables.get(i)).getId())))
            	ris.append(cpm.getComboForVar(((VarphyBean)this.variables.get(i)).getId()).getDescFromValue((String)this.values.get(i)));
       	 	else
       	 		ris.append((String)this.values.get(i));
            
            ris.append("</var>");
        }
        return ris.toString();
    }
    
    public boolean check(VarphyBean variable)
    {
        String rwStatus = null;
        boolean to_add = false;

        if (variable != null)
            rwStatus = variable.getReadwrite();

        // check readwrite
        if (rwStatus != null)
        {
            rwStatus = rwStatus.trim();
            
            //Exclude Read Only variables
            if(!rwStatus.equalsIgnoreCase("1"))
            {
            	// Exclude buttons
        		if (variable.getButtonpath() == null || (variable.getButtonpath() !=null && variable.getButtonpath().equalsIgnoreCase("")))
	            {
	            	// PROFILE FILTER: if FILTER_MANUFACTURER show all variables  
	            	if (session.getVariableFilter()==ProfileBean.FILTER_MANUFACTURER)
	            	{
	            		to_add = true;
	            	}
	            	// else show all read/write but Manufacturer
	            	else if (!rwStatus.equalsIgnoreCase("11"))
	            	{
	            		to_add = true;
	            	}
	            }
            }
        }
        
        if (to_add)
        {
        	if (session.isButtonActive("dtlview", "tab1name", "subtab2name"))
        		inputAvailable.add(new Boolean(true));
        	else
        		inputAvailable.add(new Boolean(false));
        	return true;
        }
        return false;
    }
    
    private String[] getHeaderTable(String lang)
    {
        LangService temp = LangMgr.getInstance().getLangService(lang);
        return  new String[]{
                    temp.getString("dtlview", "detaildevicecol3"),
                    temp.getString("dtlview", "col5"),
                    temp.getString("dtlview", "detaildevicecol1"),
                    temp.getString("dtlview", "detaildevicecol0")};
    }
    
    private String getOnBlur(boolean isOnScreenKey, int idxvar)
    {
    	if(isOnScreenKey)
    	{
    		return " onblur='checkOnlyAnalogOnBlur(this);' ";
    	}
    	else
    	{
    		return " onblur='controlMinMax("+idxvar+");checkOnlyAnalogOnBlur(this);' ";
    	}
    }
    private String buildInput(VarphyBean var, int idx)
    {
    	String input = "";
        String protec = "";
        
        int idxvar = var.getId().intValue();
        int typeVar = var.getType();
        int decimals = var.getDecimal();
    	boolean isOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
        //controllo diritti utente:
        if (((Boolean)this.inputAvailable.get(idx)).booleanValue())
        {
	        //sezione combobox:
        	if ((cpm != null) && (cpm.containComboForVar(var.getId())))
	    	{
		    		ComboParam combo = cpm.getComboForVar(var.getId());
		    		OptionParam opt = null;
		    		
		    		input = "<div id='cmbcontainer_" + idxvar + "' style='width: 100%; position: relative; top: 0px; left: 0px;'>\n";
		    		//x IE-7:
		    		input += "<select style='width:100%;' style='font-size:12pt;' name='dtlst_" + idxvar +"' id='dtlst_" + idxvar +"' onmousedown='cmbAdapt("+idxvar+","+(idx+1)+");' onchange='cmbNormalize("+idxvar+");' onblur='cmbNormalize("+idxvar+");' onselect='this.returnValue=false;' onclick='this.returnValue=false;' >\n";
		    		//x IE-6+7:
		    		//input += "<select style='width:100%;' class='lswtype' name='dtlst_" + idxvar +"' id='dtlst_" + idxvar +"' onmouseover='ctrl_cmb("+idxvar+");cmbAdapt("+idxvar+","+(idx+1)+");this.focus();' onclick='' onchange='cmbNormalize("+idxvar+");' onblur='cmbNormalize("+idxvar+");'>\n";
		    		input += "<option value=''></option>\n";
		    		
		    		for (int j = 0; j < combo.getOptionNumber(); j++)
		    		{
		    			opt = combo.getOption(j);
		    			input += "<option value='"+opt.getValue()+"'>"+opt.getDesc()+"</option>\n";
		    		}
		    		
		    		input += "</select>\n";
		    		input += "</div>\n";
	    	}
	    	else //sezione inputbox
	    	{
		        String virtkey = "";
		        //determino se � abilitata la VirtualKeyboard:
		        if (isOnScreenKey)
		        {
		            virtkey = " class='keyboardInput' ";
		        }
		        
		        String min = "";
		        String max = "";
		        
		        int pk_id;
		
		        /* tipi:
		         * 1 -> Digitale
		         * 2 -> Analogica
		         * 3 -> Intera
		         */
		            
		            // minimo valore ammesso 
		            min = var.getMinValue();
		            
		            if ((min != null) && (min.contains("pk")))
		            {
		            	pk_id = Integer.parseInt(min.substring(2,min.length()));
		            	
		            	try
		            	{
							min = ControllerMgr.getInstance().getFromField(pk_id).getFormattedValue();
						}
		            	catch (Exception e)
						{
							min = "";
						}
		            }
		
		            // massimo valore ammesso
		            max = var.getMaxValue();
		            
		            if ((max != null) && (max.contains("pk")))
		            {
		            	pk_id = Integer.parseInt(max.substring(2,max.length()));
		            	
		            	try
		            	{
							max = ControllerMgr.getInstance().getFromField(pk_id).getFormattedValue();
						}
		            	catch (Exception e)
		            	{
							max = "";
						}
		            }
		        	
		        	if (typeVar == 1) // Digitale
		            {
		                input = "<input "+virtkey+" type='text' size='5' " + protec + " class='lswtype' name='dtlst_" + idxvar +
		                    "' id='dtlst_" + idxvar +
		                    "' value='' onkeydown='checkOnlyDigit(this,event);' onblur='checkOnlyDigitOnBlur(this);'/>\n";
		            }
		            else if (decimals > 0) // Analogica o Intera con decimali
		            {
		                input = "<input "+virtkey+" type='text' size='5' " + protec + " class='lswtype' name='dtlst_" + idxvar +
		                    "' id='dtlst_" + idxvar +
		                    "' value='' onkeydown='checkOnlyAnalog(this,event);'"+getOnBlur(isOnScreenKey,idxvar)+"/>\n";
		                input = input + "<input type='hidden' id='min_"+idxvar+"' value='"+min+"'/>\n";
		            	input = input + "<input type='hidden' id='max_"+idxvar+"' value='"+max+"'/>\n";
		            }
		            else // Intera
		            {
		                input = "<input "+virtkey+" type='text' size='5' " + protec + " class='lswtype' name='dtlst_" + idxvar +
		                    "' id='dtlst_" + idxvar +
		                    "' value='' onkeydown='checkOnlyNumber(this,event);'"+getOnBlur(isOnScreenKey,idxvar)+"/>\n";
		                input = input + "<input type='hidden' id='min_"+idxvar+"' value='"+min+"'/>\n";
		            	input = input + "<input type='hidden' id='max_"+idxvar+"' value='"+max+"'/>\n";
		            }
	        }
    	}
        return input;
    }
    
    //metodo per ctrl se una stringa rappresenta valore numerico oppure stringa di chars:
    public static boolean isStringOfChars(String characters)
    {
    	boolean result = false;
    	
    	if ((characters != null) && (!"***".equals(characters)))
    	{
	    	try
	    	{
	    		Float test = new Float(characters);
	    	}
	    	catch (Exception e)
	    	{
	    		result = true;
	    	}
    	}
    	return result;
    }
}
