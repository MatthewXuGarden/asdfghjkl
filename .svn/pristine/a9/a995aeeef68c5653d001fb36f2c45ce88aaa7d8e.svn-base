package com.carel.supervisor.presentation.rule;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;
import com.carel.supervisor.presentation.io.CioMAIL;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;


public class EmailListBox
{
	private static int screenw = 1024;
	private static int screenh = 768;
	
    public EmailListBox()
    {
    }

    public String getEmailListBox(int idsite, int actioncode, String language)
    	throws DataBaseException
    {
    	return getEmailListBox(idsite, actioncode, language, "E");
    }
    
    public String getEmailListBox(int idsite, int actioncode, String language, String actiontype)
        throws DataBaseException
    {
        List email1 = new ArrayList();
        List email2 = new ArrayList();

        LangService lan = LangMgr.getInstance().getLangService(language);
        DispatcherBook[] emailarray = DispatcherBookList.getInstance().getReceiversByType("E");
        ListBoxElement tmp = null;

        for (int i = 0; i < emailarray.length; i++)
        {
            tmp = new ListBoxElement(emailarray[i].getReceiver() +" -> " + emailarray[i].getAddress(),
                    String.valueOf(emailarray[i].getKey()));
            email1.add(tmp);
        }

        ActionBeanList actionlist = new ActionBeanList();
        String param = actionlist.getActionParameters(idsite, actioncode, actiontype);

        if (!param.equals(""))
        {
            String[] paramArray = param.split(";");
            int[] par = new int[paramArray.length];
            DispatcherBook tmpAddr = null;
            
            // skip report parameters
            int i = actiontype.compareToIgnoreCase("P") == 0 || actiontype.compareToIgnoreCase("Pr") == 0
            	? 2 : 0;
            for (; i < par.length; i++)
            {
                par[i] = Integer.parseInt(paramArray[i]);
                tmpAddr = DispatcherBookList.getInstance().getReceiver(par[i]);

                if (tmpAddr != null)
                {
                    email2.add(new ListBoxElement(tmpAddr.getReceiver() +" -> " + tmpAddr.getAddress(),
                            String.valueOf(tmpAddr.getKey())));
                }
            }
        }

        DblListBox emaillist = new DblListBox(email1, email2,false,true,true,null,true,RuleConstants.MAX_EMAIL_ITEMS);
        emaillist.setScreenH(screenh);
        emaillist.setScreenW(screenw);
        emaillist.setSrcButton2("images/dbllistbox/arrowsx_on.png");
        emaillist.setIdlistbox("email");
        // 20090116 - BUG 5289
        // Modified javascript function in order to check the items' upper limit
        // The upper limit is set in the RuleConstants class   
        //emaillist.setFncButton1("to2notRemove1MaxItemsChk(email1, "+ RuleConstants.MAX_EMAIL_ITEMS +");return false;");        
        //emaillist.setFncButton2("to1Rem(email2);return false;");
        //emaillist.setHeight(300);
        emaillist.setLeftRowsListBox(15);
        emaillist.setRightRowsListBox(15);
        emaillist.setWidthListBox(400);
        emaillist.setWidth_select(600);
        emaillist.setHeaderTable1(lan.getString("setaction", "emailbook"));
        emaillist.setHeaderTable2(lan.getString("setaction", "emaillist"));

        return emaillist.getHtmlDblListBox();
    }
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }
    public static String GetDeviceConf(int idsite, String language)
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	CioMAIL mail = new CioMAIL(idsite);
    	mail.loadConfiguration();
    	String smtp = mail.getSmtp();
    	String sender = mail.getSender();
    	String type = mail.getType();
    	String provider = mail.getProviderId();
    	int trynumber = mail.getTrynum();
    	int retryafter = mail.getRetrynum();
    	String smtpUser = mail.getUser();
    	String smtpPass = mail.getPass();
    	
    	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
    	String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");
    	
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("<fieldset class='field'>"+
		"<legend class='standardTxt'>"+lan.getString("setio","conf")+"</legend>"+		
		"<table border='0' width='100%' cellspacing='5' cellpadding='1'>"+
			"<tr>"+
				"<td width='45%' valign='top'>"+
					"<table border='0' width='100%' cellspacing='5' cellpadding='2'>"+
						"<tr>"+
							"<td class='standardTxt'>"+lan.getString("setio","smtp")+":</td>"+
							"<td class='standardTxt'><input disabled type='text' class='standardTxt"+cssVirtualKeyboardClass+"'  size='25' maxlength='100' value='"+smtp+"'/> *</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='standardTxt'>"+lan.getString("setio","mit")+":</td>"+
							"<td class='standardTxt'><input disabled type='text' class='standardTxt"+cssVirtualKeyboardClass+"' size='25' maxlength='100' value='"+sender+"'/> *</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='standardTxt'>"+lan.getString("setio","numtry")+":</td>");
    	trynumber = trynumber>0?trynumber:0;
    	buffer.append("					<td class='standardTxt'><input disabled type='text' class='standardTxt"+cssVirtualKeyboardClass+"' size='4' maxlength='2' value='"+trynumber +"'/></td>"+
						"</tr>"+
						"<tr>"+
							"<td class='standardTxt'>"+lan.getString("setio","retry")+":</td>");
    	retryafter = retryafter>0?retryafter:0;
    	buffer.append("			<td class='standardTxt'><input disabled type='text' class='standardTxt"+cssVirtualKeyboardClass+"' size='4' maxlength='2' value='"+ retryafter+"'/> "+lan.getString("setio","min")+"</td>"+
						"</tr>"+
					"</table>"+
				"</td>"+
				"<td width='45%' valign='top'>"+
					"<table border='0' width='100%' cellspacing='5' cellpadding='2'>"+
						"<tr>"+
							"<td class='standardTxt' width='30%'>"+lan.getString("setio","coll")+":</td>"+
							"<td class='standardTxt'><nobr>");
    	String checked = (type.equalsIgnoreCase("L")||type.equalsIgnoreCase(""))?"checked":"";
		buffer.append("<input disabled type='radio' value='L' "+ checked+" >LAN");
		checked = (type.equalsIgnoreCase("D")||type.equalsIgnoreCase(""))?"checked":"";
		buffer.append("<input disabled type='radio' value='D' "+ checked+" >DialUp");
		buffer.append("				</nobr>"+
							"</td>"+
						"</tr>"+
						"<tr>"+
							"<td colspan='2'>"+
								"<div id='setioDiv' style='visibility:visible;'>"+
									"<table border='0' width='100%' cellspacing='0' cellpadding='0'>"+
										"<tr>"+
											"<td class='standardTxt' width='30%'>"+lan.getString("setio","prov")+":</td>"+
											"<td style='padding-left:5px;'>"+
											"<input disabled type='text' size=25 value='"+provider+"' class='standardTxt"+cssVirtualKeyboardClass+"'/>"+	
											"</td>"+
										"</tr>"+
									"</table>"+
								"</div>"+
							"</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='standardTxt' width='30%'>"+lan.getString("ldapPage","user")+":</td>"+
							"<td class='standardTxt'>"+
								"<input disabled type='text' class='standardTxt"+cssVirtualKeyboardClass+"' size='25' value='"+smtpUser+"'/>"+
							"</td>"+
						"</tr>"+
						"<tr>"+
							"<td class='standardTxt' width='30%'>"+lan.getString("ldapPage","password")+":</td>"+
							"<td class='standardTxt'>"+
								"<input disabled type='password' class='standardTxt"+cssVirtualKeyboardClass+"' size='25' value='"+smtpPass+"'/>"+
							"</td>"+
						"</tr>"+
					"</table>"+	
				"</td>"+
				"<td width='10%' valign='top' align='right'>"+
				"&nbsp;"+
				"</td>"+
			"</tr>"+
		"</table>"+
	"</fieldset>");
		return buffer.toString();
    }
}
