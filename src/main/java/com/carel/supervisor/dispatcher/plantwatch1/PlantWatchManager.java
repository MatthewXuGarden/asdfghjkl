package com.carel.supervisor.dispatcher.plantwatch1;

import java.util.Date;
import java.util.Map;

public class PlantWatchManager implements IPlantWatchEvent {
	private PlantWatch pw = null;
	public void OnConnected(int Node) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#GetAllarmsFormDate(java.lang.String, java.lang.String, java.util.Date, com.carel.supervisor.dispatcher.plantwatch1.PlantWatchAlarmList)
	 */
	public void GetAllarmsFormDate(	String dirpath, 
									String xmlfile, 
									Date fromDate, 
									PlantWatchAlarmList pwal) {
		pw.GetAllarmsFormDate(dirpath, xmlfile, fromDate, pwal);
	}

	public void OnSynchronized(int Node) {
		// TODO Auto-generated method stub

	}
	public void OnDisconnected(int Node) {
		// TODO Auto-generated method stub

	}
	PlantWatchManager(String name, String modemToWait, Map mp_UserPassword)
	{
		try
		{
			if(pw == null)
			{
				pw = new PlantWatch(name,modemToWait,this); 
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#CloseService()
	 */
	public boolean CloseService() {
		return pw.CloseService();
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#ConnectToDevice(int, int)
	 */
	public boolean ConnectToDevice(int CommPort, int Ident) {
		return pw.ConnectToDevice(CommPort, Ident);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#ConnectToDevice(java.lang.String, java.lang.String, int)
	 */
	public boolean ConnectToDevice(String Modem, String Tel, int Ident) {
		return pw.ConnectToDevice(Modem, Tel, Ident);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#DisconnectDevice()
	 */
	public boolean DisconnectDevice() {
		return pw.DisconnectDevice();
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#isConnected()
	 */
	public boolean isConnected() {
		return pw.isConnected();
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#ReadAllVar(java.lang.String, long, long)
	 */
	public boolean ReadHistoryVar(String dirpath, long FromDate, long ToDate, PlantWatchHistoryVars pwhvs) {
		return pw.ReadHistoryVar(dirpath, FromDate, ToDate, pwhvs);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#ReadVar(int, int, int, com.carel.supervisor.dispatcher.plantwatch1.VarDouble)
	 */
	public boolean ReadVar(int Unit, int Address, int Type, VarDouble val) {
		return pw.ReadVar(Unit, Address, Type, val);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#SendVar(int, int, int, java.lang.Double)
	 */
	public boolean SendVar(int Unit, int Address, int Type, Double val) {
		return pw.SendVar(Unit, Address, Type, val);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#AddUserPasswordList(java.util.Map)
	 */
	public void AddUserPasswordList(Map mp_UserPassword) {
		pw.AddUserPasswordList(mp_UserPassword);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#GetSiteConfig(java.lang.String)
	 */
	public PlantWatchSiteConfig GetSiteConfig(String dirpath) {
		return pw.GetSiteConfig(dirpath);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#GetVarUnitType(java.lang.String, java.lang.String)
	 */
	public PlantWatchVarUnitType GetVarUnitType(String dirpath, String type) {
		return pw.GetVarUnitType(dirpath, type);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#ReadHistoryVar(java.lang.String, long, long, int, com.carel.supervisor.dispatcher.plantwatch1.PlantWatchHistoryVars)
	 */
	public boolean ReadHistoryVar(String dirpath, long fromDate, long toDate, int unit, PlantWatchHistoryVars pwhvs) {
		return pw.ReadHistoryVar(dirpath, fromDate, toDate, unit, pwhvs);
	}

	/* (non-Javadoc)
	 * @see com.carel.supervisor.dispatcher.plantwatch1.PlantWatch#ReadUnitVar(int, java.lang.String, com.carel.supervisor.dispatcher.plantwatch1.PlantWatchUnitVars)
	 */
	public boolean ReadUnitVar(int unit, String dirpath, PlantWatchUnitVars pwvs) {
		return pw.ReadUnitVar(unit, dirpath, pwvs);
	}

}
