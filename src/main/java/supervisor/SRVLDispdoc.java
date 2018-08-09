package supervisor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dispatcher.main.DispDocLinker;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class SRVLDispdoc extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String id = request.getParameter("id");
        String ty = request.getParameter("type");
        
        if(ty != null)
        {
            if(ty.equalsIgnoreCase("E"))
                renderEmail(request,response,id);
            else if(ty.equalsIgnoreCase("S"))
                renderSms(request,response,id);
            else if(ty.equalsIgnoreCase("F"))
                renderFax(request,response,id);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doGet(request,response);
    }
    
    private void renderEmail(HttpServletRequest request, HttpServletResponse response,String id)
    {
        UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
        RequestDispatcher dispatcher = null;
        String[] ret = DispDocLinker.retriveDoc(userSession.getIdSite(),Integer.parseInt(id));
        
        if(ret != null)
            dispatcher = getServletContext().getRequestDispatcher("/servlet/document?path="+ret[1]+"");
        
        try
        {
            dispatcher.forward(request,response);
        } 
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(Master.class);
            logger.error(e);
        }
    }
    
    private void renderSms(HttpServletRequest request, HttpServletResponse response,String id)
    {
        UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
        String[] ret = DispDocLinker.retriveDoc(userSession.getIdSite(),Integer.parseInt(id));
        
        try
        {
        	response.setContentType("text/html; charset=UTF-8");
        	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
            out.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>");
            out.write("<b>"+ret[1]+"</b>");
            out.write("</body></html>");
            out.flush();
        } 
        catch (IOException e)
        {
            Logger logger = LoggerMgr.getLogger(Master.class);
            logger.error(e);
        }
    }
    
    private void renderFax(HttpServletRequest request, HttpServletResponse response,String id)
    {
        UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
        String[] ret = DispDocLinker.retriveDoc(userSession.getIdSite(),Integer.parseInt(id));
              
        try
        {
            // Carico il file in memoria
            StringBuffer sb = new StringBuffer();
            FileInputStream fis = new FileInputStream(ret[1]);
       		int ch = -1;
            while((ch = fis.read()) != -1)
                sb.append((char)ch);
            
            PrintWriter out = response.getWriter();
            out.write("<html><head>\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\""+userSession.getEncoding()+"\"\n");
            out.write("</head><body><table border='0' cellpadding='1' cellspacing='1'>");

            String header = parseHeaderInfo(sb.toString());
            
            out.write("<tr><td>"+header+"</td></tr>");
            
            boolean loop = false;
            int chS = 0;
            int par = 0;
            
            do
            {
                chS = sb.toString().indexOf("START",par);
                if(chS != -1)
                {
                    loop = true;
                    par = sb.toString().indexOf("\\par",chS);
                    par = sb.toString().indexOf("\\par",par+1);
                    String s = cleanRTFString(sb.toString().substring(chS,par));
                    out.write("<tr><td>"+s+"</td></tr>");
                }
                else
                    loop = false;
            }
            while(loop);
            
            chS = 0;
            par = 0;
            
            do
            {
                chS = sb.toString().indexOf("END",par);
                if(chS != -1)
                {
                    loop = true;
                    par = sb.toString().indexOf("\\par",chS);
                    par = sb.toString().indexOf("\\par",par+1);
                    String s = cleanRTFString(sb.toString().substring(chS,par));
                    out.write("<tr><td>"+s+"</td></tr>");
                }
                else
                    loop = false;
            }
            while(loop);
            
            out.write("</table></body></html>");
            out.flush();
            out.close();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    }
    
    //parsing of header fax information from rtf
    private String parseHeaderInfo(String in)
    {
    	String head1 = "From / Da / Von / De / Desde:";
    	String head2 = "From No. / Da Num. / Von Nr. / De No. / De Nu.:";
    	String head3 = "To No. / A Num. / Zu Nr. / \\u192? No. / A Nu.:";
    	String head4 = "Date / Data / Datum / Date / Fecha:";
    	
    	String from = "";
    	String fromNo = "";
    	String to = "";
    	String date = "";   
    	
    	int sindex = in.indexOf(head1);
    	int eindex = in.indexOf("\\par",sindex);
    	if(sindex != -1 && eindex != -1)
    	{
    		sindex = in.indexOf(" ",sindex + head1.length());
    		from = in.substring(sindex, eindex);
    		from = from.replace("\\tab","");
    	}
    	
    	sindex = in.indexOf(head2);
    	eindex = in.indexOf("\\par",sindex);
    	if(sindex != -1 && eindex != -1)
    	{
    		sindex = in.indexOf(" ",sindex + head2.length());
    		fromNo = in.substring(sindex, eindex);
    		fromNo = fromNo.replace("\\tab","");
    	}
    	
    	sindex = in.indexOf(head3);
    	eindex = in.indexOf("\\par",sindex);
    	if(sindex != -1 && eindex != -1)
    	{
    		sindex = in.indexOf(" ",sindex + head3.length());
    		to = in.substring(sindex, eindex);
    		to = to.replace("\\tab","");
    	}
    	
    	sindex = in.indexOf(head4);
    	eindex = in.indexOf("\\par",sindex);
    	if(sindex != -1 && eindex != -1)
    	{
    		sindex = in.indexOf(" ",sindex +  head4.length());
    		date = in.substring(sindex, eindex);
    		date = date.replace("\\tab","");
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(head1 + " <b>" + from + "</b><br>");
    	sb.append(head2 + " <b>" + fromNo + "</b><br>");
    	sb.append(head3 + " <b>" + to + "</b><br>");
    	sb.append(head4 + " <b>" + date + "</b><br><br>");
    	
    	return convertUnicodeEscape(sb.toString());
    }
    
    // conversion of unicode escape RTF tags in unicode HTML tags   
    private String convertUnicodeEscape(String in)
    {
    	int sindex = 0;
    	int eindex = 0;
    	int code = 0;
    	StringBuffer sb = new StringBuffer();
    	    	
    	while(true)
    	{		
    		eindex = in.indexOf("\\u",sindex);
    		
    		if (eindex == -1)
    		{
    			eindex = in.length();	
    			sb.append(in.substring(sindex, eindex));
    			break;
    		}
    		sb.append(in.substring(sindex, eindex));
    		
    		sindex = in.indexOf("?", eindex);
    		
    		code = Integer.parseInt(in.substring(eindex + 2, sindex));
    		
    		// RTF unicode tags are coded using 16 bit numbers
    		// necessary to convert negative representation
    		if(in.charAt(eindex + 2) == '-')
    		{
    			code = code + 65536;    			
    		}
    		sb.append("&#" + code + "&nbsp;");
    		
    		if(sindex != -1)
    		{
    			sindex = sindex +1;
    		}
    	}
    	return sb.toString();
    }
    
    private String cleanRTFString(String in)
    {
        int ch = -1;
        ch = in.indexOf("\\tab");
        if(ch != -1)
            in = in.substring(0,ch) + in.substring(ch+"\\tab".length());
        
        ch = -1;
        ch = in.indexOf("\\tab");
        if(ch != -1)
            in = in.substring(0,ch) + in.substring(ch+"\\tab".length());
        
        ch = -1;
        ch = in.indexOf("\\par");
        if(ch != -1)
            in = in.substring(0,ch) + in.substring(ch+"\\par".length());
        
        int sindex = 0;
    	int eindex = 0;
    	
    	StringBuffer sb = new StringBuffer();
    	   	
    	while(sindex != -1)
    	{		
    		eindex = in.indexOf("\\f",sindex);
    		if (eindex == -1)
    		{
    			eindex = in.length();	
    		}
    		sb.append(in.substring(sindex, eindex)+"&nbsp;&nbsp;");
    		
			sindex = in.indexOf(" ", eindex);
    	}
        
        return "<b>"+convertUnicodeEscape(sb.toString())+"</b>";
    }
}
