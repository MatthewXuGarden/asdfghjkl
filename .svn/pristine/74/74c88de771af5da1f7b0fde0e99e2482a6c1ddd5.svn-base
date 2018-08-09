package supervisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.sdk.obj.CurrNode;
import com.carel.supervisor.presentation.sdk.obj.CurrUnit;
import com.carel.supervisor.presentation.sdk.obj.CurrVar;
import com.carel.supervisor.presentation.sdk.util.Sfera;
import com.carel.supervisor.presentation.session.UserSession;

public class SRVLSdk extends HttpServlet 
{
	private static final String CONTENT = "text/xml; charset=UTF-8";
	private CurrUnit cu;
	private CurrNode cn;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType(CONTENT);
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        response.setHeader("Cache-Control", "no-cache");
        //response.getWriter().write(xmlResp);
        //response.getWriter().flush();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		cu = (CurrUnit)request.getSession().getAttribute("CurrUnit");
		cn = (CurrNode)request.getSession().getAttribute("CurrNode");
		
		response.setContentType(CONTENT);
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
		String xmlResp = "";
		String lang = "";
		
		// Retrive UserSession and check
        UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
        if (ServletHelper.validateSessionWithoutSet(userSession))
        {
			// Retrive parameters from querystring
	        BufferedReader br = request.getReader();
	        String data = "";
	        String strtmp = "";
	        String folder = userSession.getProperty("folder");
	        
	        while((strtmp = br.readLine())!=null)
	        {
	        	data+=strtmp;
	        }
	        
	        String s = userSession.getProperty("iddev");
	        
	        try
	        {
				lang = userSession.getLanguage();
			}
	        catch (Exception e)
	        {
				lang = "EN_en";
			}
			
	        if("dtlview".equalsIgnoreCase(folder) && s!=null && !s.equals("") && userSession.isDeviceVisible(Integer.parseInt(s)))
	        {
	        	xmlResp = refreshCustomDtlView(Integer.parseInt(s), data, lang);
	        }
	        else
	        {
	        	xmlResp = refreshCustom(data, lang, cn);
	        }
        }
        
