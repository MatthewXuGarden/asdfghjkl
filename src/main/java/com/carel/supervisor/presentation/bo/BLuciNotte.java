package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.director.lightnight.LightNightMgr;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.lucinotte.LNField;
import com.carel.supervisor.presentation.lucinotte.LNGroups;
import com.carel.supervisor.presentation.lucinotte.LNUtils;
import com.carel.supervisor.presentation.session.Transaction;
import com.carel.supervisor.presentation.session.UserSession;

public class BLuciNotte extends BoMaster
{
	private static final long serialVersionUID = -7268127237384133063L;
	private static final int REFRESH_TIME = 10000;
    private final int MAX_VAR = 6;
    private final String[] VARMODELS = {"cmd_onoff","cmd_luci","cmd_notte","st_onoff","st_luci","st_notte"};
    
    public BLuciNotte(String l)
    {
        super(l, REFRESH_TIME);
    }
    
    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "onload_tab1();");
        p.put("tab2name", "onload_tab2();");
        p.put("tab3name", "onload_tab3();");
        p.put("tab4name", "onload_tab4();");
        p.put("tab5name", "onload_tab5();");
        p.put("tab6name", "onload_tab6();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "lucinotte.js");
        p.put("tab2name", "lucinotte.js;keyboard.js;");
        p.put("tab3name", "lucinotte.js");
        p.put("tab4name", "lucinotte.js");
        p.put("tab5name", "lcnt_days.js;keyboard.js;");
        p.put("tab6name", "lucinotte.js");

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        //String user = us.getUserName();

        if ((us.getProperty("cmd") != null) && (! "".equals(us.getProperty("cmd"))))
        {
            String cmd = us.getPropertyAndRemove("cmd");
            
            if ("save_grps_status".equals(cmd))
            {
//                Transaction transaction = us.getTransaction();
//                Properties properties = new Properties();
//                            
//                int idsite = us.getIdSite();
                
                //recupero num max gruppi:
                int n_max_grps = LNGroups.MAX_GROUPS;
                
                //recupero valori x tabella Groups:
                int k = 0;
                
                Object[][] paramsGroups = new Object[n_max_grps][7];
                
                for (int i = 1; i <= n_max_grps; i++)
                {
                    if ((prop.getProperty("name_grp_"+i) != null) && (! "".equals(prop.getProperty("name_grp_"+i))))
                    {
                        paramsGroups[k][0] = new Integer(i); //idgroup
                        paramsGroups[k][1] = prop.getProperty("name_grp_"+i);
                        paramsGroups[k][3] = prop.getProperty("onoff_grp_"+i);
                        
                        //se attivo manuale, disabilito scheduler e campo x il gruppo, ma senza variarne il singolo stato:
                        if (prop.getProperty("manual_grp_"+i) != null)
                        {
                            //attivo modalit� manuale x il gruppo:
                            paramsGroups[k][2] = "on";
                            
                            //disabilito scheduler x il gruppo:
                            paramsGroups[k][4] = "off";

                            //disabilito ctrl del campo x il gruppo:
                            paramsGroups[k][5] = "off";
                        }
                        else
                        {
                            paramsGroups[k][2] = "off";
                            
                            paramsGroups[k][4] = prop.getProperty("scheduler_grp_"+i);
                            paramsGroups[k][5] = prop.getProperty("campo_grp_"+i);
                        }
                        
                        paramsGroups[k][6] = prop.getProperty("lcnt_grp_"+i);
                        k++;
                    }
                }
                
                //solo se ho modificato valori, aggiorno la tabella generale dei gruppi:
                if (k > 0)
                {
                    /*
                    //elimino valori precedenti:
                    String sql_del = "delete from ln_groups where idgroup in (select distinct idgroup from ln_devsxgroup)";
                    DatabaseMgr.getInstance().executeStatement(sql_del, null);
                    
                    //inserisco valori aggiornati:
                    String sql_ins = "insert into ln_groups values (?,?,?,?,?,?,?)";
                    */
                    
                    //aggiorno valori dei gruppi modificati:
                    String sql_upd = "update ln_groups set nome_grp=?, manuale=?, onoff=?, scheduler=?, campo=?, lcnt=? where idgroup=?";
                    Object[][] pgrp = new Object[k][7];
                    
                    for (int j = 0; j < k; j++)
                    {
                        pgrp[j][0] = paramsGroups[j][1];
                        pgrp[j][1] = paramsGroups[j][2];
                        pgrp[j][2] = paramsGroups[j][3];
                        pgrp[j][3] = paramsGroups[j][4];
                        pgrp[j][4] = paramsGroups[j][5];
                        pgrp[j][5] = paramsGroups[j][6];
                        pgrp[j][6] = paramsGroups[j][0]; //idgroup
                    }
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_upd, pgrp);
                    
                }
                LightNightMgr.getInstance().reload();
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN51", 
                		LangMgr.getInstance().getLangService(us.getLanguage()).getString("lucinotte", "ln1"));
            }
            else
            if ("save_devsxgrps_status".equals(cmd))
            {
                //recupero num max gruppi:
                int n_max_grps = LNGroups.MAX_GROUPS;
                
                //**********
                //1. sezione gruppi:
                //**********
                //recupero valori x tabella Groups:
                int k = 0;
                
                Object[][] paramsGroups = new Object[n_max_grps][4];
                
                for (int i = 1; i <= n_max_grps; i++)
                {
                    if ((prop.getProperty("name_grp_"+i) != null) && (! "".equals(prop.getProperty("name_grp_"+i))))
                    {
                        paramsGroups[k][0] = new Integer(i); //idgroup
                        paramsGroups[k][1] = prop.getProperty("name_grp_"+i);
                        
                        if (prop.getProperty("onoff_grp_"+i) != null)
                        {
                            paramsGroups[k][2] = "on";
                        }
                        else
                        {
                            paramsGroups[k][2] = "off";
                        }
                        
                        if (prop.getProperty("lcnt_grp_"+i) != null)
                        {
                            paramsGroups[k][3] = "on";
                        }
                        else
                        {
                            paramsGroups[k][3] = "off";
                        }
                        
                        k++;
                    }
                }
                
                //solo se ho modificato valori, aggiorno la tabella generale dei gruppi:
                if (k > 0)
                {
                    //aggiorno valori dei gruppi modificati:
                    String sql_upd = "update ln_groups set nome_grp=?, onoff=?, lcnt=? where idgroup=?";
                    Object[][] pgrp = new Object[k][4];
                    
                    for (int j = 0; j < k; j++)
                    {
                        pgrp[j][0] = paramsGroups[j][1];
                        pgrp[j][1] = paramsGroups[j][2];
                        pgrp[j][2] = paramsGroups[j][3];
                        pgrp[j][3] = paramsGroups[j][0]; //idgroup
                    }
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_upd, pgrp);
                }
                
                //**********
                //2. sezione devs x gruppo:
                //**********
                // recupero id dei devices:
                String ids_devs = prop.getProperty("ids_devs");
                String[] iddevs = ids_devs.split(";");
                int n_devs = iddevs.length;
                
                Object[][] dvxgrp = new Object[n_devs*n_max_grps][2];
                Object[][] dvlcnt = new Object[n_devs][2];
                String devs = "";
                
                k = 0;
                int h = 0;
                
                for (int j = 0; j < iddevs.length; j++) //per ogni dev
                {   
                    for (int w = 1; w <= n_max_grps; w++) //per ogni gruppo
                    {
                        if (prop.getProperty("dev_"+iddevs[j]+"__grp_"+w) != null)
                        {
                            dvxgrp[k][0] = new Integer(iddevs[j]); //iddev
                            dvxgrp[k][1] = new Integer(w); //idgroup
                            
                            k++;
                        }
                    }
                    
                    //solo se il device appartiene ad almeno un gruppo:
                    //if ((prop.getProperty("dev_"+iddevs[j]+"__grp_-1") == null) || (prop.getProperty("lcnt_dev_"+iddevs[j]) != null))
                    if (prop.getProperty("dev_"+iddevs[j]+"__grp_-1") == null)
                    {
                        devs += iddevs[j] + ","; //iddev
                            
                        dvlcnt[h][0] = new Integer(iddevs[j]); //iddev
                        dvlcnt[h][1] = prop.getProperty("lcnt_dev_"+iddevs[j]); //modalit� luci/notte
                        
                        h++;
                    }
                }
                
                if (! "".equals(devs))
                    devs = devs.substring(0, devs.length() - 1);
                
                //elimino associazioni precedenti:
                String sql_del_1 = "delete from ln_devsxgroup";
                
                DatabaseMgr.getInstance().executeStatement(sql_del_1, null);
                
                //String sql_del_2 = "delete from ln_devlcnt where iddev in ("+devs+")";
                String sql_del_2 = "delete from ln_devlcnt";
                
                DatabaseMgr.getInstance().executeStatement(sql_del_2, null);
                
                //inserisco associazioni correnti:
                if (k > 0)
                {
                    String sql_ins_1 = "insert into ln_devsxgroup (iddev, idgroup) values (?,?)";
                    
                    Object[][] devsxgroup = new Object[k][2];
                    
                    for (int i = 0; i < k; i++)
                    {
                        devsxgroup[i][0] = dvxgrp[i][0];
                        devsxgroup[i][1] = dvxgrp[i][1];
                    }
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_ins_1, devsxgroup);
                }
                
                if (h > 0)
                {
                    String sql_ins_2 = "insert into ln_devlcnt (iddev,lcnt) values (?,?)";
                    
                    Object[][] devslcnt = new Object[h][2];
                    
                    for (int i = 0; i < h; i++)
                    {
                        devslcnt[i][0] = dvlcnt[i][0];
                        devslcnt[i][1] = dvlcnt[i][1];
                    }
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_ins_2, devslcnt);
                }
                
                // controllo tabella ln_fieldvars:
                /*
                if (k > 0)
                {
                    String sql_del_field = "delete from ln_fieldvars where idgroup=? and iddev not in " +
                                        " (select iddev from ln_devsxgroup where idgroup=?)";
                    
                    Object[][] pgrp = new Object[n_max_grps][2];
                    
                    for (int z = 1; z < n_max_grps; z++)
                    {
                        pgrp[z][0] = new Integer(z);
                        pgrp[z][1] = new Integer(z);
                    }
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_del_field, pgrp);
                }
                */
                
                //se cambio associazioni gruppi-devs e/o devs-luci/notte:
                if ((k > 0) || (h > 0))
                {
                }
                LightNightMgr.getInstance().reload();
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN51", 
                		LangMgr.getInstance().getLangService(us.getLanguage()).getString("lucinotte", "ln4"));
            }
            else
            if ("save_fieldvars".equals(cmd))
            {
                //recupero num max gruppi:
                int n_max_grps = LNGroups.MAX_GROUPS;
                
                int k = 0;
                
                Object[][] paramsField = new Object[n_max_grps][5];
                
                for (int i = 1; i <= n_max_grps; i++)
                {
                    //se ho scelto sia il dev che una sua var digitale, allora:
                    if ((prop.getProperty("devs_grp_"+i) != null) && (! "-1".equals(prop.getProperty("devs_grp_"+i))))
                    {
                        if ((prop.getProperty("digvar_grp_"+i) != null) && (! "-1".equals(prop.getProperty("digvar_grp_"+i))))
                        {
                            paramsField[k][0] = new Integer(i); //idgroup
                            paramsField[k][1] = new Integer(prop.getProperty("devs_grp_"+i)); //iddevice
                            paramsField[k][2] = new Integer(prop.getProperty("digvar_grp_"+i)); //idvariable
                            paramsField[k][3] = new Integer(prop.getProperty("statoon_grp_"+i));
                            
                            if (prop.getProperty("active_grp_"+i) != null)
                            {
                                paramsField[k][4] = "on";
                            }
                            else
                            {
                                paramsField[k][4] = "off";
                            }
                            
                            k++;
                        }
                    }
                }
                
                
                //elimino associazioni precedenti:
                String sql_del = "delete from ln_fieldvars";
                
                DatabaseMgr.getInstance().executeStatement(sql_del, null);
                
                
                //elimino stato del campo dalle info generali dei gruppi:
                String sql_del2 = "update ln_groups set campo=?";
                
                DatabaseMgr.getInstance().executeStatement(sql_del2, new Object[]{"off"});
                
                
                //inserisco associazioni attuali:
                if (k > 0)
                {
                    String sql_ins = "insert into ln_fieldvars (idgroup, iddev, idvardig, stato_on, attivo) values (?,?,?,?,?)";
                    
                    Object[][] fieldvars = new Object[k][5];
                    Object[][] paramsGroup = new Object[k][2];
                    
                    for (int i = 0; i < k; i++)
                    {
                        fieldvars[i][0] = paramsField[i][0];
                        paramsGroup[i][1] = fieldvars[i][0];
                        
                        fieldvars[i][1] = paramsField[i][1];
                        fieldvars[i][2] = paramsField[i][2];
                        fieldvars[i][3] = paramsField[i][3];
                        
                        fieldvars[i][4] = paramsField[i][4];
                        paramsGroup[i][0] = fieldvars[i][4];
                    }
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_ins, fieldvars);
                    
                    //aggiorno stato gruppi:
                    String sql_upd = "update ln_groups set campo=? where idgroup=?";
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_upd, paramsGroup);
                    
                }
                LightNightMgr.getInstance().reload();
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN51", 
                		LangMgr.getInstance().getLangService(us.getLanguage()).getString("lucinotte", "ln6"));
            }
            else
            if ("save_scheduling".equals(cmd))
            {
                Transaction transaction = us.getTransaction();
                Properties properties = new Properties();
                
                //Integer idgroup = new Integer(prop.getProperty("cmb_grps"));
                Integer timef = new Integer(prop.getProperty("timetype"));
                
            	// per ogni gruppo:
            	for (int ng = 1; ng <= LNGroups.MAX_GROUPS; ng++)
                {
                    //elimino schedulazioni precedenti:
                    String sql_del = "delete from ln_scheduler where idgroup=?";
                    
                    Object[] param = new Object[]{new Integer(ng)};
                    
                    DatabaseMgr.getInstance().executeStatement(null, sql_del, param);
                    
                    //inserisco schedulazioni attuali:
                    String sql_ins = "insert into ln_scheduler (idgroup,day,on_1,off_1,on_2,off_2) values (?,?,?,?,?,?)";
                    
                    Object[][] parSched = new Object[7][6];
                    
                    for (int i = 1; i <= 7; i++)
                    {
                        parSched[i-1][0] = new Integer(ng); //group
                        parSched[i-1][1] = new Integer(i); //day
                        parSched[i-1][2] = new Integer(prop.getProperty("g"+ng+"_day"+i+"_on1"));
                        parSched[i-1][3] = new Integer(prop.getProperty("g"+ng+"_day"+i+"_off1"));
                        parSched[i-1][4] = new Integer(prop.getProperty("g"+ng+"_day"+i+"_on2"));
                        parSched[i-1][5] = new Integer(prop.getProperty("g"+ng+"_day"+i+"_off2"));
                    }
                    
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_ins, parSched);
                    
                    //aggiorno stato gruppo:
                    String sql_upd = "update ln_groups set scheduler=? where idgroup=?";
                    
                    Object[] parGrp = new Object[2];
                    parGrp[1] = new Integer(ng);
                    parGrp[0] = (prop.getProperty("sched_grp"+ng) != null ? "on" : "off"); //active ?
                    
                    DatabaseMgr.getInstance().executeStatement(null, sql_upd, parGrp);
                }
            	
            	LNUtils.setTimeFormat(timef);
                
                //properties.setProperty("idlcntgrp", idgroup.toString());
                properties.setProperty("timef", timef.toString());
                transaction.setSystemParameter(properties);

                LightNightMgr.getInstance().reload();
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN51", 
                		LangMgr.getInstance().getLangService(us.getLanguage()).getString("lucinotte", "ln2"));
            }
            else
            if (cmd.equals("save_daysexcept"))
            {
                int n_dates = Integer.parseInt(prop.getProperty("max_id"));
                int k = 0;
                    
                String sql_del = "delete from ln_exceptdays";
                    
                String sql_ins = "insert into ln_exceptdays (idmonth,idday,description,on_1,off_1,on_2,off_2,idgrp) values (?,?,?,?,?,?,?,?)";
                    
                Object[][] params = new Object[n_dates+1][8];
                  
                for (int i = 0; i <= n_dates; i++)
                {
                    //se non � in lista per la cancellazione:
                    if (prop.getProperty("del_"+i) == null)
                    {
                        params[k][0] = new Integer(prop.getProperty("month_"+i));
                        params[k][1] = new Integer(prop.getProperty("day_"+i));
                        params[k][2] = prop.getProperty("descr_"+i);
                            
                        params[k][3] = new Integer(prop.getProperty("on1_day_"+i));
                        params[k][4] = new Integer(prop.getProperty("off1_day_"+i));
                        params[k][5] = new Integer(prop.getProperty("on2_day_"+i));
                        params[k][6] = new Integer(prop.getProperty("off2_day_"+i));
                            
                        params[k][7] = new Integer(prop.getProperty("grp_"+i));
                        k++;
                    }
                }
                    
                // elimino date precedenti:
                DatabaseMgr.getInstance().executeStatement(sql_del, null);
                    
                if (k > 0)
                {
                    Object[][] p2i = new Object[k][8];
                       
                    for (int j = 0; j < k; j++)
                    {
                        p2i[j][0] = params[j][0];
                        p2i[j][1] = params[j][1];
                        p2i[j][2] = params[j][2];
                            
                        p2i[j][3] = params[j][3];
                        p2i[j][4] = params[j][4];
                        p2i[j][5] = params[j][5];
                        p2i[j][6] = params[j][6];

                        p2i[j][7] = params[j][7];
                    }
                        
                    // salvo nuove date:
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_ins, p2i);
                }
                LightNightMgr.getInstance().reload();
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN51", 
                		LangMgr.getInstance().getLangService(us.getLanguage()).getString("lucinotte", "ln3"));
            }
            else
            if ("save_models".equals(cmd))
            {
                Transaction transaction = us.getTransaction();
                Properties properties = new Properties();
                
                Integer iddevmdl = new Integer(prop.getProperty("cmb_devmdl"));
                
                Object[][] varmdl = new Object[MAX_VAR][4];
                
                int k = 0;
                
                for (int j = 0; j < MAX_VAR; j++)
                {
                    if ((prop.getProperty(VARMODELS[j]) != null) && (Integer.parseInt(prop.getProperty(VARMODELS[j])) != -1 ))
                    {
                        varmdl[k][0] = iddevmdl;
                        varmdl[k][1] = Integer.parseInt(prop.getProperty(VARMODELS[j]));
                        varmdl[k][2] = new Integer(j);
                        
                        if (prop.getProperty(VARMODELS[j]+"_inv") != null)
                            varmdl[k][3] = new Integer(LNField.INVERSE);
                        else
                            varmdl[k][3] = new Integer(LNField.DIRECT);
                        
                        k++;
                    }
                }
                
                String sql_del = "delete from ln_varmdl where iddevmdl=?";
                DatabaseMgr.getInstance().executeStatement(null, sql_del, new Object[]{iddevmdl});
                
                if (k > 0)
                {
                    Object[][] models = new Object[k][4];
                    
                    for (int w = 0; w < k; w++)
                    {
                        models[w][0] = varmdl[w][0]; //devmdl
                        models[w][1] = varmdl[w][1]; //varmdl
                        models[w][2] = varmdl[w][2]; //tipo
                        models[w][3] = varmdl[w][3]; //logica
                    }
                    
                    /*String sql_del = "delete from ln_varmdl where iddevmdl=?";
                    DatabaseMgr.getInstance().executeStatement(null, sql_del, new Object[]{iddevmdl});*/
                    
                    String sql_ins = "insert into ln_varmdl (iddevmdl,idvarmdl,tipo,logica) values (?,?,?,?)";
                    DatabaseMgr.getInstance().executeMultiStatement(null, sql_ins, models);
                }
                
                properties.setProperty("iddevmdl", iddevmdl.toString());
                transaction.setSystemParameter(properties);

                LightNightMgr.getInstance().reload();
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN51", 
                		LangMgr.getInstance().getLangService(us.getLanguage()).getString("lucinotte", "ln5"));
            }
        }
        else
        if (us.getProperty("execmd") != null)
        {
            String execmd = us.getPropertyAndRemove("execmd");
            Integer nrgrp = new Integer(us.getProperty("exegrp"));
            
            if ("go_On".equals(execmd))
            {
                LightNightMgr.getInstance().setActive(nrgrp, true);
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN52", " on");
            }
            else
            if ("go_Off".equals(execmd))
            {
                LightNightMgr.getInstance().setActive(nrgrp, false);
                EventMgr.getInstance().info(1, us.getUserName(), "lucinotte", "LN52", " off");
            }
        }
    }
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        StringBuffer response = new StringBuffer("<response>");
        
        String cmd = prop.getProperty("cmd");
        String lang = us.getLanguage();
        Integer idSite = new Integer(us.getIdSite());
        
        if ("loadDigVars".equals(cmd))
        {
            //caricamento var digitali del device:
            Integer iddevice = new Integer(prop.getProperty("iddevice"));
            
            String sql = "select cfvariable.idvariable, cftableext.description from cfvariable, cftableext where " +
                            " cfvariable.iddevice=? and cfvariable.type in (1) and cftableext.tablename='cfvariable' and " +
                            " cftableext.languagecode=? and cftableext.idsite=? and cftableext.tableid=cfvariable.idvariable " +
                            " and cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null order by cftableext.description";
            
            Object[] params = new Object[]{iddevice, lang, idSite};
            
            RecordSet rs = null;
            Record rc = null;
            
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
            
            if ((rs != null) && (rs.size() > 0))
            {
                for (int i = 0; i < rs.size(); i++)
                {
                    rc = rs.get(i);
                    
                    response.append("<var id='" + rc.get("idvariable").toString()+"'>");
                    response.append("<![CDATA[" + rc.get("description").toString()+"]]>");
                    response.append("</var>");
                }
            }
        }
        else
        if ("loadMdlVars".equals(cmd))
        {
            //caricamento modelli var digitali del devmdl:
            Integer iddevmdl = new Integer(prop.getProperty("iddevmdl"));
            
            RecordSet rs = null;
            Record rc = null;
            
            // 1. se devmdl di device fisico:
            if (iddevmdl.intValue() > 0)
            {
            
                String sql = "select cfvarmdl.idvarmdl, cfvarmdl.readwrite, cftableext.description from cfvarmdl, cftableext where " +
                                " cfvarmdl.iddevmdl=? and cfvarmdl.type in (1) and cftableext.tablename='cfvarmdl' and " +
                                " cftableext.languagecode=? and cftableext.idsite=? and cftableext.tableid=cfvarmdl.idvarmdl " +
                                " order by cftableext.description";
                
                Object[] params = new Object[]{iddevmdl, lang, idSite};
                
                
                
                rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
                
                if ((rs != null) && (rs.size() > 0))
                {
                    for (int i = 0; i < rs.size(); i++)
                    {
                        rc = rs.get(i);
                        
                        response.append("<varmdl id='" + rc.get("idvarmdl").toString()+"'>");
                        response.append("<![CDATA[" + rc.get("description").toString()+"]]>");
                        response.append("<![CDATA[" + rc.get("readwrite").toString()+"]]>");
                        response.append("</varmdl>");
                    }
                }
            }
            else // 2. se device logico:
            {
                //includo solo le var logiche direttamente dipendenti da una var fisica:
                String sql_logic = "select cfvariable.idvarmdl, cfvariable.readwrite, cftableext.description from cfvariable, cftableext where " +
                " cfvariable.iddevice=? and cfvariable.type in (1) and cftableext.tablename='cfvariable' and " +
                " cftableext.languagecode=? and cftableext.idsite=? and cftableext.tableid=cfvariable.idvariable and " +
                " cfvariable.iscancelled='FALSE' and (cfvariable.idvarmdl is not null) and (cfvariable.idhsvariable is not null) " +
                " order by cftableext.description";

                Integer idlogicdev = new Integer(- iddevmdl.intValue());
                
                Object[] params = new Object[]{idlogicdev, lang, idSite};
                
                rs = null;
                rc = null;
                
                rs = DatabaseMgr.getInstance().executeQuery(null, sql_logic, params);
                
                if ((rs != null) && (rs.size() > 0))
                {
                    for (int i = 0; i < rs.size(); i++)
                    {
                        rc = rs.get(i);
                        
                        response.append("<varmdl id='" + rc.get("idvarmdl").toString()+"'>");
                        response.append("<![CDATA[" + rc.get("description").toString()+"]]>");
                        response.append("<![CDATA[" + rc.get("readwrite").toString()+"]]>");
                        response.append("</varmdl>\n");
                    }
                }
            }
            
            //caricamento modelli var scelti x il devmdl:
            String sql_2 = "select * from ln_varmdl where iddevmdl=? order by tipo";
                
            Object[] par_2 = new Object[]{iddevmdl};
                
            rs = DatabaseMgr.getInstance().executeQuery(null, sql_2, par_2);
                
            if ((rs != null) && (rs.size() > 0))
            {
                for (int i = 0; i < rs.size(); i++)
                {
                    rc = rs.get(i);
                        
                    response.append("<varmdldev id='" + rc.get("idvarmdl").toString()+"'>");
                    response.append("<![CDATA[" + rc.get("tipo").toString()+"]]>");
                    response.append("<![CDATA[" + rc.get("logica").toString()+"]]>");
                    response.append("</varmdldev>");
                }
            }
        }
        else
        if ("loadScheduling".equals(cmd))
        {
            //caricamento conf. scheduling del gruppo:
            //Integer idgroup = new Integer(prop.getProperty("idgroup"));
            
            //String sql_1 = "select * from ln_scheduler where idgroup=? order by day";
        	String sql_1 = "select ln_scheduler.*,ln_groups.scheduler from ln_scheduler,ln_groups where " +
        					" ln_groups.idgroup=ln_scheduler.idgroup order by idgroup,day";
            
            RecordSet rs = null;
            Record rc = null;
            
            String group = "";
            String prevgr = "";
            
            //Object[] params = new Object[]{idgroup};
            
            rs = DatabaseMgr.getInstance().executeQuery(null, sql_1, null);
            
            if ((rs != null) && (rs.size() > 0))
            {
                
            	//response.append("<group id='"+idgroup.intValue()+"'>");
                //response.append("</group>");
            	
            	for (int i = 0; i < rs.size(); i++)
                {
                    rc = rs.get(i);
                    
                    group = rc.get("idgroup").toString();
                    
                    if (! group.equals(prevgr) && (! "".equals(prevgr)))
                    {
                    	response.append("</group>");
                    	response.append("<group id='"+group+"'>");
                    	response.append("<sched id='" + rc.get("scheduler").toString()+"'>");
                        response.append("</sched>");
                    }
                    if ("".equals(prevgr))
                    {
                    	response.append("<group id='"+group+"'>");
                    	response.append("<sched id='" + rc.get("scheduler").toString()+"'>");
                        response.append("</sched>");
                    }
                    
                    response.append("<day id='" + rc.get("day").toString()+"'>");
                    response.append("<![CDATA[" + rc.get("on_1").toString()+"]]>");
                    response.append("<![CDATA[" + rc.get("off_1").toString()+"]]>");
                    response.append("<![CDATA[" + rc.get("on_2").toString()+"]]>");
                    response.append("<![CDATA[" + rc.get("off_2").toString()+"]]>");
                    response.append("</day>");
                    
                    prevgr = group;
                }
            	
            	response.append("</group>");
            }
            
            /*
            //caricamento stato dello scheduling (e manuale) del gruppo:
            //String sql_2 = "select scheduler, manuale from ln_groups where idgroup=?";
            String sql_2 = "select scheduler, manuale from ln_groups order by idgroup";
            
            rs = DatabaseMgr.getInstance().executeQuery(null, sql_2, null);
            
            if ((rs != null) && (rs.size() > 0))
            {
                response.append("<sched id='" + rs.get(0).get("scheduler").toString()+"'>");
                response.append("</sched>");
                response.append("<manual id='" + rs.get(0).get("manuale").toString()+"'>");
                response.append("</manual>");
            }
            */
        }

        response.append("</response>");
        
        return response.toString();
    }
}
