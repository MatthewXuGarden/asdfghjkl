package com.carel.supervisor.presentation.rule;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.FMemory;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;
import com.carel.supervisor.presentation.io.CioFAX;


public class FaxListBox
{

	
    private static int screenw = 1024;
	private static int screenh = 768;
    
   
    public FaxListBox()
    {
    }

    public String getFaxListBox(int idsite, int actioncode, String language)
        throws DataBaseException
    {
        List fax1 = new ArrayList();
        List fax2 = new ArrayList();

        LangService lan = LangMgr.getInstance().getLangService(language);

        DispatcherBook[] faxarray = DispatcherBookList.getInstance()
                                                      .getReceiversByType("F");
        ListBoxElement tmp = null;

        for (int i = 0; i < faxarray.length; i++)
        {
            tmp = new ListBoxElement(faxarray[i].getReceiver() + " -> " + faxarray[i].getAddress(),
                    String.valueOf(faxarray[i].getKey()));
            fax1.add(tmp);
        }

        ActionBeanList actionlist = new ActionBeanList();
        String param = actionlist.getActionParameters(idsite, actioncode, "F");

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
                    fax2.add(new ListBoxElement(tmpAddr.getReceiver()  + " -> " + tmpAddr.getAddress(),
                            String.valueOf(tmpAddr.getKey())));
                }
            }
        }

        //DblListBox faxlist = new DblListBox(fax1, fax2,false,true,true,null,false);
        DblListBox faxlist = new DblListBox(fax1, fax2,false,true,true,null,true,RuleConstants.MAX_FAX_ITEMS);
        faxlist.setScreenH(screenh);
        faxlist.setScreenW(screenw);
        faxlist.setSrcButton2("images/dbllistbox/arrowsx_on.png");
        faxlist.setIdlistbox("fax");
        // 20090116 - BUG #5289
        // Modified javascript function in order to check the items' upper limit
        // The upper limit is set in the RuleConstants class       
        //faxlist.setFncButton1("to2notRemove1MaxItemsChk(fax1,"+ RuleConstants.MAX_FAX_ITEMS +");return false;");
        //faxlist.setFncButton2("to1Rem(fax2);return false;");
        //faxlist.setHeight(300);
        faxlist.setLeftRowsListBox(20);
        faxlist.setRightRowsListBox(20);
        faxlist.setWidthListBox(400);
        faxlist.setWidth_select(600);
        
        faxlist.setHeaderTable1(lan.getString("setaction", "faxbook"));
        faxlist.setHeaderTable2(lan.getString("setaction", "faxlist"));

        return faxlist.getHtmlDblListBox();
    }
    
    public static String isDeviceConf(String language)
    {
    	String ret = "";
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	FMemory zMem = (FMemory) DispMemMgr.getInstance().readConfiguration("F");
    	ret = zMem.getFisicDeviceId();
    	if(ret == null)
    		ret = "";
    	
    	if(ret.length() != 0)
    		ret = "";
    	else
    	{
    		ret = lan.getString("setaction", "nodevice");
    	}
    	return ret;
    }
    public static String GetDeviceConf(int idsite,String language)
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	CioFAX cio = new CioFAX(idsite);
		cio.loadConfiguration();
		
		int idConfiguration = cio.getIdconf();
		String modemLabel = cio.getModemId();
		int trynumber = cio.getTrynum();
		int retryafter = cio.getRetrynum();
		String centra = cio.getCentralino();
		 		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<fieldset class='field'>");
		buffer.append("<legend class='standardTxt'>"+lan.getString("setio","conf")+"</legend>");
		buffer.append("<table border='0' width='100%' cellspacing='5' cellpadding='1'>");
		buffer.append("	<tr>");
		buffer.append("		<td class='standardTxt' width='15%'>"+lan.getString("setio","modem")+":</td>");
		buffer.append("		<td class='standardTxt' width='35%'>");
		buffer.append("		<input disabled type='text' size=50 value='"+modemLabel+"' class='standardTxt'/>");	
		buffer.append("		</td>");
		buffer.append("		<td class='standardTxt' width='10%'>"+lan.getString("setio","centra")+":</td>");
		buffer.append("	    <td width='30%'><input disabled type='text' size='4' maxlength='6'  value='"+centra+"'/></td>");
		buffer.append("	    <td width='10%' align='right'></td>");
		buffer.append("	</tr>");
		buffer.append("	<tr>");
		buffer.append("		<td class='standardTxt'>"+lan.getString("setio","numtry")+":</td>");
		trynumber = trynumber>0?trynumber:0;
		buffer.append("		<td class='standardTxt'><input disabled type='text' size='4' value='"+trynumber+"' class='standardTxt' /></td>");
		buffer.append("	</tr>");
		buffer.append("	<tr>");
		buffer.append("		<td class='standardTxt'>"+lan.getString("setio","retry")+":</td>");
		retryafter = retryafter>0?retryafter:0;
		buffer.append("		<td class='standardTxt'><input disabled type='text' size='4' maxlength='2' value='"+ retryafter+"' class='standardTxt'/> "+lan.getString("setio","min")+"</td>");
		buffer.append("</tr>");
		buffer.append("</table>");
		buffer.append("</fieldset>");
		return buffer.toString();
    }
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }
}
