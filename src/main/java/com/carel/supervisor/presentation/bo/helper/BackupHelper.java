package com.carel.supervisor.presentation.bo.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.io.FileSystemUtils;
import com.carel.supervisor.base.io.Unzipper;
import com.carel.supervisor.base.io.Zipper;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;
import com.carel.supervisor.director.packet.PacketStaticData;
import com.carel.supervisor.field.FieldConnectorMgr;


public class BackupHelper
{
    public static final String BACKUP_PATH = "backup";
    public static final String ALL_PATH = "all";
    public static final String CONF_PATH = "conf";
    public static final String RULE_PATH = "rule";
    public static final String IDE_PATH = "ide";
    public static final String TMP_PATH = "tmp";
    public static final String ALL_FILE = "bkp";
    public static final String CONF_FILE = "bkp";
    public static final String RULE_FILE = "bkp";
    public static final String CONF_EXT = ".conf";
    public static final String RULE_EXT = ".rule";
    public static final String SITEBACKUP_EXT = ".zip";
    public static final int CLOAK_VERSION = 100;
    
    private static final String TABLE_NAME_1="hsproduct";
    private static final String TABLE_NAME_2="productinfo";
    private static final String KEY="cp";
    private static final String INFO_FILE_NAME="info";
    private static final String VERSION_FILE_NAME="version";
    
  
    /*
     * Costruttore
     */
    private BackupHelper(){}

    public static String dataOdierna()
    {
    	Timestamp dtOdierna = new Timestamp(System.currentTimeMillis());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String dtOggi = df.format( dtOdierna );
        
        return dtOggi;
    }
    
    public static String backupAll(String user) throws IOException
    {
    	boolean transaction = false;
        String dataOggi = dataOdierna();
        
        String sVer = getVersion();
        
        String dirBackup = BaseConfig.getCarelPath()+BACKUP_PATH+
        				   File.separator+ALL_PATH+File.separator+ALL_FILE+
        				   "_"+dataOggi+"_"+sVer;
    	
        dumpSequences();
       
        try
        {
        	File file = new File(dirBackup);
            file.mkdir();
            
            // Trava - Set Version
            writeVersion(file,sVer);
            // End
           
            String sql = "select tablename from pg_tables where schemaname =?";
            RecordSet recordSet = null; 
            recordSet =	DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{"public"});
            String command = "";
            String tableName = null;
            sql = null;

            for(int i = 0; i<recordSet.size(); i++)
            {
                tableName = (String)recordSet.get(i).get(0);
                if(!tableName.equals(TABLE_NAME_1) && !tableName.equals(TABLE_NAME_2)){
	                	sql = dirBackup.replace("\\", "\\\\")+File.separator+File.separator+tableName+".bck";
		                command = "copy " + tableName + " to '" + sql + "' BINARY";
		                DatabaseMgr.getInstance().executeStatement(null,command, null);
                }
            }

            writeProdType(dirBackup+File.separator+INFO_FILE_NAME,System.currentTimeMillis()+ ProductInfoMgr.getInstance().getProductInfo().get(KEY));
            
            // Backup of maps files (removed, now is managed by RecoveryTool)
            //String mstrmapsPath = BaseConfig.getAppHome()+"app" + File.separator + "mstrmaps";
            
            //File dirIde = new File(BaseConfig.getCarelPath()+BACKUP_PATH+File.separator+IDE_PATH);
            //if(!dirIde.exists())
            //	dirIde.mkdirs();
            
            //com.carel.supervisor.presentation.bo.helper.Zipper.addFileToEscludeFromZip("SubTab1.jsp");
            //com.carel.supervisor.presentation.bo.helper.Zipper.zip(mstrmapsPath,ALL_FILE+"_"+dataOggi,BaseConfig.getCarelPath()+BACKUP_PATH+File.separator+IDE_PATH);
            
            
            transaction = true;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);

