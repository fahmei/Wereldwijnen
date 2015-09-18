package be.vdab.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "landen")
public class Land {

	@Id
	@GeneratedValue
	long id;
	
	private String naam;
	
	
	
}
