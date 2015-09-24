package be.vdab.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vdab.services.LandenService;

@WebServlet("/bevestigen.htm")
public class BevestigenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final transient LandenService landenService = new LandenService();
	
	private static final String VIEW = "/WEB-INF/JSP/bevestigen.jsp";
	private static final String REDIRECT_NAAR_INDEX_URL = "%s/index.htm";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("bestelbonId") == null) {

			terugNaarIndexPagina(request, response);

		} else {
			request.setAttribute("landen", landenService.findAlleLanden());
			
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
	
	}

	private void terugNaarIndexPagina(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(String.format(REDIRECT_NAAR_INDEX_URL, request.getContextPath()));
	}
	
}
