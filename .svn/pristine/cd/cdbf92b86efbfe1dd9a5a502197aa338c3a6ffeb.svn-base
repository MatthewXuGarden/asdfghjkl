package com.carel.supervisor.presentation.bo;

import java.io.File;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import supervisor.ServUpload;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.presentation.bean.BookletCabinetBean;
import com.carel.supervisor.presentation.bean.BookletCabinetDeviceList;
import com.carel.supervisor.presentation.bean.BookletCabinetList;
import com.carel.supervisor.presentation.bean.BookletConfBean;
import com.carel.supervisor.presentation.bean.BookletDevVarBean;
import com.carel.supervisor.presentation.bean.BookletListBean;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.report.ReportBeanHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class BBooklet extends BoMaster {
	private static final long serialVersionUID = 8848862850779604793L;
	private static final int REFRESH_TIME = -1;

	public BBooklet(String lang) {
		super(lang, REFRESH_TIME);
	}

	protected Properties initializeRefreshTime() {
		Properties p = new Properties();
		return p;
	}

	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.put("tab1name","initialize();");
		p.put("tab2name","subtab2_initialize();");
		return p;
	}

	protected Properties initializeJsOnLoad() {
		String virtkey = VirtualKeyboard.getInstance().isOnScreenKey() ? ";keyboard.js;" : "";
		
		Properties p = new Properties();
		p.put("tab1name", "booklet.js;dbllistbox.js;dataselect.js;../arch/FileDialog.js" + virtkey);
		p.put("tab2name", "booklet.js;dbllistbox.js;dataselect.js;../arch/FileDialog.js" + virtkey);
		return p;
	}

	//remove by Kevin
