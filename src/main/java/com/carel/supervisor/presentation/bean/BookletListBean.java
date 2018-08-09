package com.carel.supervisor.presentation.bean;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import supervisor.ServUpload;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;

public class BookletListBean {
	// private static final int column_number = 8;
	// private static final String IDDEVICE = "iddevice";
	private JRBeanCollectionDataSource devparamList;
	private JRBeanCollectionDataSource siteinfoList;
	private BookletConfBean bcf;
	private int idSite = -1;
	private String language = "EN_en";
	private String beanName = "";
	private String field1 = "";
	private String field2 = "";
	private String field3 = "";
	private String field4 = "";
	private String field5 = "";

	public BookletListBean(int site, String username) throws DataBaseException {
		this.idSite = site;
		// String sql="select iduser from cfusers where username=?";
		// RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,new
		// Object[]{username} );
		// Record record=rs.get(0);
		// if(rs.size()>0){
		// this.iduser= (Integer)record.get("iduser");
		// }
	}

	public void searchBookletDevVarBySiteId() throws Exception {
		String sql = "select "+ //--b.idbooklet, 
			" b.iddevice, b.idvariable, "+
			" d.description as device, "+ 
			" v.description as variable, "+ 
			" v.shortdescr as varcode "+ 
			" from "+ 
			" bookletdevvar as b, cftableext as d, cftableext as v "+
			" where "+
			" d.idsite=1 and d.tablename='cfdevice' and d.tableid=b.iddevice and d.languagecode=? and "+
			" v.idsite=1 and v.tablename='cfvariable' and v.tableid=b.idvariable and v.languagecode=? "+
			" order by device,variable ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { language, language });
		load(1, language, rs);
	}
	
	public String[][] searchFiles() throws Exception
	{
		String sql = "select distinct cabinet, file_name from booklet_cabinet "+
					"inner join booklet_cabinet_dev on booklet_cabinet_dev.idcabinet=booklet_cabinet.id "+
					"inner join bookletdevvar on bookletdevvar.iddevice=booklet_cabinet_dev.iddevice";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		String[][] result = new String[rs.size()][2];
		for(int i=0;i<rs.size();i++)
		{
			result[i][0] = BaseConfig.getCarelPath()+ServUpload.BOOKLET_CABINET_PATH+ File.separator +rs.get(i).get(1).toString();
			result[i][1] = rs.get(i).get(0).toString();
		}
		return result;
	}

	public void searchBookletDevVarOfAllSiteId() throws Exception {
//		String sql = "select 0 as idbooklet, cfd.idsite, cfd.iddevice, cfv.idvariable, cted.description as device, ctev.languagecode as languagecode, ctev.description as variable, ctev.shortdescr as varcode "
//				+ " from cfdevice as cfd inner join cftableext as cted on cted.tableid = cfd.iddevice and cted.tablename='cfdevice' and cted.idsite = cfd.idsite and cted.languagecode = '"
//				+ language
//				+ "' "
//				+ " inner join cfvariable as cfv on cfv.iddevice=cfd.iddevice and cfv.iscancelled='FALSE' and cfd.iscancelled='FALSE' and cfv.readwrite>1 and cfv.idhsvariable is not null "
//				+ " inner join cftableext as ctev on ctev.tableid = cfv.idvariable and ctev.tablename='cfvariable' and ctev.idsite = cfv.idsite and ctev.languagecode = '"
//				+ language
//				+ "' "
//				+ " and cfv.idsite="
//				+ this.idSite
//				+ " and cfd.idsite="
//				+ this.idSite
//				+ "	order by device,variable";
		String sql = " select cfv.iddevice, cfv.idvariable, cted.description as device, " +
				" ctev.description as variable, ctev.shortdescr as varcode "+
				" from  "+
				" cfvariable as cfv, cftableext as cted, cftableext as ctev "+
				" where "+
				" cfv.iscancelled='FALSE' and "+
				" cfv.readwrite!='1 ' and "+
				" cfv.type!=4 and "+
				" cfv.idhsvariable is not null and "+
				" cted.idsite = 1 and "+ 
				" cted.tablename='cfdevice'  and "+ 
				" cted.tableid = cfv.iddevice and "+ 
				" cted.languagecode = '"+language+"'  and "+ 
				" ctev.idsite = 1 and "+ 
				" ctev.tablename='cfvariable' and "+ 
				" ctev.tableid = cfv.idvariable and "+ 
				" ctev.languagecode = '"+language+"' "+
				" order by device, variable ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
		load(this.idSite, language, rs);
	}

