package com.carel.supervisor.presentation.devices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.UseSelFSRecord;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLNullSizeElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.script.EnumerationMgr;


public class DeviceList
{
    private int numDevice4Page = 20;
    private List devices = null;
    private int fixedColumns = 3;
    //private int phantomColums = 2;
    private int width = 0;
    private int height = 0;
    private int pageNumber = 1;
    private String title = "";
    private int screenw = 1024;
    private int screenh = 768;
    private int pageTotal=1;
    private int maxcols = 3;
    
    public DeviceList(UserSession userSession,String title,boolean getAllDevices4Print)
    throws Exception
	{
    	this.title = title;
        int[] ids = userSession.getTransaction().getIdDevices();
        devices = new ArrayList();
        if ((ids != null) && (ids.length > 0))
        {
    		String mgv2d = userSession.getProperty("maxgrpvars2disp"); 
            if (mgv2d != null)
            {
            	maxcols = Integer.parseInt(mgv2d);
            }
            else
            {
            	maxcols = getMaxCols(ids, userSession.getIdSite());
        		userSession.setProperty("maxgrpvars2disp", ""+maxcols+"");
            }
            if (ids == null)
            {
                return;
            }
            Device device = null;
            List list = null;
            DeviceStructureList deviceStructureList = userSession.getGroup().getDeviceStructureList();
            DeviceStructure deviceStructure = null;
            VarphyBean[] varphyBean = null;

            Map mapDevFisic = new HashMap();
            Map mapDevLogic = new HashMap();

            String[] tmp = new String[ids.length];

            for (int i = 0; i < ids.length; i++)
            {
                list = new ArrayList();
                deviceStructure = deviceStructureList.get(ids[i]);

                varphyBean = deviceStructure.getVariables(); //vars to display
                
                if (varphyBean.length > maxcols)
                	maxcols = varphyBean.length;

                for (int j = 0; j < varphyBean.length; j++)
                {
                    list.add(retrieveValue(varphyBean[j],userSession.getLanguage()));
                }

                tmp[i] = deviceStructure.getCode();
                device = new Device(ids[i], deviceStructure.getCode(),
                        deviceStructure.getDescription(),
                        deviceStructure.getGroupName(), 0, list);

                // Prima i device fisici e poi quelli logici. 
                if (deviceStructure.isLogic())
                {
                    mapDevLogic.put(tmp[i], device);
                }
                else
                {
                    mapDevFisic.put(tmp[i], device);
                }
            }
            
            Arrays.sort(tmp);

            Device temporaneo = null;

            for (int i = 0; i < tmp.length; i++)
            {
                temporaneo = (Device) mapDevFisic.get(tmp[i]);

                if (temporaneo != null)
                {
                    devices.add(temporaneo);
                }
            }

            temporaneo = null;

            for (int i = 0; i < tmp.length; i++)
            {
                temporaneo = (Device) mapDevLogic.get(tmp[i]);

                if (temporaneo != null)
                {
                    devices.add(temporaneo);
                }
            }
        }
	    
	}
    
