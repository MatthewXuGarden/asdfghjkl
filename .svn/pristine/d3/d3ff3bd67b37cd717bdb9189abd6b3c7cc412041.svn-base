package com.carel.supervisor.dataaccess.datalog.impl;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class ConditionBean
{
    private static final String FLD_1 = "pvcode";
    private static final String FLD_2 = "idsite";
    private static final String FLD_3 = "idcondition";
    private static final String FLD_4 = "condcode";
    private static final String FLD_5 = "condtype";
    private static final String FLD_6 = "idvariable";
    private static final String FLD_7 = "isgeneral";
    private int idSite = 0;
    private int idCondition = 0;
	//Start alarmP Fixing 2007/06/27
    private int priorityCondition = -1;
    //End
    private String codeSite = "";
    private String codeCondition = "";
    private String typeCondition = "";
    private int idVariableGe = 0;
    private boolean isgeneral = false;
    private List idvariable = null;
    private List lbvariable = null;
    private List lbdevice   = null;

    public ConditionBean(Record r) throws DataBaseException
    {
        this.idSite = ((Integer) r.get(FLD_2)).intValue();
        this.idCondition = ((Integer) r.get(FLD_3)).intValue();
        this.codeSite = UtilBean.trim(r.get(FLD_1));
        this.codeCondition = UtilBean.trim(r.get(FLD_4));
        this.typeCondition = UtilBean.trim(r.get(FLD_5));
        if(r.hasColumn(FLD_6))
        	this.idVariableGe = ((Integer) r.get(FLD_6)).intValue();
        if(r.hasColumn(FLD_7) && UtilBean.trim(r.get(FLD_7)).equalsIgnoreCase("TRUE"))
        {
        	this.isgeneral = true;
        }
        else  if(r.hasColumn(FLD_7) && UtilBean.trim(r.get(FLD_7)).equalsIgnoreCase("FALSE"))
        {
        	this.isgeneral = false;
        }
		//Start alarmP Fixing 2007/06/27
        if(typeCondition.equals("P")) 
            priorityCondition=getPriority(idCondition);
        //End
        idvariable = new ArrayList();
        lbvariable = new ArrayList();
        lbdevice   = new ArrayList();
    }
    
    public int getIdVarGen()
    {
    	return this.idVariableGe;
    }
    
	//Start alarmP Fixing 2007/06/27
    public int getPriority(int idCondition) throws DataBaseException 
    {
        String sql="select idvariable from cfvarcondition, cfcondition where cfvarcondition.idcondition=cfcondition.idcondition and cfcondition.idcondition=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idCondition)});
        if(rs.size()>0)
        {
            return ((Integer)rs.get(0).get("idvariable")).intValue();
        }
        return -1;
    }
    //End
    public String getCodeCondition()
    {
        return codeCondition;
    }

    public String getCodeSite()
    {
        return codeSite;
    }

    public int getIdCondition()
    {
        return idCondition;
    }

    public int getIdSite()
    {
        return idSite;
    }
    public boolean getIsgeneral()
    {
    	return this.isgeneral;
    }
	//Start alarmP Fixing 2007/06/27
    public int getPriorityCondition()
    {
        return priorityCondition;
    }
    //End

    public String getTypeCondition()
    {
        return typeCondition;
    }

    public void addVariable(int id, String desc, String descdev)
    {
        this.idvariable.add(new Integer(id));
        this.lbvariable.add(desc);
        this.lbdevice.add(descdev);
    }

    public String[][] getVariable()
    {
        String[][] ret = new String[this.idvariable.size()][2];

        for (int i = 0; i < ret.length; i++)
        {
            ret[i][0] = ((Integer) this.idvariable.get(i)).toString();
            ret[i][1] = (String) this.lbvariable.get(i);
        }

        return ret;
    }
    public String[][] getData()
    {
        String[][] ret = new String[this.idvariable.size()][3];

        for (int i = 0; i < ret.length; i++)
        {
            ret[i][0] = ((Integer) this.idvariable.get(i)).toString();
            ret[i][1] = (String) this.lbvariable.get(i);
            ret[i][2] = (String)this.lbdevice.get(i);
        }

        return ret;
    } 
    public String getDataForClient()
    {
    	String lbv = "";
        String lbd = "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < this.idvariable.size(); i++)
        {
            if (i != 0)
            {
                sb.append("@@");
            }
            
            lbd = (String)this.lbdevice.get(i);
            lbv = (String)this.lbvariable.get(i);
            if(lbd == null)
            	lbd = "";
            if(lbv == null)
            	lbv = "";
            lbd = Replacer.replace(lbd, "\"", "'");
            lbv = Replacer.replace(lbv, "\"", "'");
            
            sb.append(this.idvariable.get(i) + "$?" + lbd+" -> "+lbv);
        }

        return sb.toString();
    }
}
