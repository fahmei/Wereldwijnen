package be.vdab.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.vdab.services.LandenService;
import be.vdab.services.WijnenService;
import be.vdab.valueobjects.Mandje;

@WebServlet("/toevoegen.htm")
public class ToevoegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String VIEW = "/WEB-INF/JSP/toevoegen.jsp";
	private static final String REDIRECT_NAAR_INDEX_URL = "%s/index.htm";

	private final transient LandenService landenService = new LandenService();
	private final transient WijnenService wijnenService = new WijnenService();

	private final static Logger logger = Logger.getLogger(LandenService.class.getName());

	// DO GET EN POST
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		hoofdMenu(request);

		if (!tonenVanDeWijnEnToevoegen(request, response)) {
			terugNaarIndexPagina(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("toevoegen") != null) {
			if (request.getParameter("aantalFlessen") != null) {

				int aantalFlessen = aantalFlessenOphalenUitRequest(request);
				long wijnId = wijnIdOphalenUitRequest(request, response);

				nieuweLijnToevoegen(request, response, aantalFlessen, wijnId);
				terugNaarIndexPagina(request, response);

			} else {
				foutBijParameterAantalFlessen(request);
				request.getRequestDispatcher(VIEW).forward(request, response);
			}
		}

	}

	// PRIVATE

	private boolean tonenVanDeWijnEnToevoegen(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long wijnId = setWijn(request, response);

		if (wijnId > 0) {
			setAantalFlessenAlInMandje(wijnId, request);
			request.getRequestDispatcher(VIEW).forward(request, response);
			return true;
		} else {
			return false;
		}
	}

	private void nieuweLijnToevoegen(HttpServletRequest request, HttpServletResponse response, int aantalFlessen,
			long wijnId) throws ServletException, IOException {

		HttpSession session = request.getSession();

		if (wijnId > 0) {
			Mandje mandje = mandjeOpmakenOfOphalen(session);

			if (aantalFlessen == 0) {
				if (lijnVerwijderenUitMandjeAlsAanwezigIs(wijnId, mandje)) {
					mandjeUitSessionVerwijderenAlsLeegIs(mandje, session);
				} else {
					mandjeInSessionVervangenOfAanmaken(mandje, session);
				}
			} else if (aantalFlessen > 0) {
				mandje.addBestelLijn(wijnId, aantalFlessen);
				mandjeInSessionVervangenOfAanmaken(mandje, session);
			}
		}
	}

	private boolean lijnVerwijderenUitMandjeAlsAanwezigIs(long wijnId, Mandje mandje) {
		if (mandje.getAantalFlessenAlInMandje(wijnId) == 0) {
			mandje.removeLijn(wijnId);
			return true;
		} else {
			return false;
		}
	}

	private void mandjeUitSessionVerwijderenAlsLeegIs(Mandje mandje, HttpSession session) {
		if (mandje.isLeeg()) {
			session.removeAttribute("mandje");
		}
	}

	private void mandjeInSessionVervangenOfAanmaken(Mandje mandje, HttpSession session) {
		session.setAttribute("mandje", mandje);
	}

	private long wijnIdOphalenUitRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			return Long.parseLong(request.getParameter("wijnId"));
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "parameters ongeldig", ex);
			terugNaarIndexPagina(request, response);
			return 0L;
		}
	}

	private int aantalFlessenOphalenUitRequest(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("aantalFlessen"));
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "parameters ongeldig", ex);
			foutBijParameterAantalFlessen(request);
			return 0;
		}

	}

	private void setAantalFlessenAlInMandje(long wijnId, HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("mandje") != null) {
			Mandje mandje = (Mandje) session.getAttribute("mandje");

			int aantalFlessenInMandje = mandje.getAantalFlessenAlInMandje(wijnId);

			if (aantalFlessenInMandje > 0) {
				request.setAttribute("alInMandje", aantalFlessenInMandje);
			}
		}
	}

	private long setWijn(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			long wijnId = Long.parseLong(request.getParameter("wijnId"));
			if (wijnId > 0) {
				request.setAttribute("wijn", wijnenService.findWijnById(wijnId));
				return wijnId;
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "parameters ongeldig", ex);
			terugNaarIndexPagina(request, response);
			return 0L;
		}
	}

	private void mandjeBeschikbaarMakenAlsErAlEenBestaat(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		request.setAttribute("mandjeAanwezig", (session != null && session.getAttribute("mandje") != null));

	}

	private void terugNaarIndexPagina(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(String.format(REDIRECT_NAAR_INDEX_URL, request.getContextPath()));
	}

	private void hoofdMenu(HttpServletRequest request) {
		request.setAttribute("landen", landenService.findAlleLanden());
		mandjeBeschikbaarMakenAlsErAlEenBestaat(request);
	}

	private void foutBijParameterAantalFlessen(HttpServletRequest request) {
		request.setAttribute("fout", "gelieve een positieve getal in te voeren");
	}

	private Mandje mandjeOpmakenOfOphalen(HttpSession session) {
		Mandje mandje;
		if (session.getAttribute("mandje") != null) {
			mandje = (Mandje) session.getAttribute("mandje");
		} else {
			mandje = new Mandje();
		}
		return mandje;
	}
}
