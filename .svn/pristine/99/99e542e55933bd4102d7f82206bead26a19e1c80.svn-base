package com.carel.supervisor.controller.database;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class TimeBandBean
{
    private static final String IDSITE = "idsite";
    private static final String IDTIMEBAND = "idtimeband";
    private static final String PVCODE = "pvcode";
    private static final String TIMECODE = "timecode";
    private static final String TIMETYPE = "timetype";
    private static final String TIMEBAND = "timeband";
    private static final String ISCYCLIC = "iscyclic";
    public static final int DAYLY = 1;
    public static final int WEEKLY = 2;
    public static final int MONTHLY = 3;
    public static final int YEAR_ONE_SHOT = 4;
    public static final int YEAR_REPEAT = 5;
    private Integer idtimeband = null;
    private Integer idsite = null;
    private String timecode = null;
    private String pvcode = null;
    private int timetype = 0;
    private String timeband = null;
    private boolean iscyclic = false;

    public TimeBandBean(Record record)
    {
        idtimeband = (Integer) record.get(IDTIMEBAND);
        pvcode = UtilBean.trim(record.get(PVCODE));
        idsite = (Integer) record.get(IDSITE);
        timetype = ((Integer) record.get(TIMETYPE)).intValue();
        timecode = UtilBean.trim(record.get(TIMECODE));
        timeband = UtilBean.trim(record.get(TIMEBAND));
        iscyclic = UtilBean.trim(record.get(ISCYCLIC)).equals("TRUE");
    }

    public TimeBandBean(Integer idsite, String timecode, String pvcode, int timetype,
        String timeband, boolean iscyclic)
    {
        this.pvcode = pvcode;
        this.idsite = idsite;
        this.timetype = timetype;
        this.timecode = timecode;
        this.timeband = timeband;
        this.iscyclic = iscyclic;
    }

    public Integer getIdsite()
    {
        return idsite;
    }

    public void setIdsite(Integer idsite)
    {
        this.idsite = idsite;
    }

    public boolean isIscyclic()
    {
        return iscyclic;
    }

    public void setIscyclic(boolean iscyclic)
    {
        this.iscyclic = iscyclic;
    }

    public String getTimeband()
    {
        return timeband;
    }

    public void setTimeband(String timeband)
    {
        this.timeband = timeband;
    }

    public String getTimecode()
    {
        return timecode;
    }

    public void setTimecode(String timecode)
    {
        this.timecode = timecode;
    }

    public int getTimetype()
    {
        return timetype;
    }

    public void setTimetype(int timetype)
    {
        this.timetype = timetype;
    }

    public Integer getIdtimeband()
    {
        return idtimeband;
    }

    public void setIdtimeband(Integer idtimeband)
    {
        this.idtimeband = idtimeband;
    }

    public String getPvcode()
    {
        return pvcode;
    }

    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }
}
