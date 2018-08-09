 package com.carel.supervisor.dispatcher.enhanced;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class SyncLoader
{
    private long dimLogPrec = 0;
    private long dimLog = 0;
    private long dimL00 = 0;
    private long dimL00Prec = 0;
    private String AlrDir = "";
    Map NodeConfiguration;

    public SyncLoader(String AlrDir, long dimLog, long dimL00, long dimLogPrec, long dimL00Prec)
    {
        this.dimLog = dimLog;
        this.dimL00 = dimL00;
        this.dimLogPrec = dimLogPrec;
        this.dimL00Prec = dimL00Prec;
        this.AlrDir = AlrDir;
        NodeConfiguration = GetNodeConf();
    }

    public Vector Alarms(Vector AlrList)
    {
        //Vector AlrList = null;
        FileInputStream alr;
        byte[] ByteBuffer;

        try
        {
            //Carico la parte del log da importare.
            //AlrList = new Vector();
            if ((dimL00 - dimL00Prec) > 0)
            {
                try
                {
                    alr = new FileInputStream(AlrDir + "/alarms.l00");
                    ByteBuffer = new byte[(int) dimL00];
                    alr.skip(dimL00Prec);
                    alr.read(ByteBuffer);

                    String StringBufferRead = new String(ByteBuffer);

                    //StringBufferRead = StringBufferRead.substring( (int)dimL00Prec);
                    //Adesso che ho il buffer da caricare, lo trasformo in n elemnti di tipo SyncRecord
                    while (StringBufferRead.length() > 0)
                    {
                        int CRPos = StringBufferRead.indexOf("\r\n");

                        if (CRPos < 0)
                        {
                            break;
                        }

                        String Row = StringBufferRead.substring(0, CRPos - 1).trim();

                        if (Row.equals("") || (Row.length() == 0))
                        {
                            break;
                        }

                        SyncRecord rec = new SyncRecord();
                        rec = ConvertToSyncRec(Row);
                        AlrList.add(rec);
                        StringBufferRead = StringBufferRead.substring(CRPos + 2,
                                StringBufferRead.length());
                    }
                }
                catch (FileNotFoundException e)
                {
                	Logger logger = LoggerMgr.getLogger(this.getClass());
                	logger.error(e);

                    return AlrList;
                }
                catch (IOException e)
                {
                	 Logger logger = LoggerMgr.getLogger(this.getClass());
                     logger.error(e);

                    return AlrList;
                }
            }

            if ((dimLog - dimLogPrec) > 0)
            {
                try
                {
                    alr = new FileInputStream(AlrDir + "/alarms.log");
                    ByteBuffer = new byte[(int) dimLog];
                    alr.skip(dimLogPrec);
                    alr.read(ByteBuffer);

                    String StringBufferRead = new String(ByteBuffer);

                    //StringBufferRead = StringBufferRead.substring( (int)dimLogPrec);
                    //Adesso che ho il buffer da caricare, lo trasformo in n elemnti di tipo SyncRecord
                    while (StringBufferRead.length() > 0)
                    {
                        int CRPos = StringBufferRead.indexOf("\r\n");

                        if (CRPos < 0)
                        {
                            break;
                        }

                        String Row = StringBufferRead.substring(0, CRPos).trim();

                        if (Row.equals("") || (Row.length() == 0))
                        {
                            break;
                        }

                        SyncRecord rec = new SyncRecord();
                        rec = ConvertToSyncRec(Row);

                        if ((rec != null) && (rec.GlobalIdent != -1))
                        {
                            AlrList.add(rec);
                        }

                        StringBufferRead = StringBufferRead.substring(CRPos + 2,
                                StringBufferRead.length());
                    }
                }
                catch (FileNotFoundException e)
                {
                	Logger logger = LoggerMgr.getLogger(this.getClass());
                	logger.error(e);

                    return AlrList;
                }
                catch (IOException e)
                {
                	Logger logger = LoggerMgr.getLogger(this.getClass());
                	logger.error(e);

                    return AlrList;
                }
            }
        }
        catch (Exception e)
        {
        	 Logger logger = LoggerMgr.getLogger(this.getClass());
             logger.error(e);
        }

        return AlrList;
    }

    public SyncRecNode GetNode()
    {
        SyncRecNode Node = null;

        try
        {
            Node = new SyncRecNode();

            INIFile objINI = new INIFile(AlrDir + "/nd_0.ncf");
            Node.NodeIdent = objINI.getIntegerProperty("Node", "Ident").intValue();
            Node.NodeDescription = objINI.getStringProperty("Node", "Description");
            Node.TelNumber = objINI.getStringProperty("Node", "Tel");
            Node.User = objINI.getStringProperty("Node", "Name");
            Node.Password = objINI.getStringProperty("Node", "Password");

            objINI = null;
        }
        catch (Exception e)
        {
        	 Logger logger = LoggerMgr.getLogger(this.getClass());
             logger.error(e);
        }

        return Node;
    }

    public Map GetNodeConfiguration()
    {
        return NodeConfiguration;
    }

    private Map GetNodeConf()
    {
        Map NodeConfiguration = null;

        try
        {
            NodeConfiguration = Collections.synchronizedMap(new TreeMap());

            BufferedReader in = new BufferedReader(new FileReader(AlrDir + "/nd_0.ncf"));
            String Line = "";
            boolean InSection = false;

            while ((Line = in.readLine()) != null)
            {
                if (InSection && (Line.indexOf("[") != -1))
                {
                    break;
                }

                if (InSection)
                {
                    //Sono nella parte del file che contiene la configurazione del sito
                    String[] LineArray = StringUtility.split(Line, ",");
                    SyncRecNodeConfiguration nc = new SyncRecNodeConfiguration();
                    nc.GlobalIdent = Integer.parseInt(LineArray[LineArray.length-3]);
                    nc.Line = Integer.parseInt(LineArray[0]);
                    nc.SerialIdent = Integer.parseInt(LineArray[1]);
                    nc.UnitType = LineArray[2];
                    nc.UnitType = nc.UnitType.substring(1, nc.UnitType.length());
                    nc.UnitType = nc.UnitType.substring(0, nc.UnitType.length() - 1);
                    //composizione unitdescription
                    String tmpUnitDescription = LineArray[3];
                    for(int x=4;x<LineArray.length-3;x++)
                    {
                    	tmpUnitDescription += ","+LineArray[x];
                    }
                    nc.UnitDescription = tmpUnitDescription.toString();
                    nc.UnitDescription = nc.UnitDescription.substring(1, nc.UnitDescription.length());
                    nc.UnitDescription = nc.UnitDescription.substring(0, nc.UnitDescription.length() - 1);
                    //NodeConfiguration.put(key, nc);
                    String key = nc.Line + "_" + nc.SerialIdent;
                    NodeConfiguration.put(key, nc);
                }

                if (Line.indexOf("[Units]") != -1)
                {
                    InSection = true;
                }
            }

            in.close();
            in = null;
        }
        catch (FileNotFoundException e)
        {
        	 Logger logger = LoggerMgr.getLogger(this.getClass());
             logger.error(e);
        }
        catch (IOException e)
        {
        	 Logger logger = LoggerMgr.getLogger(this.getClass());
             logger.error(e);
        }
        catch (Exception e)
        {
        	 Logger logger = LoggerMgr.getLogger(this.getClass());
             logger.error(e);
        }

        return NodeConfiguration;
    }

    public Vector GetLines()
    {
        Vector Lines = null;

        try
        {
            Lines = new Vector();

            INIFile objINI = new INIFile(AlrDir + "/driver.ini");
            String Line = "";

            for (int i = 1; i <= 6; i++)
            {
                Line = objINI.getStringProperty("config", "Line" + i);

                if (Line != "")
                {
                    String[] LineArray = StringUtility.split(Line, ",");
                    SyncRecLine RLine = new SyncRecLine();

                    RLine.CommNuber = Integer.parseInt(LineArray[0]);
                    RLine.BaudRate = Integer.parseInt(LineArray[1]);
                    RLine.LineOpenMode = LineArray[2];
                    RLine.Linea = i;
                    Lines.add(RLine);
                }
            }

            objINI = null;
        }
        catch (Exception e)
        {
        	 Logger logger = LoggerMgr.getLogger(this.getClass());
             logger.error(e);
        }

        return Lines;
    }

    private SyncRecord ConvertToSyncRec(String Row)
    {
        SyncRecord rec = null;

        try
        {
            rec = new SyncRecord();
            String[] LineArray = StringUtility.split(Row, ",");
            String LineType = LineArray[3].substring(0, 2).trim().toUpperCase();

            if (LineType.equalsIgnoreCase("S") || LineType.equalsIgnoreCase("E") || LineType.equalsIgnoreCase("R"))
            {
                //E' un allarme e non un evento e lo trasformo
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");

                try
                {
                    rec.AlrDate = df.parse(LineArray[0]);
                }
                catch (ParseException e)
                {
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);

                    return rec;
                }
                if(LineType.equalsIgnoreCase("R"))
                {	
                	rec.isRestart = true;
                	// In questo modo la riga viene caricata nel vector 
                	rec.GlobalIdent = 0L;
                	return rec;
                }
                String[] UnitArray = StringUtility.split(LineArray[1], ".");
                rec.Line = Integer.parseInt(UnitArray[1]);

                rec.SerialIdent = Integer.parseInt(UnitArray[2]);
                rec.AlarmDescription = LineArray[3].substring(3);

                String key = rec.Line + "_" + rec.SerialIdent;

                SyncRecNodeConfiguration obj = ((SyncRecNodeConfiguration) NodeConfiguration.get(key));

                if (obj != null)
                {
                    rec.GlobalIdent = ((SyncRecNodeConfiguration) NodeConfiguration.get(key)).GlobalIdent;
                    rec.UnitType = ((SyncRecNodeConfiguration) NodeConfiguration.get(key)).UnitType;
                    rec.UnitDescription = ((SyncRecNodeConfiguration) NodeConfiguration.get(key)).UnitDescription;

                    int len = LineArray[2].length() - 2;
                    String var = LineArray[2].trim().substring(0, len);

                    //var manca dell' h finale.
                    rec.VarAddress = Integer.parseInt(var, 16);
                    rec.StartEnd = LineType.equalsIgnoreCase("S");
                }
            }
        }
        catch (Exception e)
        {
        	 Logger logger = LoggerMgr.getLogger(this.getClass());
             logger.error(e);
        }

        return rec;
    }
}
