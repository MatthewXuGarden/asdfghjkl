package com.carel.supervisor.presentation.fs;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.fs.FSManager;
import com.carel.supervisor.plugin.fs.FSRack;
import com.carel.supervisor.presentation.bean.DeviceBean;


public class FSRackBean {
	
	private static final Integer MAX_RACKS = FSManager.getMaxracks();
	private static final Integer DUMMY_RACK = -1;
	
	public static final Integer TIME_WINDOW = 7200;
	public static final Integer WAIT_TIME = 1800;
	public static final Integer MAX_OFF_TIME = 30;
	public static final Integer MAX_OFF_UTIL = 3;
	
    //return configured list of devices that can be used as rack
    public static FSRackAux[] retrieveFreeRackOfSite(String lang) throws DataBaseException {
    
    	DeviceBean[] devices = null;
        FSRackAux[] racks = null;

        String sql = "select fsdevmdl.*,cfdevice.*,t1.description,t2.description as vdesc from " +
        		"cfdevice,cftableext as t1,cfvariable ,cftableext as t2,cfdevmdl,fsdevmdl where " +
        		"cfdevice.iddevmdl=cfdevmdl.iddevmdl and cfdevmdl.code = fsdevmdl.devcode and " +
        		"fsdevmdl.israck=? and cfdevice.iscancelled=? and t1.tablename=? and " +
        		"t1.languagecode=? and t1.tableid=cfdevice.iddevice and " +
        		"cfdevmdl.iddevmdl=cfdevice.iddevmdl and " +
        		"cfvariable.iddevice=cfdevice.iddevice and cfvariable.code=fsdevmdl.var1 and " +
        		"cfvariable.idhsvariable notnull and " +
        		"t2.tableid=cfvariable.idvariable and t2.tablename=? and " +
        		"t2.languagecode=? order by iddevice;";        
        Object[] params = new Object[6];
        params[0] = "TRUE";
        params[1] = "FALSE";
        params[2] = "cfdevice";
        params[3] = lang;
        params[4] = "cfvariable";
        params[5] = lang;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

        devices = new DeviceBean[rs.size()];
        racks = new FSRackAux[rs.size()];
        
        int i;
        for (i = 0; i < devices.length ; i++)
        {
            devices[i] = new DeviceBean(rs.get(i), lang);
            racks[i] = new FSRackAux(devices[i],rs.get(i).get("devcode").toString(),rs.get(i).get("var1").toString(),
            		rs.get(i).get("var2")!=null?rs.get(i).get("var2").toString():"",
            		rs.get(i).get("var3")!=null?rs.get(i).get("var3").toString():"",
            		rs.get(i).get("var4")!=null?rs.get(i).get("var4").toString():"",rs.get(i).get("vdesc").toString());
        }
        
        return racks;
    }

    public static Map<Integer,Float> getMaxDCFromDB(int idrack) throws DataBaseException
    {
    	String sql ="select idutil,maxdc from fsutil where idrack = ?";
    	
    	Object[] params = new Object[1];
    	params[0] = new Integer(idrack);
    	
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
    	
    	Map<Integer,Float> map = new HashMap<Integer,Float>();
    	Record r = null;
    	for (int i=0;i<rs.size();i++)
    	{
    		r = rs.get(i);
    		map.put((Integer)r.get("idutil"),r.get("maxdc")==null?null:Float.valueOf(r.get("maxdc").toString()));
    	}
    	
    	return map;
    }
    
