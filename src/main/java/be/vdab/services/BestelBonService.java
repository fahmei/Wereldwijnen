package be.vdab.services;

import be.vdab.dao.BestelBonDAO;
import be.vdab.entities.BestelBon;
import be.vdab.valueobjects.Mandje;

public class BestelBonService {
	
	private final BestelBonDAO bestelBonDAO = new BestelBonDAO();
	
	public void maakNieuweBestelBonVanMandje(BestelBon bestelBon, Mandje mandje){
		bestelBonDAO.beginTransaction();
	
		bestelBon.setBestelbonLijnen(mandje.vanMandjeNaarBestelBonLijnenMetLock());
		
		if(bestelBon.isKlaar()){
			bestelBonDAO.maakNieuweBestelBon(bestelBon);
		}
		
		bestelBonDAO.commit();
	}

}
