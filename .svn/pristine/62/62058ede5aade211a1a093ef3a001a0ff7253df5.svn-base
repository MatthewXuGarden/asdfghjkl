<%@ page language="java"  pageEncoding="UTF-8" 
import="java.util.*" 
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"
import="com.carel.supervisor.dataaccess.datalog.impl.*"
import="com.carel.supervisor.base.config.BaseConfig"
import="java.sql.Timestamp"
import="com.carel.supervisor.dataaccess.db.*"
import="com.carel.supervisor.presentation.tabmenu.*"
import="com.carel.supervisor.base.profiling.*"
import="java.text.*"
import="com.carel.supervisor.base.conversion.*"
import="com.carel.supervisor.dataaccess.language.*"
import="com.carel.supervisor.presentation.devices.DeviceList"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLDiv"
import="com.carel.supervisor.presentation.note.Note"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"

%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	String language = sessionUser.getLanguage();
	String user = sessionUser.getUserName();
	String id = sessionUser.getCurrentUserTransaction().getProperty("id");
	String cmd = sessionUser.getPropertyAndRemove("cmd");
	String idnota = sessionUser.getPropertyAndRemove("idnota");
	String textarea = sessionUser.getPropertyAndRemove("noteArea");
	String tablenote = sessionUser.getCurrentUserTransaction().getProperty("notetable");
	if(tablenote == null || tablenote.length() == 0)
	{
		// bye Kevin
		//when note.jsp is the default page, the url is: note.jsp?tablenote=xxx
		//need to get parameter from request, not from user transaction
		tablenote =  request.getParameter("notetable");
	}
	String onlyreadnote = sessionUser.getPropertyAndRemove("onlyreadnote");
	int screenh = sessionUser.getScreenHeight();
	int screenw = sessionUser.getScreenWidth();
	int idsite = sessionUser.getIdSite();	
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	// name
	String name="";
	
	GroupListBean groups = sessionUser.getGroup();
		
	int[] idsgroup = null;
	
	boolean canDeleteNotes=true;
	
	if (tablenote.equals("cfgroup"))
	{
		String idgroup = sessionUser.getCurrentUserTransaction().getProperty("group");		
		id = idgroup;
		

		if (idgroup.equals("1"))
		{ 
		  idsgroup = groups.getIds();	
		  // name
		  name = groups.getGlobalGroup().getDescription();
		} else 
		{ // gruppo di una data area
		  // name
	      name = groups.getDescriptionGroupByGroupId((new Integer(id)).intValue());
	    }
	}
	else if (tablenote.equals("cfline"))
	{
		id=sessionUser.getProperty("line");
	}
	
	else if (tablenote.equals("cfdevice"))
	{ // dispositivo
		id=sessionUser.getProperty("iddev");
		// name
		DeviceDetailBuilder ddBuilder = new DeviceDetailBuilder(sessionUser);
	    name = ddBuilder.getNameTable(sessionUser,(new Integer(id)).intValue());
	}
	
	else if (tablenote.equals("cfsite"))
	{
		id= String.valueOf(idsite);
	}
	
	else if (tablenote.equals("hsalarm"))
	{
		String idalarm=id;
		if (idalarm.contains("_"))
		{
			idalarm = id.split("_")[0];
		}

		Integer idAl = new Integer(idalarm); 
		
		AlarmLogList all = new AlarmLogList();
		all.retriveByAlarmId( sessionUser.getLanguage(),null,"firstPV",idsite,true,idAl );
		AlarmLog a = all.getById(idAl);
		if (AlarmMngTable.mandatoryNote(new Integer(a.getPriority().trim()))){			
			if (a.getAckuser()!=null)
				canDeleteNotes=false;
		}
		
		
	}
	
	if (id.contains("_"))
	{
		id = id.split("_")[0];
	}
	
	NoteLogList note = new NoteLogList();
	NoteLog nota = new NoteLog();
	if (cmd!=null)
	{
		Note.executeNoteAction(ut,idsite,user,cmd, textarea,idnota,tablenote,Integer.parseInt(id));
	}
	
	if ((tablenote.equals("cfgroup")) && (idsgroup!=null)) 
	{
		for (int i=0;i<idsgroup.length;i++)
			note.retrieve(idsite, tablenote, idsgroup[i]);
	}
	else
	{
		note.retrieve(idsite, tablenote, Integer.parseInt(id) ); 
	}
					
	String htmlTable = "";
	String noNotes= "";
	int rows = note.size();
	final int columns = 4;
	HTMLElement[][] dati = new HTMLElement[rows][];
	String[] headerTable = {multiLanguage.getString("notetable", "inserttime") , multiLanguage.getString("notetable", "lastmodifytime"), multiLanguage.getString("notetable", "user"), multiLanguage.getString("notetable", "nota")};
	String[] ClickRowFunction = new String[rows];
	String[] dblClickRowFunction = new String[rows];
	String[] rowsClasses = new String[rows];

	if (rows != 0)
	{
			for (int i = 0; i < rows; i++)
			{
					dati[i] = new HTMLElement[columns];
					nota = ((NoteLog) note.getNote(i));
					String cut=null;
					if (!(nota.getNote().equals("")))   //nota nulla setto a nbsp x stile
					{
						if ( nota.getNote().length()   > 69)   //nota lunga la taglio
						{
							cut = nota.getNote().substring(0,65)+"...";
							cut = cut.replaceAll("%!","'");
						}
						else
						{
							cut = nota.getNote();
							cut = cut.replaceAll("%!","'");
						}
					}
					else 
						cut= "&nbsp;";
					
					dblClickRowFunction[i] = String.valueOf(nota.getId());
					ClickRowFunction[i] = String.valueOf(nota.getId());
					
					dati[i][0] = new HTMLSimpleElement(DateUtils.date2String(((Date) nota.getStartTime()),"yyyy/MM/dd HH:mm"));
					dati[i][1] = new HTMLSimpleElement(DateUtils.date2String(((Date) nota.getLastTime()),"yyyy/MM/dd HH:mm"));
					dati[i][2] = new HTMLSimpleElement(nota.getUserNote());
					dati[i][3] = new HTMLDiv(cut);
					dati[i][3].addAttribute("name", nota.getNote());
					dati[i][3].addAttribute("id", String.valueOf(nota.getId()));
			}
	
			HTMLTable table = new HTMLTable("note", headerTable, dati,true,true);
			// Azione DBCLICK
			if (!isProtected)
			{
				table.setDbClickRowAction("modifyNote('$1')");
			}
			
			if (canDeleteNotes)
				// Azione CLICK
				table.setSgClickRowAction("selectedNote('$1');");
			else
				table.setSgClickRowAction("selectedNoteNoDelete('$1');");
				
			
			table.setSnglClickRowFunction(ClickRowFunction);
			if (!isProtected)
			{
				table.setDlbClickRowFunction(dblClickRowFunction);
			}
			table.setRowsClasses(rowsClasses);
			table.setScreenH(screenh);
			table.setScreenW(screenw);
			table.setColumnSize(0,120);
			table.setColumnSize(1,120);
			table.setColumnSize(2,120);
			table.setColumnSize(3,470);
			table.setHeight(180);
			table.setWidth(900);

			// Get HTML table
			htmlTable = table.getHTMLText();	
	} 
	else
	{
			HTMLTable table = new HTMLTable("note", headerTable, dati);
			table.setScreenH(screenh);
			table.setScreenW(screenw);
			table.setColumnSize(0,120);
			table.setColumnSize(1,120);
			table.setColumnSize(2,120);
			table.setColumnSize(3,470);
			
			
			table.setHeight(180);
			table.setWidth(900);

			// Get HTML table
			htmlTable = table.getHTMLText();
	}
	
	String confirmDeleteNote = multiLanguage.getString("notepage","confirmdeletepopup");
	String longNote = multiLanguage.getString("notepage","longnote");
	String blankNote = multiLanguage.getString("notepage","blanknote");
	String wronguser = multiLanguage.getString("notepage","wronguser");
	String b_wronguser = ut.getProperty("wronguser");

	ut.setProperty("wronguser","no");
	
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
%>

