package com.carel.supervisor.presentation.io;

import java.io.*;
import java.util.Properties;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.LoggerMgr;


public class CioPrinter 
{
	private int idsite						= 0;
	private static String strDefaultPath	= "C:\\Program Files\\Foxit Software\\Foxit Reader\\Foxit Reader.exe";
	private boolean bReportPrinter			= false;
	private String strReportPrinter			= "";
	private String strReportPrinterPath		= "";
	private boolean bAlarmPrinter			= false;
	private String strAlarmPrinter			= "";
	private String strAlarmPrinterPath		= "";
	private static final String strFaxSgn	= "Fax";	// used to remove fax from the printer list
	private static final String strFoxitSgn	= "Foxit";	// used to identify FoxitReader tool
	private static final String strAdobeSgn	= "Adobe";	// used to identify AdobeReader tool

	
	public CioPrinter(int idsite) {
		this.idsite = idsite;
		loadConfiguration();
	}
	public CioPrinter() {
	}
	public boolean isReportPrinter()
	{
		return bReportPrinter;
	}
	
	public String getReportPrinter()
	{
		return strReportPrinter;
	}
	
	public String getReportPrinterPath()
	{
		return strReportPrinterPath;
	}
	
	public String[] getReportPrinterCmd(String fileName)
	{
		return getPrinterCmd(strReportPrinterPath, strReportPrinter, fileName);
	}	
	
	public boolean isAlarmPrinter()
	{
		return bAlarmPrinter;
	}
	
	public String getAlarmPrinter()
	{
		return strAlarmPrinter;
	}
	
	public String getAlarmPrinterPath()
	{
		return strAlarmPrinterPath;
	}	
	
	public String[] getAlarmPrinterCmd(String fileName)
	{
		return getPrinterCmd(strAlarmPrinterPath, strAlarmPrinter, fileName);
	}	
	
	
	public void loadConfiguration()
	{
		Properties p = null;
		InputStream in = null;
		try	{
			// report printer
			in = new FileInputStream(BaseConfig.getCarelPath()+"Services"+File.separator+"printer.ini"); 
			p = new Properties();
			p.load(in);
			in.close();
			strReportPrinter = p.getProperty("printer");
			bReportPrinter = (strReportPrinter != null) && (strReportPrinter.length()) > 0;
			if( !bReportPrinter )
				strReportPrinter = p.getProperty("printer_selection");
			if( strReportPrinter == null )
				strReportPrinter = "";
			strReportPrinterPath = p.getProperty("path");
			if( (strReportPrinterPath == null) || (strReportPrinterPath.length() <= 0) )
				strReportPrinterPath = strDefaultPath;
			// alarm printer
			in = new FileInputStream(BaseConfig.getCarelPath()+"Services"+File.separator+"printalrm.ini"); 
			p = new Properties();
			p.load(in);
			in.close();
			strAlarmPrinter = p.getProperty("printer");
			bAlarmPrinter = (strAlarmPrinter != null) && (strAlarmPrinter.length() > 0);
			if( !bAlarmPrinter )
				strAlarmPrinter = p.getProperty("printer_selection");
			if( strAlarmPrinter == null )
				strAlarmPrinter = "";
			strAlarmPrinterPath = p.getProperty("path");
			if( (strAlarmPrinterPath == null) || (strAlarmPrinterPath.length() <= 0) )
				strAlarmPrinterPath = strDefaultPath;
		}
		catch (Exception e) {
			LoggerMgr.getLogger(CioPrinter.class).error(e);
		}
	}
	
