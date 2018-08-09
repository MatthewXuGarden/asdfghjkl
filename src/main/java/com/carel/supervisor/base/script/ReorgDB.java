package com.carel.supervisor.base.script;

public class ReorgDB
{
    private ScriptInvoker script = new ScriptInvoker();

    public ReorgDB()
    {
    }

    public void reorg()
    {
        reorgInner("hsvariable", "hsvariable_dx1");
        reorgInner("buffer", "buffer_idx");
        reorgInner("hsalarm", "hsalarm_idx");
        reorgInner("hsevent", "hsevent_pk_hsevent");
        reorgInner("rulestate", "hsevent_pk");
    }

    private void reorgInner(String tableName, String index)
    {
        try
        {
            script.execute(new String[] { "c:\\reorg.bat", tableName, index }, "C:\\RESULT.TXT");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
