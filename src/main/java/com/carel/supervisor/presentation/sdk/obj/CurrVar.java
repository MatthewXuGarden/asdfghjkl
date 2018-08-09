package com.carel.supervisor.presentation.sdk.obj;

import java.text.DecimalFormat;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.presentation.sdk.util.Sfera;

public class CurrVar
{
    private String value = "***";
    private String description = "";
    private String desc1 = "";
    private String desc2 = "";
    private String munit = "";
    private int type = 0;
    private String code = "";
    private int id;
    private String assint = "";
    private String assintdefault = "";
    private String rw;
    private int iddevice;
    // Pattern
    private String pattern = null;
    
    public CurrVar(int id, String code, String value,String desc,String desc1,String desc2,String mu,int type, String rw, int iddevice)
    {
    	this.id = id;
        this.value = value;
        this.description = desc;
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.munit = mu;
        this.type = type;
       	this.rw = rw;
       	this.iddevice = iddevice;
    }
    
    public String getValue()
    {
        return this.value;
    }
    
    public String getDescription()
    {
        return this.description;
    }
    
    public String getMUnit()
    {
        return this.munit;
    }
    
    public String getDescr1()
    {
        return this.desc1;
    }
    
    public String getDescr2()
    {
        return this.desc2;
    }
    
    public int getType()
    {
        return this.type;
    }
    
    public String getCode()
    {
    	return this.code;
    }
    
    public int getIdDevice()
    {
    	return this.iddevice;
    }
    
    public int getId()
    {
    	return id;
    }
    
    public String getFormattedValue() {
    	return this.getFormattedValue(this.pattern);
    }
    
    public String getFormattedValue(String pattern)
    {
    	float f = Float.NaN;
    	String ret = null;
    	DecimalFormat format = null;
    	if(pattern != null)
    		format = new DecimalFormat(pattern);
    	
    	if(format != null)
    	{
	    	try 
	    	{
	    		f = Float.parseFloat(this.value);
	    		ret = format.format(f);
	    	} 
	    	catch(Exception e){
	    		ret = null;
	    	}
    	}
    	
    	if(ret != null)
    		return ret;
    	else
    		return this.value;
    }
   
    public String getRefreshableValue()
    {
    	return "<span id='"+this.id+"' name='"+this.id+"'>"+this.value+"</span>";
    }
    
    public String getRefreshableFormattedValue(String pattern)
    {
    	if(this.pattern == null)
    		this.pattern = pattern;
    	return "<span id='ftt"+this.id+"_"+iddevice+"' name='ftt"+this.id+"_"+iddevice+"'>"+getFormattedValue(this.pattern)+"</span>";
    }
    
    public String getRefreshableAssint(String values)
    {
    	return getRefreshableAssint(values, "");
    }

    public String getRefreshableAssint(String values, String def)
    {
    	if(values!=null)
    	{
	    	assint=values;
	    	assintdefault = def!=null?def:"";
	    	String retStr = "<span id='img"+this.id+"_"+iddevice+"' name='img"+this.id+"_"+iddevice+"'>"+Sfera.assint(this.value, assint, assintdefault)+"</span>"
	    	+ "<input id='imgval" + this.id + "' type='hidden' value=\"" + values + "\"/>";
	    	return retStr;
    	}
    	else
    		return "";
    }

	public String getAssint()
	{
		return assint;
	}
    
	public String getAssintDefault()
	{
		return assintdefault;
	}
    
    public void setValue(String v)
    {
    	this.value = v;
    }
    
    public String getSetField()
    {
    	if(this.rw!=null && !this.rw.trim().equals("1"))
    		return "<input type=\"text\" name=\"dtlst_"+this.id+"\" id=\"dtlst_"+this.id+"\" style=\"width:95%;\" value=\"\">";
    	else
    		return "";
    }
    
    public String getPostName()
    {
    	if(this.rw!=null && !this.rw.trim().equals("1"))
    		return "dtlst_"+this.id;
    	else
    		return "";
    }
    
    public String getSimpleButton(int pageId,String value,String srcImage,String shortDesc)
    {
    	StringBuffer sb = new StringBuffer("");
    	int idVarSet = this.id;
    	if(this.rw!=null&&!this.rw.equals("1"))
    	{
    		sb.append("<form name='frmdtlbtt"+pageId+"' id='frmdtlbtt"+pageId+"' action='servlet/master;' method='post'>");
    		sb.append("<input type='hidden' id='dtlst_"+idVarSet+"' name='dtlst_"+idVarSet+"' value='"+value+"'/>");
    		sb.append("</form>");
            sb.append("<img id='bttdtlst_"+idVarSet+"' src='"+srcImage+"' onclick='PVPK_SetButtonValue("+pageId+");return false;' style='cursor:pointer;' alt='"+shortDesc+"'></img>");
    	}
    	return sb.toString();
    }
    
    
    /**
     * @deprecated
     */
    public String getBAnd(int mask)
    {
    	try
		{
			float f = ControllerMgr.getInstance().getFromField(id).getCurrentValue();
			if(new Float(f).equals(Float.NaN)) throw new Exception();
			int ret = new Float(f).intValue() & mask;
            return ""+ret;
		} catch (Exception e1)
		{
        	return "***";
		}
    }
    
    public String getHiddenInput(){
    	return "<input type=\"hidden\"  name=\"dtlst_"+this.id+"\" id=\"dtlst_"+this.id+"\"  value=\"\" >";
    }
    
    public String getCheckbox( int value ,String fn){
    	return  "<input type=\"checkbox\" "+(value==1?"checked":"")+" name=\""+this.id+"\" id=\""+this.id+"\"   onclick=\""+fn+"(this)\"  value=\""+value+"\" >";
    }

}