	public String[][] searchFilesOfAll() throws Exception
	{
		String sql = "select distinct cabinet, file_name from booklet_cabinet "+
					"inner join booklet_cabinet_dev on booklet_cabinet_dev.idcabinet=booklet_cabinet.id ";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		String[][] result = new String[rs.size()][2];
		for(int i=0;i<rs.size();i++)
		{
			result[i][0] = BaseConfig.getCarelPath()+ServUpload.BOOKLET_CABINET_PATH+ File.separator +rs.get(i).get(1).toString();
			result[i][1] = rs.get(i).get(0).toString();
		}
		return result;
	}
	public void searchBookletConfBySiteId() throws Exception {
		String sql = "select bc.idsite,bc.idbooklet," +
				"dtl.idbookletconf,dtl.option,dtl.selected " +
				"from bookletconf as bc, bookletconfdtl dtl " +
				"where idsite=? and bc.idbooklet=dtl.idbooklet order by dtl.option";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(this.idSite) });
		if (rs.size() > 0) {
			// Record record=rs.get(0);
			this.bcf = new BookletConfBean(rs);
		} else {
			this.bcf = new BookletConfBean();
		}
	}

	public void searchBookletSiteInfo(String siteName,String user, String language) throws Exception {
		LangService lang = LangMgr.getInstance().getLangService(language);
		List<BookletSiteInfoBean> lst = new ArrayList<BookletSiteInfoBean>();
		lst.add(new BookletSiteInfoBean(lang.getString("booklet", "name"), siteName));
		//by Kevin, only show fieldName and user
		lst.add(new BookletSiteInfoBean(lang.getString("alrview", "user"), user));
//		lst.add(new BookletSiteInfoBean(lang.getString("booklet", "identity"), site.getCode()));
//		lst.add(new BookletSiteInfoBean(lang.getString("booklet", "phone"), site.getPhone()));
//		lst.add(new BookletSiteInfoBean(lang.getString("booklet", "password"), site.getPassword()));
		siteinfoList = new JRBeanCollectionDataSource(lst);
	}

	public int addDevVar(String repdev, String repvar) throws Exception {
		int idbook = -1;
		String sql = "select * from bookletconf where idsite=?";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(this.idSite) });
		if (rs.size() == 0) {
			Object[] values = new Object[3];
			SeqMgr o = SeqMgr.getInstance();
			idbook = o.next(null, "bookletconf", "idbooklet");
			values[0] = idbook;
			values[1] = new Integer(this.idSite);
			values[2] = new Timestamp(System.currentTimeMillis());
			String exeSql = "insert into bookletconf(idbooklet,idsite,lastupdate) values(?,?,?) ";
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);

			sql = "select * from bookletconf where idsite=?";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(this.idSite) });
		}
		idbook = (Integer) rs.get(0).get("idbooklet");

		String exeSql = "delete from bookletdevvar where idsite=?";
		DatabaseMgr.getInstance().executeStatement(null, exeSql, new Object[] { new Integer(this.idSite) });
		String insert = "insert into bookletdevvar(idbooklet,idsite,iddevice,idvariable,lastupdate) values (?,?,?,?,?)";
		if (!"".equals(repdev) && !"".equals(repvar)) {
			Object[] values = new Object[5];
			String[] dev = repdev.split(",");
			String[] var = repvar.split(",");
			for (int i = 0; i < dev.length; i++) {
				values[0] = idbook;
				values[1] = new Integer(this.idSite);
				values[2] = new Integer(dev[i]);
				values[3] = new Integer(var[i]);
				values[4] = new Timestamp(System.currentTimeMillis());
				DatabaseMgr.getInstance().executeStatement(null, insert, values);
			}
		}
		return idbook;
	}

	public int loadBookletDefault() throws Exception {
		int idbook = -1;

		//master record check
		String sql = "select * from bookletconf where idsite=?";
		RecordSet recset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(this.idSite) });
		if (recset.size() > 0) {
			idbook = (Integer) recset.get(0).get("idbooklet");
		} else {
			Object[] values = new Object[3];
			SeqMgr o = SeqMgr.getInstance();
			idbook = o.next(null, "bookletconf", "idbooklet");
			values[0] = idbook;
			values[1] = new Integer(this.idSite);
			values[2] = new Timestamp(System.currentTimeMillis());
			String exeSql = "insert into bookletconf(idbooklet,idsite,lastupdate) values(?,?,?) ";
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
		}

		//main config check
		sql = "select * from bookletconfdtl where idbooklet=?";
		recset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idbook) });
		if (recset.size()==0) {
			Object[] values = new Object[4];
			SeqMgr o = SeqMgr.getInstance();
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[1] = idbook;
			String exeSql = "insert into bookletconfdtl(idbookletconf,idbooklet,option,selected) values(?,?,?,?)";
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "devparam";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "siteinfo";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "siteconf";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "schedash";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "userconf";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "activealarm";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "plugmod";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "notes";
			values[3] = new Boolean(true);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
			try {
			values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
			values[2] = "ispdf";
			values[3] = new Boolean(false);
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			} catch (Exception e) {}
		}
		
		//default variables config
		sql = " select v.iddevice, v.idvariable "+
				" from cfvariable as v, cfdevice as d, cfdevmdl as dm, bookletdevmdl as b "+
				" where " +
				" b.devcode=dm.code and "+
				" d.iddevmdl= dm.iddevmdl and "+
				" d.iscancelled='FALSE' and v.code=b.varcode and "+
				" v.iddevice=d.iddevice and v.iscancelled='FALSE' " +
				" order by v.iddevice, v.priority limit "+BookletConfBean.MAXENTRIES;
		Connection conn = null;
		Object[] values = new Object[5];
		try {
			DatabaseMgr.getInstance().executeStatement("delete from bookletdevvar", null);
			idbook = (Integer) DatabaseMgr.getInstance().executeQuery(null, "select * from bookletconf").get(0).get("idbooklet");
			conn = DatabaseMgr.getInstance().getConnection(null);
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				int dev = rs.getInt("iddevice");
				int var = rs.getInt("idvariable");
				values[0] = idbook;
				values[1] = new Integer(1);
				values[2] = new Integer(dev);
				values[3] = new Integer(var);
				values[4] = new Timestamp(System.currentTimeMillis());
				String insert = "insert into bookletdevvar(idbooklet,idsite,iddevice,idvariable,lastupdate) values (?,?,?,?,?)";
				DatabaseMgr.getInstance().executeStatement(null, insert, values);
			}
			conn.close();
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				LoggerMgr.getLogger(this.getClass()).error(e2);
			}
		}
		return idbook;
	}

	public int deleteDevVar(int idSite, int idDev, int idVar) throws Exception {
		String delSql = "delete from bookletdevvar where idsite=? and iddevice=? and idvariable=?";
		DatabaseMgr.getInstance().executeStatement( null, delSql,
				new Object[] { new Integer(idSite), new Integer(idDev), new Integer(idVar) });
		return 1;
	}

	public int updateBookConf(String confStr) throws Exception {
		boolean isExist = false;
		int idbook = -1;
		String sql = "select * from bookletconf where idsite=?";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(this.idSite) });
		if (rs.size() > 0) {
			idbook = (Integer) rs.get(0).get("idbooklet");
		} else {
			Object[] values = new Object[3];
			SeqMgr o = SeqMgr.getInstance();
			idbook = o.next(null, "bookletconf", "idbooklet");
			values[0] = idbook;
			values[1] = new Integer(this.idSite);
			values[2] = new Timestamp(System.currentTimeMillis());
			String exeSql = "insert into bookletconf(idbooklet,idsite,lastupdate) values(?,?,?) ";
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
		}

		String[] fiel = new String[] { "devparam", "siteinfo", "siteconf", "schedash", 
				"userconf", "activealarm", "plugmod", "notes", "ispdf" };
		sql = "select * from bookletconfdtl where idbooklet=? and option=?";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idbook), fiel[0] });
		if (rs.size() > 0) {
			isExist = true;
		}
		if (isExist) {
			String[] s = confStr.split(",");
			Object[] values = new Object[3];
			for (int i = 0; i < s.length; i++) {
				if (s[i].toLowerCase().equals("true")) {
					values[0] = new Boolean(true);
				} else {
					values[0] = new Boolean(false);
				}
				values[1] = idbook;
				values[2] = fiel[i];
				String exeSql = "update bookletconfdtl set selected=? where idbooklet=? and option=?";
				DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			}
		} else {
			String[] s = confStr.split(",");
			Object[] values = new Object[4];
			for (int i = 0; i < s.length; i++) {
				SeqMgr o = SeqMgr.getInstance();
				values[0] = o.next(null, "bookletconfdtl", "idbookletconf");
				values[1] = idbook;
				values[2] = fiel[i];
				if (s[i].toLowerCase().equals("true")) {
					values[3] = new Boolean(true);
				} else {
					values[3] = new Boolean(false);
				}
				String exeSql = "insert into bookletconfdtl(idbookletconf,idbooklet,option,selected) values(?,?,?,?)";
				DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
			}
		}
		return 1;
	}

	private void load(int site, String language, RecordSet rs) throws Exception {
		if (rs.size() > 0) {
			BookletDevVarBean bdvBean = null;
			Record record = null;
			List<Object> lst = new ArrayList<Object>();
			BookletDevVarBean temp = null;
			for (int i = 0; i < rs.size(); i++) {
				record = rs.get(i);
				bdvBean = new BookletDevVarBean(record, site, language);
				lst.add(bdvBean);
				String[] cabinet = BookletCabinetList.getCabinetByDeviceId(bdvBean.getIdDev());
				for(int j=0;j<cabinet.length;j++)
				{
					String str = cabinet[j];
					if(j == 0)
						bdvBean.setCabinet(str);
					else
						bdvBean.setCabinet(bdvBean.getCabinet()+","+str);
				}
				temp = bdvBean;
			}
			devparamList = new JRBeanCollectionDataSource(lst);
		}
	}

	public BookletConfBean getBcf() {
		return bcf;
	}

	public void setBcf(BookletConfBean bcf) {
		this.bcf = bcf;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getField1() {
		return field1;
	}

	public String getField2() {
		return field2;
	}

	public String getField3() {
		return field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}
	
	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	public JRBeanCollectionDataSource getDevparamList() {
		return devparamList;
	}

	public void setDevparamList(JRBeanCollectionDataSource devparamList) {
		this.devparamList = devparamList;
	}

	public JRBeanCollectionDataSource getSiteinfoList() {
		return siteinfoList;
	}

	public void setSiteinfoList(JRBeanCollectionDataSource siteinfoList) {
		this.siteinfoList = siteinfoList;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
