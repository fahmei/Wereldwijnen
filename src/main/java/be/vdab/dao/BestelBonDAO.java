package be.vdab.dao;

import be.vdab.entities.BestelBon;

public class BestelBonDAO extends AbstractDAO{
	
	public void maakNieuweBestelBon(BestelBon bestelBon){
			getEntityManager()
			.persist(bestelBon);
		
	}
}
