package com.carel.supervisor.controller.function;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class CalcElementData
{
    private static final String FUNCTIONCODE = "functioncode";
    private static final String OPERTYPE = "opertype";
    private static final String PARAMETERS = "parameters";
    private static final String OPERORDER = "operorder";
    private CalcElementData next = null;
    private String operator = null;
    private String[] variables = null;
    private int id = 0;
    private int functionId = 0;

    public CalcElementData(int functionId, int id, String operator,
        String variables)
    {
        this.id = id;
        this.operator = operator;
        this.functionId = functionId;
        this.variables = variables.split(";");
    }

    public CalcElementData(Record record)
    {
        this.id = ((Integer) record.get(OPERORDER)).intValue();
        this.operator = UtilBean.trim((String) record.get(OPERTYPE));

        String var = UtilBean.trim((String) record.get(PARAMETERS));
        this.variables = var.split(";");
        this.functionId = ((Integer) record.get(FUNCTIONCODE)).intValue();
    }

    /**
         * @return: int
         */
    public int getFunctionId()
    {
        return functionId;
    }

    /**
    * @return: String
    */
    public String getOperator()
    {
        return operator;
    }

    /**
     * @return: String[]
     */
    public String[] getVariables()
    {
        return variables;
    }

    /**
     * @return: ElementData
     */
    public CalcElementData getNext()
    {
        return next;
    }

    public void setNext(CalcElementData next)
    {
        this.next = next;
    }

    public CalcElementData getById(int id)
    {
        CalcElementData temp = next;

        while ((id != temp.id) && (null != temp))
        {
            temp = temp.next;
        }

        return temp;
    }
}
