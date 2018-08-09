package com.carel.supervisor.presentation.rule;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.memory.DMemory;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;
import com.carel.supervisor.presentation.io.CioDIAL;


public class RemoteListBox
{
	private static int screenw = 1024;
	private static int screenh = 768;

    public RemoteListBox()
    {
    }

    public String getRemoteListBox(int idsite, int actioncode, String language)
        throws DataBaseException
    {
        List remote1 = new ArrayList();
        List remote2 = new ArrayList();

        LangService lan = LangMgr.getInstance().getLangService(language);

        DispatcherBook[] remotearray = DispatcherBookList.getInstance()
                                                         .getReceiversByType("D");
        ListBoxElement tmp = null;

        for (int i = 0; i < remotearray.length; i++)
        {
            tmp = new ListBoxElement(remotearray[i].getReceiver() +" -> " + remotearray[i].getAddress(),
                    String.valueOf(remotearray[i].getKey()));
            remote1.add(tmp);
        }

        ActionBeanList actionlist = new ActionBeanList();
        String param = actionlist.getActionParameters(idsite, actioncode, "D");

        if (!param.equals(""))
        {
            String[] paramArray = param.split(";");
            int[] par = new int[paramArray.length];
            DispatcherBook tmpAddr = null;

            for (int i = 0; i < par.length; i++)
            {
                par[i] = Integer.parseInt(paramArray[i]);
                tmpAddr = DispatcherBookList.getInstance().getReceiver(par[i]);

                if (tmpAddr != null)
                {
                    remote2.add(new ListBoxElement(tmpAddr.getReceiver() +" -> " + tmpAddr.getAddress(),
                            String.valueOf(tmpAddr.getKey())));
                }
            }
        }

        DblListBox remotelist = new DblListBox(remote1, remote2,false,true,true,null,true,RuleConstants.MAX_REMOTE_ITEMS);
        remotelist.setScreenH(screenh);
        remotelist.setScreenW(screenw);
        remotelist.setIdlistbox("remote");
        // 20090116 - BUG #5289
        // Modified javascript function in order to check the items' upper limit
        // The upper limit is set in the RuleConstants class   
        //remotelist.setFncButton1("to2notRemove1MaxItemsChk(remote1, "+ RuleConstants.MAX_REMOTE_ITEMS +");return false;");
        remotelist.setSrcButton2("images/dbllistbox/arrowsx_on.png");
        //remotelist.setFncButton2("to1Rem(remote2);return false;");
        //remotelist.setHeight(300);
        remotelist.setLeftRowsListBox(20);
        remotelist.setRightRowsListBox(20);
        remotelist.setWidthListBox(400);
        remotelist.setWidth_select(600);
        remotelist.setHeaderTable1(lan.getString("setaction", "remotebook"));
        remotelist.setHeaderTable2(lan.getString("setaction", "remotelist"));
        

        return remotelist.getHtmlDblListBox();
    }
    
    public static String isDeviceConf(String language)
    {
    	String ret = "";
    	DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");
    	ret = zMem.getFisicDeviceId();
    	if(ret == null)
    		ret = "";
    	
    	if(ret.length() != 0)
    		ret = "";
    	else
    	{
    		LangService lan = LangMgr.getInstance().getLangService(language);
    		ret = lan.getString("setaction", "nodevice");
    	}
    	return ret;
    }
    public static String GetDeviceConf(int idsite, String language)
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	CioDIAL ras = new CioDIAL(idsite);
    	ras.loadConfiguration();
    	
    	String modemLabel = ras.getModemLabel();
    	String user = ras.getUser();
    	String number = ras.getNumber();
    	int trynumber = ras.getRetryNum();
    	int retryafter = ras.getRetryAfter();
    	String centra = ras.getCentralino();
    	
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("<fieldset class='field'>"+
		"<legend class='standardTxt'>"+lan.getString("setio","conf")+"</legend>"+
		"<table border='0' width='100%' cellspacing='1' cellpadding='1'>"+
			"<tr>"+
				"<td width='100%'>"+
					"<table border='0' width='100%' cellspacing='5' cellpadding='1'>"+
						"<tr>"+
							"<td class='standardTxt' width='15%'>"+lan.getString("setio","modem")+":</td>"+
							"<td class='standardTxt'width='35%'>"+
								"<input disabled type='text' size='25' value='"+modemLabel+"' class='standardTxt' />"+
							"</td>"+
						    "<td class='standardTxt' width='10%' align='right'>"+lan.getString("setio","centra")+":</td>"+
						    "<td width='30%'><input disabled type='text' size='4' value='"+ centra+"' class='standardTxt'/></td>"+
						    "<td width='10%' valign='top' align='right'>"+
							//"	<input type='button'  value='"+lan.getString("setio","conf")+"' onclick=top.frames['manager'].loadTrx('nop&folder=setio&bo=BSetIo&type=click&resource=SubTab4.jsp&desc=ncode12'); /></td>"+
							"</td>"+
						"</tr>"+					
						"<tr>"+
							"<td class='standardTxt'>"+lan.getString("setio","numtry")+":</td>");
    	trynumber = trynumber>0?trynumber:0;
    	buffer.append("		<td class='standardTxt'><input disabled type='text' size='4' value='"+ trynumber+"' class='standardTxt'/></td>"+
						"</tr>"+
						"<tr>"+
							"<td class='standardTxt'>"+lan.getString("setio","retry")+":</td>");
    	retryafter = retryafter>0?retryafter:0;
    	buffer.append("			<td class='standardTxt'><input disabled type='text' size='4' value='"+ retryafter+"' class='standardTxt' />"+lan.getString("setio","min")+"</td>"+
						"</tr>"+
					"</table>"+
				"</td>"+
			"</tr>"+
		"</table>"+
	"</fieldset>");
    	return buffer.toString();
    }
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }

}
