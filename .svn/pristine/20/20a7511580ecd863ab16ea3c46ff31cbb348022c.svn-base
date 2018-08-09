package com.carel.supervisor.presentation.vscheduler;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.director.vscheduler.CData;
import com.carel.supervisor.director.vscheduler.CDataDef;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.text.SimpleDateFormat;


public class VSCategory extends VSBase {
	// db columns
	public static final String CATEGORY_ID = "idcategory"; 
	public static final String CATEGORY_NAME = "name"; 
	public static final String CATEGORY_SYMBOL = "symbol";

	// used by isDevMdl
	private int idCatCached = 0;
	private int aidCatDevMdls[] = new int[0];
	
	// ids of preloaded categories starts from 1066000 
	private static final int CATEGORY_DEFAULT = 1066000;
	
	// used during category import
	private HashMap<String, Integer> mapEmptyCat = null;
	private HashMap<String, Integer> mapNoDevMdl = null;
	
	public VSCategory()
	{
	}
	
	
	public VSCategory(int idSite, String language)
	{
		super(idSite, language);
	}
	
	
	public void addCategory(String strName, String strSymbol, int[][] anMap)
	{
		try {
			Connection connection = DatabaseMgr.getInstance().getConnection(null);			
			PreparedStatement preparedStatement = connection.prepareStatement("insert into vs_category values(DEFAULT, ?, ?);");
			preparedStatement.setObject(1, strName);
			preparedStatement.setObject(2, strSymbol);
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement("select currval('vs_category_idcategory_seq');");
			ResultSet rs = preparedStatement.executeQuery();
			if( anMap.length > 0 ) {
				if( rs.next() == true ) {
					int idCategory = rs.getInt(1);
					connection.commit();
					connection.close();
					String sql = "insert into vs_categorymap values(?, ?, ?, ?)";
					for(int i = 0; i < anMap.length; i++) {
						int nFlags = 0;
						if( anMap[i][2] != 0 )
							nFlags |= CDataDef.nReverseLogic;
						if( anMap[i][3] != 0 )
							nFlags |= CDataDef.nAutoReset;
						Object params[] = new Object[] { idCategory, anMap[i][0], anMap[i][1], nFlags };
						DatabaseMgr.getInstance().executeStatement(null, sql, params);
					}
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
		}
		catch(Exception e){
			LoggerMgr.getLogger(VSCategory.class).error(e);
		}//catch		
	}
	
	
	public void updateCategory(int id, String strName, String strSymbol, int[][] anMap)
	{
		try {
			String sql = "update vs_category set name = ?, symbol = ? where idcategory = ?";
			Object params[] = new Object[] { strName, strSymbol, id };
			DatabaseMgr.getInstance().executeStatement(null, sql, params);
			sql = "delete from vs_categorymap where idcategory = " + id;
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
			if( anMap.length > 0 ) {
				sql = "insert into vs_categorymap values(?, ?, ?, ?)";
				for(int i = 0; i < anMap.length; i++) {
					int nFlags = 0;
					if( anMap[i][2] != 0 )
						nFlags |= CDataDef.nReverseLogic;
					if( anMap[i][3] != 0 )
						nFlags |= CDataDef.nAutoReset;
					params = new Object[] { id, anMap[i][0], anMap[i][1], nFlags };
					DatabaseMgr.getInstance().executeStatement(null, sql, params);
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
		}
	}

	
	public void removeCategory(int id)
	{
		try {
			String sql = (id == -1 ? "delete from vs_category"
				: "delete from vs_category where idcategory = " + id);
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
			// vs_categorymap related records are removed by db constrints
			// no need to remove them explicitly
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
		}
	}
	
	
	public boolean isDevMdl(int idCat, int idDevMdl)
	{
		if( idCat != idCatCached ) {
			idCatCached = idCat;
			try {
				String sql = "select distinct iddevmdl from vs_categorymap where idcategory =" + idCat;
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
				aidCatDevMdls = new int[rs.size()];
				boolean bRet = false;
				// caching cat and lookup idDevMdl
				for(int i = 0; i < aidCatDevMdls.length; i++) {
					aidCatDevMdls[i] = Integer.parseInt(rs.get(i).get(0).toString());
					if( idDevMdl == aidCatDevMdls[i] )
						bRet = true;
				}
				return bRet;
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(VSCategory.class).error(e);
				idCatCached = 0;
				aidCatDevMdls = new int[0];
				return false;
			}
		}
		// lookup idDevMdl using cached cat
		for(int i = 0; i < aidCatDevMdls.length; i++)
			if( idDevMdl == aidCatDevMdls[i] )
				return true;
		return false;
	}
	
	
	public boolean isDevAvailable(int idCat, int idDevMdl, int idDev)
	{
		if( !isDevMdl(idCat, idDevMdl) )
			return false;
		
		try {
			String sql = "select * from vs_groupdevs inner join vs_group on vs_groupdevs.idgroup = vs_group.idgroup where idcategory = ? and iddevice = ?";
			Object params[] = new Object[] { idCat, idDev };
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			return rs.size() <= 0;
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return false;
		}
	}
	
	
	public Properties[] getCategories()
	{
		
		try {
			String sql = "select * from vs_category where app is NULL order by name";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			Properties aProp[] = new Properties[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				aProp[i] = new Properties();
				aProp[i].setProperty(CATEGORY_ID, r.get(CATEGORY_ID).toString());
				aProp[i].setProperty(CATEGORY_NAME, r.get(CATEGORY_NAME).toString());
				aProp[i].setProperty(CATEGORY_SYMBOL, r.get(CATEGORY_SYMBOL).toString());
			}
			return aProp;
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return new Properties[0];
		}
	}
	
	
	public String getHTMLCategoryTable()
	{
		try {
			// data
			String sql = "select * from vs_category where app is NULL order by idcategory";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				data[i] = new HTMLElement[4];
				data[i][0] = new HTMLSimpleElement(r.get(CATEGORY_NAME).toString());
				String strFileName = r.get(CATEGORY_SYMBOL).toString();
				String strName = lang.getString("vs", strFileName);
				data[i][1] = new HTMLSimpleElement(strName.length() == 0 ? strFileName : strName);
				data[i][2] = new HTMLSimpleElement("<img src='" + SYMBOL_PATH + strFileName + "'>");
				astrClickRowFunction[i] = r.get(CATEGORY_ID).toString();
				// hidden column
				data[i][3] = new HTMLSimpleElement("" + getCategoryVariablesNo((Integer)r.get(CATEGORY_ID)));
			}
			
			// header
			String[] headerTable = new String[3];
	        headerTable[0] = lang.getString("vs", "cat_name");
	        headerTable[1] = lang.getString("vs", "symbol");
	        headerTable[2] = "";
	        
	        // table
	        HTMLTable table = new HTMLTable("categoryTable", headerTable, data);
	        table.setTableId(1);
	        table.setSgClickRowAction("onSelectCategory('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onModifyCategory('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(nScreenWidth);
	        table.setScreenH(nScreenHeight);
	        table.setRowHeight(24); // to match the symbol height
	        table.setHeight(106); // room for 4 categories
	        table.setColumnSize(0, 440);
	        table.setColumnSize(1, 340);
	        table.setColumnSize(2, 30);
	        table.setWidth(900);
	        table.setAlignType(new int[] { 0, 0, 1 });
	        return table.getHTMLText();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return "";
		}
	}
	

	public String getHTMLCatMapTable()
	{
		// data
		HTMLElement[][] data = new HTMLElement[0][];
		
		// header
		String[] headerTable = new String[5];
        headerTable[0] = lang.getString("vs", "dev_mdl");
        headerTable[1] = lang.getString("vs", "var_mdl");
        headerTable[2] = lang.getString("vs", "var_type");
        headerTable[3] = lang.getString("vs", "reverse_logic");
        headerTable[4] = lang.getString("vs", "auto_reset");
        
        // table
        HTMLTable table = new HTMLTable("catmapTable", headerTable, data);
        table.setTableId(2);
        table.setSgClickRowAction("onSelectCatMap('$1')");
        //table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onDeleteCatMap('$1')");
        //table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(nScreenWidth);
        //table.setScreenH(nScreenHeight);
        table.setRowHeight(20);
        table.setHeight(176);
        table.setColumnSize(0, 300);
        table.setColumnSize(1, 300);
        table.setColumnSize(2, 50);
        table.setColumnSize(3, 50);
        table.setColumnSize(4, 50);
        table.setWidth(850);
        table.setAlignType(new int[] { 0, 0, 0, 1, 1 });

        return table.getHTMLText();
	}
	

	public String getHTMLDevMdlTable()
	{
		try {
			// data
			DevMdlBeanList listDevMdl = new DevMdlBeanList();
			DevMdlBean[] aDevMdl = listDevMdl.retrieve(idSite, lang.getLanguage());
			HTMLElement[][] data = new HTMLElement[aDevMdl.length][];
			String[] astrClickRowFunction = new String[aDevMdl.length];
			for(int i = 0; i < aDevMdl.length; i++) {
				data[i] = new HTMLElement[1];
				data[i][0] = new HTMLSimpleElement(aDevMdl[i].getDescription());
				astrClickRowFunction[i] = "" + aDevMdl[i].getIddevmdl();
			}
			
			// header
			String[] headerTable = new String[1];
	        headerTable[0] = lang.getString("vs", "dev_mdl");
	        
	        // table
	        HTMLTable table = new HTMLTable("devmdlTable", headerTable, data);
	        table.setTableId(3);
	        table.setSgClickRowAction("onSelectDevModel('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onSelectDevModel('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(nScreenWidth);
	        table.setHeight(176);
	        table.setColumnSize(0,330);
	        table.setWidth(370);
	        
	        return table.getHTMLTextBufferNoWidthCalc().toString();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return "";
		}
	}
	
	
	public String getHTMLVarMdlTable()
	{
		// data
		HTMLElement[][] data = new HTMLElement[0][];
		/*
		data[0] = new HTMLElement[2];
		data[0][0] = new HTMLSimpleElement("");
		data[0][1] = new HTMLSimpleElement("");
		*/
		// header
		String[] headerTable = new String[3];
        headerTable[0] = lang.getString("vs", "var_code");
		headerTable[1] = lang.getString("vs", "var_mdl");
        headerTable[2] = lang.getString("vs", "var_type");
        
        // table
        HTMLTable table = new HTMLTable("varmdlTable", headerTable, data);
        table.setTableId(4);
        table.setSgClickRowAction("onSelectVarModel('$1')");
        //table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onAddCatMap('$1')");
        //table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(nScreenWidth);
        table.setHeight(176);
        table.setColumnSize(0, 60);
        table.setColumnSize(1, 300);
        table.setColumnSize(2, 40);
        table.setWidth(450);
        
        return table.getHTMLText();
	}
	
	
	public static int getCategoryVariablesNo(int idCat)
	{
		int nCount = 0;
		try {
			String sql = "select iddevice from vs_group inner join vs_groupdevs "
				+ "on vs_group.idgroup=vs_groupdevs.idgroup where idcategory=?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idCat });
			for(int i = 0; i < rs.size(); i++) {
				int idDevice = (Integer)rs.get(i).get(0);
				nCount += VSGroup.getDeviceVariablesNo(idDevice, idCat);
			}
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
		}
		return nCount;
	}
	
	
	public boolean importCategories(String strFileName)
	{
		// read xml file
		StringBuffer strbufImpExp = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(strFileName)), "UTF8"));
			String strLine;
			while( (strLine = br.readLine()) != null )
				strbufImpExp.append(strLine);
			br.close();
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return false;
		}
		
		try {
			int idLastCategory = 0;
			XMLNode xmlImpExp = XMLNode.parse(strbufImpExp.toString());
			if( xmlImpExp.getNodeName().equals("vs_impexp") ) {
				// file valid, remove all existing categories (db contraints remove groups and schedules)
				removeCategory(-1);
				
				// collect additional info
				mapNoDevMdl = new HashMap<String, Integer>();
				mapEmptyCat = new HashMap<String, Integer>();
				
				XMLNode axmlCategory[] = xmlImpExp.getNodes();
				for(int i = 0; i < axmlCategory.length; i++) {
					if( axmlCategory[i].getNodeName().equals("vs_category") ) {
						XMLNode xmlCategory = axmlCategory[i]; 
						int idcategory = Integer.parseInt(xmlCategory.getAttribute("idcategory"));
						if( idcategory > idLastCategory && idcategory < CATEGORY_DEFAULT)
							idLastCategory = idcategory;
						String name = xmlCategory.getAttribute("name");
						String symbol = xmlCategory.getAttribute("symbol");
						
						try {
							// add category from file
							String sqlCat = "insert into vs_category values(?, ?, ?);";
							Object paramsCat[] = new Object[] { idcategory, name, symbol };
							DatabaseMgr.getInstance().executeStatement(null, sqlCat, paramsCat);
							
							XMLNode axmlCategoryMap[] = xmlCategory.getNodes();
							for(int j = 0; j < axmlCategoryMap.length; j++) {
								if( axmlCategoryMap[j].getNodeName().equals("vs_categorymap") ) {
									XMLNode xmlCategoryMap = axmlCategoryMap[j];
									String devcode = xmlUnEscape(xmlCategoryMap.getAttribute("devcode"));
									String varcode = xmlUnEscape(xmlCategoryMap.getAttribute("varcode"));
									int flags = Integer.parseInt(xmlCategoryMap.getAttribute("flags"));
									try {
										// add category map from file
										String sqlCatMap = "insert into vs_categorymap values(?, (select iddevmdl from cfdevmdl where code=?), (select idvarmdl from cfvarmdl where iddevmdl=(select iddevmdl from cfdevmdl where code=?) and code=?), ?);";
										Object paramsCatMap[] = new Object[] { idcategory, devcode, devcode, varcode, flags };
										DatabaseMgr.getInstance().executeStatement(null, sqlCatMap, paramsCatMap);
									}
									catch(DataBaseException e) {
										mapNoDevMdl.put(devcode, new Integer(0));
										LoggerMgr.getLogger(VSCategory.class).error(e);
									}
								}
							}
						}
						catch(DataBaseException e) {
							LoggerMgr.getLogger(VSCategory.class).error(e);
						}
					}
				}
				
				try {
					// update sequences
					String sql = "select setval(?, ?, ?)";
					Object params[] = new Object[] { "vs_category_idcategory_seq", idLastCategory, true };
					DatabaseMgr.getInstance().executeStatement(null, sql, params);
					params = new Object[] { "vs_group_idgroup_seq", 1, false };
					DatabaseMgr.getInstance().executeStatement(null, sql, params);
				}
				catch(DataBaseException e) {
					LoggerMgr.getLogger(VSCategory.class).error(e);
				}
				
				try {
					// remove empty categories
					String sql = "select * from vs_category where idcategory not in (select idcategory from vs_categorymap)";
					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
					for(int i = 0; i < rs.size(); i++) {
						Record r = rs.get(i);
						Integer id = (Integer)r.get(CATEGORY_ID);
						String name = r.get(CATEGORY_NAME).toString();
						mapEmptyCat.put(name, id);
						removeCategory(id);
					}
				}
				catch(DataBaseException e) {
					LoggerMgr.getLogger(VSCategory.class).error(e);
				}
			} // if( xmlImpExp.getNodeName().equals("vs_impexp") )
			else {
				LoggerMgr.getLogger(VSCategory.class).error("invalid root node " + xmlImpExp.getNodeName() + ", expected vs_impexp");
				return false;
			}
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			LoggerMgr.getLogger(VSCategory.class).info(strbufImpExp.toString());
			return false;
		}
		
		return true;
	}
	
	
	public boolean exportCategories(String strFileName)
	{
		// prepare xml data
		XMLNode xmlImpExp = new XMLNode("vs_impexp", "");
		xmlImpExp.setAttribute("date", (new SimpleDateFormat("yyyy/MM/dd hh:mm")).format(Calendar.getInstance().getTime()));
		Properties cat[] = getCategories();
		String sql = "select vs_categorymap.flags, " 
			+ "(select code from cfdevmdl where vs_categorymap.iddevmdl=cfdevmdl.iddevmdl) as devcode, " 
			+ "(select code from cfvarmdl where vs_categorymap.idvarmdl=cfvarmdl.idvarmdl) as varcode " 
			+ "from vs_categorymap where idcategory = ?;";				
		for(int i = 0; i < cat.length; i++) {
			XMLNode xmlCategory = new XMLNode("vs_category", "");
			xmlCategory.setAttribute("idcategory", cat[i].getProperty(CATEGORY_ID));
			xmlCategory.setAttribute("name", cat[i].getProperty(CATEGORY_NAME));
			xmlCategory.setAttribute("symbol", cat[i].getProperty(CATEGORY_SYMBOL));
			try {
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { Integer.parseInt(cat[i].getProperty(CATEGORY_ID)) } );
				for(int j = 0; j < rs.size(); j++) {
					Record r = rs.get(j);
					XMLNode xmlCategoryMap = new XMLNode("vs_categorymap", "");
					xmlCategoryMap.setAttribute("devcode", xmlEscape(r.get("devcode").toString()));
					xmlCategoryMap.setAttribute("varcode", xmlEscape(r.get("varcode").toString()));
					xmlCategoryMap.setAttribute("flags", r.get("flags").toString());
					xmlCategory.addNode(xmlCategoryMap);
				}
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(VSCategory.class).error(e);
			}
			xmlImpExp.addNode(xmlCategory);
		}
		
		// write xml file
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(strFileName)), "UTF8"), true);
			pw.write(xmlImpExp.getStringBuffer().toString());
			pw.close();
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return false;
		}
		
		return true;
	}
	
	
	// call after category import
	public String getNoDevMdls()
	{
		if( !mapNoDevMdl.isEmpty() ) {
			StringBuffer strbuf = new StringBuffer();
			Iterator<String> keys = mapNoDevMdl.keySet().iterator();
			while( keys.hasNext() )
				strbuf.append(keys.next() + "\n");
			return strbuf.toString();
		}
		else {
			return null;
		}
	}
	
	
	// call after category import
	public String getEmptyCat()
	{
		if( !mapEmptyCat.isEmpty() ) {
			StringBuffer strbuf = new StringBuffer();
			Iterator<String> keys = mapEmptyCat.keySet().iterator();
			while( keys.hasNext() )
				strbuf.append(keys.next() + "\n");
			return strbuf.toString();
		}
		else {
			return null;
		}
	}
};
