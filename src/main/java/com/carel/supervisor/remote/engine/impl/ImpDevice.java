package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.remote.engine.master.ImpMaster;

public class ImpDevice extends ImpMaster 
{
	public static final int NUM_COL = 17;
	
	public ImpDevice(int idsite,String na,String db,String lang)
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
					insert(arData[0],arData[1],arData[3],arData[4],arData[5],arData[6],arData[7],arData[8],
						   arData[9],arData[10],arData[11],arData[12],arData[13],arData[14],arData[15],arData[16]);
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
	
	private void insert(String idDevice,String pvCode,String isLogic,String idDevMdl,String idline,String addres,
						String little,String code,String img,String idGroup,String gloIdx,String isEnable,
						String isCancel,String isTime,String upTime,String desc)
		throws Exception
	{
		String sql = "insert into cfdevice values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] param = new Object[16];
		
		Integer iDevice = null;
		Integer iDevMdl = null;
		Integer iLine = null;
		Integer iAddre = null;
		Integer iGroup = null;
		Integer iGlbIdx = null;
		Timestamp in = null;
		Timestamp up = null;
		
		try {
			iDevice = new Integer(idDevice);
			param[0] = iDevice;
		}
		catch(Exception e){
			param[0] = null;
		}
		
		param[1] = pvCode;
		param[2] = new Integer(this.idSite);
		param[3] = isLogic;
		
		try {
			iDevMdl = new Integer(idDevMdl);
			param[4] = iDevMdl;
		}
		catch(Exception e){
			param[4] = null;
		}
		
		try {
			iLine = new Integer(idline);
			param[5] = iLine;
		}
		catch(Exception e){
			param[5] = null;
		}
		
		try {
			iAddre = new Integer(addres);
			param[6] = iAddre;
		}
		catch(Exception e){
			param[6] = null;
		}
			
		param[7] = little;
		param[8] = code;
		param[9] = img;
		
		try {
			iGroup = new Integer(idGroup);
			param[10] = iGroup;
		}
		catch(Exception e){
			param[10] = null;
		}
		
		try {
			iGlbIdx = new Integer(gloIdx);
			param[11] = iGlbIdx;
		}
		catch(Exception e){
			param[11] = null;
		}
		
		param[12] = isEnable;
		param[13] = isCancel;
		
		try {
			in = new Timestamp(Long.parseLong(isTime));
			param[14] = in;
		}
		catch(Exception e){
			param[14] = null;
		}
		
		try {
			up = new Timestamp(Long.parseLong(upTime));
			param[15] = up;
		}
		catch(Exception e){
			param[15] = null;
		}
		
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
			try {
				this.descriptionIns(iDevice.intValue(),desc,up.getTime());
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
		catch(DataBaseException dbe) {
			update(iDevice,pvCode,isLogic,iDevMdl,iLine,iAddre,little,code,img,iGroup,iGlbIdx,isEnable,isCancel,in,up);
			try {
				this.descriptionUpd(iDevice.intValue(),desc,up.getTime());
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
	}
	
	private void update(Integer idDevice,String pvCode,String isLogic,Integer idDevMdl,Integer idLine,Integer address,String little,
						String code,String img,Integer idGroup,Integer glbidx,String enabled,String cancel,Timestamp insT,Timestamp updT)
		throws Exception
	{
		
		String sql = 
		"update cfdevice set pvcode=?,islogic=?,iddevmdl=?,idline=?,address=?,littlendian=?,code=?," +
		"imagepath=?,idgroup=?,globalindex=?,isenabled=?,iscancelled=?,inserttime=?,lastupdate=? " + 
		"where iddevice=? and idsite=?";
		
		Object[] param = 
		{pvCode,isLogic,idDevMdl,idLine,address,little,code,img,idGroup,glbidx,enabled,cancel,insT,updT,idDevice,new Integer(this.idSite)};
		
		DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
	}
}