    public DeviceList(UserSession userSession, int numPage)
        throws Exception
    {
        this.title = "";
        this.pageNumber = numPage;
        this.numDevice4Page = (int) SystemConfMgr.getInstance()
                                                 .get("listdevicesize")
                                                 .getValueNum();

        int[] ids = userSession.getTransaction().getIdDevices();
        
        this.pageTotal=(ids.length/numDevice4Page)+1;
        devices = new ArrayList();

        if ((ids != null) && (ids.length > 0))
        {

    		String mgv2d = userSession.getProperty("maxgrpvars2disp"); 
            if (mgv2d != null)
            {
            	maxcols = Integer.parseInt(mgv2d);
            }
            else
            {
            	maxcols = getMaxCols(ids, userSession.getIdSite());
        		userSession.setProperty("maxgrpvars2disp", ""+maxcols+"");
            }
        	
        	ids = pageDevice(ids);

            if (ids == null)
            {
                return;
            }

            Device device = null;
            List list = null;
            DeviceStructureList deviceStructureList = userSession.getGroup()
                                                                 .getDeviceStructureList();
            DeviceStructure deviceStructure = null;
            VarphyBean[] varphyBean = null;

            ArrayList<String> codeFisDev = new ArrayList<String>();
            ArrayList<String> codeLogDev = new ArrayList<String>();

            String[] tmp = new String[ids.length];
            HashMap<String, Device> codesId = new HashMap<String, Device>();

            for (int i = 0; i < ids.length; i++)
            {
                list = new ArrayList();
                deviceStructure = deviceStructureList.get(ids[i]);

                varphyBean = deviceStructure.getVariables(); //vars to display
                
                if (varphyBean.length > maxcols)
                	maxcols = varphyBean.length;

                for (int j = 0; j < varphyBean.length; j++)
                {
                    list.add(retrieveValue(varphyBean[j],userSession.getLanguage()));
                }
                
                device = new Device(ids[i], deviceStructure.getCode(),
                        deviceStructure.getDescription(),
                        deviceStructure.getGroupName(), 0, list);

                // Prima i device fisici e poi quelli logici. 
                if (deviceStructure.isLogic())
                {
                	codesId.put(deviceStructure.getCode(), device);
                	codeLogDev.add(deviceStructure.getCode());
                }
                else
                {
                	String code = deviceStructure.getCode();
                    if(code != null && code.indexOf(".")>0)
                    {
    	                int line = Integer.valueOf(code.substring(0,code.indexOf(".")));
    	                String lineStr = String.format("%05d", line);
    	                code = lineStr + code.substring(code.indexOf("."),code.length());
                    }
                    codesId.put(code, device);
                    codeFisDev.add(code);
                }
            }
            
            String[] sCodeF = new String[codeFisDev.size()];
            for (int i = 0; i < sCodeF.length; i++)
            	sCodeF[i] = (String) codeFisDev.get(i);
            Arrays.sort(sCodeF);
            String[] sCodeL = new String[codeLogDev.size()];
            for (int i = 0; i < sCodeL.length; i++)
                sCodeL[i] = (String) codeLogDev.get(i);
            Arrays.sort(sCodeL);

            Device temporaneo = null;

            for (int i = 0; i < sCodeF.length; i++)
            {
                temporaneo = (Device) codesId.get(sCodeF[i]);

                if (temporaneo != null)
                {
                    devices.add(temporaneo);
                }
            }

            temporaneo = null;

            for (int i = 0; i < sCodeL.length; i++)
            {
                temporaneo = (Device) codesId.get(sCodeL[i]);

                if (temporaneo != null)
                {
                    devices.add(temporaneo);
                }
            }
        }
    }
    
    private int getMaxCols(int[] iddevs, int idsite)
    {
    	int maxcols2disp = fixedColumns;

    	String sql = "select count(idvariable) as numvars from cfvariable where iscancelled='FALSE' and idsite="+
    				idsite+" and toDisplay='HOME' and readwrite='1' and type<>4 and idhsvariable is not null and iddevice in (";
    	
    	int i = 0;
    	for (i = 0; i < iddevs.length - 1; i++)
    		sql = sql + iddevs[i] + ",";
    	
    	sql = sql + iddevs[i] + ") group by iddevice order by numvars DESC";
    	
    	try
    	{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			maxcols2disp = Integer.parseInt(rs.get(0).get("numvars").toString());
		}
    	catch (Exception e)
    	{
			//PVPro-generated catch block
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
    	
    	return maxcols2disp;
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }


    public DeviceList(UserSession userSession, String title)
        throws Exception
    {
        this(userSession, 1);
        this.title = title;
    }

    private static String retrieveValue(VarphyBean varphyBean,String lang)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<div style='float:left'>");
        buffer.append(varphyBean.getShortDescription());
        buffer.append("</div><div style='float:right'>");
        String label = "";
        Float val = null;
        if (DeviceStatusMgr.getInstance().isOffLineDevice(varphyBean.getDevice()))
        {
            buffer.append("***");
        }
        else
        {
            try
            {
            	val = ControllerMgr.getInstance()
                .getFromField(varphyBean).getCurrentValue();
            	
            	label = EnumerationMgr.getInstance().getEnumCode(varphyBean.getIdMdl(), val,lang); 
            	if (!"".equals(label))
            	{
            		buffer.append(label);
            	}
            	else
            		buffer.append(ControllerMgr.getInstance()
                                           .getFromField(varphyBean)
                                           .getFormattedValue());
            }
            catch (Exception e)
            {
                buffer.append("***");
            }
        }

        buffer.append("");
        buffer.append(" ");
        buffer.append(varphyBean.getMeasureUnit());
        buffer.append("</div>");

        return buffer.toString();
    }

