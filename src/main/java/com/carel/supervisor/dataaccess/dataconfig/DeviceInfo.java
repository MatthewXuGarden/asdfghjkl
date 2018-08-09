package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.db.*;


public class DeviceInfo implements IDumpable
{
    private static final String PVCODE = "pvcode";
    private static final String SITE = "idsite";
    private static final String CODE = "code";
    private static final String ENABLED = "isenabled";
    private static final String ISLOGIC = "islogic";
    private static final String ID = "iddevice";
    private static final String MODEL = "iddevmdl";
    private static final String LINE = "idline";
    private static final String ADDRESS = "address";
    private static final String GLOBALINDEX = "globalindex";
    private static final String LITTLE_ENDIAN = "littlendian";
    private static final String DESCRIPTION = "desc";
    private static final String COMPORT = "comport";
    private static final String DEVTYPE = "devtype";
    private static final String LINECODE = "linenum";


    private String pv = null;
    private int site = 0;
    private String code = null;
    private boolean enabled = true;
    private boolean isLogic = false;
    private Integer id = null;
    private Integer model = null;
    private Integer line = null;
    private Integer address = null;
    private Integer globalindex = null;
    private boolean isLittleEndian = true;
    private String description = "";
    private String comport = "";
    private String devtype = "";
    private int linecode =0;

    private LineInfo lineInfo = null;

    public DeviceInfo(Record record)
    {
        site = ((Integer) record.get(SITE)).intValue();
        code = UtilBean.trim(record.get(CODE));
        enabled = UtilBean.checkBoolean(record.get(ENABLED), true); //FOR DEFAULT, DEVICE IS ENABLED
        id = (Integer) record.get(ID);
        model = (Integer) record.get(MODEL);
        line = (Integer) record.get(LINE);
        address = (Integer) record.get(ADDRESS);
        globalindex = (Integer) record.get(GLOBALINDEX);
        isLogic = UtilBean.checkBoolean(record.get(ISLOGIC), false); //FOR DEFAULT DEVICE IS PHYSICAL
        isLittleEndian = UtilBean.checkBoolean(record.get(LITTLE_ENDIAN), true); //FOR DEFAULT DEVICE IS PHYSICAL
        
        if(record.hasColumn(DESCRIPTION))
        	description = UtilBean.trim(record.get(DESCRIPTION));
        if(record.hasColumn(COMPORT))
        	comport = UtilBean.trim(record.get(COMPORT));
        if(record.hasColumn(LINECODE))
        	linecode = ((Integer) record.get(LINECODE)).intValue();
        if(record.hasColumn(DEVTYPE))
        	devtype = UtilBean.trim(record.get(DEVTYPE));
    }

    public boolean isLogic()
    {
        return isLogic;
    }

    public void setLogic(boolean isLogic)
    {
        this.isLogic = isLogic;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public int getSite()
    {
        return site;
    }

    public void setSite(int site)
    {
        this.site = site;
    }

    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(PVCODE, pv);
        dumpWriter.print(SITE, site);
        dumpWriter.print(CODE, code);
        dumpWriter.print(PVCODE, pv);
        dumpWriter.print(ENABLED, enabled);

        return dumpWriter;
    }

    public Integer getAddress()
    {
        return address;
    }

    public void setAddress(Integer address)
    {
        this.address = address;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getLine()
    {
        return line;
    }

    public void setLine(Integer line)
    {
        this.line = line;
    }

    public Integer getModel()
    {
        return model;
    }

    public void setModel(Integer model)
    {
        this.model = model;
    }

    public LineInfo getLineInfo()
    {
        return lineInfo;
    }

    public void setLineInfo(LineInfo lineInfo)
    {
        this.lineInfo = lineInfo;
    }

    public Integer getGlobalindex()
    {
        return globalindex;
    }

    public void setGlobalindex(Integer globalindex)
    {
        this.globalindex = globalindex;
    }

    public boolean isLittleEndian()
    {
        return isLittleEndian;
    }
    
    public String getDescription() 
    {
    	return description;
    }

	public String getDevtype() {
		return devtype;
	}

	public void setDevtype(String devtype) {
		this.devtype = devtype;
	}

	public String getComport() {
		return comport;
	}

	public void setComport(String comport) {
		this.comport = comport;
	}

	public int getLinecode() {
		return linecode;
	}

	public void setLinecode(int linecode) {
		this.linecode = linecode;
	}
}
