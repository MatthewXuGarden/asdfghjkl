package com.carel.supervisor.presentation.devices;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangSection;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.session.UserSession;


public class ParamDetail
{
    public static final String VIEW = "dtlview";
    List<VarphyBean> listVarWrite = null;
    List<String> listValueWrite = null;
    List<String> listValueRead = null;
    List<Boolean> listVarWriteDisab = null;
    private String idLang = "";
    private int idDevice = -1;
    private int idSite = -1;
    private LangSection translator = null;
    private UserSession session = null;

    public ParamDetail(UserSession userSession)
    {
        this.idSite = userSession.getIdSite();

        if (this.idSite < 0)
        {
            this.idSite = 1;
        }

        try
        {
            this.idDevice = Integer.parseInt(userSession.getProperty("iddev"));
        }
        catch (NumberFormatException e)
        {
        }

        idLang = userSession.getLanguage();
        this.translator = LangMgr.getInstance().getLangService(idLang)
                                 .getSection(VIEW);
        this.session = userSession;
        listVarWrite = new ArrayList<VarphyBean>();
        listValueWrite = new ArrayList<String>();
        listValueRead = new ArrayList<String>();
        listVarWriteDisab = new ArrayList<Boolean>();
    }

    public VarphyBean[] getListVarWrite()
    {
        if (listVarWrite.size() > 0)
        {
            VarphyBean[] varlist = new VarphyBean[listVarWrite.size()];

            for (int i = 0; i < listVarWrite.size(); i++)
            {
                varlist[i] = (VarphyBean) listVarWrite.get(i);
            }

            return varlist;
        }
        else
        {
            return null;
        }
    }

    public boolean[] getListVarDisab()
    {
        if (listVarWriteDisab.size() > 0)
        {
            boolean[] var_attr = new boolean[listVarWriteDisab.size()];

            for (int i = 0; i < listVarWriteDisab.size(); i++)
            {
                var_attr[i] = (Boolean) listVarWriteDisab.get(i);
            }

            return var_attr;
        }
        else
        {
            return null;
        }
    }

    public int getNumWrite()
    {
        return this.listVarWrite.size();
    }

    public String getMinValueWrite(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getMinValue();
    }

    public int getIdVar(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getId().intValue();
    }

    public int getTypeVar(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getType();
    }

    public String getMaxValueWrite(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getMaxValue();
    }

    public String getDefaultValueWrite(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getDefaultValue();
    }

    public String readValueWrite(int index)
    {
        return (String) listValueWrite.get(index);
    }

    public String readValueRead(int index)
    {
        return (String) listValueRead.get(index);
    }

    public String getUMWrite(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getMeasureUnit();

        return (ret != null) ? ret : "";
    }

    public String getLongDescWrite(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getLongDescription();

        return (ret != null) ? ret : "";
    }

    public String getShortDescWrite(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getShortDescription();

        return (ret != null) ? ret : "";
    }

    public String getGrpCodeWrite(int index)
    {
        Integer ret = ((VarphyBean) listVarWrite.get(index)).getGrpCode();

        return (ret != null) ? ret.toString() : "";
    }

    public String translate(String s)
    {
        return this.translator.get(s);
    }

    public String getNameTable(UserSession sessionUser, int idDev)
    {
        String ret = "";

        try
        {
            DeviceStructureList deviceStructureList = sessionUser.getGroup()
                                                                 .getDeviceStructureList();
            DeviceStructure deviceStructure = deviceStructureList.get(idDev);
            ret = deviceStructure.getDescription();
        }
        catch (Exception e)
        {
        }

        return ret;
    }

