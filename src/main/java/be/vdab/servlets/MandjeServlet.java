package be.vdab.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.vdab.entities.BestelBon;
import be.vdab.enums.Bestelwijze;
import be.vdab.services.BestelBonService;
import be.vdab.services.LandenService;
import be.vdab.valueobjects.Mandje;

@WebServlet("/mandje.htm")
public class MandjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final transient LandenService landenService = new LandenService();
	private final transient BestelBonService bestelBonService = new BestelBonService();
	
	private final static Logger logger = Logger.getLogger(LandenService.class.getName());

	private static final String VIEW = "/WEB-INF/JSP/mandje.jsp";
	private static final String REDIRECT_NAAR_INDEX_URL = "%s/index.htm";
	private static final String REDIRECT_NAAR_BEVESTIGEN_URL = "%s/bevestigen.htm?bestelbonId=";

	private static final String NAAM = "naam";
	private static final String STRAAT = "straat";
	private static final String HUISNUMMER = "huisnummer";
	private static final String POSTCODE = "postcode";
	private static final String GEMEENTE = "gemeente";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (!mandjeAanwezig(request, session)) {

			terugNaarIndexPagina(request, response);

		} else {
			hoofdMenu(request);
			
			inhoudMandjeInRequestVerwerken(request, session);

			request.getRequestDispatcher(VIEW).forward(request, response);

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("bevestigingsKnop") != null) {
			HttpSession session = request.getSession(false);

			if (!mandjeAanwezig(request, session)) {
				terugNaarIndexPagina(request, response);
			} else {
				nieuweBestelBonAanmakenEnFoutenVerwerken(request, session, response);
			}
		}
	}

	private void nieuweBestelBonAanmakenEnFoutenVerwerken(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, String> fouten = new HashMap<>();

		String naam = veldControlerenEnTeruggeven(NAAM, request, fouten);
		String straat = veldControlerenEnTeruggeven(STRAAT, request, fouten);
		String huisNr = veldControlerenEnTeruggeven(HUISNUMMER, request, fouten);
		String postCode = veldControlerenEnTeruggeven(POSTCODE, request, fouten);
		String gemeente = veldControlerenEnTeruggeven(GEMEENTE, request, fouten);

		Bestelwijze bestelwijze = radioButtonsControlerenEnTeruggeven("bestelwijze", request, fouten);

		if (!fouten.isEmpty()) {
			hoofdMenu(request);

			inhoudMandjeInRequestVerwerken(request, session);

			request.setAttribute("fouten", fouten);

			request.getRequestDispatcher(VIEW).forward(request, response);
		} else {

			Mandje mandje = (Mandje) session.getAttribute("mandje");
			
			BestelBon bestelBon = mandje.vanMandjeNaarBestelBonMetGegevens(naam, straat, huisNr, postCode, gemeente, bestelwijze);

			bestelBonService.maakNieuweBestelBonVanMandje(bestelBon, mandje);
			
			session.removeAttribute("mandje");

			response.sendRedirect(String.format(REDIRECT_NAAR_BEVESTIGEN_URL + bestelBon.getId(), request.getContextPath()));
		}
	}

	private Bestelwijze radioButtonsControlerenEnTeruggeven(String bestelwijze, HttpServletRequest request,
			Map<String, String> fouten) {
		
			try{
				Bestelwijze gekozenBestelWijze = Bestelwijze.valueOf(request.getParameter(bestelwijze));
				return gekozenBestelWijze;
			}catch(IllegalArgumentException ex){
				logger.log(Level.SEVERE, "parameters ongeldig", ex);
				fouten.put(bestelwijze, "Gelieve een geldige bestelwijze aan te duiden");
				return null;
			}catch (NullPointerException ex) {
				fouten.put(bestelwijze, "Gelieve de bestelwijze aan te duiden");
				return null;
			}
			
			
			
	}

	private boolean mandjeAanwezig(HttpServletRequest request, HttpSession session) {
		return session != null && session.getAttribute("mandje") != null;
	}

	private String veldControlerenEnTeruggeven(String veld, HttpServletRequest request, Map<String, String> fouten) {

		String resultaat = request.getParameter(veld);
		if (resultaat == null || resultaat.isEmpty()) {
			fouten.put(veld, "Gelieve een " + veld + " in te vullen");
		}

		return resultaat;
	}

	private void inhoudMandjeInRequestVerwerken(HttpServletRequest request, HttpSession session) {
		Mandje mandje = (Mandje) session.getAttribute("mandje");
		BestelBon bestelBon = mandje.vanMandjeNaarBestelBonZonderGegevens();

		request.setAttribute("bestelBon", bestelBon);
	}

	private void terugNaarIndexPagina(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(String.format(REDIRECT_NAAR_INDEX_URL, request.getContextPath()));
	}

	private void hoofdMenu(HttpServletRequest request) {
		request.setAttribute("landen", landenService.findAlleLanden());
	}
}
