package com.carel.supervisor.presentation.assistance;

import java.util.HashMap;
import java.util.Map;

public class UserInMgr
{
    private static UserInMgr ui = new UserInMgr();
    private Map userin = null;
    private Map sessid = null;
    
    private UserInMgr()
    {
        this.userin = new HashMap();
        this.sessid = new HashMap();
    }
    
    public static UserInMgr getInstance() {
        return ui;
    }
    
    public boolean validUserIn(String sessionId) {
        return this.sessid.containsKey(sessionId);
    }
    
    public void addUseIn(String user,String sessionid)
    {
        String sid = (String)this.userin.remove(user);
        if(sid != null)
            this.sessid.remove(sid);
        
        this.userin.put(user,sessionid);
        this.sessid.put(sessionid,null);
    }
}
