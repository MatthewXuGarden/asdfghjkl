package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.profile.ProfileGroupsBeanList;


public class GroupListBean
{
    private static final String IDGROUP = "idgroup";
    private static final String ISGLOBAL = "isglobal";
    private static final String DESCRIPTION = "description";
    private Map groupList = new HashMap();
    private GroupBean globalGroup = null;
    private int[] ids = null;
    private DeviceStructureList deviceStructureList = null;
    public GroupListBean()
    {
    }

    public GroupListBean(int site, String language, ProfileGroupsBeanList profileGroupsBeanList)
    throws Exception
	{
    	Record record = null;
    	Integer groupId = null;
    	String isglobal = null;
    	GroupBean groupBean = null;
    	List groupIds = new ArrayList();
	    String sql = "select cfgroup.idgroup as idgroup, cfgroup.isglobal as isglobal, cftableext.description as description " +
	        "from cfgroup inner join cftableext on cftableext.tableid = cfgroup.idgroup and cftableext.tablename='cfgroup' " +
	        "and cfgroup.idsite = ? and cftableext.languagecode = ? and cftableext.idsite = ? order by cfgroup.isglobal desc, description asc, cfgroup.idgroup asc";
	
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	            new Object[] { new Integer(site), language, new Integer(site) });
	
	    for (int i = 0; i < rs.size(); i++)
	    {
	        record = rs.get(i);
	        groupId = (Integer) record.get(IDGROUP);
	
	        if (checkGroup(profileGroupsBeanList, groupId))
	        {
	            isglobal = UtilBean.trim(record.get(ISGLOBAL));
	            groupBean = new GroupBean(groupId.intValue(), isglobal);
	            groupBean.setDescription((String) record.get(DESCRIPTION));
	            groupList.put(groupId, groupBean);
	            groupIds.add(groupId);
	            if ("TRUE".equals(isglobal) && groupId.intValue() == 1)
                {
                    globalGroup = groupBean;
                }
	        }
	    }
	    ids = new int[groupIds.size()];
	    for(int i=0;i<ids.length;i++)
	    {
	    	ids[i] = (Integer)groupIds.get(i);
	    }
	    loadDeviceStructureList(site, language);
	}
    private void loadDeviceStructureList(int site, String language)
    throws Exception
	{
	    DeviceListBean deviceListBean = new DeviceListBean(site, language);
	    int[] idsDevice = deviceListBean.getIds();
	    DeviceBean deviceBean = null;
	    int idgroup = 0;
	    DeviceStructure deviceStructure = null;
	    deviceStructureList = new DeviceStructureList();
	    
	    if (idsDevice != null)
	    {
	    	String[] tmp = new String[idsDevice.length];
	        Map map = new HashMap();
	        for (int i = 0; i < idsDevice.length; i++)
	        {
	            deviceBean = deviceListBean.getDevice(idsDevice[i]);
	            idgroup = deviceBean.getGroupId();
	            
	            deviceStructure = new DeviceStructure(site, idsDevice[i], idgroup,
	                    deviceBean.getCode(), deviceBean.getDescription(),
	                    getDescriptionGroupByGroupId(idgroup),
	                    deviceBean.getImageDevice());
	            
	            deviceStructure.setIsLogic(deviceBean.islogic());
	            deviceStructure.loadVariables(language, site);
	            deviceStructure.setIdDevMdl(deviceBean.getIddevmdl());
	            
	            // SDK
	            if(deviceStructure != null)
	                deviceStructure.setAddressIn(deviceBean.getAddress());
	            // Fine
	            
	            tmp[i] = deviceStructure.getCode();
	            map.put(tmp[i], deviceStructure);
	        }
	
	        Arrays.sort(tmp);
	
	        for (int i = 0; i < tmp.length; i++)
	        {
	            deviceStructureList.addDevice((DeviceStructure)map.get(tmp[i]));
	        }
	    }
	
	    deviceStructureList.loadVarGroups(site, language);
	}
    public String getDescriptionGroupByGroupId(int groupId)
    {
        if (-1 == groupId)
        {
            return "";
        }
        if(groupList != null && groupList.containsKey(groupId))
        {
        	GroupBean group = (GroupBean)groupList.get(groupId);
        	if(group.isGlobal())
        	{
        		return "";
        	}
        	else
        	{
        		return group.getDescription();
        	}
        }
        return "";
    }
    public DeviceStructureList getDeviceStructureList()
    {
        return deviceStructureList;
    }
    public void reloadDeviceStructureList(int site, String language)
    	throws Exception
	{
	    deviceStructureList.clear();
	    loadDeviceStructureList(site, language);
	}
    private boolean checkGroup(ProfileGroupsBeanList profileGroupsBeanList, Integer idGroup)
	{
	    if (null == profileGroupsBeanList || profileGroupsBeanList.size() == 0)
	    {
	        return true;
	    }
	    else
	    {
	        return profileGroupsBeanList.isGroupActive(idGroup.intValue());
	    }
	}
    public GroupBean getGlobalGroup()
    {
    	return this.globalGroup;
    }
    public GroupBean[] retrieveAllGroups(int idsite, String language)
        throws DataBaseException
    {
        String sql =
            "select cfgroup.*, cftableext.description from cfgroup inner join cftableext " +
            " on cftableext.tableid=cfgroup.idgroup and cftableext.tablename='cfgroup' and languagecode=? and " +
            " cfgroup.idsite = ? and cftableext.idsite = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, new Integer(idsite), new Integer(
                        idsite) });
        GroupBean[] groups = new GroupBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            groups[i] = new GroupBean(rs.get(i), idsite, language);
            groupList.put(new Integer(groups[i].getGroupId()), groups[i]);
        }
        this.updateGroupIds();
        return groups;
    }

    public GroupBean[] retrieveAllGroupsNoGlobal(int idsite, String language)
        throws DataBaseException
    {
        String sql =
            "select cfgroup.*, cftableext.description from cfgroup inner join cftableext " +
            " on cftableext.tableid=cfgroup.idgroup and cftableext.tablename='cfgroup' and languagecode=? and " +
            " cfgroup.idsite = ? and cfgroup.isglobal = ? and cftableext.idsite = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(idsite), "FALSE", new Integer(idsite)
                });
        GroupBean[] groups = new GroupBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            groups[i] = new GroupBean(rs.get(i), idsite, language);
            groupList.put(new Integer(groups[i].getGroupId()), groups[i]);
        }
        this.updateGroupIds();
        return groups;
    }


    public GroupBean get(int i)
    {
        return (GroupBean) groupList.get(new Integer(i));
    }

    public int size()
    {
        return groupList.size();
    }

    public int[] getIds()
    {
        return ids;
    }
    
    private void updateGroupIds()
    {
    	Iterator iterator = groupList.keySet().iterator();
        ids = new int[groupList.size()];
        int count = -1;

        while (iterator.hasNext())
        {
            count++;
            ids[count] = ((Integer) iterator.next()).intValue();
        }
        Arrays.sort(ids);
    }
}
