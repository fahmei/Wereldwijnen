package be.vdab.valueobjects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.vdab.entities.BestelBon;
import be.vdab.entities.Wijn;
import be.vdab.enums.Bestelwijze;
import be.vdab.services.WijnenService;

public class Mandje implements Serializable{
	private static final long serialVersionUID = 1L;

	Map<Long, Integer> mandje;
	
	
//CONSTRUCTORS
	
	public Mandje(){
		mandje = new HashMap<>();
	}
	
	public Mandje(long wijnId, int aantalFlessen) {
		mandje = new HashMap<>();
		mandje.put(wijnId, aantalFlessen);
	}

	
//GETTERS
	public int getAantalFlessenAlInMandje(long wijnId) {
		return mandje.containsKey(wijnId) ? mandje.get(wijnId) : 0;
	}
	
	public boolean isLeeg() {
		return mandje.isEmpty();
	}

//EIGEN
	public void addBestelLijn(long wijnId, int aantalFlessen) {
		mandje.put(wijnId, aantalFlessen);
	}
	
	public void removeLijn(long wijnId) {
		mandje.remove(wijnId);
	}

	
//CONVERTIES
	public Set<BestelbonLijn> vanMandjeNaarBestelBonLijnen() {
		Set<BestelbonLijn> bestelbonlijnen = new HashSet<>();
		WijnenService wijnenService = new WijnenService();
		
		
		List<Wijn> wijnen = wijnenService.findByMultipleIds(mandje.keySet());
		
		for (Wijn wijn:wijnen) {
			bestelbonlijnen.add(new BestelbonLijn(wijn, mandje.get(wijn.getId())));
		}
		
		return bestelbonlijnen;
	}
	
	public Set<BestelbonLijn> vanMandjeNaarBestelBonLijnenMetLock() {
		Set<BestelbonLijn> bestelbonlijnen = new HashSet<>();
		WijnenService wijnenService = new WijnenService();
		
		
		List<Wijn> wijnen = wijnenService.findByMultipleIdsWithLock(mandje.keySet());
		
		for (Wijn wijn:wijnen) {
			bestelbonlijnen.add(new BestelbonLijn(wijn, mandje.get(wijn.getId())));
		}
		
		return bestelbonlijnen;
	}
	
	public BestelBon vanMandjeNaarBestelBonZonderGegevens() {

		return new BestelBon(vanMandjeNaarBestelBonLijnen());
	}

	public BestelBon vanMandjeNaarBestelBonMetGegevens(String naam, String straat, String huisNr, String postCode,
			String gemeente, Bestelwijze bestelwijze) {

		return new BestelBon(naam, new Adres(straat, huisNr, postCode, gemeente), bestelwijze);
	}
}
