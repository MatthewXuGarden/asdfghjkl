package com.carel.supervisor.presentation.sdk.util;

import java.io.File;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class CustomChecker
{
    public static boolean useCustom(String folder,String resource)
    {
        boolean ris = false;
        try
        {
            if(resource != null && !resource.startsWith(".."))
            {
                File fc = new File(buildPath(folder,resource));
                if(fc.exists())
                    ris = true;
            }
        }
        catch(Exception e) {
        }
        return ris;
    }
    
    public static String isDevCustomFor(int idDevMdl,String folder,String resource)
    {
        String device = "NOP";
        String sql = "select folder from cfdevcustom where iddevmdl=?";
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idDevMdl)});
            device = UtilBean.trim(rs.get(0).get("folder"));
            
            String phome = buildPath(folder,resource,device);
            
            File fc = new File(phome);
            if(!fc.exists())
                device = "NOP";
        }
        catch(Exception e) {
        }
        return device;
    }
    
    /*
     * SDK for Logic Device
     */
    public static String isLogicCustomFor(boolean isLogic,int idDevice,String folder,String resource)
    {
        String device = "NOP";
        
        if(folder!= null && folder.equalsIgnoreCase("dtlview") && isLogic)
        {
            String phome = buildPathLogic(folder,resource,""+idDevice);
            device = ""+idDevice+"_"+resource;
            File fc = new File(phome);
            if(!fc.exists())
                device = "NOP";
        }
        return device;
    }
    
    public static String isSectionCustomFor(int idDevMdl,String resource)
    {
        String device = "NOP";
        String sql = "select folder from cfdevcustom where iddevmdl=?";
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idDevMdl)});
            device = UtilBean.trim(rs.get(0).get("folder"));
            
            String phome = buildSectionPath(device,resource);
            
            File fc = new File(phome);
            if(!fc.exists())
                device = "NOP";
        }
        catch(Exception e) {
        }
        return device;
    }
    
    private static String buildPath(String folder,String resource) 
    {
        return buildPath(folder,resource,null);
    }
    
    private static String buildPath(String folder,String resource,String dev) 
    {
        return BaseConfig.getAppHome()+"custom"+
                File.separator+folder+(dev==null?"":File.separator+dev)+File.separator+resource;
    	
    	/*return System.getenv("PVPRO_HOME")+File.separator+
               "engine"+File.separator+"webapps"+File.separator+"PlantVisorPRO"+File.separator+"custom"+
               File.separator+folder+(dev==null?"":File.separator+dev)+File.separator+resource;*/
    }
    
    /*
     * Build Path for Logic Devices
     */
    private static String buildPathLogic(String folder,String resource,String dev) 
    {   
        return BaseConfig.getAppHome()+"custom"+
        		File.separator+folder+File.separator+"devicelogic"+File.separator+dev+"_"+resource;
        
        /*return System.getenv("PVPRO_HOME")+File.separator+
        "engine"+File.separator+"webapps"+File.separator+"PlantVisorPRO"+File.separator+"custom"+
        File.separator+folder+File.separator+"devicelogic"+File.separator+dev+"_"+resource;*/
    
    }
    
    private static String buildSectionPath(String dev,String resource)
    {
    	 return BaseConfig.getAppHome()+"custom"+
         		File.separator+"dtlview_section"+File.separator+dev+File.separator+resource;
    	
    	/*return System.getenv("PVPRO_HOME")+
               "\\engine\\webapps\\PlantVisorPRO\\custom\\dtlview_section\\"+
               dev+"\\"+resource;*/
    }
}
