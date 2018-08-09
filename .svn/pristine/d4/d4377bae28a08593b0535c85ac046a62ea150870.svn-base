package com.carel.supervisor.report;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConf;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.plugin.fs.FSManager;
import com.carel.supervisor.presentation.bo.helper.VariableHelper;
import com.carel.supervisor.report.bean.AlarmBean;


public class AlarmReport extends Report {
	
	private LinkedList<AlarmBean> ll;

	private Object[][] tableAlarm;
	private Object[][] tableLog;

	public static final String I18NTYPE = "i18ntype";
	public static final String I18NTIMESTAMP = "i18ntimestamp";
	public static final String I18NDEVICE = "i18ndevice";
	public static final String I18NALR = "i18nalr";
	public static final String I18NDESCALR = "i18ndescalr";

	private static final String TIME = null;

    @SuppressWarnings("unchecked")
	private Object[][] createTableAlarm(String language, int idSite, int[] idvar) {
    	String infos[] = new String[0];
        Object[][] alarmData = new Object[idvar.length][];
        Timestamp time = null;
        Map<String, Object> map = (Map<String, Object>)parameters.get(DESCRIPTIONS);
        for (int i = 0; i < alarmData.length; i++) {
        	alarmData[i] = new Object[4];
			infos = (String[])map.get(new Integer(idvar[i]));
            try {
                time = ((Timestamp[])parameters.get(ALR_END))[i];
                if (time == null) {
                    time = ((Timestamp[])parameters.get(ALR_START))[i];
                    alarmData[i][0] = "START";
                } else {
                    alarmData[i][0] = "END";
                }
            } catch (Exception e) {
                alarmData[i][0] = " ";
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
            alarmData[i][1] = time;
            alarmData[i][2] = ((infos != null)?infos[1]:" ");
            alarmData[i][3] = ((infos != null)?infos[0]:" ");
        }
        return alarmData;
    }
    
    private Object[][] createTableLog(boolean queryRack,String language, int idSite, int[] idvar)
 {
		StringBuffer sb = new StringBuffer();
		for (int i : idvar) {
			try {
				Variable var = ControllerMgr.getInstance().getFromField(i);
				if (0 != var.getInfo().getAddressIn().intValue()) {
					sb.append(i).append(",");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		if (sb.length() < 1) {
			return null;
		}
		String sql = "select idvariable from cfvariable where iddevice in ( select distinct var.iddevice from cfvariable var,cfdevice dev,cfdevmdl dm where var.idvariable in ("
				+ sb.substring(0, sb.length() - 1)
				+ ") and  var.idsite = 1 and var. iscancelled='FALSE' and  dev.idsite = 1 and dev.iscancelled='FALSE' and var.iddevice = dev.iddevice and dev.iddevmdl = dm.iddevmdl and dm.code in (select devcode from fsdevmdl)  "
				+ ") and  iscancelled='FALSE' and idsite =1  and idhsvariable is not null and idhsvariable<>-1";
		
		if(queryRack)
			sql += " union "+
				"select distinct rack_v.idvariable from fsrack "+
				" inner join cfvariable rack_v on rack_v.iddevice=fsrack.iddevice and rack_v.iscancelled='FALSE' and rack_v.idhsvariable is not null and rack_v.idhsvariable<>-1 "+
				" inner join fsutil on fsrack.idrack=fsutil.idrack "+
				" inner join cfvariable as util_v on util_v.idvariable=fsutil.solenoid "+
				" inner join cfvariable as util_alarm on util_alarm.iddevice=util_v.iddevice and util_alarm.idvariable in ("+sb.substring(0, sb.length() - 1) +")" +
				" order by idvariable ";
		RecordSet rs = null;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		if (null == rs) {
			return null;
		}
		int[] idvar2 = new int[rs.size()];
		for (int j = 0; j < rs.size(); j++) {
			idvar2[j] = ((Integer) rs.get(j).get(0)).intValue();
		}
		Variable[] variables = null;
		try {
			variables = ControllerMgr.getInstance().getFromField(idvar2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] infos = new String[0];
		Object[][] logData = new Object[idvar2.length][];
		Map map = getDescValue(language, idSite, idvar2);
		for (int i = 0; i < logData.length; i++) {
			logData[i] = new Object[3];
			Variable t = variables[i];
			infos = (String[]) map.get(t.getInfo().getId());
			logData[i][0] = (infos != null ? infos[1] : " ");
			logData[i][1] = (infos != null ? infos[0] : " ");
			logData[i][2] = (Float.isNaN(t.getCurrentValue())?"***":t.getCurrentValue() + (infos != null ? infos[2]: " "));
		}
		return logData;
	}

    public static Map<String, String[]> getDescValue(String language, int idSite, int[] idvar)
    {
      HashMap result = new HashMap();
      RecordSet rs = null;
      Record r = null;
      String sql = "";
      Integer s1 = null;
      String s2 = "";
      String s3 = "";
      String s4 = "";
      sql = "select var.idvariable as uno, var.measureunit as ms , tvar.description as due,tdev.description as tre  from cfvariable as var,cftableext as tvar,cftableext as tdev where var.idvariable in (";

      StringBuffer sb = new StringBuffer();
      Object[] param = new Object[idvar.length + 6];
      int idx = 0;

      for (int i = 0; i < idvar.length; i++)
      {
        sb.append("?");

        if (i != idvar.length - 1)
        {
          sb.append(",");
        }

        param[idx] = new Integer(idvar[i]);
        idx++;
      }

      param[(idx++)] = new Integer(idSite);
      param[(idx++)] = language;
      param[(idx++)] = "cfvariable";
      param[(idx++)] = new Integer(idSite);
      param[(idx++)] = language;
      param[(idx++)] = "cfdevice";

      sql = sql + sb.toString();
      sql = sql + ") and tvar.idsite=? and tvar.languagecode=? and tvar.tablename=? and tvar.tableid=var.idvariable and tdev.idsite=? and tdev.languagecode=? and tdev.tablename=? and tdev.tableid=var.iddevice";
      try
      {
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs != null)
        {
          for (int i = 0; i < rs.size(); i++)
          {
            r = rs.get(i);

            if (r == null)
              continue;
            s1 = (Integer)r.get("uno");
            s2 = UtilBean.trim(r.get("due"));
            s3 = UtilBean.trim(r.get("tre"));
            s4 = UtilBean.trim(r.get("ms"));
            result.put(s1, new String[] { s2, s3, s4 });
          }
        }

      }
      catch (Exception e)
      {
        Logger logger = LoggerMgr.getLogger(VariableHelper.class);
        logger.error(e);
      }

      return result;
    }


	//	@Override
	public File generate() {
		String language = ""+parameters.get(LANGUAGE);
		LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
		tableAlarm = createTableAlarm(language,(Integer)parameters.get(IDSITE),(int[])parameters.get(VARIABLES));
		SystemConf logEnable = SystemConfMgr.getInstance().get("email_log");
	    if (logEnable != null)
	    {
	    	if (logEnable.getValue().equals("TRUE"))
	    	{
	    		this.tableLog = createTableLog(FSManager.getStart().intValue() == 1,language, ((Integer)this.parameters.get(IDSITE)).intValue(), (int[])this.parameters.get(VARIABLES));
	    	}
	    }
		parameters.put(TIME, new Timestamp(System.currentTimeMillis()));
		parameters.put(TITLE, lang.getString("report", "alarmtit"));
		ll = new LinkedList<AlarmBean>();
		for(int i=0; i<tableAlarm.length;i++){
			ll.add(new AlarmBean((String)tableAlarm[i][0],(Timestamp)tableAlarm[i][1],(String)tableAlarm[i][2],(String)tableAlarm[i][3]));
		}
		
		parameters.put(I18NDESCALR,lang.getString("report", "alarmtit"));
		parameters.put(I18NDESC,lang.getString("report", I18NDESC));
		parameters.put(I18NDATE,lang.getString("report", I18NDATE));
		parameters.put(I18NTIME,lang.getString("report", I18NTIME));
		parameters.put(I18NTYPE,lang.getString("report", I18NTYPE));
		parameters.put(I18NTIMESTAMP,lang.getString("report", I18NTIMESTAMP));
		parameters.put(I18NDEVICE,lang.getString("report", I18NDEVICE));
		parameters.put(I18NALR,lang.getString("report", I18NALR));
		parameters.put(I18NPAGE,lang.getString("report", I18NPAGE));
		parameters.put(I18NOF,lang.getString("report", I18NOF));
		String strAppHome = BaseConfig.getAppHome();
		String strLogo = ProductInfoMgr.getInstance().getProductInfo().get("imgtop");
		parameters.put(Report.LOGO, strAppHome + "images\\" + strLogo);
		try {
			PrinterMgr2 p = PrinterMgr2.getInstance();
			JRBeanCollectionDataSource bcd = new JRBeanCollectionDataSource(ll);
			File f = p.export(p.fill("PDF_Alarm", parameters, bcd), "PDF", "ALARM_"+String.valueOf(System.currentTimeMillis()));
			
			/*
             *  STAMPA AUTOMATICA SU STAMPANTE FISICA IN PRINTALRM.INI (config nome stampante e percorso exe AcrobatReader)
             */
			// DYN: 12/05/2010 printing removed at all; there is no need for printing at this level
			// DispPDFPrinter.printAlarmFile(f.getAbsolutePath());
            /* DYN: 17/11/2009 code replaced with DispPDFPrinter
			String path = f.getAbsolutePath();
        	String printer = ""+"";
            String acrobat_exe = "";
            Properties prop = new Properties();
            InputStream in = null;
            try
            {
                in = new FileInputStream(BaseConfig.getCarelPath()+  "Services" + File.separator + "printalrm.ini"); 
                prop = new Properties();
                prop.clear();
                prop.load(in);
                in.close();
            }
            catch (Exception e) {
                LoggerMgr.getLogger(PAction.class).error(e);
            }
                                    
            if (p != null)
            {
                printer = prop.getProperty("printer");
                acrobat_exe = prop.getProperty("path");
                if (printer!=null && !printer.equals(""))
                {
                    try 
                    {
                        //String file_to_print = BaseConfig.getCarelPath() + "PvPro" + File.separator+path;
                    	String file_to_print = path;
                    	ScriptInvoker script = new ScriptInvoker();
                        
                    	// This line to print with Adobe Reader
	                    //String[] batch = new String[]{acrobat_exe, "/n", "/t", "\""+file_to_print+"\"", "\""+printer+"\""};
	                    
	                    // This line to print with Foxit Reader
	                    String[] batch = new String[]{acrobat_exe, "/t", "\""+file_to_print+"\"", "\""+printer+"\""};
                        
                    	script.execute(batch, "C:\\Carel\\PlantVisorPRO\\log\\Carel.log", true);
                    } 
                    catch (Exception e)
                    {
                        LoggerMgr.getLogger(PAction.class).error(e);
                    }
                }
            }
        	*/
            /*
             *  FINE STAMPA AUTOMATICA ALLARMI
             */ 
			return f; 
		}catch(Exception e){
			LoggerMgr.getLogger(this.getClass()).error(e);
			return null;
		}
	}

	public String getFileName() {
		return getFile().getName();
	}

	public Object[][] getTableAlarm() {
		return tableAlarm;
	}
	
	public Object[][] getTableLog() {
	    return this.tableLog;
	}
}
