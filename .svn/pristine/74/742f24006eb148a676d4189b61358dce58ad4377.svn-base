package supervisor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class SRVLDocument extends HttpServlet 
{
	private static final long serialVersionUID = 895154246838474277L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
		String path = request.getParameter("path");
		if(path!=null)
		{
			UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
			if( !ServletHelper.validateSession(sessionUser) )
			{
				 response.setContentType("text/html;charset=utf-8");
				 ((HttpServletResponse) response).sendRedirect("../");
				 return;
			}
			if(".pdf".equalsIgnoreCase(path.substring(path.length()-4)))
			{
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename="+new File(path).getName());
			}
			else if(".csv".equalsIgnoreCase(path.substring(path.length()-4)))
			{
				response.setContentType("text/csv");
				response.setHeader("Content-Disposition", "inline; filename="+new File(path).getName());
			}
			else if(".html".equalsIgnoreCase(path.substring(path.length()-5)) ||
					".htm".equalsIgnoreCase(path.substring(path.length()-4)))
			{
				response.setContentType("text/html");
				response.setHeader("Content-Disposition", "inline; filename="+new File(path).getName());
			}
			else if(".xls".equalsIgnoreCase(path.substring(path.length()-4)))
			{
				response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "inline; filename="+new File(path).getName());
			}
			else if(".zip".equalsIgnoreCase(path.substring(path.length()-4)))
			{
				response.setContentType("application/zip");
				response.setHeader("Content-Disposition", "inline; filename="+new File(path).getName());
			}
			//Kevin, download
			else
			{
			//	response.setHeader("Content-Disposition:attachment;", "inline; filename="+new File(path).getName());
				//Glisten
				String fileName = new File(path).getName();
				fileName = java.net.URLDecoder.decode(fileName,"UTF-8");
				response.setHeader("Content-disposition","attachment; filename=\""+fileName+"\"");
			}
		}
		String pathFile = java.net.URLDecoder.decode(request.getParameter("path"),"UTF-8");
		
		BufferedInputStream bif = null;
		
		OutputStream out = response.getOutputStream();
		
		try
		{
			securityCheckPoint(pathFile);
			bif = new BufferedInputStream(new FileInputStream(pathFile));
			
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			response.setContentType("text/html");
			response.setHeader("Content-disposition", "");
			OutputStreamWriter osw = new OutputStreamWriter(out);
			osw.write("<html><body><h1>File not found</h1></body></html>");
			osw.close();
		}
		
		int ch = 0;
		if (bif != null)
		{
			while ((ch = bif.read()) > -1)
	        {
	            out.write(ch);
	        }
			bif.close();
		}
		
		out.flush();
		out.close();
		
		// cancella file dopo download begin
		try{
			if(new Boolean(request.getParameter("delete"))){
				new File(path).delete();
			}
		}catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		// cancella file dopo download end
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		doGet(request,response);
	}
	
	private void securityCheckPoint(String fileName) throws FileNotFoundException
	{
		String pvproPath = BaseConfig.getCarelPath().toLowerCase() + "pvpro\\"; 
		String checkName = fileName.toLowerCase();
		if( checkName.startsWith("c:") && !checkName.startsWith(pvproPath) )
			throw(new FileNotFoundException(fileName));

		boolean bHideD = true;
		try {
			// if HW version is 2.0.5 or higher then it has SSD. Don't hide 'D' from file dialog
			bHideD = Integer.parseInt(BaseConfig.getReleaseHW().replace(".","")) < 205;
		} catch(NumberFormatException e) {}
		if( bHideD && checkName.startsWith("d:") )
			throw(new FileNotFoundException(fileName));
	}
}
