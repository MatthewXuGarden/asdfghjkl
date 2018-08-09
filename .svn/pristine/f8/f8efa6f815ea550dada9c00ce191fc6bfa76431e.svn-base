package com.carel.supervisor.director.guardian;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.field.Variable;


public class ValueLogger
{
    private List values = new ArrayList();
    private Integer idVar = null;

    public ValueLogger(Integer idVar)
    {
        this.idVar = idVar;
    }

    public void clear()
    {
        values.clear();
    }

    //	Restituisce il numero di dati uguali
    public boolean isCostant()
    {
        long num = (long) SystemConfMgr.getInstance().get("errorping").getValueNum();

        if (num <= values.size())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Al primo diverso, cancella il vettore
    public void refresh() throws Exception
    {
        //Variable variable = ControllerMgr.getInstance().getFromField(idVar.intValue());
        Variable variable = DirectorMgr.getInstance().getVarGuardian(idVar);

        if (null != variable)
        {
            if ((!variable.isDeviceOffLine()) && (!variable.isDeviceDisabled()))
            {
                values.add(new Float(variable.getCurrentValue()));

                if (1 == values.size())
                {
                    return;
                }

                float val1 = ((Float) values.get(0)).floatValue();
                float val2 = 0;

                for (int i = 1; i < values.size(); i++)
                {
                    val2 = ((Float) values.get(i)).floatValue();

                    if (val2 != val1)
                    {
                        clear();

                        return;
                    }
                }
            }
        }
    }
}
