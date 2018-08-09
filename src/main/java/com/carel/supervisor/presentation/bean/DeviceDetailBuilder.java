package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangSection;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleGroup;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTableFisa;


public class DeviceDetailBuilder
{
    public static final String VIEW = "dtlview";
    List listVarRead = null;
    List listVarWrite = null;
    List listVarButton = null;
    List listVarButtonValue = null;
    List listValueWrite = null;
    List listValueRead = null;
    List listVarWriteAttr = null;
    private String idLang = "";
    private int idDevice = -1;
    private int idSite = -1;
    private LangSection translator = null;
    private UserSession session = null;

    public DeviceDetailBuilder(UserSession userSession)
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
        this.translator = LangMgr.getInstance().getLangService(idLang).getSection(VIEW);
        this.session = userSession;
        listVarRead = new ArrayList();
        listVarWrite = new ArrayList();
        listVarButton = new ArrayList();
        listVarButtonValue = new ArrayList();
        listValueWrite = new ArrayList();
        listValueRead = new ArrayList();
        listVarWriteAttr = new ArrayList();
    }

    /*
    public void loadVarphyToDisplay() throws Exception
    {
        String rwStatus = null;

        int[] iArDevice = { this.idDevice };
        VarphyBean[] listVarphy1 = VarphyBeanList.getListVarToDisplay(this.idLang, this.idSite,
                iArDevice);
        VarphyBean[] listVarphy2 = VarphyBeanList.getMainListVarToDisplay(this.idLang, this.idSite,
                iArDevice);
        int dim = listVarphy1.length + listVarphy2.length;
        VarphyBean[] listVarphy = new VarphyBean[dim];
        int i = 0;

        // carico le variabili con HOME
        for (i = 0; i < listVarphy1.length; i++)
        {
            listVarphy[i] = listVarphy1[i];
        }

        // carico le variabili con MAIN
        for (int j = 0; j < listVarphy2.length; j++)
        {
            listVarphy[i] = listVarphy2[j];
            i = i + 1;
        }

        if (listVarphy != null)
        {
            for (i = 0; i < listVarphy.length; i++)
            {
                rwStatus = null;

                if (listVarphy[i] != null)
                {
                    rwStatus = listVarphy[i].getReadwrite();
                }

                if (rwStatus != null)
                {
                    rwStatus = rwStatus.trim();

                    if (rwStatus.equalsIgnoreCase("1")) //Variabili sola lettura, le faccio vedere sempre
                    {
                        listVarRead.add(listVarphy[i]);

                        if (!BaseConfig.isRemote())
                        {
                            try
                            {
                                listValueRead.add(ControllerMgr.getInstance()
                                                               .getFromField(listVarphy[i])
                                                               .getFormattedValue());
                            }
                            catch (Exception e)
                            {
                                listValueRead.add("***");
                            }
                        }
                        else
                        {
                            listValueRead.add("***");
                        }
                    }

                    //Variabili scrittura servizio
                    else if (rwStatus.equalsIgnoreCase("3") || rwStatus.equalsIgnoreCase("7"))
                    {
                        if ((listVarphy[i].getButtonpath() != null) &&
                                (!listVarphy[i].getButtonpath().equalsIgnoreCase("")))
                        {
                            if (ProfileBean.PERMISSION_READ_WRITE == session.getPermission(
                                        ProfileBean.FUNCT_SERV_PARAM))
                            {
                                listVarButton.add(listVarphy[i]);

                                // valori online dell variabili "button"
                                if (!BaseConfig.isRemote())
                                {
                                    try
                                    {
                                        listVarButtonValue.add(ControllerMgr.getInstance()
                                                                            .getFromField(listVarphy[i])
                                                                            .getFormattedValue());
                                    }
                                    catch (Exception e)
                                    {
                                        listVarButtonValue.add("***");
                                    }
                                }
                                else
                                {
                                    listVarButtonValue.add("***");
                                }
                            }
                        }
                        else
                        {
                            listVarWrite.add(listVarphy[i]);

                            if (!BaseConfig.isRemote())
                            {
                                try
                                {
                                    listValueWrite.add(ControllerMgr.getInstance()
                                                                    .getFromField(listVarphy[i])
                                                                    .getFormattedValue());
                                }
                                catch (Exception e)
                                {
                                    listValueWrite.add("***");
                                }
                            }
                            else
                            {
                                listValueWrite.add("***");
                            }

                            if (ProfileBean.PERMISSION_READ_WRITE == session.getPermission(
                                        ProfileBean.FUNCT_SERV_PARAM))
                            {
                                listVarWriteAttr.add(new Boolean(true));
                            }
                            else
                            {
                                listVarWriteAttr.add(new Boolean(false));
                            }
                        }
                    }

                    // Variabili scrittura costruttore
                    else if (rwStatus.equalsIgnoreCase("11"))
                    {
                        if (ProfileBean.PERMISSION_READ_WRITE == session.getPermission(
                                    ProfileBean.FUNCT_CONSTR_PARAM))
                        {
                            if ((listVarphy[i].getButtonpath() != null) &&
                                    (!listVarphy[i].getButtonpath().equalsIgnoreCase("")))
                            {
                                listVarButton.add(listVarphy[i]);

                                // valori online dell variabili "button"
                                if (!BaseConfig.isRemote())
                                {
                                    try
                                    {
                                        listVarButtonValue.add(ControllerMgr.getInstance()
                                                                            .getFromField(listVarphy[i])
                                                                            .getFormattedValue());
                                    }
                                    catch (Exception e)
                                    {
                                        listVarButtonValue.add("***");
                                    }
                                }
                                else
                                {
                                    listVarButtonValue.add("***");
                                }
                            }
                            else
                            {
                                listVarWrite.add(listVarphy[i]);

                                if (!BaseConfig.isRemote())
                                {
                                    try
                                    {
                                        listValueWrite.add(ControllerMgr.getInstance()
                                                                        .getFromField(listVarphy[i])
                                                                        .getFormattedValue());
                                    }
                                    catch (Exception e)
                                    {
                                        listValueWrite.add("***");
                                    }
                                }
                                else
                                {
                                    listValueWrite.add("***");
                                }

                                listVarWriteAttr.add(new Boolean(true));
                            }
                        }
                    }
                }
            }
        }
    }
    */
    /*
    public int getNumButton()
    {
        return this.listVarButton.size();
    }
    */
    
    /*
    public int getIdVarButton(int index)
    {
        return ((VarphyBean) this.listVarButton.get(index)).getId().intValue();
    }
    */
    
    public int getIdVar(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getId().intValue();
    }

    public int getTypeVar(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getType();
    }
    
    public int getDecimal(int index)
    {
        return ((VarphyBean) listVarWrite.get(index)).getDecimal();
    }

    public String getUMRead(int index)
    {
        String ret = ((VarphyBean) listVarRead.get(index)).getMeasureUnit();

        return (ret != null) ? ret : "";
    }

    public String getUMWrite(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getMeasureUnit();

        return (ret != null) ? ret : "";
    }

    public String getShortDescRead(int index)
    {
        String ret = ((VarphyBean) listVarRead.get(index)).getShortDescription();

        return (ret != null) ? ret : "";
    }

    public String getShortDescWrite(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getShortDescription();

        return (ret != null) ? ret : "";
    }

    public String getShortDescButton(int index)
    {
        String ret = ((VarphyBean) listVarButton.get(index)).getShortDescription();

        return (ret != null) ? ret : "";
    }

    public String getImageButtonPath(int index)
    {
        String ret = ((VarphyBean) listVarButton.get(index)).getButtonpath();

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
    
    public String getCode(int index)
    {
        String ret = ((VarphyBean) listVarWrite.get(index)).getShortDesc();

        return (ret != null) ? ret : "";
    }
    
    public String buildHtmlFisa(String tabName, String language)
    {
        String groupCode = "";
        List groupList = new ArrayList();
        HTMLSimpleGroup simpleGroup = null;
        HTMLSimpleElement[] simpleEle = null;
        boolean firstTime = true;
        DeviceStructureList devStruncList = session.getGroup().getDeviceStructureList();
        int idVar = -1;

        try
        {
            loadVarphyToWrite();
        }
        catch (Exception e)
        {
        }

        for (int i = 0; i < listVarWrite.size(); i++)
        {
            if (!getGrpCodeWrite(i).equalsIgnoreCase(groupCode))
            {
                if (firstTime)
                {
                    firstTime = false;
                }
                else
                {
                    groupList.add(simpleGroup);
                }

                groupCode = getGrpCodeWrite(i);

                String tmp = devStruncList.getVarGroups(new Integer(groupCode));
                simpleGroup = new HTMLSimpleGroup(tmp, "<b>" + tmp + "</b>", 5);
            }

            idVar = getIdVar(i);

            int typeVar = getTypeVar(i);
            int decimals = getDecimal(i);
            String curVal = null;

            try
            {
                curVal = ControllerMgr.getInstance().getFromField(idVar).getFormattedValue();
            }
            catch (Exception e)
            {
                curVal = "***";
            }

            simpleEle = new HTMLSimpleElement[5];
            simpleEle[0] = new HTMLSimpleElement(curVal);
            simpleEle[1] = new HTMLSimpleElement(buildInputType(idVar, typeVar, i,decimals));
            simpleEle[2] = new HTMLSimpleElement(getUMWrite(i));
            simpleEle[3] = new HTMLSimpleElement(getCode(i));
            simpleEle[4] = new HTMLSimpleElement(getShortDescWrite(i));

            simpleGroup.addRow(simpleEle);
        }

        // Last group
        groupList.add(simpleGroup);

        HTMLSimpleGroup[] sgList = new HTMLSimpleGroup[groupList.size()];

        for (int i = 0; i < sgList.length; i++)
        {
            sgList[i] = (HTMLSimpleGroup) groupList.get(i);
        }

        String[] colName = new String[5];
        colName[0] = this.translator.get("col1");
        colName[1] = this.translator.get("col2");
        colName[2] = this.translator.get("col3");
        colName[3] = this.translator.get("col5");
        colName[4] = this.translator.get("col4");

        HTMLTableFisa tf = new HTMLTableFisa("DtlPrmFisa", 1, 905, 300, colName, sgList,
                new int[] { 55, 55, 60, 112, 545 }, language);
        tf.setTableTitle(tabName);

        return tf.getHTMLText();
    }

    public String getNameTable(UserSession sessionUser, int idDev)
    {
        String ret = "";

        try
        {
            DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
            DeviceStructure deviceStructure = deviceStructureList.get(idDev);
            ret = deviceStructure.getDescription();
        }
        catch (Exception e)
        {
        }

        return ret;
    }

    private void loadVarphyToWrite() throws Exception
    {
        String rwState = null;
        int[] iArDevice = { this.idDevice };
        VarphyBean[] listVarphy = VarphyBeanList.getListVarWritable(this.idLang, this.idSite,
                iArDevice);

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

                //Variabili scrittura servizio
                if (rwState.equalsIgnoreCase("3") || rwState.equalsIgnoreCase("7"))
                {
                    listVarWrite.add(listVarphy[i]);

                    if (session.getVariableFilter()==ProfileBean.FILTER_SERVICES || session.getVariableFilter()==ProfileBean.FILTER_SERVICES)
                    {
                        listVarWriteAttr.add(new Boolean(true));
                    }
                    else
                    {
                        listVarWriteAttr.add(new Boolean(false));
                    }
                }

                // Variabili scrittura costruttore
                else if (rwState.equalsIgnoreCase("11"))
                {
                    if (session.getVariableFilter()==ProfileBean.FILTER_SERVICES)
                    {
                        listVarWrite.add(listVarphy[i]);
                        listVarWriteAttr.add(new Boolean(true));
                    }
                }
            }
        }
    }

    private String buildInputType(int idVar, int type, int pos,int decimals)
    {
        StringBuffer sb = new StringBuffer();
        String protec = "";

        if (((Boolean) listVarWriteAttr.get(pos)).booleanValue())
        {
            if (type == 1) //digitale
            {
                sb.append("<input " + protec +
                    " class='lswtype' type='text' size='4' name='dtlst_" + idVar + "' id='dtlst_" +
                    idVar +
                    "' onkeydown='checkOnlyDigit(this,event);' onblur='checkOnlyDigitOnBlur(this);'/>");
            }
            else if (decimals>0) //analogica o intera con decimal!=0
            {
                sb.append("<input " + protec +
                    " class='lswtype' type='text' size='4' name='dtlst_" + idVar + "' id='dtlst_" +
                    idVar +
                    "' onkeydown='checkOnlyAnalog(this,event);' onblur='checkOnlyAnalogOnBlur(this);'/>");
            }
            else //intera
            {
                sb.append("<input " + protec +
                    " class='lswtype' type='text' size='4' name='dtlst_" + idVar + "' id='dtlst_" +
                    idVar +
                    "' onkeydown='checkOnlyNumber(this,event);' onblur='checkOnlyAnalogOnBlur(this);'/>");
            }
        }

        return sb.toString();
    }
    
    /*
    public String buildTableOne()
    {
        HTMLTable deviceTable = new HTMLTable("dtltbuno", getHeaderTableOne(this.idLang),
                getDataTableOne(), false, false);
        deviceTable.setWidth(680);
        deviceTable.setHeight(123);
        deviceTable.setColumnSize(0, 560);
        deviceTable.setColumnSize(1, 76);

        //deviceTable.setColumnSize(2, 60);
        return deviceTable.getHTMLText();
    }
    */
    
    /*
    public String buildTableTwo()
    {
        HTMLTable deviceTable2 = new HTMLTable("dtltbdue", getHeaderTableTwo(this.idLang),
                getDataTableTwo(), false, false);
        deviceTable2.setWidth(680);
        deviceTable2.setHeight(123);
        deviceTable2.setColumnSize(0, 500);
        deviceTable2.setColumnSize(1, 100);
        deviceTable2.setColumnSize(2, 60);

        //deviceTable2.setColumnSize(3, 60);
        deviceTable2.setTableId(2);

        //deviceTable2.setRowSelectColor(false);
        return deviceTable2.getHTMLText();
    }
    */
    
    /*
    private String[] getHeaderTableOne(String lang)
    {
        LangService temp = LangMgr.getInstance().getLangService(lang);
        String[] headOne = new String[2];
        headOne[0] = temp.getString("dtlview", "detaildevicecol3");
        headOne[1] = temp.getString("dtlview", "detaildevicecol0");

        // headOne[2] = temp.getString("dtlview", "detaildevicecol2"); -- eliminazione per variazione specifiche 
        return headOne;
    }
    */
    
    /*
    private String[] getHeaderTableTwo(String lang)
    {
        LangService temp = LangMgr.getInstance().getLangService(lang);
        String[] headOne = new String[3];
        headOne[0] = temp.getString("dtlview", "detaildevicecol3");
        headOne[1] = temp.getString("dtlview", "detaildevicecol1");
        headOne[2] = temp.getString("dtlview", "detaildevicecol0");

        // headOne[3] = temp.getString("dtlview", "detaildevicecol2"); -- eliminazione per variazione specifiche 
        return headOne;
    }
    */
    
    /*
    private HTMLElement[][] getDataTableOne()
    {
        HTMLElement[][] data = new HTMLSimpleElement[this.listVarRead.size()][2];
        int dim = data.length;

        for (int i = 0; i < dim; i++)
        {
            if (this.getUMRead(i).length() > 0)
            {
                data[i][0] = new HTMLSimpleElement(this.getShortDescRead(i) + " [" +
                        this.getUMRead(i) + "]");
            }
            else
            {
                data[i][0] = new HTMLSimpleElement(this.getShortDescRead(i));
            }

            data[i][1] = new HTMLSimpleElement((String) listValueRead.get(i));

            //data[i][2] = new HTMLSimpleElement(this.getUMRead(i)); -- eliminazione per variazione specifiche 
        }

        return data;
    }
    */
    
    /*
    private HTMLElement[][] getDataTableTwo()
    {
        HTMLElement[][] data = new HTMLSimpleElement[listVarWrite.size()][3];

        for (int i = 0; i < data.length; i++)
        {
            if (this.getUMWrite(i).length() > 0)
            {
                data[i][0] = new HTMLSimpleElement(this.getShortDescWrite(i) + " [" +
                        this.getUMWrite(i) + "]");
            }
            else
            {
                data[i][0] = new HTMLSimpleElement(this.getShortDescWrite(i));
            }

            data[i][1] = new HTMLSimpleElement(buildInput(i));
            data[i][2] = new HTMLSimpleElement((String) listValueWrite.get(i));

            // data[i][3] = new HTMLSimpleElement(this.getUMWrite(i)); -- eliminazione per variazione specifiche 
        }

        return data;
    }
    */
    
    /*
    private String buildInput(int i)
    {
        int idxvar = getIdVar(i);
        int typeVar = getTypeVar(i);
        String input = "";
        String protec = "";

        if (((Boolean) listVarWriteAttr.get(i)).booleanValue())
        {
            if (typeVar == 1) //input x variabile digitale
            {
                input = "<input type='text' " + protec + " class='lswtype' name='dtlst_" + idxvar +
                    "' id='dtlst_" + idxvar +
                    "' value='' onkeydown='checkOnlyDigit(this,event);' onblur='checkOnlyDigitOnBlur(this);'/>";
            }
            else if (typeVar == 2) //input x variabile analogica
            {
                input = "<input type='text' " + protec + " class='lswtype' name='dtlst_" + idxvar +
                    "' id='dtlst_" + idxvar +
                    "' value='' onkeydown='checkOnlyAnalog(this,event);' onblur='checkOnlyAnalogOnBlur(this);'/>";
            }
            else //input variabile intera
            {
                input = "<input type='text' " + protec + " class='lswtype' name='dtlst_" + idxvar +
                    "' id='dtlst_" + idxvar +
                    "' value='' onkeydown='checkOnlyNumber(this,event);' onblur='checkOnlyAnalogOnBlur(this);'/>";
            }
        }

        return input;
    }
    */
    /*
    public String getButtonValue(int index)
    {
        return (String) listVarButtonValue.get(index);
    }
    */

    /*
    public String buildTableOneRefresh()
    {
        HTMLTable deviceTable = new HTMLTable("dtltbuno", getHeaderTableOne(this.idLang),
                getDataTableOne(), false, false);
        deviceTable.setWidth(680);
        deviceTable.setHeight(123);
        deviceTable.setColumnSize(0, 560);
        deviceTable.setColumnSize(1, 76);

        //deviceTable.setColumnSize(2, 60);
        return deviceTable.getHTMLTextBufferRefresh().toString();
    } //buildTableOneRefresh
    */
    /*
    public String buildTableTwoRefresh()
    {
        HTMLTable deviceTable2 = new HTMLTable("dtltbdue", getHeaderTableTwo(this.idLang),
                getDataTableTwo(), false, false);
        deviceTable2.setWidth(680);
        deviceTable2.setHeight(123);
        deviceTable2.setColumnSize(0, 500);
        deviceTable2.setColumnSize(1, 100);
        deviceTable2.setColumnSize(2, 60);

        //deviceTable2.setColumnSize(3, 60);
        deviceTable2.setTableId(2);

        //deviceTable2.setRowSelectColor(false);
        return deviceTable2.getHTMLTextBufferRefresh().toString();
    } //buildTableTwoRefresh
    */
}