	public boolean saveConfiguration(boolean bReportPrinter, String strReportPrinter, boolean bAlarmPrinter, String strAlarmPrinter)
	{
		Properties p = null;
		OutputStream out = null;
		try	{
			// report printer
			p = new Properties();
			p.setProperty(bReportPrinter ? "printer" : "printer_selection", strReportPrinter);
			if( !bReportPrinter )
				p.setProperty("printer", "");
			p.setProperty("path", strReportPrinterPath);
			out = new FileOutputStream(BaseConfig.getCarelPath()+"Services"+File.separator+"printer.ini"); 
			p.store(out, "report printer");
			out.close();
			// alarm printer
			p = new Properties();
			p.setProperty(bAlarmPrinter ? "printer" : "printer_selection", strAlarmPrinter);
			if( !bAlarmPrinter )
				p.setProperty("printer", "");
			p.setProperty("path", strAlarmPrinterPath);
			out = new FileOutputStream(BaseConfig.getCarelPath()+"Services"+File.separator+"printalrm.ini"); 
			p.store(out, "alarm printer");
			out.close();
			return true;
		}
		catch (Exception e) {
			LoggerMgr.getLogger(CioPrinter.class).error(e);
			return false;
		}
	}
	//save only ReportPrinter
	//By Kevin
	public void SaveConfigurationReportPrinter(boolean bReportPrinter,String strReportPrinter)
		throws Exception
	{
		Properties p = null;
		OutputStream out = null;
		p = new Properties();
		p.setProperty(bReportPrinter ? "printer" : "printer_selection", strReportPrinter);
		if( !bReportPrinter )
			p.setProperty("printer", "");
		p.setProperty("path", strReportPrinterPath);
		out = new FileOutputStream(BaseConfig.getCarelPath()+"Services"+File.separator+"printer.ini"); 
		p.store(out, "report printer");
		out.close();
	}
	public boolean removeConfiguration()
	{
		Properties p = null;
		OutputStream out = null;
		try	{
			// report printer
			p = new Properties();
			p.setProperty("printer", "");
			p.setProperty("path", "");
			out = new FileOutputStream(BaseConfig.getCarelPath()+"Services"+File.separator+"printer.ini"); 
			p.store(out, "report printer");
			out.close();
			// alarm printer
			p = new Properties();
			p.setProperty("printer", "");
			p.setProperty("path", "");
			out = new FileOutputStream(BaseConfig.getCarelPath()+"Services"+File.separator+"printalrm.ini"); 
			p.store(out, "alarm printer");
			out.close();
			return true;
		}
		catch (Exception e) {
			LoggerMgr.getLogger(CioPrinter.class).error(e);
			return false;
		}
	}

	
	public static String GetDefaultPrinter() { 
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		if( printService != null ) {
			String name = printService.getName();
			return isFax(name) ? "" : name;
		}
		else {
			return "";
		}
	}
	

	public static String[] GetPrinters()
	{
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        String astrPrinters[] = new String[getNoOfPrinters(printServices)];
		for(int i = 0, j = 0; i < printServices.length; i++) {
			String name = printServices[i].getName();
			if( !isFax(name) )
				astrPrinters[j++] = name;
		}
		if( astrPrinters.length > 1 )
			java.util.Arrays.sort(astrPrinters);
		return astrPrinters;
	}

	
	public static String[] GetPrintersAndFaxes()
	{
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        String astrPrinters[] = new String[printServices.length];
		for(int i = 0; i < printServices.length; i++)
			astrPrinters[i] = printServices[i].getName();
		if( astrPrinters.length > 1 )
			java.util.Arrays.sort(astrPrinters);
		return astrPrinters;
	}

	
	private static int getNoOfPrinters(PrintService[] printServices)
	{
		int n = 0;
		for(int i = 0; i < printServices.length; i++)
			if( !isFax(printServices[i].getName()) )
				n++;
		return n;
	}
	
	
	private static boolean isFax(String name)
	{
		return name.equalsIgnoreCase(strFaxSgn) || name.startsWith(strFaxSgn) || name.startsWith(strFaxSgn.toUpperCase());
	}
	
	
	private String[] getPrinterCmd(String printerPath, String printerName, String fileName)
	{
		String cmd[];
		if( printerPath.toUpperCase().contains(strAdobeSgn.toUpperCase()) ) {
            cmd = new String[] {
            	printerPath,
            	"/n",
            	"/t",
            	"\"" + fileName + "\"",
            	"\"" + printerName +"\""
            };
		}
		else {
            cmd = new String[] {
            	printerPath,
            	"/t",
            	"\"" + fileName + "\"",
            	"\"" + printerName +"\""
            };
		}
		return cmd;
	}
	/**simon add 2010-3-3
	 * this will output the printer list for diagnose function(outside batch file).
	 * @param argv
	 * @throws Exception
	 */
	  public static void main(String[] argv) throws Exception {
		CioPrinter cp = new CioPrinter();
		String[] t = cp.GetPrinters();
		System.out.println("[Printer List]");
		if (t != null) {
			System.out.println("Printer counts:" + t.length);
			for (int i = 0; i < t.length; i++) {
				System.out.println("-Printer" + (i + 1) + ": " + t[i]);
			}
		}
	}
}
