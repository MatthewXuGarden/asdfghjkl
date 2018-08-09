package com.carel.supervisor.dispatcher.plantwatch1;
import java.io.*;
import java.net.*;

import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;

public class PlantWatchComm {
	
	static int BUFFSIZE = 5000;         
	private static PlantWatchComm me = null;
	private Socket client = null;
	private String Address = "127.0.0.1";
	private int    Port = 2012;
	DatagramSocket recv_sock = null, send_sock = null;
	private static final int SIZE = 1680;
	
	private BufferedOutputStream output = null;
	private BufferedInputStream  input = null;
 	
	private PlantWatchComm()
	{
		try {
			client = new Socket(Address , Port);
			output = new BufferedOutputStream(client.getOutputStream(),BUFFSIZE);
			input =  new BufferedInputStream(client.getInputStream(), BUFFSIZE);
//			 allocate the datagram socket
			try {
				recv_sock = new DatagramSocket();
				send_sock = new DatagramSocket();
			} 
			catch (SocketException se) {
				se.printStackTrace();
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	public static synchronized PlantWatchComm getIstance()
	{
		if(me==null)
		{
			me = new PlantWatchComm();
		}
		return me;
		
	};
	
	

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public int getPort() {
		return Port;
	}

	public void setPort(int port) {
		Port = port;
	};
	
	public boolean SendRecord(PlantWatchCommRec rec)
	{
		try {
			
			ByteArrayOutputStream bytestream;
			bytestream = new ByteArrayOutputStream(SIZE);//1148 d' la dim in byte della struttura da inviare
			DataOutputStream out;
			out = new DataOutputStream(bytestream);
			out.writeInt( Integer.reverseBytes(rec.MessageId) );
			out.writeInt( Integer.reverseBytes(rec.Unit ) );
			out.writeInt( Integer.reverseBytes(rec.Address) );
			out.writeLong( Long.reverseBytes(rec.lValue));
			out.write ( rec.bValue);
			out.writeDouble(rec.dValue);
			out.write ( rec.VarType);
			out.write( rec.SerialModem);
			out.write( rec.Result);
			out.writeInt( Integer.reverseBytes(rec.CommPort));
			for(int i=0;i<200;i++)
				out.write( rec.Tel[i]);
			for(int i=0;i<200;i++)
				out.write( rec.UserName[i]);
			for(int i=0;i<200;i++)
				out.write( rec.Password[i]);
			for(int i=0;i<512;i++)
				out.write( rec.DirPath[i]);
			for(int i=0;i<512;i++)
				out.write( rec.Modem[i]);
			out.writeInt( Integer.reverseBytes(rec.Ident) );
			out.writeLong( Long.reverseBytes(rec.FromDate) );
			out.writeLong( Long.reverseBytes(rec.ToDate) );
				
			//System.out.println(bytestream.size());
			output.write(bytestream.toByteArray(), 0, bytestream.size());
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean ReadData(PlantWatchCommRec rec, int SecTimeOut)
	{
		int count = SecTimeOut;
		while(count>0)
		{
			if( ReadData(rec))
				break;
			try {
				Thread.sleep( 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count--;
			if(count==0)
				return false;
		}
		return true;
	}
	public boolean ReadData(PlantWatchCommRec rec)
	{
		try 
		{   //Se quello che leggo e' piu' piccolo di un record allora esco.
			if(input.available() < (int)SIZE)//in questo modo la funzione non é bloccante
				return false;
			ByteArrayInputStream bytestream;
			byte buffer[] = new byte[SIZE]; 
			input.read(buffer, 0, SIZE);
			bytestream = new ByteArrayInputStream(buffer);//1148 d' la dim in byte della struttura da inviare
			DataInputStream in;
			in = new DataInputStream(bytestream);
			rec.MessageId = in.readInt(); 
			rec.MessageId = Integer.reverseBytes(rec.MessageId);
			rec.Unit = in.readInt(); 
			rec.Unit= Integer.reverseBytes(rec.Unit);
			rec.Address= in.readInt(); 
			rec.Address= Integer.reverseBytes(rec.Address);
			rec.lValue = in.readLong(); 
			rec.lValue= Long.reverseBytes(rec.lValue);
			rec.bValue = in.readByte();
			rec.dValue = in.readDouble();
			rec.VarType = in.readByte();
			rec.SerialModem = in.readByte();
			rec.Result = in.readByte();
			rec.CommPort = in.readInt(); 
			rec.CommPort = Integer.reverseBytes(rec.CommPort);
			for(int i=0;i<200;i++)
				rec.Tel[i] = (char)in.readByte(); 
			for(int i=0;i<200;i++)
				rec.UserName[i]= (char)in.readByte();
			for(int i=0;i<200;i++)
				rec.Password[i]= (char)in.readByte();
			for(int i=0;i<512;i++)				
				rec.DirPath[i]= (char)in.readByte();
			for(int i=0;i<512;i++)				
				rec.Modem[i]= (char)in.readByte();
			rec.Ident = in.readInt(); 
			rec.Ident = Integer.reverseBytes(rec.Ident);
			rec.FromDate = in.readLong(); 
			rec.FromDate = Long.reverseBytes(rec.FromDate);
			rec.ToDate = in.readLong(); 
			rec.ToDate= Long.reverseBytes(rec.ToDate);
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
			
	}
	public void finalize() 
	{
		if(client!=null)
		{
			// TODO Aggiungere messaggio chiusura applicazione  PlantWatch 
			
			try {
				output = null;
				input =  null;
  			    recv_sock = null;
				send_sock = null;
				client.close();
				me = null;
				super.finalize();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			} 
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dirpath = "c:/temp";
		String Tel = "3382439042";
		
		PlantWatchCommRec rec = new PlantWatchCommRec();
		//Devo copiare carattere x volta o Java cambia le dimensione degli array
		for(int i = 0; i<dirpath.toCharArray().length; i++ )
			rec.DirPath[i] = dirpath.charAt(i);
		for (int i=0; i<rec.DirPath.length ; i++)
			System.out.println(rec.DirPath[i] + " ");

		//rec.Tel = Tel.toCharArray();
		/*
		for (int i=0; i<rec.Tel.length ; i++)
			System.out.print(rec.Tel[i] + " ");
		String s = new String(rec.Tel );
		byte b[] = s.getBytes(); 
		for (int i=0; i<b.length ; i++)
			System.out.print(b[i] + " ");
		*/
		PlantWatchComm pw = PlantWatchComm.getIstance();
		rec.MessageId = PlantWatchIDs.REQ_CONNECTION ;
		rec.Address = 0;
		rec.bValue = 0;
		rec.lValue = 0;
		rec.Result = 0;
		rec.dValue = 0;
		rec.CommPort = 1;
		rec.SerialModem = 1;
		rec.Ident = 1;
		String UserName = "RemoteUser";
		for(int i = 0; i<UserName.toCharArray().length; i++ )
			rec.UserName[i] = UserName.charAt(i);
		String Password = "4";
		for(int i = 0; i<Password.toCharArray().length; i++ )
			rec.Password[i] = Password.charAt(i);

		//rec.dValue = -154.2;
		//rec.Unit = 1;
		//rec.bValue = 1;
		//rec.Result = 1;
		//
		String Modem = "D-Link DU-562M External Modem";
		for(int i = 0; i<Modem .toCharArray().length; i++ )
			rec.Modem[i] = Modem.charAt(i);
		
		
		pw.SendRecord( rec);
		PlantWatchCommRec rec2 = new PlantWatchCommRec();
		pw.ReadData(rec2);
		pw.finalize();
		
		
	}
}
