package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;

public class DebugBean 
{
	private static final String DEVMDL_CODE = "devmdlcode";
	private static final String ST_CODE = "stcode";
	private static final String TREG_CODE = "tregcode";
	private static final String TOFF_CODE = "toffcode";
	private static final String TON_CODE = "toncode";
	private static final String TDEF_CODE = "tdefcode";
	private static final String TSAT_CODE = "tsatcode";
	private static final String TASP_CODE = "taspcode";
	private static final String SH_CODE = "shcode";
	private static final String SHSET_CODE = "shsetcode";
	private static final String VALV_CODE = "valvcode";
	private static final String REQ_CODE = "reqcode";
	private static final String DEFR_CODE = "defrcode";
	private static final String FA_CODE = "facode";
	private static final String FB_CODE = "fbcode";
	private static final String FC_CODE = "fccode";
	private static final String P1_CODE = "p1code";
	private static final String COP_CODE = "copcode";
	private static final String COOLING_CODE = "coolingcode";
	private static final String CONSUMPTION_CODE = "consumptioncode";
	private static final String TH2O_IN_CODE = "th2o_incode";
	private static final String TH2O_OUT_CODE = "th2o_outcode";
	private static final String H2O_DIFF_CODE = "h2o_diffcode";
	private static final String COMP_SPPED_CODE = "comp_speedcode";
	private static final String LIQ_INJ_CODE = "liq_injcode";
	private static final String ENVELOP_CODE = "envelopecode";
	
	
	private String devmdlcode = null;
	private String stcode = null;
	private String tregcode = null;
	private String toffcode = null;
	private String toncode = null;
	private String tdefcode = null;
	private String tsatcode = null;
	private String taspcode = null;
	private String shcode = null;
	private String shsetcode = null;
	private String valvcode = null;
	private String reqcode = null;
	private String defrcode = null;
	private String facode = null;
	private String fbcode = null;
	private String fccode = null;
	private String p1code = null;
	private String copcode = null;
	private String coolingcode = null;
	private String consumptioncode = null;
	private String th2o_incode = null;
	private String th2o_outcode = null;
	private String h2o_diffcode = null;
	private String comp_speedcode = null;
	private String liq_injcode = null;
	private String envelopecode = null;
	

	public DebugBean(Record record)
	 	throws DataBaseException
	{
		this.devmdlcode = (String) record.get(DEVMDL_CODE);
		this.stcode = (String)record.get(ST_CODE);
		this.tregcode = (String)record.get(TREG_CODE);
		this.toffcode = (String)record.get(TOFF_CODE);
		this.toncode = (String)record.get(TON_CODE);
		this.tdefcode = (String)record.get(TDEF_CODE);
		this.tsatcode = (String)record.get(TSAT_CODE);
		this.taspcode = (String)record.get(TASP_CODE);
		this.shcode = (String)record.get(SH_CODE);
		this.shsetcode = (String)record.get(SHSET_CODE);
		this.valvcode = (String)record.get(VALV_CODE);
		this.reqcode = (String)record.get(REQ_CODE);
		this.defrcode = (String)record.get(DEFR_CODE);
		this.facode = (String)record.get(FA_CODE);
		this.fbcode = (String)record.get(FB_CODE);
		this.fccode = (String)record.get(FC_CODE);
		this.p1code = (String)record.get(P1_CODE);
		this.copcode = (String)record.get(COP_CODE);
		this.coolingcode = (String)record.get(COOLING_CODE);
		this.consumptioncode = (String)record.get(CONSUMPTION_CODE);
		this.th2o_incode = (String)record.get(TH2O_IN_CODE);
		this.th2o_outcode = (String)record.get(TH2O_OUT_CODE);
		this.h2o_diffcode = (String)record.get(H2O_DIFF_CODE);
		this.comp_speedcode = (String)record.get(COMP_SPPED_CODE);
		this.liq_injcode = (String)record.get(LIQ_INJ_CODE);
		this.envelopecode = (String)record.get(ENVELOP_CODE);
	}
	 
