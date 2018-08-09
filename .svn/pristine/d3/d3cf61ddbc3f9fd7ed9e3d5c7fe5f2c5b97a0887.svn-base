package com.carel.supervisor.dataaccess.event;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class Category
{
    private static final String CATEGORY = "categorycode";
    private static final String DESCRIPTION = "description";
    
    private String categorycode = null;
    private String category = null;
    
    public Category(Record record)
    {
        categorycode = UtilBean.trim(record.get(CATEGORY));
        category = UtilBean.trim(record.get(DESCRIPTION));
    }

    /**
     * @return: String
     */
    public String getCategorycode()
    {
        return categorycode;
    }

    /**
     * @return: String
     */
    public String getCategory()
    {
        return category;
    }

}