//	public String createHistoryTable(int idsite, String language, String title, int width, int height, int screenh, int screenw) {
//		ReportBeanListPres.setScreenH(screenh);
//		ReportBeanListPres.setScreenW(screenw);
//		return ReportBeanListPres.getHTMLHSReportTable(idsite, language, title, width, height);
//	}

	public String getRepositoryPath() {
		return DispatcherMgr.getInstance().getRepositoryPath();
	}
	
	// DOCTYPE STRICT is necessary to have FileDialog correctly functioning
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab1name", DOCTYPE_STRICT);
		p.put("tab2name", DOCTYPE_STRICT);
		return p;
    }

	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception {
		String toPrint = "";
		String command = prop.getProperty("cmd");
		if("RELOAD".equalsIgnoreCase(command)){
			return;
		}
		if (tabName != null && tabName.equalsIgnoreCase("tab1name")) {
			String prompt = prop.getProperty("prompt");
			String isopen = prop.getProperty("isopen");
			String reptype = prop.getProperty("reptype");

			if (prompt.equalsIgnoreCase("csv") || prompt.equalsIgnoreCase("pdf")) {
				toPrint = ReportBeanHelper.buildBookletReport(us.getIdSite(), us.getLanguage(), us.getUserName(), prompt, isopen, reptype, "");
				toPrint = DispatcherMgr.getInstance().getRepositoryPath() + toPrint;
				us.setProperty("promptDocument", toPrint);
			}
		}
		else if("tab2name".equals(tabName))
		{
			if("save".equals(command))
			{
				String idStr = prop.getProperty("id");
				String cabinet = prop.getProperty("cabinet");
				String filename = prop.getProperty("filename");
				String iddevicesStr = prop.getProperty("iddevices");
				String[] temps = iddevicesStr.split(";");
				int[] iddevices = new int[temps.length];
				for(int i=0;i<iddevices.length;i++)
					iddevices[i] = Integer.valueOf(temps[i]);
				Timestamp now = new Timestamp(System.currentTimeMillis()); 
				BookletCabinetBean bean = null;
				if(!"".equals(idStr))
				{
					int id = Integer.valueOf(idStr);
					bean = BookletCabinetList.retrieveById(id);
				}
				else
				{
					bean = new BookletCabinetBean();
				}
				bean.setCabinet(cabinet);
				//when edit, filename is ""
				if(filename != null && filename.length()>0)
					bean.setFileName(filename);
				bean.setLastUpdate(now);
				BookletCabinetList.save(bean);
				BookletCabinetDeviceList.save(bean, iddevices);
			}
			else if("delete".equals(command))
			{
				int id = -1;
				try{ id = Integer.valueOf(prop.getProperty("id"));}
				catch(Exception ex){}
				if(id != -1)
				{
					BookletCabinetBean bean = BookletCabinetList.retrieveById(id);
					String fileName = bean.getFileName();
					BookletCabinetDeviceList.deleteByCabinetId(id);
					BookletCabinetList.delete(id);
					if(!BookletCabinetList.isFileInUse(fileName))
					{
						File file = new File(BaseConfig.getCarelPath()+ServUpload.BOOKLET_CABINET_PATH+ File.separator + fileName);
						if(file.exists())
						{
							file.delete();
						}
					}
				}
			}
		}
	}

	@Override
	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception {
		
		String language = us.getLanguage();
		String temp = prop.getProperty("model");
		int model = -1;
		if (temp != null && Integer.parseInt(temp) > 0) {
			model = Integer.parseInt(temp);
		}
		int dev = -1;
		temp = prop.getProperty("dev");
		if (temp != null && Integer.parseInt(temp) > 0) {
			dev = Integer.parseInt(temp);
		}
		int idsite = us.getIdSite();
		StringBuffer ris = new StringBuffer();
		
		try {
			
			String section = prop.getProperty("section");
			String method = prop.getProperty("method");
			
			// get fullpath export filename
			String fullpath = (prop.getProperty("path"));
			if(fullpath!= null && (fullpath.equals("") || fullpath.equalsIgnoreCase("undefined")))
				fullpath = "";
			
			ris.append("<response>");
			ris.append("<method>" + method + "</method>");
			ris.append("<booklet>");
			BookletListBean bdvBean = new BookletListBean(idsite, us.getUserName());
			bdvBean.setLanguage(language);
			// conf
			if ("conf".equals(section)) {
				ris.append(getBookConf(bdvBean, us));
			}
			// devicemodel
			if ("devMdl".equals(section)) {
				ris.append(getDevMdl(bdvBean, us, idsite, language, model));
			}
			// devices list
			if ("deviceslist".equals(section)) {
				ris.append(getDevList(bdvBean, us, dev, model, idsite, language, tabName.equals("tab1name")));
			}
			// parameters list
			if ("parameterslist".equals(section)) {
				ris.append(getParams(bdvBean, us, dev, model, idsite, language));
			}
			// devvar list
			if ("devvar".equals(section)) {
				ris.append(getDevVar(bdvBean, us));
			}
			if ("adddevvar".equalsIgnoreCase(section)) {
				ris.append(getDevVarMdl(us, prop));
			}
			if ("report".equals(section)) {
				String prompt = prop.getProperty("prompt");
				String isopen = prop.getProperty("isopen");
				String reptype = prop.getProperty("reptype");
				ris.append(getReport(bdvBean, us, prompt, isopen, reptype, fullpath));
			}
			if ("saveBooklet".equals(section)) {
				try{
					String bookconf = prop.getProperty("bookconf");
					String repdev = prop.getProperty("repdev");
					String repvar = prop.getProperty("repvar");
					updateBookConf(bdvBean, bookconf);
					addDevVar(bdvBean, repdev, repvar);
				}catch (Exception e1) {
					LoggerMgr.getLogger(this.getClass()).error(e1);
					ris.append("<error><![CDATA["+
							LangMgr.getInstance().getLangService(language).getString("booklet", "saveerror")+
							"]]></error>");
				}
			}
			if ("loadDefault".equals(section)) {
				bdvBean.loadBookletDefault();
			}
			ris.append("</booklet>");
			ris.append("</response>");
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		if("tab2name".equals(tabName))
		{
			String cmd = prop.getProperty("cmd");
			if("edit".equals(cmd))
			{
				StringBuilder response= new StringBuilder();
				response.append("<response>");
				int id = Integer.valueOf(prop.getProperty("id"));
				BookletCabinetBean bean = BookletCabinetList.retrieveById(id);
				if(bean != null)
				{
					response.append("<id>");
					response.append("<![CDATA[");
					response.append(id);
					response.append("]]>");	
					response.append("</id>");
					
					response.append("<cabinet>");
					response.append("<![CDATA[");
					response.append(bean.getCabinet());
					response.append("]]>");	
					response.append("</cabinet>");
					
					response.append("<filename>");
					response.append("<![CDATA[");
					response.append(bean.getFileName());
					response.append("]]>");	
					response.append("</filename>");
					
					// devices
					response.append("<devices>");
					response.append("<![CDATA[");
					response.append("<select id='paramLst' size='10' multiple onchange='' ondblclick='dblClickParamList(this);'  class='selectB'>");
					String sql= "select cfdevice.iddevice,cftableext.description "+
								" from booklet_cabinet_dev "+
								" inner join cfdevice on cfdevice.iddevice = booklet_cabinet_dev.iddevice "+
								" inner join cftableext on cftableext.idsite="+idsite+" and cftableext.tablename='cfdevice' and cftableext.tableid=cfdevice.iddevice and cftableext.languagecode='"+language+"' "+
								" where booklet_cabinet_dev.idcabinet=?";
					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{id});;
					for(int i=0;i<rs.size();i++)
					{
						int iddevice = Integer.valueOf(rs.get(i).get(0).toString());
						String description = rs.get(i).get(1).toString();
						response.append("<option value='"+iddevice+"' class='"+(i%2==0?"Row1":"Row2")+"'>"+description+"</option>");
					}
					response.append("</select>");
					response.append("]]>");	
					response.append("</devices>");
				}
				response.append("</response>");
				return  response.toString();
			}
		}
		return ris.toString();
	}

	private StringBuffer getBookConf(BookletListBean bdvBean, UserSession us){
		StringBuffer ris = new StringBuffer();
		try {
			bdvBean.searchBookletConfBySiteId();
			String dv = bdvBean.getBcf().isDevparam() ? "1" : "0";
			String si = bdvBean.getBcf().isSiteinfo() ? "1" : "0";
			String sc = bdvBean.getBcf().isSiteconf() ? "1" : "0";
			String sd = bdvBean.getBcf().isSchedash() ? "1" : "0";
			String uc = bdvBean.getBcf().isUserconf() ? "1" : "0";
			String aa = bdvBean.getBcf().isActivealarm() ? "1" : "0";
			String pm = bdvBean.getBcf().isPlugmodule() ? "1" : "0";
			String ns = bdvBean.getBcf().isNotes() ? "1" : "0";
			ris.append("<conf>\n");
			ris.append("<dv><![CDATA[" + dv + "]]></dv>\n");
			ris.append("<si><![CDATA[" + si + "]]></si>\n");
			ris.append("<sc><![CDATA[" + sc + "]]></sc>\n");
			ris.append("<sd><![CDATA[" + sd + "]]></sd>\n");
			ris.append("<uc><![CDATA[" + uc + "]]></uc>\n");
			ris.append("<aa><![CDATA[" + aa + "]]></aa>\n");
			ris.append("<pm><![CDATA[" + pm + "]]></pm>\n");
			ris.append("<ns><![CDATA[" + ns + "]]></ns>\n");
			ris.append("</conf>\n");
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

	private StringBuffer getDevMdl(BookletListBean bdvBean, UserSession us, int idsite, String language, int model) {
		StringBuffer ris = new StringBuffer();
		String ss="";
		try {
			DevMdlBeanList devmdllist = new DevMdlBeanList();
			DevMdlBean[] devmdl = devmdllist.retrieveOnLine(idsite, language);

			for (int i = 0; i < devmdl.length; i++) {
				DevMdlBean tmp = devmdl[i];
				if (tmp.getIddevmdl() == model)
					ss = "selected";
				else
					ss = "";
				ris.append("<devMdl>\n");
				ris.append("<value><![CDATA[" + tmp.getIddevmdl() + "]]></value>\n");
				ris.append("<text><![CDATA[" + tmp.getDescription() + "]]></text>\n");
				ris.append("<selected><![CDATA[" + ss + "]]></selected>\n");
				ris.append("<idx><![CDATA[" + i + "]]></idx>\n");
				ris.append("</devMdl>\n");
			}
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

	private StringBuffer getDevList(BookletListBean bdvBean, UserSession us, int dev, int model, int idsite,
			String language, boolean bSingleClick) {
		StringBuffer ris = new StringBuffer();
		try {
			ris.append("<![CDATA[");
			
			//new constructor called. 3rd parameter indicates hiding of Internal IO device
			// Nicola Compagno 24032010
			DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage(), true);
			DeviceBean tmp_dev = null;
			int[] ids = devs.getIds();
			ris.append("<select id='devLst' size='10' onchange='' "
				+ (bSingleClick ? "onclick" : "ondblclick")	
				+ "='dblClickDevList(this);' class='selectB' "
				+ (model!=-1?" multiple ":"")+">");
			int device=0;
			for (int i=0;i<devs.size();i++){
				tmp_dev = devs.getDevice(ids[i]);
				if ((model == -1) || (model == tmp_dev.getIddevmdl())) {
					ris.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+
							" value='"+tmp_dev.getIddevice()+
							"' id='dev"+tmp_dev.getIddevice()+"' class='"+(i%2==0?"Row1":"Row2")+"'>"+
							tmp_dev.getDescription()+"</OPTION>\n");
				}
			}
			ris.append("</select>");
			ris.append("]]>");	

		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

	private StringBuffer getParams(BookletListBean bdvBean, UserSession us,
			/*int idx, int count, */int dev, int model, int idsite, String language) {
		StringBuffer ris = new StringBuffer();
		try {
			VarphyBeanList varlist = new VarphyBeanList();
			VarphyBean[] vars = null;
			VarMdlBeanList varMdlBeanList = new VarMdlBeanList();
			VarMdlBean[] varMdlBeans = null;
			String la= us.getLanguage();
			//modifica per tirar su solo i parametri
			if(dev!=-1) {
				vars = varlist.getParameterOfDevice(la,1,dev);
			} else if(model!=-1) {
				varMdlBeans = varMdlBeanList.retrieveOrderedIfDevIsPresent(language,1,model,"FALSE",0, true );
			}
			ris.append("<![CDATA[");
			ris.append("<select id='paramLst' size='10' multiple onchange='' ondblclick='dblClickParamList(this);' class='selectB'>");
			if(vars!=null && vars.length>0){
				for (int i=0;i<vars.length;i++) {
					VarphyBean aux = vars[i];
					ris.append("<option value=\"");
					ris.append(String.valueOf(aux.getId()));
					ris.append("\" id='var");
					ris.append(String.valueOf(aux.getId()));
					ris.append("'");
					ris.append(" class='"+(i%2==0?"Row1":"Row2")+"' >");
					ris.append(aux.getShortDescription());
					ris.append("</option>");
				}
			} else if(varMdlBeans!=null && varMdlBeans.length>0) {
				for (int i=0;i<varMdlBeans.length;i++) {
					VarMdlBean aux = varMdlBeans[i];
					ris.append("<option value=\"");
					ris.append(String.valueOf(aux.getIdvarmdl()));
					ris.append("\" id='var");
					ris.append(String.valueOf(aux.getIdvarmdl()));
					ris.append("'");
					ris.append(" class='"+(i%2==0?"Row1":"Row2")+"' >");
					ris.append(aux.getDescription());
					ris.append("</option>");
				}
			}
			ris.append("</select>");
			ris.append("]]>");	
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

	@SuppressWarnings("unchecked")
	private StringBuffer getDevVar(BookletListBean bdvBean, UserSession us){//, int idx, int count) {
		StringBuffer ris = new StringBuffer();
		boolean profile_booklet = us.isButtonActive("booklet", "tab1name", "Save");
		try {
			bdvBean.searchBookletDevVarBySiteId();
			ris.append("<![CDATA[");
			if (bdvBean.getDevparamList() != null) {
				Collection coll = bdvBean.getDevparamList().getData();
				Iterator itr = coll.iterator();
				for (int i = 0; itr.hasNext() && i<=BookletConfBean.MAXENTRIES; i++) {
					BookletDevVarBean b = (BookletDevVarBean) itr.next();
					ris.append("<tr class='"+(i%2==0?"Row1":"Row2")+"' id='"+b.getIdDev()+"_"+b.getIdVar()+"'>\n");
					ris.append("<td class='standardTxt'>" + b.getDevDesc() + "</td>\n");
					ris.append("<td class='standardTxt'>" + b.getVarDesc() + "</td>\n");
					ris.append("<td class='standardTxt'  style='text-align: center;cursor: pointer;'>");
					ris.append("<IMG " +
							(profile_booklet?"onclick='deleteItem(this);'":"") + 
								" src='images/actions/removesmall_on_black.png'>");
					ris.append("</td>\n");
					ris.append("</tr>\n");
				}
			}
			ris.append("]]>");
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

	private StringBuffer getDevVarMdl(UserSession us, Properties prop){
		StringBuffer ris = new StringBuffer();
		try {
			ris.append("<![CDATA[");
			List<Record> l = VarphyBeanList.getMappingMdlInstances(us.getLanguage(), 
					prop.getProperty("devices"), 
					prop.getProperty("variables"), 
					prop.getProperty("exclude"),
					BookletConfBean.MAXENTRIES);
			for (Iterator<Record> iterator = l.iterator(); iterator.hasNext();) {
				Record record = iterator.next();
    			String idPerRow = (Integer)record.get(0)+"_"+(Integer)record.get(2);
    			ris.append("<tr class='Row1' id ='" );
    			ris.append(idPerRow);
    			ris.append("'>");
    			ris.append("<td class='standardTxt' style='display: none;'>");
    			ris.append(idPerRow);
    			ris.append("</td>");
    			ris.append("<td class='standardTxt'>");
    			ris.append((String)record.get(3));
    			ris.append("</td>");
    			ris.append("<td class='standardTxt'>");
    			ris.append((String)record.get(4));
    			ris.append("</td>");
				ris.append("<td class='standardTxt'  style='text-align: center;cursor: pointer;'>");
				ris.append("<img onclick='deleteItem(this);' src='images/actions/removesmall_on_black.png'/>");
    			ris.append("</td>");
    			ris.append("</tr>");
			}
			ris.append("]]></booklet><booklet><added>"+l.size()+"</added>");
		}catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}
	
	private StringBuffer getReport(BookletListBean bdvBean, UserSession us, String prompt, String isopen, String reportType, String fullpath) {
		String toPrint = "";
		StringBuffer ris = new StringBuffer();
		try {
			if (prompt.equalsIgnoreCase("csv") || prompt.equalsIgnoreCase("pdf")) {
				toPrint = ReportBeanHelper.buildBookletReport(us.getIdSite(),
						us.getLanguage(), us.getUserName(), prompt, isopen, reportType, fullpath);

				ris.append("<report>\n");
				ris.append("<path><![CDATA[" + toPrint + "]]></path>\n");
				ris.append("</report>\n");
			}
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}

	private void updateBookConf(BookletListBean bdvBean, String bookconf) {
		try {
			bdvBean.updateBookConf(bookconf);
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	private void addDevVar(BookletListBean bdvBean, String repdev, String repvar) throws Exception{
		bdvBean.addDevVar(repdev, repvar);
	}
}
