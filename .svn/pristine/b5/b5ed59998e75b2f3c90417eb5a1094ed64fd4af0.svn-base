package com.carel.supervisor.director.maintenance;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;


public class VarHistorMgr
{
    public VarHistorMgr()
    {
    }

    /*Codici di ritorno:
     
     1 Errore semi-grave HACCP table
     2 errore grave HACCP table
     3 Errore semi-grave HISTOR table
     4 errore grave HISTOR table
     0 OK
     
    */
    public static int move() throws Exception
    {
    	int code = move("hsvarhaccp");
    	if (code != 0)
    	{
    		return code;
    	}	
    	code = move("hsvarhistor");
    	if (code != 0)
    	{
    		code+=2;
    		return code;
    	}
    	return 0;
    }
    
    private static int move(String table) throws Exception
    {
        String sql = "";

        try
        {
            //creo nuova tabella
            sql = "CREATE TABLE "+table+"2 (idsite SMALLINT, " +
                "idvariable INTEGER NOT NULL, key SMALLINT, frequency INTEGER," +
                "status BIGINT, " + "inserttime TIMESTAMP WITH TIME ZONE," + "value REAL, " +
                "n1 SMALLINT, value1 REAL, n2 SMALLINT, value2 REAL, " +
                "n3 SMALLINT, value3 REAL, n4 SMALLINT, value4 REAL, " +
                "n5 SMALLINT, value5 REAL, n6 SMALLINT, value6 REAL, " +
                "n7 SMALLINT, value7 REAL, n8 SMALLINT, value8 REAL, " +
                "n9 SMALLINT, value9 REAL, n10 SMALLINT, value10 REAL, " +
                "n11 SMALLINT, value11 REAL, n12 SMALLINT, value12 REAL, " +
                "n13 SMALLINT, value13 REAL, n14 SMALLINT, value14 REAL, " +
                "n15 SMALLINT, value15 REAL, n16 SMALLINT, value16 REAL, " +
                "n17 SMALLINT, value17 REAL, n18 SMALLINT, value18 REAL, " +
                "n19 SMALLINT, value19 REAL, n20 SMALLINT, value20 REAL, " +
                "n21 SMALLINT, value21 REAL, n22 SMALLINT, value22 REAL, " +
                "n23 SMALLINT, value23 REAL, n24 SMALLINT, value24 REAL, " +
                "n25 SMALLINT, value25 REAL, n26 SMALLINT, value26 REAL, " +
                "n27 SMALLINT, value27 REAL, n28 SMALLINT, value28 REAL, " +
                "n29 SMALLINT, value29 REAL, n30 SMALLINT, value30 REAL, " +
                "n31 SMALLINT, value31 REAL, n32 SMALLINT, value32 REAL, " +
                "n33 SMALLINT, value33 REAL, n34 SMALLINT, value34 REAL, " +
                "n35 SMALLINT, value35 REAL, n36 SMALLINT, value36 REAL, " +
                "n37 SMALLINT, value37 REAL, n38 SMALLINT, value38 REAL, " +
                "n39 SMALLINT, value39 REAL, n40 SMALLINT, value40 REAL, " +
                "n41 SMALLINT, value41 REAL, n42 SMALLINT, value42 REAL, " +
                "n43 SMALLINT, value43 REAL, n44 SMALLINT, value44 REAL, " +
                "n45 SMALLINT, value45 REAL, n46 SMALLINT, value46 REAL, " +
                "n47 SMALLINT, value47 REAL, n48 SMALLINT, value48 REAL, " +
                "n49 SMALLINT, value49 REAL, n50 SMALLINT, value50 REAL, " +
                "n51 SMALLINT, value51 REAL, n52 SMALLINT, value52 REAL, " +
                "n53 SMALLINT, value53 REAL, n54 SMALLINT, value54 REAL, " +
                "n55 SMALLINT, value55 REAL, n56 SMALLINT, value56 REAL, " +
                "n57 SMALLINT, value57 REAL, n58 SMALLINT, value58 REAL, " +
                "n59 SMALLINT, value59 REAL, n60 SMALLINT, value60 REAL, " +
                "n61 SMALLINT, value61 REAL, n62 SMALLINT, value62 REAL, " +
                "n63 SMALLINT, value63 REAL, n64 SMALLINT) WITHOUT OIDS";

            DatabaseMgr.getInstance().executeStatement(null, sql, null);

            //sposto dati
            sql = "insert into "+table+"2 select * from "+table;
            DatabaseMgr.getInstance().executeStatement(null, sql, null);

            //creo indici
            sql = "CREATE INDEX "+table+"2_i_"+table+"key ON "+table+"2 USING btree (idvariable, key)";
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
            sql = "CREATE INDEX "+table+"2_i_"+table+"time ON "+table+"2 USING btree (idvariable, inserttime)";
            DatabaseMgr.getInstance().executeStatement(null, sql, null);

            //      lancio analyze
            sql = "analyze "+table+"2";
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
        }
        catch (Exception e)//ATTENZIONE SI E' ROTTO QUALCOSA MENTRE SI STAVA EFFETTUANDO LA MANUTENZIONE
        {
        	try
        	{
        		sql = "drop table "+table+"2";
        		DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	}
        	catch(Exception e1)
        	{
        	}
            return 1;
        }

        try
        {
            //cancello vecchia tabella
            sql = "truncate "+table;
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
            sql = "drop table "+table;
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
            sql = "alter table "+table+"2 rename to "+table;
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
            sql = "alter index "+table+"2_i_"+table+"key rename to "+table+"_i_"+table+"key";
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
            sql = "alter index "+table+"2_i_"+table+"time rename to "+table+"_i_"+table+"time";
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
            //rinomino nuova tabella
        }
        catch (Exception e) //ATTENZIONE SI E' ROTTO QUALCOSA MENTRE SI STAVA EFFETTUANDO LA MANUTENZIONE. BISOGNA CHIAMARE ASSISTENZA
        {
        	return 2;
        }

        
        return 0;
        
    }

    public static void main(String[] args) throws Exception
    {
        BaseConfig.init();
        move();
    }
}
