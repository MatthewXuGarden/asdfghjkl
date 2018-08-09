package com.carel.supervisor.presentation.rule;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.SMemory;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;
import com.carel.supervisor.presentation.io.CioSMS;


public class SmsListBox
{
	private static int screenw = 1024;
	private static int screenh = 768;
	
	
    public SmsListBox()
    {
    }

    public String getSmsListBox(int idsite, int actioncode, String language)
        throws DataBaseException
    {
        List sms1 = new ArrayList();
        List sms2 = new ArrayList();

        LangService lan = LangMgr.getInstance().getLangService(language);
        DispatcherBook[] smsarray = DispatcherBookList.getInstance()
                                                      .getReceiversByType("S");
        ListBoxElement tmp = null;

        for (int i = 0; i < smsarray.length; i++)
        {
            tmp = new ListBoxElement(smsarray[i].getReceiver()+" -> " + smsarray[i].getAddress(),
                    String.valueOf(smsarray[i].getKey()));
            sms1.add(tmp);
        }

        ActionBeanList actionlist = new ActionBeanList();
        String param = actionlist.getActionParameters(idsite, actioncode, "S");

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
                    sms2.add(new ListBoxElement(tmpAddr.getReceiver()+" -> " + tmpAddr.getAddress(),
                            String.valueOf(tmpAddr.getKey())));
                }
            }
        }

        DblListBox smslist = new DblListBox(sms1, sms2,false,true,true,null,true,RuleConstants.MAX_SMS_ITEMS);
        smslist.setScreenH(screenh);
        smslist.setScreenW(screenw);
        smslist.setIdlistbox("sms");
        //use the default one arrowsx_on.png
        smslist.setSrcButton2("images/dbllistbox/arrowsx_on.png");
        // 20090116 - BUG #5289
        // Modified javascript function in order to check the items' upper limit
        // The upper limit is set in the RuleConstants class 
        //delete by kevin use the default add/remove js function
        //smslist.setFncButton1("to2notRemove1MaxItemsChk(sms1, "+ RuleConstants.MAX_SMS_ITEMS +");return false;");
        //smslist.setFncButton2("to1Rem(sms2);return false;");
        //smslist.setHeight(300);
        smslist.setLeftRowsListBox(20);
        smslist.setRightRowsListBox(20);
        smslist.setHeaderTable1(lan.getString("setaction", "smsbook"));
        smslist.setHeaderTable2(lan.getString("setaction", "smslist"));
        smslist.setWidthListBox(400);
        smslist.setWidth_select(600);

        return smslist.getHtmlDblListBox();
    }
    
    public static String isDeviceConf(String language)
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	String ret = "";
    	SMemory zMem = (SMemory) DispMemMgr.getInstance().readConfiguration("S");
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
		CioSMS cioSms = new CioSMS(idsite);
		cioSms.loadConfiguration();
		
		int idConfiguration = cioSms.getIdConf();
		String modemLabel = cioSms.getLabelModem();
		String type = cioSms.getType();
		int provider = cioSms.getProviderId();
		String providerlb = cioSms.getProviderLb();
		String call = cioSms.getCall();
		int trynumber = cioSms.getTrynum();
		int retryafter = cioSms.getRetrynum();
		String centra = cioSms.getCentralino();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<fieldset class='field'>");
		buffer.append("<legend class='standardTxt'>"+lan.getString("setio","conf")+"</legend>");
		buffer.append("<table border='0' width='100%' cellspacing='5' cellpadding='1'>");
		buffer.append("	<tr>");
		buffer.append("		<td width='45%' valign='top'>");
		buffer.append("			<table border='0' width='100%' cellspacing='5' cellpadding='1'>");
		buffer.append("				<tr>");
		buffer.append("					<td class='standardTxt'>"+lan.getString("setio","modem")+":</td>");
		buffer.append("					<td class='standardTxt'>");
		buffer.append("		<input disabled type='text' size=50 value='"+modemLabel+"' class='standardTxt'/>");	
		buffer.append("					</td>");
		buffer.append("					</tr>");
		buffer.append("					<tr>");
		buffer.append("						<td class='standardTxt'>"+lan.getString("setio","tipo")+":</td>");
		buffer.append("						<td class='standardTxt'><nobr>");
		String checked = (type.equalsIgnoreCase("A")||type.equalsIgnoreCase(""))?"checked":"";
		buffer.append("						<input  disabled  type='radio'  value='A'"+checked+" >  analogic");
		checked = (type.equalsIgnoreCase("G")||type.equalsIgnoreCase(""))?"checked":"";
		buffer.append("						<input  disabled  type='radio'  value='G' "+checked+" >GSM");
		buffer.append("						</nobr>");
		buffer.append("					</td>");
		buffer.append("				</tr>");
		buffer.append("				<tr>");
		buffer.append("					<td class='standardTxt'>"+lan.getString("setio","prov")+":</td>");
		buffer.append("					<td class='standardTxt'>");
		buffer.append("		<input disabled type='text' size=50 value='"+providerlb+"' class='standardTxt'/>");	
		buffer.append("					</td>");
		buffer.append("				</tr>");
		buffer.append("				<tr>");
		buffer.append("					<td class='standardTxt'>"+lan.getString("setio","call")+":</td>");
		buffer.append("					<td>");
		String callDescr = "";
		if(call.equalsIgnoreCase("N"))
		{
			callDescr = lan.getString("setio","national");
		}
		else if(call.equalsIgnoreCase("I"))
		{
			callDescr = lan.getString("setio","international");
		}
		buffer.append("		<input disabled type='text' size=50 value='"+callDescr+"' class='standardTxt'/>");	
		buffer.append("					</td>");
		buffer.append("				</tr>");
		buffer.append("			</table>");
		buffer.append("		</td>");
		buffer.append("		<td width='45%' valign='top'>");
		buffer.append("			<table border='0' width='100%' cellspacing='5' cellpadding='1'>");
		buffer.append("				<tr>");
		buffer.append("					<td class='standardTxt' width='30%'>"+lan.getString("setio","centra")+":</td>");
		buffer.append("					<td><input  disabled type='text' class='standardTxt' size='4' maxlength='10' value='"+centra+"'/></td>");
		buffer.append("				<tr>");
		buffer.append("				<tr>");
		buffer.append("					<td></td>");
		buffer.append("				</tr>");
		buffer.append("				<tr>");
		buffer.append("					<td class='standardTxt' width='30%'>"+lan.getString("setio","numtry")+":</td>");
		trynumber = trynumber>0?trynumber:0;
		buffer.append("					<td class='standardTxt'><input  disabled type='text' class='standardTxt' size='4' maxlength='2' value='"+trynumber+"'/></td>");
		buffer.append("				</tr>");
		buffer.append("				<tr>");
		buffer.append("					<td class='standardTxt' width='30%'>"+lan.getString("setio","retry")+":</td>");
		retryafter = retryafter>0?retryafter:0;
		buffer.append("					<td class='standardTxt'><input disabled type='text' class='standardTxt' size='4' maxlength='2' value='"+retryafter+"'/> "+lan.getString("setio","min")+"</td>");
		buffer.append("				</tr>");
		buffer.append("			</table>");	
		buffer.append("		</td>");
		buffer.append("<td width='10%' valign='top' align='right'>");
		//buffer.append("	<input type='button'  value='"+lan.getString("setio","conf")+"' onclick=top.frames['manager'].loadTrx('nop&folder=setio&bo=BSetIo&type=click&resource=SubTab2.jsp&desc=ncode12'); /></td>");
		buffer.append("</td>");
		buffer.append("	</tr>");
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
