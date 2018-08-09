package com.carel.supervisor.presentation.copydevice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.devices.ParamDetail;
import com.carel.supervisor.presentation.session.UserSession;


public class PageImpExp
{
    public static final String DIR_EXPORT_FILE = "DeviceSetting";
	public static final String CAREL_STANDARD_NAME = "CAREL";
	private static final String PreSet="PreSet_";
	private Map fileMap = null;
	private static String fileFullName="";
    public PageImpExp()
    {
    	fileMap = new HashMap();
    }

    public static boolean expDeviceFile(UserSession us, Properties prop)
        throws Exception
    {
        String path = BaseConfig.getCarelPath();
        String s_filename = us.getProperty("exp_file");
        String s_path = us.getProperty("file_path");
        
        int varsWritten = 0;

        //retrive code device
        int iddev = Integer.parseInt(us.getProperty("iddev"));
        String dev_code = getDeviceCode(us.getIdSite(), iddev);

        // nome finale del file
        //CREAZIONE FILE
        String dir_destination = "";
        if(("undefined".equals(s_filename)) || ("".equals(s_filename))){
        	DeviceStructureList deviceStructureList = us.getGroup().getDeviceStructureList();
        	DeviceStructure deviceStructure = deviceStructureList.get(iddev);
        	String code = deviceStructure.getCode();
        	 s_filename = "Param_" +dev_code+"_"+ code + "$$" + dev_code;
        	 dir_destination = path +BaseConfig.getTemporaryFolder();
        	 us.setProperty("export_type", "REMOTE");
        }else{
        	if(!"".equals(s_path)){
            	dir_destination=s_path;
            	String temp=dir_destination.substring(dir_destination.length()-s_filename.length());
            	if(temp.equals(s_filename)){
            		dir_destination=dir_destination.substring(0, (dir_destination.length()-s_filename.length()));
            	}
        	}else{
	        	dir_destination = path + DIR_EXPORT_FILE;
	        	us.setProperty("inner", "inner");
        	}
        	s_filename = s_filename + "$$" + dev_code;
        	us.setProperty("export_type", "LOCAL");
        }

        //controllo se esiste directory, senn� la creo
        File dir_ex = new File(dir_destination);

        if (!dir_ex.exists())
        {
            File create = new File(dir_destination);
            create.mkdir();
        }

        File file = new File(dir_destination + File.separator + s_filename);
        FileWriter fw = new FileWriter(file);

        //retrieve info variabili
        ParamDetail paramDetail = new ParamDetail(us);
        paramDetail.loadVarphyToWrite();

        VarphyBean[] vars = paramDetail.getListVarWrite();
        VarphyBean var = null;
        //ciclo su tutte le variabili in scrittura per il dispositivo
        for (int i = 0; i < vars.length; i++)
        {
            var = vars[i];

            String curVal = null;
            String key = "dtlst_" + var.getId();

            if (prop.containsKey(key))   //controllo di scrivere sul file solo le variabili in pagina nel momento del salvataggio
            {
                curVal = prop.getProperty(key);

                if (!"".equalsIgnoreCase(curVal))   //se non � settata la variabile non la salvo nel file
                {
                    fw.write(vars[i].getCodeVar() + "=" + curVal + "\n");
                    varsWritten++; //conto quante vars ho effettivamente salvato nel file
                }
            }
        }

        fw.close();
        fileFullName=file.getPath();
        if (varsWritten > 0) //vars saved
        	return true;
        else
        {
        	file.delete(); //no vars saved
        	return false;
        }
    }

