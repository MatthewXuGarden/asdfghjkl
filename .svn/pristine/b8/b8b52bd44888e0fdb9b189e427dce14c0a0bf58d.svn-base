package com.carel.supervisor.plugin.energy;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class EnergyConfiguration {

	public static final String NUMGROUPS = "groups";
	public static final String NUMCONSUMERS = "consumerpergroup";
	public static final String MAXCONSUMERS = "maxconsumers";
	public static final String GROUP = "group";
	public static final String CONSUMER = "cons";
	public static final String SITE = "site";

	private Properties properties;
	private List<EnergyGroup> groups;
	private HashMap<Integer, EnergyActiveDevice> actives;
	private Boolean loaded = false;
	
	public EnergyConfiguration() {
		properties = new Properties();
		groups = new LinkedList<EnergyGroup>();
		actives = new HashMap<Integer, EnergyActiveDevice>();
		EnergyGroup g;
		EnergyConsumer c;
		RecordSet rs, rs2;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from energysite;");
			for (int i = 0; i < rs.size(); i++) {
				properties.setProperty(rs.get(i).get("key").toString(), UtilBean.trim(rs.get(i).get("value")));
			}
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from energygroup order by idgroup;");
			for (int i = 0; i < rs.size(); i++) {
				g = new EnergyGroup(rs.get(i));
				// rs2 = DatabaseMgr.getInstance().executeQuery(null, "select *
				// from energyconsumer where idgroup = " + g.getId() + " order
				// by idgroup, idconsumer;");
				rs2 = DatabaseMgr.getInstance().executeQuery(
						null,
						" select energyconsumer.*, dev.iddevice  from  "
								+ " energyconsumer, cfdevice as dev,  cfvariable as kw, cfvariable as kwh "
								+ " where energyconsumer.idgroup = " + g.getId()
								+ " and  energyconsumer.idkw=kw.idvariable and "
								+ " energyconsumer.idkwh=kwh.idvariable and kw.iddevice=dev.iddevice and "
								+ " dev.iscancelled='FALSE' and kw.iscancelled='FALSE' and kwh.iscancelled='FALSE' "
								+ " order by idconsumer;");
				for (int j = 0; j < rs2.size(); j++) {
					c = new EnergyConsumer(rs2.get(j));
					g.addConsumer(c);
				}
				groups.add(g);
			}
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from energyactive,cfvariable  " +
					" where energyactive.idvariable=cfvariable.idvariable and cfvariable.iscancelled='FALSE' order by idgroup;");
			for (int i = 0; i < rs.size(); i++) {
				try{
					Integer idgrp;
					Integer idvariable;
					Integer iddevice;
					Boolean inverse;
					Boolean enabled;
					idgrp = (Integer)rs.get(i).get("idgroup");
					iddevice = (Integer)rs.get(i).get("iddevice");
					idvariable = (Integer)rs.get(i).get("idvariable");
					inverse = (Boolean)rs.get(i).get("inverse");
					try{
						enabled = (Boolean)rs.get(i).get("enabled");
					}catch(Exception e){
						enabled = false;
					}
					EnergyActiveDevice ead = new EnergyActiveDevice(idgrp, iddevice, idvariable, inverse, enabled);
					actives.put(ead.getIdgrp(), ead);
				}catch (Exception ee) {
					LoggerMgr.getLogger(this.getClass()).error(ee);
				}
			}
		} catch (Exception e) {
			EventMgr.getInstance().error(1, "Energy", "EGErr", "EG001", null);
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		loaded = true;
	}

	public List<EnergyConsumer> getConsumerList() {
		List<EnergyConsumer> l = new LinkedList<EnergyConsumer>();
		for (Iterator<EnergyGroup> itr = groups.iterator(); itr.hasNext();) {
			l.addAll(itr.next().getConsumers());
		}
		return l;
	}
	
	public List<EnergyConsumer> getConsumerList(String language) {
		List<EnergyConsumer> l = new LinkedList<EnergyConsumer>();
		for (Iterator<EnergyGroup> itr = groups.iterator(); itr.hasNext();) {
			EnergyGroup  energy = itr.next();
			for(int i=0; i<energy.getConsumers().size();i++){
				energy.getConsumers().get(i).setName(getConsumer(energy.getConsumers().get(i).getIdDev(),language));	
			}
			l.addAll(energy.getConsumers());
		}
		return l;
	}

	public String getConsumer(Integer iddevice,String language){
		String deviceName="";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(
					null,
					" select description from cftableext"
							+ " where tableid=" + iddevice + " and tablename='cfdevice' " 
							+ "and idsite=1 and languagecode='" + language + "';");
			if(rs.size()>0){
				deviceName = (String)rs.get(0).get(0);
			}
		} catch (Exception ee) {
			LoggerMgr.getLogger(this.getClass()).error(ee);
		}
		return deviceName;
	}

	
	public EnergyConsumer getConsumer(int idConsumer)
	{
		List<EnergyConsumer> list = getConsumerList();
		for(int i = 0; i < list.size(); i++) {
			EnergyConsumer ec = list.get(i);
			if( ec.getIdconsumer() == idConsumer )
				return ec;
		}
		return null;
	}
	
	
	public Float getSiteKw() {
		Float acc = 0f;
		try {
			for (Iterator<EnergyGroup> itr = groups.iterator(); itr.hasNext();) {
				EnergyGroup eg = itr.next();
				if (Float.NaN == eg.getGroupKw())
					return Float.NaN;
				else
					acc += eg.getGroupKw();
			}
		} catch (Exception e) {
			return Float.NaN;
		}
		return acc;
	}

	public List<EnergyGroup> getGroups() {
		return groups;
	}

	public EnergyGroup getGroup(Integer id) {
		for (Iterator<EnergyGroup> itr = groups.iterator(); itr.hasNext();) {
			EnergyGroup eg = itr.next();
			if (eg.getId().equals(id))
				return eg;
		}
		return null;
	}

	public Boolean getLoaded() {
		return loaded;
	}

	public String getSiteProperty(String name) {
		return properties.getProperty(name);
	}

	public String getSiteProperty(String name, String def) {
		return properties.getProperty(name, def);
	}

	public Timestamp getFirstSample() {
		try {
			Timestamp ret = (Timestamp) DatabaseMgr.getInstance().executeQuery(null,
					"select min(firstsample) from energylogsupport where firstsample>'1970-01-02'").get(0).get(0);
			if(ret.getTime()<1)
				return null;
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	public Timestamp getLastSample() {
		try {
			Timestamp ret = (Timestamp) DatabaseMgr.getInstance().executeQuery(null,
					"select max(lastsample) from energylogsupport").get(0).get(0);
			if(ret.getTime()<1)
				return null;
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	public EnergyActiveDevice getActiveDevice(int idgrp) {
		return actives.get(idgrp);
	}

}
