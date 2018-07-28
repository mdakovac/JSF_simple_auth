package authentication;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(false);

		String loginURI = request.getContextPath() + "/faces/index.xhtml";
		String homeURI = request.getContextPath() + "/faces/home.xhtml";
		
		System.out.println(request.getRequestURI());

		boolean loggedIn = session != null && session.getAttribute("user") != null;
		boolean loginRequest = request.getRequestURI().equals(loginURI);
		boolean resourceRequest = request.getRequestURI()
				.startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER);
		
		if (loggedIn || loginRequest || resourceRequest) {
			// ako je ulogiran i pokusa se vratit na login/register page,
			// preusmjeri na home
			if(loggedIn && loginRequest) {
				response.sendRedirect(homeURI);
			}
			
			// inace nastavi
			chain.doFilter(request, response);
		} else {
			// ako nije ulogiran, redirect na login page
			response.sendRedirect(loginURI);
		}
		
	}
}
