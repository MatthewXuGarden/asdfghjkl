package com.carel.supervisor.dispatcher.engine.print;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.action.PAction;
import com.carel.supervisor.dispatcher.comm.external.ExternalPrint;
import com.carel.supervisor.presentation.io.CioPrinter;


public class DispPDFPrinter
{
    public static boolean printFile(String pathFile)
    {
        boolean ret = true;

        //String[] s = {pathFile};
        try
        {
            //PrintPDF.main(s);
            ExternalPrint ePrint = new ExternalPrint(pathFile);
            ret = ePrint.send();
        }
        catch (Exception e)
        {
            ret = false;

            Object[] prm = { pathFile };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action", EventDictionary.TYPE_INFO,
                "D026", prm);
        }

        return ret;
    }
    
    public static boolean printReportFile(String pathFile)
    {
    	CioPrinter cio = new CioPrinter(1);
    	if( cio.isReportPrinter() ) {
    		int filterResponse = filter(pathFile); 
        	if( filterResponse == 0 ) {
	    		try {
		    		ScriptInvoker script = new ScriptInvoker();
		            // log path should changed to a printer log file
	                return script.execute(cio.getReportPrinterCmd(pathFile), "C:\\Carel\\PlantVisorPRO\\log\\Carel.log", false) == 0;
	            } catch(Exception e) {
	            	LoggerMgr.getLogger(DispPDFPrinter.class).error(e);	
	            }
        	}
        	else if( filterResponse > 0 ) {
                Object[] prm = { pathFile.substring(pathFile.lastIndexOf("\\")+1) };
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action", EventDictionary.TYPE_WARNING, "D059", prm);
        	}
    	}
    	return false;
    }
    
    public static boolean printAlarmFile(String pathFile)
    {
    	CioPrinter cio = new CioPrinter(1);
    	if( cio.isAlarmPrinter() ) {
    		int filterResponse = filter(pathFile); 
        	if( filterResponse == 0 ) {
        		try {
		    		ScriptInvoker script = new ScriptInvoker();
		            // log path should changed to a printer log file
	                return script.execute(cio.getAlarmPrinterCmd(pathFile), "C:\\Carel\\PlantVisorPRO\\log\\Carel.log", false) == 0;
	            } catch(Exception e) {
	            	LoggerMgr.getLogger(DispPDFPrinter.class).error(e);	
	            }
        	}
        	else if( filterResponse > 0 ) {
                Object[] prm = { pathFile.substring(pathFile.lastIndexOf("\\")+1) };
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action", EventDictionary.TYPE_WARNING, "D059", prm);
        	}
    	}
    	return false;
    }
    
    public static int filter(String pathfile)
    {
    	if( !pathfile.toLowerCase().endsWith(".pdf") )
    		return 1;
    	if( pathfile.endsWith("PDF_Alive.pdf") )
    		return -1;
    	return 0;
    }
}
