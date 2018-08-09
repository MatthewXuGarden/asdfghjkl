package com.carel.supervisor.base.io;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;

public class SocketComm 
{
	public static String sendCommand(String host,int port,String cmd)
	{
		StringBuffer sb = new StringBuffer();
		OutputStreamWriter os = null;
		InputStreamReader is = null;
		Socket s = null;
		int c = -1;
		
		try 
		{
			s = new Socket(host,port);
			os = new OutputStreamWriter(s.getOutputStream());
			is = new InputStreamReader(s.getInputStream());
			os.write(cmd);
			os.flush();
			s.shutdownOutput();
			
			while((c = is.read())!= -1)
				sb.append((char)c);
		} 
		catch(Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(SocketComm.class);
			logger.error("Connection with: "+host+" on port: "+port+" refused; cmd: "+cmd+". Please check services");
			sb = new StringBuffer("ERROR");
		} 
		finally
		{
			try {
				is.close();
			}
			catch(Exception e){
				Logger logger = LoggerMgr.getLogger(SocketComm.class);
				logger.error("[SocketComm]-[sendCommand] [Error in close InputStream]");
			}
			
			try {
				os.close();
			}
			catch(Exception e){
				Logger logger = LoggerMgr.getLogger(SocketComm.class);
				logger.error("[SocketComm]-[sendCommand] [Error in close OutputStream]");
			}
			
			try {
				s.close();
			}
			catch(Exception e){
				Logger logger = LoggerMgr.getLogger(SocketComm.class);
				logger.error("[SocketComm]-[sendCommand] [Error in close Socket]");
			}
		}
		
		return sb.toString();
	}
}
