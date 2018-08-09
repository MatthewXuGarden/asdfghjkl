package com.carel.supervisor.field;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.poi.hpsf.Util;

import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class FileMgr
{
	private static final int INIT_ERR = -1;
	private static final int NOT_INIT = 0;
	private static final int INIT_OK = 1;
	
	private static FileMgr singleton = null;
	private static int initialized = NOT_INIT;
	private Logger logger;
	private Vector<Integer> v;
	private Properties prop;
	private LinkedList<FileRecord> ll = new LinkedList<FileRecord>();
	private static final String PROPERTIES = "/scheduler/conf/manager.properties";
	private static final String SENDPATH = "sourcepath";
	private static final String CHUNKSIZE = "chunksize";
	private static final String FILECOUNT = "filecount";
	private static final String FILEDATE = "filedate";

	private FileMgr()
	{
		logger = LoggerMgr.getLogger(this.getClass());
		prop = new Properties();
		try
		{
			prop.load(new FileInputStream(new File(System.getenv("PVPRO_HOME") + PROPERTIES)));
			reload();
			initialized = INIT_OK;
		} catch (FileNotFoundException e)
		{
			logger.error("Properties file not found", e);
			initialized = INIT_ERR;
		} catch (IOException e)
		{
			logger.error("Properties file not found", e);
			initialized = INIT_ERR;
		}
	}

	public static synchronized FileMgr getInstance()
	{
		if(initialized == INIT_ERR)
			return null;
		if (singleton == null || initialized==NOT_INIT)
			singleton = new FileMgr();
		return singleton;
	}

	public synchronized void putValue(int idvar, Float value)
	{
		if (v.contains(new Integer(idvar)))
		{
			ll.add(new FileRecord(idvar, new Timestamp(System.currentTimeMillis()), value));
		}
	}

	public synchronized void submitToFile()
	{
		if (ll.size() == 0)
			return;
		try
		{
			File f = this.getCurrentFile();
			FileOutputStream fos = new FileOutputStream(f, true);
			for (int x = 0; x < ll.size(); x++)
			{
				fos.write(ll.get(x).toString().concat(System.getProperty("line.separator")).getBytes());
			}
			ll.clear();
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e)
		{
			logger.error("File error", e);
			e.printStackTrace();
		} catch (IOException e)
		{
			logger.error("File error", e);
			e.printStackTrace();
		}
	}

	private synchronized File getCurrentFile()
	{
		File tmp = null;
		try
		{
			IProductInfo pi = ProductInfoMgr.getInstance().getProductInfo();

			Calendar c = Calendar.getInstance();
			String yy = "" + c.get(Calendar.YEAR);
			String mm = "" + (c.get(Calendar.MONTH) < 9 ? "0" : "") + (c.get(Calendar.MONTH) + 1);
			String dd = "" + (c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + c.get(Calendar.DAY_OF_MONTH);
			String hh = "" + (c.get(Calendar.HOUR_OF_DAY));

			if(pi.get(FILECOUNT)==null)
				pi.store(FILECOUNT,"0");
			if(pi.get(FILEDATE)==null)
				pi.store(FILEDATE, ""+yy+mm+dd+hh);

//			if (new File(System.getenv("PVPRO_HOME") + prop.getProperty(SENDPATH)).isDirectory() &&
//							new File(System.getenv("PVPRO_HOME") + prop.getProperty(SENDPATH)).listFiles(new MyFileFilter()).length == 0)
//			{
//				pi.set(FILECOUNT, "0");
//			}

			if(!pi.get(FILEDATE).substring(0,8).equals(yy+mm+dd))
			{
				pi.set(FILECOUNT, "0");
				pi.set(FILEDATE, yy+mm+dd+hh);
			}
			long counter = Integer.parseInt(pi.get(FILECOUNT));
			
			String filename = "" + yy + mm + dd /* + hh */+ "-" + new DecimalFormat("0000000000").format(counter) + ".txt";
			tmp = new File(System.getenv("PVPRO_HOME") + prop.getProperty(SENDPATH) + filename);
			if ((tmp.exists() && tmp.length() > Long.parseLong(prop.getProperty(CHUNKSIZE))) || !pi.get(FILEDATE).equals(""+yy+mm+dd+hh))
			{
				filename = "" + yy + mm + dd /* + hh */+ "-" + new DecimalFormat("0000000000").format(counter + 1) + ".txt";
				tmp = new File(System.getenv("PVPRO_HOME") + prop.getProperty(SENDPATH) + filename);
				try
				{
					if(checkIsDelete()){ 
						String path = System.getenv("PVPRO_HOME") + prop.getProperty(SENDPATH);
						deleteDirLimitNum(path,5000);
					}
					pi.set(FILECOUNT, "" + (counter + 1));
					pi.set(FILEDATE, yy+mm+dd+hh);
				} catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error("Productinfo set A", e);
					e.printStackTrace();
				}
			}
		} catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error("Productinfo set B", e);
			e.printStackTrace();
		}
		return tmp;
	}
	public static int runHours = 24;
	public static int runMinutes = 10;
	public Date nextRunTime = nextRunTime();
	public Date nextRunTime(){
		Date currentTime = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		cal.add(Calendar.MINUTE , runMinutes);
		nextRunTime = cal.getTime();	
		return nextRunTime;
	}
	
	public Date nextTime(){
		Date currentTime = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		cal.add(Calendar.HOUR, runHours);
		nextRunTime = cal.getTime();	
		return nextRunTime;
	}
	public boolean checkIsDelete(){
		boolean result = false;
		Date currentTime = new Timestamp(System.currentTimeMillis());
		if(currentTime.after(nextRunTime)){
			result = true;
			nextTime();
		}
		return result;
	}
	
	public static void deleteDirLimitNum(String path, int num)
    {
        File f = new File(path);
        if(!f.exists())
        	return;
        File[] list = f.listFiles(new FilenameFilter(){
        	public boolean accept(File dir, String name){
        		return name.indexOf("20")!=-1;
        	}	
        });
        Arrays.sort(list);
        int n=list.length-num;
        if(n>=0){
        	for (int i = 0; i < n; i++){   		
        		list[i].delete();	
        	}
        }
    } 
	
	public synchronized void reload()
	{
		String sql = "SELECT * FROM cftransfervar";
		RecordSet rs;
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			v = new Vector<Integer>(rs.size());
			for (int i = 0; i < rs.size(); i++)
			{
				v.add((Integer) rs.get(i).get("idvar"));
			}
			//add by Kevin. also reload property(configuration) file
			prop = new Properties();
			prop.load(new FileInputStream(new File(System.getenv("PVPRO_HOME") + PROPERTIES)));
		} catch (DataBaseException e)
		{
			logger.error("Database error", e);
			e.printStackTrace();
		} catch (Exception e)
		{
			logger.error("Other error retrieving ids", e);
			e.printStackTrace();
		}
	}

	private class FileRecord
	{
		private int idvar;
		private Timestamp ts;
		private Float val;

		public FileRecord(int idvar, Timestamp ts, Float val)
		{
			this.idvar = idvar;
			this.ts = ts;
			this.val = val != null ? new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() : null;
		}

		public String toString()
		{
			return "" + idvar + "\t" + ts.toString() + "\t" + val;
		}
	}
	
	class MyFileFilter implements FileFilter
	{

		public MyFileFilter()
		{
			super();
		}

		public boolean accept(File f)
		{
			Calendar c = Calendar.getInstance();
			String yy = "" + c.get(Calendar.YEAR);
			String mm = "" + (c.get(Calendar.MONTH) < 9 ? "0" : "") + (c.get(Calendar.MONTH) + 1);
			String dd = "" + (c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + c.get(Calendar.DAY_OF_MONTH);
			if (Pattern.compile("" + yy + mm + dd + "-\\d{10}.txt").matcher(f.getName()).matches())
			{
				return true;
			} else
				return false;
		}

	}
}