        // Response
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(xmlResp);
        response.getWriter().flush();
        cu = null;
	}
	
	// x compatibilita' con versione precedente:
	private String refreshCustom(int id, String cstmvar)
	{
		return refreshCustomDtlView(id, cstmvar, "EN_en");
	}
	
	private String refreshCustomDtlView(int id, String cstmvar, String language)
	{
		if(cu == null) return "null";
		if(id == 0) return "---";
		StringBuffer toReturn = new StringBuffer();
		toReturn.append("<response>");
		StringTokenizer st = new StringTokenizer(cstmvar,",");
		
		while(st.hasMoreTokens())
		{
			String v = st.nextToken();
			toReturn.append("<var id=\""+v+"\">");
			if(v.matches("[\\d]*"))
			{
				try
				{
					Variable vv = ControllerMgr.getInstance().getFromField(Integer.parseInt(v));
					String f = vv.getFormattedValue();
					if(f.equals(Float.NaN))
					{
						toReturn.append("<![CDATA[***]]>");
					}
					else
					{
						toReturn.append(f);
					}
				} catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			else if(v.startsWith("img"))
			{
				try
				{
					v = v.split("_")[0];
					Variable vv = ControllerMgr.getInstance().getFromField(Integer.parseInt(v.substring(3)));
					Float f = vv.getCurrentValue();
					String code = vv.getInfo().getCode();
					CurrVar cv = cu.getVariable(code);
					toReturn.append("<![CDATA["+Sfera.assint(f, cv.getAssint(), cv.getAssintDefault())+"]]>");
				} 
				catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			else if(v.startsWith("ftt"))
			{
				try
				{
					v = v.split("_")[0];
					Variable vv = ControllerMgr.getInstance().getFromField(Integer.parseInt(v.substring(3)));
					Float f = vv.getCurrentValue();
					String code = vv.getInfo().getCode();
					CurrVar cv = cu.getVariable(code);
					toReturn.append("<![CDATA["+cv.getFormattedValue()+"]]>");
				} 
				catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			else if(v.startsWith("devsts"))
			{
				try
				{
					toReturn.append("<![CDATA["+Sfera.assint(UtilDevice.getLedColor(new Integer(id)),cu.getAssint(), cu.getAssintDefault())+"]]>");
				} catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			toReturn.append("</var>");
		}
		toReturn.append("<alarms>");
		if (cu!=null)
		{
			cu.loadAlarms();
			StringBuffer sb = new StringBuffer("");
			for(int w=0; w<cu.getAlarmNumber(); w++)
			{
				sb.append("<alr>");
				sb.append("<datetime><![CDATA["+(cu.getAlarmAt(w).getDate()!=null?cu.getAlarmAt(w).getDate():"")+"]]></datetime>");
				sb.append("<description><![CDATA["+(cu.getAlarmAt(w).getDesc()!=null?cu.getAlarmAt(w).getDesc():"")+"]]></description>");
				sb.append("<priority><![CDATA["+(cu.getAlarmAt(w).getPriority().trim()!=null?cu.getAlarmAt(w).getPriority().trim():"")+"]]></priority>");
				sb.append("<ackuser><![CDATA["+(cu.getAlarmAt(w).getAckuser()!=null?cu.getAlarmAt(w).getAckuser():"")+"]]></ackuser>");
				sb.append("<acktime><![CDATA["+(cu.getAlarmAt(w).getAcktime()!=null?cu.getAlarmAt(w).getAcktime():"")+"]]></acktime>");
				sb.append("<resetuser><![CDATA["+(cu.getAlarmAt(w).getResetuser()!=null?cu.getAlarmAt(w).getResetuser():"")+"]]></resetuser>");
				sb.append("<resettime><![CDATA["+(cu.getAlarmAt(w).getResettime()!=null?cu.getAlarmAt(w).getResettime():"")+"]]></resettime>");
				sb.append("<prio><![CDATA["+(cu.getAlarmAt(w).getPrio())+"]]></prio>");
				sb.append("</alr>");
			}
			toReturn.append(sb);
		}
		toReturn.append("</alarms>");
		toReturn.append("</response>");
		return toReturn.toString();
	}
	
	private String refreshCustom(String cstmvar, String language,CurrNode cn)
	{
		StringBuffer toReturn = new StringBuffer();
		toReturn.append("<response>");
		StringTokenizer st = new StringTokenizer(cstmvar,",");
		
		while(st.hasMoreTokens())
		{
			String v = st.nextToken();
			toReturn.append("<var id=\""+v+"\">");
			if(v.matches("[\\d]*"))
			{
				try
				{
					Variable vv = ControllerMgr.getInstance().getFromField(Integer.parseInt(v));
					String f = vv.getFormattedValue();
					if(f.equals(Float.NaN))
					{
						toReturn.append("<![CDATA[***]]>");
					}
					else
					{
						toReturn.append(f);
					}
				} catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			else if(v.startsWith("img"))
			{
				try
				{
					int iddev = Integer.parseInt(v.split("_")[1]);
					v = v.split("_")[0];
					Variable vv = ControllerMgr.getInstance().getFromField(Integer.parseInt(v.substring(3)));
					Float f = vv.getCurrentValue();
					//String code = VarphyBeanList.retrieveVarById(1, Integer.parseInt(v.substring(3)), language).getCodeVar();
					String code = vv.getInfo().getCode();
					cu = cn.getDeviceFromCurrNodeById(iddev);
					CurrVar cv = cu.getVariable(code);
					toReturn.append("<![CDATA["+Sfera.assint(f, cv.getAssint(), cv.getAssintDefault())+"]]>");
				} catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			else if(v.startsWith("ftt"))
			{
				try
				{
					v = v.split("_")[0];
					Variable vv = ControllerMgr.getInstance().getFromField(Integer.parseInt(v.substring(3)));
					Float f = vv.getCurrentValue();
					//String code = VarphyBeanList.retrieveVarById(1, Integer.parseInt(v.substring(3)), language).getCodeVar();
					String code = vv.getInfo().getCode();
					CurrVar cv = cu.getVariable(code);
					toReturn.append("<![CDATA["+cv.getFormattedValue()+"]]>");
				} 
				catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			else if(v.startsWith("devsts"))
			{
				try
				{
					if (v.contains("_"))
					{
						String s_id = v.split("_")[1];
						cu = cn.getDeviceFromCurrNodeById(Integer.parseInt(s_id));
					}
					
					toReturn.append("<![CDATA["+Sfera.assint(UtilDevice.getLedColor(new Integer(cu.getId())),cu.getAssint(), cu.getAssintDefault())+"]]>");
				} catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
					toReturn.append("***");
				}
			}
			toReturn.append("</var>");
		}
		toReturn.append("<alarms>");
		
		StringBuffer sb = new StringBuffer("");
		// ITERARE MAPPA dei device in CurrNode
		Map<Integer,CurrUnit> map = cn.getDeviceMap();
		Set<Integer> set = map.keySet();
		for (Integer key : set)
		{
			cu = map.get(key) ;
			cu.loadAlarms();
			
			for(int w=0; w<cu.getAlarmNumber(); w++)
			{
				sb.append("<alr>");
				sb.append("<datetime><![CDATA["+(cu.getAlarmAt(w).getDate()!=null?cu.getAlarmAt(w).getDate():"")+"]]></datetime>");
				sb.append("<description><![CDATA["+(cu.getAlarmAt(w).getDesc()!=null?cu.getAlarmAt(w).getDesc():"")+"]]></description>");
				sb.append("<priority><![CDATA["+(cu.getAlarmAt(w).getPriority().trim()!=null?cu.getAlarmAt(w).getPriority().trim():"")+"]]></priority>");
				sb.append("<ackuser><![CDATA["+(cu.getAlarmAt(w).getAckuser()!=null?cu.getAlarmAt(w).getAckuser():"")+"]]></ackuser>");
				sb.append("<acktime><![CDATA["+(cu.getAlarmAt(w).getAcktime()!=null?cu.getAlarmAt(w).getAcktime():"")+"]]></acktime>");
				sb.append("<resetuser><![CDATA["+(cu.getAlarmAt(w).getResetuser()!=null?cu.getAlarmAt(w).getResetuser():"")+"]]></resetuser>");
				sb.append("<resettime><![CDATA["+(cu.getAlarmAt(w).getResettime()!=null?cu.getAlarmAt(w).getResettime():"")+"]]></resettime>");
				sb.append("<prio><![CDATA["+(cu.getAlarmAt(w).getPrio())+"]]></prio>");
				sb.append("</alr>");
			}
		}
		toReturn.append(sb);
		toReturn.append("</alarms>");
		toReturn.append("</response>");
		return toReturn.toString();
	}
}
