package com.carel.supervisor.presentation.bean;

import java.text.SimpleDateFormat;
import java.util.*;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*;


public class SiteBookletBean
{
	private int idsite = 0; // used to identify each booklet instance
	
	// binary flags used for catalog
	public static final int FLAG_CAT_DEFAULT	= 0x00000000; // booklet during creation process
	public static final int FLAG_CAT_BOOKLET	= 0x00000001; // booklet created
	public static final int FLAG_CAT_RESET		= 0x00000002; // booklet it is archived; no changes allowed
	
	// binary flags used for all pages
	public static final int FLAG_READ_ONLY		= 0x80000000; // record it is marked as read only


	public SiteBookletBean()
	{
	}

	
	public SiteBookletBean(int idsite)
	{
		this.idsite = idsite;
		loadCatalog();
	}
	
	
	public void loadAll()
	{
		loadCatalog();
		//loadSiteBooklet();
		loadCover();
		loadInstructions();
		loadContacts();
		loadSiteUsage();
		loadSiteType();
		loadSecondaryFluid();
		loadSiteReference();
		loadSafetyDevices1();
		loadSafetyDevices2();
		loadRefrigerantRecovery();
		loadPreventionPlan();
		loadLeakageVerification();
		loadNotices();
	}
	
	
	public void createBooklet()
	{
		try {
			// set booklet flag
			DatabaseMgr.getInstance().executeStatement("update sb_catalog set flags = flags | x'00000001'::int where idsite=?;", new Object[] { idsite });			
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void resetBooklet()
	{
		try {
			if( isReset() ) {
				/* booklet it is archived, it is not possible to make any other change
				// clear reset flag
				DatabaseMgr.getInstance().executeStatement("update sb_catalog set flags = flags & x'FFFFFFFE'::int where idsite=?;", new Object[] { idsite });			
				// set read only flag
				DatabaseMgr.getInstance().executeStatement("update sb_cover set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_instructions set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_contact set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_site_type set st_flags = st_flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_secondary_fluid set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_site_reference set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_safety_devices set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_ref_recovery set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_prevention_plan set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_leak_verify set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_notices set flags = flags | x'80000000'::int where idsite=?;", new Object[] { idsite });
				*/
			}
			else {
				// set reset flag
				DatabaseMgr.getInstance().executeStatement("update sb_catalog set flags = flags | x'00000002'::int, date_archived = current_timestamp where idsite=?;", new Object[] { idsite });			
				/* booklet it is archived, it is not possible to make any other change
				// clear read only flag
				DatabaseMgr.getInstance().executeStatement("update sb_cover set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_instructions set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_contact set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_site_type set st_flags = st_flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_secondary_fluid set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_site_reference set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_safety_devices set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_ref_recovery set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_prevention_plan set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_leak_verify set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				DatabaseMgr.getInstance().executeStatement("update sb_notices set flags = flags & x'7FFFFFFF'::int where idsite=?;", new Object[] { idsite });
				*/
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private boolean isRecordReadOnly(Properties[] aProp, int idrecord)
	{
		if( aProp == null )
			return false;
		
		for(int i = 0; i < aProp.length; i++) {
			Properties prop = aProp[i];
			if( prop != null ) {
				int idProp = Integer.parseInt(prop.getProperty("idrecord"));
				if( idProp == idrecord ) {
					int nFlags = Integer.parseInt(prop.getProperty("flags"));
					return (nFlags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
				}
			}
			
		}
		
		return false;
	}
	
	
	public static String formatBookletDate(String strDateField)
	{
		String strDate = "";
		String[] aDate = strDateField.split("/");
		if( aDate != null && aDate.length == 3 )
			strDate = aDate[2] + "/" + aDate[1] + "/" + aDate[0];
		return strDate;
	}
	
	
	// Catalog
	public String name									= "";
	public String description							= "";
	public String user_name								= "";
	public Date date_creation;
	public Date date_archived;
	// binary flags
	private int catalog_flags							= FLAG_CAT_DEFAULT;

	
	public void loadCatalog()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_catalog where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				name					= (String)r.get("name");
				description				= (String)r.get("description");
				user_name				= (String)r.get("username");
				catalog_flags			= (Integer)r.get("flags");
				date_creation			= (Date)r.get("date_creation");
				if( isReset() )
					date_archived		= (Date)r.get("date_archived");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveCatalog(Properties prop)
	{
		name					= prop.getProperty("name");
		description				= prop.getProperty("description");
		user_name				= prop.getProperty("user_name");
		catalog_flags			= FLAG_CAT_DEFAULT;
		
		try {
			idsite = SeqMgr.getInstance().next(null, "sb_catalog", "idsite");
			DatabaseMgr.getInstance().executeStatement("insert into sb_catalog values (?,?,?,?,?,current_timestamp,null)",
				new Object[] { idsite, name, description, user_name, catalog_flags }
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isBooklet()
	{
		return (catalog_flags & FLAG_CAT_BOOKLET) == FLAG_CAT_BOOKLET;
	}

	
	public boolean isReset()
	{
		return (catalog_flags & FLAG_CAT_RESET) == FLAG_CAT_RESET;
	}
	

	public int getId()
	{
		return idsite;
	}
	
	
	public static String getHtmlCatalog(String language, int nScreenWidth, int nScreenHeight)
	{
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			// data
			String sql = "select * from sb_catalog where flags <> 0 order by idsite;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				astrClickRowFunction[i] = r.get("idsite").toString();
				data[i] = new HTMLElement[7];
				data[i][0] = new HTMLSimpleElement(r.get("name").toString());
				data[i][1] = new HTMLSimpleElement(r.get("description").toString());
				boolean bReset = ((Integer)r.get("flags") & FLAG_CAT_RESET) == FLAG_CAT_RESET;
				data[i][2] = new HTMLSimpleElement(bReset ? "<img src='images/actions/reset_on_black.png'>" : "&nbsp;");
				data[i][3] = new HTMLSimpleElement(r.get("username").toString());
				data[i][4] = new HTMLSimpleElement(sdf.format((Date)r.get("date_creation")));
				data[i][5] = new HTMLSimpleElement(bReset ? sdf.format((Date)r.get("date_archived")): "&nbsp;");
				// hidden column
				data[i][6] = new HTMLSimpleElement(r.get("flags").toString());
			}
			
			// header
			LangService lang = LangMgr.getInstance().getLangService(language);
			String[] headerTable = new String[6];
	        headerTable[0] = lang.getString("sitebooklet_cat", "name");
	        headerTable[1] = lang.getString("sitebooklet_cat", "description");
	        headerTable[2] = lang.getString("sitebooklet_cat", "status");
	        headerTable[3] = lang.getString("sitebooklet_cat", "user_name");
	        headerTable[4] = lang.getString("sitebooklet_cat", "date_creation");
	        headerTable[5] = lang.getString("sitebooklet_cat", "date_archived");
			
	        // table
	        HTMLTable table = new HTMLTable("catalogTable", headerTable, data);
	        table.setTableId(1);
	        table.setSgClickRowAction("onSelectBooklet('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onModifyBooklet('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(nScreenWidth);
	        table.setScreenH(nScreenHeight);
	        table.setHeight(320);
	        table.setColumnSize(0, 170);
	        table.setColumnSize(1, 380);
	        table.setColumnSize(2, 20);
	        table.setColumnSize(3, 50);
	        table.setColumnSize(4, 20);
	        table.setColumnSize(5, 20);
	        table.setWidth(900);
	        table.setAlignType(new int[] { 0, 0, 1, 0, 0, 0 });
	        return table.getHTMLText();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(SiteBookletBean.class).error(e);
			return "";
		}
	}
	
	
	// Site booklet (page replaced by Cover)
	public String header								= "";
	public String note									= "";
	public String ref_low_temp_name						= "";
	public String ref_low_temp_id						= "";
	public String ref_avg_temp_name						= "";
	public String ref_avg_temp_id						= "";
	public String cond_supermarket_name					= "";
	public String cond_supermarket_id					= "";
	public String cond_units_name						= "";
	public String cond_units_id							= "";
	public String heat_pump_name						= "";
	public String heat_pump_id							= "";
	// binary flags
	private int site_booklet_flags						= 0;
	public static final int FLAG_ref_low_temp_name		= 0x001;
	public static final int FLAG_ref_low_temp_id		= 0x002;
	public static final int FLAG_ref_avg_temp_name		= 0x004;
	public static final int FLAG_ref_avg_temp_id		= 0x008;
	public static final int FLAG_cond_supermarket_name	= 0x010;
	public static final int FLAG_cond_supermarket_id	= 0x020;
	public static final int FLAG_cond_units_name		= 0x040;
	public static final int FLAG_cond_units_id			= 0x080;
	public static final int FLAG_heat_pump_name			= 0x100;
	public static final int FLAG_heat_pump_id			= 0x200;
	
	
	public void loadSiteBooklet()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_main where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				header					= (String)r.get("header");
				note					= (String)r.get("note");
				ref_low_temp_name		= (String)r.get("ref_low_temp_name");
				ref_low_temp_id			= (String)r.get("ref_low_temp_id");
				ref_avg_temp_name		= (String)r.get("ref_avg_temp_name");
				ref_avg_temp_id			= (String)r.get("ref_avg_temp_id");
				cond_supermarket_name	= (String)r.get("cond_supermarket_name");
				cond_supermarket_id		= (String)r.get("cond_supermarket_id");
				cond_units_name			= (String)r.get("cond_units_name");
				cond_units_id			= (String)r.get("cond_units_id");
				heat_pump_name			= (String)r.get("heat_pump_name");
				heat_pump_id			= (String)r.get("heat_pump_id");
				site_booklet_flags		= (Integer)r.get("flags");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveSiteBooklet(Properties prop)
	{
		header					= prop.getProperty("header");
		note					= prop.getProperty("note");
		ref_low_temp_name		= prop.getProperty("ref_low_temp_name");
		ref_low_temp_id			= prop.getProperty("ref_low_temp_id");
		ref_avg_temp_name		= prop.getProperty("ref_avg_temp_name");
		ref_avg_temp_id			= prop.getProperty("ref_avg_temp_id");
		cond_supermarket_name	= prop.getProperty("cond_supermarket_name");
		cond_supermarket_id		= prop.getProperty("cond_supermarket_id");
		cond_units_name			= prop.getProperty("cond_units_name");
		cond_units_id			= prop.getProperty("cond_units_id");
		heat_pump_name			= prop.getProperty("heat_pump_name");
		heat_pump_id			= prop.getProperty("heat_pump_id");
		site_booklet_flags = 0;
		if( prop.getProperty("cb_ref_low_temp_name") != null )
			site_booklet_flags |= FLAG_ref_low_temp_name;
		if( prop.getProperty("cb_ref_low_temp_id") != null )
			site_booklet_flags |= FLAG_ref_low_temp_id;
		if( prop.getProperty("cb_ref_avg_temp_name") != null )
			site_booklet_flags |= FLAG_ref_avg_temp_name;
		if( prop.getProperty("cb_ref_avg_temp_id") != null )
			site_booklet_flags |= FLAG_ref_avg_temp_id;
		if( prop.getProperty("cb_cond_supermarket_name") != null )
			site_booklet_flags |= FLAG_cond_supermarket_name;
		if( prop.getProperty("cb_cond_supermarket_id") != null )
			site_booklet_flags |= FLAG_cond_supermarket_id;
		if( prop.getProperty("cb_cond_units_name") != null )
			site_booklet_flags |= FLAG_cond_units_name;
		if( prop.getProperty("cb_cond_units_id") != null )
			site_booklet_flags |= FLAG_cond_units_id;
		if( prop.getProperty("cb_heat_pump_name") != null )
			site_booklet_flags |= FLAG_heat_pump_name;
		if( prop.getProperty("cb_heat_pump_id") != null )
			site_booklet_flags |= FLAG_heat_pump_id;
		
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_main where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_main values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,current_timestamp)",
				new Object[] { idsite, header, note,
					ref_low_temp_name,
					ref_low_temp_id,
					ref_avg_temp_name,
					ref_avg_temp_id,
					cond_supermarket_name,
					cond_supermarket_id,
					cond_units_name,
					cond_units_id,
					heat_pump_name,
					heat_pump_id,
					site_booklet_flags
				}
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public String checkSiteBookletFlag(int nFlag)
	{
		return (nFlag & site_booklet_flags) == nFlag ? "checked" : "";
	}

	
	// Cover
	//public String header								= "";
	public String smk									= "";
	public String system_type							= "";
	public String circuit_id							= "";
	// binary flags
	private int cover_flags								= 0;

	
	public void loadCover()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_cover where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				header					= (String)r.get("header");
				smk						= (String)r.get("smk");
				system_type				= (String)r.get("system_type");
				circuit_id				= (String)r.get("circuit_id");
				cover_flags				= (Integer)r.get("flags");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveCover(Properties prop)
	{
		header					= prop.getProperty("header");
		smk						= prop.getProperty("smk");
		system_type				= prop.getProperty("system_type");
		circuit_id				= prop.getProperty("circuit_id");
		cover_flags				= FLAG_READ_ONLY;
		
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_cover where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_cover values (?,?,?,?,?,?,current_timestamp)",
				new Object[] { idsite,
					header,
					smk,
					system_type,
					circuit_id,
					cover_flags
				}
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isCoverReadOnly()
	{
		return (cover_flags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
	}
	
	
	// Instructions
	public String text									= "";
	// binary flags
	private int instructions_flags						= 0;
	
	
	public void loadInstructions()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_instructions where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				text					= (String)r.get("text");
				instructions_flags		= (Integer)r.get("flags");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveInstructions(Properties prop)
	{
		text					= prop.getProperty("text");
		instructions_flags		= FLAG_READ_ONLY;
		
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_instructions where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_instructions values (?,?,?,current_timestamp)",
				new Object[] { idsite, text, instructions_flags }
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isInstructionsReadOnly()
	{
		return (instructions_flags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
	}
	
	
	// Contact
	// contact type
	public static final int CTYPE_OWNER					= 0;
	public static final int CTYPE_MANAGER				= 1;
	public static final int CTYPE_PLANT					= 2;
	public static final int CTYPE_HEAD_OF_PLANT			= 3;
	public static final int CTYPE_HEAD_OF_AUDIT			= 4;
	public Properties[] aContacts = new Properties[5];
	
	
	public void loadContacts()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_contact where idsite=?",
				new Object[] { idsite }
			);
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Properties prop = new Properties();
				aContacts[(Integer)r.get("ctype")] = prop;
				prop.setProperty("surname", (String)r.get("surname"));
				prop.setProperty("name", (String)r.get("name"));
				prop.setProperty("address", (String)r.get("address"));
				prop.setProperty("phone", (String)r.get("phone"));
				prop.setProperty("fax", (String)r.get("fax"));
				prop.setProperty("e_mail", (String)r.get("e_mail"));
				prop.setProperty("other", (String)r.get("other"));
				prop.setProperty("flags", r.get("flags").toString());
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	

	public void saveContact(int ctype, Properties prop)
	{
		switch( ctype ) {
		case CTYPE_OWNER:
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_contact where idsite=? and ctype=?",
						new Object[] { idsite, ctype }
				);
				DatabaseMgr.getInstance().executeStatement("insert into sb_contact values (?,?,?,'',?,?,?,?,'',?,current_timestamp)",
					new Object[] { idsite, ctype,
						prop.getProperty("surname_0"),
						prop.getProperty("address_0"),
						prop.getProperty("phone_0"),
						prop.getProperty("fax_0"),
						prop.getProperty("e_mail_0"),
						FLAG_READ_ONLY
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
			break;
		case CTYPE_MANAGER:
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_contact where idsite=? and ctype=?",
						new Object[] { idsite, ctype }
				);
				DatabaseMgr.getInstance().executeStatement("insert into sb_contact values (?,?,?,'','',?,?,?,'',?,current_timestamp)",
					new Object[] { idsite, ctype,
						prop.getProperty("surname_1"),
						prop.getProperty("phone_1"),
						prop.getProperty("fax_1"),
						prop.getProperty("e_mail_1"),
						FLAG_READ_ONLY
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
			break;
		case CTYPE_PLANT:
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_contact where idsite=? and ctype=?",
						new Object[] { idsite, ctype }
				);
				DatabaseMgr.getInstance().executeStatement("insert into sb_contact values (?,?,'','',?,?,?,?,'',?,current_timestamp)",
					new Object[] { idsite, ctype,
						prop.getProperty("address_2"),
						prop.getProperty("phone_2"),
						prop.getProperty("fax_2"),
						prop.getProperty("e_mail_2"),
						FLAG_READ_ONLY
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
			break;
		case CTYPE_HEAD_OF_PLANT:
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_contact where idsite=? and ctype=?",
						new Object[] { idsite, ctype }
				);
				DatabaseMgr.getInstance().executeStatement("insert into sb_contact values (?,?,?,?,'',?,?,?,'',?,current_timestamp)",
					new Object[] { idsite, ctype,
						prop.getProperty("surname_3"),
						prop.getProperty("name_3"),
						prop.getProperty("phone_3"),
						prop.getProperty("fax_3"),
						prop.getProperty("e_mail_3"),
						FLAG_READ_ONLY
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
			break;
		case CTYPE_HEAD_OF_AUDIT:
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_contact where idsite=? and ctype=?",
						new Object[] { idsite, ctype }
				);
				DatabaseMgr.getInstance().executeStatement("insert into sb_contact values (?,?,?,?,'',?,?,?,?,?,current_timestamp)",
					new Object[] { idsite, ctype,
						prop.getProperty("surname_4"),
						prop.getProperty("name_4"),
						prop.getProperty("phone_4"),
						prop.getProperty("fax_4"),
						prop.getProperty("e_mail_4"),
						prop.getProperty("other_4"),
						FLAG_READ_ONLY
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
			break;
		}
	}
	
	
	public boolean isContactReadOnly(int ctype)
	{
		if( aContacts[ctype] == null )
			return false;
		String strFlags = aContacts[ctype].getProperty("flags");
		int nFlags = strFlags != null ? Integer.parseInt(strFlags) : 0;
		return (nFlags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
	}

	
	// Site usage
	public String site_usage = "";
	
	
	public void loadSiteUsage()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select note from sb_site_usage where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				site_usage = (String)r.get("note");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveSiteUsage(Properties prop)
	{
		site_usage = prop.getProperty("note2");
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_site_usage where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_site_usage values (?,?,current_timestamp)",
				new Object[] { idsite, site_usage }
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	// Site type
	// binary flags
	private int site_type_flags							= 0;
	public static final int ST_other					= 0x001;
	public static final int ST_direct_expandion			= 0x002;
	public static final int ST_secondary_fluid			= 0x004;
	public String site_type_other						= "";	
	private int refrigerant_type_flags					= 0;
	public static final int REFT_0						= 0x001;
	public static final int REFT_1						= 0x002;
	public static final int REFT_2						= 0x004;
	public static final int REFT_3						= 0x008;
	public static final int REFT_4						= 0x010;
	public static final int REFT_5						= 0x020;
	public static final int REFT_6						= 0x040;
	public static final int REFT_7						= 0x080;
	public static final int REFT_8						= 0x100;
	public String ref_type_other						= "";
	
	
	public String checkSiteTypeFlag(int nFlag)
	{
		return (nFlag & site_type_flags) == nFlag ? "checked" : "";
	}
	
	
	public String checkRefrigerantTypeFlag(int nFlag)
	{
		return (nFlag & refrigerant_type_flags) == nFlag ? "checked" : "";
	}

	
	public void loadSiteType()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_site_type where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				site_type_flags			= (Integer)r.get("st_flags");
				site_type_other			= (String)r.get("site_type_other");
				refrigerant_type_flags	= (Integer)r.get("rt_flags");
				ref_type_other			= (String)r.get("ref_type_other");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveSiteType(Properties prop)
	{
		site_type_flags = 0;
		if( prop.getProperty("cb_site_type_other") != null ) {
			site_type_flags |= ST_other;
			site_type_other = prop.getProperty("site_type_other");
		}
		if( prop.getProperty("cb_direct_expansion") != null )
			site_type_flags |= ST_direct_expandion;
		if( prop.getProperty("cb_secondary_fluid") != null )
			site_type_flags |= ST_secondary_fluid;
		site_type_flags |= FLAG_READ_ONLY;
		refrigerant_type_flags = 0;
		if( prop.getProperty("cb_ref_type_0") != null ) {
			refrigerant_type_flags |= REFT_0;
			ref_type_other = prop.getProperty("ref_type_0");
		}
		if( prop.getProperty("cb_ref_type_1") != null )
			refrigerant_type_flags |= REFT_1;
		if( prop.getProperty("cb_ref_type_2") != null )
			refrigerant_type_flags |= REFT_2;
		if( prop.getProperty("cb_ref_type_3") != null )
			refrigerant_type_flags |= REFT_3;
		if( prop.getProperty("cb_ref_type_4") != null )
			refrigerant_type_flags |= REFT_4;
		if( prop.getProperty("cb_ref_type_5") != null )
			refrigerant_type_flags |= REFT_5;
		if( prop.getProperty("cb_ref_type_6") != null )
			refrigerant_type_flags |= REFT_6;
		if( prop.getProperty("cb_ref_type_7") != null )
			refrigerant_type_flags |= REFT_7;
		if( prop.getProperty("cb_ref_type_8") != null )
			refrigerant_type_flags |= REFT_8;
		
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_site_type where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_site_type values (?,?,?,?,?,current_timestamp)",
				new Object[] { idsite,
					site_type_flags, site_type_other,
					refrigerant_type_flags, ref_type_other
				}
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isSiteTypeReadOnly()
	{
		return (site_type_flags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
	}
	
	
	public boolean isSecondaryFluid()
	{
		return (site_type_flags & ST_secondary_fluid) == ST_secondary_fluid;
	}
	
	
	// Secondary fluid
	public String water_ethyleneglycol					= "";
	public String water_propyleneglycol					= "";
	public String organic_mixtures						= "";
	public String organic_mixtures_brand				= "";
	public String organic_mixtures_type					= "";
	public String refrigerant_type						= "";
	public String secondary_fluid_other					= "";
	public String secondary_fluid_kg					= "";
	public String secondary_fluid_date					= "";
	// binary flags
	private int secondary_fluid_type_flags				= 0;
	public static final int SFT_water_ethyleneglycol	= 0x01;
	public static final int SFT_water_propyleneglycol	= 0x02;
	public static final int SFT_organic_mixtures		= 0x04;
	public static final int SFT_refrigerant_type		= 0x08;
	public static final int SFT_secondary_fluid_other	= 0x10;
	
	
	public String checkSecondaryFluidTypeFlag(int nFlag)
	{
		return (nFlag & secondary_fluid_type_flags) == nFlag ? "checked" : "";
	}
	
	
	public void loadSecondaryFluid()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_secondary_fluid where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				water_ethyleneglycol		= (String)r.get("water_ethyleneglycol");
				water_propyleneglycol		= (String)r.get("water_propyleneglycol");
				organic_mixtures			= (String)r.get("organic_mixtures");
				organic_mixtures_brand		= (String)r.get("organic_mixtures_brand");
				organic_mixtures_type		= (String)r.get("organic_mixtures_type");
				refrigerant_type			= (String)r.get("refrigerant_type");
				secondary_fluid_other		= (String)r.get("secondary_fluid_other");
				secondary_fluid_kg			= (String)r.get("kg");
				secondary_fluid_date		= (String)r.get("date");
				secondary_fluid_type_flags	= (Integer)r.get("flags");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveSecondaryFluid(Properties prop)
	{
		secondary_fluid_type_flags = 0;
		if( prop.getProperty("cb_water_ethyleneglycol") != null )
			secondary_fluid_type_flags |= SFT_water_ethyleneglycol;
		if( prop.getProperty("cb_water_propyleneglycol") != null )
			secondary_fluid_type_flags |= SFT_water_propyleneglycol;
		if( prop.getProperty("cb_organic_mixtures") != null )
			secondary_fluid_type_flags |= SFT_organic_mixtures;
		if( prop.getProperty("cb_refrigerant_type") != null )
			secondary_fluid_type_flags |= SFT_refrigerant_type;
		if( prop.getProperty("cb_secondary_fluid_other") != null )
			secondary_fluid_type_flags |= SFT_secondary_fluid_other;
		secondary_fluid_type_flags |= FLAG_READ_ONLY;
		
		water_ethyleneglycol		= prop.getProperty("water_ethyleneglycol");
		water_propyleneglycol		= prop.getProperty("water_propyleneglycol");
		organic_mixtures			= prop.getProperty("organic_mixtures");
		organic_mixtures_brand		= prop.getProperty("organic_mixtures_brand");
		organic_mixtures_type		= prop.getProperty("organic_mixtures_type");
		refrigerant_type			= prop.getProperty("refrigerant_type");
		secondary_fluid_other		= prop.getProperty("secondary_fluid_other");
		secondary_fluid_kg			= prop.getProperty("kg");
		secondary_fluid_date		= prop.getProperty("date_year") + "/" + prop.getProperty("date_month") + "/" + prop.getProperty("date_day");
		
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_secondary_fluid where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_secondary_fluid values (?,?,?,?,?,?,?,?,?,?,?,current_timestamp)",
				new Object[] { idsite,
					water_ethyleneglycol,
					water_propyleneglycol,
					organic_mixtures, organic_mixtures_brand, organic_mixtures_type,
					refrigerant_type,
					secondary_fluid_other,
					secondary_fluid_kg,
					secondary_fluid_date,
					secondary_fluid_type_flags
				}
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isSecondaryFluidReadOnly()
	{
		return (secondary_fluid_type_flags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
	}

	
	// Site reference
	public String type_model						= "";
	public String registration_number				= "";
	public String built_year						= "";
	public String brand_model						= "";
	public String min_value							= "";
	public String max_value							= "";
	public String level_estimation					= "";
	public String level_reference					= "";
	// binary flags
	private int site_reference_flags				= 0;
	public static final int SREF_level_sensors		= 0x01;
	public static final int SREF_level_indicators	= 0x02;
	

	public String checkSiteReferenceFlag(int nFlag)
	{
		return (nFlag & site_reference_flags) == nFlag ? "checked" : "";
	}
	

	public void loadSiteReference()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_site_reference where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				type_model					= (String)r.get("type_model");
				registration_number			= (String)r.get("registration_number");
				built_year					= (String)r.get("built_year");
				brand_model					= (String)r.get("brand_model");
				min_value					= (String)r.get("min_value");
				max_value					= (String)r.get("max_value");
				level_estimation			= (String)r.get("level_estimation");
				level_reference				= (String)r.get("level_reference");
				site_reference_flags		= (Integer)r.get("flags");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveSiteReference(Properties prop)
	{
		site_reference_flags = 0;
		if( prop.getProperty("cb_level_sensors") != null )
			site_reference_flags |= SREF_level_sensors;
		if( prop.getProperty("cb_level_indicators") != null )
			site_reference_flags |= SREF_level_indicators;
		site_reference_flags |= FLAG_READ_ONLY;

		type_model					= prop.getProperty("type_model");
		registration_number			= prop.getProperty("registration_number");
		built_year					= prop.getProperty("built_year");
		brand_model					= prop.getProperty("brand_model");
		min_value					= prop.getProperty("min_value");
		max_value					= prop.getProperty("max_value");
		level_estimation			= prop.getProperty("level_estimation");
		level_reference				= prop.getProperty("level_reference");
		
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_site_reference where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_site_reference values (?,?,?,?,?,?,?,?,?,?,current_timestamp)",
				new Object[] { idsite,
					type_model,
					registration_number,
					built_year,
					brand_model,
					min_value, max_value,
					level_estimation,
					level_reference,
					site_reference_flags
				}
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isSiteReferenceReadOnly()
	{
		return (site_reference_flags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
	}
	
	
	// Safety devices - Limitation of pressure
	public Properties[] aSafetyDevices1 = null;

	
	public void loadSafetyDevices1()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_safety_devices_1 where idsite=? order by replacement_date, idrecord",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				aSafetyDevices1 = new Properties[rs.size()];
				for(int i = 0; i < rs.size(); i++) {
					aSafetyDevices1[i] = new Properties();
					Record r = rs.get(i);
					aSafetyDevices1[i].setProperty("idrecord", r.get("idrecord").toString());
					aSafetyDevices1[i].setProperty("safety_pressure_pos", (String)r.get("safety_pressure_pos"));
					aSafetyDevices1[i].setProperty("safety_pressure_series", (String)r.get("safety_pressure_series"));
					aSafetyDevices1[i].setProperty("safety_pressure_action", (String)r.get("safety_pressure_action"));
					aSafetyDevices1[i].setProperty("safety_pressure_ped_category", (String)r.get("safety_pressure_ped_category"));
					aSafetyDevices1[i].setProperty("replacement_type", (String)r.get("replacement_type"));
					aSafetyDevices1[i].setProperty("replacement_series", (String)r.get("replacement_series"));
					aSafetyDevices1[i].setProperty("replacement_pressure_calibration", (String)r.get("replacement_pressure_calibration"));
					aSafetyDevices1[i].setProperty("replacement_ped_category", (String)r.get("replacement_ped_category"));
					aSafetyDevices1[i].setProperty("replacement_expires", (String)r.get("replacement_expires"));
					aSafetyDevices1[i].setProperty("replacement_date", (String)r.get("replacement_date"));
					aSafetyDevices1[i].setProperty("flags", r.get("flags").toString());
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveSafetyDevices1(UserSession us, Properties prop)
	{
		String cmd = prop.getProperty("cmd");
		if( cmd.equals("save") ) {
			try {
				String strIdRecord = prop.getProperty("idrecord");
				Integer idrecord = 0;
				if( !strIdRecord.isEmpty() ) {
					idrecord = Integer.parseInt(strIdRecord); 
					DatabaseMgr.getInstance().executeStatement("delete from sb_safety_devices where idsite=? and idrecord=?",
						new Object[] { idsite, idrecord }
					);
				}
				else {
					 idrecord = SeqMgr.getInstance().next(null, "sb_safety_devices", "idrecord");
				}
				DatabaseMgr.getInstance().executeStatement("insert into sb_safety_devices_1 values (?,?,?,?,?,?,?,?,?,?,?,?,?,current_timestamp)",
					new Object[] { idsite, idrecord,
						prop.getProperty("safety_pressure_pos"),
						prop.getProperty("safety_pressure_series"),
						prop.getProperty("safety_pressure_action"),
						prop.getProperty("safety_pressure_ped_category"),
						prop.getProperty("replacement_type"),
						prop.getProperty("replacement_series"),
						prop.getProperty("replacement_pressure_calibration"),
						prop.getProperty("replacement_ped_category"),
						prop.getProperty("replacement_expires"),
						prop.getProperty("date_year") + "/" + prop.getProperty("date_month") + "/" + prop.getProperty("date_day"),
						FLAG_READ_ONLY
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("del") ) {
			Integer idrecord = Integer.parseInt(prop.getProperty("idrecord"));
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_safety_devices_1 where idsite=? and idrecord=?",
					new Object[] { idsite, idrecord }
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("mod") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
			ut.setProperty("idrecord", prop.getProperty("idrecord"));
		}
		else if( cmd.equals("add") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
		}
	}
	
	
	public boolean isSafetyDevices1ReadOnly(int idrecord)
	{
		return isRecordReadOnly(aSafetyDevices1, idrecord);
	}
	
	
	// Safety devices - Pressure relief
	public Properties[] aSafetyDevices2 = null;

	
	public void loadSafetyDevices2()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_safety_devices_2 where idsite=? order by replacement_date, idrecord",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				aSafetyDevices2 = new Properties[rs.size()];
				for(int i = 0; i < rs.size(); i++) {
					aSafetyDevices2[i] = new Properties();
					Record r = rs.get(i);
					aSafetyDevices2[i].setProperty("idrecord", r.get("idrecord").toString());
					aSafetyDevices2[i].setProperty("safety_valve_pos", (String)r.get("safety_valve_pos"));
					aSafetyDevices2[i].setProperty("safety_valve_series", (String)r.get("safety_valve_series"));
					aSafetyDevices2[i].setProperty("safety_valve_pressure_calibration", (String)r.get("safety_valve_pressure_calibration"));
					aSafetyDevices2[i].setProperty("replacement_type", (String)r.get("replacement_type"));
					aSafetyDevices2[i].setProperty("replacement_series", (String)r.get("replacement_series"));
					aSafetyDevices2[i].setProperty("replacement_pressure_calibration", (String)r.get("replacement_pressure_calibration"));
					aSafetyDevices2[i].setProperty("replacement_ped_category", (String)r.get("replacement_ped_category"));
					aSafetyDevices2[i].setProperty("replacement_expires", (String)r.get("replacement_expires"));
					aSafetyDevices2[i].setProperty("replacement_date", (String)r.get("replacement_date"));
					aSafetyDevices2[i].setProperty("flags", r.get("flags").toString());
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveSafetyDevices2(UserSession us, Properties prop)
	{
		String cmd = prop.getProperty("cmd");
		if( cmd.equals("save") ) {
			try {
				String strIdRecord = prop.getProperty("idrecord");
				Integer idrecord = 0;
				if( !strIdRecord.isEmpty() ) {
					idrecord = Integer.parseInt(strIdRecord); 
					DatabaseMgr.getInstance().executeStatement("delete from sb_safety_devices_2 where idsite=? and idrecord=?",
						new Object[] { idsite, idrecord }
					);
				}
				else {
					 idrecord = SeqMgr.getInstance().next(null, "sb_safety_devices", "idrecord");
				}
				DatabaseMgr.getInstance().executeStatement("insert into sb_safety_devices_2 values (?,?,?,?,?,?,?,?,?,?,?,?,current_timestamp)",
					new Object[] { idsite, idrecord,
						prop.getProperty("safety_valve_pos"),
						prop.getProperty("safety_valve_series"),
						prop.getProperty("safety_valve_pressure_calibration"),
						prop.getProperty("replacement_type"),
						prop.getProperty("replacement_series"),
						prop.getProperty("replacement_pressure_calibration"),
						prop.getProperty("replacement_ped_category"),
						prop.getProperty("replacement_expires"),
						prop.getProperty("date_year") + "/" + prop.getProperty("date_month") + "/" + prop.getProperty("date_day"),
						FLAG_READ_ONLY
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("del") ) {
			Integer idrecord = Integer.parseInt(prop.getProperty("idrecord"));
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_safety_devices_2 where idsite=? and idrecord=?",
					new Object[] { idsite, idrecord }
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("mod") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
			ut.setProperty("idrecord", prop.getProperty("idrecord"));
		}
		else if( cmd.equals("add") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
		}
	}
	
	
	public boolean isSafetyDevices2ReadOnly(int idrecord)
	{
		return isRecordReadOnly(aSafetyDevices2, idrecord);
	}

	
	// Refrigerant recovery
	public Properties[] aRefrigerantRecovery = null;
	
	
	public void loadRefrigerantRecovery()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_ref_recovery where idsite=? order by date, idrecord",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				aRefrigerantRecovery = new Properties[rs.size()];
				for(int i = 0; i < rs.size(); i++) {
					aRefrigerantRecovery[i] = new Properties();
					Record r = rs.get(i);
					aRefrigerantRecovery[i].setProperty("idrecord", r.get("idrecord").toString());
					aRefrigerantRecovery[i].setProperty("recovered_ref_type", (String)r.get("recovered_ref_type"));
					aRefrigerantRecovery[i].setProperty("recovered_qantity", (String)r.get("recovered_qantity"));
					aRefrigerantRecovery[i].setProperty("recovery_date", (String)r.get("recovery_date"));
					aRefrigerantRecovery[i].setProperty("replaced_ref_type", (String)r.get("replaced_ref_type"));
					aRefrigerantRecovery[i].setProperty("replaced_qantity", (String)r.get("replaced_qantity"));
					aRefrigerantRecovery[i].setProperty("intervention_date", (String)r.get("intervention_date"));
					aRefrigerantRecovery[i].setProperty("recovery_equipment", (String)r.get("recovery_equipment"));
					aRefrigerantRecovery[i].setProperty("iso_equipment", (String)r.get("iso_equipment"));
					aRefrigerantRecovery[i].setProperty("operator_stamp", (String)r.get("operator_stamp"));
					aRefrigerantRecovery[i].setProperty("date", (String)r.get("date"));
					aRefrigerantRecovery[i].setProperty("flags", r.get("flags").toString());
					aRefrigerantRecovery[i].setProperty("user_name", r.get("username").toString());
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveRefrigerantRecovery(UserSession us, Properties prop)
	{
		String cmd = prop.getProperty("cmd");
		if( cmd.equals("save") ) {
			try {
				String strIdRecord = prop.getProperty("idrecord");
				Integer idrecord = 0;
				if( !strIdRecord.isEmpty() ) {
					idrecord = Integer.parseInt(strIdRecord); 
					DatabaseMgr.getInstance().executeStatement("delete from sb_ref_recovery where idsite=? and idrecord=?",
						new Object[] { idsite, idrecord }
					);
				}
				else {
					 idrecord = SeqMgr.getInstance().next(null, "sb_ref_recovery", "idrecord");
				}
				DatabaseMgr.getInstance().executeStatement("insert into sb_ref_recovery values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,current_timestamp)",
					new Object[] { idsite, idrecord,
						prop.getProperty("recovered_ref_type"),
						prop.getProperty("recovered_qantity"),
						prop.getProperty("date1_year") + "/" + prop.getProperty("date1_month") + "/" + prop.getProperty("date1_day"), 
						prop.getProperty("replaced_ref_type"),
						prop.getProperty("replaced_qantity"),
						prop.getProperty("date2_year") + "/" + prop.getProperty("date2_month") + "/" + prop.getProperty("date2_day"), 
						prop.getProperty("recovery_equipment"),
						prop.getProperty("iso_equipment"),
						prop.getProperty("operator_stamp"),
						prop.getProperty("date_year") + "/" + prop.getProperty("date_month") + "/" + prop.getProperty("date_day"),
						FLAG_READ_ONLY,
						prop.getProperty("user_name")
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("del") ) {
			Integer idrecord = Integer.parseInt(prop.getProperty("idrecord"));
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_ref_recovery where idsite=? and idrecord=?",
					new Object[] { idsite, idrecord }
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("mod") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
			ut.setProperty("idrecord", prop.getProperty("idrecord"));
		}
		else if( cmd.equals("add") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
		}
	}
	
	
	public boolean isRefrigerantRecoveryReadOnly(int idrecord)
	{
		return isRecordReadOnly(aRefrigerantRecovery, idrecord);
	}
	
	
	// Prevention plan
	public String prevention_plan_date					= "";
	public String prevention_plan_quantity				= "";
	public String pp_label0								= "";
	public String pp_label1_1							= "";
	public String pp_label1_2							= "";
	public String pp_label1_3							= "";
	public String pp_label2_1							= "";
	public String pp_label2_2							= "";
	public String pp_label2_3							= "";
	public String pp_label3_1							= "";
	public String pp_label3_2							= "";
	public String pp_label3_3							= "";
	// binary flags
	private int prevention_plan_flags					= 0;
	public static final int PP_interval_quarterly		= 0x001;
	public static final int PP_interval_semiannual		= 0x002;
	public static final int PP_interval_annual			= 0x004;
	public static final int PP_check_ref_charge			= 0x008;
	public static final int PP_check_loss_central		= 0x010;
	public static final int PP_check_critical_points	= 0x020;
	public static final int PP_cp_electricity_supply	= 0x040;
	public static final int PP_cp_capacitors			= 0x080;
	public static final int PP_cp_hi_pressure_pipe		= 0x100;
	public static final int PP_cp_evaporators			= 0x200;
	public static final int PP_cp_lo_pressure_pipe		= 0x400;
	public static final int PP_cp_filters_valves		= 0x800;
	
	
	public String checkPreventionPlanFlag(int nFlag)
	{
		return (nFlag & prevention_plan_flags) == nFlag ? "checked" : "";
	}
	
	
	public void loadPreventionPlan()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_prevention_plan where idsite=?",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				Record r = rs.get(0);
				prevention_plan_date		= (String)r.get("date");
				prevention_plan_quantity	= (String)r.get("quantity");
				pp_label0					= (String)r.get("pp_label0");
				pp_label1_1					= (String)r.get("pp_label1_1");
				pp_label1_2					= (String)r.get("pp_label1_2");
				pp_label1_3					= (String)r.get("pp_label1_3");
				pp_label2_1					= (String)r.get("pp_label2_1");
				pp_label2_2					= (String)r.get("pp_label2_2");
				pp_label2_3					= (String)r.get("pp_label2_3");
				pp_label3_1					= (String)r.get("pp_label3_1");
				pp_label3_2					= (String)r.get("pp_label3_2");
				pp_label3_3					= (String)r.get("pp_label3_3");
				prevention_plan_flags		= (Integer)r.get("flags");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	

	public void savePreventionPlan(Properties prop)
	{
		prevention_plan_date		= prop.getProperty("date_year") + "/" + prop.getProperty("date_month") + "/" + prop.getProperty("date_day"); 
		prevention_plan_quantity	= prop.getProperty("prevention_plan_quantity");
		pp_label0					= prop.getProperty("pp_label0");
		pp_label1_1					= prop.getProperty("pp_label1_1");
		pp_label1_2					= prop.getProperty("pp_label1_2");
		pp_label1_3					= prop.getProperty("pp_label1_3");
		pp_label2_1					= prop.getProperty("pp_label2_1");
		pp_label2_2					= prop.getProperty("pp_label2_2");
		pp_label2_3					= prop.getProperty("pp_label2_3");		
		pp_label3_1					= prop.getProperty("pp_label3_1");
		pp_label3_2					= prop.getProperty("pp_label3_2");
		pp_label3_3					= prop.getProperty("pp_label3_3");		
		prevention_plan_flags = 0;
		if( prop.getProperty("cb_interval_quarterly") != null )
			prevention_plan_flags |= PP_interval_quarterly;
		if( prop.getProperty("cb_interval_semiannual") != null )
			prevention_plan_flags |= PP_interval_semiannual;
		if( prop.getProperty("cb_interval_annual") != null )
			prevention_plan_flags |= PP_interval_annual;
		if( prop.getProperty("cb_check_ref_charge") != null )
			prevention_plan_flags |= PP_check_ref_charge;
		if( prop.getProperty("cb_check_loss_central") != null )
			prevention_plan_flags |= PP_check_loss_central;
		if( prop.getProperty("cb_check_critical_points") != null )
			prevention_plan_flags |= PP_check_critical_points;
		if( prop.getProperty("cb_cp_electricity_supply") != null )
			prevention_plan_flags |= PP_cp_electricity_supply;
		if( prop.getProperty("cb_cp_capacitors") != null )
			prevention_plan_flags |= PP_cp_capacitors;
		if( prop.getProperty("cb_cp_hi_pressure_pipe") != null )
			prevention_plan_flags |= PP_cp_hi_pressure_pipe;
		if( prop.getProperty("cb_cp_evaporators") != null )
			prevention_plan_flags |= PP_cp_evaporators;
		if( prop.getProperty("cb_cp_lo_pressure_pipe") != null )
			prevention_plan_flags |= PP_cp_lo_pressure_pipe;
		if( prop.getProperty("cb_cp_filters_valves") != null )
			prevention_plan_flags |= PP_cp_filters_valves;
		prevention_plan_flags |= FLAG_READ_ONLY;
		
		try {
			DatabaseMgr.getInstance().executeStatement("delete from sb_prevention_plan where idsite=?",
				new Object[] { idsite }
			);
			DatabaseMgr.getInstance().executeStatement("insert into sb_prevention_plan values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,current_timestamp)",
				new Object[] { idsite,
					prevention_plan_date,
					prevention_plan_quantity,
					pp_label0,
					pp_label1_1, pp_label1_2, pp_label1_3,
					pp_label2_1, pp_label2_2, pp_label2_3,
					pp_label3_1, pp_label3_2, pp_label3_3,					
					prevention_plan_flags
				}
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isPreventionPlanReadOnly()
	{
		return (prevention_plan_flags & FLAG_READ_ONLY) == FLAG_READ_ONLY;
	}
	
	
	// Leakage verification
	public Properties[] aLeakageVerification = null;
	// binary flags
	public static final int LV_no_leaks					= 0x001;
	public static final int LV_leaks					= 0x002;
	public static final int LV_level_sensors			= 0x004;
	public static final int LV_level_indicators			= 0x008;
	
	
	public String checkLeakageVerificationFlag(int nFlags, int nFlag)
	{
		return (nFlag & nFlags) == nFlag ? "checked" : "";
	}

	
	public void loadLeakageVerification()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_leak_verify where idsite=? order by date, idrecord",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				aLeakageVerification = new Properties[rs.size()];
				for(int i = 0; i < rs.size(); i++) {
					aLeakageVerification[i] = new Properties();
					Record r = rs.get(i);
					aLeakageVerification[i].setProperty("idrecord", r.get("idrecord").toString());
					aLeakageVerification[i].setProperty("instrument_type", (String)r.get("instrument_type"));
					aLeakageVerification[i].setProperty("instrument_sensitivity", (String)r.get("instrument_sensitivity"));
					aLeakageVerification[i].setProperty("test_date", (String)r.get("test_date"));
					aLeakageVerification[i].setProperty("leakage_description", (String)r.get("leakage_description"));
					aLeakageVerification[i].setProperty("corrective_action", (String)r.get("corrective_action"));
					aLeakageVerification[i].setProperty("ref_to_restore_quantity", (String)r.get("ref_to_restore_quantity"));
					aLeakageVerification[i].setProperty("brand_model", (String)r.get("brand_model"));
					aLeakageVerification[i].setProperty("min_value", (String)r.get("min_value"));
					aLeakageVerification[i].setProperty("max_value", (String)r.get("max_value"));
					aLeakageVerification[i].setProperty("level_estimation", (String)r.get("level_estimation"));
					aLeakageVerification[i].setProperty("level_reference", (String)r.get("level_reference"));
					aLeakageVerification[i].setProperty("operator_stamp", (String)r.get("operator_stamp"));
					aLeakageVerification[i].setProperty("date", (String)r.get("date"));
					aLeakageVerification[i].setProperty("flags", r.get("flags").toString());
					aLeakageVerification[i].setProperty("user_name", (String)r.get("username"));
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveLeakageVerification(UserSession us, Properties prop)
	{
		String cmd = prop.getProperty("cmd");
		if( cmd.equals("save") ) {
			try {
				String strIdRecord = prop.getProperty("idrecord");
				Integer idrecord = 0;
				if( !strIdRecord.isEmpty() ) {
					idrecord = Integer.parseInt(strIdRecord); 
					DatabaseMgr.getInstance().executeStatement("delete from sb_leak_verify where idsite=? and idrecord=?",
						new Object[] { idsite, idrecord }
					);
				}
				else {
					 idrecord = SeqMgr.getInstance().next(null, "sb_leak_verify", "idrecord");
				}

				int flags = 0;
				if( prop.getProperty("cb_no_leaks") != null )
					flags |= LV_no_leaks;
				if( prop.getProperty("cb_leaks") != null )
					flags |= LV_leaks;
				if( prop.getProperty("cb_level_sensors") != null )
					flags |= LV_level_sensors;
				if( prop.getProperty("cb_level_indicators") != null )
					flags |= LV_level_indicators;
				flags |= FLAG_READ_ONLY;
				
				DatabaseMgr.getInstance().executeStatement("insert into sb_leak_verify values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,current_timestamp)",
					new Object[] { idsite, idrecord,
						prop.getProperty("instrument_type"),
						prop.getProperty("instrument_sensitivity"),
						prop.getProperty("date1_year") + "/" + prop.getProperty("date1_month") + "/" + prop.getProperty("date1_day"), 
						prop.getProperty("leakage_description"),
						prop.getProperty("corrective_action"),
						prop.getProperty("ref_to_restore_quantity"),
						prop.getProperty("brand_model"),
						prop.getProperty("min_value"),
						prop.getProperty("max_value"),
						prop.getProperty("level_estimation"),
						prop.getProperty("level_reference"),
						prop.getProperty("operator_stamp"),
						prop.getProperty("date_year") + "/" + prop.getProperty("date_month") + "/" + prop.getProperty("date_day"),
						flags,
						prop.getProperty("user_name")
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("del") ) {
			Integer idrecord = Integer.parseInt(prop.getProperty("idrecord"));
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_leak_verify where idsite=? and idrecord=?",
					new Object[] { idsite, idrecord }
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("mod") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
			ut.setProperty("idrecord", prop.getProperty("idrecord"));
		}
		else if( cmd.equals("add") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
		}
	}
	
	
	public boolean isLeakageVerificationReadOnly(int idrecord)
	{
		return isRecordReadOnly(aLeakageVerification, idrecord);
	}
	
	
	// Notices
	public Properties[] aNotices = null;
	
	
	public void loadNotices()
	{
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from sb_notices where idsite=? order by date, idrecord",
				new Object[] { idsite }
			);
			if( rs.size() >= 1 ) {
				aNotices = new Properties[rs.size()];
				for(int i = 0; i < rs.size(); i++) {
					aNotices[i] = new Properties();
					Record r = rs.get(i);
					aNotices[i].setProperty("idrecord", r.get("idrecord").toString());
					aNotices[i].setProperty("tech_company", (String)r.get("tech_company"));
					aNotices[i].setProperty("procedure_description", (String)r.get("procedure_description"));
					aNotices[i].setProperty("date", (String)r.get("date"));
					aNotices[i].setProperty("flags", r.get("flags").toString());
					aNotices[i].setProperty("user_name", r.get("username").toString());					
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveNotices(UserSession us, Properties prop)
	{
		String cmd = prop.getProperty("cmd");
		if( cmd.equals("save") ) {
			try {
				String strIdRecord = prop.getProperty("idrecord");
				Integer idrecord = 0;
				if( !strIdRecord.isEmpty() ) {
					idrecord = Integer.parseInt(strIdRecord); 
					DatabaseMgr.getInstance().executeStatement("delete from sb_notices where idsite=? and idrecord=?",
						new Object[] { idsite, idrecord }
					);
				}
				else {
					 idrecord = SeqMgr.getInstance().next(null, "sb_notices", "idrecord");
				}
				DatabaseMgr.getInstance().executeStatement("insert into sb_notices values (?,?,?,?,?,?,?,current_timestamp)",
					new Object[] { idsite, idrecord,
						prop.getProperty("tech_company"),
						prop.getProperty("procedure_description"),
						prop.getProperty("date_year") + "/" + prop.getProperty("date_month") + "/" + prop.getProperty("date_day"),
						FLAG_READ_ONLY,
						prop.getProperty("user_name")
					}
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("del") ) {
			Integer idrecord = Integer.parseInt(prop.getProperty("idrecord"));
			try {
				DatabaseMgr.getInstance().executeStatement("delete from sb_notices where idsite=? and idrecord=?",
					new Object[] { idsite, idrecord }
				);
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		else if( cmd.equals("mod") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
			ut.setProperty("idrecord", prop.getProperty("idrecord"));
		}
		else if( cmd.equals("add") ) {
			UserTransaction ut = us.getCurrentUserTransaction();
			ut.setProperty("cmd", cmd);
		}
	}
	

	public boolean isNoticesReadOnly(int idrecord)
	{
		return isRecordReadOnly(aNotices, idrecord);
	}
}
