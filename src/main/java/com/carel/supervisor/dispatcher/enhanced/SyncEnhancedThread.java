package com.carel.supervisor.dispatcher.enhanced;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SyncEnhancedThread extends Thread
{
    private static final int RequestSize = 308;
    private Socket client;

    SyncEnhancedThread(Socket client, String name)
    {
        super.setName(name);
        this.client = client;
    }

    public void run()
    {
        try
        {
            InputStream i = client.getInputStream();
            OutputStream o = client.getOutputStream();
            byte[] Buffer = new byte[RequestSize];
            i.read(Buffer);

            String s = new String(Buffer, 4, 54);

            int iIdent = 0;
            s = s.trim();

            try
            {
                iIdent = Integer.parseInt(s);
            }
            catch (Exception e)
            {
            }

            String ip_remote = client.getInetAddress().getHostAddress();

            ByteArrayOutputStream bytestream = new ByteArrayOutputStream(310);
            DataOutputStream out = new DataOutputStream(bytestream);
            out.writeInt(Integer.reverseBytes(iIdent));

            char[] szThisTel = new char[50];
            char[] szMessage = new char[512];

            for (int ii = 0; ii < 50; ii++)
            {
                out.write(szThisTel[ii]);
            }

            for (int ii = 0; ii < 50; ii++)
            {
                out.write(szMessage[ii]);
            }

            o.write(bytestream.toByteArray(), 0, bytestream.size());
            o.flush();
            bytestream = null;
            out = null;

            //Adesso ho l'ident dell'enhanced e posso andare nel db a
            //prendere la password e l'utente del nodo 
            //per poi sincronizarmi via ftp.
            SyncIdentIPList ipl = SyncIdentIPList.getIstance();
            ipl.Add(s, ip_remote);

            //ritorna l'ident in base all'IP.
            //String ident = ipl.getIdent( ip_remote);
            client.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
