package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.Map;


public class ProfileBeanList
{
    private ProfileBean[] profile_list;
    private Map profile_by_id = new HashMap();
    private int screenw = 1024;
    private int screenh = 768;
    
    
    public ProfileBeanList(int idsite, boolean with_carel_profile) 
    {
    	String sql = "";
    	if (with_carel_profile)
    		sql = "select * from profilelist where idprofile not in (-4,-6) order by upper (code)";
    	else
    		sql = "select * from profilelist where idprofile not in (-4,-5,-6) order by upper (code)";
        RecordSet rs;
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
		

        profile_list = new ProfileBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            profile_list[i] = new ProfileBean(rs.get(i));
            profile_by_id.put(profile_list[i].getIdprofile(), profile_list[i]);
        }
		} catch (DataBaseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public ProfileBean getProfile(int i)
    {
        return profile_list[i];
    }

    public ProfileBean getProfileById(int id)
    {
        return (ProfileBean) profile_by_id.get(new Integer(id));
    }

    public int size()
    {
        return profile_list.length;
    }

    public static int addProfile(int idsite, String description, String param, boolean nomenu)
        throws DataBaseException
    {
        String sql = "insert into profilelist values (?,?,?,?,?)";
        SeqMgr o = SeqMgr.getInstance();
        Object[] values = new Object[5];
        values[0] = o.next(null, "profilelist", "idprofile");
        values[1] = description;
        values[2] = param;
        values[3] = new Timestamp(System.currentTimeMillis());
        values[4] = nomenu;
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        return ((Integer) values[0]).intValue();
    }
    public static void updateProfileId(int oldProfileId,int newProfileId,String code)
    	throws DataBaseException
    {
    	String sql = "update profilelist set idprofile=?,code=? where idprofile=?";
    	Object[] values = {newProfileId,code,oldProfileId};
    	DatabaseMgr.getInstance().executeStatement(sql, values);
    	
    	values = new Object[]{newProfileId,oldProfileId};

        sql = "update profilemaps set idprofile=? where idprofile = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        sql = "update profilevars set idprofile=? where idprofile = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        sql = "update profilegroups set idprofile=? where idprofile = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }
    public static void deleteProfile(int idprofile, int idsite)
        throws DataBaseException
    {
        String sql = "delete from profilelist where idprofile = ?";
        Object[] values = new Object[1];
        values[0] = new Integer(idprofile);
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        values = new Object[1];
        values[0] = new Integer(idprofile);

        sql = "delete from profilemaps where idprofile = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        sql = "delete from profilevars where idprofile = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        sql = "delete from profilegroups where idprofile = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public static ProfileBean retrieveProfile(int idprofile, int idsite)
        throws DataBaseException
    {
        String sql = "select * from profilelist where idprofile = ?";
        Object[] values = new Object[1];
        values[0] = new Integer(idprofile);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);

        return new ProfileBean(rs.get(0));
    }

    public static void updateProfile(int idsite, int idprofile,
        String new_description, String param,boolean nomenu) throws DataBaseException
    {
        String sql = "update profilelist set code=?,status=?,nomenu=? where idprofile = ?";
        Object[] values = new Object[4];
        values[0] = new_description;
        values[1] = param;
        values[2] = nomenu;
        values[3] = new Integer(idprofile);
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public String getHtmlProfileTable(int height, int width, String language,
        String tablename)
    {
        String html = "";

        //header tabella
        LangService langService = LangMgr.getInstance().getLangService(language);
        String[] header = new String[]
            {
                langService.getString("ldapTables", "description"),
                langService.getString("siteview","devconfig")
            };

        //dati tabella
        HTMLElement[][] data = null;
        String[] sngClick = null;
        ProfileBean tmp_prof = null;
        int rows = size();  

        data = new HTMLElement[rows][];
        sngClick = new String[rows];

        for (int i = 0; i < rows; i++)
        {
            tmp_prof = profile_list[i];

           
                data[i] = new HTMLElement[2];
                data[i][0] = new HTMLSimpleElement(tmp_prof.getCode());
                
                //al click passo "id" - "descrizione" - "stato"
                sngClick[i] = String.valueOf(tmp_prof.getIdprofile()) + "-" +
                    tmp_prof.getCode()+"-"+!tmp_prof.getNomenu() + "-" + tmp_prof.getStatus();
                data[i][1] = new HTMLSimpleElement("<img src=\"images/button/settings_on_black.png\" style='cursor:pointer;' onclick=loadProfile(aValue["+i+"][2])>");
           
        }

        HTMLTable profilesTable = new HTMLTable(tablename, header, data, true,
                false);

        profilesTable.setSgClickRowAction("resetProfile('$1');");
        profilesTable.setDbClickRowAction("loadProfile('$1');");
        profilesTable.setSnglClickRowFunction(sngClick);
        profilesTable.setDlbClickRowFunction(sngClick);
        profilesTable.setScreenH(screenh);
        profilesTable.setScreenW(screenw);
        profilesTable.setHeight(height);
        profilesTable.setWidth(width);
        profilesTable.setRowHeight(25);
        profilesTable.setAlignType(1, HTMLTable.CENTER);
        profilesTable.setColumnSize(0, width - 160);
        profilesTable.setColumnSize(1, 100);

        html = profilesTable.getHTMLText();

        return html;
    }

    public static boolean isProfileUsed(int idprofile)
        throws DataBaseException
    {
        String sql = "select count(1) from cfusers where idprofile=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idprofile) });

        if (rs.get(0).get("count").toString().equals("0"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }

}