    // return Rack table
    public static String getHTMLTable(FSRackAux[] racks,String language) throws DataBaseException
    {
       
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	    	
    	String sql = "select fsrack.idrack,fsrack.iddevice,cfvariable.code from fsrack,cfvariable where fsrack.iddevice=cfvariable.iddevice and " +
    			"cfvariable.idvariable=fsrack.setpoint and cfvariable.iscancelled='FALSE' and cfvariable.idvariable = fsrack.setpoint and " +
    			"cfvariable.idhsvariable is not null and idrack > " + DUMMY_RACK.toString();
    	    	
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
    	
    	String idRacksSelected = "";
    	int[] idRack = new int[rs.size()];
    	int[] idDev = new int[rs.size()];
    	String[] sp = new String[rs.size()];
    	
    	if (rs.size() > 0)
    	{

	    	int j;
	    	for (j=0; j < rs.size()-1; j++)
	    	{
	    		idRacksSelected += rs.get(j).get("idrack").toString() + ", ";
	    		idRack[j] = ((Integer)rs.get(j).get("idrack")).intValue();
	    		idDev[j] = ((Integer)rs.get(j).get("iddevice")).intValue();
	    		sp[j] = rs.get(j).get("code").toString();
	    	}
	    	idRacksSelected += rs.get(j).get("idrack").toString();
	    	idRack[j] = ((Integer)rs.get(j).get("idrack")).intValue();
	    	idDev[j] = ((Integer)rs.get(j).get("iddevice")).intValue();
    		sp[j] = rs.get(j).get("code").toString();
	    	
    	}
    	
    	StringBuffer html = new StringBuffer("");
    	
    	html.append("<table width='98%' class='table' cellpadding='1' cellspacing='1'>\n");
        html.append("<tr class='th' height='22'>");
        html.append("<td align='center'><b>"+lan.getString("fsdetail","rack")+"</b></td>");
        html.append("<td align='center'><b>"+lan.getString("fsdetail","line")+"</b></td>");
        html.append("<td align='center'><b>"+lan.getString("fsdetail","address")+"</b></td>");
        html.append("<td align='center'><b>"+lan.getString("fs","enabled")+"</b></td>");
        html.append("</tr>\n");
    	
        int i=0;
        if (racks != null)
        {
            int z = 0;
           
            for (i = 0; i < racks.length; i++)
            {
            	boolean check = false;
            	racks[i].getDevmdl_code();
                    	
            	html.append("<tr class='"+(i%2==0?"Row1":"Row2")+"' >\n");
            	html.append("<td class='standardTxt'>"+racks[i].getRack().getDescription()+"  ("+racks[i].getSp_desc() +")</td>\n");
            	html.append("<td class='standardTxt' align='center'>"+StringUtility.split(racks[i].getRack().getCode(),".")[0]+"</td>\n");
            	html.append("<td class='standardTxt' align='center'>"+StringUtility.split(racks[i].getRack().getCode(),".")[1]+"</td>\n");
                
                String data3i = "";
                data3i = "<input type='checkbox' ";
                
                //check if current rack is used
                z = 0;
                while (z < idDev.length)
                {
                	if ( idDev[z] == racks[i].getRack().getIddevice() && sp[z].equalsIgnoreCase(racks[i].getSp_code()))
                	{
                		check = true;
                		break;
                	}
                	z++;
                }
                //if selected -> checked
                if (check) 
                {
                	data3i += "checked";
                }
                data3i += " id='rack_n"+i+"' name='rack_n"+i+"' value='" + racks[i].getRack().getIddevice() + "'";
                // hidden fields
                data3i += " />" + " <input type='hidden' id='devcode"+i+"' name='devcode"+i+"' value='" + racks[i].getDevmdl_code() + "' />";
                data3i += "<input type='hidden' id='sp_code"+i+"' name='sp_code"+i+"' value='"+racks[i].getSp_code()+"' />";
                data3i += "<input type='hidden' id='min_code"+i+"' name='min_code"+i+"' value='"+racks[i].getMin_code()+"' />";
                data3i += "<input type='hidden' id='max_code"+i+"' name='max_code"+i+"' value='"+racks[i].getMax_code()+"' />";
                data3i += "<input type='hidden' id='d_code"+i+"' name='d_code"+i+"' value='"+racks[i].getGrad_code()+"' />";
                
                html.append("<td class='standardTxt' align='center'>"+data3i+"</td>");
                html.append("</tr>");
            }
            html.append("</table>");
            
        }

        
        String page = html.toString();
        page += " <input type='hidden' id='numRacks' name='numRacks' value='" + i + "' /> ";
        page += " <input type='hidden' id='maxNumRacks' name='maxNumRacks' value='" + MAX_RACKS + "' /> ";
        page += " <input type='hidden' id='archtabtoload' name='archtabtoload' value='' /> ";
        
        return page;
    }
    
    //return rack from db by id (fsrack)
    public static FSRack getActualRackFromDB(int idCurrRack,String lang) throws DataBaseException
    {
    	FSRack currRack = null;
    	String sql = "select fsrack.*,cftableext.description from fsrack,cftableext where idrack = ? and cftableext.idsite=? and cftableext.languagecode=? and cftableext.tableid=fsrack.iddevice and tablename=?";
    	Object[] params = new Object[4];
    	params[0] = new Integer(idCurrRack);
    	params[1] = new Integer(1);
    	params[2] = lang;
    	params[3] = "cfdevice";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
    	
    	Record r = rs.get(0);
    	currRack = new FSRack(r,lang);
    	return currRack;
    }
    
