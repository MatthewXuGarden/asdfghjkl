package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.remote.engine.master.ImpMaster;

public class ImpVariable extends ImpMaster 
{
	public static final int NUM_COL = 39;
	
	public ImpVariable(int idsite,String na,String db,String lang)
	{
		super(idsite,na,db,lang);
	}
	
	protected void innerImport(String data)
	{
		String[] arData = null;
		int idx = 0;
		
		if(data != null)
		{
			if(data.endsWith(SEP))
				data = data.substring(0,data.length()-SEP.length());
			
			StringTokenizer st = new StringTokenizer(data,SEP);
			if(st != null)
			{
				arData = new String[st.countTokens()];
				while(st.hasMoreTokens()){
					arData[idx] = decodeValue((String)st.nextToken());
					idx++;
				}
			}
			
			if(arData != null && arData.length == NUM_COL) {
				try {
					insert(arData[0],arData[1],arData[3],arData[4],arData[5],arData[6],arData[7],arData[8],arData[9],
						   arData[10],arData[11],arData[12],arData[13],arData[14],arData[15],arData[16],arData[17],
						   arData[18],arData[19],arData[20],arData[21],arData[22],arData[23],arData[24],arData[25],
						   arData[26],arData[27],arData[28],arData[29],arData[30],arData[31],arData[32],arData[33],
						   arData[34],arData[35],arData[36],arData[37],arData[38]);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected boolean clear() {
		return true;
	}
	
	private void insert(String idVar,String pvCode,String idDevice,String isLogic,String idVarMdl,String funcCode,
						String code,String type,String addIn,String addOut,String vardimension,String varlenght,
						String bitPos,String signed,String decimal,String display,String button,String prioriti,
						String readwrite,String min,String max,String def,String measure,String idvargroup,
						String imgon,String imgoff,String freq,String delta,String delay,String isonchange,
						String ishaccp,String isactive,String iscancelled,String grpcategory,String idhsvariable,
						String insTime,String updTime,String desc)
		throws Exception
	{
		String sql =  
		"insert into cfvariable values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Object[] param = new Object[38];
		int idx = 0;
		
		Integer iVar = null;
		Integer iDevice = null;
		Integer iVarMdl = null;
		Integer iFunction = null;
		Integer iType = null;
		Integer iaddrIn = null;
		Integer iaddrOut = null;
		Integer iDimension = null;
		Integer iLenght = null;
		Integer iPosition = null;
		Integer iDecimal = null;
		Integer iPriority = null;
		Integer iVarGroup = null;
		Integer iFrequency = null;
		Float 	fDelta = null;
		Integer iDelay = null;
		Integer iGrpCategory =null;
		Integer iHsVariable = null;
		Timestamp in = null;
		Timestamp up = null;
		
		try {
			iVar = new Integer(idVar);
			param[idx++] = iVar;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = pvCode;
		param[idx++] = new Integer(this.idSite);
		
		try {
			iDevice = new Integer(idDevice);
			param[idx++] = iDevice;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = isLogic;
		
		try {
			iVarMdl = new Integer(idVarMdl);
			param[idx++] = iVarMdl;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iFunction = new Integer(funcCode);
			param[idx++] = null;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = code;
		
		try {
			iType = new Integer(type);
			param[idx++] = iType;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iaddrIn = new Integer(addIn);
			param[idx++] = iaddrIn;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iaddrOut = new Integer(addOut);
			param[idx++] = iaddrOut;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iDimension = new Integer(vardimension);
			param[idx++] = iDimension;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iLenght = new Integer(varlenght);
			param[idx++] = iLenght;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iPosition = new Integer(bitPos);
			param[idx++] = iPosition;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = signed;
		
		try {
			iDecimal = new Integer(decimal);
			param[idx++] = iDecimal;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = display;
		param[idx++] = button;
		
		try {
			iPriority = new Integer(prioriti);
			param[idx++] = iPriority;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = readwrite;
		param[idx++] = min;
		param[idx++] = max;
		param[idx++] = def;
		param[idx++] = measure;
		
		
		try {
			iVarGroup = new Integer(idvargroup);
			param[idx++] = iVarGroup;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = imgon;
		param[idx++] = imgoff;
		
		try {
			iFrequency = new Integer(freq);
			param[idx++] = iFrequency;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			fDelta = new Float(delta);
			param[idx++] = fDelta;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iDelay = new Integer(delay);
			param[idx++] = iDelay;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		param[idx++] = isonchange;
		param[idx++] = ishaccp;
		param[idx++] = isactive;
		param[idx++] = iscancelled;
		
		
		try {
			iGrpCategory = new Integer(grpcategory);
			param[idx++] = iGrpCategory;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			iHsVariable = new Integer(idhsvariable);
			param[idx++] = iHsVariable;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			in = new Timestamp(Long.parseLong(insTime));
			param[idx++] = in;
		}
		catch(Exception e){
			param[idx++] = null;
		}
		
		try {
			up = new Timestamp(Long.parseLong(updTime));
			param[idx++] = up;
		}
		catch(Exception e){
			param[idx++] = null;
		}
				
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
			try {
				this.descriptionIns(iVar.intValue(),desc,up.getTime());
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
		catch(DataBaseException dbe) {
			update(iVar,iDevice,pvCode,isLogic,iVarMdl,iFunction,code,iType,iaddrIn,iaddrOut,iDimension,iLenght,
				   iPosition,signed,iDecimal,display,button,iPriority,readwrite,min,max,def,measure,iVarGroup,
				   imgon,imgoff,iFrequency,fDelta,iDelay,isonchange,ishaccp,isactive,iscancelled,iGrpCategory,
				   iHsVariable,in,up);
			try {
				this.descriptionUpd(iVar.intValue(),desc,up.getTime());
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
	}
	
	private void update(Integer idVar,Integer idDevice,String pvCode,String isLogic,Integer idVarMdl,Integer funcCode,
						String code,Integer type,Integer addIn,Integer addOut,Integer vardimension,Integer varlenght,
						Integer bitPos,String signed,Integer decimal,String display,String button,Integer prioriti,
						String readwrite,String min,String max,String def,String measure,Integer idvargroup,
						String imgon,String imgoff,Integer freq,Float delta,Integer delay,String isonchange,
						String ishaccp,String isactive,String iscancelled,Integer grpcategory,Integer idhsvariable,
						Timestamp insTime,Timestamp updTime)
		throws Exception
	{
		
		String sql = "update cfvariable set pvcode=?,iddevice=?,islogic=?,idvarmdl=?,functioncode=?,code=?,type=?," +
					 "addressin=?,addressout=?,vardimension=?,varlength=?,bitposition=?,signed=?,decimal=?," +
					 "todisplay=?,buttonpath=?,priority=?,readwrite=?,minvalue=?,maxvalue=?,defaultvalue=?,measureunit=?,"+
					 "idvargroup=?,imageon=?,imageoff=?,frequency=?,delta=?,delay=?,isonchange=?,ishaccp=?,isactive=?,"+
					 "iscancelled=?,grpcategory=?,idhsvariable=?,inserttime=?,lastupdate=? where idvariable=? and idsite=?";
		
		Object[] param = {pvCode,idDevice,isLogic,idVarMdl,funcCode,code,type,addIn,addOut,vardimension,varlenght,
				bitPos,signed,decimal,display,button,prioriti,readwrite,min,max,def,measure,idvargroup,imgon,imgoff,
				freq,delta,delay,isonchange,ishaccp,isactive,iscancelled,grpcategory,idhsvariable,insTime,updTime,
				idVar,new Integer(this.idSite)};
		
		DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
	}
}
