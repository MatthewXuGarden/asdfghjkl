package com.carel.supervisor.dispatcher.comm.external;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dispatcher.DispatcherMgr;


public class ExternalPrint extends External
{
    private String pathFile = "";

    public ExternalPrint(String path)
    {
        super(0, "", "P");
        this.pathFile = path;
    }

    protected String[] getParams()
    {
        return new String[]
        {
            "java", "-classpath",
            this.pathex + "PrintService.jar;" + this.pathex + "DispatcherLight.jar;" + this.pathex +
            "pdfbox.jar;", "com.carel.supervisor.service.Starter", this.pathFile
        };
    }

    protected String getLogFile()
    {
        return "PrintService.log";
    }

    // Sovrascritto
    public boolean send()
    {
        int port = 1979;
        try{
        	port = Integer.parseInt(BaseConfig.getProductInfo("print"));
        }
        catch(Exception e){
        }

        try
        {
            port = Integer.parseInt(DispatcherMgr.getInstance().getPrintServerPort());
        }
        catch (Exception e)
        {
        }

        Socket s = null;
        OutputStreamWriter os = null;
        InputStreamReader is = null;
        StringBuffer sb = new StringBuffer();
        boolean ris = false;
        int c = -1;

        try
        {
            s = new Socket("localhost", port);
            os = new OutputStreamWriter(s.getOutputStream());
            is = new InputStreamReader(s.getInputStream());

            os.write(this.pathFile);
            os.flush();
            s.shutdownOutput();

            while ((c = is.read()) != -1)
            {
                sb.append((char) c);
            }

            if ((sb.toString() != null) && sb.toString().equalsIgnoreCase("OK"))
            {
                ris = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (Exception e)
            {
            }

            try
            {
                os.close();
            }
            catch (Exception e)
            {
            }

            try
            {
                s.close();
            }
            catch (Exception e)
            {
            }
        }

        return ris;
    }
}
