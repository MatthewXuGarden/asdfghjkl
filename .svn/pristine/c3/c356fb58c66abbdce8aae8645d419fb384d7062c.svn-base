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

import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;


public class SvgmapsSessionControlFilter implements Filter
{
	Pattern appPattern = null;
	public void destroy() {
	  
	 }
	
	 public void doFilter(ServletRequest request, ServletResponse response,
			   FilterChain filter) throws IOException, ServletException {
		 
		 response.setContentType("text/html; charset=UTF-8");
	     response.setCharacterEncoding("UTF-8");
	     request.setCharacterEncoding("UTF-8");
	     
	     UserSession userSess = ServletHelper.retrieveSession(((HttpServletRequest)request).getRequestedSessionId(),(HttpServletRequest)request);
	     
	     ServletHelper.validateSession(userSess); // this method is used to update the session timestamp 
	     										  // when the user navigates the SVG pages (otherwise the session expires)
	     										  // the return value is not used
	     filter.doFilter(request, response);
	 }

	 public void init(FilterConfig arg0) throws ServletException {
	 }
}