            File dir = new File(dirBackup);
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++)
                files[i].delete();

            dir.delete();
            //new File(dirBackup + File.separator + INFO_FILE).delete();
        }

        cleanSequences();
        
        if (transaction)
        {
            EventMgr.getInstance().info(new Integer(1), user, "Action", "S017", format(dirBackup));
            return dirBackup;
        }
        else
        {
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S018", format(dirBackup));
            return "Error";
        }
    }

    public static String restoreAll(String user, String dirBackup)
    {
        LoggerMgr.getLogger(BackupHelper.class).info("STARTING FULL BKP RESTORE");
    	
    	boolean transaction = false;
        String completePathBackup = BaseConfig.getCarelPath()+BACKUP_PATH+File.separator+ALL_PATH+File.separator+dirBackup;
        String sVer = readVersion(completePathBackup);
        boolean checkVer = checkVersion(sVer);
        if(checkVer)
        {
	        try
	        {	
	        	String[] tableList = null;
	        	File f = new File(completePathBackup);
	        	if(f != null)
	        		tableList = f.list();
	        	else
	        		tableList = new String[0];
	        	
	        	String command = "";
	            String sql = null;
	            String tableName = "";
	            String tableError = "";
	            
	            removeForeignKeys();
            
	            for (int i = 0; i < tableList.length; i++)
	            {
	                try
	                {
	                	tableName = tableList[i];
	                	
	                	if(!INFO_FILE_NAME.equals(tableName) && !VERSION_FILE_NAME.equals(tableName))
	                	{
	                		tableName = tableList[i].substring(0,tableList[i].length()-4);
		                	
		                	LoggerMgr.getLogger(BackupHelper.class).info("table: "+tableName);
		                	
		                	if(tableName != null)
		                	{
			                	sql = "truncate " + tableName + " cascade";
			                	DatabaseMgr.getInstance().executeStatement(null,sql,null);
			                	
			                	LoggerMgr.getLogger(BackupHelper.class).info("TRUNCATE OK");
			                
			                    sql = completePathBackup.replace("\\", "\\\\")+File.separator+File.separator+tableList[i];
			                    command = "copy " + tableName + " from '" + sql + "' BINARY";
			                    DatabaseMgr.getInstance().executeStatement(null,command,null);
			                    
			                    LoggerMgr.getLogger(BackupHelper.class).info("COPY OK");
		                	}
	                	}
	                }
	                catch (Exception e) {
	                	if( !tableError.isEmpty() )
	                		tableError += ",";
	                	tableError += tableName;
	                	Logger logger = LoggerMgr.getLogger(BackupHelper.class);
	                	logger.error(e);
	                }
	            }
	            
	            // restore of maps files removed (now managed by RecoveryTool)
	            //com.carel.supervisor.presentation.bo.helper.Zipper.unzip(new File(BaseConfig.getCarelPath()+BACKUP_PATH+File.separator+IDE_PATH+File.separator+dirBackup+".zip"));
	            
	            // reset registration            
	            cleanProductRegistration();
	        	LoggerMgr.getLogger(BackupHelper.class).info("Prod. registration cleaned");
	            
	        	insertForeignKeys();
	        	
	        	restoreSequences();
	        	cleanSequences();
	        	
	        	if( tableError.isEmpty() )
	        		transaction = true;
	        	else
	        		LoggerMgr.getLogger(BackupHelper.class).info("Error detected during table import: " + tableError);
	        }
	        catch (Exception e) {
	            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
	            logger.error(e);
	        }
	
	        // Refresh dei contatori
	        try
	        {
	        	String sql = "update sykeys set value = (select max(idevent) + 10 from hsevent) where tablename='hsevent' and colname='idevent'";
	        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
	        	
	        	SeqMgr.getInstance().refresh();
	        }
	        catch(Exception e)
	        {
	        	Logger logger = LoggerMgr.getLogger(BackupHelper.class);
	            logger.error(e);
	        }
        }
        
        if (transaction)
        {
            EventMgr.getInstance().info(new Integer(1), user, "Action", "S015", format(dirBackup));
            LoggerMgr.getLogger(BackupHelper.class).info("import finished");
            return "OK";
        }
        else
        {
        	 LoggerMgr.getLogger(BackupHelper.class).info("import error");
        	if(!checkVer)
        	{
        		try {
        			EventMgr.getInstance().log(new Integer(1),user,"Action",EventDictionary.TYPE_WARNING,
        									   "S040",new Object[]{sVer,getVersion()});
                } catch (Exception e){}
        	}
        	
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S016", format(dirBackup));
            return "Error";
        }
    }
    
    
    /*
     * BackUp tabelle di configurazione sito.
     * Richiama un batch esterno che prende la lista delle tabelle da un file.
     */
    public static String backupConf(String user)
    {
    	String fileName = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator + CONF_FILE + "_" + dataOdierna() + "_" + getVersion() + CONF_EXT;
    	backupConf(user,fileName);
    	return fileName;
    }
    public static int backupConf(String user,String fileName)
    {
    	int i = 0;

    	String pathBatch = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + "exportConf.bat";
    	
    	String pathBackup = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + 
    						CONF_PATH + File.separator;

        String pathBackupTmp = pathBackup + TMP_PATH + File.separator;
		
        FileSystemUtils.deleteDir(pathBackupTmp, null);

        ScriptInvoker script = new ScriptInvoker();

        try
        {
            i = script.execute(new String[]{pathBatch}, BaseConfig.getLogFile());
        }
        catch (Exception e)
        {
            i = 1;
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            
            return 0;
        }
        
        if(i != 0)
        	EventMgr.getInstance().error(new Integer(1), user, "Action", "S022", "");
        
        // Trava - Check version
        String sVer = getVersion();
        writeVersion(new File(pathBackupTmp), sVer);
        // End
        
        
        try
        {
        	writeProdType(pathBackupTmp+File.separator+INFO_FILE_NAME,System.currentTimeMillis()+ ProductInfoMgr.getInstance().getProductInfo().get(KEY));
        	
        	Zipper.zipDir(pathBackupTmp, fileName);
            FileSystemUtils.deleteDir(pathBackupTmp, null);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S022", "");

            return 0;
        }

        EventMgr.getInstance().info(new Integer(1), user, "Action", "S021",
            format(fileName));

        return 1;
    }
    
    /*
     * Restore tabelle di configurazione sito
     * Esegue INSERT SELECT in tabelle di storico.
     * Lancia un batch esterno per la TRUNCATE delle tabelle.
     * Lancia un batch esterno per l'IMPORT dei backup.
     */
    public static String restoreConf(String user, String fileConf)
    {
    	int i = 0;

        String pathBackupTmp = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator +
            CONF_PATH + File.separator + TMP_PATH + File.separator;

        FileSystemUtils.deleteDir(pathBackupTmp, null);

        try {
        	
        	Unzipper.unzipDir(fileConf,pathBackupTmp);
        	String sVer = readVersion(pathBackupTmp);
            boolean checkVer = checkVersion(sVer);
            if(!checkVer)
            {
            	try {
        			EventMgr.getInstance().log(new Integer(1),user,"Action",EventDictionary.TYPE_WARNING,
        									   "S040",new Object[]{sVer,getVersion()});
                } catch (Exception e){}
            	return "Error";
            }          
        }
        catch(Exception e) {
        	Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S020", format(fileConf));
            return "Error";
        }
        
        try {
            cleanTables();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S020", format(fileConf));
            return "Error";
        }
        
        ScriptInvoker script = new ScriptInvoker();
        
        /*
         * TRUNCATE
         */
        String pathBatch = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + "clearConf.bat";

        try
        {
            i = script.execute(new String[]{pathBatch}, BaseConfig.getLogFile());
        }
        catch (Exception e)
        {
            i = 1;
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
        }
        
        if (i != 0)
        {
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S020",
                format(fileConf));

            return "Error";
        }
        
        /*
         * IMPORT
         */
        pathBatch = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + "importConf.bat";

        try
        {
            i = script.execute(new String[]{pathBatch}, BaseConfig.getLogFile());
        }
        catch (Exception e)
        {
            i = 1;
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
        }
        
        // Pulizia inconsistenze condizioni e/o regole
        try
		{
            String cleansql = "select functioncode from cfvariable where code='LOGIC_CONDITION'";
			RecordSet cleanrs = DatabaseMgr.getInstance().executeQuery(null, cleansql);

			if (cleanrs.size() > 0)
			{
				cleansql = "delete from cffunction where idfunction in (";
			
				for(int w=0; w < cleanrs.size()-1; w++)
				{
					cleansql += cleanrs.get(w).get("functioncode").toString() + ", ";
				}
				cleansql += cleanrs.get(cleanrs.size()-1).get("functioncode")+")";

				DatabaseMgr.getInstance().executeStatement(null, cleansql, null);

				// eseguo next query solo se ho avuto risultati da quella precedente:
				cleansql = "delete from cfvariable where code='LOGIC_CONDITION'";
				DatabaseMgr.getInstance().executeStatement(null, cleansql, null);
			
			}
			
		}
        catch (Exception e1)
		{
        	Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e1);
		}        
		
        
        // reset registration
        cleanProductRegistration();
        
        //Refresh dei contatori
        try
        {
        	String sql = "update sykeys set value = (select max(idevent) + 10 from hsevent) where tablename='hsevent' and colname='idevent'";
        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	
        	SeqMgr.getInstance().refresh();
        }
        catch(Exception e) {}
        
        
        if (i != 0)
        {
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S020",
                format(fileConf));

            return "Error";
        }

        //ri-allineo tbl buffer con hsvarhistor e hsvarhaccp:
        boolean res = reInitializeBuffer();
        if (! res)
        {
        	Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error("reInitializeBuffer() - FAILED");
        }
        
        try
        {
            FieldConnectorMgr.getInstance().writeProtocol();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S020", format(fileConf));
            return "Error";
        }
        EventMgr.getInstance().info(new Integer(1), user, "Action", "S019", format(fileConf));

        return "OK";
    }

    /*
     * Backup delle tabelle delle regole
     */
    public static String backupRule(String user)
    {
    	String fileName = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator +
    	RULE_FILE + "_" + dataOdierna() + "_" + getVersion() + RULE_EXT;
    	backupRule(user,fileName);
    	return fileName;
    }
    public static int backupRule(String user,String fileName)
    {
    	int i = 0;
    	
    	String pathBatch = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + "exportRule.bat";

    	String pathBackup = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + RULE_PATH + File.separator;
        
        String pathBackupTmp = pathBackup + TMP_PATH + File.separator;
        
        FileSystemUtils.deleteDir(pathBackupTmp, null);

        ScriptInvoker script = new ScriptInvoker();

        try
        {
            i = script.execute(new String[]{pathBatch}, BaseConfig.getLogFile());
        }
        catch (Exception e)
        {
            i = 1;
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
        }

        if (i != 0)
        {
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S026", "");
            return 0;
        }
        
        // Trava - Check version
        String sVer = getVersion();
        writeVersion(new File(pathBackupTmp), sVer);
        // End
        
        try
        {
        	writeProdType(pathBackupTmp+File.separator+INFO_FILE_NAME,System.currentTimeMillis()+ ProductInfoMgr.getInstance().getProductInfo().get(KEY));
        	
        	Zipper.zipDir(pathBackupTmp, fileName);
            FileSystemUtils.deleteDir(pathBackupTmp, fileName);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S026", "");
            return 0;
        }

        EventMgr.getInstance().info(new Integer(1),user,"Action","S025",format(fileName));

        return 1;
    }

    public static String restoreRule(String user, String ruleFile)
        throws DataBaseException
    {
    	int i=0;
    	        
        String pathBackupTmp = BaseConfig.getCarelPath()+BACKUP_PATH+File.separator+RULE_PATH+File.separator+TMP_PATH+File.separator;

        FileSystemUtils.deleteDir(pathBackupTmp, null);
        
        try {
        	
        	Unzipper.unzipDir(ruleFile, pathBackupTmp);	
        	
        	String sVer = readVersion(pathBackupTmp);
            boolean checkVer = checkVersion(sVer);
            if(!checkVer)
            {
            	try {
        			EventMgr.getInstance().log(new Integer(1),user,"Action",EventDictionary.TYPE_WARNING,
        									   "S040",new Object[]{sVer,getVersion()});
                } catch (Exception e){}
            	return "Error";
            }
        }
        catch(Exception e) {
        	Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S024", format(ruleFile));
            return "Error";
        }
        
        try
        {
        	cleanTables();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S024", format(ruleFile));
            return "Error";
        }

        ScriptInvoker script = new ScriptInvoker();
        
        /*
         * TRUNCATE CONF
         */
        String pathBatch = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + "clearConf.bat";

        try
        {
            i = script.execute(new String[]{pathBatch}, BaseConfig.getLogFile());
        }
        catch (Exception e)
        {
            i = 1;
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
        }

        if (i != 0)
        {
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S026", "");
            return "Error";
        }
        
        /*
         * TRUNCATE RULE
         */
        pathBatch = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + "clearRule.bat";

        try
        {
            i = script.execute(new String[]{pathBatch}, BaseConfig.getLogFile());
        }
        catch (Exception e)
        {
            i = 1;
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
        }

        if (i != 0)
        {
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S026", "");
            return "Error";
        }
        
        /*
         * IMPORT
         */
        pathBatch = BaseConfig.getCarelPath() + BACKUP_PATH + File.separator + "importRule.bat";

        try
        {
            i = script.execute(new String[]{pathBatch}, BaseConfig.getLogFile());
        }
        catch (Exception e)
        {
            i = 1;
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
        }

        if (i != 0)
        {
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S020",format(ruleFile));

            return "Error";
        }
        
        // reset registration
        cleanProductRegistration();
        
        // Refresh dei contatori
        try
        {
        	String sql = "update sykeys set value = (select max(idevent) + 10 from hsevent) where tablename='hsevent' and colname='idevent'";
        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	SeqMgr.getInstance().refresh();
        }
        catch(Exception e)
        {
        }
        
        try
        {
            FileSystemUtils.deleteDir(pathBackupTmp, null);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S024", format(ruleFile));

            return "Error";
        }

        //ri-allineo tbl buffer con hsvarhistor e hsvarhaccp:
        boolean res = reInitializeBuffer();
        if (! res)
        {
        	Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error("reInitializeBuffer() - FAILED");
        }
        
        try
        {
            FieldConnectorMgr.getInstance().writeProtocol();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            EventMgr.getInstance().error(new Integer(1), user, "Action", "S020", format(ruleFile));

            return "Error";
        }

        EventMgr.getInstance().info(new Integer(1), user, "Action", "S023", format(ruleFile));

        return "OK";
    }
    
    /*
     * Salataggio dati tabelle delle regole
     */
    private static void cleanTables() throws Exception
    {
    	
        DataHs dataHs = CreateSqlHs.getDeleteData("cfaction",
                new String[]
                {
                    "idaction", "pvcode", "idsite", "code", "actioncode", "actiontype",
                    "isscheduled", "template", "parameters"
                }, null, null, null);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());

        dataHs = CreateSqlHs.getDeleteData("cfcondition",
                new String[] { "idcondition", "pvcode", "idsite", "isgeneral", "condcode", "condtype" },
                null, null, null);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());

        dataHs = CreateSqlHs.getDeleteData("cfrule",
                new String[]
                {
                    "idrule", "pvcode", "idsite", "rulecode", "idcondition", "idtimeband",
                    "actioncode", "delay", "isenabled"
                }, null, null, null);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());

        dataHs = CreateSqlHs.getDeleteData("cftimeband",
                new String[]
                {
                    "idtimeband", "pvcode", "idsite", "timecode", "timetype", "timeband", "iscyclic"
                }, null, null, null);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());

        dataHs = CreateSqlHs.getDeleteData("cffunction",
                new String[]
                {
                    "idfunction", "pvcode", "idsite", "functioncode", "opertype", "parameters",
                    "operorder"
                }, null, null, null);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());
        
        dataHs = CreateSqlHs.getDeleteData("cfvarcondition",
                new String[] { "idcondition", "pvcode", "idsite", "idvariable" }, null, null, null);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());

       
    }
    
    public static void truncateDB() throws Exception
    {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < recordset.size(); i++)
        {
            buffer.append((String) recordset.get(i).get(0));

            if (i < (recordset.size() - 1))
            {
                buffer.append(",");
            }
        }

        sql = "truncate table " + buffer.toString();
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
    }
    
    public static String format(String s) {
        return Replacer.replace(s, "\\", "\\\\");
    }
    
    public static String unformat(String s)	{
    	return Replacer.replace(s, "\\\\", "\\");
    }
    
    /*
     * This is used to remove foreign keys that prevent database restore (of some tables)
     * only for keys that don't imply alphabetical order between tables related by the key
     */
    
    private static void removeForeignKeys()
    {
    	try
        {
    		String sql = "ALTER TABLE vs_exception DROP CONSTRAINT vs_exception_idgroup_fkey";
    		DatabaseMgr.getInstance().executeStatement(sql,null);
        }
    	catch(Exception e)
    	{
    		Logger logger = LoggerMgr.getLogger(BackupHelper.class);
        	logger.error(e);
    	}
    		
    }
    
    private static void insertForeignKeys()
    {
    	try
        {
    		String sql = "ALTER TABLE vs_exception ADD CONSTRAINT vs_exception_idgroup_fkey FOREIGN KEY (idgroup) REFERENCES vs_group (idgroup) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE";
    		DatabaseMgr.getInstance().executeStatement(sql,null);
        }
    	catch(Exception e)
    	{
    		Logger logger = LoggerMgr.getLogger(BackupHelper.class);
        	logger.error(e);
    	}
    }
    
    /*
     * In fase di export dei dati imposto anche la versione di PVPRO
     * da cui sono prelevati
     */
    public static String getVersion()
    {
    	String ris = "";
    	String sVer = "";
    	String sFix = "";
    	try 
    	{
    		sVer = ProductInfoMgr.getInstance().getProductInfo().get("version");
    		sFix = ProductInfoMgr.getInstance().getProductInfo().get("fix");
    		
    		if(sVer == null)
    			sVer = "NOP";
    		ris += sVer;
    		
    		if(sFix == null)
    			sFix = "";
    		ris += sFix;
    	}
    	catch(Exception e) {
    		Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
    	}
    	return ris;
    }
    
    private static void writeProdType(String filename, String prodType) throws Exception
    {
    	File fileInfo= new File(filename);
    	try 
    	{
	        FileOutputStream fileOutputStream= new FileOutputStream(fileInfo);
	        BufferedOutputStream out= new BufferedOutputStream(fileOutputStream);
	
	        out.write(prodType.getBytes());
	        
	        out.flush();
	        out.close();
	        fileOutputStream.close();
    	}
        catch (Exception e)
        {
        	throw e;
        }
    }
    
    private static void writeVersion(File f, String sVer)
    {
    	String sFile = f.getAbsolutePath()+File.separator+"version";
    	try 
    	{
    		FileOutputStream fos = new FileOutputStream(sFile);
    		fos.write(sVer.getBytes());
    		fos.flush();
    		fos.close();
		} 
    	catch (Exception e) {
		}
    }
    
    private static String readVersion(String sPath)
    {
    	String ret = "";
    	try 
    	{
    		FileInputStream fis = new FileInputStream(sPath+File.separator+"version");
    		byte[] buf = new byte[fis.available()];
    		fis.read(buf);
    		ret = new String(buf);
    		ret = ret.trim();
    		fis.close();
		} 
    	catch (Exception e) {
		}
    	return ret;
    }
       
    private static boolean checkVersion(String sVerRead)
    {
    	boolean ret = false;
    	try 
    	{
    		String sVerDb = getVersion();
    		if(sVerRead.equalsIgnoreCase(sVerDb))
    			ret = true;
		} 
    	catch (Exception e) {
    		ret = false;
		}
    	return ret;
    }
    
    private static boolean reInitializeBuffer()
    {
    	boolean ok = true;
    	
    	//re-inizializza la tbl "buffer" x corretto ri-avvio storicizzazione valori:
    	String sql_upd = "update buffer set keyactual=-1, isturn=FALSE";
    	
    	//elimina dalla tbl "buffer" var eliminate dal sito:
    	String sql_del = "delete from buffer where idvariable in (select idvariable from cfvariable where iscancelled='TRUE')";
    	
    	try
    	{
    		DatabaseMgr.getInstance().executeStatement(null, sql_upd, null);
		}
    	catch (Exception e)
    	{
    		Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
            
            ok = false;
		}
    	
    	try
    	{
    		DatabaseMgr.getInstance().executeStatement(null, sql_del, null);
		}
    	catch (Exception e1)
    	{
    		Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e1);
            
            ok = false;
		}
    	
    	return ok;
    }
    
    
    private static void cleanProductRegistration() 
    {
    	try
    	{
    		String sql = "truncate table hsproduct";      	
    		DatabaseMgr.getInstance().executeStatement(null, sql, null);
    		
        	sql = "truncate table cfmodule";
        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	
        	sql = "update productinfo set value='NA' where key='cp'";
        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	
        	sql = "update productinfo set value='"+PacketStaticData.DEFAULT_DEVICE_NUMBER+"' where key='license'";
        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	
        	sql = "update productinfo set value='"+PacketStaticData.DEFAULT_LOGGING_THRESHOLD+"' where key='logging_threshold'";
        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	
        	ProductInfoMgr.getInstance().getProductInfo().set(KEY, "NA");
    	}
    	catch (Exception e)
    	{
    		Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
    	}

    }
    
    public static boolean SiteBackupExportFile(String user,String oldfile, String newfile)
    {
    	try
    	{
    		String oldpath = BaseConfig.getCarelPath()+BACKUP_PATH+
			   File.separator+ALL_PATH+File.separator+oldfile+File.separator;
    		Zipper.zipDir(oldpath, newfile);
    		return true;
    	}
    	catch(Exception ex)
    	{
    		return false;
    	}
    }
    public static boolean SiteBackupUploadFile(String user,String zipfile)
    {
    	try
    	{
    		String folder = "";
    		//local
    		if(zipfile.indexOf(File.separator) == -1)
    		{
    			folder = zipfile.substring(zipfile.lastIndexOf('/')+1, zipfile.length()-SITEBACKUP_EXT.length());
    		}
    		else//remote
    		{
    			folder = zipfile.substring(zipfile.lastIndexOf(File.separator)+1, zipfile.length()-SITEBACKUP_EXT.length());
    		}
    		String path = BaseConfig.getCarelPath()+BACKUP_PATH+
			   File.separator+ALL_PATH+File.separator+folder;
    		FileSystemUtils.deleteDir(path, null);
    		Unzipper.unzipDir(zipfile, path);
    		return true;
    	}
    	catch(Exception ex)
    	{
    		return false;
    	}
    }
    
    
    private static void dumpSequences()
    {
    	try {
	    	String sql = "select c.relname from pg_class c where c.relkind = 'S'"; // get sequence names
	        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql);
			String sqlInsert = "insert into blackboard values ('pg_sequences', ?, (select nextval(?)), CURRENT_TIMESTAMP);";
	        for(int i = 0; i < recordSet.size(); i++) {
	        	try {
		            String sequenceName = (String)recordSet.get(i).get(0);
					DatabaseMgr.getInstance().executeStatement(null, sqlInsert, new Object[]{sequenceName, sequenceName});
	        	} catch(Exception e) {
	        		LoggerMgr.getLogger(BackupHelper.class).error(e);
	        	}
	        }
        } catch(Exception e) {
        	LoggerMgr.getLogger(BackupHelper.class).error(e);
        }
    }
    
    
    private static void restoreSequences()
    {
    	try {
	    	String sql = "select c.relname from pg_class c where c.relkind = 'S'"; // get sequence names
	        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql);
			String sqlRestore = "select setval(?, (select cast(params as bigint) from blackboard where app = 'pg_sequences' and type = ?), false)"; 
	        for(int i = 0; i < recordSet.size(); i++) {
	        	try {
		            String sequenceName = (String)recordSet.get(i).get(0);
					DatabaseMgr.getInstance().executeStatement(null, sqlRestore, new Object[]{sequenceName, sequenceName});
	        	} catch(Exception e) {
	        		LoggerMgr.getLogger(BackupHelper.class).error(e);
	        	}
	        }
        } catch(Exception e) {
        	LoggerMgr.getLogger(BackupHelper.class).error(e);
        }
    }

    
    private static void cleanSequences()
    {
    	try {
	    	String sql = "delete from blackboard where app = 'pg_sequences'";
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
        } catch(Exception e) {
        	LoggerMgr.getLogger(BackupHelper.class).error(e);
        }
    }

}