    public static String getDeviceCode(int idsite, int iddevice)
        throws DataBaseException
    {
        String dev_code = "";
        String sql =
            "select cfdevmdl.code from cfdevmdl,cfdevice where cfdevice.idsite=? and cfdevice.iddevice=?" +
            " and cfdevmdl.iddevmdl=cfdevice.iddevmdl";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(iddevice) });

        if (rs.size() != 0)
        {
            dev_code = rs.get(0).get("code").toString();
            
            //epurazione "dev_code" da chars non ammessi da win in nomi di files e dirs:
            /*
            dev_code = Replacer.replace(dev_code, "/", ""); //x code "I/O Module" come nome di file
            dev_code = Replacer.replace(dev_code, ">", ""); //x code "pCO AHU FLSTDMAHUA >= ver1.8" come nome di file
            dev_code = Replacer.replace(dev_code, "*", ""); //x code "pCO1-2 Shelter (*MSHE)" e altri come nome di file
            */
            dev_code = StringUtility.clrBadOSChars(dev_code);
        }

        return dev_code;
    }

    public static String getComboConfigImport(int idsite, int iddev)
        throws DataBaseException
    {
        StringBuffer html = new StringBuffer();
        String dev_code = getDeviceCode(idsite, iddev);
        String path = BaseConfig.getCarelPath();
        String file_path = path + DIR_EXPORT_FILE;
        File dir = new File(file_path);
        File[] files = dir.listFiles();

        boolean comboexist = false;

        if (files != null)
        {
            html.append("<SELECT id='fileCombo'>");

            for (int i = 0; i < files.length; i++)
            {
                String f_name = files[i].getName();
                if(f_name.indexOf("$$") == -1)
                	continue;
                String[] file_split = StringUtility.split(f_name, "$$");
                String f_ext = file_split[1];
                String name = file_split[0];

                if (f_ext.equals(dev_code))
                {
                    html.append("<OPTION value='" + f_ext + "'>" +
                        name + "</OPTION>");
                    comboexist = true;
                }
            }

            html.append("</SELECT>");
        }

        if (comboexist)
        {
            return html.toString();
        }
        else
        {
            return null;
        }
    }
	public String getComboXML(int idsite,String language, int iddevmdl, DevMdlBeanList mdlList)
	    throws DataBaseException
	{
		LangService lan = LangMgr.getInstance().getLangService(language); 
		if(fileMap.containsKey(iddevmdl))
		{
			return (String)fileMap.get(iddevmdl);
		}
	    if(mdlList == null || mdlList.size() == 0)
	    {
	    	return "";
	    }
	    DevMdlBean devMdl = mdlList.getMdlBeanbyid(iddevmdl);
	    if(devMdl == null)
	    {
	    	return "";
	    }
	    StringBuffer html = new StringBuffer();
        String dev_code = devMdl.getCode();
	    String path = BaseConfig.getCarelPath();
	    String file_path = path + DIR_EXPORT_FILE;
	    File dir = new File(file_path);
	    File[] files = dir.listFiles();
	    File carelfile = new File(path+DIR_EXPORT_FILE+File.separator+CAREL_STANDARD_NAME+"$$"+dev_code);
	    if (files != null)
	    {
	        if(carelfile.exists() == true)
	        {
	        	html.append("<template>");
	        	html.append("<![CDATA["+CAREL_STANDARD_NAME+"$$"+dev_code+"]]>");
	        	html.append("</template>");
	        }
	        for (int i = 0; i < files.length; i++)
	        {
	            String f_name = files[i].getName();
	            if(f_name.indexOf("$$") == -1)
	            	continue;
	            String[] file_split = StringUtility.split(f_name, "$$");
	            String f_ext = file_split[1];
	            String name = file_split[0];

	            if (f_ext.equals(dev_code) && name.equals(CAREL_STANDARD_NAME) == false)
	            {
	            	String[] temp=name.split("_");
	            	String desc="";
	            	if(temp.length==2){
	            		desc=lan.getString("wizard",PreSet+temp[1]);
	            	}
	            	if(desc==null || "".equals(desc)) desc=name;
	            	
	            	html.append("<template>");
	            	html.append("<file><![CDATA["+f_name+"]]></file>");
	            	html.append("<desc><![CDATA["+desc+"]]></desc>");
	            	html.append("</template>");
	            }
	        }
	    }
	    fileMap.put(iddevmdl, html.toString());
	    return html.toString();
	}
    public static String getTableAllDeviceSameModel(int id_master, UserSession sessionUser) throws Exception
    {
    	int idsite = sessionUser.getIdSite();
    	String language = sessionUser.getLanguage();
    	GroupListBean group = sessionUser.getGroup();
    	int[] ids_group = group.getIds();
    	DeviceListBean devices =  new DeviceListBean(idsite,language,ids_group);
    	DeviceBean master = devices.getDevice(id_master);
    	
    	//retrieve dispositivi dello stesso modello
        /*DeviceListBean devices = new DeviceListBean(idsite, language,
                new Integer(iddevice));*/
        DeviceBean dev = null;
        int[] ids = devices.getIds();

        LangService langService = LangMgr.getInstance().getLangService(language);

        StringBuffer html = new StringBuffer();

        html.append("<div>");
        html.append(
            "<div style='width:520;height:335;background-color:#CACACA' align='left' valign='middle'>");
        html.append(
            "<TABLE border='0' cellspacing='1' cellpadding='1' class='table' >\n");
        html.append("<TR class='th'>\n");
        html.append(
            "<TD align='center' class='standardTxt' width='60'><input type='checkbox' id='sel_all' onclick='sel_all();'/></TD>\n");
        html.append("<TD class='standardTxt' width='430'><b>" +
            langService.getString("dtlview", "description") + "</b></TD>\n");
        html.append("</TR></TABLE>\n");

        html.append("<div style='width:517;height:305;overflow:auto;'>");
        html.append(
            "<TABLE  border='0' cellspacing='1' cellpadding='1' class='table' id='deviceTable'>\n");
        
        int cont = 0;
        for (int i = 0; i < devices.size(); i++)
        {
            dev = devices.getDevice(ids[i]);
            //if (dev.getIddevice()!=master.getIddevice()&&dev.getIddevmdl()==master.getIddevmdl())
            // abilitato Self-Broadcast:
            if (dev.getIddevmdl() == master.getIddevmdl())
        	{
                html.append("<TR class='Row1'>");
                html.append(
                    "<TD width='60' class='standardTxt' align='center'><INPUT type='checkbox' id='ch_" +
                    cont + "' onclick='no_sel_all();'>");
                html.append("</TD>\n");
                html.append("<TD width='430' class='standardTxt'>" + dev.getDescription());
                if (dev.getIddevice() == master.getIddevice())
                {
                	html.append(" (*)");
                }
                html.append("</TD>\n");
                html.append("<INPUT type='hidden' id='h_" + cont + "' value='" +
                    dev.getIddevice() + "'</TR>");
                cont++;
        	}
        }

        html.append("</TABLE>\n");
        html.append("</div>");
        html.append("</div>");
        html.append("(*) = self broadcast");
        html.append("</div>");

        return html.toString();
    }

    //public static boolean setDevices(UserSession us, int[] ids_devices, Properties prop)
    public static void setDevices(UserSession us, int[] ids_devices, Properties prop)
        throws Exception
    {
    	String user = us.getUserName();
        boolean exported = false;

        //prendere valori delle variabili del device modello
        ParamDetail paramDetail = new ParamDetail(us);
        paramDetail.loadVarphyToWrite();

        //Map value_to_set = new HashMap(); //mappa idvarmdl-> value
        List varmdl = new ArrayList();
        Map values = new HashMap();
        VarphyBean[] vars = paramDetail.getListVarWrite();
        VarphyBean var = null;

        for (int i = 0; i < vars.length; i++)
        {
        	var = vars[i];

            String curVal = null;
            String key = "dtlst_" + var.getId();

            
            if (prop.containsKey(key))   //controllo di scrivere sul file solo le variabili in pagina nel momento del salvataggio
            {
                curVal = prop.getProperty(key);
                curVal = Replacer.replace(curVal.trim(), ",", ""); //elimino separatore migliaia "," dal valore

                if (!"".equalsIgnoreCase(curVal))   //se non � settata la variabile non la esporto
                {
                	varmdl.add(vars[i].getIdMdl());
                	values.put(vars[i].getIdMdl(), curVal);
                	//value_to_set.put(vars[i].getIdMdl(), curVal);
                }
            }
        }
        
        if (varmdl.size() > 0)
        {
	        StringBuffer sql = new StringBuffer("select * from cfvariable where iddevice=? and idvarmdl in (");
	        //Object[] tmp = new Object[varmdl.size() + 1];
	        Object[] tmp = null;
	        
	        for(int i = 0; i < varmdl.size(); i++)
	        {
	        	sql.append(varmdl.get(i));
	        	//sql.append("?");
	        	//tmp[i+1] = varmdl.get(i);
	        	if (i < (varmdl.size() - 1))
	        	{
	        		sql.append(",");
	        	}
	        }
	        sql.append(") and idhsvariable is not null and iscancelled='FALSE' and idsite=1 order by priority, idvargroup");
	        
	        String valnow = "";
	        
	        /*
	         * La sezione sottostante diventa un thread: vedere fine metodo.
	         */
	        
	        /*
	        SetContext setContext = new SetContext();
	        setContext.setCallback(new BroadCastCallBack());
	        setContext.setUser(user);
	        */
	        
	        /*
	        RecordSet recordset = null;
	        Record r = null;
	        
	        for(int i = 0; i < ids_devices.length; i++)
	        {
	        	if ((i+1)%50 == 0)
	        		Thread.sleep(30000);
	        	
	        	//tmp[0] = new Integer(ids_devices[i]);
	        	tmp = new Object[]{new Integer(ids_devices[i])};
	        	
	        	// creo un setContext x ogni device:
	        	SetContext setContext = new SetContext();
	        	setContext.setCallback(new DefaultCallBack());
		        setContext.setUser(user);
	        	
	        	try
	        	{
		        	recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), tmp);
		        	for(int j = 0; j < recordset.size(); j++)
		        	{
		        		r = recordset.get(j);
		        		valnow = "";
		        		
		        		VariableInfo vbean = new VariableInfo(r);
		        		valnow = ControllerMgr.getInstance().getFromField(vbean.getId().intValue()).getFormattedValue(); //idvariable

		        		//accodo solo var che non sono off-line:
		        		if ((valnow != null) && (!"***".equals(valnow)) && (!"".equals(valnow)))
		        		{
		        			setContext.addVariable(ControllerMgr.getInstance().retrieve(vbean), Float.parseFloat((String)values.get(vbean.getModel())));
		        		}
		        		
		        		//Aggiungere variabili zippate LDAC TO DO
		        	}
		        	setContext.processForZipped(ids_devices[i]);
	        	}
	        	catch(Exception e)
	        	{
	        		LoggerMgr.getLogger(PageImpExp.class).error(e);
	        	}
	        	
	        	// accodo un setContext dopo ogni device:
	        	SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(PriorityMgr.BROADCAST_LBL)); 
	            exported = true;
	        }
	        */
	
	        /*
	        SetDequeuerMgr.getInstance().add(setContext); 
            exported = true;
            */
	        
	        BroadcastThread bthread = new BroadcastThread();
	        
	        bthread.setUsr(user); //imposto utente cha ha avviato il broadcast
	        bthread.setSQL(sql.toString()); //imposto template della query da eseguire
	        bthread.setIdsDevs(ids_devices); //imposto su quali devices eseguire il broadcast
	        bthread.setValues((HashMap)values); //imposto i valori da settare per ogni device
	        
	        bthread.start(); //avvio broadcast
	    }

        //return exported; //vecchia versione
        return;
    }
    public ArrayList getDeviceSettingFile(String filename) {
    	ArrayList<String> sb = new ArrayList<String>();
		String file_path = BaseConfig.getCarelPath() + DIR_EXPORT_FILE + File.separator+filename;
		File file = new File(file_path);
		if (file.exists() == true) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					sb.add(tempString);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
		}
		return sb;
	}

	public static String getFileFullName() {
		return fileFullName;
	}

	public static void setFileFullName(String fileFullName) {
		PageImpExp.fileFullName = fileFullName;
	}
	public static boolean impDeviceFile(UserSession us, Properties prop)throws Exception{
		boolean flag=true;
//		File upload_file=us.getProperty("upload_file");
		
		return flag;
	}
}
