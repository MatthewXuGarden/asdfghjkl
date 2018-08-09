package com.carel.supervisor.report;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class Report {

	public static final String SITENAME = "sitename";
	public static final String LOGO = "logo";
	public static final String I18NDESC = "i18ndesc";
	public static final String I18NDATE = "i18ndate";
	public static final String I18NTIME = "i18ntime";
	public static final String I18NSTAMP = "i18nstamp";
	public static final String I18NSIGN1 = "i18nsign1";
	public static final String I18NSIGN2 = "i18nsign2";
	public static final String I18NDEVICE = "i18ndevice";
	public static final String I18NVAR = "i18nvar";
	public static final String I18NVALUE = "i18nvalue";
	public static final String IMAGE = "image";
	public static final String LANGUAGE = "language";
	public static final String VARIABLES = "vars";
	public static final String VARS_MDL = "varsmdl";
	public static final String TIMEVALUES = "timeval";
	public static final String START = "start";
	public static final String END = "end";
	public static final String DESCRIPTIONS = "descriptions";
	public static final String HACCP = "haccp";
	public static final String USER = "user";
	public static final String I18NFROM = "i18nfrom";
	public static final String I18NTO = "i18nto";
	public static final String OFFSET = "offset";
	public static final String SUB_REP = "sub_rep";
	public static final String ALR_START = "alr_tstart";
	public static final String ALR_END = "alr_tend";
	public static final String ExportFileName = "export_filename";
	public static final String ExportPath = "export_path";
	public static final String ISLOCAL = "islocal";

	public static final String TITLE = "title";

	public static final String IDSITE = "idsite";

	public static final String I18NPAGE = "i18npage";
	public static final String I18NOF = "i18nof";
	public static final String REPORTDETAIL = "reportdetail";
	
	//code for report name(the column code in table cfreportkernel)
	public static final String CODE = "code";
	
	public static final int MAX_SAMPLES = 40000;

	protected Map<String, Object> parameters;

	private File file;
	
	//add by Kevin, an identifier for where to save export
	private boolean toTempFolder = false;
	

	public Report() {
		this.parameters = new HashMap<String, Object>();
	}

	public abstract File generate();

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public void setFile(File f){
		this.file = f;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public boolean isToTempFolder() {
		return toTempFolder;
	}

	public void setToTempFolder(boolean toTempFolder) {
		this.toTempFolder = toTempFolder;
	}
}
