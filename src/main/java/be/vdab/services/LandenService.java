package be.vdab.services;

import java.util.List;

import be.vdab.dao.LandenDAO;
import be.vdab.entities.Land;

public class LandenService {

	private final LandenDAO landenDAO = new LandenDAO();
	
	public List<Land> findAlleLanden(){
		return landenDAO.findAlleLanden();
	}
	
	public Land findLandById(long id){
		return landenDAO.read(id);
	}
	
}