    // return the combobox with all racks saved on fsrack. This combo is used on the associations page Rack->Utils (app/fs/SubTab2.jsp)
    public static String getComboRacks(int idrack_selected,String lang) throws DataBaseException
    {
        
    	String sql = "select idrack, t1.description,t2.description as vdesc,code from fsrack, cftableext as t1,cftableext as t2, cfvariable where idrack <> ? and " + 
    			"idrack in (select distinct idrack from fsutil) and t1.tablename=? and " +
    			"t1.tableid=fsrack.iddevice and t1.languagecode=? and t1.idsite=? and " +
    			"cfvariable.idvariable=fsrack.setpoint and t2.tablename=? and t2.tableid=cfvariable.idvariable and t2.languagecode=? and t2.idsite=?";
    	
        Object[] params = new Object[7];
        params[0] = DUMMY_RACK;
        params[1] = "cfdevice";
        params[2] = lang;
        params[3] = new Integer(1);
        params[4] = "cfvariable";
        params[5] = lang;
        params[6] = new Integer(1);
        
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        
        Record r = null;
        String selected = "";
        StringBuffer combo = new StringBuffer();
        
        combo.append("<select id='comborack' name='comborack' onchange='change_rack();MioAskModUser()'>");
        
        for (int i=0;i<rs.size();i++)
        {
        	r = rs.get(i);
        	int idrack = ((Integer) r.get("idrack")).intValue();
        	selected = (idrack==idrack_selected?"selected":"");
        	combo.append("<option class='standardTxt' "+selected+" value='"+idrack+"' >"+r.get("description")+" ("+r.get("vdesc")+")</option>\n");
        }
        
        combo.append("</select>");
        
        return combo.toString();
    }
    
    
    // SAVE-DELETE-UPDATE racks on DB
    public static void saveRacksSelected(Properties props) throws DataBaseException
    {
    	
    	// ## Retrieve info of racks saved on DB ##
    	String sql = "select fsrack.idrack,fsrack.iddevice,cfvariable.code from fsrack,cfvariable where fsrack.iddevice=cfvariable.iddevice and " +
		"cfvariable.idvariable=fsrack.setpoint and cfvariable.iscancelled='FALSE' and cfvariable.idvariable = fsrack.setpoint and " +
		"cfvariable.idhsvariable is not null and idrack > " + DUMMY_RACK.toString();
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
    	
    	// saved_rack_info: iddevice_setpoint   PrimaryKey used to recognize a row on fsrack without use idrack 
    	//(before saving new rack, idrack doesn't exist)
    	int idRackOldLength = 0;
    	String[] saved_rack_info = new String[rs.size()];
    	
    	if (rs.size() > 0)
    	{
	    	int j;
	    	for (j = 0; j < (rs.size() - 1); j++)
	    	{
	    		saved_rack_info[j] = rs.get(j).get("iddevice").toString() +"_"+ rs.get(j).get("code").toString();
	    	}
	    	saved_rack_info[j] = rs.get(j).get("iddevice").toString() +"_"+ rs.get(j).get("code").toString();
	    	idRackOldLength = ++j;
    	}
    	
    	
    	// ## retrieve rack selected (from page properties) ##
    	int numRacks = Integer.parseInt(props.getProperty("numRacks"));
    	
    	String[] info_rack_new = new String[numRacks];
    	int idRackNewLength = 0;
    	
    	String[] idDevCode = new String[numRacks];
    	String sp[] = new String[numRacks];
    	String min[] = new String[numRacks];
    	String max[] = new String[numRacks];
    	String delta[] = new String[numRacks];
    	String iddevice[] = new String[numRacks];
    	
    	int idr = 0;
    	 
    	for (int i = 0; i < numRacks; i++)
    	{
    		// solo se la corrispondente checkbox � stata selezionata allora:
    		if ((null != props.getProperty("rack_n" + i)) && (! "".equals(props.getProperty("rack_n" + i))))
    		{
    			//iddevice
    			iddevice[idr] = props.getProperty("rack_n" + i);
    			//code setpoint variable
    			
    			sp[idr] = props.getProperty("sp_code" + i);
    			min[idr] = props.getProperty("min_code" + i);
    			max[idr] = props.getProperty("max_code" + i);
    			delta[idr] = props.getProperty("d_code" + i);
    			
    			//PK Rack
    			info_rack_new[idr] = iddevice[idr]+"_"+sp[idr];
    			
    			idDevCode[idr] = props.getProperty("devcode" + i);
    			idr++;
    		}
    	}
    	idRackNewLength = idr;
    	
    	String[] idDevToAdd = new String[MAX_RACKS];
    	String[] idDevCodeToAdd = new String[MAX_RACKS];
    	String[] spCodeToAdd = new String[MAX_RACKS];
    	String[] idRackToDel = new String[MAX_RACKS];
    	
    	int i, j;
    	int k = 0; //number of rack to delete
    	int h = 0; //number of rack to add
    	int w = 0; //number of rack to update
    	
    	// find racks to delete
    	for (i = 0; i < idRackOldLength; i++)
    	{
    		j = 0;
    		while((j < idRackNewLength) && (!info_rack_new[j].equalsIgnoreCase(saved_rack_info[i])))
    			j++;
    		if (j >= idRackNewLength)
    		{
    			// array rack to delete
    			idRackToDel[k++] = saved_rack_info[i];
    	
    		}
    	}
    	
    	// if there are racks to delete
    	if (idRackToDel!=null && idRackToDel.length!=0)
    	{
    		boolean logResult = false;
    		String tmp = "";
    		String tmp_id = "";
    		String tmp_code ="";
    		
    		for (i=0;i<idRackToDel.length;i++)
    		{
    			tmp = 	idRackToDel[i];
    			if (tmp!=null && !"null".equalsIgnoreCase(tmp))
    			{
	    			tmp_id = tmp.split("_")[0];
	    			tmp_code = tmp.substring(tmp_id.length()+1, tmp.length());
	    			
	    			// delete from fsutil:
	        		sql = "delete from fsutil where idrack = ( select idrack from fsrack where iddevice=" + tmp_id + " and setpoint = (" +
	        				"select idvariable from cfvariable where iddevice ="+tmp_id+" and code='"+tmp_code+"' and iscancelled='FALSE' and cfvariable.idhsvariable notnull))";
	        		DatabaseMgr.getInstance().executeStatement(null, sql, null, logResult);
	        		
	        		// delete from fsrack:
	        		sql = "delete from fsrack where idrack = ( select idrack from fsrack where iddevice=" + tmp_id + " and setpoint = (" +
	            				"select idvariable from cfvariable where iddevice ="+tmp_id+" and code='"+tmp_code+"' and iscancelled='FALSE' and cfvariable.idhsvariable notnull))";
	        		logResult = false;
	        		DatabaseMgr.getInstance().executeStatement(null, sql, null, logResult);
    			}
    		}
    	}

    	// new rack to insert
    	for (i=0; i < idRackNewLength; i++)
    	{
    		j = 0;
    		while ((j < idRackOldLength) && (!saved_rack_info[j].equalsIgnoreCase(info_rack_new[i])))
    			j++;
    		if (j >= idRackOldLength)
    		{
    			// save iddevice to add
    			idDevToAdd[h] = iddevice[i];
    			// save device code to add
    			idDevCodeToAdd[h] = idDevCode[i];
    			spCodeToAdd[h] = sp[i];
    			h++;
    		}
    	}
    	
    	SeqMgr o = SeqMgr.getInstance();
                
    	// insert into FSRACK
    	if (h > 0)
    	{
    		Object[][] rackParams = new Object[h][7]; // x ogni riga: iddevice + 4 x valori var + aux.
    		
    		RecordSet rsFinal = null;
        	
        	for (int r2a = 0; r2a < h; r2a++)
        	{
        		//  for each rack, retrieve vars form 1 to 4
        		sql = "select * from fsdevmdl where (israck = ?) and (devcode = '" + idDevCodeToAdd[r2a] + "') and var1='"+spCodeToAdd[r2a]+"'";
        		Object[] params = new Object[1];
        		params[0] = "TRUE";
        	
        		rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
            
        		Object[] parametri = new Object[2];
        		String sqlFinal = "select iddevice, code, idvariable from cfvariable where (iddevice = ?) and (code = ?) and idhsvariable is not null";
        		
        		// by default aux field = "new"
        		rackParams[r2a][6] = "new";
        		
        		i = 0;
        		rackParams[r2a][i++] = o.next(null, "fsrack", "idrack");  //idrack
        		rackParams[r2a][i++] = new Integer(idDevToAdd[r2a]); // = iddev
        		
        		// for each var of fsdevmdl
        		boolean aux_old = true;
        		for (w = 0; w < 4; w++)
        		{
        			parametri[0] = idDevToAdd[r2a];
        			parametri[1] = rs.get(0).get("var" + (w+1)); // = var(w+1)
        			        			
        			rsFinal = DatabaseMgr.getInstance().executeQuery(null, sqlFinal, parametri);
        			
        			// retrieve from CFVARIABLE ids of variables 
        			if (rsFinal.size() > 0)
        			{
        				rackParams[r2a][i] = rsFinal.get(0).get("idvariable");
        			}
        			else
        			{
        				rackParams[r2a][i] = null;
        			}

        			if (null == rackParams[r2a][i])
        			{
        				rackParams[r2a][i] = parametri[1];
        			}
        			else// if one of variable from 2 to 4 is null, aux = "old"
        				if(w>1)
        					aux_old = false;
        			// next param
        			i++;
        		}
        		if(aux_old)
        			rackParams[r2a][6] = "old";
        	}
        	
        	sql = "insert INTO fsrack (idrack, iddevice,setpoint, minset, maxset, gradient, timewindow, waittime, maxofftime, maxoffutil, aux)" 
        			+ " VALUES (?,?, ?, ?, ?, ?, "+TIME_WINDOW+", "+WAIT_TIME+", "+MAX_OFF_TIME+", "+MAX_OFF_UTIL+", ?)";
        	
        	DatabaseMgr.getInstance().executeMultiStatement(null, sql, rackParams);
    	}
    }
}