    public void loadVarphyToWrite() throws Exception
    {
        String rwState = null;
        int[] iArDevice = { this.idDevice };
        VarphyBean[] listVarphy = VarphyBeanList.getListVarWritable(this.idLang,
                this.idSite, iArDevice);

        for (int i = 0; i < listVarphy.length; i++)
        {
            rwState = null;

            if (listVarphy[i] != null)
            {
                rwState = listVarphy[i].getReadwrite();
            }

            if (rwState != null)
            {
                rwState = rwState.trim();
                
             // PROFILE FILTER: if FILTER_MANUFACTURER show all variables  
            	if (session.getVariableFilter()==ProfileBean.FILTER_MANUFACTURER)
            	{
            		listVarWrite.add(listVarphy[i]);
            	}
            	// else show all read/write but Manufacturer
            	else if (!rwState.equalsIgnoreCase("11"))
            	{
            		listVarWrite.add(listVarphy[i]);
            	}
                
                
                //check set param button for disable input text
            	if (session.isButtonActive("dtlview", "tab2name", "button2"))
            	{
            		listVarWriteDisab.add(new Boolean(false));
                }
                else
                {
                	listVarWriteDisab.add(new Boolean(true));
                }
            }
        }
    }

    public String getCode(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getShortDesc();

        return (ret != null) ? ret : "";
    }

    public String getCodeVar(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getCodeVar();

        return (ret != null) ? ret : "";
    }

    public String getHTMLGroupsHeader(int idsite, int iddevice, int screen_w,
        String language) throws DataBaseException
    {
        List<String> group_desc = new ArrayList<String>();
        List<Integer> group_ids = new ArrayList<Integer>();

        DeviceStructureList devStruncList = session.getGroup()
                                                   .getDeviceStructureList();
        LangService lan = LangMgr.getInstance().getLangService(language);

        try
        {
            loadVarphyToWrite();
        }
        catch (Exception e)
        {
        }
        
        // gruppo per visualizzare tutti i parametri id=0;
        group_desc.add(lan.getString("dtlview", "s_allparams"));
        group_ids.add(0);
        
        // WV - Quick - START
//        try 
//        {
//            String sql = "select iddevmdl from cfdevice where iddevice=?"; 
//            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(iddevice)});
//            Integer modello=(Integer)(rs.get(0).get("iddevmdl"));
//        
//            sql="select WVCONF,WVTYPE,WVPROB,WVUNIT from (select count(*) as WVCONF from wvcfconf where iddevmdl=?)" +
//                " as firsrt, (select count(*) as WVTYPE from wvcfreftype where iddevmdl=? ) as second, " +
//                "(select count(*) as WVPROB from wvcfprobtype where iddevmdl=?) as third, " +
//                "(select count(*) as WVUNIT from wvcfunittype where iddevmdl=?) as fourth";
//       
//            rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{modello,modello,modello,modello});
//        
//            if((((Integer)rs.get(0).get("wvconf")).intValue()>0)||(((Integer)rs.get(0).get("wvtype")).intValue()>0)||(((Integer)rs.get(0).get("wvprob")).intValue()>0)||(((Integer)rs.get(0).get("wvunit")).intValue()>0))
//            {
//                group_desc.add(lan.getString("dtlview", "s_confparam"));
//                group_ids.add(-1);
//            }
//        }
//        catch(Exception e) {
//        }
        // END
        
        // COMBO
        try 
        {
            String sql = "select iddevmdl from cfdevice where iddevice=?"; 
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(iddevice)});
            Integer modello=(Integer)(rs.get(0).get("iddevmdl"));
        
            String sql_conf = " select distinct idcombogroup from cfcombo where iddevmdl=? and idcombogroup is not null";
            rs = DatabaseMgr.getInstance().executeQuery(null,sql_conf,new Object[]{modello});
        
