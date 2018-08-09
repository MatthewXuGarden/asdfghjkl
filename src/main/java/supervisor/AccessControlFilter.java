package supervisor;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;


public class AccessControlFilter implements Filter
{
	Pattern appPattern = null;
	Pattern managerPattern = null;
	public void destroy() {
	  
	 }
	 public void doFilter(ServletRequest request, ServletResponse response,
	   FilterChain filter) throws IOException, ServletException {
		 HttpServletRequest httpRequest = (HttpServletRequest)request;
		 String url = httpRequest.getRequestURI(); 
		 //not allow to browse url directly: /app/section/SubTabx.jsp, 
		 if(appPattern.matcher(url).find())
		 {
			 response.setContentType("text/html;charset=utf-8");
			 ((HttpServletResponse) response).sendRedirect("../../");
			 return;
		 }
		 else if(managerPattern.matcher(url).find() && !(url.indexOf("Poller")>0||url.indexOf("Receiver")>0))
		 {
			 UserSession sessionUser = ServletHelper.retrieveSession(httpRequest.getRequestedSessionId(),httpRequest);
			 if( !ServletHelper.validateSession(sessionUser) )
			 {
				 response.setContentType("text/html;charset=utf-8");
				 ((HttpServletResponse) response).sendRedirect("../../");
				 return;
			 }
		 }
		 filter.doFilter(request, response);
	 }
	 public void init(FilterConfig arg0) throws ServletException {
		 appPattern = Pattern.compile("/app/\\w+/SubTab\\d+\\.jsp");
		 managerPattern = Pattern.compile("/arch/manager/.*");
	 }
}
