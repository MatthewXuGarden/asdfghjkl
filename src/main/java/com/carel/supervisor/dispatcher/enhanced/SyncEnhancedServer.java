package com.carel.supervisor.dispatcher.enhanced;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.carel.supervisor.base.log.LoggerMgr;

public class SyncEnhancedServer extends Thread
{
	private boolean Terminate = false;
	private int Port = 2016;
	private PVEShield pves = null;

	public SyncEnhancedServer(String name)
	{
		super.setName(name);
		pves = new PVEShield();
	}

	/**
	 * @param args
	 */
	public void run()
	{
		Socket client = null;

		try
		{
			ServerSocket server = new ServerSocket(Port);

			while (!Terminate)
			{
				System.out.println("In attesa di connessioni...");
				client = server.accept();
				
				if (client.getInetAddress().equals(InetAddress.getLocalHost()))
				{
					InputStream is = client.getInputStream();
					byte[] buffer = new byte[32];
					is.read(buffer);
					if (new String(buffer).trim().equalsIgnoreCase("pauseserversocket"))
					{
						if(pves != null)
						{
							if(pves.getCounter() > 0)
							{
								pves.resetCounter();
							}
							else
							{
								pves = new PVEShield();
							}
							
							if(!pves.isStarted())
								pves.startPoller();
						}
						else
						{
							pves = new PVEShield();
							pves.startPoller();
						}
					}
				}
				
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
				
				if(!pves.isEnable())
				{
					System.out.println("Connessione da: "+client.getInetAddress().toString());
					LoggerMgr.getLogger(this.getClass()).info("Connection from: "+client.getInetAddress().toString());
					SyncEnhancedThread st = new SyncEnhancedThread(client, client.getInetAddress().toString());
					st.start();
				}
				else
				{
					System.out.println("PVE blocked");
					LoggerMgr.getLogger(this.getClass()).info("PVE blocked");
				}
			}
		} catch (IOException e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
		} 
	}

	public static void main(String[] args)
	{
		SyncEnhancedServer ses = new SyncEnhancedServer("Enhanced Server");
		ses.start();

		while (!ses.Terminate)
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean isTerminate()
	{
		return Terminate;
	}

	public synchronized void setTerminate(boolean terminate)
	{
		Terminate = terminate;
	}

	public synchronized int getPort()
	{
		return Port;
	}

	public synchronized void setPort(int port)
	{
		Port = port;
	}
}