            if(rs.size()>0)
            {
            	for(int i=0;i<rs.size();i++)
            	{
	            	sql = "select description from cftableext where tablename = ? and tableid = ? and languagecode = ?";
	            	Integer idcombogroup = (Integer)rs.get(i).get("idcombogroup");
	            	RecordSet res = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{"cfcombogroup",idcombogroup,lan.getLanguage()});
	            	group_desc.add((String)res.get(0).get("description"));
	                group_ids.add(-idcombogroup);
            	}
            }
        }
        catch(Exception e) {
        }
        //END
        
        Integer id_grp = null;

        for (int i = 0; i < getNumWrite(); i++)
        {
            id_grp = new Integer(getGrpCodeWrite(i));

            if (!group_ids.contains(id_grp))
            {
                String tmp = devStruncList.getVarGroups(new Integer(id_grp));
                group_desc.add(tmp);
                group_ids.add(new Integer(id_grp));
            }
        }
               
        
        int group_number = group_ids.size();
        int rows = 0;

        // creazione html
        if (group_number > 0)
        {
            StringBuffer tmp = new StringBuffer(
                    "<table border='0' cellpadding='0' cellspacing='4'  align='left' valign='top'>");// width='" +  (screen_w - 124) + "'

            String grps = "";
            int i = 0;
            int cell_width = 170;
            int cell_height = 30;
            int column_number = (screen_w - 124) / cell_width;

            while (i < group_number)
            {
                rows++;
                if (rows%2 == 0) {
                	tmp.append("<tr class='Row1'>");
                }
                else {
                	tmp.append("<tr class='Row1'>");
                }

                for (int j = 0; j < column_number; j++)
                {
                    if (i < group_number)
                    {
                        tmp.append("<td onclick='if(checkModUser()){load_params(" +
                            group_ids.get(i) +
                            ")}' align='center' width='" +
                            cell_width + "px' height='" + cell_height +
                            "px' class='groupCategory' id='td_grp_" +
                            group_ids.get(i) + "'>" +
                            group_desc.get(i) + "</td>");
                        grps = grps + group_ids.get(i) + ";";
                        i++;

                        if (j == (column_number - 1))
                        {
                            tmp.append("</tr>");
                        }
                    }
                    else
                    {
                        tmp.append("</tr>");

                        break;
                    }
                }
            }

            tmp.append("</table>");
            grps = grps.substring(0, grps.length() - 1);
            tmp.append("<input type='hidden' id='idsgroups' value='" + grps +
                "'/>");
            tmp.append("<input type='hidden' id='row_header' value='" + rows +
                "'/>");

            return tmp.toString();
        }

        return "";
    }
    

    public String getSelectOptions(int idsite, int iddevice, String language, int idParamGroup) throws DataBaseException
    {
		List<String> group_desc = new ArrayList<String>();
		List<Integer> group_ids = new ArrayList<Integer>();
		
		DeviceStructureList devStruncList = session.getGroup().getDeviceStructureList();
		LangService lan = LangMgr.getInstance().getLangService(language);

        try {
            loadVarphyToWrite();
        }
        catch(Exception e) {
        }
        
		group_desc.add(lan.getString("dtlview", "s_allparams"));
		group_ids.add(0);
        
        // COMBO
        try 
        {
            String sql = "select iddevmdl from cfdevice where iddevice=?"; 
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(iddevice)});
            Integer modello=(Integer)(rs.get(0).get("iddevmdl"));
        
            String sql_conf = " select distinct idcombogroup from cfcombo where iddevmdl=? and idcombogroup is not null";
            rs = DatabaseMgr.getInstance().executeQuery(null,sql_conf,new Object[]{modello});
        
            if(rs.size()>0)
            {
            	for(int i=0;i<rs.size();i++)
            	{
	            	sql = "select description from cftableext where tablename = ? and tableid = ? and languagecode = ?";
	            	Integer idcombogroup = (Integer)rs.get(i).get("idcombogroup");
	            	RecordSet res = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{"cfcombogroup",idcombogroup,lan.getLanguage()});
	            	group_desc.add((String)res.get(0).get("description"));
	                group_ids.add(-idcombogroup);
            	}
            }
        }
        catch(Exception e) {
        }
        //END
		
        
        Integer id_grp = null;

        for (int i = 0; i < getNumWrite(); i++)
        {
            id_grp = new Integer(getGrpCodeWrite(i));

            if (!group_ids.contains(id_grp))
            {
                String tmp = devStruncList.getVarGroups(new Integer(id_grp));
                group_desc.add(tmp);
                group_ids.add(new Integer(id_grp));
            }
        }
               
        // create select options
        StringBuffer tmp = new StringBuffer();
        for(int i = 0; i < group_ids.size(); i++) {
        	tmp.append("<option value=\"");
        	tmp.append(group_ids.get(i));
        	if( group_ids.get(i) == idParamGroup )
        		tmp.append("\" selected>");
        	else
        		tmp.append("\">");
        	tmp.append(group_desc.get(i));
        	tmp.append("</option>\n");
        }
        return tmp.toString();
    }
}