<%@page import="com.carel.supervisor.presentation.alarms.AlarmMngTable"%><input type='hidden' id='onlyreadnote' value='<%=onlyreadnote%>'/>
<input type='hidden' id='b_wronguser' value='<%=b_wronguser%>'/>
<input type='hidden' id='s_wronguser' value='<%=wronguser%>'/>
<input type='hidden' id='isprotected' value='<%=((isProtected)?"1":"0")%>'/>
<input type='hidden' id='vkeytype' value='PVPro' />

<table border="0" width="100%" height="70%" cellspacing="1" cellpadding="1">
	<tr height="5%" valign="top">
		<td class="tdTitleTable"><%=name%></td>
	</tr>	
		<% if (OnScreenKey) { %>
		<tr height="35%" valign="top">
		<td>
			<form id="frm" name="frm">
				<textarea class="keyboardInput" <%=(isProtected?"disabled":"")%> onkeydown="note_filterInput(this,event);" name="noteArea" class="notetxt" id="noteArea" style="width:97.0%" rows="10"></textarea>
			</form>
			<form id="removeForm" method="post"><input id="remove" type="hidden" value=""></form>
			<input id="confirmDelete" type="hidden" value="<%=confirmDeleteNote%>"></input>
			<input id="longNote" type="hidden" value="<%=longNote%>"></input>
			<input id="blankNote" type="hidden" value="<%=blankNote%>"></input>
		</td>
	</tr>
	<tr><td><p/></td></tr>
	<tr height="*" valign="top" id="trNote">
		<td><%=htmlTable%></td>
	</tr>
	<% } else { %>
	<tr height="*" valign="top" id="trNote">
		<td><%=htmlTable%></td>
	</tr>
	<tr><td><p/></td></tr>
	<tr height="35%" valign="top">
		<td>
			<form id="frm" name="frm">
				<textarea <%=(isProtected?"disabled":"")%> onkeydown="note_filterInput(this,event);" name="noteArea" class="notetxt" id="noteArea" style="width:98.7%" rows="10"></textarea>
			</form>
			<form id="removeForm" method="post"><input id="remove" type="hidden" value=""></form>
			<input id="confirmDelete" type="hidden" value="<%=confirmDeleteNote%>"></input>
			<input id="longNote" type="hidden" value="<%=longNote%>"></input>
			<input id="blankNote" type="hidden" value="<%=blankNote%>"></input>
		</td>
	</tr>
	<% } %>
</table>

<%if (!canDeleteNotes) { %>
	<SCRIPT type="text/javascript">
		setTimeout('disableAction(2);',200);
	</SCRIPT>
<% } %>