    public List getDevicesList()
    {
        return devices;
    }

    public void addDevice(Device a)
    {
        devices.add(a);
    }

    public void removeDevice(int i)
    {
        devices.remove(i);
    }

    public int sizeDevices()
    {
        return devices.size();
    }

    public Device getDevice(int i)
    {
        return (Device) devices.get(i);
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    private int getTotColumns()
    {
        int addColumns = 0;

        for (int i = 0; i < devices.size(); i++)
        {
            if (((Device) devices.get(i)).extraColumns() > addColumns)
            {
                addColumns = ((Device) devices.get(i)).extraColumns();
            }
        }

        return (fixedColumns + addColumns);
    }

    private int getAdditionalColumns()
    {
        int addColumns = 0;

        for (int i = 0; i < devices.size(); i++)
        {
            if (((Device) devices.get(i)).extraColumns() > addColumns)
            {
                addColumns = ((Device) devices.get(i)).extraColumns();
            }
        }

        return addColumns;
    }

    private String[] getHeaderDeviceTable(String LanguageUsed, boolean group_info)
    {
        LangService temp = LangMgr.getInstance().getLangService(LanguageUsed);
        String[] intestazione = null;
        int group_col = group_info ? 1 : 0;
        int y = 0;

        intestazione = new String[fixedColumns + maxcols + group_col];

        intestazione[y++] = temp.getString("deviceTable", "state");

        intestazione[y++] = temp.getString("deviceTable", "description");

        //for (int i = 0; i < getAdditionalColumns(); i++)
    	for (int i = 0; i < maxcols; i++)
        {
            intestazione[y++] = temp.getString("deviceTable", "variable") + (i+1);
        }

        if (group_info)
        {
            intestazione[y++] = temp.getString("deviceTable", "group");
        }

        intestazione[y] = temp.getString("deviceTable", "id");

        return intestazione;
    }
    public String getAllDevices4Print(String language){
    	StringBuffer sb = new StringBuffer();
    	sb.append("<mytitle>"+ "<![CDATA[" + this.title  + "]]>" + "</mytitle>");
    	String pv_type = BaseConfig.getProductInfo("type");
        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
            ? true : false;
	    String header[] = getHeaderDeviceTable(language, group_info); 
	    sb.append("<ths>");
		for(int k=0;k<header.length;k++){
			sb.append("<th>"+"<![CDATA[" +header[k]+"]]>" +"</th>");
		}
		sb.append("</ths>");
    	for(int i=0;i<devices.size();i++ ){
    		sb.append("<tr>");
    		Device d = (Device)devices.get(i);
    		sb.append("<td >"+"<![CDATA[" +getLed(d.getId())+"]]>" +"</td>");
    		sb.append("<td>"+"<![CDATA[" +d.getDescription()+"]]>" +"</td>");
    		for (int j=0;j<maxcols;j++){
    			if(j < d.extraColumns())
    				sb.append("<td>"+"<![CDATA[" +d.getVariable(j) +"]]>" +"</td>");
    			else
    				sb.append("<td>"+" "+"</td>");
    		}
    		if(d.getId() == 1)
    			sb.append("<td>"+"<![CDATA[]]>" +"</td>");
    		else
    			sb.append("<td>"+"<![CDATA[" +d.getCode()+"]]>" +"</td>");
    		sb.append("</tr>");
    	}
    	return sb.toString();
    	
    }
    private HTMLElement[][] getDeviceTable(boolean group_info)
    {
        int rows = devices.size();
        HTMLElement[][] tableData = null;
        int group_col = group_info ? 1 : 0;
        int y = 0;

        tableData = new HTMLElement[rows][];

        for (int i = 0; i < rows; i++)
        {
            Device dvc = ((Device) devices.get(i));
            y = 0;

            tableData[i] = new HTMLElement[fixedColumns + maxcols + group_col];

            tableData[i][y] = new HTMLNullSizeElement(getLed(dvc.getId()));
            tableData[i][y++].addAttribute("align", "center");

            tableData[i][y++] = new HTMLSimpleElement(dvc.getDescription());

			//for (int j = 0; j < getAdditionalColumns(); j++)
        	for (int j = 0; j < maxcols; j++)
			{
				if(j < dvc.extraColumns())
					tableData[i][y++] = new HTMLSimpleElement(dvc.getVariable(j));
				else
					tableData[i][y++] = new HTMLSimpleElement("");
			}

            if (group_info)
            {
                tableData[i][y++] = new HTMLSimpleElement(dvc.getGroup());
            }

            tableData[i][y] = new HTMLSimpleElement( dvc.getCode().equalsIgnoreCase("-1.000")?"":dvc.getCode() );
        }

        return tableData;
    }

    private HTMLTable getDeviceTablePrv(String tableName, String language, UserSession us)
    {
        String pv_type = BaseConfig.getProductInfo("type");
        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
            ? true : false;

        HTMLTable deviceTable = new HTMLTable(tableName,
                getHeaderDeviceTable(language, group_info),
                getDeviceTable(group_info), true, true);
        
        deviceTable.setScreenH(this.screenh);
        deviceTable.setScreenW(this.screenw);
        
        deviceTable.setHeight(height);
        deviceTable.setWidth(width);
        deviceTable.setPage(true);
        deviceTable.setPageNumber(pageNumber);
        //int cols = deviceTable.getColumns();
        int curcol = 0;

        deviceTable.setColumnSize(curcol++, 75);
        deviceTable.setColumnSize(curcol++, 300);
//        while(curcol < cols-1)
//        	deviceTable.setColumnSize(curcol++, 155);        	
//        deviceTable.setColumnSize(2, 155);
//        deviceTable.setColumnSize(3, 155);
        deviceTable.setTableTitle(this.title);
        deviceTable.setTableId(9);

        if (group_info)
        {
            deviceTable.setColumnSize(4, 58);
            deviceTable.setAlignType(4, HTMLTable.CENTER);
            deviceTable.setColumnSize(3, 155);
        	deviceTable.setColumnSize(1, 276);
            deviceTable.setAlignType(4, HTMLTable.CENTER);
        }
        String[] dblclick = new String[sizeDevices()];

        for (int i = 0; i < sizeDevices(); i++)
        {
            dblclick[i] = new String(String.valueOf(
                        ((Device) devices.get(i)).getId()));
        }
        
        deviceTable.setPageTotal(this.pageTotal);
        if (us.isMenuActive("dtlview"))
        {
	        deviceTable.setDbClickRowAction(
	            "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev=$1&desc=ncode01');");
	        deviceTable.setDlbClickRowFunction(dblclick);
        }
        return deviceTable;
    }

    public String getHTMLDeviceTable(String tableName, String language, UserSession us)
    {
        StringBuffer buffer = new StringBuffer(getDeviceTablePrv(tableName,
                    language,us).getHTMLText());

        return buffer.toString();
    }

    public String getHTMLDeviceTableRefresh(String tableName, String language,UserSession us)
    {
        return getDeviceTablePrv(tableName, language,us).getHTMLTextBufferRefresh()
                   .toString();
    }

    private String getLed(Integer idDev)
    {
        StringBuffer buffer = new StringBuffer();
        String imgDecod = UtilDevice.getLedColor(idDev);

        buffer.append("<div id='DLed" + idDev + "' style='");
        buffer.append("background-image:url(images/led/L" + imgDecod +
            ".gif);background-repeat:no-repeat;background-position:center' ");
        buffer.append("><div style='visibility:hidden;'>" + imgDecod +
            "</div></div>");

        return buffer.toString();
    }

    private int[] pageDevice(int[] ids)
    {
        int[] devicesPage = null;

        if (pageNumber == 0)
        {
            float numPage = (float) ids.length / (float) numDevice4Page;

            if ((float) (numPage - (int) numPage) > 0)
            {
                pageNumber = (int) numPage + 1;
            }
            else
            {
                pageNumber = (int) numPage;
            }
        }

        int n = ids.length - ((pageNumber - 1) * numDevice4Page);

        if (n > 0)
        {
            devicesPage = new int[(n < numDevice4Page) ? n : numDevice4Page];

            for (int i = (pageNumber - 1) * numDevice4Page, j = 0;
                    ((i < ((pageNumber) * numDevice4Page)) && (i < ids.length));
                    i++, j++)
            {
                devicesPage[j] = ids[i];
            }

            return devicesPage;
        } //if

        return null;
    } //pageDevice

    public static boolean isDeviceModelUsed(Integer iddevmdl)
        throws DataBaseException
    {
        String sql = "select 1 from cfdevice where iddevmdl = ? and iscancelled = 'FALSE'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { iddevmdl });

        if (rs.size() != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