	public String getDevmdlcode() 
	{
		return devmdlcode;
	}
	
	public void setDevmdlcode(String devmdlcode) 
	{
		this.devmdlcode = devmdlcode;
	}

	public String getStcode() {
		return stcode;
	}

	public void setStcode(String stcode) {
		this.stcode = stcode;
	}

	public String getTregcode() {
		return tregcode;
	}

	public void setTregcode(String tregcode) {
		this.tregcode = tregcode;
	}

	public String getToffcode() {
		return toffcode;
	}

	public void setToffcode(String toffcode) {
		this.toffcode = toffcode;
	}

	public String getToncode() {
		return toncode;
	}

	public void setToncode(String toncode) {
		this.toncode = toncode;
	}

	public String getTdefcode() {
		return tdefcode;
	}

	public void setTdefcode(String tdefcode) {
		this.tdefcode = tdefcode;
	}

	public String getTsatcode() {
		return tsatcode;
	}

	public void setTsatcode(String tsatcode) {
		this.tsatcode = tsatcode;
	}

	public String getTaspcode() {
		return taspcode;
	}

	public void setTaspcode(String taspcode) {
		this.taspcode = taspcode;
	}

	public String getValvcode() {
		return valvcode;
	}

	public void setValvcode(String valvcode) {
		this.valvcode = valvcode;
	}

	public String getReqcode() {
		return reqcode;
	}

	public void setReqcode(String reqcode) {
		this.reqcode = reqcode;
	}

	public String getDefrcode() {
		return defrcode;
	}

	public void setDefrcode(String defrcode) {
		this.defrcode = defrcode;
	}

	public String getShcode() {
		return shcode;
	}

	public void setShcode(String shcode) {
		this.shcode = shcode;
	}

	public String getShsetcode() {
		return shsetcode;
	}

	public void setShsetcode(String shsetcode) {
		this.shsetcode = shsetcode;
	}

	public String getFacode() {
		return facode;
	}

	public void setFacode(String facode) {
		this.facode = facode;
	}

	public String getFbcode() {
		return fbcode;
	}

	public void setFbcode(String fbcode) {
		this.fbcode = fbcode;
	}

	public String getFccode() {
		return fccode;
	}

	public void setFccode(String fccode) {
		this.fccode = fccode;
	}

	public String getP1code() {
		return p1code;
	}

	public void setP1code(String p1code) {
		this.p1code = p1code;
	}

	public String getCopcode() {
		return copcode;
	}

	public void setCopcode(String copcode) {
		this.copcode = copcode;
	}

	public String getCoolingcode() {
		return coolingcode;
	}

	public void setCoolingcode(String coolingcode) {
		this.coolingcode = coolingcode;
	}

	public String getConsumptioncode() {
		return consumptioncode;
	}

	public void setConsumptioncode(String consumptioncode) {
		this.consumptioncode = consumptioncode;
	}

	public String getTh2o_incode() {
		return th2o_incode;
	}

	public void setTh2o_incode(String th2o_incode) {
		this.th2o_incode = th2o_incode;
	}

	public String getTh2o_outcode() {
		return th2o_outcode;
	}

	public void setTh2o_outcode(String th2o_outcode) {
		this.th2o_outcode = th2o_outcode;
	}

	public String getH2o_diffcode() {
		return h2o_diffcode;
	}

	public void setH2o_diffcode(String h2o_diffcode) {
		this.h2o_diffcode = h2o_diffcode;
	}

	public String getComp_speedcode() {
		return comp_speedcode;
	}

	public void setComp_speedcode(String comp_speedcode) {
		this.comp_speedcode = comp_speedcode;
	}

	public String getLiq_injcode() {
		return liq_injcode;
	}

	public void setLiq_injcode(String liq_injcode) {
		this.liq_injcode = liq_injcode;
	}

	public String getEnvelopecode() {
		return envelopecode;
	}

	public void setEnvelopecode(String envelopecode) {
		this.envelopecode = envelopecode;
	}
}
